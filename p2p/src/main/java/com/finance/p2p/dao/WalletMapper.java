package com.finance.p2p.dao;

import java.util.List;

import com.finance.p2p.biz.sys.bean.MainBean;
import com.finance.p2p.entity.Wallet;

public interface WalletMapper {
	int deleteByPrimaryKey(Long userId);

	int insert(Wallet record);

	int insertSelective(Wallet record);

	Wallet selectByPrimaryKey(Long userId);

	int updateByPrimaryKeySelective(Wallet record);

	int updateByPrimaryKey(Wallet record);

	/**
	 * 查询出所有钱包余额大于0的人
	 * 
	 * @return
	 */
	List<Long> selectWalletBig();

	/**
	 * 统计数据
	 * 
	 * @return
	 */
	MainBean statistics();

	/**
	 * 除了可提现金额外都是更新
	 * 
	 * @param record
	 * @return
	 */
	int updateUserWallet(Wallet record);

	/**
	 * 这里还是采用更新制度
	 * 
	 * @param record
	 * @return
	 */
	int updateWallet(Wallet record);
}