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
import com.finance.p2p.biz.sys.service.ReleaseCodeService;
import com.github.pagehelper.Page;

/**
 * 激活码
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("release")
public class ReleaseCodeController extends BaseController {

	@Autowired
	private ReleaseCodeService releaseCodeService;

	/**
	 * 管理员查看界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index() {
		return "/release/index";
	}
	
	/**
	 * 新增激活码界面
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public Object add() {
		return "/release/add";
	}
	
	/**
	 * 购买提交
	 * @param title
	 * @param content
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public Object submit(String phone, Integer num) {
		return releaseCodeService.submit(phone, num);
	}
	
	/**
	 * 管理员查询所有用户购买信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list(HttpServletRequest request) {

		Page<?> page = getPageObject(request);

		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("startDate", request.getParameter("startDate"));
		condition.put("endDate", request.getParameter("endDate"));

		List<?> list = releaseCodeService.getRCRecordList(condition);

		Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));// 记录操作的次数

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sEcho", sEcho);
		result.put("iTotalRecords", page.getTotal());// 数据总条数
		result.put("iTotalDisplayRecords", page.getTotal());// 显示的条数
		result.put("data", list);// 数据集合
		return result;
	}
}
