package com.bpm.process.model;
/**
 * ��ʷ����ʵ��MODEL�����ڴ�������
 * @author HXL
 * 2012-12-13
 */
public class HistoryProcessInstanceModel {

	
	private String startUser;//������
	private String status;//����״̬
	private String startTime;//�����¼�
	private String processInstanceId;//����ʵ��ID
	private String processDefinitionId;//���̶���ID
	public String getStartUser() {
		return startUser;
	}
	public void setStartUser(String startUser) {
		this.startUser = startUser;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}
	
	
	
}
