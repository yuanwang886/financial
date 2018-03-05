package com.finance.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Buy {
    private Long id;

    private Long userId;

    private BigDecimal money;

    private BigDecimal matchMoney;

    private BigDecimal outMoney;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private Date modifyTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }

    public BigDecimal getMatchMoney() {
        return matchMoney;
    }

    public void setMatchMoney(BigDecimal matchMoney) {
        this.matchMoney = matchMoney.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(BigDecimal outMoney) {
        this.outMoney = outMoney.setScale(2, BigDecimal.ROUND_HALF_UP);;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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