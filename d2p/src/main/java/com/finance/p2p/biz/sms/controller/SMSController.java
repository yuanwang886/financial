package com.finance.p2p.biz.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.sms.bean.SmsBean;
import com.finance.p2p.biz.sms.service.SMSService;

@Controller
@RequestMapping("sms")
public class SMSController {

	@Autowired
	private SMSService smsService;

	/**
	 * 发送短信
	 * 
	 * @param bean
	 * @return
	 */
	@RequestMapping("send")
	@ResponseBody
	public Object send(@RequestBody SmsBean bean) {
		return smsService.send(bean);
	}
}
