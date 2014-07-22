package com.afunms.webservice.model;

import java.io.Serializable;

/**
 * @description ����ֵ
 * @author wangxiangyong
 * @date Jan 17, 2012 5:40:40 PM
 */
public class AttributeValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8389771338283409172L;
	private String moid;
	private String attributeName;// ������
	private String value;// ����ֵ

	public String getMoid() {
		return moid;
	}

	public void setMoid(String moid) {
		this.moid = moid;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
