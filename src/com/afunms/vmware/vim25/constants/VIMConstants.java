package com.afunms.vmware.vim25.constants;

import com.afunms.vmware.vim25.util.Util;

/**
 * VMWare�ж���ĳ���
 * 
 * @author LXL
 */
public interface VIMConstants {

	// VMWare��ö��ֵ
	// http://pubs.vmware.com/vsphere-51/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc%2Findex-enums.html

	// ��������dc����ĸ��¼��,Ĭ��30����
	long DC_UPDATE_INTERVAL = 30 * Util.MINUTE_MILLISECONDS;

	// ����ʵ���״̬
	// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.ManagedEntity.Status.html
	// ״̬δ֪
	String STATUE_GRAY = "gray";
	// ״̬����
	String STATUE_GREEN = "green";
	// ʵ��һ��������
	String STATUE_RED = "red";
	// ʵ�����������
	String STATUE_YELOW = "yellow";

	// �ֽ�����λ
	// Byte��KB��MB��GB��TB��PB��EB��ZB��YB��DB��NB
	long KB_SIZE = 1024;
	long MB_SIZE = KB_SIZE * KB_SIZE;
	long GB_SIZE = MB_SIZE * KB_SIZE;
	long TB_SIZE = GB_SIZE * KB_SIZE;
	long PB_SIZE = TB_SIZE * KB_SIZE;
	String KB = " KB";
	String MB = " MB";
	String GB = " GB";
	String TB = " TB";
	String PB = " PB";

	// CPU��λ
	String MHZ = "MHZ";

	// VMWare�е���Դ
	// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvmodl.ManagedObjectReference.html
	// http://pubs.vmware.com/vsphere-50/index.jsp?topic=%2Fcom.vmware.wssdk.apiref.doc_50%2Fvim.ManagedEntity.html
	// vsdk_prog_guide.pdf��45/246ҳTable 4-1. Standalone ESX/ESXi and vCenter
	// Server Inventories
	String RESOURCE_CCR = "ClusterComputeResource";
	String RESOURCE_CR = "ComputeResource";
	String RESOURCE_DC = "Datacenter";
	String RESOURCE_DS = "Datastore";
	String RESOURCE_DVS = "DistributedVirtualSwitch";
	String RESOURCE_FOLDER = "Folder";
	String RESOURCE_HO = "HostSystem";
	// String RESOURCE_NETWORK = "Network";
	String RESOURCE_RP = "ResourcePool";
	String RESOURCE_VAPP = "VirtualApp";
	String RESOURCE_VM = "VirtualMachine";
	String RESOURCE_STORAGEPOD = "StoragePod";
	// ��������Managed Object - Datacenter������
	// ��Դ��ͬʱҲ��������
	// һ���������Ķ�Ӧ�������Դ�������ݴ洢������,ʹ�������id��key
	String RESOURCE_DC_DATASTORE = "datastore";
	String RESOURCE_DC_NETWORK = "network";
	// һ���������Ķ�Ӧһ������Դ����4���ļ���,ʹ���������ĵ�dcId��key
	String RESOURCE_DC_DSFOLDER = "datastoreFolder";
	String RESOURCE_DC_HOFOLDER = "hostFolder";
	String RESOURCE_DC_NWFOLDER = "networkFolder";
	String RESOURCE_DC_VMFOLDER = "vmFolder";

	// VMWare�и��ֲ�����Ȩ��
	// vsdk_prog_guide.pdf��215/246ҳTable D-1. Privileges Required for vCenter
	// Server and ESX/ESXi Operations

	// VMWare�����񷵻صĽ��
	String INFO_STATE = "info.state";
	String INFO_ERROR = "info.error";
	String INFO_PROGRESS = "info.progress";
	String INFO_RESULT = "info.result";
	// ����չ,Ϊ�������������ɺ󷵻�������id
	String INFO_RESULT2 = "info.result2";

	// info.state�гɹ���״̬
	String STATE_OK = "success";
	String STATE_ERROR = "error";

	// ������Ϣ
	String ERROR_TASK = "ִ���������:";
	String ERROR_UNKNOWN = "δ֪����:";

	// ��̬����
	// ����Managed Object - Task������
	String DYNAMICPROPERTY_INFO = "info";
	// �����������,�кܶ������config����
	String DYNAMICPROPERTY_CONFIG = "config";
	// ��ժҪ
	String DYNAMICPROPERTY_SUMMARY = "summary";
	// �������
	String DYNAMICPROPERTY_ENVIRONMENTBROWSER = "environmentBrowser";
	// ����̬
	String DYNAMICPROPERTY_RUNTIME = "runtime";
	// ����һҳ
	String DYNAMICPROPERTY_LATESTPAGE = "latestPage";
	// ����
	String DYNAMICPROPERTY_DESCRIPTION = "description";
	// ����
	String DYNAMICPROPERTY_NAME = "name";
	// ����״̬
	String DYNAMICPROPERTY_OVERALLSTATUS = "overallStatus";
	// ��������
	String DYNAMICPROPERTY_ALARMACTIONSENABLED = "alarmActionsEnabled";
	// �洢,datastore
	String DYNAMICPROPERTY_DATASTORE = RESOURCE_DC_DATASTORE;
	// ����,network
	String DYNAMICPROPERTY_NETWORK = RESOURCE_DC_NETWORK;
	// �洢�ļ���,datastoreFolder
	String DYNAMICPROPERTY_DATASTOREFOLDER = RESOURCE_DC_DSFOLDER;
	// �����ļ���,hostFolder
	String DYNAMICPROPERTY_HOSTFOLDER = RESOURCE_DC_HOFOLDER;
	// �����ļ���,networkFolder
	String DYNAMICPROPERTY_NETWORKFOLDER = RESOURCE_DC_NWFOLDER;
	// ������ļ���,vmFolder
	String DYNAMICPROPERTY_VMFOLDER = RESOURCE_DC_VMFOLDER;
	// �ļ��е�childEntity
	String DYNAMICPROPERTY_CHILDENTITY = "childEntity";
	// �ļ��е�childType
	String DYNAMICPROPERTY_CHILDTYPE = "childType";
	// ����
	String DYNAMICPROPERTY_HOST = "host";
	// �����
	String DYNAMICPROPERTY_VM = "vm";
	// ��Դ��
	String DYNAMICPROPERTY_RESOURCEPOOL = "resourcePool";
	// �洢
	String DYNAMICPROPERTY_STORAGE = "storage";
	// ����
	String DYNAMICPROPERTY_OWNER = "owner";
	// Ӳ��
	String DYNAMICPROPERTY_HARDWARE = "hardware";
	// ������Ϣ��չ
	String DYNAMICPROPERTY_CONFIGURATIONEX = "configurationEx";
	// �����ĸ澯
	String DYNAMICPROPERTY_TRIGGEREDALARMSTATE = "triggeredAlarmState";
	// ϵͳ�ж�������ܼ��,Ĭ���ǹ�ȥһ��,��ȥһ��,��ȥһ����,��ȥһ��
	String DYNAMICPROPERTY_HISTORICALINTERVAL = "historicalInterval";
	// ϵͳ��֧�ֵ����ܼ�����
	String DYNAMICPROPERTY_PERFCOUNTER = "perfCounter";

	// �¼�Map��key
	// ����
	String EVENT_DESC = "desc";
	// ����(������):��Ϣ�;����
	// ������Ҫ����ʱ���������ȷ��������,��ĳ�����͵�Event���Ǿ����,�������������ص�˵��,��ȷ���Ƿ�����
	// https://www.vmware.com/pdf/Messages.pdf
	// http://www.veeam.com/support/vcEvents.html
	String EVENT_CATEGORY = "category";
	// ʱ��
	String EVENT_TIME = "time";
	// ����,TaskEvent��info��TaskInfo,�������������Ϣ
	String EVENT_TASK = "task";
	// Ŀ��,��ǰ�豸�¼�,Ŀ��ͨ�������-->����-->��Դ��-->��������,���¿�ʼȡ��һ��
	String EVENT_TARGET = "target";
	// �û�
	String EVENT_USER = "user";

	// �¼������е�key
	// ExtendedEvent
	String EVENTDETAIL_KEY_EXTENDEDEVENT = "ExtendedEvent";
	// EventEx
	String EVENTDETAIL_KEY_EVENTEX = "EventEx";

	// �¼�"����"
	String EVENT_CATEGORY_UNKNOWN = "δ֪";

	// TaskMap��key
	// ����
	String TASK_NAME = "name";
	// Ŀ��
	String TASK_TARGET = "target";
	// ״̬
	String TASK_STATE = "state";
	// ������Ϣ
	String TASK_ERROR = "error";
	// ������
	String TASK_USER = "user";
	// ����ʼʱ��
	String TASK_QUEUETIME = "queuetime";
	// ��ʼʱ��
	String TASK_STARTTIME = "starttime";
	// ���ʱ��
	String TASK_COMPLETETIME = "completetime";

	// ������_ϵͳ
	String TASK_USER_SYSTEM = "ϵͳ";
	// ������_��ʱ����
	String TASK_USER_SCHEDULE = "��ʱ����:";
	// ������_�澯
	String TASK_USER_ALARM = "�澯:";

	// AlarmMap��key
	// ����
	String ALARM_ENTITY = "entity";
	// ״̬
	String ALARM_STATUS = "status";
	// ����
	String ALARM_NAME = "name";
	// ����ʱ��
	String ALARM_TIME = "time";
	// ȷ��ʱ��
	String ALARM_ACKNOWLEDGEDTIME = "atime";
	// ȷ���û�
	String ALARM_ACKNOWLEDGEDUSER = "auser";

	// �洢���б�
	String SUMMARY_RESOURCE_DS_LIST = "dslist";
	// �洢���б��е�Map��key
	// �洢��
	String SUMMARY_RESOURCE_DS_NAME = "dsname";
	// ״̬
	String SUMMARY_RESOURCE_DS_STATUS = "dsstatus";
	// ����������
	// ֵ�з�SSD,
	// ��ssd�йص�QueryConfigTarget����ConfigTarget.scsiDisk(VirtualMachineScsiDiskDeviceInfo).disk(HostScsiDisk).ssd
	String SUMMARY_RESOURCE_DS_DRIVETYPE = "dsdrivetype";
	// ����
	String SUMMARY_RESOURCE_DS_CAPACITY = "dscapacity";
	// ���ÿռ�
	String SUMMARY_RESOURCE_DS_FREESPACE = "dsfreespace";
	// �ϴθ���
	String SUMMARY_RESOURCE_DS_TIME = "dstime";
	// ��������
	String SUMMARY_RESOURCE_DS_ALARMACTIONS = "dsalarmactions";
	// Storage I/O Control
	String SUMMARY_RESOURCE_DS_IOCTRL = "dsioctrl";

	// �����б�
	String SUMMARY_RESOURCE_NET_LIST = "netlist";
	// �����б��е�Map��key
	// ����
	String SUMMARY_RESOURCE_NET_NAME = "netname";
	// ����
	String SUMMARY_RESOURCE_NET_TYPE = "nettype";
	// ״̬
	String SUMMARY_RESOURCE_NET_STATUS = "netstatus";
	// ��������
	String SUMMARY_RESOURCE_NET_ALARMACTIONS = "netalarmactions";

	// ͬ���ӿ�Map�е�keyֵ
	// 1����������
	String SYNC_DC = RESOURCE_DC;
	// 2���ƣ�Ⱥ��
	String SYNC_CR = RESOURCE_CR;
	// 3����Դ��
	String SYNC_RP = RESOURCE_RP;
	// 4���洢 �洢Ⱥ��,RESOURCE_DS
	String SYNC_DS = RESOURCE_DS;
	// 5�������
	String SYNC_HO = RESOURCE_HO;
	// 6�����
	String SYNC_VM = RESOURCE_VM;
	// 7��ģ��
	String SYNC_TEMPLATE = "Template";
	// ����
	String SYNC_OTHER = "other";

	// ����������
	// VID
	String SYNC_COMMON_VID = "vid";
	// ����
	String SYNC_COMMON_NAME = "name";
	// �洢��ID
	String SYNC_COMMON_DSID = "dsid";
	// ��������ID
	String SYNC_COMMON_DCID = "dcid";
	// Ⱥ��ID
	String SYNC_COMMON_CRID = "crid";

	// ����������
	// ��Դ
	String SYNC_OTHER_SOURCE = "source";
	// ����
	String SYNC_OTHER_TYPE = "type";
	// ���õķ���
	String SYNC_OTHER_METHOD = "method";

	// �����������
	// 4��������������ID
	String SYNC_VM_HOID = "hoid";
	// 6������
	String SYNC_VM_DESC = "desc";
	// 9���洢��ID,SYNC_COMMON_DSID
	// 10��cup�ܺ���
	String SYNC_VM_CPU = "cpu";
	// 11���ڴ�����
	String SYNC_VM_MEMORYSIZEMB = "memorysizemb";
	// 12���洢����
	String SYNC_VM_DSSIZEMB = "dssizemb";
	// 13����������
	String SYNC_VM_NETNUM = "netnum";
	// 15���������ϵͳ
	String SYNC_VM_GUESTFULLNAME = "guestfullname";
	// 18����������ID,SYNC_COMMON_DCID
	// 20����Դ������ID
	String SYNC_VM_RPID = "rpid";
	// 22��cpu ����
	String SYNC_VM_NUMCPU = "numcpu";
	// 23��ÿ��cpu����
	String SYNC_VM_NUMCORE = "numcore";
	// 29: ��Դ״̬,ֵ����poweredOn,poweredOff,suspended
	String SYNC_VM_POWERSTATE = "powerstate";

	// ���ݴ洢������
	// 3���洢�ص�����
	String SYNC_DS_CAPACITY = "capacity";
	// 7����������
	String SYNC_DS_FREESPACE = "freespace";
	// StoragePod���Ӷ���
	String SYNC_DS_CHILDREN = "children";

	// Ⱥ��������
	// --7����ǰ�����������cpu�����ܺ�
	// ժҪ-->����-->��CPU��Դ,summary.getTotalCpu,��λMHz
	String SYNC_CR_TOTALCPU = "totalcpu";
	// ժҪ-->����-->����������,summary.getNumCpuCores
	String SYNC_CR_NUMCPUCORES = "numcpucores";
	// --8����ǰ������������ڴ�����
	// ժҪ-->����-->���ڴ�,summary.getTotalMemory,��λ�ֽ�
	String SYNC_CR_TOTALMEMORY = "totalmemory";
	// --9����ǰ�������������������
	// ժҪ-->����-->�ܴ洢
	String SYNC_CR_TOTALDSSIZEMB = "totaldssizemb";
	// ժҪ-->����-->������summary.getNumHosts(��Ч������summary.getNumEffectiveHosts)
	String SYNC_CR_NUMHOSTS = "numhosts";

	// ����������
	// 3�����������ͺ�
	String SYNC_HO_MODEL = "model";
	// 7��IP��ַ��������û�У���ȷ���Ƿ���ȡ����
	String SYNC_HO_IP = "ip";
	// 13��cpu����
	String SYNC_HO_NUMCPU = "numcpu";
	// 14��ÿ��cpu����
	String SYNC_HO_NUMCORE = "numcore";
	// 15��cpu��Ƶ
	String SYNC_HO_CPUMHZ = "cpumhz";
	// 16���ڴ��С
	String SYNC_HO_MEMORYSIZEMB = "memorysizemb";
	// 17����������������
	String SYNC_HO_NUMNICS = "numnics";
	// 20: ��Դ״̬,ֵ����poweredOn,poweredOff,standBy,unknown
	String SYNC_HO_POWERSTATE = "powerstate";

	// ���ܼ��
	String PERFINTERVAL_DAY = "1";

	// ���ܼ��
	String PERFINTERVAL_WEEK = "2";

	// ���ܼ��
	String PERFINTERVAL_MONTH = "3";

	// ���ܼ��
	String PERFINTERVAL_YEAR = "4";

}
