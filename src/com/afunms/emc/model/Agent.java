package com.afunms.emc.model;

import com.afunms.common.base.BaseVo;

public class Agent extends BaseVo{

	private String id;
	private String nodeid;
	private String agentRev;//�汾
	private String name;//����		
	private String descr;//����
	private String node;//�豸���ļ�ϵͳ�е�λ��
	private String physicalNode;//����λ��
	private String Signature;//spǩ��	
	private String peerSignature;//�Ե�spǩ��
	private String revision;//flare΢��汾��
	private String SCSIId;//sp���ӵ�������scsi id
	private String model;//sp�ͺ�
	private String modelType;//sp�ͺ�����
	private String promRev;//sp rrom�汾
	private String SPMemory;//sp�ڴ�
	private String serialNo;//���к�
	private String SPIdentifier;//��ʶ
	private String cabinet;//��������
	public String getAgentRev() {
		return agentRev;
	}
	public void setAgentRev(String agentRev) {
		this.agentRev = agentRev;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getPhysicalNode() {
		return physicalNode;
	}
	public void setPhysicalNode(String physicalNode) {
		this.physicalNode = physicalNode;
	}
	public String getSignature() {
		return Signature;
	}
	public void setSignature(String signature) {
		Signature = signature;
	}
	public String getPeerSignature() {
		return peerSignature;
	}
	public void setPeerSignature(String peerSignature) {
		this.peerSignature = peerSignature;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public String getSCSIId() {
		return SCSIId;
	}
	public void setSCSIId(String id) {
		SCSIId = id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getModelType() {
		return modelType;
	}
	public void setModelType(String modelType) {
		this.modelType = modelType;
	}
	public String getPromRev() {
		return promRev;
	}
	public void setPromRev(String promRev) {
		this.promRev = promRev;
	}
	public String getSPMemory() {
		return SPMemory;
	}
	public void setSPMemory(String memory) {
		SPMemory = memory;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getSPIdentifier() {
		return SPIdentifier;
	}
	public void setSPIdentifier(String identifier) {
		SPIdentifier = identifier;
	}
	public String getCabinet() {
		return cabinet;
	}
	public void setCabinet(String cabinet) {
		this.cabinet = cabinet;
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
