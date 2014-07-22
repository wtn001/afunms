<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.BusinessSystem"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@ include file="/include/globe.inc"%>
<%
	List list = (List) request.getAttribute("list");
	JspPage jp = (JspPage) request.getAttribute("page");
	String rootPath = request.getContextPath();
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
			<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var listAction = "<%=rootPath%>/cabinetbsreport.do?action=list";
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
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">

										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 3D���� >> �������� >> ҵ��ϵͳ����ʹ���б� </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
										</tr>
																									    <tr>
							<td>
							<table width="100%" cellpadding="0" cellspacing="0" >
							<tr>
						    <td bgcolor="#ECECEC" width="80%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
           </td>
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
																	<td align="center" class="body-data-title">
																		���
																	</td>
																	<td align="center" class="body-data-title">
																		ҵ������
																	</td>
																	<td align="center" class="body-data-title">
																		����
																	</td>
																	<td align="center" class="body-data-title">
																	   ��ϵ��
																	</td>
																	<td align="center" class="body-data-title">
																	   ��ϵ�绰
																	</td>
																	<td align="center" class="body-data-title">
																	   ����
																	</td>	
																	<td align="center" class="body-data-title">
																		����
																	</td>
																</tr>
																<%
																	for (int i = 0; i < list.size(); i++) {
																		BusinessSystem vo = (BusinessSystem) list.get(i);
																%>
																<tr<%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list">
																		<font color='blue'><%=1 + i%></font>
																	</td>
																	<td align="center" class="body-data-list"><%=vo.getName()%></td>
																	<td align="center" class="body-data-list"><%=vo.getDescr()%></td>
																	<td align="center" class="body-data-list"><%=vo.getContactname()%></td>
																	<td align="center" class="body-data-list"><%=vo.getContactphone()%></td>
																	<td align="center" class="body-data-list"><%=vo.getContactemail()%></td>
																	<td align="center" class="body-data-list">
																<a href="<%=rootPath%>/cabinetbsreport.do?action=opercabinetlist&operid=<%=vo.getId() %>">ҵ�񱨱�</a>
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
</HTML>
