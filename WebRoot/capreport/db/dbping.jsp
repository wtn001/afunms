<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<%
  String rootPath = request.getContextPath();
  //System.out.println(rootPath);
  String menuTable = (String)request.getAttribute("menuTable");
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



<script language="JavaScript" type="text/JavaScript">

function query(){
	//subforms = document.forms[0];
	mainForm.action="<%=rootPath%>/dbreport.do?action=dbping&ids=<%=String.valueOf(request.getAttribute("ids"))%>";
	mainForm.submit();
}

function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/dbreport.do?action=dbping"
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
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostmemory";
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
	createChart();
}

function setClass(){
	document.getElementById('dbReportTitle-0').className='detail-data-title';
	document.getElementById('dbReportTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('dbReportTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

/************************************************************************
画曲线图
************************************************************************/
function createChart(){
	//画连通率曲线
	var pingSo = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","100%", "338", "8", "#FFFFFF");
    pingSo.addVariable("path", "<%=rootPath%>/amchart/");
    pingSo.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netPingReport_settings.xml"));
  	pingSo.addVariable("chart_data","<%=request.getAttribute("pingChartDivStr")%>");  
 	pingSo.write("pingChartDiv");
 	//画响应时间曲线图
 	//var responsetimeSo = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","100%", "338", "8", "#FFFFFF");
    //responsetimeSo.addVariable("path", "<%=rootPath%>/amchart/");
    //responsetimeSo.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netResponsetimeReport_settings.xml"));
  	//responsetimeSo.addVariable("chart_data","<%=request.getAttribute("responsetimeDivStr")%>");  
 	//responsetimeSo.write("responsetimeChartDiv");
}

</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
	<input type=hidden name="orderflag">
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
			</div>
		<div id="loading-mask" style=""></div>		
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
							<td class="td-container-main-report">
								<table id="container-main-report" class="container-main-report">
									<tr>
										<td>
											<table id="report-content" class="report-content">
												<tr>
													<td>
														<table id="report-content-header" class="report-content-header">
										                	<tr>
											    				<td>
														    		<%=dbReportTitleTable%>
														    	</td>
														  	</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="report-content-body" class="report-content-body">
				        									<tr>
				        										<td>
				        											
				        											<table id="report-data-header" class="report-data-header">
				        												<tr>
															  				<td>
																
																				<table id="report-data-header-title" class="report-data-header-title">
																					<tr>
																						<td>
																							开始日期
																								<input type="text" name="startdate" value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																								<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
																								截止日期
																								<input type="text" name="todate" value="<%=todate%>" size="10"/>
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																								<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																								&nbsp;&nbsp;<input type="button" name="doprocess" value="生成报表" onclick="query()">
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
				        											
				        											<table id="report-data-body" class="report-data-body">
				        												
				        												<tr> 
                					<td>
                						<table width="100%" border="0" cellpadding="0" cellspacing="1">
                						<table>
  									<tr>
  										<td>数据库连通率报表&nbsp;&nbsp;时间段:&nbsp;<%=(String)request.getAttribute("starttime")%>&nbsp;至&nbsp;<%=(String)request.getAttribute("totime")%>
  										</td>
  									
                      								<td align=right valign=top>&nbsp;&nbsp;
											<a href="<%=rootPath%>/dbreport.do?action=downloaddbpingreport&startdate=<%=startdate%>&todate=<%=todate%>" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
                      										&nbsp;&nbsp;
                      									<a href="<%=rootPath%>/dbreport.do?action=createpdf"><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18  border="0">导出PDF</a>&nbsp;&nbsp;&nbsp;&nbsp;
<a href="<%=rootPath%>/dbreport.do?action=createdoc"><img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORD</a>&nbsp;&nbsp;&nbsp;&nbsp;
                      								</td> 
                      							</tr>
                      							</table>
                    							<tr bgcolor="DEEBF7"> 
                      								<td>
  											<table   cellSpacing="1"  cellPadding="0" border=0 bgcolor=black width=100%>
												<tr>
													<td colspan="8">
														<table>
															<tr>
																<td>
																	<div id="pingChartDiv" style="float: left;"></div>
																</td>
															</tr>
														</table>
													</td>
												</tr>
  												<tr  class="firsttr">
    													<td align=center bgcolor=white width=5%>&nbsp;</td>
    													<td align=center bgcolor=white width=25%>IP地址</td>
		  											<td align=center bgcolor=white width=25%>数据库类型</td>
		  											<td align=center bgcolor=white width=25%>数据库名称</td>
													<td align=center bgcolor=white width=25%>数据库应用</td>
		  											<td align=center bgcolor=white width=10%>
		  												<input type="button"  name="button3" styleClass="button" onclick="changeOrder('avgping')" value="平均连通率">
		  											</td>
		  											<td align=center bgcolor=white width=10%>
		  												<input type="button"  name="button3" styleClass="button" onclick="changeOrder('downnum')" value="宕机次数">
		  											</td>
   												</tr>

<%
			int index = 0;
			//I_MonitorIpList monitorManager=new MonitoriplistManager();
			List pinglist = (List)request.getAttribute("pinglist");
			if(pinglist != null && pinglist.size()>0){
				for(int i=0;i<pinglist.size();i++){
						List _pinglist = (List)pinglist.get(i);
						String ip = (String)_pinglist.get(0);
						String dbtype = (String)_pinglist.get(1);
						String equname = (String)_pinglist.get(2);
						String dbuse = (String)_pinglist.get(3);
						String avgping = (String)_pinglist.get(4);
            String downnum = (String)_pinglist.get(5);
%>

 												<tr  class="othertr" <%=onmouseoverstyle%>>

    													<td bgcolor=white align=center>&nbsp;<%=i+1%></td>
       													<td bgcolor=white align=center>
      														<%=ip%>&nbsp;</td>
      													<td bgcolor=white align=center>
      														<%=dbtype%></td>
      													<td bgcolor=white align=center>
      														<%=equname%></td>   
														<td bgcolor=white align=center>
      														<%=dbuse%></td>  
       													<td bgcolor=white align=center>
      														<%=avgping%></td>
       													<td bgcolor=white align=center>
      														<%=downnum%></td>
 												</tr>
 <%
 }
}
 %>   
																	   
																	</table>
															
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
			</td>
		</tr>
</table>
</form>
</BODY>
</HTML>