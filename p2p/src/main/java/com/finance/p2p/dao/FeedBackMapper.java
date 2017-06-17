package com.finance.p2p.dao;

import java.util.List;
import java.util.Map;

import com.finance.p2p.entity.FeedBack;

public interface FeedBackMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FeedBack record);

    int insertSelective(FeedBack record);

    FeedBack selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FeedBack record);

    int updateByPrimaryKey(FeedBack record);
    
    /**
     * 查询所有的反馈意见
     * @return
     */
    List<FeedBack> getFeedbackList(Map<String, Object> condition);
}