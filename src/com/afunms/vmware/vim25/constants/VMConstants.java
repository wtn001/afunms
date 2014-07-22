package com.afunms.vmware.vim25.constants;

/**
 * ������ж���ĳ���
 * 
 * @author LXL
 * 
 */
public interface VMConstants extends VIMConstants {

	// ���������
	// PowerOnVM_Task
	String OP_POWERON = "poweron";
	// PowerOffVM_Task
	String OP_POWEROFF = "poweroff";
	// SuspendVM_Task
	String OP_SUSPEND = "suspend";
	// ResetVM_Task
	String OP_RESET = "reset";
	// ShutdownGuest
	String OP_SHUTDOWN = "shutdown";
	// StandbyGuest
	String OP_STANDBY = "standby";
	// RebootGuest
	String OP_REBOOT = "reboot";

	// ��Դ״̬,����
	String POWERSTATE_POWERON = "poweredOn";

	// ��������
	String DISKTYPE_THICK = "thick";
	String DISKTYPE_EAGERZEROEDTHINK = "eagerZeroedThick";
	String DISKTYPE_THIN = "thin";

	// ����ģʽ
	String DISKMODE_PERSISTENT = "persistent";

	// ������ַģʽ
	String ETHERNETCARD_ADDRESSTYPE_GENERATED = "generated";

	// ����Ĭ������
	String ETHERNETCARD_DEFAULT_NAME = "VM Network";

	// ժҪMap�е�keyֵ
	// ����
	// �ͻ�������ϵͳ
	String SUMMARY_COMMON_GUESTFULLNAME = "guestfullname";
	// ������汾,��������vmx-08
	String SUMMARY_COMMON_VERSION = "version";
	// CPU
	String SUMMARY_COMMON_CPU = "numcpu";
	// �ڴ�,��λΪMB
	String SUMMARY_COMMON_MEMORYSIZEMB = "memorysizemb";
	// �ڴ濪��,current overhead reservation,��λΪ�ֽ�
	// δ��������������������ģ��Ҳ��ֵ
	String SUMMARY_COMMON_MEMORYOVERHEAD = "memoryoverhead";
	// VMware Tools
	// ����״̬toolsRunningStatus
	// VirtualMachineToolsRunningStatus:guestToolsExecutingScripts,guestToolsNotRunning,guestToolsRunning
	// ״̬(toolsStatus,toolsVersionStatus[Since4.0],toolsVersionStatus2[Since5.0])
	// VirtualMachineToolsVersionStatus:guestToolsBlacklisted[Since5.0],guestToolsCurrent,guestToolsNeedUpgrade,guestToolsNotInstalled,
	// guestToolsSupportedNew[Since5.0],guestToolsSupportedOld[Since5.0],guestToolsTooNew[Since5.0],guestToolsTooOld[Since5.0],guestToolsUnmanaged
	String SUMMARY_COMMON_VMWARETOOLS = "vmwaretools";
	// IP��ַ
	String SUMMARY_COMMON_IPADDRESS = "ipaddress";
	// DNS����
	String SUMMARY_COMMON_HOSTNAME = "hostname";
	// EVCģʽ,��ʱ�޷���ȡ
	String SUMMARY_COMMON_EVCMODEL = "evcmodel";
	// ״��,poweredOff,poweredOn,suspended
	String SUMMARY_COMMON_POWERSTATE = "powerstate";
	// ����
	String SUMMARY_COMMON_HOST = "host";
	// �����,��ʱ�޷���ȡ
	String SUMMARY_COMMON_ACTIVETASK = "activetask";
	// vSphere HA����,��ʱ�޷���ȡ
	String SUMMARY_COMMON_VSPHEREHA = "vsphereha";
	// ��Դ
	// �����ĵ�����CPU,Basic CPU performance statistics,��λMHz,��������ֵ��ֵ�仯�ȽϿ�
	String SUMMARY_RESOURCE_OVERALLCPUUSAGE = "overallcpuusage";
	// �����ĵ������ڴ棺consumed host memory,��λΪMB
	String SUMMARY_RESOURCE_HOSTMEMORYUSAGE = "hostmemoryusage";
	// ��ͻ����ڴ棺active guest memory����λΪMB
	String SUMMARY_RESOURCE_GUESTMEMORYUSAGE = "guestmemoryusage";
	// δ����Ĵ洢
	String SUMMARY_RESOURCE_STORAGE_UNSHARED = "storageunshared";
	// ��ʹ�õĴ洢
	String SUMMARY_RESOURCE_STORAGE_COMMITTED = "storagecommitted";
	// �ñ��Ĵ洢
	// �ñ��Ĵ洢44.23GBΪgetCommitted+getUncommitted
	// δ�������ʹ��Ϊ40.00GB
	// getCommitted=42949982899
	// getUncommitted=4537319424
	// getUnshared=42949982899
	String SUMMARY_RESOURCE_STORAGE_ALL = "storageall";

	// �洢���б���صĶ������VIMConstants

}
