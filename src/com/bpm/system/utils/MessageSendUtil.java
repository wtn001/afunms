package com.bpm.system.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.afunms.common.util.DBManager;
import com.afunms.common.util.SysLogger;

/**
 * ���ŷ��͹���
 * @author HXL
 *
 */
public class MessageSendUtil {

	/**
	 * 
	 * @param identityType ��ѡ������: ASSIGNEE CANDIDATE OWNER 
	 * @param identityGroupId ��λID
	 * @param userId �û�ID
	 * @param taskName ������
	 * @param processDefinitionName ���̶���ʵ������
	 * @param starter ���̷�����
	 * @param flag �Ƿ��Ͷ���
	 * @return
	 */
	public static String sendMessage(String identityType,String identityGroupId,String identityUserId,String taskName,String processDefinitionName,String starter,String flag) {
		if("0".equals(flag)) {
			//������һ����ִ���˷����ţ��������̷����˷��ͻ�ִ���š�
			return "";
		}else {
			String sql1 = String.format("select mobile from system_user where user_id ='%s'", starter);//��ȡ����ִ�����ֻ���
			String sql2 = "";//��ȡ��һ��������ִ�����ֻ���
			
			if("ASSIGNEE".equals(identityType.toUpperCase())) {
				sql2 = String.format("select mobile from system_user where user_id ='%s'", identityUserId);
			}else if("CANDIDATE".equals(identityType.toUpperCase())) {
				sql2 = String.format("select mobile from system_user where user_id in (select USER_ID_ from act_id_membership where GROUP_ID_='%s')", identityGroupId);
			}
			DBManager dbm = new DBManager();
			ResultSet rs = null;
			String mobile1 = "";
			List<String> mobiles = new ArrayList<String>();
			try {
				rs = dbm.executeQuery(sql1);
				if(rs.next()) {
					mobile1 = rs.getString("mobile");
				}
			} catch (SQLException e) {
				SysLogger.error("���Ͷ���ʱ��ȡ����ִ�����ֻ���ʧ��", e);
			}
			if(!"".equals(sql2)) {
				try {
					rs = dbm.executeQuery(sql2);
					while(rs.next()) {
					   mobiles.add(rs.getString("mobile"));
					}
				} catch (SQLException e) {
					SysLogger.error("���Ͷ���ʱ��ȡ��һ����ִ�����ֻ���ʧ��", e);
				}
			}
			
			
			
			
			
		}
		
		
		return "";
	}
}
