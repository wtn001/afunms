package com.afunms.ip.stationtype.model;

import com.afunms.common.base.BaseVo;

public class stationstorage extends BaseVo {

	private int id;
	private String station;
	private int type;
	private int station_id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getStation_id() {
		return station_id;
	}
	public void setStation_id(int station_id) {
		this.station_id = station_id;
	}
}
