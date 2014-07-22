package com.afunms.polling.snmp.device;

/*
 * @author hukelei@dhcc.com.cn
 *
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.Devicecollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.topology.model.HostNode;
import com.gatherResulttosql.HostDatatempDeviceRttosql;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WindowsDeviceSnmp extends SnmpMonitor {
	private Hashtable sendeddata = ShareData.getProcsendeddata();
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public WindowsDeviceSnmp() {
	}
	
	private static Hashtable device_Status = null;
	static {
		device_Status = new Hashtable();
		device_Status.put("1", "δ֪");
		device_Status.put("2", "����");
		device_Status.put("3", "�澯");
		device_Status.put("4", "����");
		device_Status.put("5", "ֹͣ");
	};
	private static Hashtable device_Type = null;
	static {
		device_Type = new Hashtable();
		device_Type.put("1.3.6.1.2.1.25.3.1.1", "����");
		device_Type.put("1.3.6.1.2.1.25.3.1.2", "δ֪");
		device_Type.put("1.3.6.1.2.1.25.3.1.3", "CPU");
		device_Type.put("1.3.6.1.2.1.25.3.1.4", "����");
		device_Type.put("1.3.6.1.2.1.25.3.1.5", "��ӡ��");
		device_Type.put("1.3.6.1.2.1.25.3.1.6", "����");
		device_Type.put("1.3.6.1.2.1.25.3.1.10", "�Կ�");
		device_Type.put("1.3.6.1.2.1.25.3.1.11", "����");
		device_Type.put("1.3.6.1.2.1.25.3.1.12", "Э������");
		device_Type.put("1.3.6.1.2.1.25.3.1.13", "����");
		device_Type.put("1.3.6.1.2.1.25.3.1.14", "���ƽ����");
		device_Type.put("1.3.6.1.2.1.25.3.1.15", "����");
		device_Type.put("1.3.6.1.2.1.25.3.1.16", "��ӡ��");
		device_Type.put("1.3.6.1.2.1.25.3.1.17", "����");
		device_Type.put("1.3.6.1.2.1.25.3.1.18", "�Ŵ�");
		device_Type.put("1.3.6.1.2.1.25.3.1.19", "ʱ��");
		device_Type.put("1.3.6.1.2.1.25.3.1.20", "��̬�ڴ�");
		device_Type.put("1.3.6.1.2.1.25.3.1.21", "�̶��ڴ�");
	};

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @see com.dhcc.webnms.host.snmp.AbstractSnmp#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
		Hashtable returnHash=new Hashtable();
		Vector deviceVector=new Vector();
		List cpuList = new ArrayList();
		Host node = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		//�ж��Ƿ��ڲɼ�ʱ�����
    	if(ShareData.getTimegatherhash() != null){
    		if(ShareData.getTimegatherhash().containsKey(node.getId()+":equipment")){
    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
    			int _result = 0;
    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(node.getId()+":equipment"));
    			if(_result ==1 ){
    				//SysLogger.info("########ʱ�����: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else if(_result == 2){
    				//SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
    			}else {
    				SysLogger.info("######## "+node.getIpAddress()+" ���ڲɼ��ڴ�ʱ�����,�˳�##########");
    				//���֮ǰ�ڴ��в����ĸ澯��Ϣ
//    			    try{
//    			    	//���֮ǰ�ڴ��в������ڴ�澯��Ϣ
//						CheckEventUtil checkutil = new CheckEventUtil();
//						checkutil.deleteEvent(node.getId()+":net:memory");
//    			    }catch(Exception e){
//    			    	e.printStackTrace();
//    			    }
    				return returnHash;
    			}
    			
    		}
    	}
    	
		try {
			Devicecollectdata devicedata=null;
			Calendar date=Calendar.getInstance();
			
			  try{
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				  com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(node.getIpAddress());
				  Date cc = date.getTime();
				  String time = sdf.format(cc);
				  snmpnode.setLastTime(time);
			  }catch(Exception e){
				  
			  }
			//-------------------------------------------------------------------------------------------device start
			  try{
			  String[] oids =                
				  new String[] {               
					  "1.3.6.1.2.1.25.3.2.1.1",  //hrDeviceIndex
					  "1.3.6.1.2.1.25.3.2.1.2",  //hrDeviceType
					  "1.3.6.1.2.1.25.3.2.1.3",    //hrDeviceDescr
					  "1.3.6.1.2.1.25.3.2.1.5" };   //hrDeviceStatus

			  String[][] valueArray = null;   	  
				try {
					//valueArray = SnmpUtils.getTableData(node.getIpAddress(), node.getCommunity(), oids, node.getSnmpversion(), 3, 1000*30);
					valueArray = SnmpUtils.getTableData(node.getIpAddress(),node.getCommunity(),oids,node.getSnmpversion(),
		   		  			node.getSecuritylevel(),node.getSecurityName(),node.getV3_ap(),node.getAuthpassphrase(),node.getV3_privacy(),node.getPrivacyPassphrase(),3,1000*30);
				} catch(Exception e){
					valueArray = null;
					//SysLogger.error(node.getIpAddress() + "_WindowsSnmp");
				}	
				for(int i=0;i<valueArray.length;i++){
					devicedata = new Devicecollectdata();
					String devindex = valueArray[i][0];
					String type = valueArray[i][1];
					String name = valueArray[i][2];
					String status = valueArray[i][3];
					if(status == null)status="";
					if(device_Status.containsKey(status))status = (String)device_Status.get(status);
					devicedata.setDeviceindex(devindex);
					devicedata.setIpaddress(node.getIpAddress());
					devicedata.setName(name);
					devicedata.setStatus(status);
					devicedata.setType((String)device_Type.get(type));
					deviceVector.addElement(devicedata);
					//SysLogger.info(name+"######"+devindex+"######"+(String)device_Type.get(type)+"######"+status);
				    
				}  
		   }catch(Exception e){
				  //e.printStackTrace();
		   }			   
		   //-------------------------------------------------------------------------------------------device end
			}catch(Exception e){
				//returnHash=null;
				//e.printStackTrace();
				//return null;
			}
		
//		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(node.getIpAddress());
//		if(ipAllData == null)ipAllData = new Hashtable();
//		ipAllData.put("device",deviceVector);
//	    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
//	    returnHash.put("device",deviceVector);
	    
	    if(!(ShareData.getSharedata().containsKey(node.getIpAddress())))
		{
			Hashtable ipAllData = new Hashtable();
			if(ipAllData == null)ipAllData = new Hashtable();
			if(deviceVector != null && deviceVector.size()>0)ipAllData.put("device",deviceVector);
			
		    ShareData.getSharedata().put(node.getIpAddress(), ipAllData);
		}else
		 {
			if(deviceVector != null && deviceVector.size()>0)((Hashtable)ShareData.getSharedata().get(node.getIpAddress())).put("device",deviceVector);
		 }
	    returnHash.put("device",deviceVector);
	    
	    
	    
	    
	    
	    String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	    if(!"0".equals(runmodel)){
			//�ɼ�������Ƿ���ģʽ,����Ҫ����������д����ʱ���
	    	//�Ѳɼ��������sql
		    HostDatatempDeviceRttosql totempsql =new HostDatatempDeviceRttosql();
		    totempsql.CreateResultTosql(returnHash, node);
	    }
	    
	    return returnHash;
	}
}





