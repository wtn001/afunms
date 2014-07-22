package com.afunms.polling.task;

import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.beanutils.BeanUtils;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.TomcatDao;
import com.afunms.application.dao.TomcatPreDao;
import com.afunms.application.model.Tomcat;
import com.afunms.application.model.TomcatPre;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.om.Pingcollectdata;

public class TomcatDataCollector {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	DecimalFormat df = new DecimalFormat("#.##");

	Calendar cal = Calendar.getInstance();

	Date ct = cal.getTime();

	public void collect_Data(NodeGatherIndicators tomcatIndicators) {
		int linkFlag = 1;
		Hashtable rsHt = new Hashtable();
		try {
			Tomcat node = new Tomcat();
			TomcatDao dao = new TomcatDao();
			try {
				node = (Tomcat) dao.findByID(tomcatIndicators.getNodeid());
			} catch (Exception e) {
			} finally {
				dao.close();
			}
			com.afunms.polling.node.Tomcat tc = new com.afunms.polling.node.Tomcat();
			try {
				BeanUtils.copyProperties(tc, node);
				com.afunms.polling.node.Tomcat tomcatNode = (com.afunms.polling.node.Tomcat) PollingEngine.getInstance().getTomcatByIP(tc.getIpAddress());
				tomcatNode.setLastTime(sdf.format(ct));
				tomcatNode.setAlarm(false);
				tomcatNode.getAlarmMessage().clear();
				tomcatNode.setStatus(0);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// ��ʼ�ɼ���Ϣ
			String jmxURL = "service:jmx:rmi:///jndi/rmi://" + tc.getIpAddress() + ":" + tc.getPort() + "/jmxrmi";
			JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
			Map map = new HashMap();

			String[] credentials = new String[] { tc.getUser().trim(), tc.getPassword().trim() };
			map.put("jmx.remote.credentials", credentials);
			JMXConnector connector = null;
			try {
				connector = JMXConnectorFactory.connect(serviceURL, map);
			} catch (Exception e) {
				SysLogger.info("[ERROR] �޷�Զ�̷���Tomcat JMX " + tc.getIpAddress());
			}

			if (null == connector) {
				linkFlag = 0;
				rsHt.put("ping", "100");
			} else {
				rsHt.put("ping", "100");
				MBeanServerConnection mbsc = connector.getMBeanServerConnection();

				// ����Ŀ¼
				ObjectName dirObjName = new ObjectName("Catalina:type=Engine");
				String baseDir = null;
				baseDir = mbsc.getAttribute(dirObjName, "baseDir").toString();
				rsHt.put("baseDir", baseDir);

				// tomcat����
				ObjectName nameObjName = new ObjectName("Catalina:type=Server");
				String serverInfo = null;
				serverInfo = mbsc.getAttribute(nameObjName, "serverInfo").toString();
				rsHt.put("serverInfo", serverInfo);

				// JVM�汾
				ObjectName nameVObjName = new ObjectName("JMImplementation:type=MBeanServerDelegate");
				String implementationVersion = null;
				implementationVersion = mbsc.getAttribute(nameVObjName, "ImplementationVersion").toString();
				rsHt.put("implementationVersion", implementationVersion);

				// �߳���
				Set MBeanset = mbsc.queryMBeans(null, null);
				Iterator MBeansetIterator = MBeanset.iterator();
				// ----http---
				String httpName = null;
				String httpCurrentThreadCount = null;
				String httpCurrentThreadsBusy = null;
				String httpMaxThreads = null;
				String httpMaxTime = null;
				String httpProcessingTime = null;
				String httpRequestCount = null;
				String httpErrorCount = null;
				String httpBytesReceived = null;
				String httpBytesSent = null;

				// ----ajp---
				String ajpName = null;
				String ajpCurrentThreadCount = null;
				String ajpCurrentThreadsBusy = null;
				String ajpMaxThreads = null;
				String ajpMaxTime = null;
				String ajpProcessingTime = null;
				String ajpRequestCount = null;
				String ajpErrorCount = null;
				String ajpBytesReceived = null;
				String ajpBytesSent = null;

				while (MBeansetIterator.hasNext()) {
					ObjectInstance objectInstance = (ObjectInstance) MBeansetIterator.next();
					ObjectName objectName = objectInstance.getObjectName();
					String canonicalName = objectName.getCanonicalName();
					if (canonicalName.startsWith("Catalina:name=http") && canonicalName.contains("type=ThreadPool")) {
						ObjectName ob = new ObjectName(canonicalName);
						httpName = mbsc.getAttribute(ob, "name").toString();
						httpCurrentThreadCount = mbsc.getAttribute(ob, "currentThreadCount").toString();
						httpCurrentThreadsBusy = mbsc.getAttribute(ob, "currentThreadsBusy").toString();
						httpMaxThreads = mbsc.getAttribute(ob, "maxThreads").toString();
						canonicalName = canonicalName.replace("ThreadPool", "GlobalRequestProcessor");
						ob = new ObjectName(canonicalName);
						httpMaxTime = mbsc.getAttribute(ob, "maxTime").toString();
						httpProcessingTime = mbsc.getAttribute(ob, "processingTime").toString();
						httpRequestCount = mbsc.getAttribute(ob, "requestCount").toString();
						httpErrorCount = mbsc.getAttribute(ob, "errorCount").toString();
						httpBytesReceived = mbsc.getAttribute(ob, "bytesReceived").toString();
						httpBytesSent = mbsc.getAttribute(ob, "bytesSent").toString();
						rsHt.put("httpName", httpName);
						rsHt.put("httpCurrentThreadCount", httpCurrentThreadCount);
						rsHt.put("httpCurrentThreadsBusy", httpCurrentThreadsBusy);
						rsHt.put("httpMaxThreads", httpMaxThreads);
						rsHt.put("httpMaxTime", httpMaxTime);
						rsHt.put("httpProcessingTime", httpProcessingTime);
						rsHt.put("httpRequestCount", httpRequestCount);
						rsHt.put("httpErrorCount", httpErrorCount);
						rsHt.put("httpBytesReceived", httpBytesReceived);
						rsHt.put("httpBytesSent", httpBytesSent);

					} else if (canonicalName.startsWith("Catalina:name=ajp") && canonicalName.contains("type=ThreadPool")) {
						ObjectName ob = new ObjectName(canonicalName);
						ajpName = mbsc.getAttribute(ob, "name").toString();
						ajpCurrentThreadCount = mbsc.getAttribute(ob, "currentThreadCount").toString();
						ajpCurrentThreadsBusy = mbsc.getAttribute(ob, "currentThreadsBusy").toString();
						ajpMaxThreads = mbsc.getAttribute(ob, "maxThreads").toString();
						canonicalName = canonicalName.replace("ThreadPool", "GlobalRequestProcessor");
						ob = new ObjectName(canonicalName);
						ajpMaxTime = mbsc.getAttribute(ob, "maxTime").toString();
						ajpProcessingTime = mbsc.getAttribute(ob, "processingTime").toString();
						ajpRequestCount = mbsc.getAttribute(ob, "requestCount").toString();
						ajpErrorCount = mbsc.getAttribute(ob, "errorCount").toString();
						ajpBytesReceived = mbsc.getAttribute(ob, "bytesReceived").toString();
						ajpBytesSent = mbsc.getAttribute(ob, "bytesSent").toString();
						rsHt.put("ajpName", ajpName);
						rsHt.put("ajpCurrentThreadCount", ajpCurrentThreadCount);
						rsHt.put("ajpCurrentThreadsBusy", ajpCurrentThreadsBusy);
						rsHt.put("ajpMaxThreads", ajpMaxThreads);
						rsHt.put("ajpMaxTime", ajpMaxTime);
						rsHt.put("ajpProcessingTime", ajpProcessingTime);
						rsHt.put("ajpRequestCount", ajpRequestCount);
						rsHt.put("ajpErrorCount", ajpErrorCount);
						rsHt.put("ajpBytesReceived", ajpBytesReceived);
						rsHt.put("ajpBytesSent", ajpBytesSent);
					}
				}

				// ��ȡϵͳ�����Ϣ
				String operatingSystemName = null;// ϵͳ����
				long processCpuTime = 0;// ����cpuʱ��
				String arch = null;// ��ϵ�ṹ
				String availableProcessors = null;// ��������Ŀ
				String totalPhysicalMemorySize = null;// �ܵ������ڴ�
				String totalSwapSpaceSize = null;// �ܵĽ����ռ�
				String freePhysicalMemorySize = null;// ���е������ڴ�
				String freeSwapSpaceSize = null;// ���еĽ����ڴ�
				String committedVirtualMemorySize = null;// ����������ڴ�
				ObjectName opObjName = new ObjectName("java.lang:type=OperatingSystem");
				operatingSystemName = mbsc.getAttribute(opObjName, "Name").toString() + " " + mbsc.getAttribute(opObjName, "Version").toString();
				processCpuTime = (Long) mbsc.getAttribute(opObjName, "ProcessCpuTime");
				arch = mbsc.getAttribute(opObjName, "Arch").toString();
				availableProcessors = mbsc.getAttribute(opObjName, "AvailableProcessors").toString();
				totalPhysicalMemorySize = mbsc.getAttribute(opObjName, "TotalPhysicalMemorySize").toString();
				totalSwapSpaceSize = mbsc.getAttribute(opObjName, "TotalSwapSpaceSize").toString();
				freePhysicalMemorySize = mbsc.getAttribute(opObjName, "FreePhysicalMemorySize").toString();
				freeSwapSpaceSize = mbsc.getAttribute(opObjName, "FreeSwapSpaceSize").toString();
				committedVirtualMemorySize = mbsc.getAttribute(opObjName, "CommittedVirtualMemorySize").toString();
				rsHt.put("operatingSystemName", operatingSystemName);
				rsHt.put("processCpuTime", df.format((processCpuTime * 1.0 / 1000 / 1000 / 1000)));
				rsHt.put("arch", arch);
				rsHt.put("availableProcessors", availableProcessors);
				rsHt.put("totalPhysicalMemorySize", totalPhysicalMemorySize);
				rsHt.put("totalSwapSpaceSize", totalSwapSpaceSize);
				rsHt.put("freePhysicalMemorySize", freePhysicalMemorySize);
				rsHt.put("freeSwapSpaceSize", freeSwapSpaceSize);
				rsHt.put("committedVirtualMemorySize", committedVirtualMemorySize);

				// JVM���в���
				String vmVendor = null;// ����
				String vmNameVer = null;// ������ƺͰ汾
				String startTime = null;// ����ʱ��
				String upTime = null;// ����ʱ��
				String bootClassPath = null;// ������·��
				String classPath = null;// ��·��
				String libraryPath = null;// ��·��
				String[] InputArguments = null;// VM����
				ObjectName runtimeObjName = new ObjectName("java.lang:type=Runtime");
				vmVendor = mbsc.getAttribute(runtimeObjName, "VmVendor").toString();
				vmNameVer = mbsc.getAttribute(runtimeObjName, "VmName").toString();
				startTime = sdf.format(new Date((Long) mbsc.getAttribute(runtimeObjName, "StartTime")));
				upTime = this.formatTimeSpan((Long) mbsc.getAttribute(runtimeObjName, "Uptime"));
				bootClassPath = mbsc.getAttribute(runtimeObjName, "BootClassPath").toString();
				classPath = mbsc.getAttribute(runtimeObjName, "ClassPath").toString();
				libraryPath = mbsc.getAttribute(runtimeObjName, "LibraryPath").toString();
				InputArguments = (String[]) mbsc.getAttribute(runtimeObjName, "InputArguments");
				rsHt.put("vmVendor", vmVendor);
				rsHt.put("vmNameVer", vmNameVer);
				rsHt.put("startTime", startTime);
				rsHt.put("upTime", upTime);
				rsHt.put("bootClassPath", bootClassPath);
				rsHt.put("classPath", classPath);
				rsHt.put("libraryPath", libraryPath);
				rsHt.put("InputArguments", InputArguments);

				// JVM�ڴ�ʹ�����
				double heapPercent = 0;
				ObjectName heapObjName = new ObjectName("java.lang:type=Memory");
				MemoryUsage heapMemoryUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(heapObjName, "HeapMemoryUsage"));
				long heapMaxMemory = heapMemoryUsage.getMax();// �����
				long heapCommitMemory = heapMemoryUsage.getCommitted();// �ѵ�ǰ����
				long heapUsedMemory = heapMemoryUsage.getUsed();// ��ʹ��
				heapPercent = (double) heapUsedMemory * 100 / heapCommitMemory;// ��ʹ����
				rsHt.put("heapPercent", df.format(heapPercent));
				rsHt.put("heapMaxMemory", heapMaxMemory);
				rsHt.put("heapCommitMemory", heapCommitMemory);
				rsHt.put("heapUsedMemory", heapUsedMemory);

				double nonHeapPercent = 0;
				MemoryUsage nonheapMemoryUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(heapObjName, "NonHeapMemoryUsage"));
				long nonHeapMaxMemory = nonheapMemoryUsage.getMax();// �Ƕ����
				long nonHeapcommitMemory = nonheapMemoryUsage.getCommitted();// �Ƕѵ�ǰ����
				long nonHeapUsedMemory = heapMemoryUsage.getUsed();// �Ƕ�ʹ��
				nonHeapPercent = (double) nonHeapUsedMemory * 100 / nonHeapcommitMemory;// �Ƕ�ʹ����
				rsHt.put("nonHeapPercent", df.format(nonHeapPercent));
				rsHt.put("nonHeapMaxMemory", nonHeapMaxMemory);
				rsHt.put("nonHeapcommitMemory", nonHeapcommitMemory);
				rsHt.put("nonHeapUsedMemory", nonHeapUsedMemory);

				ObjectName permObjName = null;
				MemoryUsage permGenUsage = null;
				double permGenPercent = 0;

				try {
					permObjName = new ObjectName("java.lang:type=MemoryPool,name=Perm Gen");
					permGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(permObjName, "Usage"));
				} catch (Exception e) {
					SysLogger.info("###### ��Perm Gen�ڵ�");
				}

				if (null == permGenUsage) {
					try {
						permObjName = new ObjectName("java.lang:type=MemoryPool,name=PS Perm Gen");
						permGenUsage = MemoryUsage.from((CompositeDataSupport) mbsc.getAttribute(permObjName, "Usage"));
					} catch (Exception e) {
						SysLogger.info("###### ��PS Perm Gen�ڵ�");
					}

				}
				long permGenMaxMemory = permGenUsage.getMax();// �־ö����
				long permGenCommittedMemory = permGenUsage.getCommitted();// �־öѴ�С
				long permGenUsedMemory = heapMemoryUsage.getUsed();// �־ö�ʹ��
				permGenPercent = (double) permGenUsedMemory * 100 / permGenCommittedMemory;// �־ö�ʹ����
				rsHt.put("permGenPercent", df.format(permGenPercent));
				rsHt.put("permGenMaxMemory", permGenMaxMemory);
				rsHt.put("permGenCommittedMemory", permGenCommittedMemory);
				rsHt.put("permGenUsedMemory", permGenUsedMemory);

				// JVM �߳���Ϣ
				String ThreadCount = null;// ��߳�
				String PeakThreadCount = null;// ��ֵ
				String DaemonThreadCount = null;// �ػ��߳�
				String TotalStartedThreadCount = null;// �Ѿ��������߳�����
				ObjectName threadObjName = new ObjectName("java.lang:type=Threading");
				ThreadCount = mbsc.getAttribute(threadObjName, "ThreadCount").toString();
				PeakThreadCount = mbsc.getAttribute(threadObjName, "PeakThreadCount").toString();
				DaemonThreadCount = mbsc.getAttribute(threadObjName, "DaemonThreadCount").toString();
				TotalStartedThreadCount = mbsc.getAttribute(threadObjName, "TotalStartedThreadCount").toString();
				rsHt.put("ThreadCount", ThreadCount);
				rsHt.put("PeakThreadCount", PeakThreadCount);
				rsHt.put("DaemonThreadCount", DaemonThreadCount);
				rsHt.put("TotalStartedThreadCount", TotalStartedThreadCount);

				// �����
				String loadedClassCount = null;// ��ǰװ�ص�����
				String totalLoadedClassCount = null;// ��װ��������
				String unloadedClassCount = null;// �Ѿ�ж��������
				ObjectName classObjName = new ObjectName("java.lang:type=ClassLoading");
				loadedClassCount = mbsc.getAttribute(classObjName, "LoadedClassCount").toString();
				totalLoadedClassCount = mbsc.getAttribute(classObjName, "TotalLoadedClassCount").toString();
				unloadedClassCount = mbsc.getAttribute(classObjName, "UnloadedClassCount").toString();
				rsHt.put("loadedClassCount", loadedClassCount);
				rsHt.put("totalLoadedClassCount", totalLoadedClassCount);
				rsHt.put("unloadedClassCount", unloadedClassCount);

				// �����ռ���

				Vector collectorVector = new Vector();
				ObjectName collectorObjName = new ObjectName("java.lang:type=GarbageCollector,*");
				Set<ObjectName> collectorSet = mbsc.queryNames(collectorObjName, null);
				String rs = null;
				for (ObjectName obj : collectorSet) {
					ObjectName objName = new ObjectName(obj.getCanonicalName());
					rs = "Name='" + obj.getKeyProperty("name") + "',Collections=" + mbsc.getAttribute(objName, "CollectionCount") + ",Time Spent='" + mbsc.getAttribute(objName, "CollectionTime") + " ms'";
					collectorVector.add(rs);
				}
				rsHt.put("collector", collectorVector);

				// Session
				Vector managerVector = new Vector();
				String temp = new String();
				ObjectName managerObjName = new ObjectName("Catalina:type=Manager,*");
				Set<ObjectName> s = mbsc.queryNames(managerObjName, null);
				for (ObjectName obj : s) {
					ObjectName objname = new ObjectName(obj.getCanonicalName());
					temp = obj.getKeyProperty("path") + ":" + mbsc.getAttribute(objname, "maxActiveSessions") // ���Ự��
							+ ":" + mbsc.getAttribute(objname, "activeSessions") // ��Ự��
							+ ":" + mbsc.getAttribute(objname, "sessionCounter");// �Ự��
					managerVector.add(temp);
				}
				rsHt.put("manager", managerVector);
			}

			Pingcollectdata hostdata = null;
			if (null != rsHt && null != rsHt.get("heapPercent")) {
				hostdata = new Pingcollectdata();
				hostdata.setIpaddress(tc.getIpAddress());
				Calendar date = Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("tomcat_jvm");
				hostdata.setEntity("Utilization");
				hostdata.setSubentity("jvm_utilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("%");
				hostdata.setThevalue((String) rsHt.get("heapPercent"));
				try {
					Hashtable sendeddata = ShareData.getSendeddata();
					TomcatDao tomcatdao = new TomcatDao();
					tomcatdao.createHostData(hostdata);
					if (sendeddata.containsKey("tomcat" + ":" + tc.getIpAddress()))
						sendeddata.remove("tomcat" + ":" + tc.getIpAddress());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			hostdata = new Pingcollectdata();
			hostdata.setIpaddress(tc.getIpAddress());
			Calendar date = Calendar.getInstance();
			hostdata.setCollecttime(date);
			hostdata.setCategory("TomcatPing");
			hostdata.setEntity("Utilization");
			hostdata.setSubentity("ConnectUtilization");
			hostdata.setRestype("dynamic");
			hostdata.setUnit("%");
			if (1 == linkFlag) {
				hostdata.setThevalue("100");
			} else {
				hostdata.setThevalue("0");
			}
			TomcatDao tomcatdao = new TomcatDao();
			try {
				tomcatdao.createHostData(hostdata);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				tomcatdao.close();
			}
			rsHt.put("ping", hostdata);
			rsHt.put("alias", node.getAlias());
			ShareData.setTomcatdata(node.getIpAddress() + ":" + tomcatIndicators.getNodeid(), rsHt);

			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
			updateTomcatData(nodeDTO, rsHt);
			rsHt = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	private void updateTomcatData(NodeDTO nodeDTO, Hashtable hashtable) {
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List list = alarmIndicatorsUtil.getAlarmIndicatorsForNode(nodeDTO.getId() + "", nodeDTO.getType(), nodeDTO.getSubtype());
		if (list == null || list.size() == 0) {
			return;
		}

		CheckEventUtil checkEventUtil = new CheckEventUtil();
		for (int i = 0; i < list.size(); i++) {
			try {
				AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list.get(i);
				if ("ping".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
					Pingcollectdata pingEntity = (Pingcollectdata) hashtable.get("ping");
					if (pingEntity != null && !"".equals(pingEntity)) {
						checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, pingEntity.getThevalue());
					}
				} else if ("jvm".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
					String jvm = (String) hashtable.get("heapPercent");
					if (jvm != null && !"".equals(jvm)) {
						checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, jvm);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void CreateResultTosql(Hashtable hash, int nodeid) {
		TomcatPreDao dao = new TomcatPreDao();
		try {
			if (hash != null) {
				String result = (String) hash.get("portsum1");
				if (result != null) {
					String[] portsum = result.split(",");
					TomcatPre vo = new TomcatPre();
					vo.setNodeid(nodeid);
					vo.setMaxThread(portsum[0].split(":")[1]);
					vo.setCurCount(portsum[1].split(":")[1]);
					vo.setCurThBusy(portsum[2].split(":")[1]);
					vo.setMaxSThread(portsum[3].split(":")[1]);
					vo.setMaxProTime(portsum[4].split(":")[1]);
					vo.setProTime(portsum[5].split(":")[1]);
					vo.setRequestCount(portsum[6].split(":")[1]);
					vo.setErrorCount(portsum[7].split(":")[1]);
					vo.setBytesReceived(portsum[8].split(":")[1]);
					vo.setBytesSent(portsum[9].split(":")[1]);
					dao.save(vo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String formatTimeSpan(long span) {
		long minseconds = span % 1000;

		span = span / 1000;
		long seconds = span % 60;

		span = span / 60;
		long mins = span % 60;

		span = span / 60;
		long hours = span % 24;

		span = span / 24;
		long days = span;
		return (new Formatter()).format("%1$d�� %2$02d:%3$02d:%4$02d.%5$03d", days, hours, mins, seconds, minseconds).toString();
	}

}