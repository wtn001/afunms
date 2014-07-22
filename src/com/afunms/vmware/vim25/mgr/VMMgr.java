package com.afunms.vmware.vim25.mgr;

import com.afunms.vmware.vim25.cache.VIMCache;
import com.afunms.vmware.vim25.common.VIMMgr;
import com.afunms.vmware.vim25.constants.VMConstants;
import com.afunms.vmware.vim25.util.Util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vmware.apputils.version.ExtendedAppUtil;
import com.vmware.vim25.DatastoreSummary;
import com.vmware.vim25.FileFault;
import com.vmware.vim25.InvalidDatastore;
import com.vmware.vim25.InvalidPowerState;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.NotSupported;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.ToolsUnavailable;
import com.vmware.vim25.VirtualCdrom;
import com.vmware.vim25.VirtualCdromRemotePassthroughBackingInfo;
import com.vmware.vim25.VirtualDevice;
import com.vmware.vim25.VirtualDeviceConfigSpec;
import com.vmware.vim25.VirtualDeviceConfigSpecFileOperation;
import com.vmware.vim25.VirtualDeviceConfigSpecOperation;
import com.vmware.vim25.VirtualDeviceConnectInfo;
import com.vmware.vim25.VirtualDisk;
import com.vmware.vim25.VirtualDiskFlatVer2BackingInfo;
import com.vmware.vim25.VirtualEthernetCard;
import com.vmware.vim25.VirtualEthernetCardNetworkBackingInfo;
import com.vmware.vim25.VirtualIDEController;
import com.vmware.vim25.VirtualLsiLogicController;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineConfigOption;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineFileInfo;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.VirtualPCNet32;
import com.vmware.vim25.VirtualSCSISharing;
import com.vmware.vim25.VmConfigFault;

/**
 * ���������,<br>
 * ���ݵĻ�ȡ����vCenter�еĽ���,����ժҪ������(ʵʱ����ʷ)���������¼���������<br>
 * ���������ɾ�Ĳ�������CreateVM_Task��CloneVM_Task��ReconfigVM_Task��Destroy_Task��<br>
 * �������״̬��������PowerOffVM_Task��PowerOnVM_Task��SuspendVM_Task��ResetVM_Task��
 * ShutdownGuest��StandbyGuest��RebootGuest��<br>
 * 
 * ���в������б�μ�<br>
 * http://pubs.vmware.com/vsphere-50/topic/com.vmware.wssdk.apiref.doc_50/vim.
 * VirtualMachine.html�е�����<br>
 * 
 * @author LXL
 * 
 */
public class VMMgr extends VIMMgr implements VMConstants {

	// LOGGER
	private final static Logger LOGGER = Logger.getLogger(VMMgr.class);

	/**
	 * ���������,CreateVM_Task
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param haId
	 * @param rpId
	 * @param name
	 * @param descr
	 * @param guestId
	 * @param numCpus
	 * @param numCores
	 * @param memoryMB
	 * @param dsId
	 * @param diskSizeMB
	 * @return
	 */
	public static Map<String, Object> createVM(String url, String username,
			String password, String dcId, String haId, String rpId,
			String name, String descr, String guestId, int numCpus,
			int numCores, long memoryMB, String dsId, long diskSizeMB) {
		// CreateVM_Task
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.Folder.html&path=5_0_2_5_3_75

		// VirtualMachineConfigSpec
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.vm.ConfigSpec.html

		// 1.VimBindingStub����ִ��createVM_Task����
		// ��4������,_this,config,pool,host
		// ����host���Բ�ָ��,����pool��Ҫ��Ȩ��Resource.AssignVMToPool

		// 1._this:��dc��ȡһ��vmFolder
		// ��˾������vmFolder���νṹitimsDC(datacenter-2)-->vm(group-v3)
		// ��˾�����е�����Ⱥ�����Խ��itimsDC(datacenter-2)-->host,group-h4(datacenter-2)-->itimsHA(domain-c73)
		// �ڹ���ϵͳ�е��ƶ�Ӧ����Ⱥ��,�ڱ�������Ⱥ����idͬʱҲ����dc��id
		// ManagedObjectReference vmFolderMor =
		// cb.getServiceUtil().getMoRefProp(
		// dcmor, "vmFolder");

		// 2.config:VirtualMachineConfigSpec���������

		// 3.pool:��dc��hostFolder�����»�ȡComputeResource
		// ��˾������ComputeResource���νṹitimsDC(datacenter-2)-->host(group-h4)-->itimsHA(domain-c73)-->Resources(resgroup-74)-->testRP(resgroup-121)
		// vim����:������Ŀ¼�л�ȡ������Դ
		// ManagedObjectReference hfmor =
		// cb.getServiceUtil().getMoRefProp(dcmor,
		// "hostFolder");
		// ArrayList crmors = cb.getServiceUtil().getDecendentMoRefs(hfmor,
		// "ComputeResource");

		// 4.host:������ָ��,�ɼ�����Դ�Զ�����

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);

			// ��ȡ_this��pool
			ManagedObjectReference dcMor = cache.getDatacenter(dcId);
			// ��ȡ���������µ�������ļ���
			ManagedObjectReference vmFolderMor = cache.getVMFolder(dcId);
			// ��ȡ����Ⱥ��
			ManagedObjectReference haMor = cache.getComputeResource(haId);
			if (haMor == null) {
				haMor = cache.getClusterComputeResource(haId);
			}
			// ��ȡ��Դ��
			ManagedObjectReference rpMor = cache.getResourcePool(rpId);
			// ��ȡ���ݴ洢
			ManagedObjectReference dsMor = cache.getDatastore(dsId);

			if ((dcMor != null) && (vmFolderMor != null) && (haMor != null)
					&& (rpMor != null) && (dsMor != null)) {

				ExtendedAppUtil ecb = getECB(url, username, password);

				// ����VirtualMachineConfigSpec
				// http://pubs.vmware.com/vsphere-50/topic/com.vmware.wssdk.apiref.doc_50/vim.vm.ConfigSpec.html
				VirtualMachineConfigSpec vmConfig = createVmConfigSpec(ecb,
						dcMor, haMor, name, descr, guestId, numCpus, numCores,
						memoryMB, dsMor, diskSizeMB);

				ManagedObjectReference taskMor = ecb.getServiceConnection3()
						.getService().createVM_Task(vmFolderMor, vmConfig,
								rpMor, null);

				execTaskAndRecordResultMap(ecb, taskMor, resultMap);

				// �������������ɹ�,��ȡ�������Ӧ������id
				if (Util.normalizeObject(resultMap.get(INFO_STATE)).toString()
						.equals(STATE_OK)) {
					String vmId = Util.normalizeObject(
							resultMap.get(INFO_RESULT)).toString();
					if (!vmId.equals("")) {
						ManagedObjectReference vmMor = cache
								.getVirtualMachine(vmId);
						Object runtime = getDynamicProperty(ecb, vmMor,
								DYNAMICPROPERTY_RUNTIME);
						if ((runtime != null)
								&& (runtime instanceof VirtualMachineRuntimeInfo)) {
							VirtualMachineRuntimeInfo runtimeInfo = (VirtualMachineRuntimeInfo) runtime;
							if (runtimeInfo.getHost() != null) {
								String hoId = runtimeInfo.getHost().get_value();
								resultMap.put(INFO_RESULT2, hoId);
							}
						}
					}
				}
			} else {
				if (dcMor == null) {
					recordResultMapError(resultMap, "��������'" + dcId + "'������");
				}
				if (vmFolderMor == null) {
					recordResultMapError(resultMap, "��������'" + dcId
							+ "'��vmFolder������");
				}
				if (haMor == null) {
					recordResultMapError(resultMap, "����Ⱥ��'" + haId + "'������");
				}
				if (rpMor == null) {
					recordResultMapError(resultMap, "'��Դ��" + rpId + "'������");
				}
				if (dsMor == null) {
					recordResultMapError(resultMap, "'���ݴ洢" + dsId + "'������");
				}
			}
		} catch (Exception e) {
			LOGGER.error("createVM error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * �����������������Ϣ
	 * 
	 * @param ecb
	 * @param dcMor
	 * @param haMor
	 * @param name
	 * @param descr
	 * @param guestId
	 * @param numCpus
	 * @param numCores
	 * @param memoryMB
	 * @param dsMor
	 * @param diskSizeMB
	 * @return
	 * @throws Exception
	 */
	private static VirtualMachineConfigSpec createVmConfigSpec(
			ExtendedAppUtil ecb, ManagedObjectReference dcMor,
			ManagedObjectReference haMor, String name, String descr,
			String guestId, int numCpus, int numCores, long memoryMB,
			ManagedObjectReference dsMor, long diskSizeMB) throws Exception {

		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.VirtualDiskManager.VirtualDiskType.html
		// ���ñ��ӳ����� ��zeroed thick�� //Ĭ��ѡ��
		// thick=zeroedthick
		// ���ñ����� ��eager zeroed thick��,������ʱ���ر�ĳ���
		// eagerZeroedThick
		// �����ñ� ��thin��
		// thin
		// ���ñ��ӳ����㣺��Ĭ�ϵĺ��ʽ����������̡�����������Ϊ������̷�������ռ䡣����ʱ������������豸�ϱ������κ����ݣ������Ժ��������״�ִ��д����ʱ�ᰴ��Ҫ�������㡣
		// ���ñ����㣺����֧��Ⱥ�����ܣ��� Fault
		// Tolerance���ĺ���̡��ڴ���ʱΪ������̷�������Ŀռ䡣��ƽ���ʽ�෴���ڴ��������лὫ�����豸�ϱ������������㡣�������ָ�ʽ�Ĵ��������ʱ����ܻ�ȴ����������͵Ĵ��̳���
		// �����ñ���ʹ�þ����ñ���ʽ������������ñ��Ĵ���ֻʹ�øô����������Ҫ�����ݴ洢�ռ䡣����Ժ󾫼������Ҫ����ռ䣬��������������Ϊ���������������

		// 0-thick��1-eagerZeroedThick����0-thick��getEagerlyScrub=null;1-eagerZeroedThick��getEagerlyScrub=true
		// 0-thick��2-thin����0-thick��getThinProvisioned=false;2-thin��getThinProvisioned=true
		// 1-eagerZeroedThickh��2-thin����1-eagerZeroedThickh��getEagerlyScrub=true,getThinProvisioned=false;2-thin��getEagerlyScrub=null,getThinProvisioned=true

		String diskType = DISKTYPE_THICK;

		VirtualMachineConfigSpec vmConfig = new VirtualMachineConfigSpec();

		// ����
		vmConfig.setName(name);
		// ��ע
		vmConfig.setAnnotation(descr);
		// ����ϵͳid
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.vm.GuestOsDescriptor.GuestOsIdentifier.html
		vmConfig.setGuestId(guestId);
		// CPU
		vmConfig.setNumCPUs(numCpus);
		vmConfig.setNumCoresPerSocket(numCores);
		// �ڴ�
		vmConfig.setMemoryMB(memoryMB);

		// ����������е��豸,�����,����,���̵�
		VirtualDeviceConfigSpec[] deviceConfigs = new VirtualDeviceConfigSpec[4];
		vmConfig.setDeviceChange(deviceConfigs);

		// SCSI������
		// --getBusNumber=0
		// --getScsiCtlrUnitNumber=7
		// --getHotAddRemove=true
		// --getSharedBus=noSharing
		// --getKey=1000
		// --getControllerKey=100
		// --getUnitNumber=3
		// --getBacking=null
		// --getConnectable=null

		VirtualDeviceConfigSpec scsiConfig = new VirtualDeviceConfigSpec();
		deviceConfigs[0] = scsiConfig;
		scsiConfig.setOperation(VirtualDeviceConfigSpecOperation.add);
		VirtualLsiLogicController scsiCtrl = new VirtualLsiLogicController();
		scsiConfig.setDevice(scsiCtrl);
		scsiCtrl.setBusNumber(0);
		int scsiCtlrKey = 20;
		scsiCtrl.setKey(scsiCtlrKey);
		// ������������VirtualSCSISharing.physicalSharing
		// scsiCtrl.setSharedBus(VirtualSCSISharing.physicalSharing);
		scsiCtrl.setSharedBus(VirtualSCSISharing.noSharing);
		// ����������δ����ScsiCtlrUnitNumber��HotAddRemove

		// ����
		DatastoreSummary dsSummary = (DatastoreSummary) ecb.getServiceUtil3()
				.getDynamicProperty(dsMor, DYNAMICPROPERTY_SUMMARY);
		String dsName = "";
		if (dsSummary != null) {
			dsName = dsSummary.getName();
		}

		VirtualDeviceConfigSpec diskConfig = new VirtualDeviceConfigSpec();
		deviceConfigs[1] = diskConfig;
		diskConfig
				.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);
		diskConfig.setOperation(VirtualDeviceConfigSpecOperation.add);

		// ----------------------com.vmware.vim25.VirtualDisk
		// --getCapacityInKB=16777216
		// --getShares=com.vmware.vim25.SharesInfo@c206c0d0
		// ----getShares=1000
		// ----getLevel=normal
		// --getStorageIOAllocation=com.vmware.vim25.StorageIOAllocationInfo@c206c0d1
		// ----getLimit=-1
		// ----getShares=com.vmware.vim25.SharesInfo@c206c0d0
		// ------getShares=1000
		// ------getLevel=normal
		// ----------------------
		// --getKey=2000
		// --getControllerKey=1000
		// --getUnitNumber=0
		// --getBacking=com.vmware.vim25.VirtualDiskFlatVer2BackingInfo@acca9802
		// ----getChangeId=null
		// ----getContentId=f03ee93a387f3ed891df36b1fffffffe
		// ----getDatastore=datastore-13
		// ----getDeltaDiskFormat=null
		// ----getDiskMode=persistent
		// ----getDynamicType=null
		// ----getFileName=[datastore1] testOPs/testOPs.vmdk
		// ----getUuid=6000C294-dedc-b5e9-e681-131218cd2db4
		// ----getDigestEnabled=false
		// ----getEagerlyScrub=null
		// ----getParent=null
		// ----getSplit=false
		// ----getThinProvisioned=false
		// ----getWriteThrough=false
		// --getConnectable=null
		VirtualDisk disk = new VirtualDisk();
		diskConfig.setDevice(disk);

		disk.setControllerKey(scsiCtlrKey);
		disk.setUnitNumber(0);
		// disk.setKey(0);
		disk.setKey(30);

		disk.setCapacityInKB(diskSizeMB * 1024);

		// http://pubs.vmware.com/vsphere-50/topic/com.vmware.wssdk.apiref.doc_50/vim.vm.device.VirtualDisk.FlatVer2BackingInfo.html
		VirtualDiskFlatVer2BackingInfo backingInfo = new VirtualDiskFlatVer2BackingInfo();
		disk.setBacking(backingInfo);
		// ===============================================================================
		// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.vm.device.VirtualDiskOption.DiskMode.html
		// The "persistent" mode is supported by every backing type.
		backingInfo.setDiskMode(DISKMODE_PERSISTENT);

		// 0-thick��1-eagerZeroedThick����0-thick��getEagerlyScrub=null;1-eagerZeroedThick��getEagerlyScrub=true
		// 0-thick��2-thin����0-thick��getThinProvisioned=false;2-thin��getThinProvisioned=true
		// 1-eagerZeroedThickh��2-thin����1-eagerZeroedThickh��getEagerlyScrub=true,getThinProvisioned=false;2-thin��getEagerlyScrub=null,getThinProvisioned=true

		// diskType= thick | eagerZeroedThick | thin
		if (diskType.equalsIgnoreCase(DISKTYPE_THIN)) {
			backingInfo.setThinProvisioned(true);
		} else {
			backingInfo.setThinProvisioned(false);
		}
		if (diskType.equalsIgnoreCase(DISKTYPE_EAGERZEROEDTHINK)) {
			backingInfo.setEagerlyScrub(true);
		} else {
			backingInfo.setEagerlyScrub(false);
		}
		backingInfo.setDatastore(dsMor);
		backingInfo.setFileName("");

		// ����
		VirtualDeviceConfigSpec nicConfig = new VirtualDeviceConfigSpec();
		deviceConfigs[2] = nicConfig;
		nicConfig.setOperation(VirtualDeviceConfigSpecOperation.add);
		VirtualEthernetCard ethCard = new VirtualPCNet32();
		nicConfig.setDevice(ethCard);
		ethCard.setAddressType(ETHERNETCARD_ADDRESSTYPE_GENERATED);
		ethCard.setKey(40);
		VirtualEthernetCardNetworkBackingInfo ethBacking = new VirtualEthernetCardNetworkBackingInfo();
		ethCard.setBacking(ethBacking);
		ethBacking.setDeviceName(ETHERNETCARD_DEFAULT_NAME);

		// �½�IDE�豸,���ִ���:�豸��4���������ò����ڵĿ�������50����
		// int ideCtrlKey = 50;
		// int ideBusNumber = 3;
		// VirtualDeviceConfigSpec ideConfig = new VirtualDeviceConfigSpec();
		// deviceConfigs[3] = ideConfig;
		// VirtualIDEController ideCtrl = new VirtualIDEController();
		// ideConfig.setDevice(ideCtrl);
		// ideConfig.setOperation(VirtualDeviceConfigSpecOperation.add);
		// ideCtrl.setKey(ideCtrlKey);
		// ideCtrl.setBusNumber(ideBusNumber);

		// ����
		VirtualDeviceConfigSpec cdRomConfig = new VirtualDeviceConfigSpec();
		deviceConfigs[3] = cdRomConfig;
		VirtualCdrom cdRom = new VirtualCdrom();
		cdRomConfig.setDevice(cdRom);
		cdRomConfig.setOperation(VirtualDeviceConfigSpecOperation.add);

		// backing_����,���Բ�����VirtualCdromRemotePassthroughBackingInfo
		// // ----getDeviceName=
		// // ----getUseAutoDetect=false
		// // ----isExclusive=false
		VirtualCdromRemotePassthroughBackingInfo vcrpb = new VirtualCdromRemotePassthroughBackingInfo();
		vcrpb.setDeviceName("");
		vcrpb.setExclusive(false);
		vcrpb.setUseAutoDetect(false);
		cdRom.setBacking(vcrpb);

		// connectable_����
		// ----getStatus=untried
		// ----isAllowGuestControl=true
		// ----isConnected=false
		// ----isStartConnected=false
		VirtualDeviceConnectInfo cdRomConnect = new VirtualDeviceConnectInfo();
		cdRom.setConnectable(cdRomConnect);
		// cdRomConnect.setStatus(arg0);
		cdRomConnect.setAllowGuestControl(true);
		cdRomConnect.setConnected(false);
		cdRomConnect.setStartConnected(false);

		// deviceInfo_�豸��Ϣ,������

		int cdRomUnitNumber = 0;

		// ʹ�ò��ҵ�IDE���������ִ���:Ϊ�豸��3��ָ���˲����ݵ��豸���á�
		// controllerKey_������ID
		// key_�豸ID
		// unitNumber_�������ϵ������豸��
		int ideCtrlKey = -1;

		// Ѱ��IDE������
		ManagedObjectReference envBrowseMor = ecb.getServiceUtil3()
				.getMoRefProp(haMor, DYNAMICPROPERTY_ENVIRONMENTBROWSER);
		VirtualMachineConfigOption cfgOpt = ecb.getServiceConnection3()
				.getService().queryConfigOption(envBrowseMor, null, null);
		VirtualDevice[] defaultDevices = cfgOpt.getDefaultDevice();
		for (int i = 0; i < defaultDevices.length; i++) {
			VirtualDevice device = defaultDevices[i];
			if (device instanceof VirtualIDEController) {
				ideCtrlKey = device.getKey();
				int[] dArr = ((VirtualIDEController) device).getDevice();
				if (dArr != null) {
					cdRomUnitNumber = dArr.length + 1;
					for (int k = 0; k < dArr.length; k++) {
						if (cdRomUnitNumber <= dArr[k]) {
							cdRomUnitNumber = dArr[k] + 1;
						}
					}
				} else {
					cdRomUnitNumber = 0;
				}
				i = defaultDevices.length + 1;
			}
		}
		cdRom.setControllerKey(ideCtrlKey);
		cdRom.setUnitNumber(cdRomUnitNumber);
		// cdRom.setKey(cdRomKey);
		cdRom.setKey(60);

		// �ļ�
		VirtualMachineFileInfo vmfi = new VirtualMachineFileInfo();
		// VmPathName=[datastore1] testOPs/testOPs.vmx
		vmfi.setVmPathName("[" + dsName + "]");
		vmConfig.setFiles(vmfi);

		return vmConfig;
	}

	/**
	 * ��¡�����,�����������ǰ��ģ��״̬,���������¡�������,ģ���¡��ģ��<br>
	 * ������Ĳ���:��¡<br>
	 * ģ��Ĳ���:��¡
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param vmId
	 * @param cloneName
	 * @return
	 */
	public static Map<String, Object> cloneVM(String url, String username,
			String password, String dcId, String vmId, String cloneName) {
		return cloneVM(url, username, password, dcId, vmId, cloneName, true, "");
	}

	/**
	 * �������¡��ģ��<br>
	 * ������Ĳ���:��¡Ϊģ��<br>
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param vmId
	 * @param cloneName
	 * @return
	 */
	public static Map<String, Object> cloneVM2Template(String url,
			String username, String password, String dcId, String vmId,
			String cloneName) {
		return cloneVM(url, username, password, dcId, vmId, cloneName, false,
				"");
	}

	/**
	 * ģ�岿��Ϊ�����<br>
	 * ģ��Ĳ���:�Ӹ�ģ�岿�������
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param vmId
	 * @param cloneName
	 * @param rpId
	 * @return
	 */
	public static Map<String, Object> deployTemplate2VM(String url,
			String username, String password, String dcId, String vmId,
			String cloneName, String rpId) {
		return cloneVM(url, username, password, dcId, vmId, cloneName, false,
				rpId);
	}

	/**
	 * ��¡�����<br>
	 * ������Ĳ���:��¡����¡Ϊģ�塢ת����ģ��(markAsTemplate)<br>
	 * ģ��Ĳ���:��¡��ת���������(markAsVirtualMachine)���Ӹ�ģ�岿�������<br>
	 * ֻ��"�Ӹ�ģ�岿�������"ʱ����ҪrpId
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param vmId
	 * @param cloneName
	 * @param isKeep
	 * @param rpId
	 * @return
	 */
	private static Map<String, Object> cloneVM(String url, String username,
			String password, String dcId, String vmId, String cloneName,
			boolean isKeep, String rpId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference vmMor = cache.getVirtualMachine(vmId);

			if (vmMor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// VirtualMachine.config(VirtualMachineConfigInfo).template��ʶ������Ƿ�Ϊģ��
				// VirtualMachine.summary(VirtualMachineSummary).config(VirtualMachineConfigSummary).template��ʶ������Ƿ�Ϊģ��
				VirtualMachineConfigInfo config = (VirtualMachineConfigInfo) getDynamicProperty(
						ecb, vmMor, DYNAMICPROPERTY_CONFIG);
				// ��ǰ�������ģ��״̬
				boolean currTemp = config.isTemplate();
				// �������������ģ��״̬
				boolean expectTemp = false;
				if (isKeep) {
					expectTemp = currTemp;
				} else {
					expectTemp = !currTemp;
				}

				cloneVM(resultMap, url, username, password, dcId, vmMor,
						cloneName, expectTemp, rpId);
			} else {
				if (vmMor == null) {
					recordResultMapError(resultMap, "�����'" + vmId + "'������");
				}
			}
		} catch (Exception e) {
			LOGGER.error("cloneVM error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ��¡�����,CloneVM_Task<br>
	 * <br>
	 * �Ƿ�ת��Ϊģ��
	 * 
	 * @param resultMap
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param vmMor
	 * @param cloneName
	 * @param isTemplate
	 * @param rpId
	 * @return
	 */
	private static void cloneVM(HashMap<String, Object> resultMap, String url,
			String username, String password, String dcId,
			ManagedObjectReference vmMor, String cloneName, boolean isTemplate,
			String rpId) {
		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);

			ManagedObjectReference dcMor = cache.getDatacenter(dcId);
			// ManagedObjectReference vmMor = cache.getVirtualMachine(vmId);
			ManagedObjectReference vmFolderMor = cache.getVMFolder(dcId);
			ManagedObjectReference rpMor = null;
			if (!Util.normalizeString(rpId).equals("")) {
				rpMor = cache.getResourcePool(rpId);
			}

			if ((dcMor != null) && (vmMor != null) && (vmFolderMor != null)) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec();
				VirtualMachineRelocateSpec relocSpec = new VirtualMachineRelocateSpec();
				cloneSpec.setLocation(relocSpec);
				cloneSpec.setPowerOn(false);
				cloneSpec.setTemplate(isTemplate);
				// ����Ǵ�ģ�岿��Ϊ�������Ҫָ����Դ��
				if (rpMor != null) {
					relocSpec.setPool(rpMor);
				}

				ManagedObjectReference taskMor = ecb.getServiceConnection3()
						.getService().cloneVM_Task(vmMor, vmFolderMor,
								cloneName, cloneSpec);

				execTaskAndRecordResultMap(ecb, taskMor, resultMap);
			} else {
				if (dcMor == null) {
					recordResultMapError(resultMap, "��������'" + dcId + "'������");
				}
				if (vmMor == null) {
					// recordResultMapError(resultMap, "�����'" + vmId
					// + "'������");
					recordResultMapError(resultMap, "�����'" + "'������");
				}
				if (vmFolderMor == null) {
					recordResultMapError(resultMap, "��������'" + dcId
							+ "'��vmFolder������");
				}
			}
		} catch (Exception e) {
			LOGGER.error("cloneVM error, ", e);
			recordResultMapException(resultMap, e);
		}
	}

	/**
	 * �������ת��Ϊģ��<br>
	 * ������Ĳ���:ת����ģ��(markAsTemplate)<br>
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param vmId
	 * @return
	 */
	public static Map<String, Object> markAsTemplate(String url,
			String username, String password, String dcId, String vmId) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference vmMor = cache.getVirtualMachine(vmId);

			if (vmMor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// VirtualMachine.config(VirtualMachineConfigInfo).template��ʶ������Ƿ�Ϊģ��
				// VirtualMachine.summary(VirtualMachineSummary).config(VirtualMachineConfigSummary).template��ʶ������Ƿ�Ϊģ��

				ecb.getServiceConnection3().getService().markAsTemplate(vmMor);

				// ��¼������ȷ�ı�ʶ
				recordResultMapSuccess(resultMap);
			} else {
				if (vmMor == null) {
					recordResultMapError(resultMap, "�����'" + vmId + "'������");
				}
			}
		} catch (FileFault ff) {
			LOGGER.error("markAsTemplate error, ", ff);
			recordResultMapError(resultMap, "����������ļ�����");
		} catch (InvalidPowerState ips) {
			LOGGER.error("markAsTemplate error, ", ips);
			recordResultMapError(resultMap, "�������Դδ�ر�");
		} catch (InvalidState is) {
			LOGGER.error("markAsTemplate error, ", is);
			recordResultMapError(resultMap, "�������ǰ״̬�쳣");
		} catch (NotSupported ns) {
			LOGGER.error("markAsTemplate error, ", ns);
			recordResultMapError(resultMap, "�������֧��");
		} catch (RuntimeFault rf) {
			LOGGER.error("markAsTemplate error, ", rf);
			recordResultMapError(resultMap, "��������̬����");
		} catch (VmConfigFault vcf) {
			LOGGER.error("markAsTemplate error, ", vcf);
			recordResultMapError(resultMap, "ģ�岻����");
		} catch (Exception e) {
			LOGGER.error("markAsTemplate error, ", e);
			recordResultMapException(resultMap, e);
		}
		return resultMap;
	}

	/**
	 * ��ģ��ת��Ϊ�����<br>
	 * ģ��Ĳ���:ת���������(markAsVirtualMachine)<br>
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param vmId
	 * @param rpId
	 * @return
	 */
	public static Map<String, Object> markAsVirtualMachine(String url,
			String username, String password, String dcId, String vmId,
			String rpId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference vmMor = cache.getVirtualMachine(vmId);
			ManagedObjectReference rpMor = cache.getResourcePool(rpId);

			if ((vmMor != null) && (rpMor != null)) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// VirtualMachine.config(VirtualMachineConfigInfo).template��ʶ������Ƿ�Ϊģ��
				// VirtualMachine.summary(VirtualMachineSummary).config(VirtualMachineConfigSummary).template��ʶ������Ƿ�Ϊģ��

				ecb.getServiceConnection3().getService().markAsVirtualMachine(
						vmMor, rpMor, null);

				// ��¼������ȷ�ı�ʶ
				recordResultMapSuccess(resultMap);
			} else {
				if (vmMor == null) {
					recordResultMapError(resultMap, "�����'" + vmId + "'������");
				}
				if (rpMor == null) {
					recordResultMapError(resultMap, "'��Դ��" + rpId + "'������");
				}
			}
		} catch (FileFault ff) {
			LOGGER.error("markAsTemplate error, ", ff);
			recordResultMapError(resultMap, "����������ļ�����");
		} catch (InvalidDatastore ids) {
			LOGGER.error("markAsTemplate error, ", ids);
			recordResultMapError(resultMap, "��Ŀ��洢���޷�ִ�е�ǰ����");
		} catch (InvalidState is) {
			LOGGER.error("markAsTemplate error, ", is);
			recordResultMapError(resultMap, "�������ǰ״̬�쳣");
		} catch (NotSupported ns) {
			LOGGER.error("markAsTemplate error, ", ns);
			recordResultMapError(resultMap, "�������֧��");
		} catch (RuntimeFault rf) {
			LOGGER.error("markAsTemplate error, ", rf);
			recordResultMapError(resultMap, "��������̬����");
		} catch (VmConfigFault vcf) {
			LOGGER.error("markAsTemplate error, ", vcf);
			recordResultMapError(resultMap, "ģ�岻����");
		} catch (Exception e) {
			LOGGER.error("markAsTemplate error, ", e);
			recordResultMapException(resultMap, e);
		}
		return resultMap;
	}

	/**
	 * �޸������,ReconfigVM_Task<br>
	 * ֻ�ṩ�޸�CPU��,�ڴ���,����һ���µĴ��̵ȹ���,��֧���������������̡����ӹ�������������Ȳ���<br>
	 * CPU��(2��)���ڴ�����������ֻ�д���0ʱ�Ż������仯,���д���Ϊ������һ����̡�
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param dcId
	 * @param numCpus
	 * @param numCores
	 * @param memoryMB
	 * @param diskSizeMB
	 * @return
	 */
	public static Map<String, Object> reconfigVM(String url, String username,
			String password, String dcId, String vmId, int numCpus,
			int numCores, long memoryMB, long diskSizeMB) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);

			ManagedObjectReference dcMor = cache.getDatacenter(dcId);
			ManagedObjectReference vmMor = cache.getVirtualMachine(vmId);

			if ((dcMor != null) && (vmMor != null)) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				VirtualMachineConfigSpec vmConfig = new VirtualMachineConfigSpec();
				if (numCpus > 0) {
					vmConfig.setNumCPUs(numCpus);
				}
				if (numCores > 0) {
					vmConfig.setNumCoresPerSocket(numCores);
				}
				if (memoryMB > 0) {
					vmConfig.setMemoryMB(memoryMB);
				}

				// ����һ���´���
				if (diskSizeMB > 0) {
					// ��ȡ�����ԭ��������Ϣ
					VirtualMachineConfigInfo vmConfigInfo = (VirtualMachineConfigInfo) ecb
							.getServiceUtil3().getDynamicProperty(vmMor,
									DYNAMICPROPERTY_CONFIG);

					if (vmConfigInfo != null) {

						// ����device�ı仯
						VirtualDeviceConfigSpec diskConfig = new VirtualDeviceConfigSpec();
						vmConfig
								.setDeviceChange(new VirtualDeviceConfigSpec[] { diskConfig });
						diskConfig
								.setFileOperation(VirtualDeviceConfigSpecFileOperation.create);
						diskConfig
								.setOperation(VirtualDeviceConfigSpecOperation.add);

						// ����
						VirtualDisk disk = new VirtualDisk();
						diskConfig.setDevice(disk);

						// SCSI������
						VirtualLsiLogicController scsiCtrl = null;
						// ���д���
						VirtualDisk oldDisk = null;

						for (VirtualDevice vDevice : vmConfigInfo.getHardware()
								.getDevice()) {
							if (vDevice instanceof VirtualLsiLogicController) {
								scsiCtrl = (VirtualLsiLogicController) vDevice;
							}
							if (vDevice instanceof VirtualDisk) {
								oldDisk = (VirtualDisk) vDevice;
							}
							if ((scsiCtrl != null) && (oldDisk != null)) {
								break;
							}
						}

						if ((scsiCtrl != null) && (oldDisk != null)) {
							disk.setControllerKey(scsiCtrl.getKey());
							disk.setUnitNumber(vmConfigInfo.getHardware()
									.getDevice().length + 1);
							disk.setCapacityInKB(diskSizeMB * 1024);
							disk.setKey(-1);

							VirtualDiskFlatVer2BackingInfo backingInfo = new VirtualDiskFlatVer2BackingInfo();
							disk.setBacking(backingInfo);

							if (oldDisk.getBacking() instanceof VirtualDiskFlatVer2BackingInfo) {
								VirtualDiskFlatVer2BackingInfo oldBacking = (VirtualDiskFlatVer2BackingInfo) oldDisk
										.getBacking();
								backingInfo.setDiskMode(oldBacking
										.getDiskMode());
								backingInfo.setThinProvisioned(oldBacking
										.getThinProvisioned());
								backingInfo.setEagerlyScrub(oldBacking
										.getEagerlyScrub());
								backingInfo.setDatastore(oldBacking
										.getDatastore());
								backingInfo.setFileName("");

							} else {
								// VirtualDeviceBackingInfo���Ͳ���
							}
						} else {
							// SCSI�ʹ���Ϊ��
						}
					} else {
						// ������ϢΪ��
					}
				}

				ManagedObjectReference taskMor = ecb.getServiceConnection3()
						.getService().reconfigVM_Task(vmMor, vmConfig);

				execTaskAndRecordResultMap(ecb, taskMor, resultMap);
			} else {
				if (dcMor == null) {
					recordResultMapError(resultMap, "��������'" + dcId + "'������");
				}
				if (vmMor == null) {
					recordResultMapError(resultMap, "�����'" + vmId + "'������");
				}
			}
		} catch (Exception e) {
			LOGGER.error("cloneVM error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ɾ�������,destroy_Task
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vmId
	 * @return
	 */
	public static Map<String, Object> destroyVM(String url, String username,
			String password, String vmId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getVirtualMachine(vmId);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				ManagedObjectReference taskMor = ecb.getServiceConnection3()
						.getService().destroy_Task(mor);

				execTaskAndRecordResultMap(ecb, taskMor, resultMap);
			} else {
				recordResultMapError(resultMap, "�����'" + vmId + "'������");
			}
		} catch (Exception e) {
			LOGGER.error("destroyVM error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * �޸������״̬,��ͣ�Ȳ���
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vmId
	 * @param operation
	 * @return
	 */
	public static Map<String, Object> vmOperation(String url, String username,
			String password, String vmId, String operation) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getVirtualMachine(vmId);
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				ManagedObjectReference taskMor = null;

				if (operation.equals(OP_POWERON)) {
					taskMor = ecb.getServiceConnection3().getService()
							.powerOnVM_Task(mor, null);
				} else if (operation.equals(OP_POWEROFF)) {
					taskMor = ecb.getServiceConnection3().getService()
							.powerOffVM_Task(mor);
				} else if (operation.equals(OP_SUSPEND)) {
					taskMor = ecb.getServiceConnection3().getService()
							.suspendVM_Task(mor);
				} else if (operation.equals(OP_RESET)) {
					taskMor = ecb.getServiceConnection3().getService()
							.resetVM_Task(mor);
				} else if ((operation.equals(OP_SHUTDOWN))
						|| (operation.equals(OP_STANDBY))
						|| (operation.equals(OP_REBOOT))) {
					vmGuestOperation(ecb, mor, operation, resultMap);
				}
				if (taskMor != null) {
					execTaskAndRecordResultMap(ecb, taskMor, resultMap);
				}

				if (!resultMap.containsKey(INFO_STATE)) {
					recordResultMapError(resultMap, "���������'" + operation
							+ "'�޷�ʶ��");
				}
			} else {
				recordResultMapError(resultMap, "�����'" + vmId + "'������");
			}
		} catch (Exception e) {
			LOGGER.error("vmOperation error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}

	/**
	 * ���������ϵͳ����
	 * 
	 * @param ecb
	 * @param mor
	 * @param resultMap
	 */
	private static void vmGuestOperation(ExtendedAppUtil ecb,
			ManagedObjectReference mor, String operation,
			HashMap<String, Object> resultMap) {
		try {
			if (operation.equals(OP_SHUTDOWN)) {
				ecb.getServiceConnection3().getService().shutdownGuest(mor);
			} else if (operation.equals(OP_STANDBY)) {
				ecb.getServiceConnection3().getService().standbyGuest(mor);
			} else if (operation.equals(OP_REBOOT)) {
				ecb.getServiceConnection3().getService().rebootGuest(mor);
			}
		} catch (InvalidPowerState ips) {
			LOGGER.error("vmShutdownGuest error, ", ips);
			recordResultMapError(resultMap, "�������ǰδ����");
		} catch (InvalidState is) {
			LOGGER.error("vmShutdownGuest error, ", is);
			recordResultMapError(resultMap, "�������ǰ״̬�쳣");
		} catch (RuntimeFault rf) {
			LOGGER.error("vmShutdownGuest error, ", rf);
			recordResultMapError(resultMap, "��������̬����");
		} catch (TaskInProgress tp) {
			LOGGER.error("vmShutdownGuest error, ", tp);
			recordResultMapError(resultMap, "�������æ");
		} catch (ToolsUnavailable tu) {
			LOGGER.error("vmShutdownGuest error, ", tu);
			recordResultMapError(resultMap, "�������VMware Toolsδ����");
		} catch (Exception e) {
			recordResultMapException(resultMap, e);
		}
	}

	/**
	 * ��ȡ�������ժҪ��Ϣ
	 * 
	 * @param url
	 * @param username
	 * @param password
	 * @param vmId
	 * @return
	 */
	public static Map<String, Object> getSummary(String url, String username,
			String password, String vmId) {

		HashMap<String, Object> resultMap = new HashMap<String, Object>();

		try {
			VIMCache cache = VIMCache.getInstance(url, username, password);
			ManagedObjectReference mor = cache.getVirtualMachine(vmId);
//			System.out.println("vmId--:"+vmId+"===mor-�Ƿ����---------------------------"+mor);
			
			if (mor != null) {
				ExtendedAppUtil ecb = getECB(url, username, password);

				// ����
				VirtualMachineConfigInfo config = (VirtualMachineConfigInfo) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_CONFIG);

				// ����
				VirtualMachineRuntimeInfo runtime = (VirtualMachineRuntimeInfo) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_RUNTIME);
				
				
				// ժҪ
				VirtualMachineSummary summary = (VirtualMachineSummary) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_SUMMARY);

				// �洢�б�
				ManagedObjectReference[] dsList = (ManagedObjectReference[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_DATASTORE);

				// �����б�
				ManagedObjectReference[] netList = (ManagedObjectReference[]) getDynamicProperty(
						ecb, mor, DYNAMICPROPERTY_NETWORK);

				// List<Object> properties = getDynamicProperties(ecb, mor,
				// new String[] { DYNAMICPROPERTY_CONFIG,
				// DYNAMICPROPERTY_SUMMARY,
				// DYNAMICPROPERTY_DATASTORE,
				// DYNAMICPROPERTY_NETWORK });
				// VirtualMachineConfigInfo config = (VirtualMachineConfigInfo)
				// properties
				// .get(0);
				// VirtualMachineSummary summary = (VirtualMachineSummary)
				// properties
				// .get(1);
				// ManagedObjectReference[] dsList = (ManagedObjectReference[])
				// properties
				// .get(2);
				// ManagedObjectReference[] netList = (ManagedObjectReference[])
				// properties
				// .get(3);

				String hoId = "";
				if ((runtime != null) && (runtime.getHost() != null)) {
					hoId = runtime.getHost().get_value();
				}
				resultMap.put(SYNC_VM_HOID, hoId);
				resultMap.put(SYNC_COMMON_VID, mor.get_value());
				resultMap.put(SYNC_COMMON_NAME, config.getName());
				int numCore = config.getHardware().getNumCoresPerSocket();
				resultMap.put(SYNC_VM_NUMCORE, numCore);
				if ((config != null) && (summary != null)) {
					// ����
					// �ͻ�������ϵͳ
					resultMap.put(SUMMARY_COMMON_GUESTFULLNAME, config
							.getGuestFullName());

					// ������汾,��������vmx-08
					resultMap.put(SUMMARY_COMMON_VERSION, config.getVersion());
					// CPU
					resultMap.put(SUMMARY_COMMON_CPU, summary.getConfig()
							.getNumCpu());
					// �ڴ�,��λΪMB
					resultMap.put(SUMMARY_COMMON_MEMORYSIZEMB,
							getByteSizeStrFromMB(summary.getConfig()
									.getMemorySizeMB()));

					// �ڴ濪��,current overhead reservation,��λΪ�ֽ�
					resultMap.put(SUMMARY_COMMON_MEMORYOVERHEAD,
							getByteSizeStrFromBytes(summary.getRuntime()
									.getMemoryOverhead()));
					// VMware Tools:
					// ����״̬toolsRunningStatus
					// VirtualMachineToolsRunningStatus:guestToolsExecutingScripts,guestToolsNotRunning,guestToolsRunning
					// ״̬(toolsStatus,toolsVersionStatus[Since4.0],toolsVersionStatus2[Since5.0])
					// VirtualMachineToolsVersionStatus:guestToolsBlacklisted[Since5.0],guestToolsCurrent,guestToolsNeedUpgrade,guestToolsNotInstalled,
					// guestToolsSupportedNew[Since5.0],guestToolsSupportedOld[Since5.0],guestToolsTooNew[Since5.0],guestToolsTooOld[Since5.0],guestToolsUnmanaged
					// ��������(��ǰ�汾)
					// http://pubs.vmware.com/vsphere-50/topic/com.vmware.wssdk.apiref.doc_50/vim.vm.Summary.GuestSummary.html
					// toolsStatus
					// toolsVersionStatus[Since4.0]
					// toolsVersionStatus2[Since5.0]
					// ��ʱֻ��ȡtoolsVersionStatus
					resultMap.put(SUMMARY_COMMON_VMWARETOOLS,
							cache.getEnum(summary.getGuest()
									.getToolsRunningStatus())
									+ "("
									+ cache.getEnum(summary.getGuest()
											.getToolsVersionStatus()) + ")");
					// IP��ַ
					resultMap
							.put(SUMMARY_COMMON_IPADDRESS, Util
									.normalizeString(summary.getGuest()
											.getIpAddress()));
					// DNS����
					resultMap.put(SUMMARY_COMMON_HOSTNAME, Util
							.normalizeString(summary.getGuest().getHostName()));
					// XXX EVCģʽ,��ʱ�޷���ȡ
					// String SUMMARY_COMMON_EVCMODEL = "evcmodel";
					// ״��,poweredOff,poweredOn,suspended
					// powerstate��alarmDesc���з���
//					resultMap.put(SUMMARY_COMMON_POWERSTATE, cache
//							.getAlarmDesc(summary.getRuntime().getPowerState()
//									.getValue()));
					
					String powerState = "";
					if (runtime != null) {
						powerState = runtime.getPowerState().toString();
					}
					resultMap.put(SYNC_VM_POWERSTATE, powerState);
					// ����
					resultMap.put(SUMMARY_COMMON_HOST, getEntityName(ecb,
							summary.getRuntime().getHost()));
					// XXX �����,��ʱ�޷���ȡ
					// String SUMMARY_COMMON_ACTIVETASK = "activetask";
					// XXX vSphere HA����,��ʱ�޷���ȡ
					// String SUMMARY_COMMON_VSPHEREHA = "vsphereha";

					// ��Դ
					// �����ĵ�����CPU,Basic CPU performance
					// statistics,��λMHz,��������ֵ��ֵ�仯�ȽϿ�
					resultMap.put(SUMMARY_RESOURCE_OVERALLCPUUSAGE, summary
							.getQuickStats().getOverallCpuUsage()
							+ MHZ);
					// �����ĵ������ڴ棺consumed host memory,��λΪMB
					resultMap.put(SUMMARY_RESOURCE_HOSTMEMORYUSAGE,
							getByteSizeStrFromMB(summary.getQuickStats()
									.getHostMemoryUsage()));
					// ��ͻ����ڴ棺active guest memory����λΪMB
					resultMap.put(SUMMARY_RESOURCE_GUESTMEMORYUSAGE,
							getByteSizeStrFromMB(summary.getQuickStats()
									.getGuestMemoryUsage()));
					// δ����Ĵ洢
					resultMap.put(SUMMARY_RESOURCE_STORAGE_UNSHARED,
							getByteSizeStrFromBytes(summary.getStorage()
									.getUnshared()));
					// ��ʹ�õĴ洢
					resultMap.put(SUMMARY_RESOURCE_STORAGE_COMMITTED,
							getByteSizeStrFromBytes(summary.getStorage()
									.getCommitted()));
					// �ñ��Ĵ洢
					resultMap.put(SUMMARY_RESOURCE_STORAGE_ALL,
							getByteSizeStrFromBytes(summary.getStorage()
									.getCommitted()
									+ summary.getStorage().getUncommitted()));

					// �洢�б�
					DatastoreMgr.recordResultMapDsList(resultMap, ecb, dsList);

					// �����б�
					NetworkMgr.recordResultMapNetList(resultMap, ecb, netList);

					// ��¼������ȷ�ı�ʶ
					recordResultMapSuccess(resultMap);
				} else {
					recordResultMapError(resultMap, "��ȡ'"
							+ DYNAMICPROPERTY_CONFIG + "'��'"
							+ DYNAMICPROPERTY_SUMMARY + "'��ϢΪ��");
				}
			} else {
				recordResultMapError(resultMap, "�����'" + vmId + "'������");
			}
		} catch (Exception e) {
			LOGGER.error("getSummary error, ", e);
			recordResultMapException(resultMap, e);
		}

		return resultMap;
	}
}
