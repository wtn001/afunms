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
<%@ page import="com.afunms.polling.om.oraclerac.*"%>

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.monitor.item.base.*"%>
<%@page import="com.afunms.monitor.executor.base.*"%>
<%@page import="com.afunms.application.model.DpConfig"%>
<%@page import="com.afunms.common.util.CreatePiePicture"%>
<%@page import="com.afunms.initialize.*"%>


<%
  String rootPath = request.getContextPath();;
  DpConfig vo  = (DpConfig)request.getAttribute("vo");    
  String id = (String)request.getAttribute("id");
  String flag_1 = (String)request.getAttribute("flag");
  Hashtable returnHash = (Hashtable)request.getAttribute("returnHash");
  String collecttime = "";
  collecttime = (String)returnHash.get("collecttime");
  //if(collecttime.equals("") || collecttime == null){
  //	  collecttime = "δ��ͨ";
  //}
  String starttime1 ="";
  String totime1 = "";
  String pingconavg = "0";
  String picip = CommonUtil.doip(vo.getIpAddress());
  	
  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
  if (starttime1 == null || starttime1.equals("")){
	starttime1 = sdf1.format(new Date()) + " 00:00:00";
  }
  if (totime1 == null || totime1.equals("")){
	totime1 = sdf1.format(new Date()) + " 23:59:59";
  }
  
  Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager = new HostCollectDataManager();
	try {
		ConnectUtilizationhash = hostmanager.getCategory(vo.getId()+""
				, "Ping", "ConnectUtilization",
				starttime1, totime1);
	} catch (Exception ex) {
		ex.printStackTrace();
	}

	if (ConnectUtilizationhash.get("avgpingcon") != null) {
		pingconavg = (String) ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}
	String pathPing = ResourceCenter.getInstance().getSysPath()+ "resource\\image\\dashBoardGray.png";
	 CreateMetersPic cmp = new CreateMetersPic();
	    cmp.createChartByParam(picip , pingconavg , pathPing,"��ͨ��","pingdata"); 
%>
<% String menuTable = (String)request.getAttribute("menuTable");%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

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

		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>


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
	document.getElementById('dpDetailTitle-0').className='detail-data-title';
	document.getElementById('dpDetailTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('dpDetailTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>



<script>
$(document).ready(function(){
	//$("#testbtn").bind("click",function(){
	//	gzmajax();
	//});
setInterval(gzmajax,60000);
});
</script>



	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" id="flag" name="flag" value="<%=flag_1%>">
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
								<td class="td-container-main-application-detail">
									<table id="container-main-application-detail"
										class="container-main-application-detail">
										<tr>
											<td>
												<table class="container-main-application-detail">
													<tr>
														<td> 
															<table id="application-detail-content"
	class="application-detail-content">
	<tr>
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
			 	<jsp:param name="contentTitle" value="������Ϣ"/> 
			 </jsp:include>
		</td>
	</tr>
	<tr>
		<td>
			<table id="application-detail-content-body" class="application-detail-content-body">
				<tr>
					<td>
						<table align=center cellpadding=0 cellspacing="0" width=100%>
							<tr>
								<td width="80%" align="left" valign="top">
									<table>
										<tr>
											<td>
												<table>
													<tr>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;����:
														</td>
														<td width="35%"><%=vo.getAlias() %>
														</td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;IP��ַ:
														</td>
														<td width="35%"><%=vo.getIpAddress() %>
														</td>
													</tr>
													<tr>
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;����:
														</td>
														<td width="35%">hpdp
														</td>
													</tr>
													<tr bgcolor="#F1F1F1">
														<td width="15%" height="26" align="left" nowrap class=txtGlobal>
															&nbsp;�ɼ�ʱ��:
														</td>
														<td width="35%"><%=collecttime %>
														</td>
													</tr>

												</table>
											</td>
											<td width="40%" align="center" valign="top">
												<table  align=center  cellpadding=0 cellspacing="0" width=100%>
													<tr bgcolor=#F1F1F1 height="29">
														<td align="center">����ƽ����ͨ�� </td>
													</tr>
													<tr  height=160>
														<td align="center"> <img src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingdata.png"> </td>
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
		<td>
			 <jsp:include page="/topology/includejsp/detail_content_footer.jsp"/>
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
												<table id="application-detail-data"
													class="application-detail-data">
													<tr>
														<td class="detail-data-header">
															<%=DpDetailTitleTable %>
														</td>
													</tr>
													<tr>
														<td>
															<table class="application-detail-data-body">
																<tr>
																	<td>
																		<table width="100%" cellPadding=0 rules=none
																			border="0" algin="center">
																			<tr>
																				<td align=center>
																					<table border="0" id="table1" cellpadding="0"
																						cellspacing="0" width="80%">
																						
																						<TBODY>
																							<tr style="background-color: #ECECEC;" height=28>
																								<td align=center>
																									&nbsp;
																								</td>
																								<td align=center>
																									SESSION_ID
																								</td>
																								<td align=center>
																									TYPE
																								</td>
																								<td align=center>
																									STATUS
																								</td>
																								<td align=center>
																									USERGROUP
																								</td>
																							</tr>
																							<%
																			                  if(returnHash != null && returnHash.size()>0){
																			                  		Vector v = (Vector)returnHash.get("hpdp");
																			                  		Hashtable hpdp = new Hashtable();
																			                    	for(int i=0;i<v.size();i++){
																			                    		hpdp = new Hashtable();
																			                    		hpdp = (Hashtable)v.get(i);
																		                  %>
																							<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																								height="28">
																								<td class="application-detail-data-body-list"
																									align="center"><%=i+1%></td>
																								<td class="application-detail-data-body-list"
																									align=center>
																									&nbsp;<%=hpdp.get("sessionId")%></td>
																								<td class="application-detail-data-body-list"
																									align=center>
																									&nbsp;<%=hpdp.get("type")%></td>
																								<td class="application-detail-data-body-list"
																									align=center>
																									&nbsp;<%=hpdp.get("status")%></td>
																								<td class="application-detail-data-body-list"
																									align=center>
																									&nbsp;<%=hpdp.get("userGroup")%></td>
																							</tr>
																							<%
																				          			}
																				          		}
																				          	%>
																						</TBODY>
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
					<td  width=13% valign=top align=right>
							<jsp:include page="/include/dptoolbar.jsp">
								<jsp:param value="<%=id %>" name="id" />
								<jsp:param value="dp" name="subtype"/>
							</jsp:include>
							
						</td>
				</tr>
			</table>

		</form>
		
	</BODY>
</HTML>