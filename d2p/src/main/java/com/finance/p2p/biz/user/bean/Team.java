package com.finance.p2p.biz.user.bean;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 我的团队信息
 * 
 * @author Administrator
 *
 */
public class Team {

	/** 真实姓名 */
	private String realName;

	/** 团队规模数 */
	private Integer subordinateNum;

	/** 推广人员数 */
	private Integer popularizeNum;

	/** 锁定人员数 */
	private Integer lockingNum;

	/** 上级领导人 */
	private Person superior;

	/** 下级推广人 */
	private List<Person> subordinate;

	/** 总激活码数 */
	private Integer sumNum;

	/** 卖出激活码树木 */
	private Integer sellNum;

	/** 已经激活人数 */
	private Integer usedNum;

	/** 剩余激活码数 */
	private Integer lastNum;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getSubordinateNum() {
		return subordinateNum;
	}

	public void setSubordinateNum(Integer subordinateNum) {
		this.subordinateNum = subordinateNum;
	}

	public Integer getPopularizeNum() {
		return popularizeNum;
	}

	public void setPopularizeNum(Integer popularizeNum) {
		this.popularizeNum = popularizeNum;
	}

	public Integer getLockingNum() {
		return lockingNum;
	}

	public void setLockingNum(Integer lockingNum) {
		this.lockingNum = lockingNum;
	}

	public Person getSuperior() {
		return superior;
	}

	public void setSuperior(Person superior) {
		this.superior = superior;
	}

	public List<Person> getSubordinate() {
		return subordinate;
	}

	public void setSubordinate(List<Person> subordinate) {
		this.subordinate = subordinate;
	}

	public Integer getSumNum() {
		return sumNum;
	}

	public void setSumNum(Integer sumNum) {
		this.sumNum = sumNum;
	}

	public Integer getSellNum() {
		return sellNum;
	}

	public void setSellNum(Integer sellNum) {
		this.sellNum = sellNum;
	}

	public Integer getUsedNum() {
		return usedNum;
	}

	public void setUsedNum(Integer usedNum) {
		this.usedNum = usedNum;
	}

	public Integer getLastNum() {
		return lastNum;
	}

	public void setLastNum(Integer lastNum) {
		this.lastNum = lastNum;
	}

	public static class Person {

		private Long userId;

		private String realName;

		private String phone;

		private Integer state;

		private Integer activation;

		@JsonSerialize(using = ToStringSerializer.class)
		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getRealName() {
			return realName;
		}

		public void setRealName(String realName) {
			this.realName = realName;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public Integer getState() {
			return state;
		}

		public void setState(Integer state) {
			this.state = state;
		}

		public Integer getActivation() {
			return activation;
		}

		public void setActivation(Integer activation) {
			this.activation = activation;
		}

	}

}
