package com.afunms.vmware.vim25.mgr;

import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.NetworkConstants;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.ManagedEntityStatus;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NetworkSummary;

/**
 * �������,<br>
 * 
 * ���в������б�μ�<br>
 * http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.
 * doc_50%2Fvim.Network.html�е�����<br>
 * 
 * @author LXL
 * 
 */
public class NetworkMgr extends VIMMgr implements NetworkConstants {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(NetworkMgr.class);

	/**
	 * ��¼�����б�
	 * 
	 * @param resultMap
	 * @param ecb
	 * @param netList
	 */
	public static void recordResultMapNetList(
			HashMap<String, Object> resultMap, ExtendedAppUtil ecb,
			ManagedObjectReference[] netList) {
		try {
			// �����б�
			ArrayList<HashMap<String, String>> netResultList = new ArrayList<HashMap<String, String>>();
			resultMap.put(SUMMARY_RESOURCE_NET_LIST, netResultList);
			if (netList != null) {
				for (ManagedObjectReference netMor : netList) {
					ManagedEntityStatus overallStatus = (ManagedEntityStatus) VIMMgr
							.getDynamicProperty(ecb, netMor,
									DYNAMICPROPERTY_OVERALLSTATUS);
					Boolean alarmActionsEnabled = (Boolean) VIMMgr
							.getDynamicProperty(ecb, netMor,
									DYNAMICPROPERTY_ALARMACTIONSENABLED);
					NetworkSummary netSummary = (NetworkSummary) VIMMgr
							.getDynamicProperty(ecb, netMor,
									DYNAMICPROPERTY_SUMMARY);

					// �����б��е�Map��key
					HashMap<String, String> netResultMap = new HashMap<String, String>();
					netResultList.add(netResultMap);

					// ״̬
					if (overallStatus != null) {
						netResultMap.put(SUMMARY_RESOURCE_NET_STATUS,
								overallStatus.getValue());
					} else {
						netResultMap.put(SUMMARY_RESOURCE_NET_STATUS, "");
					}
					// ��������
					if (alarmActionsEnabled != null) {
						netResultMap.put(SUMMARY_RESOURCE_NET_ALARMACTIONS,
								alarmActionsEnabled.toString());
					} else {
						netResultMap.put(SUMMARY_RESOURCE_NET_ALARMACTIONS, "");
					}

					// ����
					if (netSummary != null) {
						netResultMap.put(SUMMARY_RESOURCE_NET_NAME, netSummary
								.getName());
					} else {
						netResultMap.put(SUMMARY_RESOURCE_NET_NAME, "");

					}
					// XXX ����,��ʱ�޷���ȡ
					// String SUMMARY_RESOURCE_NET_TYPE = "nettype";
				}
			}
		} catch (Exception e) {
			LOGGER.error("recordResultMapNetList error, ", e);
		}
	}
}
