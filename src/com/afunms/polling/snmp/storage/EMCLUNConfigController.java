package com.afunms.polling.snmp.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.dao.AlarmIndicatorsNodeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.AlarmHelper;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.EnvConfig;
import com.afunms.emc.dao.lunConDao;
import com.afunms.emc.dao.lunPerDao;
import com.afunms.emc.dao.raidDao;
import com.afunms.emc.dao.utilDao;
import com.afunms.emc.model.Crus;
import com.afunms.emc.model.Lun;
import com.afunms.emc.model.RaidGroup;
import com.afunms.emc.parser.CrusParser;
import com.afunms.emc.parser.LunParser;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.dao.ConnectDao;
import com.afunms.topology.dao.VMWareVidDao;
import com.afunms.topology.model.Connect;
import com.afunms.topology.model.HostNode;
import com.afunms.vmware.vim25.vo.VMWareDao;
import com.gatherResulttosql.HDSEnvDriveResultTosql;



public class EMCLUNConfigController {
	private String command;
	private Process process; 
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public EMCLUNConfigController(String ipaddress, String name, String username,
			String password) {
		this.command = "cmd /c dscli -hmc1 "+ipaddress+ " -user " + username +" -passwd "+ password;
	}
	
	public boolean init(){
		return true;
	}
	
	public EMCLUNConfigController() {   
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	
//	public List<Lun> parse(){
//		String data = this.collectData();
//		List<Lun> diskList = LunParser.parse(data);
//		return diskList;
//	}
//	
//	public String collectData(){
//		Runtime runtime = Runtime.getRuntime();
//		StringBuffer dataBuffer = new StringBuffer();
//		try {
////			process = runtime.exec("cmd /c dscli -hmc1 "+ipaddress+ " -user " + username +" -passwd "+ password);
//			process = runtime.exec(this.command);
//			inputStream = process.getInputStream();
////			outputStream = process.getOutputStream();
////			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
//			
//			
//			String fileName = "C:/Users/Administrator/Desktop/emc存储/getlun.log";
////			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(fileName)));
//			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//			
//			
//			
////			outputStreamWriter.write(this.command + "\r\n");
////			outputStreamWriter.write("exit" + "\r\n");
////			outputStreamWriter.flush();
////			outputStreamWriter.close();
//			String inStr = "";
//			
//			while((inStr = bufferedReader.readLine()) != null){
//				dataBuffer.append(inStr + "\r\n");
////				System.out.println(inStr);
//			}
////			parseLog(this.command , dataBuffer);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		System.out.println(dataBuffer.toString());
//		return dataBuffer.toString();
//	}
	
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode){
		Hashtable returnHash = new Hashtable();
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));//Integer.parseInt(alarmIndicatorsNode.getNodeid())
		SysLogger.info("######## "+host.getIpAddress()+" 开始采集emc存储的lun数据##########");
		if(host == null)return returnHash;
		
		//判断是否在采集时间段内
    	if(ShareData.getTimegatherhash() != null){
    		if(ShareData.getTimegatherhash().containsKey(host.getId()+":equipment")){
    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
    			int _result = 0;
    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(host.getId()+":equipment"));
    			if(_result ==1 ){
    				//SysLogger.info("########时间段内: 开始采集 "+node.getIpAddress()+" PING数据信息##########");
    			}else if(_result == 2){
    				//SysLogger.info("########全天: 开始采集 "+node.getIpAddress()+" PING数据信息##########");
    			}else {
    				SysLogger.info("######## "+host.getIpAddress()+" 不在采集flash时间段内,退出##########");
//    				//清除之前内存中产生的告警信息
//    			    try{
//    			    	//清除之前内存中产生的内存告警信息
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
    	
    	String username = "";
    	String pws = "";
    	String password = "";
		ConnectDao condao = new ConnectDao();
		List<Connect> list_vo = condao.getbynodeid(Long.parseLong(host.getId()+""));
		Connect vo = null;
		if(list_vo != null && list_vo.size()>0)
		{
			 vo = (Connect) list_vo.get(0);
		}
		
//		username = vo.getUsername();
//		pws = vo.getPwd();
//		try{
//			if(!pws.equals(""))
//			{
//				password = EncryptUtil.decode(pws);
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData == null)ipAllData = new Hashtable();
		
		Runtime runtime = Runtime.getRuntime();
		StringBuffer dataBuffer = new StringBuffer();
		try {
//			process = runtime.exec(this.command);
//			process = runtime.exec("cmd /c dscli -hmc1 "+host.getIpAddress()+ " -user " + username +" -passwd "+ password);
//			inputStream = process.getInputStream();
			
			String fileName = "C:/Users/Administrator/Desktop/emc存储/getlun.log";
//			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(fileName)));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String inStr = "";
			while((inStr = bufferedReader.readLine()) != null){
				dataBuffer.append(inStr + "\r\n");
//				System.out.println(inStr);
			}
//			parseLog(this.command , dataBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(dataBuffer.toString());
		
		List<Lun> lunList = LunParser.parse(dataBuffer.toString());
//		for(int i =0;i<diskList.size();i++)
//		{
//			Disk vo = diskList.get(i);
//			System.out.println(vo.getId());
//			System.out.println(vo.getIdleTicks());
//			System.out.println(vo.getBusyTicks());
//			System.out.println(vo.getCapacity());
//			System.out.println(vo.getCurrentSpeed());
//			System.out.println(vo.getDriveType());
//			System.out.println(vo.getHardReadErrors());
//			System.out.println(vo.getHardWriteErrors());
//			System.out.println(vo.getHotSpare());
//			System.out.println(vo.getKbytesRead());
//			System.out.println(vo.getMaximumSpeed());
//			System.out.println("--------------------------");
//		}
		Hashtable emcdata = null;
		if(!(ShareData.getEmcdata().containsKey(host.getIpAddress())))
		{
			if(emcdata == null)emcdata = new Hashtable();
			if(lunList != null && lunList.size()>0)emcdata.put("lunconfig",lunList);
		    ShareData.getEmcdata().put(host.getIpAddress(), emcdata);
		}else
		 {
			if(lunList != null && lunList.size()>0)((Hashtable)ShareData.getEmcdata().get(host.getIpAddress())).put("lunconfig",lunList);
		 }
		 returnHash.put("lunconfig", lunList);
		    
//		    try{
//				AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_STORAGE, "emc","lunconfig");
//				
//				AlarmHelper helper=new AlarmHelper();
//				Hashtable<String, EnvConfig> envHashtable=helper.getAlarmConfig(host.getIpAddress(), "lunconfig");
//				for(int i = 0 ; i < list.size() ; i ++){
//					 
//					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//					
//					//对风扇值进行告警检测
//					CheckEventUtil checkutil = new CheckEventUtil();
//					if (lunList.size()>0) {
//						for (int j = 0; j < lunList.size(); j++) {
//							Lun data=(Lun)lunList.get(j);
//							if (data!=null) {
//								EnvConfig config=envHashtable.get(data.getEntity());
//								if(config!=null&&config.getEnabled()==1){
//									alarmIndicatorsnode.setAlarm_level(config.getAlarmlevel());
//									alarmIndicatorsnode.setAlarm_times(config.getAlarmtimes()+"");
//									alarmIndicatorsnode.setLimenvalue0(config.getAlarmvalue()+"");
//								    checkutil.checkEvent(host,alarmIndicatorsnode,data.getThevalue(),data.getSubentity());
//								  
//								}
//							}
//							
//						}
//						
//					}
//					
//				}
//				
//		    }catch(Exception e){
//		    	e.printStackTrace();
//		    }
		 
		 try{
				List list = this.EMCgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_STORAGE, "emc_vnx","lun");
				
				for(int i = 0 ; i < list.size() ; i ++){
					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
					String flag = "";
					String name = "";
					if(alarmIndicatorsnode.getEnabled().equals("1")){
						flag = alarmIndicatorsnode.getSubentity();
//						VMWareVidDao queryname = new VMWareVidDao();
//						name = queryname.queryName(host.getId()+"", flag);
						name = alarmIndicatorsnode.getName();
					}else{
						continue;
					}
//					SysLogger.info("alarmIndicatorsnode name ======"+alarmIndicatorsnode.getName());
					//Host node,String vid,AlarmIndicatorsNode nm,String category,String flag,String name,String guestname
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("harderror")){
					    this.checkDisk(host,flag,alarmIndicatorsnode,"lun","harderror",name,flag);//vid=AlarmIndicatorsNode.getSubentity();flag=map的键值(在utilDao  的queryLast()方法里定义的)；
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("softerror")){
						this.checkDisk(host,flag,alarmIndicatorsnode,"lun","softerror",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("lunlength")){
						this.checkDisk(host,flag,alarmIndicatorsnode,"lun","length",name,flag);
					}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		 
		 
		 
		 
		 
//		    enviroment=null;
		    
		    //把采集结果生成sql
		    lunConDao sys = new lunConDao();
		    List<Lun> a_vo = sys.query(alarmIndicatorsNode.getNodeid()+"");
//		    if(a_vo != null && lunList != null && lunList.size()>0 && a_vo.size()>0){ 
//		    	sys = new lunConDao();
//		    	sys.delete(alarmIndicatorsNode.getNodeid()+"");
//		    	sys = new lunConDao();
//		    	sys.saveList(lunList, alarmIndicatorsNode.getNodeid()+"");
//		    }else
            if(a_vo.size() == 0 && lunList != null && lunList.size()>0){
		    	sys.saveList(lunList, alarmIndicatorsNode.getNodeid()+"");
		    }
		    
		    //性能入库
		    if(lunList != null && lunList.size()>0){
		    	lunPerDao dao = new lunPerDao();
		    	dao.saveList(lunList, host.getIpAddress());
		    }
		    SysLogger.info("########完成采集emc存储的lun信息##########");
		    return returnHash;
	}
	
	
	
	public boolean destroy(){
		try {
			if(inputStream != null){
				inputStream.close();
			}
			if(outputStream != null){
				outputStream.close();
			}
			if(process != null){
				process.destroy();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void main(String[] args) {
		String ipaddress = "10.204.109.23";
		String name = "10.204.109.23";
		String username = "admin";
		String password = "passw0rd";
		EMCLUNConfigController emcLUNConfigController = new EMCLUNConfigController(ipaddress,name,username,password);
//		List<Lun> lunList = emcLUNConfigController.parse();
		emcLUNConfigController.destroy();
	}
	
	
	public void checkDisk(Host node,String vid,AlarmIndicatorsNode nm,String category,String flag,String name,String guestname){
		SysLogger.info("### 开始运行emc存储--"+category+"--指标是否告警... ###");
		
//		System.out.println(nm.getName()+"-------------------------------"+nm.getEnabled());
		
		CheckEventUtil ce = new CheckEventUtil();
		if ("0".equals(nm.getEnabled())) {
			//告警指标未监控 不做任何事情 返回
			return;
		}
//		if (vid == null || vid.size() == 0) {
//			//未采集到数据 不做任何事情 返回
//			return;
//		}
//			for(int i=0;i<map.size();i++){
				AlarmIndicatorsNodeDao alarm = new AlarmIndicatorsNodeDao();
				AlarmIndicatorsNode vo = new AlarmIndicatorsNode();
				List list = (List) alarm.VMgetByNodeIdAndTypeAndSubType(node.getId()+"", "storage", "emc_vnx", category, vid,name);
				if(list.size()>0){
				vo = (AlarmIndicatorsNode)list.get(0);
//				if(vo.getEnabled().equals("0"))break;
				int limevalue0 = Integer.parseInt(vo.getLimenvalue0());
				int limevalue1 = Integer.parseInt(vo.getLimenvalue1());
				int limevalue2 =Integer.parseInt(vo.getLimenvalue2());
				System.out.println(nm.getName() + "============一级阀值=============" + limevalue0 + "==二级阀值==" + limevalue1 + "===三级阀值===" + limevalue2);
				nm.setLimenvalue0(limevalue0 + "");
				nm.setLimenvalue1(limevalue1 + "");
				nm.setLimenvalue2(limevalue2 + "");
				NodeUtil nodeUtil = new NodeUtil();
				NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
				
				utilDao dao = new utilDao();
				String tablename ="";
				if(category.equalsIgnoreCase("lun")){
					tablename = "emclunper";
				}else if(category.equalsIgnoreCase("disk")){
					tablename = "emcdiskper";
				}else if(category.equalsIgnoreCase("envpower")){
					tablename = "emcenvpower";
				}else if(category.equalsIgnoreCase("envstore")){
					tablename = "emcenvstore";
				}else if(category.equalsIgnoreCase("bakpower")){
					tablename = "emcbakpower";
				}
				HashMap value = dao.queryLast(tablename, node.getIpAddress(), vid);
				SysLogger.info("###########"+category+"告警检测###############################");
				if(value.size()!=0){
				  SysLogger.info("###########"+category+":"+value.get(flag).toString()+"告警检测###############################");
				  ce.checkEvent(nodeDTO, nm, value.get(flag).toString(), guestname);
				}
//				}
			}
	}

	public List EMCgetAlarmInicatorsThresholdForNode(String nodeId , String type , String subtype,String category){ 
		String key= nodeId +":"+ type+":"+ subtype; 
//		SysLogger.info("##########################虚拟机告警:"+key);
		List resultList=new ArrayList();//根据Key查询得到的结果集合需要调整的 
		try{
			Hashtable hs=ResourceCenter.getInstance().getAlarmHashtable();
			
			
			if(hs == null) hs = new Hashtable();
			if(category != null && category.trim().length()>0){				
				if(hs.containsKey(key))
				{
					resultList=(ArrayList)hs.get(key);
				}
			}else{
				if(hs.size()>0){
					Enumeration newProEnu = hs.keys();
					while(newProEnu.hasMoreElements())
					{
						String alarmName = (String)newProEnu.nextElement();
						if(alarmName.startsWith(nodeId +":"+ type+":")){
							resultList=(ArrayList)hs.get(alarmName);
						}
					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		List list = new ArrayList();
		for(int i=0;i<resultList.size();i++){
			AlarmIndicatorsNode vo = new AlarmIndicatorsNode();
			vo = (AlarmIndicatorsNode) resultList.get(i);
			if(vo.getCategory().equalsIgnoreCase(category)){
				list.add(vo);
			}
		}
		return list;  
	} 
	
	
	
	
}
