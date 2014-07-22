package com.afunms.vmware.vim25.common;

import static org.junit.Assert.fail;
import com.afunms.vmware.vim25.constants.VIMConstants;
import com.afunms.vmware.vim25.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.AlarmInfo;
import com.vmware.vim25.ManagedObjectReference;

public class VIMMgrTest implements VIMConstants {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(VIMMgrTest.class);

	String username = "administrator";

	// // 公司
	 String url = "https://10.10.152.14/sdk";
	 String password = "123456";

	// 山西移动
//	String url = "https://10.210.92.11/sdk";
//	// String password = "www.123qwe.com";
//	String password = "Cloud.sxyd2012";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

//	@Test
//	@SuppressWarnings("unchecked")
	public void testSyncVIMObjs() {

		Map<String, Object> resultMap = VIMMgr.syncVIMObjs(url, username,
				password);
		if (Util.normalizeObject(resultMap.get(INFO_STATE)).toString().equals(
				STATE_OK)) {

			ArrayList<HashMap<String, Object>> list = null;

			String type = SYNC_DC;
			LOGGER.info(type + "=" + resultMap.get(type));
			LOGGER
					.info("\n-----------------------------------------------------------------\n");

			type = SYNC_CR;
			LOGGER.info(type + "=" + resultMap.get(type));
			LOGGER
					.info("\n-----------------------------------------------------------------\n");

			type = SYNC_RP;
			list = (ArrayList<HashMap<String, Object>>) resultMap.get(type);
			for (HashMap<String, Object> obj : list) {
				LOGGER.info(type + "=" + obj);
			}
			LOGGER
					.info("\n-----------------------------------------------------------------\n");

			type = SYNC_DS;
			list = (ArrayList<HashMap<String, Object>>) resultMap.get(type);
			for (HashMap<String, Object> obj : list) {
				LOGGER.info(type + "=" + obj);
			}
			LOGGER
					.info("\n-----------------------------------------------------------------\n");

			type = SYNC_HO;
			list = (ArrayList<HashMap<String, Object>>) resultMap.get(type);
			for (HashMap<String, Object> obj : list) {
				LOGGER.info(type + "=" + obj);
			}
			LOGGER
					.info("\n-----------------------------------------------------------------\n");

			type = SYNC_VM;
			list = (ArrayList<HashMap<String, Object>>) resultMap.get(type);
			for (HashMap<String, Object> obj : list) {
				LOGGER.info(type + "=" + obj);
			}
			LOGGER
					.info("\n-----------------------------------------------------------------\n");

			type = SYNC_TEMPLATE;
			list = (ArrayList<HashMap<String, Object>>) resultMap.get(type);
			for (HashMap<String, Object> obj : list) {
				LOGGER.info(type + "=" + obj);
			}
			LOGGER
					.info("\n-----------------------------------------------------------------\n");

			type = SYNC_OTHER;
			LOGGER.info(type + "=" + resultMap.get(type));
		}
	}

	// @Test
	public void testGetAbout() {
		// 测试成功
		VIMMgr.getAbout(url, username, password);
	}

	// @Test
	public void testGetEvents() throws Exception {
		// 测试成功
		String vtype = VIMConstants.RESOURCE_VM;
		// oracle_CentOS5.7_152.23
		String vid = "vm-17";
		int timeUnit = Calendar.DAY_OF_YEAR;
		// 时间不起作用
		int timeDiff = 0;
		printResultList(vid, VIMMgr.getEvents(url, username, password, vtype,
				vid, timeUnit, timeDiff));

		vtype = VIMConstants.RESOURCE_HO;
		// 10.10.152.15
		vid = "host-12";
		timeUnit = Calendar.DAY_OF_YEAR;
		timeDiff = 5;
		printResultList(vid, VIMMgr.getEvents(url, username, password, vtype,
				vid, timeUnit, timeDiff));

		vtype = VIMConstants.RESOURCE_RP;
		// testRP
		vid = "resgroup-131";
		timeUnit = Calendar.WEEK_OF_MONTH;
		timeDiff = 1;
		printResultList(vid, VIMMgr.getEvents(url, username, password, vtype,
				vid, timeUnit, timeDiff));
	}

//	@SuppressWarnings("unchecked")
	private void printResultList(String vid, Map<String, Object> resultMap)
			throws Exception {
		LOGGER.info("-----------------------------------------" + vid);
		ArrayList<HashMap<String, String>> resultList = (ArrayList<HashMap<String, String>>) resultMap
				.get(VIMConstants.INFO_RESULT);
		for (HashMap<String, String> resultEvt : resultList) {
			LOGGER.info("==========" + resultEvt);
			String desc = resultEvt.get(VIMConstants.EVENT_DESC);
			LOGGER.info("==========desc=" + desc);
		}
	}

	// @Test
	public void testGetTasks() throws Exception {
		// 测试成功
		String vtype = VIMConstants.RESOURCE_VM;
		// oracle_CentOS5.7_152.23
		String vid = "vm-17";
		printResultList(vid, VIMMgr.getTasks(url, username, password, vtype,
				vid));

		vtype = VIMConstants.RESOURCE_HO;
		// 10.10.152.15
		vid = "host-12";
		printResultList(vid, VIMMgr.getTasks(url, username, password, vtype,
				vid));

		vtype = VIMConstants.RESOURCE_RP;
		// testRP
		vid = "resgroup-131";
		printResultList(vid, VIMMgr.getTasks(url, username, password, vtype,
				vid));
	}

	// @Test
	public void testPrintAllAlarms() {
		// 测试成功
		try {
			ExtendedAppUtil ecb = VIMMgr.getECB(url, username, password);

			ManagedObjectReference almMgr = ecb.getServiceConnection3()
					.getServiceContent().getAlarmManager();

			ManagedObjectReference[] almList = ecb.getServiceConnection3()
					.getService().getAlarm(almMgr, null);
			if (almList != null) {
				LOGGER.info("--------------------------------------getAlarm");
				for (ManagedObjectReference almMor : almList) {
					// http://pubs.vmware.com/vsphere-50/topic/com.vmware.wssdk.apiref.doc_50/vim.alarm.Alarm.html
					AlarmInfo almInfo = (AlarmInfo) VIMMgr.getDynamicProperty(
							ecb, almMor, VIMConstants.DYNAMICPROPERTY_INFO);

					// CustomFieldDef[] availableFields = (CustomFieldDef[])
					// VIMMgr.getDynamicProperty(ecb,
					// almMor, "availableField");

					// CustomFieldValue[] values = (CustomFieldValue[])
					// VIMMgr
					// .getDynamicProperty(ecb, almMor, "value");

					LOGGER.info("--------------------------------------");
					LOGGER.info("=========getActionFrequency="
							+ almInfo.getActionFrequency());
					LOGGER.info("=========getAlarm=" + almInfo.getAlarm());
					LOGGER.info("=========getCreationEventId="
							+ almInfo.getCreationEventId());
					LOGGER.info("=========getDescription="
							+ almInfo.getDescription());
					if (almInfo.getDynamicProperty() != null) {
						LOGGER.info("=========getDynamicProperty="
								+ almInfo.getDynamicProperty().length);
					}
					LOGGER.info("=========getDynamicType="
							+ almInfo.getDynamicType());
					LOGGER.info("=========getEntity=" + almInfo.getEntity());
					if ((almInfo.getExpression() != null)
							&& (almInfo.getExpression().getDynamicProperty() != null)) {
						LOGGER
								.info("=========getExpression="
										+ almInfo.getExpression()
												.getDynamicProperty().length);
					}
					LOGGER.info("=========getKey=" + almInfo.getKey());
					LOGGER.info("=========getLastModifiedTime="
							+ Util.getDBDateTime(almInfo.getLastModifiedTime()
									.getTimeInMillis()));
					LOGGER.info("=========getLastModifiedUser="
							+ almInfo.getLastModifiedUser());
					LOGGER.info("=========getName=" + almInfo.getName());
					LOGGER.info("=========getSetting--getReportingFrequency="
							+ almInfo.getSetting().getReportingFrequency());
					LOGGER.info("=========getSetting--getToleranceRange="
							+ almInfo.getSetting().getToleranceRange());
					LOGGER.info("=========getSystemName="
							+ almInfo.getSystemName());
				}
			}
		} catch (Exception e) {
			LOGGER.error("testPrintAllAlarms error, ", e);
		}
	}

	// @Test
	public void testGetAlarms() throws Exception {
		// 测试成功
		String vtype = VIMConstants.RESOURCE_VM;
		// CentOS5.7,模板
		String vid = "vm-19";
		printResultList(vid, VIMMgr.getAlarms(url, username, password, vtype,
				vid));

		vtype = VIMConstants.RESOURCE_VM;
		// Win2003_itims_152.114
		vid = "vm-83";
		printResultList(vid, VIMMgr.getAlarms(url, username, password, vtype,
				vid));

		// 接口无法获取虚拟机意外的对象的告警
		vtype = VIMConstants.RESOURCE_HO;
		// 10.10.152.15
		vid = "host-12";
		printResultList(vid, VIMMgr.getAlarms(url, username, password, vtype,
				vid));
	}

//	 @Test
	public void testGetPerformances() {
		// TODO
		String vid = "vm-33";
		String vtype = VIMConstants.RESOURCE_VM;
		String perfIntervalKey = VIMConstants.PERFINTERVAL_DAY;
		String perfCounterKeys[] = { "cpu.usage.average",
		"cpu.ready.summation", "mem.vmmemctl.average",
		"mem.swapoutRate.average", "mem.swapinRate.average",
		"cpu.usagemhz.average", "mem.usage.average",
		"disk.usage.average", "net.usage.average" };

		Calendar endTime = Calendar.getInstance();
		 
		LOGGER.info(VIMMgr.getPerformances(url, username, password, vtype, vid,
		perfIntervalKey, perfCounterKeys, endTime));
	}

}
