package com.afunms.device.model;

import com.afunms.common.base.BaseVo;

public class Cabinet extends BaseVo{
/**
	 * 
	 */
	private static final long serialVersionUID = 9037106395148670649L;
private int id;
private String city;//����
private String roadInfo;//·����Ϣ
private String affiliation;//��������
private String belong;//����Ͻ��
private String latitude;//����
private String longitude;//γ��
private String ipaddress;//ip��ַ
private String rackNumber;//������
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getRoadInfo() {
	return roadInfo;
}
public void setRoadInfo(String roadInfo) {
	this.roadInfo = roadInfo;
}
public String getAffiliation() {
	return affiliation;
}
public void setAffiliation(String affiliation) {
	this.affiliation = affiliation;
}
public String getBelong() {
	return belong;
}
public void setBelong(String belong) {
	this.belong = belong;
}

public String getLatitude() {
	return latitude;
}
public void setLatitude(String latitude) {
	this.latitude = latitude;
}
public String getLongitude() {
	return longitude;
}
public void setLongitude(String longitude) {
	this.longitude = longitude;
}
public String getIpaddress() {
	return ipaddress;
}
public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
}
public String getRackNumber() {
	return rackNumber;
}
public void setRackNumber(String rackNumber) {
	this.rackNumber = rackNumber;
}

}
