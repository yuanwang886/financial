package com.finance.p2p.biz.sys.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.sys.service.MainService;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.entity.User;

/**
 * 首页
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("main")
public class MainController {

	@Autowired
	private MainService mainService;

	/**
	 * 获取用户菜单
	 * 
	 * @return
	 */
	@RequestMapping("/menu")
	@ResponseBody
	public Object getMenuList(HttpSession session) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		return mainService.getMenuList(user.getUserRole());
	}

	/**
	 * 首页显示
	 * 
	 * @return
	 */
	@RequestMapping("/homePage")
	public Object homePage(HttpSession session, Model model) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		if (user.getUserRole().equals(USERKey.ADMIN)) {
			model.addAttribute("sysData", mainService.querySysData());
		}
		return "/sys/homePage";
	}

	/**
	 * 刷新主页
	 * 
	 * @return
	 */
	@RequestMapping("")
	public Object mainPage(HttpSession session, Model model) {
		//需要查询用户对应的消息数目
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		model.addAttribute("notice", mainService.countNotice(user));
		return "/sys/main";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true)); // true:允许输入空值，false:不能为空值
	}
}
