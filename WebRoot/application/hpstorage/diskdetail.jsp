<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%@page import="com.afunms.common.util.ShareData"%>
<%@page import="com.afunms.polling.node.Disk"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%
String rootPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rootPath+"/";
String id = request.getParameter("id");
String ip = request.getParameter("ip");
List<Disk> disks = null;
Disk disk = null;
Hashtable ipAllData = null;
ipAllData = (Hashtable) ShareData.getSharedata().get("hpstorage:" + ip);
if(ipAllData != null){
	disks = (List<Disk>) ipAllData.get("disks");
	disk = disks.get(Integer.parseInt(id));
}

%>
<html>
	<head>
	    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	    <link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />	       
		<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script language="JavaScript" type="text/javascript">
	function toclose()
	{	   
	   window.close();
	}	
	</script>

	</head>

	<body>
		<table class="detail-data-body">
			<tr bgcolor="#ECECEC">
				<td colspan="4" align="left">
					<font style="font-weight: bold">������ϸ��Ϣ:</font>
				</td>
			</tr>
		<%
          if(disk != null)
          {
       %>
			<tr>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��������:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getName())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;����״̬:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getStatus())%>
				</td>
			</tr>
			<tr bgcolor="#ECECEC">
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��������:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getDiskState())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��������:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getVendorID())%>
				</td>
			</tr>
			<tr>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��Ʒ���:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getProductID())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��Ʒ�汾:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getProductRevision())%>
				</td>
			</tr>
			<tr bgcolor="#ECECEC">
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��������:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getDataCapacity())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;�鳤��:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getBlockLength())%>
				</td>
			</tr>
			<tr>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��ַ:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getAddress())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;�ڵ�ȫ������:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getNodeWWN())%>
				</td>
			</tr>
			<tr bgcolor="#ECECEC">
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;��ʼ������:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getInitializeState())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;������:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getRedundancyGroup())%>
				</td>
			</tr>
			<tr>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;�����к�:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getVolumeSetSerialNumber())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;���к�:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getSerialNumber())%>
				</td>
			</tr>
			<tr bgcolor="#ECECEC">
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;�̼��汾:
				</td>
				<td width="35%">
					<%=SysUtil.ifNull(disk.getFirmwareRevision())%>
				</td>
				<td width="15%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;
				</td>
				<td width="35%">
					&nbsp;
				</td>
			</tr>
			<%
           }
        %>
        <tr>          
          <td colspan="4" align="center">
			 <input type="button" value="�ر�" onclick="toclose()" />			 
		  </td>          
        </tr>          
	   </table>
	</body>
</html>
