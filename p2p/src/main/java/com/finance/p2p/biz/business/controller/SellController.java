package com.finance.p2p.biz.business.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.business.service.SellService;
import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.common.controller.BaseController;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.sys.utils.Pubfun;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.Wallet;
import com.framework.exception.BusinessException;

/**
 * 卖出
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/business/sell")
public class SellController extends BaseController {

	@Autowired
	private SellService sellService;

	/**
	 * 卖出须知
	 * 
	 * @return
	 */
	@RequestMapping("/advice")
	public Object advice() {
		return "/business/advice";
	}

	/**
	 * 卖出首界面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public Object index(HttpSession session, Model model) {

		// 我曹，这里要计算用户到底有多少钱啊
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		Wallet wallet = sellService.selectUserWallet(user.getUserId());
		model.addAttribute("wallet", wallet);
		return "/business/sell";
	}

	/**
	 * 用户确定卖出
	 * 
	 * @param session
	 * @param money
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public Object sellSubmit(HttpSession session, Integer money, String password) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		return sellService.sellSubmit(user.getUserId(), money, password);
	}

	/**
	 * 得到帮助记录界面
	 * 
	 * @return
	 */
	@RequestMapping("/list/index")
	public Object listIndex() {
		return "/business/sellList";
	}

	/**
	 * 查询帮助的结果
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list(Long id, HttpSession session) {
		Object user = session.getAttribute(USERKey.USER_SESSION);
		return sellService.list(id, (User) user);
	}

	/**
	 * 获取打款账户信息
	 * 
	 * @return
	 */
	@RequestMapping("/account")
	public Object account(Model model, Long accountId) {
		model.addAttribute("account", sellService.selectSellAccount(accountId));
		model.addAttribute("type", 2);
		return "/business/account";
	}

	/**
	 * 启动确认收款界面
	 * 
	 * @return
	 */
	@RequestMapping("/confirm/index")
	public Object confirmIndex() {
		return "/business/confirm";
	}

	/**
	 * 用户确认收款
	 * 
	 * @return
	 */
	@RequestMapping("/confirm")
	@ResponseBody
	public Object confirm(Long id, String password, HttpSession session) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		password = Pubfun.encryptMD5(user.getPhone(), password);
		if (!StringUtils.equals(password, user.getPayPassword())) {
			throw new BusinessException("支付密码不正确~");
		}
		sellService.confirm(user, id);
		return new BaseData();
	}
}
