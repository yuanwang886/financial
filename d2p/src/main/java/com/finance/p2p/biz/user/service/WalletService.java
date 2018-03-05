package com.finance.p2p.biz.user.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.business.bean.BuyBean;
import com.finance.p2p.biz.business.bean.SellBean;
import com.finance.p2p.biz.sys.utils.Const.StateKey;
import com.finance.p2p.dao.BuyMapper;
import com.finance.p2p.dao.SellMapper;
import com.finance.p2p.dao.WalletMapper;
import com.finance.p2p.entity.Buy;
import com.finance.p2p.entity.Sell;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.Wallet;

@Service
public class WalletService {

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private BuyMapper buyMapper;

	@Autowired
	private SellMapper sellMapper;

	public Wallet queryUserWallet(User user) {

		return walletMapper.selectByPrimaryKey(user.getUserId());
	}

	public BuyBean queryUserLastBuy(User user) {

		Buy buy = buyMapper.queryUserLastBuy(user.getUserId());

		BuyBean bean = new BuyBean();
		if (buy != null) {
			BeanUtils.copyProperties(buy, bean);
			bean.setStateName(StateKey.MAP.get(buy.getState()));
		}
		return bean;
	}

	public SellBean queryUserLastSell(User user) {
		Sell sell = sellMapper.queryUserLastSell(user.getUserId());

		SellBean bean = new SellBean();
		// 属性拷贝
		if (sell != null) {
			BeanUtils.copyProperties(sell, bean);
			bean.setStateName(StateKey.MAP.get(sell.getState()));
		}
		return bean;
	}
}
