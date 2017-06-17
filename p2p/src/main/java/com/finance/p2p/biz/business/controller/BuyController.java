package com.finance.p2p.biz.business.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.business.service.BuyService;
import com.finance.p2p.biz.common.controller.BaseController;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.entity.User;

/**
 * 买入相关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/business/buy")
public class BuyController extends BaseController {

	@Autowired
	private BuyService buyService;

	/**
	 * 购买首界面
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping("/index")
	public Object edit(HttpSession session, Model model) {
		return "/business/buy";
	}

	/**
	 * 责任风险
	 * 
	 * @return
	 */
	@RequestMapping("/risk")
	public Object risk() {
		return "/business/risk";
	}

	/**
	 * 用户确定购买
	 * 
	 * @param session
	 * @param money
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public Object buySubmit(HttpSession session, Integer money, String password) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		return buyService.buySubmit(user.getUserId(), money, password);
	}

	/**
	 * 提供帮助记录界面
	 * 
	 * @return
	 */
	@RequestMapping("/list/index")
	public Object listIndex() {
		return "/business/buyList";
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
		return buyService.list(id, (User) user);
	}

	/**
	 * 获取收款账户信息
	 * 
	 * @return
	 */
	@RequestMapping("/account")
	public Object account(Model model, Long accountId) {
		model.addAttribute("account", buyService.selectSellAccount(accountId));
		model.addAttribute("type", 1);
		return "/business/account";
	}

	/**
	 * 启动上传界面
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/upload")
	public Object upload(Model model, Long id) {
		model.addAttribute("id", id);
		return "/business/upload";
	}
	
	/**
	 * 显示图片
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping("/showImg")
	public Object upload(Model model, String url) {
		model.addAttribute("imgUrl", url);
		return "/business/showImg";
	}
}
