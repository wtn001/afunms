package com.afunms.topology.model;

public class LinkModel {
	private String id;
	private String from;//��·����ʼ����
	private String to;//��·��Ŀ������
	private String linkInfo;//��·��ʾ��Ϣ
	private String linkStatus;//��·״̬
	private Float linkWeight;//��·�ߵĿ��
	private String linkMenuInfo;//��·�˵���Ϣ
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getLinkInfo() {
		return linkInfo;
	}
	public void setLinkInfo(String linkInfo) {
		this.linkInfo = linkInfo;
	}
	public String getLinkStatus() {
		return linkStatus;
	}
	public void setLinkStatus(String linkStatus) {
		this.linkStatus = linkStatus;
	}
	public Float getLinkWeight() {
		return linkWeight;
	}
	public void setLinkWeight(Float linkWeight) {
		this.linkWeight = linkWeight;
	}
	public String getLinkMenuInfo() {
		return linkMenuInfo;
	}
	public void setLinkMenuInfo(String linkMenuInfo) {
		this.linkMenuInfo = linkMenuInfo;
	}

}
