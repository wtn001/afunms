<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@ include file="/include/globe.inc"%>

<%
  String ctrlId = (String)request.getAttribute("ctrlId");
  String hideCtrlId = (String)request.getAttribute("hideCtrlId");
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  
  JspPage jp = (JspPage)request.getAttribute("page");
  
  String field = (String)request.getAttribute("field");
  String sorttype = (String)request.getAttribute("sorttype");
  if(sorttype == null || sorttype.trim().length() == 0)
  {
  	  sorttype = "";
  }
  
  String flag = (String)request.getAttribute("flag");
  String category = (String)request.getAttribute("category");
  
  String nameImg = "";
  
  String categoryImg = "";
  
  String ipaddressImg = "";
  
  String pingImg = "";
  
  String cpuImg = "";
  
  String memoryImg = "";
  
  String inutilhdxImg = "";
  
  String oututilhdxImg = "";
  
  String imgSrc = "";
  
  if("desc".equals(sorttype))
  {
  	  imgSrc = "/afunms/resource/image/btn_up2.gif";
  }else if("asc".equals(sorttype)){
  	  imgSrc = "/afunms/resource/image/btn_up1.gif";
  }
  
  if("name".equals(field)){
  	  nameImg = "<img src='" + imgSrc + "'>";
  }
  if("category".equals(field)){
  	  categoryImg = "<img src='" + imgSrc + "'>";
  }
  if("ipaddress".equals(field)){
  	  ipaddressImg = "<img src='" + imgSrc + "'>";
  }
  if("ping".equals(field)){
  	  pingImg = "<img src='" + imgSrc + "'>";
  }
  if("cpu".equals(field)){
  	  cpuImg = "<img src='" + imgSrc + "'>";
  }
  if("memory".equals(field)){
  	  memoryImg = "<img src='" + imgSrc + "'>";
  }
  if("inutilhdx".equals(field)){
  	  inutilhdxImg = "<img src='" + imgSrc + "'>";
  }
  if("oututilhdx".equals(field)){
  	  oututilhdxImg = "<img src='" + imgSrc + "'>";
  }
  
  
  //System.out.println("======type===========" + sorttype);
%>


<html>
	<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">    
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
		 	
			var show = true;
			var hide = false;
			//修改菜单的上下箭头符号
			function my_on(head,body)
			{
				var tag_a;
				for(var i=0;i<head.childNodes.length;i++)
				{
					if (head.childNodes[i].nodeName=="A")
					{
						tag_a=head.childNodes[i];
						break;
					}
				}
				tag_a.className="on";
			}
			function my_off(head,body)
			{
				var tag_a;
				for(var i=0;i<head.childNodes.length;i++)
				{
					if (head.childNodes[i].nodeName=="A")
					{
						tag_a=head.childNodes[i];
						break;
					}
				}
				tag_a.className="off";
			}
			//添加菜单	
			function initmenu()
			{
				var idpattern=new RegExp("^menu");
				var menupattern=new RegExp("child$");
				var tds = document.getElementsByTagName("div");
				for(var i=0,j=tds.length;i<j;i++){
					var td = tds[i];
					if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
						menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
						menu.init();		
					}
				}
			
			}
		</script>
		<script language="JavaScript">

			//公共变量
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*根据传入的id显示右键菜单
			*/
			function showMenu(id,nodeid,ip)
			{
				ipaddress=ip;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        popMenu(itemMenu,100,"100");
			    }
			    else
			    {
			        popMenu(itemMenu,100,"1111");
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    return false;
			}
			/**
			*显示弹出菜单
			*menuDiv:右键菜单的内容
			*width:行显示的宽度
			*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //创建弹出菜单
			    var pop=window.createPopup();
			    //设置弹出菜单的内容
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //获得弹出菜单的行数
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //循环设置每行的属性
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //如果设置该行不显示，则行数减一
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //设置是否显示该行
			        rowObjs[i].style.display=(hide)?"none":"";
			        //设置鼠标滑入该行时的效果
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //设置鼠标滑出该行时的效果
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //屏蔽菜单的菜单
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //选择右键菜单的一项后，菜单隐藏
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //显示菜单
			    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
			    return true;
			}
			function detail()
			{
			    location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net"+node;
			}
			function ping()
			{
				window.open("<%=rootPath%>/tool/ping.jsp?ipaddress="+ipaddress,"oneping", "height=400, width= 500, top=300, left=100");
				//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
			}
			function traceroute()
			{
				window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}
			function telnet()
			{
				window.open('<%=rootPath%>/network.do?action=telnet&ipaddress='+ipaddress,'onetelnet', 'height=0, width= 0, top=0, left= 0');
				//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
			}	
			function clickMenu()
			{
			}
		</script>
		<script type="text/javascript">
		
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/network.do?action=delete";
  			var listAction = "<%=rootPath%>/subscribeReportConfig.do?action=device_list";
  			
			function toAdd()
			{
				var checked_ctrl = $("#hostNodeTable tr");
				alert(checked_ctrl.length()+"!");
			}
			  
			function doCancelManage(){  
     				mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
     				mainForm.submit();
	  		}
	  		
	  		function doDelete(){  
     				mainForm.action = "<%=rootPath%>/network.do?action=delete";
     				mainForm.submit();
	  		}
	  		
	  		function doQuery(){  
				window.location = window.location;
  			}
  			
  			function doSearch(){  
				mainForm.action = "<%=rootPath%>/network.do?action=monitornodelist";
     			mainForm.submit();
  			}
  			
  			function showCpu(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_cpu_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showMemery(id , ip){  
				CreateWindow("<%=rootPath%>/detail/host_memory_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showPing(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_ping_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function showFlux(id , ip){  
				CreateWindow("<%=rootPath%>/detail/net_flux_month.jsp?id=" + 2 + "&ip=" +ip);
  			}
  			
  			function CreateWindow(url){
				msgWindow=window.open(url,"protypeWindow","toolbar=no,width=850,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
			}
			
			function showSort(fieldValue){  
				var field = document.getElementById('field');
				field.value = fieldValue;
				mainForm.action = "<%=rootPath%>/network.do?action=monitornodelist";
     			mainForm.submit();
  			}
		</script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js"
			charset="gb2312"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#saveBtn').bind('click',function(){
			if($("input:checked").length==0)
				return;
			var i=0;
			var checked_ctrl = $("#hostNodeTable tr:not(:first)").find("input:checked");
			//var hide_ctrl = $("#hostNodeTable tr:not(:first) td:nth-child(3)");
			var hide_ctrl = $("#hostNodeTable tr:not(:first)").find("input:checked").parent().parent();//find("input:hidden")
			//alert(hide_ctrl.length);
			//$('#showPnl').html(hide_ctrl.eq(1).text());
			//alert(checked_ctrl.length+"!");
			var checked_val="";
			var hide_val="";
			for(i=0;i<hide_ctrl.length;i++)
			{
				//alert(hide_ctrl.eq(i).find("td").eq(1).text());
				checked_val=checked_val+","+hide_ctrl.eq(i).find("td").eq(2).text();
				hide_val=hide_val+","+hide_ctrl.eq(i).find("td").eq(0).find("input").val();
			}
			var parent_checked_ctrl = parent.opener.document.getElementById("<%=ctrlId%>");
			var parent_hide_ctrl = parent.opener.document.getElementById("<%=hideCtrlId%>");
			//alert("<%=ctrlId%>");
			//alert("<%=hideCtrlId%>");
 			parent_checked_ctrl.value=checked_val;
 			parent_hide_ctrl.value = hide_val;
 			//alert(hide_val);
			window.close();
			//$('#showPnl').html(checked_val+","+hide_val);
		});
	});
</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.detail()">查看状态</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.ping();">ping</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.traceroute()">traceroute</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.telnet()">telnet</td>
			</tr>		
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.clickMenu()">关闭</td>
			</tr>
		</table>
		</div>
		<!-- 右键菜单结束-->
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden id="flag" name="flag" value="<%=flag%>">
			<input type=hidden id="category" name="category" value="<%=category%>">
			<input type=hidden id="ctrlId" name="ctrlId" value="<%=ctrlId%>">
			<input type=hidden id="hideCtrlId" name="hideCtrlId" value="<%=hideCtrlId%>">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title">  设备列表 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
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
													    			<td  class="body-data-title">
							    										<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
							  										 	</jsp:include>
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr> 
		        									<tr>
		        										<td>
		        											<table id="hostNodeTable">
		        												<tr>
		        													<input type="hidden" id="field" name="field" value="<%=field%>">
		        													<input type="hidden" id="sorttype" name="sorttype" value="<%=sorttype%>">
		        													<td align="center" class="body-data-title" width="5%"><!-- <INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()"> -->序号</td>
		        													<td align="center" class="body-data-title" width="11%"><a href="#" onclick="showSort('name')">IP地址</a><%=nameImg%></td>
		        													<td align="center" class="body-data-title" width="5%"><a href="#" onclick="showSort('category')">名称</a><%=categoryImg%></td>
		        												</tr>
		        												<%
					        									    if(list!=null&& list.size()>0)
					        									    {
					        									        for(int i = 0 ; i < list.size() ; i++)
					        									        {
					        									        	HostNode hostNode = (HostNode)list.get(i);
					        									        	//System.out.println(hostNode.getId()+"%%%%%%%%%%%%%%%%");
					        									%>
					        									            <tr <%=onmouseoverstyle%>>
						        									            <td align="center" class="body-data-list"><INPUT type="checkbox" id="checkbox" name="deviceId" value="<%=hostNode.getId()%>"><%=jp.getStartRow()+i %></td>
					        													<td align="center" class="body-data-list" id="<%=hostNode.getIpAddress()%>"><%=hostNode.getIpAddress()%></td>
					        													<td align="center" class="body-data-list" id="<%=hostNode.getAlias()%>"><%=hostNode.getAlias()%></td>
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
		        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
									                  			<tr>
									                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
									                    			<td></td>
									                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
									                  			</tr>
									              			</table>
		        										</td>
		        									</tr>
		        								</table>
		        							</td>
		        						</tr>
		        						<tr>
											<TD nowrap colspan="4" align=center>
												<br>
												<input type="button" value="保存" style="width:50" class="formStylebutton" id="saveBtn">&nbsp;&nbsp;
												<input type=reset class="formStylebutton" style="width:50" value="关闭" onclick="javascript:window.close()">
											</TD>	
										</tr>
		        					</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div id="showPnl"></div>
		</form>
	</body>
</html>
