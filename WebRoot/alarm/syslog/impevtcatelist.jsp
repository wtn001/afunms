<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.base.BaseVo"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@ page import="com.afunms.event.model.*"%>
<%@ page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.event.manage.NetSyslogManager" %>
<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");

	JspPage jp = (JspPage) request.getAttribute("page");

	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");
	String ipaddress = (String) request.getAttribute("ipaddress");
	if (ipaddress == null)ipaddress = "";
//	String eventid = (String) request.getAttribute("eventid");
//	if (eventid == null)eventid = "";
	String priority = (String) request.getAttribute("priority");
	String processname = (String) request.getAttribute("processname");
	if (processname == null)processname = "";
	String message = (String) request.getAttribute("message");
	if (message == null)message = "";
	String busi = (String) request.getAttribute("busi");//����ҵ�����ͣ�������û��˻����� ��busi=userAccout������busiΪnull
	if (busi == null)busi = "";
	String tabTitle = "";
	String streventname = (String)request.getAttribute("streventname");
	if (streventname == null)streventname = "";
	else if("userLoginSuccess".equals(streventname)){
		tabTitle = "��¼�ɹ����¼�";
	}else if("userLoginFailure".equals(streventname)){
		tabTitle = "��¼ʧ�ܵ��¼�";
	}else if("userLogoutSuccess".equals(streventname)){
		tabTitle = "�ǳ��ɹ����¼�";
	}else if("nwDevUserLoginSuccess".equals(streventname)){
		tabTitle = "�����豸��¼�ɹ����¼�";
	}else if("nwDevUserLoginFailure".equals(streventname)){
		tabTitle = "�����豸��¼ʧ�ܵ��¼�";
	}else if("nwDevUserLogoutSuccess".equals(streventname)){
		tabTitle = "�����豸�ǳ��ɹ����¼�";
	}else if("qsUserChangePassSuccess".equals(streventname)){
		tabTitle = "�����޸ĳɹ����û�";
	}else if("qsUserChangePassFailure".equals(streventname)){
		tabTitle = "�����޸�ʧ�ܵ��û�";
	}else if("qsUserAccountDisabled".equals(streventname)){
		tabTitle = "�ʺű�ɾ���ͽ��õ���ϸ��Ϣ";
	}else if("qsUserModifyLog".equals(streventname)){
		tabTitle = "����������־���û��б���Ϣ";
	}
	String username = (String)request.getAttribute("username");
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>

<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script src="<%=rootPath%>/resource/js/dtree.js"
			type="text/javascript"></script>
		<script src="<%=rootPath%>/resource/js/prototype.js"
			type="text/javascript"></script>
		<link href="<%=rootPath%>/include/dtree.css" type="text/css"
			rel="stylesheet" />

		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/netsyslog.do?action=delete";
  var listAction = "<%=rootPath%>/netsyslog.do?action=syslogimpevtcatelist";
  
	function CreateWindow(urlstr){
		msgWindow=window.open(urlstr,"protypeWindow","toolbar=no,width=500,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
	}
	function query(){
   		mainForm.action = "<%=rootPath%>/netsyslog.do?action=syslogimpevtcatelist&jp=1";
  		mainForm.submit();
  	}
</script>
	</head>
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form method="post" name="mainForm">
			<!-- <input type=hidden name="eventid" value="<%//=eventid%>"> -->
			<input type=hidden name="streventname" value="<%=streventname%>">
			<input type=hidden name="username" value="<%=username%>">
			<table border="0" id="table1" cellpadding="0" cellspacing="0"
				width=100%>
				<tr>
					<td width="200" valign=top align=center>
						<%=menuTable%>
					</td>
					<td align="center" valign=top>
						<table width="98%" cellpadding="0" cellspacing="0" algin="center">
							<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left">
												<img src="<%=rootPath%>/common/images/right_t_01.jpg"
													width="5" height="29" />
											</td>
											<td class="layout_title">
												<b>�澯 >> SYSLOG���� >> ���STSLOG >> <%
													if (null == tabTitle || "null".equalsIgnoreCase(tabTitle)) {
												%>ȫ��<%
													} else
														out.print(tabTitle);
												%>
												</b>
											</td>
											<td align="right">
												<img src="<%=rootPath%>/common/images/right_t_03.jpg"
													width="5" height="29" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="100%" align='left'>
												��ʼ����
												<input type="text" name="startdate" value="<%=startdate%>"
													size="10">
												<a onclick="event.cancelBubble=true;"
													href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
													<img id=imageCalendar1 align=absmiddle width=34 height=21
														src="<%=rootPath%>/include/calendar/button.gif" border=0>
												</a>
												��ֹ����
												<input type="text" name="todate" value="<%=todate%>"
													size="10" />
												<a onclick="event.cancelBubble=true;"
													href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
													<img id=imageCalendar2 align=absmiddle width=34 height=21
														src="<%=rootPath%>/include/calendar/button.gif" border=0>
												</a>
												��Դ<input type="text" name="processname" value="<%=processname%>">
												��Ϣ<input type="text" name="message" value="<%=message%>">
												IP<input type="text" name="ipaddress" value="<%=ipaddress%>">
												<input type="button" name="submitss" value="��ѯ" onclick="query()">
											</td>
										</tr>
									</table>
								</td>
							</tr>
							
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="100%" align='left'>
												<!-- <input name="ipaddress" value="<%=ipaddress%>" type="hidden" /> -->
												<input name="priority" value="<%=priority%>" type="hidden" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="80%" align="center">
												<jsp:include page="../../common/page.jsp">
													<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
													<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
												</jsp:include>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td align=right bgcolor="#ECECEC">
									<a
										href="<%=rootPath%>/netsyslog.do?action=exportEvtCatelist&ipaddress=<%=ipaddress%>"
										target="_blank"><img name="selDay1" alt='����EXCEL'
											style="CURSOR: hand"
											src="<%=rootPath%>/resource/image/export_excel.gif" width=18
											border="0">����EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
									<a
										href="<%=rootPath%>/netsyslog.do?action=exportImpevtCatelistall&ipaddress=<%=ipaddress%>&username=<%=username%>&priority=<%=priority%>&startdate=<%=startdate %>&todate=<%=todate %>&processname=<%=processname %>&message=<%=message %>&streventname=<%=streventname %>"
										target="_blank"><img name="selDay1" alt='����EXCEL'
											style="CURSOR: hand"
											src="<%=rootPath%>/resource/image/export_excel.gif" width=18
											border="0">����ȫ��EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
										<tr align="center" height=28 class="microsoftLook0">
											<td>
												<strong>���</strong>
											</td>
											<td width="10%">
												<strong>�ȼ�</strong>
											</td>
											<td width="15%">
												<strong>�豸��</strong>
											</td>
											<td width="40%">
												<strong>����</strong>
											</td>
											<td>
												<strong>����ʱ��</strong>
											</td>
											<td>
												<strong>��ϸ��Ϣ</strong>
											</td>

										</tr>
										<%
											NetSyslog syslog = null;
											int startRow = jp.getStartRow();
											session.setAttribute("startRow", startRow);
											session.setAttribute("list", list);
											java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat(
													"MM-dd HH:mm");
											String priorityName = "", hostname = "", strIpaddress = "", strMessage = "";
											Long id = 0L;
											Date cc = null;
											for (int i = 0; i < list.size(); i++) {
												syslog = (NetSyslog) list.get(i);
												if(null != syslog){
													priorityName = syslog.getPriorityName(); 
													hostname = syslog.getHostname();
													strIpaddress = syslog.getIpaddress();
													strMessage = syslog.getMessage();
													id = syslog.getId();
													cc = syslog.getRecordtime().getTime();
												}
												String rtime1 = _sdf.format(cc);
										%>

										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25>

											<td>
												&nbsp;<%=startRow + i%></td>
											<td><%=priorityName %>&nbsp;
											</td>
											<td><%=hostname %>(<%=strIpaddress%>)&nbsp;
											</td>
											<td><%=strMessage %></td>
											<td><%=rtime1%></td>
											<td>
												<input type="button" value="��ϸ��Ϣ" class="button"
													onclick="javascript:return CreateWindow('<%=rootPath%>/netsyslog.do?action=netsyslogdetail&id=<%=id %>&ipaddress=<%=strIpaddress %>&busi=<%=busi %>')">
											</td>
										</tr>
										<%
											}
										%>

									</table>
								</td>
							</tr>
							<tr>
								<td background="<%=rootPath%>/common/images/right_b_02.jpg">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_01.jpg"
													width="5" height="11" />
											</td>
											<td></td>
											<td align="right" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_03.jpg"
													width="5" height="11" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>
