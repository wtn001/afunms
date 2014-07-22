package com.bpm.process.action;
/**
 *  Description:
 * ����taskId,�ύ��������ͬ����߲�������
 * @author ywx
 *
 */
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.util.SessionConstant;
import com.afunms.system.model.User;
import com.bpm.process.service.ProcessService;
import com.bpm.system.utils.ConstanceUtil;
import com.bpm.system.utils.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("prototype")
public class TaskFormKeySubmitAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9039024333469979953L;
	@Resource
	private ProcessService processService;
	private String taskId;
	private String taskExtId;
	private String taskFormType;
	private String result;
	private String backActivityId;
	private String processInstanceId;
	private String afterActivityId;
	private HttpServletRequest request;
	private String ordersolution;
	private String orderwarntype;//�澯������ping
	private Map<String,Object> session;
	private String ordertype;//�ڵ�������host
	private String ordernodetype;//�ڵ����ϵͳ����
	
	@Override
	public String execute() throws Exception {
		if(result.equals("����"))
		{
			request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
			String reason=request.getParameter("reject");
			processService.backProcess(taskId, backActivityId,reason);
		}
		else if(result.equals("���"))
		{
		 if(result.equals(ConstanceUtil.PRO_HALF_MESSAGE))
		{
			if(ConstanceUtil.FORM_WRITE.equals(taskFormType)) {
				Map map = new HashMap();
				request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
				Enumeration<String> en = request.getParameterNames();
				String tempkey = "";
				while(en.hasMoreElements()) {
				   tempkey = en.nextElement();
				   map.put(tempkey, request.getParameter(tempkey));
				}
				map.put("reject", null);
				processService.afterProcess(taskId, afterActivityId,map,ConstanceUtil.FORM_WRITE);
				if(StringUtil.isNotBlank(ordersolution)) {
					session=ActionContext.getContext().getSession();
					User user=(User)session.get(SessionConstant.CURRENT_USER);
					processService.stroeOrderSolution(ordernodetype, ordersolution, orderwarntype,ordertype ,user.getUserid());
				}
			}
			
			else if(ConstanceUtil.FORM_READ_WRITE.equals(taskFormType)) {
				Map map = new HashMap();
				request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
				Enumeration<String> en = request.getParameterNames();
				String tempkey = "";
				while(en.hasMoreElements()) {
				   tempkey = en.nextElement();
				   map.put(tempkey, request.getParameter(tempkey));
				}
				map.put("reject", null);
				processService.afterProcess(taskId, afterActivityId,map,ConstanceUtil.FORM_READ_WRITE);
				if(StringUtil.isNotBlank(ordersolution)) {
					session=ActionContext.getContext().getSession();
					User user=(User)session.get(SessionConstant.CURRENT_USER);
					processService.stroeOrderSolution(ordernodetype, ordersolution, orderwarntype,ordertype ,user.getUserid());
				}
			}
			else {
				processService.afterProcess(taskId, afterActivityId,null,ConstanceUtil.FORM_READ);
			}
			
		}
		}
		else if(ConstanceUtil.FORM_READ.equals(taskFormType))//����
		{
			processService.taskFormKeyComplete(taskId,taskExtId);
			
		}else if(ConstanceUtil.FORM_READ_WRITE.equals(taskFormType)) {//����д
			//��ȡ��Ҫд�����Ϣ
			Map map = new HashMap();
			request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
			Enumeration<String> en = request.getParameterNames();
			String tempkey = "";
			while(en.hasMoreElements()) {
			   tempkey = en.nextElement();
			   map.put(tempkey, request.getParameter(tempkey));
			}
			map.put("reject", null);
			map.put("isbanjie", "1");
			processService.taskFormKeyComplete(taskId, taskExtId, map);
			if(StringUtil.isNotBlank(ordersolution)) {
				session=ActionContext.getContext().getSession();
				User user=(User)session.get(SessionConstant.CURRENT_USER);
				processService.stroeOrderSolution(ordernodetype, ordersolution, orderwarntype,ordertype ,user.getUserid());
			}
			
		}
		else if(ConstanceUtil.FORM_WRITE.equals(taskFormType))//��д
		{
			Map map = new HashMap();
			request = (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
			Enumeration<String> en = request.getParameterNames();
			String tempkey = "";
			while(en.hasMoreElements()) {
			   tempkey = en.nextElement();
			   map.put(tempkey, request.getParameter(tempkey));
			}
			map.put("reject", null);
			map.put("isbanjiebutton", ConstanceUtil.Process_BANJIE);//0�ǲ���Ҫ��ᰴť
			map.put("ordersolution", null);
			processService.taskFormKeyComplete(taskId, map);
			if(StringUtil.isNotBlank(ordersolution)) {
				session=ActionContext.getContext().getSession();
				User user=(User)session.get(SessionConstant.CURRENT_USER);
				processService.stroeOrderSolution(ordernodetype, ordersolution, orderwarntype,ordertype ,user.getUserid());
			}
		}
		else {
			return "error";
		}
		
		return SUCCESS;
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
	public String getTaskExtId() {
		return taskExtId;
	}
	public void setTaskExtId(String taskExtId) {
		this.taskExtId = taskExtId;
	}
	public String getTaskFormType() {
		return taskFormType;
	}
	public void setTaskFormType(String taskFormType) {
		this.taskFormType = taskFormType;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getAfterActivityId() {
		return afterActivityId;
	}
	public void setAfterActivityId(String afterActivityId) {
		this.afterActivityId = afterActivityId;
	}

	public String getOrdersolution() {
		return ordersolution;
	}

	public void setOrdersolution(String ordersolution) {
		this.ordersolution = ordersolution;
	}

	
	public String getOrderwarntype() {
		return orderwarntype;
	}

	public void setOrderwarntype(String orderwarntype) {
		this.orderwarntype = orderwarntype;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public String getOrdernodetype() {
		return ordernodetype;
	}

	public void setOrdernodetype(String ordernodetype) {
		this.ordernodetype = ordernodetype;
	}
	
}

