package com.afunms.cabinet.model;

import com.afunms.common.base.BaseVo;

/**
 * @description ��������model
 * @author wangxiangyong
 * @date Dec 23, 2011
 */
public class RoomGuest extends BaseVo{
	private int id;
	private int cabinetid;
	private String name;// ����
	private String unit;// ��λ
	private String phone;// ��ϵ�绰
	private String mail;// �����ʼ�
	private String inTime;// ����ʱ��
	private String outTime;// �볡ʱ��
	private String dotime; //����ʱ��
	private String reason;// ����ԭ��
	private String audit;// ������
    private String bak; //��ע
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCabinetid() {
		return cabinetid;
	}

	public void setCabinetid(int cabinetid) {
		this.cabinetid = cabinetid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getDotime() {
		return dotime;
	}

	public void setDotime(String dotime) {
		this.dotime = dotime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getAudit() {
		return audit;
	}

	public void setAudit(String audit) {
		this.audit = audit;
	}

	public String getBak() {
		return bak;
	}

	public void setBak(String bak) {
		this.bak = bak;
	}

}
