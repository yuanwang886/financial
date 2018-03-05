package com.finance.p2p.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.entity.SNotice;

public interface SNoticeMapper {
	int deleteByPrimaryKey(Long id);

	int insert(SNotice record);

	int insertSelective(SNotice record);

	SNotice selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(SNotice record);

	int updateByPrimaryKey(SNotice record);

	/**
	 * 根据条件查询系统通知
	 * 
	 * @param condition
	 * @return
	 */
	List<SNotice> getNoticeList(Map<String, Object> condition);
	
	/**
	 * 统计用户未读的系统消息数目
	 * @param nowDate
	 * @return
	 */
	Integer selectUnReadByDate(@Param("nowDate")Date nowDate);
}