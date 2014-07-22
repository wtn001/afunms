<%@page import="com.afunms.common.base.JspPage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.afunms.bpm.util.MenuConstance"%>
<%@page import="com.bpm.process.model.HistoryProcessInstanceModel"%>
<%@page import="com.bpm.system.utils.ConstanceUtil"%>
<%@page import="com.bpm.system.utils.StringUtil"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程综合查询</title>
<%
String rootPath=request.getContextPath();

String starttime1 = StringUtil.toBlank((String)request.getAttribute("starttime1"));
String starttime2 = StringUtil.toBlank((String)request.getAttribute("starttime2"));
String status = StringUtil.toBlank((String)request.getAttribute("status"));
String startuser = StringUtil.toBlank((String)request.getAttribute("startuser"));
String menuTable = MenuConstance.getMenuTable();
JspPage jp = (JspPage)request.getAttribute("jsppage");
%>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
		
<%if(null!=jp){ %>
<script language="javascript">	
var curpage= <%=jp.getCurrentPage()%>;
var totalpages = <%=jp.getPageTotal()%>;
var listAction = "<%=rootPath%>/controller/hpi.action";
</script>
<%} %>
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
 function query()
  {  
     mainForm.action = "<%=rootPath%>/controller/hpi.action";
     mainForm.submit();
  }  
  
  function getstatus() {
     var ss = document.getElementById("status");
     for(var i=0;i<ss.options.length;i++) {
        if("<%=status%>"==ss.options[i].value) {
           ss.options[i].selected="selected";
           break;
        }
        
     
     }
  }
</script>
</head>
<body id="body" class="body" onload="initmenu();getstatus()">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
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
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 流程管理 >> 流程任务 >> 综合查询 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						
		        						<tr>
		        						  <td>
		        						  <table>
		        						      <tr>
					        						<td>
														<table>
															<tr>
															    <td class="detail-data-body-title" style="text-align: left;">
																	&nbsp;开始日期
																	<input type="text" name="starttime1" value="<%=starttime1%>" size="10">
																	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].starttime1,null,0,330)">
																	<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																
																	至
																	<input type="text" name="starttime2" value="<%=starttime2%>" size="10"/>
																	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].starttime2,null,0,330)">
																	<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																	&nbsp;处理状态
																	<select name="status" id="status">
																	<option value="">请选择</option>
																	<option value="<%=ConstanceUtil.PRO_START%>">启动</option>
																	<option value="<%=ConstanceUtil.PRO_RUN%>" >运行</option>
																	<option value="<%=ConstanceUtil.PRO_HALF%>" >办结</option>
																	<option value="<%=ConstanceUtil.PRO_END%>" >结束</option>
																	</select>
																	&nbsp;创建人：
																	<input type="text" name="startuser" id=startuser value="<%=startuser%>">
																	<input type="button" name="submitss" value="查询" onclick="query()">
																</td>
															</tr>
															</table>
							  						</td>                       
					        					</tr>
		        						     </table>
		        						
		        						  </td>
		        						</tr>
		        						
		        						
		        						
		        						
		        						
		        						<% if(jp!=null){ %>
		        						<tr>
		        							<td>
		        								<table id="content-body" class="content-body">
		        									<tr >
														<td >
															<table width="100%" >
																<tr>
									    							<td class="body-data-title" width="80%" align="center">
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
														<td>
															<table>
					        									<tr>
          																 <td align="center" class="body-data-title">流程实例ID</td>
          																 <td align="center" class="body-data-title">流程定义ID</td>
         																 <td align="center" class="body-data-title">启动时间</td>
         															     <td align="center" class="body-data-title">创建人</td>
         																 <td align="center" class="body-data-title">处理状态</td>
         																 <td align="center" class="body-data-title">流程跟踪</td>
					        									</tr>
		<%
		HistoryProcessInstanceModel hpi=null;
	 	if (jp.getList() != null)
	 	{
	 		for (int i = 0; i < jp.getList().size(); i++) 
	 		{
	 		hpi=(HistoryProcessInstanceModel)jp.getList().get(i);
	 			%><tr>
				<td align="center" class="body-data-list"><%=hpi.getProcessInstanceId() %></td>
				<td align="center" class="body-data-list"><%=hpi.getProcessDefinitionId() %></td>
				<td align="center" class="body-data-list"><%=hpi.getStartTime()%></td>
				<td align="center" class="body-data-list"><%=hpi.getStartUser()%></td>
				<td align="center" class="body-data-list"><%
				if(ConstanceUtil.PRO_HALF.equals(hpi.getStatus())) {
					out.print(ConstanceUtil.PRO_HALF_MESSAGE);
				}
				else if(ConstanceUtil.PRO_RUN.equals(hpi.getStatus())) 
				 {out.print(ConstanceUtil.PRO_RUN_MESSAGE);} 
				else if(ConstanceUtil.PRO_START.equals(hpi.getStatus())) 
				 {out.print(ConstanceUtil.PRO_START_MESSAGE);} 
				
				else {out.print(ConstanceUtil.PRO_END_MESSAGE);
				
				}
				%></td>
				<td align="center" class="body-data-list">
				<a target="_blank" href="../controller/taskView.action?processDefinitionId=<%=hpi.getProcessDefinitionId()%>&processInstanceId=<%=hpi.getProcessInstanceId()%>">查看</a>
				</td>
			</tr><%
	 		}
	 	}
	 	%>
		        											</table>
														</td>
													</tr> 
		        								</table>
		        							</td>
		        						</tr>
		        						<% } %>
		        						<tr>
		        							<td>
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
		        					</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
