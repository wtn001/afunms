package com.afunms.util;

import com.afunms.common.util.SystemConstant;

public class DBConvert {

	/**
	 * �����mysql �򲻱�  �����oracle��ת��
	 * @author GANYI
	 * @param YYYY-MM-SS MM:HH:SS��ʽ��String�ַ��� 
	 * @return 
	 * @����ʱ�� 2012-4-17 14:50
	 * 
	 */
	public static String mysqlAndOracleConvert(String time) {
		if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
			return "'"+time+"'";
		}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
			return "to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')";
		}
		return time;
	}
}
