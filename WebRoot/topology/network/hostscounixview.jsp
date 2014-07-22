<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@ page import="com.afunms.config.model.Nodeconfig"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%
  	String menuTable = (String)request.getAttribute("menuTable");
  	String tmp = request.getParameter("id"); 
	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String hostname = "";
	String sysname = "";
	String CSDVersion = "";
	String mac = "";
	List cpuperflist = new ArrayList();
	Hashtable pefcpuhash = new Hashtable();
	
    	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
    	System.out.println("sysOid===="+host.getSysOid());
    	String orderflag = request.getParameter("orderflag");
    	if(orderflag == null || orderflag.trim().length()==0)
    		orderflag="index";
        String[] time = {"",""};
        DateE datemanager = new DateE();
        Calendar current = new GregorianCalendar();
        current.set(Calendar.MINUTE,59);
        current.set(Calendar.SECOND,59);
        time[1] = datemanager.getDateDetail(current);
        current.add(Calendar.HOUR_OF_DAY,-1);
        current.set(Calendar.MINUTE,0);
        current.set(Calendar.SECOND,0);
        time[0] = datemanager.getDateDetail(current);
        
        Nodeconfig nodeconfig = new Nodeconfig();
        String processornum = "";
	    Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
	     if(ipAllData != null){
	     
		    cpuperflist = (List)ipAllData.get("cpuperflist");
			if(cpuperflist != null && cpuperflist.size()>0){
				pefcpuhash = (Hashtable)cpuperflist.get(0);
			}
		
			nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
			if(nodeconfig != null){
				mac = nodeconfig.getMac();
				processornum = nodeconfig.getNumberOfProcessors();
				CSDVersion = nodeconfig.getCSDVersion();
				hostname = host.getAlias();//nodeconfig.getHostname();
			}
			Vector cpuV = (Vector)ipAllData.get("cpu");
			if(cpuV != null && cpuV.size()>0){
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
		
		//得到数据采集时间
		collecttime = (String)ipAllData.get("collecttime");
		
		//得到系统启动时间
		Vector systemV = (Vector)ipAllData.get("system");
		if(systemV != null && systemV.size()>0){
			for(int i=0;i<systemV.size();i++){
				Systemcollectdata systemdata=(Systemcollectdata)systemV.get(i);
				if(systemdata.getSubentity().equalsIgnoreCase("sysUpTime")){
					sysuptime = systemdata.getThevalue();
				}
				if(systemdata.getSubentity().equalsIgnoreCase("sysServices")){
					sysservices = systemdata.getThevalue();
				}
				if(systemdata.getSubentity().equalsIgnoreCase("sysDescr")){
					sysdescr = systemdata.getThevalue();
				}
				if(systemdata.getSubentity().equalsIgnoreCase("SysName")){
					sysname = systemdata.getThevalue();
				}
			}
		}
	}
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
								
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
		
	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager=new HostCollectDataManager();
	try{
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}
	 //System.out.println("3333333333");	
    request.setAttribute("id", tmp);
    request.setAttribute("ipaddress", host.getIpAddress());
	request.setAttribute("cpuvalue", cpuvalue);
	request.setAttribute("collecttime", collecttime);
	request.setAttribute("sysuptime", sysuptime);
	request.setAttribute("sysservices", sysservices);
	request.setAttribute("sysdescr", sysdescr);
	request.setAttribute("pingconavg", new Double(pingconavg));
	
  	String[] diskItem={"AllSize","UsedSize","Utilization"};

	Hashtable max = (Hashtable) request.getAttribute("max");

	Hashtable Disk = (Hashtable)request.getAttribute("Disk");
    String newip = (String)request.getAttribute("newip");
    	
	double avgpingcon = (Double)request.getAttribute("pingconavg");
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	
	int cpuper = Double.valueOf(cpuvalue).intValue();

  	String rootPath = request.getContextPath(); 
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  	String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  	DefaultPieDataset dpd = new DefaultPieDataset();
  	dpd.setValue("可用率",avgpingcon);
  	dpd.setValue("不可用率",100 - avgpingcon);
  	chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
  
       	chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   
        chart3 = ChartCreator.createMeterChart(40.0,"",120,120);       
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
    	
    	String subtype="";
	if("scoopenserver".equals(host.getSysOid())) {
		subtype = "scoopenserver";
	}else if("scounixware".equals(host.getSysOid())){
		subtype = "scounixware";
	}
     	
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
function gzmajax(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				$("#flashcontent00gzm").html(data.percent1+":"+data.percent2+":"+data.cpuper);
				var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1="+data.percent1+"&percentStr1=可用&percent2="+data.percent2+"&percentStr2=不可用", "Pie_Component", "160", "160", "8", "#ffffff");
				so.write("flashcontent00");
				var so1 = new SWFObject("<%=rootPath%>/flex/DHCCGauge.swf?percent="+data.cpuper, "Pie_Component1", "160", "160", "8", "#ffffff");
				so1.write("flashcontent01");
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
setInterval(gzmajax,60000);
});
</script>

<script language="javascript">	
  
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
	
	
});

  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
    function toGetConfigFile()
  {
        msg.style.display="block";
        mainForm.action = "<%=rootPath%>/cfgfile.do?action=getcfgfile&ipaddress=<%=host.getIpAddress()%>";
        mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  
function changeOrder(para){
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function reportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostping_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
  
 
  
// 全屏观看
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("topology/network/index.jsp", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}

</script>

<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}

//添加菜单	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}
	setClass();
}

function setClass(){
	document.getElementById('scounixDetailTitle-1').className='detail-data-title';
	document.getElementById('scounixDetailTitle-1').onmouseover="this.className='detail-data-title'";
	document.getElementById('scounixDetailTitle-1').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>


</head>
<body id="body" class="body" onload="initmenu();">

	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="id">
		
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<%=menuTable%>
							</td>	
						</tr>
					</table>
				</td>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-detail">
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td>
											<table id="container-main-detail" class="container-main-detail" >
												<tr>
													<td>
									<table id="detail-content" class="detail-content" width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
				                      <tr>
					                    <td align="left" ><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					                    <td class="layout_title" align="center"><b >设备详细信息</b></td>
					                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
				                     </tr>
			             	          <tr>
				                       <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;主机名:
										        <span id="lable"><%=hostname%></span> </td>
                                       <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;系统名称:
						 					<span id="sysname"><%=sysname%></span></td>
                                       <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP地址:
											<%
												List iplist = null;
												IpAliasDao ipdao = new IpAliasDao();
												try {
													iplist = ipdao.loadByIpaddress(host.getIpAddress());
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													ipdao.close();
												}
	
												IpAlias ipalias = null;
												ipdao = new IpAliasDao();
												try {
													ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(), "1");
	
												} catch (Exception e) {
													e.printStackTrace();
												} finally {
													ipdao.close();
												}
	
												if (iplist == null)
													iplist = new ArrayList();
											%>
												<select name="ipalias<%=host.getIpAddress()%>">
													<option selected><%=host.getIpAddress()%></option>
													<%
														for (int j = 0; j < iplist.size(); j++) {
															IpAlias voTemp = (IpAlias) iplist.get(j);
													%>
															<option
															<%
																if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ 
															%>
																	selected 
															<%
																} 
															%>		
																>
															<%=voTemp.getAliasip()%>
															</option>
													<%
														}
													%>
												</select>
												[<a href="#" style="cursor: hand" onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">修改</a>]
											</TD>
				                           </tr>
			                              </table>
				        							</td>
				        						</tr>
				        					</table>
										</td>
									</tr>
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=scounixDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
												      		  <tr>
                    <td align=center >
											    		  <% String str1 = "",str2="";
                  if(max.get("avgpingcon")!=null){
                          str1 = (String)max.get("avgpingcon");
                  }
                  if(max.get("avgrespcon")!=null){
                          str2 = (String)max.get("avgrespcon");
                  }
                  %>          

                    
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
              							<tr> 
                							<td width="100%" align=center> 
                								<div id="flashcontent1">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=host.getIpAddress()%>", "Line_CPU", "346", "250", "8", "#ffffff");
													so.write("flashcontent1");
												</script>				
							                </td>
										</tr>             
									</table> 
                  </td>
                  <td align=center >
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent2">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Area_Memory.swf?ipadress=<%=host.getIpAddress()%>", "Area_flux", "346", "250", "8", "#ffffff");
													so.write("flashcontent2");
												</script>				
							                </td>
										</tr>             
									</table> 
                      </td>
                    </tr> 
                    <tr>
                    <td align=center >
                    <br>
                      <table cellpadding="0" cellspacing="0" width=48% align=center>
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent3">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "250", "8", "#ffffff");
													so.write("flashcontent3");
												</script>				
							                </td>
										</tr>             
									</table> 
                  </td>
                  <td align=center >
                  <br>
                      <table cellpadding="0" cellspacing="0" width=48%>
              							<tr> 
                							<td width="100%"  align=center> 
                								<div id="flashcontent4">
													<strong>You need to upgrade your Flash Player</strong>
												</div>
												<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "250", "8", "#ffffff");
													so.write("flashcontent4");
												</script>				
							                </td>
										</tr>             
									</table> 
                      </td>
                  </tr>                                                     					
	            <tr>
		            <td align=center colspan=2>
		                <br>
		                <table cellpadding="0" cellspacing="0" width=92%>
                    <tr>
			            <td width="100%" align=center height="350">
			                <table cellpadding="0" cellspacing="0" width=100% height="100%">
			         		    <tr> 
			           			    <td width="100%"  align=center> 
			           				    <div id="flashcontent6">
											<strong>You need to upgrade your Flash Player</strong>
										</div>
										<script type="text/javascript">
											var so = new SWFObject("<%=rootPath%>/flex/AreaCpu_detail_month.swf?ipadress=<%=host.getIpAddress()%>&id=2", "AreaCpu_detail", "750", "350", "8", "#ffffff");
											so.write("flashcontent6");
										</script>				
					                </td>
						        </tr>             
							</table> 
			            </td>
			        </tr>
			        <tr> 
                      <td>
                        <center>              
                <!--CPU性能开始-->
                
                <table width="90%" cellpadding="0" cellspacing="0">
                  <tr lign="center" height="28" bgcolor="#ECECEC"> 
                  	<td width="20%"  class="detail-data-body-title" align="center">%用户</td>
                    	<td width="20%"  class="detail-data-body-title" align="center">%系统</td>
                    	<td width="20%"  class="detail-data-body-title" align="center">%io等待</td>
                    	<td width="20%"  class="detail-data-body-title" align="center">%空闲</td>
                    	<td width="20%"  class="detail-data-body-title" align="center">物理</td>
                    
                  </tr>
                 <%
                 	if(pefcpuhash != null && pefcpuhash.size()>0){
      		%>
                  <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28> 
                    <td  class="detail-data-body-list" align="center"><%=(String)pefcpuhash.get("%usr")%></td>
                    <td  class="detail-data-body-list" align="center"><%=(String)pefcpuhash.get("%sys")%></td>
                    <td  class="detail-data-body-list" align="center"><%=(String)pefcpuhash.get("%wio")%></td>
                    <td  class="detail-data-body-list" align="center"><%=(String)pefcpuhash.get("%idle")%></td>
                    <td  class="detail-data-body-list" align="center"><%=(String)pefcpuhash.get("%intr")%></td>
                  </tr>      		
      		<% } %>
                </table>
                <!--CPU性能结束-->                        
                       </center>                         
                       </td>
                    </tr>            
						</table> 
		            </td>
	            </tr>
											    		</table>
											    </td>
											  </tr>
											</table>
											
										</td>
									</tr>
								</table>
							</td>
							<td class="td-container-main-tool">
								<jsp:include page="/include/scounixwaretoolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
									<jsp:param value="<%=subtype%>" name="subtype"/>
								</jsp:include>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		
	</form>
</BODY>
</HTML>