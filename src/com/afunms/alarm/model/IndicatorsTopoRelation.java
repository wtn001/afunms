package com.afunms.alarm.model;

import com.afunms.common.base.BaseVo;

public class IndicatorsTopoRelation extends BaseVo{
	
	/** ����ͼid */
	private String topoId;
	
	/** �豸�ĸ澯ָ�� id */
	private String indicatorsId;
	
	/** �豸�ĸ澯ָ�� id �������� ���� ���̵��̷� id �� ���������ٵĸ����˿ں� */
	private String sIndex;

	private String nodeid;
	
	public String getNodeid() {
		return nodeid;
	}

	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	/**
	 * @return the topoId
	 */
	public String getTopoId() {
		return topoId;
	}

	/**
	 * @param topoId the topoId to set
	 */
	public void setTopoId(String topoId) {
		this.topoId = topoId;
	}

	/**
	 * @return the indicatorsId
	 */
	public String getIndicatorsId() {
		return indicatorsId;
	}

	/**
	 * @param indicatorsId the indicatorsId to set
	 */
	public void setIndicatorsId(String indicatorsId) {
		this.indicatorsId = indicatorsId;
	}

	/**
	 * @return the sIndex
	 */
	public String getSIndex() {
		return sIndex;
	}

	/**
	 * @param index the sIndex to set
	 */
	public void setSIndex(String index) {
		sIndex = index;
	}
	
	
	
	
}
