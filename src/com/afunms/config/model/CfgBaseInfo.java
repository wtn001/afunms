package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

public class CfgBaseInfo extends BaseVo{
private int id;
private String policyName;//��������
private String name;      //������
private String value;     //ֵ
private String priority;  //����
private String type;      //����
private String collecttime;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public String getPolicyName() {
	return policyName;
}
public void setPolicyName(String policyName) {
	this.policyName = policyName;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getPriority() {
	return priority;
}
public void setPriority(String priority) {
	this.priority = priority;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public String getCollecttime() {
	return collecttime;
}
public void setCollecttime(String collecttime) {
	this.collecttime = collecttime;
}

}
