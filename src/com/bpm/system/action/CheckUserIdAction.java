package com.bpm.system.action;


import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.system.service.SystemService;
/**
 * 
 * Description:校验用户账号是否存在
 * EditMenuAction.java Create on 2012-10-23
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
@Controller
@Scope("prototype")
public class CheckUserIdAction extends BaseAction {

	private String result;//结果
	private String userId;//用户账号
	@Resource
	private SystemService systemService;
	
	@Override
	public String execute() throws Exception {
		
		result = null==systemService.getUserById(userId)?"success":"error";
		
		
		return SUCCESS;
	}

	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
