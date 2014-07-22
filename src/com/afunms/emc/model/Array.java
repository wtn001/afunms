package com.afunms.emc.model;

import com.afunms.common.base.BaseVo;

public class Array extends BaseVo{
	private String status;
	private String presentWatts;
	private String averagewatts;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPresentWatts() {
		return presentWatts;
	}
	public void setPresentWatts(String presentWatts) {
		this.presentWatts = presentWatts;
	}
	public String getAveragewatts() {
		return averagewatts;
	}
	public void setAveragewatts(String averagewatts) {
		this.averagewatts = averagewatts;
	}
	
}
