package com.afunms.ip.stationtype.model;

import com.afunms.common.base.BaseVo;

public class ip_bussinesstype extends BaseVo {

	private int id;
	private String busname;
	private String buskind;
	private String segment;//Íø¶Î
	private String gateway;//Íø¹Ø
	private String encryption;
	private int field_id;
	private String vlan_ip;
	private String vlan;
	private int backbone_id;
	private String flag;
	private String ip_use;

	public String getIp_use() {
		return ip_use;
	}
	public void setIp_use(String ip_use) {
		this.ip_use = ip_use;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBusname() {
		return busname;
	}
	public void setBusname(String busname) {
		this.busname = busname;
	}
	public String getBuskind() {
		return buskind;
	}
	public void setBuskind(String buskind) {
		this.buskind = buskind;
	}
	public String getSegment() {
		return segment;
	}
	public void setSegment(String segment) {
		this.segment = segment;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getEncryption() {
		return encryption;
	}
	public void setEncryption(String encryption) {
		this.encryption = encryption;
	}
	public int getField_id() {
		return field_id;
	}
	public void setField_id(int field_id) {
		this.field_id = field_id;
	}
	public String getVlan_ip() {
		return vlan_ip;
	}
	public void setVlan_ip(String vlan_ip) {
		this.vlan_ip = vlan_ip;
	}
	public String getVlan() {
		return vlan;
	}
	public void setVlan(String vlan) {
		this.vlan = vlan;
	}

	public int getBackbone_id() {
		return backbone_id;
	}
	public void setBackbone_id(int backbone_id) {
		this.backbone_id = backbone_id;
	}


	
	
}
