<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Supper"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.*"%>
<%@page import="com.afunms.ip.stationtype.dao.*"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	JspPage jp = (JspPage) request.getAttribute("page");
	
	
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script language="JavaScript" type="text/javascript">
  var listAction = "<%=rootPath%>/supper.do?action=list";
  var delAction = "<%=rootPath%>/supper.do?action=delete";
  var xx = "";
  function toedit(){
      window.location="<%=rootPath%>/supper.do?action=ready_edit&id="+xx;
  }
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/supper.do?action=ready_add";
     mainForm.submit();
  }
  
  
  function toDelete()
  {
     mainForm.action = "<%=rootPath%>/ipfield.do?action=delete";
     mainForm.submit();
  }
</script>
		<script language="JavaScript" type="text/javascript">
  function toCheck()
  {
     if(FrmDeal.pwd.value=="")
     {
        alert("<密码>不能为空!");
        FrmDeal.pwd.focus();
        return false;
     }
     FrmDeal.action = "check.jsp";
     FrmDeal.submit();
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
									                	<td class="content-title"> IP管理 >> IP处理 >> 列表 </td>
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
																<tr>
																	<td class="body-data-title" style="text-align: right;">
																		<a href="#" onclick="#"></a>
																		<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<td align="center" class="body-data-title">
																		<INPUT type="checkbox" class=noborder name="checkall"
																			onclick="javascript:chkall()">
																	</td>
																	<td align="center" class="body-data-title">
																		序号
																	</td>
																	<td align="center" class="body-data-title">
																		场站名
																	</td>
																	<td align="center" class="body-data-title">
																		路由器
																	</td>
																	<td align="center" class="body-data-title">
																		交换机一
																	</td>
																	<td align="center" class="body-data-title">
																		交换机二
																	</td>
																	<td align="center" class="body-data-title">
																		有无其他网络设备
																	</td>
																	<td align="center" class="body-data-title">
																		有无网络机柜
																	</td>
																	<td align="center" class="body-data-title">
																		网络安装地点
																	</td>
																	<td align="center" class="body-data-title">
																		施工调试单位
																	</td>
																	<td align="center" class="body-data-title">
																		处理
																	</td>
																</tr>
																<%
																	
																	ip_select vo = null;
																	alltype at = null;
																	alltype at1 = null;
																	String backbone1 = "";
																	String backbone2 = "";
																	String id = "";
																	//int startRow = jp.getStartRow();
																	if(list.size()!= 0 && list!=null ){
																	for (int i = 0; i < list.size(); i++) {
																		vo = (ip_select) list.get(i);
																	//	int num = vo.getRouteCount()+vo.getSwitchCount();
																		   String[] backbone_id = vo.getIds().split(",");
																		   id = backbone_id[0];
																		   
																	//	   DaoInterface all = new alltypeDao();
																	//	   at = (alltype)all.findByID(backbone_id[0]);
																	//	   if(backbone_id[1].equals("1")){
																	//	       backbone1 = at.getBackbone_name();
																	//	   }else{
																	//	       backbone2 = at.getBackbone_name();
																	//	   }
																		
																%>
																<tr <%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list">
																		<INPUT type="checkbox" class=noborder name=checkbox  id='d'
																			value="<%=vo.getId()%>">
																	</td>
																	<td align="center" class="body-data-list">
																		<font color='blue'><%=1 + i%></font>
																	</td>
																	<td align="center" class="body-data-list">
																		<%=vo.getName() %>
																	</td>
																	<td align="center" class="body-data-list"><%=vo.getRouteName()%></td>
																	<td align="center" class="body-data-list"><%=vo.getS1Name()%></td>
																	<td align="center" class="body-data-list"><%=vo.getS2Name()%></td>
																	<td align="center" class="body-data-list"><%=vo.getDeviceType()%><%=vo.getDeviceCount()%></td>
																	<td align="center" class="body-data-list">
																		<%if(vo.getHasCabinet().equals("0")){ %>有<%}else{ %>无<%} %>
																	</td>
																	<td align="center" class="body-data-list"><%=vo.getLocation()%></td>
																	<td align="center" class="body-data-list"><%=vo.getDebugUnit()%></td>
																	<%if(vo.getFlag().equals("0")){ %>
																	<td align="center" class="body-data-list">
																	
																		<a
																			href="<%=rootPath%>/ipfield.do?action=conduct&id=<%=vo.getId()%>&backbone_name=<%=id %> "><img
																				src="<%=rootPath%>/resource/image/editicon.gif" border="0" />
																		</a>
																	</td>
																	<%}else{ %>
																	<td align="center" class="body-data-list">已处理
																	</td>
																	
																	<%} %>
																</tr>
																<%
																	}
																	}
																%>
															</table>
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
		</form>
	</BODY>
</HTML>