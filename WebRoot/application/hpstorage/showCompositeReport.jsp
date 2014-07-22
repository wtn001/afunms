<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.polling.node.Disk"%>
<%@page import="com.afunms.common.util.ShareData"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.polling.node.ArrayInfo"%>
<%@page import="com.afunms.polling.node.Enclosure"%>
<%@page import="com.afunms.polling.node.Controller"%>
<%@page import="com.afunms.polling.node.Port"%>
<%@page import="com.afunms.polling.node.Lun"%>
<%@page import="com.afunms.polling.node.VFP"%>
<%@page import="com.afunms.polling.node.SubSystemInfo"%>
<%@page import="com.afunms.polling.node.EnclosureFru"%>
<%@page import="com.afunms.polling.node.CtrlPort"%>
<%@page import="com.afunms.polling.node.DIMM"%>
<%@page import="com.afunms.polling.node.Battery"%>
<%@page import="com.afunms.polling.node.Processor"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<html>
<head>
<%
  String rootPath = request.getContextPath();
  //System.out.println(rootPath); 
  String ipaddress = (String)request.getAttribute("ipaddress");
  String startdate = (String)request.getAttribute("startdate");
  String todate = (String)request.getAttribute("todate");
  int nodeid = (Integer)request.getAttribute("nodeid");
  
  List<Disk> disks = null;
  ArrayInfo arrayinfo = null;
  List<Enclosure> enclosures = null;
  List<Controller> controllers = null;
  List<Port> ports = null;
  List<Lun> luns = null;
  List<VFP> vfps = null;
  SubSystemInfo subSystemInfo = null;
  
  Hashtable ipAllData = new Hashtable();
  //ֻȡ��ǰ�ɼ����ۺ���Ϣ
  ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ipaddress);
  if(ipAllData != null){
      disks = (List<Disk>)ipAllData.get("disks");
      arrayinfo = (ArrayInfo)ipAllData.get("arrayinfo");
      enclosures = (List<Enclosure>)ipAllData.get("enclosures");
      controllers = (List<Controller>)ipAllData.get("controllers");
      ports = (List<Port>)ipAllData.get("ports");
      luns = (List<Lun>)ipAllData.get("luns");
      vfps = (List<VFP>)ipAllData.get("vfps");
      subSystemInfo = (SubSystemInfo)ipAllData.get("subSystemInfo");
  }
%>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<title>�ۺϱ���</title>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
<!-- snow add end -->
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script language="JavaScript" type="text/JavaScript">
function network_composite_word_report()
{
	mainForm.action = "<%=rootPath%>/netreport.do?action=downloadHPStorageCompReport&ipaddress=<%=ipaddress%>&str=1";
	mainForm.submit();
}
function network_composite_excel_report()
{
	mainForm.action = "<%=rootPath%>/netreport.do?action=downloadHPStorageCompReport&ipaddress=<%=ipaddress%>&str=0";
	mainForm.submit();
}
function network_composite_pdf_report()
{
	mainForm.action = "<%=rootPath%>/netreport.do?action=downloadHPStorageCompReport&ipaddress=<%=ipaddress%>&str=4";
	mainForm.submit();
}
function network_composite_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	//var prewindow = window.open("<%=rootPath%>/netreport.do?action=showPingReport&ipaddress=<%=ipaddress%>&startdate="+starttime+"&todate="+endtime,"hello","portScanWindow","width=800,height=400,scrollbars=yes,resizable=yes");
	mainForm.action = "<%=rootPath%>/netreport.do?action=showHPstorCompositeReport&ipaddress=<%=ipaddress%>&id=<%=nodeid%>&startdate="+starttime+"&todate="+endtime;
	mainForm.submit();
	//prewindow.opener.close();
}
function network_ping_cancel()
{
window.close();
}

  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostcpu";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});
function setClass(){
	document.getElementById('hpStorageReportDetailTile-2').className='detail-data-title';
	document.getElementById('hpStorageReportDetailTile-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('hpStorageReportDetailTile-2').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		document.getElementById("id").value="<%=nodeid%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
</head>
<body id="body" class="body" onload="setClass();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form id="mainForm" method="post" name="mainForm">
<input type=hidden id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
<input type=hidden id="id" name="id">
	<table id="container-main" class="container-main">
		<tr>
			<td>
				<table id="container-main-win" class="container-main-win">
					<tr>
						<td>
							<table id="win-content" class="win-content">
								<tr>
									<td>
										<table id="win-content-header" class="win-content-header">
				                			<tr>
							                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
							                	<td class="win-content-title" style="align:center">&nbsp;�ۺϱ���</td>
							                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
											</tr>									    
					       				</table>
				       				</td>
				       			</tr>
				       			<tr>
									<td>
										<table id="report-content-header" class="report-content-header">
						                	<tr>
							    				<td>
										    		<%=hpStorageReportDetailTileTable%>
										    	</td>
										  	</tr>
							        	</table>
        							</td>
        						</tr>
						       	<tr>
						       		<td>
						       			<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
														<td height="28" align="left">
															&nbsp;��ʼ����
															<input type="text" id="mystartdate" name="startdate" value="<%=startdate%>" size="10">
															<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
															<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
																							
															��ֹ����
															<input type="text" id="mytodate" name="todate" value="<%=todate%>" size="10"/>
															<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
															<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0></a>
															<input type="button" name="doprocess" value="ȷ��" onclick="network_composite_ok()">
														</td>
														<td height="28" align="left">
															<a href="javascript:network_composite_word_report()"><img name="selDay1" alt='����word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">����WORD</a>
														</td>
														<td height="28" align="left">
															<a href="javascript:network_composite_excel_report()"><img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL</a>
														</td>
														<td height="28" align="left">&nbsp;
															<a href="javascript:network_composite_pdf_report()"><img name="selDay1" alt='����word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">����PDF</a>
														</td>
														</tr>  
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" ></td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">													
													<input type=hidden name="eventid">
													<div id="loading">
													<div class="loading-indicator">
														<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
													</div>
													<table border="1" width="100%">
													     <tr>
							                                  <td align="center" style="font-size: 20"><br><b>HP�洢�ۺϱ���<br><br></td>
						                                 </tr>
						                                 <tr>
						                                   <td align="left" style="font-size: 15">
						                                      <br><b>������Ϣ<br><br>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="center">
						                                 <%  
						                                       if(arrayinfo != null)
						                                       {
						                                 %>
						                                 
						                                      <table>
                    										   <tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����״̬:</td>
                      											<td width="35%"><%=SysUtil.ifNull(arrayinfo.getArrayStatus())%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�̼��汾:</td>
                      											<td width="35%"><%=SysUtil.ifNull(arrayinfo.getFirmwareRevision())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��Ʒ�汾:</td>
                      											<td width="35%"><%=SysUtil.ifNull(arrayinfo.getProductRevision())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;���ؿ�������Ʒ�汾:</td>
                      											<td width="35%"><%=SysUtil.ifNull(arrayinfo.getLocalControllerProductRevision())%> </td>	
                    										   </tr>
                    										   <tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;Զ�̿�������Ʒ�汾:</td>
                      											<td width="35%"><%=SysUtil.ifNull(arrayinfo.getRemoteControllerProductRevision())%> </td>																
                    										    </tr>                    										
                									          </table>						                                   
						                                 <%
						                                     }
						                                  %>
						                                 </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="center">						                                      											               
											                <%
											                     if(enclosures != null && enclosures.size()>0)
											                     {
											                     for(int i=0;i<enclosures.size();i++)
											                     {
											                           Enclosure encl = enclosures.get(i);
											                 %>
											                 <table>
											                 <tr bgcolor="#ECECEC">
											                  <td colspan="4" align="left"><font style="font-weight: bold">������Ϣ:</font></td>
											                 </tr>
											                 <tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(encl.getName())%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(encl.getEnclosureID())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����״̬:</td>
                      											<td width="35%"><%=SysUtil.ifNull(encl.getEnclosureStatus())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(encl.getEnclosureType())%> </td>	
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����ȫ������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(encl.getNodeWWN())%> </td>	
																<!-- <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��־����ʱ��:</td>
                      											<td width="35%"><=arrayinfo.getCommandexecutioTimestamp()%> </td> -->
                    										</tr>                     										
                    										  <%
                    										       if(encl.getFrus() != null && encl.getFrus().size()>0)
                    										       {
                    										  %>
                    										  <tr bgcolor="#ECECEC">
											                  <td colspan="4" align="left"><font style="font-weight: bold">FRU��Ϣ:</font></td>
											                  </tr>
                    										  <tr>
                    										     <td colspan="4">
                    										       <table>
                    										         <tr>
                    										            <td width="20%" height="26" align="center">����</td>
                    										            <td width="30%" height="26" align="center">���</td>
                    										            <td width="30%" height="26" align="center">��ʶ</td>
                    										            <td width="20%" height="26" align="center">״̬</td>                    										                               										            
                    										         </tr>
                    										   <%
                    										          for(int j=0;j<encl.getFrus().size();j++)
                    										           {
                    										               EnclosureFru fru = encl.getFrus().get(j);
                    										 %>
                    										 <%
                    										                if(j%2 == 0)
                    										                {
                    										  %>
                    										             <tr bgcolor="#ECECEC">
                    										  
                    										  <%
                    										                }else{
                    										    
                    										  %>
                    										         <tr>
                    										  <%
                    										                }
                    										  %>
                    										            <td width="20%" height="26" align="center"><%=SysUtil.ifNull(fru.getFru()) %></td>
                    										            <td width="30%" height="26" align="center"><%=SysUtil.ifNull(fru.getHwComponent()) %></td>
                    										            <td width="30%" height="26" align="center"><%=SysUtil.ifNull(fru.getIdentification()) %></td>
                    										            <td width="20%" height="26" align="center"><%=SysUtil.ifNull(fru.getIdStatus()) %></td>                    										                               										            
                    										         </tr>
                    										 <%
                    										           }
                    										    %>                    										    
                    										      </table>
                    										      </td>
                    										      </tr>
                    										  <%
                    										       }
                    										  %>
                    										    <tr>
                      											    <td bgcolor="#ECECEC" colspan="4">&nbsp;</td>
                    										    </tr>
                    										    </table>
                    										  <%                 										  
											                     }
											                     }
											                  %>
                    									
						                                   </td>
						                                 </tr>
						                                 
						                                 <tr>
						                                   <td align="center">						                                      
											                 <%  
											                        if(controllers != null && controllers.size()>0)
											                        {
											                           for(int i=0;i<controllers.size();i++)
											                           {
											                               Controller cont = controllers.get(i);
											                  %>
											                  <table>
											                  <tr bgcolor="#ECECEC">
											                     <td colspan="4" align="left"><font style="font-weight: bold">��������Ϣ:</font></td>
											                  </tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getName())%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;״̬:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getStatus())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;���к�:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getSerialNumber())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�����̱��:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getVendorID())%> </td>
                    										</tr> 
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��Ʒ���:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getProductID())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��Ʒ�汾:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getProductRevision())%> </td>
                    										</tr>
                    										
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�̼��汾:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getFirmwareRevision())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�����̲�Ʒ����:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getManufacturingProductCode())%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getControllerType())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����ع̼��汾:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getBatteryChargerFirmwareRevision())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;������������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getEnclosureSwitchSetting())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getEnclosureID())%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getLoopPair())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getLoopID())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����������ַ:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getDriveAddressBasis())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ι̵�ַ:</td>
                      											<td width="35%"><%=SysUtil.ifNull(cont.getHardAddress())%> </td>
                    										</tr> 
                    										 <%  
                    										         if(cont.getFrontPortList() != null && cont.getFrontPortList().size()>0)
                    										         {                    										                       										 %>
                    										
                    										  <tr>
                    										     <td colspan="4" align="left"><font style="font-weight: bold">ǰ�˿��б�</font></td>
                    										  </tr>                    						
                    										  
                    										  <%
                    										             for(int j=0;j<cont.getFrontPortList().size();j++)
                    										             {
                    										                    CtrlPort fctp = cont.getFrontPortList().get(j);
                    										  
                    										  %>                     										  
                    										    <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getStatus())%> </td>
                    										   </tr>
                    										    <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�˿�ʵ��:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getPortInstance())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�����ַ:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getHardAddress())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getLinkState())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ڵ�ȫ������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getNodeWWN())%> </td>
                    										   </tr>
                    										   
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�˿�ȫ������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getPortWWN())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getTopology())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getDataRate())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�˿ڱ��:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getPortID())%> </td>
                    										   </tr> 
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�豸����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getDeviceHostName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�豸·��:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(fctp.getDevicePath())%> </td>
                    										   </tr>
                    										   <tr>
                      											    <td colspan="4">&nbsp;</td>
                    										   </tr>            										                  										
                    										<%
                    										            }
                    										            }             										            
                    										  
                    										         if(cont.getBackPortList() != null && cont.getBackPortList().size()>0)
                    										         {                    										                       										 %>
                    										 
                    										  <tr>
                    										     <td colspan="4" align="left"><font style="font-weight: bold">��˿��б�</font></td>
                    										  </tr>                    						
                    										  
                    										  <%
                    										             for(int j=0;j<cont.getBackPortList().size();j++)
                    										             {
                    										                    CtrlPort bctp = cont.getBackPortList().get(j);
                    										  
                    										  %>                     										  
                    										    <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getStatus())%> </td>
                    										   </tr>
                    										    <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�˿�ʵ��:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getPortInstance())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�����ַ:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getHardAddress())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getLinkState())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ڵ�ȫ������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getNodeWWN())%> </td>
                    										   </tr>
                    										   
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�˿�ȫ������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getPortWWN())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getTopology())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getDataRate())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�˿ڱ��:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getPortID())%> </td>
                    										   </tr> 
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�豸����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getDeviceHostName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�豸·��:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(bctp.getDevicePath())%> </td>
                    										   </tr>
                    										   <tr>
                      											    <td colspan="4">&nbsp;</td>
                    										   </tr>           										                  										
                    										<%
                    										            }
                    										            }
                    										            if(cont.getDimmList() != null && cont.getDimmList().size()>0)
                    										            {
                    										%>
                    										  <tr>
                    										     <td colspan="4" align="left"><font style="font-weight: bold">˫��ֱ��ʽ�洢ģ���б�</font></td>
                    										  </tr> 
                    										  
                    										<%
                    										             for(int j=0;j<cont.getDimmList().size();j++)
                    										             {
                    										                    DIMM dimm = cont.getDimmList().get(j);
                    										  
                    										  %>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(dimm.getName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(dimm.getStatus())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(dimm.getCapacity())%> </td>                      											
                      											    <!-- <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											    <td width="35%"><=dimm.getStatus()%> </td> -->
                    										   </tr>
                    										   <tr>
                      											    <td colspan="4">&nbsp;</td>
                    										   </tr>
                    										<%
                    										            }
                    										            }
                    										            if(cont.getBattery() != null)
                    										            {
                    						
                    										 %>
                    										    <tr>
                    										        <td colspan="4" align="left"><font style="font-weight: bold">�����Ϣ</font></td>
                    										    </tr> 
                    										    
                    										 <%
                    										            Battery battery = cont.getBattery();
                    										  %>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getStatus())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��ʶ:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getIdentification())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getManufacturerName())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�豸����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getDeviceName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getManufacturerDate())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;ʣ�����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getRemainingCapacity())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;ʣ������ٷֱ�:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getPctRemainingCapacity())%> </td>
                    										   </tr>
                    										   <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��ѹ:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getVoltage())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ŵ�����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(battery.getDischargeCycles())%> </td>
                    										   </tr>
                    										   <tr>
                      											    <td colspan="4">&nbsp;</td>
                    										   </tr>
                    										 <%
                    										            }
                    										            if(cont.getProcessor() != null)
                    										            {
                    										  %>
                    										    <tr>
                    										        <td colspan="4" align="left"><font style="font-weight: bold">��������Ϣ</font></td>
                    										    </tr>
                    										    <%
                    										               Processor processor = cont.getProcessor();
                    										     %>
                    										     <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;����:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(processor.getName())%> </td>                      											
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(processor.getStatus())%> </td>
                    										    </tr>
                    										     <tr bgcolor="#ECECEC">
                      											    <td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��ʶ:</td>
                      											    <td width="35%"><%=SysUtil.ifNull(processor.getIdentification())%> </td>                      											
                      											    <!-- <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;״̬:</td>
                      											    <td width="35%"><=processor.getStatus()%> </td> -->
                    										    </tr>
                    										    <tr>
                      											    <td colspan="4">&nbsp;</td>
                    										    </tr>
                    										    
                    										  <%
                    										            }
                    										  %>
                    										    </table>
                    										  <% 			       
                    										           }
                    										      }
                    										 %>				
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="left" style="font-size: 15">
						                                      <br><b>�˿���Ϣ<br><br>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="center">						                                     
											                 
											                 <%  
											                        if(ports != null && ports.size()>0)
											                        {
											                  %>
											                   <table>
											                    <tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="center">�˿�����</td>
                      											<td width="10%" height="26" align="center">�˿ڱ��</td>	
																<td width="15%" height="26" align="center">�˿���Ϊ</td>
                      											<td width="35%" height="26" align="center">�˿�����</td>
                      											<td width="10%" height="26" align="center">�������ֵ</td>
                      											<td width="15%" height="26" align="center">���ݴ�������</td>
                    										    </tr>
											                  <%
											                           for(int i=0;i<ports.size();i++)
											                           {
											                               Port port = ports.get(i);
											                                if(i%2 == 1)
											                                {
											                 %>
											                   
											                    <tr bgcolor="#ECECEC">   
											                 <%
											                       }else{
											                  %>
											                    <tr>
											                  <%
											                             }
											                   %>                  										
                      											<td width="15%" height="26" align="center"><%=SysUtil.ifNull(port.getName())%></td>
                      											<td width="10%" height="26" align="center"><%=SysUtil.ifNull(port.getPortID())%></td>	
																<td width="15%" height="26" align="center"><%=SysUtil.ifNull(port.getBehavior())%></td>
                      											<td width="35%" height="26" align="center"><%=SysUtil.ifNull(port.getTopology())%></td>
                      											<td width="10%" height="26" align="center"><%=SysUtil.ifNull(port.getQueueFullThreshold())%></td>
                      											<td width="15%" height="26" align="center"><%=SysUtil.ifNull(port.getDataRate())%></td>                      											
                    										</tr>
                    										                   										
                    										<%
                    										           }
                    										 %>
                    										 </table> 
                    										 <%
                    										      }
                    										 %>                 										
                									        
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="left" style="font-size: 15">
						                                      <br><b>������Ϣ<br><br>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="center">
						                                      
											                 <%  
											                        if(disks != null && disks.size()>0)
											                        {
											                 %>
											                 <table>
											                   <tr bgcolor="#ECECEC">
											                  <td width="10%" height="26" align="center">����</td>
											                  <td width="15%" height="26" align="center">״̬</td>
											                  <td width="15%" height="26" align="center">����</td>
											                  <td width="30%" height="26" align="center">����</td>											                  
											                  <td width="5%" height="26" align="center">������</td>
											                  <td width="15%" height="26" align="center">��ʼ������</td>
											                  <td width="10%" height="26" align="center">�̼��汾</td>
											                  </tr>
											                 <%
											                           for(int i=0;i<disks.size();i++)
											                           {
											                               Disk disk = disks.get(i);										                 
											                                if(i%2 == 1)
											                                {
											                 %>
											                    <tr bgcolor="#ECECEC">   
											                 <%
											                             }else{
											                  %>
											                    <tr>
											                  <%
											                             }
											                   %>  
											                   <td width="10%" height="26" align="center"><%=SysUtil.ifNull(disk.getName()) %></td>
											                   <td width="15%" height="26" align="center"><%=SysUtil.ifNull(disk.getStatus()) %></td>
											                   <td width="15%" height="26" align="center"><%=SysUtil.ifNull(disk.getVendorID()) %></td>
											                   <td width="30%" height="26" align="center"><%=SysUtil.ifNull(disk.getDataCapacity()) %></td>											                  
											                   <td width="5%" height="26" align="center"><%=SysUtil.ifNull(disk.getRedundancyGroup()) %></td>
											                   <td width="15%" height="26" align="center"><%=SysUtil.ifNull(disk.getInitializeState()) %></td>
											                   <td width="10%" height="26" align="center"><%=SysUtil.ifNull(disk.getFirmwareRevision()) %></td>
											                </tr>											                 
                    										<%
                    										           }
                    										 %>
                    										 </table> 
                    										 <%
                    										      }
                    										 %>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="left" style="font-size: 15">
						                                      <br><b>�߼���Ԫ<br><br>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="center">                                   
											              
											                 <%  
											                        if(luns != null && luns.size()>0)
											                        {
											                  %>
											                  <table>
											                <tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="center">����</td>
                      											<td width="20%" height="26" align="center">������</td>                      											
                      											<td width="15%" height="26" align="center">�Ƿ�</td>
                      											<td width="20%" height="26" align="center">��������</td>
                      											<td width="30%" height="26" align="center">ȫ������</td>	
                    										</tr>
											                  <%
											                           for(int i=0;i<luns.size();i++)
											                           {
											                               Lun lun = luns.get(i);
											                                if(i%2 == 1)
											                                {
											                 %>
											                    <tr bgcolor="#ECECEC">   
											                 <%
											                             }else{
											                  %>
											                    <tr>
											                  <%
											                             }
											                   %>										               											                
                      											<td width="15%" height="26" align="center"><%=SysUtil.ifNull(lun.getName()) %></td>
                      											<td width="20%" height="26" align="center"><%=SysUtil.ifNull(lun.getRedundancyGroup()) %></td>                      											
                      											<td width="15%" height="26" align="center"><%=SysUtil.ifNull(lun.getActive()) %></td>
                      											<td width="20%" height="26" align="center"><%=SysUtil.ifNull(lun.getDataCapacity()) %></td>
                      											<td width="30%" height="26" align="center"><%=SysUtil.ifNull(lun.getWwn()) %></td>	
                    										</tr>									                 
                    										                  										
                    										<%
                    										           }
                    										 %>
                    										 </table> 
                    										 <%
                    										      }
                    										 %>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="left" style="font-size: 15">
						                                      <br><b>VFP��Ϣ<br><br>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="center">
						                                      										           
											                 <%  
											                        if(vfps != null && vfps.size()>0)
											                        {
											                  %>
											                  <table>
											              <tr bgcolor="#ECECEC">
											                <td width="30%" height="26" align="center">����:</td>
                      									    <td width="40%" height="26" align="center">������</td>	
															<td width="30%" height="26" align="center">��ҳֵ</td>                      										
											              </tr>
											                  <%
											                           for(int i=0;i<vfps.size();i++)
											                           {
											                               VFP vfp = vfps.get(i);
											                                if(i%2 == 1)
											                                {
											                 %>
											                    <tr bgcolor="#ECECEC">   
											                 <%
											                             }else{
											                  %>
											                    <tr>
											                  <%
											                             }
											                   %> 											                
											                   <td width="30%" height="26" align="center"><%=SysUtil.ifNull(vfp.getName()) %></td>
                      									       <td width="40%" height="26" align="center"><%=SysUtil.ifNull(vfp.getVFPBaudRate()) %></td>	
															   <td width="30%" height="26" align="center"><%=SysUtil.ifNull(vfp.getVFPPagingValue()) %></td>                      										
											               </tr>                    										                    										                  										
                    										<%
                    										           }
                    										 %>
                    										 </table> 
                    										 <%
                    										      }
                    										 %>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="left" style="font-size: 15">
						                                      <br><b>��ϵͳ��Ϣ<br><br>
						                                   </td>
						                                 </tr>
						                                 <tr>
						                                   <td align="center">
						                                      
											                 <%  
											                        if(subSystemInfo != null)
											                        {
											                           
											                  %>
											                  <table>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;RAIDģʽ:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getRaidLevel())%> </td>	
																<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�Զ���ʽ������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getAutoFormatDrive())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��ֱ���:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getHangDetection())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������ٽ�ֵ:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getCapacityDepletionThreshold())%> </td>
                    										</tr>  
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��ֱ���:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getHangDetection())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;��������ٽ�ֵ:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getCapacityDepletionThreshold())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�������ٽ�ֵ:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getQueueFullThresholdMaximum())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�Ż�����:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getEnableOptimizePolicy())%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�ֶ�����:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getEnableManualOverride())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ֶ�����Ŀ��:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getManualOverrideDestination())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;���治�ܶ�:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getReadCacheDisable())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ؽ����ȼ�:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getRebuildPriority())%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�Ƿ񼤻ȫ����:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getSecurityEnabled())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�ػ������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getShutdownCompletion())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;��ϵͳ���ͱ��:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getSubsystemTypeID())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�Ƿ���������ע:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getUnitAttention())%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�Ƿ����л���:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getVolumeSetPartition())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;���治��д:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getWriteCacheEnable())%> </td>
                    										</tr>
                    										<tr bgcolor="#ECECEC">
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;д���:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getWriteWorkingSetInterval())%> </td>                      											
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�Ƿ���Ԥȡ:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getEnablePrefetch())%> </td>
                    										</tr>
                    										<tr>
                      											<td width="15%" height="26" align="left" nowrap class=txtGlobal >&nbsp;�رյڶ�·������:</td>
                      											<td width="35%"><%=SysUtil.ifNull(subSystemInfo.getDisableSecondaryPathPresentation())%> </td>                      											
                      											<!-- <td width="15%" height="26" align="left" nowrap class=txtGlobal>&nbsp;�Ƿ���Ԥȡ:</td>
                      											<td width="35%"><=subSystemInfo.getEnablePrefetch()%> </td> -->
                    										</tr> 
                    										</table>                										                  										
                    										<%
                    										         
                    										      }
                    										 %>                									    
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
			
            		<div align=center>
            			<input type=button value="�رմ���" onclick="window.close()">
            		</div>  
					<br>
</form>
</body>
</html>