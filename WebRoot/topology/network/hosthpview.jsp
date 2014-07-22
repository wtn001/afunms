<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
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
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.detail.service.cpuInfo.CpuInfoService"%>
<%@page import="com.afunms.detail.service.diskInfo.DiskInfoService"%>
<%@page import="com.afunms.detail.service.OtherInfo.OtherInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.sysInfo.DiskPerfInfoService"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.detail.service.interfaceInfo.InterfaceInfoService"%>
<%@page import="com.afunms.detail.service.systemInfo.SystemInfoService"%>
<%@page import="com.afunms.detail.service.pingInfo.PingInfoService"%>

<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%
	String runmodel = PollingEngine.getCollectwebflag(); 
 	String rootPath = request.getContextPath();
 	String menuTable = (String)request.getAttribute("menuTable");
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	double cpuvalue = 0;
	String pingconavg ="0";
	String maxpingvalue="0";
	String avgresponse="0";
	String maxresponse="0";
	String collecttime = "";
	String sysuptime = ""; 
	String sysservices = "";
	String sysdescr = "";
	
  	HostNodeDao hostdao = new HostNodeDao();
  	List hostlist = hostdao.loadHost();
  	
  	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
  	String ipaddress = host.getIpAddress();
  	String orderflag = request.getParameter("orderflag");
  	if(orderflag == null || orderflag.trim().length()==0){
  		orderflag="index";
  	}
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
     
     I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
        
     Vector vector = new Vector();
     Vector cpuV = new Vector();
     Vector systemV = new Vector();
     Vector pingData = new Vector();
     String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
     String pingvalue= "0";
     String responsevalue="0";
     try{
       if("0".equals(runmodel)){
  			//采集与访问是集成模式
      		//vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
      		Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
      	if (ipAllData != null) {
				cpuV = (Vector) ipAllData.get("cpu");
				//system
				systemV = (Vector) ipAllData.get("system");
				//pingdata
				pingData = (Vector) ShareData.getPingdata().get(
						host.getIpAddress());
			}
      		
		if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			collecttime = sdf1.format(cc);
			pingvalue = pingdata.getThevalue();//当前连通率
			pingdata = (Pingcollectdata)pingData.get(1);
			responsevalue = pingdata.getThevalue();//当前响应时间
		}
      	}else{
     		//采集与访问是分离模式
    		NodeUtil nodeUtil = new NodeUtil();
       		NodeDTO nodedto = nodeUtil.creatNodeDTOByNode(host);
       		//cpu
       		CpuInfoService cpuInfoService = new CpuInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()); 
			cpuV = cpuInfoService.getCpuInfo();
			//得到系统启动时间
			SystemInfoService systemInfoService = new SystemInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype());
			systemV = systemInfoService.getSystemInfo(); 
       		//pingdata
       		//pingData = new PingInfoService(nodedto.getId()+"",nodedto.getType(),nodedto.getSubtype()).getPingInfo();
       		PingInfoService pingInfoService = new PingInfoService(String.valueOf(nodedto.getId()),nodedto.getType(),nodedto.getSubtype());
		    pingData  = pingInfoService.getPingInfo();  
		    if(pingData != null && pingData.size()>0){
			Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
			Calendar tempCal = (Calendar)pingdata.getCollecttime();							
			Date cc = tempCal.getTime();
			pingvalue = pingdata.getThevalue();//当前连通率
			pingdata = (Pingcollectdata)pingData.get(1);
			responsevalue = pingdata.getThevalue();//当前响应时间
		}
     	}
     }catch(Exception e){
     	e.printStackTrace();
     }
        
	
	//if(ipAllData != null){
		
	if(cpuV != null && cpuV.size()>0){
		CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
		if(cpu != null && cpu.getThevalue() != null){
			cpuvalue = new Double(cpu.getThevalue());
		}
	}
	//得到系统启动时间
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
			//进程数
			//if(systemdata.getSubentity().equalsIgnoreCase("ProcessCount")){
			//	processCount = systemdata.getThevalue();
			//}
			//if(systemdata.getSubentity().equalsIgnoreCase("SysName")){
			//	sysname = systemdata.getThevalue();
			//}
		}
	}
	//}
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
		maxpingvalue = (String)ConnectUtilizationhash.get("pingmax");
		maxpingvalue =maxpingvalue.replaceAll("%","");
	}
	//响应时间
	try{
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ResponseTime",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
	
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		avgresponse = (String)ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("毫秒", "").replaceAll("%","");
		maxresponse = (String)ConnectUtilizationhash.get("pingmax");
		maxresponse=maxresponse.replaceAll("%","");
	}
	//ping
	//pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
	if(pingData != null && pingData.size()>0){
		Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
		Calendar tempCal = (Calendar)pingdata.getCollecttime();							
		Date cc = tempCal.getTime();
		collecttime = sdf1.format(cc);
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

	String[] memoryItem={"AllSize","UsedSize","Utilization"};
	String[] memoryItemch={"总容量","已用容量","当前利用率","最大利用率"};
	String[] sysItem={"sysName","sysUpTime","sysContact","sysLocation","sysServices","sysDescr"};
	String[] sysItemch={"设备名","设备启动时间","设备联系","设备位置","设备服务","设备描述"};
	Hashtable hash = (Hashtable)request.getAttribute("hash");
	Hashtable hash1 = (Hashtable)request.getAttribute("hash1");
	Hashtable max = (Hashtable) request.getAttribute("max");
	Hashtable imgurl = (Hashtable)request.getAttribute("imgurl");
	
	double avgpingcon = (Double)request.getAttribute("pingconavg");
	
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	
	int cpuper = Double.valueOf(cpuvalue).intValue();

  //String rootPath = request.getContextPath(); 
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  //ResponseTimeItem item1 = (ResponseTimeItem)host.getMoidList().get(0); 
  String chart1 = null,chart2 = null,chart3 = null,responseTime = null;
  DefaultPieDataset dpd = new DefaultPieDataset();
  dpd.setValue("可用率",avgpingcon);
  dpd.setValue("不可用率",100 - avgpingcon);
  chart1 = ChartCreator.createPieChart(dpd,"",130,130);  
  
  //if(item1.getSingleResult()!=-1)
  //{
     //responseTime = item1.getSingleResult() + " ms";
  
     //SnmpItem item2 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_CPU);
     //if(item2!=null&&item2.getSingleResult()!=-1)
         //chart2 = ChartCreator.createMeterChart(item2.getSingleResult(),"",150,150); 
      chart2 = ChartCreator.createMeterChart(cpuvalue,"",120,120);   

  //SnmpItem item3 = (SnmpItem)host.getItemByMoid(MoidConstants.CISCO_MEMORY);
  //if(item3!=null&&item3.getSingleResult()!=-1)
     chart3 = ChartCreator.createMeterChart(40.0,"",120,120);       
  //}
  //else
     //responseTime = "无响应"; 
     
    Vector ifvector = (Vector)request.getAttribute("vector"); 	
     
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
    String ip = host.getIpAddress();
	String picip = CommonUtil.doip(ip);
	
    	//生成连通率图形
    Double realValue=Double.valueOf(pingvalue.replaceAll("%",""));
	CreatePiePicture _cpp = new CreatePiePicture();
	_cpp.createPingPic(picip,realValue);                      //当前连通率
	_cpp.createAvgPingPic(picip,avgpingcon);                  //平均连通率
	_cpp.createMinPingPic(picip,maxpingvalue.replace("%",""));//最小连通率
	
		 //生成CPU仪表盘
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createCpuPic(picip,cpuper);	  
	
	//生成响应时间图形
	CreateBarPic cbp=new CreateBarPic();
	cbp.createResponseTimePic(picip,responsevalue,maxresponse,avgresponse);
     
     //amchar 连通率
		 StringBuffer dataStr1 = new StringBuffer();
		 	dataStr1.append("连通;").append(Math.round(realValue)).append(";false;7CFC00\\n");
		 	dataStr1.append("未连通;").append(100-Math.round(realValue)).append(";false;FF0000\\n");
		 	String realdata = dataStr1.toString();
		 			StringBuffer dataStr2 = new StringBuffer();
		 	dataStr2.append("连通;").append(Math.round(avgpingcon)).append(";false;7CFC00\\n");
		 	dataStr2.append("未连通;").append(100-Math.round(avgpingcon)).append(";false;FF0000\\n");
		 	String avgdata = dataStr2.toString();
		 			StringBuffer dataStr3 = new StringBuffer();
		 			String maxping=maxpingvalue.replaceAll("%", "");
		 	dataStr3.append("连通;").append(Math.round(Float.parseFloat(maxping))).append(";false;7CFC00\\n");
		 	dataStr3.append("未连通;").append(100-Math.round(Float.parseFloat(maxping))).append(";false;FF0000\\n");
		 	String maxdata = dataStr3.toString();			  	   
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
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
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=ajaxUpdate_availability&tmp=<%=tmp%>&nowtime="+(new Date()),
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
  	location.href="<%=rootPath%>/detail/dispatcher.jsp?id=net<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
	        rowObjs[i].cells[1].onmouseover=function()
	        {
	            this.style.color="blue";
	        }
	        //设置鼠标滑出该行时的效果
	        rowObjs[i].cells[1].onmouseout=function(){
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
	    var x=event.clientX;
	    var y=event.clientY;
	    var w=$(window).width();
	    var h=$(window).height();
	    if((x+width)>=w){
        	x=w-width-5;
       	}
       	if((y+rowCount*25)>=h){
       		y=h-rowCount*25-5;
       	}
	    pop.show(x+2,y+2,width,rowCount*25,document.body);
	    return true;
	}
	function detail()
	{
	    //location.href="<%=rootPath%>/detail/dispatcher.jsp?id="+node;
	    window.open ("<%=rootPath%>/monitor.do?action=show_hostutilhdx&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+node+"&ifname="+ipaddress, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
	}
	function portset()
	{
		window.open ("<%=rootPath%>/panel.do?action=show_portreset&ipaddress=<%=host.getIpAddress()%>&ifindex="+node, "newwindow", "height=200, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes");
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
	document.getElementById('hpunixDetailTitle-0').className='detail-data-title';
	document.getElementById('hpunixDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('hpunixDetailTitle-0').onmouseout="this.className='detail-data-title'";
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


</head>
<body id="body" class="body" onload="initmenu();">

<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr><td style="border-right:#ccc 1px solid ;border-top-style: none;" width="5%" align="center"><img src="<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif"></td>
			<td style="cursor: default; border: outset 0;padding-left='5px'"
				onclick="parent.detail()">查看状态</td>
		</tr>
		<tr><td style="border-right:#ccc 1px solid;border-top-style: none;" width="5%" align="center"><img src="<%=rootPath%>/common/images/xingneng.png"></td>
			<td style="cursor: default; border: outset 0;padding-left='5px'"
				onclick="parent.portset();">端口设置</td>
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
					                    <td class="layout_title" align="center"><b >设备详细信息</b></td>
					                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
				                     </tr>
			             	          <tr>
				                       <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;主机名:
										        <span id="lable"><%=host.getAlias()%></span> </td>
                                       <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;系统名称:
						 					<span id="sysname"><%=host.getSysName()%></span></td>
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
									<tr>
										<td>
											<table id="detail-data" class="detail-data" style="margin-left: 10px;">
												<tr>
											    	<td class="detail-data-header">
											    		<%=hpunixDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
												      		<tr>
												      			<td>
												      				<table>
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
																								var so = new SWFObject("<%=rootPath%>/flex/Ping.swf?ipadress=<%=host.getIpAddress()%>", "Ping", "346", "180", "8", "#ffffff");
																								so.write("flashcontent3");
																							</script>				
																		                </td>
																		                </tr>
																		                <tr>
																	<td width=100% align=center>
																		<table cellpadding="1" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td height="29" class="detail-data-body-title" style="height:29">名称</td>
																				<td class="detail-data-body-title" style="height:29">当前（%）</td>
																				<td class="detail-data-body-title" style="height:29">最小（%）</td>
																				<td class="detail-data-body-title" style="height:29">平均（%）</td>
																			</tr>
																			<tr>
																				<td class="detail-data-body-list" height="29">连通率</td>
																				<td class="detail-data-body-list"><%=pingvalue%></td>
																				<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(maxpingvalue.replaceAll("%","")))%></td>
																				<td class="detail-data-body-list"><%=Math.round(Float.parseFloat(pingconavg.replaceAll("%","")))%></td>
																			</tr>
																		</table>
																	</td>
								</tr>
																					           
																				</table> 
														                  	</td>
														                  	<td align=center>
                       <br>
                      <table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>连通率实时</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        										<table  cellpadding="1" cellspacing="1" width=48%>
								<tr>
									<td valign=top>
			<table width="90%" cellpadding="0" cellspacing="0"  border=0>
				<tr>
			      	<td valign=top>
			      	<div id="realping">
					   <strong>You need to upgrade your Flash Player</strong>
					</div>
					<script type="text/javascript"
						 src="<%=rootPath%>/include/swfobject.js"></script>
						  <script type="text/javascript">
						 var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						   so.addVariable("path", "<%=rootPath%>/amchart/");
						   so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						   so.addVariable("chart_data","<%=realdata%>");
						   so.write("realping");
					</script>
			 <!--   <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>realping.png">--> 
				</td>
				<td valign=top>
				<div id="maxping">
					 <strong>You need to upgrade your Flash Player</strong>
			    </div>
					<script type="text/javascript"
						 src="<%=rootPath%>/include/swfobject.js"></script>
					<script type="text/javascript">
						var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						    so.addVariable("path", "<%=rootPath%>/amchart/");
						    so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						    so.addVariable("chart_data","<%=maxdata%>");
						    so.write("maxping");
					</script>
			 <!--	<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>minping.png">--> 
				</td>
				<td valign=top>
				<div id="avgping">
				  <strong>You need to upgrade your Flash Player</strong>
				</div>
				<script type="text/javascript"
							       src="<%=rootPath%>/include/swfobject.js"></script>
					<script type="text/javascript">
						  var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf", "ampie","165", "165", "8", "#FFFFFF");
						      so.addVariable("path", "<%=rootPath%>/amchart/");
						      so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/pingStatepie.xml"));
						      so.addVariable("chart_data","<%=avgdata%>");
						      so.write("avgping");
					</script>
			 <!--	<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingavg.png">--> 
				</td>
				</tr>
				<tr height=20>
			      	<td valign=top align=center><b>当前</b></td>
				<td valign=top align=center><b>最小</b></td>
				<td valign=top align=center><b>平均</b></td>
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
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											                  			<tr>
											                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
											                    			<td></td>
											                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
														                  	<tr>
														                  	<td align=center>
																            <br>
																                <table cellpadding="0" cellspacing="0" width=48%>
											              							<tr> 
											                							<td width="100%"  align=center> 
											                								<div id="flashcontent5">
																								<strong>You need to upgrade your Flash Player</strong>
																							</div>
																							<script type="text/javascript">
																								var so = new SWFObject("<%=rootPath%>/flex/Response_time.swf?ipadress=<%=host.getIpAddress()%>", "Response_time", "346", "180", "8", "#ffffff");
																								so.write("flashcontent5");
																							</script>				
																		                </td>
																					</tr> 
																					<tr>
																	<td width=100% align=center>
																		<table cellpadding="1" cellspacing="1" style="align:center;width:346" bgcolor="#FFFFFF">
																			<tr bgcolor="#FFFFFF">
																				<td class="detail-data-body-title" style="height:29" height="29">名称</td>
																				<td class="detail-data-body-title" style="height:29">当前（ms）</td>
																				<td class="detail-data-body-title" style="height:29">最大（ms）</td>
																				<td class="detail-data-body-title" style="height:29">平均（ms）</td>
																			</tr>
																			<tr>
																				<td class="detail-data-body-list" height="29">响应时间</td>
																				<td class="detail-data-body-list"><%=responsevalue%></td>
																				<td class="detail-data-body-list"><%=maxresponse%></td>
																				<td class="detail-data-body-list"><%=avgresponse%></td>
																			</tr>
																		</table>
																	</td>
																	</tr>            
																				</table> 
																            </td>
																            <td align=center>
														            	<br>
<table id="body-container" class="body-container" style="background-color:#FFFFFF;" width="40%">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title"><b>响应时间详情</b></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        										<table  cellpadding="1" cellspacing="1" width=48%>
              							
																<tr>
																	<td valign=top>
									   
										

																	<table cellpadding="0" cellspacing="0" width=48% align=center>
																	<tr>
																	<td align=center colspan=3>
																		<img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>response.png">	
																	</td>
																	</tr> 
																	<tr height=30>
																      	<td valign=top align=center><b>当前</b></td>
																	<td valign=top align=center><b>最大</b></td>
																	<td valign=top align=center><b>平均</b></td>
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
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
				        									<tr>
				        										<td>
				        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
											                  			<tr>
											                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
											                    			<td></td>
											                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
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
										</td>
										
										
										
									</tr>
								</table>
							</td>
							<td class="td-container-main-tool">
								<jsp:include page="/include/toolbar.jsp">
									<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress"/>
									<jsp:param value="<%=host.getSysOid()%>" name="sys_oid"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="host" name="category"/>
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