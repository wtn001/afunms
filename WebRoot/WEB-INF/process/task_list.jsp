<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.bpm.util.MenuConstance"%>
<%@page import="org.activiti.engine.task.Task"%>
<%@page import="com.bpm.system.utils.DateFormat"%>
<%@page import="java.util.Map"%>
<%@page import="com.afunms.system.util.CodeUtil"%>
<%@page import="com.bpm.system.utils.StringUtil"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="path" value="${request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>待签收任务列表</title>
<%
String rootPath=request.getContextPath();
String menuTable = MenuConstance.getMenuTable();
JspPage jp = (JspPage)request.getAttribute("jsppage");
Map<String,String> modeltype = CodeUtil.getCodeModeType_Definition();
%>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
<%if(null!=jp){ %>
<script language="javascript">	
var curpage= <%=jp.getCurrentPage()%>;
var totalpages = <%=jp.getPageTotal()%>;
var listAction = "<%=rootPath%>/controller/taskList.action";
</script>
<%} %>
<script language="javascript">	
  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/controller/taskList.action";
     mainForm.submit();
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
</script>
</head>
<body id="body" class="body" onload="initmenu();">
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
									                	<td class="content-title"> 流程 >> 流程任务 >> 待签收任务 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
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
          																 <td align="center" class="body-data-title">序号</td>
          																 <td align="center" class="body-data-title">创建时间</td>
         																 <td align="center" class="body-data-title">当前任务名称</td>
         																 <td align="center" class="body-data-title">流程类型</td>
         															     <td align="center" class="body-data-title">流程跟踪</td>
         																 <td align="center" class="body-data-title">签收</td>
					        									</tr>
		<%
	 	Task task=null;
	 	if (jp.getList() != null)
	 	{
	 		for (int i = 0; i < jp.getList().size(); i++) 
	 		{
	 		task=(Task)jp.getList().get(i);
	 			%><tr>
				<td align="center" class="body-data-list"><%=jp.getPerPage()*(jp.getCurrentPage()-1)+i+1%></td>
				<td align="center" class="body-data-list"><%=DateFormat.TIME_FORMAT.format(task.getCreateTime()) %></td>
				<td align="center" class="body-data-list"><%=task.getName() %></td>
				<td align="center" class="body-data-list"><%=StringUtil.isBlank(modeltype.get(task.getProcessDefinitionId()))?"":modeltype.get(task.getProcessDefinitionId())%></td>
				<td align="center" class="body-data-list"><a target="_blank" href="../controller/taskView.action?processInstanceId=<%=task.getProcessInstanceId() %>
				&processDefinitionId=<%=task.getProcessDefinitionId() %> ">查看
					</a>
				</td>
				<td align="center" class="body-data-list">
				<a href="../controller/taskClaim.action?taskId=<%=task.getId() %>">签收
				</a>
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
