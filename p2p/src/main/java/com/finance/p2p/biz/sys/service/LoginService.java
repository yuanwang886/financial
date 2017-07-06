package com.finance.p2p.biz.sys.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.p2p.biz.common.service.CacheService;
import com.finance.p2p.biz.sms.service.SMSService;
import com.finance.p2p.biz.sys.utils.Const.SMSKey;
import com.finance.p2p.biz.sys.utils.Const.TimeKey;
import com.finance.p2p.biz.sys.utils.Const.TrackKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.sys.utils.Global;
import com.finance.p2p.biz.sys.utils.Pubfun;
import com.finance.p2p.dao.AccountMapper;
import com.finance.p2p.dao.IncomeTrackMapper;
import com.finance.p2p.dao.ReleaseMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.dao.UserTimeMapper;
import com.finance.p2p.dao.WalletMapper;
import com.finance.p2p.entity.IncomeTrack;
import com.finance.p2p.entity.Release;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.UserTime;
import com.finance.p2p.entity.Wallet;
import com.framework.SysConst.YesOrNO;
import com.framework.utils.DateUtil;
import com.framework.utils.IdWorker;
import com.framework.utils.UUIDGenerator;

@Service
public class LoginService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private UserTimeMapper userTimeMapper;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private SMSService sMSService;

	@Autowired
	private ReleaseMapper releaseMapper;

	@Autowired
	private IncomeTrackMapper incomeTrackMapper;

	/**
	 * 用户注册
	 * 
	 * @param realname
	 * @param phone
	 * @param password
	 * @param invitePhone
	 * @return
	 * @throws Exception
	 */
	@Transactional
	public User register(String realname, String phone, String password, String invitePhone, String code)
			throws Exception {
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password) || StringUtils.isEmpty(invitePhone)) {
			throw new Exception("请输入手机号,密码以及邀请人(A113)");
		}

		// 首先校验手机合法性
		if (phone.length() != 11) {
			throw new Exception("手机号码不合法(A112)");
		}

		if (invitePhone.length() != 11) {
			throw new Exception("推荐人手机号码不合法(A111)");
		}

		if (StringUtils.equals(phone, invitePhone)) {
			throw new Exception("注册账号不能与推荐人手机号码一致(A110)");
		}

		// 校验短信合法性
		sMSService.verify(phone, SMSKey.TEMP_1001, code);

		BigDecimal zero = new BigDecimal(0);

		return registerDeal(phone, password, realname, invitePhone, zero);
	}

	public User registerDeal(String phone, String password, String realname, String invitePhone, BigDecimal money)
			throws Exception {
		User user = userMapper.selectByPhone(phone);
		if (user != null) {
			throw new Exception("该手机号已经注册(A109)");
		}
		user = new User();
		Date nowDate = new Date();
		user.setUserId(IdWorker.getId());
		user.setUserRole(USERKey.UESR);
		user.setUserLevel(USERKey.LEVEL_0);
		user.setSex(USERKey.MAN);
		user.setState(USERKey.UNLOCK);
		user.setRealname(realname);
		user.setPhone(phone);
		user.setPassword(Pubfun.encryptMD5(phone, password));
		user.setMegSwitch(USERKey.OPEN);

		// 如果邀请人不为空，那么我们开始设置层级关系
		if (StringUtils.isEmpty(invitePhone)) {
			// 如果为空，那么就是内置人员
			user.setPayPassword(Pubfun.encryptMD5(phone, password));
			user.setRelation(user.getUserId() + "");
			user.setActivation(YesOrNO.YES);
		} else {
			User parent = userMapper.selectByPhone(invitePhone);
			if (parent == null) {
				// 说明上级电话是错误的
				throw new Exception("邀请人号码错误(A108)");
			} else {
				if (parent.getState().equals(USERKey.LOCK)) {
					throw new Exception("邀请人被锁定，您暂时不能注册(A107)");
				}
				user.setActivation(YesOrNO.NO);

				// 由于值保存2级关系，所以这个地方要特殊处理下 186 186_188 186_188_189 188——189——150
				String[] r = parent.getRelation().split(USERKey.RelationSplit);
				if (r.length == 3) {
					// 那么需要截取
					user.setRelation(r[1] + USERKey.RelationSplit + r[2] + USERKey.RelationSplit + user.getUserId());
				} else {
					user.setRelation(parent.getRelation() + USERKey.RelationSplit + user.getUserId());
				}
			}
		}
		user.setInvitePhone(invitePhone);

		user.setPayTime(DateUtil.dateAddYear(nowDate, TimeKey.USER_PAYTIME)); // 新注册用户保证在没有激活前不要锁定
		user.setLockTime(null);
		user.setCreateTime(nowDate);
		user.setModifyTime(nowDate);
		userMapper.insert(user);

		// 将用户信息放session
		user.setInviteUrl(Global.getDomainPath() + "/register?invitePhone=" + phone);
		user.setToken(createToken(user.getUserId()));

		// 增加用户钱包数据
		Wallet wallet = new Wallet();
		BigDecimal zero = new BigDecimal(0);
		wallet.setUserId(user.getUserId());
		wallet.setBuyMoney(zero);
		wallet.setSellMoney(zero);
		wallet.setInterest(zero);
		wallet.setIntegrity(money); // 这些钱是直接加到奖金上面
		wallet.setCantraded(money);
		wallet.setFreeze(zero);
		wallet.setWallet(money);
		wallet.setCreateTime(nowDate);
		wallet.setModifyTime(nowDate);
		walletMapper.insert(wallet);

		// 用户通知时间信息
		UserTime userTime = new UserTime();
		userTime.setUserId(user.getUserId());
		userTime.setpNoticeTime(nowDate);
		userTime.setsNoticeTime(nowDate);
		userTimeMapper.insert(userTime);

		// 设置用户激活码信息
		Release release = new Release();
		release.setUserId(user.getUserId());
		release.setSumNum(0);
		release.setUsedNum(0);
		release.setSellNum(0);
		release.setCreateTime(nowDate);
		release.setModifyTime(nowDate);
		releaseMapper.insertSelective(release);

		// 差生额外记录
		if (money.compareTo(zero) > 0) {
			IncomeTrack incomeTrack = new IncomeTrack();
			incomeTrack.setId(user.getUserId());
			incomeTrack.setUserId(user.getUserId());
			incomeTrack.setType(TrackKey.TYPE_6);
			incomeTrack.setMoney(money);
			incomeTrack.setRemark("您获得内置金额" + money + "元。 " + DateUtil.formatDate(nowDate));
			incomeTrack.setIncomeTime(nowDate);
			incomeTrack.setCreateTime(nowDate);
			incomeTrack.setModifyTime(nowDate);
			incomeTrackMapper.insert(incomeTrack);
		}

		return user;
	}

	/**
	 * 开始处理用户登录
	 * 
	 * @param phone
	 * @param password
	 */
	public User login(String phone, String password) throws Exception {

		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
			throw new Exception("请输入手机号,密码(A106)");
		}

		// 首先校验手机合法性
		if (phone.length() != 11) {
			throw new Exception("手机号码不合法(A105)");
		}

		User user = userMapper.selectByPhone(phone);
		if (user == null) {
			throw new Exception("该用户未注册，请先注册(A104)");
		}

		// 获取密码，暂时就这么简单的处理下算了
		String pwd = Pubfun.encryptMD5(phone, password);
		if (!StringUtils.equals(pwd, user.getPassword())) {
			// 防止暴力破解，每天只能尝试8次，暂时就不处理了
			throw new Exception("用户名密码错误,请重新输入(A103)");
		}

		// 增加一步查询用户的银行信息
		user.setAccount(accountMapper.selectByPrimaryKey(user.getUserId()));
		user.setInviteUrl(Global.getDomainPath() + "/register?invitePhone=" + phone);
		user.setToken(createToken(user.getUserId()));
		// 如果已经到这里来了，那么表示登陆成功，可以登录了
		return user;
	}

	/**
	 * 根据手机查找用户
	 * 
	 * @param phone
	 * @return
	 */
	public User selectByPhone(String phone) {
		return userMapper.selectByPhone(phone);
	}
	
	/**
	 * 根据主键查询
	 * @param userId
	 * @return
	 */
	public User selectByUserId(Long userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	/**
	 * 用户忘记密码
	 * 
	 * @param phone
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public boolean forgot(String phone, String password, String code) throws Exception {
		if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(password)) {
			throw new Exception("请输入手机号,密码(A102)");
		}
		// 首先校验手机合法性
		if (phone.length() != 11) {
			throw new Exception("手机号码不合法(A101)");
		}

		sMSService.verify(phone, SMSKey.TEMP_1002, code);

		User user = userMapper.selectByPhone(phone);
		if (user == null) {
			throw new Exception("该用户未注册，请先注册(A100)");
		}
		String pwd = Pubfun.encryptMD5(phone, password);
		user.setPassword(pwd);
		user.setModifyTime(new Date());
		userMapper.updateByPrimaryKeySelective(user);
		return true;
	}

	/**
	 * 获取用户token
	 * 
	 * @param userId
	 * @param type
	 * @return
	 */
	private String createToken(Long userId) {
		// 生成token
		String token = UUIDGenerator.getUUID();
		// 我们先删除历史token
		cacheService.delete(USERKey.USER_TOKEN, userId.toString());
		// 将token放入缓存
		cacheService.setValue(USERKey.USER_TOKEN, userId.toString(), token);
		return token;
	}

}
