package com.afunms.config.model;

import com.afunms.common.base.BaseVo;


/**
 * 
 *  ip ��ַ����bean 
 * @author 
 *
 */
public class IpConfig extends BaseVo{
	
	private int id; //id 
	private String ipaddress;//ip ��ַ
	private int discrictid; //����id
	private int deptid;  //����id
	private int employeeid; //Ա��id
	private String ipdesc; //ip ַ����
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ip) {
		this.ipaddress = ip;
	}
	public int getDiscrictid() {
		return discrictid;
	}
	public void setDiscrictid(int discrictid) {
		this.discrictid = discrictid;
	}
	public int getDeptid() {
		return deptid;
	}
	public void setDeptid(int deptid) {
		this.deptid = deptid;
	}
	public int getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(int employeeid) {
		this.employeeid = employeeid;
	}
	public String getIpdesc() {
		return ipdesc;
	}
	public void setIpdesc(String ipdesc) {
		this.ipdesc = ipdesc;
	}
	
	
}
