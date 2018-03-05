package com.finance.p2p.biz.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.dao.FeedBackMapper;
import com.finance.p2p.entity.FeedBack;
import com.finance.p2p.entity.User;
import com.framework.utils.IdWorker;

/**
 * 意见反馈
 * 
 * @author Administrator
 *
 */
@Service
public class FeedbackService {
	@Autowired
	private FeedBackMapper feedBackMapper;

	/**
	 * 提交用户反馈，后台管理员可以查看
	 * 
	 * @param user
	 * @param content
	 * @return
	 */
	public Object submit(User user, String content) {
		FeedBack bean = new FeedBack();

		bean.setId(IdWorker.getId());
		bean.setUserId(user.getUserId());
		bean.setContent(content);
		bean.setPhone(user.getPhone());

		Date nowDate = new Date();
		bean.setCreateTime(nowDate);
		bean.setModifyTime(nowDate);

		feedBackMapper.insertSelective(bean);
		return new BaseData();
	}

	/**
	 * 管理员根据条件查询意见反馈列表
	 * 
	 * @return
	 */
	public List<FeedBack> getFeedbackList(Map<String, Object> condition) {
		return feedBackMapper.getFeedbackList(condition);
	}

	/**
	 * 管理员删除某条意见反馈
	 * 
	 * @param id
	 * @return
	 */
	public Object deleteFeedbackById(Long id) {
		feedBackMapper.deleteByPrimaryKey(id);
		return new BaseData();
	}
}
