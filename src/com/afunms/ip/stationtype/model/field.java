package com.afunms.ip.stationtype.model;

import com.afunms.common.base.BaseVo;

public class field extends BaseVo {
	private int id;
	private String name;
	private String running;
	private int backbone_id;
	private int flag;
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
	public String getRunning() {
		return running;
	}
	public void setRunning(String running) {
		this.running = running;
	}
	public int getBackbone_id() {
		return backbone_id;
	}
	public void setBackbone_id(int backbone_id) {
		this.backbone_id = backbone_id;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	

	

}
