package com.afunms.cabinet.model;

import com.afunms.common.base.BaseVo;

/**
 * ���������ƶ�
 * 
 * @author wxy
 * @version Dec 19, 2011 3:36:26 PM
 */
public class RoomLaw extends BaseVo{
	private int id;
	private int cabinetid;// ����ID�����nms_eqproom��id��Ӧ��
	private int userid;// �û�ID(���system_user��id��Ӧ)
	private String name;// �����ƶ�����
	private String location;//����λ��
	private String filename;// �ļ�·����
	private String dotime;// ����ʱ��
	private String description;// ����

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getDotime() {
		return dotime;
	}

	public void setDotime(String dotime) {
		this.dotime = dotime;
	}

	public int getCabinetid() {
		return cabinetid;
	}

	public void setCabinetid(int cabinetid) {
		this.cabinetid = cabinetid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
