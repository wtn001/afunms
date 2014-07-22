<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.bpm.process.model.ProcessTaskModel"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计详情</title>
<%
String rootPath=request.getContextPath();
JspPage jp = (JspPage)request.getAttribute("jsppage");
String type = (String)request.getAttribute("type");
String person = (String)request.getAttribute("person");
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
String exectname = (String)request.getAttribute("exectname");

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
var condition="type=<%=type %>";
var listAction = "<%=rootPath%>/controller/processStatisticalsDetail.action";
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
     mainForm.action = "<%=rootPath%>/controller/processStatisticalsDetail.action";
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
			<input type="hidden" name="type" value="<%=type %>">
			<input type="hidden" name="person" value="<%=person %>">
			<input type="hidden" name="startdate" value="<%=startdate %>">
			<input type="hidden" name="todate" value="<%=todate %>">
			<input type="hidden" name="exectname" value="<%=exectname %>">
			<table id="body-container" class="body-container">
				<tr>
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
									                	<td class="content-title"> 流程 >> 统计 >> 详情 </td>
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
          																 <td align="center" class="body-data-title">任务实例名称</td>
          																 <td align="center" class="body-data-title">开始时间</td>
         																 <td align="center" class="body-data-title">结束时间</td>
					        									</tr>
		<%
	 	ProcessTaskModel model=null;
	 	if (jp.getList() != null)
	 	{
	 		for (int i = 0; i < jp.getList().size(); i++) 
	 		{
	 		model=(ProcessTaskModel)jp.getList().get(i);
	 			%><tr>
				<td align="center" class="body-data-list"><%=model.getName() %></td>
				<td align="center" class="body-data-list"><%=model.getStarttime() %></td>
				<td align="center" class="body-data-list">
				<% if(model.getEndtime()!=null)
				{ out.print(model.getEndtime()); }
				 %>
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
