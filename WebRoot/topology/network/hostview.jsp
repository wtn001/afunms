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
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@page import="com.afunms.temp.model.NodeTemp"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%
	String runmodel = PollingEngine.getCollectwebflag();
  	String rootPath = request.getContextPath();
  	String menuTable = (String)request.getAttribute("menuTable");
  	String tmp = request.getParameter("id"); 
  	String flag1 = request.getParameter("flag");
  	String url = rootPath+"/monitor.do?action=ajaxUpdate_usage&id="+tmp;
  	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  	
	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = "";
	String sysuptime = "";
	String sysservices = "";
	String sysdescr = "";
	String vvalue = "0";
	String pvalue = "0";
	float capvalue = 0f;
	float fvvalue = 0f;
	String vused = "0";
	
	String responsevalue = "";
	String pingvalue= "";
	String syslocation = "";
	String pingcollecttime = "";
	String memcollecttime = "";
	int cpuper =0;
	
	Vector memoryVector = new Vector();
	
	Vector ifvector = new Vector();
    	
    	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
    	if(host == null){
    		//�����ݿ����ȡ
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
    	
    	String ipaddress = host.getIpAddress();
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
        String starttime = time[0];
        String endtime = time[1];
        
        if("0".equals(runmodel)){
        	//�ɼ�������Ǽ���ģʽ
        	I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
	        String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdxPerc","InBandwidthUtilHdxPerc","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
	        try{
	        	ifvector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
			if(ipAllData != null){
				Vector cpuV = (Vector)ipAllData.get("cpu");
				if(cpuV != null && cpuV.size()>0){
					CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
					if(cpu != null && cpu.getThevalue() != null){
						cpuvalue = new Double(cpu.getThevalue());
					}
				}
				//�õ�ϵͳ����ʱ��
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
					}
				}
				//�õ��ڴ�������
				
				memoryVector = (Vector)ipAllData.get("memory");
				if(memoryVector != null && memoryVector.size()>0){
					for(int i=0;i<memoryVector.size();i++){
						Memorycollectdata memorydata=(Memorycollectdata)memoryVector.get(i);
						if("VirtualMemory".equalsIgnoreCase(memorydata.getSubentity()) && "Utilization".equalsIgnoreCase(memorydata.getEntity())) {
							vvalue = Math.round(Float.parseFloat(memorydata.getThevalue()))+"";
							fvvalue = Float.parseFloat(memorydata.getThevalue());
						}else if("PhysicalMemory".equalsIgnoreCase(memorydata.getSubentity()) && "Utilization".equalsIgnoreCase(memorydata.getEntity())) {
							pvalue = Math.round(Float.parseFloat(memorydata.getThevalue()))+"";
						}
						if("VirtualMemory".equalsIgnoreCase(memorydata.getSubentity()) && "Capability".equalsIgnoreCase(memorydata.getEntity())) {
							capvalue = Float.parseFloat(memorydata.getThevalue());
						}
					}
					DecimalFormat df=new DecimalFormat("#.##");
					vused = df.format(capvalue*fvvalue/100)+"";
				}
				Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
				if(pingData != null && pingData.size()>0){
					Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
					Calendar tempCal = (Calendar)pingdata.getCollecttime();							
					Date cc = tempCal.getTime();
					collecttime = sdf1.format(cc);
				}
			}
        }else{
        	//�ɼ�������Ƿ���ģʽ
        	List systemList = new ArrayList();
		List pingList = new ArrayList();
		List memoryList = new ArrayList();
		List flashList = new ArrayList();
		I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
	        String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdxPerc","InBandwidthUtilHdxPerc","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
	        try{
	        	ifvector = hostlastmanager.getInterface(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
		try{
			systemList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getSystemInfo();
			pingList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrPingInfo();
			cpuvalue = new Double(new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrCpuAvgInfo());
			cpuper = Double.valueOf(cpuvalue).intValue();;
			memoryList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrMemoryInfo();
			//flashList = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrFlashInfo();
			//pingconavg = new NetService(host.getId()+"", nodedto.getType(), nodedto.getSubtype()).getCurrDayPingAvgInfo();
		}catch(Exception e){
		}
		if(memoryList != null && memoryList.size()>0){
			for(int i=0;i<memoryList.size();i++){
				Memorycollectdata memorycollectdata = new Memorycollectdata();
				NodeTemp nodetemp = (NodeTemp)memoryList.get(i);
				if(i ==0){
					SimpleDateFormat _sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d = _sdf1.parse(nodetemp.getCollecttime());
					//Calendar c = Calendar.getInstance();
					//c.setTime(d);
					_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
					memcollecttime = _sdf1.format(d);
				}
				memorycollectdata.setSubentity(nodetemp.getSindex());
				memorycollectdata.setEntity(nodetemp.getSubentity());
				memorycollectdata.setThevalue(nodetemp.getThevalue());
				memoryVector.add(memorycollectdata);
			}
		}
		//�õ��ڴ�������
		if(memoryVector != null && memoryVector.size()>0){
			for(int i=0;i<memoryVector.size();i++){
				Memorycollectdata memorydata=(Memorycollectdata)memoryVector.get(i);
				if("VirtualMemory".equalsIgnoreCase(memorydata.getSubentity()) && "Utilization".equalsIgnoreCase(memorydata.getEntity())) {
					vvalue = Math.round(Float.parseFloat(memorydata.getThevalue()))+"";
					fvvalue = Float.parseFloat(memorydata.getThevalue());
				}else if("PhysicalMemory".equalsIgnoreCase(memorydata.getSubentity()) && "Utilization".equalsIgnoreCase(memorydata.getEntity())) {
					pvalue = Math.round(Float.parseFloat(memorydata.getThevalue()))+"";
				}
				if("VirtualMemory".equalsIgnoreCase(memorydata.getSubentity()) && "Capability".equalsIgnoreCase(memorydata.getEntity())) {
					capvalue = Float.parseFloat(memorydata.getThevalue());
				}
			}
			DecimalFormat df=new DecimalFormat("#.##");
			vused = df.format(capvalue*fvvalue/100)+"";
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
					pingvalue = nodetemp.getThevalue();
					SimpleDateFormat _sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date d = _sdf1.parse(nodetemp.getCollecttime());
					//Calendar c = Calendar.getInstance();
					//c.setTime(d);
					_sdf1 = new SimpleDateFormat("MM-dd HH:mm");
					pingcollecttime = _sdf1.format(d);
				}
				if("ResponseTime".equals(nodetemp.getSindex())){
					responsevalue = nodetemp.getThevalue();
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
	
	//----�õ���Ӧʱ�� 
	String avgresponse = "0";//-----------
	String maxresponse = "0";//----------- 
	
	try{
		ConnectUtilizationhash = hostmanager.getCategory(host.getIpAddress(),"Ping","ResponseTime",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		avgresponse = (String)ConnectUtilizationhash.get("avgpingcon");
		avgresponse = avgresponse.replace("����", "").replaceAll("%","");
		maxresponse = (String)ConnectUtilizationhash.get("pingmax");
		maxresponse=maxresponse.replaceAll("%","");
	}
	
	Hashtable hash = (Hashtable)request.getAttribute("hash");
	
	double avgpingcon = (Double)request.getAttribute("pingconavg");
	
	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	
	cpuper = Double.valueOf(cpuvalue).intValue();  
  
 
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
    	//CommonUtil commutil = new CommonUtil();
	String picip = CommonUtil.doip(ip);
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
	        _titleModel.setPicName(picip+"pingavg");
	        _titleModel.setTopTitle("");
	        
	        double[] _data1 = {avgpingcon, 100-avgpingcon};
	        //double[] _data2 = {77, 87};
	        String[] p_labels = {"��ͨ", "δ��ͨ"};
	        int[] _colors = {0x66ff66, 0xff0000}; 
	        String _title1="��һ����";
	        String _title2="�ڶ�����";
	        _cpp.createOneRingChart(_data1,p_labels,_colors,_titleModel);
	        
	   //����CPU�Ǳ���
		CreateMetersPic cmp = new CreateMetersPic();
		cmp.createCpuPic(picip, cpuper); //����CPU�Ǳ���
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script src="<%=rootPath%>/include/AC_OETags.js" language="javascript"></script>
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
  	location.href="<%=rootPath%>/monitor.do?action=hostping&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&flag=<%=flag1%>&orderflag="+para;
} 
function openwin3(operate,index,ifname) 
{	
        window.open ("<%=rootPath%>/monitor.do?action="+operate+"&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&ifindex="+index+"&ifname="+ifname, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
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
	        rowObjs[i].cells[1].onmouseover=function()
	        {
	            this.style.color="blue";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[1].onmouseout=function(){
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
	function realtimeMonitor()
	{
		window.open ('<%=rootPath%>/monitor.do?action=portdetail&id=<%=tmp%>&ip=<%=host.getIpAddress()%>&ifindex='+node,'�˿�����','top=200,left=300,height=435,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
	}	
	function bandwidth()
	{
		window.open ('<%=rootPath%>/monitor.do?action=bandwidthdetail&id=<%=tmp%>&ip=<%=host.getIpAddress()%>&ifindex='+node,'����','top=200,left=300,height=435,width=660, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
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
</script>


</head>
<body id="body" class="body" onload="initmenu();">
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr><td style="border-right:#ccc 1px solid ;border-top-style: none;" width="5%" align="center"><img src="<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif"></td>
			<td style="cursor: default; border: outset 0;padding-left='5px'"
				onclick="parent.detail()">�鿴״̬</td>
		</tr>
			
		<tr><td style="border-right:#ccc 1px solid;border-top-style: none;" width="5%" align="center"><img src="<%=rootPath%>/common/images/xingneng.png"></td>
			<td style="cursor: default; border: outset 0;padding-left='5px'"
				onclick="parent.portset();">�˿�����</td>
		</tr>	
		<tr><td style="border-right:#ccc 1px solid;border-top-style: none;" width="5%" align="center"><img src="<%=rootPath%>/common/images/arrows_0.gif"></td>
			<td style="cursor: default; border: outset 0;padding-left='5px'" align="left"
				onclick="parent.realtimeMonitor();">ʵʱ���</td>
		</tr>
			
		<tr><td style="border-right:#ccc 1px solid;border-top-style: none;" width="5%" align="center"><img src="<%=rootPath%>/common/images/ziyuan.png"></td>
			<td style="cursor: default; border: outset 0;padding-left='5px'"
				onclick="parent.bandwidth();">����</td>
		</tr>
		<tr><td style="border-right:#ccc 1px solid;border-top-style: none;" width="5%" align="center"><img src="<%=rootPath%>/common/images/3D.png"></td>
			<td style="cursor: default; border: outset 0;padding-left='5px'"
				onclick="parent.portstatus();">�˿�״̬</td>
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
							<td class="td-container-main-detail" width=85%>
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
				 <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;�豸��ǩ:
										<!--  	<td width="70%"><%=host.getAlias()%> [<a href="<%=rootPath%>/network.do?action=ready_editalias&id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>"><img src="<%=rootPath%>/resource/image/editicon.gif" border=0>�޸�</a>]</td>-->
										        <span id="lable"><%=host.getAlias()%></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;ϵͳ����:
											<span id="sysname"><%=host.getSysName()%></span></td>
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP��ַ:
											<%
												 IpAliasDao ipdao = new IpAliasDao();
												 List iplist = ipdao.loadByIpaddress(host.getIpAddress());
												 ipdao.close();
												 ipdao = new IpAliasDao();
												 IpAlias ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(),"1");
												 ipdao.close();
												 if(iplist == null)iplist = new ArrayList(); %>
											
												<select name="ipalias<%=host.getIpAddress() %>">
													<option selected><%=host.getIpAddress() %></option>
													<% 
													   for(int j=0 ;j<iplist.size() ; j++){
																IpAlias voTemp = (IpAlias)iplist.get(j); 
															%>
															<option <%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>selected<%} %>><%=voTemp.getAliasip() %></option>
													<%} %>
												</select>
												[
												<a href="#"  style="cursor:hand" onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">�޸�</a>
												]
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
											    		<%=hostDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
												      		<tr>
												      			<td>
											    		<table width="100%" border="0" cellpadding="0" cellspacing="1" bgcolor="#FFFFFF">
  <tr align="center"> 
    <td rowspan="2" width="6%" align="center" bgcolor="#ECECEC">����</td>
    <td rowspan="2" width="9%" align="center" bgcolor="#ECECEC">����</td>
    <td rowspan="2" width="9%" align="center" bgcolor="#ECECEC">����Ӧ��</td>
    <td rowspan="2" width="11%" align="center" bgcolor="#ECECEC">����(kb/s)</td>
    <td rowspan="2" width="9%" align="center" bgcolor="#ECECEC">��ǰ״̬</td>
    <td colspan="2" height="23" bgcolor="#ECECEC">����������</td>
    <td height="23" colspan="2" align="center" bgcolor="#ECECEC">����</td>
    <td rowspan="2"  align="center" align="center" bgcolor="#ECECEC">�鿴����</td>
    <!--<td rowspan="2" width="9%" align="center">�鿴���</td>-->
  </tr>
  <tr> 
    <td   align="center" width="8%" bgcolor="#ECECEC"><input  name="button1" type="button" value="���ڴ���" onclick="changeOrder('OutBandwidthUtilHdxPerc')"></td>
    <td   align="center" width="8%" bgcolor="#ECECEC"><input  name="button2" type="button" value="��ڴ���" onclick="changeOrder('InBandwidthUtilHdxPerc')"></td>
    <td   align="center" width="8%" bgcolor="#ECECEC"><input type=button  name="button3" styleClass="button" value="��������" onclick="changeOrder('OutBandwidthUtilHdx')"></td>
    <td   align="center"  width="8%" bgcolor="#ECECEC"><input type=button  name="button4" styleClass="button" value="�������" onclick="changeOrder('InBandwidthUtilHdx')"></td>
  </tr>
             <%
             int[]  width = {6,18,11,10,10,8,8,8,8};
             for(int i=0 ;i<ifvector.size() ; i++){
                String[] strs = (String[])ifvector.get(i);
                String ifname = strs[1];
                String index = strs[0];
             %>
            <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25 class="detail-data-body-list">
            <%for(int j=0 ; j<strs.length ; j++){
               if(j==3){
                 String status = strs[j];
                 if(status.equals("up")){
                   url =rootPath+"/resource/image/topo/up.gif";
                 }
                 else if(status.equals("down")){
                   url =rootPath+"/resource/image/topo/down.gif";
                 }
                 else {
                   url =rootPath+"/resource/image/topo/testing.gif";
                 }
            %>
               <td width="<%=width[j]%>%" align="center" class="detail-data-body-list"><img src="<%=url%>"></td>
            <%
              }
            else{
            	if (j==1){
            	if (hash != null && hash.size()>0){
            		String linkuse="";
            		if (hash.get(ipaddress+":"+index)!= null)
            			linkuse = (String)hash.get(ipaddress+":"+index); 
            		%>
              			<td width="<%=width[j]/2%>%" class="detail-data-body-list"><%=strs[j]%></td>
              			<td width="<%=width[j]/2%>%" class="detail-data-body-list"><%=linkuse%></td>            		
              		<%            		
            	}else{
            	%>
              			<td width="<%=width[j]/2%>%" class="detail-data-body-list"><%=strs[j]%></td>
              			<td width="<%=width[j]/2%>%" class="detail-data-body-list"></td>            		            	
            	<%
            	}
              }else{
              %>
              <td width="<%=width[j]%>%" class="detail-data-body-list"><%=strs[j]%></td>
              <%
              }
              }
            }%>
    <td  width="5%"align="center" class="detail-data-body-list">
    
    &nbsp;<img src="<%=rootPath%>/resource/image/status.gif" border="0" width="15" oncontextmenu="showMenu('2','<%=index%>','111')">
    
    </td>
    <!--<td  width="9%"align="center"><a  style="cursor:hand" onclick="openwin3('read_about','<%=index%>','<%=ifname%>')">�鿴���</a></td>-->
            </tr>
            <%}%>            
                      
                      
                      
                      
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
							<%
								String subtype="windows";
								if(host.getSysOid().indexOf("1.3.6.1.4.1.2021")>=0 || host.getSysOid().indexOf("1.3.6.1.4.1.8072")>=0){
									subtype="linux";
								}
							%>
							<td class="td-container-main-tool" width=15%>
								<jsp:include page="/include/toolbar.jsp">
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