<%@page language="java" contentType="text/html;charset=gb2312"%>
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

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@ page import="com.afunms.config.dao.*"%>
<%@ page import="com.afunms.config.model.*"%>

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
	String memoryvalue = "0";
	//end

	//ping和响应时间begin
	String avgresponse = "0";
	String maxresponse = "0";
	String responsevalue = "0";
	String pingconavg = "0";
	String maxpingvalue = "0";
	String pingvalue = "0";
	//end

	//时间设置begin
	String[] time = { "", "" };
	DateE datemanager = new DateE();
	Calendar current = new GregorianCalendar();
	current.set(Calendar.MINUTE, 59);
	current.set(Calendar.SECOND, 59);
	time[1] = datemanager.getDateDetail(current);
	current.add(Calendar.HOUR_OF_DAY, -1);
	current.set(Calendar.MINUTE, 0);
	current.set(Calendar.SECOND, 0);
	time[0] = datemanager.getDateDetail(current);
	String starttime = time[0];
	String endtime = time[1];
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf2.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	//end
	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	if (host == null) {
		//从数据库里获取
		HostNodeDao hostdao = new HostNodeDao();
		HostNode node = null;
		try {
			node = (HostNode) hostdao.findByID(tmp);
		} catch (Exception e) {
		} finally {
			hostdao.close();
		}
		HostLoader loader = new HostLoader();
		loader.loadOne(node);
		loader.close();
		host = (Host) PollingEngine.getInstance().getNodeByID(
				Integer.parseInt(tmp));
	}
	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
	String ipaddress = host.getIpAddress();

	//存放IPMAC信息
	Vector ipmacvector = new Vector();

	if ("0".equals(runmodel)) {
		//采集与访问是集成模式
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData != null) {
			Vector cpuV = (Vector) ipAllData.get("cpu");
			if (cpuV != null && cpuV.size() > 0) {

				CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
			//2.内存获取
			memoryVector = (Vector) ipAllData.get("memory");
			//得到系统启动时间
			Vector systemV = (Vector) ipAllData.get("system");
			if (systemV != null && systemV.size() > 0) {
				for (int i = 0; i < systemV.size(); i++) {
					Systemcollectdata systemdata = (Systemcollectdata) systemV
							.get(i);
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysUpTime")) {
						sysuptime = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysServices")) {
						sysservices = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysDescr")) {
						sysdescr = systemdata.getThevalue();
					}
				}
			}
			//得到IPMAC信息
			ipmacvector = (Vector) ipAllData.get("ipmac");
			if (ipmacvector == null)
				ipmacvector = new Vector();
		}

		//----ping值和响应时间begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host
					.getIpAddress(), "Ping", "ConnectUtilization",
					starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				pingconavg = (String) ConnectUtilizationhash
						.get("avgpingcon");
				pingconavg = pingconavg.replace("%", "");
				maxpingvalue = (String) ConnectUtilizationhash
						.get("pingmax");
				maxpingvalue = maxpingvalue.replaceAll("%", "");
			}
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host
					.getIpAddress(), "Ping", "ResponseTime",
					starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash
						.get("avgpingcon");
				avgresponse = avgresponse.replace("毫秒", "").replaceAll(
						"%", "");
				maxresponse = (String) ConnectUtilizationhash
						.get("pingmax");
				maxresponse = maxresponse.replaceAll("%", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----ping值和响应时间end

		Vector pingData = (Vector) ShareData.getPingdata().get(
				host.getIpAddress());
		if (pingData != null && pingData.size() > 0) {
			Pingcollectdata pingdata = (Pingcollectdata) pingData
					.get(0);
			Calendar tempCal = (Calendar) pingdata.getCollecttime();
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
		}
	} else {
		//采集与访问是分离模式
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		List arpList = new ArrayList();
		//3.
		List memoryList = new ArrayList();
		try {
			systemList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getCurrPingInfo();
			cpuvalue = new Double(new NetService(host.getId() + "",
					nodedto.getType(), nodedto.getSubtype())
					.getCurrCpuAvgInfo());
			pingconavg = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype())
					.getCurrDayPingAvgInfo();
			//arpList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getARPInfo();
			//4.
			memoryList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype())
					.getCurrMemoryInfo();
		} catch (Exception e) {
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
		if (systemList != null && systemList.size() > 0) {
			for (int i = 0; i < systemList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) systemList.get(i);
				if ("sysUpTime".equals(nodetemp.getSindex()))
					sysuptime = nodetemp.getThevalue();
				if ("sysDescr".equals(nodetemp.getSindex()))
					sysdescr = nodetemp.getThevalue();
				if ("sysLocation".equals(nodetemp.getSindex()))
					syslocation = nodetemp.getThevalue();
			}
		}
		if (pingList != null && pingList.size() > 0) {
			for (int i = 0; i < pingList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) pingList.get(i);
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					collecttime = nodetemp.getCollecttime();
				}
			}
		}
		if (arpList != null && arpList.size() > 0) {
			for (int i = 0; i < arpList.size(); i++) {
				IpMac ipmac = (IpMac) arpList.get(i);
				ipmacvector.add(ipmac);
			}
		}
		//----ping值和响应时间begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host
					.getIpAddress(), "Ping", "ConnectUtilization",
					starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				pingconavg = (String) ConnectUtilizationhash
						.get("avgpingcon");
				pingconavg = pingconavg.replace("%", "");
				maxpingvalue = (String) ConnectUtilizationhash
						.get("pingmax");
				maxpingvalue = maxpingvalue.replaceAll("%", "");
			}
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host
					.getIpAddress(), "Ping", "ResponseTime",
					starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash
						.get("avgpingcon");
				avgresponse = avgresponse.replace("毫秒", "").replaceAll(
						"%", "");
				maxresponse = (String) ConnectUtilizationhash
						.get("pingmax");
				maxresponse = maxresponse.replaceAll("%", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----ping值和响应时间end
	}

	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();
	String rootPath = request.getContextPath();

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

	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	//6.
	int allmemoryvalue = 0;
	if (memoryVector != null && memoryVector.size() > 0) {
		for (int i = 0; i < memoryVector.size(); i++) {
			Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector
					.get(i);
			allmemoryvalue = allmemoryvalue
					+ Integer.parseInt(memorycollectdata.getThevalue());
		}
		memoryvalue = (allmemoryvalue / memoryVector.size()) + "";
	}
	//生成当天平均连通率图形
	CreatePiePicture _cpp = new CreatePiePicture();
	TitleModel _titleModel = new TitleModel();
	_titleModel.setXpic(150);
	_titleModel.setYpic(150);//160, 200, 150, 100
	_titleModel.setX1(75);//外环向左的位置
	_titleModel.setX2(60);//外环向上的位置
	_titleModel.setX3(65);
	_titleModel.setX4(30);
	_titleModel.setX5(75);
	_titleModel.setX6(70);
	_titleModel.setX7(10);
	_titleModel.setX8(115);
	_titleModel.setBgcolor(0xffffff);
	_titleModel.setPictype("png");
	_titleModel.setPicName(picip + "pingavg");
	_titleModel.setTopTitle("");

	double[] _data1 = { avgpingcon, 100 - avgpingcon };
	String[] p_labels = { "连通", "未连通" };
	int[] _colors = { 0x66ff66, 0xff0000 };
	_cpp.createOneRingChart(_data1, p_labels, _colors, _titleModel);

	//生成CPU仪表盘 
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper);
	List<AclDetail> detailList = (List<AclDetail>) request.getAttribute("detailList");
	
	CreateAmLinePic linePic = new CreateAmLinePic();
	HashMap<Integer, List<?>> sortData = linePic.getSortDetailData(detailList);
	HashMap<String,String> rateMap = linePic.makeAclAmChart(sortData,ipaddress);

	
%>
<html>
	<head>


		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />

		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
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
	
setInterval(gzmajax,60000);
});
</script>

		<script language="javascript">	

$(document).ready(function(){
$('span[id*="span"]').toggle(
function() {$(this).html('&nbsp;&nbsp;-&nbsp;&nbsp;');},
function(){$(this).html('&nbsp;&nbsp;+&nbsp;&nbsp;');}
);
});

  function query()
  {  
     
     mainForm.action = "<%=rootPath%>/monitor.do?action=telnetAcl&flag=1";
     mainForm.submit();
  }
  
  function exportReport(exportType){
  var id=$("#id").val();
  var startdate=$("#startdate").val();
  var todate=$("#todate").val();
 
 window.open('<%=rootPath%>/telnetConfig.do?action=downloadAclReport&id='+id+'&exportType='+exportType+'&startdate='+startdate+'&todate='+todate+'&nowtime='+(new Date()),"_blank","toolbar=no,width=1,height=1,top=2000,left=3000,directories=no,status=no,menubar=no,alwaysLowered=yes");
 
}
function toggles(id){

$('#acl'+id).slideToggle('slow');

}
</script>
		<script language="JavaScript">

	//公共变量
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*根据传入的id显示右键菜单
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //创建弹出菜单
	    var pop=window.createPopup();
	    //设置弹出菜单的内容
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //获得弹出菜单的行数
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //循环设置每行的属性
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //如果设置该行不显示，则行数减一
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //设置是否显示该行
	        rowObjs[i].style.display=(hide)?"none":"";
	        //设置鼠标滑入该行时的效果
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //设置鼠标滑出该行时的效果
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //屏蔽菜单的菜单
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //选择右键菜单的一项后，菜单隐藏
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //显示菜单
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
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
	document.getElementById('netDetailTitle-10').className='detail-data-title';
	document.getElementById('netDetailTitle-10').onmouseover="this.className='detail-data-title'";
	document.getElementById('netDetailTitle-10').onmouseout="this.className='detail-data-title'";
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
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>

	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
			
		</div>
		<!-- 右键菜单结束-->
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="orderflag">

			<input type=hidden name="id" id="id" value="<%=tmp%>">
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
								<td class="td-container-main-detail" width=85%>
									<table id="container-main-detail" class="container-main-detail">
										<tr>
											<td>
												<jsp:include page="/topology/includejsp/systeminfo_net.jsp">
													<jsp:param name="rootPath" value="<%=rootPath%>" />
													<jsp:param name="tmp" value="<%=tmp%>" />
													<jsp:param name="sysdescr" value="<%=sysdescr%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="collecttime" value="<%=collecttime%>" />
													<jsp:param name="picip" value="<%=picip%>" />
													<jsp:param name="syslocation" value="<%=syslocation%>" />
													<jsp:param name="flag1" value="<%=flag1%>" />
													<jsp:param name="pingavg"
														value="<%=Math.round(Float.parseFloat(pingconavg))%>" />
													<jsp:param name="avgresponse" value="<%=avgresponse%>" />
													<jsp:param name="memoryvalue" value="<%=memoryvalue%>" />
												</jsp:include>
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=netDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td colspan=5 height="28" bgcolor="#ECECEC">


															&nbsp;&nbsp;开始日期
															<input type="text" name="startdate" id="startdate"
																value="<%=startdate%>" size="10">
															<a onclick="event.cancelBubble=true;"
																href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																<img id=imageCalendar1 align=absmiddle width=34
																	height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a> 截止日期
															<input type="text" name="todate" id="todate" value="<%=todate%>"
																size="10" />
															<a onclick="event.cancelBubble=true;"
																href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																<img id=imageCalendar2 align=absmiddle width=34
																	height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a>

															<input type="button" name="submitss" value="查询"
																onclick="query()">
															&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															<span style="CURSOR: hand" onclick="exportReport('doc')"><img
																	name="selDay1" alt='导出WORD'
																	src="<%=rootPath%>/resource/image/export_word.gif"
																	width=18 border="0">导出WORD</span>&nbsp;&nbsp;&nbsp;
															<span style="CURSOR: hand" onclick="exportReport('xls')"><img
																	name="selDay1" alt='导出EXCEL'
																	src="<%=rootPath%>/resource/image/export_excel.gif"
																	width=18 border="0">导出EXCEL</span>&nbsp;&nbsp;&nbsp;
															<span style="CURSOR: hand" onclick="exportReport('pdf')"><img
																	name="selDay1" alt='导出PDF'
																	src="<%=rootPath%>/resource/image/export_pdf.gif"
																	width=18 border="0">导出PDF</span>&nbsp;
															<hr>
														</td>

													</tr>
													
													<tr>
														<td>
															<table class="detail-data-body" id="tab">
																

																<%
																	Iterator it= rateMap.keySet().iterator();
																	int i=0;
                                                                while (it.hasNext())
                                                             {
															   Object key=it.next();
															  
															   String data=rateMap.get(key);
															 i++;
															 String classStyle="";
                                                               if(i%2==0){
                                                               classStyle="bgcolor='#f5fafe'";
                                                               }
																%>
																<tr >
																    <td <%=classStyle %> style="FONT-SIZE: 11pt; LINE-HEIGHT: 16pt;" >&nbsp;&nbsp;<span id="span<%=i %>" onclick="toggles('<%=i %>')" style="CURSOR: hand">&nbsp;&nbsp;+&nbsp;&nbsp;</span>策略名称：<%=key %></td>
																</tr>
																<tr id='acl<%=i %>' style='display:none;'>


																	<td width=94%>
																		<table cellpadding="0" cellspacing="0">
																			<tr>
																				<td width="100%" align=center>
																					<div id="net<%=i %>">
																					</div>

																					<script type="text/javascript">
                                                                   var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","825", "408", "8", "#FFFFFF");
		                                                            so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                            so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/acl.xml"));
			                                                        so.addVariable("chart_data","<%=data%>");
				                                                    so.write("net<%=i%>");
                                                                 </script>
																				</td>
																			</tr>
																		</table>
																	</td>

																</tr>
																<%
																	 }
																//}
																%>
																
															</table>
														</td>
													</tr>
												</table>

											</td>
										</tr>
									</table>
								</td>
								<td class="td-container-main-tool" width="15%">
									<jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="network" name="category" />
										<jsp:param value="<%=nodedto.getSubtype()%>" name="subtype" />
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
