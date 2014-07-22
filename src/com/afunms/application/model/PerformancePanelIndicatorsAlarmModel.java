package com.afunms.application.model;

import com.afunms.common.base.BaseVo;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 16, 2011 3:26:49 PM
 * ��˵�� :��������ָ��澯��Ϣģ��
 */
public class PerformancePanelIndicatorsAlarmModel extends BaseVo{

	private String id;
	
	/**
	 * �豸��id
	 */
	private String deviceId;
	
	/**
	 * �豸������
	 */
	private String deviceType;
	
	/**
	 * ָ�������
	 */
	private String indicatorName;
	
	/**
	 * ָ�������
	 */
	private String indicatorDesc;
	
	/**
	 * �澯�ȼ�
	 */
	private String alarmLevel;
	
	/**
	 * �澯����
	 */
	private String alarmDesc;
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmDesc() {
		return alarmDesc;
	}

	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public String getIndicatorDesc() {
		return indicatorDesc;
	}

	public void setIndicatorDesc(String indicatorDesc) {
		this.indicatorDesc = indicatorDesc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}

