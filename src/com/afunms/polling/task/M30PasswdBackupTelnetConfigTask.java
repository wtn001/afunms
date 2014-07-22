package com.afunms.polling.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.afunms.alarm.dao.AlarmWayDetailDao;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.alarm.send.SendAlarmDispatcher;
import com.afunms.alarm.send.SendAlarmFilter;
import com.afunms.capreport.common.DateTime;
import com.afunms.capreport.dao.BaseDaoImp;
import com.afunms.event.model.EventList;
import com.database.config.SystemConfig;
/**
 * ��ʱ�����޸�����
 * @author ChengFeng
 *
 */
public class M30PasswdBackupTelnetConfigTask  extends MonitorTask{

	private static Logger log = Logger.getLogger(M30PasswdBackupTelnetConfigTask.class);

	private final String hms = " 00:00:00";

	/*
	 * (non-Javadoc)
	 *  
	 * @see java.util.TimerTask#run() 
	 */
	@Override
	public void run() {
		subscribe();
//		System.gc();
	}

	/**
	 * ��ʱ 
	 */
	private void subscribe() { 
		DateTime dt = new DateTime(); 
		String time = dt.getMyDateTime(DateTime.Datetime_Format_14);
		
		String sql = "SELECT * FROM sys_pwdbackup_telnetconfig s WHERE status = '��' and s.BACKUP_DATE > 10000 AND s.BACKUP_DATE <= "
				+ time;
		ArrayList<Map<String, String>> ssconfAL=null;
		//�������ļ��������Ƿ�������ʱѲ�� wxy add
		String flag= SystemConfig.getConfigInfomation("Agentconfig","Pwdserver");
		
		if(flag!=null&&flag.equals("enable")){
			BaseDaoImp cd = new BaseDaoImp();
			ssconfAL = cd.executeQuery(sql);
			
		}
		Map<String, String> ssidAL = null;
		if (ssconfAL != null) {
			log.info("-------------------------------(��ʱ���Ѹ�������)��ʱ��ִ��ʱ�䣺" + dt.getMyDateTime(DateTime.Datetime_Format_2)
					+ "-------------------------------");
			try {
				for (int i = 0; i < ssconfAL.size(); i++) {
					ssidAL = ssconfAL.get(i);
					String status = ssidAL.get("status");//״̬
					String telnetconfigips = ssidAL.get("telnetconfigips");//IP��ַ
					String backup_sendfrequency = ssidAL.get("BACKUP_SENDFREQUENCY");//���ѵ�Ƶ��
					String backup_time_month = ssidAL.get("BACKUP_TIME_MONTH");//
					String backup_time_week = ssidAL.get("BACKUP_TIME_WEEK");//
					String backup_time_day = ssidAL.get("BACKUP_TIME_DAY");//
					String backup_time_hou = ssidAL.get("BACKUP_TIME_HOU");//
					String warntype = ssidAL.get("warntype");////���ѷ�ʽ
					//String bkpType = ssidAL.get("bkpType");//
					boolean istrue = false;
					// ����Ƶ�ʣ�1:ÿ��;2:ÿ��;3:ÿ��;4ÿ����;5ÿ��
					/**
					 * �ж�ʱ����
					 * ��ȡСʱ����Χ0-23��
					 * ��ȡһ���еĵڼ���ȡֵ��Χ1-31��
					 * ȡһ�����ڵĵڼ��졣 ��ĩΪһ�����ڵĵ�һ�죻
					 * ��ȡ�·ݣ���Χ1-12��
					 */
					if ("ÿ��".equals(backup_sendfrequency)) {
							if (backup_time_hou.contains("/" + (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours())
									+ "/")) {
								istrue = true;
							}
						} else if ("ÿ��".equals(backup_sendfrequency)) {
							if (backup_time_week.contains("/" + (dt.getDay() - 1) + "/")
									&& backup_time_hou.contains("/"
											+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
								istrue = true;
							}
						} else if ("ÿ��".equals(backup_sendfrequency)) {
							if (backup_time_day.contains("/" + (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate())
									+ "/")
									&& backup_time_hou.contains("/"
											+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
								istrue = true;
							}
						} else if ("ÿ��".equals(backup_sendfrequency)) {
							if (backup_time_month.contains("/" + (dt.getMonth() < 10 ? "0" + dt.getMonth() : dt.getMonth())
									+ "/")
									&& backup_time_day.contains("/"
											+ (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate()) + "/")
									&& backup_time_hou.contains("/"
											+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
								istrue = true;
							}
						} else if ("ÿ��".equals(backup_sendfrequency)) {
							if (backup_time_month.contains("/" + (dt.getMonth() < 10 ? "0" + dt.getMonth() : dt.getMonth())
									+ "/")
									&& backup_time_day.contains("/"
											+ (dt.getDate() < 10 ? "0" + dt.getDate() : dt.getDate()) + "/")
									&& backup_time_hou.contains("/"
											+ (dt.getHours() < 10 ? "0" + dt.getHours() : dt.getHours()) + "/")) {
								istrue = true;
							}
						}
					
					if (istrue) {
						log.info("��ʱ���Ѹ������뿪ʼ--passwdtimingbackup_telnetconfig=" + telnetconfigips);
					    
						//�ռ�Ҫ���Ķ�������
						if(!telnetconfigips.equals("") && telnetconfigips != null){
							String[] ips = telnetconfigips.split(",");//ȡ��Ҫ���ѵ��豸IP
							EventList eventList = new EventList();	
							eventList.setRecordtime(Calendar.getInstance());
							 
							for(String ip : ips){
								if(ip != null && !ip.equals("") && !ip.equals(",") && "��".equals(status)){
									//������ʾ�����ͽ�������
									//1���������ѷ�ʽ��ȡ�����ѵ����ݣ��Լ�����ϸ��alarmWayDetail
									//2���½�һ��EvenList
									eventList.setContent("IP��ַΪ��"+ip+"���豸��½�����Ѿ������������趨����Чʱ�䣬����ȷ���Ƿ���Ҫ�޸����룡");
									List<AlarmWayDetail> list = getAlarmWayDetail(warntype);
									if(list == null || list.size() == 0){
										//SysLogger.info("### �澯ָ��: " + alarmIndicatorsNode.getName() +  " �޸澯��ϸ���� ���澯 ###");
									}else{
										SendAlarmFilter sendAlarmFilter = new SendAlarmFilter();
										for(int k = 0 ; k < list.size(); k++){
											AlarmWayDetail alarmWayDetail = list.get(k); 
											//System.out.println("!!!!!!!!!!!!!!i do it !&!!!!!!!"); 
											SendAlarmDispatcher.sendAlarm(eventList , alarmWayDetail);
											
										}
									}   	
						
									}
								}	
							}	 
						}
					}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}
	
	private List<AlarmWayDetail> getAlarmWayDetail(String alarmWayId){
		List<AlarmWayDetail> list = null;
		AlarmWayDetailDao alarmWayDetailDao = new AlarmWayDetailDao();
		try {
			list = (List<AlarmWayDetail>)alarmWayDetailDao.findByAlarmWayId(alarmWayId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			alarmWayDetailDao.close();
		}
		return list;
	}


}
