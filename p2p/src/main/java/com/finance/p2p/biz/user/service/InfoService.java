package com.finance.p2p.biz.user.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.p2p.biz.common.bean.BaseData;
import com.finance.p2p.biz.sms.service.SMSService;
import com.finance.p2p.biz.sys.utils.Pubfun;
import com.finance.p2p.biz.sys.utils.Const.SMSKey;
import com.finance.p2p.biz.user.bean.Info;
import com.finance.p2p.dao.AccountMapper;
import com.finance.p2p.dao.UserMapper;
import com.finance.p2p.entity.Account;
import com.finance.p2p.entity.User;

@Service
public class InfoService {

	@Autowired
	private AccountMapper accountMapper;

	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private SMSService sMSService;

	/**
	 * 获取用户信息
	 * 
	 * @param user
	 * @return
	 */
	public Object info(User user) {

		Info info = new Info();

		info.setAccount(account(user));
		info.setUser(user);
		return info;
	}

	/**
	 * 获取用户账户信息
	 * 
	 * @param user
	 * @return
	 */
	public Account account(User user) {
		return accountMapper.selectByPrimaryKey(user.getUserId());
	}

	/**
	 * 提交用户账户信息
	 * 
	 * @param user
	 * @param account
	 * @return
	 */
	public BaseData submit(User user, Account account) {

		account.setUserId(user.getUserId());
		Date date = new Date();
		Account data = accountMapper.selectByPrimaryKey(user.getUserId());
		if (data == null) {
			account.setCreateTime(date);
			account.setModifyTime(date);
			accountMapper.insertSelective(account);
		} else {
			account.setModifyTime(date);
			accountMapper.updateByPrimaryKeySelective(account);
		}
		return new BaseData();
	}

	/**
	 * 提交用户支付密码信息
	 * 
	 * @param user
	 * @param account
	 * @return
	 */
	public BaseData paySubmit(User user, String password, String code) {
		
		sMSService.verify(user.getPhone(), SMSKey.TEMP_1003, code);
		
		password = Pubfun.encryptMD5(user.getPhone(), password);
		user.setPayPassword(password);
		
		User u = new User();
		u.setUserId(user.getUserId());
		u.setPayPassword(password);
		u.setModifyTime(new Date());
		userMapper.updateByPrimaryKeySelective(u);
		return new BaseData();
	}
}
