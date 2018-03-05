package com.finance.p2p.biz.sms.bean;

/**
 * 
 * @author Administrator
 *
 */
public class SmsBean {

	// 手机号码
	private String phone;

	// 短信类型
	private Integer type;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}
