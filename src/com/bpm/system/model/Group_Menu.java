package com.bpm.system.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.stereotype.Component;
/**
 * 
 * Description:用户组与菜单之间的映射，用于权限管理。
 * Group_Menu.java Create on 2012-10-21 下午6:44:20 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
@Component
@Entity(name="group_menu")
public class Group_Menu {

	private String priKey;//主键
	private String groupId;//用户组ID
	private int menu_id;//菜单ID
	@Id
	public String getPriKey() {
		return priKey;
	}
	public void setPriKey(String priKey) {
		this.priKey = priKey;
	}
	@Column(length=32)
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	
	
	
	
	
}
