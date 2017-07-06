package com.finance.p2p.biz.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.utils.Const.StateKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.dao.BusinessRecordMapper;
import com.finance.p2p.dao.BuyMapper;
import com.finance.p2p.dao.PNoticeMapper;
import com.finance.p2p.dao.SellMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.entity.BusinessRecord;
import com.finance.p2p.entity.PNotice;
import com.finance.p2p.entity.Sell;
import com.finance.p2p.entity.User;
import com.framework.exception.BusinessException;
import com.framework.utils.IdWorker;

@Service
public class AllUserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BuyMapper buyMapper;

	@Autowired
	private SellMapper sellMapper;

	@Autowired
	private BusinessRecordMapper businessRecordMapper;

	@Autowired
	private PNoticeMapper pNoticeMapper;

	/**
	 * 管理员根据条件人员列表
	 * 
	 * @return
	 */
	public List<User> getUserList(Map<String, Object> condition) {
		return userMapper.getUserList(condition);
	}

	/**
	 * 锁定一个用户
	 * 
	 * @param userId
	 * @return
	 */
	@Transactional
	public Object lockUserByUserId(Long userId) {

		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			throw new BusinessException("锁定用户不存在,请联系管理员");
		}

		Date date = new Date();
		user.setState(USERKey.LOCK);
		user.setLockTime(date);
		user.setModifyTime(date);

		List<BusinessRecord> brList = businessRecordMapper.getUserRecordList(userId);
		for (BusinessRecord br : brList) {
			// 修改卖家金额
			Sell sell = sellMapper.selectByPrimaryKey(br.getSellId());
			sell.setMatchMoney(sell.getMatchMoney().add(br.getMoney()));
			sell.setState(StateKey.STATE_0);
			sell.setModifyTime(date);
			sellMapper.updateByPrimaryKeySelective(sell);
		}
		// 寿险锁定当前用户
		userMapper.updateByPrimaryKey(user);
		// 删除用户所有的匹配记录
		businessRecordMapper.deleteUserRecordWhenLock(userId);
		// 跟新没完成的买入
		buyMapper.updateUserBuyWhenLock(userId, date);
		// 更新用户所有的卖出
		sellMapper.updateUserSellWhenLock(userId, date);

		// 通知用户被锁定了
		PNotice notice = new PNotice();
		notice.setId(IdWorker.getId());
		notice.setUserId(userId);
		notice.setTitle("您已经被系统管理员锁定");
		notice.setContent("您已经被系统管理员锁定。原因：请联系管理员");
		notice.setCreateTime(date);
		notice.setModifyTime(date);
		pNoticeMapper.insert(notice);
		
		// 既然用户已经冻结，暂时我们继续放在利息计算池中，方便统计
		// incomeBuyMapper.deleteUserAllList(br.getBuyAccountId());
		return new BaseData();
	}
}
