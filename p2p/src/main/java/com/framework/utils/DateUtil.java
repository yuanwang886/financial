package com.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	private static SimpleDateFormat sd_normal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static SimpleDateFormat sd_simple = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 根据日期新增几年
	 * 
	 * @param date
	 * @param year
	 * @return
	 * @throws Exception
	 */
	public static Date dateAddYear(Date date, Integer year) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.YEAR, year);
		return calendar.getTime();
	}

	/**
	 * 根据日期新增天数
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date dateAddDay(Date date, Integer day) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 * 根据日期增加小时
	 * 
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date dateAddHour(Date date, Integer hour) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, hour);
		return calendar.getTime();
	}

	/**
	 * 从第二天开始计算
	 * 
	 * @param date
	 * @return
	 */
	public static Date dateBySecond(Date date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 日期格式化
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return sd_normal.format(date);
	}

	public static Date formatDate(String date) {
		try {
			return sd_normal.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 日期简单格式化
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateSimple(Date date) {
		return sd_simple.format(date);
	}

	public static Date formatDateSimple(String date) {
		try {
			return sd_simple.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
