package com.afunms.alarm.send;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.EventList;

public class SendPageAlarm implements SendAlarm {
	
	public void sendAlarm(EventList eventList,String uid){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date())+ " 00:00:00";
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		EventListDao eventListDao = new EventListDao();
		try {
			Calendar tempCal = (Calendar)eventList.getRecordtime();			   				
			Date cc = tempCal.getTime();
			String recordtime = sdf.format(cc);
			if(eventList.getLevel1()>0){
				List list = eventListDao.getEventlist(startTime,endTime,eventList.getManagesign()+"",eventList.getLevel1()+"",eventList.getBusinessid(),eventList.getNodeid(),eventList.getSubentity());
				if(list!=null&&list.size()>0){   
					EventList vo = (EventList) list.get(0);
					eventListDao.update(recordtime,eventList.getContent(),vo.getId()+"");
				} else { 
					eventList.setLasttime(recordtime);
					eventListDao.save(eventList);
					//�����̨���͸澯
				}
//				Ĭ�������,�������������澯����д����
				// �������澯����д����
				AlarmInfo alarminfo = new AlarmInfo();
				alarminfo.setContent(eventList.getContent());
				alarminfo.setIpaddress(eventList.getEventlocation());
				alarminfo.setLevel1(new Integer(2));
				alarminfo.setRecordtime(Calendar.getInstance());
				alarminfo.setType("");
				AlarmInfoDao alarmdao = new AlarmInfoDao();
				try {
					alarmdao.save(alarminfo);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					alarmdao.close();
				}
			} else {
				///////////////////////////////
				List list = eventListDao.getEventlist(startTime,endTime,eventList.getManagesign()+"","99",eventList.getBusinessid(),eventList.getNodeid(),eventList.getSubentity());
				if(list!=null&&list.size()>0){
					for(int i=0; i<list.size(); i++){
						EventList vo = (EventList) list.get(i);
						if(vo.getLevel1()>0){
							String time = null;// �澯����ʱ�䣬Ĭ�Ϸ���Ϊ��λ
							long timeLong = 0;
							tempCal = (Calendar)vo.getRecordtime();
							cc = tempCal.getTime();
							String collecttime = sdf.format(cc);
							Date firstAlarmDate = null;
							try {
								firstAlarmDate = sdf.parse(collecttime);
							} catch (ParseException e) {
								e.printStackTrace();
							}
							if (firstAlarmDate != null) {
								timeLong = new Date().getTime() - firstAlarmDate.getTime();
							}
							if (timeLong < 1000 * 60) {// С��1����,��
								time = timeLong / 1000 + "��";
							} else {// С��1Сʱ,��
								time = timeLong / (60 * 1000) + "��";
							}
							eventListDao.update(recordtime,"0",vo.getContent()+" (�ø澯�ѻָ����澯����ʱ��"+time+")", vo.getId()+"");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
	}
	public void sendAlarm(List<EventList> list){
		EventListDao eventListDao = new EventListDao();
		try {
			eventListDao.save(list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			eventListDao.close();
		}
	}
	public void sendAlarm(EventList eventList, AlarmWayDetail alarmWayDetail) {
		// TODO Auto-generated method stub
		
	}
}
