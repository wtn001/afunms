package com.afunms.cabinet.model;

import com.afunms.common.base.BaseVo;

public class MachineCabinet extends BaseVo{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * id
	 */
	private int id;
	/**
	 * ����
	 */
	private String name;
	/**
	 * ����Xλ��
	 */
	private String machinex;
	/**
	 * ����Yλ��
	 */
	private String machiney;
	/**
	 * ����Zλ��
	 */
	private String machinez;
	/**
	 * U
	 */
	private String uselect;
	/**
	 * ����
	 */
	private String motorroom;
	/**
	 * ���ܹ��
	 */
	private String standards;
	/**
	 * ��Դ����
	 */
	private String powers;
	/**
	 * �߶�
	 */
	private String heights;
	/**
	 * ���
	 */
	private String widths;
	/**
	 * ���
	 */
	private String depths;
	/**
	 * ������
	 */
	private String nos;
	
	private String icon = "../resource/image/menu/jgpz.gif";
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
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
	public String getMachinex() {
		return machinex;
	}
	public void setMachinex(String machinex) {
		this.machinex = machinex;
	}
	public String getMachiney() {
		return machiney;
	}
	public void setMachiney(String machiney) {
		this.machiney = machiney;
	}
	public String getMachinez() {
		return machinez;
	}
	public void setMachinez(String machinez) {
		this.machinez = machinez;
	}
	public String getUselect() {
		return uselect;
	}
	public void setUselect(String uselect) {
		this.uselect = uselect;
	}
	public String getMotorroom() {
		return motorroom;
	}
	public void setMotorroom(String motorroom) {
		this.motorroom = motorroom;
	}
	public String getStandards() {
		return standards;
	}
	public void setStandards(String standards) {
		this.standards = standards;
	}
	public String getPowers() {
		return powers;
	}
	public void setPowers(String powers) {
		this.powers = powers;
	}
	public String getHeights() {
		return heights;
	}
	public void setHeights(String heights) {
		this.heights = heights;
	}
	public String getWidths() {
		return widths;
	}
	public void setWidths(String widths) {
		this.widths = widths;
	}
	public String getDepths() {
		return depths;
	}
	public void setDepths(String depths) {
		this.depths = depths;
	}
	public String getNos() {
		return nos;
	}
	public void setNos(String nos) {
		this.nos = nos;
	}

	
}
