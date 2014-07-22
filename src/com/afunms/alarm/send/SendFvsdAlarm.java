package com.afunms.alarm.send;

import java.util.Date;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.afunms.event.model.EventList;
import com.dhcc.itsm.webservice.server.databinding.Alarm;
import com.dhcc.itsm.webservice.server.service.alarm.AlarmPushService;


public class SendFvsdAlarm {

    private static String withinIP = "10.10.152.63";

    private static String withoutIP = "114.251.193.153";
    
    
    
    
    public boolean sendFVSDAlarm(EventList eventList) {
        boolean result = true;
        try {
            String content = eventList.getContent();
            Alarm alarm = new Alarm();
            alarm.setMessage(content);
            alarm.setOccurTime(eventList.getRecordtime().getTime());
            alarm.setAlarmType(1);
            alarm.setAlarmId(String.valueOf(eventList.getEventlocation()));
            alarm.setMoType("8a8a18bf257c7bfb01257caae5570018");
            alarm.setSeverity("1");
            Alarm[] alarms = {alarm};
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(AlarmPushService.class);
            factory.setAddress("http://" + withinIP + ":8080/fvsd/services/AlarmPushService");
            AlarmPushService service = (AlarmPushService) factory.create();
            System.out.println("开始调用服务台发送接口发送告警信息");
            service.sendAlarm(alarm);
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public static void main(String[] args) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        EventList eventList = new EventList();
//        eventList.setContent("告警测试!!!");
//        eventList.setRecordtime(calendar);
//        eventList.setId(33333);
//        new SendFvsdAlarm().sendFVSDAlarm(eventList);
        new SendFvsdAlarm().alarmClientTest();
    }
    
    public void alarmClientTest(){
        
        Alarm alarm = new Alarm();
        alarm.setMessage("告警测试");
        alarm.setOccurTime(new Date());
        alarm.setAlarmType(1);
        alarm.setAlarmId("33333");
        alarm.setMoType("8a8a18bf257c7bfb01257caae5570018");
        alarm.setSeverity("1");
        
        Alarm alarm1 = new Alarm();
        alarm1.setMessage("告警测试1");
        alarm1.setOccurTime(new Date());
        alarm1.setAlarmType(2);
        alarm1.setAlarmId("44444");
        alarm1.setMoType("8a8a18bf257c7bfb01257caae5570018");
        alarm1.setSeverity("3");
        
        Alarm[] alarms = {alarm,alarm1};
        
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(AlarmPushService.class);
        factory.setAddress("http://10.18.37.100:8080/fvsd_3.4.1/services/AlarmPushService");
        AlarmPushService alarmPush = (AlarmPushService) factory.create();
        System.out.println("invoke webservice.....start");
//      alarmPush.sendAlarm(alarm);
        try {
            alarmPush.bulkSendAlarm(alarms);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("invoke webservice.....end");
        
    }
}
