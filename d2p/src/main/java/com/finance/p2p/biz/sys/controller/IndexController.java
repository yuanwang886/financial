package com.finance.p2p.biz.sys.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finance.p2p.biz.sys.utils.Const.USERKey;

@Controller
public class IndexController {

	/**
	 * 进入登录界面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("index")
	public String index() {
		return "/sys/login";
	}

	/**
	 * 进入注册页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("register")
	public String register(Model model, String invitePhone) {
		model.addAttribute("invitePhone", invitePhone);
		return "/sys/register";
	}
	
	/**
	 * 进入内部注册页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("regbyadmin")
	public String registerAdmin(Model model) {
		return "/sys/registerByAdmin";
	}

	/**
	 * 忘记密码
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("forgot")
	public String forgot() {
		return "/sys/forgot";
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		session.removeAttribute(USERKey.USER_SESSION);
		return "/sys/login";
	}

	/**
	 * 用户超时
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("timeout")
	public String timeout(HttpSession session) {
		session.removeAttribute(USERKey.USER_SESSION);
		return "/error/555";
	}
}
