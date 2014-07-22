<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="java.lang.*"%>




<%
	String menuTable = (String) request.getAttribute("menuTable");//�˵�
	Hashtable allData = (Hashtable) request.getAttribute("allData");
	String rootPath = request.getContextPath();

	String data = "";
	int allCount = 0;
	int exactCount = 0;
	int wrongCount = 0;
	int disabledCount = 0;
	List deviceList = null;
	if (allData != null) {

		if (allData.containsKey("deviceList"))
			deviceList = (List) allData.get("deviceList");
		if (allData.containsKey("deviceVec")) {
			Vector vector = (Vector) allData.get("deviceVec");
			allCount = (Integer) vector.get(0);
			exactCount = (Integer) vector.get(1);
			wrongCount = (Integer) vector.get(2);
			disabledCount = (Integer) vector.get(3);
		}
	}
	data = "<pie><slice title='Υ��'>" + wrongCount
			+ "</slice><slice title='˳��'>" + exactCount
			+ "</slice><slice title='������'>" + disabledCount
			+ "</slice></pie>";

	String common = "<img src='/afunms/img/common.gif'>";
	String correct = "<img src='/afunms/img/correct.gif'>";
	String serious = "<img src='/afunms/img/serious.gif'>";
	String urgency = "<img src='/afunms/img/urgency.gif'>";
	String statusImg = "<img src='/afunms/img/correct.gif'>";
	String disable = "<img src='/afunms/img/blue.gif'>";
	String error = "<img src='/afunms/img/error.gif'>";
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
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script language="JavaScript" type="text/javascript">
 function viewDetail(id,ip)
  {
    var url="<%=rootPath%>/configRule.do?action=viewDetail&id="+id+"&ip="+ip;
	window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
  }  
  function setStrategy(){
  location.href="<%=rootPath%>/configRule.do?action=strategyList"
  } 
  function setGroup(){
  location.href="<%=rootPath%>//configRule.do?action=groupRuleList"
  } 
  function setRule(){
  location.href="<%=rootPath%>/configRule.do?action=ruleDetailList"
  } 
  
 function showDetail(id,divId){

document.getElementById(id).innerHTML="<span  onclick=\"hiddenDetail(\'"+id+"\',\'"+divId+"\')\" >-&nbsp;</span>";
document.getElementById(divId).style.display = "block";

}
function hiddenDetail(id,divId){
document.getElementById(id).innerHTML="<span  onclick=\"showDetail(\'"+id+"\',\'"+divId+"\')\">&nbsp;+&nbsp;&nbsp;</span>";
document.getElementById(divId).style.display = "none";

}
function showRule(strategyId,groupId,ruleId,ip,isVor){
 var url="<%=rootPath%>/configRule.do?action=showRule&strategyId="+strategyId+"&groupId="+groupId+"&ruleId="+ruleId+"&ip="+ip+"&isVor="+isVor;
	CreateWindow(url);
}
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow", "height=450, width=750, toolbar= no, menubar=no, scrollbars=no, resizable=no, location=no, status=no,top=100,left=300")

} 
function exportReport(type){
 var url="<%=rootPath%>/configRule.do?action=downloadReport&type="+type;
 window.open(url,"_blank","toolbar=no,width=1,height=1,top=2000,left=3000,directories=no,status=no,menubar=no,alwaysLowered=yes");
}
</script>
	</head>

	<body id="body">
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
						<table id="container-main" class="container-main">
							<tr>
								<td>
									<table id="container-main-detail" class="container-main-detail">
										<tr>

											<td>
												<table id="detail-content" class="detail-content">
													<tr>
														<td>
															<table width="100%"
																background="<%=rootPath%>/common/images/right_t_02.jpg"
																cellspacing="0" cellpadding="0">
																<tr>
																	<td align="left">
																		<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																			width="5" height="29" />
																	</td>
																	<td class="layout_title">
																		<b>�Զ���>> �����ļ�����>> �Ϲ��Թ��� >> ���в���</b>
																	</td>
																	<td align="right">
																		<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																			width="5" height="29" />
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-body"
																class="detail-content-body">
																<tr>
																	<td>
																		<table cellpadding=0 width=100% align=center
																			algin="center">
																			<tr>
																				<td>
																					<table id="container-main" class="container-main">
																						<tr>

																							<td colspan="1" width="94%">
																								<table id="win-content-body"
																									class="win-content-body">
																									<tr>

																										<td>
																											<div>
																												<table bgcolor="#ECECEC">
																													<tr align="left" valign="middle">
																														<td height="21" align="left" valign=top>

																															&nbsp;
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

																							<td align="center" valign=top>
																								<table width="98%" cellpadding="0"
																									cellspacing="0" algin="center">

																									<tr bgcolor="#FFFFFF">
																										<td>
																											<table width="100%" cellpadding="0"
																												cellspacing="1">
																												<tr>

																													<td bgcolor="#FFFFFF" width="100%"
																														align=center>
																														<table align=center border=1 width="100%">
																															<tr>
																																<td width="40%" align=center>
																																	<table>
																																		<tr>
																																			<td height="36" align=center>
																																				���в��Ե��豸��&nbsp;&nbsp;<%=exactCount + wrongCount + disabledCount%></td>
																																		</tr>
																																		<tr>
																																			<td height="36" align=center>
																																				�Ϲ���Ե��豸��&nbsp;&nbsp;<%=exactCount%></td>
																																		</tr>
																																		<tr>
																																			<td height="36" align=center>
																																				Υ�����Ե��豸��&nbsp;&nbsp;<%=wrongCount%></td>
																																		</tr>
																																		<tr>
																																			<td height="36" align=center>
																																				���ݲ������豸��&nbsp;&nbsp;<%=disabledCount%></td>
																																		</tr>
																																	</table>

																																</td>

																																<td width="60%" align=center>
																																	<div id="strategyPie" align=center>

																																	</div>
																																	<script type="text/javascript"
																																		src="<%=rootPath%>/include/swfobject.js"></script>
																																	<script type="text/javascript">
													<% if(wrongCount==0&&exactCount==0&&disabledCount==0){%>
													 var _div=document.getElementById("strategyPie");
														  var img=document.createElement("img");
																img.setAttribute("src","<%=rootPath%>/resource/image/nodata.gif");
																_div.appendChild(img);
						                            
						                             <%}else{%>
						                                 var so = new SWFObject("<%=rootPath%>/amchart/ampie.swf",  "ampie", "320", "250", "8", "#FFFFFF");
						                                 so.addVariable("path", "<%=rootPath%>/amchart/");
						                                 so.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/strategyPie.xml"));
						                                 so.addVariable("chart_data","<%=data%>");
						                                 so.write("strategyPie"); 
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
																									<tr>
																										<td colspan="2">
																											<table cellspacing="1" cellpadding="0"
																												width="100%">
																												<tr class="microsoftLook0" height=28>

																													<td width='5%'
																														class="detail-data-body-title">
																														���
																													</td>

																													<td width='15%'
																														class="detail-data-body-title">
																														IP��ַ
																													</td>
																													<td width='10%'
																														class="detail-data-body-title">
																														�Ϲ���״̬
																													</td>
																													<td width='15%'
																														class="detail-data-body-title">
																														Υ��������
																													</td>
																													<td width='10%'
																														class="detail-data-body-title">
																														�Ϲ��Թ�����
																													</td>
																													<td width='17%'
																														class="detail-data-body-title">
																														��������
																													</td>
																													<td width='18%'
																														class="detail-data-body-title">
																														�ϴμ��ʱ��
																													</td>

																												</tr>
																												<%
																													int count = 0;
																													String disStr = "";
																													String enStr = "";
																													if (deviceList != null && deviceList.size() > 0) {
																														for (int i = 0; i < deviceList.size(); i++) {
																															CheckResult result = (CheckResult) deviceList.get(i);

																															if (result.getCount0() > 0 || result.getCount1() > 0
																																	|| result.getCount2() > 0) {

																																statusImg = "<img src='/afunms/img/error.gif'>";
																															} else {
																																statusImg = "<img src='/afunms/img/correct.gif'>";
																															}
																															if (result.getStatus().equals("������")) {
																																statusImg = "<img src='/afunms/img/blue.gif'>";
																																disStr = "[������]";
																																enStr = "[������]";
																															} else {
																																disStr = common + result.getCount0() + "��" + serious
																																		+ " " + result.getCount1() + " " + urgency
																																		+ " " + result.getCount2();
																																enStr = correct + " " + result.getExactCount();
																															}
																												%>
																												<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
																													class="microsoftLook">

																													<td height=25 align=center
																														class="detail-data-body-list">
																														<font color='blue'><%=1 + i%></font>
																													</td>
																													<td height=25 align=center
																														class="detail-data-body-list">
																														<span id="show<%=i%>"><span
																															onclick="showDetail('show<%=i%>','group<%=i%>')">&nbsp;&nbsp;+&nbsp;&nbsp;</span>
																														</span><%=result.getIp()%>
																													</td>
																													<td height=25 align=center
																														class="detail-data-body-list"><%=statusImg%><%=result.getStatus()%></td>
																													<td height=25 align=center
																														class="detail-data-body-list"><%=disStr%></td>

																													<td height=25 align=center
																														class="detail-data-body-list"><%=enStr%></td>
																													<td height=25 align=center
																														class="detail-data-body-list"><%=result.getName()%></td>
																													<td height=25 align=center
																														class="detail-data-body-list"><%=result.getCheckTime()%></td>

																												</tr>

																												<tr>
																													<td colspan=7>
																														<div id="group<%=i%>"
																															style="display: none;">
																															<table>
																																<tr>
																																	<td nowrap align="center" height="25"
																																		width="20%">
																																		&nbsp;<%=error%>
																																		&nbsp;Υ���Ĺ���
																																	</td>
																																	<td width="80%" align="left">
																																		&nbsp;&nbsp;
																																	</td>

																																</tr>

																																<%
																																	CheckResultDao resultDao2 = new CheckResultDao();
																																			List list = resultDao2.getReslutByIdAndIp(result.getId()
																																					+ "", result.getIp());
																																			if (list != null && list.size() > 0) {
																																				boolean flag1 = true;
																																				for (int k = 0; k < list.size(); k++) {
																																					String level = "";
																																					String levelStr = "";
																																					CompCheckResultModel model = (CompCheckResultModel) list
																																							.get(k);

																																					if (model.getViolationSeverity() == 2) {
																																						level = "<img src='/afunms/img/urgency.gif'>";
																																						levelStr = "���ص�";
																																					} else if (model.getViolationSeverity() == 1) {
																																						level = "<img src='/afunms/img/serious.gif'>";
																																						levelStr = "��Ҫ��";
																																					} else if (model.getViolationSeverity() == 0) {
																																						level = "<img src='/afunms/img/common.gif'>";
																																						levelStr = "��ͨ��";
																																					}
																																					if (model.getIsViolation() == 0) {
																																%>
																																<tr>
																																	<td nowrap align="center" height="32"
																																		width="10%">
																																		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=level%><%=levelStr%></td>
																																	<td width="90%" height="32"
																																		align="left">
																																		&nbsp;&nbsp;
																																		<a href="#"
																																			onclick="showRule('<%=model.getStrategyId()%>','<%=model.getGroupId()%>','<%=model.getRuleId()%>','<%=model.getIp()%>','0')"><%=model.getRuleName()%></a>
																																		[<%=model.getGroupName()%>]-
																																		<%=model.getDescription()%></td>

																																</tr>
																																<%
																																	} else if (model.getIsViolation() == 1) {
																																						if (flag1) {
																																							flag1 = false;
																																%>
																																<tr>
																																	<td nowrap align="center" height="32"
																																		width="20%">
																																		&nbsp;<%=correct%>&nbsp; �Ϲ�Ĺ���
																																	</td>
																																	<td height="32" width="80%"
																																		align="left">
																																		&nbsp;&nbsp;
																																	</td>

																																</tr>
																																<%
																																	}
																																%>
																																<tr>
																																	<td nowrap align="center" height="32"
																																		width="10%">
																																		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=level%><%=levelStr%></td>
																																	<td height="32" width="90%"
																																		align="left">
																																		&nbsp;&nbsp;
																																		<a href="#"
																																			onclick="showRule('<%=model.getStrategyId()%>','<%=model.getGroupId()%>','<%=model.getRuleId()%>','<%=model.getIp()%>','1')"><%=model.getRuleName()%></a>
																																		[<%=model.getGroupName()%>]-
																																		<%=model.getDescription()%></td>

																																</tr>
																																<%
																																	}
																																				}
																																			}
																																%>
																															</table>

																														</div>
																													</td>
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

		</form>

	</body>
</HTML>
