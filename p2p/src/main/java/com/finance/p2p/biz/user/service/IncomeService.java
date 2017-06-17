package com.finance.p2p.biz.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.dao.IncomeTrackMapper;
import com.finance.p2p.entity.User;
import com.framework.SysConst.SysKey;

@Service
public class IncomeService {

	@Autowired
	private IncomeTrackMapper incomeTrackMapper;

	public Object list(Long id, User user) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("limit", SysKey.LIMIT);
		condition.put("id", id);
		condition.put("userId", user.getUserId());
		List<?> list = incomeTrackMapper.getUserIncomeList(condition);
		return new BaseData(list);
	}
}
