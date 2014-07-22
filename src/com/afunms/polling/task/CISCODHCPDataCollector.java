
package com.afunms.polling.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.DHCPConfigDao;
import com.afunms.application.model.DHCPConfig;
import com.afunms.application.model.WeblogicConfig;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.snmp.dhcp.CiscoDhcpScopeSnmp;
import com.afunms.polling.snmp.dhcp.DhcpScopeSnmp;



/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CISCODHCPDataCollector{
	/**
	 * 
	 */
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public CISCODHCPDataCollector() {
	}

	public void collect_data(String id,Hashtable gatherHash) {
		DHCPConfig dhcpconf = null;
        try {  
        	DHCPConfigDao configdao = new DHCPConfigDao();
        	try{
        		dhcpconf = (DHCPConfig)configdao.findByID(id);
        	}catch(Exception e){
        		
        	}finally{
        		configdao.close();
        	}
        	if (dhcpconf == null)return;
            int serverflag = 0;
            CiscoDhcpScopeSnmp dhcpsnmp=null;
         	Hashtable hash = null;
         	dhcpsnmp = new CiscoDhcpScopeSnmp(dhcpconf.getIpAddress(),dhcpconf.getCommunity(),161);
         	hash=dhcpsnmp.collectData(gatherHash,dhcpconf);
     		if(hash == null) {
     			hash = new Hashtable();
     		} 
     		com.afunms.polling.node.DHCP _tnode=(com.afunms.polling.node.DHCP)PollingEngine.getInstance().getDHCPByID(dhcpconf.getId());
				Calendar _date=Calendar.getInstance();
				Date _cc = _date.getTime();
	 			String _tempsenddate = sdf.format(_cc);
	 			//��ʼ��DHCP�����״̬
				_tnode.setLastTime(_tempsenddate);
				_tnode.setAlarm(false);
				_tnode.getAlarmMessage().clear();
				_tnode.setStatus(0);
     		if(hash.get("dhcpping") != null){
			    try{
					AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
					List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(dhcpconf.getId()), AlarmConstant.TYPE_SERVICE, "ciscodhcp");
					for(int k = 0 ; k < list.size() ; k ++){
						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
						//��CISCODHCP����ֵ���и澯���
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.updateData(_tnode,hash,"service","dhcp",alarmIndicatorsnode);
						//}
					}
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
     			
     		}
     		//���������ж��Ƿ�澯
     		
     		if(hash.get("serverValue") != null){}
				try{
					ShareData.setDhcpdata(dhcpconf.getIpAddress(), hash);
					
				}catch(Exception ex){
					ex.printStackTrace();
				}            		
     		dhcpsnmp=null;
     		hash=null;            	 
         }catch(Exception exc){
         	exc.printStackTrace();
         }
	}

	public void createSMS(String weblogic,WeblogicConfig weblogicconf){
 	//��������		 	
 	//���ڴ����õ�ǰ���IP��PING��ֵ
		Hashtable sendeddata = ShareData.getSendeddata();
		Calendar date=Calendar.getInstance();
		try{
			if (!sendeddata.containsKey(weblogic+":"+weblogicconf.getId())){
				//�����ڣ��������ţ�������ӵ������б���
 			Smscontent smscontent = new Smscontent();
 			String time = sdf.format(date.getTime());
 			smscontent.setLevel("2");
 			smscontent.setObjid(weblogicconf.getId()+"");
 			if("weblogicDomain".equals(weblogic)){
 				smscontent.setMessage(weblogicconf.getAlias()+" ("+weblogicconf.getIpAddress()+")"+"��Weblogic Server����ֹͣ");
 			}
 			if("weblogicServer".equals(weblogic)){
 				smscontent.setMessage(weblogicconf.getAlias()+" ("+weblogicconf.getIpAddress()+")"+"�Ļ��״̬Ϊ ���");	
 			}
 			smscontent.setRecordtime(time);
 			smscontent.setSubtype("weblogic");
 			smscontent.setSubentity("ping");
 			smscontent.setIp(weblogicconf.getIpAddress());
 			
 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
 			//���Ͷ���
 			SmscontentDao smsmanager=new SmscontentDao();
 			smsmanager.sendURLSmscontent(smscontent);	
			sendeddata.put(weblogic+":"+weblogicconf.getId(),date);		 					 				
			}else{
				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
				Calendar formerdate =(Calendar)sendeddata.get(weblogic+":"+weblogicconf.getId());		 				
 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
 			Date last = null;
 			Date current = null;
 			Calendar sendcalen = formerdate;
 			Date cc = sendcalen.getTime();
 			String tempsenddate = formatter.format(cc);
 			
 			Calendar currentcalen = date;
 			cc = currentcalen.getTime();
 			last = formatter.parse(tempsenddate);
 			String currentsenddate = formatter.format(cc);
 			current = formatter.parse(currentsenddate);
 			
 			long subvalue = current.getTime()-last.getTime();			 			
 			if (subvalue/(1000*60*60*24)>=1){
 				//����һ�죬���ٷ���Ϣ
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel("2");
	 			smscontent.setObjid(weblogicconf.getId()+"");
	 			if("weblogicDomain".equals(weblogic)){
	 				smscontent.setMessage(weblogicconf.getAlias()+" ("+weblogicconf.getIpAddress()+")"+"�Ļ��״̬Ϊ ���");
	 			}
	 			if("weblogicServer".equals(weblogic)){
	 				smscontent.setMessage(weblogicconf.getAlias()+" ("+weblogicconf.getIpAddress()+")"+"��Weblogic Server����ֹͣ");	
	 			}
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype(weblogic);
	 			smscontent.setSubentity("ping");
	 			smscontent.setIp(weblogicconf.getIpAddress());
	 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
	 			//���Ͷ���
	 			SmscontentDao smsmanager=new SmscontentDao();
	 			smsmanager.sendURLSmscontent(smscontent);
				//�޸��Ѿ����͵Ķ��ż�¼	
	 			sendeddata.put(weblogic+":"+weblogicconf.getId(),date);	
	 		}	
			}	 			 			 			 			 	
 	}catch(Exception e){
 		e.printStackTrace();
 	}
 }

}

