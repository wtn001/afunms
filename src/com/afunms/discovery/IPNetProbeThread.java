/**
 * <p>Description:probe the router table</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-13
 */

package com.afunms.discovery;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.afunms.common.util.*;
import com.afunms.polling.task.ThreadPool;
import com.afunms.topology.dao.DiscoverConfigDao;
import com.afunms.topology.model.DiscoverConfig;

public class IPNetProbeThread extends BaseThread
//public class IPNetProbeThread implements Runnable
{
   private Host node;
   private List addressList;
   private SnmpUtil snmp = null;
   public IPNetProbeThread(Host node,List list)
   {
      this.node = node;
      addressList = list;
   }
   
   public void run()
   {       
	   if(DiscoverEngine.getInstance().getStopStatus() == 1)return;
	   snmp = SnmpUtil.getInstance();
	   SysLogger.info("��ʼ�����豸"+node.getIpAddress()+"�ĵ�ַת����");	    
	   Set shieldList = DiscoverResource.getInstance().getShieldSet();
	   List netshieldList = DiscoverResource.getInstance().getNetshieldList();
	   List netincludeList = DiscoverResource.getInstance().getNetincludeList();
	   IfEntity ifEntity = null;
	   IfEntity endifEntity = null;

       // �����̳߳�
	   //ThreadPool threadPool = new ThreadPool(addressList.size());	
	   int threadNum = 10;
	   
	   if(addressList.size() < 10){
		   threadNum = addressList.size();
	   }
	   
	   
	   ThreadPool threadPool = null;
//	   int MAX_THREADS = DiscoverResource.getInstance().getMaxThreads(); //����߳���,����tomcat����   
//	   ExecutorService runnableExecutor = Executors.newFixedThreadPool(MAX_THREADS);
	   try{
		   threadPool = new ThreadPool(threadNum);	
		   for(int i=0;i<addressList.size();i++)
		   {
			   //###################################
			   //������Ҫ������,�򲻽��н�������ARP��ɼ�
			   //if(1==1)continue;
			   //###################################   
				 try{
				   //node.updateCount(2);     	      	 
				   IpAddress ipAddr = (IpAddress)addressList.get(i);
				   threadPool.runTask(NetMediThread.createTask(ipAddr, node)); 	
//				   runnableExecutor.execute(NetMediThread.createTask(ipAddr, node));
				   //SysLogger.info("��"+node.getIpAddress()+"�ĵ�ַת������IP "+ipAddr.getIpAddress()+",��ʼ����");
				 }catch(Exception ex){
					 ex.printStackTrace();
				 }
	      }//end_for
		   // �ر��̳߳ز��ȴ������������
	  	   threadPool.join();
	  	   threadPool.close();
	  	   threadPool = null;
	   }catch(Exception e){
		   
	   }
	   
//	   try{
//		   //threadPool = new ThreadPool(threadNum);	
//		   for(int i=0;i<addressList.size();i++)
//		   {
//			   //###################################
//			   //������Ҫ������,�򲻽��н�������ARP��ɼ�
//			   //if(1==1)continue;
//			   //###################################   
//				 try{
//				   //node.updateCount(2);     	      	 
//				   IpAddress ipAddr = (IpAddress)addressList.get(i);
//				   DiscoverEngine.getInstance().addRunnable(NetMediThread.createTask(ipAddr, node));
//				   //threadPool.runTask(NetMediThread.createTask(ipAddr, node)); 		   
//				   //SysLogger.info("��"+node.getIpAddress()+"�ĵ�ַת������IP "+ipAddr.getIpAddress()+",��ʼ����");
//				 }catch(Exception ex){
//					 ex.printStackTrace();
//				 }
//	      }//end_for
//		   // �ر��̳߳ز��ȴ������������
//	   }catch(Exception e){
//		   
//	   }

	   DiscoverEngine.getInstance().addDiscoverdcount(); 
	   snmp = null;
	   setCompleted(true);
	   //**************************************
	   //setCompleted(true); ����THREAD ��Ҫ�ſ�
	   //**************************************
	   
   }//end_run
   public void analsysNDPHost(Host node,Host host){
     	if(host.getNdpHash() == null)
      		host.setNdpHash(snmp.getNDPTable(host.getIpAddress(),host.getCommunity()));
      	//�жϸ�NDP���Ƿ����node��MAC��ַ,������,��˵��node�Ǹ����豸ֱ�����ӹ�ϵ
      	Hashtable hostNDPHash = host.getNdpHash();
 		if(host.getMac() == null){
      		String descr = (String)hostNDPHash.get(node.getMac());
      		IfEntity nodeIfEntity = node.getIfEntityByDesc(descr);
      		Link link = new Link();		           
      		link.setStartId(node.getId());          
      		link.setStartIndex(nodeIfEntity.getIndex());
      		link.setStartIp(node.getIpAddress());
      		link.setStartPhysAddress(node.getMac()); //��¼������mac
      		link.setStartDescr(descr);
      		link.setEndIp(host.getIpAddress());
      		DiscoverEngine.getInstance().addHost(host,link);
  		}else{
  			if(node.getNdpHash() != null){
          		if(node.getNdpHash().containsKey(host.getMac())){
          			String hostdesc = (String)node.getNdpHash().get(host.getMac());
              		String descr = (String)hostNDPHash.get(node.getMac());
              		IfEntity nodeIfEntity = node.getIfEntityByDesc(descr);
              		IfEntity hostIfEntity = host.getIfEntityByDesc(hostdesc);
              		Link link = new Link();		           
              		link.setStartId(node.getId());          
              		link.setStartIndex(nodeIfEntity.getIndex());
              		link.setStartIp(node.getIpAddress());
              		link.setStartPhysAddress(node.getMac()); //��¼������mac
              		link.setStartDescr(descr);
              		link.setEndIndex(hostIfEntity.getIndex());
              		link.setEndIp(host.getIpAddress());
              		link.setEndPhysAddress(hostIfEntity.getPhysAddress());
              		link.setEndDescr(hostdesc);
              		DiscoverEngine.getInstance().addHost(host,link);
          		}else{
              		String descr = (String)hostNDPHash.get(node.getMac());
              		IfEntity nodeIfEntity = node.getIfEntityByDesc(descr);
              		Link link = new Link();		           
              		link.setStartId(node.getId());          
              		link.setStartIndex(nodeIfEntity.getIndex());
              		link.setStartIp(node.getIpAddress());
              		link.setStartPhysAddress(node.getMac()); //��¼������mac
              		link.setStartDescr(descr);
              		link.setEndIp(host.getIpAddress());
              		DiscoverEngine.getInstance().addHost(host,link);
          		}
  			}else{
  				String descr = (String)hostNDPHash.get(node.getMac());
          		IfEntity nodeIfEntity = node.getIfEntityByDesc(descr);
          		Link link = new Link();		           
          		link.setStartId(node.getId());          
          		link.setStartIndex(nodeIfEntity.getIndex());
          		link.setStartIp(node.getIpAddress());
          		link.setStartPhysAddress(node.getMac()); //��¼������mac
          		link.setStartDescr(descr);
          		link.setEndIp(host.getIpAddress());
          		DiscoverEngine.getInstance().addHost(host,link);
  			}

  		}
   }
}