/*
 * Created on 20011-8-31
 * zhangys
 */
package com.afunms.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SyslogFinals {
	private static final Map eventMap = new HashMap();
	public static final Map<Integer, String> devCategoryMap = new HashMap<Integer, String>();
	
	public static final String USERLOGINSUCCESS = "userLoginSuccess";
	public static final String USERLOGINFAILURE = "userLoginFailure";
	public static final String USERLOGOUTSUCCESS = "userLogoutSuccess";
	
	public static final String NWDEVUSERLOGINSUCCESS = "nwDevUserLoginSuccess";
	public static final String NWDEVUSERLOGINFAILURE = "nwDevUserLoginFailure";
	public static final String NWDEVUSERLOGOUTSUCCESS = "nwDevUserLogoutSuccess";	
	
	public static final String QSUSERCHANGEPASSSUCCESS = "qsUserChangePassSuccess";
	public static final String QSUSERCHANGEPASSFAILURE = "qsUserChangePassFailure";
	public static final String QSUSERACCOUNTDISABLED = "qsUserAccountDisabled";
	public static final String QSUSERMODIFYLOG = "qsUserModifyLog";
	
	public static final String QSLOGINFAILURE = "qsLoginFailure";
	public static final String QSNWLOGINFAILURE = "qsNWLoginFailure";
	public static final String QSACCOUTABUSED = "qsAccoutAbused";
	
	private static final List<List> userLoginSuccessList = new ArrayList<List>();
	private static final List<List> userLoginFailureList = new ArrayList<List>();
	private static final List<List> userLogoutSuccessList = new ArrayList<List>();
	
	private static final List<List> nwDevUserLoginSuccessList = new ArrayList<List>();
	private static final List<List> nwDevUserLoginFailureList = new ArrayList<List>();
	private static final List<List> nwDevUserLogoutSuccessList = new ArrayList<List>();	
	
	private static final List<List> qsUserChangePassSuccessList = new ArrayList<List>();	//�����޸ĳɹ����û� 
	private static final List<List> qsUserChangePassFailureList = new ArrayList<List>();	//�����޸�ʧ�ܵ��û� 
	private static final List<List> qsUserAccountDisabledList = new ArrayList<List>();		//��Щ�˺ű�ɾ��/���ã�
	private static final List<List> qsUserModifyLogList = new ArrayList<List>();			//��Щ�û��޸Ļ�������˰�ȫ�����־
	
	private static final List<List> qsLoginFailureList = new ArrayList<List>();				//��Щ�����ĵ�¼ʧ�ܴ������
	private static final List<List> qsNWLoginFailureList = new ArrayList<List>();			//�����з����˶��ٵ�¼ʧ�ܵ��¼���
	private static final List<List> qsAccoutAbusedList = new ArrayList<List>();				//��Щ�û��˺ű������¼��Ƚ϶ࣿ
	
//	ϵͳʱ���Ѹ���
	
	static{
		devCategoryMap.put(1, "·����");
		devCategoryMap.put(2, "·�ɽ�����");
		devCategoryMap.put(3, "������");
		devCategoryMap.put(4, "������");
		devCategoryMap.put(5, "����");
		devCategoryMap.put(6, "����");
		devCategoryMap.put(7, "·����");
		devCategoryMap.put(8, "����ǽ");
		devCategoryMap.put(9, "ATM");
		devCategoryMap.put(13, "CMTS");
		devCategoryMap.put(14, "�洢");
	
		//�������֮����߼���ϵ��1: and 2��or 3: not
		//һ�������еĹؼ����Ƿ�����0������ sql��message like '%xxx%' and message like '%yyy%'��1������ sql��message like '%xxx%yyy%',��������
		userLoginSuccessList.add(new ArrayList(Arrays.asList(1, 1, "��¼�ɹ�: �û���:","��¼ ID:","��¼����: 2")));
		userLoginSuccessList.add(new ArrayList(Arrays.asList(0, 1, "�ɹ��������¼: �û���:","��¼ ID:","��¼����: 3")));
		userLoginSuccessList.add(new ArrayList(Arrays.asList(0, 1, "��¼�ɹ�: �û���:","��¼ ID:","��¼����: 5")));
		userLoginSuccessList.add(new ArrayList(Arrays.asList(0, 1, "��¼�ɹ�: �û���:","��¼ ID:","��¼����: 7")));
		eventMap.put(USERLOGINSUCCESS, userLoginSuccessList);
		
		userLoginFailureList.add(new ArrayList(Arrays.asList(1, 0, "��¼ʧ��: ԭ��:","�û���:","��¼����: 2")));
		userLoginFailureList.add(new ArrayList(Arrays.asList(2, 0, "��¼ʧ��: ԭ��:","�û���:","��¼����: 3")));
		eventMap.put(USERLOGINFAILURE, userLoginFailureList);

		userLogoutSuccessList.add(new ArrayList(Arrays.asList(1, 0, "�û�ע��: �û���:","��¼ ID:","��¼����: 2")));
		userLogoutSuccessList.add(new ArrayList(Arrays.asList(1, 0, "�û�ע��: �û���: ANONYMOUS LOGON","��¼ ID:","��¼����: 3")));
		userLogoutSuccessList.add(new ArrayList(Arrays.asList(1, 0, "�û�ע��: �û���: Guest","��¼ ID:","��¼����: 3")));
		eventMap.put(USERLOGOUTSUCCESS, userLogoutSuccessList);
		
		nwDevUserLoginSuccessList.add(new ArrayList(Arrays.asList(1, 0, "/LOGIN(l):")));
		eventMap.put(NWDEVUSERLOGINSUCCESS, nwDevUserLoginSuccessList);
		
		nwDevUserLoginFailureList.add(new ArrayList(Arrays.asList(1, 0, "/LOGINFAIL(l):")));
		nwDevUserLoginFailureList.add(new ArrayList(Arrays.asList(2, 0, "failed to login")));
		eventMap.put(NWDEVUSERLOGINFAILURE, nwDevUserLoginFailureList);
		
		nwDevUserLogoutSuccessList.add(new ArrayList(Arrays.asList(1, 0, "/LOGOUT(l):")));
		eventMap.put(NWDEVUSERLOGOUTSUCCESS, nwDevUserLogoutSuccessList);
		
		//�����޸ĳɹ����û� id 628
		//LEN-D3117189Administrator: �������û��ʻ�����: Ŀ���ʻ���: zhangyashe Ŀ����: LEN-D3117189 Ŀ���ʻ� ID: %{S-1-5-21-1891745410-3080488357-2315099178-1004} ���з��û���: Administrator ���з�������: LEN-D3117189 ���з���¼ ID: (0x0,0x214F6)		
		qsUserChangePassSuccessList.add(new ArrayList(Arrays.asList(1,0,"�������û��ʻ�����: Ŀ���ʻ���:","Ŀ����:","Ŀ���ʻ� ID:")));
		eventMap.put(QSUSERCHANGEPASSSUCCESS, qsUserChangePassSuccessList);
		
		//�����޸�ʧ�ܵ��û� TODO �������־��ʽ �������·�ʽ����
		qsUserChangePassFailureList.add(new ArrayList(Arrays.asList(1,0,"�޸����û��ʻ�����: Ŀ���ʻ���:","Ŀ����:","Ŀ���ʻ� ID:")));
		eventMap.put(QSUSERCHANGEPASSFAILURE, qsUserChangePassFailureList);
		
		//0B03��Щ�˺ű�ɾ��/���ã�629
		//LEN-D3117189Administrator: ͣ�����û��ʻ�: Ŀ���ʻ���: zhangyashe Ŀ����: LEN-D3117189 Ŀ���ʻ� ID: %{S-1-5-21-1891745410-3080488357-2315099178-1004} ���з��û���: Administrator ���з�������: LEN-D3117189 ���з���¼ ID: (0x0,0x2211B)		
		qsUserAccountDisabledList.add(new ArrayList(Arrays.asList(1,0,"ͣ�����û��ʻ�: Ŀ���ʻ���:","Ŀ����:","Ŀ���ʻ� ID:")));
		qsUserAccountDisabledList.add(new ArrayList(Arrays.asList(2,0,"ɾ�����û��ʻ�: Ŀ���ʻ���:","Ŀ����:","Ŀ���ʻ� ID:")));
		eventMap.put(QSUSERACCOUNTDISABLED, qsUserAccountDisabledList);
		
		//��Щ�û��޸Ļ�������˰�ȫ�����־��517
		//Eventlog was cleared: 'Security' TODO û���û����� 
		qsUserModifyLogList.add(new ArrayList(Arrays.asList(1,0,"Eventlog was cleared:")));
		eventMap.put(QSUSERMODIFYLOG, qsUserModifyLogList);
		
		//��Щ�����ĵ�¼ʧ�ܴ������
		qsLoginFailureList.add(new ArrayList(Arrays.asList(1, 0, "��¼ʧ��: ԭ��:","�û���:","��¼����: 2")));
//		qsLoginFailureList.add(new ArrayList(Arrays.asList(2, 0, "��¼ʧ��: ԭ��:","�û���:","��¼����: 3")));
		qsLoginFailureList.add(new ArrayList(Arrays.asList(2, 0, "/LOGINFAIL(l):")));
		qsLoginFailureList.add(new ArrayList(Arrays.asList(2, 0, "failed to login")));
		//qsLoginFailureList.add(new ArrayList(Arrays.asList(3, 0, "failed to login")));
		eventMap.put(QSLOGINFAILURE, qsLoginFailureList);
		
		//�����з����˶��ٵ�¼ʧ�ܵ��¼���
		qsNWLoginFailureList.add(new ArrayList(Arrays.asList(1, 0, "��¼ʧ��: ԭ��:","�û���:","��¼����: 3")));
		qsNWLoginFailureList.add(new ArrayList(Arrays.asList(2, 0, "/LOGINFAIL(l):")));
		qsNWLoginFailureList.add(new ArrayList(Arrays.asList(2, 0, "failed to login")));
		eventMap.put(QSNWLOGINFAILURE, qsNWLoginFailureList);
		
		//TODO ��Щ�û��˺ű������¼��Ƚ϶ࣿ
		qsAccoutAbusedList.add(new ArrayList(Arrays.asList(1, 0, "��¼ʧ��: ԭ��:","�û���:","��¼����: 3")));
		qsAccoutAbusedList.add(new ArrayList(Arrays.asList(2, 0, "/LOGINFAIL(l):")));
		qsAccoutAbusedList.add(new ArrayList(Arrays.asList(2, 0, "failed to login")));
		eventMap.put(QSACCOUTABUSED, qsAccoutAbusedList);
	}
	
	private static String getSQL(List list, int iSort){
		String sql = "(";
		if (iSort == 0) {
			for (int i = 2; i < list.size(); i++) {
				sql += "message like '%" + (String)list.get(i) + "%'";
				if (i < list.size()-1) sql += " and ";
			}
		}else if(iSort == 1){
			sql += "message like '%";
			for (int i = 2; i < list.size(); i++) {
				sql += (String)list.get(i) + "%";
			}
			sql += "'";
		}
		sql += ")";
		return sql;
	}
	
	public static String getMsgClause(String key){
		List list = (ArrayList)eventMap.get(key);
		String sql = "";
		if (null != list && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				List cdtList = (ArrayList)list.get(i);
				int flag = (Integer)cdtList.get(0);
				int iSort = (Integer)cdtList.get(1);
				
				if(flag == 3){
					continue;
				}
				
				if (sql.length() == 0) {
					sql = getSQL(cdtList, iSort);
				}else{
					if (flag == 1) {
						sql = sql + " and " + getSQL(cdtList, iSort);
					}else if(flag == 2){
						sql = sql + " or " + getSQL(cdtList, iSort);
					}
				}
			}
		}
		return " (" + sql + ") ";
	}

}
