package com.finance.p2p.biz.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.user.service.TeamService;
import com.finance.p2p.entity.User;

/**
 * 用户消息
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user/team")
public class TeamController {

	@Autowired
	private TeamService teamService;

	/**
	 * 我的团队界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index(HttpSession session, Model model) {

		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		model.addAttribute("team", teamService.info((User) user));

		return "/user/team";
	}

	/**
	 * 一级团队首届面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/first")
	public Object first(HttpSession session, Model model) {
		return "/user/team1";
	}

	/**
	 * 二级团队
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/second")
	public Object second(HttpSession session, Model model) {
		return "/user/team2";
	}

	/**
	 * 获取下级排单情况
	 * 
	 * @param id
	 * @param type
	 * @param session
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list(Long id, Integer type, HttpSession session) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		return teamService.info(user, type, id);
	}

	/**
	 * 激活某一个用户
	 * 
	 * @param session
	 * @param userId
	 * @return
	 */
	@RequestMapping("/activation")
	@ResponseBody
	public Object activation(HttpSession session, Long userId) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		return teamService.activation(user, userId);
	}

	/**
	 * 下发激活码提交
	 * 
	 * @param session
	 * @param userId
	 * @param num
	 * @return
	 */
	@RequestMapping("/release")
	@ResponseBody
	public Object release(HttpSession session, Long userId, Integer num) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		return teamService.release(user, userId, num);
	}
}
