package com.afunms.webservice.model;

import java.io.Serializable;

import com.afunms.common.base.BaseVo;

/**
 * @description ci �������ʹ����
 * @author wangxiangyong
 * @date Jan 17, 2012 3:20:12 PM
 */
public class CiType extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8218705498260765947L;
	private int id; // Ψһ��ʶId
	private String ciType; // �������ʹ���
	private String fatherType; // ���������ʹ���
	private String attributeName;// ��������
	private String description; // ����

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCiType() {
		return ciType;
	}

	public void setCiType(String ciType) {
		this.ciType = ciType;
	}

	public String getFatherType() {
		return fatherType;
	}

	public void setFatherType(String fatherType) {
		this.fatherType = fatherType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
