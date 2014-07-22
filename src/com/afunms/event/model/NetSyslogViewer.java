/**
 * @author zhangys
 * @project afunms
 * @date 2011-07
 */
package com.afunms.event.model;

import com.afunms.common.base.BaseVo;

public class NetSyslogViewer extends BaseVo{
	private long id;
	private String status;//���״̬
	private int category;//�豸����
	private String hostName;//������
	private String ipaddress;//IP��ַ
	private int errors;//������
	private int warnings;//������
	private int failures;//ʧ����
	private int others;//����
	private int all;//ȫ��

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public int getErrors() {
		return errors;
	}
	public void setErrors(int errors) {
		this.errors = errors;
	}
	public int getWarnings() {
		return warnings;
	}
	public void setWarnings(int warnings) {
		this.warnings = warnings;
	}
	public int getFailures() {
		return failures;
	}
	public void setFailures(int failures) {
		this.failures = failures;
	}
	public int getOthers() {
		return others;
	}
	public void setOthers(int others) {
		this.others = others;
	}
	public int getAll() {
		return all;
	}
	public void setAll(int all) {
		this.all = all;
	}
	public long getId() {
		return id;
	}
	public void setId(long l) {
		id = l;
	}
	public String getIpaddress(){
		return ipaddress;
	}
	public void setIpaddress(String ipaddress){
		this.ipaddress=ipaddress;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getCategory() {
		return category;
	}
}
