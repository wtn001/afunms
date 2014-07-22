<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%
	String menuTable = (String) request.getAttribute("menuTable");//�˵�
	String tmp = request.getParameter("id");
	int alarmLevel = (Integer) request.getAttribute("alarmLevel");
	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
	NetAppEnvironment environment = (NetAppEnvironment) request.getAttribute("environment");
	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
	String rootPath = request.getContextPath();
%>
<html>
	<head>


		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<link rel="stylesheet"
			href="<%=rootPath%>/common/css/tableNewStyle.css" type="text/css"></link>

		<script type="text/javascript">
				function refer(action){
					document.getElementById("id").value="<%=tmp%>";
					var mainForm = document.getElementById("mainForm");
					mainForm.action = '<%=rootPath%>' + action;
					mainForm.submit();
				}
			</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="id">
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
								<td class="td-container-main-detail" width=98%>
									<table id="container-main-detail" class="container-main-detail">
										<!-- ��������� -->
										<tr>
											<td>
												<table id="detail-content" class="detail-content"
													width="100%" cellspacing="0" cellpadding="0">
													<!-- �������� -->
													<tr class="bar">
														<td colspan=4 align=center>
															<b>�豸��ϸ��Ϣ</b>
														</td>
													</tr>
													<!-- ��������end -->
													<!-- �豸��Ҫ��Ϣ -->
													<tr class="bar">
														<td width="20%" style="padding-left: 30px;">
															�豸��ǩ:<%=host.getAlias()%>
														</td>
														<td width="20%">
															ϵͳ����:<%=host.getSysName()%>
														</td>
														<td width=15%>
															<table>
																<tr>
																	<td width="10px;">
																		<img
																			src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmLevel)%>">
																	</td>
																	<td>
																		<span><%=NodeHelper.getStatusDescr(alarmLevel)%></span>
																	</td>
																</tr>
															</table>
														</td>
														<td width="20%">
															IP��ַ:<%=host.getIpAddress()%>
														</td>
													</tr>
													<!-- �豸��Ҫ��Ϣend -->
												</table>
											</td>
										</tr>
										<!-- ���������end -->
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=netAppDetailTitleTable%>
														</td>
													</tr>

													<tr>
														<td>
															<table class="tbEntity">
																<tr>
																	<td class="title">
																		����������Ϣ
																	</td>
																</tr>
															</table>
															<table class="tbEntity">
																<thead>
																	<tr>
																		<th>
																			�¶�
																		</th>
																		<th>
																			����
																		</th>
																		<th>
																			��Դ
																		</th>
																		<th>
																			���
																		</th>
																	</tr>
																</thead>
																<tbody>

																	<%
																		if (environment != null) {
																			String temper = "<img src=" + rootPath + "/resource/image/topo/status_ok.gif>����";
																			if (!"1".equals(environment.getEnvOverTemperature()))
																				temper = "<img src=" + rootPath + "/resource/image/topo/alert.gif>�쳣";
																			String fan = "<img src=" + rootPath + "/resource/image/topo/status_ok.gif>����";
																			if (!"0".equals(environment.getEnvFailedFanCount()))
																				fan = "<img src=" + rootPath + "/resource/image/topo/alert.gif>�쳣";
																			String power = "<img src=" + rootPath + "/resource/image/topo/status_ok.gif>����";
																			if (!"0".equals(environment.getEnvFailedPowerSupplyCount()))
																				power = "<img src=" + rootPath + "/resource/image/topo/alert.gif>�쳣";
																			String battery = "<img src=" + rootPath + "/resource/image/topo/status_ok.gif>����";
																			if (!"1".equals(environment.getNvramBatteryStatus()))
																				battery = "<img src=" + rootPath + "/resource/image/topo/alert.gif>�쳣";
																	%>
																	<tr align="center" height=28 class="microsoftLook">
																		<td align="center"><%=temper%></td>
																		<td align="center"><%=fan%></td>
																		<td align="center"><%=power%></td>
																		<td align="center"><%=battery%></td>
																	</tr>
																	<%
																		}
																	%>
																</tbody>
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
					<td class="td-container-main-tool" width='15%'>
						<jsp:include page="/include/netapptoolbar.jsp">
							<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
							<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
							<jsp:param value="<%=tmp%>" name="tmp" />
							<jsp:param value="network" name="category" />
							<jsp:param value="<%=nodedto.getSubtype()%>" name="subtype" />
						</jsp:include>
					</td>
				</tr>

			</table>
		</form>
	</BODY>
</HTML>