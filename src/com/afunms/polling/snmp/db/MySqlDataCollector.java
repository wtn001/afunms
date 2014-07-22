package com.afunms.polling.snmp.db;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import montnets.SmsDao;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmConstant;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.alarm.util.AlarmResourceCenter;
import com.afunms.application.dao.DBDao;
import com.afunms.application.dao.DBTypeDao;
import com.afunms.application.model.DBTypeVo;
import com.afunms.application.model.DBVo;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SystemConstant;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.indicators.dao.NodeGatherIndicatorsDao;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.DBNode;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.snmp.LoadMySqlFile;
import com.afunms.system.util.TimeGratherConfigUtil;

public class MySqlDataCollector{

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicator) {
		DBDao dbdao = new DBDao();
		Hashtable returndata = new Hashtable();
		List dbmonitorlists = new ArrayList();
		NodeGatherIndicatorsDao indicatorsdao = new NodeGatherIndicatorsDao();
    	List<NodeGatherIndicators> monitorItemList = new ArrayList<NodeGatherIndicators>(); 
		dbmonitorlists = ShareData.getDBList();
		try{
			DBVo dbmonitorlist = new DBVo();
			if (dbmonitorlists != null && dbmonitorlists.size() > 0) {
				for (int i = 0; i < dbmonitorlists.size(); i++) {
					DBVo vo = (DBVo) dbmonitorlists.get(i);
					if (vo.getId() == Integer.parseInt(nodeGatherIndicator
							.getNodeid())) {
						dbmonitorlist = vo;
						break;
					}
				}
			}
			//δ����
			if (dbmonitorlist.getManaged() == 0) {
				// ���δ���������ɼ�����ϢΪ��
				return returndata;
			}
			try{
	    		//��ȡ�����õ�DB2���б�����ָ��
	    		monitorItemList = indicatorsdao.getByInterval("5", "m",1,"db","mysql");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}finally{
	    		indicatorsdao.close();
	    	}
			
	    	if(monitorItemList == null)monitorItemList = new ArrayList<NodeGatherIndicators>();
	    	Hashtable gatherHash = new Hashtable();
	    	for(int i=0;i<monitorItemList.size();i++){
	    		NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)monitorItemList.get(i);
	    		if(nodeGatherIndicators.getNodeid().equals(nodeGatherIndicator.getNodeid())){
	    			gatherHash.put(nodeGatherIndicators.getName(), nodeGatherIndicators);
	    		}
	    	}
			
			//ȡ��mysql�ɼ�
			Hashtable monitorValue = new Hashtable();
			DBNode dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
			//�ж��豸�Ƿ��ڲɼ�ʱ����� 0:���ڲɼ�ʱ�����,���˳�;1:��ʱ�����,���вɼ�;2:�����ڲɼ�ʱ�������,��ȫ��ɼ�
			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
			int result = 0;
			result = timeconfig.isBetween(dbnode.getId()+"", "db");
			if(result == 0){
				SysLogger.info("###### "+dbnode.getIpAddress()+" ���ڲɼ�ʱ�����,����######");
				return null;
			}
			String serverip = dbmonitorlist.getIpAddress();
			String username = dbmonitorlist.getUser();
			String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
			String dbnames = dbmonitorlist.getDbName();
			//�жϸ����ݿ��Ƿ���������
			String[] dbs = dbnames.split(",");
			//�жϸ����ݿ��Ƿ���������
			boolean mysqlIsOK = false;
			
//					if (dbnode.getCollecttype() == SystemConstant.DBCOLLECTTYPE_SHELL) {
//						// �ű��ɼ���ʽ
//						//System.out.println("-------mysql���ýű���ʽ�ɼ�-----");
//						String filename = ResourceCenter.getInstance().getSysPath() + "/linuxserver/" + serverip + ".mysql.log";
//						File file = new File(filename);
//						if (!file.exists()) {
//							// �ļ�������,������澯
//							try {
//								createFileNotExistSMS(serverip);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							return;
//						}
//						SysLogger.info("#################��ʼ����Mysql:" + serverip + "�����ļ�###########");
//						LoadMySqlFile loadmysql = new LoadMySqlFile(filename);
//						Hashtable mysqlData = new Hashtable();
//						try {
//							// sqlserverdata = loadsqlserver.getSQLInital();
//							mysqlData = loadmysql.getMySqlCongfig();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						if (mysqlData != null && mysqlData.size() > 0) {
//							//System.out.println(mysqlData.containsKey("status"));
//							if (mysqlData.containsKey("status")) {
//								int status = Integer.parseInt((String) mysqlData.get("status"));
//								if (status == 1)
//									mysqlIsOK = true;
//								if (!mysqlIsOK) {
//									dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
//									dbnode.setAlarm(true);
//									dbnode.setStatus(3);
//									createSMS("mysql", dbmonitorlist);
//									allFlag = 1;
//								} else {
//									// �������ϣ���������ݲɼ�
//									for (int k = 0; k < dbs.length; k++) {
//										String dbStr = dbs[k];
//										Hashtable returnValue = new Hashtable();
//										returnValue = (Hashtable) mysqlData.get(dbStr);
//										if(returnValue!=null)
//										   monitorValue.put(dbStr, returnValue);
//									}
//									if (allFlag == 1) {
//										// ��һ�����ݿ��ǲ�ͨ��
//										// ��Ҫ�������ݿ����ڵķ������Ƿ�����ͨ
//										Host host = (Host) PollingEngine.getInstance().getNodeByIP(serverip);
//										Vector ipPingData = (Vector) ShareData.getPingdata().get(serverip);
//										if (ipPingData != null) {
//											Pingcollectdata pingdata = (Pingcollectdata) ipPingData.get(0);
//											Calendar tempCal = (Calendar) pingdata.getCollecttime();
//											Date cc = tempCal.getTime();
//											String time = sdf.format(cc);
//											String lastTime = time;
//											String pingvalue = pingdata.getThevalue();
//											if (pingvalue == null || pingvalue.trim().length() == 0)
//												pingvalue = "0";
//											double pvalue = new Double(pingvalue);
//											if (pvalue == 0) {
//												// �������������Ӳ���***********************************************
//												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
//												dbnode.setAlarm(true);
//												dbnode.setStatus(3);
//												List alarmList = dbnode.getAlarmMessage();
//												if (alarmList == null)
//													alarmList = new ArrayList();
//												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
//												String sysLocation = "";
//												try {
//													SmscontentDao eventdao = new SmscontentDao();
//													String eventdesc = "MYSQL(" + dbmonitorlist.getDbName() + " IP:"
//															+ dbmonitorlist.getIpAddress() + ")" + "�����ݿ����ֹͣ";
//													eventdao.createEventWithReasion("poll", dbmonitorlist.getId() + "",
//															dbmonitorlist.getAlias() + "(" + dbmonitorlist.getIpAddress() + ")",
//															eventdesc, 3, "db", "ping", "���ڵķ��������Ӳ���");
//												} catch (Exception e) {
//													e.printStackTrace();
//												}
//											} else {
//												Pingcollectdata hostdata = null;
//												hostdata = new Pingcollectdata();
//												hostdata.setId(Long.parseLong(id+""));
//												hostdata.setIpaddress(serverip);
//												Calendar date = Calendar.getInstance();
//												hostdata.setCollecttime(date);
//												hostdata.setCategory("MYPing");
//												hostdata.setEntity("Utilization");
//												hostdata.setSubentity("ConnectUtilization");
//												hostdata.setRestype("dynamic");
//												hostdata.setUnit("%");
//												hostdata.setThevalue("0");
//												try {
//													dbdao.createHostData(hostdata);
//													// ���Ͷ���
//													dbnode = (DBNode) PollingEngine.getInstance()
//															.getDbByID(dbmonitorlist.getId());
//													dbnode.setAlarm(true);
//													List alarmList = dbnode.getAlarmMessage();
//													if (alarmList == null)
//														alarmList = new ArrayList();
//													dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
//													dbnode.setStatus(3);
//													createSMS("mysql", dbmonitorlist);
//												} catch (Exception e) {
//													e.printStackTrace();
//												}
//											}
//
//										} else {
//											Pingcollectdata hostdata = null;
//											hostdata = new Pingcollectdata();
//											hostdata.setId(Long.parseLong(id+""));
//											hostdata.setIpaddress(serverip);
//											Calendar date = Calendar.getInstance();
//											hostdata.setCollecttime(date);
//											hostdata.setCategory("MYPing");
//											hostdata.setEntity("Utilization");
//											hostdata.setSubentity("ConnectUtilization");
//											hostdata.setRestype("dynamic");
//											hostdata.setUnit("%");
//											hostdata.setThevalue("0");
//											try {
//												dbdao.createHostData(hostdata);
//												// ���Ͷ���
//												dbnode = (DBNode) PollingEngine.getInstance().getDbByID(dbmonitorlist.getId());
//												dbnode.setAlarm(true);
//												List alarmList = dbnode.getAlarmMessage();
//												if (alarmList == null)
//													alarmList = new ArrayList();
//												dbnode.getAlarmMessage().add("���ݿ����ֹͣ");
//												dbnode.setStatus(3);
//												createSMS("mysql", dbmonitorlist);
//											} catch (Exception e) {
//												e.printStackTrace();
//											}
//										}
//									} else {
//										Pingcollectdata hostdata = null;
//										hostdata = new Pingcollectdata();
//										hostdata.setId(Long.parseLong(id+""));
//										hostdata.setIpaddress(serverip);
//										Calendar date = Calendar.getInstance();
//										hostdata.setCollecttime(date);
//										hostdata.setCategory("MYPing");
//										hostdata.setEntity("Utilization");
//										hostdata.setSubentity("ConnectUtilization");
//										hostdata.setRestype("dynamic");
//										hostdata.setUnit("%");
//										hostdata.setThevalue("100");
//										try {
//											dbdao.createHostData(hostdata);
//										} catch (Exception e) {
//											e.printStackTrace();
//										}
//
//									}
//									if (allFlag == 0) {
//										// �����ݿ��������ϣ���������ݿ����ݵĲɼ�
//										/*
//										 * SybaseVO sysbaseVO = new SybaseVO();
//										 * try{ sysbaseVO =
//										 * dbdao.getSysbaseInfo(serverip,
//										 * port,username, passwords);
//										 * }catch(Exception e){
//										 * e.printStackTrace(); } if (sysbaseVO ==
//										 * null)sysbaseVO = new SybaseVO();
//										 * Hashtable retValue = new Hashtable();
//										 * retValue.put("sysbaseVO", sysbaseVO);
//										 * ShareData.setSysbasedata(serverip,
//										 * retValue); List allspace =
//										 * sysbaseVO.getDbInfo(); if(allspace !=
//										 * null && allspace.size()>0){ for(int
//										 * k=0;k<allspace.size();k++){ TablesVO
//										 * tvo = (TablesVO)allspace.get(k);
//										 * if(sybspaceconfig
//										 * .containsKey(serverip
//										 * +":"+tvo.getDb_name())){ //�澯�ж�
//										 * Sybspaceconfig sybconfig =
//										 * (Sybspaceconfig
//										 * )sybspaceconfig.get(serverip
//										 * +":"+tvo.getDb_name()); Integer
//										 * usedperc =
//										 * Integer.parseInt(tvo.getDb_usedperc
//										 * ());
//										 * if(usedperc>sybconfig.getAlarmvalue
//										 * ()){ //������ֵ�澯 dbnode =
//										 * (DBNode)PollingEngine
//										 * .getInstance().getDbByID
//										 * (dbmonitorlist.getId());
//										 * dbnode.setAlarm(true); List alarmList =
//										 * dbnode.getAlarmMessage();
//										 * if(alarmList == null)alarmList = new
//										 * ArrayList();
//										 * dbnode.getAlarmMessage().
//										 * add(sybconfig
//										 * .getSpacename()+"��ռ䳬����ֵ"
//										 * +sybconfig.getAlarmvalue());
//										 * //dbnode.setStatus(3);
//										 * createSybSpaceSMS
//										 * (dbmonitorlist,sybconfig); } } } }
//										 */
//									}
//									if (allFlag == 0) {
//										monitorValue.put("runningflag", "��������");
//									} else {
//										monitorValue.put("runningflag", "<font color=red>����ֹͣ</font>");
//									}
//
//									if (monitorValue != null && monitorValue.size() > 0) {
//										ShareData.setMySqlmonitordata(serverip, monitorValue);
//									}
//								}
//							}
//						}
//						// //////////////////////////////////////////
//					} else {
						//JDBC�ɼ���ʽ
						for (int k = 0; k < dbs.length; k++) {
							SysLogger.info("#### ��ʼ�ɼ�MYSQL�����ݿ�: " + dbs[k] + " ####" + serverip);
							String dbStr = dbs[k];
							try {
								mysqlIsOK = dbdao.getMySqlIsOk(serverip, username, passwords, dbStr);
							} catch (Exception e) {
								e.printStackTrace();
								mysqlIsOK = false;
							}
							if (mysqlIsOK) {
								//�������ϣ���������ݲɼ�
								Hashtable returnValue = new Hashtable();
								try {
									SysLogger.info("#### ��ʼ�ɼ�MYSQL���ݿ�start... ####");
//									Date startDate = new Date();
									returnValue = dbdao.getMYSQLData(serverip, username, passwords, dbStr,gatherHash);
//									SysLogger.info("#### MYSQL���ݿ�: " + dbs[k] + " ####" + serverip +"�ɼ�ʱ��Ϊ��" + (new Date().getTime()-startDate.getTime()));
//									returnValue = dbdao.getMySqlDBConfig(serverip, username, passwords, dbStr);
//									Vector vector = dbdao.getStatus(serverip, username, passwords, dbStr);
//									Vector vector1 = dbdao.getVariables(serverip, username, passwords, dbStr);
//									returnValue.put("variables", vector);
//									returnValue.put("global_status", vector1);
//									Vector dispose = dbdao.getDispose(serverip, username, passwords, dbStr);
//									Vector dispose1 = dbdao.getDispose1(serverip, username, passwords, dbStr);
//									Vector dispose2 = dbdao.getDispose2(serverip, username, passwords, dbStr);
//									Vector dispose3 = dbdao.getDispose3(serverip, username, passwords, dbStr);
//									returnValue.put("dispose", dispose);
//									returnValue.put("dispose1", dispose1);
//									returnValue.put("dispose2", dispose2);
//									returnValue.put("dispose3", dispose3);//ɨ�����
								} catch (Exception e) {
									e.printStackTrace();
								}
								monitorValue.put(dbStr, returnValue);

							}
							SysLogger.info("#### �����ɼ�MYSQL--" + dbs[k] + " -- " + serverip+" ####");
							if (monitorValue != null && monitorValue.size() > 0) {
								ShareData.setMySqlmonitordata(serverip, monitorValue);
							}
							// �������������ݿ⣬��Ӹ澯��Ϣ 	
							IpTranslation tranfer = new IpTranslation();
							String hex = tranfer.formIpToHex(dbmonitorlist.getIpAddress());
							String sip = hex+":"+dbmonitorlist.getId();
							SysLogger.info("#### �����ӵ�mysql���ݿ�"+serverip+"����Ӹ澯��Ϣ ####");
							try {
								updateData(dbmonitorlist,ShareData.getMySqlmonitordata());
							} catch (Exception e) {
								e.printStackTrace();
							} 
							//����IP��ַ���ԭ�е���Ϣ
							dbdao.clearMysqlTableData("nms_mysqlinfo", sip);
							//����ɼ���Mysql������Ϣ
							dbdao.addMysql_nmsinfo(sip, monitorValue, dbs);
						}
						SysLogger.info("#### �����ɼ�MYSQL���ݿ�"+serverip+" ####" );
//					}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(dbdao!=null)
				dbdao.close();
			SysLogger.info("#### MYSQL����������� ####");
		}
		return returndata;
	}

	/**
	 * ���¸澯��Ϣ    HONGLI
	 * @param vo ���ݿ�ʵ��
	 * @param collectingData ���ݿ�ʵ���еĸ���������Ϣ
	 */
	public void updateData(Object vo , Object collectingData){
//		SysLogger.info("##############updateDate  ��ʼ###########");
		DBVo mysql = (DBVo)vo;		
		Hashtable monitorValueHashtable = (Hashtable)((Hashtable)collectingData).get(mysql.getIpAddress());
		CheckEventUtil checkEventUtil = new CheckEventUtil();
		NodeUtil nodeUtil = new NodeUtil();
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(mysql);
//		SysLogger.info("##############HONG mysql--datahashtable--"+monitorValueHashtable);
		String[] dbs = mysql.getDbName().split(",");
		for (int k = 0; k < dbs.length; k++) {
			Hashtable mysqldHashtable = (Hashtable)monitorValueHashtable.get(dbs[k]);
//			SysLogger.info("##############HONG mysql--mysqlHashtable--"+mysqldHashtable);
			Vector val =  (Vector)mysqldHashtable.get("Val");//���ݿ���ϸ��Ϣ
			java.util.Iterator iterator = val.iterator();//����
			
			String maxUsedConnections = "";  //��������Ӧ�����������
			String threadsConnected = "";//��ǰ�򿪵����ӵ�����
			String threadsCreated = "";//���������������ӵ��߳���
			String openTables = "";//��ǰ�򿪵ı������
			while (iterator.hasNext()) {
				Hashtable tempHashtable = (Hashtable)iterator.next();
				String variableName = (String)tempHashtable.get("variable_name");
				if(("Max_used_connections").equals(variableName)){
					maxUsedConnections = (String)tempHashtable.get("value");
				}
				if(("Threads_connected").equals(variableName)){
					threadsConnected = (String)tempHashtable.get("value");				
				}
				if(("Threads_created").equals(variableName)){
					threadsCreated = (String)tempHashtable.get("value");		
				}
				if(("Open_tables").equals(variableName)){
					openTables = (String)tempHashtable.get("value");		
				}
			}
			
//		SysLogger.info("##############HONG mysql--maxUsedConnections��Threads_connected��Threads_created��Open_tables"
//				+maxUsedConnections+" ��"+threadsConnected+" ��"+threadsCreated+" ��"+openTables);
			
		/*
			Hashtable sqlserverHashtable = (Hashtable)datahashtable.get("retValue");//�õ��ɼ�sqlserver���ݿ����Ϣ
			
			Hashtable memeryHashtable = (Hashtable)sqlserverHashtable.get("pages");//�õ��������ͳ����Ϣ
			
			Hashtable locksHashtable = (Hashtable)sqlserverHashtable.get("locks");//�õ�����ϸ��Ϣ
			
			Hashtable connsHashtable = (Hashtable)sqlserverHashtable.get("conns");//�õ����ݿ�ҳ����ͳ��
			*/
			AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
			List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(mysql.getId()), AlarmConstant.TYPE_DB, "mysql");//��ȡ�ɼ�ָ���б�
//			SysLogger.info("##############HONG mysql-list.size--"+list.size()+"###########");
			for(int i = 0 ; i < list.size() ; i ++){
				AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
//				SysLogger.info("##############HONG mysql-alarmIndicatorsNode.getEnabled--"+alarmIndicatorsNode.getEnabled()+"###########");
				if("1".equals(alarmIndicatorsNode.getEnabled())){
					String indicators = alarmIndicatorsNode.getName();
					String value = "";//value ��ָʵ�����ݿ��е�ֵ���� ������������    HONGLI
//					SysLogger.info("##############HONG mysql-indicators--"+indicators+"##########");
					if("max_used_connections".equals(indicators)){
						value = maxUsedConnections;//key ��DBDao collectSQLServerMonitItemsDetail �����е�pages��keyһ��
//						SysLogger.info("#######HONG mysql-maxUsedConnections-->  "+maxUsedConnections+"");
					}else if("threads_connected".equals(indicators)){
						value = threadsConnected;
					}else if("threads_created".equals(indicators)){
						value = threadsCreated;
					}else if("open_tables".equals(indicators)){
						value = openTables;
					}else {					
						continue;
					}
//					SysLogger.info("#######HONG mysql--indicator��value--"+indicators+"��"+value+"####");
					if(value == null)continue;
					checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, value,null);
				}
				
				
			}
		}
	}
}
