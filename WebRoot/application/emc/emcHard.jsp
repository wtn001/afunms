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
<%@ page import="com.afunms.polling.loader.HostLoader"%>
<%@ page import="com.afunms.common.util.CEIString"%>
<%@ page import="com.afunms.emc.model.*"%>
<%@ page import="com.afunms.emc.parser.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>

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


<%
	String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	String menuTable = (String) request.getAttribute("menuTable");//�˵�
	String tmp = request.getParameter("id");
	String flag1 = request.getParameter("flag");
	//HONGLI ADD START �ؼ��˿ڵ�չʾ
	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	String showAllPortFlag = request.getParameter("showAllPortFlag");//��ʾ���ж˿�  0:��ʾ���ж˿�   1����ʾ�ؼ��˿�
	List<String> portIndexList = new ArrayList<String>();//�ؼ��˿ڵĶ˿������б�
	//System.out.println("showAllPortFlag------------------"+showAllPortFlag);

	List<Crus> hard = (List) request
			.getAttribute("list");
	if (showAllPortFlag == null) {
		showAllPortFlag = "0";
	}
	if ("1".equals(showAllPortFlag)) {
		//�õ����йؼ��˿�
		List list = new ArrayList();
		PortconfigDao dao = new PortconfigDao();
		String ipaddress = host.getIpAddress();
		try {
			list = dao.loadByIpaddress(ipaddress);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dao.close();
		}
		//�õ��ؼ��˿ڵĶ˿������б�
		com.afunms.config.model.Portconfig vo = null;
		for (int i = 0; i < list.size(); i++) {
			vo = (com.afunms.config.model.Portconfig) list.get(i);
			if (vo == null) {
				continue;
			}
			if (vo.getSms() == 1) {//����״̬�Ķ˿�
				portIndexList.add(String.valueOf(vo.getPortindex()));
			}
		}
	}
	//HONGLI ADD END
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	double cpuvalue = 0;
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String syslocation = "";

	//�ڴ������ʺ���Ӧʱ�� begin 
	Vector memoryVector = new Vector();
	String memoryvalue = "0";
	//end

	//ping����Ӧʱ��begin
	String avgresponse = "0";
	String maxresponse = "0";
	String responsevalue = "0";
	String pingconavg = "0";
	String maxpingvalue = "0";
	String pingvalue = "0";
	//end

	//ʱ������begin
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

	String curin = "0";
	String curout = "0";

	String mypage = "liusu";
	Vector vector = new Vector();

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
	String orderflag = request.getParameter("orderflag");
	if (orderflag == null || orderflag.trim().length() == 0)
		orderflag = "index";
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
			//2.�ڴ��ȡ
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
					if (systemdata.getSubentity().equalsIgnoreCase(
							"sysLocation")) {
						syslocation = systemdata.getThevalue();
					}
				}
			}
		}
		Vector pingData = (Vector) ShareData.getPingdata().get(
				host.getIpAddress());
		if (pingData != null && pingData.size() > 0) {
			Pingcollectdata pingdata = (Pingcollectdata) pingData
					.get(0);
			Calendar tempCal = (Calendar) pingdata.getCollecttime();
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
		}
		//----pingֵ����Ӧʱ��begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host
					.getIpAddress(), "Pings", "ConnectUtilization",
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
					.getIpAddress(), "Pings", "ResponseTime",
					starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash
						.get("avgpingcon");
				avgresponse = avgresponse.replace("����", "").replaceAll(
						"%", "");
				maxresponse = (String) ConnectUtilizationhash
						.get("pingmax");
				maxresponse = maxresponse.replaceAll("%", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end

		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		//   String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","ifOutBroadcastPkts","ifInBroadcastPkts","ifOutMulticastPkts","ifInMulticastPkts","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "OutBandwidthUtilHdxPerc",
				"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdx",
				"InBandwidthUtilHdx" };
		try {
			vector = hostlastmanager.getInterface_share(host
					.getIpAddress(), netInterfaceItem, orderflag,
					starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} else {
		//�ɼ�������Ƿ���ģʽ
		List systemList = new ArrayList();
		List pingList = new ArrayList();
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
		I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();
		//   String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","ifOutBroadcastPkts","ifInBroadcastPkts","ifOutMulticastPkts","ifInMulticastPkts","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
		String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
				"ifOperStatus", "OutBandwidthUtilHdxPerc",
				"InBandwidthUtilHdxPerc", "OutBandwidthUtilHdx",
				"InBandwidthUtilHdx" };
		try {
			vector = hostlastmanager.getInterface(host.getIpAddress(),
					netInterfaceItem, orderflag, starttime, endtime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//----pingֵ����Ӧʱ��begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host
					.getIpAddress(), "Pings", "ConnectUtilization",
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
					.getIpAddress(), "Pings", "ResponseTime",
					starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash
						.get("avgpingcon");
				avgresponse = avgresponse.replace("����", "").replaceAll(
						"%", "");
				maxresponse = (String) ConnectUtilizationhash
						.get("pingmax");
				maxresponse = maxresponse.replaceAll("%", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end
	}

	PortconfigDao portdao = new PortconfigDao();
	Hashtable hash = new Hashtable();
	try {
		hash = portdao.getIpsHash(host.getIpAddress());
	} catch (Exception e) {
	} finally {
		portdao.close();
	}
	if (hash == null)
		new Hashtable();

	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();

	String rootPath = request.getContextPath();

	Vector ifvector = vector;

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
	//CommonUtil commutil = new CommonUtil();
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

	//���豸�ɼ������ݣ��Ƿ�ֻ��ping���ݱ�־
	boolean isOnlyPing = NodeUtil.isOnlyCollectPing(host);
	//    System.out.println(picip+"---------------����CPU�Ǳ���----------------"+cpuper);

	//����CPU�Ǳ���
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper);
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

<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="/afunms/detail/jquery-1.7.min.js"></script>
<script type="text/javascript" src="/afunms/detail/test.js"></script>
<link href="/afunms/detail/intstyle1.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.limitWidth{ width:180px; margin:0 auto;}
.sideNavWrap{border:1px solid #c9e1f4; border-bottom:0;}
.limitWidth .style .desc{margin-left:0!important;}
.limitWidth .style.sideNav .icon{float:left; margin-left:10px;}



.out1{display:block; border-top:0px; margin:0% 20% 0% 30%; position:absolute; }
.b_shadow1{
	display:block; 
    position:absolute;
    z-index:-1;
    left:30%;
    top:10px;
    width:300px; /* �������ݿ�ȣ�*/

    padding-top:20px;
    padding-bottom:20px;
    border:1px solid #C0C0C0;background-color:#FFF; 
    -webkit-box-shadow: 1px 1px px rgba(0, 0, 0, 0.5); 
    -moz-box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5); 
    box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.5); 
    /*IE6,IE7�﷨*/
    filter:progid:DXImageTransform.Microsoft.dropshadow(OffX=1, OffY=1, Color='gray'); 
    /*IE8�﷨*/
    -ms-filter:"progid:DXImageTransform.Microsoft.dropshadow(OffX=1, OffY=1, Color='gray')"
} 
.title1{
	width:60px; /*���ñ�����*/
	height:30px;
	 left:30%;
	border:0px;
	display:block;
	background:#FFF; /*���ñ���ɫ*/
	text-align:center;
}



</style>
<script>

/*
*��ʾ�ؼ��˿�
*/
function showPort(){
	var showAllPortFlag = document.getElementById("allportflag").value;
	mainForm.action = "<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&flag=<%=flag%>&showAllPortFlag="+showAllPortFlag;
    mainForm.submit();
}

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


  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
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
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&flag=<%=flag1%>&orderflag="+para+"&showAllPortFlag=<%=showAllPortFlag%>";
} 
function openwin3(operate,index,ifname)
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
} 

//function modifyIpAlias(ipalias){
	//window.alert(ipalias);
	//document.getElementById('ipalias').innerHTML = ipalias;
	//var ipaddress = document.getElementById('ipaddress');
	//�첽���ú�̨���� DWR
//	IpAliasRemoteManager.modifyIpAlias(ipaddress,ipalias);
//}

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
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
	//	alter(ip+"======================"+id);
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"11111111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	   
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	      
	           rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    //location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
       	    // alert("<%=tmp%>"+"****"+"<%=host.getIpAddress()%>"+"****"+node+"******"+ipaddress);
	    window.open ("<%=rootPath%>/monitor.do?action=show_utilhdx&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+node+"&ifname="+ipaddress, "newwindow", "height=450, width=840, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
	}
	function portset()
	{
		window.open ("<%=rootPath%>/panel.do?action=show_portreset&ipaddress=<%=host.getIpAddress()%>&ifindex="+node, "newwindow", "height=250, width=700, toolbar= no, menubar=no, scrollbars=yes, resizable=no, location=yes, status=yes");
		//window.open("<%=rootPath%>/tool/tracerouter.jsp?ipaddress="+ipaddress,"newtracerouter", "height=400, width= 500, top=300, left=100");
		//window.open('/nms/netutil/tracerouter.jsp?ipaddress='+ipaddress);
	}
	function realtimeMonitor()
	{
		window.open ('<%=rootPath%>/monitor.do?action=portdetail&id=<%=tmp%>&ip=<%=host.getIpAddress()%>&ifindex='+node,'�˿�����','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function bandwidth()
	{
		window.open ('<%=rootPath%>/monitor.do?action=bandwidthdetail&id=<%=tmp%>&ip=<%=host.getIpAddress()%>&ifindex='+node,'����','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function dataPacket()
	
	{
		window.open ('<%=rootPath%>/monitor.do?action=datapacket&id=<%=tmp%>&ip=<%=host.getIpAddress()%>&ifindex='+node,'�㲥���ݰ�','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function multicastPacket()
	
	{
		window.open ('<%=rootPath%>/monitor.do?action=multicastpacket&id=<%=tmp%>&ip=<%=host.getIpAddress()%>&ifindex='+node,'�ಥ���ݰ�','top=200,left=300,height=480,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}
	function portstatus()
	{
		window.open ('<%=rootPath%>/portconfig.do?action=showPortStatus&index='+node+'&ip=<%=host.getIpAddress()%>','�˿�״̬','top=200,left=300,height=435,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
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
	document.getElementById('storageDetailTitle-2').className='detail-data-title';
	document.getElementById('storageDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('storageDetailTitle-2').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

function Hard(flag){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/performancePanelAjaxManager.ajax?action=emcHard&ip=<%=host.getIpAddress()%>&flag="+flag+"&nodeid=<%=tmp%>",
			success:function(data){
			      document.getElementById("div").innerHTML =data.option;
			}
		});
}



function arrayenvwt(){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvarraywt&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }
function arrayenvavgwt(){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvarrayavgwt&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }
function envtmp(flag){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvmemtmp&flag="+flag+"&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }
function envavgtmp(flag){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvmemavgtmp&flag="+flag+"&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }
function memenvwt(flag){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvmemwt&flag="+flag+"&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }
function memenvavgwt(flag){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvmemavgwt&flag="+flag+"&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }
function bakenvwt(flag){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvbakwt&flag="+flag+"&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }
function bakenvavgwt(flag){  
               window.open("<%=rootPath%>/netreport.do?action=emcenvbakavgwt&flag="+flag+"&ip=<%=host.getIpAddress()%>",'mywindow','width=,height=,resizable=1');
       }



</script>  
</head>
<body id="body" class="body" onload="initmenu();">
<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">�鿴״̬</td>
		</tr>
			
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.portset();">�˿�����</td>
		</tr>	
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.realtimeMonitor();">����ʵʱ</td>
		</tr>
			
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.bandwidth();">����ʵʱ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.dataPacket();">�㲥��ʵʱ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.multicastPacket();">�ಥ��ʵʱ</td>				
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.portstatus();">�˿�״̬</td>
		</tr>
	</table>
	</div>
<!-- �Ҽ��˵�����-->
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
					<table id="container-main" class="container-main" >
						<tr>
							<td class="td-container-main-detail"  width=98%> 
								<table id="container-main-detail" class="container-main-detail" >
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
										<!--  	<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>�޸�</a>]</td>-->
										        <span id="lable"><font color=black><%=host.getAlias()%></font></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;<font color=black>ϵͳ����:</font>
											<span id="sysname"><font color=black>EMC_VNX</font></span></td>
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;<font color=black>IP��ַ:<%=host.getIpAddress()%></font>
											
											
												
												
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
											    		<%=emc%>
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
	                                                                                     <%
								                                      Crus vo1 = null;
								                                      if(hard != null && hard.size()>0){
								                                        for(int i=0;i<hard.size();i++){
								                                          vo1 = (Crus)hard.get(i);
								                                     %>
	                                                                                     <li ><a href="#" onclick="Hard('<%=vo1.getName() %>');"><%=vo1.getName().substring(0,vo1.getName().indexOf("B")-1) %></a>
		                                                                                 </li>
		                                                                                <%}} %>
	                                                                               </ul>
	                                                                          </div>
												                 </td>
												            </tr> 												            
												           <!-- END -->
												         </table>
												       </td>
												       
												       <td align=left width=700 valign=top >
												         <div id = "div" >
												            <table id="detail-content-body" class="">
												             <tr>
												              <td>
												                <div id='wuhost'>
												                <table cellspacing="0" cellpadding="0" width="width:100%" bgcolor="#FFFFFF" border=0  class="detail-data-body">
								                                    <%
								                                      Crus vo = null;
								                                      Crus voo = null;
								                                      if(hard != null && hard.size()>0){
								                                        for(int i=0;i<hard.size();i++){
								                                             vo = hard.get(i);
								                                             //System.out.println(vo.getSpStateStr().equals("null"));
								                                             if(!vo.getSpStateStr().equals("null") || vo.getSpStateStr() != null){voo = vo;break;}
								                                          }
								                                      }
								                                      if(voo!= null){
								                                     %>
								                                    <tr >						
																			<td align="right" height="29" width="20%">����:&nbsp;</td>				
																			<td width="80%" colspan=3>&nbsp;
																				<%=voo.getName() %>
																			</td>
																	</tr>
																	<tr  style="background-color: #ECECEC;">						
																			<td align="right" height="29" width="20%">�洢��(SP)״̬:&nbsp;</td>				
																			<td width="80%" colspan=3>&nbsp;
																				<%//if(voo.getSpStateStr() != null){out.print(voo.getSpStateStr().trim().replace("{","").replace("}","").replace("=","->"));} %>
																			     <table >
																			       <% 
																			          if(voo.getSpStateStr() != null){
																			              String[] cabling = voo.getSpStateStr().replace("{","").replace("}","").split(",");
																			              if(cabling != null && cabling.length>0){
																			                for(int i =0;i<cabling.length;i++){ 
																			       %>
																			        <tr>
																			           <td width="100%" >&nbsp;
																			              <%=cabling[i] %>
																			          </td>
																			        </tr>
																			        <% 
																			          }
																			         }
																			        }
																			        %>
																			    </table>
																			</td>
																	</tr>
																	<tr >						
																			<td align="right" height="29" width="20%">��Դ״̬:&nbsp;</td>				
																			<td width="80%" colspan=3>&nbsp;
																				<%//if(voo.getPowerState() != null){out.print(voo.getPowerState().trim().replace("{","").replace("}","").replace("=","->"));} %>
																			     <table >
																			       <% 
																			          if(voo.getPowerState() != null){
																			              String[] cabling = voo.getPowerState().replace("{","").replace("}","").split(",");
																			              if(cabling != null && cabling.length>0){
																			                for(int i =0;i<cabling.length;i++){ 
																			       %>
																			        <tr>
																			           <td width="100%" >&nbsp;
																			              <%=cabling[i] %>
																			          </td>
																			        </tr>
																			        <% 
																			          }
																			         }
																			        }
																			        %>
																			    </table>
																			</td>
																	</tr>
																	<tr style="background-color: #ECECEC;">						
																			<td align="right" height="29" width="20%">���õ�Դ(SPS)״̬:&nbsp;</td>				
																			<td width="80%" colspan=3>&nbsp;
																				<%//if(voo.getSpStateStr() != null){out.print(voo.getSpStateStr().trim().replace("{","").replace("}","").replace("=","->"));} %>
																			     <table >
																			       <% 
																			          if(voo.getSpStateStr() != null){
																			              String[] cabling = voo.getSpStateStr().replace("{","").replace("}","").split(",");
																			              if(cabling != null && cabling.length>0){
																			                for(int i =0;i<cabling.length;i++){ 
																			       %>
																			        <tr>
																			           <td width="100%" >&nbsp;
																			              <%=cabling[i] %>
																			          </td>
																			        </tr>
																			        <% 
																			          }
																			         }
																			        }
																			        %>
																			    </table>
																			</td>
																	</tr>
																	<tr >						
																			<td align="right" height="29" width="15%">��������(Cabling)״̬:&nbsp;</td>				
																			<td width="85%" colspan=3>&nbsp;
																				<%//if(voo.getBusCabling() != null){out.print(voo.getBusCabling().trim().replace("{","").replace("}","").replace("=",":"));} %>
																			    <table>
																			       <% 
																			          if(voo.getBusCabling() != null){
																			              String[] cabling = voo.getBusCabling().replace("{","").replace("}","").split(",");
																			              if(cabling != null && cabling.length>0){
																			                for(int i =0;i<cabling.length;i++){ 
																			       %>
																			        <tr>
																			           <td width="100%" >&nbsp;
																			              <%=cabling[i] %>
																			          </td>
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
								                                   }
								                                   %>
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
									</table>
									
							</td>
							<td class="td-container-main-tool" width='15%' >
								<jsp:include page="/include/toolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="network" name="category"/>
									<jsp:param value="<%=nodedto.getSubtype()%>" name="subtype"/>
									<jsp:param value="<%=mypage%>" name="page"/>
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
//Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
//mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=detailPage_bkpCfg&ipaddress=<%=host.getIpAddress()%>&page=liusu&id="+<%=tmp%>;
//mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=detailPage_readybkpCfg&ipaddress=<%=host.getIpAddress()%>&page=liusu&id="+<%=tmp%>;
//mainForm.submit();
window.open("<%=rootPath%>/vpntelnetconf.do?action=detailPage_readybkpCfg&ipaddress=<%=host.getIpAddress()%>&page=liusu&id="+<%=tmp%>,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
});
});
</script>
</BODY>
</HTML>