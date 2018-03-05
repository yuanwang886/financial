package com.framework.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PubFun {

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * 
	 * @param type
	 * @param map
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 */
	public static Object convertMap(Class<?> type, Map<?, ?> map)
			throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
		Object obj = type.newInstance(); // 创建 JavaBean 对象

		// 给 JavaBean 对象的属性赋值
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				// 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
				Object value = map.get(propertyName);

				Object[] args = new Object[1];
				args[0] = value;

				descriptor.getWriteMethod().invoke(obj, args);
			}
		}
		return obj;
	}

	/**
	 * 将一个 JavaBean 对象转化为一个 Map
	 * 
	 * @param bean
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Map<String, Object> convertBean(Object bean)
			throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Class<? extends Object> type = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);

		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

	/**
	 * 校验手机号码的正确格式
	 * 
	 * @param phone
	 * @return Boolean
	 */
	public static boolean checkPhone(String phone) {
		String reg = "^(12|13|14|15|17|18)\\d{9}$";
		Pattern pattern = Pattern.compile(reg);
		Matcher m = pattern.matcher(phone);
		if (m.find()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 随机生成数字
	 */
	private static final Random radom = new Random();

	/**
	 * 获取指定位数的随机数
	 * 
	 * @param bit
	 * @return
	 */
	public static final String getRandom(int bit) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bit; i++) {
			int next = radom.nextInt(10);
			sb.append(next);
		}
		return sb.toString();
	}

	public static final int getRandomRange(int num) {
		return radom.nextInt(num);
	}

	public static final int getRangeRandom(int max) {
		int s = radom.nextInt(max);
		return s;
	}

	/**
	 * 隐藏手机号中间4位
	 * 
	 * @param username
	 * @return
	 */
	public static String dealPhone(String username) {
		if (username != null && username.length() == 11 && username.startsWith("1")) {
			// 根据这个我们判定为手机号
			StringBuilder sb = new StringBuilder(username);
			sb.replace(3, 7, "****");
			username = sb.toString();
		}
		return username;
	}
}
