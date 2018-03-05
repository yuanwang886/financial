package com.finance.p2p.biz.user.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.utils.Const.ReleaseKey;
import com.finance.p2p.biz.sys.utils.Const.TimeKey;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.biz.user.bean.History;
import com.finance.p2p.biz.user.bean.Match;
import com.finance.p2p.biz.user.bean.Team;
import com.finance.p2p.biz.user.bean.Team.Person;
import com.finance.p2p.dao.BusinessRecordMapper;
import com.finance.p2p.dao.ReleaseHistoryMapper;
import com.finance.p2p.dao.ReleaseMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.entity.Release;
import com.finance.p2p.entity.ReleaseHistory;
import com.finance.p2p.entity.User;
import com.framework.SysConst.SysKey;
import com.framework.SysConst.YesOrNO;
import com.framework.exception.BusinessException;
import com.framework.utils.DateUtil;
import com.framework.utils.IdWorker;

@Service
public class TeamService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BusinessRecordMapper businessRecordMapper;

	@Autowired
	private ReleaseMapper releaseMapper;

	@Autowired
	private ReleaseHistoryMapper releaseHistoryMapper;

	/**
	 * 团队基本信息
	 * 
	 * @param user
	 * @return
	 */
	public Object info(User user) {

		Team team = new Team();

		// 上级代理人
		if (!StringUtils.isBlank(user.getInvitePhone())) {
			team.setSuperior(userMapper.getSuperiorInfo(user.getInvitePhone()));
		}

		// 这个里直接推荐人
		List<Person> list = userMapper.getUserTeamInfo(user.getPhone());
		team.setRealName(user.getRealname());
		team.setSubordinate(list);
		Integer lockingNum = 0;
		for (Person p : list) {
			if (p.getState().equals(USERKey.LOCK)) {
				lockingNum++;
			}
		}

		Integer subordinateNum = userMapper.selectSubordinateNum(String.valueOf(user.getUserId()), null);

		team.setLockingNum(lockingNum);
		team.setSubordinateNum(subordinateNum);
		team.setPopularizeNum(list.size());

		Release r = releaseMapper.selectByPrimaryKey(user.getUserId());
		if (r == null) {
			team.setSumNum(0);
			team.setSellNum(0);
			team.setUsedNum(0);
			team.setLastNum(0);
		} else {
			team.setSumNum(r.getSumNum());
			team.setSellNum(r.getSellNum());
			team.setUsedNum(r.getUsedNum());
			team.setLastNum(r.getSumNum() - r.getSellNum() - r.getUsedNum());
		}
		return team;
	}

	public Object info(User user, Integer type, Long id) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("limit", SysKey.LIMIT);
		condition.put("id", id);
		condition.put("type", type);
		condition.put("phone", user.getPhone());
		condition.put("userId", user.getUserId());
		List<Match> list = businessRecordMapper.getMatchList(condition);
		for (Match m : list) {
			switch (m.getState()) {
			case 0:
				m.setStateName("未打款");
				break;
			case 1:
				m.setStateName("已打款对方未确认");
				break;
			case 2:
				m.setStateName("收款方已确认");
				break;
			}
		}
		return new BaseData(list);
	}

	/**
	 * 激活用户
	 * 
	 * @param parent
	 * @param userId
	 * @return
	 */
	@Transactional
	public Object activation(User parent, Long userId) {
		User p = userMapper.selectByPrimaryKey(userId);
		if (p == null) {
			throw new BusinessException("激活对象不存在(A012)");
		}
		if (!StringUtils.equals(p.getInvitePhone(), parent.getPhone())) {
			throw new BusinessException("你没有权限激活该人员(A011)");
		}
		if (parent.getState().equals(USERKey.LOCK)) {
			throw new BusinessException("您当前已被锁定，暂无权限激活该人员(A010)");
		}
		Date nowDate = new Date();
		// 开始更新激活码数量
		Release releaseParent = releaseMapper.selectByPrimaryKey(parent.getUserId());
		if (releaseParent == null) {
			throw new BusinessException("您当前没有可用的激活码(A009)");
		} else {
			Integer sum = releaseParent.getSumNum() - releaseParent.getSellNum() - releaseParent.getUsedNum();
			if (sum.compareTo(1) >= 0) {
				releaseParent.setSumNum(0);
				releaseParent.setUsedNum(1);
				releaseParent.setSellNum(0);
				releaseParent.setModifyTime(nowDate);
				releaseMapper.update(releaseParent);
			} else {
				throw new BusinessException("您当前的激活码数量不够，请先购买(A008)");
			}
		}

		p.setActivation(YesOrNO.YES);
		p.setPayTime(DateUtil.dateAddDay(nowDate, TimeKey.DAY_2)); // 设置任务计划校验的时间为2天后
		p.setLockTime(null);
		p.setModifyTime(nowDate);

		// 由于值保存2级关系，所以这个地方要特殊处理下 186 186_188 186_188_189 188——189——150
		if (StringUtils.isEmpty(p.getRelation())) {
			String[] r = parent.getRelation().split(USERKey.RelationSplit);
			if (r.length == 3) {
				// 那么需要截取
				p.setRelation(r[1] + USERKey.RelationSplit + r[2] + USERKey.RelationSplit + p.getUserId());
			} else {
				p.setRelation(parent.getRelation() + USERKey.RelationSplit + p.getUserId());
			}
		}
		userMapper.updateByPrimaryKeySelective(p);

		// 增加激活码历史记录
		ReleaseHistory releaseHistory = new ReleaseHistory();
		releaseHistory.setId(IdWorker.getId());
		releaseHistory.setUserId(parent.getUserId());
		releaseHistory.setUserIdOp(userId);
		releaseHistory.setType(ReleaseKey.TYPE_1);
		releaseHistory.setNum(1);
		releaseHistory.setCreateTime(nowDate);
		releaseHistory.setModifyTime(nowDate);
		releaseHistoryMapper.insert(releaseHistory);

		return new BaseData();
	}

	/**
	 * 提交下发激活码个数
	 * 
	 * @param parent
	 * @param userId
	 * @param num
	 * @return
	 */
	@Transactional
	public Object release(User parent, Long userId, Integer num) {
		User p = userMapper.selectByPrimaryKey(userId);
		if (p == null) {
			throw new BusinessException("发放对象不存在(A007)");
		}
		if (!StringUtils.equals(p.getInvitePhone(), parent.getPhone())) {
			throw new BusinessException("你没有权限发放激活码(A006)");
		}
		if (parent.getState().equals(USERKey.LOCK)) {
			throw new BusinessException("您当前已被锁定，暂无权限激活该人员(A005)");
		}
		if (p.getState().equals(USERKey.LOCK)) {
			throw new BusinessException("发放对象已被锁定(A004)");
		}
		Date nowDate = new Date();
		Release releaseParent = releaseMapper.selectByPrimaryKey(parent.getUserId());
		if (releaseParent == null) {
			throw new BusinessException("您当前没有可用的激活码(A003)");
		} else {
			Integer sum = releaseParent.getSumNum() - releaseParent.getSellNum() - releaseParent.getUsedNum();
			if (sum.compareTo(num) >= 0) {
				releaseParent.setSumNum(0);
				releaseParent.setUsedNum(0);
				releaseParent.setSellNum(num);
				releaseParent.setModifyTime(nowDate);
				releaseMapper.update(releaseParent);
			} else {
				throw new BusinessException("您当前的激活码数量不够，请先购买(A002)");
			}
		}

		Release releaseChild = releaseMapper.selectByPrimaryKey(userId);
		if (releaseChild == null) {
			throw new BusinessException("发放对象不存在(A001)");
		} else {
			releaseChild.setSumNum(num);
			releaseChild.setUsedNum(0);
			releaseChild.setSellNum(0);
			releaseChild.setModifyTime(nowDate);
			releaseMapper.update(releaseChild);
		}

		// 增加激活码历史记录
		ReleaseHistory releaseHistory = new ReleaseHistory();
		releaseHistory.setId(IdWorker.getId());
		releaseHistory.setUserId(parent.getUserId());
		releaseHistory.setUserIdOp(userId);
		releaseHistory.setType(ReleaseKey.TYPE_2);
		releaseHistory.setNum(num);
		releaseHistory.setCreateTime(nowDate);
		releaseHistory.setModifyTime(nowDate);
		releaseHistoryMapper.insert(releaseHistory);

		return new BaseData();
	}
	
	/**
	 * 
	 * @param parent
	 * @param userId
	 * @return
	 */
	@Transactional
	public Object history(User user, Long id) {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("limit", SysKey.LIMIT);
		condition.put("id", id);
		condition.put("userId", user.getUserId());
		List<History> list = releaseHistoryMapper.selectHistoryByUserId(condition);
		return new BaseData(list);
	}
}
