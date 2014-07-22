package com.bpm.process.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.process.model.ProcessStatisticalsModel;
import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionSupport;
/**
 * 流程统计
 * @author ywx
 * 2012-12-19
 */

@Controller
@Scope("prototype")
public class ProcessStatisticalsListAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1860809232823690861L;
	private String type;//参与方式 (任务和实例方式)，0任务，1实例
	private String person;//部门和人员方式，0人员，1岗位
	private String startdate;
	private String todate;
	private String imgname;
	
	@Resource
	private ProcessService processService;
	private List<ProcessStatisticalsModel> list;
	private String pieXml;
	private String barXml;
	@Override
	public String execute() throws Exception {
		list = processService.processStatisticals(type,person,startdate,todate);
		pieXml=processService.getPieXml(list,person);
		barXml=processService.getBarXml(list,person);
		//ActionContext.getContext().getSession().put("listPsm", list);
		return SUCCESS;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getTodate() {
		return todate;
	}


	public void setTodate(String todate) {
		this.todate = todate;
	}


	public String getImgname() {
		return imgname;
	}


	public void setImgname(String imgname) {
		this.imgname = imgname;
	}


	public String getPieXml() {
		return pieXml;
	}


	public List<ProcessStatisticalsModel> getList() {
		return list;
	}


	public void setList(List<ProcessStatisticalsModel> list) {
		this.list = list;
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
