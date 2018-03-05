package com.finance.p2p.biz.business.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.p2p.biz.sms.service.SMSService;
import com.finance.p2p.biz.sys.utils.Const.RateKey;
import com.finance.p2p.biz.sys.utils.Const.RecordKey;
import com.finance.p2p.biz.sys.utils.Const.SMSKey;
import com.finance.p2p.biz.sys.utils.Const.StateKey;
import com.finance.p2p.biz.sys.utils.Const.TimeKey;
import com.finance.p2p.biz.sys.utils.Const.TrackKey;
import com.finance.p2p.biz.sys.utils.Const.TypeKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.sys.utils.Pubfun;
import com.finance.p2p.dao.BusinessRecordMapper;
import com.finance.p2p.dao.BuyMapper;
import com.finance.p2p.dao.IncomeBuyMapper;
import com.finance.p2p.dao.IncomeTrackMapper;
import com.finance.p2p.dao.PNoticeMapper;
import com.finance.p2p.dao.SellMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.dao.WalletMapper;
import com.finance.p2p.entity.BusinessRecord;
import com.finance.p2p.entity.Buy;
import com.finance.p2p.entity.IncomeTrack;
import com.finance.p2p.entity.PNotice;
import com.finance.p2p.entity.Sell;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.Wallet;
import com.framework.utils.DateUtil;
import com.framework.utils.IdWorker;

@Service
public class TaskService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BuyMapper buyMapper;

	@Autowired
	private SellMapper sellMapper;

	@Autowired
	private BusinessRecordMapper businessRecordMapper;

	@Autowired
	private PNoticeMapper pNoticeMapper;

	@Autowired
	private IncomeBuyMapper incomeBuyMapper;

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private SellService sellService;

	@Autowired
	private IncomeTrackMapper incomeTrackMapper;

	@Autowired
	private SMSService smsService;

	/**
	 * 让人无计划识别用户48小时未支付的，预付款12小时未支付的，其他付款24小时未支付的将用户进行冻结
	 */
	@Transactional
	public void userStateLockTask() {

		Date date = new Date();

		// 这个是48小时的,针对新用户或者已经提款完毕的
		userMapper.updateUserStateBy48Hour(date);

		// 把刚才这批用户查询出来发消息
		List<Long> userIdList = userMapper.selectLockUserList(date);
		for (Long userId : userIdList) {
			PNotice n = getNotice(userId, TimeKey.HOUR_48, date);
			pNoticeMapper.insert(n);
		}

		// 金额款24小时未支付的要锁定
		List<BusinessRecord> recordList = businessRecordMapper.selectLockRecord(date, TypeKey.TYPE_2,
				RecordKey.STATE_0);
		for (BusinessRecord br : recordList) {

			// 首先发出消息通知
			PNotice n = getNotice(br.getBuyAccountId(), TimeKey.HOUR_12, date);
			pNoticeMapper.insert(n);

			// 冻结用户
			User user = new User();
			user.setUserId(br.getBuyAccountId());
			user.setLockTime(date);
			user.setModifyTime(date);
			user.setState(USERKey.LOCK);
			userMapper.updateByPrimaryKeySelective(user);

			// 修改卖家金额
			Sell sell = new Sell();
			sell.setId(br.getSellId());
			sell.setMatchMoney(br.getMoney());
			sell.setState(StateKey.STATE_0);
			sell.setModifyTime(date);
			sellMapper.updateById(sell);

			// 删除此条记录
			businessRecordMapper.deleteByPrimaryKey(br.getId());

			// 冻结买家记录
			Buy buy = new Buy();
			buy.setId(br.getBuyId());
			buy.setMatchMoney(br.getMoney());
			buy.setState(StateKey.STATE_9);
			buy.setModifyTime(date);
			buyMapper.updateById(buy);
		}
	}

	/**
	 * 用户100%剩余款进行处理
	 */
	@Transactional
	public void userSurplusTask() {

		// 找出这部分用户其中1个
		Date date = new Date();
		Buy buy = buyMapper.selectSurplus(date, TimeKey.DAY_7);

		if (buy == null) {
			return;
		}
		BigDecimal subsistMoney = buy.getMatchMoney();
		if (subsistMoney.compareTo(new BigDecimal(0)) < 1) {
			// 增加一个控制防止错误。
			return;
		}
		userSubsistDeal(null, subsistMoney, subsistMoney, date, buy, TypeKey.TYPE_2);
	}

	/**
	 * 对内置用户进行全额匹配
	 */
	@Transactional
	public void userInnerMatchTask() {

		// 找出这部分用户其中1个
		Date date = new Date();
		Buy buy = buyMapper.selectInnerMatch();

		if (buy == null) {
			return;
		}

		BigDecimal subsistMoney = buy.getMatchMoney();

		if (subsistMoney.compareTo(new BigDecimal(0)) < 1) {
			// 增加一个控制防止错误。
			return;
		}

		userSubsistDeal(null, subsistMoney, subsistMoney, date, buy, TypeKey.TYPE_2);
	}

	/**
	 * 用户利息计算器
	 */
	@Transactional
	public void userInterestTask() {

		System.out.println("进入利息计算器。。。。");
		Date date = new Date(); //0:0:30
		/**
		 * 用户利息计算器,计算前一天的利息
		 */
		// 这个地方原来是 Date nowDate = Pubfun.getTodayTime(date);
		// 采用date的方式进行数据库查询，阿里云自己安装的是没问题的，但是RDS始终查询不出结果，所以这里改进，具体原因自己购买一个RDS试试
		String nowDate = DateUtil.formatDate(Pubfun.getTodayTime(date));

		incomeBuyMapper.calcUserIncome(nowDate, RateKey.DAY_RATE, RateKey.DAY_MAX);

		// 查询出所有钱包金额大于0的用户，来计算用户前天的利息
		List<Long> userList = walletMapper.selectWalletBig();
		for (Long userId : userList) {
			User user = userMapper.selectByPrimaryKey(userId);
			if (user.getState().equals(USERKey.LOCK)) {
				// 如果当前利息的用户被锁定了，那么就不计算利息了
				continue;
			}

			// 用户利息
			Integer interest = incomeBuyMapper.calcUserInterest(nowDate, userId);
			if (interest == null || interest == 0) {
				continue;
			}

			// 用户在buy里面超过10天可以卖出的金额
			Integer canTraded = incomeBuyMapper.calcUserTraded(nowDate, userId, TimeKey.DAY_5);

			BigDecimal interestMoney = new BigDecimal(interest);

			// 增加收入轨迹
			IncomeTrack track = new IncomeTrack();
			track.setId(IdWorker.getId());
			track.setUserId(userId);
			track.setMoney(interestMoney);
			track.setType(TrackKey.TYPE_3);
			track.setRemark("您获得购买利息" + interest + "元。 " + DateUtil.formatDate(date));
			track.setIncomeTime(date);
			track.setCreateTime(date);
			track.setModifyTime(date);
			incomeTrackMapper.insert(track);

			/**
			 * 用户钱包整理
			 */
			BigDecimal zero = new BigDecimal(0);
			Wallet wallet = walletMapper.selectByPrimaryKey(userId);
			BigDecimal origalInterest = wallet.getInterest().add(interestMoney); // 利息总金额
			BigDecimal origalIntegrity = wallet.getIntegrity(); // 原来奖金金额

			wallet.setBuyMoney(zero);
			wallet.setSellMoney(zero);
			wallet.setIntegrity(zero);
			wallet.setInterest(interestMoney);
			wallet.setCantraded(zero);
			wallet.setFreeze(zero);
			wallet.setWallet(interestMoney);

			if (canTraded != null && canTraded > 0) {
				wallet.setCantraded(new BigDecimal(canTraded).add(origalInterest).add(origalIntegrity));
			} else {
				wallet.setCantraded(origalInterest.add(origalIntegrity));
			}
			wallet.setModifyTime(date);
			walletMapper.updateWallet(wallet);
			System.out.println("更新用户钱包。。。。" + userId);
		}
		System.out.println("完成利息计算器。。。。");
	}

	/**
	 * 确认用户付款
	 */
	public void confirmPayTask() {
		Date date = new Date();
		List<BusinessRecord> recordList = businessRecordMapper.selectConfirmRecord(date, RecordKey.STATE_1);
		for (BusinessRecord br : recordList) {
			sellService.confirm(null, br.getId());
		}
	}

	/**
	 * 用户级别任务处理
	 */
	@Transactional
	public void userLevelTask() {
		getUserList(0L, new Date());
	}

	/**************** 以下为私有方法 ************************/

	private void userSubsistDeal(Long sellId, BigDecimal subsistMoney, BigDecimal lastMoney, Date date, Buy buy,
			Integer type) {

		BigDecimal zero = new BigDecimal(0);
		// 找到提现的用户前5个
		List<Sell> sellList = sellMapper.selectSellTop(sellId, buy.getUserId());

		if (CollectionUtils.isEmpty(sellList)) {
			// 这里貌似有个bug，就是一个人的金额没有匹配完，再次循环的时候到这里，需要判断subsistMoney与lastMoney的关系
			if (subsistMoney.compareTo(lastMoney) == 0) {
				// 如果进入这里还是相等的，那么就不要处理了直接return
			} else {
				// 对于这种比较尴尬，没有人员，但是金额大于0
				Buy buyNew = new Buy();
				buyNew.setId(buy.getId());
				buyNew.setMatchMoney(buy.getMatchMoney().subtract(subsistMoney).add(lastMoney).setScale(2,
						BigDecimal.ROUND_HALF_UP));
				if (buyNew.getMatchMoney().compareTo(zero) == 0) {
					buyNew.setState(StateKey.STATE_4);
				}
				buyNew.setModifyTime(date);
				buyMapper.updateByPrimaryKeySelective(buyNew);
			}
			return;
		}
		// 进行匹配，前不够的就返回
		/*
		 * -1 小于 0 等于 1 大于
		 */
		for (Sell sell : sellList) {
			sellId = sell.getId();
			if (lastMoney.compareTo(sell.getMatchMoney()) < 1 && lastMoney.compareTo(zero) > 0) {
				// 首付款比别人提现金额要小
				BusinessRecord record = getBusinessRecord(buy.getUserId(), buy.getId(), sell.getUserId(), sell.getId(),
						type, lastMoney, date);
				businessRecordMapper.insert(record);

				// 提现人员金额减少，等下一个人来匹配
				Sell sellNew = new Sell();
				sellNew.setId(sell.getId());
				sellNew.setMatchMoney(sell.getMatchMoney().subtract(lastMoney));
				if (sellNew.getMatchMoney().compareTo(zero) == 0) {
					sellNew.setState(StateKey.STATE_4);
				}
				sellNew.setModifyTime(date);
				sellMapper.updateByPrimaryKeySelective(sellNew);

				lastMoney = zero;

			} else if (lastMoney.compareTo(sell.getMatchMoney()) == 1 && lastMoney.compareTo(zero) > 0) {
				// 首付款比别人提现金额要小
				BusinessRecord record = getBusinessRecord(buy.getUserId(), buy.getId(), sell.getUserId(), sell.getId(),
						type, sell.getMatchMoney(), date);
				businessRecordMapper.insert(record);

				// 提现人员金额减少，等下一个人来匹配
				Sell sellNew = new Sell();
				sellNew.setId(sell.getId());
				sellNew.setMatchMoney(zero);
				sellNew.setState(StateKey.STATE_4);
				sellNew.setModifyTime(date);
				sellMapper.updateByPrimaryKeySelective(sellNew);

				lastMoney = lastMoney.subtract(sell.getMatchMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
			}

			if (lastMoney.compareTo(zero) == 0) {
				break;
			}
		}

		if (lastMoney.compareTo(zero) == 1) {
			userSubsistDeal(sellId, subsistMoney, lastMoney, date, buy, type);
		} else {
			// 如果已经减少为0了，就直接跳出
			// 修改买入人员信息
			Buy buyNew = new Buy();
			buyNew.setId(buy.getId());
			buyNew.setMatchMoney(buy.getMatchMoney().subtract(subsistMoney));
			if (buyNew.getMatchMoney().compareTo(zero) == 0) {
				buyNew.setState(StateKey.STATE_4);
			}
			buyNew.setModifyTime(date);
			buyMapper.updateByPrimaryKeySelective(buyNew);
		}
	}

	/**
	 * 查询特定的用户
	 * 
	 * @param userId
	 * @param timeType
	 * @param date
	 * @return
	 */
	private PNotice getNotice(Long userId, Integer timeType, Date date) {
		PNotice notice = new PNotice();
		notice.setId(IdWorker.getId());
		notice.setUserId(userId);
		notice.setTitle("由于您没有按照规则买入付款，您的账户被锁定");
		if (timeType.equals(TimeKey.HOUR_48)) {
			notice.setContent("您已经被系统锁定。原因：超过48小时未提供帮助");
		} else if (timeType.equals(TimeKey.HOUR_12)) {
			notice.setContent("您已经被系统锁定。原因：余额款超过12小时未提供帮助");
		} else if (timeType.equals(TimeKey.HOUR_0)) {
			notice.setContent("您已经被系统管理员锁定。原因：请联系管理员");
		}
		notice.setCreateTime(date);
		notice.setModifyTime(date);
		return notice;
	}

	private BusinessRecord getBusinessRecord(Long buyUserId, Long buyId, Long sellUserId, Long sellId, Integer type,
			BigDecimal money, Date date) {
		BusinessRecord record = new BusinessRecord();
		record.setId(IdWorker.getId());
		record.setBuyId(buyId);
		record.setSellId(sellId);
		record.setType(type);
		record.setBuyAccountId(buyUserId);
		record.setSellAccountId(sellUserId);
		record.setMoney(money);
		record.setState(RecordKey.STATE_0);
		record.setLockTime(Pubfun.calLockTime(date, type));
		record.setCreateTime(date);
		record.setModifyTime(date);

		// 算了 我们就在这里发短信了
		User user = userMapper.selectByPrimaryKey(buyUserId);
		smsService.send(user.getPhone(), SMSKey.TEMP_1004);
		return record;
	}

	private void getUserList(Long userId, Date date) {
		// 这个计划我们怎么处理，需要对整个表进行扫描查询
		List<User> userList = userMapper.getAllUserList(userId);
		if (CollectionUtils.isEmpty(userList)) {
			return;
		}
		Integer directNum = 0; // 直接人员
		Integer subordinateNum = 0;// 团队人员
		for (User u : userList) {
			userId = u.getUserId();
			directNum = userMapper.selectDirectNum(u.getPhone());
			if (directNum.compareTo(USERKey.LEVEL_3_U) >= 0) {
				// 那么我们就要看团队大小了
				subordinateNum = userMapper.selectSubordinateNum(String.valueOf(u.getUserId()), USERKey.UNLOCK);
				if (subordinateNum.compareTo(USERKey.LEVEL_3_T) >= 0) {
					// 那么用户可以晋升为老大了
					User user = new User();
					user.setUserId(u.getUserId());
					user.setUserLevel(USERKey.LEVEL_3);
					user.setModifyTime(date);
					userMapper.updateByPrimaryKeySelective(user);
				}
			} else if (directNum.compareTo(USERKey.LEVEL_2_U) >= 0) {
				User user = new User();
				user.setUserId(u.getUserId());
				user.setUserLevel(USERKey.LEVEL_2);
				user.setModifyTime(date);
				userMapper.updateByPrimaryKeySelective(user);
			} else if (directNum.compareTo(USERKey.LEVEL_1_U) >= 0) {
				User user = new User();
				user.setUserId(u.getUserId());
				user.setUserLevel(USERKey.LEVEL_1);
				user.setModifyTime(date);
				userMapper.updateByPrimaryKeySelective(user);
			}
		}
		getUserList(userId, date);
	}
}