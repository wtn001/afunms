package com.bpm.process.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.process.model.ProcessStatisticalsModel;
import com.bpm.process.service.ProcessService;
import com.bpm.system.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 流程统计
 * @author ywx
 * 2013-5-30
 */

@Controller
@Scope("prototype")
public class ProInsStatisticalsDetailAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7664418744082109469L;
	private String type;//统计方式，1待处理，2待签收，3本周已办，4本周已完成
	@Resource
	private ProcessService processService;
	private List<ProcessStatisticalsModel> list;
	private String pieXml;
	private String barXml;
	@Override
	public String execute() throws Exception {
		if(StringUtil.isBlank(type)) type="1";
		list = processService.queryProInsStatDetail(type);
		pieXml=processService.getProInsPieXml(list);
		barXml=processService.getProInsBarXml(list);
		return SUCCESS;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	public List<ProcessStatisticalsModel> getList() {
		return list;
	}


	public void setList(List<ProcessStatisticalsModel> list) {
		this.list = list;
	}


	public String getPieXml() {
		return pieXml;
	}


	public void setPieXml(String pieXml) {
		this.pieXml = pieXml;
	}


	public String getBarXml() {
		return barXml;
	}


	public void setBarXml(String barXml) {
		this.barXml = barXml;
	}


}
