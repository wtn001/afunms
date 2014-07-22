<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.model.UserTaskLog"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.schedule.model.*"%>
<%@page import="java.text.SimpleDateFormat;"%>
<%@include file="/include/globe.inc"%>
<%
  	String rootPath = request.getContextPath(); 
  	String menuTable = (String)request.getAttribute("menuTable");
  	User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
  	
  	List<Schedule> scheduleList = (List<Schedule>)request.getAttribute("scheduleList");
	Map<Integer,User> userMap = (HashMap<Integer,User>)request.getAttribute("userMap");
	Map<String,Period> periodMap = (HashMap<String,Period>)request.getAttribute("periodMap");
	Map<String,Position> positionMap = (HashMap<String,Position>)request.getAttribute("positionMap");
	String username = "",strPeriod = "", strPosition = "";
%>

<html>
<head>
<title>日历视图打印</title>
<link rel="shortcut icon" href="<%=rootPath%>/system/usertasklog/resource/images/e2cs_16x16.png">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/system/usertasklog/resource/ext2/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/system/usertasklog/resource/ext2/resources/css/core.css">
<script type="text/javascript" src="<%=rootPath %>/system/usertasklog/resource/ext2/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath %>/system/usertasklog/resource/ext2/ext-all-debug.js"></script>
<link id="css" rel="stylesheet" type="text/css" href="<%=rootPath %>/system/usertasklog/resource/calendar.css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">


<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<style type="text/css">
<!--
/*Custom CSS for the sview.blankHTML porperty */
.custom_image_addNewEvent_scheduler{cursor:pointer;	padding-top: 15px;	padding-right: 5px;	padding-bottom: 5px;	padding-left: 5px;}
.custom_text_addNewEvent_scheduler{font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 14px; font-weight: bold;	color: #FFFFFF;	text-decoration: none;	text-align: center;	padding: 3px;}
.test_taskovercss{ background-image:url(<%=rootPath%>/system/usertasklog/resource/images/task_over_001.png); background-repeat:repeat-y; color:#FFFFFF;} 
.test_taskovercss_b{ background-color:#999999;color:#FFFFFF;background-image: url(<%=rootPath%>/system/usertasklog/resource/images/__header_background_001.png);	background-repeat: repeat-y;} 
.test_taskovercss_sched{ background-image:url(<%=rootPath%>/system/usertasklog/resource/images/header_background_002.png); background-repeat:repeat-x; color:#FFFFFF;}
.test_taskovercss_sched_b{ background-image:url(<%=rootPath%>/system/usertasklog/resource/images/header_background_001.png); background-repeat:repeat-x; color:#FFFFFF;}
.holiday{height:16px; background-image: url(<%=rootPath%>/system/usertasklog/resource/imgs_test/cake.png);	background-repeat: no-repeat;background-position: left 2px;	text-indent: 18px;}
-->
</style>
<script type="text/javascript" src="<%=rootPath %>/schedule/scheduling/resource/locale/e2cs_zh_CN.js"></script>
<script type="text/javascript" src="<%=rootPath %>/schedule/scheduling/resource/e2cs_pack.js"></script>


<script type="text/javascript"><!--
//设置网页打印的页眉页脚为空
function PageSetup_Null()
{
	var HKEY_Root,HKEY_Path,HKEY_Key;
	HKEY_Root="HKEY_CURRENT_USER";
	HKEY_Path="\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
	
	var Wsh = new ActiveXObject("WScript.Shell");
    HKEY_Key="header";
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");
    HKEY_Key="footer";
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"");
    HKEY_Key="margin_left" 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"0"); //键值设定--左边边界
    HKEY_Key="margin_top" 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"0"); //键值设定--上边边界
    HKEY_Key="margin_right" 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"0"); //键值设定--右边边界
    HKEY_Key="margin_bottom" 
    Wsh.RegWrite(HKEY_Root+HKEY_Path+HKEY_Key,"0"); //键值设定--下边边界
}

function doPrint() {
    try
    {
        PageSetup_Null();
    }
    catch(e)
    {
        var errorMsg = e.message+"\r"+"请设置:IE选项->安全->Internet->"+"ActiveX控件和插件"+"\r"+"对未标记为可安全执行脚本的ActiveX的控件初始化并执行脚本->允许或提示";
        alert(errorMsg);
        return;
    }
    window.print();
}

function init(obj){
	var obj = window.opener.document.getElementById("calendar").innerHTML;
	var div = document.getElementById("calendar");
	div.innerHTML = obj;
	doPrint();
	window.close();
}
--></script>
<style type="text/css">
body{margin-left: 10px;	margin-top: 10px;	margin-right: 10px;	margin-bottom: 10px;}
.maintitle {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 18px;color: #003366;}
.mainsubtitle {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 14px;color: #003366;}
.main_notes{	font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 9px;}
.task_time {background-image: url(<%=rootPath %>/system/usertasklog/resource/imgs/clock.png);}
.task_time_off {background-image:url(<%=rootPath %>/system/usertasklog/resource/imgs/clock_red.png);}
#samplebox #sampleboxtitle {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 14px;color: #FFFFFF;	background-color: #003366;padding: 3px;}
#samplebox {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 11px;}
#samplebox .textsample{	font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 10px;}
.settings_monthview {font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 14px;color: #FFFFFF;	background-color: #006633;padding:4px;font-weight: bold; cursor:pointer;}
.settings_overview  {font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 14px;color: #FFFFFF; background-color: #006699;padding:4px;font-weight: bold;cursor:pointer;}
.style1 {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 9px; font-weight: bold; }
</style>
</head>
<body onLoad="init();">
	<form method="post" name="mainForm" id="mainForm">
		<input type="hidden" name="content" id="content">
		<input type="hidden" name="date" id="date">
		<input type="hidden" name="id" id="id">
		<table width="100%" border="0" style="margin-top:30px;" align="center">
			<tr align="center">
				<td valign="middle">
					<div id="calendar"></div>
					<div id="pruebapanel"></div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
