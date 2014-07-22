package com.afunms.polling.snmp.cpu;
        
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
import com.afunms.common.util.SnmpService;
import com.afunms.common.util.SnmpUtil;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetcpuResultTosql;

public class VenusCpuSnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public VenusCpuSnmp() {

	}

	public void collectData(Node node, MonitoredItem item) {

	}

	public void collectData(HostNode node) {

	}

	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		int result = 0;
		Hashtable returnHash=new Hashtable();
		Vector<CPUcollectdata> cpuVector=new Vector<CPUcollectdata>();
		List cpuList = new ArrayList();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		if(node == null)return null;
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
			  
			  //-------------------------------------------------------------------------------------------cpu start	
			 // int result = 0;
			  String temp = "0";
			  try{
				  //String temp = "0";
				String[] oids = new String[]{"1.3.6.1.4.1.15227.1.3.1.1.1"};
				String[][] valueArray = null;	
				//valueArray = SnmpUtils.getCpuTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000);
				valueArray = SnmpUtils.getTemperatureTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
	   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
//				SnmpService snmp = new SnmpService();
//				valueArray = snmp.getCpuTableData(node.getIpAddress(),node.getCommunity(),oids);
	   			int allvalue=0;
	   			int flag = 0;	   			
				if(valueArray != null){
				   	  for(int i=0;i<valueArray.length;i++)
				   	  {
				   		String _value = valueArray[i][0];	
//				   		SysLogger.info("=======Start collect data as ip VenusCpuSnmp####-----_value:"+_value);
				   		String index = valueArray[i][1];	
				   		if(!"".equals(_value) && _value!=null)
				   		{
				   		 if(_value.indexOf("%")>0){
				   			String[] values=_value.split("%");					   			
				   			for(int k=0;k<values.length;k++){
				   				_value=values[k];
				   			}
				   		  }
				   	  }
				   		   int value=0;
				   		   value=Integer.parseInt(_value);
				   		   allvalue = allvalue+Integer.parseInt(_value);
				    		flag = flag +1;
					   		List alist = new ArrayList();
					   		alist.add(index);
					   		alist.add(_value);
					   		cpuList.add(alist);
					   		//SysLogger.info("=======Start collect data as ip VenusCpuSnmp=====_value:"+_value);
					  }
				}
				
				
		
				if(flag >0){
				
				int intvalue = (allvalue/flag);
				temp = intvalue+"";
			 }
				//SysLogger.info("=======Start collect data as ip VenusCpuSnmp=====allvalue:"+allvalue);
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
	   		    cpudata=new CPUcollectdata();
				cpudata.setIpaddress(node.getIpAddress());
				cpudata.setCollecttime(date);
				cpudata.setCategory("CPU");
				cpudata.setEntity("Utilization");
				cpudata.setSubentity("Utilization");
				cpudata.setRestype("dynamic");
				cpudata.setUnit("%");
				cpudata.setThevalue(result+"");
//				SysLogger.info("=======Start collect data as ip VenusCpuSnmp&&&&&&&&-----result:"+result);
				cpuVector.addElement(cpudata);
		
			  }
			  catch(Exception e){
				  //e.printStackTrace();
			  }
			  //-------------------------------------------------------------------------------------------cpu end	
			}
			catch(Exception e){
				//returnHash=null;
				//e.printStackTrace();
				//return null;
			}
			
			Hashtable ipAllData = new Hashtable();
			try{
				ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
			}catch(Exception e){
				
			}
			if(ipAllData == null)ipAllData = new Hashtable();
			if(cpuVector != null && cpuVector.size()>0)ipAllData.put("cpu",cpuVector);
			if(cpuList != null && cpuList.size()>0)ipAllData.put("cpulist",cpuList);
		    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		    returnHash.put("cpu", cpuVector);
		    
		    //��CPUֵ���и澯���
		    Hashtable collectHash = new Hashtable();
			collectHash.put("cpu", cpuVector);
		    try{
				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_FIREWALL, "venus","cpu");
				for(int i = 0 ; i < list.size() ; i ++){
					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
					//��CPUֵ���и澯���
					CheckEventUtil checkutil = new CheckEventUtil();
					checkutil.updateData(node,collectHash,"firewall","venus",alarmIndicatorsnode);
					//}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }		
			  //�ѽ��ת����sql
		    NetcpuResultTosql tosql=new NetcpuResultTosql();
		    tosql.CreateResultTosql(returnHash, node.getIpAddress());
		    NetHostDatatempCpuRTosql totempsql=new NetHostDatatempCpuRTosql();
		    totempsql.CreateResultTosql(returnHash, node);
		    return returnHash;
		}
}
