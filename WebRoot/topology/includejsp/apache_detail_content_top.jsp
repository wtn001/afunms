<%@page language="java" contentType="text/html;charset=gb2312"%>
<%
 String rootPath = request.getContextPath();
 String name = request.getParameter("name")==null?"":request.getParameter("name");
 String state = request.getParameter("state")==null?"":request.getParameter("state");
 String ip = request.getParameter("ip")==null?"":request.getParameter("ip");
 String imgsrc = request.getParameter("imgsrc")==null?"":request.getParameter("imgsrc");

%>
	<style type="text/css" media="print">
		    .noprint{display:none;}
		</style>
<table width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
	<tr>
	 	<td align="left"><div class="noPrint"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></div></td>
		<td class="layout_title"><font size=2><b>设备详细信息</b></font></td>
	 	<td align="right"><div class="noPrint"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></div></td>
	</tr>
	<tr>
		<td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;名称:
										        <span id="lable"><%=name %></span> </td>
        <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;状态:
						 					<span id="sysname">
<img src='<%=imgsrc%>'>&nbsp;<%=state%></span></td>
        <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP地址:
                                            <span id="sysname"><%=ip%></span>
											</TD>
	</tr>
</table>