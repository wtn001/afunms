package com.bpm.system.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.system.service.SystemService;
/**
 * 
 * Description:删除用户信息
 * EditMenuAction.java Create on 2012-10-23
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
@Controller
@Scope("prototype")
public class UserDelAction extends BaseAction {

	private String checkbox[];
	private String result;
	@Resource
	private SystemService systemService;
	
	@Override
	public String execute() throws Exception {
		
		return result = systemService.deleteUsers(checkbox);
	}

	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	public String[] getCheckbox() {
		return checkbox;
	}


	public void setCheckbox(String[] checkbox) {
		this.checkbox = checkbox;
	}
    
	

	
	
	
}
