package com.finance.p2p.biz.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.common.controller.BaseController;
import com.finance.p2p.biz.sys.service.LockService;
import com.github.pagehelper.Page;

/**
 * 用户锁定相关处理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/lock")
public class LockController extends BaseController {

	@Autowired
	private LockService lockService;

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("index")
	public String index() {
		return "/sys/lock";
	}

	/**
	 * 锁定人员列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(HttpServletRequest request) {

		Page<?> page = getPageObject(request);

		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("phone", request.getParameter("phone"));
		condition.put("realname", request.getParameter("realname"));

		List<?> list = lockService.getLockUserList(condition);

		Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));// 记录操作的次数

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sEcho", sEcho);
		result.put("iTotalRecords", page.getTotal());// 数据总条数
		result.put("iTotalDisplayRecords", page.getTotal());// 显示的条数
		result.put("data", list);// 数据集合
		return result;
	}

	/**
	 * 解锁一个用户
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/unlock")
	@ResponseBody
	public Object unlock(Long userId) {
		return lockService.unLockUserByUserId(userId);

	}
}
