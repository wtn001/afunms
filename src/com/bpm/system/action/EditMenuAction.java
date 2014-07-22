package com.bpm.system.action;


import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.bpm.system.service.SystemService;
/**
 * 
 * Description:编辑菜单
 * EditMenuAction.java Create on 2012-10-23 下午3:28:27 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
@Controller
@Scope("prototype")
public class EditMenuAction extends BaseAction{
	
	private String result;//处理结果
	private int edit_menu_id;//菜单ID
	private String edit_menu_name;//菜单名称
	private String edit_menu_url;//菜单响应地址
	private int edit_parent_id;//父菜单地址
	private int del_menu_id;//需要删除的记录
	@Resource
	private SystemService systemService;
	
	@Override
	public String execute() throws Exception {
		result = systemService.editSecMenu(edit_menu_id, edit_menu_name, edit_menu_url, edit_parent_id,del_menu_id);
		return SUCCESS;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getEdit_menu_id() {
		return edit_menu_id;
	}

	public void setEdit_menu_id(int edit_menu_id) {
		this.edit_menu_id = edit_menu_id;
	}

	public String getEdit_menu_name() {
		return edit_menu_name;
	}

	public void setEdit_menu_name(String edit_menu_name) {
		this.edit_menu_name = edit_menu_name;
	}

	public String getEdit_menu_url() {
		return edit_menu_url;
	}

	public void setEdit_menu_url(String edit_menu_url) {
		this.edit_menu_url = edit_menu_url;
	}

	public int getEdit_parent_id() {
		return edit_parent_id;
	}

	public void setEdit_parent_id(int edit_parent_id) {
		this.edit_parent_id = edit_parent_id;
	}

	public int getDel_menu_id() {
		return del_menu_id;
	}

	public void setDel_menu_id(int del_menu_id) {
		this.del_menu_id = del_menu_id;
	}

	
	
}
