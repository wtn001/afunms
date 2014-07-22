<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@ page import="com.afunms.polling.base.Node" %>
<%@ page import="com.afunms.polling.PollingEngine" %>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath(); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html> 
<head>
<title>连通率</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=rootPath%>/include/list.js"></script>
<script language="JavaScript">var tabImageBase = "<%=rootPath%>/chart/report/tabs";</script> 
<script language="JavaScript" src="<%=rootPath%>/chart/js/dhtml.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/chart/js/graph.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/print.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>


<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script language="JavaScript" src="<%=rootPath%>/include/validation.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script> 
<style>
BODY {background-image: URL(<%=rootPath%>/common/images/bg.jpg); 
　　background-repeat:repeat;} 
.content-title{
	font-family:宋体, Arial, Helvetica, sans-serif;
	font-size:12px;
	font-weight:bold;
	color:#000000;
	text-align:left;
	background-image: URL(<%=rootPath%>/common/images/right_t_02.jpg);
	background-position: center; 
}
</style>
</head>
	<%
		String ip = (String) request.getParameter("ip");
		String id = (String) request.getParameter("id");
		String category = (String) request.getParameter("category");
		String allipstr=SysUtil.doip(ip);
		Node node = (Node) PollingEngine.getInstance().getNodeByIp(ip);
		if (node.getCategory() == 14)category="net_storage";
		//System.out.println("node category===="+node.getCategory());
	%>
<body class="WorkWin_Body">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form method="post" name="mainForm">
<table width=100% border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					
					<td class="content-title" align="center" height="29">
						&nbsp;<%=node.getAlias()%>
						(<%=node.getIpAddress()%>)
					</td>
					
					
				</tr>
				  <tr bgcolor="#ffffff"> 
    <td colspan="10">
    <table width="100%" cellpadding="0" cellspacing="0" id="table2">
        <tr> 
          <td><div align="center">
              <table cellpadding="0" cellspacing="0" width=98%>
              							<tr> 
                							<td width="100%" align="center"> 
                								<div id="flashcontent1">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<%
												System.out.println("category====="+category);
												%>
												<%if(category!=null && category.equals("net_storage") && node.getOstype() != 44){ %>
													<script type="text/javascript">
														var so = new SWFObject("<%=rootPath%>/flex/common_line.swf?title=连通曲线&tablename=pings<%=allipstr%>", "common_line", "800", "250", "8", "#ffffff");														
														so.write("flashcontent1");
													</script>

												<%}else{%>
													<script type="text/javascript">
														var so = new SWFObject("<%=rootPath%>/flex/Ping_month.swf?ipadress=<%=ip%>&id=<%=id%>", "Response_time_month", "800", "250", "8", "#ffffff");
														so.write("flashcontent1");
													</script>	
												<%} %>			
							                </td>
										</tr>             
									</table>
            </div>
            </td>
        </tr>
      </table>
      </td>
  </tr>
  <tr width=100% height="15" bgcolor="#ffffff"><td></td></tr></table>
    </form>
</body>
</html> 