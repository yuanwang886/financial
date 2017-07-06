package com.finance.p2p.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.biz.sys.bean.InternalBean;
import com.finance.p2p.biz.user.bean.Team.Person;
import com.finance.p2p.entity.User;

public interface UserMapper {
	int deleteByPrimaryKey(Long userId);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Long userId);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	// 以下为新增sql
	User selectByPhone(@Param("phone") String phone);

	// 查询用户基本统计信息
	List<Person> getUserTeamInfo(String phone);

	// 统计下面的直接人员
	Integer selectDirectNum(@Param("phone") String phone);

	// 统计下面的团队人员
	Integer selectSubordinateNum(@Param("userId") String userId, @Param("state") Integer state);

	// 查询上级用户基本统计信息
	Person getSuperiorInfo(String phone);

	// 冻结48小时未购买的用户
	int updateUserStateBy48Hour(@Param("nowDate") Date nowDate);

	// 查询在这个时间点被锁定的用户
	List<Long> selectLockUserList(@Param("nowDate") Date nowDate);

	// 查询已经交易完成的用户
	int userSellStateTask(@Param("nowDate") Date nowDate);

	// 根据条件查询用户列表
	List<User> getLockUserList(Map<String, Object> condition);

	// 查询所有的用户信息
	List<User> getAllUserList(@Param("userId") Long userId);

	// 管理员查询内置人员
	List<InternalBean> getInternalUserList(Map<String, Object> condition);
	
	// 根据条件查询所有用户列表
	List<User> getUserList(Map<String, Object> condition);
}