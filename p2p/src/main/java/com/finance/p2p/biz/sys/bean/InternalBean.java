package com.finance.p2p.biz.sys.bean;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class InternalBean {

	private Long userId;

	private String name;

	private String phone;

	private BigDecimal wallet; // 钱包金额

	private BigDecimal cantraded; // 可卖出金额

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public BigDecimal getWallet() {
		return wallet;
	}

	public void setWallet(BigDecimal wallet) {
		this.wallet = wallet;
	}

	public BigDecimal getCantraded() {
		return cantraded;
	}

	public void setCantraded(BigDecimal cantraded) {
		this.cantraded = cantraded;
	}

}
