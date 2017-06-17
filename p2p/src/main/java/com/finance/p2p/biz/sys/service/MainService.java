package com.finance.p2p.biz.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sys.bean.MainBean;
import com.finance.p2p.biz.sys.bean.NoticeBean;
import com.finance.p2p.dao.MenuMapper;
import com.finance.p2p.dao.PNoticeMapper;
import com.finance.p2p.dao.SNoticeMapper;
import com.finance.p2p.dao.UserTimeMapper;
import com.finance.p2p.dao.WalletMapper;
import com.finance.p2p.entity.Menu;
import com.finance.p2p.entity.User;
import com.finance.p2p.entity.UserTime;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class MainService {

	@Autowired
	private MenuMapper menuMapper;

	@Autowired
	private WalletMapper walletMapper;

	@Autowired
	private SNoticeMapper sNoticeMapper;

	@Autowired
	private PNoticeMapper pNoticeMapper;

	@Autowired
	private UserTimeMapper userTimeMapper;

	public Object getMenuList(Integer userRole) {

		List<Menu> menuList = menuMapper.getMenuList(userRole);

		return new BaseData(menuList);
	}

	public MainBean querySysData() {
		MainBean bean = walletMapper.statistics();
		return bean;
	}

	public NoticeBean countNotice(User user) {
		NoticeBean bean = new NoticeBean();

		UserTime userTime = userTimeMapper.selectByPrimaryKey(user.getUserId());
		if (userTime == null) {
			bean.setPersonMsgNum(0);
			bean.setSysMsgNum(0);
		} else {
			bean.setPersonMsgNum(pNoticeMapper.selectUnReadByDate(userTime.getpNoticeTime(), user.getUserId()));
			bean.setSysMsgNum(sNoticeMapper.selectUnReadByDate(userTime.getsNoticeTime()));
		}

		return bean;
	}
}
