/*
 * Created on 2011-07-9
 *
 */
package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.apache.commons.beanutils.BeanUtils;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.cpu.CiscoIronportCpuSnmp;
import com.afunms.polling.snmp.disk.CiscoDiskIOSnmp;
import com.afunms.polling.snmp.fan.CiscoIronportFanSnmp;
import com.afunms.polling.snmp.interfaces.InterfaceSnmp;
import com.afunms.polling.snmp.interfaces.PackageSnmp;
import com.afunms.polling.snmp.memory.CiscoIronportMemorySnmp;
import com.afunms.polling.snmp.memory.CiscoIronportMemoryStatueSnmp;
import com.afunms.polling.snmp.ping.PingSnmp;
import com.afunms.polling.snmp.power.CiscoIronportPowerSnmp;
import com.afunms.polling.snmp.queue.CiscoQueueMessageSnmp;
import com.afunms.polling.snmp.queue.CiscoQueueSnmp;
import com.afunms.polling.snmp.queue.CiscoQueueStatueSnmp;
import com.afunms.polling.snmp.raid.CiscoRaidSnmp;
import com.afunms.polling.snmp.system.SystemSnmp;
import com.afunms.polling.snmp.temperature.CiscoIronportTemperatureSnmp;
import com.afunms.topology.dao.ConnectTypeConfigDao;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.ConnectTypeConfig;
import com.afunms.topology.model.HostNode;

/**
 * @author yangjun
 * @date 2010-07-9 
 * ���ʼ���ȫ�����豸���вɼ�
 */
public class M5GatewayTask extends MonitorTask {
	
	public void run() {
		try{
			//SysLogger.info("wwwwwwwwwwwwwwwwwwwwwwM5GatewayTaskwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww: "+Thread.activeCount());
	        ConnectTypeConfigDao connectTypeConfigDao = new ConnectTypeConfigDao();
	        Hashtable connectConfigHashtable = new Hashtable();
			List configList = new ArrayList();
			try{  
				configList = connectTypeConfigDao.loadAll();
			}catch(Exception e){
				
			}finally{
				connectTypeConfigDao.close();
				connectTypeConfigDao = null;
			}
			if(configList != null && configList.size()>0){
				for(int i=0;i<configList.size();i++){
					ConnectTypeConfig connectTypeConfig = (ConnectTypeConfig)configList.get(i);
					connectConfigHashtable.put(connectTypeConfig.getNode_id(), connectTypeConfig);
				}
			}
			ShareData.getConnectConfigHashtable().put("connectConfigHashtable", connectConfigHashtable);
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	try {
	    		//��ȡ�����õ����б�����ָ��
	    		monitorItemList = indicatorsdao.getByIntervalAndType("5", "m",1,"gateway");
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	} finally {
	    		indicatorsdao.close();
	    	}
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
			int numThreads = 200;        		
    		try {
    			List numList = new ArrayList();
    			TaskXml taskxml = new TaskXml();
    			numList = taskxml.ListXml();
    			for (int i = 0; i < numList.size(); i++) {
    				Task task = new Task();
    				BeanUtils.copyProperties(task, numList.get(i));
    				if (task.getTaskname().equals("netthreadnum")){
    					numThreads = task.getPolltime().intValue();
    				}
    			}

    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		HostNodeDao nodedao = new HostNodeDao();
    		List nodelist = new ArrayList();
    		try{
    			nodelist = nodedao.loadMonitorGateway();
    		}catch(Exception e){
    			
    		}finally{
    			nodedao.close();
    		}
    		Hashtable nodehash = new Hashtable();
    		if(nodelist != null && nodelist.size()>0){
    			for(int i=0;i<nodelist.size();i++){
    				HostNode node = (HostNode)nodelist.get(i);
    				nodehash.put(node.getId()+"", node.getId());
    			}
    		}        		
    		final Hashtable alldata = new Hashtable();
    		Date _startdate = new Date();
    		Hashtable docollcetHash = new Hashtable();
    		
    		for (int i=0; i<monitorItemList.size(); i++) {
    			NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
    			if(docollcetHash.containsKey(nodeGatherIndicators.getNodeid())){
        			//���˵������ӵ��豸
        			if(!nodehash.containsKey(nodeGatherIndicators.getNodeid()))continue;
    				List tempList = (List)docollcetHash.get(nodeGatherIndicators.getNodeid());
    				tempList.add(nodeGatherIndicators);
    				docollcetHash.put(nodeGatherIndicators.getNodeid(), tempList);
    			}else{
        			//���˵������ӵ��豸
        			if(!nodehash.containsKey(nodeGatherIndicators.getNodeid()))continue;
    				List tempList = new ArrayList();
    				tempList.add(nodeGatherIndicators);
    				docollcetHash.put(nodeGatherIndicators.getNodeid(), tempList);
    			}
    		}
    		if(docollcetHash != null){
    			numThreads = docollcetHash.size();
    			if(numThreads > 200){
    				numThreads = 200;
    			}
    		}
    		// �����̳߳�,��Ŀ���ݱ����ӵ��豸����
    		ThreadPool threadPool = null;													
    		// ��������
    		if(docollcetHash != null && docollcetHash.size()>0){
    			threadPool = new ThreadPool(docollcetHash.size());	
    			Enumeration newProEnu = docollcetHash.keys();
    			while(newProEnu.hasMoreElements())
    			{
    				String nodeid = (String)newProEnu.nextElement();
    				List dolist = (List)docollcetHash.get(nodeid);
    				threadPool.runTask(createTask(nodeid,dolist,alldata));
    	
    			}
    			// �ر��̳߳ز��ȴ������������
        		threadPool.join();             		
        		threadPool.close();
        		//threadPool.destroy();
    		}
    		threadPool = null;
    		
    		Date _enddate = new Date();
    		SysLogger.info("##############################");
			SysLogger.info("### �����ʼ���ȫ�����豸�ɼ�ʱ�� "+(_enddate.getTime()-_startdate.getTime())+"####");
			SysLogger.info("##############################");
    		
    		HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
    		Date startdate = new Date();
    		hostdataManager.createHostItemData(alldata, "net");
    		
    		Date enddate = new Date();
    		SysLogger.info("##############################");
			SysLogger.info("### �����ʼ���ȫ�����豸���ʱ�� "+(enddate.getTime()-startdate.getTime())+"####");
			SysLogger.info("##############################");
			alldata.clear();
			//alldata = null;
		}catch(Exception e){
			SysLogger.info(e.getMessage());
			e.printStackTrace();
			//���������⣬����Ҫ���µ���������ʱ����ķ���
		}finally{
			Runtime.getRuntime().gc();
			SysLogger.info("********M5GatewayTask Thread Count : "+Thread.activeCount());
		}
	}
	
	/**
    ��������
	 */	
	private static Runnable createTask(final String nodeid,final List list,final Hashtable alldata) {
    return new Runnable() {
        public void run() {
        	try {                	
            	Hashtable returnHash = new Hashtable();
            	AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
            	NodeGatherIndicators alarmIndicatorsNode = null;
            	Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeid));
            	
            	if(list != null && list.size()>0){
            		for(int k=0;k<list.size();k++){
            			alarmIndicatorsNode = (NodeGatherIndicators)list.get(k);            			
            			if(alarmIndicatorsNode.getName().equalsIgnoreCase("cpu")){
                    		//CPU�Ĳɼ�
                    		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    			//CISCO��CPU
                    			if(node != null){
                    				CiscoIronportCpuSnmp ciscocpusnmp = null;
                        			try{
                        				Date startdate1 = new Date();
                        				ciscocpusnmp = (CiscoIronportCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.CiscoIronportCpuSnmp").newInstance();
                            			returnHash = ciscocpusnmp.collect_Data(alarmIndicatorsNode);
                            			Date enddate1 = new Date();
                                		SysLogger.info("##############################");
                        				SysLogger.info("### "+node.getIpAddress()+" Ironport�豸CPU�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                        				SysLogger.info("##############################");
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("cpu", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("cpu", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("cpu", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("memory")){
                    		//�ڴ�Ĳɼ�
                    		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                        		//CISCO��MEMORY
                    			if(node != null){
                    				CiscoIronportMemorySnmp ciscomemorysnmp = null;
                        			try{
                        				Date startdate1 = new Date();
                        				ciscomemorysnmp = (CiscoIronportMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.CiscoIronportMemorySnmp").newInstance();
                            			returnHash = ciscomemorysnmp.collect_Data(alarmIndicatorsNode);
                            			Date enddate1 = new Date();
                            			SysLogger.info("##############################");
                        				SysLogger.info("### "+node.getIpAddress()+" Ironport�豸memory�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                        				SysLogger.info("##############################");
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("memory", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("memory", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("memory", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("systemgroup")){
                    		if(node != null){
                    			SystemSnmp systemsnmp = null;
                    			try{
                    				Date startdate1 = new Date();
                    				systemsnmp = (SystemSnmp)Class.forName("com.afunms.polling.snmp.system.SystemSnmp").newInstance();
                        			returnHash = systemsnmp.collect_Data(alarmIndicatorsNode);
                        			Date enddate1 = new Date();
                            		SysLogger.info("##############################");
                    				SysLogger.info("### "+node.getIpAddress()+" �ʼ���ȫ�����豸SystemGroup�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                    				SysLogger.info("##############################");
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
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("ping")){
                    		if(node != null){
                    			PingSnmp pingsnmp = null;
                    			try{
                    				Date startdate1 = new Date();
                    				pingsnmp = (PingSnmp)Class.forName("com.afunms.polling.snmp.ping.PingSnmp").newInstance();
                        			returnHash = pingsnmp.collect_Data(alarmIndicatorsNode);
                        			Date enddate1 = new Date();
                             		SysLogger.info("##############################");
                     				SysLogger.info("### "+node.getIpAddress()+" �ʼ���ȫ�����豸Ping�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                     				SysLogger.info("##############################");
                        			if(alldata.containsKey(node.getIpAddress())){
                        				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                        				if(ipdata != null){
                        					ipdata.put("ping", returnHash);
                        				}else{
                        					ipdata = new Hashtable();
                        					ipdata.put("ping", returnHash);
                        				}
                        				alldata.put(node.getIpAddress(), ipdata);
                        				
                        			}else{
                        				Hashtable ipdata = new Hashtable();
                        				ipdata.put("ping", returnHash);
                        				alldata.put(node.getIpAddress(), ipdata);
                        				
                        			}
                        			
                        			//��PINGֵ���и澯���
                        			if(returnHash != null && returnHash.size()>0){
                        				Vector pingvector = (Vector)returnHash.get("ping");
                        				if(pingvector != null){
                        					for (int i = 0; i < pingvector.size(); i++) {
                                				Pingcollectdata pingdata = (Pingcollectdata) pingvector.elementAt(i);
                                				if(pingdata.getSubentity().equalsIgnoreCase("ConnectUtilization")){
                            						//��ͨ�ʽ����ж�               						
                            						List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), alarmIndicatorsNode.getType(), "");
                            						for(int m = 0 ; m < list.size() ; m ++){
                            							AlarmIndicatorsNode _alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(m);
                            							if("1".equals(_alarmIndicatorsNode.getEnabled())){
                            								if(_alarmIndicatorsNode.getName().equalsIgnoreCase("ping")){
                            									CheckEventUtil checkeventutil = new CheckEventUtil();
                            									//SysLogger.info(_alarmIndicatorsNode.getName()+"=====_alarmIndicatorsNode.getName()=========");
                            									checkeventutil.checkEvent(node, _alarmIndicatorsNode, pingdata.getThevalue());
                            								}
                            							}  
                            						}
                            						
                            					}
                                			}
                        				}
                        			}
                        		}catch(Exception e){
                        			e.printStackTrace();
                        		}
                    		}
                    	} else if(alarmIndicatorsNode.getName().equalsIgnoreCase("interface")){
                    		if(node != null){
                    			InterfaceSnmp interfacesnmp = null;
                    			try{
//                    				SysLogger.info("############################################################");
//                    				SysLogger.info("##########��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...##########");
//                    				SysLogger.info("############################################################");
                    				Date startdate1 = new Date();
                    				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
                					returnHash = interfacesnmp.collect_Data(alarmIndicatorsNode);
                        			Date enddate1 = new Date();
                            		SysLogger.info("##############################");
                    				SysLogger.info("### "+node.getIpAddress()+" Ironport�豸Interface�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                    				SysLogger.info("##############################");
//                    				�Գ���������ֵ���и澯���
                        		    try{
                        				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_F5, "f5");
                        				for(int i = 0 ; i < list.size() ; i ++){
                        					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
                        					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                        							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                        						//���ܳ��������ֵ���и澯���
//                            					CheckEventUtil checkutil = new CheckEventUtil();
//                            					checkutil.updateData(node,returnHash,"f5","f5",alarmIndicatorsnode);
                        					}
                        				}
                        		    }catch(Exception e){
                        		    	e.printStackTrace();
                        		    }
                        			//�������ݿ�
                        			if(alldata.containsKey(node.getIpAddress())){
                        				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                        				if(ipdata != null){
                        					ipdata.put("interface", returnHash);
                        				}else{
                        					ipdata = new Hashtable();
                        					ipdata.put("interface", returnHash);
                        				}
                        				alldata.put(node.getIpAddress(), ipdata);
                        				
                        			}else{
                        				Hashtable ipdata = new Hashtable();
                        				ipdata.put("interface", returnHash);
                        				alldata.put(node.getIpAddress(), ipdata);
                        				
                        			}
                        		}catch(Exception e){
                        			e.printStackTrace();
                        		}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("packs")){
                    		if(node != null){
                    			PackageSnmp packagesnmp = null;
                    			try{
                    				Date startdate1 = new Date();
                    				packagesnmp = (PackageSnmp)Class.forName("com.afunms.polling.snmp.interfaces.PackageSnmp").newInstance();
                        			returnHash = packagesnmp.collect_Data(alarmIndicatorsNode);
                        			Date enddate1 = new Date();
                             		SysLogger.info("##############################");
                     				SysLogger.info("### "+node.getIpAddress()+" Ironport�豸Pack�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                     				SysLogger.info("##############################");
                        			if(alldata.containsKey(node.getIpAddress())){
                        				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                        				if(ipdata != null){
                        					ipdata.put("packs", returnHash);
                        				}else{
                        					ipdata = new Hashtable();
                        					ipdata.put("packs", returnHash);
                        				}
                        				alldata.put(node.getIpAddress(), ipdata);
                        				
                        			}else{
                        				Hashtable ipdata = new Hashtable();
                        				ipdata.put("packs", returnHash);
                        				alldata.put(node.getIpAddress(), ipdata);
                        				
                        			}
                        		}catch(Exception e){
                        			e.printStackTrace();
                        		}
                    		}
                    	} else if(alarmIndicatorsNode.getName().equalsIgnoreCase("diskio")){
                    		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    			if(node != null){
                        			CiscoDiskIOSnmp diskiosnmp = null;
                        			try{
                        				Date startdate1 = new Date();
                        				diskiosnmp = (CiscoDiskIOSnmp)Class.forName("com.afunms.polling.snmp.disk.CiscoDiskIOSnmp").newInstance();
                            			returnHash = diskiosnmp.collect_Data(alarmIndicatorsNode);
                            			Date enddate1 = new Date();
                                 		SysLogger.info("##############################");
                         				SysLogger.info("### "+node.getIpAddress()+" �ʼ���ȫ�����豸diskio�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                         				SysLogger.info("##############################");
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("diskio", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("diskio", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("diskio", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                        		}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("queue")){
                    		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    			if(node != null){
                        			CiscoQueueSnmp queuesnmp = null;
                        			try{
                        				Date startdate1 = new Date();
                        				queuesnmp = (CiscoQueueSnmp)Class.forName("com.afunms.polling.snmp.queue.CiscoQueueSnmp").newInstance();
                            			returnHash = queuesnmp.collect_Data(alarmIndicatorsNode);
                            			Date enddate1 = new Date();
                                 		SysLogger.info("##############################");
                         				SysLogger.info("### "+node.getIpAddress()+" �ʼ���ȫ�����豸queue�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                         				SysLogger.info("##############################");
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("queue", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("queue", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("queue", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                        		}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("queuestatue")){
                    		if(node != null){
                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    				CiscoQueueStatueSnmp queuesnmp = null;
                        			try{
                        				//SysLogger.info("��ʼ�ɼ�queuestatue��Ϣ################");
                        				Date startdate1 = new Date();
                        				queuesnmp = (CiscoQueueStatueSnmp)Class.forName("com.afunms.polling.snmp.queue.CiscoQueueStatueSnmp").newInstance();
                            			returnHash = queuesnmp.collect_Data(alarmIndicatorsNode);
                            			Date enddate1 = new Date();
                                 		SysLogger.info("##############################");
                         				SysLogger.info("### "+node.getIpAddress()+" �ʼ���ȫ�����豸queuestatue�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                         				SysLogger.info("##############################");
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("queuestatue", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("queuestatue", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("queuestatue", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("temperature")){
                    		if(node != null){
                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    				//CISCO�¶�
                    				CiscoIronportTemperatureSnmp tempersnmp = null;
                        			try{
                        				//SysLogger.info("��ʼ�ɼ��ʼ���ȫ�����¶���Ϣ################");
                        				tempersnmp = (CiscoIronportTemperatureSnmp)Class.forName("com.afunms.polling.snmp.temperature.CiscoIronportTemperatureSnmp").newInstance();
                            			returnHash = tempersnmp.collect_Data(alarmIndicatorsNode);
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("temperature", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("temperature", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("temperature", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("fan")){
                    		if(node != null){
                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    				//CISCO����
                    				CiscoIronportFanSnmp fansnmp = null;
                        			try{
                        				//SysLogger.info("��ʼ�ɼ��ʼ���ȫ�����¶���Ϣ################");
                        				fansnmp = (CiscoIronportFanSnmp)Class.forName("com.afunms.polling.snmp.fan.CiscoIronportFanSnmp").newInstance();
                            			returnHash = fansnmp.collect_Data(alarmIndicatorsNode);
                            			//FDB���������ݿ�
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("fan", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("fan", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("fan", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("power")){
                    		if(node != null){
                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    				//CISCO��Դ
                    				CiscoIronportPowerSnmp powersnmp = null;
                        			try{
                        				//SysLogger.info("��ʼ�ɼ�CISCO�ʼ���ȫ���ص�Դ��Ϣ################");
                        				powersnmp = (CiscoIronportPowerSnmp)Class.forName("com.afunms.polling.snmp.power.CiscoIronportPowerSnmp").newInstance();
                            			returnHash = powersnmp.collect_Data(alarmIndicatorsNode);
                            			//�������ݿ�
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("power", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("power", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("power", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("memorystatue")){
                    		if(node != null){
                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    				CiscoIronportMemoryStatueSnmp memorystatuesnmp = null;
                        			try{
                        				//SysLogger.info("��ʼ�ɼ��ʼ���ȫ����memorystatue��Ϣ################");
                        				memorystatuesnmp = (CiscoIronportMemoryStatueSnmp)Class.forName("com.afunms.polling.snmp.memory.CiscoIronportMemoryStatueSnmp").newInstance();
                            			returnHash = memorystatuesnmp.collect_Data(alarmIndicatorsNode);
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("memorystatue", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("memorystatue", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("memorystatue", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("raid")){
                    		if(node != null){
                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    				CiscoRaidSnmp raidsnmp = null;
                        			try{
                        				//SysLogger.info("��ʼ�ɼ��ʼ���ȫ����raid��Ϣ################");
                        				raidsnmp = (CiscoRaidSnmp)Class.forName("com.afunms.polling.snmp.raid.CiscoRaidSnmp").newInstance();
                            			returnHash = raidsnmp.collect_Data(alarmIndicatorsNode);
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("raid", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("raid", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("raid", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("queuemessage")){
                    		if(node != null){
                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("cisco")){
                    				CiscoQueueMessageSnmp queuesnmp = null;
                        			try{
                        				//SysLogger.info("��ʼ�ɼ��ʼ���ȫ����queuemessage��Ϣ################");
                        				Date startdate1 = new Date();
                        				queuesnmp = (CiscoQueueMessageSnmp)Class.forName("com.afunms.polling.snmp.queue.CiscoQueueMessageSnmp").newInstance();
                            			returnHash = queuesnmp.collect_Data(alarmIndicatorsNode);
                            			Date enddate1 = new Date();
                                 		SysLogger.info("##############################");
                         				SysLogger.info("### "+node.getIpAddress()+" �ʼ���ȫ�����豸queuemessage�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
                         				SysLogger.info("##############################");
                            			if(alldata.containsKey(node.getIpAddress())){
                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
                            				if(ipdata != null){
                            					ipdata.put("queuemessage", returnHash);
                            				}else{
                            					ipdata = new Hashtable();
                            					ipdata.put("queuemessage", returnHash);
                            				}
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}else{
                            				Hashtable ipdata = new Hashtable();
                            				ipdata.put("queuemessage", returnHash);
                            				alldata.put(node.getIpAddress(), ipdata);
                            				
                            			}
                            		}catch(Exception e){
                            			e.printStackTrace();
                            		}
                    			}
                    		}
                    	}
            		}
            	}      		
            }catch(Exception exc){
            	
            }
        }
    };
	}
}
