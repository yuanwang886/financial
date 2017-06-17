package com.finance.p2p.biz.user.bean;

import com.finance.p2p.entity.Account;
import com.finance.p2p.entity.User;

public class Info {

	private User user;

	private Account account;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
}
