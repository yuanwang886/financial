package com.finance.p2p.dao;

import com.finance.p2p.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;

public interface RoleMenuMapper {
    int deleteByPrimaryKey(@Param("roleId") Long roleId, @Param("menuId") Long menuId);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);
}