package com.afunms.polling.om;

import java.io.Serializable;

/**
 * cmts�еĵ�ַ����
 * @author Administrator
 *
 */
public class CMTSaddresscollectdata implements Serializable {

	private String ipAddress;// IP��ַ

	private String macAddress;// MAC��ַ

	private String statusAddress;// ��ַ״̬

	private String collecttime;// �ɼ�ʱ��

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getStatusAddress() {
		return statusAddress;
	}

	public void setStatusAddress(String statusAddress) {
		this.statusAddress = statusAddress;
	}

	public String getCollecttime() {
		return collecttime;
	}

	public void setCollecttime(String collecttime) {
		this.collecttime = collecttime;
	}
}
