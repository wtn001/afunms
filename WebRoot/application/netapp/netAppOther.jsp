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
    Vector<NetAppTree> treeVector = (Vector<NetAppTree>) request.getAttribute("tree");
    Vector<NetAppAggregate> aggregateVector = (Vector<NetAppAggregate>) request.getAttribute("aggregate");
    Vector<NetAppSnapshot> snapshotVector = (Vector<NetAppSnapshot>) request.getAttribute("snapshot");
    Vector<NetAppQuota> quotaVector = (Vector<NetAppQuota>) request.getAttribute("quota");
    Vector<NetAppPlex> plexVector = (Vector<NetAppPlex>) request.getAttribute("plex");
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
																		Tree�б�
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
																			����
																		</th>
																		<th>
																			TreeName
																		</th>
																		<th>
																			Tree����
																		</th>

																		<th>
																			״̬
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																	    NetAppTree vo = null;
																	    Hashtable treeStatusHash = new Hashtable();
																	    treeStatusHash.put("1", "normal");
																	    treeStatusHash.put("2", "snapmirrored");
																	    treeStatusHash.put("3", "snapvaulted");
																	    Hashtable treeTypeHash = new Hashtable();
																	    treeTypeHash.put("1", "unix");
																	    treeTypeHash.put("2", "ntfs");
																	    treeTypeHash.put("3", "mixed");
																	    if (treeVector != null && treeVector.size() > 0) {
																	        String treeState = "";
																	        String treeType = "";
																	        for (int i = 0; i < treeVector.size(); i++) {
																	            vo = (NetAppTree) treeVector.get(i);
																	            if (vo.getTreeStyle() != null && treeTypeHash.get(vo.getTreeStyle()) != null) {
																	                treeType = (String) treeTypeHash.get(vo.getTreeStyle());
																	            } else if (vo.getTreeStatus() != null && treeStatusHash.get(vo.getTreeStatus()) != null) {
																	                treeState = (String) treeStatusHash.get(vo.getTreeStatus());
																	            }
																	%>
																	<tr class="data">
																		<td>
																			<%=vo.getTreeIndex()%></td>
																		<td><%=vo.getTreeVolumeName()%></td>
																		<td><%=vo.getTreeName()%></td>
																		<td><%=treeType%></td>
																		<td><%=treeState%></td>
																	</tr>
																	<%
																	    }
																	    }
																	%>
																</tbody>
															</table>
														</td>
													</tr>

													<tr>
														<td>
															<table class="tbEntity">
																<tr>
																	<td class="title">
																		Aggregate�б�
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
																			AggregateName
																		</th>
																		<th>
																			AggregateFSID
																		</th>
																		<th>
																			��������
																		</th>
																		<th>
																			Ψһ��ʶ
																		</th>
																		<th>
																			����
																		</th>
																		<th>
																			Raid����
																		</th>

																		<th>
																			״̬
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																	    NetAppAggregate aggregateVo = null;
																	    Hashtable aggregateTypeHash = new Hashtable();
																	    aggregateTypeHash.put("1", "traditional");
																	    aggregateTypeHash.put("2", "aggregate");

																	    Hashtable aggregateHostHash = new Hashtable();
																	    aggregateHostHash.put("1", "local");
																	    aggregateHostHash.put("2", "partner");

																	    if (aggregateVector != null && aggregateVector.size() > 0) {
																	        String aggregateType = "";
																	        String aggregateHost = "";
																	        for (int i = 0; i < aggregateVector.size(); i++) {
																	            aggregateVo = (NetAppAggregate) aggregateVector.get(i);
																	            if (aggregateVo.getAggrType() != null && aggregateTypeHash.get(aggregateVo.getAggrType()) != null) {
																	                aggregateType = (String) aggregateTypeHash.get(aggregateVo.getAggrType());
																	            } else if (aggregateVo.getAggrOwningHost() != null && aggregateHostHash.get(aggregateVo.getAggrOwningHost()) != null) {
																	                aggregateHost = (String) aggregateHostHash.get(aggregateVo.getAggrOwningHost());
																	            }
																	%>
																	<tr class="data">
																		<td>
																			<%=aggregateVo.getAggrIndex()%></td>
																		<td><%=aggregateVo.getAggrName()%></td>
																		<td><%=aggregateVo.getAggrFSID()%></td>
																		<td><%=aggregateHost%></td>
																		<td><%=aggregateVo.getAggrUUID()%></td>
																		<td><%=aggregateType%></td>
																		<td><%=aggregateVo.getAggrRaidType()%></td>
																		<td><%=aggregateVo.getAggrState()%></td>
																	</tr>
																	<%
																	    }
																	    }
																	%>
																</tbody>
															</table>
														</td>
													</tr>

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
																			����
																		</th>
																		<th>
																			��������
																		</th>
																		<th>
																			��������
																		</th>
																		<th>
																			��Ӧ��
																		</th>
																		<th>
																			��Ӧ������
																		</th>
																		<th>
																			�þ���ڿ�����Ŀ
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																	    NetAppSnapshot snapshotVo = null;
																	    Hashtable snapshotTypeHash = new Hashtable();
																	    snapshotTypeHash.put("1", "traditionalVolume");
																	    snapshotTypeHash.put("2", "flexibleVolume");
																	    snapshotTypeHash.put("3", "aggregate");

																	    if (snapshotVector != null && snapshotVector.size() > 0) {
																	        String snapshotType = "";
																	        for (int i = 0; i < snapshotVector.size(); i++) {
																	            snapshotVo = (NetAppSnapshot) snapshotVector.get(i);
																	            if (snapshotVo.getSlVType() != null && snapshotTypeHash.get(snapshotVo.getSlVType()) != null) {
																	                snapshotType = (String) snapshotTypeHash.get(snapshotVo.getSlVType());
																	            }
																	%>
																	<tr class="data">
																		<td>
																			<%=snapshotVo.getSlVIndex()%></td>
																		<td><%=snapshotVo.getSlVName()%></td>
																		<td><%=snapshotVo.getSlVMonth()%>��<%=snapshotVo.getSlVDay()%>��<%=snapshotVo.getSlVHour()%>ʱ<%=snapshotVo.getSlVMinutes()%>��
																		</td>
																		<td><%=snapshotVo.getSlVVolumeName()%></td>
																		<td><%=snapshotType%></td>
																		<td><%=snapshotVo.getSlVNumber()%></td>
																	</tr>
																	<%
																	    }
																	    }
																	%>
																</tbody>
															</table>
														</td>
													</tr>

													<tr>
														<td>
															<table class="tbEntity">
																<tr>
																	<td class="title">
																		����б�
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
																			����
																		</th>
																		<th>
																			��ʼ������
																		</th>

																		<th>
																			״̬
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																	    NetAppQuota quotaVo = null;
																	    Hashtable quotaStateHash = new Hashtable();
																	    quotaStateHash.put("1", "quotaStateOff");
																	    quotaStateHash.put("2", "quotaStateOn");
																	    quotaStateHash.put("3", "quotaStateInit");
																	    if (quotaVector != null && quotaVector.size() > 0) {
																	        String quotaState = "";
																	        for (int i = 0; i < quotaVector.size(); i++) {
																	            quotaVo = (NetAppQuota) quotaVector.get(i);
																	            if (quotaVo.getQuotaState() != null && quotaStateHash.get(quotaVo.getQuotaState()) != null) {
																	                quotaState = (String) quotaStateHash.get(quotaVo.getQuotaState());
																	            }
																	%>
																	<tr class="data">
																		<td>
																			<%=quotaVo.getQuotaId()%></td>
																		<td><%=quotaVo.getQuotaName()%></td>
																		<td><%=quotaVo.getQuotaInitPercent()%></td>
																		<td><%=quotaState%></td>
																	</tr>
																	<%
																	    }
																	    }
																	%>
																</tbody>
															</table>
														</td>
													</tr>

													<tr>
														<td>
															<table class="tbEntity">
																<tr>
																	<td class="title">
																		Plex�б�
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
																			����
																		</th>
																		<th>
																			plexPercentResyncing
																		</th>
																		<th>
																			״̬
																		</th>
																	</tr>
																</thead>
																<tbody>
																	<%
																	    NetAppPlex plexVo = null;
																	    Hashtable plexStateHash = new Hashtable();
																	    plexStateHash.put("1", "offline");
																	    plexStateHash.put("2", "resyncing");
																	    plexStateHash.put("3", "online");
																	    if (plexVector != null && plexVector.size() > 0) {
																	        String plexState = "";
																	        for (int i = 0; i < plexVector.size(); i++) {
																	            plexVo = (NetAppPlex) plexVector.get(i);
																	            if (plexVo.getPlexStatus() != null && plexStateHash.get(plexVo.getPlexStatus()) != null) {
																	                plexState = (String) plexStateHash.get(plexVo.getPlexStatus());
																	            }
																	%>
																	<tr class="data">
																		<td>
																			<%=plexVo.getPlexIndex()%></td>
																		<td><%=plexVo.getPlexName()%></td>
																		<td><%=plexVo.getPlexPercentResyncing()%></td>
																		<td><%=plexState%></td>
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