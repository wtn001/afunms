<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.indicators.model.NodeDTO" %>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.polling.PollingEngine" %>
<%@page import="java.util.*"%>
<%@page import="com.afunms.common.util.ShareData" %>
<%@page import="com.afunms.polling.node.Was" %>
<%
String rootPath=request.getContextPath();  
String  wasName= request.getParameter("wasName")==null?"":request.getParameter("wasName"); //vo.getName() 
String  wasIpaddress= request.getParameter("wasIpaddress")==null?"":request.getParameter("wasIpaddress"); //vo.getIpaddress()
String  wasPort= request.getParameter("wasPort")==null?"":request.getParameter("wasPort"); //vo.getPortnum()
int  wasFlag= Integer.parseInt(request.getParameter("wasFlag")==null?"":request.getParameter("wasFlag"));//vo.getMon_flag()
String  wasVersion= request.getParameter("wasVersion")==null?"":request.getParameter("wasVersion"); //vo.getVersion()
String  percent1= request.getParameter("percent1")==null?"0":request.getParameter("percent1"); //percent1 
String  percent2= request.getParameter("percent2")==null?"0":request.getParameter("percent2"); //percent2 
float avgPing=Float.parseFloat(percent1);
 StringBuffer dataStr = new StringBuffer();
		 	dataStr.append("��ͨ;").append(Math.round(avgPing)).append(";false;7CFC00\\n");
		 	dataStr.append("δ��ͨ;").append(100-Math.round(avgPing)).append(";false;FF0000\\n");
		 	String avgdata = dataStr.toString();

int id = Integer.parseInt(request.getParameter("id"));
       Was was = (Was)PollingEngine.getInstance().getWasByID(id);
       int status = 0;
       int alarmLevel = 0;
       if(was != null){	
		Hashtable checkEventHashtable = ShareData.getCheckEventHash();
		NodeUtil nodeUtil = new NodeUtil();
		/*===========for status start==================*/
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(was);
		if(nodeDTO!=null){
			String chexkname = id+":"+nodeDTO.getType()+":"+nodeDTO.getSubtype()+":";
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
       }
%>
<table id="application-detail-content" class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value=" <%=wasName%> ��ϸ��Ϣ"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body" class="application-detail-content-body">
				<tr>
					<td> 
						<table style="BORDER-COLLAPSE: collapse" bordercolor=#ececec rules=none align=center border=1 cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="70%" align="left" valign="top">
									<table>
										<tr>
											<td width="15%" height="29" align="left" nowrap class=txtGlobal>
												&nbsp;����:
											</td>
											<td width="35%"><%=wasName%></td>
											<td width="15%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;IP��ַ:
											</td>
											<td width="35%"><%=wasIpaddress%>
											</td>
										</tr>
										<tr bgcolor="#ECECEC">
											<td width="15%" height="29" align="left" nowrap class=txtGlobal>
												&nbsp;�˿�:
											</td>
											<td width="35%"><%=wasPort%>
											</td>
											<td width="15%" height="29" align="left" nowrap class=txtGlobal>
												&nbsp;״̬:
											</td>
											<td>
											<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmLevel)%>" border="0">
											<%=NodeHelper.getStatusDescr(alarmLevel)%>
											</td>
										</tr>
										<tr>
											<td width="15%" height="29" align="left" nowrap class=txtGlobal>
												&nbsp;�汾:
											</td>
											<td width="35%"><%=wasVersion%>
											</td>
											<td width="15%" height="29" align="left" nowrap class=txtGlobal>
												&nbsp;����״̬:
											</td>
											<%
												if (wasFlag == 1) {
											%>
											<td width="35%">
												�Ѽ���
											</td>
											<%
												} else {
											%>
											<td width="35%">
												δ����
											</td>
											<%
												}
											%>
										</tr>
									</table>
								</td>
								<td width="19%" align="center" valign="middle" class=dashLeft>
									<table style="BORDER-COLLAPSE: collapse"  rules=none align=center border=1 bordercolor=#ececec cellpadding=0 cellspacing="0" width=100%>
										<tr>
											<td  align="left" valign="middle" class=dashLeft>
												
													<table style="BORDER-COLLAPSE: collapse" 
													rules=none align=center border=0 cellpadding=0
													cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="26">
														<td align="center">
															������ͨ��
														</td>
													</tr>
													<tr class="topNameRight">
														<td height="30" align="center">
														<!--  	<div id="flashcontent00">
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
															<img src="<%=rootPath%>/resource/image/Loading_2.gif"></td>
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