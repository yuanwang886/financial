package com.finance.p2p.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.entity.Buy;

public interface BuyMapper {
	int deleteByPrimaryKey(Long id);

	int insert(Buy record);

	int insertSelective(Buy record);

	Buy selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Buy record);

	int updateByPrimaryKey(Buy record);

	/**
	 * 查询用户是否应交易完成
	 * 
	 * @param userId
	 * @return
	 */
	int countBuyByCondtion(@Param("userId") Long userId, @Param("state") Integer state);

	/**
	 * 查询出一个需要匹配预付款或者余额的用户
	 * 
	 * @return
	 */
	Buy selectSubsist();

	/**
	 * 查询出一个需要匹配余额的用户
	 * 
	 * @param state
	 * @return
	 */
	Buy selectSurplus(@Param("nowDate") Date nowDate, @Param("day") Integer day);

	/**
	 * 查询用户最后一笔买入信息系
	 * 
	 * @param userId
	 * @return
	 */
	Buy queryUserLastBuy(Long userId);

	/**
	 * 计算用户是否还存在计算利息的数据
	 * 
	 * @param userId
	 * @return
	 */
	Integer countUserUnComplate(@Param("userId") Long userId, @Param("state") Integer state);

	/**
	 * 当锁定一个用户处理的购买数据
	 * 
	 * @return
	 */
	int updateUserBuyWhenLock(@Param("userId") Long userId, @Param("nowDate") Date nowDate);
	
	/**
	 * 当解锁一个用户处理的购买数据
	 * @param userId
	 * @param nowDate
	 * @return
	 */
	int updateUserBuyWhenUnLock(@Param("userId") Long userId, @Param("nowDate") Date nowDate);
}