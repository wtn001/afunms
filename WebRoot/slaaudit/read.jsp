<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.common.util.SessionConstant" %>
<%@page import="com.afunms.slaaudit.model.SlaAudit"%>

<%
	SlaAudit vo = (SlaAudit)request.getAttribute("vo");
	String username = (String)request.getAttribute("username");
	String ipaddress = (String)request.getAttribute("ipaddress");
   String rootPath = request.getContextPath();     
        User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER); 
%>
<html><head>
<title>SLA���������Ϣ</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="Pragma" content="no-cache">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

</head>
<BODY  id="body" class="body" background="<%=rootPath%>/resource/image/bg6.jpg">
<form id="mainForm" method="post" name="mainForm">
<table>
	<tr>
		<td width="16">��</td>
		<td align="center">
		<br>
		<table width="100%" border=0 cellpadding=0 cellspacing=0>
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
				<table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">�Զ��� >> CISCO SLA���� >> SLA���������ϸ��Ϣ</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>

			<tr>
			<td colspan="2">
				  <table cellpadding="0" cellspacing="1" width="100%" id="detail-content-body" class="detail-content-body">
						<tr style="background-color: #ECECEC;">
						    <TD nowrap align="right" height="25" width="10%">IP��ַ&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<%=ipaddress%></TD>
							<TD nowrap align="right" height="25">SLA����&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=vo.getSlatype()%></TD>	
						</tr>	
						<tr >	
							<TD nowrap align="right" height="25">����&nbsp;</TD>	
							<%
																		if("add".equals(vo.getOperation())){
																	%>
																	
																	<td  class="body-data-list" style="align:left"><img src="<%=rootPath%>/img/correct.gif">&nbsp;����</td>
																	<%
																		}else{
																	%>
																	<td class="body-data-list" style="align:left"><img src="<%=rootPath%>/img/error.gif">&nbsp;ɾ��</td>
																	<%}
																	int dostatus = vo.getDostatus(); 
				                                                       String statusStr = "<font color=red>ʧ��</font>";     
				                                                       if(1==dostatus) statusStr = "�ɹ�"; 
																	
																	%>									
							<TD nowrap align="right" height="25">����״̬&nbsp;</TD>
							<TD nowrap>&nbsp;<%=statusStr%></TD>	
						</tr>
						
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="25">����ʱ��&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=vo.getDotime()%></TD>								
							<TD nowrap align="right" height="25">�����û�&nbsp;</TD>				
							<TD nowrap>&nbsp;<%=username%></TD>	
						</tr>
						<tr>
						    <TD nowrap align="right" height="25" width="10%">��������&nbsp;</TD>				
							<TD nowrap width="90%" colspan=3>&nbsp;
							<textarea id="commands" name="commands" rows="10" cols="80"><%=(vo.getCmdcontent())%></textarea>
							</TD>
						</tr>
						
						
						<tr style="background-color: #FFFFFF;">
							<TD nowrap colspan="4" align="center">
								<br>
								<input type="reset"  style="width:50" value="�ر�" onclick="javascript:window.close()">
							</TD>	
						</tr>						
				</TABLE>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</form>	
</body>
</html>
