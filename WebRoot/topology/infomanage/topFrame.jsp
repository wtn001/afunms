<%@page language="java" contentType="text/html;charset=GB2312"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.List"%>
<%@page import="com.afunms.machine.model.StoreyInfo"%>
<%@page import="com.afunms.machine.dao.StoreyInfoDao"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%
   String rootPath = request.getContextPath();
   User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
   //System.out.println(current_user.getBusinessids());
   String bids[] = current_user.getBusinessids().split(",");
   String fileName = (String)session.getAttribute(SessionConstant.CURRENT_FLOOR_VIEW);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="<%=rootPath%>/resource/css/topo_style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=rootPath%>/resource/css/top.css" type="text/css">
<title>topFrame</title>
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript" src="js/toolbar.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript" src="js/customview.js"></script>
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
    window.parent.frames['mainFrame'].location.reload();//保存链路后刷新拓扑图
	var curTarget = "showMap.jsp?fullscreen=<%=fullscreen%>";
	var display = false;	    // 是否显示快捷列表
	var controller = false;		// 是否显示控制器
	
function searchNode()
{	
	var ip = window.prompt("请输入需要搜索的信息点编码", "在此输入信息点编码");
	if (ip == null)
		return true;
	else if (ip == "在此输入信息点编码")
		return;

	var coor = window.parent.mainFrame.getNodeCoor(ip);
	if (coor == null)
	{
		var msg = "没有在图中搜索到编码为 "+ ip +" 的信息点。";
		window.alert(msg);
		return;
	}
	else if (typeof coor == "string")
	{
		window.alert(coor);
		return;
	}

	// 移动设备到中心标记处
	window.parent.mainFrame.moveMainLayer(coor);
}

// 保存视图
function saveFile() {
	if (!admin) {
		window.alert("您没有保存视图的权限！");
		return;
	}
	parent.mainFrame.saveFile();
}

// 刷新视图
function refreshFile() 
{
	if (window.confirm("“刷新”前是否需要保存当前视图？")) {
		parent.topFrame.saveFile();
	}
	parent.mainFrame.location.reload();
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

//拓扑图属性
function editSubMap(){
    if("<%=fileName%>"=="testData.jsp"){
        alert("请选择视图!");
    } else {
        var url="<%=rootPath%>/storey.do?action=readyEditSmall&xml=<%=fileName%>";
        showModalDialog(url,window,'dialogwidth:500px; dialogheight:400px; status:no; help:no;resizable:0');
    }
    
}
//重建视图
function rebuild(){
    if (window.confirm("该操作会重新构建当前楼层墙面点位置，还继续吗？")) {
		window.location = "<%=rootPath%>/storey.do?action=reBuildSubMap&xml=<%=fileName%>";
		alert("操作成功!");
        parent.location.reload();
	}
}
//备份视图
function backup(){
    var url="<%=rootPath%>/storey.do?action=readybackup&xml=<%=fileName%>";
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
}
//恢复视图
function resume(){
    var url="<%=rootPath%>/storey.do?action=readyresume&xml=<%=fileName%>";
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
}
function autoRefresh() 
{
	window.clearInterval(freshTimer);
	freshTimer = window.setInterval("refreshFile()",60000);
}

//选择视图
function changeView()
{
	if(document.all.submapview.value == "")return;
	window.parent.parent.location = "../infomanage/index.jsp?submapXml=" + document.all.submapview.value;
}
function cwin()
  {
     if(parent.parent.search.cols!='230,*')
     {
        parent.parent.search.cols='230,*';
        document.all.pic.src ="<%=rootPath%>/resource/image/hide_menu.gif";
        document.all.pic.title="隐藏树形";
     }
     else
     {
        parent.parent.search.cols='0,*';
        document.all.pic.src ="<%=rootPath%>/resource/image/show_menu.gif";
        document.all.pic.title="显示树形";
     }
  }
//新增示意图元
function createDemoObj(){
    //window.parent.mainFrame.ShowHide("1",null);拖拽方式
    
    var url="<%=rootPath%>/submap.do?action=readyAddHintMeta&xml=<%=fileName%>";
    var returnValue = showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
    //parent.mainFrame.location.reload();
}
</script>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#CEDFF6">
<form name="topviewForm">
<table width="100%" height="35" border="0" cellspacing="0" cellpadding="0" style="padding:0px;border-top:#CEDFF6 1px solid;border-left:#CEDFF6 1px solid;border-right:#CEDFF6 1px solid;border-bottom:#D6D5D9 1px solid;background-color:#F5F5F5;">
  <tr>
    <td>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/common/images/menubg.jpg">
	  <tr>
	    <td align="left" width="56">
	    </td>
		<td width="56"><input type="button" name="search" class="button_search_out" onmouseover="javascript:buttonSearchOver();" onmouseout="javascript:buttonSearchOut();" onclick="javascript:searchNode();" title="搜索" <%=disable%>/></td>
		<td width="56"><input type="button" name="save" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" title="保存当前楼层数据" <%=disable%>/></td>
		<td width="56"><input type="button" name="refresh" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshFile();" title="刷新当前楼层数据"/></td>
		<td width="56"><input type="button" name="editmap" class="button_editmap_out" onmouseover="javascript:buttonEditMapOver();" onmouseout="javascript:buttonEditMapOut();" onclick="javascript:editSubMap();" title="楼层属性"/></td>
		<td width="56"><input type="button" name="create5" class="button_create5_out" onmouseover="javascript:buttonCreate5Over();" onmouseout="javascript:buttonCreate5Out();" onclick="javascript:rebuild();" title="重建视图"/></td>
		<td width="56"><input type="button" name="create6" class="button_create6_out" onmouseover="javascript:buttonCreate6Over();" onmouseout="javascript:buttonCreate6Out();" onclick="javascript:backup();" title="备份视图"/></td>
		<td width="56"><input type="button" name="create7" class="button_create7_out" onmouseover="javascript:buttonCreate7Over();" onmouseout="javascript:buttonCreate7Out();" onclick="javascript:resume();" title="恢复视图"/></td>
		<td width="56">
		</td>
		<td width="100">
			<select name="submapview" onchange="changeView()">
			<option value="">--选择楼层--</option>
<%
	StoreyInfoDao dao = new StoreyInfoDao();
	List list = dao.loadAll();
	for(int i=0; i<list.size(); i++)
	{
		StoreyInfo vo = (StoreyInfo)list.get(i);
		int tag = 0;
		if(bids!=null&&bids.length>0){
		    for(int j=0;j<bids.length;j++){
		        if(vo.getBid()!=null&&!"".equals(vo.getBid())&&!"".equals(bids[j])&&vo.getBid().indexOf(bids[j])!=-1){
		            tag++;
		        }
		    }
		}
		if(tag>0&&!fileName.equals(vo.getXml())){
		    out.print("<option value='" + vo.getXml()+ "'>" + vo.getStoreyName()+ "</option>");
		}
	}	
%>
			</select>
		</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  </tr>
	</table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
