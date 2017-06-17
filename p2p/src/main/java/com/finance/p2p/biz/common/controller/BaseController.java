package com.finance.p2p.biz.common.controller;

import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 
 * @author Administrator
 *
 */
public class BaseController {

	protected Page<?> getPageObject(HttpServletRequest request) {

		return this.getPageObject(request, null);
	}

	protected Page<?> getPageObject(HttpServletRequest request, String orderByClause) {
		// Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));//
		// 记录操作的次数
		// 每次加1
		Integer iDisplayStart = Integer.valueOf(request.getParameter("iDisplayStart"));// 起始
		Integer iDisplayLength = Integer.valueOf(request.getParameter("iDisplayLength"));// 每页显示的size

		// 获取第1页，10条内容，默认查询总数count
		Integer pageNum = iDisplayStart / iDisplayLength + 1;
		return PageHelper.startPage(pageNum, iDisplayLength);
	}
}
