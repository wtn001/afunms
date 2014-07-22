<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.polling.node.Resin"%>
<%@page import="com.afunms.application.manage.ResinManager"%>
<%@page import="com.afunms.application.resinmonitor.TCPPort"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%
	String rootPath = request.getContextPath();
	String tmp = request.getParameter("id");
	String menuTable = (String) request.getAttribute("menuTable");

	Hashtable data_ht = new Hashtable();
	ResinManager tm = new ResinManager();
	List<TCPPort> list= (List)request.getAttribute("tcpportList");
	Resin resin = (Resin) PollingEngine.getInstance().getResinByID(Integer.parseInt(tmp));
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
			charset="gb2312" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="gb2312"></script>

		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script type="text/javascript">
function setClass(){
	document.getElementById('resDetailTitle-2').className='detail-data-title';
	document.getElementById('resDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('resDetailTitle-2').onmouseout="this.className='detail-data-title'";			
	}
</script>
		<script type="text/javascript">
function event(){
	mainForm.action = "<%=rootPath%>/resin.do?action=event"+"&flag=<%=flag%>";
    mainForm.submit();
}
function alarm(){
	var id=<%=request.getAttribute("id")%>
	mainForm.action = "<%=rootPath%>/resin.do?action=alarm&id="+id+"&flag=<%=flag%>";
    mainForm.submit();
}
function detail(){
	var id=<%=request.getAttribute("id")%>
	mainForm.action = "<%=rootPath%>/resin.do?action=detail&id="+id+"&flag=<%=flag%>";
    mainForm.submit();
}
function tcpport(){
	var id=<%=request.getAttribute("id")%>
	mainForm.action = "<%=rootPath%>/resin.do?action=tcpport&id="+id+"&flag=<%=flag%>";
    mainForm.submit();
}
function system(){
	var id=<%=request.getAttribute("id")%>
	mainForm.action = "<%=rootPath%>/resin.do?action=system&id="+id+"&flag=<%=flag%>";
    mainForm.submit();
}
function pool(){
	var id=<%=request.getAttribute("id")%>
	mainForm.action = "<%=rootPath%>/resin.do?action=connPool&id="+id+"&flag=<%=flag%>";
    mainForm.submit();
}
</script>

	</head>
	<body id="body" class="body" onload="setClass();">
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="orderflag">
			<input type=hidden name="id">

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
								<td class="td-container-main-service-detail">
								<table id="container-main-service-detail" class="container-main-service-detail">
										<tr>
											<td>
												<jsp:include
													page="/topology/includejsp/middleware_resin.jsp">
													<jsp:param name="tmp" value="<%=tmp%>" />
													<jsp:param name="avgpingcon" value="0" />
													<jsp:param name="avgjvmcon" value="0" />
												</jsp:include>
											</td>
										</tr>

										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=resDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
																<tr>
																	<td>
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="1">
																			<tr bgcolor="DEEBF7">
																				<td>
																					<table width="100%" border="0" cellpadding="3"
																						cellspacing="1" bgcolor="#FFFFFF">
																						<tr align="center" bgcolor="#ECECEC" height="28">
																							<td width="15%" align="center">
																								����
																							</td>
																							<td width="10%" align="center">
																								״̬
																							</td>
																							<td width="10%" align="center">
																								���̻��
																							</td>
																							<td width="10%" align="center">
																								���̿�����
																							</td>
																							<td width="10%" align="center">
																								��������
																							</td>
																							<td width="10%" align="center">
																								��������
																							</td>
																							<td width="10%" align="center">
																								���߳�
																							</td>
																							<td width="10%" align="center">
																								Select
																							</td>
																							<td width="10%" align="center">
																								Comet
																							</td>
																						</tr>
                                                                                       <% if(list!=null){
                                                                                           for(int i=0;i<list.size();i++){
                                                                                             TCPPort tcpport=list.get(i);
                                                                                            %>
																						<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																							<td class="detail-data-body-list" align=center><%=tcpport.getListener() %></td>
																							<td class="detail-data-body-list" align=center><%=tcpport.getStatus() %></td>
																							<td class="detail-data-body-list" align=center><%=tcpport.getThreadTotal() %></td>
																							<td class="detail-data-body-list" align=center><%=tcpport.getThreadActive() %></td>
																							<td class="detail-data-body-list" align=center><%=tcpport.getThreadIdle() %></td>
																							<td class="detail-data-body-list" align=center><%=tcpport.getKeepaliveTotal() %></td>
																							<td class="detail-data-body-list" align=center><%=tcpport.getKeepaliveThread() %></td>
																						    <td class="detail-data-body-list" align=center><%=tcpport.getKeepaliveSelect() %></td>
																							<td class="detail-data-body-list" align=center><%=tcpport.getKeepaliveComet() %></td>
																						</tr>
																						
                                                                                        <%}} %>
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
						</table>
					</td>
					<td width="10px"></td>
					<td valign=top width=200px>
									<jsp:include page="/include/resintoolbar.jsp">
										<jsp:param value="<%=resin.getId()%>" name="id" />
										<jsp:param value="<%=resin.getIpAddress()%>" name="ip" />
										<jsp:param value="resin" name="subtype" />
										<jsp:param value="middleware" name="type" />
										
									</jsp:include>
				  </td>
				</tr>
			</table>

		</form>
		<script type="text/javascript">

</script>
	</BODY>
</HTML>