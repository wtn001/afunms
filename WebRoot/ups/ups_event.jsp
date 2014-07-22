<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.ShareData"%> 

<%@page import="com.afunms.polling.om.Systemcollectdata"%>
<%@page import="java.util.*"%>   
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@ page import="com.afunms.event.model.EventList"%>   

<%
     String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	 String rootPath = request.getContextPath();
	 String menuTable = (String)request.getAttribute("menuTable");
     HostNode vo  = (HostNode)request.getAttribute("vo");
     String startdate = (String)request.getAttribute("startdate");
     String todate = (String)request.getAttribute("todate");
     //UPSNode upsnode = (UPSNode)PollingEngine.getInstance().getUpsByID(vo.getId());
     Hashtable ipAllData = null;
     if("0".equals(runmodel)){////�ɼ�������Ǽ���ģʽ
    	ipAllData = (Hashtable)ShareData.getSharedata().get(vo.getIpAddress());
     }else{//�ɼ�����ʷ���ģʽ
     	//MgeUpsDao mgeUpsDao = new MgeUpsDao();
     	//try{
     	//	ipAllData = mgeUpsDao.getUpsIpData(vo.getIpAddress());
     	//}catch(Exception e){
     	//	e.printStackTrace();	
     	//} finally{
     	//	mgeUpsDao.close();	
     	//}
     }
     Vector batteryVector = null;
     if(ipAllData != null && ipAllData.containsKey("battery")){
    	 batteryVector = (Vector)ipAllData.get("battery");
     }
     String DCDY = "";
     String DCDL = "";
     String DCSYHBSJ = "";
     String DCWD = "";
     String HJWD = "";
     String sysName = "";
     String SBMS = "";
     if(batteryVector!=null){
         for(int i=0;i<batteryVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)batteryVector.get(i);
             if("DCDY".equalsIgnoreCase(systemdata.getEntity())){
                 DCDY = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCDL".equalsIgnoreCase(systemdata.getEntity())){
                 DCDL = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCSYHBSJ".equalsIgnoreCase(systemdata.getEntity())){
                 DCSYHBSJ = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("DCWD".equalsIgnoreCase(systemdata.getEntity())){
                 DCWD = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("HJWD".equalsIgnoreCase(systemdata.getEntity())){
                 HJWD = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SBMC".equalsIgnoreCase(systemdata.getEntity())){
                 sysName = systemdata.getThevalue()+systemdata.getUnit();
             }
             if("SBMS".equalsIgnoreCase(systemdata.getEntity())){
                 SBMS = systemdata.getThevalue()+systemdata.getUnit();
             }
         }
     }
     Vector systemVector = null;
     if(ipAllData != null && ipAllData.containsKey("system")){
     	 systemVector = (Vector)ipAllData.get("system");
     }
     String sysDescr = "";
     String sysUpTime = "";
     if(systemVector!=null){
         for(int i=0;i<systemVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)systemVector.get(i);
             if("sysDescr".equalsIgnoreCase(systemdata.getSubentity())){
                 sysDescr = systemdata.getThevalue();
             }
             if("sysUpTime".equalsIgnoreCase(systemdata.getSubentity())){
                 sysUpTime = systemdata.getThevalue();
             }
         }
     }
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
								
	String pingconavg ="0";
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
		
	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager=new HostCollectDataManager();
	try{
		ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}
	request.setAttribute("pingconavg", new Double(pingconavg));
	double avgpingcon = (Double)request.getAttribute("pingconavg");
    int percent1 = Double.valueOf(avgpingcon).intValue();
    percent1 = 100;
	int percent2 = 100-percent1;
int level1 = Integer.parseInt(request.getAttribute("level1")+"");
int _status = Integer.parseInt(request.getAttribute("status")+"");

String level1str="";
String level2str="";
String level3str="";
if(level1 == 1){
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
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="gb2312"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="javascript">	
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
	document.getElementById('upsDetailTitle-3').className='detail-data-title';
	document.getElementById('upsDetailTitle-3').onmouseover="this.className='detail-data-title'";
	document.getElementById('upsDetailTitle-3').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
  function query()
  {  
     mainForm.action = "<%=rootPath%>/ups.do?action=event";
     mainForm.submit();
  }
</script>
</head>
<body id="body" class="body" onload="initmenu();">
		<form method="post" name="mainForm">
<input type=hidden name="orderflag">
<input type=hidden name="id" value="<%=vo.getId()%>">
<div id="loading">
	<div class="loading-indicator">
	<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
</div>
<div id="loading-mask" style=""></div>
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>		
      <table id="body-container" class="body-container">
			<tr>
				 <!--  <td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<=menuTable%>
							</td>	
						</tr>
					</table>
				</td>-->
			
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
				 <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;�豸��ǩ:										
										        <span id="lable"><%=vo.getAlias()%></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;ϵͳ����:
											<span id="sysname"><%=vo.getSysName()%></span></td>
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP��ַ:
											<%=vo.getIpAddress()%>
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
											    		<%=upsDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>											      
											    	<td>
											    		<table class="detail-data-body">
												      		  <tr>
                                                                <td align=left height="28" bgcolor="#ECECEC">
																
     ��ʼ����
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
	<input type="button" name="submitss" value="��ѯ" onclick="query()">
						</td>
						</tr> 
                    <tr> 
                      <td>
  <table cellSpacing="0"  cellPadding="0" border=0 height=28>
	
  <tr bgcolor="#ECECEC" height=28>
    	<td class="detail-data-body-title" align="center">&nbsp;</td>
        <td width="10%" class="detail-data-body-title" align="center"><strong>�¼��ȼ�</strong></td>
    	<td width="40%" class="detail-data-body-title" align="center"><strong>�¼�����</strong></td>
		<td class="detail-data-body-title" align="center"><strong>�Ǽ�����</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>�Ǽ���</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>����״̬</strong></td>
    	<td class="detail-data-body-title" align="center"><strong>����</strong></td>
   </tr>
<%
	int index = 0;
  	java.text.SimpleDateFormat _sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");
  	List list = (List)request.getAttribute("list");
  	for(int i=0;i<list.size();i++){
 	index++;
  	EventList eventlist = (EventList)list.get(i);
  	Date cc = eventlist.getRecordtime().getTime();
  	Integer eventid = eventlist.getId();
  	String eventlocation = eventlist.getEventlocation();
  	String content = eventlist.getContent();
  	String level = String.valueOf(eventlist.getLevel1());
  	String status = String.valueOf(eventlist.getManagesign());
  	String s = status;
  	String showlevel = null;
  	String act="��������";
  	if("0".equals(level)){
  		showlevel="��ʾ��Ϣ";
  	}
  	if("1".equals(level)){
  		showlevel="��ͨ�¼�";
  	}
  	if("2".equals(level)){
  		showlevel="�����¼�";
  	}
  	if("3".equals(level)){
  		showlevel="�����¼�";
  	}  	
   	  	if("0".equals(status)){
  		status = "δ����";
  	}
   	  	if("0".equals(status)){
  		status = "δ����";
  	}
  	if("1".equals(status)){
  		status = "������";  	
  	}
  	if("2".equals(status)){
  	  	status = "�������";
  	}
  	String rptman = eventlist.getReportman();
  	String rtime1 = _sdf.format(cc);
%>

 <tr  bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=28>

    <td class="detail-data-body-list" align="center">&nbsp;<%=index%></td>
    <%
    	if("3".equals(level)){
    %>
      <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=red align=center><%=showlevel%>&nbsp;</td>
       <%
       }else if("2".equals(level)){
       %>
       <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=orange align=center><%=showlevel%>&nbsp;</td>
       <%
       }else if("0".equals(level)){
       %>
       <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=blue align=center><%=showlevel%>&nbsp;</td>
       <%
       }else{
       %>
       <td style="border-left:1px solid #EEEEEE;border-bottom:1px solid #EEEEEE;" bgcolor=yellow align=center><%=showlevel%>&nbsp;</td>
       <%
       }
       %>      
      <td class="detail-data-body-list" align="center">
      <%=content%></td>
       <td class="detail-data-body-list" align="center">
      <%=rtime1%></td>
       <td class="detail-data-body-list" align="center">
      <%=rptman%></td>
       <td class="detail-data-body-list" align="center">
      <%=status%></td>
       <td class="detail-data-body-list" align="center">
       <%
       		if("0".equals(s)){
       		%>
       			<input type ="button" value="���ܴ���" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("1".equals(s)){
       		%>
       			<input type ="button" value="��д����" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
       		<%
       		}
       		if("2".equals(s)){
       		%>
       			<input type ="button" value="�鿴����" class="button" onclick='window.open("<%=rootPath%>/alarm/event/accitevent.jsp?eventid=<%=eventid%>","accEventWindow", "toolbar=no,height=400, width= 700, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")'>
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
									</table>
									
							</td>						
							<td class="td-container-main-tool" width="15%">
								<jsp:include page="/ups/upstoolbar.jsp">										
								    <jsp:param value="<%=vo.getIpAddress()%>" name="ipaddress" />
									<jsp:param value="<%=vo.getSysOid()%>" name="sys_oid" />
									<jsp:param value="<%=vo.getId()%>" name="tmp" />
									<jsp:param value="ups" name="category" />
									<jsp:param value="emerson" name="subtype" />
								</jsp:include>
						   </td>
						</tr>					
					</table>
				</td>
			</tr>
		</table>
	</form>
	<script>
			
Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	    Ext.get("process").on("click",function(){
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');   
        mainForm.action = "<%=rootPath%>/ups.do?action=toDetail";
        mainForm.submit();
 });    
});
</script>
	</body>
</html>