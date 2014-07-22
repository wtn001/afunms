package com.afunms.webservice.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @description �豸�ӿڵĻ�����Ϣ
 * @author wangxiangyong
 * @date Jan 14, 2012 3:44:46 PM
 */
@XmlRootElement(name = "interface")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceInterfaceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4326325399424449029L;
//	private String id; // ID
//	private String ipAddress; // �豸��IP�ĵ�ַ
	private String ifName; // �˿�����
	private String ifType; // �˿�����
	private String ifPhysAddress;// �˿�Mac��ַ

//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getIpAddress() {
//		return ipAddress;
//	}
//
//	public void setIpAddress(String ipAddress) {
//		this.ipAddress = ipAddress;
//	}

	public String getIfName() {
		return ifName;
	}

	public void setIfName(String ifName) {
		this.ifName = ifName;
	}

	public String getIfType() {
		return ifType;
	}

	public void setIfType(String ifType) {
		this.ifType = ifType;
	}

	public String getIfPhysAddress() {
		return ifPhysAddress;
	}

	public void setIfPhysAddress(String ifPhysAddress) {
		this.ifPhysAddress = ifPhysAddress;
	}

}
