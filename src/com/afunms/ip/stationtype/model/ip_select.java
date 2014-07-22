package com.afunms.ip.stationtype.model;

import com.afunms.common.base.BaseVo;

public class ip_select extends BaseVo {
	
	private int id;
	private String name;//厂站名
	private String ids;//骨干接入点id,用逗号分隔
	private String routeName;//路由器厂家
	private String routeType;//路由器型号
	private String s1Name;//路由器台数
	private String s1Type;//交换机厂家
	private String s2Type;//交换机型号
	private String s2Name;//交换机台数
    private String deviceType;//其他网络设备类型
    private String deviceCount;//其他网络设备类型台数
    private String hasCabinet;//有无机柜，0：无；1：有
    private String location;//地点
    private String debugUnit;//调试单位
    private String flag;//是否已处理 0：未处理，1：已处理
    private String rFactory;
    private String s1Factory;
    private String s2Factory;
	public String getRFactory() {
		return rFactory;
	}
	public void setRFactory(String factory) {
		rFactory = factory;
	}
	public String getS1Factory() {
		return s1Factory;
	}
	public void setS1Factory(String factory) {
		s1Factory = factory;
	}
	public String getS2Factory() {
		return s2Factory;
	}
	public void setS2Factory(String factory) {
		s2Factory = factory;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getRouteType() {
		return routeType;
	}
	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}
	
	public String getS1Name() {
		return s1Name;
	}
	public void setS1Name(String name) {
		s1Name = name;
	}
	public String getS1Type() {
		return s1Type;
	}
	public void setS1Type(String type) {
		s1Type = type;
	}
	public String getS2Type() {
		return s2Type;
	}
	public void setS2Type(String type) {
		s2Type = type;
	}
	public String getS2Name() {
		return s2Name;
	}
	public void setS2Name(String name) {
		s2Name = name;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceCount() {
		return deviceCount;
	}
	public void setDeviceCount(String deviceCount) {
		this.deviceCount = deviceCount;
	}
	public String getHasCabinet() {
		return hasCabinet;
	}
	public void setHasCabinet(String hasCabinet) {
		this.hasCabinet = hasCabinet;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDebugUnit() {
		return debugUnit;
	}
	public void setDebugUnit(String debugUnit) {
		this.debugUnit = debugUnit;
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

    
    

}
