package com.finance.p2p.dao;

import org.apache.ibatis.annotations.Param;

import com.finance.p2p.entity.UserRole;

public interface UserRoleMapper {
    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int insert(UserRole record);

    int insertSelective(UserRole record);
}