package com.afunms.polling.task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import com.afunms.application.dao.WasConfigDao;
import com.afunms.application.model.WasConfig;
import com.afunms.application.wasmonitor.UrlConncetWas;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.om.Pingcollectdata;

public class WasDataCollector {
	private Hashtable wasdata = ShareData.getWasdata();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public WasDataCollector() {
	}
	
	public void collect_data(String id,Hashtable gatherHash) {
		
		
		
		WasConfig wasconf = null;
		try {                	
            int serverflag = 0;
            String ipaddress = "";
            WasConfigDao dao=new WasConfigDao();
    		try{
    			wasconf=(WasConfig)dao.findByID(id);
    		}catch(Exception e){
    			e.printStackTrace();
    		}finally{
    			dao.close();
    		}
    		SysLogger.info("#######################WebSphere ��ʼ�ɼ�###################################");
    		SysLogger.info("###############WebSphere������:"+wasconf.getName()+" ipaddress:"+wasconf.getIpaddress()+"#####################");
            //AdminClient5 wasadmin = new AdminClient5();
            UrlConncetWas  conWas = new UrlConncetWas();
            Hashtable hst = new Hashtable();
     		com.afunms.polling.node.Was _tnode=(com.afunms.polling.node.Was)PollingEngine.getInstance().getWasByID(wasconf.getId());
			
     		Calendar _date=Calendar.getInstance();
				Date _cc = _date.getTime();
	 			String _tempsenddate = sdf.format(_cc);
	 			//��ʼ��Was�����״̬
				_tnode.setLastTime(_tempsenddate);
				_tnode.setAlarm(false);
				_tnode.getAlarmMessage().clear();
				_tnode.setStatus(0);
				
				
				
				if(gatherHash.containsKey("ping")){
					//�Կ����Խ��м��
					boolean collectWasIsOK = false;
					try{
						collectWasIsOK = conWas.connectWasIsOK(wasconf.getIpaddress(),wasconf.getPortnum());
					}catch(Exception e){
						//e.printStackTrace();
					}
					if(collectWasIsOK){
	         					//����״̬
	         					Pingcollectdata hostdata=null;
	     						hostdata=new Pingcollectdata();
	     						hostdata.setIpaddress(wasconf.getIpaddress());
	     						Calendar date=Calendar.getInstance();
	     						hostdata.setCollecttime(date);
	     						hostdata.setCategory("WasPing");
	     						hostdata.setEntity("Utilization");
	     						hostdata.setSubentity("ConnectUtilization");
	     						hostdata.setRestype("dynamic");
	     						hostdata.setUnit("%");
	     						hostdata.setThevalue("100");
	     						WasConfigDao wasconfigdao=new WasConfigDao();
	     						try{
	     							wasconfigdao.createHostData(wasconf,hostdata);
	     							if(wasdata.containsKey("was"+":"+wasconf.getIpaddress()))
	     								wasdata.remove("was"+":"+wasconf.getIpaddress());
	     						}catch(Exception e){
	     							e.printStackTrace();
	     						}finally{
	     							wasconfigdao.close();
	     						}
	     						//�������ݲɼ�
	     			     		try{
	     			     			hst = conWas.ConncetWas(wasconf.getIpaddress(), String.valueOf(wasconf.getPortnum()), "", "", wasconf.getVersion(),gatherHash);
	     			     		}catch(Exception e){
	     			     			
	     			     		}
	         		}
				}
				if(hst != null){
					ShareData.getWasdata().put(wasconf.getIpaddress(), hst);
				}
				hst = null;
         }catch(Exception exc){
         	exc.printStackTrace();
         }
	}

//private  Runnable createTask(final WasConfig wasconf) {
//    return new Runnable() {
//        public void run() {
//            
//        }
//    };
//  }
//public void createSMS(String was,WasConfig wasconf){
//	
// 	//��������		 	
// 	//���ڴ����õ�ǰ���IP��PING��ֵ
//		Calendar date=Calendar.getInstance();
//		try{
//			if (!sendeddata.containsKey(was+":"+wasconf.getId())){
//				//�����ڣ��������ţ�������ӵ������б���
// 			Smscontent smscontent = new Smscontent();
// 			String time = sdf.format(date.getTime());
// 			smscontent.setLevel("2");
// 			smscontent.setObjid(wasconf.getId()+"");
// 			if("wasserver".equals(was)){
// 				smscontent.setMessage("WAS"+" ("+wasconf.getName()+":"+wasconf.getIpaddress()+")"+"�ķ���ֹͣ��");
// 			}
// 			smscontent.setRecordtime(time);
// 			smscontent.setSubtype("wasserver");
// 			smscontent.setSubentity("ping");
// 			smscontent.setIp(wasconf.getIpaddress());
// 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
// 			//���Ͷ���
// 			SmscontentDao smsmanager=new SmscontentDao();
// 			smsmanager.sendURLSmscontent(smscontent);	
// 			sendeddata.put(was+":"+wasconf.getId(),date);		 					 				
//			}else{
//				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
//				Calendar formerdate =(Calendar)sendeddata.get(was+":"+wasconf.getId());		 				
// 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
// 			Date last = null;
// 			Date current = null;
// 			Calendar sendcalen = formerdate;
// 			Date cc = sendcalen.getTime();
// 			String tempsenddate = formatter.format(cc);
// 			
// 			Calendar currentcalen = date;
// 			cc = currentcalen.getTime();
// 			last = formatter.parse(tempsenddate);
// 			String currentsenddate = formatter.format(cc);
// 			current = formatter.parse(currentsenddate);
// 			
// 			long subvalue = current.getTime()-last.getTime();			 			
// 			if (subvalue/(1000*60*60*24)>=1){
// 				//����һ�죬���ٷ���Ϣ
//	 			Smscontent smscontent = new Smscontent();
//	 			String time = sdf.format(date.getTime());
//	 			smscontent.setLevel("2");
//	 			smscontent.setObjid(wasconf.getId()+"");
//	 			if("wasserver".equals(was)){
//	 				smscontent.setMessage("WAS"+" ("+wasconf.getName()+":"+wasconf.getIpaddress()+")"+"�ķ���ֹͣ��");
//	 			}
//	 			smscontent.setRecordtime(time);
//	 			smscontent.setSubtype("wasserver");
//	 			smscontent.setSubentity("ping");
//	 			smscontent.setIp(wasconf.getIpaddress());
//	 			//smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+" IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
//	 			//���Ͷ���
//	 			SmscontentDao smsmanager=new SmscontentDao();
//	 			smsmanager.sendURLSmscontent(smscontent);
//				//�޸��Ѿ����͵Ķ��ż�¼	
//	 			sendeddata.put(was+":"+wasconf.getId(),date);	
//	 		}	
//			}	 			 			 			 			 	
// 	}catch(Exception e){
// 		e.printStackTrace();
// 	}
// }


}






