package com.afunms.vmware.vim25.common;

import com.afunms.vmware.vim25.cache.VIMCache;
import com.afunms.vmware.vim25.constants.VIMConstants;
import com.afunms.vmware.vim25.util.Util;
import com.afunms.vmware.vim25.vo.ComparableHashMap;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.AboutInfo;
import com.vmware.vim25.AlarmInfo;
import com.vmware.vim25.AlarmState;
import com.vmware.vim25.ComputeResourceSummary;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.Event;
import com.vmware.vim25.EventFilterSpec;
import com.vmware.vim25.EventFilterSpecByEntity;
import com.vmware.vim25.EventFilterSpecByTime;
import com.vmware.vim25.EventFilterSpecRecursionOption;
import com.vmware.vim25.HostHardwareInfo;
import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.HostRuntimeInfo;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.ObjectSpec;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfFormat;
import com.vmware.vim25.PerfInterval;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PropertyFilterSpec;
import com.vmware.vim25.PropertySpec;
import com.vmware.vim25.StoragePodSummary;
import com.vmware.vim25.TaskEvent;
import com.vmware.vim25.TaskFilterSpec;
import com.vmware.vim25.TaskFilterSpecByEntity;
import com.vmware.vim25.TaskFilterSpecRecursionOption;
import com.vmware.vim25.TaskInfo;
import com.vmware.vim25.TaskReasonAlarm;
import com.vmware.vim25.TaskReasonSchedule;
import com.vmware.vim25.TaskReasonSystem;
import com.vmware.vim25.TaskReasonUser;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineStorageInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.VirtualMachineUsageOnDatastore;

/**
 * ���ù���,ʵ��VMWare�еĹ����Ӧ��һЩ�����ķ���
 * 
 * @author LXL
 * 
 */

public class VIMMgr extends VIMConnection implements VIMConstants {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(VIMMgr.class);

	// С�����2λ�����ָ�ʽ
	private static final DecimalFormat BITS2_DF = new DecimalFormat("#.##");

	/**
	 * ��¼����״̬�ʹ�����Ϣ
	 * 
	 * @param resultMap
	 * @param state
	 * @param error
	 */
	protected static void recordResultMapError(
			HashMap<String, Object> resultMap, String error) {
		resultMap.put(INFO_STATE, STATE_ERROR);
		resultMap.put(INFO_ERROR, error);
		resultMap.put(INFO_PROGRESS, "");
		resultMap.put(INFO_RESULT, "");
	}

	/**
	 * ��¼��ȷ״̬
	 * 
	 * @param resultMap
	 */
	protected static void recordResultMapSuccess(
			HashMap<String, Object> resultMap) {
		resultMap.put(INFO_STATE, STATE_OK);
		resultMap.put(INFO_ERROR, "");
		resultMap.put(INFO_PROGRESS, "");
		resultMap.put(INFO_RESULT, "");
	}

	// /**
	// * ��¼״̬��,������Ϣ,����,���
	// *
	// * @param resultMap
	// * @param state
	// * @param error
	// */
	// private static void recordResultMap(HashMap<String, Object> resultMap,
	// String state, String error, String progress, String result) {
	// resultMap.put(INFO_STATE, state);
	// resultMap.put(INFO_ERROR, error);
	// resultMap.put(INFO_PROGRESS, progress);
	// resultMap.put(INFO_RESULT, result);
	// }

	/**
	 * �����������������Ϣ
	 * 
	 * @param resultMap
	 * @param e
	 */
	protected static void recordResultMapException(
			HashMap<String, Object> resultMap, Exception e) {
		resultMap.put(INFO_STATE, STATE_ERROR);
		resultMap.put(INFO_ERROR, ERROR_UNKNOWN + e.getMessage());
		resultMap.put(INFO_PROGRESS, "");
		resultMap.put(INFO_RESULT, "");
	}

	/**
	 * ִ������
	 * 
	 * @param ecb
	 * @param taskMor
	 * @param resultMap
	 */
	protected static void execTaskAndRecordResultMap(ExtendedAppUtil ecb,
			ManagedObjectReference taskMor, HashMap<String, Object> resultMap)
			throws Exception {
		// ִ������
		ecb.getServiceUtil3().waitForTask(taskMor);

		// ��ȡ����Ķ�̬����
		TaskInfo taskInfo = (TaskInfo) getDynamicProperty(ecb, taskMor,
				DYNAMICPROPERTY_INFO);

		// ��¼����ִ�����
		resultMap.put(INFO_STATE, taskInfo.getState().getValue());
		if (taskInfo.getError() != null) {
			resultMap
					.put(INFO_ERROR, taskInfo.getError().getLocalizedMessage());
		} else {
			resultMap.put(INFO_ERROR, "");
		}
		if (taskInfo.getProgress() != null) {
			resultMap.put(INFO_PROGRESS, taskInfo.getProgress().toString());
		} else {
			resultMap.put(INFO_PROGRESS, "0");
		}
		if (taskInfo.getResult() != null) {
			resultMap.put(INFO_RESULT, taskInfo.getResult().toString());
		} else {
			resultMap.put(INFO_RESULT, "");
		}
	}

	/**
	 * ��ȡ����Ķ����̬����<br>
	 * ��������vsphere-ws\java\Axis\com\vmware\apputils\vim25\ServiceUtil.java<br>
	 * ����ExtendedAppUtil����
	 * 
	 * @param ecb
	 * @param mor
	 * @param propertyNames
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unused")
	private static List<Object> getDynamicProperties(ExtendedAppUtil ecb,
			ManagedObjectReference mor, String[] propertyNames)
			throws Exception {

		// TODO ���ﻹ�Ƿ���һ������
		ObjectContent[] objContents = getObjectPropertiesEx(ecb, null, mor,
				propertyNames);

		ArrayList<Object> propertyValues = new ArrayList<Object>();

		if (objContents != null) {
			for (ObjectContent objContent : objContents) {
				DynamicProperty[] dynamicProperty = objContent.getPropSet();
				if (dynamicProperty != null) {
					/*
					 * Check the dynamic propery for ArrayOfXXX object
					 */
					Object dynamicPropertyVal = dynamicProperty[0].getVal();
					String dynamicPropertyName = dynamicPropertyVal.getClass()
							.getName();
					if (dynamicPropertyName.indexOf("ArrayOf") != -1) {
						String methodName = dynamicPropertyName.substring(
								dynamicPropertyName.indexOf("ArrayOf")
										+ "ArrayOf".length(),
								dynamicPropertyName.length());
						/*
						 * If object is ArrayOfXXX object, then get the XXXX[]
						 * by invoking getXXX() on the object. For Ex:
						 * ArrayOfManagedObjectReference
						 * .getManagedObjectReference() returns
						 * ManagedObjectReference[] array.
						 */
						if (methodExists(dynamicPropertyVal,
								"get" + methodName, null)) {
							methodName = "get" + methodName;
						} else {
							/*
							 * Construct methodName for ArrayOf primitive types
							 * Ex: For ArrayOfInt, methodName is get_int
							 */
							methodName = "get_" + methodName.toLowerCase();
						}
						Method getMorMethod = dynamicPropertyVal.getClass()
								.getDeclaredMethod(methodName, (Class[]) null);
						propertyValues.add(getMorMethod.invoke(
								dynamicPropertyVal, (Object[]) null));
					} else if (dynamicPropertyVal.getClass().isArray()) {
						/*
						 * Handle the case of an unwrapped array being
						 * deserialized.
						 */
						propertyValues.add(dynamicPropertyVal);
					} else {
						propertyValues.add(dynamicPropertyVal);
					}
				}
			}
		}
		return propertyValues;
	}

	/**
	 * getDynamicProperties������������<br>
	 * �޸���getObjectProperties����,
	 * 
	 * @param ecb
	 * @param collector
	 * @param mobj
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	private static ObjectContent[] getObjectPropertiesEx(ExtendedAppUtil ecb,
			ManagedObjectReference collector, ManagedObjectReference mobj,
			String[] properties) throws Exception {

		if (mobj == null) {
			return null;
		}

		ManagedObjectReference usecoll = collector;
		if (usecoll == null) {
			usecoll = ecb.getServiceConnection3().getPropCol();
		}

		PropertyFilterSpec[] propFilterSpecs = new PropertyFilterSpec[properties.length];

		for (int i = 0; i < properties.length; i++) {

			PropertyFilterSpec spec = new PropertyFilterSpec();
			spec.setPropSet(new PropertySpec[] { new PropertySpec() });
			spec.getPropSet(0).setAll(
					new Boolean(properties == null || properties.length == 0));
			spec.getPropSet(0).setType(mobj.getType());
			spec.getPropSet(0).setPathSet(new String[] { properties[i] });

			spec.setObjectSet(new ObjectSpec[] { new ObjectSpec() });
			spec.getObjectSet(0).setObj(mobj);
			spec.getObjectSet(0).setSkip(Boolean.FALSE);

			propFilterSpecs[i] = spec;
		}

		return ecb.getServiceConnection3().getService().retrieveProperties(
				usecoll, propFilterSpecs);
	}

	/**
	 * ��ȡ����Ķ�̬����<br>
	 * ��������vsphere-ws\java\Axis\com\vmware\apputils\vim25\ServiceUtil.java<br>
	 * ����ExtendedAppUtil����
	 * 
	 * @param ecb
	 * @param mor
	 * @param propertyName
	 * @return
	 * @throws Exception
	 */
	public static Object getDynamicProperty(ExtendedAppUtil ecb,
			ManagedObjectReference mor, String propertyName) throws Exception {
		ObjectContent[] objContent = getObjectProperties(ecb, null, mor,
				new String[] { propertyName });

		Object propertyValue = null;
		if (objContent != null) {
			DynamicProperty[] dynamicProperty = objContent[0].getPropSet();
			if (dynamicProperty != null) {
				/*
				 * Check the dynamic propery for ArrayOfXXX object
				 */
				Object dynamicPropertyVal = dynamicProperty[0].getVal();
				String dynamicPropertyName = dynamicPropertyVal.getClass()
						.getName();
				if (dynamicPropertyName.indexOf("ArrayOf") != -1) {
					String methodName = dynamicPropertyName.substring(
							dynamicPropertyName.indexOf("ArrayOf")
									+ "ArrayOf".length(), dynamicPropertyName
									.length());
					/*
					 * If object is ArrayOfXXX object, then get the XXXX[] by
					 * invoking getXXX() on the object. For Ex:
					 * ArrayOfManagedObjectReference.getManagedObjectReference()
					 * returns ManagedObjectReference[] array.
					 */
					if (methodExists(dynamicPropertyVal, "get" + methodName,
							null)) {
						methodName = "get" + methodName;
					} else {
						/*
						 * Construct methodName for ArrayOf primitive types Ex:
						 * For ArrayOfInt, methodName is get_int
						 */
						methodName = "get_" + methodName.toLowerCase();
					}
					Method getMorMethod = dynamicPropertyVal.getClass()
							.getDeclaredMethod(methodName, (Class[]) null);
					propertyValue = getMorMethod.invoke(dynamicPropertyVal,
							(Object[]) null);
				} else if (dynamicPropertyVal.getClass().isArray()) {
					/*
					 * Handle the case of an unwrapped array being deserialized.
					 */
					propertyValue = dynamicPropertyVal;
				} else {
					propertyValue = dynamicPropertyVal;
				}
			}
		}
		return propertyValue;
	}

	/**
	 * getDynamicProperty������������<br>
	 * ��������vsphere-ws\java\Axis\com\vmware\apputils\vim25\ServiceUtil.java<br>
	 * ����ExtendedAppUtil����
	 * 
	 * @param ecb
	 * @param collector
	 * @param mobj
	 * @param properties
	 * @return
	 * @throws Exception
	 */
	private static ObjectContent[] getObjectProperties(ExtendedAppUtil ecb,
			ManagedObjectReference collector, ManagedObjectReference mobj,
			String[] properties) throws Exception {
		if (mobj == null) {
			return null;
		}

		ManagedObjectReference usecoll = collector;
		if (usecoll == null) {
			try
			{
				usecoll = ecb.getServiceConnection3().getPropCol();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		PropertyFilterSpec spec = new PropertyFilterSpec();
		spec.setPropSet(new PropertySpec[] { new PropertySpec() });
		spec.getPropSet(0).setAll(
				new Boolean(properties == null || properties.length == 0));
		spec.getPropSet(0).setType(mobj.getType());
		spec.getPropSet(0).setPathSet(properties);

		spec.setObjectSet(new ObjectSpec[] { new ObjectSpec() });
		spec.getObjectSet(0).setObj(mobj);
		spec.getObjectSet(0).setSkip(Boolean.FALSE);

		return ecb.getServiceConnection3().getService().retrieveProperties(
				usecoll, new PropertyFilterSpec[] { spec });
	}

	/**
	 * getDynamicProperty������������<br>
	 * ��������vsphere-ws\java\Axis\com\vmware\apputils\vim25\ServiceUtil.java<br>
	 * 
	 * @param obj
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static boolean methodExists(Object obj, String methodName,
			Class[] parameterTypes) {
		boolean exists = false;
		try {
			Method method = obj.getClass()
					.getMethod(methodName, parameterTypes);
			if (method != null) {
				exists = true;
			}
		} catch (Exception e) {
		}
		return exists;
	}

	/**
	 * ͬ���ӿ�,ͬ�������ݰ����������ġ�����Ⱥ������������Դ�ء�����������ݴ洢�ȣ��Լ�����֮��Ĺ�ϵ��<br>
	 * �˽ӿ��ݲ��漰�������νṹ<br>
	 * ���νṹչ����ʽĿǰ�Ȱ���VIM�ڲ��ṹչʾ�����˽ṹ��vCenter�����νṹ��ͬ,�����������Դ��ֻ�ǹ�����ϵ,û�и��ӹ�ϵ;
	 * �����������vm�ļ���,vm�ļ����������������ĵȣ�<br>
	 * <br>
	 * <br>
	 * �ⲿ�ڵ��÷���ʱӦ�����жϷ��ص�Map�е�info.state��ֵ,����Ӧ�ý�delete��SQL��insert��SQL����һ��������,
	 * ��ֹ���ַ��ص�MapΪ��, ȴ�����ݿ���е�����ɾ�������� <br>
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> syncVIMObjs(String url, String username,
			String password) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		LOGGER.info("syncVIMObjs, url='" + url + "', username='" + username
				+ "', password='" + password + "'");

		try {
			// ͬ����������
			// 1����������
			ArrayList<HashMap<String, Object>> dcList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(SYNC_DC, dcList);
			// 2���ƣ�Ⱥ��
			ArrayList<HashMap<String, Object>> crList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(SYNC_CR, crList);
			// 3����Դ��
			ArrayList<HashMap<String, Object>> rpList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(SYNC_RP, rpList);
			// 4���洢 �洢Ⱥ��
			ArrayList<HashMap<String, Object>> dsList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(SYNC_DS, dsList);
			// 5�������
			ArrayList<HashMap<String, Object>> hoList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(SYNC_HO, hoList);
			// 6�����
			ArrayList<HashMap<String, Object>> vmList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(RESOURCE_VM, vmList);
			ArrayList<HashMap<String, Object>> vmList1 = new ArrayList<HashMap<String, Object>>();
			// 7��ģ��
			ArrayList<HashMap<String, Object>> tempList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(SYNC_TEMPLATE, tempList);
			// ����,ϵͳ�޷�����Ķ���
			ArrayList<HashMap<String, Object>> otherList = new ArrayList<HashMap<String, Object>>();
			resultMap.put(SYNC_OTHER, otherList);

			// ���ݿ�������ϵ����(������������)
			// 1����������
			// 2���ƣ�Ⱥ�� 1
			// 3����Դ�� 2
			// 4���洢 �洢Ⱥ�� 2
			// 5������� 2
			// 6����� 2,3,4,5
			// 7��ģ�� 2,3,4,5

			ExtendedAppUtil ecb = getECB(url, username, password);

			// ע������������"--"��ͷ���ֶνӿ��޷��ṩ,��MISϵͳ����

			// ��ȡ���е���������
			ArrayList dcMorList = ecb.getServiceUtil3().getDecendentMoRefs(
					null, RESOURCE_DC);
			if (dcMorList != null) {
				LOGGER.info("syncVIMObjs, dcMorList='" + dcMorList + "'");

				int i=0;
				for (Object obj : dcMorList) {
					ManagedObjectReference dcMor = (ManagedObjectReference) obj;
					// ͬ���������Ķ���
					syncDatacenter(ecb, dcMor, dcList);

					// ����rp��cr�Ĺ�ϵ,Ϊ������ͨ��rp��ȡ��cr��id
					// key��rpid,value��crid
					HashMap<String, String> crRpMap = new HashMap<String, String>();
					// ��ȡȺ������������
					syncHostFolder(ecb, dcMor, crList, rpList, hoList,
							otherList, crRpMap);

					if(i == 0){
					// ��ȡvmFolder
						syncVMFolder(ecb, dcMor, vmList, tempList, rpList,
								otherList, crRpMap);
					}
					// �洢�ʹ洢Ⱥ��
					syncDatastoreFolderAndDatastore(ecb, dcMor, dsList,
							otherList);

					i++;
					// ���������»���networkFolder��networkδ����
				}
			}
			
			//�����������ȡ�������ظ�
//			if(vmList != null && vmList.size()>0){
//				for(int i=0;i<vmList.size()/dcMorList.size();i++){
//					vmList1.add(vmList.get(i));
//				}
//			}
//			resultMap.put(RESOURCE_VM, vmList1);
			
			
			// ��¼������ȷ�ı�ʶ
			recordResultMapSuccess(resultMap);
			LOGGER.error("#############ͬ���ӿڣ��ɼ���ϣ���####################");
		} catch (Exception e) {
			LOGGER.error("syncVIMObj error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ͬ���������Ķ���
	 * 
	 * @param ecb
	 * @param mor
	 * @param resultList
	 * @throws Exception
	 */
	private static void syncDatacenter(ExtendedAppUtil ecb,
			ManagedObjectReference mor,
			ArrayList<HashMap<String, Object>> resultList) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncDatacenter, mor='" + mor + "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		// �������ı��е��ֶ���
		// --1������ID

		// 2��������������
		map.put(SYNC_COMMON_NAME, getEntityName(ecb, mor));

		// --3����ע

		// 4��VMware��Ӧ����ԴID������VID
		map.put(SYNC_COMMON_VID, mor.get_value());

		resultList.add(map);
	}

	/**
	 * ��ȡ�ļ����µ�ʵ������
	 * 
	 * @param ecb
	 * @param mor
	 * @param folderName
	 * @return
	 */
	private static ManagedObjectReference[] getChildEntityFromFolder(
			ExtendedAppUtil ecb, ManagedObjectReference mor, String folderName)
			throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		ManagedObjectReference folderMor = (ManagedObjectReference) getDynamicProperty(
				ecb, mor, folderName);
		return (ManagedObjectReference[]) getDynamicProperty(ecb, folderMor,
				DYNAMICPROPERTY_CHILDENTITY);
	}

	/**
	 * ͬ����������vmFolder�µĶ���,
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param crList
	 * @param rpList
	 * @param hoList
	 * @param otherList
	 * @param crRpMap
	 * @throws Exception
	 */
	private static void syncHostFolder(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor,
			ArrayList<HashMap<String, Object>> crList,
			ArrayList<HashMap<String, Object>> rpList,
			ArrayList<HashMap<String, Object>> hoList,
			ArrayList<HashMap<String, Object>> otherList,
			HashMap<String, String> crRpMap) throws Exception {

		ManagedObjectReference[] hoMorList = getChildEntityFromFolder(ecb,
				dcMor, DYNAMICPROPERTY_HOSTFOLDER);
		if (hoMorList != null) {
			LOGGER.info("syncHostFolder, dcMor='" + dcMor + "', hoMorList='"
					+ hoMorList.length + "'");

			for (ManagedObjectReference hoMor : hoMorList) {
				if ((hoMor.getType().equals(RESOURCE_CCR))
						|| (hoMor.getType().equals(RESOURCE_CR))) {
					syncComputeResource(ecb, dcMor, hoMor, crList, rpList,
							hoList, otherList, crRpMap);
					// } else if (hoMor.getType().equals(RESOURCE_HO)) {
					// ��˾���ֳ���û���������������ֱ����ӵ�����������Ҳ�������,ϵͳ���Զ�����һ���������ͬ����Ⱥ��
					// ��Ⱥ��������Ϊdomain-s,һ���Ⱥ������Ϊdomain-c
					// syncHostSystem(ecb, dcMor, hoMor, hoList);
				} else {
					syncOtherMor(ecb, dcMor, hoMor, "syncHostFolder",
							DYNAMICPROPERTY_VMFOLDER, otherList);
				}
			}
		}
	}

	/**
	 * ͬ����Դ�ض���,����ResourcePool��VirtualApp
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param crMor
	 * @param mor
	 * @param resultList
	 * @param crRpMap
	 * @throws Exception
	 */
	private static void syncResourcePool(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference crMor,
			ManagedObjectReference mor,
			ArrayList<HashMap<String, Object>> resultList,
			HashMap<String, String> crRpMap) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncResourcePool, dcMor='" + dcMor + "', crMor='" + crMor
				+ "', mor='" + mor + "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		// ����
		ManagedObjectReference ownerMor = (ManagedObjectReference) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_OWNER);

		// ��Դ��
		ManagedObjectReference[] rpMorList = (ManagedObjectReference[]) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_RESOURCEPOOL);

		// ��Դ�ر��е��ֶ���
		// -- 1������ID

		// 2����Դ������
		map.put(SYNC_COMMON_NAME, getEntityName(ecb, mor));

		// 3�����صĽӿ�ID������VID
		map.put(SYNC_COMMON_VID, mor.get_value());

		// -- 4����Դ�ر�ע

		// ��������ID
		map.put(SYNC_COMMON_DCID, dcMor.get_value());

		// Ⱥ��ID
		map.put(SYNC_COMMON_CRID, ownerMor.get_value());

		// ������Ⱥ��,���������������ܻ�������ѭ��
		if (rpMorList != null) {
			for (ManagedObjectReference rpMor : rpMorList) {
				syncResourcePool(ecb, dcMor, crMor, rpMor, resultList, crRpMap);
			}
		}

		// ��¼cr��rp�Ĺ�ϵ
		// key��rpid,value��crid
		if (crMor != null) {
			crRpMap.put(mor.get_value(), crMor.get_value());
		}

		resultList.add(map);
	}

	/**
	 * ͬ��Ⱥ������
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param mor
	 * @param crList
	 * @param rpList
	 * @param hoList
	 * @param otherList
	 * @param crRpMap
	 * @throws Exception
	 */
	private static void syncComputeResource(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference mor,
			ArrayList<HashMap<String, Object>> crList,
			ArrayList<HashMap<String, Object>> rpList,
			ArrayList<HashMap<String, Object>> hoList,
			ArrayList<HashMap<String, Object>> otherList,
			HashMap<String, String> crRpMap) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncComputeResource, dcMor='" + dcMor + "', mor='" + mor
				+ "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		// ����Դ��
		ManagedObjectReference rpMor = (ManagedObjectReference) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_RESOURCEPOOL);

		// ��������
		ManagedObjectReference[] hoMorList = (ManagedObjectReference[]) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_HOST);

		// ���ݴ洢
		ManagedObjectReference[] dsMorList = (ManagedObjectReference[]) getDynamicProperty(
				ecb, dcMor, DYNAMICPROPERTY_DATASTORE);

		// ժҪ
		ComputeResourceSummary summary = (ComputeResourceSummary) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_SUMMARY);

		// �ƣ�Ⱥ�����е��ֶ���
		// --1������ID

		// 2��������
		map.put(SYNC_COMMON_NAME, getEntityName(ecb, mor));

		// --3����ע
		// --4��ʹ��״̬

		// 5�����صĽӿ�ID������VID
		map.put(SYNC_COMMON_VID, mor.get_value());

		// 6����������ID
		map.put(SYNC_COMMON_DCID, dcMor.get_value());

		// --7����ǰ�����������cpu�����ܺ�
		// ժҪ-->����-->��CPU��Դ,summary.getTotalCpu,��λMHz
		map.put(SYNC_CR_TOTALCPU, summary.getTotalCpu());

		// ժҪ-->����-->����������,summary.getNumCpuCores
		map.put(SYNC_CR_NUMCPUCORES, summary.getNumCpuCores());

		// --8����ǰ������������ڴ�����
		// ժҪ-->����-->���ڴ�,summary.getTotalMemory,��λ�ֽ�
		map.put(SYNC_CR_TOTALMEMORY, convertBytes2MBString(summary
				.getTotalMemory()));

		// --9����ǰ�������������������
		// ժҪ-->����-->�ܴ洢
		long totalds = 0;
		if (dsMorList != null) {
			for (ManagedObjectReference dsMor : dsMorList) {
				// �洢ժҪ
				DatastoreSummary dsSummary = (DatastoreSummary) getDynamicProperty(
						ecb, dsMor, DYNAMICPROPERTY_SUMMARY);
				totalds += dsSummary.getCapacity();
			}
		}
		map.put(SYNC_CR_TOTALDSSIZEMB, convertBytes2MBString(totalds));

		// ժҪ-->����-->������summary.getNumHosts(��Ч������summary.getNumEffectiveHosts)
		map.put(SYNC_CR_NUMHOSTS, summary.getNumHosts());

		crList.add(map);

		// �������Դ��
		syncResourcePool(ecb, dcMor, mor, rpMor, rpList, crRpMap);

		// ��������
		if (hoMorList != null) {
			for (ManagedObjectReference hoMor : hoMorList) {
				if (hoMor.getType().equals(RESOURCE_HO)) {
					syncHostSystem(ecb, dcMor, mor, hoMor, hoList);
				} else {
					syncOtherMor(ecb, dcMor, hoMor, "syncComputeResource",
							DYNAMICPROPERTY_HOST, otherList);
				}
			}
		}
	}

	/**
	 * ͬ����������
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param crMor
	 * @param mor
	 * @param resultList
	 * @throws Exception
	 */
	private static void syncHostSystem(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference crMor,
			ManagedObjectReference mor,
			ArrayList<HashMap<String, Object>> resultList) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncHostSystem, dcMor='" + dcMor + "', crMor='" + crMor
				+ "', mor='" + mor + "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		// ժҪ
		HostListSummary summary = (HostListSummary) getDynamicProperty(ecb,
				mor, DYNAMICPROPERTY_SUMMARY);

		// Ӳ��
		HostHardwareInfo hardware = (HostHardwareInfo) getDynamicProperty(ecb,
				mor, DYNAMICPROPERTY_HARDWARE);

		// ����
		HostRuntimeInfo runtime = (HostRuntimeInfo) getDynamicProperty(ecb,
				mor, DYNAMICPROPERTY_RUNTIME);

		// ��������е��ֶ�
		// --1������ID

		// 2��������������
		map.put(SYNC_COMMON_NAME, summary.getConfig().getName());

		// 3�����������ͺ�
		// ժҪ-->����-->�ͺ�,����:PowerEdge R710
		// ժҪ-->����-->������,����:Dell Inc.,hardware.getSystemInfo().getVendor
		map.put(SYNC_HO_MODEL, hardware.getSystemInfo().getModel());

		// --4����������
		// --5��ʹ������
		// --6���豸����

		// XXX 7��IP��ַ��������û�У���ȷ���Ƿ���ȡ����
		// summary.getManagementServerIp��˾��ֵΪnull,ɽ����ֵ���е���������ͬһ��IP
		// map.put(SYNC_HO_IP, Util.normalizeString(summary
		// .getManagementServerIp()));

		// XXX 8����ѹ

		// --9������״̬
		// --10��ʹ��״��
		// --11������λ��

		// 12��������ID
		map.put(SYNC_COMMON_CRID, crMor.get_value());

		// 13��cpu����
		// ժҪ-->����-->���������
		map.put(SYNC_HO_NUMCPU, summary.getHardware().getNumCpuPkgs());

		// 14��ÿ��cpu����
		// ժҪ-->����-->ÿ����۵��ں���
		map.put(SYNC_HO_NUMCORE, summary.getHardware().getNumCpuCores()
				/ summary.getHardware().getNumCpuPkgs());
		// ժҪ-->����-->�߼�������,summary.getHardware.getNumCpuThreads

		// 15��cpu��Ƶ
		// ժҪ-->����-->CPU�ں�
		map.put(SYNC_HO_CPUMHZ, summary.getHardware().getCpuMhz());

		// 16���ڴ��С
		map.put(SYNC_HO_MEMORYSIZEMB, convertBytes2MBString(hardware
				.getMemorySize()));

		// 17����������������
		map.put(SYNC_HO_NUMNICS, summary.getHardware().getNumNics());

		// --18�����д洢��С

		// 19�����صĽӿ�ID������Vid
		map.put(SYNC_COMMON_VID, mor.get_value());

		// 20: ��Դ״̬
		String powerState = "";
		if (runtime != null) {
			powerState = runtime.getPowerState().toString();
		}
		map.put(SYNC_HO_POWERSTATE, powerState);

		resultList.add(map);
	}

	/**
	 * ͬ����������vmFolder�µĶ���,Ŀǰ���ֵĶ����������(ģ��)��vApp
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param vmList
	 * @param tempList
	 * @param rpList
	 * @param otherList
	 * @param crRpMap
	 * @throws Exception
	 */
	private static void syncVMFolder(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor,
			ArrayList<HashMap<String, Object>> vmList,
			ArrayList<HashMap<String, Object>> tempList,
			ArrayList<HashMap<String, Object>> rpList,
			ArrayList<HashMap<String, Object>> otherList,
			HashMap<String, String> crRpMap) throws Exception {
		ManagedObjectReference[] hoMorList = getChildEntityFromFolder(ecb,
				dcMor, RESOURCE_DC_VMFOLDER);
		ArrayList morList = ecb.getServiceUtil3().getDecendentMoRefs(
				null, RESOURCE_VM);
		if (morList != null) {
			LOGGER.info("initCommonTypeCache type='" + RESOURCE_VM
					+ "', size='" + morList.size() + "'");
			for (Object obj : morList) {
				ManagedObjectReference vmMor = (ManagedObjectReference) obj;
				if (vmMor.getType().equals(RESOURCE_VM)) {
					syncVirtualMachine(ecb, dcMor, vmMor, vmList, tempList,
							crRpMap);
					// vmFolder�·�������Ϊ"�ѷ��������"��"Folder"
					// vmFolder�·�������Ϊ"childvApp1(�Զ�������)"��"VirtualApp"

					// ��ʱ������vApp����
					// } else if (vmMor.getType().equals(RESOURCE_VAPP)) {
					// ��˾����������Դ�����洴����vApp������,��ȡ������ΪVirtualApp
					// vApp��һ�������������vApp�ļ��ϡ�
					// �ӹ���ĽǶ�������һ�����ε�vApp����Ϊ����һ�������������ӵ�з���ҵ�����磬���ݴ洢��������������Դʹ�á�
					// �Ӽ����ĽǶ�������һ��vApp������һ��ר�ŵ���Դ�� ��
					// ���Է���vApp����������޷��ٱ�����
					// ģ���޷����뵽vApp��
					// syncVirtualApp(ecb, dcMor, vmMor, vAppList, crRpMap);
				} else {
					syncOtherMor(ecb, dcMor, vmMor, "syncVMFolder",
							DYNAMICPROPERTY_VMFOLDER, otherList);
				}
			}
		}
	}

	/**
	 * ͬ���������ģ�����
	 * 
	 * @param ecb
	 * @param dcMo
	 * @param mor
	 * @param vmList
	 * @param tempList
	 * @param crRpMap
	 * @throws Exception
	 */
	private static void syncVirtualMachine(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference mor,
			ArrayList<HashMap<String, Object>> vmList,
			ArrayList<HashMap<String, Object>> tempList,
			HashMap<String, String> crRpMap) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncVirtualMachine, dcMor='" + dcMor + "', mor='" + mor
				+ "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		// ����
		VirtualMachineConfigInfo config = (VirtualMachineConfigInfo) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_CONFIG);

		// ����
		VirtualMachineRuntimeInfo runtime = (VirtualMachineRuntimeInfo) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_RUNTIME);

		// ժҪ
		VirtualMachineSummary summary = (VirtualMachineSummary) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_SUMMARY);

		// ��Դ��
		ManagedObjectReference rpMor = (ManagedObjectReference) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_RESOURCEPOOL);

		// �洢
		VirtualMachineStorageInfo storage = (VirtualMachineStorageInfo) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_STORAGE);

		// �����б�
		ManagedObjectReference[] netMorList = (ManagedObjectReference[]) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_NETWORK);

		// ������е��ֶ�
		// ģ����е��ֶ���������ֶ�һ��ֻ������ID����ԴID��һ��

		// --1������ID

		// 2���������
		map.put(SYNC_COMMON_NAME, config.getName());

		// 3��������ID MISϵͳ�е�ID,"21��Ⱥ��ID "����(Ⱥ��)��VID

		// 4��������������ID ����Ϊ�գ�ģ���û�����ֵ���������ֵ���ܻ�仯��
		String hoId = "";
		if ((runtime != null) && (runtime.getHost() != null)) {
			hoId = runtime.getHost().get_value();
		}
		map.put(SYNC_VM_HOID, hoId);

		// --5������״̬ID

		// 6������
		map.put(SYNC_VM_DESC, Util.normalizeString(config.getAnnotation()));

		// --7��ģ��ID
		// --8��ʹ��״̬

		// 9���洢��ID
		String dsId = "";
		VirtualMachineUsageOnDatastore[] usageOnDS = storage
				.getPerDatastoreUsage();
		if ((usageOnDS != null) && (usageOnDS.length > 0)) {
			dsId = usageOnDS[0].getDatastore().get_value();
			if (usageOnDS.length > 1) {
				LOGGER.error("syncVMObj error, vm='" + mor.get_value()
						+ "' usageOnDS='" + Arrays.asList(usageOnDS) + "'");
			}
		}
		map.put(SYNC_COMMON_DSID, dsId);

		// 10��cup�ܺ���
		int numCpu = config.getHardware().getNumCPU();
		int numCore = config.getHardware().getNumCoresPerSocket();
		map.put(SYNC_VM_CPU, numCpu);

		// 11���ڴ�����
		map.put(SYNC_VM_MEMORYSIZEMB, summary.getConfig().getMemorySizeMB());

		// 12���洢����
		map.put(SYNC_VM_DSSIZEMB, convertBytes2MBString(summary.getStorage()
				.getCommitted()
				+ summary.getStorage().getUncommitted()));

		// 13����������
		// VirtualMachineConfigSummary.numEthernetCards
		map.put(SYNC_VM_NETNUM, netMorList.length);

		// --14������ϵͳ�汾��Windows��Linux�������ȣ�����ʾ

		// 15���������ϵͳ
		map.put(SYNC_VM_GUESTFULLNAME, config.getGuestFullName());

		// --16������IP��ID

		// 17�����صĽӿ�ID ����VID
		map.put(SYNC_COMMON_VID, mor.get_value());

		// 18����������ID
		map.put(SYNC_COMMON_DCID, dcMor.get_value());

		// --19��cpu��Ƶ��һ����������õĶ������ޣ�

		// 20����Դ������ID
		String rpId = "";
		if (rpMor != null) {
			rpId = rpMor.get_value();
		}
		map.put(SYNC_VM_RPID, rpId);

		// 21��Ⱥ��ID
		// ��Ҫͨ����Դ��id��ȡ��Ⱥ����ID
		String crId = Util.normalizeString(crRpMap.get(rpId));
		map.put(SYNC_COMMON_CRID, crId);
		// //ֻ��ģ�����Դ��Ϊ��
		// if (crId.equals("")) {
		// LOGGER.error("syncVirtualMachine error, id='" + mor.get_value()
		// + "', rpId='" + rpId + "', crId='" + crId + "'");
		// }

		// 22��cpu ����
		map.put(SYNC_VM_NUMCPU, numCpu / numCore);

		// 23��ÿ��cpu����
		map.put(SYNC_VM_NUMCORE, numCore);

		// --24���˻�
		// --25������
		// --26��������� 0��ͨ��� 1 loadrunner��� 2 �ǲ������
		// --27���������� 1Ϊ��¡ 2Ϊ����
		// --28����ԴID����Ӧ��ģ��id��

		// 29: ��Դ״̬
		String powerState = "";
		if (runtime != null) {
			powerState = runtime.getPowerState().toString();
		}
		map.put(SYNC_VM_POWERSTATE, powerState);

		// �ж��Ƿ�Ϊģ��
		if (config.isTemplate()) {
			tempList.add(map);
		} else {
			vmList.add(map);
		}
		
		//����ȥ���ж�
		//vmList.add(map);
	}

	/**
	 * ͬ����������datastoreFolder��datastore�µĶ���
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param dsList
	 * @param otherList
	 * @throws Exception
	 */
	private static void syncDatastoreFolderAndDatastore(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor,
			ArrayList<HashMap<String, Object>> dsList,
			ArrayList<HashMap<String, Object>> otherList) throws Exception {

		// ��ɽ���ֳ������ݿ�datastore��datastoreFolder�������б��صĴ������ظ���
		// �洢�������ǲ�ͬ��,�ڽ�������ʾ����datastore������,�����ڴ��������ѡ����̵�ʱ����ʾ����datastoreFolder������
		// ------------------------------------------------datastore
		// ============datastore-271,Cloud-CX960-Storage-VMFS5-A,Datastore
		// ============datastore-285,Cloud-CX960-Storage-VMFS5-B,Datastore
		// ============datastore-286,Cloud-CX960-Converter-VMFS3,Datastore
		// ------------------------------------------------datastoreFolder
		// ============group-p273,Cloud-CX960,StoragePod
		// ============datastore-286,Cloud-CX960-Converter-VMFS3,Datastore

		// ��¼ds�����Map
		HashMap<String, HashMap<String, Object>> dsObjMap = new HashMap<String, HashMap<String, Object>>();

		ManagedObjectReference[] dsMorList1 = (ManagedObjectReference[]) getDynamicProperty(
				ecb, dcMor, DYNAMICPROPERTY_DATASTORE);
		syncDatastoreMorList(ecb, dcMor, dsMorList1, dsObjMap,
				DYNAMICPROPERTY_DATASTORE, otherList);

		ManagedObjectReference[] dsMorList2 = getChildEntityFromFolder(ecb,
				dcMor, DYNAMICPROPERTY_DATASTOREFOLDER);
		syncDatastoreMorList(ecb, dcMor, dsMorList2, dsObjMap,
				DYNAMICPROPERTY_DATASTOREFOLDER, otherList);

		// ��Map�е����ݷ���dsList
		dsList.addAll(dsObjMap.values());
	}

	/**
	 * �������ݴ洢�б�
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param dsMorList
	 * @param dsObjMap
	 * @param source
	 * @param otherList
	 */
	private static void syncDatastoreMorList(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference[] dsMorList,
			HashMap<String, HashMap<String, Object>> dsObjMap, String source,
			ArrayList<HashMap<String, Object>> otherList) throws Exception {
		if (dsMorList != null) {
			LOGGER.info("syncDatastoreMorList, dcMor='" + dcMor
					+ "', dsMorList='" + dsMorList.length + "'");

			for (ManagedObjectReference dsMor : dsMorList) {
				if (!dsObjMap.containsKey(dsMor.get_value())) {
					if (dsMor.getType().equals(RESOURCE_DS)) {
						syncDatastore(ecb, dcMor, dsMor, dsObjMap);
					} else if (dsMor.getType().equals(RESOURCE_STORAGEPOD)) {
						syncStoragePod(ecb, dcMor, dsMor, dsObjMap);
					} else {
						syncOtherMor(ecb, dcMor, dsMor, "syncDatastoreMorList",
								source, otherList);
					}
				}
			}
		}
	}

	/**
	 * ͬ�����ݴ洢
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param mor
	 * @param dsObjMap
	 * @throws Exception
	 */
	private static void syncDatastore(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference mor,
			HashMap<String, HashMap<String, Object>> dsObjMap) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncDatastore, dcMor='" + dcMor + "', mor='" + mor + "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		// ժҪ
		DatastoreSummary summary = (DatastoreSummary) getDynamicProperty(ecb,
				mor, DYNAMICPROPERTY_SUMMARY);

		// �洢 �洢Ⱥ�����е��ֶ�
		// -- 1������ID
		// --2������״̬

		// 3���洢�ص�����
		map.put(SYNC_DS_CAPACITY, convertBytes2MBString(summary.getCapacity()));

		// 4��������ID(Ӧ������������,�洢�ǹҽ������������µ�,���ǹҽ���Ⱥ��[��]�µ�)
		map.put(SYNC_COMMON_DCID, dcMor.get_value());

		// --5���洢������
		// --6��ʹ��״̬

		// 7����������
		map.put(SYNC_DS_FREESPACE,
				convertBytes2MBString(summary.getFreeSpace()));

		// 8���洢VID
		map.put(SYNC_COMMON_VID, mor.get_value());

		// 9���洢����
		map.put(SYNC_COMMON_NAME, summary.getName());

		dsObjMap.put(mor.get_value(), map);
	}

	/**
	 * ͬ��StoragePod
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param mor
	 * @param dsObjMap
	 * @throws Exception
	 */
	private static void syncStoragePod(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference mor,
			HashMap<String, HashMap<String, Object>> dsObjMap) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncStoragePod, dcMor='" + dcMor + "', mor='" + mor + "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		// ժҪ
		StoragePodSummary summary = (StoragePodSummary) getDynamicProperty(ecb,
				mor, DYNAMICPROPERTY_SUMMARY);

		// ��ʵ���б�
		ManagedObjectReference[] dsMorList = (ManagedObjectReference[]) getDynamicProperty(
				ecb, mor, DYNAMICPROPERTY_CHILDENTITY);

		// �洢 �洢Ⱥ�����е��ֶ�
		// -- 1������ID
		// --2������״̬

		// 3���洢�ص�����
		map.put(SYNC_DS_CAPACITY, convertBytes2MBString(summary.getCapacity()));

		// 4��������ID(Ӧ������������,�洢�ǹҽ������������µ�,���ǹҽ���Ⱥ��[��]�µ�)
		map.put(SYNC_COMMON_DCID, dcMor.get_value());

		// --5���洢������
		// --6��ʹ��״̬

		// 7����������
		map.put(SYNC_DS_FREESPACE,
				convertBytes2MBString(summary.getFreeSpace()));

		// 8���洢VID
		map.put(SYNC_COMMON_VID, mor.get_value());

		// 9���洢����
		map.put(SYNC_COMMON_NAME, summary.getName());

		// ��¼һ��StoragePod��Datastore�Ĺ�ϵ
		if (dsMorList != null) {
			StringBuffer sBuff = new StringBuffer();
			for (ManagedObjectReference dsMor : dsMorList) {
				sBuff.append(dsMor.get_value() + Util.SEPARATOR_COMMA);
			}
			// ɾ�����һ���ַ�
			if (sBuff.length() > 0) {
				sBuff.delete(sBuff.length() - 1, sBuff.length());
			}
			if (sBuff.length() > 0) {
				map.put(SYNC_DS_CHILDREN, sBuff.toString());
			}
		}

		dsObjMap.put(mor.get_value(), map);
	}

	/**
	 * ͬ����������,��ϵͳ������δ���ֵĶ���
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param mor
	 * @param method
	 * @param source
	 * @param resultList
	 * @throws Exception
	 */
	private static void syncOtherMor(ExtendedAppUtil ecb,
			ManagedObjectReference dcMor, ManagedObjectReference mor,
			String method, String source,
			ArrayList<HashMap<String, Object>> resultList) throws Exception {
		// �����κ��ж�,�����ֱ���׳�Υ��,�����沶��

		LOGGER.info("syncOtherMor, dcMor='" + dcMor + "', mor='" + mor + "'");

		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put(SYNC_COMMON_VID, mor.get_value());
		map.put(SYNC_COMMON_NAME, getEntityName(ecb, mor));
		map.put(SYNC_COMMON_DCID, dcMor.get_value());
		map.put(SYNC_OTHER_SOURCE, source);
		map.put(SYNC_OTHER_TYPE, mor.getType());
		map.put(SYNC_OTHER_METHOD, method);

		resultList.add(map);
	}

	/**
	 * ���ֽ���ת��Ϊ���ʵ���ʾ�ַ���
	 * 
	 * @param bytes
	 * @return
	 */
	public static String getByteSizeStrFromBytes(long bytes) {
		String result = "";
		try {
			long dividend = 0;
			String unit = "";
			// ������С��KB
			if (bytes <= MB_SIZE) {
				dividend = KB_SIZE;
				unit = KB;
			} else if (bytes <= GB_SIZE) {
				dividend = MB_SIZE;
				unit = MB;
			} else if (bytes <= TB_SIZE) {
				dividend = GB_SIZE;
				unit = GB;
			} else if (bytes <= PB_SIZE) {
				dividend = TB_SIZE;
				unit = TB;
			} else {
				dividend = PB_SIZE;
				unit = PB;
			}
			result = BITS2_DF.format((Double.valueOf(bytes) / dividend)) + unit;
		} catch (Exception e) {
			LOGGER.error("getByteSizeStrFromBytes error,", e);
		}

		return result;
	}

	/**
	 * ���ֽ���ת��ΪMB
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static long convertBytes2MBLong(long bytes) throws Exception {
		return bytes / MB_SIZE;
	}

	/**
	 * ���ֽ���ת��ΪMB,�������Ϊ�ַ���
	 * 
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String convertBytes2MBString(long bytes) throws Exception {
		return Long.toString(convertBytes2MBLong(bytes));
	}

	/**
	 * ��MB��ת��Ϊ���ʵ���ʾ�ַ���
	 * 
	 * @param mb
	 * @return
	 */
	public static String getByteSizeStrFromMB(long mb) {
		return mb + MB;
	}

	/**
	 * ��ȡvCenter Server�������Ϣ
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public static void getAbout(String url, String username, String password) {
		try {

			ExtendedAppUtil ecb = VIMMgr.getECB(url, username, password);

			AboutInfo aboutInfo = ecb.getServiceConnection3()
					.getServiceContent().getAbout();

			LOGGER.info("=========getApiType=" + aboutInfo.getApiType());
			LOGGER.info("=========getApiVersion=" + aboutInfo.getApiVersion());
			LOGGER.info("=========getBuild=" + aboutInfo.getBuild());
			LOGGER.info("=========getFullName=" + aboutInfo.getFullName());
			LOGGER.info("=========getInstanceUuid="
					+ aboutInfo.getInstanceUuid());
			LOGGER.info("=========getLicenseProductName="
					+ aboutInfo.getLicenseProductName());
			LOGGER.info("=========getLicenseProductVersion="
					+ aboutInfo.getLicenseProductVersion());
			LOGGER
					.info("=========getLocaleBuild="
							+ aboutInfo.getLocaleBuild());
			LOGGER.info("=========getLocaleVersion="
					+ aboutInfo.getLocaleVersion());
			LOGGER.info("=========getName=" + aboutInfo.getName());
			LOGGER.info("=========getOsType=" + aboutInfo.getOsType());
			LOGGER.info("=========getProductLineId="
					+ aboutInfo.getProductLineId());
			LOGGER.info("=========getVendor=" + aboutInfo.getVendor());
			LOGGER.info("=========getVersion=" + aboutInfo.getVersion());
		} catch (Exception e) {
			LOGGER.error("getAbout error, ", e);
		}
	}

	/**
	 * ��ȡ������¼�,<br>
	 * vtype��Ӧ���Ƕ��������,������VIMConstants�е�RESOURCE_XXX,���õ��������,�������<br>
	 * vid��vtype���͵Ķ����id<br>
	 * timeUnitΪCalendar�ж����ʱ�䵥λ,��Calendar.WEEK_OF_YEAR��Calendar.DAY_OF_YEAR��<br>
	 * timeDiffΪʱ����timeUnit���Ϊ��ʼʱ��,��timeUnitΪCalendar.WEEK_OF_YEAR��timeDiffΪ2,
	 * ��ʼʱ��Ϊ��ǰʱ�䵽2��ǰ���¼�<br>
	 * <br>
	 * ���timeDiff��ֵΪ0,��ʱ�䲻������ <br>
	 * <br>
	 * �¼����������ǰ���ʱ��˳��
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @param timeUnit
	 * @param timeDiff
	 * @return
	 */
	public static Map<String, Object> getEvents(String url, String username,
			String password, String vtype, String vid, int timeUnit,
			int timeDiff) {

		// WebService�Ľӿ�
		// This file was auto-generated from WSDL
		// VimPortType vpt=ecb.getServiceConnection3().getService();
		// ServiceContent sc = ecb.getServiceConnection3().getServiceContent();

		// �¼������ͻ�ȡ�Ĺ���
		// 1.Event��û�����͵���Ϣ
		// 2.����eventFilter.setType������������,����һ���а취��ȡ
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.EventFilterSpec.html
		// 3.�鿴��������ӷ���type�Ѿ�����,��eventTypeId�����
		// 4.�����е���������һ��eventTypeId
		// ����D:\VMSDK\vsphere-ws\java\Axis\com\vmware\vim25\EventAlarmExpression.java
		// D:\VMSDK\vsphere-ws\java\Axis\com\vmware\vim25\EventEx.java
		// D:\VMSDK\vsphere-ws\java\Axis\com\vmware\vim25\EventFilterSpec.java
		// D:\VMSDK\vsphere-ws\java\Axis\com\vmware\vim25\ExtendedEvent.java
		// ������eventTypeId
		// 5.�ڲ鿴http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.Event.html
		// ����Event Extended by EventEx
		// 6.�������ӡһ��Event��className,������
		// com.vmware.vim25.VmAcquiredTicketEvent-->VmEvent
		// com.vmware.vim25.VmRemoteConsoleConnectedEvent
		// com.vmware.vim25.EventEx
		// 7.�ڴ�ӡEventEx�����Ե�ʱ����getSeverity,��Ϊ�����Ӧ��������
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.EventEx.html
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.Event.EventSeverity.html
		// ����EventEventSeverity��Ӧ4��ֵ:error,info,user,warning
		// 8.���Ǵ�ӡ������getSeverity���ǿ�
		// 9.����Ҳû�л�ȡ�����ϵ�"����"����
		// 10.EventManager��description����������EventDescription,
		// EventDescription������category��ö��������error,info,user,warning
		// EventDescription������eventInfo��������EventDescriptionEventDetail,EventDescriptionEventDetailҲ��category����
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.EventManager.html
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.EventDescription.html
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.EventDescription.EventCategory.html
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.event.EventDescription.EventDetail.html

		boolean isTimeFilter = false;
		Calendar beginTime = null;
		Calendar endTime = null;

		if (timeDiff > 0) {
			beginTime = Calendar.getInstance();
			beginTime.add(timeUnit, 0 - timeDiff);
			endTime = Calendar.getInstance();
			isTimeFilter = true;
		}

		return getEvents(url, username, password, vtype, vid, beginTime,
				endTime, isTimeFilter);
	}

	/**
	 * ��ȡ������¼�,˵���ο�ǰ��ķ���<br>
	 * �ӿ�ֱ�Ӵ��뿪ʼ�ͽ���ʱ��
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static Map<String, Object> getEvents(String url, String username,
			String password, String vtype, String vid, Calendar beginTime,
			Calendar endTime) {
		return getEvents(url, username, password, vtype, vid, beginTime,
				endTime, true);
	}

	/**
	 * ��ȡ������¼�,˵���ο�ǰ��ķ���<br>
	 * �ӿ�ֱ�Ӵ��뿪ʼ�ͽ���ʱ��,isTimeFilter�ж��Ƿ�ʹ��ʱ������
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @param beginTime
	 * @param endTime
	 * @param isTimeFilter
	 * @return
	 */
	private static Map<String, Object> getEvents(String url, String username,
			String password, String vtype, String vid, Calendar beginTime,
			Calendar endTime, boolean isTimeFilter) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		ArrayList<HashMap<String, String>> resultEvtList = new ArrayList<HashMap<String, String>>();

		try {
			LOGGER.info("getEvents start, vtype='" + vtype + "', vid='" + vid
					+ "', beginTime='" + beginTime + "', endTime='" + endTime
					+ "'");

			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getResource(vtype, vid);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				ManagedObjectReference evtMgr = ecb.getServiceConnection3()
						.getServiceContent().getEventManager();

				EventFilterSpecByEntity entitySpec = new EventFilterSpecByEntity();
				entitySpec.setEntity(mor);
				entitySpec.setRecursion(EventFilterSpecRecursionOption.all);

				EventFilterSpec eventFilter = new EventFilterSpec();
				eventFilter.setEntity(entitySpec);

				if (isTimeFilter) {
					EventFilterSpecByTime timeSpec = new EventFilterSpecByTime();
					timeSpec.setBeginTime(beginTime);
					timeSpec.setEndTime(endTime);
					eventFilter.setTime(timeSpec);
				}

				Event[] evtList = ecb.getServiceConnection3().getService()
						.queryEvents(evtMgr, eventFilter);

				if (evtList != null) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.info("getEvents , vtype='" + vtype + "', vid='"
								+ vid + "', beginTime='" + beginTime
								+ "', endTime='" + endTime + "', size='"
								+ evtList.length + "'");
					}
					for (Event evt : evtList) {
						convertEvent2Map(cache, resultEvtList, evt);
					}
				} else {
					LOGGER.error("getEvents evtList is null , vtype='" + vtype
							+ "', vid='" + vid + "', beginTime='" + beginTime
							+ "', endTime='" + endTime + "'");
				}

				resultMap.put(INFO_STATE, STATE_OK);
				resultMap.put(INFO_RESULT, resultEvtList);
			} else {
				recordResultMapError(resultMap, "����Ϊ'" + vtype + "'�Ķ���'" + vid
						+ "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getEvents error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ��Event����ת��Ϊǰ̨��ʾ�Ķ���
	 * 
	 * @param cache
	 * @param resultEvtList
	 * @param evt
	 * @throws Exception
	 */
	private static void convertEvent2Map(VIMCache cache,
			ArrayList<HashMap<String, String>> resultEvtList, Event evt)
			throws Exception {
		HashMap<String, String> evtMap = new HashMap<String, String>();
		resultEvtList.add(evtMap);

		// ����
		evtMap.put(EVENT_DESC, evt.getFullFormattedMessage());
		// ����,�ݲ���ȡ,
		// ����(������):��Ϣ�;����
		// ������Ҫ����ʱ���������ȷ��������,��ĳ�����͵�Event���Ǿ����
		// if (evt instanceof AlarmEvent) {
		// } else if (evt instanceof TaskEvent) {
		// } else if (evt instanceof VmEvent) {
		// }
		evtMap.put(EVENT_CATEGORY, cache.getEventCategory(evt));
		// ʱ��
		evtMap.put(EVENT_TIME, Util.getDBDateTime(evt.getCreatedTime()
				.getTimeInMillis()));
		// ����,�������ƿ��ܻ���Ҫ����
		if (evt instanceof TaskEvent) {
			TaskEvent taskEvt = (TaskEvent) evt;
			evtMap.put(EVENT_TASK, cache.getTaskDesc(taskEvt.getInfo()
					.getDescriptionId()));
		} else {
			evtMap.put(EVENT_TASK, "");
		}
		// Ŀ��,��ǰ�豸�¼�,Ŀ��ͨ�������-->����-->��Դ��-->��������,���¿�ʼȡ��һ��
		if (evt.getVm() != null) {
			evtMap.put(EVENT_TARGET, evt.getVm().getName());
		} else if (evt.getHost() != null) {
			evtMap.put(EVENT_TARGET, evt.getHost().getName());
		} else if (evt.getComputeResource() != null) {
			evtMap.put(EVENT_TARGET, evt.getComputeResource().getName());
		} else if (evt.getDatacenter() != null) {
			evtMap.put(EVENT_TARGET, evt.getDatacenter().getName());
		} else {
			evtMap.put(EVENT_TARGET, "");
		}
		// �û�
		evtMap.put(EVENT_USER, Util.normalizeString(evt.getUserName()));
	}

	/**
	 * ��ȡ���������,<br>
	 * vtype��Ӧ���Ƕ��������,������VIMConstants�е�RESOURCE_XXX,���õ��������,�������<br>
	 * vid��vtype���͵Ķ����id<br>
	 * timeUnitΪCalendar�ж����ʱ�䵥λ,��Calendar.WEEK_OF_YEAR��Calendar.DAY_OF_YEAR��<br>
	 * timeDiffΪʱ����timeUnit���Ϊ��ʼʱ��,��timeUnitΪCalendar.WEEK_OF_YEAR��timeDiffΪ2,
	 * ��ʼʱ��Ϊ��ǰʱ�䵽2��ǰ������<br>
	 * <br>
	 * --����ȥ��,���timeDiff��ֵΪ0,��ʱ�䲻������ <br>
	 * <br>
	 * --����ȥ��,���maxCount��ֵΪ0,��maxCountΪĬ��ֵ <br>
	 * <br>
	 * ��ȡ���ݵ�����Ŀǰ�޷�����ֻ�ܻ�ȡTaskHistoryCollector��latestPage��������,Ҳ�޷���ȡmaxCount�ľ���ֵ,
	 * ʵ�ʻ�ȡ�����ݴ�Լ10������<br>
	 * <br>
	 * ������������ǰ���ʱ������,���¼���˳�������෴
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @return
	 */
	public static Map<String, Object> getTasks(String url, String username,
			String password, String vtype, String vid) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		ArrayList<HashMap<String, String>> resultTaskList = new ArrayList<HashMap<String, String>>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getResource(vtype, vid);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				ManagedObjectReference taskMgr = ecb.getServiceConnection3()
						.getServiceContent().getTaskManager();

				TaskFilterSpecByEntity entitySpec = new TaskFilterSpecByEntity();
				entitySpec.setEntity(mor);
				entitySpec.setRecursion(TaskFilterSpecRecursionOption.all);

				TaskFilterSpec taskFilter = new TaskFilterSpec();
				taskFilter.setEntity(entitySpec);

				// //���ִ���
				// AxisFault
				// faultCode:
				// {http://schemas.xmlsoap.org/soap/envelope/}Server.userException
				// faultSubcode:
				// faultString: java.io.IOException: java.io.IOException:
				// java.io.IOException: Non nillable element 'timeType' is null.
				// faultActor:
				// faultNode:
				// faultDetail:
				// {http://xml.apache.org/axis/}stackTrace:java.io.IOException:
				// java.io.IOException: java.io.IOException: Non nillable
				// element 'timeType' is null.
				// at
				// org.apache.axis.encoding.ser.BeanSerializer.serialize(BeanSerializer.java:275)
				// at
				// org.apache.axis.encoding.SerializationContext.serializeActual(SerializationContext.java:1504)
				// at
				// org.apache.axis.encoding.SerializationContext.serialize(SerializationContext.java:980)
				// at
				// org.apache.axis.encoding.SerializationContext.serialize(SerializationContext.java:801)
				// at
				// org.apache.axis.message.RPCParam.serialize(RPCParam.java:208)
				// at
				// org.apache.axis.message.RPCElement.outputImpl(RPCElement.java:433)
				// at
				// org.apache.axis.message.MessageElement.output(MessageElement.java:1208)
				// at
				// org.apache.axis.message.SOAPBody.outputImpl(SOAPBody.java:139)
				// at
				// org.apache.axis.message.SOAPEnvelope.outputImpl(SOAPEnvelope.java:478)
				// at
				// org.apache.axis.message.MessageElement.output(MessageElement.java:1208)
				// at org.apache.axis.SOAPPart.writeTo(SOAPPart.java:315)
				// at org.apache.axis.SOAPPart.writeTo(SOAPPart.java:269)
				// at org.apache.axis.SOAPPart.saveChanges(SOAPPart.java:530)
				// at
				// org.apache.axis.SOAPPart.getContentLength(SOAPPart.java:229)
				// at org.apache.axis.Message.getContentLength(Message.java:510)
				// at
				// org.apache.axis.transport.http.HTTPSender.writeToSocket(HTTPSender.java:371)
				// at
				// org.apache.axis.transport.http.HTTPSender.invoke(HTTPSender.java:138)
				// at
				// org.apache.axis.strategies.InvocationStrategy.visit(InvocationStrategy.java:32)
				// at
				// org.apache.axis.SimpleChain.doVisiting(SimpleChain.java:118)
				// at org.apache.axis.SimpleChain.invoke(SimpleChain.java:83)
				// at
				// org.apache.axis.client.AxisClient.invoke(AxisClient.java:165)
				// at org.apache.axis.client.Call.invokeEngine(Call.java:2784)
				// at org.apache.axis.client.Call.invoke(Call.java:2767)
				// at org.apache.axis.client.Call.invoke(Call.java:2443)
				// at org.apache.axis.client.Call.invoke(Call.java:2366)
				// at org.apache.axis.client.Call.invoke(Call.java:1812)
				// at
				// com.vmware.vim25.VimBindingStub.createCollectorForTasks(VimBindingStub.java:54266)

				// ȥ������TaskFilterSpecByTime������
				// if (timeDiff > 0) {
				// TaskFilterSpecByTime timeSpec = new TaskFilterSpecByTime();
				// Calendar beginTime = Calendar.getInstance();
				// beginTime.add(timeUnit, 0 - timeDiff);
				// Calendar endTime = Calendar.getInstance();
				// timeSpec.setBeginTime(beginTime);
				// timeSpec.setEndTime(endTime);
				// taskFilter.setTime(timeSpec);
				// }

				// CreateCollectorForTasks����ֵΪTaskHistoryCollector
				ManagedObjectReference taskHisColl = ecb
						.getServiceConnection3().getService()
						.createCollectorForTasks(taskMgr, taskFilter);

				// // ����SetCollectorPageSize����maxCount����������
				// if (maxCount > 0) {
				// ecb.getServiceConnection3().getService()
				// .setCollectorPageSize(taskMor, maxCount);
				// }

				TaskInfo[] taskInfoList = (TaskInfo[]) VIMMgr
						.getDynamicProperty(ecb, taskHisColl,
								DYNAMICPROPERTY_LATESTPAGE);

				if (taskInfoList != null) {
					for (TaskInfo taskInfo : taskInfoList) {
						convertTaskInfo2Map(cache, resultTaskList, taskInfo);
					}
				}

				resultMap.put(INFO_STATE, STATE_OK);
				resultMap.put(INFO_RESULT, resultTaskList);
			} else {
				recordResultMapError(resultMap, "����Ϊ'" + vtype + "'�Ķ���'" + vid
						+ "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getTasks error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ��TaskInfo����ת��Ϊǰ̨��ʾ�Ķ���
	 * 
	 * @param cache
	 * @param resultTaskList
	 * @param taskInfo
	 * @throws Exception
	 */
	private static void convertTaskInfo2Map(VIMCache cache,
			ArrayList<HashMap<String, String>> resultTaskList, TaskInfo taskInfo)
			throws Exception {

		HashMap<String, String> taskMap = new HashMap<String, String>();
		resultTaskList.add(taskMap);

		// ����,�������ƿ��ܻ���Ҫ����
		taskMap.put(TASK_NAME, cache.getTaskDesc(taskInfo.getDescriptionId()));
		// Ŀ��
		taskMap.put(TASK_TARGET, taskInfo.getEntityName());
		// ״̬
		taskMap.put(TASK_STATE, taskInfo.getState().getValue());
		// ������Ϣ
		if (taskInfo.getError() != null) {
			taskMap.put(TASK_ERROR, taskInfo.getError().getLocalizedMessage());
		} else {
			taskMap.put(TASK_ERROR, "");
		}
		// ������
		if (taskInfo.getReason() != null) {
			if (taskInfo.getReason() instanceof TaskReasonAlarm) {
				taskMap.put(TASK_USER, TASK_USER_ALARM
						+ ((TaskReasonAlarm) taskInfo.getReason())
								.getAlarmName());
			}
			if (taskInfo.getReason() instanceof TaskReasonSchedule) {
				taskMap
						.put(TASK_USER, TASK_USER_SCHEDULE
								+ ((TaskReasonSchedule) taskInfo.getReason())
										.getName());
			}
			if (taskInfo.getReason() instanceof TaskReasonSystem) {
				taskMap.put(TASK_USER, TASK_USER_SYSTEM);
			}
			if (taskInfo.getReason() instanceof TaskReasonUser) {
				taskMap.put(TASK_USER, ((TaskReasonUser) taskInfo.getReason())
						.getUserName());
			}
		} else {
			taskMap.put(TASK_USER, "");
		}
		// ����ʼʱ��
		taskMap.put(TASK_QUEUETIME, Util.getDBDateTime(taskInfo.getQueueTime()
				.getTimeInMillis()));
		// ��ʼʱ��
		taskMap.put(TASK_STARTTIME, Util.getDBDateTime(taskInfo.getStartTime()
				.getTimeInMillis()));
		// ���ʱ��
		taskMap.put(TASK_COMPLETETIME, Util.getDBDateTime(taskInfo
				.getCompleteTime().getTimeInMillis()));
	}

	/**
	 * ��ȡ����ĸ澯,<br>
	 * vtype��Ӧ���Ƕ��������,������VIMConstants�е�RESOURCE_XXX,���õ��������,�������<br>
	 * vid��vtype���͵Ķ����id<br>
	 * <br>
	 * <br>
	 * VMWare�и澯�Ĺ���ģʽ��AlarmManager.getAlarmָ��entity��ȡ�ĸ澯ֻ���Ǻ��洴���ĸ澯,<br>
	 * vCenterServer��װ��ɺ�Ĭ�ϻᴴ��һЩ�澯,��Щ�澯��entity������������(group-d1)����<br>
	 * <br>
	 * ��ȡָ��entity�澯Ӧ��ͨ��AlarmManager.
	 * getAlarmState�л�ȡ���еĸ澯״̬getOverallStatusΪred��yellow�ĸ澯,<br>
	 * ��ͨ��almState.getAlarm()��ȡ���澯�����ơ�<br>
	 * <br>
	 * �澯�������������������ݹ���,Ӧ�ú�ʱ���޹�,Ӧ���ǰ���alm��key���򡣺��޸�Ϊ����ʱ������<br>
	 * <br>
	 * Ŀǰ���޷���vCenterServer����һ����ʾ�����Ӷ���ĸ澯,��itiimsDC�������Ŀ�����ʾ����������������������ĸ澯<br>
	 * ֻ����ʾ����ĸ澯<br>
	 * <br>
	 * �ӿ��޷���ȡ���������Ķ���ĸ澯<br>
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @return
	 */
	@SuppressWarnings("unused")
	private static Map<String, Object> getAlarms0(String url, String username,
			String password, String vtype, String vid) {
		// �ӿ��޷���ȡ���������Ķ���ĸ澯,�������Ƚ�����
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		ArrayList<ComparableHashMap<String, String>> resultAlmList = new ArrayList<ComparableHashMap<String, String>>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getResource(vtype, vid);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				ManagedObjectReference almMgr = ecb.getServiceConnection3()
						.getServiceContent().getAlarmManager();

				AlarmState[] almStateList = ecb.getServiceConnection3()
						.getService().getAlarmState(almMgr, mor);
				if (almStateList != null) {
					for (AlarmState almState : almStateList) {
						// red:ʵ��һ��������
						// yellow:ʵ�����������
						if ((almState.getOverallStatus()
								.equals(ManagedEntityStatus.red))
								|| (almState.getOverallStatus()
										.equals(ManagedEntityStatus.yellow))) {
							convertAlarmState2Map(cache, ecb, resultAlmList,
									almState);
							// �澯��ʱ������
							Collections.sort(resultAlmList);
						}
					}
				}
				resultMap.put(INFO_STATE, STATE_OK);
				resultMap.put(INFO_RESULT, resultAlmList);
			} else {
				recordResultMapError(resultMap, "����Ϊ'" + vtype + "'�Ķ���'" + vid
						+ "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getAlarms error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ��ȡ����ĸ澯,<br>
	 * <br>
	 * �澯���������ǰ���ʱ�䰡����<br>
	 * <br>
	 * ͨ������"���"�ĸ澯ͨ���ӿ��޷��ٻ�ȡ��<br>
	 * <br>
	 * ������˵��������ķ���
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @return
	 */
	public static Map<String, Object> getAlarms(String url, String username,
			String password, String vtype, String vid) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		ArrayList<ComparableHashMap<String, String>> resultAlmList = new ArrayList<ComparableHashMap<String, String>>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getResource(vtype, vid);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// ����ĸ澯
				// AlarmState[] declaredAlarmState = (AlarmState[])
				// getDynamicProperty(
				// ecb, mor, "declaredAlarmState");

				// �����ĸ澯
				AlarmState[] triggeredAlarmState = (AlarmState[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_TRIGGEREDALARMSTATE);

				if (triggeredAlarmState != null) {
					for (AlarmState almState : triggeredAlarmState) {
						// red:ʵ��һ��������
						// yellow:ʵ�����������
						if ((almState.getOverallStatus()
								.equals(ManagedEntityStatus.red))
								|| (almState.getOverallStatus()
										.equals(ManagedEntityStatus.yellow))) {
							convertAlarmState2Map(cache, ecb, resultAlmList,
									almState);
							// �澯��ʱ������
							Collections.sort(resultAlmList);
						}
					}
				}

				resultMap.put(INFO_STATE, STATE_OK);
				resultMap.put(INFO_RESULT, resultAlmList);
			} else {
				recordResultMapError(resultMap, "����Ϊ'" + vtype + "'�Ķ���'" + vid
						+ "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getAlarms error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ��AlarmState����ת��Ϊǰ̨��ʾ�Ķ���
	 * 
	 * @param cache
	 * @param ecb
	 * @param resultAlmList
	 * @param alarmState
	 * @throws Exception
	 */
	private static void convertAlarmState2Map(VIMCache cache,
			ExtendedAppUtil ecb,
			ArrayList<ComparableHashMap<String, String>> resultAlmList,
			AlarmState almState) throws Exception {
		ComparableHashMap<String, String> almMap = new ComparableHashMap<String, String>(
				false, ALARM_TIME);
		resultAlmList.add(almMap);

		// ����
		almMap.put(ALARM_ENTITY, getEntityName(ecb, almState.getEntity()));
		// ״̬
		almMap.put(ALARM_STATUS, almState.getOverallStatus().getValue());
		// ����
		AlarmInfo almInfo = cache.getAlarm(almState.getAlarm().get_value());
		almMap.put(ALARM_NAME, (almInfo != null ? almInfo.getName() : ""));
		// ����ʱ��
		almMap.put(ALARM_TIME, Util.getDBDateTime(almState.getTime()
				.getTimeInMillis()));
		if (almState.getAcknowledged()) {
			almMap.put(ALARM_ACKNOWLEDGEDTIME, Util.getDBDateTime(almState
					.getAcknowledgedTime().getTimeInMillis()));
			almMap
					.put(ALARM_ACKNOWLEDGEDUSER, almState
							.getAcknowledgedByUser());
		} else {
			almMap.put(ALARM_ACKNOWLEDGEDTIME, "");
			almMap.put(ALARM_ACKNOWLEDGEDUSER, "");
		}
	}

	/**
	 * ȷ�ϸ澯,�澯��ȷ������username,Ŀǰ��Ӧ�÷�ʽͨ���ӿ�ȷ�ϵĸ澯��ȷ���߽�����Administrator
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @param almId
	 * @return
	 */
	public static Map<String, Object> acknowledgeAlarm(String url,
			String username, String password, String vtype, String vid,
			String almId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getResource(vtype, vid);
			AlarmInfo alarmInfo = cache.getAlarm(almId);

			if ((mor != null) && (alarmInfo != null)
					&& (alarmInfo.getAlarm() != null)) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				ManagedObjectReference almMgr = ecb.getServiceConnection3()
						.getServiceContent().getAlarmManager();
				ecb.getServiceConnection3().getService().acknowledgeAlarm(
						almMgr, alarmInfo.getAlarm(), mor);

				resultMap.put(INFO_STATE, STATE_OK);
			} else {
				if (mor == null) {
					recordResultMapError(resultMap, "����Ϊ'" + vtype + "'�Ķ���'"
							+ vid + "'������");
				}
				if ((alarmInfo == null) || (alarmInfo.getAlarm() == null)) {
					recordResultMapError(resultMap, "�澯'" + almId + "'������");
				}
			}
		} catch (Exception e) {
			LOGGER.error("acknowledgeAlarm error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	// /**
	// * XXX ɾ���澯,����"���"�澯<br>
	// * �������ڵ����ݽṹӦ����ɾ��AlarmState���������ManagedEntity�������,����Ŀǰû���ҵ���صĽӿ�<br>
	// * ǰ̨�����ϵı�ǩΪ"�Ѵ����ľ���",�����AlarmAction�Ƿ��й�ϵ
	// *
	// * @param url
	// * @param username
	// * @param password
	// * @param almId
	// * @return
	// */
	// public static Map<String, Object> removeAlarm(String url, String
	// username,
	// String password, String almId) {
	// HashMap<String, Object> resultMap = new HashMap<String, Object>();
	//
	// try {
	// VIMCache cache = VIMCache.getInstance(url, username, password);
	// AlarmInfo alarmInfo = cache.getAlarm(almId);
	//
	// if ((alarmInfo != null) && (alarmInfo.getAlarm() != null)) {
	// ExtendedAppUtil ecb = getECB(url, username, password);
	// // �ø澯��ɾ��,�����豸����ø澯�����ĸ澯��������,�ͽ����ϵ�"���"����һ���߼�
	// ecb.getServiceConnection3().getService().removeAlarm(
	// alarmInfo.getAlarm());
	// resultMap.put(INFO_STATE, STATE_OK);
	// } else {
	// recordResultMapError(resultMap, "�澯'" + almId + "'������");
	// }
	// } catch (Exception e) {
	// LOGGER.error("acknowledgeAlarm error, ", e);
	// recordResultMapException(resultMap, e);
	// }
	//
	// return resultMap;
	// }

	/**
	 * ��ȡʵ�������
	 * 
	 * @param ecb
	 * @param mor
	 * @return
	 */
	public static String getEntityName(ExtendedAppUtil ecb,
			ManagedObjectReference mor) {
		String entityName = "";

		try {
			if (mor != null) {
				entityName = mor.get_value();
				entityName = (String) getDynamicProperty(ecb, mor,
						DYNAMICPROPERTY_NAME);
			}
		} catch (Exception e) {
			LOGGER.error("getEntityName error, ", e);
		}
		return entityName;
	}

	/**
	 * ��ȡ���ܲɼ�����б�
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public static Map<String, Object> getHistoricalInterval(String url,
			String username, String password) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			ExtendedAppUtil ecb = getECB(url, username, password);

			ManagedObjectReference perfMgr = ecb.getServiceConnection3()
					.getServiceContent().getPerfManager();

			PerfInterval[] historicalInterval = (PerfInterval[]) getDynamicProperty(
					ecb, perfMgr, DYNAMICPROPERTY_HISTORICALINTERVAL);
			if (historicalInterval != null) {
				resultMap.put(INFO_STATE, STATE_OK);
				resultMap.put(INFO_RESULT, historicalInterval);
			} else {
				recordResultMapError(resultMap, "��ȡ�����ܼ��Ϊ��");
			}

		} catch (Exception e) {
			LOGGER.error("getHistoricalInterval error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ��ȡ������������
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vtype
	 * @param vid
	 * @param perfIntervalKey
	 * @param perfCounterKeys
	 * @param endTime
	 * @return
	 */
	public static Map<String, Object> getPerformances(String url,
			String username, String password, String vtype, String vid,
			String perfIntervalKey, String[] perfCounterKeys, Calendar endTime) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
     
//		System.out.println("--getPerformances()--------"+vtype+"---------------username:"+username+"----password:"+password+"-----url:"+url);
		try {
			LOGGER.info("getPerformances start, vtype='" + vtype + "', vid='"
					+ vid + "', perfIntervalKey='" + perfIntervalKey
					+ "', perfCounterKeys='" + Arrays.asList(perfCounterKeys)
					+ "', endTime='" + endTime + "'");

			VIMCache cache = VIMCache.getInstance(url, username, password);

//			System.out.println("---------cache-----------"+cache);
			// ʵ�����
//			System.out.println(vtype+"---------// ʵ�����------------"+vid);
			ManagedObjectReference mor = cache.getResource(vtype, vid);

			// ���ܼ��
			PerfInterval perfInterval = cache.getPerfInterval(perfIntervalKey);

			// ���ܼ�����
			String nullPerfCounterKey = "";
			ArrayList<PerfCounterInfo> perfCounterInfoList = new ArrayList<PerfCounterInfo>();
			for (String perfCounterKey : perfCounterKeys) {
				PerfCounterInfo perfCounterInfo = cache
						.getPerfCounterInfo(perfCounterKey);
				if (perfCounterInfo == null) {
					nullPerfCounterKey = perfCounterKey;
					break;
				} else {
					perfCounterInfoList.add(perfCounterInfo);
				}
			}

			if ((mor != null) && (perfInterval != null)
					&& (nullPerfCounterKey.equals(""))) {

				ExtendedAppUtil ecb = getECB(url, username, password);
//                System.out.println("-------����VM--------"+ecb);
				
				ManagedObjectReference perfMgr = ecb.getServiceConnection3()
						.getServiceContent().getPerfManager();

				// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.PerformanceManager.QuerySpec.html
				PerfQuerySpec querySpec = new PerfQuerySpec();
				// ʵ�����
				querySpec.setEntity(mor);
				// ��ʽ
				// cvs��normal
				querySpec.setFormat(PerfFormat._csv);
				// ���
				querySpec.setIntervalId(perfInterval.getSamplingPeriod());
				// ����ʱ��
				querySpec.setEndTime(endTime);
				// Ҫ������ָ��

				PerfMetricId[] metricIds = new PerfMetricId[perfCounterInfoList
						.size()];
				for (int i = 0; i < perfCounterInfoList.size(); i++) {
					metricIds[i] = new PerfMetricId(null, null,
							perfCounterInfoList.get(i).getKey(), "");
				}
				querySpec.setMetricId(metricIds);

				// ��ѯָ��
				PerfEntityMetricBase[] perfEntityMetricBases = ecb
						.getServiceConnection3().getService().queryPerf(
								perfMgr, new PerfQuerySpec[] { querySpec });


				resultMap.put(INFO_STATE, STATE_OK);
				resultMap.put(INFO_RESULT, perfEntityMetricBases);
			} else {
				if (mor == null) {
					recordResultMapError(resultMap, "����Ϊ'" + vtype + "'�Ķ���'"
							+ vid + "'������");
				}
				if (perfInterval == null) {
					recordResultMapError(resultMap, "���ܼ��'" + perfIntervalKey
							+ "'������");
				}
				if (!nullPerfCounterKey.equals("")) {
					recordResultMapError(resultMap, "���ܼ�����'"
							+ nullPerfCounterKey + "'������");
				}
			}
		} catch (Exception e) {
			LOGGER.error("getPerformances error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}
	
	
	
}
