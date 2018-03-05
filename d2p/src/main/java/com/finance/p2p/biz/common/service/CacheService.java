package com.finance.p2p.biz.common.service;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framework.exception.BusinessException;
import com.framework.jedis.JedisManager;

/**
 * 缓存处理
 * 
 * @author administrator
 *
 */
@Service
public class CacheService {

	@Autowired
	private JedisManager jedisManager;

	public void setValue(String type, String key, String value, int expireTime) {
		try {
			jedisManager.setValue(type + key, value, expireTime);
		} catch (Exception e) {
			throw new BusinessException("缓存服务器出现错误");
		}
	}

	public void setValue(String type, String key, String value) {
		// 不设置或修改过期时间
		this.setValue(type, key, value, -1);
	}

	public void delete(String type, String key) {
		try {
			jedisManager.deleteByKey(type + key);
		} catch (Exception e) {
			throw new BusinessException("缓存服务器出现错误");
		}
	}

	/**
	 * 根据key直接删除
	 * 
	 * @param key
	 */
	public void delete(String key) {
		try {
			jedisManager.deleteByKey(key);
		} catch (Exception e) {
			throw new BusinessException("缓存服务器出现错误");
		}
	}

	public void deleteBatch(String type) {
		try {
			Set<String> set = jedisManager.getKeys(type + "*");
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				jedisManager.deleteByKey(key);
			}
		} catch (Exception e) {
			throw new BusinessException("缓存服务器出现错误");
		}
	}

	public String get(String type, String key) {
		try {
			return jedisManager.getValue(type + key);
		} catch (Exception e) {
			return null;

		}
	}

	public void incr(String type, String key) {
		try {
			jedisManager.incr(type + key);
		} catch (Exception e) {
			throw new BusinessException("缓存服务器出现错误");
		}
	}

	public Set<String> getKeys(String type) {
		try {
			return jedisManager.getKeys(type);
		} catch (Exception e) {
			return null;
		}
	}

	public void setMap(String key, Map<String, String> map) {
		try {
			jedisManager.setMap(key, map);
		} catch (Exception e) {

		}
	}

	public String getMapValue(final String key, final String field) throws Exception {
		try {
			return jedisManager.getMapValue(key, field);
		} catch (Exception e) {
			return null;
		}
	}

	public void putMapValue(String key, String field, String value) {
		try {
			jedisManager.putMapValue(key, field, value);
		} catch (Exception e) {

		}
	}

	public Boolean isExists(final String key) {
		try {
			return jedisManager.isExists(key);
		} catch (Exception e) {

		}
		return false;
	}
}
