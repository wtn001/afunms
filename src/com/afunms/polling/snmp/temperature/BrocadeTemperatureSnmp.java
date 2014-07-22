package com.afunms.polling.snmp.temperature;

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
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetDatatempTemperatureRtosql;
import com.gatherResulttosql.NetTemperatureResultTosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BrocadeTemperatureSnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public BrocadeTemperatureSnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		Hashtable returnHash=new Hashtable();
		Vector temperatureVector=new Vector();
		Vector alarmVector=new Vector();
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
    				SysLogger.info("######## "+node.getIpAddress()+" ���ڲɼ�flashʱ�����,�˳�##########");
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
				  //-------------------------------------------------------------------------------------------�¶� start
		   		  String temp = "0";
		   		  //if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
		   			String[][] valueArray = null;
		   			String[] oids =                
						  new String[] {               
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.1",//swSensorIndex   �¶ȡ����ȡ���Դ
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.2",//swSensorType    ���  temperature(1),fan(2),power-supply(3)
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.3",//swSensorStatus  ״̬
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.4",//swSensorValue   ֵ
							"1.3.6.1.4.1.1588.2.1.1.1.1.22.1.5",//swSensorInfo
		   			};
		   			//valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
		   			valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
		   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
		   			int allvalue=0;
		   			int flag = 0;
		   			int result = 0;
		   			if(valueArray != null){
					   	  for(int i=0;i<valueArray.length;i++)
					   	  {
//					   		String _value = valueArray[i][1];
//					   		String index = valueArray[i][2];
//					   		String desc = valueArray[i][0];
					   		String swSensorIndex = valueArray[i][0];
					   		String swSensorType = valueArray[i][1];
					   		String swSensorStatus = valueArray[i][2];
					   		String swSensorValue = valueArray[i][3];
					   		String swSensorInfo = valueArray[i][4];
						   	String index = valueArray[i][5];
					   		int value=0;
							if(swSensorValue != null){
								value=Integer.parseInt(swSensorValue);
								if(value > 0 && "1".equals(swSensorType)){// 1-5 Ϊ�¶�
								flag = flag +1;
						   		//�ڴ�
						   		//temperatureList.add(alist);
									if(swSensorStatus.equals("1")){//unknown
										swSensorStatus = "δ֪";
									}else if(swSensorStatus.equals("2")){//faulty
										swSensorStatus = "����";
									}else if(swSensorStatus.equals("3")){//below-min
										swSensorStatus = "������Сֵ";
									}else if(swSensorStatus.equals("4")){//nominal
										swSensorStatus = "����";
									}else if(swSensorStatus.equals("5")){//above-max
										swSensorStatus = "�������ֵ";
									}else if(swSensorStatus.equals("6")){//absent
										swSensorStatus = "ȱʧ";
									}
						   		  interfacedata = new Interfacecollectdata();
						   		  interfacedata.setIpaddress(node.getIpAddress());
						   		  interfacedata.setCollecttime(date);
						   		  interfacedata.setCategory("Temperature");
						   		  interfacedata.setEntity(index);
						   		  interfacedata.setSubentity(swSensorInfo);
						   		  interfacedata.setRestype("dynamic");
						   		  interfacedata.setUnit("��");		
						   		  interfacedata.setThevalue(value+"");
						   		  interfacedata.setBak(swSensorStatus);
								  SysLogger.info(node.getIpAddress()+"index:"+index+"   �¶ȣ� "+value);
								  temperatureVector.addElement(interfacedata);	
								}
							}
					   		SysLogger.info(node.getIpAddress()+"  index:"+swSensorIndex+"  swSensorType:"+swSensorType+ "  value:"+value+ "  swSensorStatus="+swSensorStatus);

					   	  }
							////////////////////begin:�澯��������ƽ��ֵ/////////////
							if(flag >0){
								int intvalue = (allvalue/flag);
								temp = intvalue+"";
								//SysLogger.info(host.getIpAddress()+" cpu "+allvalue/flag);
							}
							if(temp == null){
					   			  result = 0;
					   		  }else{
					   			  try{
					   				  if(temp.equalsIgnoreCase("noSuchObject")){
					   					result = 0;
					   				  }else
					   					  result = Integer.parseInt(temp); 
					   			  }catch(Exception ex){
					   				  ex.printStackTrace();
					   				  result = 0;
					   			  }
					   		  }
							 interfacedata = new Interfacecollectdata();
					   		  interfacedata.setIpaddress(node.getIpAddress());
					   		  interfacedata.setCollecttime(date);
					   		  interfacedata.setCategory("Temperature");
					   		  interfacedata.setEntity("");
					   		  interfacedata.setSubentity("");
					   		  interfacedata.setRestype("dynamic");
					   		  interfacedata.setUnit("��");		
					   		  interfacedata.setThevalue(result+"");
							  alarmVector.add(0, interfacedata);
			               ////////////////////end/////////////
					}
		   		  //} 
				  //cpuVector.add(3, temperatureList);
		   	  }
		   	  catch(Exception e)
		   	  {
		   	  }	   	  
		   	  //-------------------------------------------------------------------------------------------�¶� end
			}catch(Exception e){
			}
		
			if(!(ShareData.getSharedata().containsKey(node.getIpAddress())))
			{
				Hashtable ipAllData = new Hashtable();
				if(ipAllData == null)ipAllData = new Hashtable();
				if(temperatureVector != null && temperatureVector.size()>0)ipAllData.put("temperature",temperatureVector);
			    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
			}else
			 {
				if(temperatureVector != null && temperatureVector.size()>0)((Hashtable)ShareData.getSharedata().get(node.getIpAddress())).put("temperature",temperatureVector);
			 }
			
//		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
//		if(ipAllData == null)ipAllData = new Hashtable();
//		ipAllData.put("temperature",temperatureVector);
//	    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
	    returnHash.put("temperature", temperatureVector);
	  //���¶Ƚ��и澯���
	    Hashtable collectHash = new Hashtable();
		collectHash.put("temperature", temperatureVector);
	    try{
			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_NET, "brocade","temperature");
			
			for(int i = 0 ; i < list.size() ; i ++){
				 
				AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
				//���¶�ֵ���и澯���
				CheckEventUtil checkutil = new CheckEventUtil();
				checkutil.updateData(node,collectHash,"net","temperature",alarmIndicatorsnode);
				//}
			}
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    //ipAllData=null;
	    temperatureVector=null;
	    
	    //�Ѳɼ��������sql
	    NetTemperatureResultTosql tosql=new NetTemperatureResultTosql();
	    tosql.CreateResultTosql(returnHash, node.getIpAddress());
	    String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	    if(!"0".equals(runmodel)){
			//�ɼ�������Ƿ���ģʽ,����Ҫ����������д����ʱ���
	    	NetDatatempTemperatureRtosql temptosql=new NetDatatempTemperatureRtosql();
		    temptosql.CreateResultTosql(returnHash, node);
	    }
	    
	    
	    return returnHash;
	}
}





