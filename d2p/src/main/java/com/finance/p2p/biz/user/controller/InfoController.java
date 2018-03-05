package com.finance.p2p.biz.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.user.service.InfoService;
import com.finance.p2p.entity.Account;
import com.finance.p2p.entity.User;

/**
 * 用户信息
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user/info")
public class InfoController {

	@Autowired
	private InfoService infoService;

	/**
	 * 我的团队界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index() {
		return "/user/info";
	}

	/**
	 * 进入编辑界面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public Object edit() {
		return "/user/edit";
	}

	/**
	 * 提交用户账户信息
	 * 
	 * @param session
	 * @param account
	 * @return
	 */
	@RequestMapping("/acc/submit")
	@ResponseBody
	public Object accSubmit(HttpSession session, Account account) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		BaseData data = infoService.submit(user, account);
		user.setAccount(account);
		return data;
	}

	/**
	 * 修改支付密码
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/paypass")
	public Object paypass(HttpSession session, Model model) {
		return "/user/paypass";
	}

	/**
	 * 支付密码
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/paypass/submit")
	@ResponseBody
	public Object paySubmit(HttpSession session, String code, String password) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		BaseData data = infoService.paySubmit(user, password, code);
		return data;
	}
}
