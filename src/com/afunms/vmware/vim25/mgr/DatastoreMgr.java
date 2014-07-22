package com.afunms.vmware.vim25.mgr;

import com.afunms.vmware.vim25.cache.VIMCache;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.DatastoreConstants;
import com.afunms.vmware.vim25.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.DatastoreHostMount;
import com.vmware.vim25.DatastoreInfo;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.ManagedObjectReference;

/**
 * �洢����,<br>
 * 
 * ���в������б�μ�<br>
 * http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.
 * doc_50%2Fvim.Datastore.html�е�����<br>
 * 
 * @author LXL
 * 
 */
public class DatastoreMgr extends VIMMgr implements DatastoreConstants {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(DatastoreMgr.class);

	/**
	 * ��ȡ�洢��ժҪ��Ϣ
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dsId
	 * @return
	 */
	public static Map<String, Object> getSummary(String url, String username,
			String password, String dsId) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getDatastore(dsId);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// ��Ϣ
				DatastoreInfo info = (DatastoreInfo) getDynamicProperty(ecb,
						mor, DYNAMICPROPERTY_INFO);

				// ժҪ
				DatastoreSummary summary = (DatastoreSummary) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_SUMMARY);

				// ���������б�
				DatastoreHostMount[] hostMountList = (DatastoreHostMount[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_HOST);

				// ������б�
				ManagedObjectReference[] vmList = (ManagedObjectReference[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_VM);

				// ժҪMap�е�keyֵ
				// ����
				// λ��
				String dsurl = "";
				// ����
				String type = "";
				// ������������Ŀ
				String hostNum = "0";
				// �������ģ��
				String vmNum = "0";

				// ����
				// ����
				String capacity = "0";
				// �ñ��Ŀռ�=capacity+uncommitted-freespace
				String storageall = "0";
				// ���ÿռ�
				String freespace = "0";
				// �ϴθ���ʱ��
				String timestamp = "";

				if (info != null) {
					timestamp = Util.getDBDateTime(info.getTimestamp()
							.getTimeInMillis());
				}

				if (summary != null) {
					dsurl = summary.getUrl();
					type = summary.getType();
					capacity = getByteSizeStrFromBytes(summary.getCapacity());
					freespace = getByteSizeStrFromBytes(summary.getFreeSpace());
					storageall = getByteSizeStrFromBytes(summary.getCapacity()
							+ summary.getUncommitted() - summary.getFreeSpace());
				}

				if (hostMountList != null) {
					hostNum = Integer.toString(hostMountList.length);
				}

				if (vmList != null) {
					vmNum = Integer.toString(vmList.length);
				}

				// �������е�ֵ
				resultMap.put(SUMMARY_COMMON_URL, dsurl);
				resultMap.put(SUMMARY_COMMON_TYPE, type);
				resultMap.put(SUMMARY_COMMON_HOST, hostNum);
				resultMap.put(SUMMARY_COMMON_VM, vmNum);
				resultMap.put(SUMMARY_CAPACITY_CAPACITY, capacity);
				resultMap.put(SUMMARY_CAPACITY_STORAGEALL, storageall);
				resultMap.put(SUMMARY_CAPACITY_FREESPACE, freespace);
				resultMap.put(SUMMARY_CAPACITY_TIMESTAMP, timestamp);

				// ��¼������ȷ�ı�ʶ
				recordResultMapSuccess(resultMap);
			} else {
				recordResultMapError(resultMap, "���ݴ洢'" + dsId + "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getSummary error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ��¼�洢�б�
	 * 
	 * @param resultMap
	 * @param ecb
	 * @param dsList
	 */
	public static void recordResultMapDsList(HashMap<String, Object> resultMap,
			ExtendedAppUtil ecb, ManagedObjectReference[] dsList) {
		try {
			// �洢���б�
			ArrayList<HashMap<String, String>> dsResultList = new ArrayList<HashMap<String, String>>();
			resultMap.put(SUMMARY_RESOURCE_DS_LIST, dsResultList);
			if (dsList != null) {
				for (ManagedObjectReference dsMor : dsList) {
					ManagedEntityStatus overallStatus = (ManagedEntityStatus) VIMMgr
							.getDynamicProperty(ecb, dsMor,
									DYNAMICPROPERTY_OVERALLSTATUS);
					Boolean alarmActionsEnabled = (Boolean) VIMMgr
							.getDynamicProperty(ecb, dsMor,
									DYNAMICPROPERTY_ALARMACTIONSENABLED);
					DatastoreInfo dsInfo = (DatastoreInfo) VIMMgr
							.getDynamicProperty(ecb, dsMor,
									DYNAMICPROPERTY_INFO);
					DatastoreSummary dsSummary = (DatastoreSummary) VIMMgr
							.getDynamicProperty(ecb, dsMor,
									DYNAMICPROPERTY_SUMMARY);

					// �洢���б��е�Map��key
					HashMap<String, String> dsResultMap = new HashMap<String, String>();
					dsResultList.add(dsResultMap);

					if (dsInfo != null) {
						// �洢��
						dsResultMap.put(SUMMARY_RESOURCE_DS_NAME, dsInfo
								.getName());
						// ���ÿռ�
						dsResultMap.put(SUMMARY_RESOURCE_DS_FREESPACE,
								getByteSizeStrFromBytes(dsInfo.getFreeSpace()));
						// �ϴθ���
						dsResultMap.put(SUMMARY_RESOURCE_DS_TIME, Util
								.getDBDateTime(dsInfo.getTimestamp()
										.getTimeInMillis()));
					} else {
						dsResultMap.put(SUMMARY_RESOURCE_DS_NAME, "");
						dsResultMap.put(SUMMARY_RESOURCE_DS_FREESPACE, "");
						dsResultMap.put(SUMMARY_RESOURCE_DS_TIME, "");
					}

					// ����
					if (dsSummary != null) {
						dsResultMap
								.put(SUMMARY_RESOURCE_DS_CAPACITY,
										getByteSizeStrFromBytes(dsSummary
												.getCapacity()));
					} else {
						dsResultMap.put(SUMMARY_RESOURCE_DS_CAPACITY, "");
					}
					// ״̬
					if (overallStatus != null) {
						dsResultMap.put(SUMMARY_RESOURCE_DS_STATUS,
								overallStatus.getValue());
					} else {
						dsResultMap.put(SUMMARY_RESOURCE_DS_STATUS, "");
					}
					// ��������
					if (alarmActionsEnabled != null) {
						dsResultMap.put(SUMMARY_RESOURCE_DS_ALARMACTIONS,
								alarmActionsEnabled.toString());
					} else {
						dsResultMap.put(SUMMARY_RESOURCE_DS_ALARMACTIONS, "");
					}
					// XXX ����������,��ʱ�޷���ȡ
					// ֵ�з�SSD,
					// ��ssd�йص�QueryConfigTarget����ConfigTarget.scsiDisk(VirtualMachineScsiDiskDeviceInfo).disk(HostScsiDisk).ssd
					// String SUMMARY_RESOURCE_DS_DRIVETYPE =
					// "dsdrivetype";
					// XXX Storage I/O Control,��ʱ�޷���ȡ
					// String SUMMARY_RESOURCE_DS_IOCTRL = "dsioctrl";
				}
			}
		} catch (Exception e) {
			LOGGER.error("recordResultMapDsList error, ", e);
		}
	}
}
