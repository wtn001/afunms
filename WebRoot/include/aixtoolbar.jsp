<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.dao.IpaddressPanelDao"%>
<%@page import="com.afunms.config.model.IpaddressPanel"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.config.dao.PanelModelDao"%>
<%@page import="com.afunms.config.model.PanelModel"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%
  String rootPath = request.getContextPath();
  String tmp = (String)request.getParameter("tmp");
  HostNodeDao hostNodeDao = new HostNodeDao();
  HostNode node = hostNodeDao.loadHost(Integer.valueOf(tmp));
  hostNodeDao.close();
  String ipaddress = (String)request.getParameter("ipaddress");
  String subtype = (String)request.getParameter("subtype");
  String category = (String)request.getParameter("category");
  String sys_oid = (String)request.getParameter("sys_oid");
%>

<table class="container-main-tool">
	<tr>
		<td>
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
													<li><img src="<%=rootPath%>/resource/image/toolbar/qxgl.gif">&nbsp;<a href="<%=rootPath%>/network.do?action=menucancelmanage&id=<%=tmp%>">取消管理</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/ping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/ping.jsp?ipaddress=<%=ipaddress%>","oneping", "height=400, width= 500, top=300, left=100")'>Ping</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/webtel.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/network.do?action=telnet&ipaddress=<%=ipaddress%>","onetelnet", "height=0, width= 0, top=0, left= 0")'>Web Telnet</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/lygz.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress=<%=ipaddress%>","newtracerouter", "height=400, width= 500, top=300, left=100")'>路由跟踪</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/webssh.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/webutil/WebSSH.swf?MOID=<%=ipaddress%>","webssh", "height=555, width= 644, top=0, left=0")'>Web SSH</a></li>
												</ul>
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
	<tr>
		<td>
			<table id="tool-bar" class="tool-bar">
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
						<table id="tool-bar-body" class="tool-bar-body">
							<tr>
								<td>
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/zjjcjs.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/processgroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>进程监视配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/service.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostservicegroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>服务监视配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/cpfzylb.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/disk.do?action=toolbarlist&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>磁盘监视配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/glgz.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/nodesyslogrule.do?action=toolbarfilter&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>Syslog告警配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/glgz.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/errpt.do?action=toolbarlist&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>Errpt告警配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=host&subtype=<%=subtype%>","oneping", "height=500, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/zbqjfzylb.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=host&subtype=<%=subtype%>&ipaddress=<%=ipaddress %>","oneping", "height=500, width= 1000, top=300, left=100,scrollbars=yes")'>监视指标阀值设置</a></li>
												</ul>
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
	
	<tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">端口扫描</td>
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
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgsbbq.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=portscan&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>扫描端口</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgxtzsx.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=searchPortScanByIp&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>查看历史数据</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/dkfwjs.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/ipDistrictMatch.do?action=ready_addPortScan&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>添加扫描端口</a></li>
												</ul>
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
	<!-- 报表-->
	<tr>
		<td>
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
									<table id="tool-bar-body-list" class="tool-bar-body-list">
										<tr>
											<td>
											<%
											if(category.equals("host"))
											{
											 %>
												<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showPingReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>可用性报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showCapacityReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>性能报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showDiskReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>磁盘报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showHardwareReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>硬件报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showCompositeReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>综合报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostreport.do?action=showAnalyseReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>决策报表</a></li>
												</ul>
											<%
											}
											else
											{
											 %>
											 	<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showPingReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=470,scrollbars=yes,resizable=yes")'>可用性报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showEventReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>事件报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showCompositeReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>综合报表</a></li>
													<!--<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showConfigReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>配置报表</a></li>  -->
												</ul>
											 <%
											 }
											 %>
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
