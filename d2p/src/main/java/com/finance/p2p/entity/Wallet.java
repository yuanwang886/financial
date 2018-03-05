package com.finance.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Wallet {
    private Long userId;

    private BigDecimal buyMoney;

    private BigDecimal sellMoney;

    private BigDecimal integrity;

    private BigDecimal interest;

    private BigDecimal cantraded;

    private BigDecimal freeze;

    private BigDecimal wallet;

    private Date createTime;

    private Date modifyTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    public BigDecimal getIntegrity() {
        return integrity;
    }

    public void setIntegrity(BigDecimal integrity) {
        this.integrity = integrity;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getCantraded() {
        return cantraded;
    }

    public void setCantraded(BigDecimal cantraded) {
        this.cantraded = cantraded;
    }

    public BigDecimal getFreeze() {
        return freeze;
    }

    public void setFreeze(BigDecimal freeze) {
        this.freeze = freeze;
    }

    public BigDecimal getWallet() {
        return wallet;
    }

    public void setWallet(BigDecimal wallet) {
        this.wallet = wallet;
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
}