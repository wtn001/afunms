<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>

<%
 String rootPath = request.getContextPath();
%>
<html>
<head>
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="UTF-8" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="UTF-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="UTF-8"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="javascript">
  function doDiscover()
  {
      var chk1 = checkinput("ipaddress1","ip","IP��ַ1",15,false);
      var chk2 = checkinput("ipaddress2","ip","IP��ַ2",15,false);
      var chk3 = checkinput("name1","string","������",30,false);
      var chk4 = checkinput("name2","string","������",30,false);
      if(chk1&&chk2&&chk3&&chk4){
          Ext.MessageBox.wait('��·�����У����Ժ�.. '); 
          mainForm.action = "<%=rootPath%>/linkanalytics.do?action=linkanalytics";
          mainForm.submit();
      }
  } 
  function doDiscoverAll()
  {
        mainForm.action = "<%=rootPath%>/linkanalytics.do?action=analyticsFromNode";
        mainForm.submit();        
  }    
  function fromCore()
  {
      mainForm.core_ip.disabled = false;
      mainForm.community.disabled = false;
      //mainForm.net_ip.disabled = true;
      //mainForm.netmask.disabled = true;
      initmenu();
  }
  
  function fromNet()
  {
      mainForm.core_ip.disabled = true;
      mainForm.community.disabled = true;
      //mainForm.net_ip.disabled = false;
      //mainForm.netmask.disabled = false;
  }
</script>
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/common/images/menubg_report.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" >
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
	
		<td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
				<tr>
					<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
						<table width="100%" cellspacing="0" cellpadding="0">
		                  <tr>
		                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
		                    <td class="layout_title"><b>��·����</b></td>
		                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
		                  </tr>
		              </table>
				  	</td>
				 </tr>				
	
				<tr>
					<td>
						<table width="100%" border=0 cellpadding=0 cellspacing=1>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top">				
					<table cellSpacing="0" cellPadding="0" width="100%" border="0" align='left'>
										
						<tr>
				        										<td>
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1"
																	width="100%">
																<TBODY>
			<tr>						   
			<TD nowrap align="right" height="24" width="20%">IP��ַ1&nbsp;</TD>				
			<TD nowrap width="30%">&nbsp;<input type="text" id="ipaddress1"  name="ipaddress1" size="20" class="formStyle"><font color='red'>*</font></TD>															
			<TD nowrap align="right" height="24" width="20%">������&nbsp;</TD>				
			<TD nowrap width="30%">&nbsp;<input type="text" id="name1" name="name1" size="20" class="formStyle"><font color='red'>*</font></TD>
			<TD nowrap align="right" height="24" width="20%">SNMP�汾&nbsp;</TD>				
			<TD nowrap width="30%" >&nbsp;
								<select   name="version1"  class="formStyle">
									<option value=5>--��ѡ��--</option>
									<option value=0>SNMP&nbsp;V1</option>
									<option value=1>SNMP&nbsp;V2c</option>
									
								</select>
			</TD>						
			</tr>
			<tr style="background-color: #ECECEC;">						
			<TD nowrap align="right" height="24" width="20%">IP��ַ2&nbsp;</TD>				
			<TD nowrap width="30%">&nbsp;<input type="text" id="ipaddress2"  name="ipaddress2" size="20" class="formStyle"><font color='red'>*</font></TD>															
			<TD nowrap align="right" height="24" width="20%">������&nbsp;</TD>				
			<TD nowrap width="30%">&nbsp;<input type="text" id="name2" name="name2" size="20" class="formStyle"><font color='red'>*</font></TD>	
			<TD nowrap align="right" height="24" width="20%">SNMP�汾&nbsp;</TD>
			<TD nowrap width="30%" >&nbsp;
				<select   name="version2"  class="formStyle">
					<option value=5>--��ѡ��--</option>
					<option value=0>SNMP&nbsp;V1</option>
					<option value=1>SNMP&nbsp;V2c</option>
					
				</select>
			</TD>					
			</tr>
			<tr>
				<!--<TD nowrap align="right" height="24"></TD>	-->
				<TD nowrap width="50%" align=center colspan=6>&nbsp;<input type="button" value="��ʼ����" style="width:100" class="formStylebutton" onclick="doDiscover()"></font></TD>	
				<TD nowrap width="50%" align=center colspan=6>&nbsp;<input type="button" value="����ȫ��" style="width:100" class="formStylebutton" onclick="doDiscoverAll()"></font></TD>					
			</tr>	
			<tr>
			<td></td>				
			</tr>		
			</table>			
		</td>
	</tr>
	<tr align="center" valign="center"> 
                    			<td height="28" align="center">
						<textarea id="resultofping" name="showList" rows="15" cols="60" readonly="readonly"></textarea>                    			
                    			</td>
				</tr>  
				<tr>
				<TD nowrap colspan="4" align=center>
				      <div align=center>
            			<input type=button value="�رմ���" onclick="window.close()">
            		</div> 
					</TD>
					<td></td>	
					</tr>
</table>
				</tr>
        							
			    </table>
			  </td>
			</tr>
	      </table>
	  </form>

</BODY>
</HTML>