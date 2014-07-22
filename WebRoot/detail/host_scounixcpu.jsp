<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@include file="/include/globe.inc"%>
<%@include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.common.util.*" %>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@ page import="com.afunms.polling.om.*"%>
<%@ page import="com.afunms.config.model.Nodeconfig"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%
  	String menuTable = (String)request.getAttribute("menuTable");
	Hashtable max = (Hashtable) request.getAttribute("max");
  	String tmp = request.getParameter("id"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	double cpuvalue = 0;
	String pingconavg ="0";
	String collecttime = null;  
	String sysuptime = null;
	String sysservices = null;
	String sysdescr = null;
	List cpuperflist = new ArrayList();
	String hostname = "";
	String sysname = "";
	String CSDVersion = "";
	String mac = "";
		
    	Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp)); 
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
        
        I_HostLastCollectData hostlastmanager=new HostLastCollectDataManager();
        
        Vector vector = new Vector();
        String[] netInterfaceItem={"index","ifDescr","ifSpeed","ifOperStatus","OutBandwidthUtilHdx","InBandwidthUtilHdx"};
        try{
        	vector = hostlastmanager.getInterface_share(host.getIpAddress(),netInterfaceItem,orderflag,starttime,endtime); 
        }catch(Exception e){
        	e.printStackTrace();
        }
        Hashtable pefcpuhash = new Hashtable();
        List alldiskperf = new ArrayList();
        List alldiskio = new ArrayList();
        
        Nodeconfig nodeconfig = new Nodeconfig();
        String processornum = "";
	    Hashtable ipAllData = (Hashtable)ShareData.getSharedata().get(host.getIpAddress());
	    if(ipAllData != null){
		    nodeconfig= (Nodeconfig)ipAllData.get("nodeconfig");
		    if(nodeconfig != null){
			    mac = nodeconfig.getMac();
			    processornum = nodeconfig.getNumberOfProcessors();
			    CSDVersion = nodeconfig.getCSDVersion();
			    hostname = host.getAlias();//nodeconfig.getHostname();
		    }
		cpuperflist = (List)ipAllData.get("cpuperflist");
		if(cpuperflist != null && cpuperflist.size()>0){
			pefcpuhash = (Hashtable)cpuperflist.get(0);
		}
		
		alldiskperf = (List)ipAllData.get("alldiskperf");
		if(alldiskperf == null)alldiskperf = new ArrayList();
		alldiskio = (List)ipAllData.get("alldiskio");
		if(alldiskio == null)alldiskio = new ArrayList();
		Vector cpuV = (Vector)ipAllData.get("cpu");
		if(cpuV != null && cpuV.size()>0){
			CPUcollectdata cpu = (CPUcollectdata)cpuV.get(0);
			cpuvalue = new Double(cpu.getThevalue());
		}
		
		
		//�õ����ݲɼ�ʱ��
		collecttime = (String)ipAllData.get("collecttime");
		
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
				if(systemdata.getSubentity().equalsIgnoreCase("SysName")){
					sysname = systemdata.getThevalue();
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
		
		
	Vector pingData = (Vector)ShareData.getPingdata().get(host.getIpAddress());
	if(pingData != null && pingData.size()>0){
		Pingcollectdata pingdata = (Pingcollectdata)pingData.get(0);
		Calendar tempCal = (Calendar)pingdata.getCollecttime();							
		Date cc = tempCal.getTime();
		collecttime = sdf1.format(cc);
	}
	
	String[] diskItem={"AllSize","UsedSize","Utilization"};
	Hashtable Disk = (Hashtable)request.getAttribute("Disk");
	String newip = (String)request.getAttribute("newip");
		
        request.setAttribute("vector", vector);
        request.setAttribute("id", tmp);
        request.setAttribute("ipaddress", host.getIpAddress());
	request.setAttribute("cpuvalue", cpuvalue);
	request.setAttribute("collecttime", collecttime);
	request.setAttribute("sysuptime", sysuptime);
	request.setAttribute("sysservices", sysservices);
	request.setAttribute("sysdescr", sysdescr);
	request.setAttribute("pingconavg", new Double(pingconavg));

	double avgpingcon = (Double)request.getAttribute("pingconavg");

	int percent1 = Double.valueOf(avgpingcon).intValue();
	int percent2 = 100-percent1;
	
	int cpuper = Double.valueOf(cpuvalue).intValue();
	
  	String rootPath = request.getContextPath(); 
  
	Vector ipmacvector = (Vector)request.getAttribute("vector");
	if (ipmacvector == null)ipmacvector = new Vector();  
  
  	DefaultPieDataset dpd = new DefaultPieDataset();
  	dpd.setValue("������",avgpingcon);
  	dpd.setValue("��������",100 - avgpingcon);
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
	String subtype="";
	if("scoopenserver".equals(host.getSysOid())) {
		subtype = "scoopenserver";
	}else if("scounixware".equals(host.getSysOid())){
		subtype = "scounixware";
	}
	
     				  	   
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>


<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
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
				$("#flashcontent00gzm").html(data.percent1+":"+data.percent2+":"+data.cpuper);
				var so = new SWFObject("<%=rootPath%>/flex/Pie_Component.swf?percent1="+data.percent1+"&percentStr1=����&percent2="+data.percent2+"&percentStr2=������", "Pie_Component", "160", "160", "8", "#ffffff");
				so.write("flashcontent00");
				var so1 = new SWFObject("<%=rootPath%>/flex/DHCCGauge.swf?percent="+data.cpuper, "Pie_Component1", "160", "160", "8", "#ffffff");
				so1.write("flashcontent01");
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
  	location.href="<%=rootPath%>/detail/host_linuxutilhdx.jsp?id=<%=tmp%>&ipaddress=<%=host.getIpAddress()%>&orderflag="+para;
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
	*width:����ʾ�Ŀ���
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
	document.getElementById('scounixDetailTitle-2').className='detail-data-title';
	document.getElementById('scounixDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('scounixDetailTitle-2').onmouseout="this.className='detail-data-title'";
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
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">�鿴״̬</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.portset();">�˿�����</td>
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
											<table id="container-main-detail" class="container-main-detail" >
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
												[<a href="#" style="cursor: hand" onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">�޸�</a>]
											</TD>
				                           </tr>
			                              </table>
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
											    		<%=scounixDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											    	<td>
													<table class="detail-data-body">
												      		  <tr>
                                                                <td align=center >
											    	
                  
                                                        		  <% String str1 = "",str2="";
                  if(max.get("avgpingcon")!=null){
                          str2 = (String)max.get("avgpingcon");
                  }
                  %>                 
                <table width="100%" height="1" border="0" cellpadding="0" cellspacing="0">
<!-------------->

 <tr>
																<td align=center colspan=2 style="width:80%">
																    	<br>
                   															<table style="width:80%" border=1>
														                		<tr>
															                		<td width=80%>
																				<table width="80%">
														                					<tr>
																	                			<td align="left" height=40 bgcolor="#ECECEC">&nbsp;<B>����������</B></td>											   
																					</tr>
															       					</table>
														       					</td>
														       				</tr>
																       		<tr>
																       			<td width="80%">
																       				<table width="80%">
				                   															<tr>
																						<td align=center>
																							<img src="<%=rootPath%>/resource/image/jfreechart/<%=newip %>disk.png"/>
																						</td>
																					</tr> 
				                    															<tr>
				                  												        			<td align=center width=80%>
																            		    			<br>
																                	        			<table cellpadding="0" cellspacing="0">
												              							        			<tr> 
												                							        			<td align=center width=80%> 
												                								        			<div id="loading">
																										<div class="loading-indicator">
																											<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
																										</div>
																	                                    					<table border="1" align=center>
																											<tr>
																												<td>����ʹ�����</td><td>������</td><td>������</td><td>��������</td><td>������</td>
																											</tr>
																														<%
																														if (Disk != null && Disk.size() > 0) {
																															// д����
																															for (int i = 0; i < Disk.size(); i++) {
																													    %>
																													            <tr>
																														            <td>&nbsp;</td>
																																	<%
																																	Hashtable diskhash = (Hashtable) (Disk.get(new Integer(i)));
																																	String name = (String) diskhash.get("name");
																																	%>
																														            <td><%=name %></td>
																																	<%
																																	for (int j = 0; j < diskItem.length; j++) {
																																		String value = "";
																																		if (diskhash.get(diskItem[j]) != null) {
																																			value = (String) diskhash.get(diskItem[j]);
																																		}
																																	%>
																														            <td><%=value %></td>
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
		                <table cellpadding="0" cellspacing="0" width=92%>
		         		    <tr> 
		           			    <td width="100%"  align=center> 
		           				    <div id="flashcontent6">
										<strong>You need to upgrade your Flash Player</strong>
									</div>
									<script type="text/javascript">
										var so = new SWFObject("<%=rootPath%>/flex/Area_Disk_month.swf?ipadress=<%=host.getIpAddress()%>&id=2", "Area_Disk_month", "750", "400", "8", "#ffffff");
										so.write("flashcontent6");
									</script>				
				                </td>
					        </tr>             
						</table> 
		            </td>
	            </tr>

<!-------------->                	
                	

                    
                  </table>                                                          					
                  
            <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr> 
                <td valign="top">
                <table width="100%" height="1" border="0" cellpadding="0" cellspacing="0">
                    <tr align="left" bgcolor="#ECECEC"> 
                      <td width="90%" height="23">&nbsp;<b>��������</b> 
                        </td>
                    </tr>
            	    <tr> 
              		<td width="100%" align="center"> 
                		<table width="90%" cellpadding="0" cellspacing="0">
                		
                  			<tr align="center" height="28"> 
                    				<td width="10%" class="detail-data-body-title" align="center">������</td>
                    				<td width="10%" class="detail-data-body-title" align="center">��æ(%)</td>
                    				<td width="10%" class="detail-data-body-title" align="center">ƽ�����</td>
                    				<td width="10%" class="detail-data-body-title" align="center">��д����/��</td>
                    				<td width="15%" class="detail-data-body-title" align="center">��д�ֽڣ�K��/��</td>
                    				<td width="20%" class="detail-data-body-title" align="center">ƽ���ȴ�ʱ��(ms)</td>
                    				<td width="20%" class="detail-data-body-title" align="center">ƽ��ִ��ʱ��(ms)</td>
                  			</tr>
                  
                  
                  
                 <%
                 	if(alldiskperf != null && alldiskperf.size()>0){
                 		
      				for(int i=0;i<alldiskperf.size();i++){
          				Hashtable diskperhash = (Hashtable)alldiskperf.get(i);
     		 %>
                  <tr height=28 <%=onmouseoverstyle%>> 
                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("disklebel")%></td>
                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("%busy")%></td>
                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("avque")%></td>
                    <td  width="10%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("r+w/s")%></td>
                    <td  width="15%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("Kbs/s")%></td>
                    <td  width="20%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("avwait")%></td>
                    <td  width="20%" class="detail-data-body-list" align="center">&nbsp;<%=(String)diskperhash.get("avserv")%></td>
                    <%
                    }%>
                  </tr>
      <% } %>
                </table>
              </td>
            </tr>   
            <tr> 
              		<td width="100%" align="center"> 
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
							<td class="td-container-main-tool" width=15%>
								<jsp:include page="/include/scounixwaretoolbar.jsp">
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