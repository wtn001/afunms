package com.afunms.polling.snmp.memory;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.AlarmHelper;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.EnvConfig;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HDSEnvfanResultTosql;
import com.gatherResulttosql.HDSRunMemoryResultTosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HDCRunMemorySnmp extends SnmpMonitor {
	//private Hashtable sendeddata = ShareData.getProcsendeddata();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public HDCRunMemorySnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		SysLogger.info("########��ʼ�ɼ�hdc�Ĺ����ڴ���Ϣ##########");
		Hashtable returnHash=new Hashtable();
		Vector fanVector=new Vector();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		if(node == null)return returnHash;
		//�ж��Ƿ��ڲɼ�ʱ�����
    	if(ShareData.getTimegatherhash() != null){
    		if(ShareData.getTimegatherhash().containsKey(node.getId()+":equipment")){
    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
    			int _result = 0;
    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(node.getId()+":equipment"));
    			if(_result ==1 ){
    				//SysLogger.info("########ʱ�����: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else if(_result == 2){
    				//SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else {
    				SysLogger.info("######## "+node.getIpAddress()+" ���ڲɼ��ڴ�ʱ�����,�˳�##########");
//    				//���֮ǰ�ڴ��в����ĸ澯��Ϣ
//    			    try{
//    			    	//���֮ǰ�ڴ��в������ڴ�澯��Ϣ
//						CheckEventUtil checkutil = new CheckEventUtil();
//						checkutil.deleteEvent(node.getId()+":host:diskperc");
//						checkutil.deleteEvent(node.getId()+":host:diskinc");
//    			    }catch(Exception e){
//    			    	e.printStackTrace();
//    			    }
    				return returnHash;
    			}
    			
    		}
    	}
		try {
			Interfacecollectdata interfacedata = new Interfacecollectdata();
			Calendar date=Calendar.getInstance();
			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
			if(ipAllData == null)ipAllData = new Hashtable();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
			  try {
				  //-------------------------------------------------------------------------------------------���� start
		   		  String temp = "0";
		   		  //if(node.getSysOid().startsWith("1.3.6.1.4.1.2011.") || node.getSysOid().startsWith("1.3.6.1.4.1.25506.")){
			   			String[][] valueArray = null;
			   			String[] oids =                
							  new String[] {               
								"1.3.6.1.4.1.116.5.11.4.1.1.6.1.1",//dkcRaidListIndexSerialNumber
								"1.3.6.1.4.1.116.5.11.4.1.1.6.1.5"//dkcHWSM �����ڴ�״̬
			   			};
			   			//valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
			   			valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
			   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
			   			int flag = 0;
						if(valueArray != null){
						   	  for(int i=0;i<valueArray.length;i++)
						   	  {
						   		String _value = valueArray[i][1];
						   		String index = valueArray[i][2];
						   		String num = valueArray[i][0];
								//if(value > 0){
									flag = flag +1;
							   		List alist = new ArrayList();
							   		alist.add(index);
							   		alist.add(_value);
							   		alist.add(num);
							   		//�����ڴ�״̬
							   		//fanList.add(alist);				   		
							   		  interfacedata = new Interfacecollectdata();
							   		  interfacedata.setIpaddress(node.getIpAddress());
							   		  interfacedata.setCollecttime(date);
							   		  interfacedata.setCategory("rmemory");
							   		  interfacedata.setEntity(index);
							   		  interfacedata.setSubentity(num);
							   		  interfacedata.setRestype("dynamic");
							   		  interfacedata.setUnit("");		
							   		  interfacedata.setThevalue(_value);
//							   		  interfacedata.setThevalue("4");
									  SysLogger.info(node.getIpAddress()+" ������"+index+" �����ڴ�״̬�� "+_value);
									  fanVector.addElement(interfacedata);		   		
								//}
						   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
						   	  }
						}	   			  
		   		  //} 
				  //fanVector.add(fanList);
		   	  }
		   	  catch(Exception e)
		   	  {
		   	  }	   	  
		   	  //-------------------------------------------------------------------------------------------���� end
			}catch(Exception e){
			}
		
//		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
//		if(ipAllData == null)ipAllData = new Hashtable();
//		ipAllData.put("fan",fanVector);
//	    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
//	    returnHash.put("fan", fanVector);
	    
	    
	    if(!(ShareData.getSharedata().containsKey(node.getIpAddress())))
		{
			Hashtable ipAllData = new Hashtable();
			if(ipAllData == null)ipAllData = new Hashtable();
			if(fanVector != null && fanVector.size()>0)ipAllData.put("rmemory",fanVector);
		    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		}else
		 {
			if(fanVector != null && fanVector.size()>0)((Hashtable)ShareData.getSharedata().get(node.getIpAddress())).put("rmemory",fanVector);
		   
		 }
	    returnHash.put("rmemory", fanVector);
	    
	    try{
			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_STORAGE, "hds","rmemory");
			
			AlarmHelper helper=new AlarmHelper();
			Hashtable<String, EnvConfig> envHashtable=helper.getAlarmConfig(node.getIpAddress(), "rmemory");
			for(int i = 0 ; i < list.size() ; i ++){
				 
				AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
				
				//�Է���ֵ���и澯���
				CheckEventUtil checkutil = new CheckEventUtil();
				if (fanVector.size()>0) {
					for (int j = 0; j < fanVector.size(); j++) {
						Interfacecollectdata data=(Interfacecollectdata)fanVector.get(j);
						if (data!=null) {
							EnvConfig config=envHashtable.get(data.getEntity());
							if(config!=null&&config.getEnabled()==1){
								alarmIndicatorsnode.setAlarm_level(config.getAlarmlevel());
								alarmIndicatorsnode.setAlarm_times(config.getAlarmtimes()+"");
								alarmIndicatorsnode.setLimenvalue0(config.getAlarmvalue()+"");
							   checkutil.checkEvent(node,alarmIndicatorsnode,data.getThevalue(),data.getSubentity());
							  
							}
							}
						
					}
					
				}
				
			}
			
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    fanVector=null;
	    
	    
	    
	    //�Ѳɼ��������sql
	    HDSRunMemoryResultTosql tosql=new HDSRunMemoryResultTosql();
	    tosql.CreateResultTosql(returnHash, node.getIpAddress());
	    
	    return returnHash;
	}
}





