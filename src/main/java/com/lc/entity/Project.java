package com.lc.entity;

import java.sql.Date;

import com.lc.utils.StateUtils;


/*
@Author  DELL
@Desc    项目的实体类
@Date    2020年4月2日
*/
public class Project {
	private Integer id;
	private String name;
	private Integer userId;
	private Integer state;
	private Integer type;
	private Integer targetMoney;
	private Integer currentMoney;
	private Date createTime;
	private Date endTime;

	private String remark;
	
	//这个属性供视图view-model使用
	private String stateName;

	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", userId=" + userId + ", state=" + state + ", type=" + type
				+ ", targetMoney=" + targetMoney + ", currentMoney=" + currentMoney + ", createTime=" + createTime
				+ ", endTime=" + endTime + ",remark="+remark+"]";
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
		this.stateName = StateUtils.getStateName(state);
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTargetMoney() {
		return targetMoney;
	}

	public void setTargetMoney(Integer targetMoney) {
		this.targetMoney = targetMoney;
	}

	public Integer getCurrentMoney() {
		return currentMoney;
	}

	public void setCurrentMoney(Integer currentMoney) {
		this.currentMoney = currentMoney;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName() {
	
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
}
