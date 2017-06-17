package com.finance.p2p.biz.business.service;

import java.math.BigDecimal;
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
import com.finance.p2p.biz.sms.bean.SmsBean;
import com.finance.p2p.biz.sms.service.SMSService;
import com.finance.p2p.biz.sys.utils.Const;
import com.finance.p2p.biz.sys.utils.Const.RecordKey;
import com.finance.p2p.biz.sys.utils.Const.SMSKey;
import com.finance.p2p.biz.sys.utils.Const.StateKey;
import com.finance.p2p.biz.sys.utils.Const.TimeKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.sys.utils.Pubfun;
import com.finance.p2p.dao.AccountMapper;
import com.finance.p2p.dao.BusinessRecordMapper;
import com.finance.p2p.dao.BuyMapper;
import com.finance.p2p.dao.PNoticeMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.dao.WalletMapper;
import com.finance.p2p.entity.Account;
import com.finance.p2p.entity.BusinessRecord;
import com.finance.p2p.entity.Buy;
import com.finance.p2p.entity.PNotice;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.Wallet;
import com.framework.SysConst.SysKey;
import com.framework.SysConst.YesOrNO;
import com.framework.exception.BusinessException;
import com.framework.utils.DateUtil;
import com.framework.utils.IdWorker;

@Service
public class BuyService {

	@Autowired
	private BuyMapper buyMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BusinessRecordMapper businessRecordMapper;

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private PNoticeMapper pNoticeMapper;
	
	@Autowired
	private SMSService smsService;

	/**
	 * 用户进行购买
	 * 
	 * @param userId
	 * @param money
	 * @return
	 */
	@Transactional
	public BaseData buySubmit(Long userId, Integer money, String password) {

		User user = userMapper.selectByPrimaryKey(userId);
		if (user.getActivation().equals(YesOrNO.NO)) {
			throw new BusinessException("您当前处于未激活状态，请联系推荐人激活账户");
		}
		if (user.getState().equals(USERKey.LOCK)) {
			throw new BusinessException("您当前已经被系统锁定,不能购买");
		}
		password = Pubfun.encryptMD5(user.getPhone(), password);
		if (!StringUtils.equals(password, user.getPayPassword())) {
			throw new BusinessException("支付密码错误,不能购买");
		}
		BaseData data = new BaseData();

		// 首先需要校验用户的最后一笔交易是否完成，如果没有不能购买
		Integer count = buyMapper.countBuyByCondtion(userId, StateKey.STATE_5);
		if (count > 0) {
			data.setError("您当前还有一笔交易未完成，不能继续购买(U1001)");
		} else {

			BigDecimal bigMoney = new BigDecimal(money);
			BigDecimal zero = new BigDecimal(0);

			// 购买表写入记录
			Buy buy = new Buy();
			buy.setId(IdWorker.getId());
			buy.setUserId(userId);
			buy.setState(StateKey.STATE_0);
			buy.setMoney(bigMoney);
			buy.setMatchMoney(bigMoney);
			buy.setOutMoney(zero);
			Date date = new Date();
			buy.setCreateTime(date);
			buy.setModifyTime(date);
			buyMapper.insert(buy);

			// 同时我们去更新用户表,这样批处理就扫描不到该用户
			user.setModifyTime(date);
			user.setPayTime(DateUtil.dateAddYear(date, TimeKey.USER_PAYTIME));
			user.setLockTime(null);
			userMapper.updateByPrimaryKey(user);

			// 用户钱包记录增加
			Wallet wallet = new Wallet();
			wallet.setUserId(userId);
			wallet.setBuyMoney(bigMoney);
			wallet.setSellMoney(zero);
			wallet.setIntegrity(zero);
			wallet.setInterest(zero);
			wallet.setCantraded(zero);
			wallet.setFreeze(zero);
			wallet.setWallet(zero);
			wallet.setModifyTime(date);
			walletMapper.updateUserWallet(wallet);

			// 加入用户操作轨迹表
			PNotice pNotice = new PNotice();
			pNotice.setId(buy.getId());
			pNotice.setUserId(userId);
			pNotice.setTitle("买入操作成功，请注意匹配消息");
			pNotice.setContent("您于" + DateUtil.formatDate(date) + "成功买入" + bigMoney + ",请关注匹配消息，及时打款给对方，并在系统上传打款凭证.");
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
		List<BusinessRecord> list = businessRecordMapper.getBuyList(condition);
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
					bean.setStateName("已打款对方未确认");
					break;
				case 2:
					bean.setStateName("收款方已确认");
					break;
				}
			}
		}
		return new BaseData(list);
	}

	public Account selectSellAccount(Long accountId) {
		return accountMapper.selectByPrimaryKey(accountId);
	}

	/**
	 * 更新表状态为已经打款
	 * 
	 * @param id
	 */
	@Transactional
	public void updateRecordState(Long id, String img) {
		if (id == null || StringUtils.isEmpty(img)) {
			throw new BusinessException("数据处理错误，请重新操作(U1002)");
		}
		BusinessRecord br = businessRecordMapper.selectByPrimaryKey(id);
		if (br == null) {
			throw new BusinessException("数据处理错误，请重新操作(U1003)");
		}
		br.setState(RecordKey.STATE_1);
		br.setPicUrl(img);
		Date date = new Date();
		br.setLockTime(DateUtil.dateAddHour(date, TimeKey.HOUR_24)); //付款后24小时确认
		br.setModifyTime(date);
		businessRecordMapper.updateByPrimaryKeySelective(br);

		PNotice pNotice = new PNotice();
		pNotice.setId(IdWorker.getId());
		pNotice.setUserId(br.getSellAccountId());
		pNotice.setTitle("对方已打款，请在系统中确认");
		pNotice.setContent("订单编号：" + br.getId() + " 用户已经给您打款，请及时到系统确认收款.");
		pNotice.setCreateTime(date);
		pNotice.setModifyTime(date);
		pNoticeMapper.insert(pNotice);
		
		User sellUser = userMapper.selectByPrimaryKey(br.getSellAccountId());
		SmsBean sb = new SmsBean();
		sb.setPhone(sellUser.getPhone());
		sb.setType(SMSKey.TEMP_1005);
		smsService.send(sb);
	}
}
