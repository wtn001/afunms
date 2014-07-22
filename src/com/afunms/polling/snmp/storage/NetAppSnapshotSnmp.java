package com.afunms.polling.snmp.storage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.afunms.application.dao.NetAppDataOperator;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.NetAppSnapshot;

//NetApp Snapshot ���ղɼ���
public class NetAppSnapshotSnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		Hashtable returnHash = new Hashtable();
		Vector tempVector = new Vector();
		Host node = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		if (node == null)
			return returnHash;

		try {
			NetAppSnapshot netAppSnapshot = null;
			Calendar date = Calendar.getInstance();

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node) PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
				Date cc = date.getTime();
				String time = sdf.format(cc);
				snmpnode.setLastTime(time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				String[] oids = new String[] { ".1.3.6.1.4.1.789.1.5.5.2.1.1",  //����
						".1.3.6.1.4.1.789.1.5.5.2.1.2",  //��������ʱ��-��
						".1.3.6.1.4.1.789.1.5.5.2.1.3",  //��������ʱ��-��
						".1.3.6.1.4.1.789.1.5.5.2.1.4",  //��������ʱ��-Сʱ
						".1.3.6.1.4.1.789.1.5.5.2.1.5",  //��������ʱ��-��
						".1.3.6.1.4.1.789.1.5.5.2.1.6",  //��������
						".1.3.6.1.4.1.789.1.5.5.2.1.7",  //�����˿��յ�Volume ID
						".1.3.6.1.4.1.789.1.5.5.2.1.8",  //Volume�����Ŀ�����Ŀ
						".1.3.6.1.4.1.789.1.5.5.2.1.9",  // Volume ����
						".1.3.6.1.4.1.789.1.5.5.2.1.10",  //Volume ����
				};

				String[][] valueArray = null;
				try {
					valueArray = SnmpUtils.getTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(), 3, 1000 * 30);
				} catch (Exception e) {
					valueArray = null;
				}
				if (valueArray != null) {
					for (int i = 0; i < valueArray.length; i++) {
						netAppSnapshot = new NetAppSnapshot();
						netAppSnapshot.setIpaddress(node.getIpAddress());
						netAppSnapshot.setCollectTime(date);
						netAppSnapshot.setSlVIndex(valueArray[i][0]);
						netAppSnapshot.setSlVMonth(valueArray[i][1]);
						netAppSnapshot.setSlVDay(valueArray[i][2]);
						netAppSnapshot.setSlVHour(valueArray[i][3]);
						netAppSnapshot.setSlVMinutes(valueArray[i][4]);
						netAppSnapshot.setSlVName(valueArray[i][5]);
						netAppSnapshot.setSlVVolume(valueArray[i][6]);
						netAppSnapshot.setSlVNumber(valueArray[i][7]);
						netAppSnapshot.setSlVVolumeName(valueArray[i][8]);
						netAppSnapshot.setSlVType(valueArray[i][9]);
						tempVector.addElement(netAppSnapshot);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!(ShareData.getSharedata().containsKey(node.getIpAddress()))) {
			Hashtable ipAllData = new Hashtable();
			if (ipAllData == null)
				ipAllData = new Hashtable();
			if (tempVector != null && tempVector.size() > 0)
				ipAllData.put("snapshot", tempVector);
			ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		} else {
			if (tempVector != null && tempVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(node.getIpAddress())).put("snapshot", tempVector);
		}

		returnHash.put("snapshot", tempVector);

		tempVector = null;

		NetAppDataOperator op=new NetAppDataOperator();
		op.CreateResultTosql(returnHash, node.getIpAddress());

		return returnHash;
	}
}
