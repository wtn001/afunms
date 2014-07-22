package com.afunms.vmware.vim25.mgr;

import com.afunms.vmware.vim25.cache.VIMCache;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.HostConstants;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.HostHardwareInfo;
import com.vmware.vim25.HostListSummary;
import com.vmware.vim25.ManagedObjectReference;

/**
 * ��������,<br>
 * 
 * ���в������б�μ�<br>
 * http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.
 * doc_50%2Fvim.HostSystem.html�е�����<br>
 * 
 * @author LXL
 * 
 */
public class HostMgr extends VIMMgr implements HostConstants {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(HostMgr.class);

	/**
	 * ��ȡ������ժҪ��Ϣ
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param hoId
	 * @return
	 */
	public static Map<String, Object> getSummary(String url, String username,
			String password, String hoId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getHostSystem(hoId);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// �����
				ManagedObjectReference[] vmList = (ManagedObjectReference[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_VM);

				// Ӳ��
				HostHardwareInfo hardware = (HostHardwareInfo) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_HARDWARE);

				// ժҪ
				HostListSummary summary = (HostListSummary) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_SUMMARY);

				// �洢�б�
				ManagedObjectReference[] dsList = (ManagedObjectReference[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_DATASTORE);

				// �����б�
				ManagedObjectReference[] netList = (ManagedObjectReference[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_NETWORK);

				// ժҪMap�е�keyֵ
				// ����
				// ������
				String vendor = "";
				// �ͺ�,����:PowerEdge R710
				String model = "";
				// CPU�ں�
				String cpucore = "";
				// ����������
				String cpumodel = "";
				// XXX ���֤
				// ���������
				String numcpupkgs = "0";
				// ÿ����۵��ں���
				String numcorespersocket = "0";
				// �߼�������
				String numcputhreads = "";
				// XXX ���߳�(�),��ʱ�޷���ȡ
				// ������Ŀ
				String numnics = "0";
				// ״��(������)
				String connectionstate = "";
				// �������ģ��
				String vm = "0";
				// vMotion������(��)
				String vmotion = "";
				// XXX VMWare EVCģʽ,��ʱ�޷���ȡ,summary.getCurrentEVCModeKey()
				// XXX vSphere HA״��(��������(����)),��ʱ�޷���ȡ,
				// summary.getRuntime().getDasHostState().getState()=master
				// XXX ������FT������(��)
				// summary.getConfig().getFaultToleranceEnabled()
				// XXX �����,��ʱ�޷���ȡ
				// XXX ���������ļ�,��ʱ�޷���ȡ
				// XXX ӳ�������ļ�,��ʱ�޷���ȡ
				// XXX �����ļ��Ϲ���(������),��ʱ�޷���ȡ
				// XXX DirectPath I/O(����֧��),��ʱ�޷���ȡ

				// ��Դ
				// CPUʹ�����,��λMHz
				String overallcpuusage = "0";
				// �ڴ�ʹ�����,��λMB
				String overallmemoryusage = "0";
				// �ڴ�����,��λMB
				String memorysizemb = "0";

				// Fault Tolerance
				// XXX Fault Tolerance �汾,��ʱ�޷���ȡ
				// XXX �����������,��ʱ�޷���ȡ
				// XXX �Ѵ򿪵�Դ�������������,��ʱ�޷���ȡ
				// XXX �������������,��ʱ�޷���ȡ
				// XXX �Ѵ򿪵�Դ�ĸ������������,��ʱ�޷���ȡ

				if (hardware != null) {
					vendor = hardware.getSystemInfo().getVendor();
					model = hardware.getSystemInfo().getModel();
				}

				if (summary != null) {
					cpucore = summary.getHardware().getNumCpuCores() + " �� "
							+ summary.getHardware().getCpuMhz() + MHZ;
					cpumodel = summary.getHardware().getCpuModel();
					numcpupkgs = Integer.toString(summary.getHardware()
							.getNumCpuPkgs());
					numcorespersocket = Integer.toString(summary.getHardware()
							.getNumCpuCores()
							/ summary.getHardware().getNumCpuPkgs());
					numcputhreads = Integer.toString(summary.getHardware()
							.getNumCpuThreads());
					numnics = Integer.toString(summary.getHardware()
							.getNumNics());
					connectionstate = summary.getRuntime().getConnectionState()
							.toString();
					vmotion = Boolean.toString(summary.getConfig()
							.isVmotionEnabled());
					// ft = summary.getConfig().getFaultToleranceEnabled()
					// .toString();

					overallcpuusage = Integer.toString(summary.getQuickStats()
							.getOverallCpuUsage());
					overallmemoryusage = Integer.toString(summary
							.getQuickStats().getOverallMemoryUsage());
					memorysizemb = getByteSizeStrFromMB(convertBytes2MBLong(summary
							.getHardware().getMemorySize()));
				}

				if (vmList != null) {
					vm = Integer.toString(vmList.length);
				}

				// ����
				resultMap.put(SUMMARY_COMMON_VENDOR, vendor);
				resultMap.put(SUMMARY_COMMON_MODEL, model);
				resultMap.put(SUMMARY_COMMON_CPUCORE, cpucore);
				resultMap.put(SUMMARY_COMMON_CPUMODEL, cpumodel);
				resultMap.put(SUMMARY_COMMON_NUMCPUKGS, numcpupkgs);
				resultMap.put(SUMMARY_COMMON_NUMCORESPERSOCKET,
						numcorespersocket);
				resultMap.put(SUMMARY_COMMON_NUMCPUTHREADS, numcputhreads);
				resultMap.put(SUMMARY_COMMON_NUMNICS, numnics);
				resultMap.put(SUMMARY_COMMON_CONNECTIONSTATE, connectionstate);
				resultMap.put(SUMMARY_COMMON_VM, vm);
				resultMap.put(SUMMARY_COMMON_VMOTION, vmotion);

				// ��Դ
				resultMap
						.put(SUMMARY_RESOURCE_OVERALLCPUUSAGE, overallcpuusage);
				resultMap.put(SUMMARY_RESOURCE_OVERALLMEMORYUSAGE,
						overallmemoryusage);
				resultMap.put(SUMMARY_RESOURCE_MEMORYSIZEMB, memorysizemb);

				// �洢�б�
				DatastoreMgr.recordResultMapDsList(resultMap, ecb, dsList);

				// �����б�
				NetworkMgr.recordResultMapNetList(resultMap, ecb, netList);

				// ��¼������ȷ�ı�ʶ
				recordResultMapSuccess(resultMap);
			} else {
				recordResultMapError(resultMap, "����'" + hoId + "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getSummary error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}
}
