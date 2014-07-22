/**
 * <p>Description:mapping table NMS_POSITION</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.event.model;

import java.util.Calendar;

import com.afunms.common.base.BaseVo;

public class NetSyslogEvent extends BaseVo{
	private Long id;
	private String ipaddress;//IP��ַ
	private String hostname;//������
	private String message;//����Ϣ����
	private int facility;//�¼���Դ
	private int priority;//���ȼ�
	private String facilityName;//�¼���Դ����
	private String priorityName;//���ȼ�����
	private int processId;
	private String processName;
	private String processIdStr;
	private Calendar recordtime;//ʱ���
	private String username;//�¼���Դ
	private int eventid;  //�豸����
	
	
	/** nullable persistent field */
	private String begin_date;

	/** nullable persistent field */
	private String end_date;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long l) {
		id = l;
	}
	
	public void setFacility(int facility){
		this.facility=facility;
	}
	public void setPriority(int priority){
		this.priority=priority;
	}
	public void setHostname(String hostname){
		this.hostname=hostname;
	}
	public void setRecordtime(Calendar recordtime){
		this.recordtime=recordtime;
	}
	public void setMessage(String message){
		this.message=message;
	}
	public void setFacilityName(String facilityName){
		this.facilityName=facilityName;
	}
	public void setPriorityName(String priorityName){
		this.priorityName=priorityName;
	}
	
	public int getFacility(){
		return facility;
	}
	public int getPriority(){
		return priority;
	}
	public String getHostname(){
		return hostname;
	}
	public Calendar getRecordtime(){
		return recordtime;
	}
	public String getMessage(){
		return message;
	}
	public String getFacilityName(){
		return facilityName;
	}
	public String getPriorityName(){
		return priorityName;
	}
	public String getIpaddress(){
		return ipaddress;
	}
	public void setIpaddress(String ipaddress){
		this.ipaddress=ipaddress;
	}
	public int getProcessId() {
		return processId;
	}
	public void setProcessId(int processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessIdStr() {
		return processIdStr;
	}
	public void setProcessIdStr(String processIdStr) {
		this.processIdStr = processIdStr;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getEventid() {
		return eventid;
	}
	public void setEventid(int eventid) {
		this.eventid = eventid;
	}
}
