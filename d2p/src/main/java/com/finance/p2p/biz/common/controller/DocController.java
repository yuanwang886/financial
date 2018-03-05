package com.finance.p2p.biz.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 相关条款文档查看
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("doc")
public class DocController {

	/**
	 * 规则制度
	 * 
	 * @return
	 */
	@RequestMapping("/rule/index")
	public Object rule() {
		return "/doc/rule";
	}

	/**
	 * 帮助文档
	 * 
	 * @return
	 */
	@RequestMapping("/help/index")
	public Object help() {
		return "/doc/help";
	}

	/**
	 * 注意事项
	 * 
	 * @return
	 */
	@RequestMapping("/warn/index")
	public Object warn() {
		return "/doc/warn";
	}

}
