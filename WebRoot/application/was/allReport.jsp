<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.detail.service.IISInfo.IISInfoService"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.monitor.item.base.MonitorResult"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.application.util.*"%>
<%@page import="org.jdom.Element"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.application.manage.WasManager"%>
<%@page import="com.afunms.application.wasmonitor.*"%>
<html>
<head>
<%
	String runmodel = PollingEngine.getCollectwebflag();
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");

	WasConfig vo = (WasConfig) request.getAttribute("was");
	if (vo == null) {
		vo = new WasConfig();
	}
	HashMap system = null;
	HashMap jvm = null;
	HashMap cache = null;
	HashMap jdbc = null;
	HashMap Session = null;
	HashMap thread = null;
	HashMap thing = null;
	Hashtable wasIpaddressData = null;
	String ip = vo.getIpaddress().replace(".", "_");
	if ("0".equals(runmodel)) {
		//�ɼ�������Ǽ���ģʽ
		Hashtable wasHashtables = (Hashtable) com.afunms.common.util.ShareData
				.getWasdata();
		wasIpaddressData = (Hashtable) wasHashtables.get(vo
				.getIpaddress());
		System.out.println("wasIpaddressData:" + wasIpaddressData);
		if (wasIpaddressData != null) {
			if (vo.getVersion().equalsIgnoreCase("v5")) {
				Hashtable systemHashtable = (Hashtable) wasIpaddressData
						.get("systemDatahst");
				Hashtable cacheHashtable = (Hashtable) wasIpaddressData
						.get("cachehst");
				Hashtable jvmHashtable = (Hashtable) wasIpaddressData
						.get("jvmhst");
				Hashtable jdbcHashtable = (Hashtable) wasIpaddressData
						.get("jdbchst");
				Hashtable servletHashtable = (Hashtable) wasIpaddressData
						.get("servlethst");
				Hashtable transHashtable = (Hashtable) wasIpaddressData
						.get("transhst");
				Hashtable threadHashtable = (Hashtable) wasIpaddressData
						.get("threadhst");
				thread = CommonUtil
						.converHashTableToHashMap(threadHashtable);
				thing = CommonUtil
						.converHashTableToHashMap(transHashtable);
				Session = CommonUtil
						.converHashTableToHashMap(servletHashtable);
				jdbc = CommonUtil
						.converHashTableToHashMap(jdbcHashtable);
				jvm = CommonUtil.converHashTableToHashMap(jvmHashtable);
				cache = CommonUtil
						.converHashTableToHashMap(cacheHashtable);
				system = CommonUtil
						.converHashTableToHashMap(systemHashtable);
			} else {
				Hashtable systemHashtable = (Hashtable) wasIpaddressData
						.get("system7hst");
				Hashtable cacheHashtable = (Hashtable) wasIpaddressData
						.get("extension7hst");
				Hashtable jvmHashtable = (Hashtable) wasIpaddressData
						.get("jvm7hst");
				Hashtable jdbcHashtable = (Hashtable) wasIpaddressData
						.get("jdbc7hst");
				Hashtable servletHashtable = (Hashtable) wasIpaddressData
						.get("servlet7hst");
				Hashtable transHashtable = (Hashtable) wasIpaddressData
						.get("trans7hst");
				Hashtable threadHashtable = (Hashtable) wasIpaddressData
						.get("thread7hst");
				thread = CommonUtil
						.converHashTableToHashMap(threadHashtable);
				thing = CommonUtil
						.converHashTableToHashMap(transHashtable);
				Session = CommonUtil
						.converHashTableToHashMap(servletHashtable);
				jdbc = CommonUtil
						.converHashTableToHashMap(jdbcHashtable);
				jvm = CommonUtil.converHashTableToHashMap(jvmHashtable);
				cache = CommonUtil
						.converHashTableToHashMap(cacheHashtable);
				system = CommonUtil
						.converHashTableToHashMap(systemHashtable);
			}
		}
	} else {
		//�ɼ�����ʷ���ģʽ
		GetWasInfo wasconf = new GetWasInfo();
		try {
			system = wasconf.executeQueryHashMap(vo.getIpaddress(),
					"wassystem");
			Session = wasconf.executeQueryHashMap(vo.getIpaddress(),
					"wassession");
			cache = wasconf.executeQueryHashMap(vo.getIpaddress(),
					"wascache");
			jdbc = wasconf.executeQueryHashMap(vo.getIpaddress(),
					"wasjdbc");
			thread = wasconf.executeQueryHashMap(vo.getIpaddress(),
					"wasthread");
			thing = wasconf.executeQueryHashMap(vo.getIpaddress(),
					"wastrans");
			jvm = wasconf.executeQueryHashMap(vo.getIpaddress(),
					"wasjvm");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			wasconf.close();
		}
	}
	if (Session == null) {
		Session = new HashMap();
	}
	String tmp = vo.getId() + "";
%>
<link
	href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
	rel="stylesheet" type="text/css" />

<title>WAS�ۺϱ���</title>
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript"
	src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<script type="text/javascript"
	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"
	charset="utf-8"></script>
<script type="text/javascript"
	src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script language="JavaScript" type="text/JavaScript">
function ping_word_report()
{
	mainForm.action = "<%=rootPath%>/was.do?action=downloadAllReport&type=all&str=1&id=<%=tmp%>";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/was.do?action=downloadAllReport&type=all&str=0&id=<%=tmp%>";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/was.do?action=downloadAllReport&type=all&str=2&id=<%=tmp%>";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	mainForm.action = "<%=rootPath%>/mq.do?action=showPingReport&id=<%=tmp%>&startdate="+starttime+"&todate="+endtime;
	mainForm.submit();
}
function ping_cancel()
{
window.close();
}
function query(){
  	var startdate = mainForm.startdate.value;
  	var todate = mainForm.todate.value;      
  	var oids ="";
  	var checkbox = document.getElementsByName("checkbox");
 		for (var i=0;i<checkbox.length;i++){
 			if(checkbox[i].checked==true){
 				if (oids==""){
 					oids=checkbox[i].value;
 				}else{
 					oids=oids+","+checkbox[i].value;
 				}
 			}
 		}
 	if(oids==null||oids==""){
 		alert("������ѡ��һ���豸");
 		return;
 	}
 	window.open ("<%=rootPath%>/hostreport.do?action=downloadmultihostreport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
}

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
  	mainForm.submit();
}
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostcpu";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});
</script>



<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����

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
//��Ӳ˵�	
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
	document.getElementById('hostReportTitle-5').className='detail-data-title';
	document.getElementById('hostReportTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostReportTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
	}
</script>
<!-- snow add 2010-5-28 -->
<style>
<!--
body {
	background-image:
		url(${pageContext.request.contextPath}/resource/image/bg.jpg);
	TEXT-ALIGN: center;
}
-->
</style>
</head>
<body id="body" class="body" onload="init();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize
		scrolling=no src="<%=rootPath%>/include/calendar.htm"
		style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden id="ipaddress" name="ipaddress"
			value=<%=request.getParameter("ipaddress")%>>
		<table id="container-main" class="container-main">
			<tr>
				<td>
					<table id="container-main-win" class="container-main-win">
						<tr>
							<td>
								<table id="win-content" class="win-content">
									<tr>
										<td>
											<table id="win-content-header" class="win-content-header">
												<tr>
													<td align="left" width="5"><img
														src="<%=rootPath%>/common/images/right_t_01.jpg" width="5"
														height="29" />
													</td>
													<td class="win-content-title" style="align:center">&nbsp;WAS
														(<%=vo.getIpaddress()%>)�ۺϱ���</td>
													<td align="right"><img
														src="<%=rootPath%>/common/images/right_t_03.jpg" width="5"
														height="29" />
													</td>
												</tr>

											</table></td>
									</tr>
									<tr>
										<td>
											<table id="win-content-body" class="win-content-body">
												<tr>
													<td>
														<table bgcolor="#ECECEC">
															<tr align="left" valign="center">
																<td height="28" align="left" width=60%></td>
																<td height="28" align="left"><a
																	href="javascript:ping_word_report()"><img
																		name="selDay1" alt='����word' style="CURSOR:hand"
																		src="<%=rootPath%>/resource/image/export_word.gif"
																		width=18 border="0">����WORLD</a></td>
																<td height="28" align="left"><a
																	href="javascript:ping_excel_report()"><img
																		name="selDay1" alt='����EXCEL' style="CURSOR:hand"
																		src="<%=rootPath%>/resource/image/export_excel.gif"
																		width=18 border="0">����EXCEL</a></td>
																<td height="28" align="left">&nbsp; <a
																	href="javascript:ping_pdf_report()"><img
																		name="selDay1" alt='����word' style="CURSOR: hand"
																		src="<%=rootPath%>/resource/image/export_pdf.gif"
																		width=18 border="0">����PDF</a></td>
															</tr>
														</table></td>
												</tr>
												<tr align="left" valign="center">
													<td height="20" align="left" border="0"><input
														type=hidden name="eventid">
														<div id="loading">
															<div class="loading-indicator">
																<img
																	src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
																	width="32" height="32" style="margin-right: 8px;"
																	align="middle" />Loading...
															</div>
														</div></td>
												</tr>

												<tr align="right">
													<td width=820px align=center><table>

															<tr align="right">

																<td width=10px></td>

																<td align="right"><table border=2 rules="cols">
																		<tr>
																			<td>
																				<table border=1>
																					<tr>
																						<td></td>
																					</tr>
																				</table></td>
																		</tr>
																		<tr>
																			<td width=100%>
																				<table border=0 class="application-detail-data-body">
																					<tr>
																						<td>
																							<table width="100%" border=0 align="center">
																								<tr>
																									<td height=29>System��Ϣ</td>
																								</tr>
																								<tr>
																									<td>
																										<table class="application-detail-data-body">
																											<tr>
																												<td>
																													<table width="100%" align="center">
																														<tr>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;�����ڴ�Ŀ��գ�KB��:</td>
																															<%
																																if (system != null) {
																																	if (system.get("freeMemory") != null) {
																															%>
																															<td width="35%"><%=system.get("freeMemory")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																}
																															%>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;�Է���������������CPUƽ��ʹ����:</td>
																															<%
																																if (system != null) {
																																	if (system.get("cpuUsageSinceServerStarted") != null) {
																															%>
																															<td width="35%"><%=system.get("cpuUsageSinceServerStarted")%>%
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																}
																															%>
																														</tr>
																														<tr>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;���ϴβ�ѯ������ƽ��CPUʹ����:</td>
																															<%
																																if (system != null) {
																																	if (system.get("cpuUsageSinceLastMeasurement") != null) {
																															%>
																															<td width="35%"><%=system.get("cpuUsageSinceLastMeasurement")%>%
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																}
																															%>
																															<td></td>
																															<td></td>
																														</tr>
																													</table></td>
																											</tr>
																										</table></td>
																								</tr>

																								<tr>
																									<td>
																										<table border=1>
																											<tr>
																												<td></td>
																											</tr>
																										</table></td>
																								</tr>


																							</table></td>
																					</tr>
																					<tr>
																						<td>
																							<table width="96%" align="center">
																								<tr>
																									<td height=29>JVM��Ϣ</td>
																								</tr>
																								<tr>
																									<td>
																										<table class="application-detail-data-body">
																											<tr>
																												<td>
																													<table width="96%" align="center">
																														<tr>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;Java���������ʱ���е��ڴ棨KB��:</td>
																															<%
																																if (jvm != null) {
																																	if (jvm.get("freeMemory") != null) {
																															%>
																															<td width="35%"><%=jvm.get("freeMemory")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;Java���������ʱ���ڴ棨KB��:</td>
																															<%
																																if (jvm != null) {
																																	if (jvm.get("heapSize") != null) {
																															%>
																															<td width="35%"><%=jvm.get("heapSize")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																														</tr>
																														<tr>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;�Ѿ����е�ʱ�������룩:</td>
																															<%
																																if (jvm != null) {
																																	if (jvm.get("upTime") != null) {
																															%>
																															<td width="35%"><%=jvm.get("upTime")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;Java���������ʱʹ�õ��ڴ棨KB��:</td>
																															<%
																																if (jvm != null) {
																																	if (jvm.get("usedMemory") != null) {
																															%>
																															<td width="35%"><%=jvm.get("usedMemory")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																														</tr>
																														<tr>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;Java������ڴ�������:</td>
																															<%
																																if (jvm != null) {
																																	if (jvm.get("memPer") != null) {
																															%>
																															<td width="35%"><%=jvm.get("memPer")%>%
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal></td>
																															<td width="35%"></td>
																														</tr>

																													</table></td>
																											</tr>
																										</table></td>
																								</tr>

																								<tr>
																									<td>
																										<table border=1>
																											<tr>
																												<td></td>
																											</tr>
																										</table></td>
																								</tr>

																							</table></td>
																					</tr>
																					<tr>
																						<td>
																							<table width="96%" align="center">
																								<tr>
																									<td height=29>������Ϣ</td>
																								</tr>
																								<tr>
																									<td>
																										<table class="application-detail-data-body">
																											<tr>
																												<td>
																													<table width="96%" align="center">
																														<tr>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;�ڴ��еĸ��ٻ�����Ŀ��ǰ��:</td>
																															<%
																																if (cache != null) {
																																	if (cache.get("inMemoryCacheCount") != null) {
																															%>
																															<td width="35%"><%=cache.get("inMemoryCacheCount")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>
																																&nbsp;���ٻ�����Ŀ�����:</td>
																															<%
																																if (cache != null) {
																																	if (cache.get("maxInMemoryCacheCount") != null) {
																															%>
																															<td width="35%"><%=cache.get("maxInMemoryCacheCount")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																														</tr>
																														<tr>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal>&nbsp;��ʱ��:</td>
																															<%
																																if (cache != null) {
																																	if (cache.get("timeoutInvalidationCount") != null) {
																															%>
																															<td width="35%"><%=cache.get("timeoutInvalidationCount")%>
																															</td>
																															<%
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																																} else {
																															%>
																															<td width="35%"></td>
																															<%
																																}
																															%>
																															<td width="15%" height="26" align="left"
																																nowrap class=txtGlobal></td>

																															<td width="35%"></td>
																														</tr>

																													</table></td>
																											</tr>
																										</table></td>
																								</tr>

																								<tr>
																									<td>
																										<table border=1>
																											<tr>
																												<td></td>
																											</tr>
																										</table></td>
																								</tr>

																							</table></td>
																					</tr>
																					<tr>
																						<td>
																					<tr>
																						<td height=29>Session��Ϣ</td>
																					</tr>
																					<tr>
																					<tr>
																						<td>
																							<table class="application-detail-data-body">
																								<tr>
																									<td>
																										<table width="96%" align="center">
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��ǰ���ĻỰ����:</td>
																												<%
																													if (Session.get("activeCount") != null) {
																												%>
																												<td width="35%"><%=Session.get("activeCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>&nbsp;�����ĻỰ��:</td>
																												<%
																													if (Session.get("createCount") != null) {
																												%>
																												<td width="35%"><%=Session.get("createCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>&nbsp;ʧЧ�ĻỰ��:</td>
																												<%
																													if (Session.get("invalidateCount") != null) {
																												%>
																												<td width="35%"><%=Session.get("invalidateCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;ƽ���Ự���ڣ����룩:</td>
																												<%
																													if (Session.get("lifeTime") != null) {
																												%>
																												<td width="35%"><%=Session.get("lifeTime")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��������ӵ�����:</td>
																												<%
																													if (Session.get("liveCount") != null) {
																												%>
																												<td width="35%"><%=Session.get("liveCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��ʱʧЧ�ĻỰ��:</td>
																												<%
																													if (Session.get("timeoutInvalidationCount") != null) {
																												%>
																												<td width="35%"><%=Session.get("timeoutInvalidationCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																										</table></td>
																								</tr>
																							</table></td>
																					</tr>


																					<tr>
																						<td>
																							<table border=1>
																								<tr>
																									<td></td>
																								</tr>
																							</table></td>
																					</tr>


																					<tr>
																						<td height=29>JDBC��Ϣ</td>
																					<tr>
																					<tr>
																						<td>
																							<table class="application-detail-data-body">
																								<tr>
																									<td>
																										<table width="96%" align="center">
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;���еĿ���������:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("freePoolSize") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("freePoolSize")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;ʹ�����ӵ�ƽ��ʱ�䣨���룩:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("useTime") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("useTime")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;���ٻ��������������������:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("prepStmtCacheDiscardCount") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("prepStmtCacheDiscardCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�ȴ����ӵ�ƽ�������߳���:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("waitingThreadCount") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("waitingThreadCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��������ӵ�����:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("allocateCount") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("allocateCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;���е����ӳ�ʱ��:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("faultCount") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("faultCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>

																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��������֮ǰ��ƽ���ȴ�ʱ�䣨���룩:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("waitTime") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("waitTime")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>

																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>&nbsp;�������ӵ�����:
																												</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("createCount") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("createCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;����JDBCƽ������ʱ�䣨���룩:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("jdbcTime") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("jdbcTime")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>&nbsp;�ص�ƽ��ʹ����:
																												</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("percentUsed") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("percentUsed")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>&nbsp;���ӳصĴ�С:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("poolSize") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("poolSize")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�ѹرյ����ӵ�����:</td>
																												<%
																													if (jdbc != null) {
																														if (jdbc.get("closeCount") != null) {
																												%>
																												<td width="35%"><%=jdbc.get("closeCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																										</table></td>
																								</tr>
																							</table></td>
																					</tr>

																					<tr>
																						<td>
																							<table border=1>
																								<tr>
																									<td></td>
																								</tr>
																							</table></td>
																					</tr>

																					<tr>
																						<td height=29>Thread��Ϣ</td>
																					<tr>
																					<tr>
																						<td>
																							<table class="application-detail-data-body">
																								<tr>
																									<td>
																										<table width="96%" align="center">
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��������߳���:</td>
																												<%
																													if (thread != null) {
																														if (thread.get("activeCount") != null) {
																												%>
																												<td width="35%"><%=thread.get("activeCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>&nbsp;�����̵߳�����:
																												</td>
																												<%
																													if (thread != null) {
																														if (thread.get("createCount") != null) {
																												%>
																												<td width="35%"><%=thread.get("createCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>&nbsp;�����̵߳�����:
																												</td>
																												<%
																													if (thread != null) {
																														if (thread.get("destroyCount") != null) {
																												%>
																												<td width="35%"><%=thread.get("destroyCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�����̵߳�ƽ����:</td>
																												<%
																													if (thread != null) {
																														if (thread.get("poolSize") != null) {
																												%>
																												<td width="35%"><%=thread.get("poolSize")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>

																										</table></td>
																								</tr>
																							</table></td>
																					</tr>

																					<tr>
																						<td>
																							<table border=1>
																								<tr>
																									<td></td>
																								</tr>
																							</table></td>
																					</tr>

																					<tr>
																						<td height=29>������Ϣ</td>
																					<tr>
																					<tr>
																						<td>
																							<table class="application-detail-data-body">
																								<tr>
																									<td>
																										<table width="96%" align="center">
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�������ȫ��������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("activeCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("activeCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;���ύ��ȫ��������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("committedCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("committedCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�ڷ������Ͽ�ʼ��ȫ��������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("globalBegunCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("globalBegunCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��ʱ��ȫ��������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("globalTimeoutCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("globalTimeoutCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;ȫ������ƽ������ʱ�䣨���룩:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("globalTranTime") != null) {
																												%>
																												<td width="35%"><%=thing.get("globalTranTime")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;������ı���������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("localActiveCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("localActiveCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>

																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�������Ͽ�ʼ�ı���������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("localBegunCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("localBegunCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>

																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�ع��ı���������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("localRolledbackCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("localRolledbackCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;��ʱ�ı���������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("localTimeoutCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("localTimeoutCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;���������ƽ������ʱ�䣨���룩:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("localTranTime") != null) {
																												%>
																												<td width="35%"><%=thing.get("localTranTime")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																											</tr>
																											<tr>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal>
																													&nbsp;�ع���ȫ��������:</td>
																												<%
																													if (thing != null) {
																														if (thing.get("rolledbackCount") != null) {
																												%>
																												<td width="35%"><%=thing.get("rolledbackCount")%>
																												</td>
																												<%
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																													} else {
																												%>
																												<td width="35%"></td>
																												<%
																													}
																												%>
																												<td width="15%" height="26" align="left"
																													nowrap class=txtGlobal></td>

																												<td width="35%"></td>
																											</tr>

																										</table></td>
																								</tr>
																							</table></td>
																					</tr>
																					<tr>
																						<td>
																							<table border=1>
																								<tr>
																									<td></td>
																								</tr>
																							</table></td>
																					</tr>
																				</table>
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table> <br>
													<br>
														<div align=center>
															<input type=button value="�رմ���" onclick="window.close()">
														</div> <br></td>
												</tr>
											</table></td>
									</tr>
								</table></td>
						</tr>
					</table></td>
			</tr>
		</table>
		</td>
		</tr>
		</table>
		<br>
	</form>
</body>
</html>