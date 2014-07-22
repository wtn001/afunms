<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.inform.util.SystemSnap" %>

<%@page import="java.util.*"%>
<%
	Integer myId = (Integer) request.getAttribute("id");
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  
  List urllist = (List)request.getAttribute("urllist");
  TFTPConfig queryconf = (TFTPConfig)request.getAttribute("initconf");
  
	int status = 0;
	Node node = (Node) PollingEngine.getInstance().getTftpByID(queryconf.getId());
	String alarmmessage = "";
	if (node != null) {
		//status = node.getStatus();
		status = SystemSnap.getNodeStatus(node);
		List alarmlist = node.getAlarmMessage();
		if (alarmlist != null && alarmlist.size() > 0) {
			for (int k = 0; k < alarmlist.size(); k++) {
				alarmmessage = alarmmessage
						+ alarmlist.get(k).toString();
			}
		}
	}

	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(queryconf.getSupperid()
				+ "");
		if (supper != null)
			suppername = supper.getSu_name() + "("
					+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}  
  
  String flag_1 = (String)request.getAttribute("flag");
  
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<script language="JavaScript" type="text/JavaScript">


var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//添加菜单	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}
	setClass();
}
function setClass(){
	document.getElementById('tftpDetailTitle-0').className='detail-data-title';
	document.getElementById('tftpDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('tftpDetailTitle-0').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
		
}

function show_graph(){
      mainForm.action = "<%=rootPath%>/tftp.do?action=detail";
      mainForm.submit();
} 


</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" id="flag" name="flag" value="<%=flag_1%>">
		<input type="hidden" id="id" name="id" value="<%=myId%>">
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<%=menuTable%>
							</td>	
						</tr>
					</table>
				</td>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-application-detail">
								<table id="container-main-application-detail" class="container-main-application-detail">
									<tr>
										<td>
											<table id="application-detail-content" class="application-detail-content">
												<tr>
													<td>
														<table id="application-detail-content-header" class="application-detail-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="application-detail-content-title">TFTP 详细信息</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="application-detail-content-body" class="application-detail-content-body">
				        									<tr>
				        										<td>
				        										
				        											<table>
										               					<tr>
										     								<td>
	
																					&nbsp;服务名称
																					<select name="id" >
																					<%
																						if(urllist != null && urllist.size()>0){
																							for(int i=0;i<urllist.size();i++){
																								TFTPConfig webconfig = (TFTPConfig)urllist.get(i);
																								if(webconfig.getId() == queryconf.getId()){
																									
																					%>
																					
																					<option value="<%=webconfig.getId()%>" selected="selected"> <%=webconfig.getName()%></option>
																					
																					<%
																							}else{
																					%>
																					<option value="<%=webconfig.getId()%>" > <%=webconfig.getName()%></option>
																					<% 
																							}
																							}
																						}
																					%>
																					</select>
																				<input type="button" onclick="show_graph()" class=button value="查询">&nbsp;&nbsp;&nbsp; <br>
																				
																			</td>
									                        			</tr>
									                        			<tr>
									                        				<td>
									                        					<table cellspacing="10">
									                        						<tr> 
																			 			<td width="60%" align="center"> 
																			 				<table style="BORDER-COLLAPSE: collapse"
																									bordercolor=#ececec cellpadding=0 rules=none
																									width=100% align=center border=1 algin="center">
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;名称:
																										</td>
																										<td width="70%"><%=queryconf.getName()%></td>
																									</tr>
																									<tr>
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;类型:
																										</td>
																										<td width="70%">
																											TFTP服务监视
																										</td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;状态:
																										</td>
																										<td width="70%">
																											<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>" border="0" alt=<%=alarmmessage%>>
																										</td>
																									</tr>
																									<tr>
																										<td width="30%" height="26" align=left nowrap>
																											&nbsp;IP地址:
																										</td>
																										<td width="70%"><%=queryconf.getIpaddress()%></td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td height="29" align="left">
																											&nbsp;测试文件:
																										</td>
																										<td>
																											&nbsp;<%=queryconf.getFilename()%>
																										</td>
																									</tr>
																									<!--  
																									<tr>
																										<td width="30%" height="26" align=left nowrap>
																											&nbsp;用户名:
																										</td>
																										<td width="70%"><%=queryconf.getUsername()%></td>
																									</tr>
																									-->
																									<tr bgcolor="#F1F1F1">
																										<td height="29" align="left">
																											&nbsp;数据采集时间:
																										</td>
																										<td><%=request.getAttribute("lasttime")%></td>
																									</tr>
																									<tr>
																										<td height="29" class=txtGlobal valign=center
																											nowrap>
																											&nbsp;供应商:
																										</td>
																										<td>
																											<%
																												if (supper != null) {
																											%>
																											<a href="#" style="cursor: hand"
																												onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
																											<%
																												}
																											%>
																										</td>
																									</tr>

																								</table>
																			 			
																				          		<%
																				          			String connrate = (String)request.getAttribute("connrate");
																				          			String c = connrate.replace("%", "");
																				          		 %>
																			        		</td>        
																						<td width="40%" align="center">
																		          		         			<div id="flashcontent2">
																											<strong>You need to upgrade your Flash Player</strong>
																										</div>
																										<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Tftp_Ping_Pie.swf?id=<%=queryconf.getId()%>", "Ftp_Ping_Pie", "380", "220", "8", "#ffffff");
																											so.write("flashcontent2");
																										</script>		
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
				        								<table id="application-detail-content-footer" class="application-detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											                  			<tr>
											                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
											                    			<td></td>
											                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
									<table id="detail-data" class="detail-data">
										<tr>
											<td class="detail-data-header">
												<%=tftpDetailTitleTable%>
											</td>
										</tr>
										<tr>
											<td>
												<table class="detail-data-body">
													<tr>
														<td align=center valign=top>
															<br>
															<table cellpadding="0" cellspacing="0" width=48%
																align=center>
																<tr>
																	<td width="48%" align="center">
																				        	<table width="100%" cellspacing="0" cellpadding="0" align="center">
																					            <tr> 
																					         		<td width="100%" align="center"> 
																					         			<div id="flashcontent3">
																											<strong>You need to upgrade your Flash Player</strong>
																										</div>
																										<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Tftp_Ping_Line.swf?id=<%=queryconf.getId()%>", "Ftp_Ping_Line", "400", "250", "8", "#ffffff");
																											so.write("flashcontent3");
																										</script>				
																					                </td>
																								</tr> 
																				          	</table>
																				        </td>
																				        <td>&nbsp;</td> 
																</tr>
															</table>
															<br>
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
				<td width=15% valign=top>
															<jsp:include page="/include/tftptoolbar.jsp">
																<jsp:param value="<%=queryconf.getId() %>" name="id" />
															</jsp:include>
														</td>
			</tr>
		</table>
		
	</form>
	
		<script>			
Ext.onReady(function()
{  


	    Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('数据加载中，请稍后.. ');   
  mainForm.action = "<%=rootPath%>/tftp.do?action=sychronizeData&id=<%=queryconf.getId()%>&flag=<%=flag_1%>";
  mainForm.submit();
 });    
});
</script>

</BODY>
</HTML>