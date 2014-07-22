package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.om.Interfacecollectdata;
import com.gatherdb.GathersqlListManager;

public class HDSRunEnvResultTosql {
	
	/**
	 * 
	 * �Ѳɼ���������sql������ڴ��б���
	 */
	public void CreateResultTosql(Hashtable ipdata,String ip)
	{
		
	if(ipdata.containsKey("renv")){
		//����
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String allipstr = SysUtil.doip(ip);
	
		Vector fanVector = (Vector) ipdata.get("renv");
		if (fanVector != null && fanVector.size() > 0) {
			//�õ�����
			Interfacecollectdata fandata = (Interfacecollectdata) fanVector.elementAt(0);
			if(fandata.getThevalue() == null)return;
			if (fandata.getRestype().equals("dynamic")) {
				
				Calendar tempCal = (Calendar) fandata.getCollecttime();
				Date cc = tempCal.getTime();
				String time = sdf.format(cc);
				String tablename = "renv" + allipstr;
				long count = 0;
				if(fandata.getCount() != null){
					count = fandata.getCount();
				}
				StringBuffer sBuffer = new StringBuffer();
				sBuffer.append("insert into ");
				sBuffer.append(tablename);
				sBuffer.append("(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) ");
				sBuffer.append("values('");
				sBuffer.append(ip);
				sBuffer.append("','");
				sBuffer.append(fandata.getRestype());
				sBuffer.append("','");
				sBuffer.append(fandata.getCategory());
				sBuffer.append("','");
				sBuffer.append(fandata.getEntity());
				sBuffer.append("','");
				sBuffer.append(fandata.getSubentity());
				sBuffer.append("','");
				sBuffer.append(fandata.getUnit());
				sBuffer.append("','");
				sBuffer.append(fandata.getChname());
				sBuffer.append("','");
				sBuffer.append(fandata.getBak());
				sBuffer.append("','");
				sBuffer.append(count);
				sBuffer.append("','");
				sBuffer.append(fandata.getThevalue());
				if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append("','");
					sBuffer.append(time);
					sBuffer.append("')");
				}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append("',");
					sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
					sBuffer.append(")");
				}
				
				//System.out.println(sql);
				
				GathersqlListManager.Addsql(sBuffer.toString());
				sBuffer = null;														
			}
			fandata = null;
		}
		fanVector = null;
	}
	
	
	}

}
