package com.afunms.application.model;

import java.util.Calendar;

import com.afunms.common.base.BaseVo;

/**
 * @author hukelei 
 * @version ����ʱ�䣺2011-12-31
 * ��˵����
 */
public class Tracerts extends BaseVo {
	private int id;
	
	/**
	 * �ڵ�����
	 */
	private String nodetype;
	
	/**
	 * �ڵ�����ID
	 */
	private int configid;
	
	/**
	 * ִ��ʱ��
	 */
	private Calendar dotime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNodetype() {
		return nodetype;
	}

	public void setNodetype(String nodetype) {
		this.nodetype = nodetype;
	}

	public int getConfigid() {
		return configid;
	}

	public void setConfigid(int configid) {
		this.configid = configid;
	}

	public Calendar getDotime() {
		return dotime;
	}

	public void setDotime(Calendar dotime) {
		this.dotime = dotime;
	}

	
}
