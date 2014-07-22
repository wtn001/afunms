package com.afunms.polling.snmp;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import montnets.SmsDao;

import com.afunms.common.util.Arith;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.monitor.executor.base.MonitorInterface;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.Flashcollectdata;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.IpMac;
import com.afunms.polling.om.IpRouter;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Packs;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.om.UtilHdx;
import com.afunms.polling.om.UtilHdxPerc;
import com.afunms.polling.task.TaskXml;
import com.afunms.topology.model.HostNode;




/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CiscoSnmp extends SnmpMonitor implements MonitorInterface {
	private static Hashtable ifEntity_ifStatus = null;
	static {
		ifEntity_ifStatus = new Hashtable();
		ifEntity_ifStatus.put("1", "up");
		ifEntity_ifStatus.put("2", "down");
		ifEntity_ifStatus.put("3", "testing");
		ifEntity_ifStatus.put("5", "unknow");
		ifEntity_ifStatus.put("7", "unknow");
	};
	
//	1:normal
//	2:warning
//	3:critical
//	4:shutdown
//	5:notPresent
//	6:notFunctioning
	private static Hashtable supplyState_ifStatus = null;
	static {
		supplyState_ifStatus = new Hashtable();
		supplyState_ifStatus.put("1", "����");
		supplyState_ifStatus.put("2", "�澯");
		supplyState_ifStatus.put("3", "����");
		supplyState_ifStatus.put("4", "�ر�");
		supplyState_ifStatus.put("5", "���ṩ");
		supplyState_ifStatus.put("6", "����������");
	};
	
	private static Hashtable soltage_state = null;
	static {
		soltage_state = new Hashtable();
		soltage_state.put("1", "����");
		soltage_state.put("2", "�澯");
		soltage_state.put("3", "����");
		soltage_state.put("4", "�ر�");
		soltage_state.put("5", "���ṩ");
		soltage_state.put("6", "����������");
	};
	
	   public CiscoSnmp()
	   {
	   }
	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	   
	   public Hashtable collect_Data(HostNode node)
	   {  
		   Calendar date=Calendar.getInstance();
		   
		   Vector cpuVector=new Vector();
		   Vector memoryVector=new Vector();
		   Vector flashVector=new Vector();
		   Vector fanVector=new Vector();
		   Vector powerVector=new Vector();
		   Vector voltageVector=new Vector();
		   Vector systemVector=new Vector();
		   Vector ipmacVector = new Vector();
		   Vector iprouterVector = new Vector();
		   Vector interfaceVector=new Vector();
		   Vector utilhdxpercVector = new Vector();
		   Vector utilhdxVector=new Vector();
		   Vector packsVector = new Vector();
		   Vector inpacksVector = new Vector();
		   Vector outpacksVector = new Vector();
		   Vector inpksVector = new Vector();
		   Vector outpksVector = new Vector();
			Vector discardspercVector = new Vector();
			Vector errorspercVector = new Vector();
			Vector allerrorspercVector = new Vector();
			Vector alldiscardspercVector = new Vector();
			Vector allutilhdxpercVector=new Vector();
			Vector allutilhdxVector=new Vector();
			Vector fdbVector=new Vector();
			Vector temperatureVector=new Vector();
			
		   HostNode host = (HostNode)node;
	   	   CPUcollectdata cpudata=null;	
	   	   Systemcollectdata systemdata=null;
	   	   Interfacecollectdata interfacedata=null;
			//AllUtilHdxPerc allutilhdxperc=null;
			AllUtilHdx allutilhdx=null;
			UtilHdxPerc utilhdxperc=null;
			UtilHdx utilhdx=null;
			InPkts inpacks = null;
			OutPkts outpacks = null;
			Hashtable MACVSIP = new Hashtable();
			
		   
	   	  int result = 0;
	   	  List cpuList = new ArrayList();
		  List memoryList = new ArrayList();
		  List temperatureList = new ArrayList();
		  List fanList = new ArrayList();
		  List powerList = new ArrayList();
		  List voltageList = new ArrayList();
		  try{
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
			  Date cc = date.getTime();
			  String time = sdf.format(cc);
			  snmpnode.setLastTime(time);
		  }catch(Exception e){
			  
		  }
	   	  try {

//				-------------------------------------------------------------------------------------------cpu start
	   		  String temp = snmp.getMibValue(host.getIpAddress(),host.getCommunity(),"1.3.6.1.4.1.9.2.1.57.0");
	   			String[][] valueArray = null;
	   			String[] oids =                
					  new String[] {               
						"1.3.6.1.4.1.9.2.1.57.0"};
	   			valueArray = snmp.getCpuTableData(host.getIpAddress(),host.getCommunity(),oids);
	   			int allvalue=0;
	   			int flag = 0;
				if(valueArray != null){
					SysLogger.info(host.getIpAddress()+" cpu "+valueArray.length);
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		SysLogger.info(host.getIpAddress()+" cpu value "+valueArray[i][0]);
				   		String _value = valueArray[i][0];
				   		String index = valueArray[i][1];
				   		int value=0;
				   		try{
				   			value=Integer.parseInt(_value);
				   			allvalue = allvalue+Integer.parseInt(_value);
				   		}catch(Exception e){
				   			//e.printStackTrace();
				   		}
						if(value >0){
							flag = flag +1;
					   		List alist = new ArrayList();
					   		alist.add(index);
					   		alist.add(_value);
					   		cpuList.add(alist);
						}
				   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
				   	  }
				}
				
				if(flag >0){
					int intvalue = (allvalue/flag);
					temp = intvalue+"";
					SysLogger.info(host.getIpAddress()+" cpu "+allvalue/flag);
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
	   		  
	   		  //SysLogger.info(host.getIpAddress() + "_CiscoSnmp value="+result );
			  cpudata=new CPUcollectdata();
			  cpudata.setIpaddress(node.getIpAddress());
			  cpudata.setCollecttime(date);
			  cpudata.setCategory("CPU");
			  cpudata.setEntity("Utilization");
			  cpudata.setSubentity("Utilization");
			  cpudata.setRestype("dynamic");
			  cpudata.setUnit("%");		
			  cpudata.setThevalue(result+"");
				
			  //if (cpudata != null && !cpuusage.equalsIgnoreCase("noSuchObject"))
			  	cpuVector.addElement(cpudata);
				  if(cpuList != null && cpuList.size()>0){
					  cpuVector.add(1, cpuList);
				  }
	   	  }
	   	  catch(Exception e)
	   	  {
	   		  e.printStackTrace();
	   		  result = -1;    		  
	   		  SysLogger.error(host.getIpAddress() + "_CiscoSnmp ��ȡCPU��Ϣ����####");
	   	  }	   	  
//			-------------------------------------------------------------------------------------------cpu end	

	   	  try {
//				-------------------------------------------------------------------------------------------�ڴ� start
	   		  String temp = "0";
	   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.2011.")){
	   		  }else if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
	   			String[][] valueArray = null;
	   			String[] oids =                
					  new String[] {               
						"1.3.6.1.4.1.9.9.48.1.1.1.5",//ciscoMemoryPoolUsed
						"1.3.6.1.4.1.9.9.48.1.1.1.6"//ciscoMemoryPoolFree
	   			};
	   			valueArray = snmp.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids);
	   			int allvalue=0;
	   			int flag = 0;
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		String usedvalue = valueArray[i][0];
				   		String freevalue = valueArray[i][1];
				   		String index = valueArray[i][2];
				   		//SysLogger.info(host.getIpAddress()+"   usedvalue==="+usedvalue);
				   		float value=0.0f;
				   		//int value=0;
				   		String usedperc = "0";
				   		try{
				   		if(Long.parseLong(freevalue)+Long.parseLong(usedvalue) > 0)
				   			value = Long.parseLong(usedvalue)*100/(Long.parseLong(freevalue)+Long.parseLong(usedvalue));
				   		}catch(Exception e){
				   			
				   		}
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
							SysLogger.info(host.getIpAddress()+" �ڴ棺 "+Integer.parseInt(intvalue+""));
							memoryVector.addElement(memorycollectdata);
					   		
						}
				   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
				   	  }
				}
	   		  }
	   		//memoryVector.add(memoryList);
	   	  }
	   	  catch(Exception e)
	   	  {
	   		  e.printStackTrace();
	   		  result = -1;    		  
	   		  SysLogger.error(host.getIpAddress() + "_CISCOSnmp",e);
	   	  }	   	  
//			-------------------------------------------------------------------------------------------�ڴ� end	
	   	  
	   	  try {
//				-------------------------------------------------------------------------------------------���� start
	   		  String temp = "0";
	   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.2011.")){
	   		  }else if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
	   			String[][] valueArray = null;
	   			String[] oids =                
					  new String[] {               
						"1.3.6.1.4.1.9.9.10.1.1.4.1.1.10",//flash partition
						"1.3.6.1.4.1.9.9.10.1.1.4.1.1.4",//Size of flash partition
						"1.3.6.1.4.1.9.9.10.1.1.4.1.1.5"//Free space of the flash device partition
	   			};
	   			valueArray = snmp.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids);
	   			int allvalue=0;
	   			int flag = 0;
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		String partitionname = valueArray[i][0];
				   		String allflashvalue = valueArray[i][1];
				   		String freevalue = valueArray[i][2];
				   		String index = valueArray[i][3];
				   		//SysLogger.info(host.getIpAddress()+"   usedvalue==="+usedvalue);
				   		float value=0.0f;
				   		//int value=0;
				   		String usedperc = "0";
				   		try{
				   		if(Long.parseLong(allflashvalue) > 0)
				   			value = (Long.parseLong(allflashvalue)-Long.parseLong(freevalue))*100/(Long.parseLong(allflashvalue));
				   		}catch(Exception e){
				   			
				   		}
						if( value >0){
							int intvalue = Math.round(value); 
							//intvalue = value/intvalue;
							flag = flag +1;
					   		List alist = new ArrayList();
					   		alist.add("");
					   		alist.add(usedperc);
					   		//�ڴ�
					   		memoryList.add(alist);	
					   		Flashcollectdata flashcollectdata = new Flashcollectdata();
					   		flashcollectdata.setIpaddress(node.getIpAddress());
					   		flashcollectdata.setCollecttime(date);
					   		flashcollectdata.setCategory("Flash");
					   		flashcollectdata.setEntity("Utilization");
					   		flashcollectdata.setSubentity(index);
					   		flashcollectdata.setRestype("dynamic");
					   		flashcollectdata.setUnit("");
					   		flashcollectdata.setThevalue(intvalue+"");
							SysLogger.info(host.getIpAddress()+" "+partitionname+" "+index +" ���棺 "+Integer.parseInt(intvalue+""));
							flashVector.addElement(flashcollectdata);
					   		
						}
				   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
				   	  }
				}
	   		  }
	   		//memoryVector.add(memoryList);
	   	  }
	   	  catch(Exception e)
	   	  {
	   		  e.printStackTrace();
	   		  result = -1;    		  
	   		  SysLogger.error(host.getIpAddress() + "_CISCOSnmp",e);
	   	  }	   	  
//			-------------------------------------------------------------------------------------------���� end
	   	  
	   	  
//	   	try {
////			-------------------------------------------------------------------------------------------IOS start
//   		  String temp = "0";
//   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
//   			String[][] valueArray = null;
//   			String[] oids =                
//				  new String[] {               
//					"1.3.6.1.4.1.9.9.25.1.1.1.2"//flash partition
//					//"1.3.6.1.4.1.9.9.10.1.1.4.1.1.4",//Size of flash partition
//					//"1.3.6.1.4.1.9.9.10.1.1.4.1.1.5"//Free space of the flash device partition
//   			};
//   			valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
//   			int allvalue=0;
//   			int flag = 0;
//			if(valueArray != null){
//			   	  for(int i=0;i<valueArray.length;i++)
//			   	  {
//			   		String partitionname = valueArray[i][0];
////			   		String allflashvalue = valueArray[i][1];
////			   		String freevalue = valueArray[i][2];
//			   		//String index = valueArray[i][1];
//			   		SysLogger.info(host.getIpAddress()+"   iosvalue==="+partitionname);
////			   		float value=0.0f;
////			   		//int value=0;
////			   		String usedperc = "0";
////			   		if(Long.parseLong(allflashvalue) > 0)
////			   			value = (Long.parseLong(allflashvalue)-Long.parseLong(freevalue))*100/(Long.parseLong(allflashvalue));
//			   		
////					if( value >0){
////						int intvalue = Math.round(value); 
////						//intvalue = value/intvalue;
////						flag = flag +1;
////				   		List alist = new ArrayList();
////				   		alist.add("");
////				   		alist.add(usedperc);
////				   		//�ڴ�
////				   		memoryList.add(alist);	
////				   		Flashcollectdata flashcollectdata = new Flashcollectdata();
////				   		flashcollectdata.setIpaddress(node.getIpAddress());
////				   		flashcollectdata.setCollecttime(date);
////				   		flashcollectdata.setCategory("Flash");
////				   		flashcollectdata.setEntity("Utilization");
////				   		flashcollectdata.setSubentity(index);
////				   		flashcollectdata.setRestype("dynamic");
////				   		flashcollectdata.setUnit("");		
////				   		flashcollectdata.setThevalue(intvalue+"");
////						SysLogger.info(host.getIpAddress()+" "+partitionname+" "+index +" ���棺 "+Integer.parseInt(intvalue+""));
////						flashVector.addElement(flashcollectdata);
////				   		
////					}
//			   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
//			   	  }
//			}
//   		  }
//   		//memoryVector.add(memoryList);
//   	  }
//   	  catch(Exception e)
//   	  {
//   		  e.printStackTrace();
//   		  result = -1;    		  
//   		  SysLogger.error(host.getIpAddress() + "_CISCOSnmp",e);
//   	  }	   	  
////		-------------------------------------------------------------------------------------------���� end
	   	  
	   	  try {
//				-------------------------------------------------------------------------------------------�¶� start
	   		  String temp = "0";
	   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.2011.")){
	   		  }else if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
	   			String[][] valueArray = null;
	   			String[] oids =                
					  new String[] {               
						"1.3.6.1.4.1.9.9.13.1.3.1.2",//�¶�����
						"1.3.6.1.4.1.9.9.13.1.3.1.3"//�¶�
	   			};
	   			valueArray = snmp.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids);
	   			//int allvalue=0;
	   			//int flag = 0;
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		String _value = valueArray[i][1];
				   		String index = valueArray[i][2];
				   		String desc = valueArray[i][0];
				   		int value=0;
				   		
						//allvalue = allvalue+Integer.parseInt(_value);
						if(_value != null){
							value=Integer.parseInt(_value);
							if(value > 0){
							//flag = flag +1;
					   		List alist = new ArrayList();
					   		alist.add(index);
					   		alist.add(_value);
					   		alist.add(desc);
					   		//�ڴ�
					   		temperatureList.add(alist);				   		
					   		  interfacedata = new Interfacecollectdata();
					   		  interfacedata.setIpaddress(node.getIpAddress());
					   		  interfacedata.setCollecttime(date);
					   		  interfacedata.setCategory("Temperature");
					   		  interfacedata.setEntity(index);
					   		  interfacedata.setSubentity(desc);
					   		  interfacedata.setRestype("dynamic");
					   		  interfacedata.setUnit("��");		
					   		  interfacedata.setThevalue(_value+"");
							  SysLogger.info(host.getIpAddress()+" �¶ȣ� "+_value);
							  temperatureVector.addElement(interfacedata);	
							}
						}
				   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
				   	  }
				}
	   		  } 
			  //cpuVector.add(3, temperatureList);
	   	  }
	   	  catch(Exception e)
	   	  {
	   		  e.printStackTrace();
	   		  result = -1;    		  
	   		  SysLogger.error(host.getIpAddress() + "_CiscoSnmp",e);
	   	  }	   	  
//			-------------------------------------------------------------------------------------------�¶� end	
	   	  
	   	  try {
//				-------------------------------------------------------------------------------------------���� start
	   		  String temp = "0";
	   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.2011.")){
	   		  }else if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
	   			String[][] valueArray = null;
	   			String[] oids =                
					  new String[] {               
						"1.3.6.1.4.1.9.9.13.1.4.1.2",//����
						"1.3.6.1.4.1.9.9.13.1.4.1.3"//״̬
	   			};
	   			valueArray = snmp.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids);
	   			int flag = 0;
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		String _value = valueArray[i][1];
				   		String index = valueArray[i][2];
				   		String desc = valueArray[i][0];
				   		int value=0;
				   		try{
				   			value=Integer.parseInt(_value);
				   		}catch(Exception e){
				   			
				   		}
						//if(value > 0){
							flag = flag +1;
					   		List alist = new ArrayList();
					   		alist.add(index);
					   		alist.add(_value);
					   		alist.add(desc);
					   		//����
					   		fanList.add(alist);				   		
					   		  interfacedata = new Interfacecollectdata();
					   		  interfacedata.setIpaddress(node.getIpAddress());
					   		  interfacedata.setCollecttime(date);
					   		  interfacedata.setCategory("Fan");
					   		  interfacedata.setEntity(index);
					   		  interfacedata.setSubentity(desc);
					   		  interfacedata.setRestype("dynamic");
					   		  interfacedata.setUnit("");		
					   		  interfacedata.setThevalue(_value);
							  SysLogger.info(host.getIpAddress()+" ����״̬�� "+_value);
							  fanVector.addElement(interfacedata);		   		
						//}
				   		//SysLogger.info(host.getIpAddress()+"  "+index+"   value="+value);
				   	  }
				}
	   		  } 
			  //fanVector.add(fanList);
	   	  }
	   	  catch(Exception e)
	   	  {
	   		  e.printStackTrace();
	   		  result = -1;    		  
	   		  SysLogger.error(host.getIpAddress() + "_CiscoSnmp",e);
	   	  }	   	  
//			-------------------------------------------------------------------------------------------���� end		   	  

	   	  try {
//				-------------------------------------------------------------------------------------------��Դ start
	   		  String temp = "0";
	   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.2011.")){
	   		  }else if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
	   			String[][] valueArray = null;
	   			String[] oids =                
					  new String[] {               
						"1.3.6.1.4.1.9.9.13.1.5.1.2",//����
						"1.3.6.1.4.1.9.9.13.1.5.1.3"//״̬
	   			};
	   			valueArray = snmp.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids);
	   			int flag = 0;
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		String _value = valueArray[i][1];
				   		String index = valueArray[i][2];
				   		String desc = valueArray[i][0];
				   		int value=0;
				   		try{
				   			value=Integer.parseInt(_value);
				   		}catch(Exception e){
				   			
				   		}
						flag = flag +1;
				   		List alist = new ArrayList();
				   		alist.add(index);
				   		alist.add(_value);
				   		alist.add(desc);
				   		powerList.add(alist);				   		
				   		  interfacedata = new Interfacecollectdata();
				   		  interfacedata.setIpaddress(node.getIpAddress());
				   		  interfacedata.setCollecttime(date);
				   		  interfacedata.setCategory("Power");
				   		  interfacedata.setEntity(index);
				   		  interfacedata.setSubentity(desc);
				   		  interfacedata.setRestype("dynamic");
				   		  interfacedata.setUnit("");		
				   		  interfacedata.setThevalue(_value);
						  SysLogger.info(host.getIpAddress()+" ��Դ״̬�� "+_value);
						  powerVector.addElement(interfacedata);		   		
				   	  }
				}
	   		  } 
			  //powerVector.add(powerList);
	   	  }
	   	  catch(Exception e)
	   	  {
	   		  e.printStackTrace();
	   		  result = -1;    		  
	   		  SysLogger.error(host.getIpAddress() + "_CiscoSnmp",e);
	   	  }	   	  
//			-------------------------------------------------------------------------------------------��Դ end
	   	  
	   	  try {
//				-------------------------------------------------------------------------------------------��ѹ start
	   		  String temp = "0";
	   		  if(host.getSysOid().startsWith("1.3.6.1.4.1.9.")){
	   			String[][] valueArray = null;
	   			String[] oids =                
					  new String[] {               
						"1.3.6.1.4.1.9.9.13.1.2.1.3",//ciscoEnvMonVoltageStatusValue
						"1.3.6.1.4.1.9.9.13.1.2.1.7"//ciscoEnvMonVoltageState
	   			};
	   			valueArray = snmp.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids);
	   			int flag = 0;
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		String _value = valueArray[i][0];
				   		String index = valueArray[i][1];
				   		String state = valueArray[i][2];
				   		int value=0;
				   		try{
				   			value=Integer.parseInt(_value);
				   		}catch(Exception e){
				   			
				   		}
						flag = flag +1;
				   		List alist = new ArrayList();
				   		alist.add(index);
				   		alist.add(_value);
				   		alist.add(state);
				   		voltageList.add(alist);				   		
				   		  interfacedata = new Interfacecollectdata();
				   		  interfacedata.setIpaddress(node.getIpAddress());
				   		  interfacedata.setCollecttime(date);
				   		  interfacedata.setCategory("Voltage");
				   		  interfacedata.setEntity(index);
				   		  interfacedata.setSubentity(state);
				   		  interfacedata.setRestype("dynamic");
				   		  interfacedata.setUnit("");		
				   		  interfacedata.setThevalue(_value);
						  SysLogger.info(host.getIpAddress()+" ��ѹ״̬�� "+_value);
						  voltageVector.addElement(interfacedata);		   		
				   	  }
				}
	   		  } 
			  //powerVector.add(powerList);
	   	  }
	   	  catch(Exception e)
	   	  {
	   		  e.printStackTrace();
	   		  result = -1;    		  
	   		  SysLogger.error(host.getIpAddress() + "_CiscoSnmp",e);
	   	  }	   	  
//			-------------------------------------------------------------------------------------------��ѹ end
	   	  
//			-------------------------------------------------------------------------------------------system start			
		  try{
			
			final String[] desc=SnmpMibConstants.NetWorkMibSystemDesc;
			final String[] chname=SnmpMibConstants.NetWorkMibSystemChname;
					  String[] oids =                
						  new String[] {               
							"1.3.6.1.2.1.1.1" ,
							"1.3.6.1.2.1.1.3" ,
							"1.3.6.1.2.1.1.4" ,
							"1.3.6.1.2.1.1.5" ,
							"1.3.6.1.2.1.1.6" ,
							"1.3.6.1.2.1.1.7" 
							
							  };
					  
			String[][] valueArray = null;   	  
			try {
				valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
			} catch(Exception e){
				valueArray = null;
				SysLogger.error(host.getIpAddress() + "_CiscoSnmp");
			}
		   	  for(int i=0;i<valueArray.length;i++)
		   	  {
		   		 for(int j=0;j<6;j++){
		   			systemdata=new Systemcollectdata();
					systemdata.setIpaddress(host.getIpAddress());
					systemdata.setCollecttime(date);
					systemdata.setCategory("System");
					systemdata.setEntity(desc[i]);
					systemdata.setSubentity(desc[j]);
					systemdata.setChname(chname[j]);
					systemdata.setRestype("static");
					systemdata.setUnit("");
					String value = valueArray[i][j];
					if (j==0){
						//if (value.length()>100 && value.split(":").length>5){
							systemdata.setThevalue(value);
						//}
					}else
						systemdata.setThevalue(value);
					systemVector.addElement(systemdata);
		   		 }
		   	  }	
		  }
		  catch(Exception e){e.printStackTrace();}
		  //---------------------------------------------------system end		
		  
		  //---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip start
		     try
		     {
		        String[] oids = new String[]
		                    {"1.3.6.1.2.1.4.22.1.1",   //1.ifIndex
		        		     "1.3.6.1.2.1.4.22.1.2",   //2.mac
		                     "1.3.6.1.2.1.4.22.1.3",   //3.ip
		                     "1.3.6.1.2.1.4.22.1.4"};  //4.type
				String[][] valueArray = null;   	  
				try {
					valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
				} catch(Exception e){
					valueArray = null;
					e.printStackTrace();
					SysLogger.error(host.getIpAddress() + "_CiscoSnmp");
				}
			   	  for(int i=0;i<valueArray.length;i++)
			   	  {
			   		  IpMac ipmac = new IpMac();
			   		  for(int j=0;j<4;j++){
			   			String sValue = valueArray[i][j];
			   			//SysLogger.info("MAC===="+sValue);
			   			if(sValue == null)continue;
						if(j==0){
							ipmac.setIfindex(sValue);
						}else if (j==1){
							ipmac.setMac(sValue);
						}else if (j==2){
							ipmac.setIpaddress(sValue);									
						}
			   		 }
			   		ipmac.setIfband("0");
			   		ipmac.setIfsms("0");
					ipmac.setCollecttime(new GregorianCalendar());
					ipmac.setRelateipaddr(host.getIpAddress());
					ipmacVector.addElement(ipmac);
					//SysLogger.info("ARP hostip==>"+host.getIpAddress()+"=="+ipmac.getMac()+"====="+ipmac.getIpaddress());
					if(ipmac != null && ipmac.getMac() != null && ipmac.getIpaddress() != null)
						MACVSIP.put(ipmac.getMac(), ipmac.getIpaddress());
			   	  }	
		    }
		    catch (Exception e)
		    {
		    	//SysLogger.error("getIpNetToMediaTable(),ip=" + address + ",community=" + community);
		        //tableValues = null;
		        e.printStackTrace();
		    }
		  
		    //---------------------------------------------------�õ�����IpNetToMedia,��ֱ������豸���ӵ�ip end	
		    
		    //---------------------------------------------------�õ�����FDB,��ֱ������豸���ӵ�ip start
		     try
		     {
		    	 List fdbList = SnmpUtil.getInstance().getFdbTable(host.getIpAddress(),host.getCommunity());
		    	 if(fdbList!=null && fdbList.size()!=0){
		    		 for(int i=0;i<fdbList.size();i++){
		    			 String[] item = new String[2];
		    			 item = (String[])fdbList.get(i);
		    			 IpMac ipmac = new IpMac();
		    			 ipmac.setIfindex(item[0]);
		    			 ipmac.setMac(item[1]);
		    			 ipmac.setIfband("0");
					   	 ipmac.setIfsms("0");
						 ipmac.setCollecttime(new GregorianCalendar());
						 ipmac.setRelateipaddr(host.getIpAddress());
						 if(MACVSIP != null && MACVSIP.containsKey(ipmac.getMac())){
							 ipmac.setIpaddress((String)MACVSIP.get(ipmac.getMac()));
						 }else{
							 continue;
							 //ipmac.setIpaddress("");
						 }
						 ipmac.setRelateipaddr(host.getIpAddress());//���ý�����IP
						 //SysLogger.info("FDB hostip : "+host.getIpAddress()+"  ip :"+ipmac.getIpaddress()+" ifIndex : "+ipmac.getIfindex()+" MAC : "+ipmac.getMac()) ;
						 fdbVector.add(ipmac);
		    		 }
		    	 }
		    		 //fdbVector.add(fdbList);

		    }
		    catch (Exception e)
		    {
		        e.printStackTrace();
		    }
		  
		    //---------------------------------------------------�õ�����FDB,��ֱ������豸���ӵ�ip end		    
		    
	   	  
		    //---------------------------------------------------��ip router table�еõ�����豸������·���� start
		     try
		     {
		        String[] oids = new String[]
						                    {"1.3.6.1.2.1.4.21.1.2",       //0.if index
						                	 "1.3.6.1.2.1.4.21.1.1",       //1.ipRouterDest        		       
						                     "1.3.6.1.2.1.4.21.1.7",       //7.ipRouterNextHop
						                     "1.3.6.1.2.1.4.21.1.8",       //8.ipRouterType
						                     "1.3.6.1.2.1.4.21.1.9",       //9.ipRouterProto
						                     "1.3.6.1.2.1.4.21.1.11"};     //11.ipRouterMask
		        
				String[][] valueArray = null;   	  
				try {
					valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
				} catch(Exception e){
					valueArray = null;
					e.printStackTrace();
					SysLogger.error(host.getIpAddress() + "_CiscoSnmp");
				}
		        
			   	  for(int i=0;i<valueArray.length;i++)
			   	  {
			   		  if(valueArray[i][1] == null)continue;
						String _vbString = valueArray[i][1].toString();
						if (_vbString!=null){
							if (_vbString.equals("0.0.0.0")||_vbString.startsWith("127.0"))continue;
						}
						_vbString = valueArray[i][5].toString();
						if (_vbString!=null){
							if(_vbString.equals("0.0.0.0"))continue;
						}
						IpRouter iprouter = new IpRouter();
						iprouter.setRelateipaddr(host.getIpAddress());
						for(int j=0;j<6;j++){
							if(valueArray[i][j]!=null){
								String sValue=valueArray[i][j].toString();								
								if(j==0){
									iprouter.setIfindex(sValue);
								}else if (j==1){
									iprouter.setDest(sValue);
								}else if (j==2){
									iprouter.setNexthop(sValue);
								}else if (j==3){
									iprouter.setType(new Long(sValue));
								}else if (j==4){
									iprouter.setProto(new Long(sValue));
								}else if (j==5){
									iprouter.setMask(sValue);
								}
							}
						}
						iprouterVector.addElement(iprouter);
			   	  }	
		    }
		    catch (Exception e)
		    {
		    	//SysLogger.error("getIpNetToMediaTable(),ip=" + address + ",community=" + community);
		        //e.printStackTrace();
		    }			    		  
//         ---------------------------------------------------��ip router table�еõ�����豸������·���� end		
		    
//			-------------------------------------------------------------------------------------------interface start			
			  try{
				I_HostLastCollectData lastCollectDataManager=new HostLastCollectDataManager();
				Hashtable hash=ShareData.getOctetsdata(host.getIpAddress());
				//ȡ����ѯ���ʱ��
				TaskXml taskxml=new TaskXml();
				Task task=taskxml.GetXml("netcollecttask");
				int interval=getInterval(task.getPolltime().floatValue(),task.getPolltimeunit());
				Hashtable hashSpeed=new Hashtable();
				Hashtable octetsHash = new Hashtable();
				if (hash==null)hash=new Hashtable();
						  String[] oids =                
							  new String[] {               
								"1.3.6.1.2.1.2.2.1.1", 
								"1.3.6.1.2.1.2.2.1.2",
								"1.3.6.1.2.1.2.2.1.3",
								"1.3.6.1.2.1.2.2.1.4",
								"1.3.6.1.2.1.2.2.1.5",
								"1.3.6.1.2.1.2.2.1.6",
								"1.3.6.1.2.1.2.2.1.7",//ifAdminStatus 6
								"1.3.6.1.2.1.2.2.1.8",//ifOperStatus 7
								"1.3.6.1.2.1.2.2.1.9",//ifLastChange 8
								"1.3.6.1.2.1.31.1.1.1.1",				
								  };
				String[] oids1=                
					 new String[] {     
					//"1.3.6.1.2.1.2.2.1.1",	
					"1.3.6.1.2.1.2.2.1.10",  //ifInOctets 0        
					"1.3.6.1.2.1.2.2.1.11",//ifInUcastPkts 1
					"1.3.6.1.2.1.2.2.1.12",//ifInNUcastPkts 2
					"1.3.6.1.2.1.2.2.1.13",//ifInDiscards 3
					"1.3.6.1.2.1.2.2.1.14",//ifInErrors 4
					"1.3.6.1.2.1.2.2.1.16", //ifOutOctets 5
					"1.3.6.1.2.1.2.2.1.17",//ifOutUcastPkts 6
					"1.3.6.1.2.1.2.2.1.18",//ifOutNUcastPkts 7
					"1.3.6.1.2.1.2.2.1.19",	//ifOutDiscards 8
					"1.3.6.1.2.1.2.2.1.20"//ifOutErrors 9								
					};				 
								
				
				final String[] desc=SnmpMibConstants.NetWorkMibInterfaceDesc0;
				final String[] unit=SnmpMibConstants.NetWorkMibInterfaceUnit0;
				final String[] chname=SnmpMibConstants.NetWorkMibInterfaceChname0;
				final int[] scale=SnmpMibConstants.NetWorkMibInterfaceScale0;
				final String[] desc1=SnmpMibConstants.NetWorkMibInterfaceDesc1;
				final String[] chname1=SnmpMibConstants.NetWorkMibInterfaceChname1;
				final String[] unit1=SnmpMibConstants.NetWorkMibInterfaceUnit1;
				final int[] scale1=SnmpMibConstants.NetWorkMibInterfaceScale1;
				
				String[][] valueArray = null;   	  
				try {
					valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
				} catch(Exception e){
					valueArray = null;
					e.printStackTrace();
					SysLogger.error(host.getIpAddress() + "_H3CSnmp");
				}
				String[][] valueArray1 = null;   	  
				try {
					valueArray1 = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids1);
				} catch(Exception e){
					valueArray1 = null;
					e.printStackTrace();
					SysLogger.error(host.getIpAddress() + "_H3CSnmp");
				}
			
				long allSpeed=0;
				long allOutOctetsSpeed=0;
				long allInOctetsSpeed=0;
				long allOctetsSpeed=0;
				
				long allinpacks=0;
				long inupacks=0;//��ڵ����
				long innupacks=0;//�ǵ���
				long indiscards=0;
				long inerrors=0;
				long alloutpacks=0;
				long outupacks=0;//���ڵ���
				long outnupacks=0;//�ǵ���
				long outdiscards=0;
				long outerrors=0;
				long alldiscards=0;
				long allerrors=0;
				long allpacks=0;
			
				Vector tempV=new Vector();
				Hashtable tempHash = new Hashtable();
				if(valueArray != null){
			   	  for(int i=0;i<valueArray.length;i++)
			   	  {				   		  
						//String vbString=valueArray[i][0].toString();
			   		  if(valueArray[i][0] == null)continue;
						String sIndex=valueArray[i][0].toString();				
						tempV.add(sIndex);
						tempHash.put(i, sIndex);
						
						for(int j=0;j<10;j++){
								//��Ԥ��״̬��ifLastChange���˵�
								//if (j==6 || j==8)continue;
								if (j==8)continue;
								
								String sValue=valueArray[i][j];	
								
								interfacedata=new Interfacecollectdata();
								interfacedata.setIpaddress(host.getIpAddress());
								interfacedata.setCollecttime(date);
								interfacedata.setCategory("Interface");
								//if (desc[j].equals("ifAdminStatus"))continue;
								interfacedata.setEntity(desc[j]);
								interfacedata.setSubentity(sIndex);
								//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
								if(j==7)
									interfacedata.setRestype("static");
								else {
									interfacedata.setRestype("static");
								} 
								interfacedata.setUnit(unit[j]);
								

								if((j==4)&&sValue!=null){//����
//									long lValue=Long.parseLong(sValue)/scale[j];	
									long lValue=Long.parseLong(sValue);//yangjun
										hashSpeed.put(sIndex,Long.toString(lValue));
									allSpeed=allSpeed+lValue;					
								}
								if((j==6 || j==7)&&sValue!=null){//Ԥ��״̬�͵�ǰ״̬
		
									if (ifEntity_ifStatus.get(sValue) != null){
										interfacedata.setThevalue(ifEntity_ifStatus.get(sValue).toString());
                                       
									}else{
										interfacedata.setThevalue("0.0");
									}
									if(j == 7){
										//�˿ڸ澯�ж�yangjun
										PortconfigDao configdao = new PortconfigDao(); 			
			 							Portconfig portconfig = null;
										try {
											portconfig = configdao.getByipandindex(host.getIpAddress(),sIndex);
										} catch (RuntimeException e) {
											e.printStackTrace();
										} finally {
											configdao.close();
										}
			 							try {
											if (portconfig != null){
												if (portconfig.getSms().intValue()==1){
													//SysLogger.info("sValue-----------------"+sValue);
													if(ifEntity_ifStatus.get(sValue) != null){
														if (!"up".equalsIgnoreCase(ifEntity_ifStatus.get(sValue).toString())){
															createSMS("network","interface",host.getIpAddress(),host.getId()+"",host.getIpAddress()+" �˿� "+portconfig.getName()+" down",2,1,sIndex,host.getBid());
														}
													}
													
												}
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
									
								}
								else if((j==2)&&sValue!=null){//�˿�����
									if (Interface_IfType.get(sValue) != null){
										interfacedata.setThevalue(Interface_IfType.get(sValue).toString());
									}else{
										interfacedata.setThevalue("0.0");	
									}
								}
								else{
									if(scale[j]==0){
										interfacedata.setThevalue(sValue);
									}
									else{
										if(sValue != null){
											interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale[j]));
										}else{
											interfacedata.setThevalue("0");
										}
										
									}
								}
								
								interfacedata.setChname(chname[j]);
								interfaceVector.addElement(interfacedata);
						   } //end for j
			   	  } //end for valueArray
				}
				if(valueArray1!= null){
				for(int i=0;i<valueArray1.length;i++){
					allinpacks=0;
					inupacks=0;//��ڵ����
					innupacks=0;//�ǵ���
					indiscards=0;
					inerrors=0;
					alloutpacks=0;
					outupacks=0;//���ڵ���
					outnupacks=0;//�ǵ���
					outdiscards=0;
					outerrors=0;																		

					String sIndex = (String)tempHash.get(i);				
					if (tempV.contains(sIndex)){
										
						for(int j=0;j<10;j++){																														
							if(valueArray1[i][j]!=null){
								String sValue=valueArray1[i][j];
								//if (j==6)continue;
								interfacedata=new Interfacecollectdata();
								if(scale1[j]==0){
									interfacedata.setThevalue(sValue);
								}
								else{
									interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale1[j]));
								}
								if (j==1 || j==2){
									//��ڵ��������ݰ�,��ڷǵ��������ݰ�												
									if (sValue != null){
										allinpacks=allinpacks+Long.parseLong(sValue);
										
										Calendar cal=(Calendar)hash.get("collecttime");
										long timeInMillis=0;
										if(cal!=null)timeInMillis=cal.getTimeInMillis();
										long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
										
										inpacks=new InPkts();
										inpacks.setIpaddress(host.getIpAddress());
										inpacks.setCollecttime(date);
										inpacks.setCategory("Interface");
										String chnameBand="";
										if(j==1){
											inpacks.setEntity("ifInUcastPkts");
											chnameBand="����";
										}
										if(j==2){
											inpacks.setEntity("ifInNUcastPkts");
											chnameBand="�ǵ���";
										}
										inpacks.setSubentity(sIndex);
										inpacks.setRestype("dynamic");
										inpacks.setUnit("��");	
										inpacks.setChname(chnameBand);
										long currentPacks=Long.parseLong(sValue);												
										long lastPacks=0;	
										long l=0;											
												
										//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
										if(longinterval<2*interval){
											String lastvalue="";
											
											if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
											//ȡ���ϴλ�õ�Octets
											if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
										}
							
										if(longinterval!=0){
											if(currentPacks<lastPacks){
												currentPacks=currentPacks+4294967296L;
											}
											//������-ǰ����
											//SysLogger.info(host.getIpAddress()+"==="+sIndex+"�Ͽ�==="+currentPacks+"===="+lastPacks);
											long octetsBetween=currentPacks-lastPacks;
											l=octetsBetween;
											if(lastPacks == 0)l=0;
										}
										inpacks.setThevalue(Long.toString(l));	
										//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
										if (cal != null)
											inpacksVector.addElement(inpacks);	
									}
										//continue;
								}
								if (j==3){
									//��ڶ��������ݰ�
									if (sValue != null) indiscards=Long.parseLong(sValue);
										continue;
								}
								if (j==4){
									//��ڴ�������ݰ�
									if (sValue != null) inerrors=Long.parseLong(sValue);
									continue;
								}	
								if (j==6 || j==7){
									//��ڵ��������ݰ�,��ڷǵ��������ݰ�
									if (sValue != null){
										alloutpacks=alloutpacks+Long.parseLong(sValue);
										
										Calendar cal=(Calendar)hash.get("collecttime");
										long timeInMillis=0;
										if(cal!=null)timeInMillis=cal.getTimeInMillis();
										long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
										
										outpacks=new OutPkts();
										outpacks.setIpaddress(host.getIpAddress());
										outpacks.setCollecttime(date);
										outpacks.setCategory("Interface");
										String chnameBand="";
										
										if(j==6){
											outpacks.setEntity("ifOutUcastPkts");
											chnameBand="����";
										}
										if(j==7){
											outpacks.setEntity("ifOutNUcastPkts");
											chnameBand="�ǵ���";
										}
										outpacks.setSubentity(sIndex);
										outpacks.setRestype("dynamic");
										outpacks.setUnit("��");	
										outpacks.setChname(chnameBand);
										long currentPacks=Long.parseLong(sValue);												
										long lastPacks=0;	
										long l=0;											
												
										//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
										if(longinterval<2*interval){
											String lastvalue="";
											
											if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
											//ȡ���ϴλ�õ�Octets
											if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
										}
							
										if(longinterval!=0){
											if(currentPacks<lastPacks){
												currentPacks=currentPacks+4294967296L;
											}
											//������-ǰ����	
											long octetsBetween=currentPacks-lastPacks;
											l=octetsBetween;
											if(lastPacks == 0)l=0;
										}
										outpacks.setThevalue(Long.toString(l));	
										//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
										if (cal != null)
											outpacksVector.addElement(outpacks);
										
									}
									//continue;
								}
								if (j==8){
									//��ڶ��������ݰ�
									if (sValue != null) outdiscards=Long.parseLong(sValue);
									continue;
								}
								if (j==9){
									//��ڴ�������ݰ�
									if (sValue != null) outerrors=Long.parseLong(sValue);
									continue;
								}
								/*
								interfacedata=new Interfacecollectdata();
								if(scale1[j]==0){
									interfacedata.setThevalue(sValue);
								}
								else{
									interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale1[j]));
								}
								*/			
								Calendar cal=(Calendar)hash.get("collecttime");
								long timeInMillis=0;
								if(cal!=null)timeInMillis=cal.getTimeInMillis();
								long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;											

								//����ÿ���˿����ټ�������
								if(j==0 || j==5){
//									System.out.println(host.getIpAddress()+"=--------------------="+sValue);
									utilhdx=new UtilHdx();
									utilhdx.setIpaddress(host.getIpAddress());
									utilhdx.setCollecttime(date);
									utilhdx.setCategory("Interface");
									String chnameBand="";
									if(j==0){
										utilhdx.setEntity("InBandwidthUtilHdx");
										chnameBand="���";
									}
									if(j==5){
										utilhdx.setEntity("OutBandwidthUtilHdx");
										chnameBand="����";
										}
									utilhdx.setSubentity(sIndex);
									utilhdx.setRestype("dynamic");
									utilhdx.setUnit(unit1[j]+"/��");	
									utilhdx.setChname(sIndex+"�˿�"+"����");
									long currentOctets=Long.parseLong(sValue)/scale1[j];
									long lastOctets=0;	
									long l=0;											
											
									//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
									if(longinterval<2*interval){
										String lastvalue="";
										
										if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
										//SysLogger.info(desc1[j]+":"+sIndex+"===="+lastvalue);
										//ȡ���ϴλ�õ�Octets
										if(lastvalue!=null && !lastvalue.equals(""))lastOctets=Long.parseLong(lastvalue);
									}   
						
									if(longinterval!=0){
										long octetsBetween = 0;  
										if(currentOctets<lastOctets){
											currentOctets=currentOctets+4294967296L/scale1[j];
										} 
										//������-ǰ����	
										octetsBetween=currentOctets-lastOctets;
										//SysLogger.info(sIndex+"===currentOctets:"+currentOctets+"===lastOctets:"+lastOctets+"===octetsBetween:"+octetsBetween);
										//����˿�����
										l=octetsBetween/longinterval;
										//ͳ���ܳ����ֽ�������,���ü��㣨�����롢�ۺϣ�����������
//										if(j==0 && lastOctets!=0)allInOctetsSpeed=allInOctetsSpeed+l;
//										if(j==5 && lastOctets!=0)allOutOctetsSpeed=allOutOctetsSpeed+l;
//										//SysLogger.info("allInOctetsSpeed:"+allInOctetsSpeed+"===allOutOctetsSpeed:"+allOutOctetsSpeed);
//										if(lastOctets!=0)allOctetsSpeed=allOctetsSpeed+l;
										//yangjun
										if(j==0)allInOctetsSpeed=allInOctetsSpeed+l;
										if(j==5)allOutOctetsSpeed=allOutOctetsSpeed+l;
										allOctetsSpeed=allOctetsSpeed+l;
										
									}
									DecimalFormat df=new DecimalFormat("#.##");//yangjun 
									utilhdx.setThevalue(df.format(l*8));	
									//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
									if (cal != null)
									utilhdxVector.addElement(utilhdx);												
									
									utilhdxperc=new UtilHdxPerc();
									utilhdxperc.setIpaddress(host.getIpAddress());
									utilhdxperc.setCollecttime(date);
									utilhdxperc.setCategory("Interface");
									if(j==0)utilhdxperc.setEntity("InBandwidthUtilHdxPerc");
									if(j==5)utilhdxperc.setEntity("OutBandwidthUtilHdxPerc");
									utilhdxperc.setSubentity(sIndex);
									utilhdxperc.setRestype("dynamic");
									utilhdxperc.setUnit("%");	
									utilhdxperc.setChname(sIndex+"�˿�"+chnameBand+"����������");												
									double speed=0.0;
									if (hashSpeed.get(sIndex) != null){
									speed = Double.parseDouble(hashSpeed.get(sIndex).toString());
									}else{
										speed = Double.parseDouble("0.0");
									}
									double d=0.0;
									if(speed>0){
										//���������ʣ����١�8*100/ifspeed%
										d=Arith.div(l*800,speed);
										//d=l*800/speed;
									}
									utilhdxperc.setThevalue(Double.toString(d));
									if (cal != null)
									utilhdxpercVector.addElement(utilhdxperc);	
									
												
												
								} //end j=0 j=5
								//SysLogger.info(host.getIpAddress()+"==="+desc1[j]+":"+sIndex+"===="+sValue);
								octetsHash.put(desc1[j]+":"+sIndex,interfacedata.getThevalue());
							} //valueArray1[i][j]==null
						} //end for j
						//Hashtable packhash=lastCollectDataManager.getLastPacks(nethost);
						Hashtable packhash=ShareData.getPacksdata(host.getIpAddress()+":"+sIndex);
						//Hashtable discardshash=lastCollectDataManager.getLastDiscards(nethost);
						Hashtable discardshash=ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex);
						//Hashtable errorshash=lastCollectDataManager.getLastErrors(nethost);
						Hashtable errorshash=ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex);									
						//���㴫������ݰ�
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllInCastPkts");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(allinpacks+"");
						interfacedata.setChname("��������ݰ�");
						/******************/
						Hashtable hasht = new Hashtable();									
						hasht.put("AllInCastPkts"+":"+sIndex,allinpacks+"");																		


						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllOutCastPkts");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(alloutpacks+"");
						interfacedata.setChname("���������ݰ�");
						/******************/									
						hasht.put("AllOutCastPkts"+":"+sIndex,alloutpacks+"");
						
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllInDiscards");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(indiscards+"");
						interfacedata.setChname("����ܶ�����");
						Hashtable tempDiscards = new Hashtable();
						tempDiscards.put("AllInDiscards"+":"+sIndex,indiscards+"");

						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllOutDiscards");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(outdiscards+"");
						interfacedata.setChname("�����ܶ�����");
						tempDiscards.put("AllOutDiscards"+":"+sIndex,outdiscards+"");
						
						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllInErrors");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(inerrors+"");
						interfacedata.setChname("��ڴ������");
						Hashtable errHash = new Hashtable();
						errHash.put("AllInErrors"+":"+sIndex,inerrors+"");
						//interfaceVector.addElement(interfacedata);

						interfacedata=new Interfacecollectdata();
						interfacedata.setIpaddress(host.getIpAddress());
						interfacedata.setCollecttime(date);
						interfacedata.setCategory("Interface");
						interfacedata.setEntity("AllOutErrors");
						interfacedata.setSubentity(sIndex);									
						interfacedata.setRestype("static");
						interfacedata.setUnit("��");
						interfacedata.setThevalue(outerrors+"");
						interfacedata.setChname("���ڴ������");
						errHash.put("AllOutErrors"+":"+sIndex,outerrors+"");
						//interfaceVector.addElement(interfacedata);

						String lastvalue="";
						long lastpacks=0;
						//��ڴ������ݰ�
						if (packhash != null){
							if(packhash.get("AllInCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllInCastPkts"+":"+sIndex).toString();
						}
						
						//ȡ���ϴλ�õ�packs
						if(lastvalue!=null && !lastvalue.equals("")){										
							lastpacks=Long.parseLong(lastvalue);									
						}
									
						Packs packs = new Packs();
						packs.setIpaddress(host.getIpAddress());
						packs.setCollecttime(date);
						packs.setCategory("Interface");
						packs.setEntity("InCastPkts");
						packs.setSubentity(sIndex);
						packs.setRestype("dynamic");
						packs.setUnit("��");
						packs.setChname("������ݰ�");
						if (lastpacks > 0){
							packs.setThevalue(allinpacks-lastpacks+"");
						}else{
							packs.setThevalue("0");
						}									
						packsVector.add(packs);
						
						//��ڶ�����
						lastvalue="";
						long lastdiscards=0;
						if (discardshash != null){
							if(discardshash.get("AllInDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllInDiscards"+":"+sIndex).toString();
						}
						
						//ȡ���ϴλ�õ�packs
						if(lastvalue!=null && !lastvalue.equals("")){
							lastdiscards=Long.parseLong(lastvalue);									
						}
									
						DiscardsPerc discardsPerc = new DiscardsPerc();
						discardsPerc.setIpaddress(host.getIpAddress());
						discardsPerc.setCollecttime(date);
						discardsPerc.setCategory("Interface");
						discardsPerc.setEntity("InDiscardsPerc");
						discardsPerc.setSubentity(sIndex);
						discardsPerc.setRestype("dynamic");
						discardsPerc.setUnit("%");
						discardsPerc.setChname("��ڶ�����");
						double indiscardserc=0.0;
						if (allinpacks ==0){
							indiscardserc=0;
						}else{
							if (allinpacks-lastpacks > 0){
								indiscardserc = (indiscards-lastdiscards)/(allinpacks-lastpacks);
							}else{
								indiscardserc = 0;
							}
							
						}									
						discardsPerc.setThevalue(Double.toString(indiscardserc));
						discardspercVector.add(discardsPerc);
									
						//��ڴ�����
						lastvalue="";
						long lasterrors=0;
						if (errorshash != null){
							if(errorshash.get("AllInErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllInErrors"+":"+sIndex).toString();
						}									
						//ȡ���ϴλ�õ�error
						if(lastvalue!=null && !lastvalue.equals("")){
							lasterrors=Long.parseLong(lastvalue);									
						}																		
						ErrorsPerc errorsPerc = new  ErrorsPerc();
						errorsPerc.setIpaddress(host.getIpAddress());
						errorsPerc.setCollecttime(date);
						errorsPerc.setCategory("Interface");
						errorsPerc.setEntity("InErrorsPerc");
						errorsPerc.setSubentity(sIndex);
						errorsPerc.setRestype("dynamic");
						errorsPerc.setUnit("%");
						errorsPerc.setChname("��ڴ�����");
						double inerrorsperc=0.0;
						if (allinpacks==0){
							inerrorsperc=0;
						}else{
							if (allinpacks-lastpacks > 0){
								inerrorsperc=(inerrors-lasterrors)/(allinpacks-lastpacks);
							}else{
								inerrorsperc=0;
							}
							
						}									
						errorsPerc.setThevalue(Double.toString(inerrorsperc));
						errorspercVector.add(errorsPerc);
									
						lastvalue="";
						lastpacks=0;
						lastdiscards=0;
						lasterrors=0;
						//���ڴ������ݰ�
						if (packhash != null){
							if(packhash.get("AllOutCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllOutCastPkts"+":"+sIndex).toString();
						}
						
						//ȡ���ϴλ�õ�packs
						if(lastvalue!=null && !lastvalue.equals("")){
							lastpacks=Long.parseLong(lastvalue);																		
						}
						packs = new Packs();
						packs.setIpaddress(host.getIpAddress());
						packs.setCollecttime(date);
						packs.setCategory("Interface");
						packs.setEntity("OutCastPkts");
						packs.setSubentity(sIndex);
						packs.setRestype("dynamic");
						packs.setUnit("��");
						packs.setChname("�������ݰ�");
						if (lastpacks>0){
							packs.setThevalue(alloutpacks-lastpacks+"");
						}else{
							packs.setThevalue("0");
						}
						
						packsVector.add(packs);
									
									
						//���㶪���ʺʹ�����
						if (discardshash != null){
							if(discardshash.get("AllOutDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllOutDiscards"+":"+sIndex).toString();
						}									
						//ȡ���ϴλ�õ�packs
						if(lastvalue!=null && !lastvalue.equals("")){
							lastdiscards=Long.parseLong(lastvalue);									
						}																		
						discardsPerc = new DiscardsPerc();
						discardsPerc.setIpaddress(host.getIpAddress());
						discardsPerc.setCollecttime(date);
						discardsPerc.setCategory("Interface");
						discardsPerc.setEntity("OutDiscardsPerc");
						discardsPerc.setSubentity(sIndex);
						discardsPerc.setRestype("dynamic");
						discardsPerc.setUnit("%");
						discardsPerc.setChname("���ڶ�����");
						double outdiscardserc=0.0;
						if (alloutpacks==0){
							outdiscardserc = 0;
						}else{
							if (alloutpacks-lastpacks>0){
								outdiscardserc = (outdiscards-lastdiscards)/(alloutpacks-lastpacks);
							}else{
								outdiscardserc = 0;
							}
							
						}
									
						discardsPerc.setThevalue(Double.toString(outdiscardserc));
						discardspercVector.add(discardsPerc);
						
						lastvalue="";
						if (errorshash != null){
							if(errorshash.get("AllOutErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllOutErrors"+":"+sIndex).toString();
						}									
						//ȡ���ϴλ�õ�packs
						if(lastvalue!=null && !lastvalue.equals("")){
							lasterrors=Long.parseLong(lastvalue);									
						}																											
						errorsPerc = new  ErrorsPerc();
						errorsPerc.setIpaddress(host.getIpAddress());
						errorsPerc.setCollecttime(date);
						errorsPerc.setCategory("Interface");
						errorsPerc.setEntity("OutErrorsPerc");
						errorsPerc.setSubentity(sIndex);
						errorsPerc.setRestype("dynamic");
						errorsPerc.setUnit("%");
						errorsPerc.setChname("���ڴ�����");
						double outerrorsperc=0.0;
						if (alloutpacks>0){
							if ((alloutpacks-lastpacks)>0){
								outerrorsperc=(outerrors-lasterrors)/(alloutpacks-lastpacks);
							}else{
								outerrorsperc=0;
							}
							
						}									
						errorsPerc.setThevalue(Double.toString(outerrorsperc));
						errorspercVector.add(errorsPerc);									
									
						/* ���ӵ��ڴ���*/
						ShareData.setPacksdata(host.getIpAddress()+":"+sIndex,hasht);
						ShareData.setDiscardsdata(host.getIpAddress()+":"+sIndex,tempDiscards);
						ShareData.setErrorsdata(host.getIpAddress()+":"+sIndex,errHash);
									
						/*
						//��ǰ�˿��ۺ϶�����
						AllDiscardsPerc alldiscardsperc = new AllDiscardsPerc();
						alldiscardsperc.setIpaddress(nethost);
						alldiscardsperc.setCollecttime(date);
						alldiscardsperc.setCategory("Interface");
						alldiscardsperc.setEntity("AllDiscardsPerc");
						alldiscardsperc.setSubentity(sIndex);
						alldiscardsperc.setRestype("dynamic");
						alldiscardsperc.setUnit("%");
						alldiscardsperc.setChname("�ۺ϶�����");
						double alldiscards_perc=0.0;
						if ((allinpacks+alloutpacks)>0)
							alldiscards_perc=(indiscards+outdiscards)/(allinpacks+alloutpacks);
						alldiscardsperc.setThevalue(Double.toString(alldiscards_perc));
						alldiscardspercVector.add(alldiscardsperc);									
						*/
						/*
						//��ǰ�˿��ۺϴ�����
						AllErrorsPerc allerrorsperc = new AllErrorsPerc();
						allerrorsperc.setIpaddress(nethost);
						allerrorsperc.setCollecttime(date);
						allerrorsperc.setCategory("Interface");
						allerrorsperc.setEntity("AllErrorsPerc");
						allerrorsperc.setSubentity(sIndex);
						allerrorsperc.setRestype("dynamic");
						allerrorsperc.setUnit("%");
						allerrorsperc.setChname("�ۺϴ�����");
						double allerrors_perc=0.0;
						if ((allinpacks+alloutpacks)>0)
							allerrors_perc=(inerrors+outerrors)/(allinpacks+alloutpacks);
						allerrorsperc.setThevalue(Double.toString(allerrors_perc));
						allerrorspercVector.add(allerrorsperc);
						*/
									
					} //end for contains
							
				}
				}
				allutilhdx = new AllUtilHdx();
				allutilhdx.setIpaddress(host.getIpAddress());
				allutilhdx.setCollecttime(date);
				allutilhdx.setCategory("Interface");
				allutilhdx.setEntity("AllInBandwidthUtilHdx");
				allutilhdx.setSubentity("AllInBandwidthUtilHdx");
				allutilhdx.setRestype("dynamic");
				allutilhdx.setUnit(unit1[0]+"/��");	
				allutilhdx.setChname("�������");
				
				allutilhdx.setThevalue(Long.toString(allInOctetsSpeed*8));	
				allutilhdxVector.addElement(allutilhdx);	
				
				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllInBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllInBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("��ڴ���������");
				double lInUsagePerc=0;
				if(allSpeed>0){
					lInUsagePerc=Arith.div(allInOctetsSpeed*800.0,allSpeed);
					//lInUsagePerc=allInOctetsSpeed*800.0/allSpeed;
					if(lInUsagePerc>95)lInUsagePerc=95;
				}
				allutilhdxperc.setThevalue(Double.toString(lInUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);	
				*/
				//out
				
				allutilhdx = new AllUtilHdx();
				allutilhdx.setIpaddress(host.getIpAddress());
				allutilhdx.setCollecttime(date);
				allutilhdx.setCategory("Interface");
				allutilhdx.setEntity("AllOutBandwidthUtilHdx");
				allutilhdx.setSubentity("AllOutBandwidthUtilHdx");
				allutilhdx.setRestype("dynamic");
				allutilhdx.setUnit(unit1[0]+"/��");
				allutilhdx.setChname("��������");	
				allutilhdx.setThevalue(Long.toString(allOutOctetsSpeed*8));	
				allutilhdxVector.addElement(allutilhdx);	
				
				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllOutBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllOutBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("���ڴ���������");
				double lOutUsagePerc=0;
				if(allSpeed>0){
					lOutUsagePerc=Arith.div(allOutOctetsSpeed*800.0,allSpeed);
					//lOutUsagePerc=allOutOctetsSpeed*800.0/allSpeed;
					if(lOutUsagePerc>96)lOutUsagePerc=96;
				}
				allutilhdxperc.setThevalue(Double.toString(lOutUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);
				*/	
				
				allutilhdx = new AllUtilHdx();
				allutilhdx.setIpaddress(host.getIpAddress());
				allutilhdx.setCollecttime(date);
				allutilhdx.setCategory("Interface");
				allutilhdx.setEntity("AllBandwidthUtilHdx");
				allutilhdx.setSubentity("AllBandwidthUtilHdx");
				allutilhdx.setRestype("dynamic");
				allutilhdx.setUnit(unit1[0]+"/��");	
				allutilhdx.setChname("�ۺ�����");
				allutilhdx.setThevalue(Long.toString(allOctetsSpeed));	
				allutilhdxVector.addElement(allutilhdx);	

				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("�ۺϴ���������");
				double lAllUsagePerc=0;
				if(allSpeed>0){
					lAllUsagePerc=allOctetsSpeed*800.0/allSpeed;
					if(lAllUsagePerc>196)lAllUsagePerc=196;
				 }
				allutilhdxperc.setThevalue(Double.toString(lAllUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);	
				*/
				String flag ="0";
				//hash=null;
				hashSpeed=null;
				octetsHash.put("collecttime",date);	
				if (hash != null){					
					flag = (String)hash.get("flag");
					if (flag == null){
						flag="0";
					}else{
						if (flag.equals("0")){
							flag = "1";
						}else{
							flag = "0";
						}
					}
				}				
				octetsHash.put("flag",flag);
				ShareData.setOctetsdata(host.getIpAddress(),octetsHash);				
			}catch(Exception e){e.printStackTrace();}
//			  -------------------------------------------------------------------------------------------interface end
			
//			-------------------------------------------------------------------------------------------interface pkts start			
			  try{
				I_HostLastCollectData lastCollectDataManager=new HostLastCollectDataManager();
				Hashtable hash=ShareData.getPksdata(host.getIpAddress());
				//ȡ����ѯ���ʱ��
				TaskXml taskxml=new TaskXml();
				Task task=taskxml.GetXml("netcollecttask");
				int interval=getInterval(task.getPolltime().floatValue(),task.getPolltimeunit());
				Hashtable hashSpeed=new Hashtable();
				Hashtable pksHash = new Hashtable();
				if (hash==null)hash=new Hashtable();
						  String[] oids1 =                
							  new String[] {               
								"1.3.6.1.2.1.2.2.1.1", 
								"1.3.6.1.2.1.31.1.1.1.2",//ifInMulticastPkts ���յĶಥ���ݰ���Ŀ
								"1.3.6.1.2.1.31.1.1.1.3",//ifInBroadcastPkts ���յĹ㲥���ݰ���Ŀ
								"1.3.6.1.2.1.31.1.1.1.4",//ifOutMulticastPkts ���͵Ķಥ���ݰ���Ŀ
								"1.3.6.1.2.1.31.1.1.1.5" //ifOutBroadcastPkts ���͵Ĺ㲥���ݰ���Ŀ				
								  };				 
								
				
				final String[] desc=SnmpMibConstants.NetWorkMibInterfaceDesc3;

				String[][] valueArray1 = null;   	  
				try {
					valueArray1 = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids1);
				} catch(Exception e){
					valueArray1 = null;
					e.printStackTrace();
					SysLogger.error(host.getIpAddress() + "_H3CSnmp");
				}
				
				long allinpacks=0;
				long InMultiPks=0;
				long InBroadPks=0;
				long OutMultiPks=0;
				long OutBroadPks=0;
				long alloutpacks=0;
			
				Vector tempV=new Vector();
				Hashtable tempHash = new Hashtable();
				if(valueArray1!= null){
				for(int i=0;i<valueArray1.length;i++){
					allinpacks=0;
					InMultiPks=0;//��ڵ����
					InBroadPks=0;//�ǵ���
					OutMultiPks=0;
					OutBroadPks=0;
					alloutpacks=0;
																		
					if(valueArray1[i][0] == null)continue;
					String sIndex = valueArray1[i][0];
					//if (tempV.contains(sIndex)){
					if (sIndex != null && sIndex.trim().length()>0){
										
						for(int j=0;j<5;j++){																														
							if(valueArray1[i][j]!=null){
								String sValue=valueArray1[i][j];
								//if (j==6)continue;
								interfacedata=new Interfacecollectdata();
								interfacedata.setThevalue(sValue);
								if (j==1 || j==2){
									//���յĶಥ���ݰ���Ŀ,���յĹ㲥���ݰ���Ŀ												
									if (sValue != null){
										allinpacks=allinpacks+Long.parseLong(sValue);
										
										Calendar cal=(Calendar)hash.get("collecttime");
										long timeInMillis=0;
										if(cal!=null)timeInMillis=cal.getTimeInMillis();
										long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
										
										inpacks=new InPkts();
										inpacks.setIpaddress(host.getIpAddress());
										inpacks.setCollecttime(date);
										inpacks.setCategory("Interface");
										String chnameBand="";
										if(j==1){
											inpacks.setEntity("ifInMulticastPkts");
											chnameBand="�ಥ";
										}
										if(j==2){
											inpacks.setEntity("ifInBroadcastPkts");
											chnameBand="�㲥";
										}
										inpacks.setSubentity(sIndex);
										inpacks.setRestype("dynamic");
										inpacks.setUnit("");	
										inpacks.setChname(chnameBand);
										long currentPacks=Long.parseLong(sValue);												
										long lastPacks=0;	
										long l=0;											
												
										//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
										if(longinterval<2*interval){
											String lastvalue="";
											
											if(hash.get(desc[j]+":"+sIndex)!=null)lastvalue=hash.get(desc[j]+":"+sIndex).toString();
											//ȡ���ϴλ�õ�Octets
											if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
										}
							
										if(longinterval!=0){
											if(currentPacks<lastPacks){
												currentPacks=currentPacks+4294967296L;
											}
											//������-ǰ����
											//SysLogger.info(host.getIpAddress()+"==="+sIndex+"�Ͽ�==="+currentPacks+"===="+lastPacks);
											long octetsBetween=currentPacks-lastPacks;
											l=octetsBetween;
											if(lastPacks == 0)l=0;
										}
										inpacks.setThevalue(Long.toString(l));	
										//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
										if (cal != null)
											inpksVector.addElement(inpacks);	
									}
										//continue;
								}	
								if (j==3 || j==4){
									//���͵Ķಥ���ݰ���Ŀ,���͵Ĺ㲥���ݰ���Ŀ
									if (sValue != null){
										alloutpacks=alloutpacks+Long.parseLong(sValue);
										
										Calendar cal=(Calendar)hash.get("collecttime");
										long timeInMillis=0;
										if(cal!=null)timeInMillis=cal.getTimeInMillis();
										long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
										
										outpacks=new OutPkts();
										outpacks.setIpaddress(host.getIpAddress());
										outpacks.setCollecttime(date);
										outpacks.setCategory("Interface");
										String chnameBand="";
										
										if(j==3){
											outpacks.setEntity("ifOutMulticastPkts");
											chnameBand="�ಥ";
										}
										if(j==4){
											outpacks.setEntity("ifOutBroadcastPkts");
											chnameBand="�㲥";
										}
										outpacks.setSubentity(sIndex);
										outpacks.setRestype("dynamic");
										outpacks.setUnit("");	
										outpacks.setChname(chnameBand);
										long currentPacks=Long.parseLong(sValue);												
										long lastPacks=0;	
										long l=0;											
												
										//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
										if(longinterval<2*interval){
											String lastvalue="";
											
											if(hash.get(desc[j]+":"+sIndex)!=null)lastvalue=hash.get(desc[j]+":"+sIndex).toString();
											//ȡ���ϴλ�õ�Octets
											if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
										}
							
										if(longinterval!=0){
											if(currentPacks<lastPacks){
												currentPacks=currentPacks+4294967296L;
											}
											//������-ǰ����	
											long octetsBetween=currentPacks-lastPacks;
											l=octetsBetween;
											if(lastPacks == 0)l=0;
										}
										outpacks.setThevalue(Long.toString(l));	
										//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
										if (cal != null)
											outpksVector.addElement(outpacks);
										
									}
									//continue;
								}														
								//SysLogger.info(host.getIpAddress()+"==="+desc1[j]+":"+sIndex+"===="+sValue);
								pksHash.put(desc[j]+":"+sIndex,interfacedata.getThevalue());
							} //valueArray1[i][j]==null
						} //end for j																																		
					} //end for contains
							
				}
				}	
					
				String flag ="0";
				//hash=null;
				hashSpeed=null;
				pksHash.put("collecttime",date);	
				if (hash != null){					
					flag = (String)hash.get("flag");
					if (flag == null){
						flag="0";
					}else{
						if (flag.equals("0")){
							flag = "1";
						}else{
							flag = "0";
						}
					}
				}				
				pksHash.put("flag",flag);
				ShareData.setPksdata(host.getIpAddress(),pksHash);				
			}catch(Exception e){e.printStackTrace();}
//			  -------------------------------------------------------------------------------------------interface end
			  Hashtable returnHas = new Hashtable();
				//if (pingVector != null && pingVector.size()>0)returnHas.put("ping",pingVector);
				////if (systemVector != null && systemVector.size()>0)returnHas.put("system",systemVector);
				////if (memoryVector != null && memoryVector.size()>0)returnHas.put("memory",memoryVector);
				////if (cpuVector != null && cpuVector.size()>0)returnHas.put("cpu",cpuVector);
//				if (interfaceVector != null && interfaceVector.size()>0)returnHas.put("interface",interfaceVector);		
//				if (allutilhdxpercVector != null && allutilhdxpercVector.size()>0)returnHas.put("allutilhdxperc",allutilhdxpercVector);
//				if (allutilhdxVector != null && allutilhdxVector.size()>0)returnHas.put("allutilhdx",allutilhdxVector);
//				if (utilhdxpercVector != null && utilhdxpercVector.size()>0)returnHas.put("utilhdxperc",utilhdxpercVector);
//				if (utilhdxVector != null && utilhdxVector.size()>0)returnHas.put("utilhdx",utilhdxVector);		
//				if (discardspercVector != null && discardspercVector.size()>0)returnHas.put("discardsperc",discardspercVector);
//				if (errorspercVector != null && errorspercVector.size()>0)returnHas.put("errorsperc",errorspercVector);
//				if (allerrorspercVector != null && allerrorspercVector.size()>0)returnHas.put("allerrorsperc",allerrorspercVector);
//				if (alldiscardspercVector != null && alldiscardspercVector.size()>0)returnHas.put("alldiscardsperc",alldiscardspercVector);
//				if (packsVector != null && packsVector.size()>0)returnHas.put("packs",packsVector);
//				if (inpksVector != null && inpksVector.size()>0)returnHas.put("inpacks",inpksVector);	
//				if (outpksVector != null && outpksVector.size()>0)returnHas.put("outpacks",outpksVector);
				////if (ipmacVector != null && ipmacVector.size()>0)returnHas.put("ipmac",ipmacVector);
				////if (iprouterVector != null && iprouterVector.size()>0)returnHas.put("iprouter",iprouterVector);
				//if (fdbVector != null && fdbVector.size()>0)returnHas.put("ipmac",fdbVector);
				////if (fdbVector != null && fdbVector.size()>0)returnHas.put("fdb",fdbVector);
				////if (memoryVector != null && memoryVector.size()>0)returnHas.put("memory",memoryVector);
				////if (flashVector != null && flashVector.size()>0)returnHas.put("flash",flashVector);
				////if (temperatureVector != null && temperatureVector.size()>0)returnHas.put("temperature",temperatureVector);
				////if (fanVector != null && fanVector.size()>0)returnHas.put("fan",fanVector);
				////if (powerVector != null && powerVector.size()>0)returnHas.put("power",powerVector);
				////if (voltageVector != null && voltageVector.size()>0)returnHas.put("voltage",voltageVector);
				//returnHas.put("flag",flag);
				return returnHas;
	   }
	public void run(){
		//collectData();
	}

	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,String sIndex,String bids){
	 	//��������		 	
	 	//���ڴ����õ�ǰ���IP��PING��ֵ
	 	Calendar date=Calendar.getInstance();
	 	Hashtable sendeddata = ShareData.getSendeddata();
	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 	//System.out.println("�˿��¼�--------------------");
	 	try{
 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress+":"+sIndex)) {
 				//�����ڣ��������ţ��������ӵ������б���
	 			Smscontent smscontent = new Smscontent();
	 			String time = sdf.format(date.getTime());
	 			smscontent.setLevel(flag+"");
	 			smscontent.setObjid(objid);
	 			smscontent.setMessage(content);
	 			smscontent.setRecordtime(time);
	 			smscontent.setSubtype(subtype);
	 			smscontent.setSubentity(subentity);
	 			smscontent.setIp(ipaddress);
	 			//���Ͷ���
//	 			SmscontentDao smsmanager=new SmscontentDao();
//	 			smsmanager.sendURLSmscontent(smscontent);	
				sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);	
				
 			} else {
 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
 				SmsDao smsDao = new SmsDao();
 				List list = new ArrayList();
 				String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
 				String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 				try {
 					list = smsDao.findByEvent(content,startTime,endTime);
				} catch (RuntimeException e) {
					e.printStackTrace();
				} finally {
					smsDao.close();
				}
				if(list!=null&&list.size()>0){//�����б����Ѿ����͵���Ķ���
					Calendar formerdate =(Calendar)sendeddata.get(subtype+":"+subentity+":"+ipaddress+":"+sIndex);		 				
		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		 			Date last = null;
		 			Date current = null;
		 			Calendar sendcalen = formerdate;
		 			Date cc = sendcalen.getTime();
		 			String tempsenddate = formatter.format(cc);
		 			
		 			Calendar currentcalen = date;
		 			Date ccc = currentcalen.getTime();
		 			last = formatter.parse(tempsenddate);
		 			String currentsenddate = formatter.format(ccc);
		 			current = formatter.parse(currentsenddate);
		 			
		 			long subvalue = current.getTime()-last.getTime();	
		 			if(checkday == 1){
		 				//����Ƿ������˵��췢������,1Ϊ���,0Ϊ�����
		 				if (subvalue/(1000*60*60*24)>=1){
			 				//����һ�죬���ٷ���Ϣ
				 			Smscontent smscontent = new Smscontent();
				 			String time = sdf.format(date.getTime());
				 			smscontent.setLevel(flag+"");
				 			smscontent.setObjid(objid);
				 			smscontent.setMessage(content);
				 			smscontent.setRecordtime(time);
				 			smscontent.setSubtype(subtype);
				 			smscontent.setSubentity(subentity);
				 			smscontent.setIp(ipaddress);//���Ͷ���
//				 			SmscontentDao smsmanager=new SmscontentDao();
//				 			smsmanager.sendURLSmscontent(smscontent);
							//�޸��Ѿ����͵Ķ��ż�¼	
							sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
				 		} else {
	                        //��ʼд�¼�
			 	            String sysLocation = "";
			 				createEvent("poll",sysLocation,bids,content,flag,subtype,subentity,ipaddress,objid);
				 		}
		 			}
				} else {
 					Smscontent smscontent = new Smscontent();
 		 			String time = sdf.format(date.getTime());
 		 			smscontent.setLevel(flag+"");
 		 			smscontent.setObjid(objid);
 		 			smscontent.setMessage(content);
 		 			smscontent.setRecordtime(time);
 		 			smscontent.setSubtype(subtype);
 		 			smscontent.setSubentity(subentity);
 		 			smscontent.setIp(ipaddress);
 		 			//���Ͷ���
// 		 			SmscontentDao smsmanager=new SmscontentDao();
// 		 			smsmanager.sendURLSmscontent(smscontent);	
 					sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
 				}
 				
 			}	 			 			 			 			 	
	 	}catch(Exception e){
	 		e.printStackTrace();
	 	}
	 }

	private void createEvent(String eventtype,String eventlocation,String bid,String content,int level1,String subtype,String subentity,String ipaddress,String objid){
		//�����¼�
//		SysLogger.info("##############��ʼ�����¼�############");
		EventList eventlist = new EventList();
		eventlist.setEventtype(eventtype);
		eventlist.setEventlocation(eventlocation);
		eventlist.setContent(content);
		eventlist.setLevel1(level1);
		eventlist.setManagesign(0);
		eventlist.setBak("");
		eventlist.setRecordtime(Calendar.getInstance());
		eventlist.setReportman("ϵͳ��ѯ");
		eventlist.setBusinessid(bid);
		eventlist.setNodeid(Integer.parseInt(objid));
		eventlist.setOid(0);
		eventlist.setSubtype(subtype);
		eventlist.setSubentity(subentity);
		EventListDao eventlistdao = new EventListDao();
		try{
			eventlistdao.save(eventlist);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			eventlistdao.close();
		}
	}


	public int getInterval(float d,String t){
				int interval=0;
				  if(t.equals("d"))
					 interval =(int) d*24*60*60; //����
				  else if(t.equals("h"))
					 interval =(int) d*60*60;    //Сʱ
				  else if(t.equals("m"))
					 interval = (int)d*60;       //����
				else if(t.equals("s"))
							 interval =(int) d;       //��
				return interval;
	}
}




