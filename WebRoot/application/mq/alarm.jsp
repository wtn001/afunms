<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.inform.util.SystemSnap" %>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="java.util.*"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%
	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");
	int level1 = Integer.parseInt(request.getAttribute("level1") + "");
	int _status = Integer.parseInt(request.getAttribute("status") + "");

	String level0str = "";
	String level1str = "";
	String level2str = "";
	String level3str = "";
	if (level1 == 0) {
		level0str = "selected";
	} else if (level1 == 1) {
		level1str = "selected";
	} else if (level1 == 2) {
		level2str = "selected";
	} else if (level1 == 3) {
		level3str = "selected";
	}
	String status0str = "";
	String status1str = "";
	String status2str = "";
	if (_status == 0) {
		status0str = "selected";
	} else if (_status == 1) {
		status1str = "selected";
	} else if (_status == 2) {
		status2str = "selected";
	}
	Integer myId = Integer.parseInt((String) request.getAttribute("id"));
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");

    
	MQConfig queryconf = (MQConfig) request.getAttribute("mqConfig");

	int alarmlevel = 0;
	Node node = (Node) PollingEngine.getInstance().getMqByID(
			queryconf.getId());
	String alarmmessage = "";
	if (node != null) {
		alarmlevel = SystemSnap.getNodeStatus(node); 
		List alarmlist = node.getAlarmMessage();
		if (alarmlist != null && alarmlist.size() > 0) {
			for (int k = 0; k < alarmlist.size(); k++) {
				alarmmessage = alarmmessage
						+ alarmlist.get(k).toString();
			}
		}
	}

	

	String flag_1 = (String) request.getAttribute("flag");
%>
<html>
	<head>
        <meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
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

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
		<script language="JavaScript" type="text/JavaScript">
	function batchAccfiEvent(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_accitevent.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	//batchDoReport();
	function batchDoReport(){
		 var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_doreport.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}
	
	function batchEditAlarmLevel(){
		var eventids = ''; 
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (eventids==""){
	 					eventids=checkbox.value;
	 				}else{
	 					eventids=eventids+","+checkbox.value;
	 				}
 				}
			}
		}
        if(eventids == ""){
        	alert("δѡ��");
        	return ;
        }
		window.open("<%=rootPath%>/alarm/event/batch_editAlarmLevel.jsp?eventids="+eventids,"accEventWindow", "toolbar=no,height=400, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0");
	}

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
	document.getElementById('MQDetailTitle-5').className='detail-data-title';
	document.getElementById('MQDetailTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('MQDetailTitle-5').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

function show_graph(){
      mainForm.action = "<%=rootPath%>/mq.do?action=detail";
      mainForm.submit();
} 
function query()
  {  
     mainForm.action = "<%=rootPath%>/mq.do?action=alarm";
     mainForm.submit();
  }
</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag_1%>">
			<input type="hidden" id="id" name="id" value="<%=myId%>">
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
												<table id="application-detail-content"
													class="application-detail-content">
													<tr>
													<td>
														 <jsp:include page="/topology/includejsp/detail_content_top.jsp">
														 	<jsp:param name="contentTitle" value="MQ��ϸ��Ϣ"/> 
														 </jsp:include>
													</td>
												</tr>												
												<tr>
													<td>
														<table>
									                   <tr bgcolor="#F1F1F1">
									                      <td width="10%" height="26" valign=middle align="right" nowrap  class=txtGlobal>&nbsp;����:&nbsp;</td>
									                      <td width="20%" valign=middle align="left">
									                      <%=queryconf.getName()%>
									                      </td>
									                      <td width="15%" class=txtGlobal align="right" valign=middle nowrap>&nbsp;״̬:&nbsp;</td>
									                      <td width=15% valign=middle ><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>">&nbsp;<%=NodeHelper.getStatusDescr(alarmlevel)%></td>
									                      <td width="15%" height="26" align=right valign=center nowrap   class=txtGlobal>&nbsp;IP��ַ:&nbsp;</td>
									                      <td width="15%">
									                      <%=queryconf.getIpaddress()%>
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
															<%=MQDetailTitleTable%>
														</td>
													</tr>
													<tr>
														<td>
															<table class="detail-data-body">

																<tr>
																	<td colspan=5 height="28" bgcolor="#ECECEC">


																		��ʼ����
																		<input type="text" name="startdate"
																			value="<%=startdate%>" size="10">
																		<a onclick="event.cancelBubble=true;"
																			href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																			<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a> ��ֹ����
																		<input type="text" name="todate" value="<%=todate%>"
																			size="10" />
																		<a onclick="event.cancelBubble=true;"
																			href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																			<img id=imageCalendar2 align=absmiddle width=34
																				height=21 src=<%=rootPath%>/include/calendar/button.gif border=0> </a> �¼��ȼ�
																		<select name="level1">
																			<option value="99">
																				����
																			</option>
																			<option value="0" <%=level0str%>>
																				��ʾ��Ϣ
																			</option>
																			<option value="1" <%=level1str%>>
																				��ͨ�¼�
																			</option>
																			<option value="2" <%=level2str%>>
																				�����¼�
																			</option>
																			<option value="3" <%=level3str%>>
																				�����¼�
																			</option>
																		</select>

																		����״̬
																		<select name="status">
																			<option value="99">
																				����
																			</option>
																			<option value="0" <%=status0str%>>
																				δ����
																			</option>
																			<option value="1" <%=status1str%>>
																				���ڴ���
																			</option>
																			<option value="2" <%=status2str%>>
																				�Ѵ���
																			</option>
																		</select>
																		<input type="button" name="submitss" value="��ѯ"
																			onclick="query()"><hr>
																	</td>
																</tr>
																<tr align="right" bgcolor="#ECECEC">
																	<td><table><tr>
																	<td width="75%">&nbsp;</td>
																	<td width="15" height=15 >&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="���ܴ���" onclick='batchAccfiEvent();'/></td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="" value="��д����" onclick='batchDoReport();'/></td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	<td  height=15>&nbsp;&nbsp;<input type="button" name="submitss" value="�޸ĵȼ�" onclick="batchEditAlarmLevel();">&nbsp;&nbsp;</td>
																	<td width="15" height=15>&nbsp;&nbsp;</td>
																	</tr></table></td>
																</tr>
																<tr bgcolor="#ECECEC">
																	<td>
																		<table cellSpacing="0" cellPadding="0" border=0>

																			<tr>
																				<td class="detail-data-body-title">
																					<INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">
																				</td>
																				<td width="10%" class="detail-data-body-title">
																					<strong>�¼��ȼ�</strong>
																				</td>
																				<td width="40%" class="detail-data-body-title">
																					<strong>�¼�����</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong>����澯ʱ��</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong>�澯����</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong>�鿴״̬</strong>
																				</td>
																				<td class="detail-data-body-title">
																					<strong></strong>
																				</td>
																			</tr>
																			<%
																				int index = 0;
																				java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat(
																						"MM-dd HH:mm");
																				List list = (List) request.getAttribute("list");
																				for (int i = 0; i < list.size(); i++) {
																					index++;
																					EventList eventlist = (EventList) list.get(i);
																					Date cc = eventlist.getRecordtime().getTime();
																					Integer eventid = eventlist.getId();
																					String eventlocation = eventlist.getEventlocation();
																					String content = eventlist.getContent();
																					String level = String.valueOf(eventlist.getLevel1());
																					String my_status = String.valueOf(eventlist.getManagesign());
																					String s = my_status;
																					String act = "������";
																					String bgcolor = "";
																					String levelstr = "";
																					if ("0".equals(level)) {
																						levelstr = "��ʾ��Ϣ";
																						bgcolor = "bgcolor='blue'";
																					}
																					if ("1".equals(level)) {
																						levelstr = "��ͨ�¼�";
																						bgcolor = "bgcolor='yellow'";
																					}
																					if ("2".equals(level)) {
																						levelstr = "�����¼�";
																						bgcolor = "bgcolor='orange'";
																					}
																					if ("3".equals(level)) {
																						levelstr = "�����¼�";
																						bgcolor = "bgcolor='red'";
																					}
																					String bgcolorstr="";
																					if ("0".equals(my_status)) {
																						my_status = "δ����";
																						bgcolorstr="#9966FF";
																					}
																					if ("1".equals(my_status)) {
																						my_status = "������";
																						bgcolorstr="#3399CC";	
																					}
																					if ("2".equals(my_status)) {
																						my_status = "�������";
																						bgcolorstr="#33CC33";
																					}
																					String rptman = eventlist.getReportman();
																					String rtime1 = _sdf.format(cc);
																			%>

																			<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>

																				<td class="detail-data-body-list"><INPUT type="checkbox" name="checkbox" value="<%=eventlist.getId()%>"><%=i+1%></td>
   	 																		   <td class="detail-data-body-list" <%=bgcolor%>><%=levelstr%></td>
																				<td class="detail-data-body-list">
																					<%=content%></td>
																				<td class="detail-data-body-list">
																					<%=rtime1%></td>
																				<td class="detail-data-body-list">
																					<%=rptman%></td>
																				<td class="detail-data-body-list"  bgcolor=<%=bgcolorstr%>>
																					<%=my_status%></td>
																				<td class="detail-data-body-list" align="center">
																					<%
																						if ("0".equals(s)) {
																					%>
																					<input type="button" value="���ܴ���" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
																					<!--<input type ="button" value="���ܴ���" class="button" onclick="accEvent('<%=eventid%>')">-->
																					<%
																						}
																							if ("1".equals(s)) {
																					%>
																					<input type="button" value="��д����" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
																					<!--<input type ="button" value="��д����" class="button" onclick="fiReport('<%=eventid%>')">-->
																					<%
																						}
																							if ("2".equals(s)) {
																					%>
																					<input type="button" value="�鿴����" class="button"
																						onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=600, width= 800, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
																					<!--<input type ="button" value="�鿴����" class="button" onclick="viewReport('<%=eventid%>')">-->
																					<%
																						}
																					%>
																				</td>
																			</tr>
																			<%
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
					<td width=15% valign=top>
				       <jsp:include page="/include/mqtoolbar.jsp">
				            <jsp:param value="<%=queryconf.getId() %>" name="id" />
							<jsp:param value="<%=queryconf.getIpaddress() %>" name="ipaddress" />
				       </jsp:include>
				</td>
				</tr>
			</table>

		</form>

		<script>			
Ext.onReady(function()
{  
	    Ext.get("process").on("click",function(){
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
  mainForm.action = "<%=rootPath%>/jboss.do?action=sychronizeData&id=<%=queryconf.getId()%>&flag=<%=flag%>";
  mainForm.submit();
 });    
});
</script>
		
	</BODY>
</HTML>