package com.bpm.process.action;

/**
 * Description:
 * ����ʵ���б���ʾ�Ѿ������˵�����
 * @author ywx
 *
 */

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class ProcessInstanceListAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5972501637221022347L;
	@Resource
	private ProcessService processService;
	private List<ProcessInstance> piList;
	
	@Override
	public String execute() throws Exception {
		piList=processService.findPiList();
		return SUCCESS;
	}

	public List<ProcessInstance> getPiList() {
		return piList;
	}

	public void setPiList(List<ProcessInstance> piList) {
		this.piList = piList;
	}



}
