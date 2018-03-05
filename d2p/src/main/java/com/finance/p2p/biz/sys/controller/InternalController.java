package com.finance.p2p.biz.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.common.controller.BaseController;
import com.finance.p2p.biz.sys.service.InternalService;
import com.finance.p2p.entity.Account;
import com.finance.p2p.entity.User;
import com.github.pagehelper.Page;

@Controller
@RequestMapping("internal")
public class InternalController extends BaseController {

	@Autowired
	private InternalService internalService;

	/**
	 * 内置用户界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index() {
		return "/internal/index";
	}

	/**
	 * 管理员查询所有内置用户信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object feedbackList(HttpServletRequest request) {

		Page<?> page = getPageObject(request);

		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("phone", request.getParameter("phone"));

		List<?> list = internalService.getInternalUserList(condition);

		Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));// 记录操作的次数

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sEcho", sEcho);
		result.put("iTotalRecords", page.getTotal());// 数据总条数
		result.put("iTotalDisplayRecords", page.getTotal());// 显示的条数
		result.put("data", list);// 数据集合
		return result;
	}

	/**
	 * 新增内置用户
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public Object add() {
		return "/internal/add";
	}

	/**
	 * 提交内置用户
	 * 
	 * @param phone
	 * @param password
	 * @param money
	 * @param account
	 * @return
	 */
	@RequestMapping(value = "/submit")
	@ResponseBody
	public Object submit(String phone, String password, String money, Account account) {

		return internalService.submit(account, phone, password, money);
	}

	/**
	 * 更新内置用户
	 * 
	 * @return
	 */
	@RequestMapping("/update")
	public Object update(String userId, Model model) {
		User user = internalService.getInternalUser(Long.valueOf(userId));
		model.addAttribute("internal", user);
		return "/internal/update";
	}

	/**
	 * 提交更新用户
	 * 
	 * @param userId
	 * @param money
	 * @return
	 */
	@RequestMapping(value = "/modify")
	@ResponseBody
	public Object modify(Long userId, String money) {
		return internalService.modify(userId, money);
	}
}
