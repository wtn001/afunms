<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.discovery.*"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
	//------------���jsp���ڼ������˷��ֽ���----------------
	DiscoverMonitor monitor = DiscoverMonitor.getInstance();
	monitor.setRefreshTimes();

	String rootPath = request.getContextPath();
	User user = (User) session
			.getAttribute(SessionConstant.CURRENT_USER); //��ǰ�û�
	if (user == null) {
		out.print("ϵͳ��ʱ,�����µ�¼!");
		return;
	}
%>
<html>
	<head>
		<style>
#body { /* ������ɫ */
	background: url("<%=rootPath%>/common/images/bg.jpg");
	FONT-SIZE: 9pt;
	LINE-HEIGHT: 12pt;
}
</style>
		<script type="text/javascript"
			src="<%=rootPath%>/js/jquery/jquery-1.4.2.min.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/jquery/jquery.progressbar.min.js"
			charset="utf-8"></script>
		<script language="javascript">
 
  function stopDiscover()
  {
  	//alert("====");
  	window.location = "/afunms/discover.do?action=stop"
  	window.close();
  	//monitorForm.action = "/afunms/discover.do?action=stop";
        //monitorForm.submit();
        
  } 
 $(document).ready(function() {
				$("#spaceused").progressBar();
				
			});
</script>


		<title>Topo</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<%
			if (monitor.isCompleted()) {
		%>
		<script>
      alert("�������,����ʹ��ϵͳ��!�����������·���"); 
    </script>
		<meta http-equiv="refresh" content="60">
		<!--1����ˢ��һ��-->
		<%
			} else {
		%>
		<meta http-equiv="refresh" content="60">
		<!--1����ˢ��һ��-->
		<%
			}
		%>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
	    <link rel="stylesheet" href="<%=rootPath%>/resource/css/discover.css" type="text/css">
	</head>
	<body id="body">

		<form method="post" name="monitorForm">
			<%
				if (monitor.getRefreshTimes() > 0) {
			%>
			<table width="500" align="center" class="microsoftLook">
				<tr>
					<td align='center'>
						<font color='blue'><b>���ֽ��̼���</b>
						</font>
						<input type="button" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" value="ֹͣ����"
							onclick="stopDiscover()" target=_self>
					</td>
				</tr>
				<tr>
					<td align='center'>
						<embed src="<%=rootPath%>/flex/swf/discover.swf" height=100
							width=500></embed>
					</td>
				</tr>
				<tr>
					<td valign="top" align="center">
						<table width="100%" border=1 cellspacing=0 cellpadding=0
							bordercolorlight='#000000' bordercolordark='#FFFFFF'>
							<tr class="microsoftLook0">
								<td>
									��ʼʱ��
								</td>
								<td><%=monitor.getStartTime()%></td>
							</tr>
							<tr class="microsoftLook0">
								<td>
									����ʱ��
								</td>
								<td><%=monitor.getEndTime()%></td>
							</tr>
							<tr class="microsoftLook0">
								<td>
									�Ѿ���ʱ
								</td>
								<td><%=monitor.getElapseTime()%></td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border=1 cellspacing=0 cellpadding=0
							bordercolorlight='#000000' bordercolordark='#FFFFFF'>
							<tr bgcolor='#D4E1D5'>
								<td width="20%">
									&nbsp;
								</td>
								<td align=center width="20%">
									<b>����</b>
								</td>
								<%
									if (!monitor.isCompleted())
											out.print("<td align=center width='20%'><b>�ѷ���</b></td><td align=center width='40%'><b>���ֽ���</b></td>");
								%>
							</tr>
							<tr class='microsoftLook0' >
								<td align=center>
									�豸
								</td>
								<td align=center><%=monitor.getHostTotal()%></td>
								<%
									double usePer = 0;
										if (!monitor.isCompleted()) {

											if (monitor.getHostTotal() != 0) {
												usePer = monitor.getDiscoveredNodeTotal() * 100.0/ monitor.getHostTotal();
												
											}

										}
										
								%>
								<td align=center><%=monitor.getDiscoveredNodeTotal() %></td>
								<td align=center>
								<script language="javascript">
                               $(document).ready(function() {
				                 $("#used").progressBar(<%=usePer%>);
				                 
			                     });
                            </script>
									<span id='used'></span>
								</td>
								
								
							</tr>
							<tr class='microsoftLook0'>
								<td class="microsoftLook0" align=center>
									����
								</td>
								<td align=center><%=monitor.getSubNetTotal()%></td>
								<%
									if (!monitor.isCompleted())
											out.print("<td>&nbsp;</td><td>&nbsp;</td>");
								%>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align='center'>
						<font color='blue'><b><br>��ϸ</b>
						</font>
					</td>
				</tr>
				<tr class='microsoftLook0'>
					<td><%=monitor.getResultTable()%></td>
				</tr>
			</table>
			<%
				}
			%>
		</form>
	</body>
</html>