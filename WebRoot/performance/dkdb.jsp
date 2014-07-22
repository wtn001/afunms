<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.afunms.config.model.Portconfig"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String startdate = sdf.format(new Date());
	String todate = sdf.format(new Date());
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<%=rootPath%>/js/tree/Tree.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/tree/common.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
<script>
var ddtree = null;
function init()
{
	parseDataForTree();
	refreshStyle('dkdb');
}

function refreshStyle(liId){
	var varsLi = window.parent.tabMenuFrame.document.getElementsByTagName('li');
	for(var i=0; i<varsLi.length; i++){
		var varLi = varsLi[i];
		if(varLi.id == liId){
			window.parent.tabMenuFrame.document.getElementById(varLi.id).className = "menu-title-over";
		}else{
			window.parent.tabMenuFrame.document.getElementById(varLi.id).className = "menu-title";
		}
	}
}

 //������ָ�������������
function parseDataForTree()
{
	ddtree = new Tree("sorttree","100%","100%",0);
	ddtree.setImagePath("<%=rootPath%>/resource/image/tree/");
	ddtree.setDelimiter(",");
	ddtree.enableCheckBoxes(1);
	
	ddtree.insertNewItem("","root","�����豸��Դ��", 0, "", "","", "");
	ddtree.setCheck("root",0);
	<%
	HostNode vo = new HostNode();
	if(list!=null&&list.size()>0)
	{
		for(int i=0;i<list.size();i++){
			vo=(HostNode)list.get(i);
	%>
			ddtree.insertNewItem("root","<%=vo.getId()%>","<%=vo.getIpAddress()%>", 0, "", "","", "");
	<%
			com.afunms.config.dao.PortconfigDao dao = new com.afunms.config.dao.PortconfigDao();
			List hostIfentityList = dao.getInfsBySms(vo.getIpAddress());
			for(int j=0;j<hostIfentityList.size();j++){
				Portconfig node = (Portconfig) hostIfentityList.get(j);
	%>
				ddtree.insertNewItem("<%=vo.getId()%>","port*"+"<%=node.getIpaddress()%>"+"*"+"<%=node.getPortindex()%>"+"*"+"<%=node.getName()%>","<%=node.getName()%>", 0, "", "","", "");
				ddtree.closeAllItems("<%=vo.getId()%>");
	<%
			}
		}
	}
	%>
}

//����ģ���б�|�鿴ģ����ϸ��Ϣ
function createWin(id){
	return CreateDeviceWindow("<%=rootPath%>/tabMenu.do?action=loaddkdbtempletdetail&id="+id);
}

function loadIds(id,ids,startdate,todate){
	$.ajax({
		type:"GET",
		dataType:"json",
		url:"<%=rootPath%>/serverAjaxManager.ajax?action=dkdbPreview&id="+id+"&ids="+ids+"&startdate="+startdate+"&todate="+todate+"&nowtime="+(new Date()),
		success:function(data){
		    if(data.portdata==0){//�˿�
				var portDiv =document.getElementById("portDiv");
			  	portDiv.innerHTML="";
			  	var netportReport =document.getElementById("netportReport");
			  	netportReport.innerHTML="";
		    }else {
		     	var so = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","885", "338", "8", "#FFFFFF");
	         	so.addVariable("path", "<%=rootPath%>/amchart/");
	         	so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/netPortReport_settings.xml"));
		     	so.addVariable("chart_data",data.portdata);
			 	so.write("netportReport");
			 	var portDiv =document.getElementById("portDiv");
			 	portDiv.innerHTML=data.portHtml;
		    }
		}
	});
}

//����ģ��ѡ��Ԥ��
function preview(id){
	var tab = Ext.getCmp('ext-tab-report');
	tab.setActiveTab(0);
	document.getElementById('editmodel').style.display='none';
	var	startdate=document.all.startdate.value;
	var	todate=document.all.todate.value;
	var ids=null;
	document.all.id_perform.value=id;
	document.all.ids_perform.value=null;
	loadIds(id,ids,startdate,todate)
}

Ext.onReady(function()
{  
	setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
});
  
//Ԥ��
function query_ok()
{
	var	startdate=document.all.startdate.value;
	var	todate=document.all.todate.value;
	var	ids = ddtree.getAllChecked().toString();
		
 

	if(ids.length<=0|| ids.length == "")
	{
	alert("��ѡ���豸�ӿڣ�����");
	return;
	}
	 var  num=0;
	 var arr = ids.match(/\*/g);
     if(arr!=null&&arr.length) num = arr.length;
     if(num>15)
	  {
	   alert("�������ѡ��5���˿ڣ���");
	    return;
	  }
	var id=null;
	document.all.ids_perform.value=ids;
	document.all.id_perform.value=id;
	loadIds(id,ids,startdate,todate);
}
</script>

<!-- Tabҳ -->
<script language="JavaScript" type="text/JavaScript">
Ext.onReady(function(){
	var	ids = ddtree.getAllChecked().toString();
    var tabs = new Ext.TabPanel({
     	id: 'ext-tab-report',
        renderTo: 'tabs1',
        width:888,
        activeTab: 0,
        frame:true,
        defaults:{autoHeight: true},
        items:[
            {contentEl:'script', title: '��������'},
           
            {contentEl:'model', title: '����ģ���б�',listeners: {activate: handleActivate}}
        ]
    });

function handleActivate(tab){
   $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=loaddkdbtemplet&nowtime="+(new Date()),
			success:function(data){
				var modelDiv =document.getElementById("model");
				modelDiv.innerHTML=data.dataStr;
			}
		});
    }
});

//����ģ��
$(document).ready(function(){
	$('#saveBtn').bind('click',function(){
		var report_name=$('#report_name').val();
		var name=$('#recievers_name').val();
		var tile=$('#tile').val();
		var desc=$('#desc').val();
		var bid=$('#bid').val();
		var bidtext=$('#bidtext').val();
		var reporttype=$('#reporttype').val();
		var exporttype=$('#exporttype').val();
		var frequency=$('#transmitfrequency').val();
		var month=$('#sendtimemonth').val();
		var week=$('#sendtimeweek').val();
		var day=$('#sendtimeday').val();
		var hou=$('#sendtimehou').val();
		var re_id=$('#recievers_id').val();
		
		if($('#recievers_name').val()==null || $('#recievers_name').val()=='')
			return;
		if($('#bidtext').val()==null || $('#bidtext').val()=='')
			return;
	
		if($('#timeConfigTable tr').length<2)
				return;
        var	ids = ddtree.getAllChecked().toString();
		if(ids.length<=0|| ids.length == "")
		{
		   alert("��ѡ���豸�Ķ˿ںţ�����");
	 	   return;
		}
		
        $.ajax({
			type:"POST",
			dataType:"json",//contentType: "application/x-www-form-urlencoded; charset=utf-8",
			data:"ids="+ids+"&tile="+tile+"&desc="+desc+"&bid="+bid+"&bidtext="+bidtext+"&exporttype="+exporttype+"&reporttype="+reporttype+"&report_name="+report_name+"&recievers_name="+name+"&transmitfrequency="+frequency+"&sendtimemonth="+month+"&sendtimeweek="+week+"&sendtimeday="+day+"&sendtimehou="+hou+"&recievers_id="+re_id+"&nowtime="+(new Date()),
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=savedkdbtemplate",//����˿ڶԱ�ģ��
			success:function(data){
				alert(data.dataStr);
				var tab = Ext.getCmp('ext-tab-report');
	            tab.setActiveTab(1);
			}
		});
        
        
	});
});
	
//�༭ģ��
function editModel(){
	document.getElementById('editmodel').style.display='block';
}
//����ģ��
function hiddenModel(){
	document.getElementById('editmodel').style.display='none';
}
//ɾ��ģ��
function deleteItem(id){
 if(window.confirm("��ȷ��Ҫɾ����")){
  	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=deletedkdbtemplet&id="+id+"&nowtime="+(new Date()),
			success:function(data){
				var modelDiv =document.getElementById("model");
				modelDiv.innerHTML=data.dataStr;
			}
		});
   }
} 

</script>
		<script language="JavaScript" type="text/javascript">
  
//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
function CreateDeviceWindow(url)
{
	
msgWindow=window.open(url,"_blank","toolbar=no,width=900,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}

function setReciever(ctrlId,hideCtrlId)
{
	return CreateWindow("<%=rootPath%>/subscribeReportConfig.do?action=user_list&&ctrlId="+ctrlId+"&&hideCtrlId="+hideCtrlId);
}
function setDevices(ctrlId,hideCtrlId)
{
	return CreateDeviceWindow("<%=rootPath%>/subscribeReportConfig.do?action=device_list&&ctrlId="+ctrlId+"&&hideCtrlId="+hideCtrlId);
}
function timeType(obj){
	var type = obj.value;
	document.getElementById('td_sendtimehou').style.display='none';
	document.getElementById('td_sendtimeday').style.display='none';
	document.getElementById('td_sendtimeweek').style.display='none';
	document.getElementById('td_sendtimemonth').style.display='none';
	document.getElementById('sendtimehou').disabled="disabled";
	document.getElementById('sendtimeday').disabled="disabled";
	document.getElementById('sendtimeweek').disabled="disabled";
	document.getElementById('sendtimemonth').disabled="disabled";
	if(type==1){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('sendtimehou').disabled="";
	}else if(type==2){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeweek').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeweek').disabled="";
	}else if(type==3){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeday').disabled="";
	}else if(type==4){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeday').disabled="";
		document.getElementById('sendtimemonth').disabled="";
	}else if(type==5){
		document.getElementById('td_sendtimehou').style.display='';
		document.getElementById('td_sendtimeday').style.display='';
		document.getElementById('td_sendtimemonth').style.display='';
		document.getElementById('sendtimehou').disabled="";
		document.getElementById('sendtimeday').disabled="";
		document.getElementById('sendtimemonth').disabled="";
	}
}
function CreateWindow(url)
{
 msgWindow=window.open(url,"_blank","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}

function exportReport(exportType){
	var startdate=document.all.startdate.value;
	var todate=document.all.todate.value;
	var ids=document.all.ids_perform.value;
	var id=document.all.id_perform.value;
	
	if(ids.length<=0|| ids.length == ""){
	 	ids = ddtree.getAllChecked().toString();
		if((ids.length<=0|| ids.length == "")&&(id.length<=0|| id.length == "")){
			alert("��ѡ���豸�Ķ˿ڻ�ģ�壡����");
			return;
		}
	}
	window.open('<%=rootPath%>/tabMenu.do?action=portcompareExport&type=net&exportType='+exportType+'&id='+id+'&ids='+ids+'&startdate='+startdate+'&todate='+todate+'&nowtime='+(new Date()),"_blank","toolbar=no,width=1,height=1,top=2000,left=3000,directories=no,status=no,menubar=no,alwaysLowered=yes");
}
</script>
<style>
			.x-tab-strip-top .x-tab-right {
				background-image: url("<%=rootPath%>/img/ext/tabs/tabs-sprite.gif");
			}
			
			.x-tab-strip-top .x-tab-left {
				background-image: url("<%=rootPath%>/img/ext/tabs/tabs-sprite.gif");
			}
			
			.x-tab-strip-top .x-tab-strip-inner {
				background-image: url("<%=rootPath%>/img/ext/tabs/tabs-sprite.gif");
			}
			
			.x-tab-panel-body {
				border-bottom-color: #EAEAEA;
				border-left-color: #EAEAEA;
				border-right-color: #EAEAEA;
				border-top-color: #EAEAEA;
			}
			
			.x-tab-panel-header {
				background-color: #EAEAEA;
				border-bottom-color: #EAEAEA;
				border-left-color: #EAEAEA;
				border-right-color: #EAEAEA;
				border-top-color: #EAEAEA;
			}
			
			.x-tab-panel-header-plain .x-tab-strip-spacer {
				background-color: #EAEAEA;
				border-bottom-color: #EAEAEA;
				border-left-color: #EAEAEA;
				border-right-color: #EAEAEA;
				border-top-color: #EAEAEA;
			}
			
			.x-panel {
				border-bottom-color: #EAEAEA;
				border-left-color: #EAEAEA;
				border-right-color: #EAEAEA;
				border-top-color: #EAEAEA;
			}
			
			.x-panel-body {
				border-bottom-color: #EAEAEA;
				border-left-color: #EAEAEA;
				border-right-color: #EAEAEA;
				border-top-color: #EAEAEA;
			}
			
			UL.x-tab-strip-top {
				background-color: #EAEAEA;
				background-image: url("<%=rootPath%>/resource/image/global/content_header_background.jpg");
			}
		</style>
	</head>
	<body id="body" class="body" onLoad="init();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>

		<form id="mainForm" method="post" name="mainForm">
			<div id="loading">
				<div class="loading-indicator">
					<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
						width="32" height="32" style="margin-right: 8px;" align="middle" />
					Loading...
				</div>
			</div>
			<div id="loading-mask" style=""></div>
			<table id="body-container" class="body-container">
				<tr>

					<td valign=top style="margin: 10px auto; position: relative;">
						<table>
							<tr>
								<td height="100%" align="left" valign="top">
									<div id="sorttree" style="margin:0 8px 0 0;padding: 10px; background: #ffffff; height: 600px; width: 240px;"></div>
								</td>
								<td width="80%" height="100%" valign="top">
									<div id='tabs1'>
										<div id="script" class="x-hide-display">
											<table border="0" cellpadding="0" cellspacing="0"
												class="win-content" id="win-content">

												<tr>

													<td colspan="1" width="94%">
														<table id="win-content-body" class="win-content-body">
															<tr>

																<td>
																	<div>
																		<table bgcolor="#ECECEC">
																			<tr align="left" valign="middle">
																				<td height="21" align="left" valign=top>
																					&nbsp;&nbsp;&nbsp;��ʼ����
																					<input type="text" id="mystartdate"
																						name="startdate" value="<%=startdate%>" size="10">
																					<a onClick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																						<img id=imageCalendar1 width=34 height=21
																							src="<%=rootPath%>/include/calendar/button.gif"
																							border=0> </a> ѡ������
																					<input type="text" id="mytodate" name="todate"
																						value="<%=todate%>" size="10" />
																					<a onClick="event.cancelBubble=true;"
																						href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																						<img id=imageCalendar2 width=34 height=21
																							src="<%=rootPath%>/include/calendar/button.gif"
																							border=0> </a> &nbsp;&nbsp;
																					<input type="button" name="doprocess" value="Ԥ  ��" onClick="query_ok()">
																					<!-- 	&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="save" value="����ģ��" onClick="savePerforIndex()">
																								&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" name="create" value="�½�ģ��" onClick="createModel()"> -->
																					&nbsp;&nbsp;&nbsp;&nbsp;
																					<input type="button" name="edit" value="�༭ģ��"
																						onClick="editModel()">
																					<input type="hidden" name="ids_perform"
																						id="ids_perform" value="" />
																					<input type="hidden" name="id_perform"
																						id="id_perform" value="" />
																					<span style="CURSOR: hand"
																						onclick="exportReport('doc')"><img
																							name="selDay1" alt='����WORD'
																							src="<%=rootPath%>/resource/image/export_word.gif"
																							width=18 border="0">����WORD</span>&nbsp;&nbsp;&nbsp;&nbsp;
																					<span style="CURSOR: hand"
																						onclick="exportReport('xls')"><img
																							name="selDay1" alt='����EXCEL'
																							src="<%=rootPath%>/resource/image/export_excel.gif"
																							width=18 border="0">����EXCEL</span>&nbsp;&nbsp;&nbsp;&nbsp;
																					<span style="CURSOR: hand"
																						onclick="exportReport('pdf')"><img
																							name="selDay1" alt='����PDF'
																							src="<%=rootPath%>/resource/image/export_pdf.gif"
																							width=18 border="0">����PDF</span>&nbsp;&nbsp;&nbsp;&nbsp;
																				</td>

																			</tr>
																		</table>
																	</div>
																</td>

															</tr>

														</table>
													</td>
												</tr>

												<tr>
													<td width=100%>
														<jsp:include page="../capreport/busireport/subscribeAdd.jsp"></jsp:include>
													</td>
												</tr>
												<tr>
													<td width=94%>
														<table cellpadding="0" cellspacing="0">
															<tr>
																<td width="100%" align=center>
																	<div id="netpingReport">
																	</div>
																</td>
															</tr>
														</table>
													</td>

												</tr>
												<tr>

													<td align=right>
														<div id="pingDiv">
														</div>
													</td>

												</tr>
												<tr>

													<td width=94%>
														<table cellpadding="0" cellspacing="0">
															<tr>
																<td width="100%" align=center>
																	<div id="netcpuReport">
																	</div>
																</td>
															</tr>
														</table>
													</td>

												</tr>
												<tr>

													<td width=94%>
														<div id="cpuDiv">
														</div>
													</td>

												</tr>
												<tr>

													<td width=94%>
														<table cellpadding="0" cellspacing="0">
															<tr>
																<td width="100%" align=center>
																	<div id="netmemReport">
																	</div>
																</td>
															</tr>
														</table>
													</td>

												</tr>
												<tr>

													<td width=94%>
														<div id="memDiv">
														</div>
													</td>

												</tr>
												<tr>

													<td width=94%>
														<table cellpadding="0" cellspacing="0">
															<tr>
																<td width="100%" align=center>
																	<div id="netportReport">
																	</div>
																</td>
															</tr>
														</table>
													</td>

												</tr>
												<tr>

													<td width=94%>
														<div id="portDiv">
														</div>
													</td>

												</tr>
											</table>

										</div>

										<div id="model" class="x-hide-display">
										</div>
									</div>
								</td>
							</tr>
						</table>

					</td>
				</tr>
			</table>
		</form>

	</BODY>
</HTML>