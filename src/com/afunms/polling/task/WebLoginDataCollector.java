/*
 * Created on 2005-4-22
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.afunms.polling.task;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.afunms.alarm.model.AlarmIndicatorsNode;
import com.afunms.alarm.util.AlarmIndicatorsUtil;
import com.afunms.application.dao.FTPConfigDao;
import com.afunms.application.dao.FtpHistoryDao;
import com.afunms.application.dao.FtpRealTimeDao;
import com.afunms.application.dao.TracertsDao;
import com.afunms.application.dao.TracertsDetailDao;
import com.afunms.application.dao.Urlmonitor_historyDao;
import com.afunms.application.dao.Urlmonitor_realtimeDao;
import com.afunms.application.dao.WebLoginConfigDao;
import com.afunms.application.manage.FTPManager;
import com.afunms.application.manage.WebLoginManager;
import com.afunms.application.model.FTPConfig;
import com.afunms.application.model.FtpHistory;
import com.afunms.application.model.FtpRealTime;
import com.afunms.application.model.Tracerts;
import com.afunms.application.model.TracertsDetail;
import com.afunms.application.model.Urlmonitor_history;
import com.afunms.application.model.Urlmonitor_realtime;
import com.afunms.application.model.WebConfig;
import com.afunms.application.model.webloginConfig;
import com.afunms.application.model.webloginData;
import com.afunms.application.util.UrlDataCollector;
import com.afunms.common.util.CheckEventUtil;
import com.afunms.common.util.DBManager;
import com.afunms.common.util.ShareData;
import com.afunms.common.util.SysLogger;
import com.afunms.common.util.SysUtil;
import com.afunms.common.util.SystemConstant;
import com.afunms.indicators.model.NodeDTO;
import com.afunms.indicators.model.NodeGatherIndicators;
import com.afunms.indicators.util.NodeUtil;
import com.afunms.initialize.ResourceCenter;
import com.afunms.polling.PollingEngine;
import com.afunms.polling.node.Ftp;
import com.afunms.polling.node.Web;
import com.afunms.polling.node.WebLogin;
import com.afunms.polling.om.Interfacecollectdata;
import com.afunms.polling.om.Pingcollectdata;
import com.afunms.system.util.TimeGratherConfigUtil;
import com.afunms.toolService.traceroute.TraceRouteExecute;
import com.gatherdb.GathersqlListManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;




public class WebLoginDataCollector {
	//WebConfigDao urldao = null;
//	private Hashtable sendeddata = ShareData.getSendeddata();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
	private static HttpClient httpClient = null;
	private static long condelay = 0;
	
	
	public WebLoginDataCollector() {
	}
	
//	public static void main(String args[]){
//		webloginConfig web = new webloginConfig();
//		web.setId(46);
//		web.setKeyword("����,����");
//		web.setName_flag("userid");
//		web.setUser_name("admin");
//		web.setPassword_flag("password");
//		web.setUser_password("admin");
//		web.setUrl("http://localhost:8080/afunms/user.do?action=login");
//		WebLoginDataCollector a =  new WebLoginDataCollector();
////		NodeGatherIndicators s = new NodeGatherIndicators();
////		s.setNodeid(46+"");
////		s.setClasspath("com.afunms.polling.task.WebLoginDataCollector");
////		a.collect_Data(s);
//		try{
//		  String result = a.MatchContant(web).trim();//����ָ���ʾ��WEB����δ�������ܾ�����
//		
//		System.out.println("result:"+result.equals(""));
//		//result = "" ֤��  URL��д����
//		
//		Integer iscanconnected = new Integer(0+"");
//		//����ƥ��
//		if(result.equals("")){
//			a.CreateResultTosql(web.getId(), 3, "0");//3��ʾWEB URL��д���� �ṩ�ĵ�½�ӿڴ���
//		}else{
//		String keyword = web.getKeyword();
//		if(keyword.contains(",")){
//			String[] match = keyword.split(",");
//			for(int i=0;i<match.length;i++){
//				if(result.contains(match[i])){
//					iscanconnected = new Integer(1);
//				}else{
//					iscanconnected = new Integer(0);
//					break;
//				}
//			}
//		}
//		System.out.println(web.getId());
//		System.out.println(iscanconnected);
//		System.out.println(condelay);
//		a.CreateResultTosql(web.getId(), iscanconnected, condelay+"");
//		}
//		}catch(Exception e){
//			a.CreateResultTosql(web.getId(), 2, "0");//2��ʾWEB����ܾ����ӣ�
//			e.printStackTrace();
//		}
//	}
	
	
	
	public void collect_Data(NodeGatherIndicators webIndicatorsNode) {
		
		//System.out.println("=====================��վ�ɼ�=========================");
		String loginid = webIndicatorsNode.getNodeid();
		webloginConfig Config = null;
//		if(ShareData.getAllurls() != null){
//			if(ShareData.getAllurls().containsKey(Integer.parseInt(loginid))){
//				Config = (webloginConfig)ShareData.getAllurls().get(Integer.parseInt(loginid));
//			}else{
//				return ;
//			}
//		}else{
//			return ;
//		}
//		WebLoginConfigDao configdao = new WebLoginConfigDao();
//		try{
//			Config = (webloginConfig)configdao.findByID(loginid);
//			if("0".equalsIgnoreCase(Config.getFlag()))return;
//		}catch(Exception e){
//			
//		}finally{
//			configdao.close();
//		}
//		System.out.println("]================Config======================"+Config);
//		if(Config == null)
//			return;
	//	if(Config.getFlag() == 0)return;
		
		//�ж��Ƿ��ڲɼ�ʱ�����
		
		try{
			
//			if(ShareData.getWeblogindata() != null){
//				if(ShareData.getWeblogindata().containsKey(Integer.parseInt(loginid))){
//					Config = (webloginConfig)ShareData.getWeblogindata().get(Integer.parseInt(loginid));
//				}else{
//					return ;
//				}
//			}
//			if("0".equalsIgnoreCase(Config.getFlag()))return;
			
				
			List<webloginConfig> ConfigList = new ArrayList<webloginConfig>();
			WebLoginManager Manager = new WebLoginManager();
			ConfigList = Manager.getWebLoginConfigListByMonflag(1);
			Calendar date=Calendar.getInstance();
			
			WebLoginConfigDao configdao = null;
				try{
					Integer iscanconnected = new Integer(0+"");
					configdao = new WebLoginConfigDao();
					try{
						Config = (webloginConfig)configdao.findByID(loginid);
						if("0".equalsIgnoreCase(Config.getFlag()))return;
					}catch(Exception e){
						
					}finally{
						configdao.close();
					}
					Hashtable hashtable = ShareData.getTimegatherhash();
			    	if(ShareData.getTimegatherhash() != null){			    		
			    		
			    		if(ShareData.getTimegatherhash().containsKey(Config.getId()+":weblogin")){
			    			TimeGratherConfigUtil timeconfig = new TimeGratherConfigUtil();			    			
			    			int _result = 0;
			    			_result = timeconfig.isBetween((List)ShareData.getTimegatherhash().get(Config.getId()+":weblogin"));
			    			if(_result ==1 ){	    				
			    				
			    			}else if(_result == 2){		    				
			    				
			    			}else {			    				
			    				return;
			    			}
			    			
			    		}
			    	}
					WebLogin web = (WebLogin)PollingEngine.getInstance().getWebLoginByID(Config.getId());	
					if(web == null){
						return;
					}
				if(web != null){
					web.setStatus(0);
					web.setAlarm(false);
					web.getAlarmMessage().clear();
					Calendar _tempCal = Calendar.getInstance();				
					Date _cc = _tempCal.getTime();
					String _time = sdf.format(_cc);
					web.setLastTime(_time);
				}
				
				
				
				try{
					String result = this.MatchContant(Config).trim();//����ָ���ʾ��WEB����δ�������ܾ�����
					
//					System.out.println("result:"+result.equals(""));
					//result = "" ֤��  URL��д����
					
					//����ƥ��
					if(result.equals("")){
						this.CreateResultTosqlPing(Config.getId(), 0 ,date);//3��ʾWEB URL��д���� �ṩ�ĵ�½�ӿڴ���
						this.CreateResultTosqlResponse(Config.getId(), 0 ,date);
					}else{
					String keyword = Config.getKeyword();
					if(keyword.contains(",")){
						String[] match = keyword.split(",");
						for(int i=0;i<match.length;i++){
							if(result.contains(match[i])){
								iscanconnected = new Integer(1);
							}else{
								iscanconnected = new Integer(0);
								break;
							}
						}
					}else{
						if(result.contains(keyword)){
							iscanconnected = new Integer(1);
						}else{
							iscanconnected = new Integer(0);
						}
					}
					
					if(!Config.getUser_code().equals("")&&Config.getUser_code()!=null&&result.contains(Config.getUser_code()))
					{//ҳ�����ݿ����Ӹ澯
						//System.out.println("=======Config.getUser_code()============"+Config.getUser_code());
						DBconnErrorOnpage(Config);
					}
					if(!Config.getUser_code().equals("")&&Config.getUser_code()!=null&&!result.contains(Config.getUser_code()))
					{
						DBconnErrorOnpageRecover(Config);
					}
					
					int b = 100;
					if(iscanconnected == 0)b=0;
					this.CreateResultTosqlResponse(Config.getId(), Integer.parseInt(condelay+""),date);
					this.CreateResultTosqlPing(Config.getId(), b ,date);
					}
					}catch(Exception e){
						this.CreateResultTosqlResponse(Config.getId(), Integer.parseInt(condelay+""),date);
						this.CreateResultTosqlPing(Config.getId(), 0 ,date);//2��ʾWEB����ܾ����ӣ�
						e.printStackTrace();
					}
				
					Hashtable weblogin = new Hashtable();
					webloginData vo = new webloginData();
					vo.setWeblogin_id(Config.getId());
					vo.setIs_connected(iscanconnected);
					vo.setIs_response(condelay+"");
					vo.setMon_time(date);
					weblogin.put(Config.getId(), vo);
					//�ŵ��ڴ���  ����ͷ�������״̬���ɹ���ʧ��
			    	if(ShareData.getWeblogin()!= null){
			    		ShareData.getWeblogin().put(Config.getId(), weblogin);
			    	}else{
			    		ShareData.setWeblogicdata(Config.getId()+"", weblogin);
			    	}
				
//				
				Vector webloginvector = new Vector();
				//��ʼ���òɼ�ֵʵ��
				Pingcollectdata interfacedata=new Pingcollectdata();
				interfacedata.setIpaddress(Config.getUrl());
				interfacedata.setCollecttime(date);
				interfacedata.setCategory("weblogin");
				interfacedata.setEntity("Utilization");
				interfacedata.setSubentity("ConnectUtilization");
				interfacedata.setRestype("dynamic");
				interfacedata.setUnit("%");
				if(iscanconnected == 1){
					interfacedata.setThevalue("100");
				}else{
					interfacedata.setThevalue("0");
				}
				interfacedata.setUnit("%");
				interfacedata.setChname("�����½");
				webloginvector.add(interfacedata);
//				
				Hashtable collectHash = new Hashtable();
				collectHash.put("weblogin", webloginvector);
				NodeUtil nodeUtil = new NodeUtil();
				NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(Config);
			    try{
			    	updateWebLoginData(nodeDTO, collectHash);
			    }catch(Exception e){
			    	e.printStackTrace();
			    }
			}catch(Exception e){
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

	private String MatchContant(webloginConfig vo){
		//System.out.println("=============MatchContant����ִ��==================");
		String result = "";
		String doo=".do?";
//		String com="com";
//		String cn="cn";
		//��½ Url 
		long starttime = 0;
		long endtime = 0;
		
//		String loginUrl = "http://localhost:8080/afunms";  
		//���½����ʵ� Url      
//		String dataUrl = "http://localhost:8080/afunms/user.do?action=login";
//		String dataUrl = "http://oa.cpibj.com.cn/yyoa/CheckLogin";
		String dataUrl = vo.getUrl();
//		int index= dataUrl.lastIndexOf(".");//��ȡurl�ַ����С�.�������һ������           
//		String a= dataUrl.substring(index+1);//����a="html"
		
		
		//System.out.println("================aaaaaaaaaaaaaaaa1111111111========================="+a);
		if(dataUrl.contains(doo))
		{
		//	System.out.println("======��ȥ��==========.do?ͬ��=========================");
			httpClient = new HttpClient();  
			httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,  
	        "gbk");//���ñ����ʽ
			//ģ���½����ʵ�ʷ�������Ҫ��ѡ�� Post �� Get ����ʽ     
			PostMethod postMethod = null;
//			GetMethod getMethod = null;
			//���õ�½ʱҪ�����Ϣ��һ����û��������룬��֤���Լ�������     
			
			try {       
				//���� HttpClient ���� Cookie,���������һ���Ĳ���     
				httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);  
//				httpClient.executeMethod(postMethod);   

				//��õ�½��� Cookie       
//				Cookie[] cookies=httpClient.getState().getCookies(); 
//				String tmpcookies= "";       
//				for(Cookie c:cookies){    
//					tmpcookies += c.toString()+";";   
//					}           
//				//���е�½��Ĳ���   
				try{
					starttime = System.currentTimeMillis();
					postMethod = new PostMethod(dataUrl); 
					endtime = System.currentTimeMillis();
					//SysLogger.info("==="+dataUrl+"---"+vo.getName_flag()+":"+vo.getUser_name()+"   "+vo.getPassword_flag()+":"+vo.getUser_password());
					NameValuePair[] data = null;
					if(vo.getCode_flag() != null){
					      data = new NameValuePair[]{        
							new NameValuePair(vo.getName_flag(), vo.getUser_name()),   
							new NameValuePair(vo.getPassword_flag(), vo.getUser_password()),
							//new NameValuePair("code", "anyany")      
							};   
					}else{
						 data = new NameValuePair[]{        
									new NameValuePair(vo.getName_flag(), vo.getUser_name()),   
									new NameValuePair(vo.getPassword_flag(), vo.getUser_password()),
									new NameValuePair(vo.getCode_flag(), "")      
									};   
					}
					postMethod.setRequestBody(data);  
				}catch(Exception e){
					endtime = System.currentTimeMillis();
					e.printStackTrace();
				}
				//��Ӧʱ��
				condelay = endtime - starttime;
//				//ÿ�η�������Ȩ����ַʱ�����ǰ��� cookie ��Ϊͨ��֤       
//				getMethod.setRequestHeader("cookie",tmpcookies);   
//				//�㻹����ͨ�� PostMethod/GetMethod ���ø�������������   
//				//���磬referer ���������ģ�UA ���������涼������Լ���˭�����������������          
//				postMethod.setRequestHeader("Referer", "http://unmi.cc");     
//				postMethod.setRequestHeader("User-Agent","Unmi Spot");
				
				httpClient.executeMethod(postMethod);   
				//��ӡ���������ݣ�����һ���Ƿ�ɹ�          
				result = postMethod.getResponseBodyAsString(); 
				try{
				  if(vo.getOutflag() == 1){
					postMethod = new PostMethod(vo.getOuturl()); 
					httpClient.executeMethod(postMethod); 
					String result1 = postMethod.getResponseBodyAsString(); 
//					System.out.println("ҳ�淵���˳������"+result1);
				  }
				}catch(Exception e){
					e.printStackTrace();
				}
//				System.out.println("ҳ�淵�ؽ����"+result);     
				} catch (Exception e) {
					result = "";
//					e.printStackTrace(); 
					}     
			return result;
		}else{
			boolean flag=false;
			
			if(!vo.getUser_name().equals("")&&vo.getUser_name()!=null)
			  {flag=true;}
			if(flag==true){
		//		System.out.println("======��ȥ��==========Эͬͬ��=========================");
//				String Url=dataUrl+"?"+"userName"+"="+"guodu"+"&"+"password"+"="+"123456";
				String Url="";
				if(dataUrl.indexOf("?")>=0){
					Url = dataUrl+"&"+vo.getName_flag()+"="+vo.getUser_name()+"&"+vo.getPassword_flag()+"="+vo.getUser_password();
				}else{
					Url = dataUrl+"?"+vo.getName_flag()+"="+vo.getUser_name()+"&"+vo.getPassword_flag()+"="+vo.getUser_password();
				}
				SysLogger.info(Url);
				httpClient = new HttpClient();  
				httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,  
		        "gbk");//���ñ����ʽ
				//ģ���½����ʵ�ʷ�������Ҫ��ѡ�� Post �� Get ����ʽ     
//			    PostMethod postMethod = null;
				GetMethod getMethod = null;
				//���õ�½ʱҪ�����Ϣ��һ����û��������룬��֤���Լ�������     
				
				try {       
					//���� HttpClient ���� Cookie,���������һ���Ĳ���     
					httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);  
//					httpClient.executeMethod(postMethod);   

					//��õ�½��� Cookie       
//					Cookie[] cookies=httpClient.getState().getCookies(); 
//					String tmpcookies= "";       
//					for(Cookie c:cookies){    
//						tmpcookies += c.toString()+";";   
//						}           
//					//���е�½��Ĳ���   
					try{
					starttime = System.currentTimeMillis();
					getMethod = new GetMethod(Url); 
					endtime = System.currentTimeMillis();
					
					
					}catch(Exception e){
						endtime = System.currentTimeMillis();
						e.printStackTrace();
					}
					//��Ӧʱ��
					condelay = endtime - starttime;
//					//ÿ�η�������Ȩ����ַʱ�����ǰ��� cookie ��Ϊͨ��֤       
//					getMethod.setRequestHeader("cookie",tmpcookies);   
//					//�㻹����ͨ�� PostMethod/GetMethod ���ø�������������   
//					//���磬referer ���������ģ�UA ���������涼������Լ���˭�����������������          
//					postMethod.setRequestHeader("Referer", "http://unmi.cc");     
//					postMethod.setRequestHeader("User-Agent","Unmi Spot");
					
					httpClient.executeMethod(getMethod);   
					//��ӡ���������ݣ�����һ���Ƿ�ɹ�          
					result = getMethod.getResponseBodyAsString(); 
//					System.out.println("===========result==================="+result);
					try{
					  if(vo.getOutflag() == 1){
						getMethod = new GetMethod(vo.getOuturl()); 
						httpClient.executeMethod(getMethod); 
						String result1 = getMethod.getResponseBodyAsString(); 
//						System.out.println("ҳ�淵���˳������"+result1);
					  }
					}catch(Exception e){
						e.printStackTrace();
					}
//					System.out.println("ҳ�淵�ؽ����"+result);     
					} catch (Exception e) {
						result = "";
//						e.printStackTrace(); 
						}     
				return result;
			}else{

				//http://oa.cpibj.com.cn/yyoa/CheckLogin?userName=�û���&password=����
			//				System.out.println("======��ȥ��==========Config.getUser_name()==��=ͬ��========================");
							String Url=dataUrl;
							httpClient = new HttpClient();  
							try{
							httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,  
					        "gbk");//���ñ����ʽ
							//ģ���½����ʵ�ʷ�������Ҫ��ѡ�� Post �� Get ����ʽ     
//						    PostMethod postMethod = null;
							GetMethod getMethod = null;
							//���õ�½ʱҪ�����Ϣ��һ����û��������룬��֤���Լ�������     
							
								try{
								starttime = System.currentTimeMillis();
								getMethod = new GetMethod(Url); 
								endtime = System.currentTimeMillis();
								
								
								}catch(Exception e){
									endtime = System.currentTimeMillis();
									e.printStackTrace();
								}
								//��Ӧʱ��
								condelay = endtime - starttime;
//								//ÿ�η�������Ȩ����ַʱ�����ǰ��� cookie ��Ϊͨ��֤       
//								getMethod.setRequestHeader("cookie",tmpcookies);   
//								//�㻹����ͨ�� PostMethod/GetMethod ���ø�������������   
//								//���磬referer ���������ģ�UA ���������涼������Լ���˭�����������������          
//								postMethod.setRequestHeader("Referer", "http://unmi.cc");     
//								postMethod.setRequestHeader("User-Agent","Unmi Spot");
								
								
									httpClient.executeMethod(getMethod);   
									//��ӡ���������ݣ�����һ���Ƿ�ɹ�          
									result = getMethod.getResponseBodyAsString();
//									System.out.println("ҳ�淵�ؽ����"+result);    
								
								} catch (Exception e) {
									result = "";
//									e.printStackTrace(); 
									} 
							return result;
							
			}
				
			}
			
		
			
			
	}	
			
		
//	public static void main(String[] args){
//		
//		webloginConfig vo = new webloginConfig();
//		WebLoginDataCollector wdc=new WebLoginDataCollector();
//		wdc.MatchContant(vo);
//		
//		
//		
//	}
//	
	
	
	private void updateWebLoginData(NodeDTO nodeDTO, Hashtable hashtable){
    	AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
    	List list = alarmIndicatorsUtil.getAlarmIndicatorsForNode(nodeDTO.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype());
    	if(list == null || list.size() ==0){
    		SysLogger.info("�޸澯ָ�� ���澯=======================");
    		return;
    	} 
    	Vector webvector = (Vector) hashtable.get("weblogin");
    	CheckEventUtil checkEventUtil = new CheckEventUtil();
    	for(int i = 0 ; i < list.size(); i++){
    		try {
				AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
				if("weblogin".equalsIgnoreCase(alarmIndicatorsNode.getName())){
					if(webvector!=null&&webvector.size()>0){
						for(int k = 0 ; k < webvector.size();k++){
							Pingcollectdata webpingdata= (Pingcollectdata)webvector.get(k);
							if("Utilization".equalsIgnoreCase(webpingdata.getEntity())&&"ConnectUtilization".equals(webpingdata.getSubentity())){
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
	
	
	
	
	public void CreateResultTosqlPing(int webloginid,Integer isconnected,Calendar time1)
	{
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        DecimalFormat df = new DecimalFormat("0.0");
		        Calendar tempCal = time1;
				Date cc = tempCal.getTime();
				String time = sdf.format(cc);
				
	      	    StringBuffer sBuffer = new StringBuffer();
				sBuffer.append("insert into weblogin_ping"+webloginid);
				sBuffer.append("(thevalue,collecttime) ");
				sBuffer.append("values(");
				sBuffer.append(isconnected);
				if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append(",'");
					sBuffer.append(time);
					sBuffer.append("')");
				}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append(",");
					sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
					sBuffer.append(")");
				}
				System.out.println(sBuffer.toString());
				AddSql(sBuffer.toString());
				sBuffer = null;	
	   }
	
	public void CreateResultTosqlResponse(int webloginid,Integer response,Calendar time1)
	{
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        DecimalFormat df = new DecimalFormat("0.0");
		        Calendar tempCal = time1;
				Date cc = tempCal.getTime();
				String time = sdf.format(cc);
				
	      	    StringBuffer sBuffer = new StringBuffer();
				sBuffer.append("insert into weblogin_response"+webloginid);
				sBuffer.append("(thevalue,collecttime) ");
				sBuffer.append("values(");
				sBuffer.append(response);
				if("mysql".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append(",'");
					sBuffer.append(time);
					sBuffer.append("')");
				}else if("oracle".equalsIgnoreCase(SystemConstant.DBType)){
					sBuffer.append(",");
					sBuffer.append("to_date('"+time+"','YYYY-MM-DD HH24:MI:SS')");
					sBuffer.append(")");
				}
				System.out.println(sBuffer.toString());
				AddSql(sBuffer.toString());
				sBuffer = null;	
	   }
	
	public static void AddSql(String sql){
		DBManager pollmg = new DBManager();// ���ݿ�������
		 try{
			 pollmg.executeUpdate(sql);
		 }catch(Exception e){
			 
		 }finally{
			 pollmg.close(); 
		 }		 
		 pollmg=null;
	}
	
	//ҳ�������ݿ����Ӹ澯
	public void DBconnErrorOnpage(webloginConfig Config)
	{
		NodeUtil nodeUtil = new NodeUtil();
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(Config);
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List list = alarmIndicatorsUtil.getAlarmIndicatorsForNode(nodeDTO.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype());
    	if(list == null || list.size() ==0){
    		SysLogger.info("�޸澯ָ�� ���澯=======================");
    		return;
    	}
    	CheckEventUtil checkEventUtil = new CheckEventUtil();
    	for(int i = 0 ; i < list.size(); i++){
    		try {
				AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
				if("dbconerroronpage".equalsIgnoreCase(alarmIndicatorsNode.getName())){					
					checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, "1");					
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
		
	}
	
	public void DBconnErrorOnpageRecover(webloginConfig Config)
	{
		NodeUtil nodeUtil = new NodeUtil();
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(Config);
		AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
		List list = alarmIndicatorsUtil.getAlarmIndicatorsForNode(nodeDTO.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype());
    	if(list == null || list.size() ==0){
    		SysLogger.info("�޸澯ָ�� ���澯=======================");
    		return;
    	}
    	CheckEventUtil checkEventUtil = new CheckEventUtil();
    	for(int i = 0 ; i < list.size(); i++){
    		try {
				AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode)list.get(i);
				if("dbconerroronpage".equalsIgnoreCase(alarmIndicatorsNode.getName())){					
					checkEventUtil.checkEvent(nodeDTO, alarmIndicatorsNode, "-1");					
				} 
			} catch (Exception e) {
				System.out.println("webpage dbconnerror recover exception");
				e.printStackTrace();
			}
    	}
	}
}