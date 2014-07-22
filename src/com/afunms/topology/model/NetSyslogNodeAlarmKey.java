/**
 * <p>Description:mapping table NMS_POSITION</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-07
 */

package com.afunms.topology.model;

import com.afunms.common.base.BaseVo;

public class NetSyslogNodeAlarmKey extends BaseVo
{
	private String id;
	private String nodeid;//�豸id
	private String keywords;//�澯�ؼ���
	private String level;//�澯�ȼ�
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
