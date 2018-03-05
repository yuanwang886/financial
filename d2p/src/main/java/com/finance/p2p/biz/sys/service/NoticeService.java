package com.finance.p2p.biz.sys.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.dao.SNoticeMapper;
import com.finance.p2p.dao.UserTimeMapper;
import com.finance.p2p.entity.SNotice;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.UserTime;
import com.framework.SysConst.SysKey;
import com.framework.utils.IdWorker;

@Service
public class NoticeService {

	@Autowired
	private SNoticeMapper sNoticeMapper;

	@Autowired
	private UserTimeMapper userTimeMapper;

	/**
	 * 提交系统公告
	 * 
	 * @param content
	 * @return
	 */
	public Object submit(String title, String content) {
		SNotice bean = new SNotice();

		bean.setId(IdWorker.getId());
		bean.setContent(content);
		bean.setTitle(title);
		Date nowDate = new Date();
		bean.setCreateTime(nowDate);
		bean.setModifyTime(nowDate);

		sNoticeMapper.insertSelective(bean);
		return new BaseData();
	}

	/**
	 * 管理员根据条件查询公告列表
	 * 
	 * @return
	 */
	public List<SNotice> getSNoticeList(Map<String, Object> condition) {
		return sNoticeMapper.getNoticeList(condition);
	}

	/**
	 * 管理员删除某条公告信息
	 * 
	 * @param id
	 * @return
	 */
	public Object deleteSNoticeById(Long id) {
		sNoticeMapper.deleteByPrimaryKey(id);
		return new BaseData();
	}

	/**
	 * 更新用户查看通知时间
	 * 
	 * @param user
	 */
	public void update(User user) {

		UserTime userTime = userTimeMapper.selectByPrimaryKey(user.getUserId());

		Date date = new Date();
		userTime.setsNoticeTime(date);
		userTimeMapper.updateByPrimaryKeySelective(userTime);
	}

	/**
	 * 查询用户需要看到的系统公告信息
	 * 
	 * @return
	 */
	public Object uList(Long id) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("limit", SysKey.LIMIT);
		condition.put("id", id);
		List<?> list = sNoticeMapper.getNoticeList(condition);
		return new BaseData(list);
	}
}
