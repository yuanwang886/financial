package com.framework.jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 缓存操作
 * 
 * @author Administrator
 *
 */
public class JedisManager {

	private JedisPool jedisPool;
	private int dbIndex = 0;

	private Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = getJedisPool().getResource();
		} catch (Exception e) {
			throw new JedisConnectionException(e);
		}
		return jedis;
	}

	private void returnResource(Jedis jedis) {
		if (jedis == null)
			return;
		jedis.close();
	}

	/**
	 * 返回所有的key，可以采用通配符
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Set<String> getKeys(final String key) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			Set<String> run(Jedis jedis) {
				return jedis.keys(key);
			}
		}.run();
	}

	/**
	 * redis存储字符串
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	public void setValue(final String key, final String value) throws Exception {
		new JedisOperate() {
			@Override
			<T> T run(Jedis jedis) {
				jedis.set(key, value);
				return null;
			}
		}.run();
	}

	/**
	 * redis存储map
	 * 
	 * @param key
	 * @param map
	 * @throws Exception
	 */
	public void setMap(final String key, final Map<String, String> map) throws Exception {
		new JedisOperate() {
			@Override
			<T> T run(Jedis jedis) {
				jedis.hmset(key, map);
				return null;
			}
		}.run();
	}

	/**
	 * map添加数据
	 * 
	 * @param key
	 * @param field
	 * @param value
	 * @throws Exception
	 */
	public void putMapValue(final String key, final String field, final String value) throws Exception {
		new JedisOperate() {
			@Override
			<T> T run(Jedis jedis) {
				jedis.hset(key, field, value);
				return null;
			}
		}.run();
	}

	/**
	 * 获取map中的值
	 * 
	 * @param key
	 *            map对应的key
	 * @param fields
	 *            map中的属性名
	 * @return
	 * @throws Exception
	 */
	public List<String> getMapValues(final String key, final String... fields) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			List<String> run(Jedis jedis) {
				return jedis.hmget(key, fields);
			}
		}.run();
	}

	/**
	 * 获取map中指定的值
	 * 
	 * @param key
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public String getMapValue(final String key, final String field) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			String run(Jedis jedis) {
				return jedis.hget(key, field);
			}
		}.run();
	}

	/**
	 * 删除map中指定的值
	 * 
	 * @param key
	 * @param field
	 * @throws Exception
	 */
	public void delMapValue(final String key, final String field) throws Exception {
		new JedisOperate() {
			@Override
			<T> T run(Jedis jedis) {
				jedis.hdel(key, field);
				return null;
			}
		}.run();
	}

	/**
	 * 为map中 key中的域 field 的值加上增量 increment
	 * 
	 * @param key
	 * @param field
	 * @param increment
	 * @return
	 * @throws Exception
	 */
	public Long incrementMapValue(final String key, final String field, final Long increment) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			Long run(Jedis jedis) {
				return jedis.hincrBy(key, field, increment);
			}
		}.run();
	}

	/**
	 * 判断map中 某个值是否存在
	 * 
	 * @param key
	 * @param field
	 * @return
	 * @throws Exception
	 */
	public Boolean mapValueIsExists(final String key, final String field) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			Boolean run(Jedis jedis) {
				return jedis.hexists(key, field);
			}
		}.run();
	}

	/**
	 * 判断key是否存在
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public Boolean isExists(final String key) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			Boolean run(Jedis jedis) {
				return jedis.exists(key);
			}
		}.run();
	}

	/**
	 * redis存储字符串
	 * 
	 * @param key
	 * @param value
	 * @param expireTime
	 *            过期时间（秒）
	 * @return
	 * @throws Exception
	 */
	public void setValue(final String key, final String value, final int expireTime) throws Exception {
		new JedisOperate() {
			@Override
			<T> T run(Jedis jedis) {
				jedis.set(key, value);
				if (expireTime > 0)
					jedis.expire(key, expireTime);
				return null;
			}
		}.run();
	}

	/**
	 * redis获取字符串
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String getValue(final String key) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			String run(Jedis jedis) {
				return jedis.get(key);
			}
		}.run();
	}

	/**
	 * 删除redis缓存
	 * 
	 * @param key
	 * @throws Exception
	 */
	public void deleteByKey(final String key) throws Exception {
		new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			Long run(Jedis jedis) {
				return jedis.del(key);
			}
		}.run();
	}

	public byte[] getValueByKey(byte[] key) throws Exception {
		Jedis jedis = null;
		byte[] result = null;
		try {
			jedis = getJedis();
			jedis.select(dbIndex);
			result = jedis.get(key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	public void deleteByKey(byte[] key) throws Exception {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(dbIndex);
			jedis.del(key);
		} catch (Exception e) {
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public void saveValueByKey(byte[] key, byte[] value, int expireTime) throws Exception {
		Jedis jedis = null;
		try {
			jedis = getJedis();
			jedis.select(dbIndex);
			jedis.set(key, value);
			if (expireTime > 0)
				jedis.expire(key, expireTime);
		} catch (Exception e) {
			throw e;
		} finally {
			returnResource(jedis);
		}
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public void incr(final String key) throws Exception {
		new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			Long run(Jedis jedis) {
				return jedis.incr(key);
			}
		}.run();
	}

	public void lpush(final String key, final String... strings) throws Exception {
		new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			Long run(Jedis jedis) {
				return jedis.lpush(key, strings);
			}
		}.run();
	}

	public String lindex(final String key, final long index) throws Exception {
		return new JedisOperate() {
			@SuppressWarnings("unchecked")
			@Override
			String run(Jedis jedis) {
				return jedis.lindex(key, index);
			}
		}.run();
	}

	public abstract class JedisOperate {

		public <T> T run() throws Exception {
			Jedis jedis = null;
			T result = null;
			try {
				jedis = getJedis();
				jedis.select(dbIndex);
				result = run(jedis);
			} catch (Exception e) {
				throw e;
			} finally {
				returnResource(jedis);
			}
			return result;
		}

		abstract <T> T run(Jedis jedis);
	}
}
