package com.afunms.vmware.vim25.vo;

import com.afunms.common.base.BaseVo;

public class VMWareVO extends BaseVo {
	
	private long id;
	private String name;
	private String vid;
	private String model;//�豸�ͺ�
	private String cpunum;//cpu����
	private String netnum;//��������
	private String memory;//�ڴ�
	private String ghz;//��Ƶ
	private String hostpower;//�������Դ
	private String fullname;//�����ȫ��
	private String cpu;// �������cpu������  eg:2��*3��
	private String memoryuse;//�ڴ����
	private String vmpower;
	private String hoid;//�����vid
	private String disk;//����
	private String cpuuse;//cpuʹ��Ƶ��
	private String hostnum;//����Դ����������
	private String mem;//���ڴ�
	private String store;//�ܴ洢
	private String unusedstore;//δʹ�õĴ洢
	private String dcid;//��������Id
	private String crid;//��Ⱥid
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getCpunum() {
		return cpunum;
	}
	public void setCpunum(String cpunum) {
		this.cpunum = cpunum;
	}
	public String getNetnum() {
		return netnum;
	}
	public void setNetnum(String netnum) {
		this.netnum = netnum;
	}
	public String getMemory() {
		return memory;
	}
	public void setMemory(String memory) {
		this.memory = memory;
	}
	public String getGhz() {
		return ghz;
	}
	public void setGhz(String ghz) {
		this.ghz = ghz;
	}
	public String getHostpower() {
		return hostpower;
	}
	public void setHostpower(String hostpower) {
		this.hostpower = hostpower;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getCpu() {
		return cpu;
	}
	public void setCpu(String cpu) {
		this.cpu = cpu;
	}
	public String getMemoryuse() {
		return memoryuse;
	}
	public void setMemoryuse(String memoryuse) {
		this.memoryuse = memoryuse;
	}
	public String getVmpower() {
		return vmpower;
	}
	public void setVmpower(String vmpower) {
		this.vmpower = vmpower;
	}
	public String getHoid() {
		return hoid;
	}
	public void setHoid(String hoid) {
		this.hoid = hoid;
	}
	public String getDisk() {
		return disk;
	}
	public void setDisk(String disk) {
		this.disk = disk;
	}
	public String getCpuuse() {
		return cpuuse;
	}
	public void setCpuuse(String cpuuse) {
		this.cpuuse = cpuuse;
	}
	public String getHostnum() {
		return hostnum;
	}
	public void setHostnum(String hostnum) {
		this.hostnum = hostnum;
	}
	public String getMem() {
		return mem;
	}
	public void setMem(String mem) {
		this.mem = mem;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getUnusedstore() {
		return unusedstore;
	}
	public void setUnusedstore(String unusedstore) {
		this.unusedstore = unusedstore;
	}
	public String getDcid() {
		return dcid;
	}
	public void setDcid(String dcid) {
		this.dcid = dcid;
	}
	public String getCrid() {
		return crid;
	}
	public void setCrid(String crid) {
		this.crid = crid;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

}
