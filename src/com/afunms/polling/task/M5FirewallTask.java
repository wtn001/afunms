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
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.impl.HostCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Task;
import com.afunms.polling.snmp.cpu.NokiaCpuSnmp;
import com.afunms.polling.snmp.disk.NokiaDiskMirrorSnmp;
import com.afunms.polling.snmp.fan.NokiaFanSnmp;
import com.afunms.polling.snmp.image.NokiaImageSnmp;
import com.afunms.polling.snmp.interfaces.InterfaceSnmp;
import com.afunms.polling.snmp.interfaces.PackageSnmp;
import com.afunms.polling.snmp.memory.NokiaMemorySnmp;
import com.afunms.polling.snmp.ping.PingSnmp;
import com.afunms.polling.snmp.power.NokiaPowerSnmp;
import com.afunms.polling.snmp.process.NokiaProcessSnmp;
import com.afunms.polling.snmp.system.SystemSnmp;
import com.afunms.topology.dao.HostNodeDao;
import com.afunms.topology.model.HostNode;
/**
 * @author yangjun     
 * @date 2011-07-9 
 * �Է���ǽ�豸���вɼ�    
 *
 */
public class M5FirewallTask extends MonitorTask {
	    
	public void run() {
		try{
			NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
	    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	try{
	    		//��ȡ�����õ����б����ӷ���ǽָ��
	    		monitorItemList = indicatorsdao.getByIntervalAndType("5", "m",1,"firewall");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
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
    			nodelist = nodedao.loadNetwork(8);
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
//    		 �����̳߳�,��Ŀ���ݱ����ӵ��豸����
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
			SysLogger.info("### ���з���ǽ�豸�ɼ�ʱ�� "+(_enddate.getTime()-_startdate.getTime())+"####");
			SysLogger.info("##############################");
    		
    		HostCollectDataManager hostdataManager=new HostCollectDataManager(); 
    		Date startdate = new Date();
    		hostdataManager.createHostItemData(alldata, "net");
    		
    		Date enddate = new Date();
    		SysLogger.info("##############################");
			SysLogger.info("### ���з���ǽ�豸���ʱ�� "+(enddate.getTime()-startdate.getTime())+"####");
			SysLogger.info("##############################");
			alldata.clear();
		}catch(Exception e){					 	
			e.printStackTrace();
		}finally{
			SysLogger.info("********M5FirewallTask Thread Count : "+Thread.activeCount());
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
	                    		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("nokia")){
	                    			//nokia��CPU
	                    			if(node != null){
	                    				NokiaCpuSnmp nokiacpusnmp = null;
	                        			try{
	                        				nokiacpusnmp = (NokiaCpuSnmp)Class.forName("com.afunms.polling.snmp.cpu.NokiaCpuSnmp").newInstance();
	                            			returnHash = nokiacpusnmp.collect_Data(alarmIndicatorsNode);
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
	                    		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("nokia")){
	                        		//nokia��MEMORY
	                    			if(node != null){
	                    				NokiaMemorySnmp nokiamemorysnmp = null;
	                        			try{
	                        				nokiamemorysnmp = (NokiaMemorySnmp)Class.forName("com.afunms.polling.snmp.memory.NokiaMemorySnmp").newInstance();
	                            			returnHash = nokiamemorysnmp.collect_Data(alarmIndicatorsNode);
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
	                    				SysLogger.info("### "+node.getIpAddress()+" ����ǽ�豸SystemGroup�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
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
	                     				SysLogger.info("### "+node.getIpAddress()+" ����ǽ�豸Ping�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
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
//	                    				SysLogger.info("############################################################");
//	                    				SysLogger.info("##########��ʼ�ɼ�"+node.getIpAddress()+" �ӿ���Ϣ...##########");
//	                    				SysLogger.info("############################################################");
	                    				Date startdate1 = new Date();
	                    				interfacesnmp = (InterfaceSnmp)Class.forName("com.afunms.polling.snmp.interfaces.InterfaceSnmp").newInstance();
                    					returnHash = interfacesnmp.collect_Data(alarmIndicatorsNode);
	                        			Date enddate1 = new Date();
	                            		SysLogger.info("##############################");
	                    				SysLogger.info("### "+node.getIpAddress()+" ����ǽ�豸Interface�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
	                    				SysLogger.info("##############################");
//	                    				�Գ���������ֵ���и澯���
                            		    try{
                            				List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(node.getId()), AlarmConstant.TYPE_F5, "f5");
                            				for(int i = 0 ; i < list.size() ; i ++){
                            					AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
                            					if(alarmIndicatorsnode.getName().equalsIgnoreCase("AllInBandwidthUtilHdx") || alarmIndicatorsnode.getName().equalsIgnoreCase("AllOutBandwidthUtilHdx")
                            							|| alarmIndicatorsnode.getName().equalsIgnoreCase("utilhdx")){
                            						//���ܳ��������ֵ���и澯���
//                                					CheckEventUtil checkutil = new CheckEventUtil();
//                                					checkutil.updateData(node,returnHash,"f5","f5",alarmIndicatorsnode);
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
	                     				SysLogger.info("### "+node.getIpAddress()+" ����ǽ�豸Pack�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
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
	                    	} else if(alarmIndicatorsNode.getName().equalsIgnoreCase("process")){
	                    		if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("nokia")){
	                    			if(node != null){
	                    				NokiaProcessSnmp processsnmp = null;
	                        			try{
	                        				Date startdate1 = new Date();
	                        				processsnmp = (NokiaProcessSnmp)Class.forName("com.afunms.polling.snmp.process.NokiaProcessSnmp").newInstance();
	                            			returnHash = processsnmp.collect_Data(alarmIndicatorsNode);
	                            			Date enddate1 = new Date();
	                                 		SysLogger.info("##############################");
	                         				SysLogger.info("### "+node.getIpAddress()+" ����ǽ�豸process�ɼ�ʱ�� "+(enddate1.getTime()-startdate1.getTime())+"####");
	                         				SysLogger.info("##############################");
	                            			if(alldata.containsKey(node.getIpAddress())){
	                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
	                            				if(ipdata != null){
	                            					ipdata.put("process", returnHash);
	                            				}else{
	                            					ipdata = new Hashtable();
	                            					ipdata.put("process", returnHash);
	                            				}
	                            				alldata.put(node.getIpAddress(), ipdata);
	                            				
	                            			}else{
	                            				Hashtable ipdata = new Hashtable();
	                            				ipdata.put("process", returnHash);
	                            				alldata.put(node.getIpAddress(), ipdata);
	                            				
	                            			}
	                            		}catch(Exception e){
	                            			e.printStackTrace();
	                            		}
	                        		}
	                    		}
	                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("fan")){
	                    		if(node != null){
	                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("nokia")){
	                    				//nokia����
	                    				NokiaFanSnmp fansnmp = null;
	                        			try{
	                        				//SysLogger.info("��ʼ�ɼ�nokia������Ϣ################");
	                        				fansnmp = (NokiaFanSnmp)Class.forName("com.afunms.polling.snmp.fan.NokiaFanSnmp").newInstance();
	                            			returnHash = fansnmp.collect_Data(alarmIndicatorsNode);
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
	                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("nokia")){
	                    				//nokia��Դ
	                    				NokiaPowerSnmp powersnmp = null;
	                        			try{
	                        				//SysLogger.info("��ʼ�ɼ�nokia��Դ��Ϣ################");
	                        				powersnmp = (NokiaPowerSnmp)Class.forName("com.afunms.polling.snmp.power.NokiaPowerSnmp").newInstance();
	                            			returnHash = powersnmp.collect_Data(alarmIndicatorsNode);
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
	                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("mirror")){
	                    		if(node != null){
	                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("nokia")){
	                    				//nokia mirror
	                    				NokiaDiskMirrorSnmp mirrorsnmp = null;
	                        			try{
	                        				//SysLogger.info("��ʼ�ɼ�nokia mirror��Ϣ################");
	                        				mirrorsnmp = (NokiaDiskMirrorSnmp)Class.forName("com.afunms.polling.snmp.disk.NokiaDiskMirrorSnmp").newInstance();
	                            			returnHash = mirrorsnmp.collect_Data(alarmIndicatorsNode);
	                            			if(alldata.containsKey(node.getIpAddress())){
	                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
	                            				if(ipdata != null){
	                            					ipdata.put("mirror", returnHash);
	                            				}else{
	                            					ipdata = new Hashtable();
	                            					ipdata.put("mirror", returnHash);
	                            				}
	                            				alldata.put(node.getIpAddress(), ipdata);
	                            				
	                            			}else{
	                            				Hashtable ipdata = new Hashtable();
	                            				ipdata.put("mirror", returnHash);
	                            				alldata.put(node.getIpAddress(), ipdata);
	                            				
	                            			}
	                            		}catch(Exception e){
	                            			e.printStackTrace();
	                            		}
	                    			}
	                    		}
	                    	}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("image")){
	                    		if(node != null){
	                    			if(alarmIndicatorsNode.getSubtype().equalsIgnoreCase("nokia")){
	                    				//nokia image
	                    				NokiaImageSnmp imagesnmp = null;
	                        			try{
	                        				//SysLogger.info("��ʼ�ɼ�nokia image��Ϣ################");
	                        				imagesnmp = (NokiaImageSnmp)Class.forName("com.afunms.polling.snmp.image.NokiaImageSnmp").newInstance();
	                            			returnHash = imagesnmp.collect_Data(alarmIndicatorsNode);
	                            			if(alldata.containsKey(node.getIpAddress())){
	                            				Hashtable ipdata = (Hashtable)alldata.get(node.getIpAddress());
	                            				if(ipdata != null){
	                            					ipdata.put("image", returnHash);
	                            				}else{
	                            					ipdata = new Hashtable();
	                            					ipdata.put("image", returnHash);
	                            				}
	                            				alldata.put(node.getIpAddress(), ipdata);
	                            				
	                            			}else{
	                            				Hashtable ipdata = new Hashtable();
	                            				ipdata.put("image", returnHash);
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
