package com.afunms.webservice.model;

import java.io.Serializable;

import com.afunms.common.base.BaseVo;

/**
 * @description ����ڵ�ID�����ʹ���Ĺ�ϵ
 * @author wangxiangyong
 * @date Jan 17, 2012 3:35:36 PM
 */
public class MoAndCiRelation extends BaseVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6785396127525579378L;
	private int id;// Ψһ��ʶID
	private int moId;// ����ڵ�ID,�൱������ϵͳ���豸�ڵ�ID,ע�⣺�ӿڵ�id���ͺͽӿ����Ե�Model���Ͳ�һ��
	private String ciType;// ���ʹ��루�˴��Ĵ��벻һ����Ӧnms_webservice_ciType,�˴�ֻ�����

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMoId() {
		return moId;
	}

	public void setMoId(int moId) {
		this.moId = moId;
	}

	public String getCiType() {
		return ciType;
	}

	public void setCiType(String ciType) {
		this.ciType = ciType;
	}

}
