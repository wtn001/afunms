<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.initialize.*"%>
<%
	String rootPath = "";
	String picip = "";
	rootPath = request.getParameter("rootPath") == null ? "" : request.getParameter("rootPath");
	picip = request.getParameter("picip") == null ? "" : request.getParameter("picip");
	String pingavg = request.getParameter("pingAvg");
	//�����ｫ����ת��Ϊ���� ����ط�������ʡ�Ըù���
	pingavg = pingavg.replace("%", "");
	pingavg = String.valueOf(Math.round(Float.parseFloat(pingavg)));
	
	String cpuAvg = request.getParameter("cpuAvg");
	cpuAvg = cpuAvg.replace("%", "");
	cpuAvg = String.valueOf(Math.round(Float.parseFloat(cpuAvg)));
	
	CreateMetersPic cmp = new CreateMetersPic();
	cmp.createAvgCpuPic(picip, cpuAvg); //����CPUƽ��ֵ�Ǳ���
	

	StringBuffer dataStr = new StringBuffer();
	dataStr.append("��ͨ;").append(pingavg).append(";false;7CFC00\\n");
	dataStr.append("δ��ͨ;").append(100 - Integer.parseInt(pingavg)).append(";false;FF0000\\n");
	String pathPing = ResourceCenter.getInstance().getSysPath() + "resource\\image\\dashBoardGray.png";
	cmp.createChartByParam(picip, pingavg, pathPing, "��ͨ��", "pingdata");
%>
<table cellPadding=0 cellspacing="0" align="center" border="1"
	bordercolor="#D3D3D3">
	<tr>
		<td width="50%" align="center" valign="top">
			<table align=center cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">
						����ƽ����ͨ��
					</td>
				</tr>
				<tr height=160>
					<td align="center">
						<img
							src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>pingdata.png">
					</td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td width="50%" align="center" valign="top">
			<table align=center cellpadding=0 cellspacing="0" width=100%>
				<tr bgcolor=#F1F1F1 height="29">
					<td align="center">
						����ƽ��CPU������
					</td>
				</tr>
				<tr height=160>
					<td align="center">
						<img
							src="<%=rootPath%>/resource/image/jfreechart/reportimg/<%=picip%>cpuavg.png">
					</td>
				</tr>
				<tr height="7">
					<td align=center>
						<img src="<%=rootPath%>/resource/image/Loading_2.gif">
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>

