<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%
String path = request.getContextPath();
String rootPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List networklist = (List) session.getAttribute("networklist");
	List hostlist = (List) session.getAttribute("hostlist");
	String hostsize = "0";
	if (hostlist != null && hostlist.size() > 0)
		hostsize = hostlist.size() + "";
	String dbsize = (String) session.getAttribute("dbsize");
	if (dbsize == null)
		dbsize = "0";
	String securesize = (String) session.getAttribute("securesize");
	if (securesize == null)
		securesize = "0";
	String storagesize = (String) session.getAttribute("storagesize");
	if (storagesize == null)
		storagesize = "0";
	String servicesize = (String) session.getAttribute("servicesize");
	if (servicesize == null)
		servicesize = "0";
	String midsize = (String) session.getAttribute("midsize");
	if (midsize == null)
		midsize = "0";
	String routesize = (String) session.getAttribute("routesize");
	if (routesize == null)
		routesize = "0";
	String switchsize = (String) session.getAttribute("switchsize");
	if (switchsize == null)
		switchsize = "0";
		String routepath = "/perform.do?action=monitornodelist&flag=1&category=net_router&treeBid=2" ;
	String switchpath = "/perform.do?action=monitornodelist&flag=1&category=net_switch&treeBid=2" ;
	String hostpath = "/perform.do?action=monitornodelist&flag=1&category=net_server&treeBid=2";
	String dbpath = "/db.do?action=list&flag=1&treeBid=2";
	String midpath = "/middleware.do?action=list&flag=1&category=middleware&treeBid=2";
	String servicepath = "/service.do?action=list&flag=1&treeBid=2";
	String securepath = "/perform.do?action=monitornodelist&flag=1&category=safeequip&treeBid=2";

	//�洢����·(��ʱ����·����������)   �޲��Ի���<Taskδ���>
	String storagepath = "/perform.do?action=monitornodelist&flag=1&category=net_router&treeBid=2";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css" />
    
    <title>My JSP 'snapshot.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
   <script type="text/javascript">
     function showTree(rightFramePath){
				//�����ں�andת��һ��  
				//rightFramePath = rightFramePath.replaceAll("&","-and-");
				//rightFramePath = rightFramePath.replaceAll("=","-equals-");
				//ʹ��ѭ���������ں�andת��һ��  
				while(rightFramePath.indexOf("&") != -1){
					rightFramePath = rightFramePath.replace("&","-and-");
				}
				while(rightFramePath.indexOf("=") != -1){
					rightFramePath = rightFramePath.replace("=","-equals-");
				}
				window.parent.location.href =  "<%=rootPath%>/performance/index.jsp?flag=1&rightFramePath="+rightFramePath;
			}
			function doInit()
	  {
		setInterval(autoRefresh,1000*10*1);
	  }
	
	function autoRefresh()
	{
	
	window.location.href="<%=rootPath%>/user.do?action=snapshot";
	}
   </script>
  </head>
  
  <body onload="doInit()">
       <table width="100%">
			<tr>
				<td align="center">
					<img
						src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getRouterStatus(), 1)%>">
					<br>
					<a href="#" onClick="showTree('<%=routepath%>');">·����(<%=routesize%>)</a>
				</td>
				<td align="center">
					<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getSwitchStatus(), 2)%>">
					<br>
					<a href="#" onClick="showTree('<%=switchpath%>');">������(<%=switchsize%>)</a>
				</td>
				<td align="center">
					<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getServerStatus(), 3)%>">
					<br>
					<a href="#" onClick="showTree('<%=hostpath%>');">������(<%=hostsize%>)</a>
				</td>
				<td align="center">
					<img src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap.getDbStatus(), 4)%>">
					<br>
					<a href="#"
						onClick="showTree('<%=dbpath%>');">���ݿ�(<%=dbsize%>)</a>
				</td>
			</tr>
			<tr>
				<td>
					<div style="height: 50px;"></div>
				</td>
			</tr>
			<tr>
				<td align="center">
					<img
						src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap
.getMiddleStatus(), 5)%>">
					<br>
					<a href="#"
						onClick="showTree('<%=midpath%>');">�м��(<%=midsize%>)</a>
				</td>
				<td align="center">
					<img
						src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap
.getServiceStatus(), 6)%>">
					<br>
					<!--<img src="<%=rootPath%>/resource/image/service.gif" ><br>-->
					<a href="#"
						onClick="showTree('<%=servicepath%>');">����(<%=servicesize%>)</a>
				</td>
				<td align="center">
					<img
						src="<%=rootPath%>/resource/<%=NodeHelper.getSnapStatusImage(SystemSnap
.getFirewallStatus(), 7)%>">
					<!--<img src="<%=rootPath%>/resource/image/topo/firewall/firewall.gif">-->
					<br>
					<a href="#"
						onClick="showTree('<%=securepath%>');">��ȫ(<%=securesize%>)</a>
				</td>
				<td align="center" bgcolor=#ffffff>
					<img
						src="<%=rootPath%>/resource/image/topo/storage.gif">
					<br>
					<a href="#"
						onClick="showTree('<%=storagepath%>');">�洢(<%=storagesize%>)</a>
				</td>
			</tr>
			<tr>
				<td>
					<div style="height: 20px;"></div>
				</td>
			</tr>
		</table>
  </body>
</html>
