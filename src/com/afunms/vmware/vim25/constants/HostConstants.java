package com.afunms.vmware.vim25.constants;

/**
 * �����ж���ĳ���
 * 
 * @author LXL
 * 
 */
public interface HostConstants extends VIMConstants {

	// ժҪMap�е�keyֵ
	// ����
	// ������
	String SUMMARY_COMMON_VENDOR = "vendor";
	// �ͺ�,����:PowerEdge R710
	String SUMMARY_COMMON_MODEL = "model";
	// CPU�ں�
	String SUMMARY_COMMON_CPUCORE = "cpucore";
	// ����������
	String SUMMARY_COMMON_CPUMODEL = "cpumodel";
	// ���֤,��ʱ�޷���ȡ
	// String SUMMARY_COMMON_LICENSE = "license";
	// ���������
	String SUMMARY_COMMON_NUMCPUKGS = "numcpupkgs";
	// ÿ����۵��ں���
	String SUMMARY_COMMON_NUMCORESPERSOCKET = "numcorespersocket";
	// �߼�������
	String SUMMARY_COMMON_NUMCPUTHREADS = "numcputhreads";
	// ���߳�(�),��ʱ�޷���ȡ
	// ������Ŀ
	String SUMMARY_COMMON_NUMNICS = "numnics";
	// ״��(������)
	String SUMMARY_COMMON_CONNECTIONSTATE = "connectionstate";
	// �������ģ��
	String SUMMARY_COMMON_VM = "vm";
	// vMotion������(��)
	String SUMMARY_COMMON_VMOTION = "vmotion";
	// VMWare EVCģʽ,��ʱ�޷���ȡ
	// vSphere HA״��(��������(����)),��ʱ�޷���ȡ,
	// summary.getRuntime().getDasHostState().getState()=master
	// String SUMMARY_COMMON_HA = "ha";
	// ������FT������(��),��ʱ�޷���ȡ,summary.getConfig().getFaultToleranceEnabled()
	// String SUMMARY_COMMON_FT = "ft";
	// �����,��ʱ�޷���ȡ
	// ���������ļ�,��ʱ�޷���ȡ
	// ӳ�������ļ�,��ʱ�޷���ȡ
	// �����ļ��Ϲ���(������),��ʱ�޷���ȡ
	// DirectPath I/O(����֧��),��ʱ�޷���ȡ

	// ��Դ
	// CPUʹ�����,��λMHz
	String SUMMARY_RESOURCE_OVERALLCPUUSAGE = "overallcpuusage";
	// �ڴ�ʹ�����,��λMB
	String SUMMARY_RESOURCE_OVERALLMEMORYUSAGE = "overallmemoryusage";
	// �ڴ�����,��λMB
	String SUMMARY_RESOURCE_MEMORYSIZEMB = "memorysizemb";

	// Fault Tolerance
	// Fault Tolerance �汾,��ʱ�޷���ȡ
	// String SUMMARY_FT_VERSION = "ftversion";
	// �����������,��ʱ�޷���ȡ
	// �Ѵ򿪵�Դ�������������,��ʱ�޷���ȡ
	// �������������,��ʱ�޷���ȡ
	// �Ѵ򿪵�Դ�ĸ������������,��ʱ�޷���ȡ

}
