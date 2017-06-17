package com.finance.p2p.biz.sys.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.bean.RCBean;
import com.finance.p2p.biz.sys.utils.Const.USERKey;
import com.finance.p2p.dao.ReleaseCodeMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.entity.ReleaseCode;
import com.finance.p2p.entity.User;
import com.framework.SysConst.YesOrNO;
import com.framework.exception.BusinessException;
import com.framework.utils.IdWorker;
import com.framework.utils.PubFun;

@Service
public class ReleaseCodeService {

	@Autowired
	private ReleaseCodeMapper releaseCodeMapper;

	@Autowired
	private UserMapper userMapper;

	public Object submit(String phone, Integer num) {

		User user = userMapper.selectByPhone(phone);
		if (user == null) {
			throw new BusinessException("当前购买用户在系统中未注册");
		}
		if (user.getState().equals(USERKey.LOCK)) {
			throw new BusinessException("当前购买用户在系统中已经被锁定");
		}
		List<ReleaseCode> list = new ArrayList<>();

		for (int i = 0; i < num; i++) {
			ReleaseCode rc = new ReleaseCode();

			rc.setId(IdWorker.getId());
			rc.setUserId(user.getUserId());
			rc.setCode(PubFun.getRandom(6));
			rc.setState(YesOrNO.NO);
			Date nowDate = new Date();
			rc.setCreateTime(nowDate);
			rc.setModifyTime(nowDate);
			
			list.add(rc);
		}
		releaseCodeMapper.insertByBatch(list);
		return new BaseData();
	}
	
	
	public List<RCBean> getRCRecordList(Map<String, Object> condition) {
		return releaseCodeMapper.getRCRecordList(condition);
	}
}
