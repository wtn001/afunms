/**
 * 
 * ����XML��TASK��Ϣ�������������
 * ����������󱣴����б�TaskList��
 * 
 * һ��������ʱ�������
 * һ���ǿ��Դ������б��а��������ע��
 */
package com.gathertask;

import java.util.Enumeration;
import java.util.Timer;
import org.apache.log4j.Logger;


import com.afunms.indicators.model.NodeGatherIndicators;
import com.gatherdb.GatherDataAlarmsqlRun;
import com.gatherdb.GatherDatatempsqlRun;
import com.gatherdb.GathersqlRun;
import com.gatherdb.nmsmemorydate;
import java.util.Hashtable;
import com.gathertask.dao.Taskdao;
import com.gathertask.MaintainTask;





public class TaskManager {
	
	private Logger logger = Logger.getLogger(TaskManager.class);
	
	
	
/**
 * �������еĲɼ�����
 * @param taskinterval ����Ĳɼ�Ƶ��
 * @param taskid �����id
 * @param taskname ���������
 * @param tasktype ���������
 * @param tasksubtype
 */
	public  void createOneTask(NodeGatherIndicators nodeGatherIndicators)
	{
		Timer timer=null;
		BaskTask btask=null;
	     
		//����Hashtable �еĲ������ж����ж������ɼ�������
		if(null!=nmsmemorydate.TaskList && nmsmemorydate.TaskList.size()>0&& nmsmemorydate.TaskList.containsKey(nodeGatherIndicators.getId()))
		{
            //ֹͣԭ����timer���б��Ҵ��ڴ���ɾ����Ӧ�Ķ���
			timer=(Timer)nmsmemorydate.TaskList.get(nodeGatherIndicators.getId());
			timer.cancel();
			nmsmemorydate.TaskList.remove(nodeGatherIndicators.getId());
			nmsmemorydate.RunGatherLinst.remove(nodeGatherIndicators.getId());
		}else
		  {
			//������ʱ�ɼ�����
			timer = new Timer();
			btask=new BaskTask();
			btask.setRunclasspath((String)nodeGatherIndicators.getClasspath());//�������·��
			btask.setTaskid(nodeGatherIndicators.getId()+"");
			btask.setNodeid((String)nodeGatherIndicators.getNodeid());
			btask.setTaskname(nodeGatherIndicators.getName());
			btask.setRunclasspath(nodeGatherIndicators.getClasspath());
			btask.setGather(nodeGatherIndicators);
			
			long intervaltime=Integer.parseInt(nodeGatherIndicators.getPoll_interval());
			if(nodeGatherIndicators.getInterval_unit().equals("s"))
			{		
				intervaltime=intervaltime*1000;
			}
			if(nodeGatherIndicators.getInterval_unit().equals("m"))
			{				
				intervaltime=intervaltime*1000*60;
			}
			if(nodeGatherIndicators.getInterval_unit().equals("h"))
			{
				intervaltime=intervaltime*1000*60*60;
			}
			if(nodeGatherIndicators.getInterval_unit().equals("d"))
			{
				intervaltime=intervaltime*1000*60*60*24;
			}
			
			long in=0;
			if(nmsmemorydate.TaskList.size()>300)
			{
				in=(nmsmemorydate.TaskList.size()/5)*200;
				
			}else
			{
				in=nmsmemorydate.TaskList.size()*200;
			}
			
			timer.schedule(btask, in, intervaltime);//������ִ�ж�ʱ����
			nmsmemorydate.TaskList.put(nodeGatherIndicators.getId()+"", timer);//��TIMER�����������
			nmsmemorydate.RunGatherLinst.put(nodeGatherIndicators.getId()+"", nodeGatherIndicators);
		}
		
	}

	

	/**
	 * ����һ��ά������
     * 5���Ӷ�ʱ���һ��timer�Ƿ���Ҫ���У����Ƕ�ʱʱ���Ѿ��ı�
	 */
		public void CreateMaintainTask()
		{
			if(!nmsmemorydate.MaintainTaskStatus)
			{
			Timer timer=null;
			MaintainTask btask=null;
			timer = new Timer();
			btask=new MaintainTask();
			timer.schedule(btask, 1000, 1*1000*60);//������ִ�ж�ʱ����
			nmsmemorydate.MaintainTaskStatus=true;//���ñ��Ϊ����
			nmsmemorydate.MaintainTasktimer=timer;
			}
				
	   }
		
		
		/**
		 * ���ݷ���ģʽ��ⶨʱ����
	     * 1�������һ��
		 */
			public void CreateDataTempTask()
			{
				//if(null!=nmsmemorydate.GatherDatatempsqlTasktimer)
				//{
				Timer timer=null;
				GatherDatatempsqlRun btask=null;
				timer = new Timer();
				btask=new GatherDatatempsqlRun();
				timer.schedule(btask, 20000, 10*1000);//������ִ�ж�ʱ����
				
				nmsmemorydate.GatherDatatempsqlTasktimer=timer;
				//}
					
		   }
			
			
			/**
			 * 
		     * ��������
			 */
				public void CreateGCTask()
				{
					//if(null!=nmsmemorydate.GatherDatatempsqlTasktimer)
					//{
					Timer timer=null;
					GcTask btask=null;
					timer = new Timer();
					btask=new GcTask();
					timer.schedule(btask, 20000, 5*1000*60);//������ִ�ж�ʱ����
					
					//nmsmemorydate.GatherDatatempsqlTasktimer=timer;
					//}
						
			   }
			
		
		
		/**
		 * ����һ��ά������
	     * 5���Ӷ�ʱ���һ��timer�Ƿ���Ҫ���У����Ƕ�ʱʱ���Ѿ��ı�
		 */
			public void CreateGahterSQLTask()
			{
				if(!nmsmemorydate.GathersqlTaskStatus)
				{
				Timer timer=null;
				GathersqlRun btask=null;
				timer = new Timer();
				btask=new GathersqlRun();
				timer.schedule(btask, 0, 5*1000);//5�������һ��
				nmsmemorydate.GathersqlTaskStatus=true;//���ñ��Ϊ����
				nmsmemorydate.GathersqlTasktimer=timer;
				}
					
		   }
			
		

	
	
	/**
	 * 
	 * �������ݿ��ļ�¼�����ɼ�����
	 * 
	 * 
	 */
    public void createAllTask()
		{
			Timer timer=null;
			
			BaskTask btask=null;
			Taskdao taskdao=new Taskdao();
			
			Hashtable runtask=taskdao.GetRunTaskList();
			
			logger.info("=�ɼ��������=="+runtask.size());
			if(null!=runtask)
			{//�����Ϊ����ѭ��

				 Enumeration allvalue=runtask.elements(); 
				 
				  while(allvalue.hasMoreElements())     
				  {     
					 
					     NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators)allvalue.nextElement();     
				     
						//����Hashtable �еĲ������ж����ж������ɼ�������
						if(null!=nmsmemorydate.TaskList && nmsmemorydate.TaskList.size()>0&& nmsmemorydate.TaskList.containsKey(nodeGatherIndicators.getId()))
						{
				            //ֹͣԭ����timer���б��Ҵ��ڴ���ɾ����Ӧ�Ķ���
							timer=(Timer)nmsmemorydate.TaskList.get(nodeGatherIndicators.getId());
							timer.cancel();
							nmsmemorydate.TaskList.remove(nodeGatherIndicators.getId());
							nmsmemorydate.RunGatherLinst.remove(nodeGatherIndicators.getId());
						}else
						  {//������ʱ�ɼ�����
							timer = new Timer();
							btask=new BaskTask();
							btask.setRunclasspath((String)nodeGatherIndicators.getClasspath());//�������·��
							btask.setTaskid(nodeGatherIndicators.getId()+"");
							btask.setNodeid((String)nodeGatherIndicators.getNodeid());
							btask.setTaskname(nodeGatherIndicators.getName());
							btask.setRunclasspath(nodeGatherIndicators.getClasspath());
							btask.setGather(nodeGatherIndicators);
							
							//SysLogger.info(nodeGatherIndicators.getClasspath()+"---------"+nodeGatherIndicators.getPoll_interval());
							long intervaltime=Integer.parseInt(nodeGatherIndicators.getPoll_interval());
							if(nodeGatherIndicators.getInterval_unit().equals("s"))
							{		
								intervaltime=intervaltime*1000;
							}
							if(nodeGatherIndicators.getInterval_unit().equals("m"))
							{
								
								intervaltime=intervaltime*1000*60;
							}
							if(nodeGatherIndicators.getInterval_unit().equals("h"))
							{
								intervaltime=intervaltime*1000*60*60;
							}
							if(nodeGatherIndicators.getInterval_unit().equals("d"))
							{
								intervaltime=intervaltime*1000*60*60*24;
							}
							
							long in=0;
							if(nmsmemorydate.TaskList.size()>300)
							{
								in=(nmsmemorydate.TaskList.size()/5)*200;
								
							}else
							{
								in=nmsmemorydate.TaskList.size()*200;
							}
							
							timer.schedule(btask, in+1000, intervaltime);//������ִ�ж�ʱ����
							nmsmemorydate.TaskList.put(nodeGatherIndicators.getId()+"", timer);//��TIMER�����������
							//SysLogger.info(nodeGatherIndicators.getName()+"==="+nodeGatherIndicators.getClasspath()+"==="+nodeGatherIndicators.getType()+"==="+nodeGatherIndicators.getSubtype()+"==="+nodeGatherIndicators.getPoll_interval()+nodeGatherIndicators.getInterval_unit());
							nmsmemorydate.RunGatherLinst.put(nodeGatherIndicators.getId()+"", nodeGatherIndicators);
						}
					  
					  
				  } 
				 
					
				
					
				}
				
		}

	
	

	/**
	 * 
	 * ����id�Ѳɼ�����ֹͣ
	 * 
	 * @param id
	 */
    public synchronized void cancelTask(String id)
    {
    	System.out.println("====ֹͣ����=="+id);
    	
    	if(null!=nmsmemorydate.TaskList.get(id) )
    	{
    	((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//ע��������
    	nmsmemorydate.TaskList.remove(id+"");
    	nmsmemorydate.RunGatherLinst.remove(id+"");
    	
    	}
    	
    }
	
    
    
	/**
	 * 
	 * ȡ�����еĲɼ�����
	 * 
	 * @param id
	 */
    public  void canceAlllTask()
    {
    	if(nmsmemorydate.TaskList.size()>0)
    	{
    	 Enumeration allvalue=nmsmemorydate.TaskList.elements(); 
    	 Enumeration key=nmsmemorydate.TaskList.keys();
		  while(allvalue.hasMoreElements())     
		  {     
		      
    	     //(Timer) allvalue.nextElement(); 
    	      String id=(String)key.nextElement();
    	    if(null!=(Timer) allvalue.nextElement() )
    	    {
    	    ((Timer) nmsmemorydate.TaskList.get(id+"")).cancel();//ע��������
    	     nmsmemorydate.TaskList.remove(id);
    	     nmsmemorydate.RunGatherLinst.remove(id);
    	    }
    	    
		}
		  
		  nmsmemorydate.TaskList.clone();
		  nmsmemorydate.RunGatherLinst.clone();
    	}
	  logger.info("======���ע���ɼ�����=====");
    	
    }
    
	
	
    /**
     * �����澯������⣬�澯����ɾ��
     * 
     */
    public void CreateGahterAlarmSQLTask()
    {
 	   
 		//if(null!=nmsmemorydate.GatherDatatempsqlTasktimer)
 		//{
 		Timer timer=null;
 		GatherDataAlarmsqlRun btask=null;
 		timer = new Timer();
 		btask=new GatherDataAlarmsqlRun();
 		timer.schedule(btask, 500, 1000*30);//������ִ�ж�ʱ����
 		
 		//nmsmemorydate.GatherDatatempsqlTasktimer=timer;
 		//}
 			
 	   
 	   
    }
    
 
	
	public static void main(String [] arg)
	{
		System.out.println("----------------------");
		TaskManager manager=new TaskManager();
		
		manager.createAllTask();
		
		System.out.println("++++���г���="+nmsmemorydate.TaskList.size());
		//manager.canceAlllTask();
		System.out.println("++++���г���="+nmsmemorydate.TaskList.size());
		
	}
	
	
    
}
