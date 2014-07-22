<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
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
	String tmp = request.getParameter("id"); 
	String flag1 = request.getParameter("flag");  
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	double cpuvalue = 0; 
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String syslocation = ""; 
	
	//内存利用率和响应时间 begin 
	Vector memoryVector = new Vector(); 
	String memoryvalue="0";
	//end
	
    //ping和响应时间begin
    String avgresponse = "0";
	String maxresponse = "0";
	String responsevalue = "0"; 
	String pingconavg ="0";
 	String maxpingvalue = "0";
	String pingvalue = "0";
    //end
    
    ArrayList<HashMap<String, Object>> vmlist = (ArrayList<HashMap<String, Object>>) request.getAttribute("vmlist");
    ArrayList<HashMap<String, Object>> wulist = (ArrayList<HashMap<String, Object>>) request.getAttribute("wulist");
    //System.out.println("-----虚拟主机个数------------》"+vmlist.size());
    
   // String host_name="";
   // HashMap host_num = new HashMap();
   // if(wulist != null && wulist.size()>0){
   //    for(int i=0;i<wulist.size();i++){host_num.put(wulist.get(i).get("vid"),wulist.get(i).get("name"));}
   // }
    //table.size() 有几个虚拟主机
    
    
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
    String starttime = time[0];
    String endtime = time[1]; 
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd"); 
	String time1 = sdf2.format(new Date());	
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	//end
    	
  
	String curin = "0";
	String curout = "0";
    	
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
			collecttime = sdf1.format(cc);
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
				if("sysUpTime".equals(nodetemp.getSindex()))sysuptime = nodetemp.getThevalue();
				if("sysDescr".equals(nodetemp.getSindex()))sysdescr = nodetemp.getThevalue();
				if("sysLocation".equals(nodetemp.getSindex()))syslocation = nodetemp.getThevalue();
			}
		}
		if(pingList != null && pingList.size()>0){
			for(int i=0;i<pingList.size();i++){
				NodeTemp nodetemp = (NodeTemp)pingList.get(i);
				if("ConnectUtilization".equals(nodetemp.getSindex())){
					collecttime = nodetemp.getCollecttime();
				}
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
    String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	int alarmlevel = SystemSnap.getNodeStatus(host);
	//6.
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
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
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
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=netAjaxUpdate&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
				 $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "平均连通率" });
				$("#cpu").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png?nowtime="+(new Date()), alt: "当前CPU利用率" }); 
			
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
//setInterval(gzmajax,60000);
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
	document.getElementById('vmwareDetailTitle-2').className='detail-data-title';
	document.getElementById('vmwareDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('vmwareDetailTitle-2').onmouseout="this.className='detail-data-title'";
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
//setInterval(modifyIpAliasajax,60000);
});
</script>

<style>


a{ text-decoration:none; border:none;}
a:hover{ text-decoration:none;}
img{ cursor:pointer; border:none;}
ul,li{list-style:none; padding:0; margin:0;}


h3{ font-size:16px; font-weight:bold; color:#002280; text-align:left; padding-left:20px; line-height:28px;}
.oper{ margin :150 0 200 0; backgroud-color:#002280;}
.line{ height:20px; background:url(/afunms/img/vmtop.png) repeat-x;}



</style>
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
				<td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;设备标签:
										<!--  	<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>修改</a>]</td>-->
										        <span id="lable"><%=host.getAlias()%></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;系统类型:
											<span id="sysname">VMWare</span> </td>
						<td width="20%" height="26" background="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>" class="performance-status">
							&nbsp;<%=NodeHelper.getStatusDescr(alarmlevel)%>
						</td>
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP地址:<%=host.getIpAddress()%>
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
											    		<table class="detail-data-body">
												      		
												      		<tr>
												      			<td valign=top align="center">
												         
												                <br>
												                      <table cellpadding="0" cellspacing="0" style="align:center;width:98%" bgcolor="#ECECEC">
									              							<%
									              							
									              							    Hashtable vmtable = new Hashtable();
									              							    HashMap wutable = new HashMap();
									              							    List list = new ArrayList();
                                                                                if(vmlist != null && vmlist.size()>0){
                                                                                   for(int i=0;i<vmlist.size();i++){
                                                                                             vmtable.put(vmlist.get(i).get("hoid"),vmlist.get(i));
                                                                                          }
                                                                                    for(int j=0;j<wulist.size();j++){
                                                                                             wutable.put(wulist.get(j).get("vid").toString(),wulist.get(j).get("name").toString());
                                                                                             List vm_list = new ArrayList();
                                                                                            // if(vmlist.contains(wulist.get(j).get("hoid"))){
                                                                                                 
                                                                                         //    }
                                                                                    }       
                                                                             //      System.out.println(vmlist.toString()+"-----table   size----------"+wutable+"----------------");
                                                                                   HashMap host_num = new HashMap();
                                                                                   String host_name="";
                                                                                   String _flag="";
                                                                            //       System.out.println("---------vmlist----------"+vmlist);
                                                                                   int x =1;
                                                                                   
                                                                                     if(wulist != null && wulist.size()>0){
                                                                                         for(int k=0;k<wulist.size();k++){
                                                                                          List<HashMap<String, Object>> num = new ArrayList();
                                                                                          for(int i=0;i<vmlist.size();i++){
                                                                                           if(vmlist.get(i).get("hoid").equals(wulist.get(k).get("vid"))){
                                                                                               num.add(vmlist.get(i));
                                                                                             }
                                                                                           }
                                                                                           if(num.size() == 0) continue;
                                                                             //              System.out.println("---------num----------"+num.size());
                                                                             //               System.out.println("---------name----------"+wulist.get(k).get("name"));
                                                                                          // for(int i=0;i<num.size();i++){
                                                                                             if(num.size() == 1){
									              							 %>
									              							<tr style="align:center"> 
									                							<td width="200"  bgcolor="#FFFFFF"></td>
									                							<td width="100" align=right bgcolor="#FFFFFF" height=350 valign=top>
									                							    <table cellpadding="0" cellspacing="0" style="align:left;" bgcolor="#FFFFFF">
									                							       <tr><td bgcolor="#FFFFFF"  valign=bottom  height=80 style="padding :0 55px 0 45px;"><font size=4 color=""><%=wulist.get(k).get("name") %></font></td></tr>
									                							      <tr>
									                							           <td bgcolor="#FFFFFF"   align=right height=5></td>
																                      </tr>
									                							      <tr>
									                							           <td bgcolor="#FFFFFF"  valign=top align=right><img src="/afunms/img/vmhost.png"></td>
																                      </tr>
																                    </table>  
																                </td>
																                <td width="750" style="align:left;" bgcolor="#FFFFFF"> 
									                									<table cellpadding="0" cellspacing="0" style="align:left;" bgcolor="#FFFFFF">
																							 <%
									                									    for(int j=0;j<num.size();j++){
									                									       if(1 == 1){
									                									          String name = num.get(j).get("name").toString();
									                									          String guestfullname = num.get(j).get("guestfullname").toString();
									                									          String cpu = num.get(j).get("cpu").toString();
									                									         // String numcore = vmlist.get(j).get("numcore").toString();
									                									          String memorysizemb = num.get(j).get("memorysizemb").toString();
									                									          String powerstate = num.get(j).get("powerstate").toString();
									                									          String state = "";
																								  if(powerstate.equalsIgnoreCase("poweredOn")){state="通电";}else if(powerstate.equalsIgnoreCase("poweredOff")){state="未通电";}else if(powerstate.equalsIgnoreCase("suspended")){state="挂起";}
									                									      
									                									    %>
																							<tr bgcolor="#FFFFFF">
																								<td  align=left>
																								  <div style="background:url(/afunms/img/vmtop.png);width:310;height=120;align:left;background-repeat: no-repeat; margin :40 0 192 0;" >
																									<table  cellpadding="0" cellspacing="0">
																									         <tr><td width=30></td><td height=8></td></tr>
																											<tr >
																												<td width=30></td><td  align=center height=22><%=name %></td>
																											</tr> 
																											<tr >
																												<td width=30></td><td  align=left height=22>全名：<%=guestfullname %></td>
																											</tr>
																											<tr >
																												<td width=30></td><td  align=left height=22>CPU配置：<%=cpu %></td>
																											</tr> 
																											<tr >
																												<td width=30></td><td  align=left height=22>内存分配(Mb)：<%=memorysizemb %></td>
																											</tr>
																											<tr >
																												<td width=30></td><td  align=left height=22>电源状态：<%=state %></td>
																											</tr>
																		   							</table>
																		   						   </div>
																								</td>
																								<td  >&nbsp;&nbsp;&nbsp;&nbsp;</td>
																							 </tr>
																							 <%} }%>
																							 
														   								</table>				
																                </td>
																			</tr>
																			<%
																			   }else{
																			 %>
																			 
																			 <tr style="align:center"> 
																			    <td width="200"  bgcolor="#FFFFFF"></td>
									                							<td width="100" align=right bgcolor="#FFFFFF" height=350 valign=top>
									                							    <table cellpadding="0" cellspacing="0" style="align:left;" bgcolor="#FFFFFF">
									                							      <tr><td bgcolor="#FFFFFF"  valign=bottom  height=80 style="padding :0 55px 0 45px;"><font size=4 color=""><%=wulist.get(k).get("name") %></font></td></tr>
									                							      <tr>
									                							           <td bgcolor="#FFFFFF"   align=right height=5></td>
																                      </tr>
									                							      <tr>
									                							           <td bgcolor="#FFFFFF"  valign=top align=right><img src="/afunms/img/vmhost.png"></td>
																                      </tr>
																                    </table>  
																                </td>
																                <td width="750" style="align:left;" bgcolor="#FFFFFF"> 
									                									<table cellpadding="0" cellspacing="0" style="align:left;" bgcolor="#FFFFFF">
									                									   <%
									                									    for(int j=0;j<num.size();j++){
									                									 //   System.out.println(_flag.equalsIgnoreCase(vmlist.get(j).get("hoid").toString()));
									                									       if(1 == 1){
									                									          String name = num.get(j).get("name").toString();
									                									          String guestfullname = num.get(j).get("guestfullname").toString();
									                									           String cpu = num.get(j).get("cpu").toString();
									                									            //String numcore = vmlist.get(j).get("numcore").toString();
									                									             String memorysizemb = num.get(j).get("memorysizemb").toString();
									                									              String powerstate = num.get(j).get("powerstate").toString();
									                									              String state = "";
																								      if(powerstate.equalsIgnoreCase("poweredOn")){state="通电";}else if(powerstate.equalsIgnoreCase("poweredOff")){state="未通电";}else if(powerstate.equalsIgnoreCase("suspended")){state="挂起";}
									                									    %>
									                									
																							<tr bgcolor="#FFFFFF">
																								<td  align=left>
																								   <%if(j==0){ %>
																								 <div style="background:url(/afunms/img/vmtop.png);width:310;height=120;align:left;background-repeat: no-repeat;" >
																								<% }else if(j==num.size()-1){%>
																								 <div style="background:url(/afunms/img/vmdown.png);width:310;height=120;align:left;background-repeat: no-repeat;" >
																								<%}else{ %>
																								 <div style="background:url(/afunms/img/vmmi.png);width:310;height=120;align:left;background-repeat: no-repeat;" >
																								<% }%>	
																									<table  cellpadding="0" cellspacing="0">
																									        <tr><td width=30></td><td height=8></td></tr>
																											<tr >
																												<td width=30></td><td  align=center height=22><%=name %></td>
																											</tr> 
																											<tr >
																												<td width=30></td><td  align=left height=22>全名：<%=guestfullname %></td>
																											</tr>
																											<tr >
																												<td width=30></td><td  align=left height=22>CPU配置：<%=cpu %></td>
																											</tr> 
																											<tr >
																												<td width=30></td><td  align=left height=22>内存分配(Mb)：<%=memorysizemb %></td>
																											</tr>
																											<tr >
																												<td width=30></td><td  align=left height=22>电源状态：<%=state %></td>
																											</tr>
																		   							</table>
																		   						   </div>
																								</td>
																								<td  >&nbsp;&nbsp;&nbsp;&nbsp;</td>
																							 </tr>
																							 <%
																							    }
																							    }
																							    }
																							  %>
														   								</table>				
																                </td>
																			</tr>
																			<%
																			//   }
																			   }
																			   }
																			   }
																			 %>
																			
																		</table> 
																		
																		
											    </td>
											  </tr>
											</table>
										</td>
									</tr>
									</table>
								</table>
							</td>
							<td width=""></td>
							<td class="td-container-main-tool" width=14%>
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
<script type="text/javascript">
Ext.onReady(function()
{
Ext.get("bkpCfg").on("click",function(){
//Ext.MessageBox.wait('数据加载中，请稍后.. ');
//mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=bkpCfg&ipaddress=<%=host.getIpAddress()%>&page=netenv&id="+<%=tmp %>;
//mainForm.submit();
window.open("<%=rootPath%>/vpntelnetconf.do?action=detailPage_readybkpCfg&ipaddress=<%=host.getIpAddress()%>&page=liusu&id="+<%=tmp %>,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
});
});
</script>
</BODY>
</HTML>