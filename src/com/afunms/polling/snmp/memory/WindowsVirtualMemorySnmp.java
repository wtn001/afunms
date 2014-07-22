package com.afunms.polling.snmp.memory;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
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
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostvirtualmemoryResultTosql;
import com.gatherResulttosql.NetHostMemoryRtsql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WindowsVirtualMemorySnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public WindowsVirtualMemorySnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returnHash=new Hashtable();
		Vector memoryVector=new Vector();
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
		if(host == null)return returnHash;
		//�ж��Ƿ��ڲɼ�ʱ�����
    	if(ShareData.getTimegatherhash() != null){
    		if(ShareData.getTimegatherhash().containsKey(host.getId()+":equipment")){
    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
    			int _result = 0;
    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(host.getId()+":equipment"));
    			if(_result ==1 ){
    				//SysLogger.info("########ʱ�����: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else if(_result == 2){
    				//SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else {
    				SysLogger.info("######## "+host.getIpAddress()+" ���ڲɼ��ڴ�ʱ�����,�˳�##########");
    				//���֮ǰ�ڴ��в����ĸ澯��Ϣ
    			    try{
    			    	//���֮ǰ�ڴ��в������ڴ�澯��Ϣ
    			    	NodeDTO nodedto = null;
    					NodeUtil nodeUtil = new NodeUtil();
    					nodedto = nodeUtil.creatNodeDTOByHost(host);
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.deleteEvent(host.getId()+"", nodedto.getType(), nodedto.getSubtype(), "virtualmemory", null);
    			    }catch(Exception e){
    			    	e.printStackTrace();
    			    }
    				return returnHash;
    			}
    			
    		}
    	}
		try {
			Diskcollectdata diskdata=new Diskcollectdata();
			Memorycollectdata memorydata=new Memorycollectdata();
			Calendar date=Calendar.getInstance();
			Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData == null)ipAllData = new Hashtable();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(host.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
			//-------------------------------------------------------------------------------------------disk start			
				try{			
					String[] oids =                
							  new String[] {               
								  "1.3.6.1.2.1.25.2.3.1.1",  
								  "1.3.6.1.2.1.25.2.3.1.2",  
								  "1.3.6.1.2.1.25.2.3.1.3",  
								  "1.3.6.1.2.1.25.2.3.1.4",  
								  "1.3.6.1.2.1.25.2.3.1.5",  
								  "1.3.6.1.2.1.25.2.3.1.6",  
								  "1.3.6.1.2.1.25.2.3.1.7" };
					
					String[][] valueArray = null;   	  
					try {
						//valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids, host.getSnmpversion(), 3, 1000*30);
						valueArray = SnmpUtils.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids,host.getSnmpversion(),
			   		  			host.getSecuritylevel(),host.getSecurityName(),host.getV3_ap(),host.getAuthpassphrase(),host.getV3_privacy(),host.getPrivacyPassphrase(),3,1000*30);
					} catch(Exception e){
						valueArray = null;
						//SysLogger.error(host.getIpAddress() + "_WindowsSnmp");
					}
					if (valueArray != null && valueArray.length > 0) {
						for(int i=0;i<valueArray.length;i++){
							diskdata=new Diskcollectdata();
							diskdata.setIpaddress(host.getIpAddress());
							diskdata.setCollecttime(date);
							diskdata.setCategory("Disk");
							diskdata.setEntity("Utilization");	
							diskdata.setRestype("static");
							diskdata.setUnit("%");
							String descriptions=valueArray[i][2];
							String byteunit=valueArray[i][3];
							String desc="";
							if(descriptions == null)descriptions="";
							
							
							
							if(descriptions.indexOf("Memory")>=0){
								desc = descriptions;
							}else{
								if(descriptions.trim().length()>2){
									desc = descriptions.substring(0, 3);
								}
							}
							if(descriptions.indexOf("\\")>=0){
							  desc = desc.replace("\\", "/");
							}
							//SysLogger.info("desc: "+desc);
	//						  if(descriptions.indexOf("\\")>=0){
	//							  desc = desc.replace("\\", "/");
	//							  desc=descriptions.substring(0,descriptions.indexOf("\\"))+"/"+descriptions.substring(descriptions.indexOf("\\")+1,descriptions.length());
	//						  }
	//						  else{
	//							  desc=descriptions;
	//						  }
							  diskdata.setSubentity(desc);
							  float value=0.0f;
							  String svb4=valueArray[i][4];
							  String svb5=valueArray[i][5];
							  int allsize=Integer.parseInt(svb4.trim());
							  int used=Integer.parseInt(svb5.trim());
							  if(allsize!=0){
								  value=used*100.0f/allsize;
							  }
							  else{
								  value=0.0f;
							  }
							  
							  
							  
							  
							  diskdata.setThevalue(Float.toString(value));
							  if (diskdata.getSubentity().equals("Virtual Memory")){
									memorydata=new Memorycollectdata();
									memorydata.setIpaddress(diskdata.getIpaddress());
									memorydata.setCollecttime(diskdata.getCollecttime());
									memorydata.setCategory("Memory");
									memorydata.setEntity("Utilization");
									memorydata.setSubentity("VirtualMemory");
									memorydata.setRestype("dynamic");
									memorydata.setUnit("%");
									DecimalFormat df=new DecimalFormat("#.##");
									memorydata.setThevalue(df.format(Float.parseFloat(diskdata.getThevalue())));
									
									
									float usize=0.0f;
									  usize=used*Long.parseLong(byteunit)*1.0f/1024/1024;
									  String unit = ""; 
									  if(usize>=1024.0f){
										  usize=usize/1024;
										  //diskdata.setUnit("G");
										  unit = "G"; 
									  }
									  else{
									  	//diskdata.setUnit("M");
									  	unit = "M"; 
									  }
									//SysLogger.info(host.getIpAddress()+" VirtualMemory used============"+usize+unit);
									//SysLogger.info("��ʼ���������ڴ�����...");
									memoryVector.addElement(memorydata);			  				  	
							  }
							  diskdata=new Diskcollectdata();
							  diskdata.setIpaddress(host.getIpAddress());
							  diskdata.setCollecttime(date);
							  diskdata.setCategory("Disk");
							  diskdata.setEntity("AllSize");
							  diskdata.setRestype("static");
							  diskdata.setSubentity(desc);
							  float size=0.0f;
							  size=allsize*Long.parseLong(byteunit)*1.0f/1024/1024;
							  String unit = ""; 
							  if(size>=1024.0f){
								  size=size/1024;
								  diskdata.setUnit("G");
								  unit = "G"; 
							  }
							  else{
							  	diskdata.setUnit("M");
							  	unit = "M"; 
							  }
							  diskdata.setThevalue(Float.toString(size));
							  if (diskdata.getSubentity().equals("Virtual Memory")){
								memorydata=new Memorycollectdata();
								memorydata.setIpaddress(diskdata.getIpaddress());
								memorydata.setCollecttime(diskdata.getCollecttime());
								memorydata.setCategory("Memory");
								memorydata.setEntity("Capability");
								memorydata.setRestype("static");
								memorydata.setSubentity("VirtualMemory");
								memorydata.setUnit(diskdata.getUnit());
								memorydata.setThevalue(Float.toString(size));	
								//SysLogger.info(host.getIpAddress()+" VirtualMemory Capability============"+size+diskdata.getUnit());
								memoryVector.addElement(memorydata);			  	
							  }
	
							  diskdata=new Diskcollectdata();
							  diskdata.setIpaddress(host.getIpAddress());
							  diskdata.setCollecttime(date);
							  diskdata.setCategory("Disk");
							  diskdata.setEntity("UsedSize");
							  diskdata.setRestype("static");
							  diskdata.setSubentity(desc);
							  size=used*Long.parseLong(byteunit)*1.0f/1024/1024;
							  if("G".equals(unit)){
								  size=size/1024;
								  diskdata.setUnit("G");
							  }else{
								     diskdata.setUnit("M");
							 }
							  
							  diskdata.setThevalue(Float.toString(size));
							  if (diskdata.getSubentity().equals("Virtual Memory")){
									memorydata=new Memorycollectdata();
									memorydata.setIpaddress(diskdata.getIpaddress());
									memorydata.setCollecttime(diskdata.getCollecttime());
									memorydata.setCategory("Memory");
									memorydata.setEntity("UsedSize");
									memorydata.setRestype("static");
									memorydata.setSubentity("VirtualMemory");
									memorydata.setUnit(diskdata.getUnit());
									memorydata.setThevalue(Float.toString(size));	
									//SysLogger.info(host.getIpAddress()+" VirtualMemory Capability============"+size+diskdata.getUnit());
									memoryVector.addElement(memorydata);	
							  }
						}
					} else {
						memorydata=new Memorycollectdata();
						memorydata.setIpaddress(diskdata.getIpaddress());
						memorydata.setCollecttime(diskdata.getCollecttime());
						memorydata.setCategory("Memory");
						memorydata.setEntity("Utilization");
						memorydata.setSubentity("VirtualMemory");
						memorydata.setRestype("dynamic");
						memorydata.setUnit("%");
						DecimalFormat df=new DecimalFormat("#.##");
						memorydata.setThevalue(df.format(0));
					
						memoryVector.addElement(memorydata);	
						
						int memoryVectorSize = memoryVector.size();
						if ((ShareData.getSharedata().containsKey(host.getIpAddress()))) {
							if (((Hashtable) (ShareData.getSharedata().get(host.getIpAddress()))).containsKey("memory")) {
								Vector formerMemoryVector = (Vector) ((Hashtable) (ShareData.getSharedata().get(host.getIpAddress()))).get("memory");
								if (formerMemoryVector != null && formerMemoryVector.size() > 0) {
									for (int i = 0; i < formerMemoryVector.size(); i++) {
										memorydata = new Memorycollectdata();
										memorydata = (Memorycollectdata) formerMemoryVector.get(i);
										if (memorydata.getEntity().equalsIgnoreCase("Capability") && memorydata.getSubentity().equalsIgnoreCase("VirtualMemory")) {
											memoryVector.addElement(memorydata);
											break;
										}
									}
								}
							}
						} 
						if(memoryVector.size() == memoryVectorSize) {	
							memorydata=new Memorycollectdata();
							memorydata.setIpaddress(diskdata.getIpaddress());
							memorydata.setCollecttime(diskdata.getCollecttime());
							memorydata.setCategory("Memory");
							memorydata.setEntity("Capability");
							memorydata.setRestype("static");
							memorydata.setSubentity("VirtualMemory");
							memorydata.setUnit(diskdata.getUnit());
							memorydata.setThevalue(Float.toString(0.0f));	
						//SysLogger.info(host.getIpAddress()+" VirtualMemory Capability============"+size+diskdata.getUnit());
							memoryVector.addElement(memorydata);
						}
						
						memorydata=new Memorycollectdata();
						memorydata.setIpaddress(diskdata.getIpaddress());
						memorydata.setCollecttime(diskdata.getCollecttime());
						memorydata.setCategory("Memory");
						memorydata.setEntity("UsedSize");
						memorydata.setRestype("static");
						memorydata.setSubentity("VirtualMemory");
						memorydata.setUnit("M");
						memorydata.setThevalue(Float.toString(-1.0f));	
						//SysLogger.info(host.getIpAddress()+" VirtualMemory Capability============"+size+diskdata.getUnit());
						memoryVector.addElement(memorydata);
					}
			  }
			  catch(Exception e){
				  //e.printStackTrace();
			  }
			  //-------------------------------------------------------------------------------------------disk end

			}catch(Exception e){
				//returnHash=null;
				//e.printStackTrace();
				//return null;
			}
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData == null)ipAllData = new Hashtable();
		Vector toAddVector = new Vector();
		Hashtable formerHash = new Hashtable();
		if(ipAllData.containsKey("memory")){
			Vector formerMemoryVector = (Vector)ipAllData.get("memory");
			if(formerMemoryVector != null && formerMemoryVector.size()>0){
				for(int i=0;i<formerMemoryVector.size();i++){
					Memorycollectdata memorydata=(Memorycollectdata)formerMemoryVector.get(i);
					formerHash.put(memorydata.getSubentity()+":"+memorydata.getEntity(), memorydata);
				}
			}
			
		}
		if(memoryVector != null && memoryVector.size()>0){
			for(int j=0;j<memoryVector.size();j++){
				Memorycollectdata memorydata=(Memorycollectdata)memoryVector.get(j);
				if(formerHash.containsKey(memorydata.getSubentity()+":"+memorydata.getEntity())){
					//������,��Ҫ���µ��滻ԭ��������
					//SysLogger.info("����   "+memorydata.getSubentity()+":"+memorydata.getEntity());
					formerHash.remove(memorydata.getSubentity()+":"+memorydata.getEntity());
					formerHash.put(memorydata.getSubentity()+":"+memorydata.getEntity(), memorydata);
				}else{
					//��������,��ֱ�Ӽ���
					//SysLogger.info("���   "+memorydata.getSubentity()+":"+memorydata.getEntity());
					toAddVector.add(memorydata);
				}
			}
		}
		if(formerHash.elements() != null && formerHash.size()>0){
			for(Enumeration enumeration = formerHash.keys(); enumeration.hasMoreElements();){
				String keys = (String)enumeration.nextElement();
				Memorycollectdata memorydata=(Memorycollectdata)formerHash.get(keys);
				//SysLogger.info("���   "+memorydata.getSubentity()+":"+memorydata.getEntity());
				toAddVector.add(memorydata);
			}
		}
		ipAllData.put("memory",toAddVector);
	    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
	    returnHash.put("memory", toAddVector);
	    
	    Hashtable collectHash = new Hashtable();
		collectHash.put("virtalmem", toAddVector);
		
		//�������ڴ�ֵ���и澯���
//	    try{
//			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "windows","virtualmemory");
//			for(int i = 0 ; i < list.size() ; i ++){
//				AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//				//�������ڴ�ֵ���и澯���
//				CheckEventUtil checkutil = new CheckEventUtil();
//				checkutil.updateData(host,collectHash,"host","windows",alarmIndicatorsnode);
//			}
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    }
	    try {
	    	if(memoryVector != null && memoryVector.size() > 0){
				int thevalue = 0;
				for(int i = 0 ; i < memoryVector.size(); i++){
					Memorycollectdata memorycollectdata = (Memorycollectdata)memoryVector.get(i);
					if("Utilization".equals(memorycollectdata.getEntity())){
						if(Double.parseDouble(memorycollectdata.getThevalue())>thevalue){
							thevalue = (int) Double.parseDouble(memorycollectdata.getThevalue());
						}
					}
				}
				CheckEventUtil checkutil = new CheckEventUtil();
				checkutil.updateData(host, nodeGatherIndicators, thevalue+"",null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("Windows �ڴ� �澯����" , e);
		}
	    ipAllData=null;
	    memoryVector=null;
	    
	    //�Ѳɼ��������sql
	    HostvirtualmemoryResultTosql  tosql=new HostvirtualmemoryResultTosql();
	    tosql.CreateResultTosql(returnHash, host.getIpAddress());
	    String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	    if(!"0".equals(runmodel)){
			//�ɼ�������Ƿ���ģʽ,����Ҫ����������д����ʱ���
	    	NetHostMemoryRtsql  totempsql=new NetHostMemoryRtsql();
		    totempsql.CreateResultTosql(returnHash, host,"VirtualMemory");
	    }
	    
	    return returnHash;
	}
}





