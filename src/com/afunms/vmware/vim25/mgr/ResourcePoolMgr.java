package com.afunms.vmware.vim25.mgr;

import com.afunms.vmware.vim25.cache.VIMCache;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.ResourcePoolConstants;
import com.afunms.vmware.vim25.constants.VMConstants;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VirtualMachineRuntimeInfo;

/**
 * ��Դ�ع���,<br>
 * 
 * ���в������б�μ�<br>
 * http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.
 * doc_50%2Fvim.ResourcePool.html�е�����<br>
 * 
 * @author LXL
 * 
 */
public class ResourcePoolMgr extends VIMMgr implements ResourcePoolConstants {

	// LOGGER
	private final static Logger LOGGER = Logger
			.getLogger(ResourcePoolMgr.class);

	/**
	 * ��ȡ��Դ�ص�ժҪ��Ϣ
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param rpId
	 * @return
	 */
	public static Map<String, Object> getSummary(String url, String username,
			String password, String rpId) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getResourcePool(rpId);
			if (mor != null) {
				ExtendedAppUtil ecb = VIMMgr.getECB(url, username, password);

				ManagedObjectReference[] rpList = (ManagedObjectReference[]) VIMMgr
						.getDynamicProperty(ecb, mor,
								DYNAMICPROPERTY_RESOURCEPOOL);

				ManagedObjectReference[] vmList = (ManagedObjectReference[]) VIMMgr
						.getDynamicProperty(ecb, mor, DYNAMICPROPERTY_VM);

				int rpNum = 0;
				int vAppNum = 0;
				if (rpList != null) {
					for (ManagedObjectReference rpMor : rpList) {
						// �ж�����ΪResourcePool��VirtualApp�ֱ�ͳ������Դ�غ���vApp�ĸ���
						if (rpMor.getType().equals(RESOURCE_RP)) {
							rpNum++;
						}
						if (rpMor.getType().equals(RESOURCE_VAPP)) {
							vAppNum++;
						}
					}
				}
				resultMap.put(SUMMARY_COMMON_RP, rpNum);
				resultMap.put(SUMMARY_COMMON_VAPP, vAppNum);

				int vmNum = 0;
				int openVMNum = 0;
				if (vmList != null) {
					// ���������
					vmNum = vmList.length;
					// �������е������,�ж�������ĵ�Դ״̬
					for (ManagedObjectReference vmMor : vmList) {
						VirtualMachineRuntimeInfo runtime = (VirtualMachineRuntimeInfo) VIMMgr
								.getDynamicProperty(ecb, vmMor,
										DYNAMICPROPERTY_RUNTIME);
						if ((runtime.getPowerState() != null)
								&& (runtime.getPowerState().getValue()
										.equals(VMConstants.POWERSTATE_POWERON))) {
							openVMNum++;
						}
					}
				}
				resultMap.put(SUMMARY_COMMON_VM, vmNum);
				resultMap.put(SUMMARY_COMMON_OPENVM, openVMNum);

				// vCenter������CPU���ڴ�������ѻ����ҵ���ResourcePoolMgrTest.testPrintRPSummary����˵��

				// ��¼������ȷ�ı�ʶ
				recordResultMapSuccess(resultMap);
			} else {
				recordResultMapError(resultMap, "��Դ��'" + rpId + "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getSummary error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}
}
