package com.afunms.polling.snmp.flash;


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

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.Flashcollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MotorolaFlashSnmp extends SnmpMonitor {
	private Hashtable sendeddata = ShareData.getProcsendeddata();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public MotorolaFlashSnmp() {
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
		Vector flashVector=new Vector();
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
			  //-------------------------------------------------------------------------------------------���� start
		   	  try {
		   		  String temp = "0";
		   		  int usedvalueperc = 0;
		   		  if(node.getSysOid().startsWith("1.3.6.1.4.1.2011.") || node.getSysOid().startsWith("1.3.6.1.4.1.25506.")){
			   			String[][] valueArray = null;
			   			String[] oids =                
							  new String[] {               
								"1.3.6.1.4.1.2011.6.1.3.1",//hwFlhTotalSize
								"1.3.6.1.4.1.2011.6.1.3.2"//hwFlhTotalFree
			   			};
			   			//valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000);
			   			valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
			   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
			   			int allvalue=0;
			   			
			   			int flag = 0;
						if(valueArray != null && valueArray.length > 0){
						   	  for(int i=0;i<valueArray.length;i++)
						   	  {
							   		String sizevalue = valueArray[i][0];
							   		String freevalue = valueArray[i][1];
							   		String index = valueArray[i][2];
							   		float value=0.0f;
							   		String usedperc = "0";
							   		if(Long.parseLong(sizevalue) > 0)
							   			value = (Long.parseLong(sizevalue)-Long.parseLong(freevalue))*100/(Long.parseLong(sizevalue));
							   		
									if( value >0){
										int intvalue = Math.round(value); 
										allvalue = allvalue +intvalue;
										//SysLogger.info(host.getIpAddress()+" ���棺 "+Integer.parseInt(intvalue+"")+" ������:"+allvalue);
										flag = flag +1;
								   		List alist = new ArrayList();
								   		alist.add("");
								   		alist.add(usedperc);
								   		//�ڴ�
								   		//flashList.add(alist);	
								   		Flashcollectdata flashcollectdata = new Flashcollectdata();
								   		flashcollectdata.setIpaddress(node.getIpAddress());
								   		flashcollectdata.setCollecttime(date);
								   		flashcollectdata.setCategory("Flash");
								   		flashcollectdata.setEntity("Utilization");
								   		flashcollectdata.setSubentity(index);
								   		flashcollectdata.setRestype("dynamic");
								   		flashcollectdata.setUnit("%");		
								   		flashcollectdata.setThevalue(intvalue+"");
										//SysLogger.info(host.getIpAddress()+" �ڴ棺 "+Integer.parseInt(intvalue+""));
										flashVector.addElement(flashcollectdata);
									}
						   	  }
						   	if(flag > 0)usedvalueperc = allvalue/flag;
//						   	//�ڴ�
//					   		Flashcollectdata flashcollectdata = new Flashcollectdata();
//					   		flashcollectdata.setIpaddress(node.getIpAddress());
//					   		flashcollectdata.setCollecttime(date);
//					   		flashcollectdata.setCategory("Flash");
//					   		flashcollectdata.setEntity("Utilization");
//					   		flashcollectdata.setSubentity("Utilization");
//					   		flashcollectdata.setRestype("dynamic");
//					   		flashcollectdata.setUnit("");		
//					   		flashcollectdata.setThevalue(usedvalueperc+"");
//							//SysLogger.info(host.getIpAddress()+" �ڴ棺 "+Integer.parseInt(usedvalueperc+""));
//							flashVector.addElement(flashcollectdata);
						}	   			  
		   		  }
		   	  }
		   	  catch(Exception e)
		   	  {
		   	  }	   	  
//				-------------------------------------------------------------------------------------------���� end
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
			if(flashVector != null && flashVector.size()>0)ipAllData.put("flash",flashVector);
		    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		}else
		 {
			if(flashVector != null && flashVector.size()>0)((Hashtable)ShareData.getSharedata().get(node.getIpAddress())).put("flash",flashVector);
		   
		 }
	    returnHash.put("flash",flashVector);
	    return returnHash;
	}
}





