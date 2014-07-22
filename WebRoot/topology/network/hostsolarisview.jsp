<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.polling.manage.PollMonitorManager"%>

<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

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
<%@ page import="com.afunms.config.model.Nodeconfig"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page
	import="com.afunms.detail.service.interfaceInfo.InterfaceInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%
	String runmodel = PollingEngine.getCollectwebflag();
	String menuTable = (String) request.getAttribute("menuTable");
	String tmp = request.getParameter("id");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg = "0";
	String maxpingvalue = "0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";

	String hostname = "";
	String porcessors = "";
	String sysname = "";
	String SerialNumber = "";
	String CSDVersion = "";
	String mac = "";
	String pingvalue = "0";
	String responsevalue = "0";
	String avgresponse = "0";
	String maxresponse = "0";
	HostNodeDao hostdao = new HostNodeDao();
	List hostlist = hostdao.loadHost();

	Host host = (Host) PollingEngine.getInstance().getNodeByID(
			Integer.parseInt(tmp));
	String ipaddress = host.getIpAddress();
	String orderflag = request.getParameter("orderflag");
	if (orderflag == null || orderflag.trim().length() == 0)
		orderflag = "index";
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

	I_HostLastCollectData hostlastmanager = new HostLastCollectDataManager();

	Nodeconfig nodeconfig = new Nodeconfig();
	Hashtable Disk = (Hashtable) request.getAttribute("Disk");
	// Hashtable Disk = new Hashtable();
	Vector vector = new Vector();
	Vector cpuV = new Vector();
	Vector systemV = new Vector();
	Vector pingData = new Vector();
	String[] netInterfaceItem = { "index", "ifDescr", "ifSpeed",
			"ifOperStatus", "OutBandwidthUtilHdx", "InBandwidthUtilHdx" };
	Vector deviceV = new Vector();
	Hashtable memhash = (Hashtable) request.getAttribute("memhash");
	Hashtable memmaxhash = (Hashtable) request
			.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request
			.getAttribute("memavghash");
	try {
		if ("0".equals(runmodel)) {
			//�ɼ�������Ǽ���ģʽ
			vector = hostlastmanager.getInterface_share(host
					.getIpAddress(), netInterfaceItem, orderflag,
					starttime, endtime);
			Hashtable ipAllData = (Hashtable) ShareData.getSharedata()
					.get(host.getIpAddress());
			if (ipAllData != null) {
				nodeconfig = (Nodeconfig) ipAllData.get("nodeconfig");
				//cpu
				cpuV = (Vector) ipAllData.get("cpu");
				//system
				systemV = (Vector) ipAllData.get("system");
				deviceV = (Vector) ipAllData.get("device");
				//�ļ�ϵͳ
			}

			Disk = hostlastmanager.getDisk_share(host.getIpAddress(),
					"Disk", "", "");
			pingData = (Vector) ShareData.getPingdata().get(
					host.getIpAddress());
			if (pingData != null && pingData.size() > 0) {
				Pingcollectdata pingdata = (Pingcollectdata) pingData
						.get(0);
				Calendar tempCal = (Calendar) pingdata.getCollecttime();
				Date cc = tempCal.getTime();
				collecttime = sdf1.format(cc);
				pingvalue = pingdata.getThevalue();//��ǰ��ͨ��
				pingdata = (Pingcollectdata) pingData.get(1);
				responsevalue = pingdata.getThevalue();//��ǰ��Ӧʱ��

			}
		} else {
			//�ɼ�������Ƿ���ģʽ
			NodeUtil nodeUtil = new NodeUtil();
			NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
			NodeconfigDao nodeconfigDao = new NodeconfigDao();
			try {
				nodeconfig = nodeconfigDao.getByNodeID(nodedto.getId()
						+ "");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				nodeconfigDao.close();
			}
			//cpu
			CpuInfoService cpuInfoService = new CpuInfoService(nodedto
					.getId()
					+ "", nodedto.getType(), nodedto.getSubtype());
			cpuV = cpuInfoService.getCpuInfo();
			//�õ�ϵͳ����ʱ��
			SystemInfoService systemInfoService = new SystemInfoService(
					nodedto.getId() + "", nodedto.getType(), nodedto
							.getSubtype());
			systemV = systemInfoService.getSystemInfo();
			//�ļ�ϵͳ
			DiskInfoService diskInfoService = new DiskInfoService(
					nodedto.getId() + "", nodedto.getType(), nodedto
							.getSubtype());
			Disk = diskInfoService.getCurrDiskListInfo();
			//ping
			PingInfoService pingInfoService = new PingInfoService(
					nodedto.getId() + "", nodedto.getType(), nodedto
							.getSubtype());
			pingData = pingInfoService.getPingInfo();
			if (pingData != null && pingData.size() > 0) {
				Pingcollectdata pingdata = (Pingcollectdata) pingData
						.get(0);
				Calendar tempCal = (Calendar) pingdata.getCollecttime();
				Date cc = tempCal.getTime();
				pingvalue = pingdata.getThevalue();//��ǰ��ͨ��
				pingdata = (Pingcollectdata) pingData.get(1);
				responsevalue = pingdata.getThevalue();//��ǰ��Ӧʱ��
			}
			List deviceList = new ArrayList();
			deviceList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getDeviceInfo();
			if (deviceList != null && deviceList.size() > 0) {
				Devicecollectdata devicedata = null;
				for (int i = 0; i < deviceList.size(); i++) {
					DeviceNodeTemp nodetemp = (DeviceNodeTemp) deviceList
							.get(i);
					devicedata = new Devicecollectdata();
					devicedata.setIpaddress(host.getIpAddress());
					devicedata.setName(nodetemp.getName());
					devicedata.setStatus(nodetemp.getStatus());
					devicedata.setType(nodetemp.getDtype());
					deviceV.addElement(devicedata);
				}
			}
			try {
				memhash = hostlastmanager.getMemory(
						host.getIpAddress(), "Memory", starttime,
						endtime);
				Disk = hostlastmanager.getDisk(host.getIpAddress(),
						"Disk", starttime, endtime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	String newip = SysUtil.doip(host.getIpAddress());
	//��Ӳ��ͼ

	new PollMonitorManager().draw_column(Disk, "", newip + "disk", 750,
			150);

	Hashtable hostinfohash = new Hashtable();
	List networkconfiglist = new ArrayList();
	List cpulist = new ArrayList();

	String processornum = "";

	//if(ipAllData != null){
	//nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
	if (nodeconfig != null) {
		mac = nodeconfig.getMac();
		processornum = nodeconfig.getNumberOfProcessors();
		CSDVersion = nodeconfig.getCSDVersion();
		hostname = nodeconfig.getHostname();
	}
	//hostinfohash = (Hashtable)ipAllData.get("hostinfo");
	//networkconfiglist = (List)ipAllData.get("networkconfig");
	//cpulist = (List)hostinfohash.get("CPUname");
	//hostname = (String)hostinfohash.get("Hostname");
	//porcessors = (String)hostinfohash.get("NumberOfProcessors");
	//sysname = (String)hostinfohash.get("Sysname");
	//SerialNumber = (String)hostinfohash.get("SerialNumber");
	//CSDVersion = (String)hostinfohash.get("CSDVersion");
	//Vector cpuV = (Vector)ipAllData.get("cpu");
	if (cpuV != null && cpuV.size() > 0) {
		CPUcollectdata cpu = (CPUcollectdata) cpuV.get(0);
		if (cpu != null && cpu.getThevalue() != null) {
			cpuvalue = new Double(cpu.getThevalue());
		}
	}
	//�õ�ϵͳ����ʱ��
	//Vector systemV = (Vector)ipAllData.get("system");
	if (systemV != null && systemV.size() > 0) {
		for (int i = 0; i < systemV.size(); i++) {
			Systemcollectdata systemdata = (Systemcollectdata) systemV
					.get(i);
			if (systemdata.getSubentity().equalsIgnoreCase("sysUpTime")) {
				sysuptime = systemdata.getThevalue();
			}
			if (systemdata.getSubentity().equalsIgnoreCase(
					"sysServices")) {
				sysservices = systemdata.getThevalue();
			}
			if (systemdata.getSubentity().equalsIgnoreCase("sysDescr")) {
				sysdescr = systemdata.getThevalue();
			}
			if (systemdata.getSubentity().equalsIgnoreCase("SysName")) {
				sysname = systemdata.getThevalue();
			}
		}
	}
	//}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";

	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager = new HostCollectDataManager();
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ConnectUtilization",
				starttime1, totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}
	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
		maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
	}

	if (pingData != null && pingData.size() > 0) {
		Pingcollectdata pingdata = (Pingcollectdata) pingData.get(0);
		Calendar tempCal = (Calendar) pingdata.getCollecttime();
		Date cc = tempCal.getTime();
		collecttime = sdf1.format(cc);
	}

	//��Ӧʱ��
	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("����", "");
		maxresponse = (String) ConnectUtilizationhash.get("pingmax");
	}

	request.setAttribute("vector", vector);
	request.setAttribute("id", tmp);
	request.setAttribute("ipaddress", host.getIpAddress());
	request.setAttribute("cpuvalue", cpuvalue);
	request.setAttribute("collecttime", collecttime);
	request.setAttribute("sysuptime", sysuptime);
	request.setAttribute("sysservices", sysservices);
	request.setAttribute("sysdescr", sysdescr);
	request.setAttribute("pingconavg", new Double(pingconavg));

	String[] memoryItem = { "AllSize", "UsedSize", "Utilization" };
	//String[] memoryItemch={"������","��������","��ǰ������","���������"};
	String[] memoryItemch = { "����", "��ǰ", "���", "ƽ��" };

	String[] sysItem = { "sysName", "sysUpTime", "sysContact",
			"sysLocation", "sysServices", "sysDescr" };
	String[] sysItemch = { "�豸��", "�豸����ʱ��", "�豸��ϵ", "�豸λ��", "�豸����",
			"�豸����" };

	String[] diskItem = { "AllSize", "UsedSize", "Utilization" };
	String[] diskItemch = { "������", "��������", "������", "i-node��ʹ��",
			"i-node������" };

	Hashtable hash = (Hashtable) request.getAttribute("hash");
	Hashtable hash1 = (Hashtable) request.getAttribute("hash1");
	Hashtable max = (Hashtable) request.getAttribute("max");
	Hashtable imgurl = (Hashtable) request.getAttribute("imgurl");
	String avgcpu = (String) max.get("cpuavg");
	String cpumax = (String) max.get("cpu");
	//String newip = (String)request.getAttribute("newip");

	double avgpingcon = (Double) request.getAttribute("pingconavg");
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;

	int cpuper = Double.valueOf(cpuvalue).intValue();

	String rootPath = request.getContextPath();

	Vector ipmacvector = (Vector) request.getAttribute("vector");
	if (ipmacvector == null)
		ipmacvector = new Vector();

	//ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
	String chart1 = null, chart2 = null, chart3 = null, responseTime = null;

	Vector ifvector = (Vector) request.getAttribute("vector");
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

	//���ɵ���ƽ����ͨ��ͼ��
	Double realValue = Double.valueOf(pingvalue.replaceAll("%", ""));
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createPingPic(picip, realValue); //��ǰ��ͨ��
	_cpp.createAvgPingPic(picip, avgpingcon); //ƽ����ͨ��
	_cpp.createMinPingPic(picip, maxpingvalue.replace("%", ""));//��С��ͨ��
	//����CPU�Ǳ���
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper);
	cmp.createAvgCpuPic(picip, avgcpu);
	cmp.createMaxCpuPic(picip, cpumax);

	//������Ӧʱ��ͼ��
	CreateBarPic cbp = new CreateBarPic();
	cbp.createResponseTimePic(picip, responsevalue.replaceAll("%", ""),
			maxresponse.replaceAll("%", ""), avgresponse.replaceAll(
					"%", ""));
	//amchar ��ͨ��
	StringBuffer dataStr1 = new StringBuffer();
	dataStr1.append("��ͨ;").append(Math.round(realValue)).append(
			";false;7CFC00\\n");
	dataStr1.append("δ��ͨ;").append(100 - Math.round(realValue)).append(
			";false;FF0000\\n");
	String realdata = dataStr1.toString();
	StringBuffer dataStr2 = new StringBuffer();
	dataStr2.append("��ͨ;").append(Math.round(avgpingcon)).append(
			";false;7CFC00\\n");
	dataStr2.append("δ��ͨ;").append(100 - Math.round(avgpingcon))
			.append(";false;FF0000\\n");
	String avgdata = dataStr2.toString();
	StringBuffer dataStr3 = new StringBuffer();
	String maxping = maxpingvalue.replaceAll("%", "");
	dataStr3.append("��ͨ;")
			.append(Math.round(Float.parseFloat(maxping))).append(
					";false;7CFC00\\n");
	dataStr3.append("δ��ͨ;").append(
			100 - Math.round(Float.parseFloat(maxping))).append(
			";false;FF0000\\n");
	String maxdata = dataStr3.toString();
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


		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
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
				$("#pingavg").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png?nowtime="+(new Date()), alt: "ƽ����ͨ��" });
				$("#cpu").attr({ src: "<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png?nowtime="+(new Date()), alt: "��ǰCPU������" }); 
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
	$('#liantong-detail').hide();
	$('#xiangying-detail').hide();
	$('#cpuxinxi-detail').hide();
	$('#cipan-detail').hide();
	$('#neicun-detail').hide();
	$('#liantong').bind('click',function(){
			$('#liantong-detail').slideToggle('slow');
		});
	$('#xiangying').bind('click',function(){
			$('#xiangying-detail').slideToggle('slow');
		});
	$('#cpuxinxi').bind('click',function(){
			$('#cpuxinxi-detail').slideToggle('slow');
		});
	$('#cipan').bind('click',function(){
			$('#cipan-detail').slideToggle('slow');
		});
	$('#neicun').bind('click',function(){
			$('#neicun-detail').slideToggle('slow');
		});
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
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function reportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostping_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
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

//���Ӳ˵�	
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
	document.getElementById('sunDetailTitle-0').className='detail-data-title';
	document.getElementById('sunDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('sunDetailTitle-0').onmouseout="this.className='detail-data-title'";
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
							     	<table id="detail-content" class="detail-content" width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
				                      <tr>
					                    <td align="left" ><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					                    <td class="layout_title" align="center"><b >�豸��ϸ��Ϣ</b></td>
					                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
				                     </tr>
			             	          <tr>
				                       <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;������:
										        <span id="lable"><%=hostname%></span> </td>
                                       <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;ϵͳ����:
						 					<span id="sysname"><%=sysname%></span></td>
                                       <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP��ַ:
											<%
												IpAliasDao ipdao = new IpAliasDao();
												List iplist = ipdao.loadByIpaddress(host.getIpAddress());
												ipdao.close();
												ipdao = new IpAliasDao();
												IpAlias ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(), "1");
												ipdao.close();
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
														<%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>
														selected <%} %>><%=voTemp.getAliasip()%></option>
													<%
														}
													%>
												</select>
												[<a href="#" style="cursor: hand"onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">�޸�</a>]
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
															<%=sunDetailTitleTable%>
														</td>

													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
																<tr id="liantong">
																	<td colspan=2>
																		<table id="add-content-header" class="add-content-header">
																			<tr id="liantong">
																				<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																				<td class="add-content-title">
																					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��ͨ��</b>
																				</td>
																				<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																			</tr>
																		</table>
																		
																	</td>
																</tr>
																<tr>
																	<td>
																		<div id="liantong-detail">
																			<table>
																				<tr>
																	<td align=center>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent3">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "316", "190", "8", "#ffffff");
													so.write("flashcontent3");
												</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center valign=top>
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 316"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td height="29" class="detail-data-body-title"
																								style="height: 29">
																								����
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								��ǰ��%��
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								��С��%��
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								ƽ����%��
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								��ͨ��
																							</td>
																							<td class="detail-data-body-list"><%=pingvalue%></td>
																							<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(maxpingvalue.replaceAll("%",
							"")))%></td>
																							<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(pingconavg
							.replaceAll("%", "")))%></td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align=center>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>��ͨ��ʵʱ</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>
																																		<tr>
																																			<td valign=top>
																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<tr>
																																						<td valign=top>
																																							<div id="realping">
																																								<strong>You need to
																																									upgrade your Flash Player</strong>
																																							</div>

																																							<script type="text/javascript">
						 var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						   so.addVariable("path", "<%=rootPath%>/amchart/");
						   so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						   so.addVariable("chart_data","<%=realdata%>");
						   so.write("realping");
					</script>
																																							</td>
																																						<td valign=top>
																																							<div id="maxping">
																																								<strong>You need to
																																									upgrade your Flash Player</strong>
																																							</div>

																																							<script type="text/javascript">
						var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						    so.addVariable("path", "<%=rootPath%>/amchart/");
						    so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						    so.addVariable("chart_data","<%=maxdata%>");
						    so.write("maxping");
					</script>
																																						</td>
																																						<td valign=top>
																																							<div id="avgping">
																																								<strong>You need to
																																									upgrade your Flash Player</strong>
																																							</div>

																																							<script type="text/javascript">
						  var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						      so.addVariable("path", "<%=rootPath%>/amchart/");
						      so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						      so.addVariable("chart_data","<%=avgdata%>");
						      so.write("avgping");
					</script>
																																						</td>
																																					</tr>
																																					<tr height=25>
																																						<td valign=top align=center>
																																							<b>��ǰ</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>��С</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>ƽ��</b>
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
																		</div>
																	</td>
																</tr>
																<tr id="xiangying">
																	<td colspan=2>
																		<table id="add-content-header" class="add-content-header">
																			<tr id="liantong">
																				<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																				<td class="add-content-title">
																					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��Ӧʱ��</b>
																				</td>
																				<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																			</tr>
																		</table>
																		
																	</td>
																</tr>
																<tr>
																	<td>
																		<div id="xiangying-detail">
																			<table>
																				<tr>
																	<td align=center valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent5">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "316", "180", "8", "#ffffff");
													so.write("flashcontent5");
												</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 316"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td class="detail-data-body-title"
																								style="height: 29" height="29">
																								����
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								��ǰ��ms��
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								���ms��
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								ƽ����ms��
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								��Ӧʱ��
																							</td>
																							<td class="detail-data-body-list"><%=responsevalue.replaceAll("%", "")%></td>
																							<td class="detail-data-body-list"><%=maxresponse.replaceAll("%", "")%></td>
																							<td class="detail-data-body-list"><%=avgresponse.replaceAll("%", "")%></td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>

																	</td>
																	<td align=center>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>��Ӧʱ������</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>



																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center colspan=3>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>response.png">
																																						</td>
																																					</tr>
																																					<tr height=30>
																																						<td valign=top align=center>
																																							<b>��ǰ</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>���</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>ƽ��</b>
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
																		</div>
																	</td>
																</tr>
																<tr id="cpuxinxi">
																	<td colspan=2>
																		<table id="add-content-header" class="add-content-header">
																			<tr id="liantong">
																				<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																				<td class="add-content-title">
																					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;CPU��Ϣ</b>
																				</td>
																				<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																			</tr>
																		</table>
																		
																	</td>
																</tr>
																<tr>
																	<td>
																		<div id="cpuxinxi-detail">
																			<table>
																				<tr>
																	<td align=center>
																		<%
																			String str1 = "", str2 = "";
																			if (max.get("avgpingcon") != null) {
																				str1 = (String) max.get("avgpingcon");
																			}
																			if (max.get("avgrespcon") != null) {
																				str2 = (String) max.get("avgrespcon");
																			}
																		%>


																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent1">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Line_CPU.swf?ipadress=<%=host.getIpAddress()%>", "Line_CPU", "316", "180", "8", "#ffffff");
													so.write("flashcontent1");
												</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="0" cellspacing="1"
																						style="align: center; width: 316"
																						bgcolor="#FFFFFF">
																						<tr bgcolor="#FFFFFF">
																							<td height="29" class="detail-data-body-title"
																								style="height: 29">
																								����
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								��ǰ
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								���
																							</td>
																							<td class="detail-data-body-title"
																								style="height: 29">
																								ƽ��
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								&nbsp;CPU������
																							</td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=cpuper%>%
																							</td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=cpumax%></td>
																							<td class="detail-data-body-list">
																								&nbsp;<%=avgcpu%></td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align=center valign=top>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>CPU����������</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>



																																				<table cellpadding="0"
																																					cellspacing="0" width=48%
																																					align=center>
																																					<tr>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpu.png">
																																						</td>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpumax.png">
																																						</td>
																																						<td align=center>
																																							<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpuavg.png">
																																						</td>
																																					</tr>
																																					<tr height=20>
																																						<td valign=top align=center>
																																							<b>��ǰ</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>���</b>
																																						</td>
																																						<td valign=top align=center>
																																							<b>ƽ��</b>
																																						</td>
																																					</tr>
																																					<tr height=20>
																																						<td colspan=3>
																																							&nbsp;
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
																																	<table cellpadding="1" cellspacing="1"
																																		width=80%>
																																		<tr>
																																			<td valign=top width=80%>
																																				<table cellpadding="0"
																																					cellspacing="0" width=80%
																																					align=center>
																																					<%
																																						if (deviceV != null) {
																																							for (int m = 0; m < deviceV.size(); m++) {
																																								Devicecollectdata devicedata = (Devicecollectdata) deviceV
																																										.get(m);
																																								String name = devicedata.getName();
																																								String type = devicedata.getType();
																																								if (!"CPU".equals(type))
																																									continue;
																																								String status = devicedata.getStatus();
																																					%>
																																					<tr height=20>
																																						<td valign=top align=center>
																																							<b>����</b>��<%=type%></td>
																																						<td valign=top align=center>
																																							<b>����</b>��<%=name%></td>
																																						<td valign=top align=center>
																																							<b>״̬</b>��<%=status%></td>
																																					</tr>
																																					<%
																																						}
																																						}
																																					%>
																																				</table>

																																			</td>

																																		</tr>
																																		<tr height=20>

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
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																</tr>
																			</table>
																		</div>
																	</td>
																</tr>
																<tr id="neicun">
																	<td colspan=2>
																		<table id="add-content-header" class="add-content-header">
																			<tr id="liantong">
																				<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																				<td class="add-content-title">
																					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;�ڴ���Ϣ</b>
																				</td>
																				<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																			</tr>
																		</table>
																		
																	</td>
																</tr>
																<tr>
																	<td>
																		<div id="neicun-detail">
																			<table>
																				<tr>
																	<td align=center valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%
																			align=center>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent2">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
													var so = new SWFObject("<%=rootPath%>/flex/Area_Memory.swf?ipadress=<%=host.getIpAddress()%>", "Area_flux", "316", "194", "8", "#ffffff");
													so.write("flashcontent2");
												</script>
																				</td>
																			</tr>
																			<tr>
																				<td align=center valign=top>

																					<table width="90%" cellpadding="0" cellspacing="0"
																						style="align: center; valign: top; width: 316">
																						<tr align="center" height="28"
																							style="background-color: #EEEEEE;">
																							<td width="21%" class="detail-data-body-title"
																								style="height: 29">
																								�ڴ���
																							</td>
																							<%
																								String pmem = "";
																								String vmem = "";
																								String pcurmem = "";
																								String pmaxmem = "";
																								String pavgmem = "";
																								String vcurmem = "";
																								String vmaxmem = "";
																								String vavgmem = "";
																								for (int j = 0; j < memoryItemch.length; j++) {
																									String item = memoryItemch[j];
																							%>
																							<td class="detail-data-body-title"><%=item%></td>
																							<%
																								}
																							%>
																						</tr>
																						<%
																							for (int k = 0; k < memhash.size(); k++) {
																								Hashtable mhash = (Hashtable) (memhash.get(new Integer(k)));
																								String name = (String) mhash.get("name");
																						%>
																						<tr>
																							<td width="20%" class="detail-data-body-list"
																								height="29">
																								&nbsp;<%=name%></td>
																							<%
																								for (int j = 0; j < memoryItem.length; j++) {
																										String value = "";
																										if (mhash.get(memoryItem[j]) != null) {
																											value = (String) mhash.get(memoryItem[j]);
																											if (j == 1) {
																												if ("PhysicalMemory".equals(name))
																													pmem = value;
																												if ("SwapMemory".equals(name))
																													vmem = value;
																											} else {
																												if ("PhysicalMemory".equals(name))
																													pcurmem = value;
																												if ("SwapMemory".equals(name))
																													vcurmem = value;
																											}
																										} else {
																											continue;
																										}
																							%>
																							<td width="17%" class="detail-data-body-list">
																								&nbsp;<%=value%></td>
																							<%
																								}
																									String value = "";
																									if (memmaxhash.get(name) != null) {
																										value = (String) memmaxhash.get(name);
																										if ("PhysicalMemory".equals(name))
																											pmaxmem = value;
																										if ("SwapMemory".equals(name))
																											vmaxmem = value;
																									}
																									String avgvalue = "";
																									if (memavghash.get(name) != null) {
																										avgvalue = (String) memavghash.get(name);
																										if ("PhysicalMemory".equals(name))
																											pavgmem = avgvalue;
																										if ("SwapMemory".equals(name))
																											vavgmem = avgvalue;
																									}
																							%>
																							<td width="20%" class="detail-data-body-list">
																								&nbsp;<%=value%></td>
																							<td width="20%" class="detail-data-body-list">
																								&nbsp;<%=avgvalue%></td>
																						</tr>
																						<%
																							}
																						%>
																					</table>

																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align=center valign=top>
																		<br>
																		<table id="body-container" class="body-container"
																			style="background-color: #FFFFFF;" width="40%">
																			<tr>
																				<td class="td-container-main">
																					<table id="container-main" class="container-main">
																						<tr>
																							<td class="td-container-main-add">
																								<table id="container-main-add"
																									class="container-main-add">
																									<tr>
																										<td style="background-color: #FFFFFF;">
																											<table id="add-content" class="add-content">
																												<tr>
																													<td>
																														<table id="add-content-header"
																															class="add-content-header">
																															<tr>
																																<td align="left" width="5">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_01.jpg"
																																		width="5" height="29" />
																																</td>
																																<td class="add-content-title">
																																	<b>�豸�ڴ���ϸ</b>
																																</td>
																																<td align="right">
																																	<img
																																		src="<%=rootPath%>/common/images/right_t_03.jpg"
																																		width="5" height="29" />
																																</td>
																															</tr>
																														</table>
																													</td>
																												</tr>
																												<tr>
																													<td valign=top>
																														<table id="detail-content-body"
																															class="detail-content-body" border='1'>
																															<tr>
																																<td valign=top>
																																	<table cellpadding="1" cellspacing="1"
																																		width=48%>

																																		<tr>
																																			<td valign=top>

																																				<%
																																					if (pcurmem == null || pcurmem.equals(""))
																																						pcurmem = "0";
																																					if (pmaxmem == null || pmaxmem.equals(""))
																																						pmaxmem = "0";
																																					if (pavgmem == null || pavgmem.equals(""))
																																						pavgmem = "0";
																																					if (vcurmem == null || vcurmem.equals(""))
																																						vcurmem = "0";
																																					if (vmaxmem == null || vmaxmem.equals(""))
																																						vmaxmem = "0";
																																					if (vavgmem == null || vavgmem.equals(""))
																																						vavgmem = "0";
																																					pcurmem = pcurmem.replaceAll("%", "");
																																					pmaxmem = pmaxmem.replaceAll("%", "");
																																					pavgmem = pavgmem.replaceAll("%", "");
																																					vcurmem = vcurmem.replaceAll("%", "");
																																					vmaxmem = vmaxmem.replaceAll("%", "");
																																					vavgmem = vavgmem.replaceAll("%", "");

																																					double dpcurmem = new Double(pcurmem);
																																					double dpmaxmem = new Double(pmaxmem);
																																					double dpavgmem = new Double(pavgmem);

																																					double dvcurmem = new Double(vcurmem);
																																					double dvmaxmem = new Double(vmaxmem);
																																					double dvavgmem = new Double(vavgmem);

     

																																					double[] d_data1 = { dpcurmem, 100 - dpcurmem };
																																					double[] d_data2 = { dvcurmem, 100 - dvcurmem };
																																					String[] d_labels = { "��ʹ��", "δʹ��" };
																																					int[] d_colors = { 0x66ff66, 0x8080ff };
																																					String d_title1 = "����";
																																					String d_title2 = "����";
        

																																					double[] dmax_data1 = { dpmaxmem, 100 - dpmaxmem };
																																					double[] dmax_data2 = { dvmaxmem, 100 - dvmaxmem };

																																					
        

																																					double[] davg_data1 = { dpavgmem, 100 - dpavgmem };
																																					double[] davg_data2 = { dvavgmem, 100 - dvavgmem };

																																					StringBuffer xmlStr = new StringBuffer();
																																					xmlStr.append("<?xml version='1.0' encoding='gb2312'?>");
																																					xmlStr.append("<chart><series>");
																																					String[] titleStr = new String[] { "��ǰ����", "��ǰ����", "ƽ������", "ƽ������",
																																							"�������", "�������" };
																																					String[] title = new String[] { "��ǰ����", "��ǰδ��", "ƽ������", "ƽ��δ��",
																																							"�������", "���δ��" };

																																					for (int i = 0; i < 6; i++) {
																																						xmlStr.append("<value xid='").append(i).append("'>").append(
																																								titleStr[i]).append("</value>");

																																					}
																																					xmlStr.append("</series><graphs>");
																																					long curp = Math.round(d_data1[0]);
																																					long curv = Math.round(d_data2[0]);
																																					long maxp = Math.round(dmax_data1[0]);
																																					long maxv = Math.round(+dmax_data2[0]);
																																					long avgp = Math.round(davg_data1[0]);
																																					long avgv = Math.round(davg_data2[0]);

																																					long[] data = { curp, curv, 100 - curp, 100 - curv, avgp, avgv,
																																							100 - avgp, 100 - avgv, maxp, maxv, 100 - maxp, 100 - maxv };
																																					int tempInt = 0, tempId = 0;
																																					for (int i = 0; i < 6; i++) {
																																						if (i == 1)
																																							tempId = 0;
																																						if (i == 3)
																																							tempId = 2;
																																						if (i == 5)
																																							tempId = 4;
																																						xmlStr
																																								.append("<graph gid='")
																																								.append(i)
																																								.append("' title='")
																																								.append(title[i])
																																								.append("'>")
																																								.append("<value xid='" + tempId + "'> " + data[tempInt])
																																								.append("</value>");
																																						xmlStr.append("<value xid='" + (++tempId) + "'>"
																																								+ data[++tempInt] + "</value>");
																																						xmlStr.append("</graph>");
																																						tempId++;
																																						tempInt++;
																																					}

																																					xmlStr.append("</graphs></chart>");
																																					String memStr = xmlStr.toString();
																																				%>

																																				<table width="90%" cellpadding="0"
																																					cellspacing="0" border=0>
																																					<tr>
																																						<td valign=top>
																																							<div id="memorypercent">
																																								<strong>You need to
																																									upgrade your Flash Player</strong>
																																							</div>
																																							<script type="text/javascript">
		                        var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "amcolumn", "470", "210", "8", "#FFFFFF");
		                        so.addVariable("path", "<%=rootPath%>/amchart/");
		                        so.addVariable("settings_file",  escape("<%=rootPath%>/amcharts_settings/memorypercent_settings.xml"));
	                            so.addVariable("chart_data", "<%=memStr%>");
		                        so.addVariable("preloader_color", "#999999");
		                        so.write("memorypercent");
	                        </script>
																																						</td>

																																					</tr>

																																					<tr height=25>
																																						<td valign=center align=center
																																							colspan=3>

																																							<table cellpadding="0"
																																								cellspacing="0" width=48%
																																								align=center>
																																								<tr height=20>
																																									<td valign=top align=center>
																																										<b>�����ڴ�����</b>��<%=pmem%></td>
																																									<td valign=top align=center>
																																										<b>�����ڴ�����</b>��<%=vmem%></td>
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
																</tr>
																			</table>
																		</div>
																	</td>
																</tr>
																<tr id="cipan">
																	<td colspan=2>
																		<table id="add-content-header" class="add-content-header">
																			<tr id="liantong">
																				<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																				<td class="add-content-title">
																					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;������Ϣ</b>
																				</td>
																				<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																			</tr>
																		</table>
																		
																	</td>
																</tr>
																<tr>
																	<td>
																		<div id="cipan-detail">
																			<table>
																				<tr>
																	<td align=center colspan=2 style="width: 95%">
																		<br>
																		<table style="width: 95%" border=1>
																			<tr>
																				<td width=95%>
																					<table width="95%">
																						<tr>
																							<td align="left" height=30 bgcolor="#ECECEC">
																								&nbsp;
																								<B>����������</B>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																			<tr>
																				<td width="95%">
																					<table width="95%">
																						<tr>
																							<%
																								String[] diskname = null;
																								String[] percent = null;
																								String[] diskValue = null;
																								String dataStr = "";
																								if (Disk != null && Disk.size() > 0) {
																									diskname = new String[Disk.size()];
																									percent = new String[Disk.size()];
																									diskValue = new String[Disk.size()];
																									// д����
																									for (int i = 0; i < Disk.size(); i++) {
																										Hashtable diskhash = (Hashtable) (Disk.get(new Integer(i)));
																										String name = (String) diskhash.get("name");
																										diskname[i] = name;

																										String value = "";
																										if (diskhash.get("Utilization") != null) {
																											value = (String) diskhash.get("Utilization");
																											percent[i] = value;
																										}
																									}

																								}
																								StringBuffer sb = new StringBuffer();

																								String[] colorStr = new String[] { "#33FF33", "#FF0033", "#9900FF",
																										"#FFFF00", "#33CCFF", "#003366", "#33FF33", "#FF0033",
																										"#9900FF", "#FFFF00", "#333399", "#0000FF", "#A52A2A",
																										"#23f266" };
																								sb.append("<chart><series>");
																								if (diskname != null) {
																									for (int k = 0; k < diskname.length; k++) {
																										sb.append("<value xid='").append(k).append("'>").append(
																												diskname[k]).append("</value>");

																									}
																								}
																								sb.append("</series><graphs><graph gid='0'>");
																								if (percent != null) {
																									for (int i = 0, j = 0; i < percent.length; i++, j++) {
																										if (i / colorStr.length == 1)
																											j = 0;
																										int perInt = Math.round(Float.parseFloat(percent[i]
																												.replaceAll("%", "")));
																										sb.append("<value xid='").append(i).append("' color='")
																												.append(colorStr[j]).append("'>" + perInt).append(
																														"</value>");
																									}
																								}
																								sb.append("</graph></graphs></chart>");
																								dataStr = sb.toString();
																							%>
																							<td align=center>
																								<!--  <img src="<%=rootPath%>/resource/image/jfreechart/<%=newip%>disk.png"/>-->
																								<div id="disksolaris">
																									<strong>You need to upgrade your Flash
																										Player</strong>
																								</div>

																								<script type="text/javascript">
						                                                                         var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","670", "388", "8", "#FFFFFF");
						                                                                             so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                             so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/dbpercent_settings.xml"));
						                                                                             so.addVariable("chart_data","<%=dataStr%>");
						                                                                             so.write("disksolaris");
						                                                                  </script>
																							</td>
																						</tr>
																						<tr>
																							<td align=center width=95%>
																								<br>
																								<table cellpadding="0" cellspacing="0">
																									<tr>
																										<td align=center width=95%>
																											<div id="loading">
																												<div class="loading-indicator">
																													<img
																														src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
																														width="32" height="32"
																														style="margin-right: 8px;" align="middle" />
																													Loading...
																												</div>
																											</div>
																											<table border="1" align=center>
																												<tr>
																													<td class="detail-data-body-title"
																														style="height: 29">
																														������
																													</td>
																													<td class="detail-data-body-title"
																														style="height: 29">
																														������
																													</td>
																													<td class="detail-data-body-title"
																														style="height: 29">
																														��������
																													</td>
																													<td class="detail-data-body-title"
																														style="height: 29">
																														������
																													</td>
																												</tr>
																												<%
																													if (Disk != null && Disk.size() > 0) {
																														// д����
																														for (int i = 0; i < Disk.size(); i++) {
																												%>
																												<tr>
																													<%
																														Hashtable diskhash = (Hashtable) (Disk.get(new Integer(i)));
																																String name = (String) diskhash.get("name");
																													%>
																													<td class="detail-data-body-list"
																														height="29"><%=name%></td>
																													<%
																														for (int j = 0; j < diskItem.length; j++) {
																																	String value = "";
																																	if (diskhash.get(diskItem[j]) != null) {
																																		value = (String) diskhash.get(diskItem[j]);
																																	}
																													%>
																													<td class="detail-data-body-list"
																														height="29"><%=value%></td>
																													<%
																														}
																													%>
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
																	</td>
																</tr>

																<tr>
																	<td align=center colspan=2>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=95%>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent10">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
										var so = new SWFObject("<%=rootPath%>/flex/Area_Disk_month.swf?ipadress=<%=host.getIpAddress()%>&id=2", "Area_Disk_month", "800", "350", "8", "#ffffff");
										so.write("flashcontent10");
									</script>
																				</td>
																			</tr>
																		</table>
																	</td>

																</tr>
																<tr>


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
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="host" name="category" />
										<jsp:param value="solaris" name="subtype" />
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