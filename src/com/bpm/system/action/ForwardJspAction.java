package com.bpm.system.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.system.utils.StringUtil;
/**
 * 
 * Description:用于页面转跳
 * ForwardJspAction.java Create on 2012-10-23 上午9:40:43 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */

@Controller
@Scope("prototype")
public class ForwardJspAction extends BaseAction {

	private String jsp;
	
	
	
	@Override
	public String execute() throws Exception {
		jsp = StringUtil.isBlank(jsp)?"error":jsp;
		return jsp;
	}



	public String getJsp() {
		return jsp;
	}



	public void setJsp(String jsp) {
		this.jsp = jsp;
	}
	
	
}
