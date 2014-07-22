<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%> 
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.common.util.*" %>
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
<%@page import="com.afunms.inform.util.SystemSnap"%>
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
<%@page import="com.afunms.system.vo.FlexVo"%>

<% 
    String runmodel = PollingEngine.getCollectwebflag();
  	String rootPath = request.getParameter("rootPath");    
	String tmp = request.getParameter("tmp"); 
	String sysdescr = request.getParameter("sysdescr"); 
	String sysuptime =request.getParameter("sysuptime");
	String collecttime =request.getParameter("collecttime");  
	String pvalue =request.getParameter("pvalue");
	String vvalue =request.getParameter("vvalue"); 
	String vused = request.getParameter("vused"); 
	String picip =request.getParameter("picip"); 
	String pingavg=request.getParameter("pingavg"); 
	String avgresponse =request.getParameter("avgresponse"); 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	if(sysuptime == null || sysuptime.equals("null")){
		sysuptime = "";
	}
	if(sysdescr == null || sysdescr.equals("null")){
		sysdescr = "";
	}
	if(collecttime == null || collecttime.equals("null")){
		collecttime = "";
	}
	
	String time1 = sdf.format(new Date());
	String starttime = time1 + " 00:00:00";
	String endtime = time1 + " 23:59:59";
	Hashtable memhash=new Hashtable();
	Hashtable diskhash=new Hashtable();
	
    Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));	 
  	SupperDao supperdao = new SupperDao();
   	Supper supper = null;
   	String suppername = "";
   	try{
   		supper = (Supper)supperdao.findByID(host.getSupperid()+"");
   		if(supper != null)suppername = supper.getSu_name()+"("+supper.getSu_dept()+")";
   	}catch(Exception e){
   		e.printStackTrace();
   	}finally{
   		supperdao.close();
   	}	
   	
   	String temp = "";
   	String cpuData = "";
   	String valueStr = "";
   	//该设备采集的数据，是否只有ping数据标志
   	Vector memoryVector = new Vector(); 
   	String memoryvalue = "0";
   	boolean isOnlyPing = NodeUtil.isOnlyCollectPing(host);
 	if(!isOnlyPing){
    	//模式
    	I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
    	if(runmodel.equals("0")){
    	    Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData!=null)memoryVector = (Vector) ipAllData.get("memory");
		    try{
				memhash = hostlastmanager.getMemory_share(host.getIpAddress(),"Memory",starttime,endtime);
			}catch(Exception e){
				e.printStackTrace();
			}
			try{
				diskhash = hostlastmanager.getDisk_share(host.getIpAddress(),"Disk",starttime,endtime);
			}catch(Exception e){
				e.printStackTrace();
			}	
		}else{
			try {
				memhash = hostlastmanager.getMemory(host.getIpAddress(),
						"Memory", starttime, endtime);
				diskhash = hostlastmanager.getDisk(host.getIpAddress(),
						"Disk", starttime, endtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	try {
		if (memoryVector != null && memoryVector.size() > 0) {
			for (int i = 0; i < memoryVector.size(); i++) {
				Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
				if(memorycollectdata.getSubentity().equals("PhysicalMemory"))
				{
					if(memorycollectdata.getEntity().equals("Utilization")){
					 	memoryvalue=memorycollectdata.getThevalue();
					}
				} 
			}
		} 
	}catch(Exception ex){
		ex.printStackTrace();
	}
       //内存最大、平均利用率 amcharts wxy add
		CreateAmColumnPic aixColumnPic=new CreateAmColumnPic();
	    temp=aixColumnPic.createAmMemoryChart(host.getIpAddress(),memhash);
 		
	   //最近一周CPU平均利用率 amcharts wxy add
	   cpuData=aixColumnPic.createCpuChartLastWeek(host.getIpAddress());
	
	   //磁盘利用率 amcharts wxy add
		valueStr=aixColumnPic.createDiskChart(diskhash);		
		
		
 	}
 	int alarmlevel = SystemSnap.getNodeStatus(host);
%>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />
<script type="text/javascript">
function openNewWindowForAlias() {
		window.open("<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=300,left=300 width=370, height=150")
	
}
function openNewWindowForSysName() {
		window.open("<%=rootPath%>/network.do?action=ready_editsysgroup&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","_blank",
			"toolbar=no,titlebar=no, location=no,directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=yes,top=250,left=350 width=370, height=250")
	
}
</script>

<table id="detail-content" class="detail-content">
	
	<tr>
		<td>
			<table id="detail-content-body" class="detail-content-body">
				<tr>
					<td> 
						<table cellpadding=0 width=100% align=center algin="center">
							<tr> 
 								<td width="60%" align="left" valign="top">
 									<table cellpadding="0" cellspacing="0" width=100% align=center>
 									    
   										<tr>
     										<td width="30%" height="26" align="left" nowrap  >&nbsp;设备标签:</td>
     										<td width="70%">
     										   <span id="lable"><%=host.getAlias()%></span>[<a href="#" onclick="openNewWindowForAlias()"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
     										</td>
   										</tr>
   										<tr bgcolor="#F1F1F1">
     										<td width="30%" height="26" align="left" nowrap>&nbsp;系统名称:</td>
     										<td width="70%">
      										<span id="sysname"><%=host.getSysName()%></span>[<a href="#" onclick="openNewWindowForSysName()"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]
    										</td>
   										</tr>                    										
   										<tr>
     										<td height="26px" align="left"   >&nbsp;状态:</td>
     										<td background="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>" class="performance-status">
												<%=NodeHelper.getStatusDescr(alarmlevel)%>
											</td>
   										</tr>
   										<tr bgcolor="#F1F1F1">
   											<td width="30%" height="26" align=left nowrap>&nbsp;IP地址:</td>
	   											<%
		                 						 	IpAliasDao ipdao = new IpAliasDao();
												 	List iplist = ipdao.loadByIpaddress(host.getIpAddress());
												 	ipdao.close();
													ipdao = new IpAliasDao();
	               									IpAlias ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(),"1");
				               						ipdao.close();
												 	if(iplist == null)iplist = new ArrayList(); 
												%>
		                  					<TD nowrap width="40%">
		                  						<select name="ipalias<%=host.getIpAddress() %>">
		                  							<option selected>
		                  								<%=host.getIpAddress() %>
		                  							</option>
 													<% 
														for(int j=0 ;j<iplist.size() ; j++){
															IpAlias voTemp = (IpAlias)iplist.get(j); 
													%>
													<option <%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>selected<%} %>><%=voTemp.getAliasip() %></option>
 													<%} %>
 												</select>
    											[
                 									<a href="#"  style="cursor:hand" onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">
                 										修改
                 									</a>
                 								]
		                  					</TD>
   										</tr>
     									<tr>
     										<td height="26" align="left"  >&nbsp;子网掩码:</td>
     										<td><%=host.getNetMask()%></td>
      									</tr>
    									<tr bgcolor="#F1F1F1">
    										<td width="30%" height="26" align=left>
    											&nbsp;类别:
    										</td>
    										<td width="70%">
    											<%=NodeHelper.getNodeCategory(host.getCategory())%>
    										</td>
    									</tr>
      									<tr>
      										<td width="30%" height="26" align=left >&nbsp;类型:</td>
      										<td width="70%"><%=host.getType()%></td>
      									</tr>
      									<tr bgcolor="#F1F1F1">
      										<td width="30%" height="26" align=left nowrap>&nbsp;系统描述:</td>
      										<td width="70%">
                  								<%
                  									String sysdescrforshow="";//用于显示设备信息简称
                  									if(sysdescr!=""&&sysdescr!=null){
														if(sysdescr.length()>40){
															sysdescrforshow=sysdescr.substring(0,40)+"...";
													 	} else{
															sysdescrforshow=sysdescr;
														}
													}
												 %>
												 <acronym title="<%=sysdescr%>"><%=sysdescrforshow%></acronym>
                 							</td>
      									</tr>
      									<tr>
      										<td height="26" >&nbsp;设备启动时间:</td>
      										<td><%=sysuptime%></td>
      									</tr>
      									<tr bgcolor="#F1F1F1">
      										<td width="30%" height="26" align=left   nowrap class=txtGlobal>
      											&nbsp;数据采集时间:
      										</td>
      										<td width="70%">
      											<%=collecttime%>
      										</td>
      									</tr>
      									<tr >
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;设备资产编号:</td>
											<td width="70%"><span id="assetid"><%=host.getAssetid()%></span></td>
										</tr>
										<tr bgcolor="#F1F1F1">
											<td width="30%" height="26" align="left" nowrap class=txtGlobal>&nbsp;机房位置:</td>
											<td width="70%"><span id="location"><%=host.getLocation()%></span></td>
										</tr>
      									<tr>
      										<td height="26" class=txtGlobal nowrap>
      											&nbsp;设备供应商:
      										</td>
      										<td>
	   											<% if(supper != null){
	   											%>
	   												<a href="#"  style="cursor:hand" onclick="window.showModalDialog('<%=rootPath%>/supper.do?action=read&id=<%=supper.getSu_id()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=suppername%></a>
	   											<%
	   												}
	   											%>
   											</td>
       									</tr>	
       									<%
       									if(!isOnlyPing){
       									%>
       									<tr bgcolor="#F1F1F1">
       										 <td width="30%" height="26">
       										 	&nbsp;PF 使用率
       										 </td>
                      						 <td width="70%">
       											<table> 
                      								<tr>
	                      								<td width="80%">
                      										<table border=1 height=15 width="100%" bgcolor=#ffffff id="PFTABLE">
			                      								<tr>
			                      									<td width="<%=Integer.parseInt(vvalue)%>%" bgcolor="green" align=center>&nbsp;&nbsp;<%=vused%>GB</td>
			                      									<td width="<%=(100-Integer.parseInt(pvalue))%>%" bgcolor=#ffffff></td>
			                      								</tr>
		                      								</table>
                      									</td>
                      									<td width="20%">
                      										&nbsp;
                      										<a onclick="fresh_PF()"> 
                      											<img src="<%=rootPath%>/resource/image/fresh.gif"></img>
                      										</a>
                      									</td> 
                      								</tr>
                     							</table>
       										</td>
       									</tr>
       									<tr>
       										<td width="30%" height="26">&nbsp;物理内存利用率</td>
                      						<td width="70%">
       											<table> 
                      								<tr>
	                      								<td width="80%">
                      						 				<table border=1 height=15 width="100%" bgcolor=#ffffff id="WLTABLE">
			                      								<tr>
			                      									<td width="<%=Integer.parseInt(pvalue)%>%" bgcolor="green" align=center>&nbsp;&nbsp;<%=pvalue%>%</td>
			                      									<td width="<%=(100-Integer.parseInt(pvalue))%>%" bgcolor=#ffffff></td>
			                      								</tr>
		                      								</table>	
	                      								</td>
	                      								<td width="20%" align=left>
                      										&nbsp;
	                      									<a onclick="fresh_WL()">
	                      										<img src="<%=rootPath%>/resource/image/fresh.gif"></img>
	                      									</a>
                      									</td> 
                      								</tr>
                   								</table>
       										</td>
       									</tr>
       									<tr  bgcolor="#F1F1F1">
       										<td width="30%" height="26">&nbsp;虚拟内存利用率</td>
                      						<td width="70%">
       											<table> 
                      								<tr>
	                      								<td width="80%">
                      						 				<table border=1 height=15 width="100%" bgcolor=#ffffff id="XNTABLE">
			                      								<tr>
			                      									<td width="<%=Integer.parseInt(vvalue)%>%" bgcolor="green" align=center>&nbsp;&nbsp;<%=vvalue%>%</td>
			                      									<td width="<%=(100-Integer.parseInt(vvalue))%>%" bgcolor=#ffffff></td>
			                      								</tr>
		                      								</table>
	                      								</td>	
	                      								<td width="20%" align=left>
	                      									&nbsp;
	                      									<a onclick="fresh_XN()">
	                      										<img src="<%=rootPath%>/resource/image/fresh.gif"/>
                      										</a>
	                      								</td>
                      								</tr>
                     							</table>
       										</td>
       									</tr>	
       									<%
       									}
       									%>							
     								</table>
								</td>			 												
    							<td width="40%" align="right" valign="top">
									<jsp:include page="/topology/includejsp/systeminfo_graphic.jsp">
										<jsp:param name="rootPath" value="<%=rootPath %>"/>
										<jsp:param name="picip" value="<%=picip %>"/>
										<jsp:param name="pingavg" value="<%=pingavg %>"/>
										<jsp:param name="avgresponse" value="<%=avgresponse %>"/>
										<jsp:param name="pvalue" value="<%=pvalue %>"/>
										<jsp:param name="isOnlyPing" value = "<%=isOnlyPing %>"/> 
									</jsp:include>
								</td>
							</tr>
							<%
							if(!isOnlyPing){
							%>
							<tr>
							             <td width="100%" align="center" valign="top" colspan=2>
							                    <table> 
                      								<tr> 
	                      								<td width="30%" align=center>
                      						 				<table  height=15 width="100%" bgcolor=#ffffff id="XNTABLE">
			                      								<tr>
			                      									<td>
			                      									<div id="hostCpu">
														
														            </div>		
	                                          <script type="text/javascript"
	                                                src="<%=rootPath%>/include/swfobject.js"></script>
	                                            
	                                               <script type="text/javascript">
	                                              <% if(!cpuData.equals("0")){%>
	                                          var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","250", "210", "8", "#FFFFFF");
				                                  so.addVariable("path", "<%=rootPath%>/amchart/");
				                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostcpu_settings.xml"));
				                                  so.addVariable("chart_data","<%=cpuData%>");
				                                  so.write("hostCpu");
				                                  <%}else{%>
				                                  var _div=document.getElementById("hostCpu");
												  var img=document.createElement("img");
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
													 _div.appendChild(img);
				                                  <%}%>
	                                          </script>
                                                                    </td>
			                      								</tr>
		                      								</table>
	                      								</td>	
	                      								<td width="40%" align=center>
	                      								<div id="Infomemory">
														<strong>You need to upgrade your Flash Player</strong>
														</div>

												<script type="text/javascript">
				
		                                           var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "330", "210", "8", "#FFFFFF");
		                                               so.addVariable("path", "<%=rootPath%>/amchart/");
		                                               so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/hostmemory_settings.xml"));
	                                                   so.addVariable("chart_data", "<%=temp%>");
		                                               so.addVariable("preloader_color", "#999999");
		                                               so.write("Infomemory");
	                                            </script>
	                      									
	                      								</td>
	                      								<td width="30%" align=center>
	                      						<div id="hostdisk">
														
														</div>		
	                                          <script type="text/javascript"
	                                                src="<%=rootPath%>/include/swfobject.js"></script>
	                                            
	                                               <script type="text/javascript">
	                                              <% if(!valueStr.equals("0")){%>
	                                          var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn","250", "210", "8", "#FFFFFF");
				                                  so.addVariable("path", "<%=rootPath%>/amchart/");
				                                  so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hostdisk_settings.xml"));
				                                  so.addVariable("chart_data","<%=valueStr%>");
				                                  so.write("hostdisk");
				                                  <%}else{%>
				                                  var _div=document.getElementById("hostdisk");
												  var img=document.createElement("img");
													  img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
													 _div.appendChild(img);
				                                  <%}%>
	                                          </script>
	                      									
	                      								</td>
                      								</tr>
                     							</table>
							          </td>
							     </tr>
							     <%
							     }
							     %>
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
