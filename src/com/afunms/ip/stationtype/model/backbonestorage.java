package com.afunms.ip.stationtype.model;

import com.afunms.common.base.BaseVo;

public class backbonestorage extends BaseVo {

	private int id;
	private String backbone;
	private int type;
	private int backbone_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBackbone() {
		return backbone;
	}
	public void setBackbone(String backbone) {
		this.backbone = backbone;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getBackbone_id() {
		return backbone_id;
	}
	public void setBackbone_id(int backbone_id) {
		this.backbone_id = backbone_id;
	}
}
