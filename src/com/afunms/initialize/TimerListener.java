package com.afunms.initialize;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import com.afunms.common.util.SysLogger;
import com.afunms.polling.task.MonitorTask;
import com.afunms.polling.task.MonitorTimer;

/**
 * @author HONGLI E-mail: ty564457881@163.com
 * @version ����ʱ�䣺Jul 14, 2011 7:15:43 PM
 * ��˵��
 */
public class TimerListener extends Thread{
	
	private MonitorTimer timer;//������timer
	
	private TimerTask task;//�������ȵ�task
	
	private long delay; //�ӳ�ʱ��  
	
	private long period;//����task��ʱ����
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	 public TimerListener() {
		super();
	}

	 /**
	 * @param task      ��timer���ȵ�task����
	 * @param delay     ����taskʱ���ӳ�ʱ��
	 * @param period    Task�����ļ��ʱ�� ��λ������ 
	 * */
	public TimerListener(TimerTask task,long delay, long period) {
		this.task = task;
		this.delay = delay;
		this.period = period;
	}

	/**
	 * @param timer  ��������timer
	 */
	public void addTimerListener(MonitorTimer timer){
		this.timer = timer;
		this.start();
	}
	
	/**
	 * �Ƿ�����Ҫ������task
	 * @param task
	 * @return
	 */
	public boolean isMonitorTask(TimerTask task){
		boolean flag = false;
		if(task instanceof MonitorTask){
			String taskName = task.getClass().getName();
			if(taskName.contains("M5Task")){
				flag = true;
			}else if(taskName.contains("M10Task")){
				flag = true;
			}//����Task���...
		}
		return flag;
	}

	public void run() {
		Date lastStartTime = null;
		Calendar calendar = null;
		boolean isMonitorTask = isMonitorTask(task);//��task�Ƿ�Ϊ��Ҫ������task
		while(true){
			calendar = Calendar.getInstance();
			if(isMonitorTask){//ֻ����������ΪMonitorTask���͵�task
				//���˵�û���趨recentlyStartTime��task
				lastStartTime = ((MonitorTask)task).getRecentlyStartTime();//���һ������������
				if(lastStartTime == null){
					continue;
				}
//				SysLogger.info("----��ʼ�ж�task��������ѵ:"+task);
				SysLogger.info("----���һ������"+task+"������--"+sdf.format(lastStartTime));
				long timeInterval = calendar.getTime().getTime() - lastStartTime.getTime();//ʵ��ʱ����
				long maxTimeInterval = period*2 - 10*1000;//���Ŀɽ��ܵ�ʱ���� = ʵ��Timer�������*2  - 10�루10��Ϊ���趨��ʱ���ӳٷ�Χ�� 
				SysLogger.info("----�Ƚ�"+task+"�����ļ��ʱ��----ʵ��ʱ����:" + timeInterval+"  ���Ŀɽ��ܵ�ʱ���� :" + maxTimeInterval);
				if(timeInterval > maxTimeInterval){//���ʱ�䳬���趨��task��ѯʱ���� *2 - 10��  ���½�timer
					SysLogger.info("----"+timer+"�Ѿ�ֹͣ���� ������Timer");
					task.cancel();
					timer.purge();//�Ƴ������Ѿ���ͣ������
					timer.canclethis(true);
					try {
						task = (TimerTask)Class.forName(task.getClass().getName()).newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					timer = new MonitorTimer();//�ؽ�timer
					timer.schedule(task, 0, period);//�½�Timer ������Task���� �����Ӽ���
					break;//�˳���while-trueѭ����  
				}
				try {
					this.sleep(period);//ͣ��һ��ʱ��֮�� ����Task�Ƿ�������ѵ�ж�
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else{
				break;
			}
		}
	}
}
