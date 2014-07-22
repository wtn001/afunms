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
public class RedGiantFirewallMemorySnmp extends SnmpMonitor {
	/**
	 * 
	 */
	public RedGiantFirewallMemorySnmp() {
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
				// ��С��20120831 ����������һ��getȡֵ��ԭ�򣺽���豸s5760��S3760-48�ڴ治��ȡ��ֵ��start
				  int intvalue = 0;
				  if (node.getSysOid().startsWith("1.3.6.1.4.1.4881.1.1.10.1.75") || //s5760
					  node.getSysOid().startsWith("1.3.6.1.4.1.4881.1.1.10.1.73") || //s5760
					  node.getSysOid().startsWith("1.3.6.1.4.1.4881.1.1.10.1.49")    //S3760-48
					  ) { 
					String oid = "1.3.6.1.4.1.4881.1.1.10.2.35.1.1.1.3.0";
					SnmpUtils snmputils = new SnmpUtils();
					try {
						String value = snmputils.get(node.getIpAddress(), node.getCommunity(), oid, node.getSnmpversion(), 3, 3000);
						if (null != value && !"null".equalsIgnoreCase(value) && Float.parseFloat(value) > 0) {
							intvalue = Math.round(Float.parseFloat(value));
					   		List alist = new ArrayList();
					   		alist.add("");
					   		alist.add(value+"");
					   		memoryList.add(alist);	
					   		Memorycollectdata memorycollectdata = new Memorycollectdata();
					   		memorycollectdata.setIpaddress(node.getIpAddress());
					   		memorycollectdata.setCollecttime(date);
					   		memorycollectdata.setCategory("Memory");
					   		memorycollectdata.setEntity("1");
					   		memorycollectdata.setSubentity("");
					   		memorycollectdata.setRestype("dynamic");
					   		memorycollectdata.setUnit("");		
					   		memorycollectdata.setThevalue(intvalue+"");
							memoryVector.addElement(memorycollectdata);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				  }
				  // ��С��20120831 ����������һ��getȡֵ��ԭ�򣺽���豸s5760�ڴ治��ȡ��ֵ��end
				  if(0 == intvalue){
			   		  if(node.getSysOid().startsWith("1.3.6.1.4.1.4881.")){
				   			String[][] valueArray = null;
				   			String[] oids =                
								  new String[] {               
				   					"1.3.6.1.4.1.4881.1.1.10.2.35.1.1.1.3"//�ڴ�������
				   			};
				   			//valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
				   			valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
				   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
				   			int allvalue=0;
				   			int flag = 0;
							if(valueArray != null){
							   	  for(int i=0;i<valueArray.length;i++)
							   	  {
							   		  String _value = valueArray[i][0];
							   		  String index = valueArray[i][1];
								   		//SysLogger.info(node.getIpAddress()+"   freevalue==="+freevalue);
								   		float value=0.0f;
								   		//int value=0;
								   		String usedperc = "0";
								   		if(Long.parseLong(_value) > 0)
								   			value = Long.parseLong(_value);
								   		
										if( value >0){
											intvalue = Math.round(value); 
											flag = flag +1;
									   		List alist = new ArrayList();
									   		alist.add("");
									   		alist.add(value+"");
									   		//�ڴ�
									   		memoryList.add(alist);	
									   		Memorycollectdata memorycollectdata = new Memorycollectdata();
									   		memorycollectdata.setIpaddress(node.getIpAddress());
									   		memorycollectdata.setCollecttime(date);
									   		memorycollectdata.setCategory("Memory");
									   		memorycollectdata.setEntity(index);
									   		memorycollectdata.setSubentity("");
									   		memorycollectdata.setRestype("dynamic");
									   		memorycollectdata.setUnit("");		
									   		memorycollectdata.setThevalue(intvalue+"");
											//SysLogger.info(host.getIpAddress()+" �ڴ棺 "+Integer.parseInt(intvalue+""));
											memoryVector.addElement(memorycollectdata);
										}
							   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
							   	  }
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

//	    Hashtable collectHash = new Hashtable();
//		collectHash.put("memory", memoryVector);
//
//	    memoryVector=null;
//	    
//		//�������ڴ�ֵ���и澯���
//	    try{
//			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_FIREWALL, "redgiant","memory");
//			for(int i = 0 ; i < list.size() ; i ++){
//				AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//				//�������ڴ�ֵ���и澯���
//				CheckEventUtil checkutil = new CheckEventUtil();
//				checkutil.updateData(node,collectHash,"firewall","redgiant",alarmIndicatorsnode);
//			}
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    }
	    try {
			if(memoryVector != null && memoryVector.size() > 0){
				for(int i = 0 ; i < memoryVector.size(); i++){
					Memorycollectdata memorycollectdata = (Memorycollectdata)memoryVector.get(i);
					if("Utilization".equals(memorycollectdata.getEntity())){
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.updateData(node, nodeGatherIndicators, memorycollectdata.getThevalue(),memorycollectdata.getSubentity());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("redgiant �ڴ� �澯����" , e);
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





