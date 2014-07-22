<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.SystemConstant"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  
  int rc = list.size();
	String actionlist=(String)request.getAttribute("actionlist");
  JspPage jp = (JspPage)request.getAttribute("page");
%>
<%
String menuTable = (String) request.getAttribute("menuTable");
%>

<html>
	<head>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet"
			type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<meta http-equiv="Page-Enter"
			content="revealTrans(duration=x, transition=y)">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css"
			rel="stylesheet">
		<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet"
			type="text/css">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css"
			type="text/css">
		<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet"
			type="text/css">
		
		<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/perform.do?action=delete";
  var listAction = "<%=rootPath%>/perform.do?action=<%=actionlist%>";
  
  function toDelete()
  {
  if(confirm('是否确定删除这条记录?')) {
      mainForm.action = "<%=rootPath%>/perform.do?action=delete";
      mainForm.submit();
      }
  }
  
   function toListByNameasc()
  {
     mainForm.action = "<%=rootPath%>/perform.do?action=listbynameasc";
     mainForm.submit();
  }
  function toListByNamedesc()
  {
     mainForm.action = "<%=rootPath%>/perform.do?action=listbynamedesc";
     mainForm.submit();
  }
   function toListByIpasc()
  {
     mainForm.action = "<%=rootPath%>/perform.do?action=listbyipasc";
     mainForm.submit();
  }
  function toListByIpdesc()
  {
     mainForm.action = "<%=rootPath%>/perform.do?action=listbyipdesc";
     mainForm.submit();
  }
  function toListByBrIpasc()
  {
     mainForm.action = "<%=rootPath%>/perform.do?action=listbybripasc";
     mainForm.submit();
  }
  function toListByBrIpdesc()
  {
     mainForm.action = "<%=rootPath%>/perform.do?action=listbybripdesc";
     mainForm.submit();
  }
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/perform.do?action=find";
     mainForm.submit();
  }
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/performance/index.jsp";
     else
        window.location = "<%=rootPath%>/performance/port.jsp";
  }
  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/perform.do?action=ready_add";
      mainForm.submit();
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
		 }
     else {
		 
		 //alert("选择了设备数为"+k);
		 mainForm.target="editall";
		 window.open("","editall","toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")
		 mainForm.action='<%=rootPath%>/perform.do?action=editall';
		 mainForm.submit();
		
		
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
	function showMenu(id,nodeid,ip,showItemMenu)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        return false;
	    }
	    else{
	    	popMenu(itemMenu,100,showItemMenu);
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    
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
	            this.style.background="#99CCFF";
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
	    pop.show(event.clientX-1,event.clientY,width,rowCount*30,document.body);
	    return true;
	}
	function detail()
	{
	    location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/perform.do?action=ready_edit&id="+node;
		//window.open('/nms/netutil/ping.jsp?ipaddress='+ipaddress);
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/perform.do?action=menucancelmanage&id="+node;
	}
	function addmanage()
	{
		location.href="<%=rootPath%>/perform.do?action=menuaddmanage&id="+node;
	}
	function cancelendpoint()
	{
		location.href="<%=rootPath%>/perform.do?action=menucancelendpoint&id="+node;
	}
	function addendpoint()
	{
		location.href="<%=rootPath%>/perform.do?action=menuaddendpoint&id="+node;
	}
	function setCollectionAgreement(endpoint)
	{
		location.href="<%=rootPath%>/remotePing.do?action=setCollectionAgreement&id="+node + "&endpoint="+endpoint;
	}
	function deleteCollectionAgreement(endpoint)
	{
		location.href="<%=rootPath%>/remotePing.do?action=deleteCollectionAgreement&id="+node + "&endpoint="+endpoint;
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

</script>
	</head>
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
		onload="initmenu();">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin;font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.edit()">
						编辑
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.cancelmanage()">
						取消管理
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.addmanage()">
						添加管理
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.cancelendpoint()">
						取消末端
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.addendpoint()">
						设置为末端
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setCollectionAgreement(1)">
						设置为远程Ping服务器
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setCollectionAgreement(3)">
						设置Telnet服务器
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.setCollectionAgreement(4)">
						设置SSH服务器
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.deleteCollectionAgreement(0)">
						取消所属协议
					</td>
				</tr>
			</table>
		</div>
		<!-- 右键菜单结束-->
		<form method="post" name="mainForm">
			<table border="0" id="table1" cellpadding="0" cellspacing="0"
				width=100%>
				<tr>
					<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title"><b>资源 >> 设备维护 >> 设备列表</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr> 
							<tr>
							<td>
							<table width="100%" cellpadding="0" cellspacing="1" >
							<tr>
						    <td bgcolor="#ECECEC" width="80%" align="center">
												<jsp:include page="../common/page.jsp">
													<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
													<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
												</jsp:include>
											    </td>
        </tr>
		</table>
		</td>
		</tr> 
										 <tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="70%" align='left'>
				&nbsp;&nbsp;&nbsp;&nbsp;<B>查询:</B>
												<SELECT name="key" style="width=100">
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
												<INPUT type="text" name="value" width="15" class="formStyle">
												<INPUT type="button" class="formStyle" value="查询"
													onclick=" return doQuery()">
												&nbsp;&nbsp;<b>展示样式 [<a href="<%=rootPath%>/perform.do?action=list&jp=1"><font color="#397DBD">列表</font></a> <a href="<%=rootPath%>/equip/list.jsp"><font color="#397DBD">图形</font></a>]</b>
												<a href="#" onclick="toEditBid()">权限设置</a>
											</td>
											<td bgcolor="#ECECEC" width="30%" align='right'>
											<a href="<%=rootPath%>/perform.do?action=downloadnetworklistfuck" target="_blank"><img name="selDay1" alt='导出EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">导出EXCEL</a>&nbsp;&nbsp;&nbsp;&nbsp;
												<a href="#" onclick="toAdd()">添加</a>
												<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
											</td>
										</tr>
								</table>
		  						</td>
									</tr>
								</table>
		  						</td>                       
        						</tr>

										<tr>
											<td colspan="2">
												<table cellspacing="1" cellpadding="0" width="100%">
													<tr class="microsoftLook0" height=28>
														<th width='5%'>
															<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()" class=noborder>序号
														</th>
														<th width='10%'>
															<table width="100%" height="100%">
																<tr>
																	<td width="85%" align='center' style="padding-left:6px;font-weight:bold;"><a href="#" onclick="toListByNameasc()">名称</a></td>
																	<td>
																		<img id="nameasc" src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																			border="0" onclick="toListByNameasc()" style="CURSOR:hand;margin-left:4px;" />
																			<img id="namedesc" src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																			border="0" onclick="toListByNamedesc()" style="CURSOR:hand;margin-left:4px;" />
																		
																	</td>
																</tr>
															</table>
														</th>
														<th width='10%'>
															<table width="100%" height="100%">
																<tr>
																	<td width="85%" align='center' style="padding-left:6px;font-weight:bold;"><a href="#" onclick="toListByIpasc()">IP地址</a></td>
																	<td>
																		<img id="ipasc" src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																			border="0" onclick="toListByIpasc()" style="CURSOR:hand;margin-left:4px;" />
																			<img id="ipasc" src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																			border="0" onclick="toListByIpdesc()" style="CURSOR:hand;margin-left:4px;" />
																		
																	</td>
																</tr>
															</table>
														</th>
														<th width='10%'>
															<table width="100%" height="100%">
																<tr>
																	<td width="85%" align='center' style="padding-left:6px;font-weight:bold;"><a href="#" onclick="toListByBrIpasc()">MAC</a></td>
																	<td>
																		<img id="bripasc" src="<%=rootPath%>/resource/image/microsoftLook/asc.gif"
																			border="0" onclick="toListByBrIpasc()" style="CURSOR:hand;margin-left:4px;" />
																		<img id="bripdesc" src="<%=rootPath%>/resource/image/microsoftLook/desc.gif"
																			border="0" onclick="toListByBrIpdesc()" style="CURSOR:hand;margin-left:4px;" />
																	</td>
																</tr>
															</table>
														</th>													
														<th width='10%'>
															子网掩码
														</th>
														<th width='10%'>
															型号
														</th>
														<th width='20%'>
															系统OID
														</th>
														<th width='5%'>
															监视
														</th>
														<th width='5%'>
															采集协议
														</th>
														<th width='5%'>
															操作
														</th>
													</tr>
													<%
														HostNode vo = null;
														int startRow = jp.getStartRow();
														for (int i = 0; i < rc; i++) {
															String showItemMenu = "";
															vo = (HostNode) list.get(i);
															if(vo.getCategory()!=4&&vo.getEndpoint()==0){
																showItemMenu = "111111000";
															}else if(vo.getEndpoint()!=0){
																showItemMenu = "111110001";
															}else{
																showItemMenu = "111111110";
															}
															String mac="--";
															if(vo.getBridgeAddress() != null && !vo.getBridgeAddress().equals("null"))mac = vo.getBridgeAddress();
													%>
													<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25>
														<td>
															<INPUT type="checkbox" class=noborder name=checkbox
																value="<%=vo.getId()%>" class=noborder>
															<font color='blue'><%=startRow + i%>
															</font>
														</td>
														<!--<a href="<%=rootPath%>/perform.do?action=read&id=<%=vo.getId()%>">-->
														<td align='center'>
															<%=vo.getAlias()%>
														</td>
														<!--</a>-->
														<td align='center'>
															<%=vo.getIpAddress()%>
														</td>
														<td align='center'>
															<%=mac%>
														</td>
														<td align='left'>
															<%=vo.getNetMask()%>
														</td>
														<td align='center'>
															<%=vo.getType()%>
														</td>
														<td align='center'>
															<%=vo.getSysOid()%>
														</td>
														<%
														if (vo.isManaged() == true) {
														%>
														<td align='center'>
															是
														</td>
														<%
														} else {
														%>
														<td align='center'>
															否
														</td>
														<%
														}
														%>
														<%
														//String collectAgreement = "";
														//if (vo.getEndpoint() == 0) {
														//	collectAgreement = "snmp";
														//}else if(vo.getEndpoint() == 1){
														//	collectAgreement = "Ping 服务器";	
														//}else if(vo.getEndpoint() == 2){
														//	collectAgreement = "Ping 节点";	
														//}else if(vo.getEndpoint() == 3){
														//	collectAgreement = "Telnet";	
														//}else if(vo.getEndpoint() == 4){
														//	collectAgreement = "sh";	
														//}
														
														String collectType = "";
														if(SystemConstant.COLLECTTYPE_SNMP == vo.getCollecttype()){
															collectType = "SNMP";
														}else if(SystemConstant.COLLECTTYPE_PING == vo.getCollecttype()){
															collectType = "PING";
														}else if(SystemConstant.COLLECTTYPE_REMOTEPING == vo.getCollecttype()){
															collectType = "REMOTEPING";
														}else if(SystemConstant.COLLECTTYPE_SHELL == vo.getCollecttype()){
															collectType = "SHELL";
														}else if(SystemConstant.COLLECTTYPE_SSH == vo.getCollecttype()){
															collectType = "SSH";
														}else if(SystemConstant.COLLECTTYPE_TELNET == vo.getCollecttype()){
															collectType = "TELNET";
														}else if(SystemConstant.COLLECTTYPE_WMI == vo.getCollecttype()){
															collectType = "WMI";
														}else if(SystemConstant.COLLECTTYPE_DATAINTERFACE == vo.getCollecttype()){
															collectType = "接口";
														}
														%>
														<td align='center'>
															<%=collectType%>
														</td>
														<td>
															&nbsp;&nbsp;
															<img src="<%=rootPath%>/resource/image/status.gif"
																border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getIpAddress()%>','<%=showItemMenu%>') alt="右键操作">
															<!--<a href="<%=rootPath%>/perform.do?action=ready_edit&id=<%=vo.getId()%>">
												<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></a>-->


														</td>
													</tr>
													<%
													}
													%>
												</table>
											</td>
										</tr>
								<tr>
					              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                  <tr>
					                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
					                    <td></td>
					                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
					                  </tr>
					              </table></td>
					            </tr>	
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>
