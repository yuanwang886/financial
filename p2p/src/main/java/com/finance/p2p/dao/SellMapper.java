package com.finance.p2p.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.entity.Sell;

public interface SellMapper {
	int deleteByPrimaryKey(Long id);

	int insert(Sell record);

	int insertSelective(Sell record);

	Sell selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Sell record);

	int updateByPrimaryKey(Sell record);

	/**
	 * 找出需要提现的用户信息
	 */
	List<Sell> selectSellTop(@Param("id") Long id, @Param("userId") Long userId);

	int countSellByCondtion(@Param("userId") Long userId, @Param("nowDate") Date nowDate);

	/**
	 * 查询用户最后一笔卖出信息系
	 * 
	 * @param userId
	 * @return
	 */
	Sell queryUserLastSell(Long userId);

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
	int updateUserSellWhenLock(@Param("userId") Long userId, @Param("nowDate") Date nowDate);

	/**
	 * 当解锁一个用户处理的购买数据
	 * 
	 * @param userId
	 * @param nowDate
	 * @return
	 */
	int updateUserSellWhenUnLock(@Param("userId") Long userId, @Param("nowDate") Date nowDate);
}