package com.afunms.cabinet.model;

import com.afunms.common.base.BaseVo;

public class OperCabinet extends BaseVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private int id;
	/**
	 * ��������
	 */
	private String roomname;
	/**
	 * ��������
	 */
	private String cabinetname;
	/**
	 * U��Ԫ
	 */
	private String useu;
	/**
	 * �豸����
	 */
	private String equipmentname;
	/**
	 * IP��ַ
	 */
	private String ipaddress;
	/**
	 * ��ϵ��
	 */
    private String contactname;
	/**
	 * ��ϵ�绰
	 */
    private String contactphone;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRoomname() {
		return roomname;
	}
	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}
	public String getCabinetname() {
		return cabinetname;
	}
	public void setCabinetname(String cabinetname) {
		this.cabinetname = cabinetname;
	}
	public String getUseu() {
		return useu;
	}
	public void setUseu(String useu) {
		this.useu = useu;
	}
	public String getEquipmentname() {
		return equipmentname;
	}
	public void setEquipmentname(String equipmentname) {
		this.equipmentname = equipmentname;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	public String getContactname() {
		return contactname;
	}
	public void setContactname(String contactname) {
		this.contactname = contactname;
	}
	public String getContactphone() {
		return contactphone;
	}
	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}
	
	
}
