<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.report.amchart.AmChartTool"%>
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
<%@page import="com.afunms.topology.dao.IpMacBaseDao"%>

<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.api.I_Portconfig"%>
<%@ page import="com.afunms.polling.om.Portconfig"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.polling.impl.PortconfigManager"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@ page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.config.dao.*"%>
<%@ page import="com.afunms.config.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.ipaccounting.dao.*"%>
<%@page import="com.afunms.ipaccounting.model.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>


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
	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");
	

	String srcipstr = "";//ԴIP��ַ
	if(request.getAttribute("srcip") != null){
		srcipstr = (String)request.getAttribute("srcip");
	}
	String curin = "0";
	String curout = "0";

	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
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
		host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
	}
	NodeUtil nodeUtil = new NodeUtil();
	NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
	String ipaddress = host.getIpAddress();

	IpAccountingDao ipdao = new IpAccountingDao();
	List flowlist = new ArrayList();
	try {
		flowlist = ipdao.getNetFLow(tmp, startdate, todate, srcipstr);
	} catch (Exception e) {
	} finally {
		ipdao.close();
	}
	if(flowlist == null){
		flowlist = new ArrayList();
	}
	
	ipdao = new IpAccountingDao();
	List flowLineList = null;
	try {
		flowLineList = ipdao.getNetFlowLineData(tmp, startdate, todate);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		ipdao.close();
	}
	if(flowlist == null){
		flowlist = new ArrayList();
	}
	session.setAttribute("flowLineList",flowLineList);//�����������ݼ���session
	AmChartTool amChartTool = new AmChartTool();
	//�����ڴ�amchartͼ
	//����netflowdata  ����ʼIPͳ�Ƶ�������״ͼ
	String netflowdata = null;
	//���� ��IPͳ�Ƶ�����������ͼ
	String netflowlinedata = amChartTool.getNetNetFlowLineChart(flowLineList); 
	
	
	//���IPMAC��Ϣ
	Vector ipmacvector = new Vector();

	if ("0".equals(runmodel)) {
		//�ɼ�������Ǽ���ģʽ
		Hashtable ipAllData = (Hashtable) ShareData.getSharedata().get(host.getIpAddress());
		if (ipAllData != null) {
			//�õ�ϵͳ����ʱ��
			Vector systemV = (Vector) ipAllData.get("system");
			if (systemV != null && systemV.size() > 0) {
				for (int i = 0; i < systemV.size(); i++) {
					Systemcollectdata systemdata = (Systemcollectdata) systemV.get(i);
					if (systemdata.getSubentity().equalsIgnoreCase("sysUpTime")) {
						sysuptime = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase("sysServices")) {
						sysservices = systemdata.getThevalue();
					}
					if (systemdata.getSubentity().equalsIgnoreCase("sysDescr")) {
						sysdescr = systemdata.getThevalue();
					}
				}
			}
		}

		//----pingֵ����Ӧʱ��begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
				pingconavg = pingconavg.replace("%", "");
				maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
				maxpingvalue = maxpingvalue.replaceAll("%", "");
			}
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ResponseTime", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
				avgresponse = avgresponse.replace("����", "").replaceAll("%", "");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse = maxresponse.replaceAll("%", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end 

		Vector pingData = (Vector) ShareData.getPingdata().get(host.getIpAddress());
		if (pingData != null && pingData.size() > 0) {
			Pingcollectdata pingdata = (Pingcollectdata) pingData.get(0);
			Calendar tempCal = (Calendar) pingdata.getCollecttime();
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
		}
	} else {
		//�ɼ�������Ƿ���ģʽ
		List systemList = new ArrayList();
		List pingList = new ArrayList();
		try {
			systemList = new NetService(host.getId() + "", nodedto.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId() + "", nodedto.getType(), nodedto.getSubtype()).getCurrPingInfo();
			pingconavg = new NetService(host.getId() + "", nodedto.getType(), nodedto.getSubtype()).getCurrDayPingAvgInfo();
		} catch (Exception e) {
		}
		//5.
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
		//----pingֵ����Ӧʱ��begin
		Hashtable ConnectUtilizationhash = new Hashtable();
		I_HostCollectData hostmanager = new HostCollectDataManager();
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ConnectUtilization", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
				pingconavg = pingconavg.replace("%", "");
				maxpingvalue = (String) ConnectUtilizationhash.get("pingmax");
				maxpingvalue = maxpingvalue.replaceAll("%", "");
			}
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		try {
			ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(), "Ping", "ResponseTime", starttime1, totime1);
			if (ConnectUtilizationhash.get("avgpingcon") != null) {
				avgresponse = (String) ConnectUtilizationhash.get("avgpingcon");
				avgresponse = avgresponse.replace("����", "").replaceAll("%", "");
				maxresponse = (String) ConnectUtilizationhash.get("pingmax");
				maxresponse = maxresponse.replaceAll("%", "");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//----pingֵ����Ӧʱ��end

		if (pingList != null && pingList.size() > 0) {
			for (int i = 0; i < pingList.size(); i++) {
				NodeTemp nodetemp = (NodeTemp) pingList.get(i);
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					collecttime = nodetemp.getCollecttime();
				}
				if ("ResponseTime".equals(nodetemp.getSindex())) {
					responsevalue = nodetemp.getThevalue();
				}
				if ("ConnectUtilization".equals(nodetemp.getSindex())) {
					pingvalue = nodetemp.getThevalue();
				}
			}
		}
	}

	double avgpingcon = new Double(pingconavg);

	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100 - percent1;
	int cpuper = Double.valueOf(cpuvalue).intValue();

	String rootPath = request.getContextPath();

	IpMacBaseDao dao = new IpMacBaseDao();
	List macbaselist = null;
	try {
		macbaselist = dao.loadAll();
	} catch (Exception e) {
	} finally {
		dao.close();
	}
	Hashtable macbaseHash = new Hashtable();
	if (macbaselist != null && macbaselist.size() > 0) {
		for (int i = 0; i < macbaselist.size(); i++) {
			IpMacBase ipmacbase = (IpMacBase) macbaselist.get(i);
			macbaseHash.put(ipmacbase.getMac() + ":" + ipmacbase.getRelateipaddr(), ipmacbase);
		}
	}

	SupperDao supperdao = new SupperDao();
	Supper supper = null;
	String suppername = "";
	try {
		supper = (Supper) supperdao.findByID(host.getSupperid() + "");
		if (supper != null)
			suppername = supper.getSu_name() + "(" + supper.getSu_dept() + ")";
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		supperdao.close();
	}
	String _flag = (String) request.getAttribute("flag");

	String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	//6.
	int allmemoryvalue = 0;
	if (memoryVector != null && memoryVector.size() > 0) {
		for (int i = 0; i < memoryVector.size(); i++) {
			Memorycollectdata memorycollectdata = (Memorycollectdata) memoryVector.get(i);
			allmemoryvalue = allmemoryvalue + Integer.parseInt(memorycollectdata.getThevalue());
		}
		memoryvalue = (allmemoryvalue / memoryVector.size()) + "";
	}
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
	String[] p_labels = { "��ͨ", "δ��ͨ" };
	int[] _colors = { 0x66ff66, 0xff0000 };
	_cpp.createOneRingChart(_data1, p_labels, _colors, _titleModel);

	//����CPU�Ǳ���
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip, cpuper);

	//���ڸ�ʽת��
	try{
		Date d1 = sdf1.parse(startdate);
		Date d2 = sdf1.parse(todate);
		startdate = sdf2.format(d1);
		todate = sdf2.format(d2);
	}catch(Exception e){
		e.printStackTrace();
	}
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
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
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css" />


		<link rel="stylesheet"
			href="<%=rootPath%>/application/resource/jquery_tablesort/flora/flora.all.css"
			type="text/css" media="screen" title="Flora (Default)">
		<script
			src="<%=rootPath%>/application/resource/jquery_tablesort/jquery-latest.js"
			type="text/javascript"></script>
		<script
			src="<%=rootPath%>/application/resource/jquery_tablesort/jquery.tablesorter.js"
			type="text/javascript"></script>
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
setInterval(gzmajax,60000);
});

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
var srcip;//ԴIP��ַ
var nodeid;//�豸ID
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
	document.getElementById('netDetailTitle-11').className='detail-data-title';
	document.getElementById('netDetailTitle-11').onmouseover="this.className='detail-data-title'";
	document.getElementById('netDetailTitle-11').onmouseout="this.className='detail-data-title'";
	

 
  Ext.get("processmac").on("click",function(){
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/macmanager.do?action=refresh&id=<%=tmp%>&flag=<%=flag%>";
  mainForm.submit();
 });
 
   Ext.get("process3").on("click",function(){
  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/ipmac.do?action=setmultimacbase&id=<%=tmp%>&flag=1";
  mainForm.submit();
 });
 

 
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	

 

	
});

/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip)
	{	
		srcip=ip;
		nodeid=nodeid;
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
	
	/**************************************************
	*��������Ϣ��ϸҳ
	**************************************************/
	function detail()
	{
	    //location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
       	    // alert("<%=tmp%>"+"****"+"<%=host.getIpAddress()%>"+"****"+node+"******"+ipaddress);
	   // window.open("<%=rootPath%>/netflow.do?action=showNetflowDetail&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+node+"&ifname="+ipaddress, "newwindow", "height=450, width=840, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
		var url = "<%=rootPath%>/netflow.do?action=showNetflowDetail&nodeid=<%=tmp%>"+"&srcip="+srcip+"&startdate=<%=startdate%>&todate=<%=todate%>";
		url = encodeURI(url);
		url = encodeURI(url); 
		 var result = showModalDialog(url, "", "dialogWidth:900px;dialogHeight:600px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes","");
		 if(result == 1) {
			window.location.reload(); 
		 }
	}
	
	/**************************************************
	*������ѯ������Ϣ
	**************************************************/
	function query(){
		mainForm.action = "<%=rootPath%>/netflow.do?action=netflow";
    	mainForm.submit(); 
	}
	
	/**************************************************
	*��������Ϣ����Ϊexcel��ʽ�ı���
	**************************************************/
	function network_netflow_excel_report(){
		mainForm.action = "<%=rootPath%>/netreport.do?action=downloadnetnetflowreport";
    	mainForm.submit(); 
	}
	
</script>
		<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$("#netflow_table").tablesorter({
			sortList:[],
			widgets: ['zebra'],
			headers: { 
				0:{sorter: false}, 
				3:{sorter: false}
			}
		});//0Ϊ����    1����
	} ); 
</script>

	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">�鿴</td>
		</tr>
	</table>
	</div>
<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="id" value="<%=tmp%>">
			<input type=hidden name="flag" value=<%=_flag%>>

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
														<td>
															<table class="detail-data-body">
																<tr>
																	<td>
																		<table width="100%" border="0" cellpadding="0"
																			cellspacing="1">
																			<tr>
																				<td colspan=5 height="28" bgcolor="#ECECEC">
																					��ʼ����
																					<input type="text" name="startdate"
																						value="<%=startdate%>" size="10">
																					<a onclick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																						<img id=imageCalendar1 align=absmiddle width=34
																							height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0> </a>
																					��ֹ����
																					<input type="text" name="todate"
																						value="<%=todate%>" size="10" />
																					<a onclick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																						<img id=imageCalendar2 align=absmiddle width=34
																							height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0> </a>
																					ԴIP��ַ
																					<input type="text" name="srcip" value="<%=srcipstr%>"/>
																					<input type="button" name="submitss" value="��ѯ"
																						onclick="query()">
																					<a href="javascript:network_netflow_excel_report()"><img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL</a>
																				</td>
																			</tr>
																			<tr bgcolor="DEEBF7">
																				<td colspan=2>
																					<table id="netflow_table" class="tablesorter"
																						cellspacing="1" cellpadding="0" width="100%">
																						<thead>
																							<tr bgcolor="#ECECEC">
																								<th align="center" style="color: #000000;"
																									class="body-data-title" width='1%' align=left>
																									���
																								</th>
																								<th align="center" style="color: #000000;"
																									class="body-data-title" width='10%'>
																									ԴIP��ַ
																								</th>
																								<th align="center" style="color: #000000;"
																									class="body-data-title" width='10%'>
																									��������KBytes��
																								</th>
																								<th align="center" style="color: #000000;"
																									class="body-data-title" width='1%'>
																									����
																								</th>
																							</tr>
																						</thead>
																						<tbody>
																							<%
																								Vector flowV = new Vector();
																								Hashtable tempHash = new Hashtable();
																								IpAccountingBase vo = null;
																								for (int i = 0; i < flowlist.size(); i++) {
																									flowV = (Vector) flowlist.get(i);
																									double thevalue = (Double) flowV.get(0);
																									if (flowV.get(1) == null) {
																										continue;
																									}
																									vo = (IpAccountingBase) flowV.get(1);
																									if (tempHash.containsKey(vo.getSrcip())) {
																										double tempV = (Double) tempHash.get(vo.getSrcip());
																										tempV = tempV + thevalue;
																										tempHash.put(vo.getSrcip(), tempV);
																									} else {
																										tempHash.put(vo.getSrcip(), thevalue);
																									}
																								}
																								int i = 0;
																								session.setAttribute("netflowHash",tempHash);//���뻺��
																								if (tempHash != null && !tempHash.isEmpty()) {
																									Enumeration portEnu = tempHash.keys();
																									netflowdata = amChartTool.getNetNetFlowChart(tempHash);
																									
																									while (portEnu.hasMoreElements()) {
																										// �жϲɼ����Ķ˿���Ϣ�Ƿ��Ѿ��ڶ˿����ñ����Ѿ����ڣ��������������
																										String srcip = (String) portEnu.nextElement(); // portstr==>ip:index
																										double thevalue = (Double) tempHash.get(srcip);
																										thevalue = Double.parseDouble(CommonUtil.format(thevalue, 3));
																										i++;
																							%>
																							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
																								<td class="detail-data-body-list"  border="0"><%=i%></td>
																								<td class="detail-data-body-list"><%=srcip%></td>
																								<td class="detail-data-body-list"><%=thevalue%></td>
																								<td class="detail-data-body-list">
																									&nbsp;
																									<img src="<%=rootPath%>/resource/image/status.gif"
																										border="0" width=15 oncontextmenu=showMenu('2','<%=tmp%>','<%=srcip%>')>
																								</td>
																							</tr>
																							<%
																									}
																								}
																							%>
																						</tbody>
																					</table>
																					<table cellpadding="0" cellspacing="0" width=48% style="background-color: white;">
																						<tr><td>&nbsp;</td></tr>
																						<tr>
																							<td width="100%" align=center>
																								<div id="net_netflow">
																								</div>
																								<script type="text/javascript">
		                                                                                               <% if(netflowdata != null && netflowdata.length() > 0){ %>	
		                                                                                               var so = new SWFObject("<%=rootPath%>/amchart/amcolumn.swf", "ampie","860", "288", "8", "#FFFFFF");
		                                                                                               so.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                                               so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netnetflow_settings.xml"));
		                                                                                               so.addVariable("chart_data","<%=netflowdata%>"); 
		                                                                                               so.write("net_netflow");
		                                                                                               <%}else{%>
														                                               var _div=document.getElementById("net_netflow");
														                                               var img=document.createElement("img");
														                                               img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
														                                               _div.appendChild(img);
														                                               <%}%>
                                                                                            	</script>
																							</td>
																						</tr>
																						<tr>
																							<td width="100%" align=center>
																								<div id="net_netflow_line">
																								</div>
																								<script type="text/javascript">
		                                                                                               <% if(netflowlinedata != null && netflowlinedata.length() > 0){ %>	
		                                                                                               var soline = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","860", "288", "8", "#FFFFFF");
		                                                                                               soline.addVariable("path", "<%=rootPath%>/amchart/");
		                                                                                               soline.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netnetflow_line_settings.xml"));
		                                                                                               soline.addVariable("chart_data","<%=netflowlinedata%>"); 
		                                                                                               soline.write("net_netflow_line");
		                                                                                               <%}else{%>
														                                               var linediv=document.getElementById("net_netflow_line");
														                                               var lineimg=document.createElement("img");
														                                               linediv.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
														                                               linediv.appendChild(img);
														                                               <%}%>
                                                                                            	</script>
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
								<td class="td-container-main-tool" width=14%>
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
		<script type="text/javascript">
Ext.onReady(function()
{
Ext.get("bkpCfg").on("click",function(){
//Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
//mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=bkpCfg&ipaddress=<%=host.getIpAddress()%>&page=netarp&id="+<%=tmp%>;
//mainForm.submit();
window.open("<%=rootPath%>/vpntelnetconf.do?action=detailPage_readybkpCfg&ipaddress=<%=host.getIpAddress()%>&page=liusu&id="+<%=tmp%>,"oneping", "height=400, width= 500, top=300, left=100,scrollbars=yes");
});
});
</script>
	</BODY>
</HTML>