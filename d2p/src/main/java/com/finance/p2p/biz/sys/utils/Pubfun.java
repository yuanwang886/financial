package com.finance.p2p.biz.sys.utils;

import java.util.Calendar;
import java.util.Date;

import com.finance.p2p.biz.sys.utils.Const.MD5Key;
import com.finance.p2p.biz.sys.utils.Const.TimeKey;
import com.finance.p2p.biz.sys.utils.Const.TypeKey;
import com.framework.encry.MD5;
import com.framework.utils.DateUtil;

public class Pubfun {

	/**
	 * 比较对应类型, 如果是预付款，那么是12小时打款， 如果是余额款那么是24小时，夜间不算，所以
	 * 
	 * @param date
	 * @param type
	 * @return
	 */
	public static Date calLockTime(Date date, Integer type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		if (type.equals(TypeKey.TYPE_2)) {
			calendar.add(Calendar.HOUR, TimeKey.HOUR_12);
		}
		return calendar.getTime();
	}

	/**
	 * 获取当天的时间 0点0分,貌似代码会出现1秒的情况，那么我们就初始化为1秒
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTodayTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 1);
		return calendar.getTime();
	}

	/**
	 * 系统密码加密方式
	 * 
	 * @param phone
	 * @param password
	 * @return
	 */
	public static String encryptMD5(String phone, String password) {
		return MD5.MD5Encode(phone + Const.MD5Key.KEY + password);
	}

	/**
	 * 短信对应的key
	 * 
	 * @param smsTemp
	 * @param phone
	 * @return
	 */
	public static String getSMSKey(Integer smsTemp, String phone) {
		return smsTemp + MD5Key.KEY + phone;
	}

	public static void main(String a[]) {

		System.out
				.println(DateUtil.formatDate(calLockTime(DateUtil.formatDate("2017-06-26 14:00:45"), TypeKey.TYPE_2)));
		
		System.out
		.println(Pubfun.getTodayTime(new Date()));

	}
}
