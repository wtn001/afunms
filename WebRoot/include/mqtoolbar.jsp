<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.dao.IpaddressPanelDao"%>
<%@page import="com.afunms.config.model.IpaddressPanel"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.config.dao.PanelModelDao"%>
<%@page import="com.afunms.config.model.PanelModel"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="com.afunms.application.model.*"%>
<%
  String rootPath = request.getContextPath();
  String id = request.getParameter("id");
  String dbPage = request.getParameter("dbPage");
  String subtype = request.getParameter("subtype");
  String toolsubtype = "";
  String nodeid = id;
  String ipaddress = request.getParameter("ipaddress");
  MQConfig mqConfig = (MQConfig) request.getAttribute("mqConfig");
 // String ipaddress=jBossConfig.getIpaddress();
%>

<script type="text/javascript">
	function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
                           <!--  
														<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">工具</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="tool-bar-body">
							<tr>
								<td>
									<table class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/toolbar/ping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/jboss.do?action=isOK&id=<%=id%>","oneping", "height=200, width= 500, top=300, left=100")'>可用性检测</a></li>
													<li><img src="<%=rootPath%>/resource/image/topo/button_refresh_bg.gif">&nbsp;<a href="#"  id="process" >数据同步</a></li>
													
												</ul>
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
						<table class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             										<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>
			-->
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">性能监视配置</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="tool-bar-body">
							<tr>
								<td>
									<table class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=nodeid%>&type=middleware&subtype=mq","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif">&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=nodeid%>&type=middleware&subtype=mq","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>指标阀值配置</a></li>
												    <li><img src="<%=rootPath%>/resource/image/menu/cpfzylb.gif" border=0 width=18>&nbsp;
												    <a href="#" onclick='window.open("<%=rootPath%>/mqchannel.do?action=channelList&jp=1&nodeid=<%=nodeid%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>通道告警配置</a></li>
												</ul>
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
						<table class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             										<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>
			
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">报表管理</td>
			                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
			       			</tr>
			        	</table>
					</td>
				</tr>
				<tr>
					<td>
						<table class="tool-bar-body">
							<tr>
								<td>
									<table class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
												    <li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/mq.do?action=showPingReport&ipaddress=<%=ipaddress%>&id=<%=nodeid%>" ,"portScanWindow","width=850,height=400,left=120,top=100,scrollbars=yes,resizable=yes")'>连通率报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/mq.do?action=allReport&type=monitor&ipaddress=<%=ipaddress%>&id=<%=nodeid%>" ,"portScanWindow","width=850,height=400,left=120,top=100,scrollbars=yes,resizable=yes")'>信息报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/mq.do?action=allReport&type=queue&ipaddress=<%=ipaddress%>&id=<%=nodeid%>" ,"portScanWindow","width=850,height=400,left=120,top=100,scrollbars=yes,resizable=yes")'>队列报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/mq.do?action=allReport&type=all&ipaddress=<%=ipaddress%>&id=<%=nodeid%>","portScanWindow","width=850,height=400,left=120,top=100,scrollbars=yes,resizable=yes")'>综合报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif">&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/mq.do?action=eventReport&ipaddress=<%=ipaddress%>&id=<%=nodeid%>","portScanWindow","width=950,height=450,left=120,top=100,scrollbars=yes,resizable=yes")'>事件报表</a></li>
												</ul>
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
						<table class="detail-content-footer">
							<tr>
								<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
             										<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
							</tr>
							
						</table>
					</td>
				</tr>
			</table>