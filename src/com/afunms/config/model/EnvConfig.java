package com.afunms.config.model;

import com.afunms.common.base.BaseVo;

/**
 * @description ���������澯����
 * @author wangxiangyong
 * @date Dec 27, 2011
 */
public class EnvConfig extends BaseVo{
	private Integer id;
	private String ipaddress;// �豸IP
	private String name; // ģ������
	private Integer enabled;// �Ƿ��͸澯
	private Integer alarmvalue;//�澯��ֵ
	private String alarmlevel;// �澯����1����ͨ��2�����أ�3��������
	private Integer alarmtimes;//�澯����
	private String entity;
	private String bak;//��ע
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getAlarmvalue() {
		return alarmvalue;
	}

	public void setAlarmvalue(Integer alarmvalue) {
		this.alarmvalue = alarmvalue;
	}

	public String getAlarmlevel() {
		return alarmlevel;
	}

	public void setAlarmlevel(String alarmlevel) {
		this.alarmlevel = alarmlevel;
	}

	public Integer getAlarmtimes() {
		return alarmtimes;
	}

	public void setAlarmtimes(Integer alarmtimes) {
		this.alarmtimes = alarmtimes;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

}
