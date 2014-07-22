package com.afunms.polling.snmp.fibrechannel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SnmpUtils;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.monitor.executor.base.SnmpMonitor;
import com.afunms.monitor.item.base.MonitoredItem;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.api.I_HostLastCollectData;
import com.afunms.polling.base.Node;
import com.afunms.polling.impl.HostLastCollectDataManager;
import com.afunms.polling.node.Host;
import com.afunms.polling.om.AllUtilHdx;
import com.afunms.polling.om.Channelcollectdata;
import com.afunms.polling.om.InPkts;
import com.afunms.polling.om.OutPkts;
import com.afunms.polling.snmp.SnmpMibConstants;
import com.afunms.topology.model.HostNode;

public class IbmCapabilitySnmp extends SnmpMonitor {
	
    private static Hashtable ifEntity_ifStatus = null;
	static {
		ifEntity_ifStatus = new Hashtable();
		ifEntity_ifStatus.put("1", "online");
		ifEntity_ifStatus.put("2", "offline");
		ifEntity_ifStatus.put("3", "testing");
		ifEntity_ifStatus.put("4", "linkFailure");
	};
	
	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 
	 */
	public IbmCapabilitySnmp() {
	}

	   public void collectData(Node node,MonitoredItem item){
		   
	   }
	   public void collectData(HostNode node){
		   
	   }
	/* (non-Javadoc)
	 * @����ͨ��������Ϣ#collectData()
	 */
	public Hashtable collect_Data(NodeGatherIndicators alarmIndicatorsNode) {
			Hashtable returnHash=new Hashtable();
			Hashtable channelHash=new Hashtable();
			Vector inframesVector = new Vector();
			Vector outframesVector = new Vector();
			Vector inOctetsVector = new Vector();
			Vector discardsVector = new Vector();
			Vector outOctetsVector = new Vector();
			Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(alarmIndicatorsNode.getNodeid()));
		
			try {
				Channelcollectdata channeldata=null;
				InPkts inpacks = new InPkts();
				OutPkts outpacks = new OutPkts();
				AllUtilHdx allutilhdx = new AllUtilHdx();
				Calendar date=Calendar.getInstance();
			
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					com.afunms.polling.base.Node snmpnode = (com.afunms.polling.base.Node)PollingEngine.getInstance().getNodeByIP(host.getIpAddress());
					Date cc = date.getTime();
					String time = sdf.format(cc);
					snmpnode.setLastTime(time);
				}catch(Exception e){
				  
				}
			  //-------------------------------------------------------------------------------------------interface start			
			  try{
				I_HostLastCollectData lastCollectDataManager=new HostLastCollectDataManager();
				Hashtable hash=ShareData.getOctetsdata(host.getIpAddress());
				if (hash==null)hash=new Hashtable();
						  String[] oids =                
							  new String[] {               
								  "1.3.6.1.2.1.75.1.1.5.1.2" ,
								  "1.3.6.1.2.1.75.1.2.2.1.1" ,
								  "1.3.6.1.2.1.75.1.2.2.1.2" ,
								  "1.3.6.1.2.1.75.1.4.3.1.1" ,
								  "1.3.6.1.2.1.75.1.4.3.1.2" ,
								  "1.3.6.1.2.1.75.1.4.3.1.3" ,
								  "1.3.6.1.2.1.75.1.4.3.1.4" ,
								  "1.3.6.1.2.1.75.1.4.3.1.5"			
								  };
				final String[] desc=SnmpMibConstants.NetWorkMibCapabilityDesc;
				final String[] chname=SnmpMibConstants.NetWorkMibCapabilityChname;
				final String[] unit=SnmpMibConstants.NetWorkMibCapabilityUnit0;
				final int[] scale=SnmpMibConstants.NetWorkMibCapabilityScale0;
				String[][] valueArray = null;   	  
				try {
					//valueArray = snmp.getTableData(host.getIpAddress(),host.getCommunity(),oids);
					valueArray = SnmpUtils.getTableData(host.getIpAddress(),host.getCommunity(),oids,host.getSnmpversion(),
		   		  			host.getSecuritylevel(),host.getSecurityName(),host.getV3_ap(),host.getAuthpassphrase(),host.getV3_privacy(),host.getPrivacyPassphrase(),3,1000*30);
				} catch(Exception e){
				}
				long allSpeed=0;
				long allOutOctetsSpeed=0;
				long allInOctetsSpeed=0;
				long allOctetsSpeed=0;
				
				long allinpacks=0;
				long indiscards=0;
				long inerrors=0;
				long alloutpacks=0;
				long outdiscards=0;
				long outerrors=0;
			
				Vector tempV=new Vector();
				Hashtable tempHash = new Hashtable();
				if(valueArray != null && valueArray.length > 0){
					for(int i=0;i<valueArray.length;i++)
			   	    {				   		  
			   		    if(valueArray[i][0] == null)continue;
							String sportName=valueArray[i][0].toString();				
							tempV.add(sportName);
							tempHash.put(i, sportName);
							Vector channelVector=new Vector();
							for(int j=0;j<8;j++){
									String sValue=valueArray[i][j];	
									channeldata=new Channelcollectdata();
									channeldata.setIpaddress(host.getIpAddress());
									channeldata.setCollecttime(date);
									channeldata.setCategory("channel");
									channeldata.setEntity(desc[j]);
									channeldata.setSubentity(sportName);
									//�˿�״̬�����棬ֻ��Ϊ��̬���ݷŵ���ʱ����
									if(j==0||j==1||j==2)
										channeldata.setRestype("static");
									else {
										channeldata.setRestype("dynamic");
									} 
									channeldata.setUnit(unit[j]);
									if((j==0 || j==1 || j==2)&&sValue!=null){//Ԥ��״̬�͵�ǰ״̬
			
										if (ifEntity_ifStatus.get(sValue) != null){
											channeldata.setThevalue(ifEntity_ifStatus.get(sValue).toString());
	                                       
										}else{
											channeldata.setThevalue(sValue);
										}
	//									if(j == 7){
											//ͨ���澯�ж�yangjun
	//										PortconfigDao configdao = new PortconfigDao(); 			
	//			 							Portconfig portconfig = null;
	//										try {
	//											portconfig = configdao.getByipandindex(host.getIpAddress(),sIndex);
	//										} catch (RuntimeException e) {
	//											e.printStackTrace();
	//										} finally {
	//											configdao.close();
	//										}
	//			 							try {
	//											if (portconfig != null){
	//												if (portconfig.getSms().intValue()==1){
	//													//SysLogger.info("sValue-----------------"+sValue);
	//													if(ifEntity_ifStatus.get(sValue) != null){
	//														if (!"up".equalsIgnoreCase(ifEntity_ifStatus.get(sValue).toString())){
	//															createSMS("network","interface",host.getIpAddress(),host.getId()+"",host.getIpAddress()+" �˿� "+portconfig.getName()+" down",2,1,sIndex,host.getBid());
	//														}
	//													}
	//													
	//												}
	//											}
	//										} catch (Exception e) {
	//											e.printStackTrace();
	//										}
	//									}
									} else {
										if(sValue != null){
											channeldata.setThevalue(Long.toString(Long.parseLong(sValue)/scale[j]));
										}else{
											channeldata.setThevalue("0");
										}
									}
									channeldata.setChname(chname[j]);
									channelVector.addElement(channeldata);
							   } //end for j
							channelHash.put(i, channelVector);
			   	           } //end for valueArray
				}
//				if(valueArray1!= null){
//				for(int i=0;i<valueArray1.length;i++){
//					allinpacks=0;
//					inupacks=0;//��ڵ����
//					innupacks=0;//�ǵ���
//					indiscards=0;
//					inerrors=0;
//					alloutpacks=0;
//					outupacks=0;//���ڵ���
//					outnupacks=0;//�ǵ���
//					outdiscards=0;
//					outerrors=0;																		
//
//					String sIndex = (String)tempHash.get(i);				
//					if (tempV.contains(sIndex)){
//										
//						for(int j=0;j<10;j++){																														
//							if(valueArray1[i][j]!=null){
//								String sValue=valueArray1[i][j];
//								//if (j==6)continue;
//								interfacedata=new Interfacecollectdata();
//								if(scale1[j]==0){
//									interfacedata.setThevalue(sValue);
//								}
//								else{
//									interfacedata.setThevalue(Long.toString(Long.parseLong(sValue)/scale1[j]));
//								}
//								if (j==1 || j==2){
//									//��ڵ��������ݰ�,��ڷǵ��������ݰ�												
//									if (sValue != null){
//										allinpacks=allinpacks+Long.parseLong(sValue);
//										
//										Calendar cal=(Calendar)hash.get("collecttime");
//										long timeInMillis=0;
//										if(cal!=null)timeInMillis=cal.getTimeInMillis();
//										long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
//										
//										inpacks=new InPkts();
//										inpacks.setIpaddress(host.getIpAddress());
//										inpacks.setCollecttime(date);
//										inpacks.setCategory("Interface");
//										String chnameBand="";
//										if(j==1){
//											inpacks.setEntity("ifInUcastPkts");
//											chnameBand="����";
//										}
//										if(j==2){
//											inpacks.setEntity("ifInNUcastPkts");
//											chnameBand="�ǵ���";
//										}
//										inpacks.setSubentity(sIndex);
//										inpacks.setRestype("dynamic");
//										inpacks.setUnit("��");	
//										inpacks.setChname(chnameBand);
//										long currentPacks=Long.parseLong(sValue);												
//										long lastPacks=0;	
//										long l=0;											
//												
//										//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
//										if(longinterval<2*interval){
//											String lastvalue="";
//											
//											if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
//											//ȡ���ϴλ�õ�Octets
//											if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
//										}
//							
//										if(longinterval!=0){
//											if(currentPacks<lastPacks){
//												currentPacks=currentPacks+4294967296L;
//											}
//											//������-ǰ����
//											//SysLogger.info(host.getIpAddress()+"==="+sIndex+"�Ͽ�==="+currentPacks+"===="+lastPacks);
//											long octetsBetween=currentPacks-lastPacks;
//											l=octetsBetween;
//											if(lastPacks == 0)l=0;
//										}
//										inpacks.setThevalue(Long.toString(l));	
//										//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
//										if (cal != null)
//											inpacksVector.addElement(inpacks);	
//									}
//										//continue;
//								}
//								if (j==3){
//									//��ڶ��������ݰ�
//									if (sValue != null) indiscards=Long.parseLong(sValue);
//										continue;
//								}
//								if (j==4){
//									//��ڴ�������ݰ�
//									if (sValue != null) inerrors=Long.parseLong(sValue);
//									continue;
//								}	
//								if (j==6 || j==7){
//									//��ڵ��������ݰ�,��ڷǵ��������ݰ�
//									if (sValue != null){
//										alloutpacks=alloutpacks+Long.parseLong(sValue);
//										
//										Calendar cal=(Calendar)hash.get("collecttime");
//										long timeInMillis=0;
//										if(cal!=null)timeInMillis=cal.getTimeInMillis();
//										long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;
//										
//										outpacks=new OutPkts();
//										outpacks.setIpaddress(host.getIpAddress());
//										outpacks.setCollecttime(date);
//										outpacks.setCategory("Interface");
//										String chnameBand="";
//										
//										if(j==6){
//											outpacks.setEntity("ifOutUcastPkts");
//											chnameBand="����";
//										}
//										if(j==7){
//											outpacks.setEntity("ifOutNUcastPkts");
//											chnameBand="�ǵ���";
//										}
//										outpacks.setSubentity(sIndex);
//										outpacks.setRestype("dynamic");
//										outpacks.setUnit("��");	
//										outpacks.setChname(chnameBand);
//										long currentPacks=Long.parseLong(sValue);												
//										long lastPacks=0;	
//										long l=0;											
//												
//										//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
//										if(longinterval<2*interval){
//											String lastvalue="";
//											
//											if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
//											//ȡ���ϴλ�õ�Octets
//											if(lastvalue!=null && !lastvalue.equals(""))lastPacks=Long.parseLong(lastvalue);
//										}
//							
//										if(longinterval!=0){
//											if(currentPacks<lastPacks){
//												currentPacks=currentPacks+4294967296L;
//											}
//											//������-ǰ����	
//											long octetsBetween=currentPacks-lastPacks;
//											l=octetsBetween;
//											if(lastPacks == 0)l=0;
//										}
//										outpacks.setThevalue(Long.toString(l));	
//										//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
//										if (cal != null)
//											outpacksVector.addElement(outpacks);
//										
//									}
//									//continue;
//								}
//								if (j==8){
//									//��ڶ��������ݰ�
//									if (sValue != null) outdiscards=Long.parseLong(sValue);
//									continue;
//								}
//								if (j==9){
//									//��ڴ�������ݰ�
//									if (sValue != null) outerrors=Long.parseLong(sValue);
//									continue;
//								}		
//								Calendar cal=(Calendar)hash.get("collecttime");
//								long timeInMillis=0;
//								if(cal!=null)timeInMillis=cal.getTimeInMillis();
//								long longinterval=(date.getTimeInMillis()-timeInMillis)/1000;											
//
//								//����ÿ���˿����ټ�������
//								if(j==0 || j==5){
//									utilhdx=new UtilHdx();
//									utilhdx.setIpaddress(host.getIpAddress());
//									utilhdx.setCollecttime(date);
//									utilhdx.setCategory("Interface");
//									String chnameBand="";
//									if(j==0){
//										utilhdx.setEntity("InBandwidthUtilHdx");
//										chnameBand="���";
//									}
//									if(j==5){
//										utilhdx.setEntity("OutBandwidthUtilHdx");
//										chnameBand="����";
//										}
//									utilhdx.setSubentity(sIndex);
//									utilhdx.setRestype("dynamic");
//									utilhdx.setUnit(unit1[j]+"/��");	
//									utilhdx.setChname(sIndex+"�˿�"+"����");
//									long currentOctets=Long.parseLong(sValue)/scale1[j];
//									long lastOctets=0;	
//									long l=0;											
//											
//									//�����ǰ�ɼ�ʱ�����ϴβɼ�ʱ��Ĳ�С�ڲɼ�����������������������ʣ��������������Ϊ0��
//									if(longinterval<2*interval){
//										String lastvalue="";
//										
//										if(hash.get(desc1[j]+":"+sIndex)!=null)lastvalue=hash.get(desc1[j]+":"+sIndex).toString();
//										//SysLogger.info(desc1[j]+":"+sIndex+"===="+lastvalue);
//										//ȡ���ϴλ�õ�Octets
//										if(lastvalue!=null && !lastvalue.equals(""))lastOctets=Long.parseLong(lastvalue);
//									}   
//						
//									if(longinterval!=0){
//										long octetsBetween = 0;  
//										if(currentOctets<lastOctets){
//											currentOctets=currentOctets+4294967296L/scale1[j];
//										} 
//										//������-ǰ����	
//										octetsBetween=currentOctets-lastOctets;
//										//SysLogger.info(sIndex+"===currentOctets:"+currentOctets+"===lastOctets:"+lastOctets+"===octetsBetween:"+octetsBetween);
//										//����˿�����
//										l=octetsBetween/longinterval;
//										//ͳ���ܳ����ֽ�������,���ü��㣨�����롢�ۺϣ�����������
////										if(j==0 && lastOctets!=0)allInOctetsSpeed=allInOctetsSpeed+l;
////										if(j==5 && lastOctets!=0)allOutOctetsSpeed=allOutOctetsSpeed+l;
////										//SysLogger.info("allInOctetsSpeed:"+allInOctetsSpeed+"===allOutOctetsSpeed:"+allOutOctetsSpeed);
////										if(lastOctets!=0)allOctetsSpeed=allOctetsSpeed+l;
//										//yangjun
//										if(j==0)allInOctetsSpeed=allInOctetsSpeed+l;
//										if(j==5)allOutOctetsSpeed=allOutOctetsSpeed+l;
//										allOctetsSpeed=allOctetsSpeed+l;
//										
//									}
//									DecimalFormat df=new DecimalFormat("#.##");//yangjun 
//									utilhdx.setThevalue(df.format(l*8));	
//									//SysLogger.info(host.getIpAddress()+" ��"+utilhdx.getSubentity()+"�˿� "+"Speed "+Long.toString(l*8));
//									if (cal != null)
//									utilhdxVector.addElement(utilhdx);												
//									
//									utilhdxperc=new UtilHdxPerc();
//									utilhdxperc.setIpaddress(host.getIpAddress());
//									utilhdxperc.setCollecttime(date);
//									utilhdxperc.setCategory("Interface");
//									if(j==0)utilhdxperc.setEntity("InBandwidthUtilHdxPerc");
//									if(j==5)utilhdxperc.setEntity("OutBandwidthUtilHdxPerc");
//									utilhdxperc.setSubentity(sIndex);
//									utilhdxperc.setRestype("dynamic");
//									utilhdxperc.setUnit("%");	
//									utilhdxperc.setChname(sIndex+"�˿�"+chnameBand+"����������");												
//									double speed=0.0;
//									if (hashSpeed.get(sIndex) != null){
//									speed = Double.parseDouble(hashSpeed.get(sIndex).toString());
//									}else{
//										speed = Double.parseDouble("0.0");
//									}
//									double d=0.0;
//									if(speed>0){
//										//���������ʣ����١�8*100/ifspeed%
//										d=Arith.div(l*800,speed);
//										//d=l*800/speed;
//									}
//									utilhdxperc.setThevalue(Double.toString(d));
//									if (cal != null)
//									utilhdxpercVector.addElement(utilhdxperc);	
//									
//												
//												
//								} //end j=0 j=5
//								//SysLogger.info(host.getIpAddress()+"==="+desc1[j]+":"+sIndex+"===="+sValue);
//								octetsHash.put(desc1[j]+":"+sIndex,interfacedata.getThevalue());
//							} //valueArray1[i][j]==null
//						} //end for j
//						//Hashtable packhash=lastCollectDataManager.getLastPacks(nethost);
//						Hashtable packhash=ShareData.getPacksdata(host.getIpAddress()+":"+sIndex);
//						//Hashtable discardshash=lastCollectDataManager.getLastDiscards(nethost);
//						Hashtable discardshash=ShareData.getDiscardsdata(host.getIpAddress()+":"+sIndex);
//						//Hashtable errorshash=lastCollectDataManager.getLastErrors(nethost);
//						Hashtable errorshash=ShareData.getErrorsdata(host.getIpAddress()+":"+sIndex);									
//						//���㴫������ݰ�
//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllInCastPkts");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(allinpacks+"");
//						interfacedata.setChname("��������ݰ�");
//						/******************/
//						Hashtable hasht = new Hashtable();									
//						hasht.put("AllInCastPkts"+":"+sIndex,allinpacks+"");																		
//
//
//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllOutCastPkts");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(alloutpacks+"");
//						interfacedata.setChname("���������ݰ�");
//						/******************/									
//						hasht.put("AllOutCastPkts"+":"+sIndex,alloutpacks+"");
//						
//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllInDiscards");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(indiscards+"");
//						interfacedata.setChname("����ܶ�����");
//						Hashtable tempDiscards = new Hashtable();
//						tempDiscards.put("AllInDiscards"+":"+sIndex,indiscards+"");
//
//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllOutDiscards");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(outdiscards+"");
//						interfacedata.setChname("�����ܶ�����");
//						tempDiscards.put("AllOutDiscards"+":"+sIndex,outdiscards+"");
//						
//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllInErrors");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(inerrors+"");
//						interfacedata.setChname("��ڴ������");
//						Hashtable errHash = new Hashtable();
//						errHash.put("AllInErrors"+":"+sIndex,inerrors+"");
//						//interfaceVector.addElement(interfacedata);
//
//						interfacedata=new Interfacecollectdata();
//						interfacedata.setIpaddress(host.getIpAddress());
//						interfacedata.setCollecttime(date);
//						interfacedata.setCategory("Interface");
//						interfacedata.setEntity("AllOutErrors");
//						interfacedata.setSubentity(sIndex);									
//						interfacedata.setRestype("static");
//						interfacedata.setUnit("��");
//						interfacedata.setThevalue(outerrors+"");
//						interfacedata.setChname("���ڴ������");
//						errHash.put("AllOutErrors"+":"+sIndex,outerrors+"");
//						//interfaceVector.addElement(interfacedata);
//
//						String lastvalue="";
//						long lastpacks=0;
//						//��ڴ������ݰ�
//						if (packhash != null){
//							if(packhash.get("AllInCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllInCastPkts"+":"+sIndex).toString();
//						}
//						
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){										
//							lastpacks=Long.parseLong(lastvalue);									
//						}
//									
//						Packs packs = new Packs();
//						packs.setIpaddress(host.getIpAddress());
//						packs.setCollecttime(date);
//						packs.setCategory("Interface");
//						packs.setEntity("InCastPkts");
//						packs.setSubentity(sIndex);
//						packs.setRestype("dynamic");
//						packs.setUnit("��");
//						packs.setChname("������ݰ�");
//						if (lastpacks > 0){
//							packs.setThevalue(allinpacks-lastpacks+"");
//						}else{
//							packs.setThevalue("0");
//						}									
//						packsVector.add(packs);
//						
//						//��ڶ�����
//						lastvalue="";
//						long lastdiscards=0;
//						if (discardshash != null){
//							if(discardshash.get("AllInDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllInDiscards"+":"+sIndex).toString();
//						}
//						
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lastdiscards=Long.parseLong(lastvalue);									
//						}
//									
//						DiscardsPerc discardsPerc = new DiscardsPerc();
//						discardsPerc.setIpaddress(host.getIpAddress());
//						discardsPerc.setCollecttime(date);
//						discardsPerc.setCategory("Interface");
//						discardsPerc.setEntity("InDiscardsPerc");
//						discardsPerc.setSubentity(sIndex);
//						discardsPerc.setRestype("dynamic");
//						discardsPerc.setUnit("%");
//						discardsPerc.setChname("��ڶ�����");
//						double indiscardserc=0.0;
//						if (allinpacks ==0){
//							indiscardserc=0;
//						}else{
//							if (allinpacks-lastpacks > 0){
//								indiscardserc = (indiscards-lastdiscards)/(allinpacks-lastpacks);
//							}else{
//								indiscardserc = 0;
//							}
//							
//						}									
//						discardsPerc.setThevalue(Double.toString(indiscardserc));
//						discardspercVector.add(discardsPerc);
//									
//						//��ڴ�����
//						lastvalue="";
//						long lasterrors=0;
//						if (errorshash != null){
//							if(errorshash.get("AllInErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllInErrors"+":"+sIndex).toString();
//						}									
//						//ȡ���ϴλ�õ�error
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lasterrors=Long.parseLong(lastvalue);									
//						}																		
//						ErrorsPerc errorsPerc = new  ErrorsPerc();
//						errorsPerc.setIpaddress(host.getIpAddress());
//						errorsPerc.setCollecttime(date);
//						errorsPerc.setCategory("Interface");
//						errorsPerc.setEntity("InErrorsPerc");
//						errorsPerc.setSubentity(sIndex);
//						errorsPerc.setRestype("dynamic");
//						errorsPerc.setUnit("%");
//						errorsPerc.setChname("��ڴ�����");
//						double inerrorsperc=0.0;
//						if (allinpacks==0){
//							inerrorsperc=0;
//						}else{
//							if (allinpacks-lastpacks > 0){
//								inerrorsperc=(inerrors-lasterrors)/(allinpacks-lastpacks);
//							}else{
//								inerrorsperc=0;
//							}
//							
//						}									
//						errorsPerc.setThevalue(Double.toString(inerrorsperc));
//						errorspercVector.add(errorsPerc);
//									
//						lastvalue="";
//						lastpacks=0;
//						lastdiscards=0;
//						lasterrors=0;
//						//���ڴ������ݰ�
//						if (packhash != null){
//							if(packhash.get("AllOutCastPkts"+":"+sIndex)!=null)lastvalue=packhash.get("AllOutCastPkts"+":"+sIndex).toString();
//						}
//						
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lastpacks=Long.parseLong(lastvalue);																		
//						}
//						packs = new Packs();
//						packs.setIpaddress(host.getIpAddress());
//						packs.setCollecttime(date);
//						packs.setCategory("Interface");
//						packs.setEntity("OutCastPkts");
//						packs.setSubentity(sIndex);
//						packs.setRestype("dynamic");
//						packs.setUnit("��");
//						packs.setChname("�������ݰ�");
//						if (lastpacks>0){
//							packs.setThevalue(alloutpacks-lastpacks+"");
//						}else{
//							packs.setThevalue("0");
//						}
//						
//						packsVector.add(packs);
//									
//									
//						//���㶪���ʺʹ�����
//						if (discardshash != null){
//							if(discardshash.get("AllOutDiscards"+":"+sIndex)!=null)lastvalue=discardshash.get("AllOutDiscards"+":"+sIndex).toString();
//						}									
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lastdiscards=Long.parseLong(lastvalue);									
//						}																		
//						discardsPerc = new DiscardsPerc();
//						discardsPerc.setIpaddress(host.getIpAddress());
//						discardsPerc.setCollecttime(date);
//						discardsPerc.setCategory("Interface");
//						discardsPerc.setEntity("OutDiscardsPerc");
//						discardsPerc.setSubentity(sIndex);
//						discardsPerc.setRestype("dynamic");
//						discardsPerc.setUnit("%");
//						discardsPerc.setChname("���ڶ�����");
//						double outdiscardserc=0.0;
//						if (alloutpacks==0){
//							outdiscardserc = 0;
//						}else{
//							if (alloutpacks-lastpacks>0){
//								outdiscardserc = (outdiscards-lastdiscards)/(alloutpacks-lastpacks);
//							}else{
//								outdiscardserc = 0;
//							}
//							
//						}
//									
//						discardsPerc.setThevalue(Double.toString(outdiscardserc));
//						discardspercVector.add(discardsPerc);
//						
//						lastvalue="";
//						if (errorshash != null){
//							if(errorshash.get("AllOutErrors"+":"+sIndex)!=null)lastvalue=errorshash.get("AllOutErrors"+":"+sIndex).toString();
//						}									
//						//ȡ���ϴλ�õ�packs
//						if(lastvalue!=null && !lastvalue.equals("")){
//							lasterrors=Long.parseLong(lastvalue);									
//						}																											
//						errorsPerc = new  ErrorsPerc();
//						errorsPerc.setIpaddress(host.getIpAddress());
//						errorsPerc.setCollecttime(date);
//						errorsPerc.setCategory("Interface");
//						errorsPerc.setEntity("OutErrorsPerc");
//						errorsPerc.setSubentity(sIndex);
//						errorsPerc.setRestype("dynamic");
//						errorsPerc.setUnit("%");
//						errorsPerc.setChname("���ڴ�����");
//						double outerrorsperc=0.0;
//						if (alloutpacks>0){
//							if ((alloutpacks-lastpacks)>0){
//								outerrorsperc=(outerrors-lasterrors)/(alloutpacks-lastpacks);
//							}else{
//								outerrorsperc=0;
//							}
//							
//						}									
//						errorsPerc.setThevalue(Double.toString(outerrorsperc));
//						errorspercVector.add(errorsPerc);									
//									
//						/* ��ӵ��ڴ���*/
//						ShareData.setPacksdata(host.getIpAddress()+":"+sIndex,hasht);
//						ShareData.setDiscardsdata(host.getIpAddress()+":"+sIndex,tempDiscards);
//						ShareData.setErrorsdata(host.getIpAddress()+":"+sIndex,errHash);
//									
//						/*
//						//��ǰ�˿��ۺ϶�����
//						AllDiscardsPerc alldiscardsperc = new AllDiscardsPerc();
//						alldiscardsperc.setIpaddress(nethost);
//						alldiscardsperc.setCollecttime(date);
//						alldiscardsperc.setCategory("Interface");
//						alldiscardsperc.setEntity("AllDiscardsPerc");
//						alldiscardsperc.setSubentity(sIndex);
//						alldiscardsperc.setRestype("dynamic");
//						alldiscardsperc.setUnit("%");
//						alldiscardsperc.setChname("�ۺ϶�����");
//						double alldiscards_perc=0.0;
//						if ((allinpacks+alloutpacks)>0)
//							alldiscards_perc=(indiscards+outdiscards)/(allinpacks+alloutpacks);
//						alldiscardsperc.setThevalue(Double.toString(alldiscards_perc));
//						alldiscardspercVector.add(alldiscardsperc);									
//						*/
//						/*
//						//��ǰ�˿��ۺϴ�����
//						AllErrorsPerc allerrorsperc = new AllErrorsPerc();
//						allerrorsperc.setIpaddress(nethost);
//						allerrorsperc.setCollecttime(date);
//						allerrorsperc.setCategory("Interface");
//						allerrorsperc.setEntity("AllErrorsPerc");
//						allerrorsperc.setSubentity(sIndex);
//						allerrorsperc.setRestype("dynamic");
//						allerrorsperc.setUnit("%");
//						allerrorsperc.setChname("�ۺϴ�����");
//						double allerrors_perc=0.0;
//						if ((allinpacks+alloutpacks)>0)
//							allerrors_perc=(inerrors+outerrors)/(allinpacks+alloutpacks);
//						allerrorsperc.setThevalue(Double.toString(allerrors_perc));
//						allerrorspercVector.add(allerrorsperc);
//						*/
//									
//					} //end for contains
//							
//				}
//				}
//				allutilhdx = new AllUtilHdx();
//				allutilhdx.setIpaddress(host.getIpAddress());
//				allutilhdx.setCollecttime(date);
//				allutilhdx.setCategory("Interface");
//				allutilhdx.setEntity("AllInBandwidthUtilHdx");
//				allutilhdx.setSubentity("AllInBandwidthUtilHdx");
//				allutilhdx.setRestype("dynamic");
//				allutilhdx.setUnit(unit1[0]+"/��");	
//				allutilhdx.setChname("�������");
//				
//				allutilhdx.setThevalue(Long.toString(allInOctetsSpeed*8));	
//				allutilhdxVector.addElement(allutilhdx);	
				
				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllInBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllInBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("��ڴ���������");
				double lInUsagePerc=0;
				if(allSpeed>0){
					lInUsagePerc=Arith.div(allInOctetsSpeed*800.0,allSpeed);
					//lInUsagePerc=allInOctetsSpeed*800.0/allSpeed;
					if(lInUsagePerc>95)lInUsagePerc=95;
				}
				allutilhdxperc.setThevalue(Double.toString(lInUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);	
				*/
				//out
				
//				allutilhdx = new AllUtilHdx();
//				allutilhdx.setIpaddress(host.getIpAddress());
//				allutilhdx.setCollecttime(date);
//				allutilhdx.setCategory("Interface");
//				allutilhdx.setEntity("AllOutBandwidthUtilHdx");
//				allutilhdx.setSubentity("AllOutBandwidthUtilHdx");
//				allutilhdx.setRestype("dynamic");
//				allutilhdx.setUnit(unit1[0]+"/��");
//				allutilhdx.setChname("��������");	
//				allutilhdx.setThevalue(Long.toString(allOutOctetsSpeed*8));	
//				allutilhdxVector.addElement(allutilhdx);	
				
				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllOutBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllOutBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("���ڴ���������");
				double lOutUsagePerc=0;
				if(allSpeed>0){
					lOutUsagePerc=Arith.div(allOutOctetsSpeed*800.0,allSpeed);
					//lOutUsagePerc=allOutOctetsSpeed*800.0/allSpeed;
					if(lOutUsagePerc>96)lOutUsagePerc=96;
				}
				allutilhdxperc.setThevalue(Double.toString(lOutUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);
				*/	
				
//				allutilhdx = new AllUtilHdx();
//				allutilhdx.setIpaddress(host.getIpAddress());
//				allutilhdx.setCollecttime(date);
//				allutilhdx.setCategory("Interface");
//				allutilhdx.setEntity("AllBandwidthUtilHdx");
//				allutilhdx.setSubentity("AllBandwidthUtilHdx");
//				allutilhdx.setRestype("dynamic");
//				allutilhdx.setUnit(unit1[0]+"/��");	
//				allutilhdx.setChname("�ۺ�����");
//				allutilhdx.setThevalue(Long.toString(allOctetsSpeed));	
//				allutilhdxVector.addElement(allutilhdx);	

				/*
				allutilhdxperc = new AllUtilHdxPerc();
				allutilhdxperc.setIpaddress(nethost);
				allutilhdxperc.setCollecttime(date);
				allutilhdxperc.setCategory("Interface");
				allutilhdxperc.setEntity("AllBandwidthUtilHdx");
				allutilhdxperc.setSubentity("AllBandwidthUtilHdxPerc");
				allutilhdxperc.setRestype("static");
				allutilhdxperc.setUnit("%");	
				allutilhdxperc.setChname("�ۺϴ���������");
				double lAllUsagePerc=0;
				if(allSpeed>0){
					lAllUsagePerc=allOctetsSpeed*800.0/allSpeed;
					if(lAllUsagePerc>196)lAllUsagePerc=196;
				 }
				allutilhdxperc.setThevalue(Double.toString(lAllUsagePerc));	
				allutilhdxpercVector.addElement(allutilhdxperc);	
				*/
//				String flag ="0";
//				//hash=null;
//				octetsHash.put("collecttime",date);	
//				if (hash != null){					
//					flag = (String)hash.get("flag");
//					if (flag == null){
//						flag="0";
//					}else{
//						if (flag.equals("0")){
//							flag = "1";
//						}else{
//							flag = "0";
//						}
//					}
//				}				
//				octetsHash.put("flag",flag);
//				ShareData.setOctetsdata(host.getIpAddress(),octetsHash);				
			}catch(Exception e){e.printStackTrace();}
//			  -------------------------------------------------------------------------------------------interface end
			}catch(Exception e){
				//returnHash=null;
				//e.printStackTrace();
				//return null;
			}finally{
//				System.gc();
			}
		
//		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
//		if(ipAllData == null)ipAllData = new Hashtable();
//		if (channelHash != null && channelHash.size()>0)ipAllData.put("channel",channelHash);		
//		if (inframesVector != null && inframesVector.size()>0)ipAllData.put("allinframes",discardsVector);
//		if (outframesVector != null && outframesVector.size()>0)ipAllData.put("alloutframes",outframesVector);
//		if (inOctetsVector != null && inOctetsVector.size()>0)ipAllData.put("inOctets",inOctetsVector);
//		if (outOctetsVector != null && outOctetsVector.size()>0)ipAllData.put("outOctets",outOctetsVector);		
//		if (discardsVector != null && discardsVector.size()>0)ipAllData.put("discards",discardsVector);
//	    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
	    
	    
	    
	    
	    if(!(ShareData.getSharedata().containsKey(host.getIpAddress())))
		{
			Hashtable ipAllData = new Hashtable();
			if(ipAllData == null)ipAllData = new Hashtable();
			
			
			if(ipAllData == null)ipAllData = new Hashtable();
			if (channelHash != null && channelHash.size()>0)ipAllData.put("channel",channelHash);		
			if (inframesVector != null && inframesVector.size()>0)ipAllData.put("allinframes",discardsVector);
			if (outframesVector != null && outframesVector.size()>0)ipAllData.put("alloutframes",outframesVector);
			if (inOctetsVector != null && inOctetsVector.size()>0)ipAllData.put("inOctets",inOctetsVector);
			if (outOctetsVector != null && outOctetsVector.size()>0)ipAllData.put("outOctets",outOctetsVector);		
			if (discardsVector != null && discardsVector.size()>0)ipAllData.put("discards",discardsVector);
		    ShareData.getSharedata().put(host.getIpAddress(), ipAllData);
		}else
		 {
			
			if (channelHash != null && channelHash.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("channel",channelHash);		
			if (inframesVector != null && inframesVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("allinframes",discardsVector);
			if (outframesVector != null && outframesVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("alloutframes",outframesVector);
			if (inOctetsVector != null && inOctetsVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("inOctets",inOctetsVector);
			if (outOctetsVector != null && outOctetsVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("outOctets",outOctetsVector);		
			if (discardsVector != null && discardsVector.size()>0)((Hashtable)ShareData.getSharedata().get(host.getIpAddress())).put("discards",discardsVector);
		   
		 }
	    
	    
	    
	    
	    
	    
	    returnHash.put("channel",channelHash);		
	    returnHash.put("allinframes",discardsVector);
	    returnHash.put("alloutframes",outframesVector);
	    returnHash.put("inOctets",inOctetsVector);
	    returnHash.put("outOctets",outOctetsVector);		
	    returnHash.put("discards",discardsVector);
	    return returnHash;
	}
	
	public int getInterval(float d,String t){
		int interval=0;
		  if(t.equals("d"))
			 interval =(int) d*24*60*60; //����
		  else if(t.equals("h"))
			 interval =(int) d*60*60;    //Сʱ
		  else if(t.equals("m"))
			 interval = (int)d*60;       //����
		else if(t.equals("s"))
					 interval =(int) d;       //��
		return interval;
	}
	
//	public void createSMS(String subtype,String subentity,String ipaddress,String objid,String content,int flag,int checkday,String sIndex,String bids){
//	 	//��������		 	
//	 	//���ڴ����õ�ǰ���IP��PING��ֵ
//	 	Calendar date=Calendar.getInstance();
//	 	Hashtable sendeddata = ShareData.getSendeddata();
//	 	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	 	//System.out.println("�˿��¼�--------------------");
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
//			 	            String sysLocation = "";
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
//	
//	private void createEvent(String eventtype,String eventlocation,String bid,String content,int level1,String subtype,String subentity,String ipaddress,String objid){
//		//�����¼�
//		SysLogger.info("##############��ʼ�����¼�############");
//		EventList eventlist = new EventList();
//		eventlist.setEventtype(eventtype);
//		eventlist.setEventlocation(eventlocation);
//		eventlist.setContent(content);
//		eventlist.setLevel1(level1);
//		eventlist.setManagesign(0);
//		eventlist.setBak("");
//		eventlist.setRecordtime(Calendar.getInstance());
//		eventlist.setReportman("ϵͳ��ѯ");
//		eventlist.setBusinessid(bid);
//		eventlist.setNodeid(Integer.parseInt(objid));
//		eventlist.setOid(0);
//		eventlist.setSubtype(subtype);
//		eventlist.setSubentity(subentity);
//		EventListDao eventlistdao = new EventListDao();
//		try{
//			eventlistdao.save(eventlist);
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			eventlistdao.close();
//		}
//	}
}
   