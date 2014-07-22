/**
 * <p>Description:action center,at the same time, the control legal power</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-06
 */

package com.afunms.initialize;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.alarm.send.SendAlarmUtil;
import com.afunms.alarm.send.SendPageAlarm;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.common.util.CommonUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SyslogDefs;
import com.afunms.event.dao.CheckEventDao;
import com.afunms.event.dao.NetSyslogDao;
import com.afunms.event.dao.SyslogDao;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.event.model.NetSyslog;
import com.afunms.event.model.Syslog;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Host;
import com.afunms.topology.dao.NetSyslogNodeAlarmKeyDao;
import com.afunms.topology.dao.NetSyslogNodeRuleDao;
import com.afunms.topology.model.NetSyslogNodeAlarmKey;
import com.afunms.topology.model.NetSyslogNodeRule;

public class ExecuteCollectSyslog implements Runnable {

	// DatagramPacket packet;// ʵ������ͨ�ţ�
	DatagramSocket socket;// �շ����ݣ�

	static public int sport = 514;// �������˶˿�514��
	int processId;// ����id
	String processName;// ������
	String processIdStr = "";// ����id�ַ�
	int facility;// �¼���Դ����
	int priority;// ���ȼ�����
	String facilityName;// �¼���Դ����
	String priorityName;// ���ȼ�����
	String hostname;// ��������
	String username;// ��½�û�
	Calendar timestamp;// ʱ���
	String message;// ����Ϣ����
	String ipaddress;// IP��ַ
	String businessid;// ҵ��ID
	boolean sign = false;

	int eventid;// �¼�ID
	
	

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ExecuteCollectSyslog() {
		try {
			socket = new DatagramSocket(sport);
			SysLogger.info("����������syslog�˿ڣ�" + socket.getLocalPort());

		} catch (SocketException e) {
			SysLogger.info("Syslog������������ʧ�ܣ���ȷ�϶˿��Ƿ���ʹ�ã�");
			e.printStackTrace();
		}
	}

	public void run() {
		if (socket == null) {
			return;
		}
		ProcessSyslog syslog=null;
		while (true) {
			byte[] b = new byte[1024];
			// byte[] b = new byte[2048];
			DatagramPacket packet = new DatagramPacket(b, b.length);// ����һ���������ݵģ�
			 syslog=new ProcessSyslog();
			try {
				 socket.receive(packet);// �������ݣ�
				 syslog.createTask(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				syslog=null;
			}
			
			// Thread thread = new Thread(createTask(packet));
			// thread.start();
		}
	}



	

	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
