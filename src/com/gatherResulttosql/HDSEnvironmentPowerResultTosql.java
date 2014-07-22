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

public class HDSEnvironmentPowerResultTosql {
	
	

	/**
	 * 
	 * �Ѳɼ���������sql������ڴ��б���
	 */
	public void CreateResultTosql(Hashtable ipdata,String ip)
	{
//	System.out.println(ipdata.size()+"------------��ʼִ�У���HDC��rpower�����������--------------"+ipdata.containsKey("epower"));
	if(ipdata.containsKey("epower")){
		//��Դ
//		SysLogger.info("��ӵ�Դ��Ϣ ################"+allipstr);
//		System.out.println("------------------------------------>>>>>>"+ipdata.get("epower"));
		Vector powerVector = (Vector) ipdata.get("epower");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String allipstr = SysUtil.doip(ip);	
//		System.out.println("��ӵ�Դ��Ϣ ################"+allipstr);
//		System.out.println("------------powerVector------------------>"+powerVector.size());
		if (powerVector != null && powerVector.size() > 0) {
			for(int i=0;i<powerVector.size();i++){
				Interfacecollectdata powerdata = (Interfacecollectdata) powerVector.elementAt(i);
				if(powerdata.getThevalue() == null)continue;
				if (powerdata.getRestype().equals("dynamic")) {
					//session.save(cpudata);
//					System.out.println("==========================================");
					Calendar tempCal = (Calendar) powerdata.getCollecttime();
					Date cc = tempCal.getTime();
					String time = sdf.format(cc);
					String tablename = "epower" + allipstr;
					long count = 0;
					if(powerdata.getCount() != null){
						count = powerdata.getCount();
					}
					StringBuffer sBuffer = new StringBuffer();
					sBuffer.append("insert into ");
					sBuffer.append(tablename);
					sBuffer.append("(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) ");
					sBuffer.append("values('");
					sBuffer.append(ip);
					sBuffer.append("','");
					sBuffer.append(powerdata.getRestype());
					sBuffer.append("','");
					sBuffer.append(powerdata.getCategory());
					sBuffer.append("','");
					sBuffer.append(powerdata.getEntity());
					sBuffer.append("','");
					sBuffer.append(powerdata.getSubentity());
					sBuffer.append("','");
					sBuffer.append(powerdata.getUnit());
					sBuffer.append("','");
					sBuffer.append(powerdata.getChname());
					sBuffer.append("','");
					sBuffer.append(powerdata.getBak());
					sBuffer.append("','");
					sBuffer.append(count);
					sBuffer.append("','");
					sBuffer.append(powerdata.getThevalue());
					if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
						sBuffer.append("','");
						sBuffer.append(time);
						sBuffer.append("')");
				    }else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
				    	sBuffer.append("',");
				    	sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
				    	sBuffer.append(")");
				    }
//					System.out.println("------"+sBuffer.toString());
					
					GathersqlListManager.Addsql(sBuffer.toString());
					sBuffer = null;
				}
			powerdata = null;
			}
		}
		powerVector = null;
	}
	
	
	}
	
	

}
