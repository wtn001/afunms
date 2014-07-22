<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.alarm.util.AlarmConstant"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="java.util.*"%>
<%
	String rootPath = request.getContextPath();
	String tmp = request.getParameter("id");
	String ipaddress = request.getParameter("ipaddress");
	Node dns =  PollingEngine.getInstance().getDnsByID(
			Integer.parseInt(tmp));
	String name = request.getParameter("name");
	
		int alarmLevel = 0;
		alarmLevel=SystemSnap.getNodeStatus(tmp+"",AlarmConstant.TYPE_MIDDLEWARE,"dns");
		String imgsrc = rootPath+"/resource/"+NodeHelper.getCurrentStatusImage(alarmLevel);
		
	
	
%>
<table id="service-detail-content" class="service-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/dns_detail_content_top.jsp">
			    <jsp:param name="name" value="<%=name%>"/> 
				<jsp:param name="state" value="<%=NodeHelper.getStatusDescr(alarmLevel)%>"/> 
				<jsp:param name="ip" value="<%=ipaddress%>"/> 
				<jsp:param name="imgsrc" value="<%=imgsrc%>"/>
			 	<jsp:param name="contentTitle" value="DNSÐÅÏ¢"/> 
			 </jsp:include>
		</td>
	</tr>
	
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>