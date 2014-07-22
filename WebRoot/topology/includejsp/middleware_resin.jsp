<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.application.manage.ResinManager"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.initialize.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%
	String rootPath = request.getContextPath();
	String runmodel = PollingEngine.getCollectwebflag();
	String tmp = request.getParameter("tmp");
	String avgpingStr = request.getParameter("avgpingcon") == null ? "0"
			: request.getParameter("avgpingcon");
	double avgpingcon = Double.parseDouble(avgpingStr);
	String avgjvm = request.getParameter("avgjvmcon") == null ? "0"
			: request.getParameter("avgjvmcon");
	double avgjvmcon = Double.parseDouble(avgjvm);
	double jvm_memoryuiltillize = 0;
	double tomcatping = 0;
	String lasttime;
	String nexttime;
	String jvm = "";
	String jvm_utilization = "";
	Hashtable data_ht = new Hashtable();
	Hashtable pollingtime_ht = new Hashtable();
	ResinManager tm = new ResinManager();
	Resin resin = (Resin) PollingEngine.getInstance().getResinByID(Integer.parseInt(tmp));
	Hashtable hash_data = null;
	if ("0".equals(runmodel)) {
		//采集与访问是集成模式
		Hashtable resinvalues = ShareData.getResindata();
		//System.out.println(tomcatvalues);
		if (resinvalues != null && resinvalues.containsKey(resin.getIpAddress())) {
			data_ht = (Hashtable) resinvalues.get(resin.getIpAddress());
			//System.out.println(data_ht+"---"+data_ht.get("server"));
		}
	} else {
		//采集与访问分离模式
		//TomcatInfoService tomcatInfoService = new TomcatInfoService();
		//data_ht = tomcatInfoService.getTomcatDataHashtable(tmp);
	}

		int alarmLevel = 0;
		Hashtable checkEventHashtable = ShareData.getCheckEventHash();
		NodeUtil nodeUtil = new NodeUtil();
		/*===========for status start==================*/
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(resin);
		if(nodeDTO!=null){
			String chexkname = tmp+":"+nodeDTO.getType()+":"+nodeDTO.getSubtype()+":";
			//System.out.println(chexkname);
			if(checkEventHashtable!=null){
				for(Iterator it = checkEventHashtable.keySet().iterator();it.hasNext();){ 
			        String key = (String)it.next(); 
			        if(key.startsWith(chexkname)){
			        	if(alarmLevel < (Integer) checkEventHashtable.get(key)){
			        		alarmLevel = (Integer) checkEventHashtable.get(key); 
			        	}
			        }
				}
			}
		}
		String imgsrc = rootPath+"/resource/"+NodeHelper.getCurrentStatusImage(alarmLevel);
			
%>
<table id="service-detail-content" class="service-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/resin_detail_content_top.jsp">
			    <jsp:param name="name" value="<%=resin.getAlias()%>"/> 
				<jsp:param name="state" value="<%=NodeHelper.getStatusDescr(alarmLevel)%>"/> 
				<jsp:param name="ip" value="<%=resin.getIpAddress()%>"/> 
				<jsp:param name="imgsrc" value="<%=imgsrc%>"/>
			 	<jsp:param name="contentTitle" value="Resin信息"/> 
			 </jsp:include>
		</td>
	</tr>
	
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>