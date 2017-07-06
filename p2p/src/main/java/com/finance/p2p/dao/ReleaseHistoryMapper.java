package com.finance.p2p.dao;

import java.util.List;
import java.util.Map;

import com.finance.p2p.biz.user.bean.History;
import com.finance.p2p.entity.ReleaseHistory;

public interface ReleaseHistoryMapper {
	int deleteByPrimaryKey(Long id);

	int insert(ReleaseHistory record);

	int insertSelective(ReleaseHistory record);

	ReleaseHistory selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ReleaseHistory record);

	int updateByPrimaryKey(ReleaseHistory record);

	/**
	 * 根据用户查询操作记录
	 * 
	 * @param userId
	 * @param id
	 * @return
	 */
	List<History> selectHistoryByUserId(Map<String, Object> condition);
}