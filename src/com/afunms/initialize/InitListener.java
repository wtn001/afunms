package com.afunms.initialize;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.application.model.SystemFlag;
import com.afunms.application.util.ControlServer;
import com.afunms.application.util.MachineTask;
import com.afunms.capreport.subscribe.SubscribeTimer;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.om.Task;
import com.afunms.polling.task.AlarmUpdateTask;
import com.afunms.polling.task.MonitorTask;
import com.afunms.polling.task.MonitorTimer;
import com.afunms.polling.task.TaskFactory;
import com.afunms.polling.task.TaskXml;
import com.afunms.system.dao.SysLogDao;
import com.afunms.system.model.SysLog;
import com.afunms.topology.dao.HostNodeDao;
import com.database.config.SystemConfig;
import com.gathertask.TaskManager;

public class InitListener implements ServletContextListener {
	private MonitorTask monitorTask = null;
	// �����澯
	private MonitorTimer soundTimer = null;
	// ��·���
	private MonitorTimer checkLinkTimer = null;
	// ����XML����
	private MonitorTimer updateXMLTimer = null;
	// ��Сʱ�鵵
	private MonitorTimer hostCollectHourTimer = null;
	// ����鵵
	private MonitorTimer hostCollectDayTimer = null;
	// ���񼶱�
	private MonitorTimer m_5_slaTelnetTimer = null;
	// ��ʱ���������ļ�
	private MonitorTimer m_30_backupTelnetConfigTimer = null;
	// ��ʱ�����޸�����
	private MonitorTimer m_30_passwordBackupTelnetConfigTimer = null;
	// ���ʿ����б�ɼ�
	private MonitorTimer h_1_acl_Timer = null;
	// 3D��������
	private MonitorTimer carbiNetTimer = null;

	SnmpTrapsListener trapListener = SnmpTrapsListener.getInstance();
	Hashtable task_ht = new Hashtable();

	public InitListener() {
		soundTimer = null;
		hostCollectHourTimer = null;
		hostCollectDayTimer = null;
		checkLinkTimer = null;
		updateXMLTimer = null;
		m_5_slaTelnetTimer = null;
		m_30_backupTelnetConfigTimer = null;
		m_30_passwordBackupTelnetConfigTimer = null;
		h_1_acl_Timer = null;
		carbiNetTimer = null;
	}

	public void contextDestroyed(ServletContextEvent event) {
		trapListener.close();
		if (monitorTask != null)
			monitorTask.destroy();
		if (soundTimer != null)
			soundTimer.canclethis(true);
		if (checkLinkTimer != null)
			checkLinkTimer.canclethis(true);
		if (updateXMLTimer != null)
			updateXMLTimer.canclethis(true);
		if (hostCollectHourTimer != null)
			hostCollectHourTimer.canclethis(true);
		if (hostCollectDayTimer != null)
			hostCollectDayTimer.canclethis(true);
		if (m_5_slaTelnetTimer != null)
			m_5_slaTelnetTimer.canclethis(true);
		if (m_30_backupTelnetConfigTimer != null)
			m_30_backupTelnetConfigTimer.canclethis(true);
		if (m_30_passwordBackupTelnetConfigTimer != null)
			m_30_passwordBackupTelnetConfigTimer.canclethis(true);
		if (h_1_acl_Timer != null)
			h_1_acl_Timer.canclethis(true);
		if (carbiNetTimer != null)
			carbiNetTimer.canclethis(true);

		if (ResourceCenter.getInstance().isStartPolling()) {
			HostNodeDao dao = new HostNodeDao();
			try {
				dao.updateInterfaceData(PollingEngine.getInstance().getNodeList());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				dao.close();
			}
		}
		saveLog("ϵͳ�ر�");
	}

	public void contextInitialized(ServletContextEvent event) {
		SystemFlag.getInstance().setFirstStart(false);
		SysInitialize sysInit = new SysInitialize();
		sysInit.setSysPath(event.getServletContext().getRealPath("/"));
		sysInit.init();
		saveLog("ϵͳ����");
		try {
			SysLogger.info("######### ���� Զ�̿��ػ� ���������� #######");
			ControlServer cs = new ControlServer(ShareData.getIp_clientInfoHash());
			MachineTask mt = new MachineTask(cs);
			Thread t = new Thread(mt);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		SysLogger.info("listener ��������.....");
		soundTimer = new MonitorTimer(true);
		hostCollectHourTimer = new MonitorTimer(true);
		hostCollectDayTimer = new MonitorTimer(true);
		checkLinkTimer = new MonitorTimer(true);
		updateXMLTimer = new MonitorTimer(true);
		m_5_slaTelnetTimer = new MonitorTimer(true);
		m_30_backupTelnetConfigTimer = new MonitorTimer(true);
		h_1_acl_Timer = new MonitorTimer(true);
		carbiNetTimer = new MonitorTimer(true);
		m_30_passwordBackupTelnetConfigTimer = new MonitorTimer(true);
		
		// ȡ���10�����ڵĸ澯��Ϣ����
		soundTimer = new MonitorTimer(true);
		soundTimer.schedule(new AlarmUpdateTask(), 0, 60 * 1000);
		// ����sysLog�ɼ�
		String flag = SystemConfig.getConfigInfomation("Agentconfig", "syslogConfig");
		if (flag != null && flag.equals("enable")) {
			ExecuteCollectSyslog executeCollectSyslog = new ExecuteCollectSyslog();
			new Thread(executeCollectSyslog).start();
		}
		SysLogger.info("[AlertAlarm][AlarmUpdateListener] ȡ���±�����Ϣ��ʱ��������");
		// ��log4j.properties�б���${appDir}��ֵ
		System.setProperty("appDir", event.getServletContext().getRealPath("/"));
		try {
			task_ht = taskNum();
			int num = task_ht.size();
			TaskFactory taskF = new TaskFactory();
			for (int i = 0; i < num; i++) {
				String taskinfo = task_ht.get(String.valueOf(i)).toString();
				String[] tmp = taskinfo.split(":");
				String taskname = tmp[0];
				float interval = Float.parseFloat(tmp[1]);
				String unit = tmp[2];
				SysLogger.info("interval is -- " + interval + "  unit is  -- " + unit + "taskname is -- " + taskname);
				try {
					monitorTask = taskF.getInstance(taskname);
					if (monitorTask != null) {
						monitorTask.setInterval(interval, unit);
						if (taskname.equals("hostcollectdatahourtask")) {
							hostCollectHourTimer.schedule(monitorTask, 10000L, monitorTask.getInterval());
						} else if (taskname.equals("hostcollectdatadaytask")) {
							hostCollectDayTimer.schedule(monitorTask, 10000L, monitorTask.getInterval());
						} else if (taskname.equals("checklinktask")) {
							checkLinkTimer.schedule(monitorTask, 10000L, monitorTask.getInterval());
						} else if (taskname.equals("updatexmltask")) {
							updateXMLTimer.schedule(monitorTask, 10000L, monitorTask.getInterval());
						} else if (taskname.equals("m5slatelnettask")) {
							m_5_slaTelnetTimer.schedule(monitorTask, 25000L, monitorTask.getInterval());
						} else if (taskname.equals("m_30_backupTelnetConfigTask")) {
							m_30_backupTelnetConfigTimer.schedule(monitorTask, 1000L, monitorTask.getInterval());
						} else if (taskname.equals("m_30_passwdChangeHintTask")) {
							m_30_passwordBackupTelnetConfigTimer.schedule(monitorTask, 1000L, monitorTask.getInterval());
						} else if (taskname.equals("h1Acltask")) {
							h_1_acl_Timer.schedule(monitorTask, 1000L, monitorTask.getInterval());
						} else if (taskname.equals("cabinettask")) {
							carbiNetTimer.schedule(monitorTask, 30000L, monitorTask.getInterval());
						}
					} else {
						throw new Exception(taskname + "  Task not find ,please check it!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			// �������������������ֹͣ162�����˿�
			trapListener.listen();
		} catch (Exception e) {
			SysLogger.info("error in ExecutePing!" + e.getMessage());
		}

		TaskManager manager = new TaskManager();
		manager.createAllTask();
		manager.CreateGahterAlarmSQLTask();
		manager.CreateMaintainTask();
		manager.CreateGahterSQLTask();
		// ��ʱ�������
		manager.CreateDataTempTask();
		// �����������ս���
		manager.CreateGCTask();
		// �������ļ�ʱ��
		SubscribeTimer.startupSubscribe();
	}

	private void saveLog(String event) {
		SysLog vo = new SysLog();
		vo.setEvent(event);
		vo.setLogTime(SysUtil.getCurrentTime());
		vo.setUser("Tomcat");
		vo.setIp("127.0.0.1");
		SysLogDao dao = new SysLogDao();
		try {
			dao.save(vo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
	}

	public Hashtable taskNum() {
		Hashtable ht = new Hashtable();
		int index = 0;
		List list = new ArrayList();
		try {
			TaskXml taskxml = new TaskXml();
			list = taskxml.ListXml();
			for (int i = 0; i < list.size(); i++) {
				Task task = new Task();
				BeanUtils.copyProperties(task, list.get(i));
				String sign = task.getStartsign();
				if ("1".equals(sign)) {
					if (task.getTaskname().equals("linktrust"))
						continue;
					String taskname = task.getTaskname();
					Float interval = task.getPolltime();
					String polltimeunit = task.getPolltimeunit();
					ht.put(String.valueOf(index), taskname + ":" + interval + ":" + polltimeunit);
					index++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ht;
	}
}