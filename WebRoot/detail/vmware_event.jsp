<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
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
<%@ page import="com.afunms.event.model.EventList"%>

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

<%@ page import="com.afunms.common.util.SystemConstant"%>

 
<%
	String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	String menuTable = (String) request.getAttribute("menuTable");//�˵�
	String tmp = request.getParameter("id"); 
	String flag1 = request.getParameter("flag");  
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	double cpuvalue = 0; 
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String syslocation = ""; 
	
	//�ڴ������ʺ���Ӧʱ�� begin 
	Vector memoryVector = new Vector(); 
	String memoryvalue="0";
	//end
	
    //ping����Ӧʱ��begin
    String avgresponse = "0";
	String maxresponse = "0";
	String responsevalue = "0"; 
	String pingconavg ="0";
 	String maxpingvalue = "0";
	String pingvalue = "0";
    //end
    
    //ʱ������begin
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
	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	if (host == null) {
		//�����ݿ����ȡ
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

	//���IPMAC��Ϣ
	Vector ipmacvector = new Vector();

	if ("0".equals(runmodel)) {
		//�ɼ�������Ǽ���ģʽ
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData != null) {
			Vector cpuV = (Vector) ipAllData.get("cpu");
			if (cpuV != null && cpuV.size() > 0) {

				CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
				cpuvalue = new Double(cpu.getThevalue());
			}
			
			memoryVector = (Vector) ipAllData.get("memory");
			
			//�õ�ϵͳ����ʱ��
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
			//�õ�IPMAC��Ϣ
			ipmacvector = (Vector) ipAllData.get("ipmac");
			if (ipmacvector == null)
				ipmacvector = new Vector();
		}

		//----pingֵ����Ӧʱ��begin
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
				avgresponse = avgresponse.replace("����", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end
		
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
		//�ɼ�������Ƿ���ģʽ
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		List arpList = new ArrayList();
		
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
			memoryList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype())
					.getCurrMemoryInfo();
			//arpList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getARPInfo();
		} catch (Exception e) {
		}
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
		//----pingֵ����Ӧʱ��begin
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
				avgresponse = avgresponse.replace("����", "").replaceAll("%","");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse=maxresponse.replaceAll("%","");		
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end
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

	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");

    int level1 =0;
    int _status=0;
	//int level1 = Integer.parseInt(request.getAttribute("level1") + "");
	//int _status = Integer.parseInt(request.getAttribute("status") + "");

	String level0str = "";
	String level1str = "";
	String level2str = "";
	String level3str = "";
	if (level1 == 0) {
		level0str = "selected";
	} else if (level1 == 1) {
		level1str = "selected";
	} else if (level1 == 2) {
		level2str = "selected";
	} else if (level1 == 3) {
		level3str = "selected";
	}

	String status0str = "";
	String status1str = "";
	String status2str = "";
	if (_status == 0) {
		status0str = "selected";
	} else if (_status == 1) {
		status1str = "selected";
	} else if (_status == 2) {
		status2str = "selected";
	}
	int allmemoryvalue = 0;
	if (memoryVector != null && memoryVector.size() > 0) {
		for (int i = 0; i < memoryVector.size(); i++) {
			Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
			allmemoryvalue = allmemoryvalue + Integer.parseInt(memorycollectdata.getThevalue());
		}
		memoryvalue = (allmemoryvalue/memoryVector.size())+"";
	}
	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	int alarmlevel = SystemSnap.getNodeStatus(host);
%> 
<html>
	<head>


		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
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
				 $("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "ƽ����ͨ��" });
				$("#cpu").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png?nowtime="+(new Date()), alt: "��ǰCPU������" }); 
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
function accEvent(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/monitor.do?action=accit";	
	mainForm.submit();
}



	function batchAccfiEvent(){
		 var eventids = ''; 
		 for( var i=0; i < mainForm.checkbox.length; i++ ){
           if(mainForm.checkbox[i].checked){
           		eventids = eventids + mainForm.checkbox[i].value;
           		eventids = eventids + ",";
           }
        }
        //��eventids�ŵ�attribute��
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_accitevent.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	//batchDoReport();
	function batchDoReport(){
		 var eventids = ''; 
		 for( var i=0; i < mainForm.checkbox.length; i++ ){
           if(mainForm.checkbox[i].checked){
           		eventids = eventids + mainForm.checkbox[i].value;
           		eventids = eventids + ",";
           }
        }
        //��eventids�ŵ�attribute��
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_doreport.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	function batchEditAlarmLevel(){
		 var eventids = ''; 
		 for( var i=0; i < mainForm.checkbox.length; i++ ){
           if(mainForm.checkbox[i].checked){
           		eventids = eventids + mainForm.checkbox[i].value;
           		eventids = eventids + ",";
           }
        }
        //��eventids�ŵ�attribute��
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_editAlarmLevel.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	

function fiReport(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/monitor.do?action=fireport";	
	mainForm.submit();	;
}
function viewReport(eventid){
	mainForm.eventid.value=eventid;
	mainForm.action="<%=rootPath%>/monitor.do?action=viewreport";	
	mainForm.submit();
}  
  function query()
  {  
     mainForm.action = "<%=rootPath%>/vmware.do?action=queryevent";
     mainForm.submit();
  }
  function opennewwin(){
    
     window.open("<%=rootPath%>/monitor.do?action=hosteventlist&nodetype=net&id=<%=tmp%>");
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
    function showDelete()
  {  
    	var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
     mainForm.action = "<%=rootPath%>/vmware.do?action=vmeventdelete";
     mainForm.submit();
  } 
  
// ȫ���ۿ�
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
//�޸Ĳ˵������¼�ͷ����
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

//��Ӳ˵�	
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
	document.getElementById('vmwareDetailTitle-4').className='detail-data-title';
	document.getElementById('vmwareDetailTitle-4').onmouseover="this.className='detail-data-title'";
	document.getElementById('vmwareDetailTitle-4').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}


//�����豸��ip��ַ
function modifyIpAliasajax(ipaddress){
	var t = document.getElementById('ipalias'+ipaddress);
	var ipalias = t.options[t.selectedIndex].text;//��ȡ�������ֵ
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyIpAlias&ipaddress="+ipaddress+"&ipalias="+ipalias,
			success:function(data){
				window.alert("�޸ĳɹ���");
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


	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin; font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.detail()">
						�鿴״̬
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.portset();">
						�˿�����
					</td>
				</tr>
			</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="alermlevel" name="alermlevel">
			<input type=hidden name="orderflag">
			<input type=hidden name="id" value="<%=tmp%>">
			<input type=hidden name="flag" value="<%=flag1%>">

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
								<td class="td-container-main-detail" width=98%>
									<table id="container-main-detail" class="container-main-detail">
										<tr>
											<td>
												<table id="detail-content" class="detail-content" width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					<td class="layout_title"><b>�豸��ϸ��Ϣ</b></td>
					<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
				</tr>
				<tr>
				<td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;<font color=black>�豸��ǩ:</font>
								 <span id="lable"><font color=black><%=host.getAlias()%></font></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;<font color=black>ϵͳ����:</font>
											<span id="sysname"><font color=black>VMWare</font></span> </td>
						<td width="20%" height="26" background="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>" class="performance-status">
							&nbsp;<%=NodeHelper.getStatusDescr(alarmlevel)%>
						 </td>					
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;<font color=black>IP��ַ:<%=host.getIpAddress()%></font>
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
																	<td  height="28" bgcolor="#ECECEC">


																		��ʼ����
																		<input type="text" name="startdate"
																			value="<%=startdate%>" size="10">
																		<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																			<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a> ��ֹ����
																		<input type="text" name="todate" value="<%=todate%>"
																			size="10" />
																		<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																			<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a> �¼��ȼ�
																		<select name="level1">
																			<option value="99">
																				����
																			</option>
																			<option value="1" <%=level1str%>>
																				��ͨ�¼�
																			</option>
																			<option value="2" <%=level2str%>>
																				�����¼�
																			</option>
																			<option value="3" <%=level3str%>>
																				�����¼�
																			</option>
																		</select>

																		����״̬
																		<select name="status">
																			<option value="99">
																				����
																			</option>
																			<option value="0" <%=status0str%>>
																				δ����
																			</option>
																			<option value="1" <%=status1str%>>
																				���ڴ���
																			</option>
																			<option value="2" <%=status2str%>>
																				�Ѵ���
																			</option>
																		</select>
																		<input type="button" name="submitss" value="��ѯ"
																			onclick="query()">
																		<!-- <input type="button" name="pop" value="�澯�´���" onclick="opennewwin()"> -->
																		<hr />
																	</td>

																</tr>
																<tr align="right" bgcolor="#ECECEC">
																	<td><table><tr>
																	<td width="75%">&nbsp;</td>
																	<td width="15" height=15 >&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="���ܴ���" onclick='batchAccfiEvent();'/></td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="��д����" onclick='batchDoReport();'/></td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="submitss" value="�޸ĵȼ�" onclick="batchEditAlarmLevel();"></td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																		<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="ɾ������" onclick='showDelete();'/>&nbsp;&nbsp;</td>
																<td width="15" height=15>&nbsp;&nbsp;</td>
																	</tr></table></td>
																</tr>


																<tr bgcolor="#ECECEC">
																	<td>
																		<table cellSpacing="0" cellPadding="0" border=0>

																			<tr>
																				<td class="detail-data-body-title">
																					<INPUT type="checkbox" name="checkall"
																						onclick="javascript:chkall()">
																					���
																				</td>
																				<td width="10%" class="detail-data-body-title">
																					<strong>�¼��ȼ�</strong>
																				</td>
																				<td width="40%" class="detail-data-body-title">
																					<strong>�¼�����</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong>����澯ʱ��</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong>�澯����</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong>�鿴״̬</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong></strong>
																				</td>
																			</tr>
																			<%
																				int index = 0;
																				java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
																				List list = (List) request.getAttribute("list");
																				if (list != null) {
																					for (int i = 0; i < list.size(); i++) {
																						index++;
																						EventList eventlist = (EventList) list.get(i);
																						Date cc = eventlist.getRecordtime().getTime();
																						Integer eventid = eventlist.getId();
																						String eventlocation = eventlist.getEventlocation();
																						String content = eventlist.getContent();
																						String level = String.valueOf(eventlist.getLevel1());
																						String status = String.valueOf(eventlist.getManagesign());
																						String s = status;
																						String act = "������";
																						String levelstr = "";
																						String bgcolor = "";
																						if ("0".equals(level)) {
																							level = "��ʾ��Ϣ";
																							bgcolor = "bgcolor='blue'";
																						}
																						if ("1".equals(level)) {
																							level = "��ͨ�澯";
																							bgcolor = "bgcolor='yellow'";
																						}
																						if ("2".equals(level)) {
																							level = "���ظ澯";
																							bgcolor = "bgcolor='orange'";
																						}
																						if ("3".equals(level)) {
																							level = "�����澯";
																							bgcolor = "bgcolor='red'";
																						}
																						String bgcolorstr = "";
																						if ("0".equals(status)) {
																							status = "δ����";
																							bgcolorstr = "#9966FF";
																						}
																						if ("1".equals(status)) {
																							status = "������";
																							bgcolorstr = "#3399CC";
																						}
																						if ("2".equals(status)) {
																							status = "�������";
																							bgcolorstr = "#33CC33";
																						}
																						String rptman = eventlist.getReportman();
																						String rtime1 = _sdf.format(cc);
																			%>

																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																				<td class="detail-data-body-list">
																					<INPUT type="checkbox" name="checkbox"
																						value="<%=eventlist.getId()%>"><%=i + 1%></td>
																				<td class="detail-data-body-list" <%=bgcolor%>><%=level%></td>
																				<td class="detail-data-body-list">
																					<%=content%></td>
																				<td class="detail-data-body-list">
																					<%=rtime1%></td>
																				<td class="detail-data-body-list">
																					<%=rptman%></td>
																				<td class="detail-data-body-list"
																					bgcolor=<%=bgcolorstr%>>
																					<%=status%></td>
																				<td class="detail-data-body-list" align="center">
																					<%
																						if ("0".equals(s) && "��ʾ��Ϣ".equals(level)) {

																								} else if ("0".equals(s)) {
																					%>
																					<input type="button" value="���ܴ���" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>

																					<%
																						}
																								if ("1".equals(s)) {
																					%>
																					<input type="button" value="��д����" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>

																					<%
																						}
																								if ("2".equals(s)) {
																					%>
																					<input type="button" value="�鿴����" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>

																					<%
																						}
																					%>
																				</td>
																			</tr>
																			<%
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

											</td>
										</tr>
									</table>
						</table>

					</td>
					<td class="td-container-main-tool" width='15%'>
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
		</form>
	</BODY>
</HTML>