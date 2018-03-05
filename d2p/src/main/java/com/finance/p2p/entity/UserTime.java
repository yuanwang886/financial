package com.finance.p2p.entity;

import java.util.Date;

public class UserTime {
    private Long userId;

    private Date sNoticeTime;

    private Date pNoticeTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getsNoticeTime() {
        return sNoticeTime;
    }

    public void setsNoticeTime(Date sNoticeTime) {
        this.sNoticeTime = sNoticeTime;
    }

    public Date getpNoticeTime() {
        return pNoticeTime;
    }

    public void setpNoticeTime(Date pNoticeTime) {
        this.pNoticeTime = pNoticeTime;
    }
}