<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc" %>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.polling.node.Tomcat"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.TomcatManager"%>
<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.polling.node.Tomcat"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.TomcatManager"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@ page import="com.afunms.detail.service.tomcatInfo.TomcatInfoService"%>
<%@page import="com.afunms.initialize.*"%>

<html>
<head>
<%
  String rootPath = request.getContextPath();
   String id = (String)request.getAttribute("id");
  String Ping = (String)request.getAttribute("Ping");
  String pingmax = (String)request.getAttribute("pingmax");
  String avgpingcon = (String)request.getAttribute("avgpingcon");
  String newip = (String)request.getAttribute("newip");
  String ipaddress = (String)request.getAttribute("ipaddress");
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
String type = (String)request.getAttribute("type");
Tomcat tomcat = (Tomcat) PollingEngine.getInstance().getTomcatByID(
			Integer.parseInt(id));
%>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<title>Tomcat���ñ���</title>
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script language="JavaScript" type="text/JavaScript">
function ping_word_report()
{
	mainForm.action = "<%=rootPath%>/tomcat.do?action=downloadReport&ipaddress=<%=ipaddress%>&str=1&id=<%=id%>";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/tomcat.do?action=downloadReport&ipaddress=<%=ipaddress%>&str=0&id=<%=id%>";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/tomcat.do?action=downloadReport&ipaddress=<%=ipaddress%>&str=2&id=<%=id%>";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	mainForm.action = "<%=rootPath%>/tomcat.do?action=showPingReport&ipaddress=<%=ipaddress%>&id=<%=id%>&startdate="+starttime+"&todate="+endtime;
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
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
</head>
<body id="body" class="body" onload="init();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form id="mainForm" method="post" name="mainForm">
<input type=hidden id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
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
							                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
							                	<td class="win-content-title" style="align:center">&nbsp;tomcat���ñ���</td>
							                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
											</tr>
									    
					       				</table>
				       				</td>
				       			</tr>
						       	<tr>
						       		<td>
						       			<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
														<td height="28" align="left" width=60%>
														</td>
														<td height="28" align="left">
															<a href="javascript:ping_word_report()"><img name="selDay1" alt='����word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">����WORLD</a>
														</td>
														<td height="28" align="left">
															<a href="javascript:ping_excel_report()"><img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL</a>
														</td>
														<td height="28" align="left">&nbsp;
															<a href="javascript:ping_pdf_report()"><img name="selDay1" alt='����word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">����PDF</a>
														</td>
														</tr>  
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" ></td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">
													
													<input type=hidden name="eventid">
													<div id="loading">
													<div class="loading-indicator">
														<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
													</div>
													<table border="1" width="90%">
														<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td width="30%" height="29" align="left" nowrap
												class=txtGlobal>
												&nbsp;����:
											</td>
											<td width="70%"><%=tomcat.getAlias()%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left" nowrap>
												&nbsp;״̬:
											</td>
											<td>
												<img
													src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(tomcat
									.getStatus())%>">
												&nbsp;<%=NodeHelper.getStatusDescr(tomcat.getStatus())%>
											</td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;IP��ַ:
											</td>
											<td><%=tomcat.getIpAddress()%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal align="left" nowrap>
												&nbsp;�˿�:
											</td>
											<td><%=tomcat.getPort()%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;Tomcat�汾:
											</td>
											<td><%=tomcat.getVersion()%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;JVM�汾:
											</td>
											<td><%=tomcat.getJvmversion()%></td>
										</tr>
										<tr>
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;JVM��Ӧ��:
											</td>
											<td><%=tomcat.getJvmvender()%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" align=left nowrap class=txtGlobal>
												&nbsp;����������ϵͳ:
											</td>
											<td><%=tomcat.getOs()%></td>
										</tr>
										<tr>
											<td height="29" nowrap class=txtGlobal>
												&nbsp;����ϵͳ�汾:
											</td>
											<td><%=tomcat.getOsversion()%></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="29" class=txtGlobal nowrap>
												&nbsp;���¸澯��Ϣ:
											</td>
											<td><%=tomcat.getLastAlarm()%></td>
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
       			</table>
			
            		<div align=center>
            			<input type=button value="�رմ���" onclick="window.close()">
            		</div>  
					<br>
</form>  
</body>
</html>