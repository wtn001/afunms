<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>

<%
	String menuTable = (String) request.getAttribute("menuTable");//�˵�
	String tmp = request.getParameter("id");
	int alarmLevel = (Integer) request.getAttribute("alarmLevel");
	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
	Vector<NetAppSpare> spareVector = (Vector<NetAppSpare>) request.getAttribute("spare");
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
	<body id="body" class="body"">
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="id">
			<table id="body-container" class="body-container">
				<tr>

					<!-- ���˵� -->
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td>
									<%=menuTable%>
								</td>
							</tr>
						</table>
					</td>
					<!-- ���˵�end -->

					<!-- �м���������ʼ -->
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

										<!-- ����ģ�� -->
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<!-- ������ -->
													<tr>
														<td class="detail-data-header">
															<%=netAppDetailTitleTable%>
														</td>
													</tr>
													<!-- ������end -->

													<!-- ��������ʼ -->
													<tr>
														<td>
														<table class="tbEntity">
																<tr>
																	<td class="title">
																		Spare�б�
																	</td>
																</tr>
															</table>
															<table id="tbEntity" class="tbEntity">
																<thead>
																	<tr>
																		<th>
																			����
																		</th>
																		<th>
																			����ID
																		</th>
																		<th>
																			��������
																		</th>
																		<th>
																			ScsiAdapter
																		</th>
																		<th>
																			�ܴ�С(Mb)
																		</th>
																		<th>
																			�ܿ���
																		</th>
																		<th>
																			�����̼�
																		</th>
																		<th>
																			���ڳ�
																		</th>
																		<th>
																			�������к�
																		</th>
																		<th>
																			����ת��
																		</th>
																		<th>
																			��������
																		</th>

																		<th>
																			״̬
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																		NetAppSpare vo = null;
																		Hashtable statusHash = new Hashtable();
																		statusHash.put("1", "spare");
																		statusHash.put("2", "addingspare");
																		statusHash.put("3", "bypassed");
																		statusHash.put("4", "unknown");
																		statusHash.put("10", "offline");
																		if (spareVector != null && spareVector.size() > 0) {
																			for (int i = 0; i < spareVector.size(); i++) {
																				vo = (NetAppSpare) spareVector.get(i);
																	%>
																	<tr class="data">
																		<td>
																			<%=vo.getSpareIndex()%></td>
																		<td><%=vo.getSpareDiskId()%></td>
																		<td><%=vo.getSpareDiskName()%></td>
																		<td><%=vo.getSpareScsiAdapter()%></td>
																		<td><%=vo.getSpareTotalMb()%></td>
																		<td><%=vo.getSpareTotalBlocks()%></td>
																		<td><%=vo.getSpareShelf()%></td>
																		<td><%=vo.getSparePool()%></td>
																		<td><%=vo.getSpareDiskSerialNumber()%></td>
																		<td><%=vo.getSpareDiskRPM()%></td>
																		<td><%=vo.getSpareDiskType()%></td>
																		<td><%=(String) statusHash.get(vo.getSpareStatus())%></td>
																	</tr>
																	<%
																		}
																		}
																	%>
																</tbody>
															</table>
														</td>
													</tr>
													<!-- ��������end -->

												</table>
											</td>
										</tr>
										<!-- ����ģ��end -->
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