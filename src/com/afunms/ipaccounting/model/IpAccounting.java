/**
 * <p>Description:mapping table NMS_ALARM_INFO</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-28
 */

package com.afunms.ipaccounting.model;

import com.afunms.common.base.BaseVo;

public class IpAccounting extends BaseVo
{
	private int id;
	private int accountingBaseID;
	private String srcip;
	private String destip;
	private int pkts;
	private int byts;
	private java.util.Calendar collecttime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAccountingBaseID() {
		return accountingBaseID;
	}
	public void setAccountingBaseID(int accountingBaseID) {
		this.accountingBaseID = accountingBaseID;
	}
	public String getSrcip() {
		return srcip;
	}
	public void setSrcip(String srcip) {
		this.srcip = srcip;
	}
	public String getDestip() {
		return destip;
	}
	public void setDestip(String destip) {
		this.destip = destip;
	}
	public int getPkts() {
		return pkts;
	}
	public void setPkts(int pkts) {
		this.pkts = pkts;
	}
	public int getByts() {
		return byts;
	}
	public void setByts(int byts) {
		this.byts = byts;
	}
	public java.util.Calendar getCollecttime() {
		return collecttime;
	}
	public void setCollecttime(java.util.Calendar collecttime) {
		this.collecttime = collecttime;
	}
	
		
}