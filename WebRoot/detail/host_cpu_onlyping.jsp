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
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%
	String temp = "";
	String runmodel = PollingEngine.getCollectwebflag();
	String menuTable = (String) request.getAttribute("menuTable");
	String[] diskItem = { "AllSize", "UsedSize", "Utilization" };
	String[] diskItemch = { "������", "��������", "������" };

	String[] memoryItem = { "Capability", "Utilization" };
	String[] memoryItemch = { "����", "��ǰ", "���", "ƽ��" };

	List cpulist = new ArrayList();
	Vector deviceV = new Vector();

	Hashtable memmaxhash = (Hashtable) request
			.getAttribute("memmaxhash");
	Hashtable memavghash = (Hashtable) request
			.getAttribute("memavghash");
	Hashtable diskhash = new Hashtable();
	Hashtable memhash = new Hashtable();

	String memcollecttime = "";
	String pingcollecttime = "";
	String avgcpu = "";
	String cpumax = "";
	Hashtable max = (Hashtable) request.getAttribute("max");
	if (max != null) {
		avgcpu = (String) max.get("cpuavg");
		cpumax = (String) max.get("cpu");
	}
	if (avgcpu.equals("") || avgcpu == null)
		avgcpu = "0";
	if (cpumax.equals("") || cpumax == null)
		cpumax = "0";
	String begindate = "";
	String enddate = "";

	String tmp = request.getParameter("id");
	String _flag = (String) request.getAttribute("flag");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg = "0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String syslocation = "";
	String vvalue = "0";
	String pvalue = "0";
	float capvalue = 0f;
	float fvvalue = 0f;
	String vused = "0";
	String maxpingvalue = "0";
	String responsevalue = "0";
	String pingvalue = "0";

	int cpuper = 0;

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
   String subtype="";
   if(nodedto!=null){
	subtype=nodedto.getSubtype();
   }
	String ipaddress = host.getIpAddress();
	//CommonUtil commutil = new CommonUtil();
	String picip = CommonUtil.doip(ipaddress);
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

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
	Vector memoryVector = new Vector();
	if ("0".equals(runmodel)) {
		//�ɼ�������Ǽ���ģʽ
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(
				host.getIpAddress());
		if (ipAllData != null) {

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
			Vector pingData = (Vector) ShareData.getPingdata().get(
					host.getIpAddress());
			if (pingData != null && pingData.size() > 0) {
				Pingcollectdata pingdata = (Pingcollectdata) pingData
						.get(0);
				Calendar tempCal = (Calendar) pingdata.getCollecttime();
				Date cc = tempCal.getTime();
				collecttime = sdf1.format(cc);
				SimpleDateFormat _sdf1 = new SimpleDateFormat(
						"MM-dd HH:mm");
				pingcollecttime = _sdf1.format(cc);
				pingvalue = pingdata.getThevalue();
				pingdata = (Pingcollectdata) pingData.get(1);
				responsevalue = pingdata.getThevalue();
			}
		}

	} else {
		//�ɼ�������Ƿ���ģʽ
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		try {
			systemList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId() + "", nodedto
					.getType(), nodedto.getSubtype()).getCurrPingInfo();
		} catch (Exception e) {
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
					pingvalue = nodetemp.getThevalue();
					SimpleDateFormat _sdf1 = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					Date d = _sdf1.parse(nodetemp.getCollecttime());
					//Calendar c = Calendar.getInstance();
					//c.setTime(d);
					_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
					pingcollecttime = _sdf1.format(d);
				}
				if ("ResponseTime".equals(nodetemp.getSindex())) {
					responsevalue = nodetemp.getThevalue();
				}
			}
		}
	}

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
		maxpingvalue = maxpingvalue.replaceAll("%", "");
	}

	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	//----�õ���Ӧʱ�� 
	String avgresponse = "0";//-----------
	String maxresponse = "0";//----------- 

	try {
		ConnectUtilizationhash = hostmanager.getCategory(host
				.getIpAddress(), "Ping", "ResponseTime", starttime1,
				totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("����", "").replaceAll("%", "");
		maxresponse = (String) ConnectUtilizationhash.get("pingmax");
		maxresponse = maxresponse.replaceAll("%", "");
	}

	double avgpingcon = new Double(pingconavg);
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;

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

	CreateMetersPic cmp = new CreateMetersPic();
	//�����ڴ�������ͼ   

	//���ɵ���ƽ����ͨ��ͼ��
	CreatePiePicture _cpp = new CreatePiePicture();
	TitleModel _titleModel = new TitleModel();
	_titleModel.setXpic(150);
	_titleModel.setYpic(150);//160, 200, 150, 100
	_titleModel.setX1(75);//�⻷�����λ��
	_titleModel.setX2(60);//�⻷���ϵ�λ��
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
	//double[] _data2 = {77, 87};
	String[] p_labels = { "��ͨ", "δ��ͨ" };
	int[] _colors = { 0x66ff66, 0xff0000 };
	String _title1 = "��һ����";
	String _title2 = "�ڶ�����";
	_cpp.createOneRingChart(_data1, p_labels, _colors, _titleModel);

	//���ɵ�ǰ��ͨ��ͼ��
	CreatePiePicture ping_cpp = new CreatePiePicture();
	TitleModel ping_titleModel = new TitleModel();
	ping_titleModel.setXpic(150);
	ping_titleModel.setYpic(150);//160, 200, 150, 100
	ping_titleModel.setX1(75);//�⻷�����λ��
	ping_titleModel.setX2(60);//�⻷���ϵ�λ��
	ping_titleModel.setX3(65);
	ping_titleModel.setX4(30);
	ping_titleModel.setX5(75);
	ping_titleModel.setX6(70);
	ping_titleModel.setX7(10);
	ping_titleModel.setX8(115);
	ping_titleModel.setBgcolor(0xffffff);
	ping_titleModel.setPictype("png");
	ping_titleModel.setPicName(picip + "realping");
	ping_titleModel.setTopTitle("");

	double[] ping_data1 = { avgpingcon, 100 - avgpingcon };
	String[] ping_labels = { "��ͨ", "δ��ͨ" };
	int[] ping_colors = { 0x66ff66, 0xff0000 };
	_cpp.createOneRingChart(ping_data1, ping_labels, ping_colors,
			ping_titleModel);

	//������С��ͨ��ͼ��
	ping_titleModel = new TitleModel();
	ping_titleModel.setXpic(150);
	ping_titleModel.setYpic(150);//160, 200, 150, 100
	ping_titleModel.setX1(75);//�⻷�����λ��
	ping_titleModel.setX2(60);//�⻷���ϵ�λ��
	ping_titleModel.setX3(65);
	ping_titleModel.setX4(30);
	ping_titleModel.setX5(75);
	ping_titleModel.setX6(70);
	ping_titleModel.setX7(10);
	ping_titleModel.setX8(115);
	ping_titleModel.setBgcolor(0xffffff);
	ping_titleModel.setPictype("png");
	ping_titleModel.setPicName(picip + "minping");
	ping_titleModel.setTopTitle("");
	double d_maxping = new Double(maxpingvalue);
	double[] minping_data1 = { d_maxping, 100 - d_maxping };
	//String[] ping_labels = {"��ͨ", "δ��ͨ"};
	//int[] ping_colors = {0x66ff66, 0xff0000}; 
	_cpp.createOneRingChart(minping_data1, ping_labels, ping_colors,
			ping_titleModel);

	CreateBarPic cbp = new CreateBarPic();
	TitleModel tm = new TitleModel();

	cbp = new CreateBarPic();

	double[] r_data1 = { new Double(responsevalue),
			new Double(maxresponse), new Double(avgresponse) };
	String[] r_labels = { "��ǰ��Ӧʱ��(ms)", "�����Ӧʱ��(ms)", "ƽ����Ӧʱ��(ms)" };
	tm = new TitleModel();
	tm.setPicName(picip + "response");//
	tm.setBgcolor(0xffffff);
	tm.setXpic(450);//ͼƬ����
	tm.setYpic(180);//ͼƬ�߶�
	tm.setX1(30);//�������
	tm.setX2(20);//�������
	tm.setX3(400);//��ͼ���
	tm.setX4(130);//��ͼ�߶�
	tm.setX5(10);
	tm.setX6(115);
	cbp.createTimeBarPic(r_data1, r_labels, tm, 40);
	tm.setPicName(picip + "response1");//
			
    //amchar ��ͨ��
   Double realValue = Double.valueOf(pingvalue.replaceAll("%", ""));
   StringBuffer dataStr1 = new StringBuffer();
	dataStr1.append("��ͨ;").append(Math.round(realValue)).append(";false;7CFC00\\n");
	dataStr1.append("δ��ͨ;").append(100-Math.round(realValue)).append(";false;FF0000\\n");
	String realdata = dataStr1.toString();
			StringBuffer dataStr2 = new StringBuffer();
	dataStr2.append("��ͨ;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
	dataStr2.append("δ��ͨ;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
	String avgdata = dataStr2.toString();
			StringBuffer dataStr3 = new StringBuffer();
			String maxping=maxpingvalue.replaceAll("%", "");
	dataStr3.append("��ͨ;").append(Math.round(Float.parseFloat(maxping))).append(";false;7CFC00\\n");
	dataStr3.append("δ��ͨ;").append(100-Math.round(Float.parseFloat(maxping))).append(";false;FF0000\\n");
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

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

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
			src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
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
	$('#xiangxi-detail').hide();
	$('#cipan-detail').hide();
	$('#neicun-detail').hide();
	$('#liantong').bind('click',function(){
			$('#liantong-detail').slideToggle('slow');
		});
	$('#xiangying').bind('click',function(){
			$('#xiangying-detail').slideToggle('slow');
		});
	$('#xiangxi').bind('click',function(){
			$('#xiangxi-detail').slideToggle('slow');
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

		<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}
function gzmajax2(){
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_memory&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vvvvalue=100-vvalue;
			var vused=data.vused;
			$('#PFTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vused+'GB</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			$('#WLTABLE').html('<tr><td width="'+pvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+pvalue+'%</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			$('#XNTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vvalue+'%</td><td width="'+vvvvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//gzmajax();
	//});
setInterval(gzmajax2,60000);
});
</script>

		<script type="text/javascript">
	function closetime(){
		Ext.MessageBox.hide();
	}
	function gzmajax_PF(){
	Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vused=data.vused;
			$('#PFTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vused+'GB</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function gzmajax_WL(){
	Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vused=data.vused;
			$('#WLTABLE').html('<tr><td width="'+pvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+pvalue+'%</td><td width="'+pppvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function gzmajax_XN(){
	Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxMemory_fresh&tmp=<%=tmp%>&nowtime="+(new Date()),
			success:function(data){
			var vvalue=data.vvalue;
			var pvalue=data.pvalue;
			var pppvalue=100-pvalue;
			var vvvvalue=100-vvalue;
			var vused=data.vused;
			$('#XNTABLE').html('<tr><td width="'+vvalue+'%" bgcolor="green" align=center>&nbsp;&nbsp;'+vvalue+'%</td><td width="'+vvvvalue+'%" bgcolor=#ffffff></td></tr>');
			}
		});
}
	function fresh_PF(){
		gzmajax_PF();
		setTimeout(closetime,2000);
	}
	function fresh_WL(){
		gzmajax_WL();
		setTimeout(closetime,2000);
	}
	function fresh_XN(){
		gzmajax_XN();
		setTimeout(closetime,2000);
	}
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
  	location.href="<%=rootPath%>/topology/network/networkview.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
} 

function cpuReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostcpu_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
  }
  
  
  
  function memoryReportByMinute(){
	window.open("<%=rootPath%>/monitor.do?action=hostmemory_report&id=<%=tmp%>&timeType=minute","newWindow","height=300, width=800, top=0, left= 200,screenX=0,screenY=0");
   
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
	document.getElementById('hostDetailTitle-1').className='detail-data-title';
	document.getElementById('hostDetailTitle-1').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostDetailTitle-1').onmouseout="this.className='detail-data-title'";
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

// zhubinhua  add   
//�����ڷ����������ɶ�ӦFlexͼƬ,,Ȼ������word�ĵ�  ����
function outputWord() {
	//alert("YES!");
	//������ͼƬ
	window["Area_flux"].createImage();
	window["Response_time"].createImage();
	window["Area_Disk"].createImage();
	window["Column_Disk"].createImage();
	window["Line_CPU"].createImage();
	window["Ping"].createImage();
	
	//window.open("<%=rootPath%>/monitor.do?action=createWord");
	var a = document.getElementById("outputWord");
	a.href="<%=rootPath%>/monitor.do?action=createWord&whattype=host&id=<%=tmp%>";
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
								<td class="td-container-main-detail" width=85%>
									<table id="container-main-detail" class="container-main-detail">
										<tr>
											<td>
												<jsp:include page="/topology/includejsp/systeminfo_host.jsp">
													<jsp:param name="rootPath" value="<%=rootPath%>" />
													<jsp:param name="tmp" value="<%=tmp%>" />
													<jsp:param name="sysdescr" value="<%=sysdescr%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="sysuptime" value="<%=sysuptime%>" />
													<jsp:param name="collecttime" value="<%=collecttime%>" />
													<jsp:param name="pvalue" value="<%=pvalue%>" />
													<jsp:param name="vvalue" value="<%=vvalue%>" />
													<jsp:param name="vused" value="<%=vused%>" />
													<jsp:param name="picip" value="<%=picip%>" />
													<jsp:param name="pingavg" value="<%=Math.round(Float.parseFloat(pingconavg))%>"/>
													<jsp:param name="avgresponse" value="<%=avgresponse%>" />
												</jsp:include>
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=hostDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">
																<!--<tr>
																<td>
																	&nbsp;
																</td>
																<td align="right">
																	<a id="outputWord" onclick="outputWord()" href=""><img name="selDay1" alt='����word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">����word</a>&nbsp;&nbsp;
																</td>
														</tr>-->
																<!--<tr>
																<td>
																	&nbsp;
																</td>
																<td align="right">
																<table>
																<tr>
																<td align=right width=40%><img name="selDay1" src="<%=rootPath%>/resource/image/topo/server-B-24.gif" border="0"></td>
																<td align=center valign=top width=20% background="<%=rootPath%>/resource/image/topo/arrow/right.gif" style="text-align: center;background-repeat:no-repeat;valign:top;" ><%=pingvalue%>%</td>
																<td align=left width=40%><img name="selDay1" src="<%=rootPath%>/resource/image/topo/server/22.gif" border="0"></td>
																</tr>
																<tr>
																<td align=right>���ܷ�����</td>
																<td align=center valign=top><%=pingvalue%>%</td>
																<td align=left><%=host.getIpAddress()%></td>
																</tr>
																</table>
																	
																</td>
														</tr>-->
																<tr id="liantong">
																	<td colspan=2>
																		<table id="add-content-header" class="add-content-header">
																			<tr id="cpuliyonglv">
																				<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																				<td class="add-content-title">
																					<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��ͨ��ʵʱ</b>
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
																	<td align=center width=43% valign=top>
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
																					var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "191", "8", "#ffffff");
																					so.write("flashcontent3");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 346"
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
																							<td class="detail-data-body-title"
																								style="height: 29">
																								�ɼ�ʱ��
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								��ͨ��
																							</td>
																							<td class="detail-data-body-list"><%=pingvalue%></td>
																							<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(maxpingvalue))%></td>
																							<td class="detail-data-body-list"><%=percent1%></td>
																							<td class="detail-data-body-list"><%=pingcollecttime%></td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>
																	</td>
																	<td align=left valign=top width=57%>

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
																																						<!--  	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>realping.png">-->
																																								<div id="realping">
							                                                                                                                                <strong>You need to upgrade your Flash Player</strong>
						                                                                                                                                 </div>
						                                                                                                                            <script type="text/javascript"
							                                                                                                                       src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                                                                           <script type="text/javascript">
						                                                                                                                           var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						                                                                                                                           so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                                           so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                                                                                                           so.addVariable("chart_data","<%=realdata%>");
						                                                                                                                           so.write("realping");
						                                                                                                                            </script>
																																						</td>
																																						<td valign=top>
																																						<!--	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>minping.png">-->
																																						<div id="maxping">
							                                                                                                                                <strong>You need to upgrade your Flash Player</strong>
						                                                                                                                                 </div>
						                                                                                                                            <script type="text/javascript"
							                                                                                                                       src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                                                                           <script type="text/javascript">
						                                                                                                                           var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						                                                                                                                           so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                                           so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                                                                                                           so.addVariable("chart_data","<%=maxdata%>");
						                                                                                                                           so.write("maxping");
						                                                                                                                            </script>
																																						</td>
																																						<td valign=top>
																																						<!--	<img
																																								src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">-->
																																						<div id="avgping">
							                                                                                                                                <strong>You need to upgrade your Flash Player</strong>
						                                                                                                                                 </div>
						                                                                                                                            <script type="text/javascript"
							                                                                                                                       src="<%=rootPath%>/include/swfobject.js"></script>
						                                                                                                                           <script type="text/javascript">
						                                                                                                                           var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","160", "155", "8", "#FFFFFF");
						                                                                                                                           so.addVariable("path", "<%=rootPath%>/amchart/");
						                                                                                                                           so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						                                                                                                                           so.addVariable("chart_data","<%=avgdata%>");
						                                                                                                                           so.write("avgping");
						                                                                                                                            </script>
																																						</td>
																																					</tr>
																																					<tr height=50>
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
																			<tr id="cpuliyonglv">
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
																	<td colspan=2>
																		<div id="xiangying-detail">
																			<table>
																				<tr>

																	<td align=center valign=top>
																		<br>
																		<table cellpadding="0" cellspacing="0" width=48%>
																			<tr>
																				<td width="100%" align=center>
																					<div id="flashcontent_5">
																						<strong>You need to upgrade your Flash
																							Player</strong>
																					</div>
																					<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "196", "8", "#ffffff");
																					so.write("flashcontent_5");
																				</script>
																				</td>
																			</tr>
																			<tr>
																				<td width=100% align=center>
																					<table cellpadding="1" cellspacing="1"
																						style="align: center; width: 346"
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
																							<td class="detail-data-body-title"
																								style="height: 29">
																								�ɼ�ʱ��
																							</td>
																						</tr>
																						<tr>
																							<td class="detail-data-body-list" height="29">
																								��Ӧʱ��
																							</td>
																							<td class="detail-data-body-list"><%=responsevalue%></td>
																							<td class="detail-data-body-list"><%=maxresponse%></td>
																							<td class="detail-data-body-list"><%=avgresponse%></td>
																							<td class="detail-data-body-list"><%=pingcollecttime%></td>
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
															</table>
														</td>
													</tr>

												</table>

											</td>
										</tr>
									</table>
								</td>
								
								<td class="td-container-main-tool" width=15%>
									<jsp:include page="/include/toolbar.jsp">
										<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
										<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
										<jsp:param value="<%=tmp%>" name="tmp" />
										<jsp:param value="host" name="category" />
										<jsp:param value="<%=subtype%>" name="subtype" />
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
