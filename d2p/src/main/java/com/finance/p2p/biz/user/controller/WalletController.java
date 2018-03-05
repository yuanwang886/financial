package com.finance.p2p.biz.user.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.user.service.WalletService;
import com.finance.p2p.entity.User;

/**
 * 用户钱包信息
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/user/wallet")
public class WalletController {

	
	@Autowired
	private WalletService walletService;
	/**
	 * 我的钱包界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public String index(HttpSession session, Model model) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		//查询用户钱包信息
		model.addAttribute("wallet", walletService.queryUserWallet(user));
		//查询最近一笔买入信息
		model.addAttribute("buy", walletService.queryUserLastBuy(user));
		//查询最近一笔卖出信息
		model.addAttribute("sell", walletService.queryUserLastSell(user));
		
		return "/user/wallet";
	}

}
