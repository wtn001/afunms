package com.afunms.common.util;

import com.afunms.application.dao.NodeIndicatorAlarmDao;
import com.afunms.application.dao.PerformancePanelDao;
import com.afunms.application.model.NodeIndicatorAlarm;
import com.afunms.event.model.EventList;
import com.afunms.topology.model.HostNode;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Aug 11, 2011 2:38:59 PM
 * ��˵��:�豸�澯��Ϣͳ�ƹ�����
 */
public class NodeAlarmUtil {
	
	/**
	 * ���澯��Ϣ��ӵ������������ݿ���
	 * @param eventList            �¼�����
	 * @param alarmIndicatorName   �澯ָ������
	 */
	public synchronized static void saveNodeAlarmInfo(EventList eventList, String alarmIndicatorName){
		NodeIndicatorAlarmDao nodeIndicatorAlarmDao = new NodeIndicatorAlarmDao();
		try{
			NodeIndicatorAlarm nodeIndicatorAlarm = new NodeIndicatorAlarm();
			nodeIndicatorAlarm.setAlarmDesc(eventList.getContent());
//			System.out.println(eventList.getLevel1()+"--------------------");
//			System.out.println("--------------------"+eventList.getLevel1().intValue());
			if(eventList.getLevel1()==null){
				nodeIndicatorAlarm.setAlarmLevel("");
			}else {
				nodeIndicatorAlarm.setAlarmLevel(eventList.getLevel1().intValue()+"");
			}
//			nodeIndicatorAlarm.setAlarmLevel(eventList.getLevel1().intValue()+"");
			nodeIndicatorAlarm.setDeviceId(eventList.getNodeid()+"");
			nodeIndicatorAlarm.setDeviceType(eventList.getSubtype());
			nodeIndicatorAlarm.setIndicatorName(alarmIndicatorName);
			boolean flag = nodeIndicatorAlarmDao.isExist(nodeIndicatorAlarm);
			//��������Ϣ���澯��Ϣ���ݿ�
			if(flag){
				nodeIndicatorAlarmDao.update(nodeIndicatorAlarm);
			}else{
				nodeIndicatorAlarmDao.save(nodeIndicatorAlarm);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			nodeIndicatorAlarmDao.close();
		}
	}
	
	/**
	 * �������ͺ�IDɾ����������
	 * @param deviceId
	 * @param deviceType
	 * @return
	 */
	public synchronized static boolean deleteByDeviceIdAndDeviceType(String deviceId, String deviceType){
		boolean flag = false;
		NodeIndicatorAlarmDao nodeIndicatorAlarmDao = new NodeIndicatorAlarmDao();
		PerformancePanelDao performancePanelDao = new PerformancePanelDao();
		try{
			nodeIndicatorAlarmDao.deleteByIdAndType(deviceId, deviceType);
			performancePanelDao.deleteByIdAndType(deviceId, deviceType);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			nodeIndicatorAlarmDao.close();
			performancePanelDao.close();
		}
		return flag;
	}
	
	/**
	 * @param obj  �����豸��ɾ�������豸ָ��������е����� 
	 * @return
	 */
	public synchronized static boolean deleteByDeviceIdAndDeviceType(Object obj){
		boolean flag = false;
		if(obj == null){
			return flag;
		}
		String deviceType = null;
		String deviceId = null;
		if(obj instanceof HostNode){
			HostNode host = (HostNode)obj;
			if((host.getCategory()== 4)){
				deviceType = "host";
			}
			if(host.getCategory()< 4 || host.getCategory() == 7 || host.getCategory() == 8|| host.getCategory() == 9){
				deviceType = "net";
			}
			deviceId = host.getId()+"";
		}
		if(deviceType != null && deviceId != null){
			flag = deleteByDeviceIdAndDeviceType(deviceId, deviceType);
		}
		return flag;
	}
	
	/**
	 * ɾ��������壬�澯��Ϣ�б��еĸ�ָ��澯��¼
	 * @param deviceId      �豸ID
	 * @param deviceType    �豸����
	 * @param indicatorName ָ������
	 * @return
	 */
	public synchronized boolean deleteByDeviceIdAndDeviceTypeAndIndicatorName(String deviceId, String deviceType, String indicatorName){
		boolean flag = false;
		NodeIndicatorAlarmDao nodeIndicatorAlarmDao = new NodeIndicatorAlarmDao();
		try{
			nodeIndicatorAlarmDao.deleteByIdAndTypeAndIndicatorName(deviceId, deviceType, indicatorName);
			flag = true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			nodeIndicatorAlarmDao.close();
		}
		return flag;
	}
}
  