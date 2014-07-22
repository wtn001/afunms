<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SystemConstant"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.topology.dao.HostInterfaceDao"%>

<%
    String rootPath = request.getContextPath();
    List list = (List) request.getAttribute("list");
    int rc = list.size();
    String actionlist = (String) request.getAttribute("actionlist");
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
  var delAction = "<%=rootPath%>/network.do?action=delete";
  var listAction = "<%=rootPath%>/network.do?action=<%=actionlist%>";
   function toListByNameasc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbynameasc";
     mainForm.submit();
  }
  function toListByNamedesc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbynamedesc";
     mainForm.submit();
  }
   function toListByIpasc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbyipasc";
     mainForm.submit();
  }
  function toListByIpdesc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbyipdesc";
     mainForm.submit();
  }
  function toListByBrIpasc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbybripasc";
     mainForm.submit();
  }
  function toListByBrIpdesc()
  {
     mainForm.action = "<%=rootPath%>/network.do?action=listbybripdesc";
     mainForm.submit();
  }
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=queryByCondition";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

function backUpRes()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=downloadnetworklistback";
      mainForm.submit();
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
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
           mainForm.action = "<%=rootPath%>/network.do?action=delete";
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
	<body id="body" class="body" onload="initmenu();">
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
																		<jsp:include page="../../common/page.jsp">
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
																		<INPUT type="button" class="formStyle" value="查询"
																			onclick=" return doQuery()">
																		&nbsp;&nbsp;
																		<b>展示样式 [<a
																			href="<%=rootPath%>/network.do?action=list&jp=1"><font
																				color="#397DBD">列表</font> </a> <a
																			href="<%=rootPath%>/equip/list.jsp"><font
																				color="#397DBD">图形</font> </a>]</b>
																		<a href="#" onclick="toEditBid()">权限设置</a> [
																		<a href="#" onclick="batchModifyMoniter('add')"><font
																			color="#397DBD">批量添加监视</font> </a>
																		<a href="#" onclick="batchModifyMoniter('remove')"><font
																			color="#397DBD">批量取消监视</font> </a>]
																	</td>
																	<td class="body-data-title" style="text-align: right;">
																		<img  alt='导出资源' style="CURSOR: hand;padding-right:8px;"
																			src="<%=rootPath%>/resource/image/backUpRes.gif"
																			border="0" onclick="return backUpRes();">
																		<img   alt='添加设备' style="CURSOR: hand;padding-right:8px;"
																			src="<%=rootPath%>/resource/image/addDevice.gif"
																			border="0" onclick="return toAdd();">
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
																		<table width="100%" height="100%">
																			<tr>
																				<td width="85%" align='center'
																					style="padding-left: 6px; font-weight: bold;">
																					<a href="#" onclick="toListByNameasc()">名称</a>
																				</td>
																				<td>
																					<img id="nameasc"
																						src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																						border="0" onclick="toListByNameasc()"
																						style="CURSOR: hand; margin-left: 4px;" />
																					<img id="namedesc"
																						src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																						border="0" onclick="toListByNamedesc()"
																						style="CURSOR: hand; margin-left: 4px;" />
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align="center" class="body-data-title" width="12%">
																		系统名称
																	</td>
																	<td align="center" class="body-data-title" width="13%">
																		<table width="100%" height="100%">
																			<tr>
																				<td width="85%" align='center'
																					style="padding-left: 6px; font-weight: bold;">
																					<a href="#" onclick="toListByIpasc()">IP地址</a>
																				</td>
																				<td>
																					<img id="ipasc"
																						src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																						border="0" onclick="toListByIpasc()"
																						style="CURSOR: hand; margin-left: 4px;" />
																					<img id="ipasc"
																						src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																						border="0" onclick="toListByIpdesc()"
																						style="CURSOR: hand; margin-left: 4px;" />
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align="center" class="body-data-title" width="12%">
																		<table width="100%" height="100%">
																			<tr>
																				<td width="85%" align='center'
																					style="padding-left: 6px; font-weight: bold;">
																					<a href="#" onclick="toListByBrIpasc()">MAC地址</a>
																				</td>
																				<td>
																					<img id="bripasc"
																						src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																						border="0" onclick="toListByBrIpasc()"
																						style="CURSOR: hand; margin-left: 4px;" />
																					<img id="bripdesc"
																						src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																						border="0" onclick="toListByBrIpdesc()"
																						style="CURSOR: hand; margin-left: 4px;" />
																				</td>
																			</tr>
																		</table>
																	</td>

																	<td align="center" class="body-data-title" width="10%">
																		型号
																	</td>

																	<td align="center" class="body-data-title" width="8%">
																		接口数量
																	</td>
																	<td align="center" class="body-data-title" width="4%">
																		监视
																	</td>
																	<td align="center" class="body-data-title" width="6%">
																		采集协议
																	</td>
																	<td align="center" class="body-data-title" width="3%">
																		操作
																	</td>
																</tr>
																<%
																    HostNode vo = null;
																    int startRow = jp.getStartRow();
																    for (int i = 0; i < rc; i++) {
																        String showItemMenu = "";
																        vo = (HostNode) list.get(i);
																        if (vo.getCategory() != 4 && vo.getEndpoint() == 0) {
																            showItemMenu = "111111000";
																        } else if (vo.getEndpoint() != 0) {
																            showItemMenu = "111110001";
																        } else {
																            showItemMenu = "111111110";
																        }
																        String mac = "--";
																        if (vo.getBridgeAddress() != null && !vo.getBridgeAddress().equals("null"))
																            mac = vo.getBridgeAddress();
																%>
																<tr>
																	<td align="center" class="body-data-list">
																		<INPUT type="checkbox" id="checkbox" name="checkbox"
																			value="<%=vo.getId()%>" class="noborder"><%=jp.getStartRow() + i%></td>
																	<td align="left" class="body-data-list"><%=vo.getAlias()%></td>
																	<%
																	    IpAliasDao ipdao = new IpAliasDao();
																	        List iplist = ipdao.loadByIpaddress(vo.getIpAddress());
																	        ipdao.close();
																	        ipdao = new IpAliasDao();
																	        IpAlias ipalias = ipdao.getByIpAndUsedFlag(vo.getIpAddress(), "1");
																	        ipdao.close();
																	        if (iplist == null)
																	            iplist = new ArrayList();
																	%>
																	<td align="left" class="body-data-list"><%=vo.getSysName()%></td>
																	<td align="center" class="body-data-list">
																		<table>
																			<tr>
																				<td>
																					<select style="width: 115px;"
																						name="ipalias<%=vo.getIpAddress()%>">
																						<option selected><%=vo.getIpAddress()%></option>
																						<%
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
																						onclick="modifyIpAliasajax('<%=vo.getIpAddress()%>');" />
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align="left" class="body-data-list">
																		<table>
																			<tr>
																				<td>
																					<%
																					    if (mac.length() >= 17) {
																					            String newmac = mac.replaceAll(",", "</option><option>");
																					            out.print("<select id='mac" + vo.getId() + "'>");
																					            out.print("<option>");
																					            out.print(newmac);
																					            out.print("</option>");
																					            out.print("</select>");
																					        } else {
																					            //out.print(mac);
																					            mac = "";
																					            out.print("<select id='mac" + vo.getId() + "' style='display:none;'></select>");
																					        }
																					%>
																				</td>
																				<td>
																					&nbsp;
																				</td>
																				<td align="right">
																					<img href="#"
																						src="<%=rootPath%>/resource/image/menu/xgmm.gif"
																						style="cursor: hand"
																						onclick="javascript:openWindow('<%=rootPath%>/topology/network/editMac.jsp?tagname=mac&id=<%=vo.getId()%>',350,200)" />
																					<!-- top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=yes,location=no, status=no -->
																				</td>
																			</tr>
																		</table>
																	</td>

																	<td align="center" class="body-data-list"><%=vo.getType()%></td>

																	<td align="center" class="body-data-list">
																		<%
																		    //接口数量
																		        int entityNumber = 0;
																		        HostInterfaceDao hostInterfaceDao = null;
																		        try {
																		            hostInterfaceDao = new HostInterfaceDao();
																		            entityNumber = hostInterfaceDao.getEntityNumByNodeid(vo.getId());
																		        } catch (RuntimeException e1) {
																		            e1.printStackTrace();
																		        } finally {
																		            hostInterfaceDao.close();
																		        }
																		%>
																		<%=entityNumber%>
																	</td>
																	<%
																	    if (vo.isManaged() == true) {
																	%>
																	<td align="center" class="body-data-list">
																		是
																	</td>
																	<%
																	    } else {
																	%>
																	<td align="center" class="body-data-list">
																		否
																	</td>
																	<%
																	    }
																	%>
																	<%
																	    String collectType = "";
																	        if (SystemConstant.COLLECTTYPE_SNMP == vo.getCollecttype()) {
																	            collectType = "SNMP";
																	        } else if (SystemConstant.COLLECTTYPE_PING == vo.getCollecttype()) {
																	            collectType = "PING";
																	        } else if (SystemConstant.COLLECTTYPE_REMOTEPING == vo.getCollecttype()) {
																	            collectType = "REMOTEPING";
																	        } else if (SystemConstant.COLLECTTYPE_SHELL == vo.getCollecttype()) {
																	            collectType = "代理";
																	        } else if (SystemConstant.COLLECTTYPE_SSH == vo.getCollecttype()) {
																	            collectType = "SSH";
																	        } else if (SystemConstant.COLLECTTYPE_TELNET == vo.getCollecttype()) {
																	            collectType = "TELNET";
																	        } else if (SystemConstant.COLLECTTYPE_WMI == vo.getCollecttype()) {
																	            collectType = "WMI";
																	        } else if (SystemConstant.COLLECTTYPE_DATAINTERFACE == vo.getCollecttype()) {
																	            collectType = "接口";
																	        }
																	%>
																	<td align="center" class="body-data-list"><%=collectType%></td>
																	<td align="center" class="body-data-list">
																		<input type="hidden" id="ipaddress" name="ipaddress"
																			value="<%=vo.getIpAddress()%>">
																		<input type="hidden" id="id" name="id"
																			value="<%=vo.getId()%>">
																		<img class="img"
																			src="<%=rootPath%>/resource/image/status.gif"
																			border="0" width=15 alt="右键操作">
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
				},{
					text :'取消管理',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelMng.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						cancelmanage(id);
					}
				},{
                    text :'添加管理',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addMn.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                       addmanage(id);
                    }
                },{
                    text :'取消末端',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelEndPoint.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        cancelendpoint(id);
                    }
                },{
                    text :'设置末端',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addEndPoint.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        addendpoint(id);
                    }
                },{
                    text :'远程Ping服务器',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/server.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        setCollectionAgreement(id,1);
                    }
                },{
                    text :'Telnet服务器',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/server.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        setCollectionAgreement(id,3);
                    }
                },{
                    text :'SSH服务器',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/server.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        setCollectionAgreement(id,4);
                    }
                },{
                    text :'取消所属协议',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelProtocol.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        setCollectionAgreement(id,0);
                    }
                }]
			});	
			
		//右键菜单方法开始
		function detail(node) {
			location.href =   "<%=rootPath%>/detail/dispatcher.jsp?id=" + node;
		}
		function edit(node) {
			location.href = "<%=rootPath%>/network.do?action=ready_edit&id=" + node;
		}
		function cancelmanage(node) {
			location.href =  "<%=rootPath%>/network.do?action=menucancelmanage&id=" + node;
		}
		function addmanage(node) {
			location.href =   "<%=rootPath%>/network.do?action=menuaddmanage&id=" + node;
		}
		function cancelendpoint(node) {
			location.href =   "<%=rootPath%>/network.do?action=menucancelendpoint&id=" + node;
		}
		function addendpoint(node) {
			location.href =  "<%=rootPath%>/network.do?action=menuaddendpoint&id=" + node;
		}
		function setCollectionAgreement(node, endpoint) {
			location.href =   "<%=rootPath%>/remotePing.do?action=setCollectionAgreement&id=" + node + "&endpoint=" + endpoint;
		}
		function deleteCollectionAgreement(node, endpoint) {
			location.href =   "<%=rootPath%>/remotePing.do?action=deleteCollectionAgreement&id=" + node + "&endpoint=" + endpoint;
		}
		//右键菜单方法结束
        </script>

</html>
