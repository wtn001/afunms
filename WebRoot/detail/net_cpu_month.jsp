<%@ page language="java" contentType="text/html; charset=GBK" %>
<%@ page import="com.afunms.polling.base.Node" %>
<%@ page import="com.afunms.polling.PollingEngine" %>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath(); 
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html> 
<head>
<title> CPU������</title>
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
����background-repeat:repeat;} 
.content-title{
	font-family:����, Arial, Helvetica, sans-serif;
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
		Node node = (Node) PollingEngine.getInstance().getNodeByIp(ip);
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
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Column_CPU.swf?ipadress=<%=ip%>&id=<%=id%>", "Line_CPU", "786", "230", "8", "#ffffff");
													so.write("flashcontent1");
												</script>				
							                </td>
										</tr>             
									</table>
            </div>
            </td>
        </tr>
      </table>
      </td>
  </tr>
  <tr width=100% height="25" bgcolor="#ffffff"><td></td></tr>
	</table>		
    </form>
</body>
</html> 