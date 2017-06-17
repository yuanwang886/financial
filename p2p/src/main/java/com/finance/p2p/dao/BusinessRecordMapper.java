package com.finance.p2p.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.biz.user.bean.Match;
import com.finance.p2p.entity.BusinessRecord;

public interface BusinessRecordMapper {
	int deleteByPrimaryKey(Long id);

	int insert(BusinessRecord record);

	int insertSelective(BusinessRecord record);

	BusinessRecord selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(BusinessRecord record);

	int updateByPrimaryKey(BusinessRecord record);

	/**
	 * 我们直接查看record表，把对应的UserID查询出来
	 * 
	 * @param nowDate
	 * @param hour
	 * @return
	 */
	List<BusinessRecord> selectLockRecord(@Param("nowDate") Date nowDate, @Param("type") Integer type,
			@Param("state") Integer state);

	/**
	 * 查询出自动确认的记录信息
	 * 
	 * @param nowDate
	 * @param state
	 * @return
	 */
	List<BusinessRecord> selectConfirmRecord(@Param("nowDate") Date nowDate, @Param("state") Integer state);

	/**
	 * 查询用户购买列表
	 * 
	 * @param condtion
	 * @return
	 */
	List<BusinessRecord> getBuyList(Map<String, Object> condition);

	/**
	 * 查询用户得到帮助的记录
	 * 
	 * @param condition
	 * @return
	 */
	List<BusinessRecord> getSellList(Map<String, Object> condition);

	/**
	 * 获取用户匹配记录
	 * 
	 * @param condition
	 * @return
	 */
	List<Match> getMatchList(Map<String, Object> condition);

}