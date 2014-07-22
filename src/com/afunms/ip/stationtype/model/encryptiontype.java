package com.afunms.ip.stationtype.model;

import com.afunms.common.base.BaseVo;

public class encryptiontype extends BaseVo {
	
	private int id;
	private String name;
    private String descr;
    private String bak;
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
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getBak() {
		return bak;
	}
	public void setBak(String bak) {
		this.bak = bak;
	}
	
}
