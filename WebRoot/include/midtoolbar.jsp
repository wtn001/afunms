<%@page language="java" contentType="text/html;charset=GB2312"%>

<%
	String rootPath = request.getContextPath();
	String id = request.getParameter("id");
	String ipaddress = request.getParameter("ipaddress");
	String type = request.getParameter("type");
	String subtype = request.getParameter("subtype");
	String flag = request.getParameter("flag");
	String nodeid = id;
	
%>
<script type="text/javascript"
	src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript"
	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js"
	charset="gb2312"></script>
<script type="text/javascript"
	src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
	charset="gb2312"></script>
<link rel="stylesheet" type="text/css"
	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
	charset="gb2312" />
<script type="text/javascript">
Ext.onReady(function()
		{  
		
		setTimeout(function(){
			        Ext.get('loading').remove();
			        Ext.get('loading-mask').fadeOut({remove:true});
			    }, 250);
			    Ext.get("process").on("click",function(){
		  
		  Ext.MessageBox.wait('数据加载中，请稍后.. ');   
		  mainForm.action = "<%=rootPath%>/dns.do?action=sychronizeData&id=<%=id%>&flag=<%=flag%>";
		  mainForm.submit();
		 });    
		});
</script>

<table class="tool-bar">
	<tr>
		<td>
			<table class="tool-bar-header">
				<tr>
					<td align="left" width="5">
						<img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5"
							height="29" />
					</td>
					<td class="tool-bar-title">
						工具
					</td>
					<td align="right">
						<img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5"
							height="29" />
					</td>
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
										<li>
											<img src="<%=rootPath%>/resource/image/toolbar/ping.gif">
											&nbsp;
											<a href="javascript:void(null)"
												onClick='window.open("<%=rootPath%>/<%=subtype %>.do?action=isOK&id=<%=id%>","oneping", "height=200, width= 500, top=300, left=100")'>可用性检测</a>
										</li>
										<li>
											<img
												src="<%=rootPath%>/resource/image/topo/button_refresh_bg.gif">
											&nbsp;
											<a href="#" id="process" >数据同步</a>
										</li>

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
					<td align="left" valign="bottom">
						<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5"
							height="12" />
					</td>
					<td align="right" valign="bottom">
						<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5"
							height="12" />
					</td>
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
					<td align="left" width="5">
						<img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5"
							height="29" />
					</td>
					<td class="tool-bar-title">
						性能监视配置
					</td>
					<td align="right">
						<img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5"
							height="29" />
					</td>
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
										<li>
											<img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif"
												border=0>
											&nbsp;
											<a href="javascript:void(null)"
												onClick='window.open("<%=rootPath%>/nodeGatherIndicators.do?action=list&nodeid=<%=nodeid%>&type=<%=type%>&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>性能监视项配置</a>
										</li>
										<li>
											<img src="<%=rootPath%>/resource/image/toolbar/jszbfzpz.gif">
											&nbsp;
											<a href="javascript:void(null)"
												onClick='window.open("<%=rootPath%>/alarmIndicatorsNode.do?action=list&nodeid=<%=nodeid%>&type=<%=type%>&subtype=<%=subtype%>","oneping", "height=600, width= 1000, top=300, left=100,scrollbars=yes")'>指标阀值配置</a>
										</li>
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
					<td align="left" valign="bottom">
						<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5"
							height="12" />
					</td>
					<td align="right" valign="bottom">
						<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5"
							height="12" />
					</td>
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
					<td align="left" width="5">
						<img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5"
							height="29" />
					</td>
					<td class="tool-bar-title">
						报表管理
					</td>
					<td align="right">
						<img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5"
							height="29" />
					</td>
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
										<li>
											<img src="<%=rootPath%>/resource/image/menu/ywbb.gif">
											&nbsp;
											<a href="#"
												onclick='window.open("<%=rootPath%>/<%=subtype%>.do?action=showPingReport&ipaddress=<%=ipaddress%>&id=<%=id%>" ,"portScanWindow","width=900,height=450,left=120,top=100 ,scrollbars=yes,resizable=yes")'>可用性报表</a>
										</li>
										<li>
											<img src="<%=rootPath%>/resource/image/menu/ywbb.gif">
											&nbsp;
											<a href="#"
												onclick='window.open("<%=rootPath%>/<%=subtype%>.do?action=allReport&ipaddress=<%=ipaddress%>&id=<%=id%>","portScanWindow","width=900,height=450,left=120,top=100 ,scrollbars=yes,resizable=yes")'>综合报表</a>
										</li>
										<li>
											<img src="<%=rootPath%>/resource/image/menu/ywbb.gif">
											&nbsp;
											<a href="#"
												onclick='window.open("<%=rootPath%>/<%=subtype%>.do?action=eventReport&ipaddress=<%=ipaddress%>&id=<%=id%>","portScanWindow","width=900,height=450,left=120,top=100,scrollbars=yes,resizable=yes")'>事件报表</a>
										</li>
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
					<td align="left" valign="bottom">
						<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5"
							height="12" />
					</td>
					<td align="right" valign="bottom">
						<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5"
							height="12" />
					</td>
				</tr>

			</table>
		</td>
	</tr>
</table>