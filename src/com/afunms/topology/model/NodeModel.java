package com.afunms.topology.model;

public class NodeModel {

	private String id;// ���

	private String fatherId;// �������

	private String name;// ��ʾ����

	private int state;// ����״̬(1=����;0=����)

	private float weight;// �ߵĴ�ϸ

	private String url;// ��ʾͼƬ

	private String deviceInfo;// ��ʾ�豸��Ϣ

	private int x;// �ڵ������

	private int y;// �ڵ�������

	private String nodeMenuInfo;// �ڵ�˵���Ϣ

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFatherId() {
		return fatherId;
	}

	public void setFatherId(String fatherId) {
		this.fatherId = fatherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getNodeMenuInfo() {
		return nodeMenuInfo;
	}

	public void setNodeMenuInfo(String nodeMenuInfo) {
		this.nodeMenuInfo = nodeMenuInfo;
	}

}
