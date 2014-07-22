package com.afunms.polling.snmp.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
import com.afunms.emc.dao.diskConDao;
import com.afunms.emc.dao.diskPerDao;
import com.afunms.emc.dao.raidDao;
import com.afunms.emc.dao.sysDao;
import com.afunms.emc.model.Agent;
import com.afunms.emc.model.Disk;
import com.afunms.emc.model.RaidGroup;
import com.afunms.emc.parser.DiskParser;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.VMWareConnectConfig;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.dao.ConnectDao;
import com.afunms.topology.dao.VMWareConnectConfigDao;
import com.afunms.topology.model.Connect;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HDSEnvDriveResultTosql;



public class EMCDiskController {
	private String command;
	private Process process;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public EMCDiskController(String ipaddress, String name, String username,
			String password) {
		this.command = "cmd /c dscli -hmc1 "+ipaddress+ " -user " + username +" -passwd "+ password;
	}
	
	public boolean init(){
		return true;
	}
	
	public EMCDiskController() {   
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	
	
//	public List<Disk> parse(){
//		String data = this.collectData();
//		List<Disk> diskList = DiskParser.parse(data);
//		return diskList;
//	}
	//NodeGatherIndicators alarmIndicatorsNode
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode){
		Hashtable returnHash = new Hashtable();
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));//Integer.parseInt(alarmIndicatorsNode.getNodeid())
		SysLogger.info("######## "+host.getIpAddress()+" 开始采集emc存储的磁盘数据##########");
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
			//process = runtime.exec(this.command);
			SysLogger.info("cmd /c naviseccli -user " + username +" -password "+ password+" -Scope 0 -h "+host.getIpAddress()+" getdisk -all");
			process = runtime.exec("naviseccli -user " + username +" -password "+ password+" -Scope 0 -h "+host.getIpAddress()+" getdisk -all");
			inputStream = process.getInputStream();
			
//			naviseccli -User admin -Password passwor
//			d -Scope 0 -h 10.254.100.230 getdisk -all
			
//			String fileName = "C:/Users/Administrator/Desktop/emc存储/getdisk.log";
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
		
		List<Disk> diskList = DiskParser.parse(dataBuffer.toString());
		Hashtable emcdata = null;
		if(!(ShareData.getEmcdata().containsKey(host.getIpAddress())))
		{
			if(emcdata == null)emcdata = new Hashtable();
			if(diskList != null && diskList.size()>0)emcdata.put("disk",diskList);
		    ShareData.getEmcdata().put(host.getIpAddress(), emcdata);
		}else
		 {
			if(diskList != null && diskList.size()>0)((Hashtable)ShareData.getEmcdata().get(host.getIpAddress())).put("disk",diskList);
		 }
		 returnHash.put("disk", diskList);
		    
		 try{
			    EMCLUNConfigController util = new EMCLUNConfigController();
				List list = util.EMCgetAlarmInicatorsThresholdForNode(String.valueOf(alarmIndicatorsNode.getNodeid()), AlarmConstant.TYPE_STORAGE, "emc","disk");
				
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
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskhardread")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskhardread",name,flag);//vid=AlarmIndicatorsNode.getSubentity();flag=map的键值(在utilDao  的queryLast()方法里定义的)；
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("disksoftwrite")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskread",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskread")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskread",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskwrite")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskwrite",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskreadkb")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskreadkb",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskwritekb")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskwritekb",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskhardwrite")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskhardwrite",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("disksoftwrite")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","disksoftwrite",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskfree")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskfree",name,flag);
					}
					if(alarmIndicatorsnode.getName().equalsIgnoreCase("diskbus")){
						util.checkDisk(host,flag,alarmIndicatorsnode,"disk","diskbus",name,flag);
					}
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    }
		    
		    //把采集结果生成sql
		    //基本信息入库
		    diskConDao sys = new diskConDao();
		    List<Disk> a_vo = sys.query(alarmIndicatorsNode.getNodeid()+"");
		    
		    if(a_vo.size()>0 && diskList != null && diskList.size()>0 && a_vo.size()>0){ 
		    	sys = new diskConDao();
		    	sys.delete(alarmIndicatorsNode.getNodeid()+"");
		    	sys = new diskConDao();
		    	sys.saveList(diskList, alarmIndicatorsNode.getNodeid()+"");
		    }else if(a_vo.size() == 0 && diskList != null && diskList.size()>0){
		    	sys = new diskConDao();
		    	sys.saveList(diskList, alarmIndicatorsNode.getNodeid()+"");
		    }
		    //性能入库
		    if(diskList != null && diskList.size()>0){
		    	diskPerDao dao = new diskPerDao();
		    	dao.saveList(diskList, host.getIpAddress());
		    }
		    SysLogger.info("########完成采集emc存储的磁盘信息##########");
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
		EMCDiskController emcDiskController = new EMCDiskController(ipaddress,name,username,password);
//		emcDiskController.collect_data(); 
//		List<Disk> diskList = emcDiskController.parse();
		emcDiskController.destroy();
	}
}
