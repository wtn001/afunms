package com.afunms.cabinet.model;

import com.afunms.common.base.BaseVo;

public class Cabinet extends BaseVo{
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
	private String name;
	/**
	 * ��U��
	 */
	private String allu;
	/**
	 * ����u��
	 */
	private String useu;
	/**
	 * ����U��
	 */
	private String tempu;
	/**
	 * Uʹ����
	 */
	private String rateu;
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
	public String getAllu() {
		return allu;
	}
	public void setAllu(String allu) {
		this.allu = allu;
	}
	public String getUseu() {
		return useu;
	}
	public void setUseu(String useu) {
		this.useu = useu;
	}
	public String getTempu() {
		return tempu;
	}
	public void setTempu(String tempu) {
		this.tempu = tempu;
	}
	public String getRateu() {
		return rateu;
	}
	public void setRateu(String rateu) {
		this.rateu = rateu;
	}

	
}
