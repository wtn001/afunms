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
<%@ page import="com.afunms.polling.om.IpMac"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@ page import="com.afunms.config.dao.*"%>
<%@ page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.temp.model.*"%>
<%@page import="com.afunms.detail.net.service.NetService"%>
<%@ page import="com.afunms.polling.loader.HostLoader"%>


<%
  	String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	String menuTable = (String) request.getAttribute("menuTable");//�˵�
	String tmp = request.getParameter("id"); 
	String flag1 = request.getParameter("flag");  
	String name = (String)request.getAttribute("name");
	String ip = (String)request.getAttribute("ipaddress");
	int manager = (Integer)request.getAttribute("manager");
	String pointId=	(String)request.getAttribute("pointId");
	String	index=(String)request.getAttribute("index");
	String	state=(String)request.getAttribute("state");
	String	conState=(String)request.getAttribute("conState");
	String	conIndex=(String)request.getAttribute("conIndex");
	String	wwnname=(String)request.getAttribute("wwnname");
	String	wwnid=(String)request.getAttribute("wwnid");
	
	
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
	
   String rootPath = request.getContextPath();  
	
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
	        popMenu(itemMenu,150,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,150,"1111");
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
 
  Ext.get("processfdb").on("click",function(){  
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/macmanager.do?action=refreshfdb&id=<%=tmp%>&flag=<%=flag%>";
  mainForm.submit();
 });
}

function refer(action){
		document.getElementById("id").value="<%=tmp%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
function showController(id , ip){  
               window.open("<%=rootPath%>/netreport.do?action=storageWwnCon&id="+id,'mywindow','width=,height=,resizable=1');
  }
 function showNumber(id , ip){  
               window.open("<%=rootPath%>/netreport.do?action=storageNumber&id="+id,'mywindow','width=,height=,resizable=1');
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
				 <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;�豸����:
										        <span id="lable"><%=name%></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�豸����:
											<span id="sysname">�����洢</span></td>
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP��ַ:<%=ip %>
											
											
												
												
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
											    		<%=Hdc%>
											    	</td>
											  </tr>
											  <tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr valign=top>
				        										<td>
				        											<table cellspacing="1">
																		
																		 <tr align="center" bgcolor="#ECECEC" height="28"> 
    <td  width="10%" align="center">���к�</td>
    <td  width="10%" align="center">�˿�ID</td>
    <td  width="10%" align="center">��������</td>
    <td  width="30%" align="center">wwn����</td>
    <td  width="10%" align="center">wwnid</td>
    <td  width="15%" align="center">���ʹ��״̬</td>
    <td  width="15%" align="center">������״̬</td>
  </tr>

     
            <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>
            <%if(index != null && !index.equals("")) {%>
            <td  class="detail-data-body-list"><%=index%></td>
              <%
            }else{
             %>
             <td  class="detail-data-body-list"></td>
              <%} %>
             <% 
             if(pointId != null && !pointId.equals("")){
              %>
            <td class="detail-data-body-list"><%=pointId%></td>
               <%
            }else{
             %>
             <td  class="detail-data-body-list"></td>
              <%} %>
              <% 
             if(conIndex != null && !conIndex.equals("")){
              %>
            <td class="detail-data-body-list"><%=conIndex%></td>
               <%
            }else{
             %>
             <td  class="detail-data-body-list"></td>
              <%} %>
              <% 
             if(wwnname != null && !wwnname.equals("")){
              %>
            <td class="detail-data-body-list"><%=wwnname%></td>
                <%
            }else{
             %>
             <td  class="detail-data-body-list"></td>
              <%} %>
              <% 
             if(wwnid != null && !wwnid.equals("")){
              %>
            <td class="detail-data-body-list"><%=wwnid%></td>
                <%
            }else{
             %>
             <td  class="detail-data-body-list"></td>
              <%} %>
             
             <%if(conState != null && !conState.equals("")) {
                  if(conState.equals("1")){
                %>
             <td align="center" class="body-data-list">
					     <table>
							 <tr>
								<td align="center" width="50%">����</td>
								<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showNumber("<%=tmp%>" , "<%=ip%>")' width=15></td>
							</tr>
						</table>
			</td>
            <%
                 }else{
             %>
             <td align="center" class="body-data-list">
					     <table>
							 <tr>
								<td align="center" width="50%">������</td>
								<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showNumber("<%=tmp%>" , "<%=ip%>")' width=15></td>
							</tr>
						</table>
			</td>
            <%
                 }
             }else{
             %>
             <td  class="detail-data-body-list"></td>
              <%} %>
             <%if(state != null && !"".equals(state)) {
                  if("1".equals(state)){
                %>
             <td align="center" class="body-data-list">
					     <table>
							 <tr>
								<td align="center" width="50%">����</td>
								<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showController("<%=tmp%>" , "<%=ip%>")' width=15></td>
							</tr>
						</table>
			</td>
            <%
                 }else{
             %>
             <td align="center" class="body-data-list">
					     <table>
							 <tr>
								<td align="center" width="50%">������</td>
								<td align="center" width="50%"><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showController("<%=tmp%>" , "<%=ip%>")' width=15></td>
							</tr>
						</table>
			</td>
            <%
                 }
             }else{
             %>
             <td  class="detail-data-body-list"></td>
              <%} %>
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
									</table>
									
							</td>
							
							<td class="td-container-main-tool" width='15%' >
								<jsp:include page="/include/dhstoolbar.jsp">
									<jsp:param value="<%=ip%>" name="ipaddress"/>
									<jsp:param value="<%=tmp%>" name="tmp"/>
									<jsp:param value="network" name="category"/>
									<jsp:param value="dhs" name="subtype"/>
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