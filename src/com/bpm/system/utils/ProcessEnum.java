package com.bpm.system.utils;

public enum ProcessEnum {
	none("0","δ֪"),
	CLAIMTASK("1","��ǩ��δ��������"),
	UNCLAIMTASK("2","δǩ������"),
	FINISHEDPRO("3","�������������"),
	UNFINISHEDPRO("4","�����Ѱ�δ�������");
	public String key;
	public String decp;
	private ProcessEnum(String _key , String _decp)
	{
		key=_key;
		decp=_decp;
	}
}
