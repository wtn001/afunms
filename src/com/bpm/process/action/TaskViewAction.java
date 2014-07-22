package com.bpm.process.action;
/**
 *  Description:
 * 根据taskId,查看任务
 * @author ywx
 *
 */
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.bpm.process.service.ProcessService;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class TaskViewAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1160368280474811242L;
	@Resource
	private ProcessService processService;
	private String taskId;
	private String executionId;
	private String processDefinitionId;
	private ActivityImpl activityImpl;
	private String processInstanceId;
	private List<HistoricTaskInstance> tasklist;
	
	@Override
	public String execute() throws Exception {
		System.out.println(taskId);
		//activityImpl=processService.findActivityImplByExecutionId(processDefinitionId,executionId);
		tasklist = processService.findHistoricTaskInstance(processInstanceId);
		return SUCCESS;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getExecutionId() {
		return executionId;
	}

	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public ActivityImpl getActivityImpl() {
		return activityImpl;
	}

	public void setActivityImpl(ActivityImpl activityImpl) {
		this.activityImpl = activityImpl;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public List<HistoricTaskInstance> getTasklist() {
		return tasklist;
	}

	public void setTasklist(List<HistoricTaskInstance> tasklist) {
		this.tasklist = tasklist;
	}
	
	

}
