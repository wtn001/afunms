<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.machine.dao.StoreyInfoDao"%>
<%@page import="com.afunms.machine.model.StoreyInfo"%>
<%
	String rootPath = request.getContextPath();
	String fileName = (String)session.getAttribute(SessionConstant.CURRENT_FLOOR_VIEW);
	StoreyInfoDao dao = new StoreyInfoDao();
	StoreyInfo vo = (StoreyInfo)dao.findByXml(fileName);
	String bg = "";    
	String Title = "";
	if(vo!=null){
	    bg = vo.getBg();
	    Title = vo.getStoreyName();
	}
%> 
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="content-type" content="text/html; charset=gb2312" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>��ʾ</title>
<link href="<%=rootPath%>/resource/css/topo_style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/menu.js"></script>
<script type="text/javascript" src="js/map.js"></script>
<script type="text/javascript" src="js/window.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/util.js"></script> 
<%
	//�������豸������ʾ��Ϣ��IP��������text��
	//g_viewflag��global.js�ж��壬Ĭ��Ϊ0������Ҫ�����
	String viewflag = request.getParameter("viewflag");	
	if (viewflag == null) 
		out.print("<script type=\"text/javascript\">g_viewFlag = 0;</script>");
	else 
		out.print("<script type=\"text/javascript\">g_viewFlag = " + viewflag + ";</script>");	
%>

<script type="text/javascript" src="js/topology.js"></script>
<script type="text/javascript">
	window.onerror = new Function('return true;');		// �ݴ�
	
	openProcDlg();  //��ʾ����
	
	var fatherXML = "<%=fileName%>";//yangjun add ��������ͼʱ��ø�ҳxml
	function saveFile() {
		resetProcDlg();
		save();  //topoloty.js�еĺ���,���ڱ���ͼ����--->String��
	}
	
	function doInit()
	{
		loadXML("<%=rootPath%>/resource/xml/<%=fileName%>");
		
		var autoR = setInterval(autoRefresh,1000*60*2);
	}
	
	function autoRefresh()
	{
	   window.location = "showMap.jsp";
	 //  var divlayer = document.all.divLayer; 
	 //   while (divlayer.hasChildNodes()) {
	//		divlayer.removeChild(divlayer.firstChild);
	//	}
	//	loadXML("<%=rootPath%>/resource/xml/<%=fileName%>");
	}
    
    function showInfo(id) {
        //alert(0);
        showLineInfo(id);
	}
</script>
<style>
v\:*{ behavior:url(#default#VML); }
</style>
</head>

<!--����ѡ��ʱ���õ����������ĸ�����-->
<img src="<%=rootPath%>/resource/image/topo/line_top.gif" id="imgTop" class="tmpImg" style="width:10; height:10 " />
<img src="<%=rootPath%>/resource/image/topo/line_left.gif" id="imgLeft" class="tmpImg" style="width:10; height:10 "/>
<img src="<%=rootPath%>/resource/image/topo/line_bottom.gif" id="imgBottom" class="tmpImg" style="width:10; height:10 "/>
<img src="<%=rootPath%>/resource/image/topo/line_right.gif" id="imgRight" class="tmpImg" style="width:10; height:10 "/>

<script type="text/javascript">
<!--
document.write('<form name="frmMap" method="post" action="<%=rootPath%>/storey.do?action=save">');
document.write('<body class="main_body" onLoad="doInit();" onmousewheel="window.parent.parent.document.body.scrollTop -= event.wheelDelta/2;" onmousedown="bodyDown()" onselectstart="return false" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">');	
//loadMoveController();		// �����ƶ�������
//loadSizeController();		// ���ش�С������
loadInfo();	
//<div id="divTitle" align="center" style="font:oblique small-caps 900 29pt ����;"><%=Title%></div>
document.write('<table height="100%"><tr><td width="100%" align="left" height="100%">');
document.write('<div id="divLayer" style="width:100%;height:100%;background-position: center;background-attachment:fixed;background-repeat: no-repeat;background-image:url(<%=rootPath%>/machine/bg/<%=bg%>);color:black;position:absolute;top:0px;left:0px;background-color:#FFFFFF;border:#F0F8FF; 1px solid;" onmousedown="divLayerDown()" onclick="javascript:closeLineFrame();"></div>');//#000066
document.write('</td><td align="right" height="100%">');
document.write('</td></tr></table>');
document.write('<input type="hidden" name="hidXml"/>');
document.write('</body></form>');
createGallery();
//-->
</script>
<script type="text/javascript">
<!--
// ���� divLayer ��С
function resizeTopDiv() {
		//document.all.divLayer.style.width = maxWidth + 1024;
		//document.all.divLayer.style.height = maxHeight + 1024;
		zoomProcDlg("out");
}
	
	setTimeout("resizeTopDiv()", 1000);	
	parent.topFrame.showController(false);
	function showDevice(action) {
		parent.location = action;
	}
	
//-->
</script>
</html>
