package com.gatherResulttosql;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.polling.om.CPUcollectdata;
import com.gatherdb.GathersqlListManager;


/**
 * 
 * 
 * �������豸��cpu�ɼ��������sql���
 * @author �����豸cpu�ɼ��������sqlʵ����
 *
 */

public class NetcpuResultTosql {
	
	
	/**
	 * 
	 * ��cpu�Ĳɼ����ݳɳ�sql������ڴ��б���
	 */
	public void CreateResultTosql(Hashtable ipdata,String ip)
	{
		
		CPUcollectdata cpudata = null;
		Hashtable cpuhash = null;
		Vector cpuVector = null;
		StringBuffer sBuffer = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String allipstr = SysUtil.doip(ip);
		
	if(ipdata.containsKey("cpu")){
		//CPU
	//	System.out.println("////////////////////////////");
		//cpuhash = (Hashtable)ipdata.get("cpu");
		cpuVector = (Vector) ipdata.get("cpu");
		if (cpuVector != null && cpuVector.size() > 0) {
			//�õ�CPUƽ��
			cpudata = (CPUcollectdata) cpuVector.elementAt(0);
			if (cpudata.getSubentity().equals("unknown")) {
				//û�вɼ������ݲ����д洢
				return;
			}
			    
			if (cpudata.getRestype().equals("dynamic")) {
				Calendar tempCal = (Calendar) cpudata.getCollecttime();
				Date cc = tempCal.getTime();
				String time = sdf.format(cc);
				String tablename = "cpu" + allipstr;
				long count = 0;
				if(cpudata.getCount() != null){
					count = cpudata.getCount();
				}
				sBuffer = new StringBuffer(150);
				sBuffer.append("insert into ");
				sBuffer.append(tablename);
				sBuffer.append("(ipaddress,restype,category,entity,subentity,unit,chname,bak,count,thevalue,collecttime) ");
				sBuffer.append("values('");
				sBuffer.append(ip);
				sBuffer.append("','");
				sBuffer.append(cpudata.getRestype());
				sBuffer.append("','");
				sBuffer.append(cpudata.getCategory());
				sBuffer.append("','");
				sBuffer.append(cpudata.getEntity());
				sBuffer.append("','");
				sBuffer.append(cpudata.getSubentity());
				sBuffer.append("','");
				sBuffer.append(cpudata.getUnit());
				sBuffer.append("','");
				sBuffer.append(cpudata.getChname());
				sBuffer.append("','");
				sBuffer.append(cpudata.getBak());
				sBuffer.append("','");
				sBuffer.append(count);
				sBuffer.append("','");
				sBuffer.append(cpudata.getThevalue());
				
				if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append("','");
					sBuffer.append(time);
					sBuffer.append("')");
				}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append("',");
					sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
					sBuffer.append(")");
				}else if("dm".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append("',");
					sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
					sBuffer.append(")");
				}
				
				
//				try {
//				//	SysLogger.info("======================"+sBuffer.toString());
//					//dbmanager.addBatch(sBuffer.toString());
//				} catch (Exception ex) {
//					ex.printStackTrace();
//				}
				//SysLogger.info("======================"+sBuffer.toString());
				GathersqlListManager.Addsql(sBuffer.toString());
				sBuffer = null;
				tablename = null;
			}
			cpudata = null;
		}
		cpuhash = null;
		cpuVector = null;
	}
	}

}
