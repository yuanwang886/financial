package com.finance.p2p.biz.sys.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.bean.InternalBean;
import com.finance.p2p.biz.sys.utils.Const.TrackKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.user.service.InfoService;
import com.finance.p2p.dao.IncomeTrackMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.dao.WalletMapper;
import com.finance.p2p.entity.Account;
import com.finance.p2p.entity.IncomeTrack;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.Wallet;
import com.framework.exception.BusinessException;
import com.framework.utils.DateUtil;
import com.framework.utils.IdWorker;

@Service
public class InternalService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private LoginService loginService;

	@Autowired
	private InfoService infoService;

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private IncomeTrackMapper incomeTrackMapper;

	/**
	 * 管理员根据条件查询内置人员列表
	 * 
	 * @return
	 */
	public List<InternalBean> getInternalUserList(Map<String, Object> condition) {
		return userMapper.getInternalUserList(condition);
	}

	/**
	 * 
	 * @return
	 */
	@Transactional
	public Object submit(Account account, String phone, String password, String money) {

		if (StringUtils.isEmpty(phone)) {
			throw new BusinessException("手机号不能为空(A2002)");
		}
		// 首先校验手机合法性
		if (phone.length() != 11) {
			throw new BusinessException("手机号码不合法(A112)");
		}
		try {
			User user = loginService.registerDeal(phone, password, account.getUserName(), null, new BigDecimal(money));
			return infoService.submit(user, account);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Transactional
	public Object modify(Long userId, String money) {

		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			throw new BusinessException("内置用户信息错误(A2006)");
		}
		if (user.getState().equals(USERKey.LOCK)) {
			throw new BusinessException("内置用户已经被锁定(A2007)");
		}
		Wallet wallet = walletMapper.selectByPrimaryKey(userId);
		if (wallet == null) {
			throw new BusinessException("内置用户信息错误(A2005)");
		}

		BigDecimal m = new BigDecimal(money);
		Date nowDate = new Date();
		wallet.setCantraded(m);
		wallet.setWallet(m);
		wallet.setIntegrity(m); // 这些钱是直接加到奖金上面
		wallet.setModifyTime(nowDate);
		walletMapper.updateUserWallet(wallet);

		IncomeTrack incomeTrack = new IncomeTrack();
		incomeTrack.setId(IdWorker.getId());
		incomeTrack.setUserId(userId);
		incomeTrack.setType(TrackKey.TYPE_6);
		incomeTrack.setMoney(m);
		incomeTrack.setRemark("您获得内置金额" + money + "元。 " + DateUtil.formatDate(nowDate));
		incomeTrack.setIncomeTime(nowDate);
		incomeTrack.setCreateTime(nowDate);
		incomeTrack.setModifyTime(nowDate);
		incomeTrackMapper.insert(incomeTrack);

		return new BaseData();
	}

	public User getInternalUser(Long userId) {
		return userMapper.selectByPrimaryKey(userId);
	}
}
