package com.afunms.vmware.vim25.constants;

/**
 * ���ݴ洢�ж���ĳ���
 * 
 * @author LXL
 * 
 */
public interface DatastoreConstants extends VIMConstants {

	// ժҪMap�е�keyֵ
	// ����
	// λ��
	String SUMMARY_COMMON_URL = "url";
	// ����
	String SUMMARY_COMMON_TYPE = "type";
	// ������������Ŀ
	String SUMMARY_COMMON_HOST = "host";
	// �������ģ��
	String SUMMARY_COMMON_VM = "vm";

	// ����
	// ����
	String SUMMARY_CAPACITY_CAPACITY = "capacity";
	// �ñ��Ŀռ�=capacity+uncommitted-freespace
	String SUMMARY_CAPACITY_STORAGEALL = "storageall";
	// ���ÿռ�
	String SUMMARY_CAPACITY_FREESPACE = "freespace";
	// �ϴθ���ʱ��
	String SUMMARY_CAPACITY_TIMESTAMP = "timestamp";

}