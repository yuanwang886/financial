package com.finance.p2p.dao;

import com.finance.p2p.entity.Account;

public interface AccountMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}