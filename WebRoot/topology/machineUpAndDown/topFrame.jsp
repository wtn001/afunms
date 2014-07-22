<%@page language="java" contentType="text/html;charset=GB2312"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%
   String rootPath = request.getContextPath();
   User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
   //System.out.println(current_user.getBusinessids());    
   String bids[] = current_user.getBusinessids().split(",");
   String fileName = (String)request.getParameter("xml");    
%>
<head>         
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="<%=rootPath%>/topology/machineUpAndDown/css/topo_style.css" rel="stylesheet" type="text/css" />
<title>topFrame</title>    
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript" src="js/toolbar.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript" src="js/customview.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/util.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/MachineUpAndDownRemoteService.js"></script>
<%
	out.println("<script type=\"text/javascript\">");
	// 判断全屏显示状态
	String fullscreen = request.getParameter("fullscreen");	
	if (fullscreen.equals("0")) 
	   fullscreen = "0";
	else 
	{
		fullscreen = "1";
		out.println("viewWidth = 0;");
	}

	// 取得用户权限---用来限制保存、刷新、编辑等操作
	boolean admin = false;
	String user = "admin";

	if (user.equalsIgnoreCase("admin") || user.equalsIgnoreCase("superuser")) {
		out.println("var admin = true;"); //为了－－编辑－－能正常使用
		admin = true;
	}
	else {
		out.println("var admin = false;");	
		admin = false;
	}
	out.println("</script>");
	
	String disable = "";//控制按钮是否激活
	if (!admin) {
		disable = "disabled=\"disabled\"";
	}
%>
<script type="text/javascript">
var fileName = "<%=fileName%>";
    window.parent.frames['mainFrame'].location.reload();//保存链路后刷新拓扑图
	var curTarget = "showMap.jsp?fullscreen=<%=fullscreen%>";
	var display = false;	    // 是否显示快捷列表
	var controller = false;		// 是否显示控制器
	
//保存视图
function saveFile() {
	if (!admin) {
		window.alert("您没有保存视图的权限！");
		return;
	}
	if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
	parent.mainFrame.saveFile();
}

// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("index.jsp?submapXml=<%=fileName%>&fullscreen=yes&user=<%=user%>", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}
var goon=0;
var tId;
var tIdend;
var tIdOne;
var tIdendOne;
var iscontinue;
//策略关机
function policyDown(){
    if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
	if (window.confirm("该操作会对当前策略组的设备进行策略关机，还继续吗？")) {
	    goon=1;
		parent.mainFrame.showLineInfo(1);
		var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
		tId = setInterval('parent.mainFrame.printedAll("'+url+'")',300);
		//调用远程关机方法
	    MachineUpAndDownRemoteService.policyDown(fileName, {
	        callback:function(data){
				if(data=="error"){
					alert("策略关机失败!");
				} else {
				    if(data){
				        stopDown();
				        alert("策略关机结束!");
				    }
				}
			}
	    });
	}
}
//终止单个设备关机
function stopDownOne(id){
    if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
    goon=0;
    parent.mainFrame.clearprintedOne(id);
    clearInterval(tIdOne);
    //clearInterval(tIdendOne);
}
//创建策略
function createDemoLink(){
    if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
    var objEntityAry = new Array();
    if(window.parent.frames['mainFrame'].objMoveAry!=null&&window.parent.frames['mainFrame'].objMoveAry.length>0){//框选
        objEntityAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry!=null&&window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrl选
        objEntityAry = window.parent.frames['mainFrame'].objEntityAry;
    } 
    if(objEntityAry==null||objEntityAry.length!=2){
        alert("请选择两个设备！");
        return;
    }
    
    var start_id = objEntityAry[0].id.replace("node_","");
    var end_id = objEntityAry[1].id.replace("node_","");
    var xml = "<%=fileName%>";
    var lineArr = window.parent.frames['mainFrame'].demoLineMoveAry;
    if(lineArr!=null&&lineArr.length>0){
        alert("选中的两台设备已经存在策略!");
        return;
    }
    var start_x_y=objEntityAry[0].style.left+","+objEntityAry[0].style.top;
    var end_x_y=objEntityAry[1].style.left+","+objEntityAry[1].style.top;
    var url="<%=rootPath%>/link.do?action=readyAddLine&xml="+xml+"&start_id="+start_id+"&end_id="+end_id+"&start_x_y="+start_x_y+"&end_x_y="+end_x_y;
    showModalDialog(url,window,'dialogwidth:510px; dialogheight:350px; status:no; help:no;resizable:0');
}
//清除关机状态
function refreshState(){
    if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
	var nodeArray = parent.mainFrame.getallnode();
    MachineUpAndDownRemoteService.refreshState(nodeArray,fileName, {
        callback:function(data){
			if(data=="error"){
				alert("清除失败!");
			} else {
			    if(data){
			        alert("清除成功!");
			    }
			}
		}
    });
    parent.mainFrame.location.reload();
}

//并行关机
function complicatDown(){
    if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
	if (window.confirm("该操作会对当前策略组的设备进行并行关机，还继续吗？")){
	    goon=1;
	    parent.mainFrame.showLineInfo(1);
	    //tId = setInterval('parent.mainFrame.printed()',800);演示
	    //tIdend = setInterval('parent.mainFrame.endprinted()',5000);演示
	    var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
		tId = setInterval('parent.mainFrame.printedAll("'+url+'")',300);
		var nodeArray = parent.mainFrame.getallnode();
	    MachineUpAndDownRemoteService.complicatDown(nodeArray,fileName, {
	        callback:function(data){
				if(data=="error"){
					alert("并行关机失败!");
				} else {
				    if(data){
				        stopDown();
				        alert("并行关机结束!");
				    }
				}
			}
	    });
	}
}
//终止关机
function stopDown(){
    if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
    goon=0;
    parent.mainFrame.clearprinted();
    MachineUpAndDownRemoteService.stopDown(fileName, {
        callback:function(data){
			if(data=="error"){
				alert("终止关机失败!");
			} else {
			    if(data){
			        alert("已终止关机!");
			    }
			}
		}
    });
    clearInterval(tId);
    clearInterval(tIdend);
}
//加载当前策略组
function rebuild(){
    if (fileName=="null") {
		window.alert("请选择策略组！");
		return;
	}
    if (window.confirm("该操作会重新重新加载当前策略组的设备，还继续吗？")) {
		window.location = "<%=rootPath%>/serverUpAndDown.do?action=reBuildSubMap&xml=<%=fileName%>";
		alert("操作成功!");
        parent.location.reload();
	}
}
// 显示视图控制器
function showController(flag) {

	var result;
	if (flag == false)
		controller = false;
	if (controller) {
		result = parent.mainFrame.showController(controller);
		      
		if (result == false) {
			window.alert("您没有选择视图，无控制器可用");
			return;
		}
			
		//document.all.controller.value = "关闭控制器";
		document.all.controller.title = "关闭显示框内的视图控制器";
		controller = false;
	}
	else {
		result = parent.mainFrame.showController(controller);
		
		if (result == false) {
			window.alert("您没有选择视图，无控制器可用");
			return;
		}

		//document.all.controller.value = "开启控制器";
		document.all.controller.title = "开启显示框内的视图控制器";
		controller = true;
	}
}
</script>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#CEDFF6">
<form name="topviewForm">
<table width="100%" height="25" border="0" cellspacing="0" cellpadding="0" style="padding:0px;border-top:#CEDFF6 1px solid;border-left:#CEDFF6 1px solid;border-right:#CEDFF6 1px solid;border-bottom:#D6D5D9 1px solid;background-color:#F5F5F5;">
  <tr>
    <td>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/common/images/menubg.jpg">
	  <tr>
		<td width="56"><input type="button" name="create1" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" value="保存视图" <%=disable%>/></td>
		<td width="56"><input type="button" name="create2" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:createDemoLink();" value="创建策略"/></td>
		<td width="56"><input type="button" name="create3" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:rebuild();" value="加载设备"/></td>
		<td width="56"><input type="button" name="create4" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:policyDown();" value="策略关机"/></td>
		<td width="56"><input type="button" name="create5" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:complicatDown();" value="并行关机"/></td>
        <td width="56"><input type="button" name="create6" class="button_create6_out" onmouseover="javascript:buttonCreate6Over();" onmouseout="javascript:buttonCreate6Out();" onclick="javascript:stopDown();" value="中止关机"/></td>
		<td width="56"><input type="button" name="create7" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshState();" value="清除关机状态"/></td>
		<td width="56">
	<%if (fullscreen.equals("0")) {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:gotoFullScreen();" value="全屏观看视图"/>
	<%}else {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:window.parent.close();" value="关闭当前窗口"/>
	<%}%>
		</td>
		<td width="56"><input type="button" name="controller" class="button_controller_out" onmouseover="javascript:buttonControllerOver();" onmouseout="javascript:buttonControllerOut();" onclick="javascript:showController();" value="关闭视图控制器"/></td>
		<td width="100">
		</td>
		<td width="30" bgcolor="#ffffff">图例:</td><td width="50" bgcolor="yellow">正在关机</td>
		<td width="50" bgcolor="green">正常关机</td>
		<td width="50" bgcolor="red">关机失败</td>
		<td width="50" bgcolor="#8E8E8E">连接异常</td>
		<td width="50" bgcolor="blue">手动关机</td>
		<td width="50" bgcolor="#CEDFF6">无操作</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  </tr>
	</table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
