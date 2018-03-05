package com.finance.p2p.biz.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.user.service.IncomeService;
import com.finance.p2p.entity.User;

/**
 * 用户金额变化轨迹
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user/income")
public class IncomeController {

	@Autowired
	private IncomeService incomeService;

	/**
	 * 用户金额轨迹首届面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index(HttpSession session) {
		return "/user/income";
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
		return incomeService.list(id, user);
	}
}
