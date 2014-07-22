package com.bpm.system.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.system.model.Menu;
import com.bpm.system.service.SystemService;
/**
 * 
 * Description:获取菜单信息
 * MenuQueryAllAction.java Create on 2012-10-18 下午3:00:47 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
@Controller
@Scope("prototype")
public class MenuQueryAllAction extends BaseAction{

	private List<Menu> list;//菜单信息
	@Resource
	private SystemService systemService;
	
	public String execute() throws Exception {
        list = systemService.queryAllMenu();
		return SUCCESS;
	}

	public List<Menu> getList() {
		return list;
	}

	public void setList(List<Menu> list) {
		this.list = list;
	}
	
}
