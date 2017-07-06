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
import com.finance.p2p.biz.sys.service.AllUserService;
import com.finance.p2p.biz.sys.service.LoginService;
import com.finance.p2p.biz.user.service.WalletService;
import com.finance.p2p.entity.User;
import com.github.pagehelper.Page;

/**
 * 所有平台用户操作信息
 *
 */
@Controller
@RequestMapping("alluser")
public class AllUserController extends BaseController {

	@Autowired
	private AllUserService allUserService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private LoginService loginService;

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping("index")
	public String index() {
		return "/sys/user";
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

		List<?> list = allUserService.getUserList(condition);

		Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));// 记录操作的次数

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sEcho", sEcho);
		result.put("iTotalRecords", page.getTotal());// 数据总条数
		result.put("iTotalDisplayRecords", page.getTotal());// 显示的条数
		result.put("data", list);// 数据集合
		return result;
	}

	/**
	 * 锁定指定的用户
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/lock")
	@ResponseBody
	public Object lock(Long userId) {
		return allUserService.lockUserByUserId(userId);

	}
	
	@RequestMapping("wallet")
	public String wallet(Long userId, Model model) {
		User user = loginService.selectByUserId(userId);
		//查询用户钱包信息
		model.addAttribute("wallet", walletService.queryUserWallet(user));
		//查询最近一笔买入信息
		model.addAttribute("buy", walletService.queryUserLastBuy(user));
		//查询最近一笔卖出信息
		model.addAttribute("sell", walletService.queryUserLastSell(user));
		
		return "/user/wallet";
	}
}
