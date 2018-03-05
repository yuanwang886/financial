package com.finance.p2p.dao;

import java.util.List;

import com.finance.p2p.entity.Menu;

public interface MenuMapper {
	int deleteByPrimaryKey(Long menuId);

	int insert(Menu record);

	int insertSelective(Menu record);

	Menu selectByPrimaryKey(Long menuId);

	int updateByPrimaryKeySelective(Menu record);

	int updateByPrimaryKey(Menu record);

	// 查询用户菜单组
	List<Menu> getMenuList(Integer role);
}