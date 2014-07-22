<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.system.model.Role"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.polling.*"%>
<%
    String rootPath = request.getContextPath();
    User user = (User) session
		    .getAttribute(SessionConstant.CURRENT_USER);
    String menuTable = (String) request.getAttribute("menuTable");
    System.out.println("-----------role��ҳģ���б�ҳ��-----------");
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script language="JavaScript" type="text/javascript">

  function toUpdate()
  {
     mainForm.action = "<%=rootPath%>/homerole.do?action=update";
     mainForm.submit();
  }
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
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> ϵͳ���� >> ��ɫ >> ��ҳģ����� </td>
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
																		<a href="#" onclick="toUpdate()">�޸�</a>&nbsp;&nbsp;&nbsp;
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr <%=onmouseoverstyle%>>
																<%
																    List<Role> Rolelist = (List<Role>) request.getAttribute("Rolelist");
																    for (int i = 0; i < Rolelist.size(); i++) {
																		//��������Ա  ��ʾ����
																		//����Ա ��ô��ӳ�������Ա�������н�ɫ
																		//һ���û� ֻ��ʾ������ɫ 
																		//��������Աģʽ
																		if (user.getRole() == 0) {
																%>
																	<td align="center" class="body-data-list">
																		<input type="radio" id="role" name="roleId"
																			<%if (user.getRole() == Rolelist.get(i).getId()){%>
																			checked="checked" <%}%> value=<%=Rolelist.get(i).getId()%>>
																		&nbsp;<%=(String) Rolelist.get(i).getRole()%>
																	</td>
																	<%
																	    //����Աģʽ
																			} else if (user.getRole() == 2 && Rolelist.get(i).getId() != 0) {
																	%>
																	<td align="center" class="body-data-list">
																		<input type="radio" id="role" name="roleId"
																			<%if (user.getRole() == Rolelist.get(i).getId()){%>
																			checked="checked" <%}%> value=<%=Rolelist.get(i).getId()%>>
																		&nbsp;<%=(String) Rolelist.get(i).getRole()%>
																	</td>
						
																	<%
																	    } else if (user.getRole() == Rolelist.get(i).getId()) {
																			    //��ͨ�û���¼����ʾ�Ľ�ɫ
																	%>
																	<td align="center" class="body-data-list">
																		<input type="radio" id="role" name="roleId"
																			checked="checked" value=<%=Rolelist.get(i).getId()%>>
																		&nbsp;<%=(String) Rolelist.get(i).getRole()%>
																	</td>
																	<%
																	    }
																	    }
																	%>
																</tr>
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
