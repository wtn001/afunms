package com.afunms.polling.snmp.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.AlarmHelper;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.model.EnvConfig;
import com.afunms.emc.dao.envPerDao;
import com.afunms.emc.model.Disk;
import com.afunms.emc.model.Environment;
import com.afunms.emc.model.MemModel;
import com.afunms.emc.parser.DiskParser;
import com.afunms.emc.parser.EnvironmentParser;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.dao.ConnectDao;
import com.afunms.topology.model.Connect;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HDSEnvDriveResultTosql;

public class EMCEnvController {
	private String command;
	private Process process;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public EMCEnvController(String ipaddress, String name, String username,
			String password) {
		this.command = "cmd /c dscli -hmc1 "+ipaddress+ " -user " + username +" -passwd "+ password;
	}
	
	public boolean init(){
		return true;
	}
	
	
	public EMCEnvController() {   
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	
	
	
//	public Environment parse(){
//		String data = this.collectData();
//		Environment enviroment = EnvironmentParser.parse(data);
//		return enviroment;
//	}
	
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
//			
//			String fileName = "C:/Users/Administrator/Desktop/emc存储/environment.log";
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
		SysLogger.info("######## "+host.getIpAddress()+" 开始采集emc存储的环境数据##########");
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
		
		username = vo.getUsername();
		pws = vo.getPwd();
		try{
			if(!pws.equals(""))
			{
				password = EncryptUtil.decode(pws);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData == null)ipAllData = new Hashtable();
		
		Runtime runtime = Runtime.getRuntime();
		StringBuffer dataBuffer = new StringBuffer();
		try {
			SysLogger.info("cmd /c naviseccli -user " + username +" -password "+ password+" -Scope 0 -h "+host.getIpAddress()+" environment -list -all");
			process = runtime.exec("naviseccli -user " + username +" -password "+ password+" -Scope 0 -h "+host.getIpAddress()+" environment -list -all");
			inputStream = process.getInputStream();
			
//			String fileName = "C:/Users/Administrator/Desktop/emc存储/environment_-_1.log";
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(fileName)));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String inStr = "";
			while((inStr = bufferedReader.readLine()) != null){
				dataBuffer.append(inStr + "\r\n");
				System.out.println(inStr);
			}
//			parseLog(this.command , dataBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(dataBuffer.toString());
		
		Environment environment = EnvironmentParser.parse(dataBuffer.toString());
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
			if(environment != null )emcdata.put("environment",environment);
		    ShareData.getEmcdata().put(host.getIpAddress(), emcdata);
		}else
		 {
			if(environment != null )((Hashtable)ShareData.getEmcdata().get(host.getIpAddress())).put("environment",environment);
		 }
		 returnHash.put("environment", environment);
		    
		 try{
			    EMCLUNConfigController util = new EMCLUNConfigController();
				List list = util.EMCgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_STORAGE, "emc","envpower");
				
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
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("envwt")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"envpower","envwt",name,flag);//vid=AlarmIndicatorsNode.getSubentity();flag=map的键值(在utilDao  的queryLast()方法里定义的)；
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("envavgwt")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"envpower","envavgwt",name,flag);
					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memtmp")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memtmp",name,flag);
//					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memavgtmp")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memavgtmp",name,flag);
//					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memwt")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memwt",name,flag);
//					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memavgwt")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memavgwt",name,flag);
//					}
				
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		 
		    //---------
		    try{
			    EMCLUNConfigController util = new EMCLUNConfigController();
				List list = util.EMCgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_STORAGE, "emc","envstore");
				
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
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("envwt")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","envwt",name,flag);//vid=AlarmIndicatorsNode.getSubentity();flag=map的键值(在utilDao  的queryLast()方法里定义的)；
//					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("envavgwt")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","envavgwt",name,flag);
//					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memtmp")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"envstore","memtmp",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memavgtmp")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"envstore","memavgtmp",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memwt")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"envstore","memwt",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memavgwt")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"envstore","memavgwt",name,flag);
					}
				
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		    //----------
		    try{
			    EMCLUNConfigController util = new EMCLUNConfigController();
				List list = util.EMCgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_STORAGE, "emc","bakpower");
				
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
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("bakwt")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"bakpower","bakwt",name,flag);//vid=AlarmIndicatorsNode.getSubentity();flag=map的键值(在utilDao  的queryLast()方法里定义的)；
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("bakavgwt")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"bakpower","bakavgwt",name,flag);
					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memtmp")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memtmp",name,flag);
//					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memavgtmp")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memavgtmp",name,flag);
//					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memwt")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memwt",name,flag);
//					}
//					if(alarmIndicatorsnode.getName().equalsIgnoreCase("memavgwt")){
//						util.checkDisk(host,flag,alarmIndicatorsnode,"environment","memavgwt",name,flag);
//					}
				
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		 
		    
		    if(environment != null){
		    	com.afunms.emc.model.Array array = environment.getArray();
		    	List<MemModel> liststore = environment.getMemList();
		    	List<MemModel> listbakpower = environment.getBakPowerList();
		    	
		    	envPerDao dao = new envPerDao();
		    	dao.saveArray(array, host.getIpAddress());
		    	
		    	dao = new envPerDao();
		    	dao.saveStore(liststore, host.getIpAddress());
		    	
		    	dao = new envPerDao();
		    	dao.saveBakPower(listbakpower, host.getIpAddress());
		    	
		    }
		    SysLogger.info("########完成采集emc存储的环境信息##########");
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
		EMCEnvController emcEnvController = new EMCEnvController(ipaddress,name,username,password);
//		Environment environment = emcEnvController.parse();
		emcEnvController.destroy();
	}
}
