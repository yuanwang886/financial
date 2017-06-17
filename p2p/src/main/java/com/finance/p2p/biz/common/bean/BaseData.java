package com.finance.p2p.biz.common.bean;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.framework.SysConst;
import com.framework.SysConst.YesOrNO;
import com.framework.utils.JsonMapper;

/**
 * 
 * @author Administrator
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseData {

	private int result = SysConst.YesOrNO.YES;

	private String desc = "";

	private Object data;

	public BaseData() {
	}

	public BaseData(int result, String desc) {
		this.result = result;
		this.desc = desc;
	}

	public BaseData setError(String errorMsg) {
		this.desc = errorMsg;
		this.result = YesOrNO.NO;
		return this;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getResult() {
		return result;
	}

	public String toJson() {
		return JsonMapper.toJsonString(this);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object object) {
		this.data = object;
	}

	public void setData(String name, Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(name, object);
		this.data = map;
	}

	public BaseData(String name, Object object) {
		this.setData(name, object);
	}

	public BaseData(Object object) {
		this.setData(object);
	}
}
