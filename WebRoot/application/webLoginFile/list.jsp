<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%
	String rootPath = request.getContextPath();
  List<webloginConfig> userWebLoginConfigList = (List<webloginConfig>)request.getAttribute("userWebLoginConfigList");
%>
<%
	String menuTable = (String)request.getAttribute("menuTable");
  
   //System.out.println(menuTable);
   if(menuTable.contains("none")){
		menuTable = "<style type=\"text/css\"><!--body {margin-left: 0px;margin-top: 0px;background-color: #ababab;}--></style><table id=\"container-menu-bar1\" class=\"container-menu-bar1\"><tr><td style='vertical-align: top;'><table id=\"0D0A-menu-bar\" class=\"menu-bar\"><tr><td><table id=\"0D0A-menu-bar-header\" class=\"menu-bar-header\"><tr><td align=\"left\"><img src=\"/afunms/resource/image/global/menuBar_header_left.jpg\" width=\"5\" height=\"29\" /></td><td class=\"menu-bar-title\"><div id=\"menu0D0A\"><a href=\"#nojs\" title=\"�۵��˵�\" class=\"on\">���ݿ����</a></div></td><td align=\"right\"><img src=\"/afunms/resource/image/global/menuBar_header_right.jpg\" width=\"5\" height=\"29\" /></td></tr></table><table id=\"0D0A-menu-bar-body\" class=\"menu-bar-body\" title=\"�˵�������\"><tr><td><table id=\"0D0A-menu-bar-body-list\" class=\"menu-bar-body-list\"><tr><td><div id=\"menu0D0Achild\"><ul><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/sjkjs.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/db.do?action=list&jp=1>���ݿ����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/sjklxgl.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/dbtype.do?action=list>���ݿ����͹���</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/oracle_gjsz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/oraspace.do?action=list&jp=1>Oracle�澯����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/sqlserver_gjsz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/sqldbconfig.do?action=list&jp=1>SQLServer�澯����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/db2_gjsz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/db2config.do?action=list&jp=1>DB2�澯����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/sybase_gjsz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/sybaseconfig.do?action=list&jp=1>Sybase�澯����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/mysql_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/mysqlconfig.do?action=list&jp=1>MySql����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/informix_gjsz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/informixspace.do?action=list&jp=1>Informix�澯����</a></td></tr></table></li></ul></div></td></tr></table></td></tr></table></td></tr></table></td></tr><tr><td style='vertical-align: top;'><table id=\"0D0B-menu-bar\" class=\"menu-bar\"><tr><td><table id=\"0D0B-menu-bar-header\" class=\"menu-bar-header\"><tr><td align=\"left\"><img src=\"/afunms/resource/image/global/menuBar_header_left.jpg\" width=\"5\" height=\"29\" /></td><td class=\"menu-bar-title\"><div id=\"menu0D0B\"><a href=\"#nojs\" title=\"�۵��˵�\" class=\"on\">�������</a></div></td><td align=\"right\"><img src=\"/afunms/resource/image/global/menuBar_header_right.jpg\" width=\"5\" height=\"29\" /></td></tr></table><table id=\"0D0B-menu-bar-body\" class=\"menu-bar-body\" title=\"�˵�������\"><tr><td><table id=\"0D0B-menu-bar-body-list\" class=\"menu-bar-body-list\"><tr><td><div id=\"menu0D0Bchild\"><ul><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/ftp_fwjs.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/FTP.do?action=list&jp=1>FTP�������</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/email_fwjs.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/mail.do?action=list&jp=1>Email�������</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/web_fwfujs.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/web.do?action=list&jp=1>WEB���ʷ������</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/dkfwjs.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/pstype.do?action=list>�˿ڷ������</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/tftp_sz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/tftp.do?action=list&jp=1>TFTP�������</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/dhcp.png width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/dhcp.do?action=list&jp=1>DHCP�������</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/web_fwfujs.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/weblogin.do?action=list&jp=1>WEB��½�������</a></td></tr></table></li></ul></div></td></tr></table></td></tr></table></td></tr></table></td></tr><tr><td style='vertical-align: top;'><table id=\"0D0C-menu-bar\" class=\"menu-bar\"><tr><td><table id=\"0D0C-menu-bar-header\" class=\"menu-bar-header\"><tr><td align=\"left\"><img src=\"/afunms/resource/image/global/menuBar_header_left.jpg\" width=\"5\" height=\"29\" /></td><td class=\"menu-bar-title\"><div id=\"menu0D0C\"><a href=\"#nojs\" title=\"�۵��˵�\" class=\"on\">�м������</a></div></td><td align=\"right\"><img src=\"/afunms/resource/image/global/menuBar_header_right.jpg\" width=\"5\" height=\"29\" /></td></tr></table><table id=\"0D0C-menu-bar-body\" class=\"menu-bar-body\" title=\"�˵�������\"><tr><td><table id=\"0D0C-menu-bar-body-list\" class=\"menu-bar-body-list\"><tr><td><div id=\"menu0D0Cchild\"><ul><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/mq_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/mq.do?action=list&jp=1>MQ����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/mq_gjsz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/mqchannel.do?action=list&jp=1>MQ�澯����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/domino_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/domino.do?action=list&jp=1>Domino����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/was_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/was.do?action=list&jp=1>WAS����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/weblogic_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/weblogic.do?action=list&jp=1>Weblogic����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/tomcat_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/tomcat.do?action=list&jp=1>Tomcat����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/iis_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/iis.do?action=list&jp=1>IIS����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/cics_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/cics.do?action=list&jp=1>CICS����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/dns_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/dns.do?action=list&jp=1>DNS����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/iislog_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/iislog.do?action=list&jp=1>IISLog����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/jboss.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/jboss.do?action=list&jp=1>JBOSS����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/apache.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/apache.do?action=list&jp=1>APACHE����</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/weblogic_js.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/tuxedo.do?action=list&jp=1>Tuxedo����</a></td></tr></table></li></ul></div></td></tr></table></td></tr></table></td></tr></table></td></tr><tr><td style='vertical-align: top;'><table id=\"0D0D-menu-bar\" class=\"menu-bar\"><tr><td><table id=\"0D0D-menu-bar-header\" class=\"menu-bar-header\"><tr><td align=\"left\"><img src=\"/afunms/resource/image/global/menuBar_header_left.jpg\" width=\"5\" height=\"29\" /></td><td class=\"menu-bar-title\"><div id=\"menu0D0D\"><a href=\"#nojs\" title=\"�۵��˵�\" class=\"on\">�������</a></div></td><td align=\"right\"><img src=\"/afunms/resource/image/global/menuBar_header_right.jpg\" width=\"5\" height=\"29\" /></td></tr></table><table id=\"0D0D-menu-bar-body\" class=\"menu-bar-body\" title=\"�˵�������\"><tr><td><table id=\"0D0D-menu-bar-body-list\" class=\"menu-bar-body-list\"><tr><td><div id=\"menu0D0Dchild\"><ul><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/wsdjc.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/temperatureHumidity.do?action=list2&jp=1>��ʪ�ȼ��</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/wsdjccspz.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/temperatureHumidity.do?action=list&jp=1>��ʪ�ȼ���������</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/wsdjc.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/ups.do?action=list&jp=1>���������豸</a></td></tr></table></li></ul></div></td></tr></table></td></tr></table></td></tr></table></td></tr><tr><td style='vertical-align: top;'><table id=\"0D0E-menu-bar\" class=\"menu-bar\"><tr><td><table id=\"0D0E-menu-bar-header\" class=\"menu-bar-header\"><tr><td align=\"left\"><img src=\"/afunms/resource/image/global/menuBar_header_left.jpg\" width=\"5\" height=\"29\" /></td><td class=\"menu-bar-title\"><div id=\"menu0D0E\"><a href=\"#nojs\" title=\"�۵��˵�\" class=\"on\">�洢����</a></div></td><td align=\"right\"><img src=\"/afunms/resource/image/global/menuBar_header_right.jpg\" width=\"5\" height=\"29\" /></td></tr></table><table id=\"0D0E-menu-bar-body\" class=\"menu-bar-body\" title=\"�˵�������\"><tr><td><table id=\"0D0E-menu-bar-body-list\" class=\"menu-bar-body-list\"><tr><td><div id=\"menu0D0Echild\"><ul><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/cclx.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/storagetype.do?action=list>�洢���͹���</a></td></tr></table></li><li><table><tr style=\"height:22px;\"><td style=\"height:17px;width:17px;\"><img src=/afunms/resource/image/menu/ccxx.gif width=16 height=16 border=0 ></td><td style=\"padding-left:5px;padding-top:2px;\"><a href=/afunms/storage.do?action=list&jp=1>�洢����</a></td></tr></table></li></ul></div></td></tr></table></td></tr></table></td></tr></table></td></tr></table>";
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link
	href="<%=rootPath%>/common/contextmenu/skins/default/contextmenu.css"
	rel="stylesheet">
<script src="<%=rootPath%>/common/contextmenu/js/jquery-1.8.2.min.js"
	type="text/javascript"></script>
<script src="<%=rootPath%>/common/contextmenu/js/contextmenu.js"
	type="text/javascript"></script>

<script language="JavaScript" type="text/javascript"
	src="<%=rootPath%>/include/navbar.js"></script>
<link
	href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
	rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/weblogin.do?action=delete";
  var listAction = "<%=rootPath%>/weblogin.do?action=list";

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/weblogin.do?action=ready_add";
     mainForm.submit();
  }
  function toDelete()
  {
     if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
     mainForm.action = "<%=rootPath%>/weblogin.do?action=delete";
     mainForm.submit();
     }
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

}

</script>
</head>
<BODY onload="initmenu();">
	<form method="post" name="mainForm">
		<table id="body-container" class="body-container">
			<tr>
				<td width="200" valign=top align=center><%=menuTable%></td>
				<td align="center" valign=top>
					<table style="width:98%" cellpadding="0" cellspacing="0"
						align="center">
						<tr>
							<td background="<%=rootPath%>/common/images/right_t_02.jpg"
								width="100%"><table id="content-header" class="content-header">
									<tr>
										<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
										<td class="content-title">&nbsp;Ӧ�� >> ������� >> WEB�����½�����б�</td>
										<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									</tr>
								</table></td>
						<tr>
							<td>
								<table width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td width="100%" style="text-align: right;" class="body-data-title">
											<a href="#" onclick="toAdd()">���</a>
											<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
										</td>
									</tr>
								</table></td>
						</tr>

						<tr>
							<td colspan="2">
								<table cellspacing="0" cellpadding="0" width="100%">
									<tr class="microsoftLook0" height=28>
										<td width='5%' align="center" class="body-data-title"><INPUT type="checkbox"
											class=noborder name="checkall" onclick="javascript:chkall()">
										</td>
										<td align="center" class="body-data-title">���</td>
										<td align="center" class="body-data-title">����</td>
										<td align="center" class="body-data-title">URL</td>
										<td align="center" class="body-data-title">�ؼ���</td>
										<td align="center" class="body-data-title">��ǰ״̬</td>
										<td align="center" class="body-data-title">�Ƿ���</td>
										<td align="center" class="body-data-title">����</td>
									</tr>
									<%
										if(userWebLoginConfigList.size() > 0){
									    		for(int i=0;i<userWebLoginConfigList.size();i++){
									       				webloginConfig vo = userWebLoginConfigList.get(i);
									       				int alarmlevel = 0;
									       				Node node = (Node)PollingEngine.getInstance().getWebLoginByID(vo.getId());
									       				String alarmmessage = "";
									       				if(node != null){
									       					alarmlevel = SystemSnap.getNodeStatus(node); 
									       					List alarmlist = node.getAlarmMessage();
									       					if(alarmlist!= null && alarmlist.size()>0){
													for(int k=0;k<alarmlist.size();k++){
														alarmmessage = alarmmessage+alarmlist.get(k).toString();
													}
												}
									       				}
									%>
									<tr bgcolor="#FFFFFF" class="microsoftLook">
										<td align="center" class="body-data-list"><INPUT type="checkbox" class=noborder
											name=checkbox value="<%=vo.getId()%>">
										</td>
										<td align="center" class="body-data-list"><font color='blue'><%=1 + i%></font>
										</td>
										<td align="center" class="body-data-list"><%=vo.getAlias()%></td>
										<td align="center" class="body-data-list"><%=vo.getUrl()%></td>
										<td align="center" class="body-data-list"><%=vo.getKeyword()%></td>
										<td align="center" class="body-data-list"><img
											src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>"
											border="0" alt=<%=alarmmessage%>>
										</td>
										<td align="center" class="body-data-list">
											<%
												if(Integer.parseInt(vo.getFlag()) == 0){
											%> &nbsp;δ���� <%
 	}else{
 %> &nbsp;�Ѽ��� <%
 	}
 %>
										</td>
										<td align="center" class="body-data-list"><input type="hidden" id="id"
											name="id" value="<%=vo.getId()%>"> <img class="img"
											src="<%=rootPath%>/resource/image/status.gif" border="0"
											width=15 alt="�Ҽ�����"></td>
									</tr>
									<%
										}
									}
									%>
								</table></td>
						</tr>
						<tr>
							<td background="<%=rootPath%>/common/images/right_b_02.jpg"><table
									width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="left" valign="bottom"><img
											src="<%=rootPath%>/common/images/right_b_01.jpg" width="5"
											height="11" />
										</td>
										<td></td>
										<td align="right" valign="bottom"><img
											src="<%=rootPath%>/common/images/right_b_03.jpg" width="5"
											height="11" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table></td>
			</tr>
		</table>
	</form>
</BODY>
<script language="JavaScript">
	$('.img').contextmenu({
		height:115,
		width:100,
		items : [{
			text :'�޸���Ϣ',
			icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
			action: function(target){
				var id=$($(target).parent()).find('#id').val();
				edit(id);
			}
		},{
			text :'������Ϣ',
			icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif',
			action: function(target){
				var id=$($(target).parent()).find('#id').val();
				detail(id);
			}
		},{
            text :'ȡ������',
            icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelMng.gif',
            action: function(target){
            	var id=$($(target).parent()).find('#id').val();
            	cancelmanage(id);
            }
        },{
            text :'��Ӽ���',
            icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addMn.gif',
            action: function(target){
            	var id=$($(target).parent()).find('#id').val();
                addmanage(id);
            }
        }]
	});	
//�Ҽ��˵�������ʼ
	function showNodeDetails(nodeId){
  	    mainForm.action = "<%=rootPath%>/detail/dispatcher.jsp?id=" + nodeId+"&flag=1&fromtopo=true";
        mainForm.submit();
    }
	function detail(node)
	{
	    location.href="<%=rootPath%>/weblogin.do?action=detail&id="+node;
	}
	function edit(node)
	{
		location.href="<%=rootPath%>/weblogin.do?action=ready_edit&id="+node;
	}
	function cancelmanage(node)
	{
		location.href="<%=rootPath%>/weblogin.do?action=changeMonflag&value=0&id="+node;
	}
	function addmanage(node)
	{
		location.href="<%=rootPath%>/weblogin.do?action=changeMonflag&value=1&id="+node;
	}
</script>
</HTML>
