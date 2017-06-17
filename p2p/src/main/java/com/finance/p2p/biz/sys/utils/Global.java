package com.finance.p2p.biz.sys.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.framework.utils.PropertiesLoader;

public class Global {

	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = new HashMap<>();

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("config/sysconfig.properties");

	/**
	 * 上传文件基础虚拟路径
	 */
	public static final String USERFILES_BASE_URL = "/userfiles/";

	/**
	 * 获取配置
	 * 
	 * @see ${fns:getConfig('adminPath')}
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null) {
			value = loader.getProperty(key);
			map.put(key, value != null ? value : StringUtils.EMPTY);
		}
		return value;
	}

	/**
	 * 获取管理端根路径
	 */
	public static String getDomainPath() {
		return getConfig("domain_url");
	}
	
	public static String getJobSwitch() {
		return getConfig("job_switch");
	}
	
	public static String getReleaseUser() {
		return getConfig("release_user");
	}
}
