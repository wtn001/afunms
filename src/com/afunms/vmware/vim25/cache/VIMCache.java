package com.afunms.vmware.vim25.cache;

import com.afunms.vmware.vim25.common.VIMConnection;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.VIMConstants;
import com.afunms.vmware.vim25.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.AlarmDescription;
import com.vmware.vim25.AlarmInfo;
import com.vmware.vim25.ElementDescription;
import com.vmware.vim25.Event;
import com.vmware.vim25.EventDescription;
import com.vmware.vim25.EventDescriptionEventDetail;
import com.vmware.vim25.EventEx;
import com.vmware.vim25.ExtendedEvent;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfInterval;
import com.vmware.vim25.TaskDescription;
import com.vmware.vim25.TypeDescription;

/**
 * VMWare���󻺳�,���ű��ౣ�治ͬ��Դ��ManagedObjectReference����,����Ƶ�����в�ѯ
 * 
 * @author LXL
 * 
 */
public class VIMCache extends VIMConnection implements VIMConstants {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(VIMCache.class);

	// ����Cache����
	private static HashMap<String, VIMCache> cacheMap = new HashMap<String, VIMCache>();

	// VIM������Ϣ
	private String url = "";
	private String username = "";;
	private String password = "";

	// ��������dc����ĸ���ʱ��
	private long dcUpdatetime = 0;

	// VIM����
	private ExtendedAppUtil localECB = null;

	// �������еĶ���
	private HashMap<String, HashMap<String, ManagedObjectReference>> vimMOMap = new HashMap<String, HashMap<String, ManagedObjectReference>>();

	// �¼�������,�����Ǿ�̬��,�汾һ�µ�vCenter������Ӧ����ȫһ��
	private HashMap<String, String> eventCategoryMap = new HashMap<String, String>();

	// ���������,�����Ǿ�̬��,�汾һ�µ�vCenter������Ӧ����ȫһ��
	private HashMap<String, String> taskDescMap = new HashMap<String, String>();

	// �澯������,�����Ǿ�̬��,�汾һ�µ�vCenter������Ӧ����ȫһ��,������һЩֵ���ظ���
	private HashMap<String, String> almDescMap = new HashMap<String, String>();

	// �澯
	private HashMap<String, AlarmInfo> almMap = new HashMap<String, AlarmInfo>();

	// ö��ֵ����
	private HashMap<String, String> enumMap = new HashMap<String, String>();

	// ���ܼ��
	private HashMap<String, PerfInterval> perfIntervalMap = new HashMap<String, PerfInterval>();

	// ���ܼ�����
	private HashMap<String, PerfCounterInfo> perfCounterInfoMap = new HashMap<String, PerfCounterInfo>();

	/**
	 * ��ȡʵ��
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static VIMCache getInstance(String url, String username,
			String password) {
		VIMCache cache = null;
		try {
			if (cacheMap.containsKey(getVIMKey(url, username))) {
				cache = cacheMap.get(getVIMKey(url, username));
			} else {
				LOGGER.info("VIMCache start initialization");

				long curr = System.currentTimeMillis();

				cache = new VIMCache();
				cacheMap.put(getVIMKey(url, username), cache);

				// ����������Ϣ����ʼ��
				cache.setConnectionInfo(url, username, password);
				cache.initCache();

				LOGGER.info("VIMCache initialization is complete, time='"
						+ (System.currentTimeMillis() - curr) / 1000 + "' s");
			}
		} catch (Exception e) {
			LOGGER.error("getInstance error, ", e);
		}
		return cache;
	}

	/**
	 * ��ȡVIM����
	 * 
	 * @return
	 */
	private ExtendedAppUtil getLocalECB() {
		try {
			if (localECB == null) {
				localECB = getECB(url, username, password);
			}
		} catch (Exception e) {
			LOGGER.error("getLocalECB error, ", e);
		}
		return localECB;
	}

	/**
	 * ��ȡʵ��,����ʱʹ��
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public static VIMCache getInstanceForTest(String url, String username,
			String password) {
		VIMCache cache = null;
		try {
			if (cacheMap.containsKey(getVIMKey(url, username))) {
				cache = cacheMap.get(getVIMKey(url, username));
			} else {
				cache = new VIMCache();
				cacheMap.put(getVIMKey(url, username), cache);

				// ����������Ϣ����ʼ��
				cache.setConnectionInfo(url, username, password);
				// ����ʱΪ�˼ӿ��ٶȲ���ʼ������
				// cache.initCache();
			}
		} catch (Exception e) {
			LOGGER.error("getInstanceForTest error, ", e);
		}
		return cache;
	}

	/**
	 * ����������Ϣ
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	private void setConnectionInfo(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	/**
	 * ��ʼ�����еĶ���
	 */
	protected void initCache() {
		try {
			// ��ǰʱ��
			long curr0 = System.currentTimeMillis();
			long curr1 = 0;

			// ��ʼ������
			getLocalECB();

			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache connect time='" + (curr1 - curr0) + "' ms");
			curr0 = curr1;

			// ��ʼ��VIM����

			// // ��������������,��ȡ�Ķ������,�����ٶȹ���
			// ObjectContent[] ocList = ecb.getServiceUtil3()
			// .getAllContainerContents();
			//
			// if (ocList != null) {
			// LOGGER.info("VIMCache initialization objsize='"
			// + ocList.length + "'");
			//
			// for (ObjectContent oc : ocList) {
			// ManagedObjectReference mor = oc.getObj();
			// if (mor.getType().equals(RESOURCE_DC)) {
			// dcUpdatetime = System.currentTimeMillis();
			// }
			// getMorMap(mor.getType()).put(mor.get_value(), mor);
			// }
			// }

			// ��������
			initTypeCache(RESOURCE_DC);

			// Ⱥ��,ComputeResource
			initTypeCache(RESOURCE_CR);

			// Ⱥ��,ClusterComputeResource
			initTypeCache(RESOURCE_CCR);

			// ����
			initTypeCache(RESOURCE_HO);

			// ��Դ��
			initTypeCache(RESOURCE_RP);

			// ��Դ��
			initTypeCache(RESOURCE_VM);

			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache initialization obj time='" + (curr1 - curr0)
					+ "' ms");
			curr0 = curr1;

			// �������ĵĴ洢����������������µ��ļ���
			String[] types = { RESOURCE_DC_DATASTORE, RESOURCE_DC_NETWORK,
					RESOURCE_DC_DSFOLDER, RESOURCE_DC_HOFOLDER,
					RESOURCE_DC_NWFOLDER, RESOURCE_DC_VMFOLDER };
			for (String type : types) {
				initDCPropertiesTypeCache(type);
				curr1 = System.currentTimeMillis();
				LOGGER.info("VIMCache initialization type='" + type
						+ "', time='" + (curr1 - curr0) + "' ms");
				curr0 = curr1;
			}

			// �¼�������
			initEventCategory();
			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache initialization type='EventCategory', time='"
					+ (curr1 - curr0) + "' ms");
			curr0 = curr1;

			// ���������
			initTaskDesc();
			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache initialization type='TaskDesc', time='"
					+ (curr1 - curr0) + "' ms");
			curr0 = curr1;

			// �澯������
			initAlarmDesc();
			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache initialization type='AlarmDesc', time='"
					+ (curr1 - curr0) + "' ms");
			curr0 = curr1;

			// �澯
			initAlarm();
			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache initialization type='Alarm', time='"
					+ (curr1 - curr0) + "' ms");
			curr0 = curr1;

			// ö��ֵ
			initEnum();
			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache initialization type='Enum', time='"
					+ (curr1 - curr0) + "' ms");
			curr0 = curr1;

			// ���ܼ��
			initPerfInterval();
			curr1 = System.currentTimeMillis();
			LOGGER.info("VIMCache initialization type='PerfInterval', time='"
					+ (curr1 - curr0) + "' ms");
			curr0 = curr1;

			// ���ܼ�����
			initPerfCounterInfo();
			curr1 = System.currentTimeMillis();
			LOGGER
					.info("VIMCache initialization type='PerfCounterInfo', time='"
							+ (curr1 - curr0) + "' ms");
			curr0 = curr1;

		} catch (Exception e) {
			LOGGER.error("initCache error, ", e);
		}
	}

	/**
	 * ��ʼ�����Ķ���
	 * 
	 * @param type
	 */
	protected void initTypeCache(String type) {
		if ((type.equals(RESOURCE_DC_DATASTORE))
				|| (type.equals(RESOURCE_DC_NETWORK))
				|| (type.equals(RESOURCE_DC_DSFOLDER))
				|| (type.equals(RESOURCE_DC_HOFOLDER))
				|| (type.equals(RESOURCE_DC_NWFOLDER))
				|| (type.equals(RESOURCE_DC_VMFOLDER))) {
			initDCPropertiesTypeCache(type);
		} else {
			initCommonTypeCache(type);
		}
	}

	/**
	 * ��ʼ���洢������
	 * 
	 * @param type
	 */
	protected void initDCPropertiesTypeCache(String type) {
		try {
			ExtendedAppUtil ecb = getLocalECB();

			// �洢������������������
			// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.Datacenter.html

			// ���»�ȡ��������,�ᵼ���������ĵ������ظ���ȡ,���ǿ��Ա�֤��������
			initCommonTypeCache(RESOURCE_DC);
			HashMap<String, ManagedObjectReference> dcVIMMOMap = vimMOMap
					.get(RESOURCE_DC);
			// LOGGER.info("initDsAndNwTypeCache dcVIMMOMap='" + dcVIMMOMap +
			// "'");
			for (Iterator<ManagedObjectReference> dcMorIt = dcVIMMOMap.values()
					.iterator(); dcMorIt.hasNext();) {
				ManagedObjectReference dcMor = dcMorIt.next();
				// LOGGER.info("initDsAndNwTypeCache dcMor='" + dcMor + "'");
				if ((type.equals(RESOURCE_DC_DATASTORE))
						|| (type.equals(RESOURCE_DC_NETWORK))) {
					// �������ĵĶ�ֵ����,���ݴ洢������,ʹ�������id��key
					ManagedObjectReference[] morList = (ManagedObjectReference[]) ecb
							.getServiceUtil3().getDynamicProperty(dcMor, type);
					if (morList != null) {
						// LOGGER.info("initDsAndNwTypeCache type='" + type
						// + "', dcMor='" + dcMor.get_value()
						// + "', size='" + morList.length + "'");
						for (ManagedObjectReference mor : morList) {
							getMorMap(type).put(mor.get_value(), mor);
						}
					}
				} else {
					// �������ĵĵ�ֵ����,4���ļ���,ʹ���������ĵ�dcId��key
					ManagedObjectReference mor = (ManagedObjectReference) ecb
							.getServiceUtil3().getDynamicProperty(dcMor, type);
					if (mor != null) {
						getMorMap(type).put(dcMor.get_value(), mor);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("initDsAndNwTypeCache error, ", e);
		}
	}

	/**
	 * ��ʼ���������
	 * 
	 * @param type
	 */
	@SuppressWarnings("unchecked")
	protected void initCommonTypeCache(String type) {
		try {
			ExtendedAppUtil ecb = getLocalECB();

			// ֻ����������dc����Ƚ�������ܻ����𷴸��ĸ���,��������
			boolean isUpdate = true;
			// isMapEmpty����д��������,���ص���!map.isEmpty()
			if ((type.equals(RESOURCE_DC)
					&& (Util.isMapEmpty(getMorMap(RESOURCE_DC))) && (!Util
					.isTimeout(dcUpdatetime, VIMConstants.DC_UPDATE_INTERVAL)))) {
				isUpdate = false;
			}

			if (isUpdate) {
				if (type.equals(RESOURCE_DC)) {
					dcUpdatetime = System.currentTimeMillis();
				}
				// getDecendentMoRefs��ȡ�Ķ�����ManagedObjectReference
				ArrayList morList = ecb.getServiceUtil3().getDecendentMoRefs(
						null, type);
				if (morList != null) {
					LOGGER.info("initCommonTypeCache type='" + type
							+ "', size='" + morList.size() + "'");
					for (Object obj : morList) {
						ManagedObjectReference mor = (ManagedObjectReference) obj;
						getMorMap(mor.getType()).put(mor.get_value(), mor);
					}
				}
			} else {
				// LOGGER.info("initCommonTypeCache type='" + type
				// + "', isUpdate='" + isUpdate + "'");
			}
			// LOGGER.info("initCommonTypeCache type='" + type + "', size='"
			// + getMorMap(type).size() + "', dcUpdatetime='"
			// + Util.getDBDateTime(dcUpdatetime) + "', isUpdate='"
			// + isUpdate + "'");
		} catch (Exception e) {
			LOGGER.error("initCommonTypeCache error, ", e);
		}
	}

	/**
	 * ���Cache�еĶ���,ÿ������Ҫ������ȡ����
	 */
	public void printVIMMOMap() {
		try {
			ExtendedAppUtil ecb = getLocalECB();

			StringBuffer sBuff = new StringBuffer();

			for (Iterator<Entry<String, HashMap<String, ManagedObjectReference>>> vimMOMapEntryIt = vimMOMap
					.entrySet().iterator(); vimMOMapEntryIt.hasNext();) {
				Entry<String, HashMap<String, ManagedObjectReference>> vimMOMapEntry = vimMOMapEntryIt
						.next();
				if (sBuff.length() > 0) {
					sBuff.delete(0, sBuff.length());
				}
				for (Iterator<Entry<String, ManagedObjectReference>> vimMOEntryIt = vimMOMapEntry
						.getValue().entrySet().iterator(); vimMOEntryIt
						.hasNext();) {
					Entry<String, ManagedObjectReference> vimMOEntry = vimMOEntryIt
							.next();
					sBuff.append(vimMOEntry.getKey());
					sBuff.append(",");
					sBuff.append(VIMMgr.getEntityName(ecb, vimMOEntry
							.getValue()));
					sBuff.append(";");

				}
				LOGGER.info("type='" + vimMOMapEntry.getKey() + "',mos='"
						+ sBuff.toString() + "'");
			}
		} catch (Exception e) {
			LOGGER.error("printVIMMOMap error, ", e);
		}
	}

	/**
	 * ��ȡ��������
	 * 
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getDatacenter(String id) {
		return getResource(RESOURCE_DC, id);
	}

	/**
	 * ��ȡ����Ⱥ��
	 * 
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getComputeResource(String id) {
		return getResource(RESOURCE_CR, id);
	}

	/**
	 * ��ȡ����Ⱥ��
	 * 
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getClusterComputeResource(String id) {
		return getResource(RESOURCE_CCR, id);
	}

	/**
	 * ��ȡ�洢
	 * 
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getDatastore(String id) {
		return getResource(RESOURCE_DC_DATASTORE, id);
	}

	/**
	 * ��ȡ�������ĵ�������ļ���
	 * 
	 * @param dcId
	 * @return
	 */
	public ManagedObjectReference getVMFolder(String dcId) {
		return getResource(RESOURCE_DC_VMFOLDER, dcId);
	}

	/**
	 * ��ȡ����
	 * 
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getHostSystem(String id) {
		return getResource(RESOURCE_HO, id);
	}

	/**
	 * ��ȡ��Դ��
	 * 
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getResourcePool(String id) {
		return getResource(RESOURCE_RP, id);
	}

	/**
	 * ��ȡ�����
	 * 
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getVirtualMachine(String id) {
		return getResource(RESOURCE_VM, id);
	}

	/**
	 * ��ȡ��Դ����
	 * 
	 * @param type
	 * @param id
	 * @return
	 */
	public ManagedObjectReference getResource(String type, String id) {
		ManagedObjectReference mor = null;

		try {
			HashMap<String, ManagedObjectReference> morMap = getMorMap(type);
			if (morMap.containsKey(id)) {
				mor = morMap.get(id);
			} else {
				initTypeCache(type);
				mor = morMap.get(id);
			}
		} catch (Exception e) {
			LOGGER.error("getResource error, ", e);
		}
		return mor;
	}

	/**
	 * ��ȡĳ�����Դ��Map
	 * 
	 * @param type
	 * @return
	 */
	private HashMap<String, ManagedObjectReference> getMorMap(String type) {
		HashMap<String, ManagedObjectReference> morMap = null;

		try {
			morMap = vimMOMap.get(type);
			if (morMap == null) {
				morMap = new HashMap<String, ManagedObjectReference>();
				vimMOMap.put(type, morMap);
			}
		} catch (Exception e) {
			LOGGER.error("getMorMap error, ", e);
		}
		return morMap;
	}

	/**
	 * ��ȡ���ж���
	 * 
	 * @return
	 */
	protected HashMap<String, HashMap<String, ManagedObjectReference>> getVIMMOMap() {
		return vimMOMap;
	}

	/**
	 * ��ʼ���¼���"����"-->�澯����Ϣ������ȵ�
	 */
	protected void initEventCategory() {
		try {
			ExtendedAppUtil ecb = getLocalECB();

			ManagedObjectReference evtMgr = ecb.getServiceConnection3()
					.getServiceContent().getEventManager();

			EventDescription evtDesc = (EventDescription) VIMMgr
					.getDynamicProperty(ecb, evtMgr,
							DYNAMICPROPERTY_DESCRIPTION);

			String key = "";
			String value = "";
			for (EventDescriptionEventDetail evtDetail : evtDesc.getEventInfo()) {
				key = "";
				if ((evtDetail.getKey().equals(EVENTDETAIL_KEY_EXTENDEDEVENT))
						|| (evtDetail.getKey().equals(EVENTDETAIL_KEY_EVENTEX))) {
					if (evtDetail.getFullFormat().indexOf("|") >= 0) {
						// EventDescriptionEventDetail=getFullFormat=ad.event.ImportCertFailedEvent|����֤��ʧ�ܡ�
						// split("|")����ַ�����ֳɵ������ַ�
						// String[] msgs = evtDetail.getFullFormat().split("|");
						String[] msgs = evtDetail.getFullFormat().split("\\|");
						key = msgs[0];
					}
					value = evtDetail.getCategory();
				} else {
					key = evtDetail.getKey();
					value = evtDetail.getCategory();
				}

				if (!key.equals("")) {
					if (!eventCategoryMap.containsKey(key)) {
						// if (LOGGER.isDebugEnabled()) {
						// LOGGER.debug("initEventCategory key='" + key + "'");
						// }
						eventCategoryMap.put(key, value);
					} else {
						// key�ظ�
					}
				} else {
					// keyΪ��
				}
			}
		} catch (Exception e) {
			LOGGER.error("initEventCategory error, ", e);
		}
	}

	/**
	 * ��ȡ�¼���"����"
	 * 
	 * @param event
	 * @return
	 */
	public String getEventCategory(Event event) {
		String result = EVENT_CATEGORY_UNKNOWN;
		try {
			String eventClazz = event.getClass().getName();
			if (event instanceof EventEx) {
				EventEx eventEx = (EventEx) event;
				eventClazz = eventEx.getEventTypeId();
			}
			if (event instanceof ExtendedEvent) {
				ExtendedEvent extendedEvent = (ExtendedEvent) event;
				eventClazz = extendedEvent.getEventTypeId();
			}

			String[] strArr = eventClazz.split("\\.");
			String lastEventClazz = strArr[strArr.length - 1];

			if (eventCategoryMap.containsKey(eventClazz)) {
				result = eventCategoryMap.get(eventClazz);
			} else if (eventCategoryMap.containsKey(lastEventClazz)) {
				result = eventCategoryMap.get(lastEventClazz);
			}

			if (result.equals(EVENT_CATEGORY_UNKNOWN)) {
				LOGGER.error("getEventCategory event='"
						+ event.getClass().getName() + "', eventClazz='"
						+ eventClazz + "', fullMsg='"
						+ event.getFullFormattedMessage() + "'");
			}
		} catch (Exception e) {
			LOGGER.error("getEventCategory error, ", e);
		}
		return result;
	}

	/**
	 * ��ʼ�����������
	 */
	protected void initTaskDesc() {
		try {
			ExtendedAppUtil ecb = getLocalECB();

			ManagedObjectReference taskMgr = ecb.getServiceConnection3()
					.getServiceContent().getTaskManager();

			TaskDescription taskDesc = (TaskDescription) VIMMgr
					.getDynamicProperty(ecb, taskMgr,
							DYNAMICPROPERTY_DESCRIPTION);

			// LOGGER.info("-------------------------------getMethodInfo");
			// for (ElementDescription elemDesc : taskDesc.getMethodInfo()) {
			// LOGGER.info("========getKey=" + elemDesc.getKey());
			// LOGGER.info("========getLabel=" + elemDesc.getLabel());
			// LOGGER.info("========getSummary=" + elemDesc.getSummary());
			// }
			//
			// LOGGER.info("-------------------------------getReason");
			// for (TypeDescription typeDesc : taskDesc.getReason()) {
			// LOGGER.info("========getKey=" + typeDesc.getKey());
			// LOGGER.info("========getLabel=" + typeDesc.getLabel());
			// LOGGER.info("========getSummary=" + typeDesc.getSummary());
			// }
			//
			// LOGGER.info("-------------------------------getState");
			// for (ElementDescription elemDesc : taskDesc.getState()) {
			// LOGGER.info("========getKey=" + elemDesc.getKey());
			// LOGGER.info("========getLabel=" + elemDesc.getLabel());
			// LOGGER.info("========getSummary=" + elemDesc.getSummary());
			// }

			for (ElementDescription elemDesc : taskDesc.getMethodInfo()) {
				taskDescMap.put(elemDesc.getKey(), elemDesc.getLabel());
			}
		} catch (Exception e) {
			LOGGER.error("initTaskDesc error, ", e);
		}
	}

	/**
	 * ��ȡ���������
	 * 
	 * @param descId
	 * @return
	 */
	public String getTaskDesc(String descId) {
		try {
			return taskDescMap.containsKey(descId) ? taskDescMap.get(descId)
					: descId;
		} catch (Exception e) {
			LOGGER.error("getTaskDesc error, ", e);
			return descId;
		}
	}

	/**
	 * ��ʼ���澯������
	 */
	protected void initAlarmDesc() {
		try {
			ExtendedAppUtil ecb = getLocalECB();

			ManagedObjectReference almMgr = ecb.getServiceConnection3()
					.getServiceContent().getAlarmManager();

			AlarmDescription almDesc = (AlarmDescription) VIMMgr
					.getDynamicProperty(ecb, almMgr,
							DYNAMICPROPERTY_DESCRIPTION);

			// SendSNMPAction,���� SNMP
			// SendEmailAction,���͵����ʼ�
			// CreateTaskAction,��������
			// RunScriptAction,���нű�
			// MethodAction,��������
			for (TypeDescription desc : almDesc.getAction()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// True,������
			// False,�ѶϿ�

			for (ElementDescription desc : almDesc
					.getDatastoreConnectionState()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// gray,��ɫ
			// green,��ɫ
			// yellow,��ɫ
			// red,��ɫ
			for (ElementDescription desc : almDesc.getEntityStatus()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// ��
			for (TypeDescription desc : almDesc.getExpr()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}
			// connected,������
			// notResponding,����Ӧ
			// disconnected,�ѶϿ�
			for (ElementDescription desc : almDesc
					.getHostSystemConnectionState()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// poweredOn,�Ѵ򿪵�Դ
			// poweredOff,�ѹرյ�Դ
			// standBy,����
			// unknown,δ֪
			for (ElementDescription desc : almDesc.getHostSystemPowerState()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// isAbove,����
			// isBelow,����
			for (ElementDescription desc : almDesc.getMetricOperator()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// isEqual,����
			// isUnequal,������
			for (ElementDescription desc : almDesc.getStateOperator()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// gray,��ɫ
			// green,��ɫ
			// yellow,��ɫ
			// red,��ɫ
			for (ElementDescription desc : almDesc
					.getVirtualMachineGuestHeartbeatStatus()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

			// poweredOff,�ѹرյ�Դ
			// poweredOn,�Ѵ򿪵�Դ
			// suspended,�ѹ���
			for (ElementDescription desc : almDesc
					.getVirtualMachinePowerState()) {
				almDescMap.put(desc.getKey(), desc.getLabel());
			}

		} catch (Exception e) {
			LOGGER.error("initAlarmDesc error, ", e);
		}
	}

	/**
	 * ��ȡ�澯������
	 * 
	 * @param descId
	 * @return
	 */
	public String getAlarmDesc(String descId) {
		try {
			return almDescMap.containsKey(descId) ? almDescMap.get(descId)
					: descId;
		} catch (Exception e) {
			LOGGER.error("getAlarmDesc error, ", e);
			return descId;
		}
	}

	/**
	 * ��ʼ���澯
	 */
	protected void initAlarm() {
		try {
			ExtendedAppUtil ecb = getLocalECB();

			ManagedObjectReference almMgr = ecb.getServiceConnection3()
					.getServiceContent().getAlarmManager();

			ManagedObjectReference[] almList = ecb.getServiceConnection3()
					.getService().getAlarm(almMgr, null);
			if (almList != null) {
				for (ManagedObjectReference almMor : almList) {
					// http://pubs.vmware.com/vsphere-50/topic/com.vmware.wssdk.apiref.doc_50/vim.alarm.Alarm.html
					AlarmInfo almInfo = (AlarmInfo) VIMMgr.getDynamicProperty(
							ecb, almMor, VIMConstants.DYNAMICPROPERTY_INFO);
					if (almInfo != null) {
						almMap.put(almMor.get_value(), almInfo);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("initAlarm error, ", e);
		}
	}

	/**
	 * ��ȡ�澯
	 * 
	 * @param id
	 * @return
	 */
	public AlarmInfo getAlarm(String id) {
		AlarmInfo almInfo = null;

		try {
			if (almMap.containsKey(id)) {
				almInfo = almMap.get(id);
			} else {
				initAlarm();
				almInfo = almMap.get(id);
			}
		} catch (Exception e) {
			LOGGER.error("getAlarm error, ", e);
		}
		return almInfo;
	}

	/**
	 * ��ʼ��ö��ֵ<br>
	 * ��ȷ��ϵͳ�Ƿ��ṩ�����ķ�����ȡ��ö��ֵ�ı��ػ�������
	 */
	protected void initEnum() {
		try {
			// VirtualMachineToolsRunningStatus
			// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.vm.GuestInfo.ToolsRunningStatus.html
			enumMap.put("guestToolsExecutingScripts", "��������");
			enumMap.put("guestToolsNotRunning", "δ����");
			enumMap.put("guestToolsRunning", "������");

			// VirtualMachineToolsVersionStatus
			// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.vm.GuestInfo.ToolsVersionStatus.html
			// enumMap.put("guestToolsBlacklisted", "Ӧ��������");//Since5.0
			enumMap.put("guestToolsCurrent", "�Ѱ�װ");
			enumMap.put("guestToolsNeedUpgrade", "������");
			enumMap.put("guestToolsNotInstalled", "δ��װ");
			// enumMap.put("guestToolsSupportedNew", ""); //Since5.0
			// enumMap.put("guestToolsSupportedOld", "");//Since5.0
			// enumMap.put("guestToolsTooNew", "");//Since5.0
			// enumMap.put("guestToolsTooOld", "");//Since5.0
			enumMap.put("guestToolsUnmanaged", "δ����");
		} catch (Exception e) {
			LOGGER.error("initEnum error, ", e);
		}
	}

	/**
	 * ��ȡö��ֵ
	 * 
	 * @param id
	 * @return
	 */
	public String getEnum(String id) {
		String value = id;

		try {
			if (enumMap.containsKey(id)) {
				value = enumMap.get(id);
			}
		} catch (Exception e) {
			LOGGER.error("getEnum error, ", e);
		}
		return value;
	}

	/**
	 * ��ʼ�����ܼ��<br>
	 */
	protected void initPerfInterval() {

		try {
			ExtendedAppUtil ecb = getLocalECB();

			ManagedObjectReference perfMgr = ecb.getServiceConnection3()
					.getServiceContent().getPerfManager();

			PerfInterval[] historicalInterval = (PerfInterval[]) VIMMgr
					.getDynamicProperty(ecb, perfMgr,
							DYNAMICPROPERTY_HISTORICALINTERVAL);
			if (historicalInterval != null) {
				for (PerfInterval perfInterval : historicalInterval) {
					String key = Integer.toString(perfInterval.getKey());

					// getKey=1
					// getLength=86400
					// getLevel=1
					// getName=��ȥһ��
					// getSamplingPeriod=300
					//					
					// getKey=2
					// getLength=604800
					// getLevel=1
					// getName=��ȥһ��
					// getSamplingPeriod=1800
					//					
					// getKey=3
					// getLength=2592000
					// getLevel=1
					// getName=��ȥһ����
					// getSamplingPeriod=7200
					//					
					// getKey=4
					// getLength=31536000
					// getLevel=1
					// getName=��ȥһ��
					// getSamplingPeriod=86400

					perfIntervalMap.put(key, perfInterval);
				}
			}
		} catch (Exception e) {
			LOGGER.error("initPerfInterval error, ", e);
		}
	}

	/**
	 * ��ȡ���ܼ��
	 * 
	 * @param id
	 * @return
	 */
	public PerfInterval getPerfInterval(String id) {
		PerfInterval value = null;
		try {
			if (perfIntervalMap.containsKey(id)) {
				value = perfIntervalMap.get(id);
			}
		} catch (Exception e) {
			LOGGER.error("getPerfInterval error, ", e);
		}
		return value;
	}

	/**
	 * ��ʼ�����ܼ�����<br>
	 */
	protected void initPerfCounterInfo() {

		try {
			ExtendedAppUtil ecb = getLocalECB();

			ManagedObjectReference perfMgr = ecb.getServiceConnection3()
					.getServiceContent().getPerfManager();

			PerfCounterInfo[] perfCounter = (PerfCounterInfo[]) VIMMgr
					.getDynamicProperty(ecb, perfMgr,
							DYNAMICPROPERTY_PERFCOUNTER);
			if (perfCounter != null) {
				for (PerfCounterInfo perfCounterInfo : perfCounter) {
					String key = perfCounterInfo.getGroupInfo().getKey()
							+ Util.SEPARATOR_PERIOD
							+ perfCounterInfo.getNameInfo().getKey()
							+ Util.SEPARATOR_PERIOD
							+ perfCounterInfo.getRollupType();
					perfCounterInfoMap.put(key, perfCounterInfo);
				}
			}
		} catch (Exception e) {
			LOGGER.error("initPerfCounterInfo error, ", e);
		}
	}

	/**
	 * ��ȡ���ܼ�����
	 * 
	 * @param id
	 * @return
	 */
	public PerfCounterInfo getPerfCounterInfo(String id) {
		PerfCounterInfo value = null;
		try {
			if (perfCounterInfoMap.containsKey(id)) {
				value = perfCounterInfoMap.get(id);
			}
		} catch (Exception e) {
			LOGGER.error("getPerfCounterInfo error, ", e);
		}
		return value;
	}

}
