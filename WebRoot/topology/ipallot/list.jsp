<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.config.model.IpAlias"%>
<%@page import="com.afunms.topology.util.IPAllotUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="javax.servlet.ServletRequest"%>
<%@page import="javax.servlet.http.HttpServlet"%>
<%@ include file="/include/globe.inc"%>

<%
	String[] key = (String[]) request.getAttribute("key");
	JspPage jp = (JspPage) request.getAttribute("page");
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css" />
		<script language="javascript">
 			 var curpage= <%=jp.getCurrentPage()%>;
 			 var totalpages = <%=jp.getPageTotal()%>;
 			 var listAction = "<%=rootPath%>/ipallot.do?action=list";
		</script>
		<script language="JavaScript" type="text/JavaScript">
			var show = true;
			var hide = false;
		</script>
	</head>
	<body>
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td><%=menuTable%></td>
							</tr>
						</table>
					</td>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
												<table width="100%" cellspacing="0" cellpadding="0">
													<tr>
														<td>
															<table id="content-header" class="content-header">
																<tr>
																	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																	<td class="content-title">资源 >> IP/MAC资源 >> IP分配统计</td>
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
																				<td class="body-data-title">
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
																				<td align="center" class="body-data-title">IP段</td>
																				<td align="center" class="body-data-title">可分配总数</td>
																				<td align="center" class="body-data-title">已分配</td>
																			</tr>
																			<%
																				
																				IPAllotUtil ipa = new IPAllotUtil();
																				List value = null;
																				int tmp = 0;
																				Map<String, List<String>> map = ipa.sort();
																				for(int i = 0;i<key.length;i++) {
																					value = map.get(key[i]);
																					tmp = value.size();
																			%>
																			<tr <%=onmouseoverstyle%>>
																				<td align="center" class="body-data-list"><font color='blue'><%=i+1%></font></td>
																				<td align="center" class="body-data-list"><a href="#" style="cursor: hand" onclick="window.showModalDialog('<%=rootPath%>/ipallot.do?action=read&key=<%=key[i]%>',window,',dialogHeight:500px;dialogWidth:700px')"><%=key[i] + ".*"%></a></td>
																				<td align="center" class="body-data-list">255</td>
																				<td align="center" class="body-data-list"><%=tmp%></td>
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
																				<td align="left" valign="bottom"> <img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
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
