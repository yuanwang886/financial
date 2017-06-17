package com.finance.p2p.biz.business.bean;

import com.finance.p2p.entity.Sell;

public class SellBean extends Sell {
	private String stateName;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
