<%@page language="java" contentType="text/html;charset=gb2312" pageEncoding="gb2312"%>
<%@page import="javax.servlet.jsp.tagext.TryCatchFinally"%>
<%@page import="com.afunms.portscan.dao.PortScanDao"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@page import="com.afunms.temp.model.*"%>
<%@page import="com.afunms.polling.loader.HostLoader"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>


<%
   String runmodel = PollingEngine.getCollectwebflag();//采集与访问模式
	String rootPath = request.getParameter("rootPath")==null?"":request.getParameter("rootPath");
	String tmp = request.getParameter("tmp")==null?"":request.getParameter("tmp");
	String sysdescr = request.getParameter("sysdescr")==null?"":request.getParameter("sysdescr");
	String sysuptime = request.getParameter("sysuptime")==null?"":request.getParameter("sysuptime");
	String collecttime = request.getParameter("collecttime")==null?"":request.getParameter("collecttime");
	String picip = request.getParameter("picip")==null?"":request.getParameter("picip");
	String syslocation = request.getParameter("syslocation")==null?"":request.getParameter("syslocation");
	String flag1 = request.getParameter("flag1")==null?"":request.getParameter("flag1");
	String avgresponse = request.getParameter("avgresponse")==null?"":request.getParameter("avgresponse"); 
	String memoryvalue = request.getParameter("memoryvalue")==null?"":request.getParameter("memoryvalue");  
	String pingavg=request.getParameter("pingavg")==null?"":request.getParameter("pingavg");  
	
	String hostnum = (String)request.getAttribute("hostnum");
	String VMnum = (String)request.getAttribute("VMnum");
	String Poolnum = (String)request.getAttribute("Poolnum"); 
	//String num1 = (String)request.getAttribute("num");
	double num = 0;
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df1 = new DecimalFormat("0.0");
	List<HashMap<String, Object>> dslist = (ArrayList<HashMap<String, Object>>)request.getAttribute("dslist");
	double capacity = 0;
    List l = new ArrayList();
    if(dslist.size()>0 && dslist != null){
    	for(int i=0;i<dslist.size();i++)
    	{
    		double num1 = Double.parseDouble(dslist.get(i).get("capacity").toString().replaceAll(" GB", ""));
    		l.add(num1);
    	}
    	for(int j = 0;j<l.size();j++){
    		capacity += (Double)l.get(j);
    	}
    	//double num1 = capacity/1024;
    }
    num = capacity/1024;
	String valueStr="0";
    String[] colorStr1 = { "#0000FF", "#36DB43", "#3DA4D8", "#556B2F",
			"#8470F4", "#8A2BE2", "#23f266", "#FFA500", "#8B4513", "FFB6C1",
			"#FFF800", "#728699", "#9F005F", "#A52A2A", "#23f266" };
    if(dslist != null && dslist.size()>0){
       StringBuffer sb1 = new StringBuffer();
	sb1.append("<chart><series>");
	
	for (int k = 0; k < dslist.size(); k++) {
		sb1.append("<value xid='").append(k).append("'>").append(
				dslist.get(k).get("name").toString()).append("</value>");
	}
	sb1.append("</series><graphs><graph gid='0'>");
	for (int i = 0; i < dslist.size(); i++) {
	String x =df1.format(((Double.parseDouble(dslist.get(i).get("capacity").toString())-Double.parseDouble(dslist.get(i).get("freespace").toString()))/Double.parseDouble(dslist.get(i).get("capacity").toString()))*100)+"";
		sb1.append("<value xid='").append(i).append("' color='")
		.append(colorStr1[i]).append("'>" + x).append(
				"</value>");
	}
	
	sb1.append("</graph></graphs></chart>");
	valueStr = sb1.toString();
	}
	
	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
	//该设备采集的数据，是否只有ping数据标志
   	boolean isOnlyPing = NodeUtil.isOnlyCollectPing(host);
	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(host.getSupperid() + "");
		if (supper != null)
			suppername = supper.getSu_name() + "("
					+ supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());	
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	
	String dataStr = "";
	String sbData = "";
	String persbData = "";
	if(!isOnlyPing){
		String data="";
		Vector vector =new Vector();
		Vector perVec =new Vector();
			 StringBuffer sb=new StringBuffer();
			 StringBuffer persb=new StringBuffer();
			 String[] colorStr = new String[] { "#D4B829", "#F57A29", "#B5DB2F", "#3189B5", "#AE3174","#FFFF00","#333399","#0000FF","#A52A2A","#23f266"};
			 String[] percolorStr = new String[] { "#0000FF", "#36DB43", "#3DA4D8", "#556B2F", "#8470F4","#8A2BE2","#23f266","#F7FD31","#8B4513","FFD700"};
			 I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
	        String[] netInterfaceItem1={"ifDescr","InBandwidthUtilHdx"}; 
	        String[] netInterfaceItem2={"ifDescr","InBandwidthUtilHdxPerc"}; 
	        try{
	        	if("0".equals(runmodel)){//采集与访问是集成模式
	        		vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem1,"InBandwidthUtilHdx",starttime1,totime1); 
		        	perVec = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem2,"InBandwidthUtilHdxPerc",starttime1,totime1); 
	        	}else{
	        	vector = hostlastmanager.getInterface(host.getIpAddress(),netInterfaceItem1,"InBandwidthUtilHdx",starttime1,totime1); 
	        	perVec = hostlastmanager.getInterface(host.getIpAddress(),netInterfaceItem2,"InBandwidthUtilHdxPerc",starttime1,totime1); 
				}
				String allipstr = SysUtil.doip(host.getIpAddress());
				
				
				if(vector!=null&&vector.size()>0){
				sb.append("<chart><series>");
				persb.append("<chart><series>");
				 for(int i=0 ;i<5 ; i++){
		                String[] strs = (String[])vector.get(i);
		                String[] perstrs = (String[])perVec.get(i);
		                String ifname = strs[0];
		                String perifname = perstrs[0];
		              
		   			 sb.append("<value xid='"+i);
		   			 sb.append("'>");
		   			 sb.append(ifname+"</value>");
		   			 persb.append("<value xid='"+i);
		   			 persb.append("'>");
		   			 persb.append(perifname+"</value>");
		                }
			
				 sb.append("</series><graphs><graph>");
				 persb.append("</series><graphs><graph>");
				 String speed ="";
				  String perspeed ="";
				 for(int i=0 ;i<5 ; i++){
					 String[] strs = (String[])vector.get(i);
					 String[] perstrs = (String[])perVec.get(i);
		            	   perspeed = perstrs[1].replaceAll("%", "");
					
						 speed = strs[1].replaceAll("kb/s", "").replaceAll("kb/秒","");
					
				 sb.append("<value xid='"+i).append("' color='")
					.append(colorStr[i]);
				 sb.append("'>");
				 sb.append(speed+"</value>");
				 persb.append("<value xid='"+i).append("' color='")
					.append(percolorStr[i]);
				 persb.append("'>");
				 persb.append(perspeed+"</value>");
				 }
				 sb.append("</graph></graphs></chart>");
				 persb.append("</graph></graphs></chart>");
				  sbData=sb.toString();
				 persbData=persb.toString();
				 }else{
				sbData="0";
				persbData="0";
				 }
				 
			} catch (Exception e) {
				e.printStackTrace();
			}	
		String value1="<chart><series><value xid='0'>down</value><value xid='1'>up</value><value xid='2'>down</value><value xid='3'>down</value></series><graphs><graph><value xid='0'>0</value><value xid='0'>0</value><value xid='1'>1</value><value xid='2'>0</value><value xid='0'>1</value></graph></graphs></chart>";
	    Vector portVec =new Vector();
	    PortScanDao dao=new PortScanDao();
	   
	    portVec=dao.getCurrentStatus(host.getIpAddress());
	   
	    
	    String[] colors={"#3DA4D8","#FF0000"};
	    StringBuffer xmlStr=new StringBuffer();
	    if(portVec!=null&&portVec.size()>0){
	    xmlStr.append("<chart><series>");
	    for(int i=0;i<portVec.size();i++){
	   
	    String[] strs = (String[])portVec.get(i);
	     xmlStr.append("<value xid='"+i);
		   			 xmlStr.append("'>");
		   			 xmlStr.append(strs[0]+"</value>");
	    }
	    String value="";
	     xmlStr.append("</series><graphs><graph>");
	      for(int i=0;i<portVec.size();i++){
	    String[] strs = (String[])portVec.get(i);
	      value=strs[1];
	     xmlStr.append("<value xid='"+i).append("' color='");
	     if(value.equals("up")){
	     xmlStr.append(colors[0]).append("'>");
	      xmlStr.append("1</value>");
	     }else{
	     xmlStr.append(colors[1]).append("'>");
	       xmlStr.append("0</value>");
	     }
			
		
	    }
	     xmlStr.append("</series><graphs><graph>");
	     dataStr=xmlStr.toString();
	     }else{
	     dataStr="0";
	     }
	}
	
	int alarmlevel = SystemSnap.getNodeStatus(host);
%>



<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript">
function openNewWindow() {
		window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=300,left=300 width=370, height=150")
	
}
function openNewWindowForSysName() {
		window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=250,left=350 width=370, height=250")
	
}
 
  function outSpeed()
  {
   
  document.getElementById('in').checked=false;
  document.getElementById('out').checked=true;
  this.utiludx("OutBandwidthUtilHdx");

  }
  function inSpeed(){
  document.getElementById('out').checked=false;
  document.getElementById('in').checked=true;
  this.utiludx("InBandwidthUtilHdx");
}
function inBandSpeed(){

 document.getElementById('outband').checked=false;
  document.getElementById('inband').checked=true;
  this.utilband("InBandwidthUtilHdxPerc");
}
function outBandSpeed(){
 document.getElementById('inband').checked=false;
  document.getElementById('outband').checked=true;
  this.utilband("OutBandwidthUtilHdxPerc");
}
function utilband(type){
 $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=inSpeedTop5&tmp=<%=tmp%>&ip=<%=host.getIpAddress()%>&type="+type+"&nowtime="+(new Date()),
			success:function(data){
			if(data.dataStr==0){
			var  _div=document.getElementById("bandwidth");
	        var  img=document.createElement("img");
		         img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
		        
			}else{
			
			var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","288", "250", "8", "#FFFFFF");
				 so.addVariable("path", "<%=rootPath%>/amchart/");
				 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/band_settings.xml"));
				 so.addVariable("chart_data",data.dataStr);
				 so.write("bandwidth");
			}	
			}
		});
}
function utiludx(type){

 $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=inSpeedTop5&tmp=<%=tmp%>&ip=<%=host.getIpAddress()%>&type="+type+"&nowtime="+(new Date()),
			success:function(data){
			if(data.dataStr==0){
			var  _div=document.getElementById("bandwidth");
	        var  img=document.createElement("img");
		         img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
		        
			}else{
			var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","288", "250", "8", "#FFFFFF");
				 so.addVariable("path", "<%=rootPath%>/amchart/");
				 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/utilhdx_settings.xml"));
				 so.addVariable("chart_data",data.dataStr);
				 so.write("utilhdx");
				}
			}
		});
}

</script>
 <table id="detail-content" class="detail-content">
	<tr>
		<td>
			<table id="detail-content-body" class="detail-content-body">
				<tr>
					<td> 
						<table  cellpadding=0 width=100% align=center algin="center">
							<tr>
								<td width="60%" align="left" valign="top">
									<table >
										<tr>
											<td width="30%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;名称:</td>
										<!--  	<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>-->
										        <td width="70%"><span id="lable"><%=host.getAlias()%></span> [<a href="#" onclick="openNewWindow()"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;状态:</td>
											<td background="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>" class="performance-status">
												<%=NodeHelper.getStatusDescr(alarmlevel)%>
											</td>
										</tr>                    										
										<tr >
											<td height="26" class=txtGlobal align="left" nowrap  >&nbsp;采集方式:</td>
											<td>接口</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP地址:</td>
											<TD nowrap width="40%">
												<%=host.getIpAddress() %>
											</TD>
										</tr>
										<tr>
											<td height="26" class=txtGlobal align="left" nowrap  >&nbsp;类别:</td>
											<td>虚拟化</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;类型</td>
											<td width="70%">VMWare</td>
										</tr>
										<tr>
											<td width="30%" height="26" align=left valign=middle nowrap  class=txtGlobal>&nbsp;是否监视:</td>
											<td width="70%">已监视</td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align=left valign=middle nowrap  class=txtGlobal>&nbsp;数据采集时间:</td>
											<td width="70%"><%=collecttime%></td>
										</tr>
										<tr>
											<td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;物理机个数:</td>
											<td width="70%"><%//if(hostnum !=null && hostnum.size()>0){out.println(hostnum.size());} %><%=hostnum %></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="26" class=txtGlobal valign=middle nowrap  >&nbsp;虚拟机个数:</td>
											<td><%//if(VMnum !=null && VMnum.size()>0){out.println(VMnum.size());} 
											%><%=VMnum %></td>
										</tr>
										<tr>
											<td width="30%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;云资源:</td>
											<td width="70%"><%//if(Poolnum !=null && Poolnum.size()>0){out.println(Poolnum.size());} %><%=Poolnum %></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td height="26" class=txtGlobal valign=middle nowrap  >&nbsp;存储:</td>
											<td><%
											if(num>=1024){out.println(
											
											df.format(num/1024)+"T");}else{out.println(num+"GB");}%></td>
										</tr>
										<tr >
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备资产编号:</td>
											<td width="70%"><span id="assetid"><%=host.getAssetid()%></span></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;机房位置:</td>
											<td width="70%"><span id="location"><%=host.getLocation()%></span></td>
										</tr>    
										<tr bgcolor="#F1F1F1">
											<td height="26" class=txtGlobal valign=middle nowrap  >&nbsp;供应商信息:</td>
											<td>
												<% if(supper != null){
												%>
													<a href="#"  style="cursor:hand" onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
												<%
													}
												%>
											</td>
										</tr>    
									</table>
								</td>						
								<td width="40%" align="right" valign="top">
									<jsp:include page="/topology/includejsp/systeminfo_graphicVM.jsp">
										<jsp:param name="rootPath" value="<%=rootPath %>"/>
										<jsp:param name="picip" value="<%=picip %>"/> 
										<jsp:param name="avgresponse" value="<%=avgresponse %>"/>  
										<jsp:param name="valueStr" value="<%=valueStr %>"/>	 
										<jsp:param name="pingavg" value="<%=pingavg %>"/>
										<jsp:param name="isOnlyPing" value="<%=isOnlyPing %>"/>	 
									</jsp:include>
								</td>  
							</tr>
						</table> 
					</td>
				</tr>
			</table>
		</td>
	</tr>
	
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
		</td>
	</tr>
</table>