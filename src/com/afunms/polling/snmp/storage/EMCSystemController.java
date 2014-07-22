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
import com.afunms.emc.dao.sysDao;
import com.afunms.emc.model.Agent;
import com.afunms.emc.model.RaidGroup;
import com.afunms.emc.parser.AgentParser;
import com.afunms.emc.parser.RaidGroupParser;
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


public class EMCSystemController {
	private String command;
	private Process process;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public EMCSystemController(String ipaddress, String name, String username,
			String password) {
		this.command = "cmd /c dscli -hmc1 "+ipaddress+ " -user " + username +" -passwd "+ password;
	}
	
	public boolean init(){
		return true;
	}
	
	public EMCSystemController() {   
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	
//	public Agent parse(){
//		String data = this.collectData();
//		Agent agent = AgentParser.parse(data);
//		return agent;
//	}
//	
//	public String collectData(){
//		Runtime runtime = Runtime.getRuntime();
//		StringBuffer dataBuffer = new StringBuffer();
//		try {
////			process = runtime.exec("cmd /c dscli -hmc1 "+ipaddress+ " -user " + username +" -passwd "+ password);
//			process = runtime.exec(command);
//			inputStream = process.getInputStream();
////			outputStream = process.getOutputStream();
////			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
//			
//			String fileName = "C:/Users/Administrator/Desktop/emc存储/getagent.log";
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
		SysLogger.info("######## "+host.getIpAddress()+" 开始采集emc存储的系统信息数据##########");
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
			SysLogger.info("cmd /c naviseccli -user " + username +" -password "+ password+" -Scope 0 -h "+host.getIpAddress()+" getagent");
			process = runtime.exec("naviseccli -user " + username +" -password "+ password+" -Scope 0 -h "+host.getIpAddress()+" getagent");
			inputStream = process.getInputStream();
			
//			String fileName = "C:/Users/Administrator/Desktop/emc存储/getagent.log";
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(new File(fileName)));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String inStr = "";
			while((inStr = bufferedReader.readLine()) != null){
				dataBuffer.append(inStr + "\r\n");
				//System.out.println(inStr);
			}
//			parseLog(this.command , dataBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.out.println(dataBuffer.toString());
		
		Agent agent = AgentParser.parse(dataBuffer.toString());
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
			if(agent != null )emcdata.put("system",agent);
		    ShareData.getEmcdata().put(host.getIpAddress(), emcdata);
		}else
		 {
			if(agent != null)((Hashtable)ShareData.getEmcdata().get(host.getIpAddress())).put("system",agent);
		 }
		 returnHash.put("system", agent);
		    
		    
		    //把采集结果生成sql
		    sysDao sys = new sysDao();
		    Agent a_vo = sys.query(alarmIndicatorsNode.getNodeid()+"");
		    if(a_vo != null && agent != null){ 
		    	sys = new sysDao();
		    	sys.delete(alarmIndicatorsNode.getNodeid()+"");
		    	sys = new sysDao();
		    	sys.save(agent, alarmIndicatorsNode.getNodeid()+"");
		    }else if(a_vo == null && agent != null){
		    	sys = new sysDao();
		    	sys.save(agent, alarmIndicatorsNode.getNodeid()+"");
		    }
		    SysLogger.info("########完成采集emc存储的系统信息##########");
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
		EMCSystemController emcSystemController = new EMCSystemController(ipaddress,name,username,password);
//		Agent agent = emcSystemController.parse();
		emcSystemController.destroy();
	}
}
