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
import com.afunms.polling.om.NetAppDisk;

//NetApp Disk�ɼ���
public class NetAppDiskSnmp extends SnmpMonitor {
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		Hashtable returnHash = new Hashtable();
		Vector tempVector = new Vector();
		Host node = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		if (node == null)
			return returnHash;

		try {
			NetAppDisk netAppDisk = null;
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
				String[] oids = new String[] { ".1.3.6.1.4.1.789.1.5.4.1.1",  //��������
						".1.3.6.1.4.1.789.1.5.4.1.2",  //��������
						".1.3.6.1.4.1.789.1.5.4.1.3",  //�����ܴ�С(kb)
						".1.3.6.1.4.1.789.1.5.4.1.4",  //�������ô�С
						".1.3.6.1.4.1.789.1.5.4.1.5",  //�������ô�С
						".1.3.6.1.4.1.789.1.5.4.1.6",  //����ʹ����
						".1.3.6.1.4.1.789.1.5.4.1.7",  //����inode
						".1.3.6.1.4.1.789.1.5.4.1.8",  //����inode
						".1.3.6.1.4.1.789.1.5.4.1.9",  //inodeʹ����
						".1.3.6.1.4.1.789.1.5.4.1.10",  //���ص�
						".1.3.6.1.4.1.789.1.5.4.1.11",  //��������ļ���
						".1.3.6.1.4.1.789.1.5.4.1.12",  //��ʹ�õ��ļ���
						".1.3.6.1.4.1.789.1.5.4.1.13",  //����ļ�������
						".1.3.6.1.4.1.789.1.5.4.1.14",  //�ļ�ϵͳ��������High��
						".1.3.6.1.4.1.789.1.5.4.1.15",  //�ļ�ϵͳ��������low��
						".1.3.6.1.4.1.789.1.5.4.1.16",  //�ļ�ϵͳ��ʹ��������High��
						".1.3.6.1.4.1.789.1.5.4.1.17",  //�ļ�ϵͳ��ʹ��������low��
						".1.3.6.1.4.1.789.1.5.4.1.18",  //�ļ�ϵͳ����������High��
						".1.3.6.1.4.1.789.1.5.4.1.19",  //�ļ�ϵͳ����������low��
						".1.3.6.1.4.1.789.1.5.4.1.20",  //�ļ�ϵͳ״̬
						".1.3.6.1.4.1.789.1.5.4.1.21",  //�ļ�ϵͳ����״̬
						".1.3.6.1.4.1.789.1.5.4.1.22",  //�ļ�ϵͳPlex��Ŀ
						".1.3.6.1.4.1.789.1.5.4.1.23",  //�ļ�ϵͳ����
				};

				String[][] valueArray = null;
				try {
					valueArray = SnmpUtils.getTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), node.getSecuritylevel(), node.getSecurityName(), node.getV3_ap(), node.getAuthpassphrase(), node.getV3_privacy(), node.getPrivacyPassphrase(), 3, 1000 * 30);
				} catch (Exception e) {
					valueArray = null;
				}
				if (valueArray != null) {
					for (int i = 0; i < valueArray.length; i++) {
						netAppDisk = new NetAppDisk();
						netAppDisk.setIpaddress(node.getIpAddress());
						netAppDisk.setCollectTime(date);
						netAppDisk.setDfIndex(valueArray[i][0]);
						netAppDisk.setDfFileSys(valueArray[i][1]);
						netAppDisk.setDfKBytesTotal(valueArray[i][2]);
						netAppDisk.setDfKBytesUsed(valueArray[i][3]);
						netAppDisk.setDfKBytesAvail(valueArray[i][4]);
						netAppDisk.setDfPerCentKBytesCapacity(valueArray[i][5]);
						netAppDisk.setDfInodesUsed(valueArray[i][6]);
						netAppDisk.setDfInodesFree(valueArray[i][7]);
						netAppDisk.setDfPerCentInodeCapacity(valueArray[i][8]);
						netAppDisk.setDfMountedOn(valueArray[i][9]);
						netAppDisk.setDfMaxFilesAvail(valueArray[i][10]);
						netAppDisk.setDfMaxFilesUsed(valueArray[i][11]);
						netAppDisk.setDfMaxFilesPossible(valueArray[i][12]);
						netAppDisk.setDfHighTotalKBytes(valueArray[i][13]);
						netAppDisk.setDfLowTotalKBytes(valueArray[i][14]);
						netAppDisk.setDfHighUsedKBytes(valueArray[i][15]);
						netAppDisk.setDfLowUsedKBytes(valueArray[i][16]);
						netAppDisk.setDfHighAvailKBytes(valueArray[i][17]);
						netAppDisk.setDfLowAvailKBytes(valueArray[i][18]);
						netAppDisk.setDfStatus(valueArray[i][19]);
						netAppDisk.setDfMirrorStatus(valueArray[i][20]);
						netAppDisk.setDfPlexCount(valueArray[i][21]);
						netAppDisk.setDfType(valueArray[i][22]);
						tempVector.addElement(netAppDisk);
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
				ipAllData.put("disk", tempVector);
			ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		} else {
			if (tempVector != null && tempVector.size() > 0)
				((Hashtable) ShareData.getSharedata().get(node.getIpAddress())).put("disk", tempVector);
		}

		returnHash.put("disk", tempVector);

		tempVector = null;

		NetAppDataOperator op=new NetAppDataOperator();
		op.CreateResultTosql(returnHash, node.getIpAddress());

		return returnHash;
	}
}
