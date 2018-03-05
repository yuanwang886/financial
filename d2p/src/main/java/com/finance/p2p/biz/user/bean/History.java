package com.finance.p2p.biz.user.bean;

import com.finance.p2p.entity.ReleaseHistory;

public class History extends ReleaseHistory {

	private String typeName;

	private String phone;

	private String userName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
