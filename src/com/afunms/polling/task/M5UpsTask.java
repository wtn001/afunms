/*
 * Created on 2011-05-10
 *
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.UPSNode;
import com.afunms.polling.om.Systemcollectdata;
import com.afunms.polling.snmp.battery.EmsBatterySnmp;
import com.afunms.polling.snmp.ping.PingSnmp;
import com.afunms.polling.snmp.statue.EmsStatueSnmp;
import com.afunms.polling.snmp.system.EmsSystemSnmp;
import com.afunms.polling.snmp.upsinput.EmsBypassSnmp;
import com.afunms.polling.snmp.upsinput.EmsInputSnmp;
import com.afunms.polling.snmp.upsoutput.EmsOutputSnmp;
import com.afunms.security.dao.MgeUpsDao;
import com.afunms.security.model.MgeUps;

/**
 * @author nielin
 * @date 2010-06-24    
 * ����Ϊ�����豸��task     
 *     
 */     
public class M5UpsTask extends MonitorTask {
	public void run() {  
		try{  
			SysLogger.info("**================******M5UpsTask ��ʼ�ɼ�UPS�豸5���ӵĲɼ�����================================");
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	try {
	    		//��ȡ�����õ����б�����ָ��
	    		monitorItemList = indicatorsdao.getByIntervalAndType("5", "m",1,"ups");
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	} finally {
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
    		int numThreads = 20;        		
//    		try {
//    			List numList = new ArrayList();
//    			TaskXml taskxml = new TaskXml();
//    			numList = taskxml.ListXml();
//    			for (int i = 0; i < numList.size(); i++) {
//    				Task task = new Task();
//    				BeanUtils.copyProperties(task, numList.get(i));
//    				if (task.getTaskname().equals("netthreadnum")){
//    					numThreads = task.getPolltime().intValue();
//    				}
//    			}
//    		} catch (Exception e) {
//    			e.printStackTrace();
//    		}
    		MgeUpsDao nodedao = new MgeUpsDao();
    		List nodelist = new ArrayList();
    		try {
    			nodelist = nodedao.loadMonitorUps();
    		} catch(Exception e) {
    			e.printStackTrace();
    		} finally {
    			nodedao.close();
    		}
    		Hashtable nodehash = new Hashtable();
    		if(nodelist != null && nodelist.size()>0){
    			for(int i=0;i<nodelist.size();i++){
    				MgeUps node = (MgeUps)nodelist.get(i);
    				nodehash.put(node.getId()+"", node.getId());
    			}
    		}
	        final Hashtable alldata = new Hashtable();
    		// �����̳߳�
    		ThreadPool threadPool = new ThreadPool(numThreads);														
    		// ��������
    		for (int i=0; i<monitorItemList.size(); i++) {
    			NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
    			//���˵������ӵ��豸
    			if(!nodehash.containsKey(nodeGatherIndicators.getNodeid()))continue;
		        threadPool.runTask(createTask(nodeGatherIndicators,alldata));           		
    		}
    		// �ر��̳߳ز��ȴ������������
    		threadPool.join();  											
    		HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
    		try {
				hostdataManager.createHostItemData(alldata, "ups");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch(Exception e) {					 	
			e.printStackTrace();
		} finally {
			Runtime.getRuntime().gc();
			SysLogger.info("********M5UpsTask Thread Count : "+Thread.activeCount());
		}
		SysLogger.info("**================******M5UpsTask ��ɲɼ�UPS�豸5���ӵĲɼ�����================================");
	}
	
	/**
    ��������
	 */	
	private static Runnable createTask(final NodeGatherIndicators nodeGatherIndicators,final Hashtable alldata) {
    return new Runnable() {
        public void run() {
            try {                	
            	Hashtable returnHash = new Hashtable();
            	
            	if(nodeGatherIndicators.getName().equalsIgnoreCase("battery")){
            		
            		if(nodeGatherIndicators.getSubtype().equalsIgnoreCase("ems")){
            			
            			UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			
            			if(node != null){
            				EmsBatterySnmp batterysnmp = null;
                			try{
                				batterysnmp = (EmsBatterySnmp)Class.forName("com.afunms.polling.snmp.battery.EmsBatterySnmp").newInstance();
                    			returnHash = batterysnmp.collect_Data(nodeGatherIndicators);
//                    			SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@battery:"+returnHash.size());
                    			try {
									if(returnHash!=null){
										Vector batteryVector = (Vector)returnHash.get("battery");
										if(batteryVector != null || batteryVector.size() < 0){
											for(int i = 0 ; i < batteryVector.size(); i++){
												Systemcollectdata collectdata = (Systemcollectdata)batteryVector.get(i); 
												updateData(node, nodeGatherIndicators, collectdata.getThevalue(),collectdata.getSubentity());
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									SysLogger.error("��Ĭ�� ��� �澯����" , e);
								}
								if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("battery", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("battery", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("battery", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            		}
            	}else if(nodeGatherIndicators.getName().equalsIgnoreCase("statue")){
//            		ups״̬�ɼ�
            		if(nodeGatherIndicators.getSubtype().equalsIgnoreCase("ems")){
            			UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
                		if(node != null){
                			EmsStatueSnmp statueSnmp = null;
                			try{
                				statueSnmp = (EmsStatueSnmp)Class.forName("com.afunms.polling.snmp.statue.EmsStatueSnmp").newInstance();
                    			returnHash = statueSnmp.collect_Data(nodeGatherIndicators);
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("statue", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("statue", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("statue", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
                		}
            		}
            	}else if(nodeGatherIndicators.getName().equalsIgnoreCase("systemgroup")){
            		if(nodeGatherIndicators.getSubtype().equalsIgnoreCase("ems")){
            			UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
                		if(node != null){
                			EmsSystemSnmp systemsnmp = null;
                			try{
                				systemsnmp = (EmsSystemSnmp)Class.forName("com.afunms.polling.snmp.system.EmsSystemSnmp").newInstance();
                    			returnHash = systemsnmp.collect_Data(nodeGatherIndicators);
//                    			SysLogger.info("@@@@@@@@@@@@@@@@@@@@systemgroup:"+returnHash);
                    			if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("systemgroup", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("systemgroup", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("systemgroup", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
                		}
            		}
            	} else if(nodeGatherIndicators.getName().equalsIgnoreCase("input")){
            		//UPS������Ϣ
                    if(nodeGatherIndicators.getSubtype().equalsIgnoreCase("ems")){
            			
            			UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			if(node != null){
            				EmsInputSnmp inputsnmp = null;
                			try{
                				inputsnmp = (EmsInputSnmp)Class.forName("com.afunms.polling.snmp.upsinput.EmsInputSnmp").newInstance();
                    			returnHash = inputsnmp.collect_Data(nodeGatherIndicators);
                    			try {
									if(returnHash!=null){
										Vector inputVector = (Vector)returnHash.get("input");
										if(inputVector != null || inputVector.size() < 0){
											for(int i = 0 ; i < inputVector.size(); i++){
												Systemcollectdata collectdata = (Systemcollectdata)inputVector.get(i); 
												updateData(node, nodeGatherIndicators, collectdata.getThevalue(),collectdata.getSubentity());
											}
										}
									} else {}
								} catch (Exception e) {
									e.printStackTrace();
									SysLogger.error("��Ĭ�� ������Ϣ �澯����" , e);
								}
//								SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@input:"+returnHash.size());
								if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("input", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("input", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("input", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    		} catch(Exception e) {
                    			e.printStackTrace();
                    		}
            			}
            		}
            	} else if(nodeGatherIndicators.getName().equalsIgnoreCase("bypass")){
            		//UPS��·��Ϣ
                    if(nodeGatherIndicators.getSubtype().equalsIgnoreCase("ems")){
            			
            			UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			if(node != null){
            				EmsBypassSnmp bypasssnmp = null;
                			try{
                				bypasssnmp = (EmsBypassSnmp)Class.forName("com.afunms.polling.snmp.upsinput.EmsBypassSnmp").newInstance();
                    			returnHash = bypasssnmp.collect_Data(nodeGatherIndicators);
                    			try {
									if(returnHash!=null){
										Vector bypassVector = (Vector)returnHash.get("bypass");
										if(bypassVector != null || bypassVector.size() < 0){
											for(int i = 0 ; i < bypassVector.size(); i++){
												Systemcollectdata collectdata = (Systemcollectdata)bypassVector.get(i); 
												updateData(node, nodeGatherIndicators, collectdata.getThevalue(),collectdata.getSubentity());
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									SysLogger.error("��Ĭ�� ��·��Ϣ �澯����" , e);
								}
//								SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@bypass:"+returnHash.size());
								if(alldata.containsKey(node.getIpAddress())) {
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("bypass", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("bypass", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			} else {
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("bypass", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    		} catch(Exception e) {
                    			e.printStackTrace();
                    		}
            			}
            		}
            	} else if(nodeGatherIndicators.getName().equalsIgnoreCase("output")){
            		//UPS�����Ϣ
                    if(nodeGatherIndicators.getSubtype().equalsIgnoreCase("ems")){
            			
            			UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            			if(node != null){
            				EmsOutputSnmp outputsnmp = null;
                			try{
                				outputsnmp = (EmsOutputSnmp)Class.forName("com.afunms.polling.snmp.upsoutput.EmsOutputSnmp").newInstance();
                    			returnHash = outputsnmp.collect_Data(nodeGatherIndicators);
                    			try {
									if(returnHash!=null){
										Vector outputVector = (Vector)returnHash.get("output");
										if(outputVector != null || outputVector.size() < 0){
											for(int i = 0 ; i < outputVector.size(); i++){
												Systemcollectdata collectdata = (Systemcollectdata)outputVector.get(i); 
												updateData(node, nodeGatherIndicators, collectdata.getThevalue(),collectdata.getSubentity());
											}
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
									SysLogger.error("��Ĭ�� �����Ϣ �澯����" , e);
								}
//								SysLogger.info("@@@@@@@@@@@@@@@@@@@@@@output:"+returnHash.size());
								if(alldata.containsKey(node.getIpAddress())){
                    				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                    				if(ipdata != null){
                    					ipdata.put("output", returnHash);
                    				}else{
                    					ipdata = new Hashtable();
                    					ipdata.put("output", returnHash);
                    				}
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}else{
                    				Hashtable ipdata = new Hashtable();
                    				ipdata.put("output", returnHash);
                    				alldata.put(node.getIpAddress(), ipdata);
                    			}
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
            			}
            		}
            	} else if(nodeGatherIndicators.getName().equalsIgnoreCase("ping")){
            		UPSNode node = (UPSNode)PollingEngine.getInstance().getUpsByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
            		if(node != null) {
            			PingSnmp pingsnmp = null;
            			try {  
            				pingsnmp = (PingSnmp)Class.forName("com.afunms.polling.snmp.ping.PingSnmp").newInstance();
                			returnHash = pingsnmp.collect_Data(nodeGatherIndicators);
                		} catch(Exception e) {
                			e.printStackTrace();
                		}
            		}
            	} else if(nodeGatherIndicators.getName().equalsIgnoreCase("alarm")){
                    if(nodeGatherIndicators.getSubtype().equalsIgnoreCase("ems")){
            			
            		}
            	}
        		
            }catch(Exception exc){
            	  exc.printStackTrace();
            }
        }
        
        // ����澯
        public void updateData(UPSNode node, NodeGatherIndicators nodeGatherIndicators, String value, String name){
        	NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodeDTO = nodeUtil.creatNodeDTOByNode(node);
        	AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
        	List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(node.getId()+"", nodeGatherIndicators.getType(),nodeGatherIndicators.getSubtype(),name.toUpperCase());
        	if(list != null && list.size()>0){
        		CheckEventUtil checkEventUtil = new CheckEventUtil();
        		for(int i = 0 ; i < list.size(); i++){
        			AlarmIndicatorsNode nm =  (AlarmIndicatorsNode)list.get(i);
//        			SysLogger.info(node.getId() + ":" + nm.getName() + ":" + nm.getType() + ":" + nm.getSubtype() + ":" + value);
        			checkEventUtil.checkEvent(nodeDTO, nm, value, name);
        		}
        	}
        }
    };
	}
}
