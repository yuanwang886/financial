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

		// 先我们查询12小时的预付款需要锁定的
		List<BusinessRecord> recordList = businessRecordMapper.selectLockRecord(date, TypeKey.TYPE_1,
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
			Sell sell = sellMapper.selectByPrimaryKey(br.getSellId());
			sell.setMatchMoney(sell.getMatchMoney().add(br.getMoney()));
			sell.setState(StateKey.STATE_0);
			sell.setModifyTime(date);
			sellMapper.updateByPrimaryKeySelective(sell);

			// 删除此条记录
			businessRecordMapper.deleteByPrimaryKey(br.getId());

			// 冻结买家记录
			Buy buy = buyMapper.selectByPrimaryKey(br.getBuyId());
			buy.setMatchMoney(buy.getMatchMoney().add(br.getMoney()));
			buy.setState(StateKey.STATE_9);
			buy.setModifyTime(date);
			buyMapper.updateByPrimaryKeySelective(buy);

			// 既然用户已经冻结，暂时我们继续放在利息计算池中，方便统计
			// incomeBuyMapper.deleteUserAllList(br.getBuyAccountId());
		}

		// 预付款24小时未支付的要锁定
		recordList = businessRecordMapper.selectLockRecord(date, TypeKey.TYPE_2, RecordKey.STATE_0);
		for (BusinessRecord br : recordList) {

			// 首先发出消息通知
			PNotice n = getNotice(br.getBuyAccountId(), TimeKey.HOUR_24, date);
			pNoticeMapper.insert(n);

			// 冻结用户
			User user = new User();
			user.setUserId(br.getBuyAccountId());
			user.setLockTime(date);
			user.setModifyTime(date);
			user.setState(USERKey.LOCK);
			userMapper.updateByPrimaryKeySelective(user);

			// 修改卖家金额
			Sell sell = sellMapper.selectByPrimaryKey(br.getSellId());
			sell.setMatchMoney(sell.getMatchMoney().add(br.getMoney()));
			sell.setState(StateKey.STATE_0);
			sell.setModifyTime(date);
			sellMapper.updateByPrimaryKeySelective(sell);

			// 删除此条记录
			businessRecordMapper.deleteByPrimaryKey(br.getId());

			// 冻结买家记录
			Buy buy = buyMapper.selectByPrimaryKey(br.getBuyId());
			buy.setMatchMoney(buy.getMatchMoney().add(br.getMoney()));
			buy.setState(StateKey.STATE_9);
			buy.setModifyTime(date);
			buyMapper.updateByPrimaryKeySelective(buy);

			// 既然用户已经冻结，暂时我们继续放在利息计算池中，方便统计
			// incomeBuyMapper.deleteUserAllList(br.getBuyAccountId());
		}
	}

	/**
	 * 用户预付款10%进行处理
	 */
	@Transactional
	public void userSubsistTask() {
		// 找出这部分用户其中1个

		Buy buy = buyMapper.selectSubsist();
		if (buy == null) {
			return;
		}
		BigDecimal subsistMoney = buy.getMoney().multiply(RateKey.SUBSIST);

		Date date = new Date();
		userSubsistDeal(null, subsistMoney, subsistMoney, date, buy, TypeKey.TYPE_1);
	}

	/**
	 * 用户90%剩余款进行处理
	 */
	@Transactional
	public void userSurplusTask() {

		// 找出这部分用户其中1个
		Date date = new Date();
		Buy buy = buyMapper.selectSurplus(date, TimeKey.DAY_6);

		if (buy == null) {
			return;
		}
		BigDecimal subsistMoney = buy.getMoney().multiply(RateKey.SURPLUS);
		userSubsistDeal(null, subsistMoney, subsistMoney, date, buy, TypeKey.TYPE_2);
	}

	/**
	 * 用户利息计算器
	 */
	@Transactional
	public void userInterestTask() {

		Date date = new Date();
		/**
		 * 用户利息计算器,计算前一天的利息
		 */
		Date nowDate = Pubfun.getTodayTime(date);

		incomeBuyMapper.calcUserIncome(nowDate, RateKey.DAY_RATE, RateKey.DAY_MAX);

		// 查询出所有钱包金额大于0的用户，来计算用户前天的利息
		List<Long> userList = walletMapper.selectWalletBig();
		for (Long userId : userList) {
			User user = userMapper.selectByPrimaryKey(userId);
			if (user.getState().equals(USERKey.LOCK)) {
				//如果当前利息的用户被锁定了，那么就不计算利息了
				continue;
			}
			
			// 用户利息
			Integer interest = incomeBuyMapper.calcUserInterest(nowDate, userId);
			if (interest == null) {
				continue;
			}

			// 用户在buy里面超过10天可以卖出的金额
			Integer canTraded = incomeBuyMapper.calcUserTraded(nowDate, userId, TimeKey.DAY_12);

			if (interest != null && interest.compareTo(0) > 0) {

				IncomeTrack track = new IncomeTrack();
				track.setId(IdWorker.getId());
				track.setUserId(userId);
				track.setMoney(new BigDecimal(interest));
				track.setType(TrackKey.TYPE_3);
				track.setRemark("您获得购买利息" + interest + "元。 " + DateUtil.formatDate(date));
				track.setIncomeTime(date);
				track.setCreateTime(date);
				track.setModifyTime(date);
				incomeTrackMapper.insert(track);
			}

			/**
			 * 用户钱包整理
			 */
			BigDecimal zero = new BigDecimal(0);
			Wallet wallet = walletMapper.selectByPrimaryKey(userId);
			wallet.setBuyMoney(zero);
			wallet.setSellMoney(zero);
			wallet.setIntegrity(zero);
			wallet.setInterest(zero);
			wallet.setCantraded(zero);
			wallet.setFreeze(zero);
			wallet.setWallet(zero);

			BigDecimal origal = wallet.getInterest();
			if (interest != null && interest.compareTo(0) > 0) {
				wallet.setInterest(new BigDecimal(interest));
				wallet.setWallet(new BigDecimal(interest));

				origal = origal.add(new BigDecimal(interest));
			}
			if (canTraded != null && canTraded.compareTo(0) > 0) {
				wallet.setCantraded(new BigDecimal(canTraded).add(origal).add(wallet.getIntegrity()));
			}
			wallet.setModifyTime(date);
			walletMapper.updateWallet(wallet);
		}
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
		// 找到提现的用户前5个
		List<Sell> sellList = sellMapper.selectSellTop(sellId, buy.getUserId());

		if (CollectionUtils.isEmpty(sellList)) {
			// 对于这种比较尴尬，没有人员，但是金额大于0
			Buy buyNew = new Buy();
			buyNew.setId(buy.getId());
			buyNew.setMatchMoney(buy.getMatchMoney().subtract(subsistMoney).add(lastMoney));
			if (buyNew.getMatchMoney().compareTo(new BigDecimal(0)) == 0) {
				buyNew.setState(StateKey.STATE_4);
			}
			buyNew.setModifyTime(date);
			buyMapper.updateByPrimaryKeySelective(buyNew);

			return;
		}
		// 进行匹配，前不够的就返回
		/*
		 * -1 小于 0 等于 1 大于
		 */
		for (Sell sell : sellList) {
			sellId = sell.getId();
			if (lastMoney.compareTo(sell.getMatchMoney()) < 1) {
				// 首付款比别人提现金额要小
				BusinessRecord record = getBusinessRecord(buy.getUserId(), buy.getId(), sell.getUserId(), sell.getId(),
						type, lastMoney, date);
				businessRecordMapper.insert(record);

				// 提现人员金额减少，等下一个人来匹配
				Sell sellNew = new Sell();
				sellNew.setId(sell.getId());
				sellNew.setMatchMoney(sell.getMatchMoney().subtract(lastMoney));
				if (sellNew.getMatchMoney().compareTo(new BigDecimal(0)) == 0) {
					sellNew.setState(StateKey.STATE_4);
				}
				sellNew.setModifyTime(date);
				sellMapper.updateByPrimaryKeySelective(sellNew);

				lastMoney = new BigDecimal(0);
				break;

			} else if (lastMoney.compareTo(sell.getMatchMoney()) == 1) {
				// 首付款比别人提现金额要小
				BusinessRecord record = getBusinessRecord(buy.getUserId(), buy.getId(), sell.getUserId(), sell.getId(),
						type, sell.getMatchMoney(), date);
				businessRecordMapper.insert(record);

				// 提现人员金额减少，等下一个人来匹配
				Sell sellNew = new Sell();
				sellNew.setId(sell.getId());
				sellNew.setMatchMoney(new BigDecimal(0));
				sellNew.setState(StateKey.STATE_4);
				sellNew.setModifyTime(date);
				sellMapper.updateByPrimaryKeySelective(sellNew);

				lastMoney = lastMoney.subtract(sell.getMatchMoney()).setScale(2, BigDecimal.ROUND_HALF_UP);
			}
		}

		if (lastMoney.compareTo(new BigDecimal(0)) == 1) {
			userSubsistDeal(sellId, subsistMoney, lastMoney, date, buy, type);
		} else {
			// 如果已经减少为0了，就直接跳出
			// 修改买入人员信息
			Buy buyNew = new Buy();
			buyNew.setId(buy.getId());
			buyNew.setMatchMoney(buy.getMatchMoney().subtract(subsistMoney));
			if (buyNew.getMatchMoney().compareTo(new BigDecimal(0)) == 0) {
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
			notice.setContent("您已经被系统锁定。原因：预付款超过12小时未提供帮助");
		} else if (timeType.equals(TimeKey.HOUR_24)) {
			notice.setContent("您已经被系统锁定。原因：余额款超过24小时未提供帮助");
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
