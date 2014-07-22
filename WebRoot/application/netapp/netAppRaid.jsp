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
    Vector<NetAppRaid> raidgroup = (Vector<NetAppRaid>) request.getAttribute("raid");
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
																		Raid�б�
																	</td>
																</tr>
															</table>
															<table id="tbEntity" class="tbEntity">
																<thead>
																	<tr>
																		<th>
																			RAID��
																		</th>
																		<th>
																			���
																		</th>
																		<th>
																			��������
																		</th>
																		<th>
																			�ܴ�С(Mb)
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
																	    NetAppRaid vo = null;
																	    Hashtable statusHash = new Hashtable();
																	    statusHash.put("1", "�");
																	    statusHash.put("2", "�����ؽ�");
																	    statusHash.put("3", "�����ؽ�");
																	    statusHash.put("4", "������֤");
																	    statusHash.put("5", "�������");
																	    statusHash.put("6", "ʧ��");
																	    if (raidgroup != null && raidgroup.size() > 0) {
																	        String raidStatus = "";
																	        for (int i = 0; i < raidgroup.size(); i++) {
																	            vo = (NetAppRaid) raidgroup.get(i);
																	            if (vo.getRaidVStatus() != null && statusHash.get(vo.getRaidVStatus()) != null) {
																	                raidStatus = (String) statusHash.get(vo.getRaidVStatus());
																	            }
																	%>
																	<tr class="data">
																		<td>
																			RAID��-<%=vo.getRaidVGroup()%></td>
																		<td><%=vo.getRaidVIndex()%></td>
																		<td><%=vo.getRaidVDiskName()%></td>
																		<td><%=vo.getRaidVTotalMb()%></td>
																		<td><%=vo.getRaidVDiskType()%></td>
																		<td><%=raidStatus%></td>
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