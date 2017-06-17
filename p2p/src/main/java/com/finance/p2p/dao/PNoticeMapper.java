package com.finance.p2p.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.entity.PNotice;

public interface PNoticeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PNotice record);

    int insertSelective(PNotice record);

    PNotice selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PNotice record);

    int updateByPrimaryKey(PNotice record);
    
	/**
	 * 根据条件查询用户消息
	 * 
	 * @param condition
	 * @return
	 */
	List<PNotice> getNoticeList(Map<String, Object> condition);
	
	
	/**
	 * 统计用户未读的用户消息数目
	 * @param nowDate
	 * @return
	 */
	Integer selectUnReadByDate(@Param("nowDate")Date nowDate, @Param("userId") Long userId);
}