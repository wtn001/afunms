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
  String tmp = (String)request.getParameter("nodeid");
  HostNodeDao hostNodeDao = new HostNodeDao();
  if(tmp == null)tmp = (String)request.getParameter("tmp");
  HostNode node = hostNodeDao.loadHost(Integer.valueOf(tmp));
  hostNodeDao.close();
  String ipaddress = node.getIpAddress();
  String subtype = (String)request.getParameter("subtype");
  String category = (String)request.getParameter("category");
  String sys_oid = node.getSysOid();
  String flag1 = request.getParameter("flag"); 
  int check_category = node.getCategory();
%>


<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>

<script type="text/javascript" src="/afunms/detail/intstyle.js"></script>
<script language="javascript">
function synchronization(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/performancePanelAjaxManager.ajax?action=synchronization&nodeid=<%=tmp%>",
			success:function(data){
			     window.alert(data.option);
			}
		});
}

function synchronizationEmc(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/performancePanelAjaxManager.ajax?action=synchronizationEmc&nodeid=<%=tmp%>",
			success:function(data){
			      window.alert(data.option);
			}
		});
}




Ext.onReady(function(){  

	     	Ext.get("process").on("click",function(){
	        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
	        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=ipaddress%>";
            mainForm.submit();
	 	});	
	});
</script>


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
													<!--<li><img src="<%=rootPath%>/resource/image/toolbar/qxgl.gif">&nbsp;<a href="<%=rootPath%>/network.do?action=menucancelmanage&id=<%=tmp%>&flag=<%=flag1%>">取消管理</a></li>-->
													<li><img src="<%=rootPath%>/resource/image/toolbar/ping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/ping.jsp?ipaddress=<%=ipaddress%>","oneping", "height=400, width= 600, top=300, left=100")'>Ping</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/lygz.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress=<%=ipaddress%>","newtracerouter", "height=400, width= 600, top=300, left=100")'>路由跟踪</a></li>
													<%
														if(!"windows".equals(subtype)){
															if(check_category != 15){
													%>
													<!--<li><img src="<%=rootPath%>/resource/image/toolbar/webssh.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/webutil/WebSSH.swf?MOID=<%=ipaddress%>","webssh", "height=555, width= 644, top=0, left=0")'>Web SSH</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/webtel.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/webutil/webtelnet.swf?MOID=<%=ipaddress%>","webtelnet", "height=555, width= 644, top=0, left=0")'>Web Telnet</a></li>-->
													<li><img src="<%=rootPath%>/resource/image/toolbar/webtel.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/network.do?action=telnet&ipaddress=<%=ipaddress%>","onetelnet", "toolbar=no,height=0, width= 0, top=0, left= 0,scrollbars=yes")'>Web Telnet</a></li>
													<%
															}
														}
													%>
													<%
														if("network".equals(category)&&(node.getCollecttype()==1)){
	               											IpaddressPanelDao dao = new IpaddressPanelDao();
															IpaddressPanel panel = dao.loadIpaddressPanel(ipaddress);
															if(panel == null){
	               										%>
	               											<li><img src="<%=rootPath%>/resource/image/toolbar/snmpping.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/tool/remotePing.jsp?ipaddress=<%=ipaddress%>&community=<%=node.getWriteCommunity()%>","remotePing", "height=400, width=500, top=300, left=400")'>Snmp RemotePing</a></li><!-- snow add at 2010-5-28 -->
	               											<li><img src="<%=rootPath%>/resource/image/toolbar/sbmb.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/network.do?action=panelnodelist&jp=1&flag=1&id=<%=tmp%>&ipaddress=<%=ipaddress%>","panelfullScreenWindow", "toolbar=no,height=200,width=900" + ",scrollbars=no"+"screenX=0,screenY=0")'>编辑设备面板</a></li>
	               										<%
	               											}else{
	               												String filename = SysUtil.doip(ipaddress);
	               												String imageType = panel.getImageType();
	               												PanelModelDao panelModelDao = new PanelModelDao();
	               												PanelModel panelModel = panelModelDao.loadPanelModel(sys_oid,imageType);
	               												String pheight = panelModel.getHeight();
	               												String pwidth = panelModel.getWidth();
               											%>    
               												<li><img src="<%=rootPath%>/resource/image/toolbar/sbmb.gif">&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/panel/view/custom.jsp?filename=<%=filename%>&oid=<%=sys_oid%>&imageType=<%=imageType%>&ip=<%=ipaddress%>","panelfullScreenWindow", "toolbar=no,height=<%=pheight%>,width=<%=pwidth%>" + ",scrollbars=no"+"screenX=0,screenY=0")'>设备面板</a></li>
               										<%
               												}
               											}
         											%> 
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
	<% if("network".equals(category)&&(node.getCollecttype()==1 || node.getCollecttype()==10 )){
				if(check_category == 8){
						//防火墙
	%>
<tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">配置</td>
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
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgsbbq.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改设备标签</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgxtzsx.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改系统组属性</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xg_snmp_cs.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsnmp&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改SNMP参数</a></li>
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
													<li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/portconfig.do?action=nodeportlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>端口配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=firewall&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmport.do?action=list&ipaddress=<%=ipaddress%>&nodeid=<%=tmp%>&type=firewall&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>端口阀值设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=firewall&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>监视指标阀值设置</a></li>
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
	<%
	}else if(check_category == 15){
						//VMWARE
	%>
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
												    <li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='synchronization();'>数据同步</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=virtual&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/vmware.do?action=list&ipaddress=<%=ipaddress%>&nodeid=<%=tmp%>&type=firewall&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>虚拟机设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=virtual&subtype=<%=subtype%>&category=physical","oneping", "height=600, width= 1100, top=300, left=100,scrollbars=yes")'>物理机指标阀值设置</a></li>
												    <li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=virtual&subtype=<%=subtype%>&category=vmware","oneping", "height=600, width= 1100, top=300, left=100,scrollbars=yes")'>虚拟机指标阀值设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=virtual&subtype=<%=subtype%>&category=yun","oneping", "height=600, width= 1100, top=300, left=100,scrollbars=yes")'>群集指标阀值设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=virtual&subtype=<%=subtype%>&category=resource","oneping", "height=600, width= 1100, top=300, left=100,scrollbars=yes")'>云资源指标阀值设置</a></li>
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
		
	<%}else if(check_category == 14){ %>
	
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
												    <li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='synchronizationEmc();'>数据同步</a></li>
												<!--  
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=virtual&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/vmware.do?action=list&ipaddress=<%=ipaddress%>&nodeid=<%=tmp%>&type=firewall&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>虚拟机设置</a></li>
												 -->	
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=storage&subtype=<%=subtype%>&category=lun","oneping", "height=600, width= 1100, top=300, left=100,scrollbars=yes")'>Lun指标阀值设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=storage&subtype=<%=subtype%>&category=disk","oneping", "height=600, width= 1100, top=300, left=100,scrollbars=yes")'>Disk指标阀值设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=storage&subtype=<%=subtype%>&category=environment","oneping", "height=600, width= 1100, top=300, left=100,scrollbars=yes")'>环境指标阀值设置</a></li>
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
	
	<%
		}else{
	%>
<tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">配置</td>
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
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgsbbq.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改设备标签</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgxtzsx.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改系统组属性</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xg_snmp_cs.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsnmp&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改SNMP参数</a></li>
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
													<li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/portconfig.do?action=nodeportlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>端口配置</a></li>
													<!--<li><img src="<%=rootPath%>/resource/image/menu/power.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/envconfig.do?action=nodepowerlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>电源告警配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/fan.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/envconfig.do?action=nodefanlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>风扇告警配置</a></li>-->
													<li><img src="<%=rootPath%>/resource/image/toolbar/bfpzwj.gif" border=0>&nbsp;<a href="javascript:void(null)" onclick='window.open("<%=rootPath%>/vpntelnetconf.do?action=readyBackupConfig&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes")'>备份配置文件</a></li>
													<!-- 
													<li><img src="<%=rootPath%>/resource/image/toolbar/pzwjlb.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/cfgfile.do?action=shownodelist&ipaddress=<%=ipaddress%>","oneping", "height=500, width= 800, top=300, left=100")'>配置文件列表</a></li>
													      -->       
													<li><img src="<%=rootPath%>/resource/image/toolbar/pzwjlb.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/vpntelnetconf.do?action=showAllFile1&ip=<%=ipaddress%>&type=<%=subtype %>","oneping", "height=500, width= 800, top=300, left=100")'>配置文件列表</a></li>                                                                                                                   
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmport.do?action=list&ipaddress=<%=ipaddress%>&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>端口阀值设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>监视指标阀值设置</a></li>
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
	<%
		}
	} else if("network".equals(category)&&(node.getCollecttype()==3)){%>
	<tr>
		<td>
			<table class="tool-bar">
				<tr>
					<td>
						<table class="tool-bar-header">
		                	<tr>
			                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
			                	<td class="tool-bar-title">配置</td>
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
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgsbbq.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改设备标签</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xgxtzsx.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改系统组属性</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/xg_snmp_cs.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/network.do?action=ready_editsnmp&id=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>修改SNMP参数</a></li>
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
													<li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/portconfig.do?action=nodeportlist&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 1000, top=300, left=100,scrollbars=yes")'>端口配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/bfpzwj.gif" border=0>&nbsp;<a href="javascript:void(null)" onclick='window.open("<%=rootPath%>/vpntelnetconf.do?action=readyBackupConfig&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes")'>备份配置文件</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/pzwjlb.gif" border=0>&nbsp;<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/cfgfile.do?action=shownodelist&ipaddress=<%=ipaddress%>","oneping", "height=500, width= 800, top=300, left=100")'>配置文件列表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/glgz.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/nodesyslogrule.do?action=toolbarfilter&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>Syslog告警配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=tmp%>&type=net&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>监视指标阀值设置</a></li>
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
	<%} else{ %>
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
													<li><img src="<%=rootPath%>/resource/image/menu/zjjcjs.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostApply.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,height=500, width= 1000, top=100, left=100,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes")'>应用配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/zjjcjs.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/processgroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>进程监视配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/service.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/hostservicegroup.do?action=list&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","portScanWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")'>服务监视配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/cpfzylb.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/disk.do?action=toolbarlist&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>磁盘监视配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/glgz.gif" border=0 width=18>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/nodesyslogrule.do?action=toolbarfilter&nodeid=<%=tmp%>&ipaddress=<%=ipaddress%>","diskWindow","toolbar=no,width=1000,height=500,directories=no,status=no,scrollbars=yes,menubar=no")'>Syslog告警配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
													<a href="javascript:void(null)" onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=tmp%>&type=host&subtype=<%=subtype%>","oneping", "height=500, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项设置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif" border=0>&nbsp;
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
	<%}%>
	<%
		if("network".equals(category)&&(node.getCollecttype()==1)&&1==2){
	%>
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
													<li><img src="<%=rootPath%>/resource/image/menu/dkpz.gif" border=0>&nbsp;<a href="#" id="processport" onclick="#">端口配置</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/bfpzwj.gif" border=0>&nbsp;<a href="#"  onclick='window.open("<%=rootPath%>/vpntelnetconf.do?action=readyBackupConfig&id=<%=tmp%>&ipaddress=<%=ipaddress%>","oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes")'>备份配置文件</a></li>
													<li><img src="<%=rootPath%>/resource/image/toolbar/pzwjlb.gif" border=0>&nbsp;<a href="#" id="process1" onclick="#">配置文件列表</a></li>
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
	<%
		}
	%>
	
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
										}else  if(check_category == 15){
											//VMWARE
										%>
											<ul>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/vmware.do?action=showPingReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>可用性报表</a></li>
											<!--  	<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" border=0>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/vmware.do?action=showCapacityReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>系统信息报表</a></li>   -->
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/vmware.do?action=showResourceReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>资源报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/vmware.do?action=showVmwareReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>虚拟机报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/vmware.do?action=showPerReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>性能报表</a></li>
													<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/vmware.do?action=eventReportList&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>事件报表</a></li>
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
													<!--<li><img src="<%=rootPath%>/resource/image/menu/ywbb.gif" width=16>&nbsp;<a href="#" onclick='window.open("<%=rootPath%>/netreport.do?action=showConfigReport&ipaddress=<%=ipaddress%>&id=<%=tmp%>","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes")'>配置报表</a></li> -->
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
