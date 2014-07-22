package com.afunms.emc.model;

import java.util.Map;

import com.afunms.common.base.BaseVo;

public class Crus extends BaseVo{
	/**
	 * 
	 */
	private String nodeid;
	private String name;
	private String spStateStr;
	private String powerState;
	private String busLCC;
	private String bussps;
	private String busCabling;
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpStateStr() {
		return spStateStr;
	}
	public void setSpStateStr(String spStateStr) {
		this.spStateStr = spStateStr;
	}
	public String getPowerState() {
		return powerState;
	}
	public void setPowerState(String powerState) {
		this.powerState = powerState;
	}
	public String getBusLCC() {
		return busLCC;
	}
	public void setBusLCC(String busLCC) {
		this.busLCC = busLCC;
	}
	public String getBussps() {
		return bussps;
	}
	public void setBussps(String bussps) {
		this.bussps = bussps;
	}
	public String getBusCabling() {
		return busCabling;
	}
	public void setBusCabling(String busCabling) {
		this.busCabling = busCabling;
	}
	
	
//	private String lccStateStr;
//	private String cablingStateStr;
//	private Map<String,String> spState;
//	private Map<String,String> lccState;
//	private Map<String,String> cablingState;
	
	
	
}
