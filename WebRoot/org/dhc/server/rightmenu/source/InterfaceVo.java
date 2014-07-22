package com.afunms.system.vo;

import java.io.Serializable;

public class InterfaceVo implements Serializable{ 
	
	private String index;
	
	private String alias;
	
	private String app;
	
	private String kbs;
	
	private String statue;
	
	private String OutBandwidthUtilHdxPerc;
	
	private String InBandwidthUtilHdxPerc;
	
	private String OutBandwidthUtilHdx;
	
	private String InBandwidthUtilHdx;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getInBandwidthUtilHdx() {
		return InBandwidthUtilHdx;
	}

	public void setInBandwidthUtilHdx(String inBandwidthUtilHdx) {
		InBandwidthUtilHdx = inBandwidthUtilHdx;
	}

	public String getInBandwidthUtilHdxPerc() {
		return InBandwidthUtilHdxPerc;
	}

	public void setInBandwidthUtilHdxPerc(String inBandwidthUtilHdxPerc) {
		InBandwidthUtilHdxPerc = inBandwidthUtilHdxPerc;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getKbs() {
		return kbs;
	}

	public void setKbs(String kbs) {
		this.kbs = kbs;
	}

	public String getOutBandwidthUtilHdx() {
		return OutBandwidthUtilHdx;
	}

	public void setOutBandwidthUtilHdx(String outBandwidthUtilHdx) {
		OutBandwidthUtilHdx = outBandwidthUtilHdx;
	}

	public String getOutBandwidthUtilHdxPerc() {
		return OutBandwidthUtilHdxPerc;
	}

	public void setOutBandwidthUtilHdxPerc(String outBandwidthUtilHdxPerc) {
		OutBandwidthUtilHdxPerc = outBandwidthUtilHdxPerc;
	}

	public String getStatue() {
		return statue;
	}

	public void setStatue(String statue) {
		this.statue = statue;
	}

	

}