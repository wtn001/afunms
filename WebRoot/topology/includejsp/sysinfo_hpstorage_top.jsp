<%@page language="java" contentType="text/html;charset=gb2312"
	pageEncoding="gb2312"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.config.dao.IpAliasDao"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.IpAlias"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%
String rootPath = request.getParameter("rootPath")==null?"":request.getParameter("rootPath");
	String tmp = request.getParameter("tmp") == null ? "" : request.getParameter("tmp");
	Host host = (Host) PollingEngine.getInstance().getNodeByID(Integer.parseInt(tmp));
	int alarmlevel = SystemSnap.getNodeStatus(host);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>

	</head>

	<body>
		<table id="detail-content" class="detail-content" width="100%"
			background="<%=rootPath%>/common/images/right_t_02.jpg"
			cellspacing="0" cellpadding="0">
			<tr>
				<td align="left">
					<img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" />
				</td>
				<td class="layout_title">
					<b>�豸��ϸ��Ϣ</b>
				</td>
				<td align="right">
					<img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" />
				</td>
			</tr>
			<tr>
				<td width="20%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;�豸��ǩ:
					<span id="lable"><%=host.getAlias()%></span>
				</td>
				<td width="20%" height="26" align="left" nowrap class=txtGlobal>
					&nbsp;�豸����:
					<span id="sysname">HP�洢</span>
				</td>
				<td width="20%" height="26" background="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>" class="performance-status">
					 <%=NodeHelper.getStatusDescr(alarmlevel)%>
				</td>
				<td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>
					&nbsp;IP��ַ:
					<%
						IpAliasDao ipdao = new IpAliasDao();
						List iplist = ipdao.loadByIpaddress(host.getIpAddress());
						ipdao.close();
						ipdao = new IpAliasDao();
						IpAlias ipalias = ipdao.getByIpAndUsedFlag(host.getIpAddress(), "1");
						ipdao.close();
						if (iplist == null)
							iplist = new ArrayList();
					%>

					<select name="ipalias<%=host.getIpAddress()%>">
						<option selected><%=host.getIpAddress()%></option>
						<%
							for (int j = 0; j < iplist.size(); j++) {
								IpAlias voTemp = (IpAlias) iplist.get(j);
						%>
						<option
							<%if(ipalias != null && ipalias.getAliasip().equals(voTemp.getAliasip())){ %>
							selected <%} %>><%=voTemp.getAliasip()%></option>
						<%
							}
						%>
					</select>
					[
					<a href="#" style="cursor: hand"
						onclick="modifyIpAliasajax('<%=host.getIpAddress()%>');">�޸�</a> ]
				</td>
			</tr>
		</table>
	</body>
</html>
