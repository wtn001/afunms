<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.Diskconfig"%>
<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	List ips = (List) request.getAttribute("ips");

	int rc = list.size();

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
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/disk.do?action=delete";
  var listAction = "<%=rootPath%>/disk.do?action=list";
  
  function doDelete(){
        if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
          mainForm.action="<%=rootPath%>/disk.do?action=delete";
          mainForm.submit();
        }
  }
  
  function doQuery()
  {  
     mainForm.action = "<%=rootPath%>/disk.do?action=find";
     mainForm.submit();
  }
  function doFromlastoconfig()
  {  
     mainForm.action = "<%=rootPath%>/disk.do?action=fromlasttoconfig";
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
      mainForm.action = "<%=rootPath%>/disk.do?action=ready_add";
      mainForm.submit();
  }
  
    function toEmpty()
  {
      mainForm.action = "<%=rootPath%>/disk.do?action=empty";
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
														<td class="content-title"> ��Դ >> ip ���ι��� >> �����б� </td>
														<td align="right"> <img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
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
																	<td class="body-data-title" style="text-align: left;">
																		&nbsp;&nbsp;
																		<B>��ѯ:</B>
																		<SELECT name="ipaddress" style="" style="width: 130">
																			<%
																				if (ips != null && ips.size() > 0) {
																					for (int k = 0; k < ips.size(); k++) {
																						String ip = (String) ips.get(k);
																			%>
																			<OPTION value="<%=ip%>"><%=ip%></OPTION>
																			<%
																				}
																				}
																			%>

																		</SELECT>
																		&nbsp;
																		<INPUT type="button" class="formStyle" value="��ѯ" onclick=" return doQuery()">
																	</td>
																	<td class="body-data-title" style="text-align: right;">
																		<INPUT type="button" class="formStyle" value="�� ��" onclick=" return toEmpty()">
																		<INPUT type="button" class="formStyle" value="ˢ ��" onclick=" return doFromlastoconfig()">
																		<INPUT type="button" class="formStyle" value="ɾ ��" onclick=" return toDelete()">
																		&nbsp;&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table width="100%" cellpadding="0" cellspacing="1">
																<tr>
																	<td class="body-data-title" style="text-align: left;">
																		<jsp:include page="../../common/page.jsp">
																		<jsp:param name="curpage"
																				value="<%=jp.getCurrentPage()%>" />
																		<jsp:param name="pagetotal"
																				value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td colspan="2">
															<table cellspacing="1" cellpadding="0" width="100%">
																<tr class="microsoftLook0">
																	<td align="center" class="body-data-title">
																		<INPUT type="checkbox" class=noborder name="checkall"
																			onclick="javascript:chkall()">
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>IP��ַ</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>��������</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>�Ƿ�澯</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>һ����ֵ</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>����</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>������ֵ</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>����</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>������ֵ</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		<strong>����</strong>
																	</td>
																	<td align="center" class="body-data-title">
																		������ʾ����
																	</td>
																	<td align="center" class="body-data-title">
																		��ע
																	</td>
																	<td align="center" class="body-data-title">
																		����
																	</td>

																</tr>
																<%
																	Diskconfig vo = null;
																	int startRow = jp.getStartRow();
																	for (int i = 0; i < rc; i++) {
																		vo = (Diskconfig) list.get(i);
																%>
																<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list">
																		<font color='blue'>&nbsp;<%=startRow + i%></font>
																		<INPUT type="checkbox" class=noborder name=checkbox
																			value="<%=vo.getId()%>" class=noborder>
																	</td>
																	<td align="center" class="body-data-list"><%=vo.getIpaddress()%></td>
																	<td align="center" class="body-data-list"><%=vo.getName()%></td>
																	<%
																		String monflag = "��";
																			if (vo.getMonflag() == 0)
																				monflag = "��";
																			String sms = "��";
																			if (vo.getSms() == 0)
																				sms = "��";
																			String sms1 = "��";
																			if (vo.getSms1() == 0)
																				sms1 = "��";
																			String sms2 = "��";
																			if (vo.getSms2() == 0)
																				sms2 = "��";
																	%>
																	<td align="center" class="body-data-list"><%=monflag%></td>
																	<td align="center" class="body-data-list"><%=vo.getLimenvalue()%></td>
																	<td align="center" class="body-data-list"><%=sms%></td>
																	<td align="center" class="body-data-list"><%=vo.getLimenvalue1()%></td>
																	<td align="center" class="body-data-list"><%=sms1%></td>
																	<td align="center" class="body-data-list"><%=vo.getLimenvalue2()%></td>
																	<td align="center" class="body-data-list"><%=sms2%></td>
																	<%
																		String reportflag = "<a href=" + rootPath
																					+ "/disk.do?action=update&id=" + vo.getId()
																					+ "&reportflag=0>ȡ����ʾ�ڱ���</a>";
																			if (vo.getReportflag() == 0) {
																				reportflag = "<a href=" + rootPath
																						+ "/disk.do?action=update&id=" + vo.getId()
																						+ "&reportflag=1>��ʾ�ڱ���</a>";
																			}
																	%>
																	<td align="center"  class="body-data-list"><%=reportflag%></td>
																	<td align="center"  class="body-data-list"><%=vo.getBak()%></td>
																	<td align="center" class="body-data-list">
																		<a href="javascript:void(null)"
																			onClick='window.open("<%=rootPath%>/disk.do?action=showedit&id=<%=vo.getId()%>","editdiskconfig", "height=400, width= 500, top=200, left= 200")'>
																			<img src="<%=rootPath%>/resource/image/editicon.gif"
																				border="0" />
																		</a>
																	</td>
																</tr>
																<%
																	}
																%>
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
