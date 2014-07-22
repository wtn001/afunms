<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.node.Weblogic"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.temp.dao.WeblogicDao" %>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.application.weblogicmonitor.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>


<%

String rootPath=request.getContextPath();  
String  IpAddress= request.getParameter("IpAddress")==null?"":request.getParameter("IpAddress"); //weblogicconf.getIpAddress()  
String  name= request.getParameter("name")==null?"":request.getParameter("name"); //weblogicconf.getAlias() 
String  id= request.getParameter("id")==null?"":request.getParameter("id");
String  Portnum= request.getParameter("Portnum")==null?"":request.getParameter("Portnum"); //weblogicconf.getPortnum() 
String  DomainName= request.getParameter("DomainName")==null?"":request.getParameter("DomainName"); //normalvalue.getDomainName()  
String  DomainActive= request.getParameter("DomainActive")==null?"":request.getParameter("DomainActive"); //normalvalue.getDomainActive() 
String  DomainAdministrationPort= request.getParameter("DomainAdministrationPort")==null?"":request.getParameter("DomainAdministrationPort"); //normalvalue.getDomainAdministrationPort() 

String  DomainConfigurationVersion= request.getParameter("DomainConfigurationVersion")==null?"":request.getParameter("DomainConfigurationVersion"); //normalvalue.getDomainConfigurationVersion() 

String  lasttime= request.getParameter("lasttime")==null?"":request.getParameter("lasttime");  
String  nexttime= request.getParameter("nexttime")==null?"":request.getParameter("nexttime"); 
String  percent1= request.getParameter("percent1")==null?"0":request.getParameter("percent1");   
String  percent2= request.getParameter("percent2")==null?"0":request.getParameter("percent2");   

com.afunms.polling.node.Weblogic weblogic = (com.afunms.polling.node.Weblogic)PollingEngine.getInstance().getWeblogicByID(Integer.parseInt(id));
    
        
		int alarmLevel = 0;
		Hashtable checkEventHashtable = ShareData.getCheckEventHash();
		NodeUtil nodeUtil = new NodeUtil();
		/*===========for status start==================*/ 
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(weblogic);
		if(nodeDTO!=null){
			String chexkname = id+":"+nodeDTO.getType()+":"+nodeDTO.getSubtype()+":";
			//System.out.println(chexkname);
			if(checkEventHashtable!=null){
				for(Iterator it = checkEventHashtable.keySet().iterator();it.hasNext();){ 
			        String key = (String)it.next(); 
			        if(key.startsWith(chexkname)){
			        	if(alarmLevel < (Integer) checkEventHashtable.get(key)){
			        		alarmLevel = (Integer) checkEventHashtable.get(key); 
			        	}
			        }
				}
			}
		}
		
int  Mon_flag=Integer.parseInt(request.getParameter("Mon_flag")==null?"":request.getParameter("Mon_flag")); //weblogicconf.getMon_flag()
 float avgPing=Float.parseFloat(percent1);
 StringBuffer dataStr = new StringBuffer();
		 	dataStr.append("��ͨ;").append(Math.round(avgPing)).append(";false;7CFC00\\n");
		 	dataStr.append("δ��ͨ;").append(100-Math.round(avgPing)).append(";false;FF0000\\n");
		 	String avgdata = dataStr.toString();
	
	String runmodel = PollingEngine.getCollectwebflag(); 
	Hashtable hash = new Hashtable();
	List serverdatalist =new ArrayList();
	if("0".equals(runmodel)){
		//�ɼ�������Ǽ���ģʽ
		hash = (Hashtable)ShareData.getWeblogicdata().get(IpAddress);
	}else{
 		List labelList = new ArrayList();
		labelList.add("normalValue");
		labelList.add("serverValue");
		WeblogicDao weblogicDao = new WeblogicDao();
		hash = weblogicDao.getWeblogicData(labelList,id);
	}
	String serverName = "";
	String serverAddr = "";
	String serverPort = "";
	String serverRun = "<font color=red>ֹͣ</font>";
	if(hash!=null){
     	serverdatalist = (List)hash.get("serverValue");
     	if(serverdatalist != null && serverdatalist.size()>0){
     		for(int i=0;i<serverdatalist.size();i++){
     			WeblogicServer vo=(WeblogicServer)serverdatalist.get(i);
     			if(vo.getServerRuntimeName().equalsIgnoreCase(weblogic.getServerName())){
     				serverRun = vo.getServerRuntimeState();
     			} 
     		}
     	}
	} 	
		 	
 %>
<table id="application-detail-content" class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="WEBLOGIC��ϸ��Ϣ"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body" class="application-detail-content-body">
				<tr>
					<td>
						<table style="BORDER-COLLAPSE: collapse" rules=none align=center border=1 cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="70%" align="left" valign="top">
									<table>
										<tr>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;����:
											</td>
											<td width="35%"><%=name%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;IP��ַ:
											</td>
											<td width="35%"><%=IpAddress%>
											</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											
											<td width="15%" height="26" align="left" nowrap class=txtGlobal>
												&nbsp;����״̬:
											</td>
											<%
												if (Mon_flag == 1) {
											%>
											<td width="35%" >
												�Ѽ��
											</td>
											<%
												} else {
											%>
											<td width="35%" >
												δ���
											</td>
											<%
												}
											%>
											<td width="15%" height="26" align="left" nowrap class=txtGlobal>
												&nbsp;״̬:
											</td>
											<td width="35%" valign=middle>
												<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmLevel)%>">
												&nbsp;<%=NodeHelper.getStatusDescr(alarmLevel)%>
											</td>
										</tr>
										<!--  										
										<tr >
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;��������:
											</td>
											<td width="35%"><%=weblogic.getServerName()%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;���������ַ:
											</td>
											<td width="35%"><%=weblogic.getServerAddr()%>
										</tr>
										
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;��������˿�:
											</td>
											<td width="35%"><%=weblogic.getServerPort()%>
											</td>
											
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;
											</td>
											<td width="35%">
										</tr>
										-->
										<tr>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;��������:
											</td>
											<td width="35%"><%=weblogic.getDomainName()%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;������״̬:
											</td>
											<%
												if ("1".equals(DomainActive)) {
											%>
											<td width="35%">
												�
											</td>
											<%
												} else {
											%>
											<td width="35%">
												���
											</td>
											<%
												}
											%>
										</tr>
										<!-- 
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;����˿�:
											</td>
											<td width="35%"><%=weblogic.getDomainPort()%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;�汾:
											</td>
											<td width="35%" colspan=3><%=weblogic.getDomainVersion()%>
											</td>
										</tr>
										 -->
										<tr bgcolor="#F1F1F1">
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;SNMP��ض˿�:
											</td>
											<td width="35%"><%=Portnum%>
											</td>
											<td width="15%" height="26" align="left" nowrap
												class=txtGlobal>
												&nbsp;�ɼ�ʱ��:
											</td>
											<td width="35%"><%=lasttime%>
											</td>
										</tr>

									</table>

								</td>
								<td width="20%" align="center" valign="middle" class=dashLeft>

									<table style="BORDER-COLLAPSE: collapse" 
													rules=none align=center border=1 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center" >
															������ͨ��
														</td>
													</tr>

										<tr>
											<td width="400" align="left" valign="middle" class=dashLeft>
												<table style="BORDER-COLLAPSE: collapse" 
													cellpadding=0 rules=none width=80% align=center border=0
													algin="center">
													<tr class="topNameRight">
														<td height="30" align="center">
														<!-- <div id="flashcontent00">
																<strong>You need to upgrade your Flash Player</strong>
															</div>
															<script type="text/javascript">
																var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1=<%=percent1%>&percentStr1=����&percent2=<%=percent2%>&percentStr2=������", "Pie_Component", "160", "160", "8", "#ffffff");
																so.write("flashcontent00"); 
															</script>
															    -->
															<div id="avgping">
				                                            <strong>You need to upgrade your Flash Player</strong>
				                                            </div>
				                                     	
				                                            <script type="text/javascript"
							                                  src="<%=rootPath%>/include/swfobject.js"></script>
					                                        <script type="text/javascript">
						                                       var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						                                           so.addVariable("path", "<%=rootPath%>/amchart/");
						                                           so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                           so.addVariable("chart_data","<%=avgdata%>");
						                                           so.write("avgping");
					                                   </script>
														</td>

													</tr>
													<tr>
														<td height="7" align=center>
															<img src="<%=rootPath%>/resource/image/Loading_2.gif">
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table> 
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>