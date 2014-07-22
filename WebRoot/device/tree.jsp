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
<%@page import="com.afunms.device.model.Cabinet"%>

<%@page import="com.afunms.device.model.Tree"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  Hashtable hashtable = (Hashtable)request.getAttribute("deviceHashtable");
  Hashtable cabinetHashtable = (Hashtable)request.getAttribute("cabinetHashtable");
  
  
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/device/js/businessTree.js"></script>
		<link href="<%=rootPath%>/config/business/businessTree/businessTree.css" rel="stylesheet" type="text/css"/>
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
		
		<script>
			var dataArray = new Array();
			
			<%
			
			if(list!=null){
				for(int i = 0 ; i < list.size() ; i ++){
					Tree tree = (Tree)list.get(i);
					int externalDeviceId=tree.getExternalDeviceId();
					String name="";
					if(tree.getExternalDeviceId()!=0&&hashtable.containsKey(tree.getExternalDeviceId())){
						ExternalDevice device=(ExternalDevice)hashtable.get(tree.getExternalDeviceId());
						externalDeviceId=device.getId();
						name=device.getDeviceId();
					}else if(cabinetHashtable.containsKey(tree.getCabinetId())){
						Cabinet cabinet=(Cabinet)cabinetHashtable.get(tree.getCabinetId());
						name=cabinet.getIpaddress();
						}
					
					
					
					%>
					dataArray[<%=i%>] = ['<%=tree.getId()%>' ,'<%=tree.getPid().trim()%>','<%=tree.getExternalDeviceId()%>','<%=name%>','<%=tree.getCabinetId()%>'];
					
					<%
				}
			}
			
			%>
			
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
				var freshMenuItem = new MenuItem('4' , '刷新','edit.gif','' );
				//if(dataArray[])
				tree.addMenuItem(addMenuItem);	
				tree.addMenuItem(editMenuItem);	
				tree.addMenuItem(deleteMenuItem);	
				tree.addMenuItem(freshMenuItem);
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
        frame.src = "<%=rootPath%>/device.do?action=externalDeviceList";
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
				tree.setOnClickForMenuItemById.call(tree , '4' , freshNode , node);
				tree.showContextMenuItem.call(tree , event.clientY , event.clientX);
			}
			function addNode(node){
				var addNode = new TreeNode(node.id, node.pid,node.deviceId,node.name,node.cabinetId);
				if(node.deviceId==0){
				 var frame = document.getElementById("frame");
                      frame.src = "<%=rootPath%>/device/addExternalDevice.jsp?id="+node.id+"&cabinetId="+node.cabinetId;
				}else{
				alert("请在机柜上添加,无法在设备上添加！")
				}
				this.closeContextMenuItem();
			}
			function editNode(node){
			      if(node.deviceId==0){
			      var frame = document.getElementById("frame");
                   frame.src = "<%=rootPath%>/device.do?action=externalDeviceListById&id="+node.cabinetId;
			      }else{
				   var frame = document.getElementById("frame");
				   
                      frame.src = "<%=rootPath%>/device/ready_editExternalDevice.jsp?id="+node.deviceId;
                      }
				this.closeContextMenuItem();
			}
			function deleteNode(node){
				this.closeContextMenuItem();
				if(window.confirm("是否删除名称为：\"" + node.name + "\"的节点")){
					if(node.isHadChild){
						if(window.confirm("此节点含有子节点 ， 若此节点删除 ， 同时删除其子节点！！！是否继续")){
							mainForm.action = "<%=rootPath%>/device.do?action=deleteCabinetById&cabinetId=" + node.cabinetId;
							mainForm.submit();
						}
					}else{
						mainForm.action = "<%=rootPath%>/device.do?action=deleteDeviceById&id=" + node.deviceId;
						mainForm.submit();
					}
				}
				
			}
			function freshNode(node){
			window.location.reload();
			}
		</script>
		
		
	</head>
	<body id="body" class="body" onload="initmenu();show();">
		<form id="mainForm" method="post" name="mainForm">
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 设备管理 >> 外部设备管理 >> 资源树 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td align="left">
		        								<table id="content-body" class="content-body" align="left">
		        									<tr align="left">
														<td width="20%" align="left" valign="top" >
															<div id="businessTree"></div>
														</td>
														<td width="80%" align="right" valign="middle" >
															
															<div>
															<iframe id="frame" src="<%=rootPath %>/device.do?action=externalDeviceList" width="100%" height="500px" scrolling="yes">
															
															</iframe> 
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
