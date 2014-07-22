package com.afunms.alarm.send;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.afunms.alarm.dao.SendAlarmTimeDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmWay;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.alarm.model.SendAlarmTime;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.event.model.CheckEvent;


/**
 * �澯��������
 * @author nielin
 *
 */
public class AlarmTimesFilter implements AlarmFilter{
	
	public AlarmFilter alarmFilter = null;
	
	public boolean isSendAlarm(CheckEvent checkEvent, AlarmIndicatorsNode alarmIndicatorsNode , AlarmWay alarmWay , AlarmWayDetail alarmWayDetail){
		boolean result = false;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date();
		// ����
		String name = alarmIndicatorsNode.getNodeid() + ":" + alarmIndicatorsNode.getType() + ":" + alarmIndicatorsNode.getName();
		if(checkEvent!=null){
			name = checkEvent.getNodeid() + ":" +  checkEvent.getType() + ":" + checkEvent.getSubtype() + ":" + checkEvent.getIndicatorsName();
			String sIndex = checkEvent.getSindex();
			if(sIndex != null && sIndex.length() > 0){
				name = name + ":" + sIndex;
			}
			name = name + ":" + checkEvent.getAlarmlevel();
		}
		SendAlarmTime sendAlarmTime = null;
		try {
			SendAlarmTimeDao sendAlarmTimeDao = null;
			try {
				// ���� checkEvent �� name �Լ� alarmWayDetail �� id ����澯����ʱ��
				sendAlarmTimeDao = new SendAlarmTimeDao();
				sendAlarmTime = (SendAlarmTime)sendAlarmTimeDao.findByNameAndId(name, alarmWayDetail.getId()+"");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				sendAlarmTimeDao.close();
			}
			//sendAlarmTime = (SendAlarmTime)sendAlarmTimeDao.findByNameAndId(alarmIndicatorsNode.getId()+"", alarmWayDetail.getId()+"");
			if(sendAlarmTime != null){
				String lasSendTime = sendAlarmTime.getLastSendTime();
				Date lastSendTimeDate = simpleDateFormat.parse(lasSendTime);
				if((currentDate.getTime() - lastSendTimeDate.getTime()) / (1000*60*60*24) >= 1){
					// ��� ��ǰ�澯ʱ�� ���� ���һ�θ澯ʱ�� һ�� �� �����ʹ���Ϊ 0
					sendAlarmTime.setSendTimes("0");
					result = true;
				} else {
					// ���� �ȽϷ��͸澯����
					int lastSendTimes = 0;
					int sendTimes = 0;
					try {
						if(sendAlarmTime.getSendTimes()!=null &&!sendAlarmTime.getSendTimes().equals("null")&& sendAlarmTime.getSendTimes().trim().length() > 0){
							lastSendTimes = Integer.valueOf(sendAlarmTime.getSendTimes());
						}
						if(alarmWayDetail.getSendTimes()!=null&& !alarmWayDetail.getSendTimes().equals("null") && alarmWayDetail.getSendTimes().trim().length() > 0){
							sendTimes = Integer.valueOf(alarmWayDetail.getSendTimes());
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(lastSendTimes < sendTimes || sendTimes == 0){
						// ��� ���ĸ澯���� С�� �������͵ĸ澯����
						result = true;
					}
				}
			} else {
				sendAlarmTime = new SendAlarmTime();
				sendAlarmTime.setName(name);
				sendAlarmTime.setAlarmWayDetailId(alarmWayDetail.getId()+"");
				sendAlarmTime.setSendTimes("0");
				result = true;
			}
			if(result){
				// ������� �� ���� ���ݿ�
				int lastSendTimes = Integer.valueOf(sendAlarmTime.getSendTimes());
				sendAlarmTime.setLastSendTime(simpleDateFormat.format(currentDate));
				sendAlarmTime.setSendTimes(String.valueOf(lastSendTimes+1));
				sendAlarmTimeDao = new SendAlarmTimeDao();
				try {
					sendAlarmTimeDao.deleteByNameAndId(name, alarmWayDetail.getId()+"");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					sendAlarmTimeDao.close();
				}
				sendAlarmTimeDao = new SendAlarmTimeDao();
				try {
					sendAlarmTimeDao.save(sendAlarmTime);
					ShareData.setSendAlarmTimes(sendAlarmTime.getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					sendAlarmTimeDao.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		if(!result){
			//SysLogger.info( alarmWay.getName() + "====�澯����===" + alarmWayDetail.getAlarmCategory() + "===��������3������===���澯=====");
			return result;
		} else if(alarmFilter!= null){
			return alarmFilter.isSendAlarm(checkEvent, alarmIndicatorsNode, alarmWay, alarmWayDetail);
		} 
		
		return result;
		
	}
	
	public void setNextFilter(AlarmFilter alarmFilter){
		this.alarmFilter = alarmFilter;
	}
	
}
