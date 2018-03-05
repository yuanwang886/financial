package com.finance.p2p.dao;

import com.finance.p2p.entity.UserTime;

public interface UserTimeMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(UserTime record);

    int insertSelective(UserTime record);

    UserTime selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(UserTime record);

    int updateByPrimaryKey(UserTime record);
}