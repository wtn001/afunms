<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>

<%
	String rootPath = request.getContextPath();

	Vector systemVector = (Vector) request.getAttribute("system");
	Vector productVector = (Vector) request.getAttribute("productInfo");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	int alarmLevel = (Integer) request.getAttribute("alarmLevel");

	String collectTime = "";//�ɼ�ʱ��
	String sysDescr = "";//ϵͳ����
	String sysUpTime = "";//�豸����ʱ��
	String productType = "";//��Ʒ����
	String productVersion = "";//��Ʒ�汾
	String productId = "";//��ƷID
	String productVendor = "";//��Ʒ��Ӧ��
	String productModel = "";//��Ʒģ��
	String productFirmwareVersion = "";//����汾
	String productSerialNum = "";//��Ʒ���к�
	String productMachineType = "";//��������
	try {
		Systemcollectdata systemOm = (Systemcollectdata) systemVector.elementAt(0);
		Date date = systemOm.getCollecttime().getTime();
		collectTime = sdf.format(date);
		for (int i = 0; i < systemVector.size(); i++) {
			systemOm = (Systemcollectdata) systemVector.elementAt(i);
			if (systemOm.getSubentity().equals("sysDescr")) {
				sysDescr = systemOm.getThevalue();
			}
			if (systemOm.getSubentity().equals("sysUpTime")) {
				sysUpTime = systemOm.getThevalue();
			}
		}
		for (int j = 0; j < productVector.size(); j++) {
			systemOm = (Systemcollectdata) productVector.elementAt(j);
			if (systemOm.getSubentity().equals("productType")) {
				productType = systemOm.getThevalue();
			} else if (systemOm.getSubentity().equals("productVersion")) {
				productVersion = systemOm.getThevalue();
			} else if (systemOm.getSubentity().equals("productId")) {
				productId = systemOm.getThevalue();
			} else if (systemOm.getSubentity().equals("productVendor")) {
				productVendor = systemOm.getThevalue();
			} else if (systemOm.getSubentity().equals("productModel")) {
				productModel = systemOm.getThevalue();
			} else if (systemOm.getSubentity().equals("productFirmwareVersion")) {
				productFirmwareVersion = systemOm.getThevalue();
			} else if (systemOm.getSubentity().equals("productSerialNum")) {
				productSerialNum = systemOm.getThevalue();
			} else if (systemOm.getSubentity().equals("productMachineType")) {
				productMachineType = systemOm.getThevalue();
			}
		}

	} catch (Exception e) {
		e.printStackTrace();
	}

	sdf = new SimpleDateFormat("yyyy-MM-dd");
	String nowTime = sdf.format(new Date());
	String starttime = nowTime + " 00:00:00";
	String totime = nowTime + " 23:59:59";

	I_HostCollectData hostmanager = new HostCollectDataManager();

	String tmp = request.getParameter("id");
	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));

	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);

	String menuTable = (String) request.getAttribute("menuTable");

	String cpuAvg = "";//����Cpuƽ��������
	Hashtable cpuHashTable = new Hashtable();
	try {
		cpuHashTable = hostmanager.getCategory(host.getIpAddress(), "CPU", "Utilization", starttime, totime);
	} catch (Exception e) {
		e.printStackTrace();
	}
	if (cpuHashTable.get("avgcpucon") != null) {
		cpuAvg = (String) cpuHashTable.get("avgcpucon");
		System.out.print(cpuAvg+" ))))))))");

	}

	Hashtable ConnectUtilizationhash = new Hashtable();
	String pingAvg = "";//����ƽ����ͨ��
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization", starttime, totime);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		pingAvg = (String) ConnectUtilizationhash.get("avgpingcon");
		pingAvg = pingAvg.replace("%", "");
	}

	String ipaddress = host.getIpAddress();
	String picip = CommonUtil.doip(ipaddress);//ͼƬIp
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
															<table class="detail-data-body">
																<tr>
																	<td>
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="0">
																			<tr>
																				<td>
																					<jsp:include
																						page="/application/netapp/systeminfo_netApp.jsp">
																						<jsp:param name="rootPath" value="<%=rootPath%>" />
																						<jsp:param name="tmp" value="<%=tmp%>" />
																						<jsp:param name="sysDescr" value="<%=sysDescr%>" />
																						<jsp:param name="sysUpTime" value="<%=sysUpTime%>" />
																						<jsp:param name="collectTime"
																							value="<%=collectTime%>" />
																						<jsp:param name="cpuAvg" value="<%=cpuAvg%>" />
																						<jsp:param name="pingAvg" value="<%=pingAvg%>" />
																						<jsp:param name="picip" value="<%=picip%>" />
																						<jsp:param name="productType"
																							value="<%=productType%>" />
																						<jsp:param name="productVersion"
																							value="<%=productVersion%>" />
																						<jsp:param name="productId" value="<%=productId%>" />
																						<jsp:param name="productVendor"
																							value="<%=productVendor%>" />
																						<jsp:param name="productModel"
																							value="<%=productModel%>" />
																						<jsp:param name="productFirmwareVersion"
																							value="<%=productFirmwareVersion%>" />
																						<jsp:param name="productSerialNum"
																							value="<%=productSerialNum%>" />
																						<jsp:param name="productMachineType"
																							value="<%=productMachineType%>" />
																					</jsp:include>
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
