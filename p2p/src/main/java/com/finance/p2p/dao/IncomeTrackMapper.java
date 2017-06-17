package com.finance.p2p.dao;

import java.util.List;
import java.util.Map;

import com.finance.p2p.entity.IncomeTrack;

public interface IncomeTrackMapper {
	int deleteByPrimaryKey(Long id);

	int insert(IncomeTrack record);

	int insertSelective(IncomeTrack record);

	IncomeTrack selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(IncomeTrack record);

	int updateByPrimaryKey(IncomeTrack record);

	/**
	 * 查询用户的金额记录
	 * 
	 * @param condition
	 * @return
	 */
	List<IncomeTrack> getUserIncomeList(Map<String, Object> condition);
}