package com.afunms.polling.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;
/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Nov 28, 2011 2:42:50 PM
 * ��˵�� ICMP���ݻ�ȡ�࣬ʹ����Ϻ���Ҫclose
 */
public class ICMPCollectDataManager {

	private DBManager dbManager = new DBManager();
	
	
	/**
	 * ��ȡrrtͳ��ֵ�������С��ƽ����\ ��ȡstatus(�ɹ���)ͳ��ֵ�������С��ƽ����
	 * @param slaconfigid
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Hashtable getICMPData(String slaconfigid, String starttime, String endtime){
		Hashtable retHash = new Hashtable();
		ResultSet rs = null;
		try{
			//SysLogger.info("select max(thevalue) maxvalue, round(avg(thevalue),1) avgvalue, min(thevalue) minvalue from slartt"+slaconfigid+" where COLLECTTIME between '"+starttime+"' and '"+endtime+"'");
			rs = dbManager.executeQuery("select max(thevalue) maxvalue, round(avg(thevalue),1) avgvalue, min(thevalue) minvalue from slartt"+slaconfigid+" where COLLECTTIME between '"+starttime+"' and '"+endtime+"'");
			if(rs.next()){
				String maxvalue = rs.getString("maxvalue");
				String avgvalue = rs.getString("avgvalue");
				String minvalue = rs.getString("minvalue");
				if(maxvalue == null){
					maxvalue = "--";
				}
				if(avgvalue == null){
					avgvalue = "--";
				}
				if(minvalue == null){
					minvalue = "--";
				}
				retHash.put("maxRrtValue", maxvalue);
				retHash.put("avgRrtValue", avgvalue);
				retHash.put("minRrtValue", minvalue);
			}
			rs.close();
			//SysLogger.info("select max(thevalue) maxvalue, round(avg(thevalue),0) avgvalue, min(thevalue) minvalue from slastatus"+slaconfigid+" where COLLECTTIME between '"+starttime+"' and '"+endtime+"'");
			rs = dbManager.executeQuery("select max(thevalue) maxvalue, round(avg(thevalue),0) avgvalue, min(thevalue) minvalue from slastatus"+slaconfigid+" where COLLECTTIME between '"+starttime+"' and '"+endtime+"'");
			if(rs.next()){
				String maxvalue = rs.getString("maxvalue");
				String avgvalue = rs.getString("avgvalue");
				String minvalue = rs.getString("minvalue");
				if(maxvalue == null){
					maxvalue = "--";
				}
				if(avgvalue == null){
					avgvalue = "--";
				}
				if(minvalue == null){
					minvalue = "--";
				}
				retHash.put("maxStatusValue", maxvalue);
				retHash.put("avgStatusValue", avgvalue);
				retHash.put("minStatusValue", minvalue);
			}
		}catch(Exception e){
			SysLogger.error("��ȡICMP�����ݳ���",e);
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return retHash;
	}
	
	public void close(){
		dbManager.close();
	}
}
