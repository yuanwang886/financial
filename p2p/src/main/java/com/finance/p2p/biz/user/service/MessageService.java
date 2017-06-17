package com.finance.p2p.biz.user.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.dao.PNoticeMapper;
import com.finance.p2p.dao.UserTimeMapper;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.UserTime;
import com.framework.SysConst.SysKey;

@Service
public class MessageService {

	@Autowired
	private PNoticeMapper pNoticeMapper;

	@Autowired
	private UserTimeMapper userTimeMapper;
	
	public void update(User user) {

		UserTime userTime = userTimeMapper.selectByPrimaryKey(user.getUserId());
		
		Date date = new Date();
		userTime.setpNoticeTime(date);
		userTimeMapper.updateByPrimaryKeySelective(userTime);
	}
	
	public Object list(Long id, User user) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("limit", SysKey.LIMIT);
		condition.put("id", id);
		condition.put("userId", user.getUserId());
		List<?> list = pNoticeMapper.getNoticeList(condition);
		return new BaseData(list);
	}
}
