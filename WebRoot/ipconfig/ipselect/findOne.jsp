<%@page language="java" contentType="text/html;charset=GB18030" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.polling.om.IpMacChange"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>
<%@ page import="com.afunms.topology.dao.IpMacBaseDao"%>
<%@ page  import="com.afunms.ip.stationtype.dao.*"%>
<%@ page  import="com.afunms.ip.stationtype.model.*"%>
<%@  page import="com.afunms.common.base.DaoInterface"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.polling.om.IpMacChange"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>
<%@ page import="com.afunms.topology.dao.IpMacBaseDao"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.polling.om.IpMacChange"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>
<%@ page import="com.afunms.topology.dao.IpMacBaseDao"%>
<%@ page  import="com.afunms.ip.stationtype.dao.*"%>
<%@ page  import="com.afunms.ip.stationtype.model.*"%>
<%@  page import="com.afunms.common.base.DaoInterface"%>

<%
	String rootPath = request.getContextPath();
	List list = (List)request.getAttribute("list");

	//int rc = list.size();

	//JspPage jp = (JspPage)request.getAttribute("page");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

       <script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
         
		<script language="javascript">		
  

  
    function doQuery()
  {  
     var val = document.getElementById("id1").value;
     if(val == "" ) {
        alert("无可查询对象");
        return false;
     }
     mainForm.action = "<%=rootPath%>/ipfield.do?action=findOne";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  
  function toDeleteAll()
  {
      mainForm.action = "<%=rootPath%>/ipmacchange.do?action=deleteall";
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

function change2(){
        var field_id = window.document.getElementById("id2").value;
          $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/ipAjax.ajax?action=station&select="+field_id,
			success:function(data){
			document.getElementById("div").innerHTML =data.option;
			}
		});
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
									                	<td class="content-title"> IP管理 >> IP分配 >> 调度数据网络接入申请表 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
										<tr>
											<td>
												<table id="content-body" class="content-body">
													<tr>
														<td>
															<table>
																
																	<td bgcolor="#ECECEC" width="50%" align='center' height="100">
																		&nbsp;&nbsp;&nbsp;
																		<b>骨干点：</b>
																		<select id="id2" size=1 name='select' size=50 onchange="change2();">
																		    <option value=''>——----------请选择----------——</option>
																			<%
																			    alltypeDao a = new alltypeDao();
																			    List backbone = a.loadAll();
																			    alltype vo = null;
																			    if(backbone.size()!= 0 && backbone != null){
																			       for(int i =0;i<backbone.size();i++){
																			         vo = (alltype)backbone.get(i);
																			         
																			 %>
																			  <option value=<%=vo.getId() %>><%=vo.getBackbone_name() %></option>
																			 <%
																			   }
																			   }
																			  %>
																		</select>
																	</td>
																	<td bgcolor="#ECECEC" width="30%" align='center' height="100" id="div">
																	</td>
																</tr>
															</table>
														</td>
													</tr>
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
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>

