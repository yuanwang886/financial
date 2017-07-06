package com.finance.p2p.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.entity.IncomeBuy;

public interface IncomeBuyMapper {
	int deleteByPrimaryKey(Long id);

	int insert(IncomeBuy record);

	int insertSelective(IncomeBuy record);

	IncomeBuy selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(IncomeBuy record);

	int updateByPrimaryKey(IncomeBuy record);

	/**
	 * 计算用户利息,根据税率
	 */
	int calcUserIncome(@Param("nowDate") Date nowDate, @Param("rate") Double rate, @Param("day") Integer day);

	/**
	 * 吧当前用户的所有钱包明细查询出来
	 * 
	 * @return
	 */
	List<IncomeBuy> selectUserBuyList(Long userId);

	/**
	 * 删除提现的记录
	 * 
	 * @param ids
	 * @return
	 */
	int deleteUserBuyList(@Param("ids") List<Long> ids);

	/**
	 * 计算当前用户的利息
	 * 
	 * @param userId
	 * @return
	 */
	Integer calcUserInterest(@Param("nowDate") Date nowDate, @Param("userId") Long userId);

	/**
	 * 计算用户可卖出金额
	 * 
	 * @param nowDate
	 * @param userId
	 * @return
	 */
	Integer calcUserTraded(@Param("nowDate") Date nowDate, @Param("userId") Long userId, @Param("day") Integer day);

	/**
	 * 计算用户是否还存在计算利息的数据
	 * 
	 * @param userId
	 * @return
	 */
	Integer countUserUnComplate(@Param("userId") Long userId);
	
	/**
	 * 删除用户所有的购买记录
	 * @param userId
	 * @return
	 */
	//int deleteUserAllList(@Param("userId") Long userId);
}