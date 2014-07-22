package com.afunms.alarm.send;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import com.afunms.alarm.dao.AlarmWayDao;
import com.afunms.alarm.dao.AlarmWayDetailDao;
import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.model.AlarmPort;
import com.afunms.alarm.model.AlarmWay;
import com.afunms.alarm.model.AlarmWayDetail;
import com.afunms.common.util.NodeAlarmUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.event.dao.AlarmInfoDao;
import com.afunms.event.model.AlarmInfo;
import com.afunms.event.model.CheckEvent;
import com.afunms.event.model.EventList;
import com.afunms.system.dao.AlertInfoServerDao;
import com.afunms.system.model.AlertInfoServer;

public class SendAlarmUtil {
	
	public void sendAlarm(CheckEvent checkEvent, EventList eventList , AlarmIndicatorsNode alarmIndicatorsNode){
		//SysLogger.info(" #### �ȼ�: " + eventList.getLevel1() + " �澯ָ��: " + alarmIndicatorsNode.getName()+" ###eventList:"+eventList.getContent());
		String alarmWayId = getAlarmWayId(eventList, alarmIndicatorsNode);
//		Ĭ�������,��Ҫ����ϵͳ�¼�
		SendPageAlarm sendPageAlarm = new SendPageAlarm();
		sendPageAlarm.sendAlarm(eventList,"");
		NodeAlarmUtil.saveNodeAlarmInfo(eventList, alarmIndicatorsNode.getName());//�������չ������
		if(alarmWayId!=null){
			String alarmWayIdArray[] = alarmWayId.split(",");
			if(alarmWayIdArray!=null&&alarmWayIdArray.length>0){
				for(int j = 0;j<alarmWayIdArray.length;j++){
					AlarmWay alarmWay = null;
					if(alarmWayIdArray[j] != null && alarmWayIdArray[j].trim().length()>0){
						try{
							alarmWay = getAlarmWay(alarmWayIdArray[j]);
						}catch(Exception e){
							e.printStackTrace();
						}
						if(alarmWay != null ){
							List<AlarmWayDetail> list = getAlarmWayDetail(alarmWayIdArray[j]);
							if(list != null && list.size() > 0){
								SendAlarmFilter sendAlarmFilter = new SendAlarmFilter();
								for(int i = 0 ; i < list.size(); i++){
									AlarmWayDetail alarmWayDetail = list.get(i);
									boolean result = sendAlarmFilter.isSendAlarm(checkEvent , alarmIndicatorsNode , alarmWay , alarmWayDetail);
									//SysLogger.info("result------------getIsMailAlarm---------"+result+"=="+alarmWay.getIsMailAlarm()+"===");
									if(result){
										try{
											SendAlarmDispatcher.sendAlarm(eventList , alarmWayDetail);
										}catch(Exception e){
											e.printStackTrace();
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * ҳ��澯
	 * @param eventList
	 * @param alarmWayDetail
	 */
//	public void sendPageAlarm(EventList eventList, AlarmWayDetail alarmWayDetail, CheckEvent checkEvent){
//		//���ݸ澯�¼�����ԭ�澯��Ϣ�ַ���
//		String type = "";
//		if("url".equals(eventList.getSubtype()) || "ftp".equals(eventList.getSubtype()) 
//				|| "mail".equals(eventList.getSubtype()) || "socket".equals(eventList.getSubtype())
//				|| "tftp".equals(eventList.getSubtype()) || "dhcp".equals(eventList.getSubtype())){
//			type = "service";
//		}else{
//			type = eventList.getSubtype();
//		}
//		String name = eventList.getNodeid()+":"+type+":"+eventList.getSubentity();
//		String sIndex=eventList.getBak();
//		if (sIndex != null && sIndex.trim().length() > 0) {
//		 name = name + ":" + sIndex;
//		}
//		if("diskperc".equals(eventList.getSubentity()) || "diskinc".equals(eventList.getSubentity())){
//			name = checkEvent.getName();
//		}
//		
//		//�ж��ڴ����Ƿ��д˸澯״̬
//		//�ж��Ƿ񱣴�checkevent�¼���Ϣ
//		boolean flag = true;
//		Hashtable checkEventHash = ShareData.getCheckEventHash();
//		if(checkEventHash != null && checkEventHash.size()>0){			
//			if(checkEventHash.containsKey(name)){//��һ�β����澯������澯��Ϣ��system_eventlist��
//				flag = false;
//			}
//		}
//		if(flag){
//			checkEventHash.put(name,eventList.getLevel1().toString());
//			SendPageAlarm sendPageAlarm = new SendPageAlarm();
//			sendPageAlarm.sendAlarm(eventList,alarmWayDetail);
//		}
//	}
	/**
	 * wupinlong add �ļ���ط��͸澯
	 * @param checkEvent
	 * @param eventList
	 * @param alarmIndicatorsNode
	 * @param alarmWayId
	 */
	public void sendAlarm(CheckEvent checkEvent, EventList eventList , AlarmIndicatorsNode alarmIndicatorsNode, String alarmWayId){
		if(alarmWayId!=null){
			String alarmWayIdArray[] = alarmWayId.split(",");
			if(alarmWayIdArray!=null&&alarmWayIdArray.length>0){
				for(int j = 0;j<alarmWayIdArray.length;j++){
					AlarmWay alarmWay = getAlarmWay(alarmWayIdArray[j]);
					if(alarmWay != null ){
						List<AlarmWayDetail> list = getAlarmWayDetail(alarmWayIdArray[j]);
						if(list != null && list.size() > 0){
							SendAlarmFilter sendAlarmFilter = new SendAlarmFilter();
							for(int i = 0 ; i < list.size(); i++){
								AlarmWayDetail alarmWayDetail = list.get(i);
								boolean result=sendAlarmFilter.isSendAlarm(checkEvent , alarmIndicatorsNode , alarmWay , alarmWayDetail);
								if(result){
									SendAlarmDispatcher.sendAlarm(eventList , alarmWayDetail);
								}
							}
						}		
					}
				}
			}
		}
		//Ĭ�������,��Ҫ����ϵͳ�¼�
		SendPageAlarm sendPageAlarm = new SendPageAlarm();
		sendPageAlarm.sendAlarm(eventList,"");
		//Ĭ�������,�������������澯����д����
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
	}
	public void sendPortAlarm(CheckEvent checkEvent, EventList eventList ,int alarmLevel,AlarmPort portNode){
		//SysLogger.info(" #### �ȼ�: " + eventList.getLevel1() + " �澯ָ��: " + alarmIndicatorsNode.getName()+" ###");
		//String alarmWayId = getAlarmWayId(eventList, alarmIndicatorsNode);
		String alarmWayId = "";
		if(alarmLevel == 1){
			alarmWayId = portNode.getWayin1();
		} else if (alarmLevel == 2){
			alarmWayId = portNode.getWayin2();
		}else if (alarmLevel == 3){
			alarmWayId = portNode.getWayin3();
		}else if (alarmLevel == 4){
			alarmWayId = portNode.getWayout1();
		}else if (alarmLevel == 5){
			alarmWayId = portNode.getWayout2();
		}else if (alarmLevel == 6){
			alarmWayId = portNode.getWayout3();
		}
		AlarmWay alarmWay = null;
		if(alarmWayId != null && alarmWayId.trim().length()>0){
			try{
				alarmWay = getAlarmWay(alarmWayId);
			}catch(Exception e){
				
			}
		}
		//Ĭ�������,�������������澯����д����
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
		AlarmIndicatorsNode alarmIndicatorsNode=new AlarmIndicatorsNode();
		alarmIndicatorsNode.setId(portNode.getId());
		alarmIndicatorsNode.setNodeid(portNode.getId()+"");
		alarmIndicatorsNode.setType(portNode.getType());
		alarmIndicatorsNode.setSubtype(portNode.getSubtype());
		alarmIndicatorsNode.setName(portNode.getName());
//		alarmIndicatorsNode.setWay0(portNode.getWay1());
//		alarmIndicatorsNode.setWay1(portNode.getWay2());
//		alarmIndicatorsNode.setWay2(portNode.getWay3());
		
		
		if(alarmWay == null ){
			//SysLogger.info("### �澯ָ��: " + alarmIndicatorsNode.getName() +  " �޸澯��ʽ ֻ����ϵͳ�澯 ###");
			//Ĭ�������,��Ҫ����ϵͳ�¼�
			AlarmWayDetail alarmWayDetail = null;
			SendPageAlarm sendPageAlarm = new SendPageAlarm();
			sendPageAlarm.sendAlarm(eventList,alarmWayDetail);
		}else{
			if("1".equals(alarmWay.getIsPageAlarm())){
				AlarmWayDetail alarmWayDetail = null;
				SendPageAlarm sendPageAlarm = new SendPageAlarm();
				sendPageAlarm.sendAlarm(eventList,alarmWayDetail);
			}
			List<AlarmWayDetail> list = getAlarmWayDetail(alarmWayId);
			if(list == null || list.size() == 0){
				//SysLogger.info("### �澯ָ��: " + alarmIndicatorsNode.getName() +  " �޸澯��ϸ���� ���澯 ###");
			}else{
				SendAlarmFilter sendAlarmFilter = new SendAlarmFilter();
				for(int i = 0 ; i < list.size(); i++){
					AlarmWayDetail alarmWayDetail = list.get(i);
					boolean result = sendAlarmFilter.isSendAlarm(checkEvent , alarmIndicatorsNode , alarmWay , alarmWayDetail);
					if(result){
						SendAlarmDispatcher.sendAlarm(eventList , alarmWayDetail);
					}
				}
			}
		}	
	}
	/**
	 * ���͸澯,ֻ��Ҫ���η��͸澯��Ϣ,������Ҫ�����ж��м��θ澯����,ֻҪ�����澯�ͷ�������ô˷���
	 * @param alarmWayId
	 * @param eventList
	 * @return
	 */
	public void sendAlarmNoIndicator(String alarmWayId, EventList eventList){
		//SysLogger.info(" #### �ȼ�: " + eventList.getLevel1() + " �澯ָ��: " + alarmIndicatorsNode.getName()+" ###");
		//String alarmWayId = getAlarmWayId(eventList, alarmIndicatorsNode);
		AlarmWay alarmWay = null;
		if(alarmWayId != null && alarmWayId.trim().length()>0){
			try{
				alarmWay = getAlarmWay(alarmWayId);
			}catch(Exception e){
				
			}
		}
		//Ĭ�������,�������������澯����д����
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
		
		
		
		if(alarmWay == null ){
			//SysLogger.info("### �澯ָ��: " + alarmIndicatorsNode.getName() +  " �޸澯��ʽ ֻ����ϵͳ�澯 ###");
			//Ĭ�������,��Ҫ����ϵͳ�¼�
			AlarmWayDetail alarmWayDetail = null;
			SendPageAlarm sendPageAlarm = new SendPageAlarm();
			sendPageAlarm.sendAlarm(eventList,alarmWayDetail);
		}else{
			if("1".equals(alarmWay.getIsPageAlarm())){
				AlarmWayDetail alarmWayDetail = null;
				SendPageAlarm sendPageAlarm = new SendPageAlarm();
				sendPageAlarm.sendAlarm(eventList,alarmWayDetail);
			}
			List<AlarmWayDetail> list = getAlarmWayDetail(alarmWayId);
			if(list == null || list.size() == 0){
				//SysLogger.info("### �澯ָ��: " + alarmIndicatorsNode.getName() +  " �޸澯��ϸ���� ���澯 ###");
			}else{
				SendAlarmFilter sendAlarmFilter = new SendAlarmFilter();
				for(int i = 0 ; i < list.size(); i++){
					AlarmWayDetail alarmWayDetail = list.get(i);
//					boolean result = sendAlarmFilter.isSendAlarm(checkEvent , alarmIndicatorsNode , alarmWay , alarmWayDetail);
//					if(result){
						SendAlarmDispatcher.sendAlarm(eventList , alarmWayDetail);
//					}
				}
			}
		}	
	}
	
	/**
	 * ��ȡ��澯��ʽ�� id
	 * @param eventList
	 * @param alarmIndicators
	 * @return
	 */
	private String getAlarmWayId(EventList eventList , AlarmIndicatorsNode alarmIndicatorsNode){
		String alarmWayId = "";
		if(eventList.getLevel1() == 1){
			alarmWayId = alarmIndicatorsNode.getWay0();
		}else if( eventList.getLevel1() == 2 ){
			alarmWayId = alarmIndicatorsNode.getWay1();
		}else if( eventList.getLevel1() == 3 ){
			alarmWayId = alarmIndicatorsNode.getWay2();
		}
		return alarmWayId;
	}
	
	/**
	 * ��ȡ��澯��ʽ�� id
	 * @param eventList
	 * @param alarmIndicators
	 * @return
	 */
	private AlarmWay getAlarmWay(String alarmWayId){
		AlarmWay alarmWay = null;
		AlarmWayDao alarmWayDao = new AlarmWayDao();
		try {
			alarmWay = (AlarmWay)alarmWayDao.findByID(alarmWayId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} finally {
			alarmWayDao.close();
		}
		return alarmWay;
	}
	
	/**
	 * ��ȡ��澯�ķ�ʽ����ϸ����
	 * @param eventList
	 * @param alarmIndicators
	 * @return
	 */
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
