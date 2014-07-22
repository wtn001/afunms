package com.afunms.polling.snmp.was;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.application.dao.WasConfigDao;
import com.afunms.application.model.WasConfig;
import com.afunms.application.wasmonitor.UrlConncetWas;
import com.afunms.common.util.ShareData;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Pingcollectdata;

public class Was_ping {
//	private Hashtable sendeddata = ShareData.getSendeddata();
	private Hashtable wasdata = ShareData.getWasdata();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Was_ping() {
	}

	public void collect_Data(NodeGatherIndicators tomcatIndicators) {
		WasConfig wasconf = null;
		String id = tomcatIndicators.getNodeid();
		// Hashtable gatherHash ;
		try {
			int serverflag = 0;
			String ipaddress = "";
			WasConfigDao dao = new WasConfigDao();
			try {
				wasconf = (WasConfig) dao.findByID(id);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
			// AdminClient5 wasadmin = new AdminClient5();
			UrlConncetWas conWas = new UrlConncetWas();
			Hashtable hst = new Hashtable();
			com.afunms.polling.node.Was _tnode = (com.afunms.polling.node.Was) PollingEngine.getInstance().getWasByID(wasconf.getId());

			Calendar _date = Calendar.getInstance();
			Date _cc = _date.getTime();
			String _tempsenddate = sdf.format(_cc);
			// ��ʼ��Was�����״̬
			_tnode.setLastTime(_tempsenddate);
			_tnode.setAlarm(false);
			_tnode.getAlarmMessage().clear();
			_tnode.setStatus(0);

			// �Կ����Խ��м��
			boolean collectWasIsOK = false;
			try {
				collectWasIsOK = conWas.connectWasIsOK(wasconf.getIpaddress(),
						wasconf.getPortnum());
			} catch (Exception e) {
				// e.printStackTrace();
			}
			if (collectWasIsOK) {
				// ����״̬
				Pingcollectdata hostdata = null;
				hostdata = new Pingcollectdata();
				hostdata.setIpaddress(wasconf.getIpaddress());
				Calendar date = Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WasPing");
				hostdata.setEntity("Utilization");
				hostdata.setSubentity("ConnectUtilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("%");
				hostdata.setThevalue("100");
				WasConfigDao wasconfigdao = new WasConfigDao();
				try {
					wasconfigdao.createHostData(wasconf, hostdata);
					if (wasdata.containsKey("was" + ":"
							+ wasconf.getIpaddress()))
						wasdata.remove("was" + ":" + wasconf.getIpaddress());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					wasconfigdao.close();
				}
				// �������ݲɼ�
				try {
					hst = conWas.ConncetWas(wasconf.getIpaddress(), String
							.valueOf(wasconf.getPortnum()), "", "", wasconf
							.getVersion(), null);
				} catch (Exception e) {

				}
			} else {
				// ���������ж��Ƿ���Ҫ�澯
				// if
				// (wlservers.containsKey(weblogicconf.getIpaddress()+":"+server.getServerRuntimeName()))
				// createSMS(weblogicconf,server);

				try {
					// com.afunms.polling.node.Weblogic tc = new
					// com.afunms.polling.node.Weblogic();
					// BeanUtils.copyProperties(tc, weblogicconf);
					// if (data_ht==null){
					// ��Ҫ�����ʼ��������ڵķ������Ƿ�����ͨ
//					Host host = (Host) PollingEngine.getInstance().getNodeByIP(
//							wasconf.getIpaddress());
					Vector ipPingData = (Vector) ShareData.getPingdata().get(
							wasconf.getIpaddress());
					ipaddress = wasconf.getIpaddress();
					if (ipPingData != null) {
						Pingcollectdata pingdata = (Pingcollectdata) ipPingData
								.get(0);
						Calendar tempCal = (Calendar) pingdata.getCollecttime();
						Date cc = tempCal.getTime();
						String _time = sdf.format(cc);
						String lastTime = _time;
						String pingvalue = pingdata.getThevalue();
						if (pingvalue == null || pingvalue.trim().length() == 0)
							pingvalue = "0";
						double pvalue = new Double(pingvalue);
						if (pvalue == 0) {
							// �������������Ӳ���***********************************************
							// com.afunms.polling.node.Weblogic
							// tnode=(com.afunms.polling.node.Weblogic)PollingEngine.getInstance().getWeblogicByIP(ipaddress);
							_tnode.setAlarm(true);
							_tnode.setStatus(1);
							List alarmList = _tnode.getAlarmMessage();
							if (alarmList == null)
								alarmList = new ArrayList();
							_tnode.getAlarmMessage().add("WAS����ֹͣ");
							String sysLocation = "";
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(ipaddress);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("WasPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("0");
							WasConfigDao wasconfigdao = new WasConfigDao();
							try {
								wasconfigdao.createHostData(wasconf, hostdata);
								if (wasdata.containsKey("was" + ":"
										+ wasconf.getIpaddress()))
									wasdata.remove("was" + ":"
											+ wasconf.getIpaddress());
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								wasconfigdao.close();
							}
							try {
								// SmscontentDao eventdao = new SmscontentDao();
								String eventdesc = "WAS����(" + wasconf.getName()
										+ " IP:" + wasconf.getIpaddress() + ")"
										+ "ֹͣ";
								// eventdao.createEventWithReasion("poll",_tnode.getId()+"",_tnode.getAdminIp()+"("+_tnode.getAdminIp()+")",eventdesc,3,"wasserver","ping","���ڵķ��������Ӳ���");
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							// com.afunms.polling.node.Weblogic
							// tnode=(com.afunms.polling.node.Weblogic)PollingEngine.getInstance().getWeblogicByIP(ipaddress);
							_tnode.setAlarm(true);
							_tnode.setStatus(3);
							List alarmList = _tnode.getAlarmMessage();
							if (alarmList == null)
								alarmList = new ArrayList();
							_tnode.getAlarmMessage().add("WAS����ֹͣ");
							Pingcollectdata hostdata = null;
							hostdata = new Pingcollectdata();
							hostdata.setIpaddress(ipaddress);
							Calendar date = Calendar.getInstance();
							hostdata.setCollecttime(date);
							hostdata.setCategory("WasPing");
							hostdata.setEntity("Utilization");
							hostdata.setSubentity("ConnectUtilization");
							hostdata.setRestype("dynamic");
							hostdata.setUnit("%");
							hostdata.setThevalue("0");
							WasConfigDao wasconfigdao = new WasConfigDao();
							try {
								wasconfigdao.createHostData(wasconf, hostdata);
								if (wasdata.containsKey("was" + ":"
										+ wasconf.getIpaddress()))
									wasdata.remove("was" + ":"
											+ wasconf.getIpaddress());
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								wasconfigdao.close();
							}
						}

					} else {
						// com.afunms.polling.node.Weblogic
						// tnode=(com.afunms.polling.node.Weblogic)PollingEngine.getInstance().getWeblogicByIP(ipaddress);
						_tnode.setAlarm(true);
						_tnode.setStatus(3);
						List alarmList = _tnode.getAlarmMessage();
						if (alarmList == null)
							alarmList = new ArrayList();
						_tnode.getAlarmMessage().add("WAS����ֹͣ");
						Pingcollectdata hostdata = null;
						hostdata = new Pingcollectdata();
						hostdata.setIpaddress(ipaddress);
						Calendar date = Calendar.getInstance();
						hostdata.setCollecttime(date);
						hostdata.setCategory("WasPing");
						hostdata.setEntity("Utilization");
						hostdata.setSubentity("ConnectUtilization");
						hostdata.setRestype("dynamic");
						hostdata.setUnit("%");
						hostdata.setThevalue("0");
						WasConfigDao wasconfigdao = new WasConfigDao();
						try {
							wasconfigdao.createHostData(wasconf, hostdata);
							if (wasdata.containsKey("was" + ":"
									+ wasconf.getIpaddress()))
								wasdata.remove("was" + ":"
										+ wasconf.getIpaddress());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							wasconfigdao.close();
						}
					}
					try {
						// createSMS("wasserver",wasconf);
						serverflag = 1;
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// try{
			// /**
			// * Author by QuZhi
			// * ����ȡ����ָ��ģ������
			// *
			// JVM/JDBCProvider/SessionManager/SystemMetrics/DynaCache/TransactionService/ORB
			// * @return /ObjectPool/ThreadPool
			// */
			// //
			// com.afunms.common.util.ShareData.setWasdata("was"+":"+wasconf.getIpaddress(),hst);
			// }catch(Exception ex){
			// ex.printStackTrace();
			// }
			if (hst != null) {
				ShareData.getWasdata().put(wasconf.getIpaddress(), hst);
			}
			hst = null;
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

//	public void createSMS(String was, WasConfig wasconf) {
//
//		// ��������
//		// ���ڴ����õ�ǰ���IP��PING��ֵ
//		Calendar date = Calendar.getInstance();
//		try {
//			if (!sendeddata.containsKey(was + ":" + wasconf.getId())) {
//				// �����ڣ��������ţ�������ӵ������б���
//				Smscontent smscontent = new Smscontent();
//				String time = sdf.format(date.getTime());
//				smscontent.setLevel("2");
//				smscontent.setObjid(wasconf.getId() + "");
//				if ("wasserver".equals(was)) {
//					smscontent.setMessage("WAS" + " (" + wasconf.getName()
//							+ ":" + wasconf.getIpaddress() + ")" + "�ķ���ֹͣ��");
//				}
//				smscontent.setRecordtime(time);
//				smscontent.setSubtype("wasserver");
//				smscontent.setSubentity("ping");
//				smscontent.setIp(wasconf.getIpaddress());
//				// smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+"
//				// IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
//				// ���Ͷ���
//				SmscontentDao smsmanager = new SmscontentDao();
//				smsmanager.sendURLSmscontent(smscontent);
//				sendeddata.put(was + ":" + wasconf.getId(), date);
//			} else {
//				// ���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
//				Calendar formerdate = (Calendar) sendeddata.get(was + ":"
//						+ wasconf.getId());
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//				Date last = null;
//				Date current = null;
//				Calendar sendcalen = formerdate;
//				Date cc = sendcalen.getTime();
//				String tempsenddate = formatter.format(cc);
//
//				Calendar currentcalen = date;
//				cc = currentcalen.getTime();
//				last = formatter.parse(tempsenddate);
//				String currentsenddate = formatter.format(cc);
//				current = formatter.parse(currentsenddate);
//
//				long subvalue = current.getTime() - last.getTime();
//				if (subvalue / (1000 * 60 * 60 * 24) >= 1) {
//					// ����һ�죬���ٷ���Ϣ
//					Smscontent smscontent = new Smscontent();
//					String time = sdf.format(date.getTime());
//					smscontent.setLevel("2");
//					smscontent.setObjid(wasconf.getId() + "");
//					if ("wasserver".equals(was)) {
//						smscontent
//								.setMessage("WAS" + " (" + wasconf.getName()
//										+ ":" + wasconf.getIpaddress() + ")"
//										+ "�ķ���ֹͣ��");
//					}
//					smscontent.setRecordtime(time);
//					smscontent.setSubtype("wasserver");
//					smscontent.setSubentity("ping");
//					smscontent.setIp(wasconf.getIpaddress());
//					// smscontent.setMessage("db&"+time+"&"+dbmonitorlist.getId()+"&"+db+"("+dbmonitorlist.getDbName()+"
//					// IP:"+dbmonitorlist.getIpAddress()+")"+"�����ݿ����ֹͣ");
//					// ���Ͷ���
//					SmscontentDao smsmanager = new SmscontentDao();
//					smsmanager.sendURLSmscontent(smscontent);
//					// �޸��Ѿ����͵Ķ��ż�¼
//					sendeddata.put(was + ":" + wasconf.getId(), date);
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}
