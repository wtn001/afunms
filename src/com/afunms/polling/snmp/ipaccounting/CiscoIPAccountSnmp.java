package com.afunms.polling.snmp.ipaccounting;

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
import com.afunms.ipaccounting.dao.IpAccountingBaseDao;
import com.afunms.ipaccounting.model.IpAccounting;
import com.afunms.ipaccounting.model.IpAccountingBase;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.NetHostDatatempCpuRTosql;
import com.gatherResulttosql.NetipaccountResultTosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CiscoIPAccountSnmp extends SnmpMonitor {
	private Hashtable sendeddata = ShareData.getProcsendeddata();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public CiscoIPAccountSnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {	
		//yangjun
		Hashtable returnHash=new Hashtable();
		Vector ipaccountVector=new Vector();
		List cpuList = new ArrayList();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		if(node == null)return returnHash;
		//HostNode host = (HostNode)node;
    	//判断是否在采集时间段内
    	if(ShareData.getTimegatherhash() != null){
    		if(ShareData.getTimegatherhash().containsKey(node.getId()+":equipment")){
    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
    			int _result = 0;
    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(node.getId()+":equipment"));
    			if(_result ==1 ){
    				//SysLogger.info("########时间段内: 开始采集 "+node.getIpAddress()+" IPAccounting数据信息##########");
    			}else if(_result == 2){
    				//SysLogger.info("########全天: 开始采集 "+node.getIpAddress()+" IPAccounting数据信息##########");
    			}else {
    				SysLogger.info("######## "+node.getIpAddress()+" 不在采集IPAccounting时间段内,退出##########");
    				//清除之前内存中产生的告警信息
//    			    try{
//    			    	//清除之前内存中产生的CPU告警信息
//						CheckEventUtil checkutil = new CheckEventUtil();
//						checkutil.deleteEvent(node.getId()+":net:cpu");
//    			    }catch(Exception e){
//    			    	e.printStackTrace();
//    			    }
    				return returnHash;
    			}
    			
    		}
    	}
		try {
			//System.out.println("Start collect data as ip "+host);
			Calendar date=Calendar.getInstance();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
		  //-------------------------------------------------------------------------------------------IPAccounting start	
		  int result = 0;
		  String temp = "0";
		  try{
			  
				SnmpUtils snmputils = new SnmpUtils();
				String retValue = "";
				try{
					String value = snmputils.get(node.getIpAddress(), node.getCommunity(), ".1.3.6.1.4.1.9.2.4.11.0",0,3, 3000);
					retValue = snmputils.set(node.getIpAddress(), node.getWritecommunity(), ".1.3.6.1.4.1.9.2.4.11.0", value, 'i', 0,3, 3000);
				}catch(Exception e){
					e.printStackTrace();
				}
			  
			  String[] oids =                
				  new String[] {               
					"1.3.6.1.4.1.9.2.4.9.1.1",//src
					"1.3.6.1.4.1.9.2.4.9.1.2",//dst
					"1.3.6.1.4.1.9.2.4.9.1.3",//pkts
					"1.3.6.1.4.1.9.2.4.9.1.4"//byts
   			};
			String[][] valueArray = null;
			
			try {
				//valueArray = SnmpUtils.getTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
				valueArray = SnmpUtils.getTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
	   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
			} catch(Exception e){
				valueArray = null;
				e.printStackTrace();
				//SysLogger.error(node.getIpAddress() + "_CiscoSnmp");
			}
   			int allvalue=0;
   			int flag = 0;
   			//休眠10秒钟
   			String str="";
   			for(int i=0;i<1000000;i++){
   				str = str.trim();
   			}
			if(valueArray != null){
				IpAccounting ipaccounting = null;
				int accountbaseid = 0;
				Calendar coldate=Calendar.getInstance();
				IpAccountingBaseDao basedao = new IpAccountingBaseDao();
				try{
					for(int i=0;i<valueArray.length;i++){	  
				   		  try{
						   		ipaccounting = new IpAccounting();
						   		ipaccounting.setSrcip(valueArray[i][0]);
						   		ipaccounting.setDestip(valueArray[i][1]);
						   		if(valueArray[i][2] == null || valueArray[i][3] == null)continue;
						   		ipaccounting.setPkts(Integer.parseInt(valueArray[i][2]));
						   		ipaccounting.setByts(Integer.parseInt(valueArray[i][3]));
						   		if(ShareData.getAllipaccountipbases() != null){
						   			if(ShareData.getAllipaccountipbases().containsKey(ipaccounting.getSrcip()+":"+ipaccounting.getDestip()+":"+node.getId())){
						   				accountbaseid = Integer.parseInt((String)ShareData.getAllipaccountipbases().get(ipaccounting.getSrcip()+":"+ipaccounting.getDestip()+":"+node.getId()));
						   			}else{
						   			//基础表存入数据库
							   			IpAccountingBase ipbase = new IpAccountingBase();
							   			ipbase.setDestip(ipaccounting.getDestip());
							   			ipbase.setSrcip(ipaccounting.getSrcip());
							   			ipbase.setNodeid(node.getId());
							   			ipbase.setProtocol("");
							   			
							   			try{
							   				basedao.save(ipbase);
							   			}catch(Exception e){
							   				e.printStackTrace();
							   			}finally{
							   				//basedao.close();
							   			}
							   			accountbaseid = ipbase.getId();
							   			ShareData.getAllipaccountipbases().put(ipaccounting.getSrcip()+":"+ipaccounting.getDestip()+":"+node.getId(), accountbaseid+"");
						   			}
						   		}else{
						   			//基础表存入数据库
						   			IpAccountingBase ipbase = new IpAccountingBase();
						   			ipbase.setDestip(ipaccounting.getDestip());
						   			ipbase.setSrcip(ipaccounting.getSrcip());
						   			ipbase.setNodeid(node.getId());
						   			ipbase.setProtocol("");
						   			//IpAccountingBaseDao basedao = new IpAccountingBaseDao();
						   			try{
						   				basedao.save(ipbase);
						   			}catch(Exception e){
						   				e.printStackTrace();
						   			}finally{
						   				//basedao.close();
						   			}
						   			accountbaseid = ipbase.getId();
						   			Hashtable allipaccountipbase = new Hashtable();
						   			allipaccountipbase.put(ipaccounting.getSrcip()+":"+ipaccounting.getDestip()+":"+node.getId(), accountbaseid+"");
						   			ShareData.setAllipaccountipbases(allipaccountipbase);
						   			
						   		}
						   		ipaccounting.setAccountingBaseID(accountbaseid);
						   		ipaccounting.setCollecttime(coldate);
						   		//SysLogger.info("srcIP: "+ipaccounting.getSrcip()+" === dstIP:"+ipaccounting.getDestip()+" ===pkts:"+ipaccounting.getPkts()+" ===byts:"+ipaccounting.getByts());
						   		ipaccountVector.add(ipaccounting);
				   		  }catch(Exception e){
				   			  e.printStackTrace();
				   		  }
				   	  }
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					basedao.close();
				}
				
				
			   	  
			}
	
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
	    if(!(ShareData.getSharedata().containsKey(node.getIpAddress())))
		{
			Hashtable ipAllData = new Hashtable();
			if(ipAllData == null)ipAllData = new Hashtable();
			if(ipaccountVector != null && ipaccountVector.size()>0)ipAllData.put("ipaccount",ipaccountVector);
		    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		}else
		 {
			if(ipaccountVector != null && ipaccountVector.size()>0)((Hashtable)ShareData.getSharedata().get(node.getIpAddress())).put("ipaccount",ipaccountVector);
		   
		 }
	    returnHash.put("ipaccount", ipaccountVector);
	    
//	    //对CPU值进行告警检测
//	    Hashtable collectHash = new Hashtable();
//		collectHash.put("ipaccount", ipaccountVector);
//	    try{
//			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_NET, "cisco","cpu");
//			for(int i = 0 ; i < list.size() ; i ++){
//				AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//				//对CPU值进行告警检测
//				CheckEventUtil checkutil = new CheckEventUtil();
//				checkutil.updateData(node,collectHash,"net","cisco",alarmIndicatorsnode);
//				//}
//			}
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    }
		  //把结果转换成sql
	    NetipaccountResultTosql tosql=new NetipaccountResultTosql();
	    tosql.CreateResultTosql(ipaccountVector, node.getIpAddress());
	    String runmodel = PollingEngine.getCollectwebflag();//采集与访问模式
	    if(!"0".equals(runmodel)){
			//采集与访问是分离模式,则不需要将监视数据写入临时表格
	    	NetHostDatatempCpuRTosql totempsql=new NetHostDatatempCpuRTosql();
		    totempsql.CreateResultTosql(returnHash, node);
	    }
	    
	    return returnHash;
	}
}





