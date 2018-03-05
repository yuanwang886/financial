package com.finance.p2p.biz.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.utils.Const.StateKey;
import com.finance.p2p.biz.sys.utils.Const.TimeKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.dao.BuyMapper;
import com.finance.p2p.dao.PNoticeMapper;
import com.finance.p2p.dao.SellMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.entity.Buy;
import com.finance.p2p.entity.PNotice;
import com.finance.p2p.entity.User;
import com.framework.exception.BusinessException;
import com.framework.utils.DateUtil;
import com.framework.utils.IdWorker;

@Service
public class LockService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BuyMapper buyMapper;

	@Autowired
	private SellMapper sellMapper;

	@Autowired
	private PNoticeMapper pNoticeMapper;

	/**
	 * 管理员根据条件人员列表
	 * 
	 * @return
	 */
	public List<User> getLockUserList(Map<String, Object> condition) {
		return userMapper.getLockUserList(condition);
	}

	@Transactional
	public Object unLockUserByUserId(Long userId) {

		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			throw new BusinessException("解锁用户不存在,请联系管理员");
		}
		Date date = new Date();

		user.setState(USERKey.UNLOCK);
		user.setLockTime(null);
		user.setModifyTime(date);
		// 判断用户是否存在未打款的数据
		Buy buy = buyMapper.queryUserLastBuy(userId);
		if (buy == null) {
			// 说明用户就没有买过单子, 其实这里可以吧paytime设置到2天以后进行校验，那么xml文件里面的 sql也需要进行修改了
			user.setPayTime(DateUtil.dateAddDay(date, TimeKey.DAY_2));
		} else if (buy.getState().equals(StateKey.STATE_9)) {
			user.setPayTime(DateUtil.dateAddYear(date, TimeKey.USER_PAYTIME));
		}
		userMapper.updateByPrimaryKey(user);

		// 更新用户的卖出记录
		buyMapper.updateUserBuyWhenUnLock(userId, date);
		sellMapper.updateUserSellWhenUnLock(userId, date);

		// 对于管理员操作解锁也需要进行记录
		PNotice notice = new PNotice();
		notice.setId(IdWorker.getId());
		notice.setUserId(userId);
		notice.setTitle("您已经被系统管理员解锁");
		notice.setContent("您已经被系统管理员手动解锁。原因：请联系管理员");
		notice.setCreateTime(date);
		notice.setModifyTime(date);
		pNoticeMapper.insert(notice);

		return new BaseData();
	}
}
