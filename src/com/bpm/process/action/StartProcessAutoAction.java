package com.bpm.process.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.afunms.common.util.SessionConstant;
import com.afunms.system.model.User;
import com.bpm.process.service.ProcessService;
import com.bpm.system.utils.ConstanceUtil;
import com.bpm.system.utils.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
/**
 * ͨ���澯��Ϣ���ɸ澯��Ϣ���̵�
 * @author HXL
 * 2011-12-11
 */
@Controller
@Scope("prototype")
public class StartProcessAutoAction extends ActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5995736972568443273L;
	private String nodeId;//�ڵ�ID
	private String content;//�澯����
	private String subtype;//����
	private String subentity;//�澯����
	private String level;//�澯����
	private String processId;//����ID
	private Map session;//session����
	private String message;//������
	private String eventid;//�����Ƿ����ɶ���
	private String manual;//�Ƿ�Ϊ�ֶ���������.ֵΪ1��ʾΪ�ֶ���д�������̡�
	//private String result;
	@Resource
	private ProcessService processService;
	
	@Override
	public String execute() throws Exception {
		if(StringUtil.isBlank(processId)) return ERROR;
		session=ActionContext.getContext().getSession();
		User user=(User)session.get(SessionConstant.CURRENT_USER);
		
		if(user==null) return ERROR;
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("isbanjiebutton", ConstanceUtil.NO_Process_BANJIE);//0�ǲ���Ҫ��ᰴť
		/*map.put("processId",processId );
        map.put("userId", user.getUserid());
		map.put("nodeId", nodeId);
		map.put("content", content);
		map.put("subtype", subtype);
		map.put("subentity", subentity);
		map.put("level", level);
		map.put("ostype", processService.getNodeOstypeById(nodeId));*/

		/*if("1".equals(manual)) {
			//result=processService.isStart(processId,user.getUserid());
			map.put("userId", user.getUserid());
			map.put("warnprocess", "false");
		}else {*/
			map.put("processId",processId );
			map.put("userId", user.getUserid());
			map.put("nodeId", nodeId);
			map.put("content", content);
			map.put("subtype", subtype);
			map.put("subentity", subentity);
			map.put("level", level);
			map.put("ostype", processService.getNodeOstypeById(nodeId));
			map.put("warnprocess", "true");
		//}
		message = processService.startProcessAuto(processId, map);
		if(StringUtil.isNotBlank(eventid))
		{
			processService.setEventOrderFlag(eventid);
		}
		return SUCCESS;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getSubentity() {
		return subentity;
	}

	public void setSubentity(String subentity) {
		this.subentity = subentity;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public ProcessService getProcessService() {
		return processService;
	}

	public void setProcessService(ProcessService processService) {
		this.processService = processService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
	}

	public String getManual() {
		return manual;
	}

	public void setManual(String manual) {
		this.manual = manual;
	}

	
	
}

