package com.bpm.process.action;
/**
 *  Description:
 * 根据taskId,提交表单请求处理（同意或者驳回任务）
 * @author ywx
 *
 */
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.process.service.ProcessService;
import com.bpm.system.utils.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class TaskSubmitAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9221093632819847152L;
	@Resource
	private ProcessService processService;
	private String taskId;
	private String day;
	private String reason;
	private String type;
	private String result;
	private String backActivityId;
	@Override
	public String execute() throws Exception {
		System.out.println(day+reason+type);
		if(!StringUtil.isBlank(result)&& result.equals("同意"))
		{
			Map map = new HashMap();
			map.put("day",day);
			map.put("reason",reason);
			map.put("type",type);
			processService.taskComplete(taskId, map);
		}
		else {
			//processService.backProcess(taskId, backActivityId);
		}
		
		return SUCCESS;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getBackActivityId() {
		return backActivityId;
	}
	public void setBackActivityId(String backActivityId) {
		this.backActivityId = backActivityId;
	}


}
