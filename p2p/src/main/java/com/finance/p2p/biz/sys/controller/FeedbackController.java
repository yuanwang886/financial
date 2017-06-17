package com.finance.p2p.biz.sys.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finance.p2p.biz.common.controller.BaseController;
import com.finance.p2p.biz.sys.service.FeedbackService;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.entity.User;
import com.github.pagehelper.Page;

/**
 * 意见反馈
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("feedback")
public class FeedbackController extends BaseController {

	@Autowired
	private FeedbackService feedbackService;

	/**
	 * 进入意见反馈界面
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public Object feedback() {
		return "/feedback/add";
	}

	/**
	 * 意见反馈
	 * 
	 * @param content
	 * @param session
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public Object submit(String content, HttpSession session) {
		return feedbackService.submit((User) session.getAttribute(USERKey.USER_SESSION), content);
	}

	/**
	 * 系统用户反馈界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index() {
		return "/feedback/index";
	}

	/**
	 * 管理员查询所有用户反馈信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object feedbackList(HttpServletRequest request) {

		Page<?> page = getPageObject(request);

		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("startDate", request.getParameter("startDate"));
		condition.put("endDate", request.getParameter("endDate"));

		List<?> list = feedbackService.getFeedbackList(condition);

		Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));// 记录操作的次数

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sEcho", sEcho);
		result.put("iTotalRecords", page.getTotal());// 数据总条数
		result.put("iTotalDisplayRecords", page.getTotal());// 显示的条数
		result.put("data", list);// 数据集合
		// 采用JsonMapper.toJsonString(result) 与 produces =
		// "application/json;charset=UTF-8" 解决返回乱码以及Long被截断的问题
		return result;
	}

	/**
	 * 管理员删除意见反馈
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public Object feedbackDelete(Long id) {
		return feedbackService.deleteFeedbackById(id);
	}
}
