package com.bpm.system.utils;

public enum ExportExcelEnum {
	none("none","δ֪"),
	process_statistical("process_statistical","���̱���ͳ��");
	public String key;
	public String decp;
	private ExportExcelEnum(String _key , String _decp)
	{
		key=_key;
		decp=_decp;
	}

}
