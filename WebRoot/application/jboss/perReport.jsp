<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc" %>

<%@page import="com.afunms.detail.service.IISInfo.IISInfoService"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*" %>
<%@page import="com.afunms.polling.om.*" %>
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
<html>
<head>
<%
   String runmodel = PollingEngine.getCollectwebflag(); 
   String rootPath = request.getContextPath(); 
   String menuTable = (String)request.getAttribute("menuTable");
   com.afunms.application.model.JBossConfig jBossConfig = (com.afunms.application.model.JBossConfig)request.getAttribute("jBossConfig");
   String tmp = jBossConfig.getId()+"";
    Double pingAvg = Double.valueOf((String) request.getAttribute("pingAvg"));
    Integer status = (Integer) request.getAttribute("status");
    String version = (String) request.getAttribute("version");
    String date = (String) request.getAttribute("date");
    String versionname = (String) request.getAttribute("versionname");
    String builton = (String) request.getAttribute("builton");
    String startdate = (String) request.getAttribute("startdate");
    String host = (String) request.getAttribute("host");
    String baselocation = (String) request.getAttribute("baselocation");
    String baselocationlocal = (String) request.getAttribute("baselocationlocal");
    String runconfig = (String) request.getAttribute("runconfig");
    String threads = (String) request.getAttribute("threads");
    String os = (String) request.getAttribute("os");
    String jvmversion = (String) request.getAttribute("jvmversion");
    String jvmname = (String) request.getAttribute("jvmname");

    String ajp = (String) request.getAttribute("ajp");
    String ajp_maxthreads = (String) request.getAttribute("ajp_maxthreads");
    String ajp_thrcount = (String) request.getAttribute("ajp_thrcount");
    String ajp_thrbusy = (String) request.getAttribute("ajp_thrbusy");
    String ajp_maxtime = (String) request.getAttribute("ajp_maxtime");
    String ajp_processtime = (String) request.getAttribute("ajp_processtime");
    String ajp_requestcount = (String) request.getAttribute("ajp_requestcount");
    String ajp_errorcount = (String) request.getAttribute("ajp_errorcount");
    String ajp_bytereceived = (String) request.getAttribute("ajp_bytereceived");
    String ajp_bytessent = (String) request.getAttribute("ajp_bytessent");
%>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<title>JBOSS���ܱ���</title>
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
	mainForm.action = "<%=rootPath%>/jboss.do?action=downloadReport&flag=per&str=1&id=<%=tmp%>";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/jboss.do?action=downloadReport&flag=per&str=0&id=<%=tmp%>";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/jboss.do?action=downloadReport&flag=per&str=2&id=<%=tmp%>";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	mainForm.action = "<%=rootPath%>/tomcat.do?action=showPingReport&id=<%=tmp%>&startdate="+starttime+"&todate="+endtime;
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
background-image: url(${pageContext.request.contextPath}/resource/image/bg.jpg);
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
							                	<td class="win-content-title" style="align:center">&nbsp;JBoss���ܱ���</td>
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
											    	<td width=100%>
											    		<table class="application-detail-data-body">
														<tr>
														<td valign=top>
												      		<table style="BORDER-COLLAPSE: collapse" cellpadding=0 rules=none width=100% align=center border=1 algin="center">
								                    				<tr>
                                              									        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����:</td>
                                              											<td width="35%"><%=jBossConfig.getAlias() %></td>
                        													            <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;IP��ַ:</td>
                                              											<td ><%=jBossConfig.getIpaddress() %> </td>         										
                    										                        </tr>
                    										                        <tr bgcolor="#ECECEC">
                    										                            <td width="10%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											                        <td width="35%"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>" border="0" ></td>
                    											                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�˿ں�:</td>
                      											                        <td ><%=jBossConfig.getPort() %> </td>
                    										                        </tr>   
                    										                        <tr>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;JBOSS�汾:</td>
                      											                        <td width="35%"><%=version%></td>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�汾ʱ��:</td>
                    											                        <td ><%=date%></td>
                    										                        </tr>
                                                                                    <tr bgcolor="#ECECEC">
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;JBOSS�汾����:</td>
                      											                        <td width="35%"><%=versionname%></td>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�汾ʱ��:</td>
                    											                        <td ><%=builton%></td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������ʱ��:</td>
                      											                        <td width="35%"><%=startdate%></td>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;������:</td>
                    											                        <td ><%=host%></td>
            										                                </tr>
                    										                        <tr bgcolor="#ECECEC">
                      											                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��װĿ¼:</td>
                      											                        <td width="35%"><%=baselocation%></td>
                    											                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ܰ�װĿ¼:</td>
                      											                        <td ><%=baselocationlocal%></td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������:</td>
                      											                        <td width="35%"><%=runconfig%></td>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;#�߳�:</td>
                      											                        <td><%=threads%></td>
                                                                                    </tr>
                                                                                    <tr bgcolor="#ECECEC" >
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����ϵͳ:</td>
                      											                        <td width="35%"><%=os%> </td>
                                                                                        <td></td>
                                                                                        <td></td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;JVM �汾��:</td>
                      											                        <td width="35%"><%=jvmversion%> </td>
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal></td>
                      											                        <td ></td>
                                                                                    </tr>
                                                                                    <tr bgcolor="#ECECEC">
                                                                                        <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;JVM �汾����:</td>
                      											                        <td ><%=jvmname%> </td>
                                                                                        <td></td>
                                                                                        <td></td>
                                                                                    </tr>
                                                                                    <tr>
                                                                                    <td  align="left" colspan=4>
                                                                                <table>
                                                                                    <tr >
                                                                                        <td height="26" colspan="7" valign="center" nowrap class=txtGlobal><div align="left" class="txtGlobalBigBold">&nbsp;<%=ajp %></div></td>
                    				                                                </tr>
            				                                                        <tr bgcolor="#ECECEC">
                      							                                        <td width="13%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����߳���:</td>
                      							                                        <td width="20%"><%=ajp_maxthreads %></td>
											                                            <td width="13%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��ǰ�߳���:</td>
                      							                                        <td width="20%"><%=ajp_thrcount %> </td>
                      							                                        <td width="14%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��ǰ�߳�æ:</td>
                      							                                        <td width="20%"><%=ajp_thrbusy %> </td>
                    				                                                </tr>
                    				                                                <tr >
                      							                                        <td width="13%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�����ʱ��:</td>
                      							                                        <td width="20%"><%=ajp_maxtime%></td>
											                                            <td width="13%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����ʱ��:</td>
                      							                                        <td width="20%"><%=ajp_processtime %> </td>
                      							                                        <td width="14%" height="26" align="left" nowrap class=txtGlobal>&nbsp;Ҫ����:</td>
                      							                                        <td width="20%"><%=ajp_requestcount %> </td>
                    				                                                </tr>
                    				                                                <tr bgcolor="#ECECEC">
                      							                                        <td width="13%" height="26" align="left" nowrap class=txtGlobal>&nbsp;������:</td>
                      							                                        <td width="20%"><%=ajp_errorcount %></td>
											                                            <td width="13%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�����ֽڴ�С:</td>
                      							                                        <td width="20%"><%=ajp_bytereceived %> </td>
                      							                                        <td width="14%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�����ֽڴ�С:</td>
                      							                                        <td width="20%"><%=ajp_bytessent %> </td> 
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
						       			</table><br><br>
            		<div align=center>
            			<input type=button value="�رմ���" onclick="window.close()">
            		</div> <br>
						       		</td>
						       	</tr>
						                
              					</table>
              				</td>
              			</tr>
      				</table>
					</td>
				</tr>
       			</table>
			 
					<br>
</form>  
</body>
</html>