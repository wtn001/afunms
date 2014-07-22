/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.TracertsDao;
import com.afunms.application.dao.TracertsDetailDao;
import com.afunms.application.dao.Urlmonitor_historyDao;
import com.afunms.application.dao.Urlmonitor_realtimeDao;
import com.afunms.application.dao.WebConfigDao;
import com.afunms.application.model.Tracerts;
import com.afunms.application.model.TracertsDetail;
import com.afunms.application.model.Urlmonitor_history;
import com.afunms.application.model.Urlmonitor_realtime;
import com.afunms.application.model.WebConfig;
import com.afunms.application.util.UrlDataCollector;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Web;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.toolService.traceroute.TraceRouteExecute;
import com.gatherdb.GathersqlListManager;

public class WebDataCollector {
	//WebConfigDao urldao = null;
//	private Hashtable sendeddata = ShareData.getSendeddata();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	public WebDataCollector() {
	}
	
	public void collect_Data(NodeGatherIndicators webIndicatorsNode) {
		String urlid = webIndicatorsNode.getNodeid();
		
		Urlmonitor_realtimeDao realtimeManager = new Urlmonitor_realtimeDao();
		try {
			
//			List url_list = new ArrayList();
			WebConfigDao urldao = new WebConfigDao();
			Hashtable realHash = realtimeManager.getAllReal();
			Calendar date = Calendar.getInstance();
			WebConfig uc = null;
			
			if(ShareData.getAllurls() != null){
				if(ShareData.getAllurls().containsKey(Integer.parseInt(urlid))){
					uc = (WebConfig)ShareData.getAllurls().get(Integer.parseInt(urlid));
				}else{
					return ;
				}
			}else{
				return ;
			}
			
			if(uc == null)return;
			if(uc.getFlag() == 0)return;
			//�ж��Ƿ��ڲɼ�ʱ�����
			Hashtable hashtable = ShareData.getTimegatherhash();
	    	if(ShareData.getTimegatherhash() != null){
	    		if(ShareData.getTimegatherhash().containsKey(uc.getId()+":webservice")){
	    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();
	    			int _result = 0;
	    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(uc.getId()+":webservice"));
	    			if(_result ==1 ){
	    				//SysLogger.info("########ʱ�����: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
	    			}else if(_result == 2){
	    				//SysLogger.info("########ȫ��: ��ʼ�ɼ� "+node.getIpAddress()+" PING������Ϣ##########");
	    			}else {
	    				SysLogger.info("######## "+uc.getIpAddress()+" ���ڲɼ�URLʱ�����,�˳�##########");
	    				//���֮ǰ�ڴ��в����ĸ澯��Ϣ
	    			    try{
	    			    	//���֮ǰ�ڴ��в������ڴ�澯��Ϣ
							CheckEventUtil checkutil = new CheckEventUtil();
							NodeDTO nodedto = null;
	    					NodeUtil nodeUtil = new NodeUtil();
	    					nodedto = nodeUtil.creatNodeDTOByNode(uc);
							checkutil.deleteEvent(uc.getId()+"", nodedto.getType(), nodedto.getSubtype(), "webping", null);
							checkutil.deleteEvent(uc.getId()+"", nodedto.getType(), nodedto.getSubtype(), "webresponsetime", null);
							checkutil.deleteEvent(uc.getId()+"", nodedto.getType(), nodedto.getSubtype(), "webpagesize", null);
							checkutil.deleteEvent(uc.getId()+"", nodedto.getType(), nodedto.getSubtype(), "webkeyword", null);
	    			    }catch(Exception e){
	    			    	e.printStackTrace();
	    			    }
	    				return ;
	    			}
	    			
	    		}
	    	}
			
			try{
				uc = (WebConfig) urldao.findByID(urlid);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				urldao.close();
			}
			
			if(uc != null){
			
			//����ʷ�澯���
			Web _web = (Web)PollingEngine.getInstance().getWebByID(uc.getId());	
			if(_web == null){
				return;
			}
			if(_web != null){
				//��ʼ��WEB���ʷ����״̬
				_web.setStatus(0);
				_web.setAlarm(false);
				_web.getAlarmMessage().clear();
				Calendar _tempCal = Calendar.getInstance();				
				Date _cc = _tempCal.getTime();
				String _time = sdf.format(_cc);
				_web.setLastTime(_time);
			}
			
			int url_id = uc.getId();
			boolean old = false;
			String str = "";
			Integer smssign = new Integer(0);
			Urlmonitor_realtime urold = null;
			if (realHash.get(url_id) != null) {
				old = true;
				urold = (Urlmonitor_realtime) realHash.get(url_id);
				str = urold.getPage_context();
				smssign = urold.getSms_sign();
			}
			UrlDataCollector udc = new UrlDataCollector();
			String contentStr = "";
			if (str != null && str.length() > 100) {
				contentStr = str.substring(0, 100);
			}

			Urlmonitor_realtime ur = udc.getUrlmonitor_realtime(uc, old,contentStr);
			// ʵʱ����
			ur.setUrl_id(url_id);
			if (old == true) {
				ur.setSms_sign(urold.getSms_sign());
			} else {
				ur.setSms_sign(smssign);
			}
			int pingflag = 0;
			int pageflag = 1;
			int keywordflag = 0;
			int condelayflag = 0;
			
			if(_web.getTracertflag() == 1){
				//·�ɸ����κ�ʱ������,����·�ɸ������ݲɼ�
				//����URL���ӵ�·�ɸ��ٹ���
				  TraceRouteExecute tre=new TraceRouteExecute();
				  List tracelist = new ArrayList();
				  Hashtable tracertHash = new Hashtable();
				  try{
					  String urls = _web.getStr();
//					  System.out.println("tracert -h 10 -w 5000 "+_web.getStr().split("//")[1]+"   IP:"+_web.getIpAddress());
				  		tracelist = tre.executeTracert("tracert -h 10 -w 5000 "+_web.getStr().split("//")[1],_web.getIpAddress());
				  }catch(Exception e){
				  		e.printStackTrace();
				  	}
				    if(tracelist != null && tracelist.size()>0){			    	
				    	Tracerts tracerts = new Tracerts();
				    	tracerts.setNodetype("url");
				    	tracerts.setConfigid(_web.getId());
				    	tracerts.setDotime(Calendar.getInstance());
				    	TracertsDao tradao = new TracertsDao();
				    	try{
				    		
				    		tradao.save(tracerts);
				    	}catch(Exception e){
				    		e.printStackTrace();
				    	}finally{
				    		tradao.close();
				    	}
				    	List dolist = new ArrayList();
				    	TracertsDetail vo = null;
				    	TracertsDao d = new TracertsDao();
				    	int id = d.queryId(_web.getId(), tracerts.getDotime());
				    	for(int j=0;j<tracelist.size();j++){
				    		String cont =(String) tracelist.get(j);
				    		if(cont != null && cont.trim().length()>0){
				    			vo = new TracertsDetail();
				    			vo.setDetails(cont);
				    			vo.setNodetype("url");
				    			vo.setTracertsid(id);
//				    			System.out.println(id+"------------------"+tracerts.getId());
				    			vo.setConfigid(_web.getId());
				    			dolist.add(vo);
				    		}
				    		//SysLogger.info(_web.getStr()+":tracert====="+tracelist.get(j));
				    	}
				    	TracertsDetailDao detaildao = new TracertsDetailDao();
				    	try{
				    		detaildao.save(dolist);
				    	}catch(Exception e){
				    		e.printStackTrace();
				    	}finally{
				    		detaildao.close();
				    	}
				    	tracertHash.put("details", dolist);
				    	tracertHash.put("tracert", tracerts);
				    	//�ŵ��ڴ���
				    	if(ShareData.getAlltracertsdata()!= null){
				    		ShareData.getAlltracertsdata().put("url:"+_web.getId(), tracertHash);
				    	}else{
				    		Hashtable temphash = new Hashtable();
				    		temphash.put("url:"+_web.getId(), tracertHash);
				    		ShareData.setAlltracertsdata(temphash);
				    	}

				    }				
			}
			

			
			
			pingflag = ur.getIs_canconnected();
			//SysLogger.info(uc.getStr()+"@@@@@@@@@@@@@@@@@@@"+ur.getIs_canconnected());
				if (ur.getIs_canconnected() == 0) {
					//���Ӳ���
					//pingflag = 0;
//		 			//��Ҫ���ӷ������ڵķ������Ƿ�����ͨ
//					Vector ipPingData = (Vector)ShareData.getPingdata().get(uc.getIpAddress());
//					if(ipPingData != null){
//						Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
//						String pingvalue = pingdata.getThevalue();
//						if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
//						double pvalue = new Double(pingvalue);
//						if(pvalue == 0){
//							//�������������Ӳ���***********************************************
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//				            String sysLocation = "";
//				              try{
//				            	  SmscontentDao eventdao = new SmscontentDao();
//				            	  String eventdesc = "WEB����("+uc.getAlias()+" ·��:"+uc.getStr()+")"+"�ķ��ʷ���ֹͣ";
//				            	  eventdao.createEventWithReasion("poll",uc.getId()+"",uc.getAlias()+"("+uc.getStr()+")",eventdesc,3,"web","ping","���ڵķ��������Ӳ���");
//				              }catch(Exception e){
//				            	  e.printStackTrace();
//				              }
//						}else{
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//						}
//						
//					}else{
//						_web.setAlarm(true);
//						_web.setStatus(3);
//						//dbnode.setStatus(3);
//						List alarmList = _web.getAlarmMessage();
//						if(alarmList == null)alarmList = new ArrayList();
//						_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//					}				
//					ur.setKey_exist("WEB���ʷ���ֹͣ");
//					ur.setChange_rate("0.0");
//					createSMS("web", uc);
				} else if (uc.getMon_flag() == 2 && ur.getIs_refresh() == 0) {
					//���Ӳ���
					//pingflag = 0;
//					//��Ҫ����WEB�������ڵķ������Ƿ�����ͨ
//					Vector ipPingData = (Vector)ShareData.getPingdata().get(uc.getIpAddress());
//					if(ipPingData != null){
//						Pingcollectdata pingdata = (Pingcollectdata)ipPingData.get(0);
//						String pingvalue = pingdata.getThevalue();
//						if(pingvalue == null || pingvalue.trim().length()==0)pingvalue="0";
//						double pvalue = new Double(pingvalue);
//						if(pvalue == 0){
//							//�������������Ӳ���***********************************************
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//				            String sysLocation = "";
//				              try{
//				            	  SmscontentDao eventdao = new SmscontentDao();
//				            	  String eventdesc = "WEB����("+uc.getAlias()+" ·��:"+uc.getStr()+")"+"�ķ��ʷ���ֹͣ";
//				            	  eventdao.createEventWithReasion("poll",uc.getId()+"",uc.getAlias()+"("+uc.getStr()+")",eventdesc,3,"web","ping","���ڵķ��������Ӳ���");
//				              }catch(Exception e){
//				            	  e.printStackTrace();
//				              }
//						}else{
//							_web.setAlarm(true);
//							_web.setStatus(3);
//							List alarmList = _web.getAlarmMessage();
//							if(alarmList == null)alarmList = new ArrayList();
//							_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//						}
//						
//					}else{
//						_web.setAlarm(true);
//						_web.setStatus(3);
//						List alarmList = _web.getAlarmMessage();
//						if(alarmList == null)alarmList = new ArrayList();
//						_web.getAlarmMessage().add("WEB���ʷ���ֹͣ");
//					}
//					ur.setKey_exist("WEB���ʷ���ֹͣ");
//					ur.setChange_rate("0.0");
//					createSMS("web", uc);
				} else {
					//pingflag = 1;
					condelayflag = ur.getCondelay();
					String[] keyword;
					if(uc.getKeyword() == null) {
						keyword = null;
					} else {
						keyword = uc.getKeyword().trim().split(",");
					}
					
					//�ж�http���ݰ���С
//					if(gatherHash.containsKey("pagesize")){
					try{
						if(Integer.parseInt(ur.getPagesize())<Integer.parseInt(_web.getPagesize_min())){
							pageflag = 0;
						}else{
							pageflag = 1;
						}
					}catch(Exception e){
						
					}
//					}
					//�ؼ��ּ��
//					if(gatherHash.containsKey("keyword")){
						String result = "WEB���ʷ���("+uc.getStr()+")δ��⵽�ؼ��� ";
						String key = "";
						int times = 0;
						if(keyword!=null&&keyword.length>0){
							//if(ur.getKey_exist()!=null&&!"".equals(ur.getKey_exist())){
							if(ur.getKey_exist()!=null&&!"".equals(ur.getPage_context())){
								for(int j=0;j<keyword.length;j++){
									if(!ur.getPage_context().contains(keyword[j])){
										key = key+keyword[j]+";";
										times++;
									}
								}
							}
						}
						
						keywordflag = times*100/(keyword==null?1:keyword.length);
						
						ur.setChange_rate(""+times*100/(keyword==null?1:keyword.length));
						String theValue = "0";
						if("".equals(key)){
							ur.setKey_exist("���йؼ��ֳɹ����!");
						} else {
							ur.setKey_exist(result+key);
							theValue = "1";						
						}
//					}else{
//						ur.setKey_exist("δ���ùؼ��ּ��!");
//					}
				}
				
				Vector webvector = new Vector();
				
				Pingcollectdata hostdata=null;
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBPing");
				hostdata.setEntity("Utilization");
				hostdata.setSubentity("ConnectUtilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("%");
				if(pingflag == 1){
					hostdata.setThevalue("100");
				}else{
					hostdata.setThevalue("0");
				}
				webvector.add(hostdata);
				

				
				//ҳ���С
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBPagesize");
				hostdata.setEntity("WEBPagesize");
				hostdata.setSubentity("ConnectUtilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("");
				if(pageflag == 0){
					//С�����õ�ҳ���С
					hostdata.setThevalue("0");
				}else{
					hostdata.setThevalue("1");
				}
				webvector.add(hostdata);
				
				//�ؼ���
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBKeyword");
				hostdata.setEntity("WEBKeyword");
				hostdata.setSubentity("ConnectUtilization");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("%");
				hostdata.setThevalue(keywordflag+"");
				webvector.add(hostdata);
				
				
				//��Ӧʱ��
				hostdata=new Pingcollectdata();
				hostdata.setIpaddress(uc.getIpAddress());
				//Calendar date=Calendar.getInstance();
				hostdata.setCollecttime(date);
				hostdata.setCategory("WEBResponsetime");
				hostdata.setEntity("WEBResponsetime");
				hostdata.setSubentity("WEBResponsetime");
				hostdata.setRestype("dynamic");
				hostdata.setUnit("ms");
				hostdata.setThevalue(condelayflag+"");
				webvector.add(hostdata);
				
				Hashtable collectHash = new Hashtable();
				collectHash.put("url", webvector);
				NodeUtil nodeUtil = new NodeUtil();
				NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(uc);
			    try{
//					AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
//					List list = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(String.valueOf(uc.getId()), AlarmConstant.TYPE_SERVICE, "url");
//					for(int k = 0 ; k < list.size() ; k ++){
//						AlarmIndicatorsNode alarmIndicatorsnode = (AlarmIndicatorsNode)list.get(k);
//						//��web����ֵ���и澯���
//						CheckEventUtil checkutil = new CheckEventUtil();
//						checkutil.updateData(_web,collectHash,"service","url",alarmIndicatorsnode);
//					}
			    	updateUrlData(nodeDTO, collectHash);
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			    
				//���и澯���
				try{
					//NodeUtil nodeUtil = new NodeUtil();
					//NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(_web);
					// �ж��Ƿ���ڴ˸澯ָ��
					AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
					List list1 = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(nodeDTO.getId()+ "", nodeDTO.getType(), nodeDTO.getSubtype());
					CheckEventUtil checkEventUtil = new CheckEventUtil();
					for (int i = 0; i < list1.size(); i++) {
						AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list1.get(i);
						if ("webkeyword".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
							checkEventUtil.checkEvent(nodeDTO,alarmIndicatorsNode, ur.getChange_rate());
						}else if ("webpagesize".equalsIgnoreCase(alarmIndicatorsNode.getName())) {
							checkEventUtil.checkEvent(nodeDTO,alarmIndicatorsNode, pageflag+"");
						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				// ����realtime
				String _pagecontext = ur.getPage_context();
				if (_pagecontext != null && _pagecontext.length() > 100) {
					ur.setPage_context(_pagecontext.substring(0, 100));
				}
				realtimeManager.close();
				realtimeManager = new Urlmonitor_realtimeDao();
				if (old == true) {
					ur.setId(urold.getId());
					ur.setMon_time(Calendar.getInstance());					
					realtimeManager.update(ur);
				}
				if (old == false) {					
					realtimeManager.save(ur);
				}
				
				// �������ʷ����
				Urlmonitor_history uh = new Urlmonitor_history();
				Urlmonitor_historyDao historyManager = new Urlmonitor_historyDao();
				uh.setUrl_id(ur.getUrl_id());
				//SysLogger.info(uc.getStr()+"========="+ur.getIs_canconnected());
				uh.setIs_canconnected(ur.getIs_canconnected());
				uh.setIs_refresh(ur.getIs_refresh());
				uh.setIs_valid(ur.getIs_valid());
				uh.setMon_time(ur.getMon_time());
				uh.setReason(ur.getReason());
				uh.setCondelay(ur.getCondelay());
				uh.setKey_exist(ur.getKey_exist());
				uh.setPagesize(ur.getPagesize());
				uh.setChange_rate(ur.getChange_rate());
				try{
					historyManager.save(uh);
				}catch(Exception e){
					
				}finally{
					historyManager.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			realtimeManager.close();
			//urldao.close();
			//SysLogger.info("********URL Thread Count : "+Thread.activeCount());
		}
	}
	private void updateUrlData(NodeDTO nodeDTO, Hashtable hashtable){
    	AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
    	List list = alarmIndicatorsUtil.getAlarmIndicatorsForNode(nodeDTO.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype());
    	if(list == null || list.size() ==0){
    		SysLogger.info("�޸澯ָ�� ���澯=======================");
    		return;
    	} 
    	Vector webvector = (Vector) hashtable.get("url");
    	CheckEventUtil checkEventUtil = new CheckEventUtil();
    	for(int i = 0 ; i < list.size(); i++){
    		try {
				AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
				if("webping".equalsIgnoreCase(alarmIndicatorsNode.getName())){
					if(webvector!=null&&webvector.size()>0){
						for(int k = 0 ; k < webvector.size();k++){
							Pingcollectdata webpingdata= (Pingcollectdata)webvector.get(k);
							if("Utilization".equalsIgnoreCase(webpingdata.getEntity())&&"ConnectUtilization".equals(webpingdata.getSubentity())){
								checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, webpingdata.getThevalue());
							}
						}
					}
				} else if ("webresponsetime".equalsIgnoreCase(alarmIndicatorsNode.getName())){
					if(webvector!=null&&webvector.size()>0){
						for(int k = 0 ; k < webvector.size();k++){
							Pingcollectdata webpingdata=(Pingcollectdata)webvector.get(k);;
							if("WEBResponsetime".equals(webpingdata.getEntity())){
								checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, webpingdata.getThevalue());
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
}
