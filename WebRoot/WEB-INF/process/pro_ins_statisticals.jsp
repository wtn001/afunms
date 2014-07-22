<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.afunms.bpm.util.MenuConstance"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.bpm.system.utils.ProcessEnum"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache"/>
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0"/>
<title>流程统计列表</title>
<%
String rootPath=request.getContextPath();
String menuTable = MenuConstance.getMenuTable();
HashMap<ProcessEnum,String> map=(HashMap<ProcessEnum,String>)request.getAttribute("map");
String pieXml=(String)request.getAttribute("pieXml");
String barXml=(String)request.getAttribute("barXml");
%>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
 		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
 <script language="JavaScript" type="text/javascript">
function exportExcel()
{
	window.open("<%=rootPath %>/controller/exportExcel.action?contenttype=pro_ins_stat");
}
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

}
 }

</script>
<script type="text/javascript">

 function showdetails(key) {
var returnvalue = window.showModalDialog('<%=rootPath%>/controller/proInsStatisticalsDetail.action?type='+key,window,'dialogWidth=1000px;dialogHeight=600px;center=yes');
return ;
}
 
</script>
</head>
<body id="body" class="body" onload="initmenu();result();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td colspan="2">
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 流程管理 >> 流程任务 >> 统计列表 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						
		        						<tr>
		        						  <td colspan="2">
		        						  <table>
		        						      <tr>
					        						<td>
														<table>
															<tr>
															
															    <td class="detail-data-body-title" style="text-align: right;">
																	&nbsp;&nbsp;<input type="button" class="botton" value="导出报表" onclick="exportExcel();"/>&nbsp;&nbsp;						
																</td>
															</tr>
															</table>
							  						</td>                       
					        					</tr>
		        						     </table>
		        						
		        						  </td>
		        						</tr>
		        						
		        						<tr>
		        							<td colspan="2">
		        								<table id="content-body" class="content-body">
													<tr>
														<td>
														 <table>
					        									<tr>
          															<td align="center" class="body-data-title">名称</td>
          															<td align="center" class="body-data-title">数量</td>
					        										<td align="center" class="body-data-title">详情</td>	
					        									</tr>
														 		<%
														 		for(ProcessEnum key:map.keySet())
														 		{
														 		%>
														 		<tr>
																	<td align="center" class="body-data-list"><%=key.decp %></td>
																	<td align="center" class="body-data-list"><%=map.get(key) %></td>
																	<td align="center" class="body-data-list">
																		<a href="javascript:void(null)" onclick="javascript:showdetails('<%=key.key%>');">详情</a>
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
		        						<tr>
		        							<td colspan="2">
		        								<table id="content-footer" class="content-footer">
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
		        						<tr>
							                	<td class="win-data-title" style="height: 29px;" ><br><br></td>
							       		</tr>
										<tr>
											<td>
												<div id="pie">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
				<script type="text/javascript">
					// <![CDATA[		
					var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","480", "320", "8", "#FFFFFF");
					so.addVariable("path", "<%=rootPath%>/amchart/");
					so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/processPie.xml"));
					so.addVariable("chart_data","<%=pieXml %>");
					so.write("pie");
					// ]]>
				</script>
				</td>
				<td>
						<div id="column">
								<strong>You need to upgrade your Flash Player</strong>
							</div>
							<script type="text/javascript">
								// <![CDATA[		
								var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","480", "320", "8", "#FFFFF");
								so.addVariable("path", "<%=rootPath%>/amchart/");
								so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/processBar.xml"));
								so.addVariable("chart_data", "<%=barXml %>");
								so.write("column");
								// ]]>
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
	</body>
</html>
