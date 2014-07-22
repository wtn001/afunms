package com.afunms.vmware.vim25.constants;

/**
 * ����Ⱥ���ж���ĳ���
 * 
 * @author LXL
 * 
 */
public interface ComputeResourceConstants extends VIMConstants {

	// ժҪMap�е�keyֵ
	// ����
	// vSphere DRS
	// ժҪMap�е�keyֵ
	// ����
	// vSphere DRS
	String SUMMARY_COMMON_DRS = "drs";
	// vSphere HA
	String SUMMARY_COMMON_HA = "ha";
	// VMWare EVC ģʽ,��ʱ�޷���ȡ
	String SUMMARY_COMMON_EVC = "evc";
	// ��CPU��Դ,,��λMHz
	String SUMMARY_COMMON_TOTALCPU = "totalcpu";
	// ���ڴ�,��λ�ֽ�
	String SUMMARY_COMMON_TOTALMEMORY = "totalmemory";
	// �ܴ洢
	String SUMMARY_COMMON_TOTALDSSIZEMB = "totaldssizemb";
	// ������summary.getNumHosts(��Ч������summary.getNumEffectiveHosts)
	String SUMMARY_COMMON_NUMHOSTS = "numhosts";
	// ����������
	String SUMMARY_COMMON_NUMCPUCORES = "numcpucores";
	// ���ݴ洢Ⱥ����
	String SUMMARY_COMMON_NUMSTORAGEPODS = "numstoragepods";
	// �����ݴ洢��
	String SUMMARY_COMMON_NUMDSS = "numdss";
	// �������ģ��
	String SUMMARY_COMMON_NUMVMS = "numvms";
	// ʹ��vMotion����Ǩ����,��ʱ�޷���ȡ
	String SUMMARY_COMMON_NUMVMOTIONS = "numvmotions";

	// vSphere HA,Ӧ���Ƕ�ӦClusterDasConfigInfo��,��ʱ�޷���ȡ
	// �������(������),admissionControlEnabled
	// ��ǰ�����л�����(1������)
	// �����õĹ����л�����(1������)
	// 0.��ǰ�����л�����(1������)�������õĹ����л�����(1������),������admissionControlPolicy�й�,
	// 1.ClusterFailoverHostAdmissionControlPolicyֻ��ָ��һ������,
	// 2.ClusterFailoverLevelAdmissionControlPolicy����Ԥ��һ������,
	// 3.ClusterFailoverResourcesAdmissionControlPolicy����CPU���ڴ�
	// �������(������),hostMonitoring
	// ��������(�ѽ���),vmMonitoring
	// Ӧ�ó�����(�ѽ���),������vmMonitoring

	// vSphere DRS,Ӧ���Ƕ�ӦClusterDrsConfigInfo��,��ʱ�޷���ȡ
	// Ǩ���Զ�������(ȫ�Զ�),defaultVmBehavior(fullyAutomatedȫ�Զ�,manual�ֹ�,partiallyAutomated�����Զ���)
	// ��Դ�����Զ�������(�ر�)
	// DRS����(0)
	// DRS����(0)
	// Ǩ�Ʒ�ֵ(Ӧ�����ȼ�Ϊ1��2��3�Ľ���),������vmotionRate
	// Ŀ���������ر�׼ƫ��(<=0.2)
	// ��ǰ�������ر�׼ƫ��(0.044 ������ƽ��)

}
