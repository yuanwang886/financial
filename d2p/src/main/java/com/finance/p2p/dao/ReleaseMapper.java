package com.finance.p2p.dao;

import com.finance.p2p.entity.Release;

public interface ReleaseMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(Release record);

    int insertSelective(Release record);

    Release selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(Release record);

    int updateByPrimaryKey(Release record);
    
    
    //采用sql更新
    int update(Release record);
}