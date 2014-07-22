package com.bpm.design.action;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.base.JspPage;
import com.bpm.design.dao.DesignDao;
import com.bpm.design.model.DesignTempModel;
import com.bpm.system.action.BaseAction;
/**
 * 
 * Description:获取流程设计文件的列表
 * QueryDesignAction.java Create on 2012-11-9 上午10:36:19 
 * @author hexinlin
 * Copyright (c) 2012 DHCC Company,Inc. All Rights Reserved.
 */
@Controller
@Scope("prototype")
public class QueryDesignAction extends BaseAction {

	private String perpagenum="";//每页需要显示的记录数
	private JspPage jsppage=new JspPage();//分页查询
	private String jp="";  //当前页
	
	@Override
	public String execute() throws Exception {
			jsppage.setCurrentPage(jp);//设置当前页面参数
			jsppage.setPerPage(perpagenum);//设置每页需要获取的记录数
			jsppage = new DesignDao().queryDesign(jsppage.getCurrentPage(),jsppage.getPerPage());
			return SUCCESS;
	}

	public String getPerpagenum() {
		return perpagenum;
	}

	public void setPerpagenum(String perpagenum) {
		this.perpagenum = perpagenum;
	}

	public JspPage getJsppage() {
		return jsppage;
	}

	public void setJsppage(JspPage jsppage) {
		this.jsppage = jsppage;
	}

	public String getJp() {
		return jp;
	}

	public void setJp(String jp) {
		this.jp = jp;
	}
	
}
