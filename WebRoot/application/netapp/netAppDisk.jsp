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
    Vector<NetAppDisk> diskVector = (Vector<NetAppDisk>) request.getAttribute("disk");
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
																		�����б�
																	</td>
																</tr>
															</table>
															<table id="tbEntity" class="tbEntity">
																<thead>
																	<tr>
																		<th>
																			��������
																		</th>
																		<th>
																			��������
																		</th>
																		<th>
																			����������(%)
																		</th>
																		<th>
																			��������
																		</th>
																		<th>
																			���̹��ص�
																		</th>

																		<th>
																			״̬
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																	    NetAppDisk vo = null;
																	    Hashtable statusHash = new Hashtable();
																	    statusHash.put("1", "δ����");
																	    statusHash.put("2", "�ѹ���");
																	    statusHash.put("3", "����");
																	    statusHash.put("4", "����ж��");
																	    statusHash.put("5", "���ڴ���");
																	    statusHash.put("6", "���ڹ���");
																	    statusHash.put("7", "unmounting");
																	    statusHash.put("8", "nofsinfo");
																	    statusHash.put("9", "replaying");
																	    statusHash.put("10", "replayed");

																	    Hashtable typeHash = new Hashtable();
																	    typeHash.put("1", "traditionalVolume");
																	    typeHash.put("2", "flexibleVolume");
																	    typeHash.put("3", "aggregate");

																	    if (diskVector != null && diskVector.size() > 0) {
																	        String diskState = "";
																	        String diskType = "";
																	        for (int i = 0; i < diskVector.size(); i++) {
																	            vo = (NetAppDisk) diskVector.get(i);
																	            if (vo.getDfType() != null && typeHash.get(vo.getDfType()) != null) {
																	                diskType = (String) typeHash.get(vo.getDfType());
																	            } else if (vo.getDfStatus() != null && statusHash.get(vo.getDfStatus()) != null) {
																	                diskState = (String) statusHash.get(vo.getDfStatus());
																	            }
																	%>
																	<tr class="data">
																		<td>
																			<%=vo.getDfIndex()%></td>
																		<td><%=vo.getDfFileSys()%></td>
																		<td><%=vo.getDfPerCentKBytesCapacity()%></td>
																		<td><%=diskType%></td>
																		<td><%=vo.getDfMountedOn()%></td>
																		<td><%=diskState%></td>
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