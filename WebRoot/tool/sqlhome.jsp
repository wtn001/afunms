<%@ page language="java" import="java.util.*" pageEncoding="gb2312"%>
<%
	String path = request.getContextPath();
	String status = request.getParameter("status");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
                    "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/style.css">
		<link rel="stylesheet" type="text/css" href="<%=path%>/common/css/table.css">
		<script language="javascript" src="<%=path%>/js/query.js"></script>
		<script language="javascript" src="<%=path%>/js/jquery.min.js"></script>
		<!-- 	<script type="text/javascript">
		function sizeChange(){
		document.getElementById("frametest").width=frametest.document.body.scrollWidth;
		document.getElementById("frametest").height=frametest.document.body.scrollHeight;
		}
		 function list(){
		 var sql=document.getElementById("name").value;
         document.getElementById("frametest").src="query.jsp?name="+encodeURIComponent(sql)+"&f="+Math.random();
		 }
  
     </script>
     -->
	</head>
	<body>
	<hr color="#007108">
		<table>
			<tr  id="main">
				<td align="left">
					
						<textarea rows="5" cols="55" id="name"></textarea>
						
				</td>
				<td>
					&nbsp;
					<input type="button" value="ִ��SQL���" onclick="querysql('<%=path %>')" />
					
				</td>
			
			</tr>
		</table>
		<!--  <div id="result" align=left></div>-->
		<!-- HONGLI ��div�滻Ϊiframe-->	
		<iframe id="frametest" align=left  style="width: 100%;height: 500px;"></iframe>
		 <iframe id="frametest" align=left frameborder=0 onload="sizeChange()"></iframe>
		
	</body>
</html>
