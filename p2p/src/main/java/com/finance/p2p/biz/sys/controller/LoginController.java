package com.finance.p2p.biz.sys.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.sys.service.LoginService;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.framework.servlet.ValidateCodeServlet;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/sys")
public class LoginController {

	@Autowired
	private LoginService loginService;

	/**
	 * 
	 * @param request
	 * @param model
	 * @param realname
	 * @param phone
	 * @param password
	 * @param code
	 * @param invitePhone
	 * @return
	 */
	@RequestMapping("/register")
	public Object register(HttpSession session, Model model, String realname, String phone, String password,
			String code, String invitePhone) {

		boolean flag = true;
		try {
			Object user = loginService.register(realname, phone, password, invitePhone, code);
			session.setAttribute(USERKey.USER_SESSION, user);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			flag = false;
		}

		if (!flag) {
			// 说明已经发生了错误
			model.addAttribute("realname", realname);
			model.addAttribute("phone", phone);
			model.addAttribute("password", password);
			model.addAttribute("invitePhone", invitePhone);
			return "/sys/register";
		} else {
			return "redirect:/main";
		}
	}

	/**
	 * 登录
	 * 
	 * @param request
	 * @param model
	 * @param phone
	 * @param password
	 * @param validateCode
	 * @return
	 */
	@RequestMapping("/login")
	public Object login(HttpServletRequest request, Model model, String phone, String password, String validateCode) {

		boolean flag = true;
		// 判断用户是否登录
		if (request.getSession().getAttribute(USERKey.USER_SESSION) == null) {
			// 首先校验验证码是否正确
			if (!ValidateCodeServlet.validate(request, validateCode)) {
				model.addAttribute("message", "验证码不正确,请重新输入");
				flag = false;
			}
			try {
				Object user = loginService.login(phone, password);
				request.getSession().setAttribute(USERKey.USER_SESSION, user);
			} catch (Exception e) {
				model.addAttribute("message", e.getMessage());
				flag = false;
			}
		}

		if (flag) {
			return "redirect:/main";
		} else {
			return "/sys/login";
		}
	}

	/**
	 * 校验用户注册手机号
	 * 
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = "/checkPhone", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Object checkPhone(@RequestParam String phone) {

		Object user = loginService.selectByPhone(phone);
		Map<String, Boolean> map = new HashMap<>();
		map.put("valid", user == null);

		return map;
	}

	/**
	 * 用户忘记密码
	 * 
	 * @param session
	 * @param model
	 * @param phone
	 * @param password
	 * @param code
	 * @return
	 */
	@RequestMapping("/forgot")
	public Object forgot(HttpSession session, Model model, String phone, String password, String code) {

		boolean flag = true;
		try {
			loginService.forgot(phone, password, code);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			flag = false;
		}

		if (!flag) {
			// 说明已经发生了错误
			model.addAttribute("phone", phone);
			model.addAttribute("password", password);
			return "/sys/forgot";
		} else {
			return "redirect:/index";
		}
	}
}
