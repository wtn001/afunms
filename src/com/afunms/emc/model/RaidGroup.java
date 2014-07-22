package com.afunms.emc.model;

import java.util.List;
//
//import com.afunms.emc.util.DefragPriority;

import com.afunms.common.base.BaseVo;

public class RaidGroup extends BaseVo{
	private String id;
	private String nodeid;
	private String rid;
	private String type;			//����
	private String[] state;			//״̬
	private List<String> disksList;		//�����б�
	private List<String> lunsList;		//lun�б�
	private String maxNumDisk;		//�����������
	private String maxNumLun;		//LUN�������
	private String rawCapacity;		//ԭʼ������Block)
	private String logicalCapacity;	//�߼�������Block)	
	private String freeCapacity;	//����������Block)	
	private String defragPriority;	//�ؽ����ȼ������졢�ߡ��С��ͣ�	
	
	private String stateStr;
	private String disklistStr;
	private String lunlistStr;
	
	public String getStateStr() {
		return stateStr;
	}
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public String getDisklistStr() {
		return disklistStr;
	}
	public void setDisklistStr(String disklistStr) {
		this.disklistStr = disklistStr;
	}
	public String getLunlistStr() {
		return lunlistStr;
	}
	public void setLunlistStr(String lunlistStr) {
		this.lunlistStr = lunlistStr;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String[] getState() {
		return state;
	}
	public void setState(String[] state) {
		this.state = state;
	}


	public String getMaxNumDisk() {
		return maxNumDisk;
	}
	public void setMaxNumDisk(String maxNumDisk) {
		this.maxNumDisk = maxNumDisk;
	}
	public String getMaxNumLun() {
		return maxNumLun;
	}
	public void setMaxNumLun(String maxNumLun) {
		this.maxNumLun = maxNumLun;
	}
	public String getRawCapacity() {
		return rawCapacity;
	}
	public void setRawCapacity(String rawCapacity) {
		this.rawCapacity = rawCapacity;
	}
	public String getLogicalCapacity() {
		return logicalCapacity;
	}
	public void setLogicalCapacity(String logicalCapacity) {
		this.logicalCapacity = logicalCapacity;
	}
	public String getFreeCapacity() {
		return freeCapacity;
	}
	public void setFreeCapacity(String freeCapacity) {
		this.freeCapacity = freeCapacity;
	}
	public String getDefragPriority() {
		return defragPriority;
	}
	public void setDefragPriority(String defragPriority) {
		this.defragPriority = defragPriority;
	}
	public List<String> getDisksList() {
		return disksList;
	}
	public void setDisksList(List<String> disksList) {
		this.disksList = disksList;
	}
	public List<String> getLunsList() {
		return lunsList;
	}
	public void setLunsList(List<String> lunsList) {
		this.lunsList = lunsList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	
	
}
