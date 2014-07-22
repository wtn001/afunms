package com.afunms.polling.snmp.memory;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

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
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostPhysicalMemoryResulttosql;
import com.gatherResulttosql.HostvirtualmemoryResultTosql;
import com.gatherResulttosql.NetHostMemoryRtsql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LinuxSwapMemorySnmp extends SnmpMonitor {
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public LinuxSwapMemorySnmp() {
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
		String memerySize = "";
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
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
						checkutil.deleteEvent(host.getId()+"", nodedto.getType(), nodedto.getSubtype(), "physicalmemory", null);
    			    }catch(Exception e){
    			    	e.printStackTrace();
    			    }
    				return returnHash;
    			}
    			
    		}
    	}
		try {
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
			//-------------------------------------------------------------------------------------------memory start			
				try{
				
					String[] oids = { 
							"1.3.6.1.2.1.25.2.3.1.2",
							"1.3.6.1.2.1.25.2.3.1.4",
							"1.3.6.1.2.1.25.2.3.1.5", 
							"1.3.6.1.2.1.25.2.3.1.6"
							
						};
					
					String[][] results = null;
					
					Float result2 = null;
					
					try {
						//results = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids, host.getSnmpversion(), 3, 1000*30);
						results = SnmpUtils.getTemperatureTableData(host.getIpAddress(),host.getCommunity(),oids,host.getSnmpversion(),
			   		  			host.getSecuritylevel(),host.getSecurityName(),host.getV3_ap(),host.getAuthpassphrase(),host.getV3_privacy(),host.getPrivacyPassphrase(),3,1000*30);
					} catch (Exception e) {
						e.printStackTrace();
					}
					float physize=0f;
					float phyused=0f;
					float virsize=0f;
					float virused=0f;
					String phybyte="";
					String virbyte="";
					if (results != null) {
						for (int i = 0; i < results.length; ++i) {
							String type = results[i][0];
				          
				            
				            if ("1.3.6.1.2.1.25.2.1.3".equals(type)) {
				            	virbyte=results[i][1];
				            	 virsize = Float.parseFloat(results[i][2]);
				            	 virused = Float.parseFloat(results[i][3]);
				            	
				            	result2 = Float.valueOf(100.0F * virused / virsize);
				            	
				            }
				          
						}
					}
					
					
					
					
					date=Calendar.getInstance();
					
					
					if(result2 != null){
						memorydata=new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("Utilization");
						memorydata.setSubentity("SwapMemory");
						memorydata.setRestype("dynamic");
						memorydata.setUnit("%");
						memorydata.setThevalue(Float.toString(result2));
						
						memoryVector.add(memorydata);
						
						memorydata = new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("Capability");
						memorydata.setRestype("static");
						memorydata.setSubentity("SwapMemory");

						float size=0.0f;
						size=virsize*Long.parseLong(virbyte)*1.0f/1024/1024;
						String unit = ""; 
						if(size>=1024.0f){
							  size=size/1024;
							  memorydata.setUnit("G");
							  unit = "G"; 
						}else{
							  memorydata.setUnit("M");
						  	unit = "M"; 
						}
						memorydata.setThevalue(Float.toString(size));
						memoryVector.addElement(memorydata);
						memorydata = new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("UsedSize");
						memorydata.setRestype("static");
						memorydata.setSubentity("SwapMemory");
						size = virused * 1.0f / 1024;
						if (size >= 1024.0f) {
							size = size / 1024;
							memorydata.setUnit("G");
						} else {
							memorydata.setUnit("M");
						}
						memorydata.setThevalue(Float.toString(size));
						memoryVector.addElement(memorydata);
					}
				}
				catch(Exception e){
					SysLogger.error("LinuxPhysicalMemory error",e);
				}
				//-------------------------------------------------------------------------------------------memory end
			}catch(Exception e){
				SysLogger.error("LinuxPhysicalMemory error",e);
			}
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData == null)ipAllData = new Hashtable();
		Vector toAddVector = new Vector();
		Hashtable formerHash = new Hashtable();
		
		if ((ShareData.getSharedata().containsKey(host.getIpAddress()))) {
			if (((Hashtable) (ShareData.getSharedata().get(host.getIpAddress()))).containsKey("memory")) {
				Vector formerMemoryVector = (Vector) ((Hashtable) (ShareData.getSharedata().get(host.getIpAddress()))).get("memory");
				if (formerMemoryVector != null && formerMemoryVector.size() > 0) {
					for (int i = 0; i < formerMemoryVector.size(); i++) {
						Memorycollectdata memorydata = (Memorycollectdata) formerMemoryVector.get(i);
						formerHash.put(memorydata.getSubentity() + ":" + memorydata.getEntity(), memorydata);
					}
				}
			}
		}

		if (memoryVector != null && memoryVector.size() > 0) {
			for (int j = 0; j < memoryVector.size(); j++) {
				Memorycollectdata memorydata = (Memorycollectdata) memoryVector.get(j);
				if (formerHash.containsKey(memorydata.getSubentity() + ":" + memorydata.getEntity())) {
					// ������,��Ҫ���µ��滻ԭ��������
					formerHash.remove(memorydata.getSubentity() + ":" + memorydata.getEntity());
					formerHash.put(memorydata.getSubentity() + ":" + memorydata.getEntity(), memorydata);
				} else {
					// ��������,��ֱ�Ӽ���
					toAddVector.add(memorydata);
				}
			}
		}
		if (formerHash.elements() != null && formerHash.size() > 0) {
			for (Enumeration enumeration = formerHash.keys(); enumeration.hasMoreElements();) {
				String keys = (String) enumeration.nextElement();
				Memorycollectdata memorydata = (Memorycollectdata) formerHash.get(keys);
				toAddVector.add(memorydata);
			}
		}

		if (!(ShareData.getSharedata().containsKey(host.getIpAddress()))) {
			
			if (ipAllData == null)
				ipAllData = new Hashtable();
			if (toAddVector != null && toAddVector.size() > 0)
				ipAllData.put("memory", toAddVector);
			ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		} else {
			if (toAddVector != null && toAddVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("memory", toAddVector);

		}
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
	    returnHash.put("memory", toAddVector);
	    
	    ipAllData=null;
	    memoryVector=null;
	    toAddVector=null;
	    //�Ѳɼ��������sql
	    
	    HostvirtualmemoryResultTosql  tosql1=new HostvirtualmemoryResultTosql();
	    tosql1.CreateResultTosql(returnHash, host.getIpAddress());
	    String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	    if(!"0".equals(runmodel)){
			//�ɼ�������Ƿ���ģʽ,����Ҫ����������д����ʱ���
	    	NetHostMemoryRtsql  totempsql=new NetHostMemoryRtsql();
		    totempsql.CreateResultTosql(returnHash, host);
	    }
	    
	    return returnHash;
	}
}





