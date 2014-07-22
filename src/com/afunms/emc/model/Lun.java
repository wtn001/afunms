package com.afunms.emc.model;

import java.util.List;

import com.afunms.common.base.BaseVo;

public class Lun extends BaseVo{
	private String id;
	private String nodeid;
	private String rgid;
	private String name; // 名称
	private String RAIDType; // RAID类型
	private String RAIDGroupID; // 所属的RAID组ID
	private String State; // 状态
	private String currentOwner; // 当前所有者
	private String defaultOwner; // 默认所有者
	private String writecache; // 是否写缓存
	private String readcache; // 是否读缓存
	private String prctRebuilt; // 磁盘重建百分比
	private String prctBound; // 磁盘绑定百分比
	private String LUNCapacity; // 容量（MB）
	private List<String> disksList; // 绑定的磁盘列表
	private String disklistStr;
	private int totalHardErrors; // 硬件错误总数
	private int totalSoftErrors; // 软件错误总数
	private int totalQueueLength; // 队列总长度
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRAIDType() {
		return RAIDType;
	}
	public void setRAIDType(String type) {
		RAIDType = type;
	}
	public String getRAIDGroupID() {
		return RAIDGroupID;
	}
	public void setRAIDGroupID(String groupID) {
		RAIDGroupID = groupID;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getCurrentOwner() {
		return currentOwner;
	}
	public void setCurrentOwner(String currentOwner) {
		this.currentOwner = currentOwner;
	}
	public String getDefaultOwner() {
		return defaultOwner;
	}
	public void setDefaultOwner(String defaultOwner) {
		this.defaultOwner = defaultOwner;
	}
	public String getWritecache() {
		return writecache;
	}
	public void setWritecache(String writecache) {
		this.writecache = writecache;
	}
	public String getReadcache() {
		return readcache;
	}
	public void setReadcache(String readcache) {
		this.readcache = readcache;
	}
	public String getPrctRebuilt() {
		return prctRebuilt;
	}
	public void setPrctRebuilt(String prctRebuilt) {
		this.prctRebuilt = prctRebuilt;
	}
	public String getPrctBound() {
		return prctBound;
	}
	public void setPrctBound(String prctBound) {
		this.prctBound = prctBound;
	}
	public String getLUNCapacity() {
		return LUNCapacity;
	}
	public void setLUNCapacity(String capacity) {
		LUNCapacity = capacity;
	}
	public List<String> getDisksList() {
		return disksList;
	}
	public void setDisksList(List<String> disksList) {
		this.disksList = disksList;
	}
	public int getTotalHardErrors() {
		return totalHardErrors;
	}
	public void setTotalHardErrors(int totalHardErrors) {
		this.totalHardErrors = totalHardErrors;
	}
	public int getTotalSoftErrors() {
		return totalSoftErrors;
	}
	public void setTotalSoftErrors(int totalSoftErrors) {
		this.totalSoftErrors = totalSoftErrors;
	}
	public int getTotalQueueLength() {
		return totalQueueLength;
	}
	public void setTotalQueueLength(int totalQueueLength) {
		this.totalQueueLength = totalQueueLength;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRgid() {
		return rgid;
	}
	public void setRgid(String rgid) {
		this.rgid = rgid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	public String getDisklistStr() {
		return disklistStr;
	}
	public void setDisklistStr(String disklistStr) {
		this.disklistStr = disklistStr;
	}
	public String getNodeid() {
		return nodeid;
	}

}
