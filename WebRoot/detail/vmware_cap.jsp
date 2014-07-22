<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<% 
 	String runmodel = PollingEngine.getCollectwebflag();//采集与访问模式
	String menuTable = (String) request.getAttribute("menuTable");//菜单
	String ip = (String)request.getAttribute("ip"); 
	System.out.println(ip);
	String tmp = request.getParameter("id"); 
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	double cpuvalue = 0; 
	String sysdescr = "";
	
	//内存利用率和响应时间 begin 
	Vector memoryVector = new Vector(); 
	//end
	
    //ping和响应时间begin
    String avgresponse = "0";
	String maxresponse = "0";
	String pingconavg ="0";
 	String maxpingvalue = "0";
    //end
    
    //时间设置begin
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
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd"); 
	String time1 = sdf2.format(new Date());	
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	//end
    	
   	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
   	if(host == null){
   		//从数据库里获取
   		HostNodeDao hostdao = new HostNodeDao();
   		HostNode node = null;
   		try{
   			node = (HostNode)hostdao.findByID(tmp);
   		}catch(Exception e){
   		}finally{
   			hostdao.close();
   		}
   		HostLoader loader = new HostLoader();
   		loader.loadOne(node);
   		loader.close();
   		host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
   	}
   	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);    	
    	 
	//存放环境信息
	Vector powervector = new Vector();
	Vector envvector = new Vector();
        
        if("0".equals(runmodel)){
		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
		if(ipAllData != null){
			Vector cpuV = (Vector)ipAllData.get("cpu");
			if(cpuV != null && cpuV.size()>0){
				
				CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
			//2.内存获取

			//得到电源信息

		}
		
		//----ping值和响应时间begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization",starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			pingconavg = pingconavg.replace("%", "");
			maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
			maxpingvalue =maxpingvalue.replaceAll("%",""); 
		}
		} catch (Exception ex) {
		
			ex.printStackTrace();
		}  
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host .getIpAddress(), "Ping", "ResponseTime", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
				avgresponse = avgresponse.replace("毫秒", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----ping值和响应时间end
		
		
		Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
		if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
		}
	}else{
		//采集与访问是分离模式
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		List arpList = new ArrayList();
		List envlist = new ArrayList();
		
		//3.
		List memoryList = new ArrayList();
		try{
			systemList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrPingInfo();
			cpuvalue = new Double(new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrCpuAvgInfo());
			pingconavg = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrDayPingAvgInfo();
			envlist = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrENVInfo();
		//4.
			memoryList = new NetService(host.getId() + "", nodedto.getType(), nodedto.getSubtype()).getCurrMemoryInfo();
		}catch(Exception e){
		}
		//5.
		if (memoryList != null && memoryList.size() > 0) {
			for (int i = 0; i < memoryList.size(); i++) {
				Memorycollectdata memorycollectdata = new Memorycollectdata();
				NodeTemp nodetemp = (NodeTemp) memoryList.get(i);
				memorycollectdata.setSubentity(nodetemp.getSindex());
				memorycollectdata.setThevalue(nodetemp.getThevalue());
				memoryVector.add(memorycollectdata);
			}
		}
		
		if(systemList != null && systemList.size()>0){
			for(int i=0;i<systemList.size();i++){
				NodeTemp nodetemp = (NodeTemp)systemList.get(i);
				if("sysDescr".equals(nodetemp.getSindex()))sysdescr = nodetemp.getThevalue();
			}
		}
		

		//----ping值和响应时间begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization",starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
			pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
			pingconavg = pingconavg.replace("%", "");
			maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
			maxpingvalue =maxpingvalue.replaceAll("%",""); 
		}
		} catch (Exception ex) {
		
			ex.printStackTrace();
		}  
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host .getIpAddress(), "Ping", "ResponseTime", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
				avgresponse = avgresponse.replace("毫秒", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----ping值和响应时间end
	}

	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();
  	String rootPath = request.getContextPath();   

     	IpAliasDao ipdao = new IpAliasDao();
     	List iplist = ipdao.loadByIpaddress(host.getIpAddress());
     	if(iplist == null)iplist = new ArrayList();
     	ipdao.close();
     
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
         		
	String systype = "";
	if(sysdescr!=null){
	    if(sysdescr.toLowerCase().indexOf("h3c")!=-1){
	        systype = "h";
	    }
	    if(sysdescr.toLowerCase().indexOf("cisco")!=-1){
	        systype = "c";
	    }
	}
  //  String ip = host.getIpAddress();
	//String picip = CommonUtil.doip(ip);
	
	//6.
	
	
      ArrayList<HashMap<String, Object>> dslist = (ArrayList<HashMap<String, Object>>)request.getAttribute("dslist");//存储
      ArrayList<HashMap<String, Object>> wulist = (ArrayList<HashMap<String, Object>>)request.getAttribute("wulist");//物理机
      ArrayList<HashMap<String, Object>> rplist = (ArrayList<HashMap<String, Object>>)request.getAttribute("rplist");//资源池
      ArrayList<HashMap<String, Object>> crlist = (ArrayList<HashMap<String, Object>>)request.getAttribute("crlist");//群集
      ArrayList<HashMap<String, Object>> dclist = (ArrayList<HashMap<String, Object>>)request.getAttribute("dclist");//数据中心
      HashMap<String, Object> map_host = (HashMap<String, Object>)request.getAttribute("map_host");
      int alarmlevel = SystemSnap.getNodeStatus(host);
%> 
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />



<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="/afunms/detail/jquery-1.7.min.js"></script>
<script type="text/javascript" src="/afunms/detail/test.js"></script>
<link href="<%=rootPath%>/detail/intstyle1.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.limitWidth{ width:180px; margin:0 auto;}
.sideNavWrap{border:1px solid #c9e1f4; border-bottom:0;}
.limitWidth .style .desc{margin-left:0!important;}
.limitWidth .style.sideNav .icon{float:left; margin-left:10px;}
</style>




<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
});
</script>
<script language="javascript">	
  
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
  
  function toOpen(){
 
	    window.open ("<%=rootPath%>/monitor.do?action=netcpu_report&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
		
}
  
function changeOrder(para){
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
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
	document.getElementById('vmwareDetailTitle-3').className='detail-data-title';
	document.getElementById('vmwareDetailTitle-3').onmouseover="this.className='detail-data-title'";
	document.getElementById('vmwareDetailTitle-3').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}


//网络设备的ip地址
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//获取下拉框的值
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("修改成功！");
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
	$('#wuhost').hide();
	$('#wuliji-detail').bind('click',function(){
			$('#wuhost').slideToggle('slow');
		});
	$('#ziyuanchi').hide();
	$('#ziyuanchi-detail').bind('click',function(){
			$('#ziyuanchi').slideToggle('slow');
		});
	$('#cunchuchi').hide();
	$('#cunchuchi-detail').bind('click',function(){
			$('#cunchuchi').slideToggle('slow');
		});
	$('#shujuzhongxin').hide();
	$('#shujuzhongxin-detail').bind('click',function(){
			$('#shujuzhongxin').slideToggle('slow');
		});	
	$('#qunji').hide();
	$('#qunji-detail').bind('click',function(){
			$('#qunji').slideToggle('slow');
		});				
	
});

function wulijiAjax(vid,flag,ip){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/performancePanelAjaxManager.ajax?action=wulijiAjax&vid="+vid+"&id=<%=tmp%>&flag="+flag+"&name="+encodeURIComponent(ip),
			success:function(data){
			      document.getElementById("div").innerHTML =data.option;
			}
		});
}

function perList(flag){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/performancePanelAjaxManager.ajax?action=perList&id=<%=tmp%>&flag="+flag,
			success:function(data){
			      document.getElementById("div").innerHTML =data.option;
			}
		});
}



 function show_vm(vid,ip,name){  
               window.open("/afunms/detail/vmware_show.jsp?vid="+vid+"&ip="+ip+"&name="+name+"&ipaddress=<%=ip%>",'mywindow','width=650,height=450,resizable=1');
  }
  
function showcpu(ip,vid){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarecpuCR&ip="+ip+"&vid="+vid,'mywindow','width=700,height=450,resizable=1');
       }
function showcpuuse(ip,vid){ 
               window.open("<%=rootPath%>/netreport.do?action=vmwarecputotalCR&ip="+ip+"&vid="+vid,'mywindow','width=,height=,resizable=1');
       }
function showmem(ip,vid){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarememCR&ip="+ip+"&vid="+vid,'mywindow','width=,height=,resizable=1');
       }
function showmemuse(ip,vid){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarememtotalCR&ip="+ip+"&vid="+vid,'mywindow','width=,height=,resizable=1');
       } 
function showcpuRP(ip,vid){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarecpuRP&ip="+ip+"&vid="+vid,'mywindow','width=,height=,resizable=1');
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
				<td width=""></td>
				<td class="td-container-main">
				<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-detail" width=85%>
								<table id="container-main-detail" class="container-main-detail">
									<tr>
										<td> 
											<table id="detail-content" class="detail-content" width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
												<tr>
													<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
													<td class="layout_title"><b>设备详细信息</b></td>
													<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
												</tr>
												<tr>
												<td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;<font color=black>设备标签:</font>
																		<!--  	<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>-->
																		        <span id="lable"><font color=black><%=host.getAlias()%></font></span> </td>
								                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;<font color=black>系统类型:</font>
																			<span id="sysname"><font color=black>VMWare</font></span> </td>
														<td width="20%" height="26" background="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>" class="performance-status">
															&nbsp;<%=NodeHelper.getStatusDescr(alarmlevel)%>
														 </td>					
								                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;<font color=black>IP地址:<%=host.getIpAddress()%></font>
																			</TD>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=vmwareDetailTitleTable%>
											    	</td>
											    </tr>
											    <tr>
											    	<td>
											    		<table class="detail-data-body" >
											    		   <tr>
											    		    <td valign=top align="left"> 
											    		    <table class="detail-data-body" border=0  >
															<tr>
												      		<td valign=top align="left">
									              				
																			 <div class="limitWidth">
																				<ul class="demoList">
																				</ul>
																				<ul class="sideNav" >
																				   
																				   <li ><a href="#" onclick="perList('physical');">物理机</a>
																						<ul>
																						   <%if(wulist!=null && wulist.size()>0){ 
																			             for(int i=0;i<wulist.size();i++){
																			                String name = wulist.get(i).get("name").toString();
																			                 String vid = wulist.get(i).get("vid").toString();
																			                
																			          %>
																					<li><a href="#" onclick="wulijiAjax('<%=vid %>','1','<%=name %>');">
																					<%
																					String temp=name;
																					int length=name.length();
																					if(length>6&&name.getBytes().length>length){
																						temp=name.substring(0,5)+"...";
																					}else if(name.length()>16){
																						temp=name.substring(0,15)+"...";
																					}%>
																					 <acronym title="<%=name%>"><%=temp%></acronym>
																					 </a></li>
																					   <% 
																			             }
																			            } 
																			          %>
																						</ul>
																					</li>
																				   
																			       <li><a href="#" onclick="perList('resourcepool');">资源池</a>
																						<ul>
																						   <%if(rplist!=null && rplist.size()>0){ 
																			             for(int i=0;i<rplist.size();i++){
																			                String name = rplist.get(i).get("name").toString();
																			                 String vid = rplist.get(i).get("vid").toString();
																			          %>
																					<li><a href="#" onclick="wulijiAjax('<%=vid %>','5','<%=name %>');">
																					<%
																					String temp=name;
																					int length=name.length();
																					if(length>6&&name.getBytes().length>length){
																						temp=name.substring(0,5)+"...";
																					}else if(name.length()>16){
																						temp=name.substring(0,15)+"...";
																					}%>
																					 <acronym title="<%=name%>"><%=temp%></acronym></a></li>
																					   <% 
																			             }
																			            } 
																			          %>
																							</ul>
																						</li>
																						
																						
																						 <li><a href="#" onclick="perList('datastore');">存储</a>
																							<ul>
																							   <%if(dslist!=null && dslist.size()>0){ 
																			             for(int i=0;i<dslist.size();i++){
																			                String name = dslist.get(i).get("name").toString();
																			                 String vid = dslist.get(i).get("vid").toString();
																			          %>
																						<li><a href="#" onclick="wulijiAjax('<%=vid %>','2','<%=name %>');">
																						<%
																						int length=name.length();
																						String temp=name;
																						if(length>6&&name.getBytes().length>length){
																							temp=name.substring(0,5)+"...";
																						}else if(name.length()>16){
																							temp=name.substring(0,15)+"...";
																						}
																						%>
																						 <acronym title="<%=name%>"><%=temp%></acronym>
																						
																						</a></li>
																						   <% 
																			             }
																			            } 
																			          %>
																						</ul>
																					</li>
																					
																					 <li><a href="#" onclick="perList('yun');">集群</a>
																						<ul>
																						   <%if(crlist!=null && crlist.size()>0){ 
																			             for(int i=0;i<crlist.size();i++){
																			                String name = crlist.get(i).get("name").toString();
																			                 String vid = crlist.get(i).get("vid").toString();
																			               
																								String temp=name;
																								int length=name.length();
																								if(length>6&&name.getBytes().length>length){
																									temp=name.substring(0,5)+"...";
																								}else if(name.length()>16){
																									temp=name.substring(0,15)+"...";
																								}
																			          %>
																							<li>
																							<a href="#" onclick="wulijiAjax('<%=vid %>','3','<%=name %>');"><acronym title="<%=name%>"><%=temp%></acronym></a>
																							</li>
				  																		 <% 
																			             }
																			            } 
																			          %>
																						</ul>
																					</li>
																					
																				</ul>
																				</div>
																			        
												                 </td>
												           </tr> 												            
												        
												            
												         </table>
												       </td>
												       
												       <td align=left width=100% valign=top >
												         <div id = "div" >
												            <table  class=detail-data-body>
												             <tr>
												              <td>
												                <div id='wuhost'>
												                <table cellspacing="0" cellpadding="0" width="100%" bgcolor="#FFFFFF" border=1  class="detail-data-body">
												                    <tr align="center" height=28 class="microsoftLook0">
												                         <th width='10%'  align="center">序号</th>
    									                                 <th width='10%' align="center">CPU利用率</th>
    									                                 <th width='15%' align="center">内存(虚拟增长)MB</th>
    									                                 <th width='20%' align="center">内存(MBps)换入/换出</th>
    									                                 <th width='15%' align="center">CPU(MHz)使用</th>
    									                                 <th width='10%' align="center">内存使用</th>
    									                                 <th width='20%' align="center">磁盘(KBps)使用</th>
								                                    </tr>
								                                    
								                                    <% 
								                                    DecimalFormat df = new DecimalFormat("0.0");
								                                    if(map_host.size()>0 && map_host != null){
								                                          for(int i =0;i<wulist.size();i++){
								                                             List list = (ArrayList)map_host.get(wulist.get(i).get("vid").toString());
								                                             if(list != null && list.size()>0){
								                                       %>
								                                    <tr align="center" height=28 class="microsoftLook">
    									                                 <td width='10%' align="center"><%=i+1 %></td>
    									                                 <td width='10%' align="center"><%=df.format(Long.parseLong((String)list.get(0))/100)+"%" %></td>
    									                                 <td width='15%' align="center"><%=list.get(2) %></td>
    									                                 <td width='20%' align="center"><%=list.get(4)+"/"+list.get(3) %></td>
    									                                 <td width='15%' align="center"><%=list.get(5) %></td>
    									                                 <td width='10%' align="center"><%=df.format(Long.parseLong((String)list.get(6))/100)+"%" %></td>
    									                                 <td width='20%' align="center"><%=list.get(7)%></td>
								                                    </tr>
								                                    <%}else{ %>
								                                     <tr align="center" height=28 class="microsoftLook">
    									                                 <td width='10%' align="center"><%=i+1 %></td>
    									                                 <td width='10%' align="center"><%="0%" %></td>
    									                                 <td width='15%' align="center"><%=0 %></td>
    									                                 <td width='20%' align="center"><%=0+"/"+0%></td>
    									                                 <td width='15%' align="center"><%=0 %></td>
    									                                 <td width='10%' align="center"><%=0+"%" %></td>
    									                                 <td width='20%' align="center"><%=0%></td>
								                                    </tr>
								                                    <%
								                                    }
								                                    }
								                                    } %>
												                  </table>
												                 </div>
												               </td>
												            </tr>
												          </table>
												          
												         </div>
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
				
				<td class="td-container-main-tool" width='14%'>
								<jsp:include page="/include/toolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="network" name="category"/>
									<jsp:param value="<%=nodedto.getSubtype()%>" name="subtype"/>
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