<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>


<%@page import="com.afunms.inform.util.SystemSnap" %>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>

<%@page import="java.util.*"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%
	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");
	int level1 = Integer.parseInt(request.getAttribute("level1") + "");
	int _status = Integer.parseInt(request.getAttribute("status") + "");

	String level0str = "";
	String level1str = "";
	String level2str = "";
	String level3str = "";
	if (level1 == 0) {
		level0str = "selected";
	} else if (level1 == 1) {
		level1str = "selected";
	} else if (level1 == 2) {
		level2str = "selected";
	} else if (level1 == 3) {
		level3str = "selected";
	}
	String status0str = "";
	String status1str = "";
	String status2str = "";
	if (_status == 0) {
		status0str = "selected";
	} else if (_status == 1) {
		status1str = "selected";
	} else if (_status == 2) {
		status2str = "selected";
	}
	
	String myflag = (String) request.getAttribute("flag");
	Integer myId = (Integer) request.getAttribute("id");
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");

	List urllist = (List) request.getAttribute("urllist");
	WebConfig queryconf = (WebConfig) request.getAttribute("initconf");

	String flag_1 = (String) request.getAttribute("flag");

	int status = 0;
	Node node = (Node) PollingEngine.getInstance().getWebByID(
			queryconf.getId());
	String alarmmessage = "";
	if (node != null) {
		status = SystemSnap.getNodeStatus(node);
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
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
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

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
		<script language="JavaScript" type="text/JavaScript">
	function batchAccfiEvent(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("未选中");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_accitevent.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	//batchDoReport();
	function batchDoReport(){
		 var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("未选中");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_doreport.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	function batchEditAlarmLevel(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("未选中");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_editAlarmLevel.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	 function alarmDelete()
  {  
    	var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("未选中");
        	return ;
        }
     mainForm.action = "<%=rootPath%>/web.do?action=alarmdelete";
     mainForm.submit();
  } 

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
}//添加菜单	
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
	document.getElementById('webDetailTitle-2').className='detail-data-title';
	document.getElementById('webDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('webDetailTitle-2').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
		
}

function show_graph(){
      mainForm.action = "<%=rootPath%>/web.do?action=detail";
      mainForm.submit();
} 
function query()
  {  
     mainForm.action = "<%=rootPath%>/web.do?action=alarm";
     mainForm.submit();
  }

</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
					<td class="td-container-main-detail">
						<table id="container-main-detail" class="container-main-detail">
							<tr>
								<td>
									<table id="detail-content" class="detail-content">
										<tr>
											<td>
												<table id="detail-content-header"
													class="detail-content-header">
													<tr>
														<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
														<td class="detail-content-title">
															web 详细信息
														</td>
														<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-content-body" class="detail-content-body">
													<tr>
														<td>
															<table>
																<tr>
																	<td>

																		<table>
																			<tr>
																				<td>

																					&nbsp;&nbsp;服务名称
																					<select name="id">
																						<%
																							if (urllist != null && urllist.size() > 0) {
																								for (int i = 0; i < urllist.size(); i++) {
																									WebConfig webconfig = (WebConfig) urllist.get(i);
																									if (webconfig.getId() == queryconf.getId()) {
																						%>

																						<option value="<%=webconfig.getId()%>"
																							selected="selected">
																							<%=webconfig.getAlias()%></option>


																						<%
																							} else {
																						%>
																						<option value="<%=webconfig.getId()%>">
																							<%=webconfig.getAlias()%></option>
																						<%
																							}
																								}
																							}
																						%>
																					</select>
																					<input type="button" onclick="show_graph()"
																						class=button value="查询">
																					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																					<br>
																				</td>


																			</tr>
																			<tr>
																				<td>
																					<table cellspacing="10">
																						<tr>
																							<td width="60%" align="center" valign=top
																								cellspacing="0" border=0>

																								<table style="BORDER-COLLAPSE: collapse"
																									bordercolor=#ececec cellpadding=0 rules=none
																									width=100% align=center border=1 algin="center">
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;名称:
																										</td>
																										<td width="70%"><%=queryconf.getAlias()%></td>
																									</tr>
																									<tr>
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;类型:
																										</td>
																										<td width="70%">
																											应用服务监视
																										</td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align="left"
																											nowrap>
																											&nbsp;状态:
																										</td>
																										<td width="70%">
																											<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>" border="0" >&nbsp;&nbsp;<%=NodeHelper.getStatusDescr(status)%>
																										</td>
																									</tr>
																									<tr>
																										<td height="29" align="left">
																											&nbsp;访问地址:
																										</td>
																										<td>
																											<a href="<%=queryconf.getStr()%>"
																												target="_blank"><%=queryconf.getStr()%></a>
																										</td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align=left nowrap>
																											&nbsp;IP地址:
																										</td>
																										<td width="70%"><%=queryconf.getIpAddress()%></td>
																									</tr>
																									<tr>
																										<td height="29" align="left">
																											&nbsp;上次轮询时间:
																										</td>
																										<td><%=request.getAttribute("lasttime")%></td>
																									</tr>
																									<tr bgcolor="#F1F1F1">
																										<td width="30%" height="26" align=left>
																											&nbsp;下次轮询时间:
																										</td>
																										<td width="70%"><%=request.getAttribute("nexttime")%></td>
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
																								<!--<table width="100%" cellspacing="0" cellpadding="0" align="center">
																					            	<tr> 
																					         		<td width="100%" align="center"> 
																					         			<div id="flashcontent4">
																											<strong>You need to upgrade your Flash Player</strong>
																										</div>
																										<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Web_Info.swf?alias=<%=queryconf.getAlias()%>&urls=<%=queryconf.getStr()%>&lasttime=<%=request.getAttribute("lasttime")%>&nexttime=<%=request.getAttribute("nexttime")%>", "Web_Info", "400", "250", "8", "#ffffff");
																											so.write("flashcontent4");
																										</script>				
																					                	</td>
																							</tr> 
																				          		</table>-->
																							</td>
																							<td width="40%" align="center">
																								<table width="100%" cellspacing="0"
																									cellpadding="0" align="center">
																									<tr>
																										<td width="100%" align="center">
																											<div id="flashcontent1">
																												<strong>You need to upgrade your
																													Flash Player</strong>
																											</div>
																											<script type="text/javascript">
																											var so = new SWFObject("<%=rootPath%>/flex/Web_Ping_Pie.swf?id=<%=queryconf.getId()%>", "Web_Ping_Pie", "380", "220", "8", "#ffffff");
																											so.write("flashcontent1");
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
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-content-footer"
													class="detail-content-footer">
													<tr>
														<td>
															<table width="100%" border="0" cellspacing="0"
																cellpadding="0">
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
												<%=webDetailTitleTable%>
											</td>
										</tr>
										<tr>
											<td>
												<table class="detail-data-body">

													<tr>
														<td colspan=5 height="28" bgcolor="#ECECEC">


															开始日期
															<input type="text" name="startdate"
																value="<%=startdate%>" size="10">
															<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a> 截止日期
															<input type="text" name="todate" value="<%=todate%>"
																size="10" />
															<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a> 事件等级
															<select name="level1">
																<option value="99">
																	不限
																</option>
																<option value="0" <%=level0str%>>
																	提示信息
																</option>
																<option value="1" <%=level1str%>>
																	普通事件
																</option>
																<option value="2" <%=level2str%>>
																	严重事件
																</option>
																<option value="3" <%=level3str%>>
																	紧急事件
																</option>
															</select>

															处理状态
															<select name="status">
																<option value="99">
																	不限
																</option>
																<option value="0" <%=status0str%>>
																	未处理
																</option>
																<option value="1" <%=status1str%>>
																	正在处理
																</option>
																<option value="2" <%=status2str%>>
																	已处理
																</option>
															</select>
															<input type="button" name="submitss" value="查询"
																onclick="query()"><hr>
														</td>
													</tr>
													<tr align="right" bgcolor="#ECECEC">
														<td><table><tr>
														<td width="75%">&nbsp;</td>
														<td width="15" height=15 >&nbsp;&nbsp;</td>
														<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="接受处理" onclick='batchAccfiEvent();'/></td>
														<td width="15" height=15>&nbsp;&nbsp;</td>
														<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="填写报告" onclick='batchDoReport();'/></td>
														<td width="15" height=15>&nbsp;&nbsp;</td>
														<td  height=15>&nbsp;&nbsp;<input type="button" name="submitss" value="修改等级" onclick="batchEditAlarmLevel();"></td>
														<td width="15" height=15>&nbsp;&nbsp;</td>
														<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="删除警告" onclick='alarmDelete();'/>&nbsp;&nbsp;</td>
																<td width="15" height=15>&nbsp;&nbsp;</td>
														</tr></table></td>
													</tr>
													<tr bgcolor="#ECECEC">
														<td>
															<table cellSpacing="0" cellPadding="0" border=0>

																<tr>
																	<td class="detail-data-body-title">
																		<INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">
																	</td>
																	<td width="10%" class="detail-data-body-title">
																		<strong>事件等级</strong>
																	</td>
																	<td width="40%" class="detail-data-body-title">
																		<strong>事件描述</strong>
																	</td>
																	<td class="detail-data-body-title">
																		<strong>最近告警时间</strong>
																	</td>
																	<td class="detail-data-body-title">
																		<strong>告警次数</strong>
																	</td>
																	<td class="detail-data-body-title">
																		<strong>查看状态</strong>
																	</td>
																	<td class="detail-data-body-title">
																		<strong></strong>
																	</td>
																</tr>
																<%
																	int index = 0;
																	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat(
																			"MM-dd HH:mm");
																	List list = (List) request.getAttribute("list");
																	String lasttime = "";
																	for (int i = 0; i < list.size(); i++) {
																		index++;
																		EventList eventlist = (EventList) list.get(i);
																		Date cc = eventlist.getRecordtime().getTime();
																		Integer eventid = eventlist.getId();
																		String eventlocation = eventlist.getEventlocation();
																		String content = eventlist.getContent();
																		String level = String.valueOf(eventlist.getLevel1());
																		String my_status = String.valueOf(eventlist.getManagesign());
																		lasttime = eventlist.getLasttime();
																		String s = my_status;
																		String act = "处理报告";
																		String bgcolor = "";
																		String levelstr = "";
																		if ("0".equals(level)) {
																			levelstr = "提示信息";
																			bgcolor = "bgcolor='blue'";
																		}
																		if ("1".equals(level)) {
																			levelstr = "普通事件";
																			bgcolor = "bgcolor='yellow'";
																		}
																		if ("2".equals(level)) {
																			levelstr = "严重事件";
																			bgcolor = "bgcolor='orange'";
																		}
																		if ("3".equals(level)) {
																			levelstr = "紧急事件";
																			bgcolor = "bgcolor='red'";
																		}
																		String bgcolorstr="";
																		if ("0".equals(my_status)) {
																			my_status = "未处理";
																			bgcolorstr="#9966FF";
																		}
																		if ("1".equals(my_status)) {
																			my_status = "处理中";
																			bgcolorstr="#3399CC";	
																		}
																		if ("2".equals(my_status)) {
																			my_status = "处理完成";
																			bgcolorstr="#33CC33";
																		}
																		String rptman = eventlist.getReportman();
																		String rtime1 = _sdf.format(cc);
																		if(lasttime.length()>5)lasttime=lasttime.substring(5);
																%>

																<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>

																	<td class="detail-data-body-list"><INPUT type="checkbox" name="checkbox" value="<%=eventlist.getId()%>"><%=i+1%></td>
   	 															    <td class="detail-data-body-list" <%=bgcolor%>><%=levelstr%></td>
																	<td class="detail-data-body-list">
																		<%=content%></td>
																	<td class="detail-data-body-list">
																		<%=lasttime%></td>
																	<td class="detail-data-body-list">
																		<%=rptman%></td>
																	<td class="detail-data-body-list" bgcolor=<%=bgcolorstr%>>
																		<%=my_status%></td>
																	<td class="detail-data-body-list" align="center">
																		<%
																		 if ("0".equals(s)&&"0".equals(level)) {
																	    	 
																					}else if ("0".equals(s)) {
																		%>
																		<input type="button" value="接受处理" class="button"
																			onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
																		<!--<input type ="button" value="接受处理" class="button" onclick="accEvent('<%=eventid%>')">-->
																		<%
																			}
																				if ("1".equals(s)) {
																		%>
																		<input type="button" value="填写报告" class="button"
																			onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
																		<!--<input type ="button" value="填写报告" class="button" onclick="fiReport('<%=eventid%>')">-->
																		<%
																			}
																				if ("2".equals(s)) {
																		%>
																		<input type="button" value="查看报告" class="button"
																			onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
																		<!--<input type ="button" value="查看报告" class="button" onclick="viewReport('<%=eventid%>')">-->
																		<%
																			}
																		%>
																	</td>
																</tr>
																<%
																	}
																%>

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
						<jsp:include page="/include/urltoolbar.jsp">
							<jsp:param value="<%=queryconf.getId()%>" name="id" />
						</jsp:include>
					</td>
				</tr>
			</table>

		</form>
		<script>			
Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
  
  Ext.MessageBox.wait('数据加载中，请稍后.. ');   
  mainForm.action = "<%=rootPath%>/web.do?action=sychronizeData&id=<%=queryconf.getId()%>&flag=<%=flag_1%>&page=alarm";
  mainForm.submit();
 });    
});
</script>
	</BODY>
</HTML>