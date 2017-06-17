package com.finance.p2p.biz.sys.bean;

public class MainBean {

	// 系统总人数
	private Integer personNum;

	// 已经锁定人数
	private Integer personLockNum;

	// 系统买入金额总数
	private Integer buyMoney;

	// 系统卖出金额总数
	private Integer sellMoney;

	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}

	public Integer getPersonLockNum() {
		return personLockNum;
	}

	public void setPersonLockNum(Integer personLockNum) {
		this.personLockNum = personLockNum;
	}

	public Integer getBuyMoney() {
		return buyMoney;
	}

	public void setBuyMoney(Integer buyMoney) {
		this.buyMoney = buyMoney;
	}

	public Integer getSellMoney() {
		return sellMoney;
	}

	public void setSellMoney(Integer sellMoney) {
		this.sellMoney = sellMoney;
	}
}
