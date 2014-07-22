package com.afunms.vmware.vim25.mgr;

import com.afunms.vmware.vim25.cache.VIMCache;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.ComputeResourceConstants;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.ClusterConfigInfoEx;
import com.vmware.vim25.ComputeResourceConfigInfo;
import com.vmware.vim25.ComputeResourceSummary;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.ManagedObjectReference;

/**
 * Ⱥ������,<br>
 * 
 * ���в������б�μ�<br>
 * http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.
 * doc_50%2Fvim.ComputeResource.html�е�����<br>
 * 
 * @author LXL
 * 
 */
public class ComputeResourceMgr extends VIMMgr implements
		ComputeResourceConstants {

	// LOGGER
	private final static Logger LOGGER = Logger
			.getLogger(ComputeResourceMgr.class);

	/**
	 * ��ȡȺ����ժҪ��Ϣ
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param crId
	 * @return
	 */
	public static Map<String, Object> getSummary(String url, String username,
			String password, String crId) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getClusterComputeResource(crId);
			if (mor == null) {
				mor = cache.getComputeResource(crId);
			}

			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// ����
				ComputeResourceConfigInfo config = (ComputeResourceConfigInfo) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_CONFIGURATIONEX);

				// ���ݴ洢
				ManagedObjectReference[] dsList = (ManagedObjectReference[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_DATASTORE);

				// ժҪ
				ComputeResourceSummary summary = (ComputeResourceSummary) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_SUMMARY);

				// // ����Դ��
				// ManagedObjectReference rpMor = (ManagedObjectReference)
				// VIMMgr
				// .getDynamicProperty(ecb, mor,
				// DYNAMICPROPERTY_RESOURCEPOOL);

				// ����
				ManagedObjectReference[] hoList = (ManagedObjectReference[]) VIMMgr
						.getDynamicProperty(ecb, mor, DYNAMICPROPERTY_HOST);

				// ժҪMap�е�keyֵ
				// ����
				// vSphere DRS
				String drs = "";
				// vSphere HA
				String ha = "";
				if ((config != null) && (config instanceof ClusterConfigInfoEx)) {
					ClusterConfigInfoEx configEx = (ClusterConfigInfoEx) config;
					drs = configEx.getDrsConfig().getEnabled().toString();
					ha = configEx.getDasConfig().getEnabled().toString();
				}
				resultMap.put(SUMMARY_COMMON_DRS, drs);
				resultMap.put(SUMMARY_COMMON_HA, ha);
				// XXX VMWare EVC ģʽ,��ʱ�޷���ȡ
				// String SUMMARY_COMMON_EVC = "evc";

				// ��CPU��Դ,,��λMHz
				String totalcpu = "0";
				// ���ڴ�,��λ�ֽ�
				String totalmemory = "0";
				// �ܴ洢
				String totaldssizemb = "0";
				// ������summary.getNumHosts(��Ч������summary.getNumEffectiveHosts)
				String numhosts = "0";
				// ����������
				String numcpucores = "0";
				// ���ݴ洢Ⱥ����
				String numstoragepods = "0";
				// �����ݴ洢��
				String numdss = "0";
				// �������ģ��
				String numvms = "0";

				if (summary != null) {
					totalcpu = Integer.toString(summary.getTotalCpu());
					totalmemory = convertBytes2MBString(summary
							.getTotalMemory());
					numhosts = Integer.toString(summary.getNumHosts());
					numcpucores = Integer.toString(summary.getNumCpuCores());
				}

				long totalds = 0;
				int numsps = 0;
				if (dsList != null) {
					numdss = Integer.toString(dsList.length);

					for (ManagedObjectReference dsMor : dsList) {
						if (dsMor.getType().equals(RESOURCE_STORAGEPOD)) {
							numsps++;
						}
						// �洢ժҪ
						DatastoreSummary dsSummary = (DatastoreSummary) getDynamicProperty(
								ecb, dsMor, DYNAMICPROPERTY_SUMMARY);
						totalds += dsSummary.getCapacity();
					}
				}
				numstoragepods = Integer.toString(numsps);
				totaldssizemb = convertBytes2MBString(totalds);

				// ���ͨ����Դ�ؼ���,��Ҫ��ȡ����Դ�ص�����������Լ�����Դ����������е���Դ�ص����������,��Ҫ�õݹ��㷨
				// if (rpMor != null) {
				// ManagedObjectReference[] vmList =
				// (ManagedObjectReference[]) VIMMgr
				// .getDynamicProperty(ecb, rpMor, DYNAMICPROPERTY_VM);
				// //����Դ�ص��������
				// numvms = Integer.toString(vmList.length);
				// // ����Ҫ��������Դ���е��������
				// }

				// ͨ����������
				int vmNum = 0;
				if (hoList != null) {
					for (ManagedObjectReference hoMor : hoList) {
						ManagedObjectReference[] vmList = (ManagedObjectReference[]) VIMMgr
								.getDynamicProperty(ecb, hoMor,
										DYNAMICPROPERTY_VM);
						if (vmList != null) {
							vmNum += vmList.length;
						}
					}
				}
				numvms = Integer.toString(vmNum);

				resultMap.put(SUMMARY_COMMON_TOTALCPU, totalcpu);
				resultMap.put(SUMMARY_COMMON_TOTALMEMORY, totalmemory);
				resultMap.put(SUMMARY_COMMON_TOTALDSSIZEMB, totaldssizemb);
				resultMap.put(SUMMARY_COMMON_NUMHOSTS, numhosts);
				resultMap.put(SUMMARY_COMMON_NUMCPUCORES, numcpucores);
				resultMap.put(SUMMARY_COMMON_NUMSTORAGEPODS, numstoragepods);
				resultMap.put(SUMMARY_COMMON_NUMDSS, numdss);
				resultMap.put(SUMMARY_COMMON_NUMVMS, numvms);

				// XXX ʹ��vMotion����Ǩ����,��ʱ�޷���ȡ
				// String SUMMARY_COMMON_NUMVMOTIONS = "numvmotions";

				// XXX vSphere HA,��ʱ�޷���ȡ,
				// Ӧ���Ƕ�ӦClusterDasConfigInfo��

				// XXX vSphere DRS,,��ʱ�޷���ȡ,
				// Ӧ���Ƕ�ӦClusterDrsConfigInfo��

				// ��¼������ȷ�ı�ʶ
				recordResultMapSuccess(resultMap);
			} else {
				recordResultMapError(resultMap, "����Ⱥ��'" + crId + "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getSummary error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}
}
