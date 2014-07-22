package com.afunms.slaaudit.model;

import com.afunms.common.base.BaseVo;

public class SlaAudit extends BaseVo {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private int id;// ID
	private int userid;// �û�ID
	private int telnetconfigid;//telnetconfig Id��topo_node_telnetconfig�� id����
	private String slatype;// sla ����
	private String operation;// ��������
	private String cmdcontent;// ���������
	private String dotime;// ����ʱ��
	private int dostatus;// ����״̬ 0��ʧ�� 1���ɹ�
   
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getTelnetconfigid() {
		return telnetconfigid;
	}

	public void setTelnetconfigid(int telnetconfigid) {
		this.telnetconfigid = telnetconfigid;
	}

	public String getSlatype() {
		return slatype;
	}

	public void setSlatype(String slatype) {
		this.slatype = slatype;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getCmdcontent() {
		return cmdcontent;
	}

	public void setCmdcontent(String cmdcontent) {
		this.cmdcontent = cmdcontent;
	}

	public String getDotime() {
		return dotime;
	}

	public void setDotime(String dotime) {
		this.dotime = dotime;
	}

	public int getDostatus() {
		return dostatus;
	}

	public void setDostatus(int dostatus) {
		this.dostatus = dostatus;
	}

	

}
