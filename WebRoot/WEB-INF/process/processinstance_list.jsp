<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="path" value="${request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程实例列表</title>
</head>
<%
String rootPath=request.getContextPath();
%>
<link href="<%=rootPath %>/css/main.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath %>/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/js/main.js"></script>
<body class="content">
<div class="list_content">
<c:out value="${path }"></c:out>
	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_box">
  		<tr>
          <td>id</td>
        </tr>
 	 	
 	 	<c:forEach items='${piList}' var='process'>
				<tr>
					<td>${process.id}</td>
				</tr>
	   </c:forEach>
  </table>
</div>

</body>
</html>