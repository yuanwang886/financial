package com.finance.p2p.biz.sys.bean;

import com.finance.p2p.entity.ReleaseCode;

public class RCBean extends ReleaseCode {

	private Integer num; // 数量

	private String phone;

	private String realName;

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
