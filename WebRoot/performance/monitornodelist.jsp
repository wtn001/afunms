<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.MonitorNodeDTO"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.ArrayList"%>

<%
    String rootPath = request.getContextPath();
    List list = (List) request.getAttribute("list");
	System.out.println(list.size()+"???????????????????????????????????????/");
    JspPage jp = (JspPage) request.getAttribute("page");

    String field = (String) request.getAttribute("field");
    String sorttype = (String) request.getAttribute("sorttype");
    if (sorttype == null || sorttype.trim().length() == 0) {
        sorttype = "";
    }
    String flag = (String) request.getAttribute("flag");
    String category = (String) request.getAttribute("category");

    String nameImg = "";

    String categoryImg = "";

    String ipaddressImg = "";

    String pingImg = "";

    String cpuImg = "";

    String memoryImg = "";

    String inutilhdxImg = "";

    String oututilhdxImg = "";

    String imgSrc = "";

    String cpuUsed = "0";
    String cpuUnUsed = "";
    if (cpuUsed.equals("0"))
        cpuUnUsed = "100";
    String memeryUsed = "0";

    String memeryUnUsed = "";

    if (memeryUsed.equals("0"))
        memeryUnUsed = "100";

    String virtualMemoryUsed = "0";

    String virtualMemoryUnUsed = "";
    String pingValue = "0";

    if ("desc".equals(sorttype)) {
        imgSrc = "/afunms/resource/image/btn_up2.gif";
    } else if ("asc".equals(sorttype)) {
        imgSrc = "/afunms/resource/image/btn_up1.gif";
    }

    if ("name".equals(field)) {
        nameImg = "<img src='" + imgSrc + "'>";
    }
    if ("category".equals(field)) {
        categoryImg = "<img src='" + imgSrc + "'>";
    }
    if ("ipaddress".equals(field)) {
        ipaddressImg = "<img src='" + imgSrc + "'>";
    }
    if ("ping".equals(field)) {
        pingImg = "<img src='" + imgSrc + "'>";
    }
    if ("cpu".equals(field)) {
        cpuImg = "<img src='" + imgSrc + "'>";
    }
    if ("memory".equals(field)) {
        memoryImg = "<img src='" + imgSrc + "'>";
    }
    if ("inutilhdx".equals(field)) {
        inutilhdxImg = "<img src='" + imgSrc + "'>";
    }
    if ("oututilhdx".equals(field)) {
        oututilhdxImg = "<img src='" + imgSrc + "'>";
    }

    String treeBid = (String) request.getAttribute("treeBid");
    DecimalFormat df = new DecimalFormat("0.00");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link
			href="<%=rootPath%>/common/contextmenu/skins/default/contextmenu.css"
			rel="stylesheet">
		<script src="<%=rootPath%>/common/contextmenu/js/jquery-1.8.2.min.js"
			type="text/javascript"></script>
		<script src="<%=rootPath%>/common/contextmenu/js/contextmenu.js"
			type="text/javascript"></script>
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/perform.do?action=delete";
  			var listAction = "<%=rootPath%>/perform.do?action=monitornodelist";
  			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/perform.do?action=ready_add";
				mainForm.submit();
			}
			  
			function doCancelManage(){  
     			mainForm.action = "<%=rootPath%>/perform.do?action=cancelmanage";
     			mainForm.submit();
	  		}
	  		
	  		function doQuery(){ 
	  			window.location.reload(true); 
  			}
  			
  			function doSearch(){  
				mainForm.action = "<%=rootPath%>/perform.do?action=monitornodelist";
     			mainForm.submit();
  			}
  			
  			function showCpu(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_cpu_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showMemery(type , id , ip){  
  			    if(type=="4"){
  			        CreateWindow("<%=rootPath%>/detail/host_memory_month.jsp?id=" + 2 + "&ip=" +ip);
  			    } else {
  			        CreateWindow("<%=rootPath%>/detail/net_memory_month.jsp?id=" + 2 + "&ip=" +ip);
  			    }
  			}
  			
  			function showPing(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_ping_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showFlux(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_flux_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function CreateWindow(url){
				msgWindow=window.open(url,"protypeWindow","toolbar=no,width=850,height=450,directories=no,status=no,scrollbars=no,menubar=no")
			}
			
			function showSort(fieldValue){  
				var field = document.getElementById('field');
				field.value = fieldValue;
				mainForm.action = "<%=rootPath%>/perform.do?action=monitornodelist";
     			mainForm.submit();
  			}
  			
  			//网络设备的ip地址
			function modifyIpAliasajax(ipaddress){
				var t = document.getElementById('ipalias'+ipaddress);
				var ipalias = t.options[t.selectedIndex].text;//获取下拉框的值
				$.ajax({
						type:"GET",
						dataType:"json",
						url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
						success:function(data){
							window.alert("修改成功！");
						}
					});
			}
			
		</script>
	</head>
	<body id="body" class="body" leftmargin="0" topmargin="0">
		<div id="showDiv" onmouseover="outDiv()"></div>
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">
			<input type=hidden id="category" name="category"
				value="<%=category%>">
			<input type=hidden id="treeBid" name="treeBid" value="<%=treeBid%>">
			<table id="body-container" class="body-container" height="100%">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content"
										class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
													<tr>
														<td align="left" width="5">
															<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																width="5" height="29" />
														</td>
														<td class="content-title">
															&nbsp;资源 >> 性能监视 >> 监视对象一览 >> 设备列表
														</td>
														<td align="right">
															<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																width="5" height="29" />
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="content-body" class="content-body">
													<tr>
														<td>
															<table>
																<tr>
																	<td>
																		<jsp:include page="../common/page.jsp">
																			<jsp:param name="curpage"
																				value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal"
																				value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<td class="body-data-title" style="text-align: left;">

																		&nbsp;
																		<B>查&nbsp;&nbsp;询:</B>
																		<SELECT name="key">
																			<OPTION value="alias" selected>
																				名称
																			</OPTION>
																			<OPTION value="ip_address">
																				IP地址
																			</OPTION>
																			<OPTION value="sys_oid">
																				系统OID
																			</OPTION>
																			<OPTION value="type">
																				型号
																			</OPTION>
																		</SELECT>
																		&nbsp;
																		<b>=</b>&nbsp;
																		<INPUT type="text" name="value" width="15"
																			class="formStyle">
																		<INPUT type="button" class="formStyle" value="查    询"
																			onclick=" return doSearch()">
																	</td>
																	<td class="body-data-title" style="text-align: right;">
																		<!--<INPUT type="button" value="添    加" onclick=" return toAdd()">-->
																		<INPUT type="button" value="删    除"
																			onclick=" return toDelete()">
																		<INPUT type="button" value="取消管理"
																			onclick=" return doCancelManage()">
																		<INPUT type="button" value="刷    新"
																			onclick=" return doQuery()">
																		&nbsp;&nbsp;&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<td align="center" class="body-data-title" width="60px">
																		<input type="hidden" id="field" name="field"
																			value="<%=field%>">
																		<input type="hidden" id="sorttype" name="sorttype"
																			value="<%=sorttype%>">
																		<INPUT type="checkbox" id="checkall" name="checkall"
																			onclick="javascript:chkall()">
																		序号
																	</td>
																	<td align="center" class="body-data-title" width="20%">
																		<a href="#" onclick="showSort('name')">名称</a><%=nameImg%></td>
																	<td align="center" class="body-data-title" width="10%">
																		<a href="#" onclick="showSort('category')">类型</a><%=categoryImg%></td>
																	<td align="center" class="body-data-title">
																		<a href="#" onclick="showSort('ipaddress')">IP地址</a><%=ipaddressImg%></td>
																	<td align="center" class="body-data-title">
																		<a href="#" onclick="showSort('ping')">可用性</a><%=pingImg%></td>
																	<%
																	    if (category != null && !category.equals("net_storage") && !category.equals("net_vmware") && !"net_virtual".equals(category)) {
																	%>
																	<td align="center" class="body-data-title">
																		<a href="#" onclick="showSort('cpu')">CPU(%)</a><%=cpuImg%></td>
																	<td align="center" class="body-data-title">
																		<a href="#" onclick="showSort('memory')">物理内存(%)</a><%=memoryImg%></td>
																	<%
																	    if (category.equals("net_server")) {
																	%>
																	<td align="center" class="body-data-title">
																		<a href="#" onclick="showSort('virtualMemory')">虚拟内存(%)</a><%=memoryImg%></td>

																	<%
																	    }else{
																	%>

																	<td align="center" class="body-data-title">
																		<a href="#" onclick="showSort('inutilhdx')">出口(KB/S)</a><%=inutilhdxImg%></td>
																	<td align="center" class="body-data-title">
																		<a href="#" onclick="showSort('oututilhdx')">入口(KB/S)</a><%=oututilhdxImg%></td>
																	<%
																	    }
																	    } else if ("net_vmware".equals(category) || "net_virtual".equals(category)) {
																	%>
																	<td align="center" class="body-data-title" width="10%">
																		<a href="#">物理机个数</a>
																	</td>
																	<td align="center" class="body-data-title" width="10%">
																		<a href="#">虚拟机个数</a>
																	</td>
																	<td align="center" class="body-data-title" width="10%">
																		<a href="#">云资源</a>
																	</td>
																	<td align="center" class="body-data-title" width="10%">
																		<a href="#">存储</a>
																	</td>
																	<%
																	    }
																	%>
																	<td align="center" class="body-data-title">
																		接口
																	</td>
																	<td align="center" class="body-data-title">
																		详情
																	</td>
																</tr>
																<%
																    if (list != null && list.size() > 0) {
																        for (int i = 0; i < list.size(); i++) {
																            MonitorNodeDTO monitorNodeDTO = (MonitorNodeDTO) list.get(i);
																            pingValue = monitorNodeDTO.getPingValue();
																            cpuUsed = monitorNodeDTO.getCpuValue();
																            cpuUnUsed = String.valueOf(100 - Double.valueOf(cpuUsed));
																            memeryUsed = monitorNodeDTO.getMemoryValue();
																            memeryUnUsed = String.valueOf(100 - Double.valueOf(memeryUsed));
																            virtualMemoryUsed = monitorNodeDTO.getVirtualMemoryValue();
																            virtualMemoryUnUsed = String.valueOf(100 - Double.valueOf(virtualMemoryUsed));
																            String cpuValueColor = monitorNodeDTO.getCpuValueColor();
																            String memeryValueColor = monitorNodeDTO.getMemoryValueColor();
																            String virtualMemeryValueColor = monitorNodeDTO.getVirtualMemoryValueColor();
																            String status = monitorNodeDTO.getStatus();
																            String statusImg = "";
																            if ("1".equals(status)) {
																                statusImg = "alarm_level_1.gif";
																            } else if ("2".equals(status)) {
																                statusImg = "alarm_level_2.gif";
																            } else if ("3".equals(status)) {
																                statusImg = "alert.gif";
																            } else {
																                statusImg = "status_ok.gif";
																            }
																%>
																<tr>
																	<td align="center" class="body-data-list">
																		<INPUT type="checkbox" id="checkbox" name="checkbox"
																			value="<%=monitorNodeDTO.getId()%>"><%=jp.getStartRow() + i%></td>
																	<td
																		background="<%=rootPath%>/resource/image/topo/<%=statusImg%>"
																		class="performance-list-status">
																		<a
																			href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=monitorNodeDTO.getId()%>&flag=1">
																			<span id='detailType_net<%=monitorNodeDTO.getId()%>'
																			onmouseover="showDetailInfo('net<%=monitorNodeDTO.getId()%>')"
																			onmousemove="moveDiv(event)" onmouseout="outDiv()">
																				<%=monitorNodeDTO.getAlias()%> </span> <span
																			id='detailTypeflag_net<%=monitorNodeDTO.getId()%>'></span>
																		</a>
																	</td>
																	<td align="center" class="body-data-list" width="75px"><%=monitorNodeDTO.getCategory()%></td>
																	<td align="left" class="body-data-list" width="140px">
																		<table>
																			<tr>
																				<td>
																					<select style="width: 115px;"
																						name="ipalias<%=monitorNodeDTO.getIpAddress()%>">
																						<option selected><%=monitorNodeDTO.getIpAddress()%></option>
																						<%
																						    IpAliasDao ipdao = new IpAliasDao();
																						            List iplist = ipdao.loadByIpaddress(monitorNodeDTO.getIpAddress());
																						            ipdao.close();
																						            ipdao = new IpAliasDao();
																						            IpAlias ipalias = ipdao.getByIpAndUsedFlag(monitorNodeDTO.getIpAddress(), "1");
																						            ipdao.close();
																						            if (iplist == null)
																						                iplist = new ArrayList();
																						            for (int j = 0; j < iplist.size(); j++) {
																						                IpAlias voTemp = (IpAlias) iplist.get(j);
																						%>
																						<option
																							<%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>
																							selected <%} %>><%=voTemp.getAliasip()%></option>
																						<%
																						    }
																						%>
																					</select>
																				</td>
																				<td>
																					&nbsp;
																				</td>
																				<td>
																					<img href="#"
																						src="<%=rootPath%>/resource/image/menu/xgmm.gif"
																						style="cursor: hand"
																						onclick="modifyIpAliasajax('<%=monitorNodeDTO.getIpAddress()%>');" />
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align="center" class="body-data-list" width="100px">
																		<table>
																			<tr>
																				<td align="center" width="50%"><%=pingValue%>%
																				</td>
																				<td align="center" width="50%">
																					<img src="<%=rootPath%>/resource/image/a_xn.gif"
																						onclick='showPing("<%=monitorNodeDTO.getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")'
																						width=15>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<%
																	    if (category != null && !category.equals("net_storage") && !category.equals("net_vmware") && !"net_virtual".equals(category)) {
																	%>
																	<td align="center" class="body-data-list" width="110px">
																		<table>
																			<tr>
																				<td><%=cpuUsed%>%
																				</td>
																				<td width=150>
																					<table border=1 height=15 width="100%"
																						bgcolor=#ffffff>
																						<tr>
																							<td width="<%=cpuUsed%>%"
																								bgcolor="<%=cpuValueColor%>"></td>
																							<td width="<%=cpuUnUsed%>%" bgcolor=#ffffff></td>
																						</tr>
																					</table>
																				</td>
																				<td>
																					<img src="<%=rootPath%>/resource/image/a_xn.gif"
																						onclick='showCpu("<%=monitorNodeDTO.getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")'
																						width=15>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align="center" class="body-data-list" width="110px">
																		<table>
																			<tr>
																				<td><%=memeryUsed%>%
																				</td>
																				<td width=150>
																					<table border=1 height=15 width="100%"
																						bgcolor=#ffffff>
																						<tr>
																							<td width="<%=memeryUsed%>%"
																								bgcolor="<%=memeryValueColor%>"></td>
																							<td width="<%=memeryUnUsed%>%" bgcolor=#ffffff></td>
																						</tr>
																					</table>
																				</td>
																				<td>
																					<img src="<%=rootPath%>/resource/image/a_xn.gif"
																						onclick='showMemery("<%=monitorNodeDTO.getSubtype()%>" , "<%=monitorNodeDTO.getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")'
																						width=15>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<%
																	    if (category.equals("net_server")) {
																	%>
																	<td align="center" class="body-data-list" width="110px">
																		<table>
																			<tr>
																				<td><%=virtualMemoryUsed%>%
																				</td>
																				<td width=150>
																					<table border=1 height=15 width="100%"
																						bgcolor=#ffffff>
																						<tr>
																							<td width="<%=virtualMemoryUsed%>%"
																								bgcolor="<%=virtualMemeryValueColor%>"></td>
																							<td width="<%=virtualMemoryUnUsed%>%"
																								bgcolor=#ffffff></td>
																						</tr>
																					</table>
																				</td>
																				<td>
																					<img src="<%=rootPath%>/resource/image/a_xn.gif"
																						onclick='showMemery("<%=monitorNodeDTO.getSubtype()%>" , "<%=monitorNodeDTO.getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")'
																						width=15>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<%
																	    }else{
																	%>
																	<td align="center" class="body-data-list"
																		width="120px;">
																		<table>
																			<tr>
																				<td align="center" width="50%"><%=monitorNodeDTO.getInutilhdxValue()%></td>
																				<td align="center" width="50%">
																					<img src="<%=rootPath%>/resource/image/a_xn.gif"
																						onclick='showFlux("<%=monitorNodeDTO.getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")'
																						width=15>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align="center" class="body-data-list"
																		width="120px;">
																		<table>
																			<tr>
																				<td align="center" width="50%"><%=monitorNodeDTO.getOututilhdxValue()%></td>
																				<td align="center" width="50%">
																					<img src="<%=rootPath%>/resource/image/a_xn.gif"
																						onclick='showFlux("<%=monitorNodeDTO.getId()%>" , "<%=monitorNodeDTO.getIpAddress()%>")'
																						width=15>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<%
																	    }
																	    } else if ("net_vmware".equals(category) || "net_virtual".equals(category)) {
																	                String num = monitorNodeDTO.getNum();
																	%>
																	<td align="center" class="body-data-list">
																		<%=monitorNodeDTO.getHostnum()%>
																	</td>
																	<td align="center" class="body-data-list">
																		<%=monitorNodeDTO.getVMnum()%>
																	</td>
																	<td align="center" class="body-data-list">
																		<%=monitorNodeDTO.getPoolnum()%>
																	</td>
																	<td align="center" class="body-data-list">
																		<%
																		    if (Double.parseDouble(num) >= 1024) {
																		                    out.println(df.format(Double.parseDouble(num) / 1024) + "TB");
																		                } else {
																		                    out.println(num + "GB");
																		                }
																		%>
																	</td>
																	<%
																	    }
																	%>
																	<td align="center" class="body-data-list" width="70px;"><%=monitorNodeDTO.getEntityNumber()%></td>
																	<td align="center" class="body-data-list" width="50px;">
																		<input type="hidden" id="ipaddress" name="ipaddress"
																			value="<%=monitorNodeDTO.getIpAddress()%>">
																		<input type="hidden" id="id" name="id"
																			value="<%=monitorNodeDTO.getId()%>">
																		<img class="img"
																			src="<%=rootPath%>/resource/image/status.gif"
																			border="0" width=15 alt="右键操作">
																	</td>
																</tr>
																<%
																    }
																    }
																%>
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="content-footer" class="content-footer">
													<tr>
														<td>
															<table width="100%" border="0" cellspacing="0"
																cellpadding="0">
																<tr>
																	<td align="left" valign="bottom">
																		<img src="<%=rootPath%>/common/images/right_b_01.jpg"
																			width="5" height="12" />
																	</td>
																	<td></td>
																	<td align="right" valign="bottom">
																		<img src="<%=rootPath%>/common/images/right_b_03.jpg"
																			width="5" height="12" />
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
					</td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		$('.img').contextmenu({
				height:115,
				width:100,
				items : [{
					text :'详细信息',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						detail(id);
					}
				},{
					text :'ping',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/ping.gif',
					action: function(target){
						var ipaddress=$($(target).parent()).find('#ipaddress').val();
						ping(ipaddress);
					}
				},{
                    text :'traceroute',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/traceroute.gif',
                    action: function(target){
                    	var ipaddress=$($(target).parent()).find('#ipaddress').val();
                       traceroute(ipaddress);
                    }
                },{
                    text :'telnet',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/telnet.gif',
                    action: function(target){
                    	var ipaddress=$($(target).parent()).find('#ipaddress').val();
                        telnet(ipaddress);
                    }
                }]
			});	
			
		//右键菜单方法开始
			function detail(id)
			{
			    location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net"+id+"&flag=1";
			}
			function ping(ipaddress)
			{
				window.open("<%=rootPath%>/tool/ping.jsp?ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100");
			}
			function traceroute(ipaddress)
			{
				window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
			}
			function telnet(ipaddress)
			{
				window.open('<%=rootPath%>/perform.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
			}
		//右键菜单方法结束
        </script>

		<script type="text/javascript">

		// 获取设备详细信息
		function showDetailInfo(typeAndId) {
			var htmlTable = "资料正在加载中，请稍侯";
			$.ajax({
				type:"POST", 
				dataType:"json", 
				data:"id=" + typeAndId + "&nowtime=" + (new Date()), 
				url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=getDeviceDetailInfo", 
				success:function (data) {
					if (data.result != null && data.result == "true") {
						var cpuTableStr = getPercentTableStr(data.cpuvalue, data.cpuUnusedvalue, data.cpuValueColor);
						var memoryTableStr = getPercentTableStr(data.memoryvalue, data.memoryUnusedvalue, data.memoryValueColor);
						htmlTable = "<table id='outer' cellspacing=\"0\" cellpadding=\"0\"><tr><td colspan='2' id='detailInfoHead'>" 
							+ data.deviceName + "</td></tr><tr class='line'><td width='80px'>设备IP地址</td><td>" 
							+ data.ipaddress + "</td></tr><tr class='line'><td>设备类别：</td><td>" 
							+ data.nodeType + "</td></tr><tr class='line'><td>平均响应时间：</td><td>" 
							+ data.avgresponse + "ms</td></tr><tr class='line'><td style='height:20px;'>CPU利用率：</td><td>" 
							+ cpuTableStr + "</td></tr><tr class='line'><td>物理内存利用率：</td><td>" 
							+ memoryTableStr + "</td></tr></table>";
			  		 showDiv(htmlTable);
				}
			}});
		}
		
		//组装信息
		function getPercentTableStr(used, unused, color) {
			var tableStr = "<div><div style=\"float: left; width: 35px; padding-left: 3px;\">" 
				+ used + "%</div><div style=\"float: left; width: 62px;\"><table id=\"inner\" height=18 width=\"100%\" border=0 bgcolor=#ffffff cellspacing=0 cellpadding=0><tr><td width=" 
				+ used + "% bgcolor=" 
				+ color + "></td><td width=" 
				+ unused + "% ></td></tr></table></div></div>";
			return tableStr;
		}
		
		function showDiv(str) {
			var showDiv = document.getElementById("showDiv");
			showDiv.style.visibility = "visible";
			showDiv.innerHTML = str;
		}
		/*移动事件*/
		function moveDiv(e) {
			var divW = 250;
			var divH = 175;
			var browerHeight = document.documentElement.clientHeight; //浏览器高度
			var browerWidth = document.documentElement.clientWidth; //浏览器宽度
			var scrollTop = document.documentElement.scrollTop; //垂直滚动条距离顶部
			var scrollLeft = document.documentElement.scrollLeft;//水平滚动条距离左边
			var mouseY = e.clientY; //当前光标Y位置
			var mouseX = e.clientX; //当前光标X位置
			var left = mouseX + 10;
			var top = mouseY + 10;
			if (left + divW >= browerWidth) {
				left = left + scrollLeft - divW;
			}
			if (mouseY > (browerHeight - 200) && mouseY < browerHeight) {
				top = top + scrollTop - divH;
			}
			document.getElementById("showDiv").style.left = left + "px";
			document.getElementById("showDiv").style.top = top + "px";
		}
		/*离开事件*/
		function outDiv() {
			document.getElementById("showDiv").style.visibility = "hidden";
		}
		</script>

	</body>
</html>
