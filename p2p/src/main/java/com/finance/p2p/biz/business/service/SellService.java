package com.finance.p2p.biz.business.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.utils.Const;
import com.finance.p2p.biz.sys.utils.Const.RateKey;
import com.finance.p2p.biz.sys.utils.Const.RecordKey;
import com.finance.p2p.biz.sys.utils.Const.StateKey;
import com.finance.p2p.biz.sys.utils.Const.TrackKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.sys.utils.Pubfun;
import com.finance.p2p.dao.AccountMapper;
import com.finance.p2p.dao.BusinessRecordMapper;
import com.finance.p2p.dao.BuyMapper;
import com.finance.p2p.dao.IncomeBuyMapper;
import com.finance.p2p.dao.IncomeTrackMapper;
import com.finance.p2p.dao.PNoticeMapper;
import com.finance.p2p.dao.SellMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.dao.WalletMapper;
import com.finance.p2p.entity.Account;
import com.finance.p2p.entity.BusinessRecord;
import com.finance.p2p.entity.Buy;
import com.finance.p2p.entity.IncomeBuy;
import com.finance.p2p.entity.IncomeTrack;
import com.finance.p2p.entity.PNotice;
import com.finance.p2p.entity.Sell;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.Wallet;
import com.framework.SysConst.SysKey;
import com.framework.exception.BusinessException;
import com.framework.utils.DateUtil;
import com.framework.utils.IdWorker;

@Service
public class SellService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private BusinessRecordMapper businessRecordMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private IncomeBuyMapper incomeBuyMapper;

	@Autowired
	private BuyMapper buyMapper;

	@Autowired
	private SellMapper sellMapper;

	@Autowired
	private IncomeTrackMapper incomeTrackMapper;

	@Autowired
	private PNoticeMapper pNoticeMapper;

	/**
	 * 查询用户钱包
	 * 
	 * @param userId
	 * @return
	 */
	public Wallet selectUserWallet(Long userId) {
		Wallet wallet = walletMapper.selectByPrimaryKey(userId);
		return wallet;
	}

	/**
	 * 用户卖出提交
	 * 
	 * @param userId
	 * @param money
	 * @param password
	 * @return
	 */
	@Transactional
	public BaseData sellSubmit(Long userId, Integer money, String password) {
		User user = userMapper.selectByPrimaryKey(userId);
		if (user.getState().equals(USERKey.LOCK)) {
			throw new BusinessException("您当前已经被系统锁定,不能购买");
		}
		password = Pubfun.encryptMD5(user.getPhone(), password);
		if (!StringUtils.equals(password, user.getPayPassword())) {
			throw new BusinessException("支付密码错误,不能卖出");
		}

		// 这里还需要判断是否存在为打款的帮助信息
		Buy buy = buyMapper.queryUserLastBuy(userId);
		if (buy != null && !buy.getState().equals(StateKey.STATE_5)) {
			throw new BusinessException("您当前存在提供帮助的金额未完成.");
		}

		BaseData data = new BaseData();

		Date date = new Date();
		BigDecimal sellMoney = new BigDecimal(money);
		// 计算当天是否已经卖出了一笔，如果有，那么不允许卖出了
		Integer count = sellMapper.countSellByCondtion(userId, date);
		if (count > 0) {
			data.setError("您当天已经提现一笔，不能继续提现(S1010)");
		} else {
			// 开始查询用户收入表数据，开始删除部分信息-1 小于 0 等于 1 大于
			List<Long> ibIdList = new ArrayList<Long>();

			List<IncomeBuy> buyList = incomeBuyMapper.selectUserBuyList(userId);
			BigDecimal buyMoney = new BigDecimal(0);
			BigDecimal zero = new BigDecimal(0);

			// 如果购买列表没有数据了，那么就是纯利息
			if (CollectionUtils.isEmpty(buyList)) {

			} else {
				for (IncomeBuy incomeBuy : buyList) {
					buyMoney = buyMoney.add(incomeBuy.getMoney());

					if (buyMoney.compareTo(sellMoney) > 0) {
						incomeBuy.setMoney(incomeBuy.getMoney().subtract(sellMoney));
						incomeBuyMapper.updateByPrimaryKeySelective(incomeBuy);
						break;
					} else {
						ibIdList.add(incomeBuy.getId());
					}
				}
			}

			if (!CollectionUtils.isEmpty(ibIdList)) {
				// 讲这些记录删除掉
				incomeBuyMapper.deleteUserBuyList(ibIdList);
			}

			Sell sell = new Sell();
			sell.setId(IdWorker.getId());
			sell.setUserId(userId);
			sell.setMoney(sellMoney);
			sell.setMatchMoney(sellMoney);
			sell.setIncomeMoney(zero);
			sell.setState(StateKey.STATE_0);
			sell.setCreateTime(date);
			sell.setModifyTime(date);
			sellMapper.insert(sell);

			// 对用户钱包的数据进行判断
			Wallet walletSell = walletMapper.selectByPrimaryKey(userId);
			if (walletSell == null) {
				throw new BusinessException("数据异常，请重试尝试(S1004)");
			}
			if (sellMoney.compareTo(walletSell.getCantraded()) > 0
					|| walletSell.getCantraded().compareTo(walletSell.getWallet()) > 0) {
				throw new BusinessException("数据异常，请重试尝试(S1008)");
			}
			// 修改卖出用户的钱包记录
			Wallet wallet = new Wallet();
			wallet.setUserId(userId);
			wallet.setBuyMoney(zero);
			wallet.setSellMoney(sellMoney);// 更新卖出金额
			wallet.setIntegrity(zero);
			wallet.setInterest(zero);
			wallet.setCantraded(sellMoney.negate()); // 更新可以交易的金额
			wallet.setFreeze(sellMoney); // 更新冻结金额
			wallet.setWallet(sellMoney.negate());// 减少钱包金额
			wallet.setModifyTime(date);
			walletMapper.updateUserWallet(wallet);

			PNotice pNotice = new PNotice();
			pNotice.setId(sell.getId());
			pNotice.setUserId(userId);
			pNotice.setTitle("卖出操作成功，请注意匹配消息");
			pNotice.setContent("您于" + DateUtil.formatDate(date) + "成功卖出" + sellMoney + ",请关注匹配消息，及时确认收款.");
			pNotice.setCreateTime(date);
			pNotice.setModifyTime(date);
			pNoticeMapper.insert(pNotice);
		}
		return data;
	}

	/**
	 * 查询用户帮助的列表
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	public Object list(Long id, User user) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("limit", SysKey.LIMIT);
		condition.put("id", id);
		condition.put("userId", user.getUserId());
		// 这里可以与buy进行合并
		List<BusinessRecord> list = businessRecordMapper.getSellList(condition);
		// 开始遍历列表信息
		if (!CollectionUtils.isEmpty(list)) {
			for (BusinessRecord bean : list) {
				if (StringUtils.isNoneEmpty(bean.getPicUrl())) {
					bean.setPicUrl(Const.UploadKey.SERVER_HTTP_PATH + bean.getPicUrl());
				}
				switch (bean.getState()) {
				case 0:
					bean.setStateName("未打款");
					break;
				case 1:
					bean.setStateName("已打款等待确认");
					break;
				case 2:
					bean.setStateName("已确认收款");
					break;
				}
			}
		}
		return new BaseData(list);
	}

	/**
	 * 查询用户的账户信息
	 * 
	 * @param accountId
	 * @return
	 */
	public Account selectSellAccount(Long accountId) {
		return accountMapper.selectByPrimaryKey(accountId);
	}

	/**
	 * 这用户确认收款，那么这里我们需要处理较多
	 * 
	 * @param id
	 */
	@Transactional
	public void confirm(User user, Long id) {
		BusinessRecord br = businessRecordMapper.selectByPrimaryKey(id);

		if (br == null) {
			throw new BusinessException("数据异常，请重试尝试(S1001)");
		}
		if (!br.getState().equals(RecordKey.STATE_1)) {
			throw new BusinessException("状态异常，请重试尝试(S1002)");
		}

		BigDecimal zero = new BigDecimal(0);
		
		Date date = new Date();
		br.setState(RecordKey.STATE_2);
		br.setModifyTime(date);
		businessRecordMapper.updateByPrimaryKeySelective(br);

		// 开始转储数据，对于买家而言，开始计算利息
		IncomeBuy incomeBuy = new IncomeBuy();
		incomeBuy.setId(IdWorker.getId());
		incomeBuy.setUserId(br.getBuyAccountId());
		incomeBuy.setMoney(br.getMoney());
		incomeBuy.setInterest(zero);
		incomeBuy.setCreateTime(date);
		incomeBuy.setModifyTime(date);
		// 这个startTime，估计要怎么修改下
		incomeBuy.setStartTime(DateUtil.dateBySecond(date));
		incomeBuyMapper.insert(incomeBuy);

		// 开始更新买入的钱包
		Wallet walletBuy = walletMapper.selectByPrimaryKey(br.getBuyAccountId());
		if (walletBuy == null) {
			throw new BusinessException("数据异常，请重试尝试(S1003)");
		}
		walletBuy = new Wallet();
		walletBuy.setUserId(br.getBuyAccountId());
		walletBuy.setBuyMoney(zero);
		walletBuy.setSellMoney(zero);
		walletBuy.setIntegrity(zero);
		walletBuy.setInterest(zero);
		walletBuy.setCantraded(zero);
		walletBuy.setFreeze(zero);
		walletBuy.setWallet(br.getMoney());// 更新钱包金额
		walletBuy.setModifyTime(date);
		walletMapper.updateUserWallet(walletBuy);

		// 开始更新卖出人的钱包
		Wallet walletSell = walletMapper.selectByPrimaryKey(br.getSellAccountId());
		if (walletSell == null) {
			throw new BusinessException("数据异常，请重试尝试(S1005)");
		}
		walletSell = new Wallet();
		walletSell.setUserId(br.getSellAccountId());
		walletSell.setBuyMoney(zero);
		walletSell.setSellMoney(zero);
		walletSell.setIntegrity(zero);
		walletSell.setInterest(zero);
		walletSell.setCantraded(zero);
		walletSell.setFreeze(br.getMoney().negate());// 更新钱包冻结金额
		walletSell.setWallet(zero);
		walletSell.setModifyTime(date);
		walletMapper.updateUserWallet(walletSell);

		// 查询出 buy的记录
		Buy buy = buyMapper.selectByPrimaryKey(br.getBuyId());
		buy.setOutMoney(buy.getOutMoney().add(br.getMoney())); // 更新实际打款金额
		buy.setModifyTime(date);
		if (buy.getMoney().compareTo(buy.getOutMoney()) == 0) {
			buy.setState(StateKey.STATE_5);
		}
		buyMapper.updateByPrimaryKeySelective(buy);

		// 查询出卖的记录
		Sell sell = sellMapper.selectByPrimaryKey(br.getSellId());
		sell.setIncomeMoney(sell.getIncomeMoney().add(br.getMoney()));
		sell.setModifyTime(date);
		if (sell.getMoney().compareTo(sell.getIncomeMoney()) == 0) {
			sell.setState(StateKey.STATE_5);

			// 我觉得这里有必要校验用户是否已经卖完了，如果是的，那么我们就更 i 想你用户为锁定状态，条件就是没有买入信息，也没有计算利息信息
			Integer buyNum = buyMapper.countUserUnComplate(br.getSellAccountId(), StateKey.STATE_5);
			Integer sellNum = sellMapper.countUserUnComplate(br.getSellAccountId(), StateKey.STATE_5);
			Integer ibNum = incomeBuyMapper.countUserUnComplate(br.getSellAccountId());
			if (buyNum.compareTo(0) == 0 && sellNum.compareTo(0) == 0 && ibNum.compareTo(0) == 0) {
				// 三个全部满足的话，我们就设置用户冻结时间
				User u = userMapper.selectByPrimaryKey(br.getSellAccountId());
				u.setModifyTime(date);
				u.setPayTime(DateUtil.dateAddDay(date, 2)); // 设置任务计划校验的时间为2天后
				u.setLockTime(null);
				userMapper.updateByPrimaryKey(u);
			}
		}
		sellMapper.updateByPrimaryKeySelective(sell);

		// 这里我们需要更新用户轨迹
		IncomeTrack buyTrack = new IncomeTrack();
		buyTrack.setId(IdWorker.getId());
		buyTrack.setUserId(br.getBuyAccountId());
		buyTrack.setMoney(br.getMoney());
		buyTrack.setType(TrackKey.TYPE_1);
		buyTrack.setRemark("对方已确认收款，您已成功买入" + br.getMoney() + "元。 " + DateUtil.formatDate(date));
		buyTrack.setIncomeTime(date);
		buyTrack.setCreateTime(date);
		buyTrack.setModifyTime(date);
		incomeTrackMapper.insert(buyTrack);

		IncomeTrack sellTrack = new IncomeTrack();
		sellTrack.setId(IdWorker.getId());
		sellTrack.setUserId(br.getSellAccountId());
		sellTrack.setMoney(br.getMoney().negate()); // 金额取反
		sellTrack.setType(TrackKey.TYPE_2);
		sellTrack.setRemark("您已成功卖出" + br.getMoney() + "元。 " + DateUtil.formatDate(date));
		sellTrack.setIncomeTime(date);
		sellTrack.setCreateTime(date);
		sellTrack.setModifyTime(date);
		incomeTrackMapper.insert(sellTrack);

		// 开始计算上级 或者上上级的奖金
		calcBonus(br.getMoney(), br.getBuyAccountId());
	}

	private void calcBonus(BigDecimal money, Long userId) {

		// 查找当前用户的上级以及上上级，没付一笔款的确认，那么我们就需要处理金额
		User user = userMapper.selectByPrimaryKey(userId);
		User parent = userMapper.selectByPhone(user.getInvitePhone());
		if (parent != null) {
			if (parent.getState().equals(USERKey.UNLOCK)) {
				// 只有当上级没有锁定
				if (parent.getUserLevel().equals(USERKey.LEVEL_3)) {
					updateWallet(parent.getUserId(), new Date(), TrackKey.TYPE_4, money.multiply(RateKey.RATE1_T));
				} else if (parent.getUserLevel().equals(USERKey.LEVEL_2)
						|| parent.getUserLevel().equals(USERKey.LEVEL_1)) {
					updateWallet(parent.getUserId(), new Date(), TrackKey.TYPE_4, money.multiply(RateKey.RATE1));
				}
				// 处理完了上级，我们继续看上上级，暂时就这样吧
				user = userMapper.selectByPhone(parent.getInvitePhone());
				if (user != null) {
					if (user.getState().equals(USERKey.UNLOCK)) {
						// 只有当上级没有锁定
						if (user.getUserLevel().equals(USERKey.LEVEL_3)) {
							updateWallet(user.getUserId(), new Date(), TrackKey.TYPE_5,
									money.multiply(RateKey.RATE2_T));
						} else if (user.getUserLevel().equals(USERKey.LEVEL_2)) {
							updateWallet(user.getUserId(), new Date(), TrackKey.TYPE_5, money.multiply(RateKey.RATE2));
						}
					}
				}
			}
		}
	}

	private void updateWallet(Long userId, Date date, Integer type, BigDecimal money) {

		IncomeTrack buyTrack = new IncomeTrack();
		buyTrack.setId(IdWorker.getId());
		buyTrack.setUserId(userId);
		buyTrack.setMoney(money);
		buyTrack.setType(type);
		if (type.equals(TrackKey.TYPE_4)) {
			buyTrack.setRemark("您获得直接团队奖金" + money + "元。 " + DateUtil.formatDate(date));
		} else if (type.equals(TrackKey.TYPE_5)) {
			buyTrack.setRemark("您获得二级团队奖金" + money + "元。 " + DateUtil.formatDate(date));
		}
		buyTrack.setIncomeTime(date);
		buyTrack.setCreateTime(date);
		buyTrack.setModifyTime(date);
		incomeTrackMapper.insert(buyTrack);

		BigDecimal zero = new BigDecimal(0);
		// 修改用户钱包
		Wallet wallet = new Wallet();
		wallet.setUserId(userId);
		wallet.setBuyMoney(zero);
		wallet.setSellMoney(zero);
		wallet.setIntegrity(money);
		wallet.setInterest(zero);
		wallet.setCantraded(money);
		wallet.setFreeze(zero);
		wallet.setWallet(money);
		wallet.setModifyTime(date);
		walletMapper.updateUserWallet(wallet);
	}
}
