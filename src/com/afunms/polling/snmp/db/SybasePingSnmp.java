package com.afunms.polling.snmp.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.DBDao;
import com.afunms.application.model.DBVo;
import com.afunms.application.util.IpTranslation;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.EncryptUtil;
import com.afunms.common.util.ShareData;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.om.Pingcollectdata;

/**
 * sybase ping �ɼ� ʹ��JDBC�ɼ�
 * 
 * @author yangjun 2013/02/23
 * 
 */
public class SybasePingSnmp extends SnmpMonitor {

	public SybasePingSnmp() {
	}

	@SuppressWarnings("unchecked")
	public Hashtable collect_Data(NodeGatherIndicators nodeGatherIndicators) {
		Hashtable returndata = new Hashtable();
		try {
			DBDao dbdao = new DBDao();
			List dbmonitorlists = new ArrayList();
			dbmonitorlists = ShareData.getDBList();
			DBVo dbmonitorlist = new DBVo();
			if (dbmonitorlists != null && dbmonitorlists.size() > 0) {
				for (int i = 0; i < dbmonitorlists.size(); i++) {
					DBVo vo = (DBVo) dbmonitorlists.get(i);
					if (vo.getId() == Integer.parseInt(nodeGatherIndicators.getNodeid())) {
						dbmonitorlist = vo;
						break;
					}
				}
			}
			if (dbmonitorlist != null) {
				if (dbmonitorlist.getManaged() == 0) {
					// ���δ���������ɼ���pingֵΪ0
					returndata.put("ping", 0);
					return returndata;
				}
				boolean sybaseIsOK = false;
				try {
					String serverip = dbmonitorlist.getIpAddress();
					String passwords = EncryptUtil.decode(dbmonitorlist.getPassword());
					String username = dbmonitorlist.getUser();
					int port = Integer.parseInt(dbmonitorlist.getPort());
					sybaseIsOK = dbdao.getSysbaseIsOk(serverip, username, passwords, port);
				} catch (Exception e) {
					sybaseIsOK = false;
				} finally {
					dbdao.close();
				}
				
				String status = "0";
				if (sybaseIsOK) {
					returndata.put("ping", "100");
					status = "1";
				} else {
					returndata.put("ping", "0");
					status = "0";
				}
				// ���µ�ǰ״̬
				dbdao = new DBDao();
				String hex = IpTranslation.formIpToHex(dbmonitorlist.getIpAddress());
				try {
					dbdao.updateNmsValueByUniquekeyAndTablenameAndKey("nms_sybasestatus", "serverip", hex+":"+dbmonitorlist.getId(), "status", status);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					dbdao.close();
				}

				// �����ڴ�
				if (!(ShareData.getSharedata().containsKey(dbmonitorlist
						.getIpAddress()
						+ ":" + dbmonitorlist.getId()))) {
					ShareData.getSharedata().put(
							dbmonitorlist.getIpAddress() + ":"
									+ dbmonitorlist.getId(), returndata);
				} else {
					Hashtable sybaseHash = (Hashtable) ShareData.getSharedata()
							.get(
									dbmonitorlist.getIpAddress() + ":"
											+ dbmonitorlist.getId());
					sybaseHash.put("ping", (String) returndata.get("ping"));
				}

				// ���
				try {
					Pingcollectdata hostdata = null;
					hostdata = new Pingcollectdata();
					hostdata.setIpaddress(dbmonitorlist.getIpAddress() + ":"
							+ dbmonitorlist.getId());
					Calendar date = Calendar.getInstance();
					hostdata.setIpaddress(dbmonitorlist.getIpAddress());
					hostdata.setCollecttime(date);
					hostdata.setCategory("SYSPing");
					hostdata.setEntity("Utilization");
					hostdata.setSubentity("ConnectUtilization");
					hostdata.setRestype("dynamic");
					hostdata.setUnit("%");
					hostdata.setThevalue((String) returndata.get("ping"));
					try {
						dbdao.createHostData(hostdata);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						dbdao.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				String pingvalue = (String) returndata.get("ping");
				// �жϸ澯
				try {
					if (pingvalue != null) {
						NodeUtil nodeUtil = new NodeUtil();
						NodeDTO nodeDTO = nodeUtil
								.conversionToNodeDTO(dbmonitorlist);
						// �ж��Ƿ���ڴ˸澯ָ��
						AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
						List list = alarmIndicatorsUtil
								.getAlarmInicatorsThresholdForNode(nodeDTO.getId()
										+ "", nodeDTO.getType(), nodeDTO
										.getSubtype());
						CheckEventUtil checkEventUtil = new CheckEventUtil();
						for (int i = 0; i < list.size(); i++) {
							AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list
									.get(i);
							if ("ping".equalsIgnoreCase(alarmIndicatorsNode
									.getName())) {
								if (pingvalue != null) {
									checkEventUtil.checkEvent(nodeDTO,
											alarmIndicatorsNode, pingvalue);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returndata;
	}
}
