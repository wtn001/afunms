<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.polling.PollingEngine"%>

<%
	String rootPath = request.getContextPath();
    String id = (String) request.getParameter("id");
	//String  commands = (String) request.getAttribute("commands");
	String type = (String) request.getParameter("type");
	//Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
	String devicetype = (String) request.getParameter("devicetype");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<link href="<%=rootPath%>/resource/css/div.css" rel="stylesheet"
			type="text/css" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/jquery/jquery-1.4.2.min.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/vpn/script.js"></script>

	</head>
	<body id="body" class="body">

		<form id="mainForm" method="post" name="mainForm">
			<div id="loading">
				<div class="loading-indicator">
					<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
						width="32" height="32" style="margin-right: 8px;" align="middle" />
					Loading...
				</div>
			</div>
			<div id="loading-mask" style=""></div>

			<table id="body-container" class="body-container">
				<tr>

					<td class="td-container-main">

						<table id="container-main" class="container-main">

							<tr>
								<td class="td-container-main-report">
									<table id="container-main-report" class="container-main-report">
										<tr>
											<td>

												<table id="report-content" class="report-content">
													<tr>
														<td>
															<table width="100%"
																background="<%=rootPath%>/common/images/right_t_02.jpg"
																cellspacing="0" cellpadding="0">
																<tr>
																	<td align="left">
																		<div class="noPrint">
																			<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																				width="5" height="29" />
																		</div>
																	</td>
																	<td class="content-title">
																		<b>vpn管理 >> 脚本命令配置</b>
																	</td>
																	<td align="right">
																		<div class="noPrint">
																			<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																				width="5" height="29" />
																		</div>
																	</td>
																</tr>
															</table>
														</td>

													</tr>

													<tr>
														<td>
															<table id="report-content-body" class="report-content-body">
																<tr>
																	<td align=center>

																		<table id="report-data-header" class="report-data-header">
																			<tr>
																				<td>
																					<table id="report-data-header-title" class="report-data-header-title">
																						<tr>
																							<td>
																								&nbsp;<font color="#DC143C">执行步骤：1.命令输入 >> 3.执行结果</font>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>

																	</td>
																</tr>

																<tr>
																	<td>
																		<div id="fragment-0">
																			<table height="350">
																				<tr>
																					<td>
																						&nbsp;
																					</td>
																					<td>
																						&nbsp;
																					</td>
																				</tr>


																				<tr>
																					<td></td>
																					<td align=center width="85%">

																						<table id="report-data-header" class="report-data-header">
																							<tr>
																								<td>
																									<table id="report-data-header-title" class="report-data-header-title">
																										<tr>
																											<td>
																											    &nbsp;<a href="#" onclick="loadFile('<%=rootPath%>')"><img src="<%=rootPath %>/resource/image/menu/load.gif"/>加载文件</a>
																												&nbsp;<a href="#" onclick="saveVpnFile('<%=rootPath%>','<%=type %>','<%=devicetype %>')"><img src="<%=rootPath%>/resource/image/menu/save.gif" />保存文件</a>
																											</td>
																										</tr>
																									</table>
																								</td>
																							</tr>
																						</table>

																					</td>
																				</tr>

																				<tr valign=top>

																					<td width="15%">
																						&nbsp;&nbsp;脚本命令模板：
																					</td>
																					<td width="85%">
																						<textarea id="commands" name="commands" rows="10" cols="85"></textarea>
																						<input type=hidden id="cmdid" name="cmdid">
																						<input type=hidden id="type" name="type" value="<%=type%>">
																						<input type=hidden id="devicetype" name="devicetype" value="<%=devicetype%>">
																					    <div class="content-title">注意：请核实确认命令的参数。</div>
																					</td>
																				</tr>
																				<tr>

																					<td colspan=2 align=center>
																						<br>
																						<input type="button" value="执 行" style="width: 50" onclick="exeCmd('<%=rootPath%>','<%=id %>','<%=type %>','<%=devicetype %>')">
																					</td>
																				</tr>
																				<tr>

																					<td colspan=2 align=center>
																						<br>
																						&nbsp;
																						<br>
																						<br>
																						<br>
																						<br>
																					</td>
																				</tr>
																			</table>

																		</div>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<div id="fragment-1" >
																<table bgcolor='#FFFFFF'>
																	<tr>
																		<td align=center>
																		    <div style="OVERFLOW-Y:auto;HEIGHT:295px;">

																			<table id="result">
																				<tr height=28>

																					<td class="report-data-body-title">
																						IP
																					</td>
																					<td class="report-data-body-title">
																						运行时间
																					</td>
																					<td class="report-data-body-title">
																						执行命令
																					</td>
																					<td class="report-data-body-title">
																						执行结果
																					</td>

																				</tr>
																			</table>
																			</div>
																		</td>
																	</tr>
																	<tr>
																		<td align=center>

																			<input type="button" value="上一步"onclick="secondFra2()">
																		   <input type="button" value="关 闭" onclick="javascript:window.close();">
																		</td>
																	</tr>
																</table>

															</div>

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

			<div id="BgDiv"></div>
			<div id="DialogDiv" style="display: none">
				<h2>
					命令配置
				</h2>
				<div class="form" id="result">
					正在提交，请稍后...
				</div>
				<div id="bottomDiv">
				</div>
			</div>
		</form>
	</body>
</html>