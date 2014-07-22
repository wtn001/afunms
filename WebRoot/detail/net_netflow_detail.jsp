<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.amchart.AmChartTool"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.dao.IpMacBaseDao"%>

<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@ page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.config.dao.*"%>
<%@ page import="com.afunms.config.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.ipaccounting.dao.*"%>
<%@page import="com.afunms.ipaccounting.model.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%
String rootPath = request.getContextPath();
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
if(!startdate.contains(" 00:00:00")){
	startdate = startdate + " 00:00:00";
}
if(!todate.contains(" 23:59:59")){
	todate = todate + " 23:59:59";
}
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<title>--流量详细--</title>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />


		<link rel="stylesheet"
			href="<%=rootPath%>/application/resource/jquery_tablesort/flora/flora.all.css"
			type="text/css" media="screen" title="Flora (Default)">
		<script
			src="<%=rootPath%>/application/resource/jquery_tablesort/jquery-latest.js"
			type="text/javascript"></script>
		<script
			src="<%=rootPath%>/application/resource/jquery_tablesort/jquery.tablesorter.js"
			type="text/javascript"></script>
		<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$("#netflow_table").tablesorter({
			sortList:[],
			widgets: ['zebra'],
			headers: { 
				0:{sorter: false}, 
				1:{sorter: false} 
			}
		});//0为升序    1降。
	} ); 
	
	/**************************************************
	*将流量信息导出为excel格式的报表
	**************************************************/
	function network_netflowdetail_excel_report(){
		//mainForm.action = "<%=rootPath%>/netreport.do?action=downloadnetnetflowdetailreport";
    	//mainForm.submit(); 
	}
	
	/**************************************************
	*将流量信息导出为excel格式的报表
	**************************************************/
	Ext.onReady(function()
	{  
 		Ext.get("process").on("click",function(){
        	Ext.MessageBox.wait('数据加载中，请稍后.. '); 
         	mainForm.action = "<%=rootPath%>/netreport.do?action=downloadnetnetflowdetailreport";
         	mainForm.submit();
         	window.close();
 		});	
	});
</script>

	</head>
	<body  style="background-color: #ECECEC;">
	<form id="mainForm" method="post" name="mainForm">
		<table>
			<tr>
				<td valign=top>
					<table>
						<tr>
							<td colspan="3">
								<table id="content-header" class="content-header">
				                	<tr>
					                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					                	<td class="content-title">&nbsp;性能 >> 设备详细 >> 流量统计  >> 流量详细 </td>
					                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
					       			</tr>
					       			<tr>
										<td colspan=3 height="28" bgcolor="#ECECEC" align="right">
											<font style="font-style: normal;">数据统计时间段 :<%=startdate %>&nbsp;到&nbsp; <%=todate %></font>
											<a href="#" id="process"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>
										</td>
					       			</tr>
					        	</table>
				        	</td>
			        	</tr>
			        	<tr>
							<td style="width: 100%;">
								<table id="netflow_table" class="tablesorter"
									cellspacing="1" cellpadding="0" width="100%">
									<thead>
										<tr bgcolor="#ECECEC">
											<th align="center" style="color: #000000;"
												class="body-data-title" width='6%' align=left>
												序号
											</th>
											<th align="center" style="color: #000000;"
												class="body-data-title" width='10%'>
												源IP地址
											</th>
											<th align="center" style="color: #000000;"
												class="body-data-title" width='10%'>
												目的IP地址
											</th>
											<th align="center" style="color: #000000;"
												class="body-data-title" width='10%'>
												总流量（KBytes）
											</th>
										</tr>
									</thead>
									<tbody>
									<%
										List flowDetailList = (ArrayList)request.getAttribute("flowDetailList");
										String netflowdata = new AmChartTool().getNetNetFlowChart(flowDetailList);
										if(flowDetailList != null && flowDetailList.size() > 0){
											for(int i=0; i<flowDetailList.size(); i++){
												Vector vec = (Vector)flowDetailList.get(i);
									%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
											<td class="detail-data-body-list">
												<%=i+1 %>
											</td>
											<td class="detail-data-body-list">
												<%=vec.get(0) %>
											</td>
											<td class="detail-data-body-list">
												<%=vec.get(1) %>
											</td>
											<td class="detail-data-body-list">
												<%=vec.get(2) %>
											</td>
										</tr>
										<%
												}
											}
										%>
									</tbody>
								</table>
								<table cellpadding="0" cellspacing="0" width=48% style="background-color: white;">
									<tr><td>&nbsp;</td></tr>
									<tr>
										<td width="100%" align=center>
											<div id="net_netflow">
											</div>
											<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
											<script type="text/javascript">
                                                 <% if(netflowdata != null && netflowdata.length() > 0){ %>	
                                                 var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","860", "288", "8", "#FFFFFF");
                                                 so.addVariable("path", "<%=rootPath%>/amchart/");
                                                 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netnetflow_settings.xml"));
                                                 so.addVariable("chart_data","<%=netflowdata%>"); 
                                                 so.write("net_netflow");
                                                 <%}else{%>
                                               	var _div=document.getElementById("net_netflow");
                                               	var img=document.createElement("img");
                                               	img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
                                               	_div.appendChild(img);
                                               	<%}%>
                                            </script>
										</td>
									</tr>
								</table>
							</td>
			        	</tr>
						<tr><td colspan="3" align="center">&nbsp;<input type="button"  onclick="window.close();" name="关闭" value="关闭"/></td></tr>
			        </table>
	        	</td>
	        </tr>
	     </table>
	</form>
</body>
</HTML>