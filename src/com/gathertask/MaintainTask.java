package com.gathertask;


import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;
import java.util.Hashtable;
import com.gathertask.dao.Taskdao;
import com.gatherdb.nmsmemorydate;
import com.afunms.common.util.ShareData;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Portconfig;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.gathertask.TaskManager;



/**
 * 
 * ά���ɼ�����
 * 5���Ӽ��һ�βɼ����񣬼���Ѿ����ܵ������뱻ֹͣ��
 * @author konglq
 *
 */
public class MaintainTask extends TimerTask{

	@Override
	public void run() {
		// TODO Auto-generated method stub
//		System.out.println("================��ʱ��ʼά����ʱ����====================");
		Taskdao taskdao=new Taskdao();
		
		Hashtable nlist=taskdao.GetRunTaskList();
		TaskManager tkmanager=new TaskManager();
		
		
		//��ʼ���ڴ���е���Ϣ���бȽ�
		if(null!= nlist && nlist.size()>0)
		{
			//���ݿ���б��бȽ��ڴ��б�
			
			NodeGatherIndicators gathertask;
			NodeGatherIndicators gathertask2;
			//Iterator it= nlist.entrySet().iterator();
			
			Enumeration it1=nlist.elements(); 
			 
			  
			
			while(it1.hasMoreElements()){
				gathertask=(NodeGatherIndicators) it1.nextElement();
			    
				//�ڴ�����а����ж�Ӧ�Ķ�ʱ����
				if(nmsmemorydate.RunGatherLinst.containsKey(gathertask.getId()+""))
				{
					
					//System.out.println("==============1==========================");
					String itime=gathertask.getPoll_interval();
					String itype=gathertask.getInterval_unit();
					gathertask2=(NodeGatherIndicators) nmsmemorydate.RunGatherLinst.get(gathertask.getId()+"");
					//System.out.println("=====1===="+itime);
					//System.out.println("=====1==type=="+itype);
					//System.out.println("====2====="+gathertask2.getPoll_interval());
					//System.out.println("=====2===="+gathertask2.getInterval_unit());
				
					
					if(itime.equals(gathertask2.getPoll_interval()) && itype.equals(gathertask2.getInterval_unit()))
					{//���½���һ����ʱ����
						//tkmanager.createOneTask(gathertask);
						//System.out.println("==========������������============"+gathertask.getId());
					}else 
					  {
						//ע������
//						System.out.println("==�������ñ��޸���������============="+gathertask.getId()+"");
						//tkmanager.cancelTask(gathertask.getId()+"");
						tkmanager.createOneTask(gathertask);
					  }
					
				}else
				 {//�ڴ���û�ж�Ӧ�Ķ�ʱ����
//					System.out.println("===========�����¶�ʱ����============="+gathertask.getId());
					tkmanager.createOneTask(gathertask);
				 }
			}
			
			
//			 System.out.println("=====��ʼ�����ڴ��б�====");
			
		 //�����ڴ��б���ҵ�ǰ��ʱ������
			 if(nmsmemorydate.RunGatherLinst.size()>0)
			 {
			 it1=nmsmemorydate.RunGatherLinst.elements();
			 
			 while(it1.hasMoreElements()){
					gathertask2=(NodeGatherIndicators) it1.nextElement();
					//�ڴ�����а����ж�Ӧ�Ķ�ʱ����
					
					//System.out.println("=======%%%%===="+nlist.size());
					if(!nlist.containsKey(gathertask2.getId()+""))
					{
//					  System.out.println("=====�����Ѿ�ֹͣ===="+gathertask2.getId());
					  tkmanager.cancelTask(gathertask2.getId()+"");

					}
			
			
		       }
			 }
	  }else
	    {
		  tkmanager.canceAlllTask();
	    }
		
		
		
//		System.out.println("================ά����ʱ�������==========������======="+nmsmemorydate.TaskList.size());
	
	
	
	}
	
	
	
	
}
	
	
	  

