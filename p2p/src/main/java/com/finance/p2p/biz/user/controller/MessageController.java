package com.finance.p2p.biz.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.user.service.MessageService;
import com.finance.p2p.entity.User;

/**
 * 用户消息
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	/**
	 * 用户消息首届面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index(HttpSession session) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		messageService.update(user);
		return "/user/message";
	}

	/**
	 * 获取用户的消息列表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list(Long id, HttpSession session) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		return messageService.list(id, user);
	}
}
