package com.finance.p2p.biz.business.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.business.service.TaskService;
import com.finance.p2p.biz.common.bean.BaseData;

@Controller
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;

	/**
	 * 锁定用户
	 */
	@RequestMapping("/userStateLock")
	@ResponseBody
	public Object userStateLockTask() {
		taskService.userStateLockTask();
		return new BaseData();
	}

	/**
	 * 自动确认用户打款24小时内
	 */
	@RequestMapping("/confirmPay")
	@ResponseBody
	public Object confirmPayTask() {
		taskService.confirmPayTask();
		return new BaseData();
	}

	/**
	 * 计算用户利息
	 */
	@RequestMapping("/userInterest")
	@ResponseBody
	public Object userInterestTask() {
		taskService.userInterestTask();
		return new BaseData();
	}

	/**
	 * 用户级别任务处理
	 */
	@RequestMapping("/userLevel")
	@ResponseBody
	public Object userLevelTask() {
		taskService.userLevelTask();
		return new BaseData();
	}

	/**
	 * 对用户进行10%预付款处理
	 */
	@RequestMapping("/userSubsist")
	@ResponseBody
	public Object userSubsistTask() {
		taskService.userSubsistTask();
		return new BaseData();
	}

	/**
	 * 对用户进行90%余款处理
	 */
	@RequestMapping("/userSurplus")
	@ResponseBody
	public Object userSurplusTask() {
		taskService.userSurplusTask();
		return new BaseData();
	}
}
