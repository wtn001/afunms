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
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetHostMemoryRtsql;
import com.gatherResulttosql.NetmemoryResultTosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MaipuMemorySnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public MaipuMemorySnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {	
		//yangjun
		Hashtable returnHash=new Hashtable();
		Vector memoryVector=new Vector();
		List memoryList = new ArrayList();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
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
    				//���֮ǰ�ڴ��в����ĸ澯��Ϣ
    			    try{
    			    	//���֮ǰ�ڴ��в������ڴ�澯��Ϣ
    			    	NodeDTO nodedto = null;
    					NodeUtil nodeUtil = new NodeUtil();
    					nodedto = nodeUtil.creatNodeDTOByHost(node);
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.deleteEvent(node.getId()+"", nodedto.getType(), nodedto.getSubtype(), "memory", null);
    			    }catch(Exception e){
    			    	e.printStackTrace();
    			    }
    				return returnHash;
    			}
    			
    		}
    	}
    	
		try {
			CPUcollectdata cpudata=null;
			Calendar date=Calendar.getInstance();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
			  try {
				  //-------------------------------------------------------------------------------------------�ڴ� start
		   		  String temp = "0";
		   		  if(node.getSysOid().startsWith("1.3.6.1.4.1.5651.")){
			   			String[][] valueArray = null;
			   			String[] oids =                
							  new String[] {               
								"1.3.6.1.4.1.5651.3.20.1.1.1.8",//hwMemSize
								"1.3.6.1.4.1.5651.3.20.1.1.1.1"//hwMemFree
			   			};
			   			String[] oids_ =                
							  new String[] {               
								"1.3.6.1.4.1.5651.3.600.10.1.1.9",//hwMemSize
								"1.3.6.1.4.1.5651.3.600.10.1.1.2"//hwMemFree
			   			};
			   			//valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
			   			valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
			   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
			   			if(valueArray == null || valueArray.length == 0){
			   				//valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids_, node.getSnmpversion(), 3, 1000*30);
			   				valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids_,node.getSnmpversion(),
				   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
			   			}
			   			int allvalue=0;
			   			int flag = 0;
						if(valueArray != null){
						   	  for(int i=0;i<valueArray.length;i++)
						   	  {
							   		String sizevalue = valueArray[i][0];
							   		String freevalue = valueArray[i][1];
							   		String index = valueArray[i][2];
							   		SysLogger.info(node.getIpAddress()+"   freevalue==="+freevalue);
							   		float value=0.0f;
							   		//int value=0;
							   		String usedperc = "0";
							   		if(Long.parseLong(sizevalue) > 0)
							   			value = (Long.parseLong(sizevalue)-Long.parseLong(freevalue))*100/(Long.parseLong(sizevalue));
							   		
									if( value >0){
										int intvalue = Math.round(value); 
										//intvalue = value/intvalue;
										flag = flag +1;
								   		List alist = new ArrayList();
								   		alist.add("");
								   		alist.add(usedperc);
								   		//�ڴ�
								   		memoryList.add(alist);	
								   		Memorycollectdata memorycollectdata = new Memorycollectdata();
								   		memorycollectdata.setIpaddress(node.getIpAddress());
								   		memorycollectdata.setCollecttime(date);
								   		memorycollectdata.setCategory("Memory");
								   		memorycollectdata.setEntity("Utilization");
								   		memorycollectdata.setSubentity(index);
								   		memorycollectdata.setRestype("dynamic");
								   		memorycollectdata.setUnit("");		
								   		memorycollectdata.setThevalue(intvalue+"");
										//SysLogger.info(host.getIpAddress()+" �ڴ棺 "+Integer.parseInt(intvalue+""));
										memoryVector.addElement(memorycollectdata);
									} else {
										SysLogger.info(node.getIpAddress()+"    value<0");
										oids_ = new String[] {               
												"1.3.6.1.4.1.5651.3.600.10.1.1.10"
							   			};
										//valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids_, node.getSnmpversion(), 3, 1000*30);
										valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids_,node.getSnmpversion(),
							   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
										if(valueArray != null){
										   	  for(int j=0;j<valueArray.length;j++){
										   		String utilize = valueArray[j][0];
//										   		index = valueArray[j][2];
										   		SysLogger.info(node.getIpAddress()+"   utilize==="+utilize);
										   		value = Float.parseFloat(utilize);
										   		if( value >0){
													int intvalue = Math.round(value); 
											   		//�ڴ�
											   		Memorycollectdata memorycollectdata = new Memorycollectdata();
											   		memorycollectdata.setIpaddress(node.getIpAddress());
											   		memorycollectdata.setCollecttime(date);
											   		memorycollectdata.setCategory("Memory");
											   		memorycollectdata.setEntity("Utilization");
											   		memorycollectdata.setSubentity(index);
											   		memorycollectdata.setRestype("dynamic");
											   		memorycollectdata.setUnit("");		
											   		memorycollectdata.setThevalue(intvalue+"");
											   		memoryVector.addElement(memorycollectdata);
											   		break;
										   		}
										   	}
										}
									}
						   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
						   	  }
						}	
						
		   		  } 
		   	  }
		   	  catch(Exception e)
		   	  {

		   	  }	   	  
		   	  //-------------------------------------------------------------------------------------------�ڴ� end
		}
		catch(Exception e){
			//returnHash=null;
			//e.printStackTrace();
			//return null;
		}
		
		if(!(ShareData.getSharedata().containsKey(node.getIpAddress())))
		{
			Hashtable ipAllData = new Hashtable();
			if(ipAllData == null)ipAllData = new Hashtable();
			if(memoryVector != null && memoryVector.size()>0)ipAllData.put("memory",memoryVector);
		    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		}else{
			if(memoryVector != null && memoryVector.size()>0)((Hashtable)ShareData.getSharedata().get(node.getIpAddress())).put("memory",memoryVector);
		   
		 }
	    returnHash.put("memory",memoryVector);
	    
	    Hashtable collectHash = new Hashtable();
		collectHash.put("memory", memoryVector);

	    try {
			if(memoryVector != null && memoryVector.size() > 0){
				int thevalue = 0;
				for(int i = 0 ; i < memoryVector.size(); i++){
					Memorycollectdata memorycollectdata = (Memorycollectdata)memoryVector.get(i);
					if("Utilization".equals(memorycollectdata.getEntity())){
						if(Integer.parseInt(memorycollectdata.getThevalue())>thevalue){
							thevalue = Integer.parseInt(memorycollectdata.getThevalue());
						}
					}
				}
				CheckEventUtil checkutil = new CheckEventUtil();
				checkutil.updateData(node, nodeGatherIndicators, thevalue+"",null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("CISCO �ڴ� �澯����" , e);
		}
		memoryVector=null;
	    //�Ѳɼ��������sql
	    NetmemoryResultTosql tosql=new NetmemoryResultTosql();
	    tosql.CreateResultTosql(returnHash, node.getIpAddress());
	    String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	    if(!"0".equals(runmodel)){
			//�ɼ�������Ƿ���ģʽ,����Ҫ����������д����ʱ���
	    	NetHostMemoryRtsql  totempsql=new NetHostMemoryRtsql();
		    totempsql.CreateResultTosql(returnHash, node);
	    }
	    
	    
	    return returnHash;
	}
}





