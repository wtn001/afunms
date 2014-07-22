<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.alarm.util.AlarmConstant"%>
<%@page import="com.afunms.polling.base.Node"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.node.Tomcat"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.initialize.*"%>
<%
	String rootPath = request.getContextPath();
	String runmodel = PollingEngine.getCollectwebflag();
	String tmp = request.getParameter("id");

	Node apache =  PollingEngine.getInstance().getApacheByID(
			Integer.parseInt(tmp));
	
	
		int alarmLevel = 0;
		alarmLevel=SystemSnap.getNodeStatus(tmp+"",AlarmConstant.TYPE_MIDDLEWARE,"apache");
		String imgsrc = rootPath+"/resource/"+NodeHelper.getCurrentStatusImage(alarmLevel);
			
%>
<table id="service-detail-content" class="service-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/apache_detail_content_top.jsp">
			    <jsp:param name="name" value="<%=apache.getAlias()%>"/> 
				<jsp:param name="state" value="<%=NodeHelper.getStatusDescr(alarmLevel)%>"/> 
				<jsp:param name="ip" value="<%=apache.getIpAddress()%>"/> 
				<jsp:param name="imgsrc" value="<%=imgsrc%>"/>
			 	<jsp:param name="contentTitle" value="ApacheÐÅÏ¢"/> 
			 </jsp:include>
		</td>
	</tr>
	
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>