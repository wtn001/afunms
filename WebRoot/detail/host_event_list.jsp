<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.topology.model.HostNode"%>

<%@page import="com.afunms.common.util.*" %>

<%@ page import="com.afunms.event.model.EventList"%>

<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>

<%@ page import="com.afunms.polling.loader.HostLoader"%>

<%

	String tmp = (String)request.getAttribute("id");
	
	String nodetype = (String)request.getAttribute("nodetype"); 
    
     
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
        
      
		
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";

  	String rootPath = request.getContextPath();    
         
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");

int level1 = Integer.parseInt(request.getAttribute("level1")+"");
int _status = Integer.parseInt(request.getAttribute("status")+"");

String level0str="";
String level1str="";
String level2str="";
String level3str="";
if(level1 == 0){
	level0str = "selected";
}else if(level1 == 1){
	level1str = "selected";
}else if(level1 == 2){
	level2str = "selected";
}else if(level1 == 3){
	level3str = "selected";	
}

String status0str="";
String status1str="";
String status2str="";
if(_status == 0){
	status0str = "selected";
}else if(_status == 1){
	status1str = "selected";
}else if(_status == 2){
	status2str = "selected";	
}      
     String _flag = (String)request.getAttribute("flag"); 
     
     
   	  	   
%>

<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
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
	

</script>
<script>
function createQueryString(){
		var user_name = encodeURI($("#user_name").val());
		var passwd = encodeURI($("#passwd").val());
		var queryString = {userName:user_name,passwd:passwd};
		return queryString;
	}


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
     mainForm.action = "<%=rootPath%>/network.do?action=find&flag=<%=_flag%>";
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

  function query()
  {  
  	mainForm.id.value = "<%=tmp%>";
  	mainForm.nodetype.value ="<%=nodetype%>";
     mainForm.action = "<%=rootPath%>/monitor.do?action=hosteventlist";
     mainForm.submit();
  }
</script>



</head>
<body id="body" class="body" >
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="id">
		<input type=hidden name="flag" value="<%=_flag%>">
		<input type=hidden name="nodetype" value="<%=nodetype%>">
		<table id="body-container" class="body-container">
			<tr>
				
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-detail" width=85%>
								<table id="container-main-detail" class="container-main-detail">
									
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												
											  <tr>
											    	<td>
											    		<table class="detail-data-body">
												      		<tr> 
                				 <td>
                					<table width="100%" border="0" cellpadding="0" cellspacing="1">
                    						
                  					</table>
                  				</td>
                  				</tr>
  						<tr>
  						<td colspan=5 bgcolor="#ECECEC" height="28">
	

		&nbsp;&nbsp;&nbsp;&nbsp;��ʼ����
		<input type="text" name="startdate" value="<%=startdate%>" size="10">
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
	<img id=imageCalendar1 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
	
		��ֹ����
		<input type="text" name="todate" value="<%=todate%>" size="10"/>
	<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
	<img id=imageCalendar2 align=absmiddle width=34 height=21 src=<%=rootPath%>/include/calendar/button.gif border=0></a>
		�¼��ȼ�
		<select name="level1">
		<option value="99">����</option>
		<option value="0" <%=level0str%>>��ʾ��Ϣ</option>
		<option value="1" <%=level1str%>>��ͨ�¼�</option>
		<option value="2" <%=level2str%>>�����¼�</option>
		<option value="3" <%=level3str%>>�����¼�</option>
		</select>
					
		����״̬
		<select name="status">
		<option value="99">����</option>
		<option value="0" <%=status0str%>>δ����</option>
		<option value="1" <%=status1str%>>���ڴ���</option>
		<option value="2" <%=status2str%>>�Ѵ���</option>
		</select>	
	<input type="button" name="submitss" value="��ѯ" onclick="query()"><hr>
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
						
                    <tr> 
                      <td>
  <table class="tab3"  cellSpacing="0"  cellPadding="0" border=0>
	
  <tr bgcolor="#ECECEC" height="28">
    	<td height="28" class="detail-data-body-title"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()"></td>
        <td width="10%" align="center" class="detail-data-body-title"><strong>�¼��ȼ�</strong></td>
    	<td width="40%" align="center" class="detail-data-body-title"><strong>�¼�����</strong></td>
		<td align="center" class="detail-data-body-title"><strong>�Ǽ�ʱ��</strong></td>
    	<td align="center" class="detail-data-body-title"><strong>���澯ʱ��</strong></td>
    	<td align="center" class="detail-data-body-title"><strong>����״̬</strong></td>
    	<td align="center" class="detail-data-body-title"><strong>����</strong></td>
   </tr>
<%
	int index = 0;
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
  	List list = (List)request.getAttribute("list");
  	for(int i=0;i<list.size();i++){
 	index++;
  	EventList eventlist = (EventList)list.get(i);
  	Date cc = eventlist.getRecordtime().getTime();
  	String lastTime=eventlist.getLasttime();
  	Integer eventid = eventlist.getId();
  	String eventlocation = eventlist.getEventlocation();
  	String content = eventlist.getContent();
  	String level = String.valueOf(eventlist.getLevel1());
  	String status = String.valueOf(eventlist.getManagesign());
  	String s = status;
  	String showlevel = level;
  	String act="������";
  	String bgcolor = "";
  	if("0".equals(level)){
  		level="��ʾ��Ϣ";
  		bgcolor = "bgcolor='blue'";
  	}
	if("1".equals(level)){
  		level="��ͨ�澯";
  		bgcolor = "bgcolor='yellow'";
  	}
  	if("2".equals(level)){
  		level="���ظ澯";
  		bgcolor = "bgcolor='orange'";
  	}
  	if("3".equals(level)){
  		level="�����澯";
  		bgcolor = "bgcolor='red'";
  	}
   	String bgcolorstr="";
  	if("0".equals(status)){
  		status = "δ����";
  		bgcolorstr="#9966FF";
  	}
  	if("1".equals(status)){
  		status = "������";  
  		bgcolorstr="#3399CC";	
  	}
  	if("2".equals(status)){
  	  	status = "�������";
  	  	bgcolorstr="#33CC33";
  	}
  	//String rptman = eventlist.getReportman();
  	String rtime1 = _sdf.format(cc);
  	String rtime2 = lastTime.substring(5,16);
%>

 <tr   bgcolor="#FFFFFF" <%=onmouseoverstyle%>>

   	  <td class="detail-data-body-list"><INPUT type="checkbox" name="checkbox" value="<%=eventlist.getId()%>"><%=i+1%></td>
   	  <td class="detail-data-body-list" <%=bgcolor%>><%=level%></td>
      <td class="detail-data-body-list">
      <%=content%></td>
       <td class="detail-data-body-list"> 
      <%=rtime1%></td>
       <td class="detail-data-body-list">
      <%=rtime2%></td>
       <td class="detail-data-body-list" bgcolor=<%=bgcolorstr%>>
      <font color=#ffffff><%=status%></font></td>
       <td class="detail-data-body-list" align="center" >
       <%
       		if("0".equals(s)){
       		%>
       			<input type ="button" value="���ܴ���" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
       		<%
       		}
       		if("1".equals(s)){
       		%>
       			<input type ="button" value="��д����" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
       		<%
       		}
       		if("2".equals(s)){
       		%>
       			<input type ="button" value="�鿴����" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,resizable=yes,scrollbars=yes,screenX=0,screenY=0")'>
       		<%
       		}
       %>
       </td>
 </tr>
 <%}

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
		
	</form>
</BODY>
</HTML>