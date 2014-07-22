package com.afunms.polling.snmp.memory;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.DecimalFormat;
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
public class WindowsPhysicalMemorySnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 
	 */
	public WindowsPhysicalMemorySnmp() {
	}

	public void collectData(Node node, MonitoredItem item) {

	}

	public void collectData(HostNode node) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	@SuppressWarnings("unchecked")
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returnHash = new Hashtable();
		Vector memoryVector = new Vector();
		Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(nodeGatherIndicators.getNodeid()));
		if(host == null)return returnHash;
		// �ж��Ƿ��ڲɼ�ʱ�����
		if (ShareData.getTimegatherhash() != null) {
			if (ShareData.getTimegatherhash().containsKey(host.getId() + ":equipment")) {
				TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
				int _result = 0;
				_result = timeconfig.isBetween((List) ShareData.getTimegatherhash().get(host.getId() + ":equipment"));
				if (_result == 1) {
					// SysLogger.info("########ʱ�����: ��ʼ�ɼ�
					// "+node.getIpAddress()+" PING������Ϣ##########");
				} else if (_result == 2) {
					// SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+"
					// PING������Ϣ##########");
				} else {
					SysLogger.info("######## " + host.getIpAddress() + " ���ڲɼ�CPUʱ�����,�˳�##########");
					// ���֮ǰ�ڴ��в����ĸ澯��Ϣ
					try {
						// ���֮ǰ�ڴ��в�����CPU�澯��Ϣ
    			    	NodeDTO nodedto = null;
    					NodeUtil nodeUtil = new NodeUtil();
    					nodedto = nodeUtil.creatNodeDTOByHost(host);
						CheckEventUtil checkutil = new CheckEventUtil();
						checkutil.deleteEvent(host.getId()+"", nodedto.getType(), nodedto.getSubtype(), "physicalmemory", null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return returnHash;
				}

			}
		}

		try {
			Memorycollectdata memorydata = new Memorycollectdata();
			Calendar date = Calendar.getInstance();
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData == null)ipAllData = new Hashtable();

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByIP(host.getIpAddress());
				Date cc = date.getTime();
				String time = sdf.format(cc);
				snmpnode.setLastTime(time);
			} catch (Exception e) {

			}
			// -------------------------------------------------------------------------------------------memory
			// start
			try {

				// String[] oids =
				// new String[] {
				// "1.3.6.1.2.1.25.5.1.1.2" };
				// String[] oids1 =
				// new String[] {
				// "1.3.6.1.2.1.25.2.2" };
				//				
				// String[][] valueArray = null;
				// try {
				// valueArray = SnmpUtils.getTableData(host.getIpAddress(),
				// host.getCommunity(), oids, host.getSnmpversion(), 3,
				// 1000*30);
				// } catch(Exception e){
				// valueArray = null;
				// //SysLogger.error(host.getIpAddress() +
				// "_WindowsMemorySnmp");
				// }
				//				
				// String[][] valueArray1 = null;
				// try {
				// valueArray1 = SnmpUtils.getTableData(host.getIpAddress(),
				// host.getCommunity(), oids1, host.getSnmpversion(), 3, 1000);
				// } catch(Exception e){
				// valueArray1 = null;
				// //SysLogger.error(host.getIpAddress() +
				// "_WindowsMemorySnmp");
				// }
				//
				// int allMemorySize=0;
				// if(valueArray1 != null){
				// for(int i=0;i<valueArray1.length;i++){
				// if(valueArray1[i][0]==null)continue;
				// allMemorySize = Integer.parseInt(valueArray1[i][0]);
				// }
				// }
				// //System.out.println("list "+list.size());
				// float value=0.0f;
				// int allUsedSize=0;
				// if(valueArray != null){
				// for(int i=0;i<valueArray.length;i++){
				// if(valueArray[i][0] == null)continue;
				// int processUsedSize = Integer.parseInt(valueArray[i][0]);
				// allUsedSize=allUsedSize+processUsedSize;
				// }
				// }
				// if(allMemorySize!=0){
				// value=allUsedSize*100f/allMemorySize;
				// }
				// else{
				// throw new Exception("allMemorySize is 0");
				// }

				String[] oids = new String[] { 
						"1.3.6.1.2.1.25.2.3.1.1", 
						"1.3.6.1.2.1.25.2.3.1.2", 
						"1.3.6.1.2.1.25.2.3.1.3", 
						"1.3.6.1.2.1.25.2.3.1.4", 
						"1.3.6.1.2.1.25.2.3.1.5",
						"1.3.6.1.2.1.25.2.3.1.6", 
						"1.3.6.1.2.1.25.2.3.1.7" };

				String[][] valueArray = null;
				try {
					//valueArray = SnmpUtils.getTableData(host.getIpAddress(), host.getCommunity(), oids, host.getSnmpversion(), 3, 1000 * 30);
					valueArray = SnmpUtils.getTableData(host.getIpAddress(),host.getCommunity(),oids,host.getSnmpversion(),
		   		  			host.getSecuritylevel(),host.getSecurityName(),host.getV3_ap(),host.getAuthpassphrase(),host.getV3_privacy(),host.getPrivacyPassphrase(),3,1000*30);
				} catch (Exception e) {
					valueArray = null;
					// SysLogger.error(host.getIpAddress() + "_WindowsSnmp");
				}
				float value = 0.0f;
				int allsize=0;
				int used=0;
				String byteunit="";
				if (valueArray != null && valueArray.length > 0) {
					for (int i = 0; i < valueArray.length; i++) {
						//SysLogger.info(valueArray[i][2]+"==="+valueArray[i][3]+"==="+valueArray[i][4]+"==="+valueArray[i][5]);
						String descriptions = valueArray[i][2];
						 byteunit=valueArray[i][3];
						if (descriptions == null)
							descriptions = "";
						String desc = "";
						if (descriptions.indexOf("Physical Memory") >= 0) {
							desc = descriptions;
						} else {
							continue;
						}
						if (descriptions.indexOf("\\") >= 0) {
							desc = desc.replace("\\", "/");
						}
						String totalSize = valueArray[i][4];
						String usedSize = valueArray[i][5];
						 allsize = Integer.parseInt(totalSize.trim());
						 used = Integer.parseInt(usedSize.trim());
						if (allsize != 0) {
							value = used * 100.0f / allsize;
						} else {
							value = 0.0f;
						}
						
						memorydata = new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("Utilization");
						memorydata.setSubentity("PhysicalMemory");
						memorydata.setRestype("dynamic");
						memorydata.setUnit("%");
						DecimalFormat df = new DecimalFormat("#.##");// yangjun
						// utilhdx.setThevalue(df.format(l*8/1000));
						memorydata.setThevalue(df.format(value));
						memoryVector.addElement(memorydata);

						memorydata = new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("Capability");
						memorydata.setRestype("static");
						memorydata.setSubentity("PhysicalMemory");

						float size=0.0f;
						size=allsize*Long.parseLong(byteunit)*1.0f/1024/1024;
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
						memorydata.setSubentity("PhysicalMemory");
						size = allsize * 1.0f / 1024;
						if (size >= 1024.0f) {
							size = size / 1024;
							memorydata.setUnit("G");
						} else {
							memorydata.setUnit("M");
						}
						memorydata.setThevalue(Float.toString(size));
						memoryVector.addElement(memorydata);
						break;
					}
				} else {
					//���û�вɼ�������
					memorydata = new Memorycollectdata();
					memorydata.setIpaddress(host.getIpAddress());
					memorydata.setCollecttime(date);
					memorydata.setCategory("Memory");
					memorydata.setEntity("Utilization");
					memorydata.setSubentity("PhysicalMemory");
					memorydata.setRestype("dynamic");
					memorydata.setUnit("%");
					DecimalFormat df = new DecimalFormat("#.##");// yangjun
					// utilhdx.setThevalue(df.format(l*8/1000));
					memorydata.setThevalue(df.format(0));
					memoryVector.addElement(memorydata);
					int memoryVectorSize = memoryVector.size();
					if ((ShareData.getSharedata().containsKey(host.getIpAddress()))) {
						if (((Hashtable) (ShareData.getSharedata().get(host.getIpAddress()))).containsKey("memory")) {
							Vector formerMemoryVector = (Vector) ((Hashtable) (ShareData.getSharedata().get(host.getIpAddress()))).get("memory");
							if (formerMemoryVector != null && formerMemoryVector.size() > 0) {
								for (int i = 0; i < formerMemoryVector.size(); i++) {
									memorydata = new Memorycollectdata();
									memorydata = (Memorycollectdata) formerMemoryVector.get(i);
									if (memorydata.getEntity().equalsIgnoreCase("Capability") && memorydata.getSubentity().equalsIgnoreCase("PhysicalMemory")) {
										memoryVector.addElement(memorydata);
										break;
									}
								}
							}
						}
					} 
					if(memoryVector.size() == memoryVectorSize) {	
						memorydata = new Memorycollectdata();
						memorydata.setIpaddress(host.getIpAddress());
						memorydata.setCollecttime(date);
						memorydata.setCategory("Memory");
						memorydata.setEntity("Capability");
						memorydata.setRestype("static");
						memorydata.setSubentity("PhysicalMemory");

						float size=0.0f;
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
						System.out.println("11111111111---------null");
						memoryVector.addElement(memorydata);
					}
					
					
					memorydata = new Memorycollectdata();
					memorydata.setIpaddress(host.getIpAddress());
					memorydata.setCollecttime(date);
					memorydata.setCategory("Memory");
					memorydata.setEntity("UsedSize");
					memorydata.setRestype("static");
					memorydata.setSubentity("PhysicalMemory");
					float size = -1.0f;
					if (size >= 1024.0f) {
						size = size / 1024;
						memorydata.setUnit("G");
					} else {
						memorydata.setUnit("M");
					}
					memorydata.setThevalue(Float.toString(size));
					memoryVector.addElement(memorydata);
				}
				
			} catch (Exception e) {
				 e.printStackTrace();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}


		Vector toAddVector = new Vector();
		Hashtable formerHash = new Hashtable();
		
		/*
		 * ����ڴ�������д��豸�����ݣ���ŵ�formerHash��
		 */
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

		// if(ipAllData.containsKey("memory")){
		// Vector formerMemoryVector = (Vector)ipAllData.get("memory");
		// if(formerMemoryVector != null && formerMemoryVector.size()>0){
		// for(int i=0;i<formerMemoryVector.size();i++){
		// Memorycollectdata
		// memorydata=(Memorycollectdata)formerMemoryVector.get(i);
		// formerHash.put(memorydata.getSubentity()+":"+memorydata.getEntity(),
		// memorydata);
		// }
		// }
		//			
		// }
		
		/*
		 * ����вɼ������ݣ����òɼ����������滻�ڴ���е�����
		 */
		if (memoryVector != null && memoryVector.size() > 0) {
			for (int j = 0; j < memoryVector.size(); j++) {
				Memorycollectdata memorydata = (Memorycollectdata) memoryVector.get(j);
				if (formerHash.containsKey(memorydata.getSubentity() + ":" + memorydata.getEntity())) {
					// ������,��Ҫ���µ��滻ԭ��������
					// SysLogger.info("����
					// "+memorydata.getSubentity()+":"+memorydata.getEntity());
					formerHash.remove(memorydata.getSubentity() + ":" + memorydata.getEntity());
					formerHash.put(memorydata.getSubentity() + ":" + memorydata.getEntity(), memorydata);
				} else {
					// ��������,��ֱ�Ӽ���
					// SysLogger.info("���----windows
					// "+memorydata.getSubentity()+":"+memorydata.getEntity());
					toAddVector.add(memorydata);
				}
			}
		}
		
		/*
		 * ���ڴ���е�����ȡ������װ�ص�toAddVector
		 */
		if (formerHash.elements() != null && formerHash.size() > 0) {
			for (Enumeration enumeration = formerHash.keys(); enumeration.hasMoreElements();) {
				String keys = (String) enumeration.nextElement();
				Memorycollectdata memorydata = (Memorycollectdata) formerHash.get(keys);
				// SysLogger.info("WINDOWS ���
				// "+memorydata.getSubentity()+":"+memorydata.getEntity()+"----------"+memorydata.getThevalue());
				toAddVector.add(memorydata);
			}
		}

		if (!(ShareData.getSharedata().containsKey(host.getIpAddress()))) {
			Hashtable ipAllData = new Hashtable();
			if (toAddVector != null && toAddVector.size() > 0)
				ipAllData.put("memory", toAddVector);
			ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		} else {
			if (toAddVector != null && toAddVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(host.getIpAddress())).put("memory", toAddVector);

		}

		// ipAllData.put("memory",toAddVector);
		// ShareData.getSharedata().put(host.getIpAddress(), ipAllData);

		returnHash.put("memory", toAddVector);

		Hashtable collectHash = new Hashtable();
		collectHash.put("physicalmem", toAddVector);
		
//		//�������ڴ�ֵ���и澯���
//	    try{
//			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(host.getId()), AlarmConstant.TYPE_HOST, "windows","physicalmemory");
//			for(int i = 0 ; i < list.size() ; i ++){
//				AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(i);
//				//�������ڴ�ֵ���и澯���
//				CheckEventUtil checkutil = new CheckEventUtil();
//				checkutil.updateData(host,collectHash,"host","windows",alarmIndicatorsnode);
//				//}
//			}
//	    }catch(Exception e){
//	    	e.printStackTrace();
//	    }
	    try {
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
		} catch (Exception e) {
			e.printStackTrace();
			SysLogger.error("Windows �ڴ� �澯����" , e);
		}
		memoryVector = null;
		toAddVector = null;

		// �Ѳɼ��������sql
		HostPhysicalMemoryResulttosql tosql = new HostPhysicalMemoryResulttosql();
		tosql.CreateResultTosql(returnHash, host.getIpAddress());
		 
		String runmodel = PollingEngine.getCollectwebflag();// �ɼ������ģʽ
		if (!"0".equals(runmodel)) {
			// �ɼ�������Ƿ���ģʽ,����Ҫ����������д����ʱ���
			NetHostMemoryRtsql totempsql = new NetHostMemoryRtsql();
			totempsql.CreateResultTosql(returnHash, host, "PhysicalMemory");
		}

		return returnHash;
	}
}
