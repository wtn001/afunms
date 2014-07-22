<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.device.model.AuditLog"%>
<%
    String rootPath = request.getContextPath();
    List list = (List) request.getAttribute("list");
    int rc = list.size();
    
    JspPage jp = (JspPage) request.getAttribute("page");
    String menuTable = (String) request.getAttribute("menuTable");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link
			href="<%=rootPath%>/common/contextmenu/skins/default/contextmenu.css"
			rel="stylesheet">
		<script src="<%=rootPath%>/common/contextmenu/js/jquery-1.8.2.min.js"
			type="text/javascript"></script>
		<script src="<%=rootPath%>/common/contextmenu/js/contextmenu.js"
			type="text/javascript"></script>

		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/device.do?action=delete";
  var listAction = "<%=rootPath%>/device.do?action=deviceAudit";
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }


  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/device.do?action=addCabinet";
      mainForm.submit();
  }
  function toDelete(){
  	var bidnum = document.getElementsByName("checkbox").length;
     var k=0;
     for(var p = 0 ; p <bidnum ; p++){
		if(document.getElementsByName("checkbox")[p].checked ==true){
			k=k+1;
		}
	}
	 if(k==0){
		 alert ("请选择设备！");
		 return;
	}
      if(confirm('是否删除选中设备?')) {
           mainForm.action = "<%=rootPath%>/device.do?action=deleteAuditById";
           mainForm.submit();
         }
  }
  
// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}
  function toEditBid(){
     var bidnum = document.getElementsByName("checkbox").length;
     var k=0;
     for(var p = 0 ; p <bidnum ; p++){
		if(document.getElementsByName("checkbox")[p].checked ==true){
			k=k+1;
		}
	}
     if(k==0){
		 alert ("请选择设备！");
	}else {
		 mainForm.target="editall";
		 window.open("","editall","toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")
		 mainForm.action='<%=rootPath%>/network.do?action=editall';
		 mainForm.submit();
	}
  }
  
  //批量添加监视 and 取消监视 
  	function batchModifyMoniter(modifyFlag){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("未选中");
        	return ;
        }
        mainForm.action = "<%=rootPath%>/network.do?action=batchModifyMoniter&eventids="+eventids+"&modifyFlag="+modifyFlag;
        mainForm.submit();
	}
</script>
		<script language="JavaScript" type="text/JavaScript">
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
function editMac(id,macstr){
	var selectObj = document.getElementById(id);
	if(macstr){
		var macs = macstr.split(",");
		for(var i=0;i<macs.length;i++){
			var option=document.createElement('option');
			option.text = macs[i];
			option.value = macs[i];
			
			try{
				selectObj.add(option,null);
			}catch(e){
				selectObj.add(option);
			}	
		}
	}
	if(selectObj.length){
		selectObj.style.display='';
	}else{
		selectObj.style.display='none';
	}
}
function openWindow(url,width,height){
	var iTop = (window.screen.availHeight-30-height)/2;
	var iLeft = (window.screen.availWidth-10-width)/2;
	window.open(url,'MAC地址编辑','width='+width+',height='+height+',left='+iLeft+',top='+ iTop +',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}
</script>
	</head>
	<body id="body" class="body" >
	
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td width="200" valign=top align=center>
						<%=menuTable%>
					</td>

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
															&nbsp;设备管理 &gt;&gt; 日志审计 &gt;&gt; 审计列表
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
																	
																	<td class="body-data-title" style="text-align: right;">
																		
																		
																		<img   alt='删除设备' style="CURSOR: hand;padding-right:8px;"
																			src="<%=rootPath%>/resource/image/deleteDevice.gif"
																			border="0" onclick="return toDelete();">
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													
													<tr>
														<td>
															<table>
																<tr>
																	<td align="center" class="body-data-title" width="6%">
																		<INPUT type="checkbox" id="checkall" name="checkall"
																			onclick="javascript:chkall()" class="noborder">
																		序号
																	</td>
																	<td align="center" class="body-data-title" width='13%'>
																		用户
																	</td>
																	<td align="center" class="body-data-title" width="12%">
																		IP
																	</td>
																	<td align="center" class="body-data-title" width="13%">
																		操作类型
																	</td>
																	<td align="center" class="body-data-title" width="12%">
																		修改时间
																	</td>

																</tr>
																<%
																
																AuditLog  vo = null;
																    int startRow = jp.getStartRow();
																    for (int i = 0; i < rc; i++) {
																        vo = (AuditLog) list.get(i);
																       
																%>
																<tr>
																	<td align="center" class="body-data-list">
																		<INPUT type="checkbox" id="checkbox" name="checkbox"
																			value="<%=vo.getId()%>" class="noborder"><%=jp.getStartRow() + i%></td>
																	<td align="left" class="body-data-list"><%=vo.getUsername()%></td>
																	
																	<td align="left" class="body-data-list"><%=vo.getIp()%></td>
																	<td align="center" class="body-data-list">
																		<%=vo.getOperateType()%>
																	</td>
																	<td align="left" class="body-data-list">
																		<%=vo.getDotime()%>
																	</td>

																	
																</tr>
																<%
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
	</body>
	<script type="text/javascript">  
			$('.img').contextmenu({
				height:235,
				width:120,
				items : [{
					text :'编辑',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						
						edit(id);
					}
				}]
			});	
			
		//右键菜单方法开始
		
		function edit(node) {
			location.href = "<%=rootPath%>/device.do?action=ready_update&id=" + node;
		}
		//右键菜单方法结束
        </script>

</html>
