package com.finance.p2p.dao;

import java.util.List;
import java.util.Map;

import com.finance.p2p.biz.sys.bean.RCBean;
import com.finance.p2p.entity.ReleaseCode;

public interface ReleaseCodeMapper {
	int deleteByPrimaryKey(Long id);

	int insert(ReleaseCode record);

	int insertSelective(ReleaseCode record);

	ReleaseCode selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ReleaseCode record);

	int updateByPrimaryKey(ReleaseCode record);

	/**
	 * 批量提交
	 * 
	 * @param list
	 * @return
	 */
	int insertByBatch(List<ReleaseCode> list);
	
	/**
	 * 查询用户购买记录
	 * @param condition
	 * @return
	 */
	List<RCBean> getRCRecordList(Map<String, Object> condition);
}