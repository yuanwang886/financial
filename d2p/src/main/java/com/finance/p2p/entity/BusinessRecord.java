package com.finance.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class BusinessRecord {
	private Long id;

	private Long buyId;

	private Long sellId;

	private Integer type;

	private Long buyAccountId;

	private Long sellAccountId;

	private BigDecimal money;

	private Integer state;

	private String picUrl;

	private Date lockTime;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private Date modifyTime;

	/** 新增字段 */
	private String stateName;

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getBuyId() {
		return buyId;
	}

	public void setBuyId(Long buyId) {
		this.buyId = buyId;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getSellId() {
		return sellId;
	}

	public void setSellId(Long sellId) {
		this.sellId = sellId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getBuyAccountId() {
		return buyAccountId;
	}

	public void setBuyAccountId(Long buyAccountId) {
		this.buyAccountId = buyAccountId;
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getSellAccountId() {
		return sellAccountId;
	}

	public void setSellAccountId(Long sellAccountId) {
		this.sellAccountId = sellAccountId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl == null ? null : picUrl.trim();
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}