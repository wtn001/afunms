/**
 * <p>Description:host,including server and exchange device</p>
 * <p>Company: dhcc.com</p>
 * @author afunms
 * @project afunms
 * @date 2006-08-27
 */

package com.afunms.polling.node;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import montnets.SmsDao;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.model.HostServiceGroup;
import com.afunms.application.model.HostServiceGroupConfiguration;
import com.afunms.application.model.JobForAS400Group;
import com.afunms.application.model.JobForAS400GroupDetail;
import com.afunms.application.model.ProcessGroup;
import com.afunms.application.model.ProcessGroupConfiguration;
import com.afunms.application.util.HostServiceGroupConfigurationUtil;
import com.afunms.application.util.JobForAS400GroupDetailUtil;
import com.afunms.application.util.ProcessGroupConfigurationUtil;
import com.afunms.common.util.CEIString;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.config.dao.PortconfigDao;
import com.afunms.config.model.Diskconfig;
import com.afunms.config.model.Portconfig;
import com.afunms.event.dao.EventListDao;
import com.afunms.event.dao.SmscontentDao;
import com.afunms.event.model.EventList;
import com.afunms.event.model.Smscontent;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.base.Node;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.CPUcollectdata;
import com.afunms.polling.om.DiscardsPerc;
import com.afunms.polling.om.Diskcollectdata;
import com.afunms.polling.om.ErrorsPerc;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Memorycollectdata;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.polling.om.Processcollectdata;
import com.afunms.polling.om.Servicecollectdata;
import com.afunms.topology.model.JobForAS400;
import com.afunms.topology.model.NodeMonitor;
import com.afunms.topology.util.NodeHelper;

public class Host extends Node
{
	private int localNet;     //��������
    private String netMask;   //��������
    private String community; //��ͬ��
    private String writecommunity; //��ͬ��
    private int snmpversion;//SNMP�汾
    private String sysOid;    //ϵͳoid 
    private String sysName;    //ϵͳ��    
    private Hashtable interfaceHash;       
    private String user;
    private String password;
    private String prompt="";
    public String showmessage;//yangjun �޸�
    private String mac;
    private int layer; //hukelei �޸ģ����ӵ�ǰ�豸�Ĳ㣬��������ͼ����
    
	//SNMP V3
	private int securitylevel; //��ȫ����  1:noAuthNopriv 2:authNoPriv 3:authPriv
	private String securityName; //�û���
	private int v3_ap;           //��֤Э��  1:MD5 2:SHA
	private String authpassphrase; //ͨ����
	private int v3_privacy;			//����Э�� 1:DES 2:AES128 3:AES196 4:AES256
	private String privacyPassphrase; //����Э����
	
       
	public Host()
    {
    }
  
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}
	
	public String getWritecommunity() {
		return writecommunity;
	}

	public void setWritecommunity(String writecommunity) {
		this.writecommunity = writecommunity;
	}
	
	public int getSnmpversion() {
		return snmpversion;
	}

	public void setSnmpversion(int snmpversion) {
		this.snmpversion = snmpversion;
	}

	public Hashtable getInterfaceHash() {
		return interfaceHash;
	}

	public void setInterfaceHash(Hashtable interfaceHash) {
		this.interfaceHash = interfaceHash;
	}

	public int getLocalNet() {
		return localNet;
	}

	public void setLocalNet(int localNet) {
		this.localNet = localNet;
	}

	public String getNetMask() {
		return netMask;
	}

	public void setNetMask(String netMask) {
		this.netMask = netMask;
	}

	public String getSysOid() {
		return sysOid;
	}

	public void setSysOid(String sysOid) {
		this.sysOid = sysOid;
	}
	
    /**
     * ���ӿ������ҵ��ӿ�
     */
    public IfEntity getIfEntityByIndex(String ifIndex)
    {    
    	if(interfaceHash==null||interfaceHash.size()==0)
    		return null;
    	
    	if(interfaceHash.get(ifIndex)==null)
    	{
    		System.out.println(ipAddress + "��û������Ϊ" + ifIndex + "�Ľӿ�");
    		return null;
    	}
		return (IfEntity)interfaceHash.get(ifIndex);
    }

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	
	public String doPoll()
	{
//		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//		List monitorItemList = new ArrayList();//alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(id+"", "net", "");
//    	try{
//    		//��ȡ�����õ����б�����ָ��
//    		if(category==4){
//    			monitorItemList = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(id+"", "host", "");
//    		}else{
//    			monitorItemList = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(id+"", "net", "");
//    		}   		
//    	}catch(Exception e){
//    		e.printStackTrace();
//    	}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		double cpuvalue = 0;
		double memoryvalue = 0;
		String pingvalue = null;
		String inhdx = "0";
		String outhdx = "0";
		String ifInUcastPkts = "0";
		String time = "";
		sysLocation = alias+"("+ipAddress+")";
		Vector ipPingData = null;
		Vector memoryVector = null;
		Vector cpuV = null;
		Vector diskVector = null; 
		Vector interfaceVector = new Vector();
		String runmodel = PollingEngine.getCollectwebflag(); 
		Hashtable ipAllData = null;
		if("0".equals(runmodel)){
			//�ɼ�������Ǽ���ģʽ
			ipAllData = (Hashtable)ShareData.getSharedata().get(ipAddress);
			ipPingData = (Vector)ShareData.getPingdata().get(ipAddress);
		}else{
			//�ɼ�������Ƿ���ģʽ  
			ipAllData = (Hashtable)ShareData.getAllNetworkData().get(ipAddress);
			ipPingData = (Vector)ShareData.getAllNetworkPingData().get(ipAddress);
		}
		
		if(ipAllData != null){
			cpuV = (Vector)ipAllData.get("cpu");
			memoryVector = (Vector)ipAllData.get("memory");
			diskVector = (Vector)ipAllData.get("disk");
			Vector allutil = (Vector)ipAllData.get("allutilhdx");
			if(allutil != null && allutil.size()==3){
				AllUtilHdx inutilhdx = (AllUtilHdx)allutil.get(0);
				inhdx = inutilhdx.getThevalue();
				AllUtilHdx oututilhdx = (AllUtilHdx)allutil.get(1);
				outhdx = oututilhdx.getThevalue();
			}
			interfaceVector = (Vector)ipAllData.get("interface");
		}
		if(cpuV != null && cpuV.size()>0){
			CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
			if(cpu != null && cpu.getThevalue() != null){
				cpuvalue = new Double(cpu.getThevalue());
			}
		}
		if(memoryVector != null && memoryVector.size()>0){
			Memorycollectdata memory = (Memorycollectdata)memoryVector.get(0);
			if(memory != null && memory.getThevalue() != null){
				memoryvalue = new Double(memory.getThevalue());
			}
		}
		if(ipPingData != null && ipPingData.size() > 0){
			Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
			pingvalue = pingdata.getThevalue();
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			time = sdf.format(cc);		
			lastTime = time;
		}
		
		StringBuffer msg = new StringBuffer(300);
		msg.append("<font color='green'>����:");		
		msg.append(NodeHelper.getNodeCategory(category));
		msg.append("</font><br>");
		if(category==4)
		{
			msg.append("��������");
			msg.append(sysName);
			msg.append("<br>����:");
			msg.append(alias);
			msg.append("<br>");
		}
		else
		{
		   msg.append("�豸��ǩ:");
		   msg.append(alias);
		   msg.append("<br>");
		}
		msg.append("IP��ַ:");
		msg.append(ipAddress);
		msg.append("<br>");
//		alarm = false;
//		status = 0;	
		
		if(!managed){
			setPrompt(msg.toString());
			return msg.toString();
		}
		StringBuffer alarmMsg = new StringBuffer(200);
//		System.out.println("ipAddress====="+ipAddress+"-------moidList.size()====="+moidList.size());
		if(moidList!=null&&moidList.size()>0){
//			CheckEventDao checkeventdao = null;
			Hashtable checkEventHash = ShareData.getCheckEventHash();	
			try{
//				checkeventdao = new CheckEventDao();
				for(int i=0;i<moidList.size();i++){
					AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) moidList.get(i);
					if(alarmIndicatorsNode!=null){
						int flag = 0;
						try {
							if(checkEventHash!=null&&checkEventHash.size()>0){
//								List<CheckEvent> elist = checkeventdao.findCheckEvent(id+"",alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),alarmIndicatorsNode.getName());
								if(checkEventHash.get(id+":"+alarmIndicatorsNode.getType()+":"+alarmIndicatorsNode.getSubtype()+":"+alarmIndicatorsNode.getName())!=null){
									flag = (Integer)checkEventHash.get(id+":"+alarmIndicatorsNode.getType()+":"+alarmIndicatorsNode.getSubtype()+":"+alarmIndicatorsNode.getName());//elist.size();
								}
							}
							if(alarmIndicatorsNode.getType().equalsIgnoreCase("net")){
				        		//�����豸
				        		if(alarmIndicatorsNode.getName().equals("cpu")){
				        			//CPU������
				        			if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				        				if(flag>0){
				            				//�и澯����
				            				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
//				            				alarm = true;
//				            				if(status < flag)status = flag;
//				            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				            			}  
				            			msg.append(alarmIndicatorsNode.getDescr() + ":" + cpuvalue + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
				        			}	
				        		}else if(alarmIndicatorsNode.getName().equals("memory")){
				        			//CPU������
				        			if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				            			if(flag>0){
				            				//�и澯����
				            				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
//				            				alarm = true;
//				            				if(status < flag)status = flag;
//				            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				            			}  
				            			msg.append(alarmIndicatorsNode.getDescr() + ":" + memoryvalue + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
				        			}	
				        		}else if(alarmIndicatorsNode.getName().equals("ping")){
				        			//������ͨ��
				        			if(pingvalue == null || pingvalue.trim().length()==0){
				        				//û�ж�PING���ݽ��вɼ�,����Ҫ�澯���
				        				pingvalue="0";
				        				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				                			msg.append(alarmIndicatorsNode.getDescr() + ":" + pingvalue + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
				            			}
				        			}else{
				        				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				                			if(flag>0){
				                				//�и澯����
				                				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
//				                				alarm = true;
//				                				if(status < flag)status = flag;
//				                				if(this.alarmlevel < flag)this.alarmlevel=flag;
				                			}else{
				                				//�ж�֮ǰ�Ƿ��и澯,�������͸澯�ָ���Ϣ
//				                				Hashtable createeventdata = ShareData.getCreateEventdata();
//				                				if (createeventdata.containsKey("network:ping:"+ipAddress)){
//				                					//���͸澯�ָ���Ϣ,��д�澯�ָ���ʾ
//				                					createeventdata.remove("network:ping:"+ipAddress);
//				                					try{
//				                						createSMS("network","ping",ipAddress,getId()+"","���ϻָ�:"+ipAddress+" "+alarmIndicatorsNode.getAlarm_info()+"�Ĺ����ѻָ�",0,0,0);
//				                					}catch(Exception e){
//				                    					e.printStackTrace();
//				                    				}
//				                				}
				                				
				                			}
				                			msg.append(alarmIndicatorsNode.getDescr() + ":" + pingvalue + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
				            			}
				        			}
				        		}else if(alarmIndicatorsNode.getName().equals("AllInBandwidthUtilHdx")){
				        			//�ӿ���Ϣ
				        			//�������
				    				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				    					if(inhdx != null )msg.append(alarmIndicatorsNode.getDescr() + ":" + inhdx + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
				        				if(flag>0){
				            				//�и澯����
				            				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"��ǰֵ:"+inhdx+" ��ֵ:"+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//				            				alarm = true;
//				            				if(status < flag)status = flag;
//				            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				            			}
				    				}
				        		}else if(alarmIndicatorsNode.getName().equals("AllOutBandwidthUtilHdx")){
				        			//�ӿ���Ϣ
				        			//��������
				    				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				    					if(inhdx != null )msg.append(alarmIndicatorsNode.getDescr() + ":" + inhdx + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
//				    					int flag = 0;
//				    					flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"AllOutBandwidthUtilHdx");
				        				if(flag>0){
				            				//�и澯����
				            				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"��ǰֵ:"+inhdx+" ��ֵ:"+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//				            				alarm = true;
//				            				if(status < flag)status = flag;
//				            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				            			}
				    				}
				        		}else if(alarmIndicatorsNode.getName().equals("interface")){
				        			//�ӿ���Ϣ
				        			
				        			if (interfaceVector != null && interfaceVector.size()>0){
				        				//�ӿ�DOWN�澯
					        			if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
					    					List portconfiglist = new ArrayList();
					    					PortconfigDao configdao = new PortconfigDao(); 			
					    					Portconfig portconfig = null;
					    					Hashtable portconfigHash = new Hashtable();
					    					try {
					    						portconfiglist = configdao.getBySms(ipAddress);
					    					} catch (RuntimeException e) {
					    						e.printStackTrace();
					    					} finally {
					    						configdao.close();
					    					}
					    					if(portconfiglist != null && portconfiglist.size()>0){
					    						for(int k=0;k<portconfiglist.size();k++){
					    							portconfig = (Portconfig)portconfiglist.get(k);
					    							portconfigHash.put(portconfig.getPortindex()+"", portconfig);
					    							//SysLogger.info("add ===="+portconfig.getPortindex()+"");
					    						}
					    					}
					    					portconfig = null;
					    					
					    					for(int m=0;m<interfaceVector.size();m++){
					    						Interfacecollectdata interfacedata=(Interfacecollectdata)interfaceVector.get(m);
					    						if(interfacedata != null){
					    							
					    							if(interfacedata.getCategory().equalsIgnoreCase("Interface")&& interfacedata.getEntity().equalsIgnoreCase("ifOperStatus") && interfacedata.getSubentity() != null){
					    								if(portconfigHash.containsKey(interfacedata.getSubentity())){
					    									//���ڶ˿�����,���ж��Ƿ�DOWN
					    									portconfig = (Portconfig)portconfigHash.get(interfacedata.getSubentity());
					    									if (!"up".equalsIgnoreCase(interfacedata.getThevalue())){
					    										//�и澯����
					    			            				alarmMsg.append("�˿� "+portconfig.getName()+" "+portconfig.getLinkuse()+" down"+"<br>");
//					    			            				alarm = true;
//					    			            				if(status < 3)status = 3;
//					    			            				if(this.alarmlevel < 3)this.alarmlevel=3;
					    									}
					    								}
					    							}
					    						}
					    					}      			
					        			}
				        				
				        			}else if(alarmIndicatorsNode.getName().equals("ifInMulticastPkts")){
				        				//��ڵ���
				        				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				        					Hashtable sharedata = ShareData.getSharedata();
				            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
				            			    if(ipdata == null)ipdata = new Hashtable();
				            			    Vector tempv = (Vector)ipdata.get("inpacks");
				            			    if (tempv != null && tempv.size()>0){
				        						for(int k=0;k<tempv.size();k++){
				        							InPkts inpacks=(InPkts)tempv.elementAt(k);
				        							if(inpacks.getEntity().equalsIgnoreCase("ifInMulticastPkts")){
//				        								int flag = 0;
//				        								flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"ifInMulticastPkts");
				        		        				if(flag>0){
				        		            				//�и澯����
				        		            				alarmMsg.append("��"+inpacks.getSubentity()+"�˿�"+alarmIndicatorsNode.getAlarm_info()+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//				        		            				alarm = true;
//				        		            				if(status < flag)status = flag;
//				        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				        		            			}
				        							}
				        							
				        						}
				            			    }
				        				}
				        				
				        			}else if(alarmIndicatorsNode.getName().equals("ifInBroadcastPkts")){
				        				//��ڷǵ���
				        				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				        					Hashtable sharedata = ShareData.getSharedata();
				            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
				            			    if(ipdata == null)ipdata = new Hashtable();
				            			    Vector tempv = (Vector)ipdata.get("inpacks");
				            			    if (tempv != null && tempv.size()>0){
				        						for(int k=0;k<tempv.size();k++){
				        							InPkts inpacks=(InPkts)tempv.elementAt(k);
				        							if(inpacks.getEntity().equalsIgnoreCase("ifInBroadcastPkts")){
//				        								int flag = 0;
//				        								flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"ifInBroadcastPkts");
				        		        				if(flag>0){
				        		            				//�и澯����
				        		            				alarmMsg.append("��"+inpacks.getSubentity()+"�˿�"+alarmIndicatorsNode.getAlarm_info()+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//				        		            				alarm = true;
//				        		            				if(status < flag)status = flag;
//				        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				        		            			}
				        							}
				        							
				        						}
				            			    }
				        				}
				        				
				        			}else if(alarmIndicatorsNode.getName().equals("ifOutMulticastPkts")){
				        				//���ڵ���
				        				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				        					Hashtable sharedata = ShareData.getSharedata();
				            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
				            			    if(ipdata == null)ipdata = new Hashtable();
				            			    Vector tempv = (Vector)ipdata.get("outpacks");
				            			    if (tempv != null && tempv.size()>0){
				        						for(int k=0;k<tempv.size();k++){
				        							OutPkts outpacks=(OutPkts)tempv.elementAt(k);
				        							if(outpacks.getEntity().equalsIgnoreCase("ifOutMulticastPkts")){
//				        								int flag = 0;
//				        								flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"ifOutMulticastPkts");
				        		        				if(flag>0){
				        		            				//�и澯����
				        		            				alarmMsg.append("��"+outpacks.getSubentity()+"�˿�"+alarmIndicatorsNode.getAlarm_info()+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//				        		            				alarm = true;
//				        		            				if(status < flag)status = flag;
//				        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				        		            			}
				        							}
				        							
				        						}
				            			    }
				        				}
				        				
				        			}else if(alarmIndicatorsNode.getName().equals("ifOutBroadcastPkts")){
				        				//���ڷǵ���
				        				if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
				        					Hashtable sharedata = ShareData.getSharedata();
				            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
				            			    if(ipdata == null)ipdata = new Hashtable();
				            			    Vector tempv = (Vector)ipdata.get("outpacks");
				            			    if (tempv != null && tempv.size()>0){
				        						for(int k=0;k<tempv.size();k++){
				        							OutPkts outpacks=(OutPkts)tempv.elementAt(k);
				        							if(outpacks.getEntity().equalsIgnoreCase("ifOutBroadcastPkts")){
//				        								int flag = 0;
//				        								flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"ifOutBroadcastPkts");
				        		        				if(flag>0){
				        		            				//�и澯����
				        		            				alarmMsg.append("��"+outpacks.getSubentity()+"�˿�"+alarmIndicatorsNode.getAlarm_info()+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//				        		            				alarm = true;
//				        		            				if(status < flag)status = flag;
//				        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
				        		            			}
				        							}
				        							
				        						}
				            			    }
				        				}
				        			} else if(alarmIndicatorsNode.getName().equals("discardsperc")){
										//�˿ڶ�����
										Hashtable sharedata = ShareData.getSharedata();
									    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
									    if(ipdata == null)ipdata = new Hashtable();
									    Vector tempv = (Vector)ipdata.get("discardsperc");
									    if (tempv != null && tempv.size()>0){
											for(int k=0;k<tempv.size();k++){
												DiscardsPerc discardsPerc=(DiscardsPerc)tempv.elementAt(k);
//												int flag = 0;
//		        								flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"discardsperc");
		        		        				if(flag>0){
		        		            				//�и澯����
		        		            				alarmMsg.append("��"+discardsPerc.getSubentity()+"�˿�"+alarmIndicatorsNode.getAlarm_info()+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//		        		            				alarm = true;
//		        		            				if(status < flag)status = flag;
//		        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
		        		            			} else {
													if(discardsPerc.getThevalue() != null)msg.append(alarmIndicatorsNode.getDescr() + ":" + discardsPerc.getThevalue() + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
												}
												
											}
									    }
									} else if(alarmIndicatorsNode.getName().equals("errorsperc")){
										//�˿ڴ�����
										Hashtable sharedata = ShareData.getSharedata();
									    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
									    if(ipdata == null)ipdata = new Hashtable();
									    Vector tempv = (Vector)ipdata.get("errorsperc");
									    if (tempv != null && tempv.size()>0){
											for(int k=0;k<tempv.size();k++){
												ErrorsPerc errorsPerc=(ErrorsPerc)tempv.elementAt(k);
//												int flag = 0;
//		        								flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"errorsperc");
		        		        				if(flag>0){
		        		            				//�и澯����
		        		            				alarmMsg.append("��"+errorsPerc.getSubentity()+"�˿�"+alarmIndicatorsNode.getAlarm_info()+alarmIndicatorsNode.getLimenvalue0()+"<br>");
//		        		            				alarm = true;
//		        		            				if(status < flag)status = flag;
//		        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
		        		            			} else {
													if(errorsPerc.getThevalue() != null)msg.append(alarmIndicatorsNode.getDescr() + ":" + errorsPerc.getThevalue() + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
												}
											}
									    }
									}
				        		}
				        	}else if(alarmIndicatorsNode.getType().equalsIgnoreCase("host")){
				        		//�����豸
				        		//SysLogger.info(nm.getType()+"======"+nm.getName());
//				        		System.out.println(ipAddress+"=="+alarmIndicatorsNode.getType()+"======"+alarmIndicatorsNode.getName());
				        		if(alarmIndicatorsNode.getName().equals("cpu")){
				        			try {
										//CPU������
										if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
//											int flag = 0;
//											flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"cpu");
											if(flag>0){
												//�и澯����
												alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
//				            				alarm = true;
//				            				if(status < flag)status = flag;
//				            				if(this.alarmlevel < flag)this.alarmlevel=flag;
											}       			
											msg.append(alarmIndicatorsNode.getDescr() + ":" + cpuvalue + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
//									System.out.println(ipAddress+"======"+alarmIndicatorsNode.getName()+"==="+alarmMsg);
				        		}else if(alarmIndicatorsNode.getName().equals("ping")){
				        			try {
										//��ͨ��
										//SysLogger.info("### ��ʼ����"+nm.getName()+" PING���ݵ�XML��Ϣ####");
										if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
											if(pingvalue == null || pingvalue.trim().length()==0){
												pingvalue="0";
											}else{
//												int flag = 0;
//												flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+"ping");
												SysLogger.info(alarmIndicatorsNode.getName()+" "+alarmIndicatorsNode.getNodeid()+" flag="+flag);
												if(flag>0){
													//�и澯����
													alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
//				                				alarm = true;
//				                				if(status < flag)status = flag;
//				                				if(this.alarmlevel < flag)this.alarmlevel=flag;
//				                				SysLogger.info(alarmMsg.toString());
//				                				SysLogger.info("alarm:"+alarm+"   alarmlevel:"+alarmlevel);
												}else{
													//�ж�֮ǰ�Ƿ��и澯,�������͸澯�ָ���Ϣ
													Hashtable createeventdata = ShareData.getCreateEventdata();
													if (createeventdata.containsKey("host:ping:"+ipAddress)){
														//���͸澯�ָ���Ϣ,��д�澯�ָ���ʾ
														createeventdata.remove("host:ping:"+ipAddress);
													}               				
												}
											}
											msg.append(alarmIndicatorsNode.getDescr() + ":" + pingvalue + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
										}
									} catch (Exception e) {
										e.printStackTrace();
									}

				        		}else if(alarmIndicatorsNode.getName().equals("diskperc")){
				        			try {
										//������Ϣ
										if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
											if (diskVector != null && diskVector.size()>0){
												Hashtable alldiskalarmdata = new Hashtable();
												try{
													alldiskalarmdata = ShareData.getAlldiskalarmdata();
												}catch(Exception e){
													e.printStackTrace();
												}
												if (alldiskalarmdata == null )alldiskalarmdata = new Hashtable();
												for(int si=0;si<diskVector.size();si++){
													Diskcollectdata diskdata = null;
													diskdata = (Diskcollectdata)diskVector.elementAt(si);
													if(diskdata.getEntity().equalsIgnoreCase("Utilization")){
														//������
														flag = 0;
														if(getOstype() == 5 || getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
//															System.out.println(ipAddress+"===flag==="+id+":"+alarmIndicatorsNode.getType()+":"+"diskperc:diskperc:"+diskdata.getSubentity().substring(0, 3));
//															elist = checkeventdao.findCheckEvent(id+"",alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),alarmIndicatorsNode.getName(),diskdata.getSubentity().substring(0, 3));
															if(checkEventHash!=null&&checkEventHash.size()>0){
//																List<CheckEvent> elist = checkeventdao.findCheckEvent(id+"",alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),alarmIndicatorsNode.getName());
																if(checkEventHash.get(id+":"+alarmIndicatorsNode.getType()+":"+alarmIndicatorsNode.getSubtype()+":"+alarmIndicatorsNode.getName()+":"+diskdata.getSubentity().substring(0, 3))!=null){
																	flag = (Integer)checkEventHash.get(id+":"+alarmIndicatorsNode.getType()+":"+alarmIndicatorsNode.getSubtype()+":"+alarmIndicatorsNode.getName()+":"+diskdata.getSubentity().substring(0, 3));//elist.size();
																}
															}
														}else{
//															System.out.println(ipAddress+"===flag==="+id+":"+alarmIndicatorsNode.getType()+":"+"diskperc:diskperc:"+diskdata.getSubentity());
//															elist = checkeventdao.findCheckEvent(id+"",alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),alarmIndicatorsNode.getName(),diskdata.getSubentity());
															if(checkEventHash!=null&&checkEventHash.size()>0){
//																List<CheckEvent> elist = checkeventdao.findCheckEvent(id+"",alarmIndicatorsNode.getType(),alarmIndicatorsNode.getSubtype(),alarmIndicatorsNode.getName());
																if(checkEventHash.get(id+":"+alarmIndicatorsNode.getType()+":"+alarmIndicatorsNode.getSubtype()+":"+alarmIndicatorsNode.getName()+":"+diskdata.getSubentity())!=null){
																	flag = (Integer)checkEventHash.get(id+":"+alarmIndicatorsNode.getType()+":"+alarmIndicatorsNode.getSubtype()+":"+alarmIndicatorsNode.getName()+":"+diskdata.getSubentity());//elist.size();
																}
															}
														}
//														System.out.println(ipAddress+"===flag==="+flag);
										    			if(flag>0){
										    				//�и澯����
										    				Diskconfig diskconfig = null;
										    				if(getOstype() == 5 || getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
										    					diskconfig = (Diskconfig)alldiskalarmdata.get(ipAddress+":"+diskdata.getSubentity().substring(0, 3)+":"+"��������ֵ");
										    				}else{
										    					diskconfig = (Diskconfig)alldiskalarmdata.get(ipAddress+":"+diskdata.getSubentity()+":"+"��������ֵ");
										    				}
										    				
										    				int limevalue = 0;
										    				if(flag == 1){
										    					limevalue = diskconfig.getLimenvalue();
										    				}else if(flag == 2){
										    					limevalue = diskconfig.getLimenvalue1();
										    				}else
										    					limevalue = diskconfig.getLimenvalue2();
										    				
										    				if(getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
										    					//�и澯����
										        				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+" "+diskdata.getSubentity().substring(0, 3)+" ��ֵ:"+limevalue+"<br>");
										    				}else{
										    					//�и澯����
										    					alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+" "+diskdata.getSubentity()+" ��ֵ:"+limevalue+"<br>");
										    				}
										    			}
//				            	        			DiskconfigDao diskconfigDao = new DiskconfigDao();
//						    						Diskconfig vo = diskconfigDao.loadByIpNameBak(ipAddress, diskdata.getSubentity(), "��������ֵ");
//						    	        			if(getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
//						    	        				if(vo!=null&&vo.getIs_show()==1)msg.append(alarmIndicatorsNode.getDescr() + ":" +diskdata.getSubentity().substring(0, 3)+" "+ CEIString.round(new Double(diskdata.getThevalue()).doubleValue(),2) + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
//						    	        			} else {
//						    	        				//UNIX������
//						    	        				if(vo!=null&&vo.getIs_show()==1)msg.append(alarmIndicatorsNode.getDescr() + ":" +diskdata.getSubentity()+" "+ CEIString.round(new Double(diskdata.getThevalue()).doubleValue(),2) + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
//						    	        			}
										    			//SysLogger.info("ostype ==== "+getOstype());
										    			if(getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
										    				//WINDOWS������
										    				msg.append(alarmIndicatorsNode.getDescr() + ":" +diskdata.getSubentity().substring(0, 3)+" "+ CEIString.round(new Double(diskdata.getThevalue()).doubleValue(),2) + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
										    			}else{
										    				//UNIX������
										    				msg.append(alarmIndicatorsNode.getDescr() + ":" +diskdata.getSubentity()+" "+ CEIString.round(new Double(diskdata.getThevalue()).doubleValue(),2) + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
										    			}
										    			//msg.append(nm.getDescr() + ":" +diskdata.getSubentity()+" "+ CEIString.round(new Double(diskdata.getThevalue()).doubleValue(),2) + " "+nm.getUnit() + "<br>");
													} 
//													System.out.println(ipAddress+"======"+alarmIndicatorsNode.getName()+"==="+alarmMsg);
													if(diskdata.getEntity().equalsIgnoreCase("UtilizationInc")){
//				            						//���������� yangjun
//				            						int flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"diskinc:"+diskdata.getSubentity());
//				            				        //int flag = checkeventutil.checkEvent("disk",new Double(diskdata.getThevalue()),nm,diskdata.getSubentity(),"��������ֵ");
////				            				        System.out.println("flag===="+flag);
//				            				        if(flag>0){
//				            	        				//�и澯����
//				            	        				//�����¼�
//				            	        				Diskconfig diskconfig = null;
//				            	        				if(getOstype() ==4 || getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
//				            	        					diskconfig = (Diskconfig)alldiskalarmdata.get(ipAddress+":"+diskdata.getSubentity().substring(0, 3)+":"+"��������ֵ");
//				            	        				}else{
//				            	        					diskconfig = (Diskconfig)alldiskalarmdata.get(ipAddress+":"+diskdata.getSubentity()+":"+"��������ֵ");
//				            	        				}
//				            	        				
//				            	        				int limevalue = 0;
//				            	        				if(flag == 1){
//				            	        					limevalue = diskconfig.getLimenvalue();
//				            	        				}else if(flag == 2){
//				            	        					limevalue = diskconfig.getLimenvalue1();
//				            	        				}else
//				            	        					limevalue = diskconfig.getLimenvalue2();
//				            	        				
//				            	        				if(getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
//				            	        					//�и澯����
//				                	        				alarmMsg.append("Ӳ�������ʳ�����ֵ "+diskdata.getSubentity().substring(0, 3)+" ��ֵ:"+limevalue+"<br>");
//				                	        				alarm = true;
//				                	        				if(status < flag)status = flag;
//				            	        				}else{
//				            	        					//�и澯����
//				                	        				alarmMsg.append("Ӳ�������ʳ�����ֵ "+diskdata.getSubentity()+" ��ֵ:"+limevalue+"<br>");
//				                	        				alarm = true;
//				                	        				if(status < flag)status = flag;
//				            	        				}
//				            	        				if(this.alarmlevel < flag)this.alarmlevel=flag;
//				            	        			}
//				            	        			if(getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
//				            	        				//WINDOWS������
//				            	        				msg.append("Ӳ��������:" +diskdata.getSubentity().substring(0, 3)+" "+ CEIString.round(new Double(diskdata.getThevalue()).doubleValue(),2) + " "+nm.getThreshlod_unit() + "<br>");
//				            	        			}else{
//				            	        				//UNIX������
//				            	        				msg.append("Ӳ��������:" +diskdata.getSubentity()+" "+ CEIString.round(new Double(diskdata.getThevalue()).doubleValue(),2) + " "+nm.getThreshlod_unit() + "<br>");
//				            	        			}
													}
												}
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
				        		}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("physicalmemory")){
				        			try {
										//�����ڴ���Ϣ
										if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
											if (memoryVector != null && memoryVector.size()>0){
												for(int si=0;si<memoryVector.size();si++){
													Memorycollectdata memorydata = (Memorycollectdata)memoryVector.elementAt(si);
													if(memorydata.getEntity().equalsIgnoreCase("Utilization")&& memorydata.getSubentity().equalsIgnoreCase("PhysicalMemory")){
														//������
//														System.out.println(ipAddress+"===flag==="+flag);
										    			if(flag>0){
										    				//�и澯����
										    				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
										    			}       			
										    			msg.append(alarmIndicatorsNode.getDescr() + ":" + memorydata.getSubentity()+" "+CEIString.round(new Double(memorydata.getThevalue()).doubleValue(),2) + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
													}
												}
											}
//											System.out.println(ipAddress+"======"+alarmIndicatorsNode.getName()+"==="+alarmMsg);
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
				        		}else if(alarmIndicatorsNode.getName().equalsIgnoreCase("virtualmemory")){
				        			try {
										//�����ڴ���Ϣ
										if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
											if (memoryVector != null && memoryVector.size()>0){
												for(int si=0;si<memoryVector.size();si++){
													Memorycollectdata memorydata = (Memorycollectdata)memoryVector.elementAt(si);
													if(memorydata.getEntity().equalsIgnoreCase("Utilization")&& memorydata.getSubentity().equalsIgnoreCase("VirtualMemory")){
														//������
										    			if(flag>0){
										    				//�и澯����
										    				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
//				            	        				alarm = true;
//				            	        				if(status < flag)status = flag;
//				            	        				if(this.alarmlevel < flag)this.alarmlevel=flag;
										    			}       			
										    			msg.append(alarmIndicatorsNode.getDescr() + ":" + memorydata.getSubentity()+" "+CEIString.round(new Double(memorydata.getThevalue()).doubleValue(),2) + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
													}
												}
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
				        		} else if(alarmIndicatorsNode.getName().equalsIgnoreCase("swapmemory")){
									try {
										//�����ڴ���Ϣ
										if("1".equalsIgnoreCase(alarmIndicatorsNode.getEnabled())){
											if (memoryVector != null && memoryVector.size()>0){
												for(int si=0;si<memoryVector.size();si++){
													Memorycollectdata memorydata = (Memorycollectdata)memoryVector.elementAt(si);
													if(memorydata.getEntity().equalsIgnoreCase("Utilization")&&memorydata.getSubentity().equalsIgnoreCase("SwapMemory")){
														//������
//														int flag = 0;
//														flag = checkeventdao.findByName(id+":"+alarmIndicatorsNode.getType()+":"+alarmIndicatorsNode.getName());
										    			if(flag>0){
										    				//�и澯����
										    				alarmMsg.append(alarmIndicatorsNode.getAlarm_info()+"<br>");
//				            	        				alarm = true;
//				            	        				if(status < flag)status = flag;
//				            	        				if(this.alarmlevel < flag)this.alarmlevel=flag;
										    			}       			
										    			msg.append(alarmIndicatorsNode.getDescr() + ":" + memorydata.getSubentity()+" "+CEIString.round(new Double(memorydata.getThevalue()).doubleValue(),2) + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
													}
												}
											}
										}
									} catch (Exception e) {
										e.printStackTrace();
									}
								} else if(alarmIndicatorsNode.getName().equals("AllInBandwidthUtilHdx")){//�ӿ���Ϣ
									//�������
									if(inhdx != null)msg.append(alarmIndicatorsNode.getDescr() + ":" + inhdx + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
								} else if(alarmIndicatorsNode.getName().equals("AllOutBandwidthUtilHdx")){
									//��������
									if(outhdx != null)msg.append(alarmIndicatorsNode.getDescr() + ":" + outhdx + " "+alarmIndicatorsNode.getThreshlod_unit() + "<br>");
								} else if(alarmIndicatorsNode.getName().equals("process")){
								} else if(alarmIndicatorsNode.getName().equals("interface")){
								} else if(alarmIndicatorsNode.getName().equals("responsetime")){
								}
				        	}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
						}
					}
				
				
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
//				checkeventdao.close();
			}
			
		}
        //����ֻ���,��д�¼�
//        List proEventList = new ArrayList();
//        if(category == 4){
//        	Hashtable sharedata = ShareData.getSharedata();
//			Hashtable datahash = (Hashtable)sharedata.get(ipAddress);
//			Vector proVector = null;
//			if (datahash != null && datahash.size() > 0){
//				proVector = (Vector) datahash.get("process");
//			}   		
//    		/*
//    		 * nielin add 2010-08-18
//    		 *
//    		 * ����������澯
//    		 * 
//    		 * start ===============================
//    		 */
//    		try{
//    			if(proVector != null && proVector.size()>0)
//    				proEventList = createProcessGroupEventList(ipAddress , proVector);
//    		}catch(Exception e){
//    			
//    		}
//    		/*
//    		 * nielin add 2010-08-18
//    		 *
//    		 * ����������澯
//    		 * 
//    		 * end ===============================
//    		 */
//        }
//        if(proEventList != null && proEventList.size()>0){
//        	alarm = true;
//        }
//        
//        
//        
//        List hostServiceEventList = new ArrayList();
//        if(category == 4){       	
//        	Hashtable sharedata = ShareData.getSharedata();
//			Hashtable datahash = (Hashtable)sharedata.get(ipAddress);
//    		//service
//			Vector winserviceVector = null;
//			if (datahash != null && datahash.size() > 0){
//				winserviceVector = (Vector) datahash.get("winservice");
//			}
//    		
//    		/*
//    		 * nielin add 2010-08-18
//    		 *
//    		 * ����������澯
//    		 * 
//    		 * start ===============================
//    		 */
//    		try{
//    			if(winserviceVector != null && winserviceVector.size()>0)
//    			hostServiceEventList = createHostServiceGroupEventList(ipAddress , winserviceVector);
//    		}catch(Exception e){
//    			e.printStackTrace();
//    		}
//    		/*
//    		 * nielin add 2010-08-18
//    		 *
//    		 * ����������澯
//    		 * 
//    		 * end ===============================
//    		 */
//        }
//        if(hostServiceEventList != null && hostServiceEventList.size()>0){
//        	alarm = true;
//        } 
//        
//        
//        List jobForAS400EventList = new ArrayList();
//        if(category == 4){
//        	
//        	Hashtable sharedata = ShareData.getSharedata();
//			//if (datahash != null && datahash.size() > 0)
//			Hashtable datahash = (Hashtable)sharedata.get(ipAddress);
//        	//Hashtable allData = ShareData.getd();
//            //
//    		//service
//			List jobForAS400List = null;
//			if (datahash != null && datahash.size() > 0){
//				jobForAS400List = (List) datahash.get("Jobs");
//			}
//    		
//    		/*
//    		 * nielin add 2010-08-18
//    		 *
//    		 * ������ҵ��澯
//    		 * 
//    		 * start ===============================
//    		 */
//    		try{
//    			if(jobForAS400List != null && jobForAS400List.size()>0){
//    				jobForAS400EventList = createJobForAS400GroupEventList(ipAddress , jobForAS400List);
//    			}
//    		}catch(Exception e){
//    			e.printStackTrace();
//    		}
//    		/*
//    		 * nielin add 2010-08-18
//    		 *
//    		 * ������ҵ��澯
//    		 * 
//    		 * end ===============================
//    		 */
//        }
//        if(jobForAS400EventList != null && jobForAS400EventList.size()>0){
//        	alarm = true;
//        	//this.alarmlevel=3;
//        }
//        
        
        
        
//		if(alarmMsg.toString().length()>0)
//		{	
//		    msg.append("<font color='red'>--������Ϣ:--</font><br>");
//		    msg.append(alarmMsg.toString());
//		    if(proEventList != null && proEventList.size()>0){
//	        	for(int i=0;i<proEventList.size();i++){
//	        		EventList eventList = (EventList)proEventList.get(i);
//	        		msg.append(eventList.getContent()+"<br>");
//	        		if(eventList.getLevel1() > this.alarmlevel){
//		    			this.alarmlevel = eventList.getLevel1();
//		    		}
//	        	}
//	        }
//		    
//		    if(hostServiceEventList != null && hostServiceEventList.size() > 0){
//		    	for(int i = 0 ; i < hostServiceEventList.size() ; i++){
//		    		EventList eventList = (EventList)hostServiceEventList.get(i);
//		    		
//		    		msg.append(eventList.getContent() + "<br>");
//		    		if(eventList.getLevel1() > this.alarmlevel){
//		    			this.alarmlevel = eventList.getLevel1();
//		    		}
//		    	}
//		    	
//		    }
//		    
//		    if(jobForAS400EventList != null && jobForAS400EventList.size() > 0){
//		    	for(int i = 0 ; i < jobForAS400EventList.size() ; i++){
//		    		EventList eventList = (EventList)jobForAS400EventList.get(i);
//		    		
//		    		msg.append(eventList.getContent() + "<br>");
//		    		if(eventList.getLevel1() > this.alarmlevel){
//		    			this.alarmlevel = eventList.getLevel1();
//		    		}
//		    	}
//		    	
//		    }
//		    
//		    
//		    Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipAddress);
//		    host.setStatus(alarmlevel);
//		    //SysLogger.info(alarmMsg.toString());
//		    this.setAlarm(true);
//		}   
		msg.append("����ʱ��:" + lastTime);	
		//SysLogger.info("===="+msg.toString());
		setPrompt(msg.toString());
        return msg.toString();		
	}
	
	/**
	 * ˢ�½ڵ��״̬���澯״̬���澯�ȼ��� 
	 */
	public synchronized void refreshNodeState(){
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List monitorItemList = new ArrayList();//alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(id+"", "net", "");
    	try{
    		//��ȡ�����õ����б�����ָ��
    		if(category==4){
    			monitorItemList = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(id+"", "host", "");
    		}else{
    			monitorItemList = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(id+"", "net", "");
    		}   		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		double cpuvalue = 0;
		String pingvalue = null;
		String inhdx = "0";
		String outhdx = "0";
		String ifInUcastPkts = "0";
		String time = "";
		sysLocation = alias+"("+ipAddress+")";
		Vector ipPingData = null;
		Vector memoryVector = null;
		Vector cpuV = null;
		Vector diskVector = null; 
		Vector interfaceVector = new Vector();
		String runmodel = PollingEngine.getCollectwebflag(); 
		Hashtable ipAllData = null;
		if("0".equals(runmodel)){
			//�ɼ�������Ƿ���ģʽ 
			ipAllData = (Hashtable)ShareData.getSharedata().get(ipAddress);
			ipPingData = (Vector)ShareData.getPingdata().get(ipAddress);
		}else{
			//�ɼ�������Ǽ���ģʽ 
			ipAllData = (Hashtable)ShareData.getAllNetworkData().get(ipAddress);
			ipPingData = (Vector)ShareData.getAllNetworkPingData().get(ipAddress);
		}
		
		if(ipAllData != null){
			cpuV = (Vector)ipAllData.get("cpu");
			memoryVector = (Vector)ipAllData.get("memory");
			diskVector = (Vector)ipAllData.get("disk");
			Vector allutil = (Vector)ipAllData.get("allutilhdx");
			if(allutil != null && allutil.size()==3){
				AllUtilHdx inutilhdx = (AllUtilHdx)allutil.get(0);
				inhdx = inutilhdx.getThevalue();
				AllUtilHdx oututilhdx = (AllUtilHdx)allutil.get(1);
				outhdx = oututilhdx.getThevalue();
			}
			interfaceVector = (Vector)ipAllData.get("interface");
		}
		if(cpuV != null && cpuV.size()>0){
			CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
			if(cpu != null && cpu.getThevalue() != null){
				cpuvalue = new Double(cpu.getThevalue());
			}
		}
		if(ipPingData != null && ipPingData.size() > 0){
			Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
			pingvalue = pingdata.getThevalue();
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			time = sdf.format(cc);		
			lastTime = time;
		}
		
		alarm = false;
		status = 0;	
		
		//CheckEventUtil checkeventutil = new CheckEventUtil();			
		Hashtable checkEventHash = ShareData.getCheckEventHash();	
		if(checkEventHash == null)checkEventHash = new Hashtable();		
		try{
			for(int i=0;i<monitorItemList.size();i++){  
	        	AlarmIndicatorsNode nm = (AlarmIndicatorsNode)monitorItemList.get(i);
	        	if(nm.getType().equalsIgnoreCase("net")){
	        		//�����豸
	        		if(nm.getName().equals("cpu")){
	        			//CPU������
	        			//SysLogger.info("limenvalue0:"+nm.getLimenvalue0()+"   cpuvalue:"+cpuvalue);
	        			if("1".equalsIgnoreCase(nm.getEnabled())){
	        				//int flag = checkeventutil.checkEvent("cpu",cpuvalue,nm,"","");
	        				int flag = 0;
	        				if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"cpu")){
	        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"cpu");
	        				}
	        				//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"cpu");
	            			if(flag>0){
	            				//�и澯����
	            				alarm = true;
	            				if(status < flag)status = flag;
	            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	            			}  
	        			}	
	        		}else if(nm.getName().equals("ping")){
	        			//������ͨ��
	        			//SysLogger.info("### ��ʼ����"+nm.getName()+" PING���ݵ�XML��Ϣ####");
	        			if(pingvalue == null || pingvalue.trim().length()==0){
	        				//û�ж�PING���ݽ��вɼ�,����Ҫ�澯���
	        				pingvalue="0";
	        			}else{
	        				if("1".equalsIgnoreCase(nm.getEnabled())){
	        					int flag = 0;
	        					if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ping")){
		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ping");
		        				}
	        					//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"ping");
	                			if(flag>0){
	                				alarm = true;
	                				if(status < flag)status = flag;
	                				if(this.alarmlevel < flag)this.alarmlevel=flag;
	                			}
	            			}
	        			}
	        		}else if(nm.getName().equals("AllInBandwidthUtilHdx")){
	        			//�ӿ���Ϣ
	        			//�������
	    				if("1".equalsIgnoreCase(nm.getEnabled())){
	    					int flag = 0;
	    					if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"AllInBandwidthUtilHdx")){
	        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"AllInBandwidthUtilHdx");
	        				}
	    					//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"AllInBandwidthUtilHdx");
	        				if(flag>0){
	            				//�и澯����
	            				alarm = true;
	            				if(status < flag)status = flag;
	            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	            			}
	    				}
	        		}else if(nm.getName().equals("AllOutBandwidthUtilHdx")){
	        			//�ӿ���Ϣ
	        			//��������
	    				if("1".equalsIgnoreCase(nm.getEnabled())){
	    					int flag = 0;
	    					if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"AllOutBandwidthUtilHdx")){
	        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"AllOutBandwidthUtilHdx");
	        				}
	    					//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"AllOutBandwidthUtilHdx");
	        				if(flag>0){
	            				//�и澯����
	            				alarm = true;
	            				if(status < flag)status = flag;
	            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	            			}
	    				}
	        		}else if(nm.getName().equals("interface")){
	        			//�ӿ���Ϣ
	        			if (interfaceVector != null && interfaceVector.size()>0){
	        				//�ӿ�DOWN�澯
		        			if("1".equalsIgnoreCase(nm.getEnabled())){
//		    					List portconfiglist = new ArrayList();
//		    					
//		    								
//		    					Portconfig portconfig = null;			
//		    					Hashtable allportconfighash = ShareData.getPortConfigHash();;
//		    					if(allportconfighash != null && allportconfighash.size()>0){
//		    						if(allportconfighash.containsKey(ipAddress)){
//		    							portconfiglist = (List)allportconfighash.get(ipAddress);
//		    						}
//		    					}				
//		    					Hashtable portconfigHash = new Hashtable();
//		    					if(portconfiglist != null && portconfiglist.size()>0){
//		    						for(int k=0;k<portconfiglist.size();k++){
//		    							portconfig = (Portconfig)portconfiglist.get(k);
//		    							portconfigHash.put(portconfig.getPortindex()+"", portconfig);
//		    						}
//		    					}
//		    					portconfig = null;
		        				List portconfiglist = new ArrayList();			
		        				Portconfig portconfig = null;			
		        				Hashtable allportconfighash = ShareData.getPortConfigHash();;
		        				if(allportconfighash != null && allportconfighash.size()>0){
		        					if(allportconfighash.containsKey(ipAddress)){
		        						portconfiglist = (List)allportconfighash.get(ipAddress);
		        					}
		        				}				
		        				Hashtable portconfigHash = new Hashtable();
		        				if(portconfiglist != null && portconfiglist.size()>0){
		        					for(int k=0;k<portconfiglist.size();k++){
		        						portconfig = (Portconfig)portconfiglist.get(k);
		        						portconfigHash.put(portconfig.getPortindex()+"", portconfig);
		        					}
		        				}
		        				portconfig = null;
//		    					List portconfiglist = new ArrayList();
//		    					PortconfigDao configdao = new PortconfigDao(); 			
//		    					Portconfig portconfig = null;
//		    					Hashtable portconfigHash = new Hashtable();
//		    					try {
//		    						portconfiglist = configdao.getBySms(ipAddress);
//		    					} catch (RuntimeException e) {
//		    						e.printStackTrace();
//		    					} finally {
//		    						configdao.close();
//		    					}
//		    					if(portconfiglist != null && portconfiglist.size()>0){
//		    						for(int k=0;k<portconfiglist.size();k++){
//		    							portconfig = (Portconfig)portconfiglist.get(k);
//		    							portconfigHash.put(portconfig.getPortindex()+"", portconfig);
//		    						}
//		    					}
//		    					portconfig = null;
		    					
		    					
		    					for(int m=0;m<interfaceVector.size();m++){
		    						Interfacecollectdata interfacedata=(Interfacecollectdata)interfaceVector.get(m);
		    						if(interfacedata != null){
		    							if(interfacedata.getCategory().equalsIgnoreCase("Interface")&& interfacedata.getEntity().equalsIgnoreCase("ifOperStatus") && interfacedata.getSubentity() != null){
		    								if(portconfigHash.containsKey(interfacedata.getSubentity())){
		    									//���ڶ˿�����,���ж��Ƿ�DOWN
		    									portconfig = (Portconfig)portconfigHash.get(interfacedata.getSubentity());
		    									if (!"up".equalsIgnoreCase(interfacedata.getThevalue())){
		    										//�и澯����
		    			            				alarm = true;
		    			            				if(status < 3)status = 3;
		    			            				if(this.alarmlevel < 3)this.alarmlevel=3;
		    									}
		    								}
		    							}
		    						}
		    					}      			
		        			}
	        				
	        			}
	        			
	        			if(nm.getSubentity().equals("AllInBandwidthUtilHdx")){
	        			}else if(nm.getName().equals("AllOutBandwidthUtilHdx")){      				
	        			}else if(nm.getName().equals("ifInMulticastPkts")){
	        				//��ڵ���
	        				if("1".equalsIgnoreCase(nm.getEnabled())){
	        					Hashtable sharedata = ShareData.getSharedata();
	            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
	            			    if(ipdata == null)ipdata = new Hashtable();
	            			    Vector tempv = (Vector)ipdata.get("inpacks");
	            			    if (tempv != null && tempv.size()>0){
	        						for(int k=0;k<tempv.size();k++){
	        							InPkts inpacks=(InPkts)tempv.elementAt(k);
	        							if(inpacks.getEntity().equalsIgnoreCase("ifInMulticastPkts")){
	        								int flag = 0;
	        								if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ifInMulticastPkts")){
	        		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ifInMulticastPkts");
	        		        				}
	        								//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"ifInMulticastPkts");
	        		        				if(flag>0){
	        		            				//�и澯����
	        		            				alarm = true;
	        		            				if(status < flag)status = flag;
	        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	        		            			}
	        							}
	        							
	        						}
	            			    }
	        				}
	        				
	        			}else if(nm.getName().equals("ifInBroadcastPkts")){
	        				//��ڷǵ���
	        				//if(ifInUcastPkts != null )msg.append(nm.getDescr() + ":" + outhdx + " "+nm.getUnit() + "<br>");
	        				if("1".equalsIgnoreCase(nm.getEnabled())){
	        					Hashtable sharedata = ShareData.getSharedata();
	            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
	            			    if(ipdata == null)ipdata = new Hashtable();
	            			    Vector tempv = (Vector)ipdata.get("inpacks");
	            			    if (tempv != null && tempv.size()>0){
	        						for(int k=0;k<tempv.size();k++){
	        							InPkts inpacks=(InPkts)tempv.elementAt(k);
	        							if(inpacks.getEntity().equalsIgnoreCase("ifInBroadcastPkts")){
	        								int flag = 0;
	        								if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ifInBroadcastPkts")){
	        		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ifInBroadcastPkts");
	        		        				}
	        								//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"ifInBroadcastPkts");
	        		        				if(flag>0){
	        		            				//�и澯����
	        		            				alarm = true;
	        		            				if(status < flag)status = flag;
	        		            				//�����¼�
	        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	        		            			}
	        							}
	        							
	        						}
	            			    }
	        				}
	        			}else if(nm.getName().equals("ifOutMulticastPkts")){
	        				//���ڵ���
	        				//if(ifInUcastPkts != null )msg.append(nm.getDescr() + ":" + outhdx + " "+nm.getUnit() + "<br>");
	        				if("1".equalsIgnoreCase(nm.getEnabled())){
	        					Hashtable sharedata = ShareData.getSharedata();
	            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
	            			    if(ipdata == null)ipdata = new Hashtable();
	            			    Vector tempv = (Vector)ipdata.get("outpacks");
	            			    if (tempv != null && tempv.size()>0){
	        						for(int k=0;k<tempv.size();k++){
	        							OutPkts outpacks=(OutPkts)tempv.elementAt(k);
	        							if(outpacks.getEntity().equalsIgnoreCase("ifOutMulticastPkts")){
	        								//int flag = checkeventutil.checkEvent("ifOutMulticastPkts",Double.parseDouble(outpacks.getThevalue()),nm,"","");
	        								int flag = 0;
	        								if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ifOutMulticastPkts")){
	        		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ifOutMulticastPkts");
	        		        				}
	        								//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"ifOutMulticastPkts");
	        		        				if(flag>0){
	        		            				//�и澯����
	        		            				alarm = true;
	        		            				if(status < flag)status = flag;
	        		            				//�����¼�
	        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	        		            			}
	        							}
	        							
	        						}
	            			    }
	        				}
	        				
	        			}else if(nm.getName().equals("ifOutBroadcastPkts")){
	        				//���ڷǵ���
	        				if("1".equalsIgnoreCase(nm.getEnabled())){
	        					Hashtable sharedata = ShareData.getSharedata();
	            			    Hashtable ipdata = (Hashtable)sharedata.get(ipAddress);
	            			    if(ipdata == null)ipdata = new Hashtable();
	            			    Vector tempv = (Vector)ipdata.get("outpacks");
	            			    if (tempv != null && tempv.size()>0){
	        						for(int k=0;k<tempv.size();k++){
	        							OutPkts outpacks=(OutPkts)tempv.elementAt(k);
	        							if(outpacks.getEntity().equalsIgnoreCase("ifOutBroadcastPkts")){
	        								//int flag = checkeventutil.checkEvent("ifOutBroadcastPkts",Double.parseDouble(outpacks.getThevalue()),nm,"","");
	        								int flag = 0;
	        								if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ifOutBroadcastPkts")){
	        		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ifOutBroadcastPkts");
	        		        				}
	        								//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"ifOutBroadcastPkts");
	        		        				if(flag>0){
	        		            				//�и澯����
	        		            				alarm = true;
	        		            				if(status < flag)status = flag;
//	        		            				//�����¼�
	        		            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	        		            			}
	        							}
	        							
	        						}
	            			    }
	        				}
	        				
	        			}
	        		}
	        	}else if(nm.getType().equalsIgnoreCase("host")){
	        		//�����豸
	        		if(nm.getName().equals("cpu")){
	        			//CPU������
	        			if("1".equalsIgnoreCase(nm.getEnabled())){
	        				int flag = 0;
	        				if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"cpu")){
	        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"cpu");
	        				}
	        				//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"cpu");
	            			if(flag>0){
	            				//�и澯����
	            				alarm = true;
	            				if(status < flag)status = flag;
	            				if(this.alarmlevel < flag)this.alarmlevel=flag;
	            			}       			
	        			}
	        			
	        		}else if(nm.getName().equals("ping")){
	        			//��ͨ��
	        			if("1".equalsIgnoreCase(nm.getEnabled())){
	            			if(pingvalue == null || pingvalue.trim().length()==0){
	            				pingvalue="0";
	            			}else{
	            				int flag = 0;
	            				if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ping")){
		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ping");
		        				}
	            				//int flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"ping");
	                			if(flag>0){
	                				//�и澯����
	                				alarm = true;
	                				if(status < flag)status = flag;
	                				if(this.alarmlevel < flag)this.alarmlevel=flag;
	                			}
	            			}
	        			}

	        		}else if(nm.getName().equals("diskperc")){
	        			//������Ϣ
	        			if("1".equalsIgnoreCase(nm.getEnabled())){
	        				if (diskVector != null && diskVector.size()>0){
	        					//Hashtable hostdata = ShareData.getHostdata();
	            				Hashtable alldiskalarmdata = new Hashtable();
	            				try{
	            					alldiskalarmdata = ShareData.getAlldiskalarmdata();
	            				}catch(Exception e){
	            					e.printStackTrace();
	            				}
	            				if (alldiskalarmdata == null )alldiskalarmdata = new Hashtable();
	            				for(int si=0;si<diskVector.size();si++){
	            					Diskcollectdata diskdata = null;
	            					diskdata = (Diskcollectdata)diskVector.elementAt(si);
	            					if(diskdata.getEntity().equalsIgnoreCase("Utilization")){
	            						//������
	            						int flag = 0;
	            						if(getOstype() ==4 || getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
	            							if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"diskperc:diskperc:"+diskdata.getSubentity().substring(0, 3))){
	        		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"diskperc:diskperc:"+diskdata.getSubentity().substring(0, 3));
	        		        				}
	            							//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"diskperc:diskperc:"+diskdata.getSubentity().substring(0, 3));
	            						}else{
	            							if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ping")){
	            								if(checkEventHash.containsKey(id+":"+nm.getType()+":"+"ping")){
	            		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ping");
	            		        				}
	        		        					//flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+"ping");
	        		        				}
	            							//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+"diskperc:diskperc:"+diskdata.getSubentity());
	            						}
	            	        			if(flag>0){
	            	        				//�и澯����
	            	        				//�����¼�
	            	        				Diskconfig diskconfig = null;
	            	        				if(getOstype() ==4 || getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
	            	        					diskconfig = (Diskconfig)alldiskalarmdata.get(ipAddress+":"+diskdata.getSubentity().substring(0, 3)+":"+"��������ֵ");
	            	        				}else{
	            	        					diskconfig = (Diskconfig)alldiskalarmdata.get(ipAddress+":"+diskdata.getSubentity()+":"+"��������ֵ");
	            	        				}
	            	        				
//	            	        				int limevalue = 0;
//	            	        				if(flag == 1){
//	            	        					limevalue = diskconfig.getLimenvalue();
//	            	        				}else if(flag == 2){
//	            	        					limevalue = diskconfig.getLimenvalue1();
//	            	        				}else
//	            	        					if(diskconfig == null){
//	            	        						
//	            	        					}else{
//	            	        						limevalue = diskconfig.getLimenvalue2();
//	            	        					}
	            	        				if(getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
	            	        					//�и澯����
	                	        				alarm = true;
	                	        				if(status < flag)status = flag;
	            	        				}else{
	            	        					//�и澯����
//	                	        				alarmMsg.append(nm.getAlarmInfo()+" "+diskdata.getSubentity()+" ��ֵ:"+limevalue+"<br>");
	                	        				alarm = true;
	                	        				if(status < flag)status = flag;
	            	        				}
	            	        				if(this.alarmlevel < flag)this.alarmlevel=flag;
	            	        			}
	            					} 
	            					if(diskdata.getEntity().equalsIgnoreCase("UtilizationInc")){
	            					}
	            				}
	            			}
	        			}
	        			
	        		}else if(nm.getName().equals("physicalmemory")){
	        			//�����ڴ���Ϣ
	        			if("1".equalsIgnoreCase(nm.getEnabled())){
	            			if (memoryVector != null && memoryVector.size()>0){
	            				for(int si=0;si<memoryVector.size();si++){
	            					Memorycollectdata memorydata = (Memorycollectdata)memoryVector.elementAt(si);
	            					if(memorydata.getEntity().equalsIgnoreCase("Utilization")&& memorydata.getSubentity().equalsIgnoreCase("PhysicalMemory")){
	            						//������
	            						int flag = 0;
	            						if(checkEventHash.containsKey(id+":"+nm.getType()+":"+nm.getName())){
        		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+nm.getName());
        		        				}
	            						//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+nm.getName());
	            	        			if(flag>0){
	            	        				//�и澯����
	            	        				alarm = true;
	            	        				if(status < flag)status = flag;
	            	        				//�����¼�
	            	        				if(this.alarmlevel < flag)this.alarmlevel=flag;
	            	        			}       			
	            					}
	            				}
	            			}
	        			}
	        		}else if(nm.getName().equals("virtualmemory")){
	        			//�����ڴ���Ϣ
	        			if("1".equalsIgnoreCase(nm.getEnabled())){
	            			if (memoryVector != null && memoryVector.size()>0){
	            				for(int si=0;si<memoryVector.size();si++){
	            					Memorycollectdata memorydata = (Memorycollectdata)memoryVector.elementAt(si);
	            					if(memorydata.getEntity().equalsIgnoreCase("Utilization")&& memorydata.getSubentity().equalsIgnoreCase("VirtualMemory")){
	            						//������
	            						int flag = 0;
	            						if(checkEventHash.containsKey(id+":"+nm.getType()+":"+nm.getName())){
        		        					flag = (Integer)checkEventHash.get(id+":"+nm.getType()+":"+nm.getName());
        		        				}
	            						//flag = checkeventdao.findByName(id+":"+nm.getType()+":"+nm.getName());
	            	        			if(flag>0){
	            	        				//�и澯����
	            	        				alarm = true;
	            	        				if(status < flag)status = flag;
	            	        				//�����¼�
	            	        				if(this.alarmlevel < flag)this.alarmlevel=flag;
	            	        			}       			
	            					}
	            				}
	            			}
	        			}
	        		}
	        	}
	        }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//checkeventdao.close();
		}
        //����ֻ���,��д�¼�
        List proEventList = new ArrayList();
        if(category == 4){
        	Hashtable sharedata = ShareData.getSharedata();
			Hashtable datahash = (Hashtable)sharedata.get(ipAddress);
			Vector proVector = null;
			if (datahash != null && datahash.size() > 0){
				proVector = (Vector) datahash.get("process");
			}   		
    		try{
    			if(proVector != null && proVector.size()>0)
    				proEventList = createProcessGroupEventList(ipAddress , proVector);
    		}catch(Exception e){
    			
    		}
        }
        if(proEventList != null && proEventList.size()>0){
        	alarm = true;
        }
        List hostServiceEventList = new ArrayList();
        if(category == 4){       	
        	Hashtable sharedata = ShareData.getSharedata();
			Hashtable datahash = (Hashtable)sharedata.get(ipAddress);
    		//service
			Vector winserviceVector = null;
			if (datahash != null && datahash.size() > 0){
				winserviceVector = (Vector) datahash.get("winservice");
			}
    		try{
    			if(winserviceVector != null && winserviceVector.size()>0)
    			hostServiceEventList = createHostServiceGroupEventList(ipAddress , winserviceVector);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
        }
        if(hostServiceEventList != null && hostServiceEventList.size()>0){
        	alarm = true;
        } 
        List jobForAS400EventList = new ArrayList();
        if(category == 4){
        	Hashtable sharedata = ShareData.getSharedata();
			Hashtable datahash = (Hashtable)sharedata.get(ipAddress);
			List jobForAS400List = null;
			if (datahash != null && datahash.size() > 0){
				jobForAS400List = (List) datahash.get("Jobs");
			}
    		try{
    			if(jobForAS400List != null && jobForAS400List.size()>0){
    				jobForAS400EventList = createJobForAS400GroupEventList(ipAddress , jobForAS400List);
    			}
    		}catch(Exception e){
    			e.printStackTrace();
    		}
        }
        if(jobForAS400EventList != null && jobForAS400EventList.size()>0){
        	alarm = true;
        	//this.alarmlevel=3;
        }
	}
	
	public String getShowMessage()
	{
        return prompt;		
	}
	private int checkEvent(String subentity,double thevalue,NodeMonitor nm,String name,String bak){
		String diskname="";
		int level = 0;
		int returnflag = 0;
		try{
		Vector limenV = new Vector();
		
		if(subentity.equalsIgnoreCase("ping")){
			if(thevalue <= nm.getLimenvalue0()){
				if(thevalue <= nm.getLimenvalue1()){
					if(thevalue <= nm.getLimenvalue2()){
						level = 3;
					}else{
						level = 2;
					}
				}else{
					level = 1;
				}
			}else{
				level = 0;
			}
			//SysLogger.info(nm.getIp()+" ping level ===="+level);
		}else if(subentity.equalsIgnoreCase("disk")){
			//���ݲ��и澯��ֵ�����ж�
			//�жϸô����Ƿ���Ҫ�澯	
//			Hashtable hostdata = ShareData.getHostdata();
			Hashtable alldiskalarmdata = new Hashtable();
			try{
				alldiskalarmdata = ShareData.getAlldiskalarmdata();
			}catch(Exception e){
				e.printStackTrace();
			}
			if (alldiskalarmdata == null )alldiskalarmdata = new Hashtable();
			//�ж��Ƿ�ΪWINDOWS����
			int limenvalue = 0;
			int limenvalue1 = 0;
			int limenvalue2 = 0;
			int monflag = 0;
			int smsflag = 0;
			int smsflag1 = 0;
			int smsflag2 = 0;
			
			Host host = (Host)PollingEngine.getInstance().getNodeByIP(nm.getIp());
			if(host == null)return level;
			
			//SysLogger.info(host.getIpAddress()+"====Ostype:"+host.getOstype());
			
			if(host.getOstype()==4){
				//SysLogger.info(host.getIpAddress()+"--------"+name+"-----------"+name.substring(0, 3));
				//SysLogger.info(host.getIpAddress()+"====ip:name: "+nm.getIp()+":"+name.substring(0, 3));
				if(alldiskalarmdata.containsKey(nm.getIp()+":"+name.substring(0, 3)+":"+bak)){//yangjun
					//SysLogger.info(host.getIpAddress()+"====���� ip:name: "+nm.getIp()+":"+name.substring(0, 3));
					Diskconfig diskconfig = (Diskconfig)alldiskalarmdata.get(nm.getIp()+":"+name.substring(0, 3)+":"+bak);
					limenvalue = diskconfig.getLimenvalue();
					limenvalue1 = diskconfig.getLimenvalue1();
					limenvalue2 = diskconfig.getLimenvalue2();
					smsflag = diskconfig.getSms1();
					smsflag1 = diskconfig.getSms2();
					smsflag2 = diskconfig.getSms3();
					monflag = diskconfig.getMonflag();
					diskname=name.substring(0, 3);
				}
			}else if(getSysOid() != null && getSysOid().startsWith("1.3.6.1.4.1.311.1.1.3")){
				//WINDOWS������
				//SysLogger.info(host.getIpAddress()+"--------"+name+"-----------"+name.substring(0, 3));
				//SysLogger.info(host.getIpAddress()+"====ip:name: "+nm.getIp()+":"+name.substring(0, 3));
				if(alldiskalarmdata.containsKey(nm.getIp()+":"+name.substring(0, 3)+":"+bak)){//yangjun
					//SysLogger.info(host.getIpAddress()+"====���� ip:name: "+nm.getIp()+":"+name.substring(0, 3));
					Diskconfig diskconfig = (Diskconfig)alldiskalarmdata.get(nm.getIp()+":"+name.substring(0, 3)+":"+bak);
					limenvalue = diskconfig.getLimenvalue();
					limenvalue1 = diskconfig.getLimenvalue1();
					limenvalue2 = diskconfig.getLimenvalue2();
					smsflag = diskconfig.getSms1();
					smsflag1 = diskconfig.getSms2();
					smsflag2 = diskconfig.getSms3();
					monflag = diskconfig.getMonflag();
					diskname=name.substring(0, 3);
				}
			}else{
				//SysLogger.info(host.getIpAddress()+"====ip:name: "+nm.getIp()+":"+name);
				if(alldiskalarmdata.containsKey(nm.getIp()+":"+name+":"+bak)){//yangjun
					//SysLogger.info(host.getIpAddress()+"====���� ip:name: "+nm.getIp()+":"+name);
					Diskconfig diskconfig = (Diskconfig)alldiskalarmdata.get(nm.getIp()+":"+name+":"+bak);
					limenvalue = diskconfig.getLimenvalue();
					limenvalue1 = diskconfig.getLimenvalue1();
					limenvalue2 = diskconfig.getLimenvalue2();
					smsflag = diskconfig.getSms1();
					smsflag1 = diskconfig.getSms2();
					smsflag2 = diskconfig.getSms3();
					monflag = diskconfig.getMonflag();
					diskname=name;
				}
			}
			//SysLogger.info(host.getIpAddress()+"====���� ip:name: "+nm.getIp()+":"+diskname+"     "+" value:"+thevalue+" "+limenvalue+" "+limenvalue1+" "+limenvalue2+" monflag:"+monflag);
			if(monflag ==0){
				//����Ҫ�澯�ж�
				return level;
			}else{
//				System.out.println(nm.getIp()+":"+name+":"+bak+"======="+thevalue);
				//���и澯�����ж�
				if(thevalue >=limenvalue){
					//����һ��
					if(thevalue >=limenvalue1){
						//���ڶ���
						if(thevalue >=limenvalue2){
							//��������
							level = 3;
						}else{
							level = 2;
						}
					}else{
						level = 1;
					}
				}else{
					level =  0;
				}
			}
		
		}else{
			if(thevalue >=nm.getLimenvalue0()){
				//����һ��
				if(thevalue >=nm.getLimenvalue1()){
					//���ڶ���
					if(thevalue >=nm.getLimenvalue2()){
						//��������
						level = 3;
					}else{
						level = 2;
					}
				}else{
					level = 1;
				}
			}else{
				level =  0;
			}
		}
		//SysLogger.info(nm.getIp()+" "+subentity+" level ############## "+level);
		if(level == 0) return level;
		//SysLogger.info(nm.getIp()+" "+subentity+" level=="+level);
		if(this.alarmHash != null && this.alarmHash.size()>0){
			//�ж��Ƿ��Ѿ����и��¼�����
			Hashtable subentityhash = null;
			if(subentity.equalsIgnoreCase("disk")){
				subentityhash = (Hashtable)this.alarmHash.get(subentity+":"+diskname);
			}else{
				subentityhash = (Hashtable)this.alarmHash.get(subentity);
			}
				
			if(subentityhash != null && subentityhash.size()>0){
				//���и��¼�����,�ж��Ƿ���ͬ����澯
				//SysLogger.info(nm.getIp()+"�Ѿ��жϹ���"+subentity+"ֵ");
				if(subentityhash.get(level) != null){
					//ͬ�������,�жϳ�������
					//SysLogger.info(nm.getIp()+"######�Ѿ��жϹ���"+subentity+"ֵ");
					int times = (Integer)subentityhash.get(level);
					int limentimes = 0;
					if(level == 1){
						//��һ����
						limentimes = nm.getTime0();
					}else if (level == 2){
						//�ڶ�����
						limentimes = nm.getTime1();
					}else{
						//��������
						limentimes = nm.getTime2();
					}
					if(times >= limentimes){
						//�����¼�
						//flag = true;
						returnflag = level;
					}else{
						//�������¼�,����Ҫ�Ѹ澯����������1
						subentityhash = new Hashtable();
						subentityhash.put(level, times+1);
						//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+"===level:"+level);
						if(subentity.equalsIgnoreCase("disk")){
							this.alarmHash.put(subentity+":"+diskname, subentityhash);
							//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+":"+diskname+"===level:"+level+"==times:"+(times+1));
						}else{
							this.alarmHash.put(subentity, subentityhash);
							//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+"===level:"+level+"==times:"+(times+1));
						}
					}
					
				}else{
					//ͬ���𲻴���,���һ�β����ü���ĸ澯,����,ͬʱ������¼����͵���������澯
					//SysLogger.info(nm.getIp()+"ͬ���𲻴��ڸ�"+subentity+"ֵ");
					//ͬ�������,�жϳ�������
					int times = 1;
					int limentimes = 0;
					if(level == 1){
						//��һ����
						limentimes = nm.getTime0();
					}else if (level == 2){
						//�ڶ�����
						limentimes = nm.getTime1();
					}else{
						//��������
						limentimes = nm.getTime2();
					}
					if(times >= limentimes){
						//�����¼�
						//flag = true;
						returnflag = level;
					}
					//this.alarmHash = new Hashtable();
					//Hashtable subentityhash = new Hashtable();
					subentityhash.put(level, 1);
					if(subentity.equalsIgnoreCase("disk")){
						this.alarmHash.put(subentity+":"+diskname, subentityhash);
						//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+":"+diskname+"===level:"+level+"===times:"+1);
					}else{
						this.alarmHash.put(subentity, subentityhash);
						//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+"===level:"+level+"===times:"+1);
					}
				}
			}else{
				//��һ��
				//ͬ�������,�жϳ�������
				//SysLogger.info(nm.getIp()+"%%%%%%%%%%%��һ���жϸ�"+subentity+"ֵ");
				int times = 1;
				int limentimes = 0;
				if(level == 1){
					//��һ����
					limentimes = nm.getTime0();
				}else if (level == 2){
					//�ڶ�����
					limentimes = nm.getTime1();
				}else{
					//��������
					limentimes = nm.getTime2();
				}
				if(times >= limentimes){
					//�����¼�
					//flag = true;
					returnflag = level;
				}
				if(alarmHash == null)alarmHash = new Hashtable();
				//this.alarmHash = new Hashtable();
				subentityhash = new Hashtable();
				subentityhash.put(level, 1);
				if(subentity.equalsIgnoreCase("disk")){
					this.alarmHash.put(subentity+":"+diskname, subentityhash);
					//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+":"+diskname+"===level:"+level+"===times:"+1);
				}else{
					this.alarmHash.put(subentity, subentityhash);
					//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+"===level:"+level+"===times:"+1);
				}
			}
		}else{
			//��һ��
			//SysLogger.info(nm.getIp()+"��һ���жϸ�"+subentity+"ֵ");
			//ͬ�������,�жϳ�������
			int times = 1;
			int limentimes = 0;
			if(level == 1){
				//��һ����
				limentimes = nm.getTime0();
			}else if (level == 2){
				//�ڶ�����
				limentimes = nm.getTime1();
			}else{
				//��������
				limentimes = nm.getTime2();
			}
			if(times >= limentimes){
				//�����¼�
				//flag = true;
				returnflag = level;
			}
			if(alarmHash == null)alarmHash = new Hashtable();
			//this.alarmHash = new Hashtable();
			Hashtable subentityhash = new Hashtable();
			subentityhash.put(level, 1);
			if(subentity.equalsIgnoreCase("disk")){
				this.alarmHash.put(subentity+":"+diskname, subentityhash);
				//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+":"+diskname+"===level:"+level+"===times:"+1);
			}else{
				this.alarmHash.put(subentity, subentityhash);
				//SysLogger.info(nm.getIp()+" put====subentity:"+subentity+"===level:"+level+"===times:"+1);
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return returnflag;
		
	}
	
//	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,int recover){
//	 	//��������		 	
//	 	//���ڴ����õ�ǰ���IP��PING��ֵ
//	 	Calendar date=Calendar.getInstance();
//	 	Hashtable sendeddata = ShareData.getSendeddata();
//	 	Hashtable createeventdata = ShareData.getCreateEventdata();
//	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	 	try{
// 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress)){
// 				//�����ڣ��������ţ�������ӵ������б���
//	 			Smscontent smscontent = new Smscontent();
//	 			String time = sdf.format(date.getTime());
//	 			smscontent.setLevel(flag+"");
//	 			smscontent.setObjid(objid);
//	 			smscontent.setMessage(content);
//	 			smscontent.setRecordtime(time);
//	 			smscontent.setSubtype(subtype);
//	 			smscontent.setSubentity(subentity);
//	 			smscontent.setIp(ipaddress);
//	 			//���Ͷ���
//	 			SmscontentDao smsmanager=new SmscontentDao();
//	 			smsmanager.sendURLSmscontent(smscontent);	
//				sendeddata.put(subtype+":"+subentity+":"+ipaddress,date);	
//				if(recover == 1){
//					if(subentity.equalsIgnoreCase("ping"))
//						createeventdata.put(subtype+":"+subentity+":"+ipaddress, date);
//				}
//				
// 			}else{
// 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
// 				//yangjun
// 				SmsDao smsDao = new SmsDao();
// 				List list = new ArrayList();
// 				String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
// 				String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
// 				try {
// 					list = smsDao.findByEvent(content,startTime,endTime);
//				} catch (RuntimeException e) {
//					e.printStackTrace();
//				} finally {
//					smsDao.close();
//				}
//				if(list!=null&&list.size()>0){//�����б����Ѿ����͵���Ķ���
//					Calendar formerdate =(Calendar)sendeddata.get(subtype+":"+subentity+":"+ipaddress);		 				
//		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		 			Date last = null;
//		 			Date current = null;
//		 			Calendar sendcalen = formerdate;
//		 			Date cc = sendcalen.getTime();
//		 			String tempsenddate = formatter.format(cc);
//		 			
//		 			Calendar currentcalen = date;
//		 			cc = currentcalen.getTime();
//		 			last = formatter.parse(tempsenddate);
//		 			String currentsenddate = formatter.format(cc);
//		 			current = formatter.parse(currentsenddate);
//
//		 			long subvalue = current.getTime()-last.getTime();	
//		 			if(checkday == 1){
//		 				//����Ƿ������˵��췢������,1Ϊ���,0Ϊ�����
//		 				if (subvalue/(1000*60*60*24)>=1){
//			 				//����һ�죬���ٷ���Ϣ
//				 			Smscontent smscontent = new Smscontent();
//				 			String time = sdf.format(date.getTime());
//				 			smscontent.setLevel(flag+"");
//				 			smscontent.setObjid(objid);
//				 			smscontent.setMessage(content);
//				 			smscontent.setRecordtime(time);
//				 			smscontent.setSubtype(subtype);
//				 			smscontent.setSubentity(subentity);
//				 			smscontent.setIp(ipaddress);//���Ͷ���
//				 			SmscontentDao smsmanager=new SmscontentDao();
//				 			smsmanager.sendURLSmscontent(smscontent);
//							//�޸��Ѿ����͵Ķ��ż�¼	
//							sendeddata.put(subtype+":"+subentity+":"+ipaddress,date);
//							if(recover == 1){
//								if(subentity.equalsIgnoreCase("ping"))
//									createeventdata.put(subtype+":"+subentity+":"+ipaddress, date);
//							}
//				 		}
//		 			}else{
//		 				createEvent("poll",sysLocation,getBid(),content,flag,subtype,subentity,ipaddress,objid);
//		 				if(recover == 1){
//							if(subentity.equalsIgnoreCase("ping"))
//								createeventdata.put(subtype+":"+subentity+":"+ipaddress, date);
//						}
//		 				//createEvent("poll",sysLocation,getBid(),nm.getAlarmInfo()+"��ǰֵ:"+CEIString.round(new Double(memorydata.getThevalue()).doubleValue(),2)+" ��ֵ:"+nm.getLimenvalue0(),flag,"host","memory");
//		 			}
//				} else {
//                    //�����ڣ��������ţ�������ӵ������б���
//		 			Smscontent smscontent = new Smscontent();
//		 			String time = sdf.format(date.getTime());
//		 			smscontent.setLevel(flag+"");
//		 			smscontent.setObjid(objid);
//		 			smscontent.setMessage(content);
//		 			smscontent.setRecordtime(time);
//		 			smscontent.setSubtype(subtype);
//		 			smscontent.setSubentity(subentity);
//		 			smscontent.setIp(ipaddress);
//		 			//���Ͷ���
//		 			SmscontentDao smsmanager=new SmscontentDao();
//		 			smsmanager.sendURLSmscontent(smscontent);	
//					sendeddata.put(subtype+":"+subentity+":"+ipaddress,date);	
//					if(recover == 1){
//						if(subentity.equalsIgnoreCase("ping"))
//							createeventdata.put(subtype+":"+subentity+":"+ipaddress, date);
//					}
// 				}
//				//
// 			}	 			 			 			 			 	
//	 	}catch(Exception e){
//	 		e.printStackTrace();
//	 	}
//	 }
	
	/**
	 * nielin add
	 * @date 2010-08-18
	 * @param ip
	 * @param proVector
	 */
	private List createProcessGroupEventList(String ip , Vector proVector){
		List retList = new ArrayList();
		if(proVector == null || proVector.size()==0)return retList;
		try {
			Node hostNode = PollingEngine.getInstance().getNodeByIP(ip);
			
			ProcessGroupConfigurationUtil processGroupConfigurationUtil = new ProcessGroupConfigurationUtil();
			List list = processGroupConfigurationUtil.getProcessGroupByIpAndMonFlag(ip, "1");
			
			if(list == null || list.size() ==0){
				return null;
			}
			
			
			for(int i = 0; i < list.size() ; i ++){
				ProcessGroup processGroup = (ProcessGroup)list.get(i);
				List processGroupConfigurationList = processGroupConfigurationUtil.getProcessGroupConfigurationByGroupId(String .valueOf(processGroup.getId()));
				
				if(processGroupConfigurationList == null || processGroupConfigurationList.size() ==0){
					continue;
				}
				
				List wrongList = new ArrayList();
				
				for(int j = 0 ; j < processGroupConfigurationList.size() ; j++){
					int num = 0;
					ProcessGroupConfiguration processGroupConfiguration = (ProcessGroupConfiguration)processGroupConfigurationList.get(j);
					for(int k = 0  ; k < proVector.size() ; k ++){
						Processcollectdata processdata = (Processcollectdata) proVector.elementAt(k);
						if("Name".equals(processdata.getEntity())){
							if(processGroupConfiguration.getName().trim().equals(processdata.getThevalue().trim())){
								num++;
							}
						}
						
						
					}
					
					int times = Integer.parseInt(processGroupConfiguration.getTimes());
					
					String status = processGroupConfiguration.getStatus();
					
					if("1".equals(status)){
						if(num > times){
							// ����ĸ���
							num = num - times;
							
							List wrongProlist = new ArrayList();
							wrongProlist.add(processGroupConfiguration.getName());
							wrongProlist.add(num);
							wrongProlist.add(status);
							
							wrongList.add(wrongProlist);
							
						}
					}else{
						if(num < times){
							// ��ʧ�ĸ���
							num = times - num;
							
							List wrongProlist = new ArrayList();
							wrongProlist.add(processGroupConfiguration.getName());
							wrongProlist.add(num);
							wrongProlist.add(status);
							
							wrongList.add(wrongProlist);
							
						}
					}
					
					
					
				}
				
				if(wrongList.size() > 0){
					String message = ip + " ������Ϊ��" + processGroup.getName() + " ���ֽ����쳣!";
					for(int j = 0 ; j < wrongList.size() ; j ++){
						List wrongProList = (List)wrongList.get(j);
						String status = (String)wrongProList.get(2);
						if("1".equals(status)){
							message = message + "���̣�" + wrongProList.get(0) + "��������Ϊ��" + wrongProList.get(1) + ";";
						}else {
							message = message + "���̣�" + wrongProList.get(0) + "��ʧ����Ϊ��" + wrongProList.get(1) + ";";
						}
						
					
					}
					
					EventList eventList = new EventList();
					eventList.setEventtype("poll");
					eventList.setEventlocation(hostNode.getAlias() + "(" + ip + ")");
					eventList.setContent(message);
					eventList.setLevel1(Integer.valueOf(processGroup.getAlarm_level()));
					eventList.setManagesign(0);
					eventList.setRecordtime(Calendar.getInstance());
					eventList.setReportman("ϵͳ��ѯ");
					eventList.setNodeid(hostNode.getId());
					eventList.setBusinessid(hostNode.getBid());
					eventList.setSubtype("host");
					eventList.setSubentity("proc");
					
					retList.add(eventList);
					
//					hostNode = PollingEngine.getInstance().getNodeByID(hostNode.getId());
//					hostNode.setAlarm(true);
//					hostNode.setAlarmlevel(3);
//					hostNode.getAlarmMessage().add(message);
					
					/*
					 * ��Ҫ���ӷ��Ͷ��ŵĹ���
					 */
//					EventListDao eventListDao = new EventListDao();
//					try {
//						eventListDao.save(eventList);
//					} catch (RuntimeException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} finally{
//						eventListDao.close();
//					}
					
//					/*
//					 * ������ֻ���,�����澯,�澯�Ĺ����ڲɼ���������
//					 */
//					try{
//						createSMS(eventList.getSubtype(), eventList.getSubentity(),ip , hostNode.getId() + "", message , eventList.getLevel1() , 1 , processGroup.getName() , eventList.getBusinessid(),hostNode.getAlias() + "(" + ip + ")");
//					}catch(Exception e){
//						
//					}
					
				}
				
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retList;
	}
	
	
	
	public List createHostServiceGroupEventList(String ip , Vector hostServiceVector){
		List returnList = new ArrayList();
		if(hostServiceVector == null || hostServiceVector.size()==0)return returnList;
		
		try {
			
			Node hostNode = PollingEngine.getInstance().getNodeByIP(ip);
			
			HostServiceGroupConfigurationUtil hostServiceGroupConfigurationUtil = new HostServiceGroupConfigurationUtil();
			List list = hostServiceGroupConfigurationUtil.gethostservicegroupByIpAndMonFlag(ip, "1");
			
			if(list == null || list.size() == 0){
				return returnList;
			}
			
			for(int i = 0 ; i < list.size() ; i++){
				HostServiceGroup hostServiceGroup = (HostServiceGroup)list.get(i);
				List hostServiceList = hostServiceGroupConfigurationUtil.gethostservicegroupConfigurationByGroupId(String.valueOf(hostServiceGroup.getId()));
				
				if(hostServiceList == null || hostServiceList.size() ==0){
					continue;
				}
				
				List wrongList = new ArrayList();
				
				
				for(int j = 0 ; j < hostServiceList.size() ; j++){
					HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration) hostServiceList.get(j);
					
					boolean isLived = false;
					
					if(hostServiceVector != null){
						for(int k = 0 ; k < hostServiceVector.size() ; k++){
							Servicecollectdata servicedata = (Servicecollectdata)hostServiceVector.get(k);
							if(hostServiceGroupConfiguration.getName().trim().equals(servicedata.getName())){
								isLived = true;
								break;
							}
						}
					}
					
					if(!isLived){
						wrongList.add(hostServiceGroupConfiguration);
					}
				}
				
				if(wrongList.size() > 0){
					String message = ip + " ����������Ϊ��" + hostServiceGroup.getName() + " ������������ʧ!";
					for(int j = 0 ; j < wrongList.size() ; j ++){
						HostServiceGroupConfiguration hostServiceGroupConfiguration = (HostServiceGroupConfiguration)wrongList.get(j);
						message = message + "��������" + hostServiceGroupConfiguration.getName() + "��ʧ;";
					
					}
					EventList eventList = new EventList();
					eventList.setEventtype("poll");
					eventList.setEventlocation(hostNode.getAlias() + "(" + ip + ")");
					eventList.setContent(message);
					eventList.setLevel1(Integer.parseInt(hostServiceGroup.getAlarm_level()));
					eventList.setManagesign(0);
					eventList.setRecordtime(Calendar.getInstance());
					eventList.setReportman("ϵͳ��ѯ");
					eventList.setNodeid(hostNode.getId());
					eventList.setBusinessid(hostNode.getBid());
					eventList.setSubtype("host");
					eventList.setSubentity("hostservice");
					
//				hostNode = PollingEngine.getInstance().getNodeByID(hostNode.getId());
//				hostNode.setAlarm(true);
//				hostNode.setAlarmlevel(3);
//				hostNode.getAlarmMessage().add(message);
					
					/*
					 * ��Ҫ���ӷ��Ͷ��ŵĹ���
					 */
//				EventListDao eventListDao = new EventListDao();
//				try {
//					eventListDao.save(eventList);
//				} catch (RuntimeException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} finally{
//					eventListDao.close();
//				}
					
					//System.out.println(message+ "===================================");
					
					
					returnList.add(eventList);
					
//					/*
//					 * ����ֻ���,�澯�Ĺ�������صĲɼ����������
//					 */
//					try{
//						createSMS(eventList.getSubtype(), eventList.getSubentity(),ip , hostNode.getId() + "", message , eventList.getLevel1() , 1 , hostServiceGroup.getName() , eventList.getBusinessid(),hostNode.getAlias() + "(" + ip + ")");
//					}catch(Exception e){
//						
//					}
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnList;
	}
	
	
//	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,String sIndex,String bids,String sysLocation){
//	 	//��������		 	
//	 	//���ڴ����õ�ǰ���IP��PING��ֵ
//	 	Calendar date=Calendar.getInstance();
//	 	Hashtable sendeddata = ShareData.getSendeddata();
//	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	 	System.out.println("�˿��¼�--------------------");
//	 	try{
// 			if (!sendeddata.containsKey(subtype+":"+subentity+":"+ipaddress+":"+sIndex)) {
// 				//�����ڣ��������ţ�������ӵ������б���
//	 			Smscontent smscontent = new Smscontent();
//	 			String time = sdf.format(date.getTime());
//	 			smscontent.setLevel(flag+"");
//	 			smscontent.setObjid(objid);
//	 			smscontent.setMessage(content);
//	 			smscontent.setRecordtime(time);
//	 			smscontent.setSubtype(subtype);
//	 			smscontent.setSubentity(subentity);
//	 			smscontent.setIp(ipaddress);
//	 			//���Ͷ���
//	 			SmscontentDao smsmanager=new SmscontentDao();
//	 			smsmanager.sendURLSmscontent(smscontent);	
//				sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);	
//				
// 			} else {
// 				//���ڣ�����ѷ��Ͷ����б����ж��Ƿ��Ѿ����͵���Ķ���
// 				SmsDao smsDao = new SmsDao();
// 				List list = new ArrayList();
// 				String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
// 				String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
// 				try {
// 					list = smsDao.findByEvent(content,startTime,endTime);
//				} catch (RuntimeException e) {
//					e.printStackTrace();
//				} finally {
//					smsDao.close();
//				}
//				if(list!=null&&list.size()>0){//�����б����Ѿ����͵���Ķ���
//					Calendar formerdate =(Calendar)sendeddata.get(subtype+":"+subentity+":"+ipaddress+":"+sIndex);		 				
//		 			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//		 			Date last = null;
//		 			Date current = null;
//		 			Calendar sendcalen = formerdate;
//		 			Date cc = sendcalen.getTime();
//		 			String tempsenddate = formatter.format(cc);
//		 			
//		 			Calendar currentcalen = date;
//		 			Date ccc = currentcalen.getTime();
//		 			last = formatter.parse(tempsenddate);
//		 			String currentsenddate = formatter.format(ccc);
//		 			current = formatter.parse(currentsenddate);
//		 			
//		 			long subvalue = current.getTime()-last.getTime();	
//		 			if(checkday == 1){
//		 				//����Ƿ������˵��췢������,1Ϊ���,0Ϊ�����
//		 				if (subvalue/(1000*60*60*24)>=1){
//			 				//����һ�죬���ٷ���Ϣ
//				 			Smscontent smscontent = new Smscontent();
//				 			String time = sdf.format(date.getTime());
//				 			smscontent.setLevel(flag+"");
//				 			smscontent.setObjid(objid);
//				 			smscontent.setMessage(content);
//				 			smscontent.setRecordtime(time);
//				 			smscontent.setSubtype(subtype);
//				 			smscontent.setSubentity(subentity);
//				 			smscontent.setIp(ipaddress);//���Ͷ���
//				 			SmscontentDao smsmanager=new SmscontentDao();
//				 			smsmanager.sendURLSmscontent(smscontent);
//							//�޸��Ѿ����͵Ķ��ż�¼	
//							sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
//				 		} else {
//	                        //��ʼд�¼�
//			 	            //String sysLocation = "";
//			 				createEvent("poll",sysLocation,bids,content,flag,subtype,subentity,ipaddress,objid);
//				 		}
//		 			}
//				} else {
// 					Smscontent smscontent = new Smscontent();
// 		 			String time = sdf.format(date.getTime());
// 		 			smscontent.setLevel(flag+"");
// 		 			smscontent.setObjid(objid);
// 		 			smscontent.setMessage(content);
// 		 			smscontent.setRecordtime(time);
// 		 			smscontent.setSubtype(subtype);
// 		 			smscontent.setSubentity(subentity);
// 		 			smscontent.setIp(ipaddress);
// 		 			//���Ͷ���
// 		 			SmscontentDao smsmanager=new SmscontentDao();
// 		 			smsmanager.sendURLSmscontent(smscontent);	
// 					sendeddata.put(subtype+":"+subentity+":"+ipaddress+":"+sIndex,date);
// 				}
// 				
// 			}	 			 			 			 			 	
//	 	}catch(Exception e){
//	 		e.printStackTrace();
//	 	}
//	 }
	
	private void createEvent(String eventtype,String eventlocation,String bid,String content,int level1,String subtype,String subentity,String ipaddress,String objid){
		//�����¼�
//		SysLogger.info("##############��ʼ�����¼�############");
		EventList eventlist = new EventList();
		eventlist.setEventtype(eventtype);
		eventlist.setEventlocation(eventlocation);
		eventlist.setContent(content);
		eventlist.setLevel1(level1);
		eventlist.setManagesign(0);
		eventlist.setBak("");
		eventlist.setRecordtime(Calendar.getInstance());
		eventlist.setReportman("ϵͳ��ѯ");
		eventlist.setBusinessid(bid);
		eventlist.setNodeid(Integer.parseInt(objid));
		eventlist.setOid(0);
		eventlist.setSubtype(subtype);
		eventlist.setSubentity(subentity);
		EventListDao eventlistdao = new EventListDao();
		try{
			eventlistdao.save(eventlist);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			eventlistdao.close();
		}
	}
	
	public void setShowmessage(String showmessage) {
		this.showmessage = showmessage;
	}
	
	/**
	 * ����as400�澯
	 * @param ip
	 * @param hostServiceVector
	 * @return
	 */
	public List createJobForAS400GroupEventList(String ip , List jobForAS400list){
		List returnList = new ArrayList();
		if(jobForAS400list == null || jobForAS400list.size()==0)return returnList;
		
		try {
			
			Node hostNode = PollingEngine.getInstance().getNodeByIP(ip);
			JobForAS400GroupDetailUtil jobForAS400GroupDetailUtil = new JobForAS400GroupDetailUtil();
			List list = jobForAS400GroupDetailUtil.getJobForAS400GroupByIpAndMonFlag(ip, "1");
			
			if(list == null || list.size() == 0){
				return returnList;
			}
			
			for(int i = 0 ; i < list.size() ; i++){
				try {
					JobForAS400Group jobForAS400Group = (JobForAS400Group)list.get(i);
					List jobForAS400DetailList = jobForAS400GroupDetailUtil.getJobForAS400GroupDetailByGroupId(String.valueOf(jobForAS400Group.getId()));
					
					if(jobForAS400DetailList == null || jobForAS400DetailList.size() ==0){
						continue;
					}
					
					List wrongList = new ArrayList();
					
					
					for(int j = 0 ; j < jobForAS400DetailList.size() ; j++){
						JobForAS400GroupDetail jobForAS400GroupDetail = (JobForAS400GroupDetail) jobForAS400DetailList.get(j);
						
						boolean isLived = false;
						List jobForAS400List2 = new ArrayList();
						if(jobForAS400list != null){
							for(int k = 0 ; k < jobForAS400list.size() ; k++){
								JobForAS400 jobForAS400 = (JobForAS400)jobForAS400list.get(k);
								if(jobForAS400GroupDetail.getName().trim().equals(jobForAS400.getName())){
									jobForAS400List2.add(jobForAS400);
									isLived = true;
								}
							}
						}
						
						String eventMessage = "";
						
						Vector perVector = new Vector();
						if(jobForAS400GroupDetail.getStatus().equals("0") && isLived ){
							// ��� ��ҵ���� ���� ��ҵ�ļ��״̬Ϊ��������� ��澯
							perVector.add(jobForAS400GroupDetail);
							perVector.add("��ҵ��" + jobForAS400GroupDetail.getName() + " ���ֻ,�Ҹ���Ϊ��" + jobForAS400List2.size() + ";");
						} else if(jobForAS400GroupDetail.getStatus().equals("1") && !isLived ){
							// ��� ��ҵδ���� ���� ��ҵ�ļ��״̬Ϊ������� ��澯
							perVector.add(jobForAS400GroupDetail);
							perVector.add("��ҵ��" + jobForAS400GroupDetail.getName() + " δ�;");
						} else if(!jobForAS400GroupDetail.getStatus().equals("0") && isLived){
							// ��� ��ҵ���� ���� ��ҵ�ļ��״̬Ϊ������� ���һ���ж�
							if(!"-1".equals(jobForAS400GroupDetail.getActiveStatusType())){
								// ��� ��ҵ�Ļ�ļ��״̬���ǲ��� ������ж�
								
								try {
									int num = Integer.valueOf(jobForAS400GroupDetail.getNum());
									if(num > jobForAS400List2.size()){
										eventMessage = "��ҵ��" + jobForAS400GroupDetail.getName() + " �����쳣,�������ڼ����Ŀ,��ʧ��" + (num - jobForAS400List2.size()) + "��";
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								String activeStatus = jobForAS400GroupDetail.getActiveStatus();
								if(activeStatus!=null){
									for(int m = 0 ; m < jobForAS400List2.size() ; m++){
										JobForAS400 jobForAS400 = (JobForAS400)jobForAS400List2.get(m);
										// �ж�ÿһ�����ֵ���ҵ
										if("1".equals(jobForAS400GroupDetail.getActiveStatusType()) 
												!= (activeStatus.indexOf(jobForAS400.getActiveStatus()) != -1) ){
											// ��� ��ҵ�״̬����Ϊ������� �� �״̬�����ڵ�ǰ���״̬�� ��������Գ����쳣
											// ��� ��ҵ�״̬����Ϊ��������� �� �״̬���ܳ����ڵ�ǰ���״̬�� �� ������� �����쳣
											eventMessage = eventMessage + "��ҵ��" + jobForAS400GroupDetail.getName() + " �����쳣״̬Ϊ; ��״̬Ϊ��" + jobForAS400.getActiveStatus() + ";";
										}
									}
								}
								
								if(eventMessage.trim().length() > 1){
									perVector.add(jobForAS400GroupDetail);
									perVector.add(eventMessage);
								}
								
							}
						}
						if(perVector.size()>1){
							wrongList.add(perVector);
						}
					}
					
					if(wrongList.size() > 0){
						String message = ip + " ����ҵ�飺" + jobForAS400Group.getName() + " �����쳣!";
						for(int j = 0 ; j < wrongList.size() ; j ++){
							Vector perVector = (Vector)wrongList.get(j);
							JobForAS400GroupDetail jobForAS400GroupDetail = (JobForAS400GroupDetail)perVector.get(0);
							message = message + perVector.get(1);
						
						}
						EventList eventList = new EventList();
						eventList.setEventtype("poll");
						eventList.setEventlocation(hostNode.getAlias() + "(" + ip + ")" + " ��ҵ��Ϊ��" + jobForAS400Group.getName());
						eventList.setContent(message);
						eventList.setLevel1(Integer.parseInt(jobForAS400Group.getAlarm_level()));
						eventList.setManagesign(0);
						eventList.setRecordtime(Calendar.getInstance());
						eventList.setReportman("ϵͳ��ѯ");
						eventList.setNodeid(hostNode.getId());
						eventList.setBusinessid(hostNode.getBid());
						eventList.setSubtype("host");
						eventList.setSubentity("jobForAS400Gourp");
						
//				hostNode = PollingEngine.getInstance().getNodeByID(hostNode.getId());
//				hostNode.setAlarm(true);
//				hostNode.setAlarmlevel(3);
//				hostNode.getAlarmMessage().add(message);
						
						/*
						 * ��Ҫ���ӷ��Ͷ��ŵĹ���
						 */
//				EventListDao eventListDao = new EventListDao();
//				try {
//					eventListDao.save(eventList);
//				} catch (RuntimeException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} finally{
//					eventListDao.close();
//				}
						
						//System.out.println(message+ "===================================");
						
						
//						returnList.add(eventList);
//						try{
//							createSMS(eventList.getSubtype(), eventList.getSubentity(),ip , hostNode.getId() + "", message , eventList.getLevel1() , 1 , jobForAS400Group.getName() , eventList.getBusinessid(),hostNode.getAlias() + "(" + ip + ")" + " ��ҵ��Ϊ��" + jobForAS400Group.getName());
//						}catch(Exception e){
//							
//						}
					}
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnList;
	}

	public int getSecuritylevel() {
		return securitylevel;
	}

	public void setSecuritylevel(int securitylevel) {
		this.securitylevel = securitylevel;
	}

	public String getSecurityName() {
		return securityName;
	}

	public void setSecurityName(String securityName) {
		this.securityName = securityName;
	}

	public int getV3_ap() {
		return v3_ap;
	}

	public void setV3_ap(int v3_ap) {
		this.v3_ap = v3_ap;
	}

	public String getAuthpassphrase() {
		return authpassphrase;
	}

	public void setAuthpassphrase(String authpassphrase) {
		this.authpassphrase = authpassphrase;
	}

	public int getV3_privacy() {
		return v3_privacy;
	}

	public void setV3_privacy(int v3_privacy) {
		this.v3_privacy = v3_privacy;
	}

	public String getPrivacyPassphrase() {
		return privacyPassphrase;
	}

	public void setPrivacyPassphrase(String privacyPassphrase) {
		this.privacyPassphrase = privacyPassphrase;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
}

