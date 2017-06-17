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
import com.finance.p2p.biz.sys.service.NoticeService;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.entity.User;
import com.github.pagehelper.Page;

/**
 * 通知处理类
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("notice")
public class NoticeController extends BaseController {

	@Autowired
	private NoticeService noticeService;

	/**
	 * 管理员通知界面
	 * 
	 * @return
	 */
	@RequestMapping("/index")
	public Object index() {
		return "/notice/index";
	}

	/**
	 * 新增公告界面
	 * 
	 * @return
	 */
	@RequestMapping("/add")
	public Object add() {
		return "/notice/add";
	}

	/**
	 * 管理员提交公告
	 * 
	 * @param content
	 * @param session
	 * @return
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public Object submit(String title, String content) {
		return noticeService.submit(title, content);
	}

	/**
	 * 管理员查询所有用户反馈信息
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

		List<?> list = noticeService.getSNoticeList(condition);

		Integer sEcho = Integer.valueOf(request.getParameter("sEcho"));// 记录操作的次数

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sEcho", sEcho);
		result.put("iTotalRecords", page.getTotal());// 数据总条数
		result.put("iTotalDisplayRecords", page.getTotal());// 显示的条数
		result.put("data", list);// 数据集合
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
	public Object delete(Long id) {
		return noticeService.deleteSNoticeById(id);
	}

	/**
	 * 用户系统公告首页
	 * 
	 * @return
	 */
	@RequestMapping("/u/index")
	public Object uIndex(HttpSession session) {
		User user = (User) session.getAttribute(USERKey.USER_SESSION);
		noticeService.update(user);
		return "/notice/list";
	}

	/**
	 * 查询用户看到的系统公告
	 * 
	 * @return
	 */
	@RequestMapping("/u/list")
	@ResponseBody
	public Object uList(Long id) {
		return noticeService.uList(id);
	}
}
