package com.afunms.emc.model;

import com.afunms.common.base.BaseVo;

public class Disk extends BaseVo{
	private String name;// Bus 0 Enclosure 0 Disk 0
	private String rid;
	private String did;
	private String id;
	private String nodeid;
	private String rgid;//������RAID��ID
	private String revision;// �汾��
	private String lun; // ����������LUN���
	private String type; // ����
	private String state; // ״̬
	private String hotSpare; // �ȱ���״̬
	private String prctRebuilt; // �����ؽ��ٷֱ�
	private String prctBound; // ���̰󶨰ٷֱ�
	private String serialNumber; // ���к�
	private String capacity; // ����
	private String hardReadErrors; // Ӳ����������
	private String hardWriteErrors; // Ӳ��д������
	private String softReadErrors; // �����������
	private String softWriteErrors; // Ӳ��д������
	private String numberofReads; // ��������
	private String numberofWrites; // д������
	private String numberofLuns; // �����˴��̵�LUN��
	private String raidGroupID; // RAID����
	private String kbytesRead; // ��KB
	private String kbytesWritten; // дKB
	private String driveType; // ������������
	private String idleTicks; // ��״̬ʱ����
	private String busyTicks; // æ״̬ʱ����
	private String currentSpeed; // ���̵�ǰ�ٶ�
	private String maximumSpeed; // ���̿����е�����ٶ�
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getLun() {
		return lun;
	}
	public void setLun(String lun) {
		this.lun = lun;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getHotSpare() {
		return hotSpare;
	}
	public void setHotSpare(String hotSpare) {
		this.hotSpare = hotSpare;
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
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public String getHardReadErrors() {
		return hardReadErrors;
	}
	public void setHardReadErrors(String hardReadErrors) {
		this.hardReadErrors = hardReadErrors;
	}
	public String getHardWriteErrors() {
		return hardWriteErrors;
	}
	public void setHardWriteErrors(String hardWriteErrors) {
		this.hardWriteErrors = hardWriteErrors;
	}
	public String getSoftReadErrors() {
		return softReadErrors;
	}
	public void setSoftReadErrors(String softReadErrors) {
		this.softReadErrors = softReadErrors;
	}
	public String getSoftWriteErrors() {
		return softWriteErrors;
	}
	public void setSoftWriteErrors(String softWriteErrors) {
		this.softWriteErrors = softWriteErrors;
	}
	public String getNumberofReads() {
		return numberofReads;
	}
	public void setNumberofReads(String numberofReads) {
		this.numberofReads = numberofReads;
	}
	public String getNumberofWrites() {
		return numberofWrites;
	}
	public void setNumberofWrites(String numberofWrites) {
		this.numberofWrites = numberofWrites;
	}
	public String getNumberofLuns() {
		return numberofLuns;
	}
	public void setNumberofLuns(String numberofLuns) {
		this.numberofLuns = numberofLuns;
	}
	public String getRaidGroupID() {
		return raidGroupID;
	}
	public void setRaidGroupID(String raidGroupID) {
		this.raidGroupID = raidGroupID;
	}
	public String getKbytesRead() {
		return kbytesRead;
	}
	public void setKbytesRead(String kbytesRead) {
		this.kbytesRead = kbytesRead;
	}
	public String getKbytesWritten() {
		return kbytesWritten;
	}
	public void setKbytesWritten(String kbytesWritten) {
		this.kbytesWritten = kbytesWritten;
	}
	public String getDriveType() {
		return driveType;
	}
	public void setDriveType(String driveType) {
		this.driveType = driveType;
	}
	public String getIdleTicks() {
		return idleTicks;
	}
	public void setIdleTicks(String idleTicks) {
		this.idleTicks = idleTicks;
	}
	public String getBusyTicks() {
		return busyTicks;
	}
	public void setBusyTicks(String busyTicks) {
		this.busyTicks = busyTicks;
	}
	public String getCurrentSpeed() {
		return currentSpeed;
	}
	public void setCurrentSpeed(String currentSpeed) {
		this.currentSpeed = currentSpeed;
	}
	public String getMaximumSpeed() {
		return maximumSpeed;
	}
	public void setMaximumSpeed(String maximumSpeed) {
		this.maximumSpeed = maximumSpeed;
	}
	public String getRgid() {
		return rgid;
	}
	public void setRgid(String rgid) {
		this.rgid = rgid;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getDid() {
		return did;
	}
	public void setDid(String did) {
		this.did = did;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	
}
