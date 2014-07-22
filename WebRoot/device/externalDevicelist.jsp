<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.device.model.ExternalDevice"%>
<%@page import="com.afunms.device.model.Tree"%>
<%@page import="com.afunms.device.util.DeviceUtil"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
 // DeviceUtil util=new DeviceUtil();
  
 // List list =util.getAllDevice();
  List list= (List)request.getAttribute("list");
  JspPage jp = (JspPage) request.getAttribute("page");
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/device/js/businessTree.js"></script>
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<link href="<%=rootPath%>/config/business/businessTree/businessTree.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/device.do?action=delete";
  var listAction = "<%=rootPath%>/device.do?action=externalDeviceList";
		
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
		
		<script>
			var dataArray = new Array();
			
			
			
			function show(){
				var tree = new Tree();
				tree.init(dataArray);
				tree.setNodeClick(nodeClick);
				tree.setNodeContextmenu(showMenuItem);
				tree.setImgPath("/afunms/config/business/businessTree/images/");
				tree.show("businessTree");
				
				var addMenuItem = new MenuItem('1' , '添加' , 'add.gif','');	
				var editMenuItem = new MenuItem('2' , '编辑' ,'edit.gif','');	
				var deleteMenuItem = new MenuItem('3' , '删除','delete.small.png','' );
				
				tree.addMenuItem(addMenuItem);	
				tree.addMenuItem(editMenuItem);	
				tree.addMenuItem(deleteMenuItem);	
				
				tree.createContextMenuItem();			
			}
			
			function nodeClick(div , node){
				changeSelectedNodeDiv(div);
				editNode.call(this , node);
				//this.closeContextMenuItem();
			}
			
			function showNodeDiv(){
				var showNodeDiv = document.getElementById("showNodeDiv");
				showNodeDiv.style.display = "inline";
			}
			
			function changeSelectedNodeDiv(div){
				var nodeNameDivs = document.getElementsByTagName("A");
				if(nodeNameDivs){
					var num = nodeNameDivs.length;
					for(var i = 0 ; i < num ; i++){
						if(nodeNameDivs[i].name = div.name){
							nodeNameDivs[i].className = 'noSelected';
						}
					}
				}
				div.className = 'selected';
			}
			var t=0;
			function setNodeDivValue(node , action){
				var id = document.getElementById("id");
				var pid = document.getElementById("pid");
				var osType = document.getElementById("osType");
				
				var brand = document.getElementById("brand");
				
				var deviceType = document.getElementById("deviceType");
				
				var supplier = document.getElementById("supplier");
				var insureder = document.getElementById("insureder");
				var deviceId = document.getElementById("deviceId");
				var ipaddress = document.getElementById("ipaddress");
				t=t+1;
    var frame = document.getElementById("frame");
        frame.src = "/index.jsp?c="+t;
				id.value = node.id;
				pid.value = node.pid;
				osType.value = node.osType;
				brand.value = node.brand;
				deviceType.value = node.deviceType;
				supplier.value = node.supplier;
				insureder.value = node.insureder;
				deviceId.value = node.deviceId;
				ipaddress.value = node.ipaddress;
				
 

				var save = document.getElementById("save");
				save.onclick = function(){
					mainForm.action = action;
					mainForm.submit();
				}
				
			}
			
			function showMenuItem(tree , node){
				changeSelectedNodeDiv(this);
				tree.setOnClickForMenuItemById.call(tree , '1' , addNode , node);
				tree.setOnClickForMenuItemById.call(tree , '2' , editNode , node);
				tree.setOnClickForMenuItemById.call(tree , '3' , deleteNode , node);
				tree.showContextMenuItem.call(tree , event.clientY , event.clientX);
			}
			function addNode(node){
				var addNode = new TreeNode('', '' , '', node.id);
				showNodeDiv(addNode);
				setNodeDivValue(addNode , "<%=rootPath%>/business.do?action=add");
				this.closeContextMenuItem();
			}
			function editNode(node){
				showNodeDiv(node);
				setNodeDivValue(node , "<%=rootPath%>/business.do?action=update");
				this.closeContextMenuItem();
			}
			function deleteNode(node){
				this.closeContextMenuItem();
				if(window.confirm("是否删除名称为：\"" + node.name + "\"的节点")){
					if(node.isHadChild){
						if(window.confirm("此节点含有子节点 ， 若此节点删除 ， 同时删除其子节点！！！是否继续")){
							mainForm.action = "<%=rootPath%>/business.do?action=delete&id=" + node.id;
							mainForm.submit();
						}
					}else{
						mainForm.action = "<%=rootPath%>/business.do?action=delete&id=" + node.id;
						mainForm.submit();
					}
				}
				
			}
		</script>
		
		
	</head>
	<body id="body" class="body" >
		<form id="mainForm" method="post" name="mainForm">
		<table id="body-container" class="body-container">
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
															&nbsp;资源 &gt;&gt; 设备维护 &gt;&gt; 设备列表
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
														<div><input name="id" id="id" type="hidden"></div>
																<div><input name="pid" id="pid" type="hidden"></div>
																
														<table>
																       <tr>
																         <td align="center" class="body-data-title" width="12%">
																           设备ID：
																           </td>
																        
																          <td align="center" class="body-data-title" width="12%">
																           设备IP：
																           </td>
																          
																           <td align="center" class="body-data-title" width="12%">
																           系统类型：
																           </td>
																           
																          <td align="center" class="body-data-title" width="12%">
																           品牌：
																           </td>
																          
																           <td align="center" class="body-data-title" width="12%">
																           设备类型：
																           </td>
																          
																           <td align="center" class="body-data-title" width="12%">
																           规格型号：
																           </td>
																          
																          <td align="center" class="body-data-title" width="12%">
																           供应商：
																           </td>
																           
																           <td align="center" class="body-data-title" width="12%">
																           承包单位：
																           </td>
																           
																       </tr>
																      <%if(list!=null){ 
																    	  for(int i=0;i<list.size();i++){
																       ExternalDevice device=(ExternalDevice)list.get(i);
																       
																      %>
																      <tr>
																      <td align="center" class="body-data-list">
																      <%= device.getId()%>
																      </td>
																       <td align="center" class="body-data-list">
																      <%= device.getIpaddress()%>
																      </td>
																       <td align="center" class="body-data-list">
																      <%= device.getOsType()%>
																      </td>
																       <td align="center" class="body-data-list">
																      <%= device.getBrand()%>
																      </td>
																       <td align="center" class="body-data-list">
																      <%= device.getDeviceType()%>
																      </td>
																      <td align="center" class="body-data-list">
																      <%= device.getSpecification()%>
																      </td>
																      <td align="center" class="body-data-list">
																      <%= device.getSupplier()%>
																      </td>
																      <td align="center" class="body-data-list">
																      <%= device.getInsureder()%>
																      </td>
																      </tr>
																      <%}} %>
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
			<table id="body-container" class="body-container">
				<tr>
					
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										
		        						<tr>
		        							<td align="left">
		        								<table id="content-body" class="content-body" align="left">
		        									<tr align="left">
														
														<td width="80%" align="right" valign="middle" >
															<div id="showNodeDiv" >
																
																<div>
																
																</div>
																
																<br>
															</div>
															<div>
															
															</div>
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
		        					</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>
