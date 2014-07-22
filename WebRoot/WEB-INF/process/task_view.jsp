<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程图片</title>
<%
String rootPath=request.getContextPath();
String processDefinitionId=(String)request.getAttribute("processDefinitionId");
String processInstanceId = (String)request.getAttribute("processInstanceId");

%>
<link href="<%=rootPath %>/css/main.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath %>/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/js/main.js"></script>
</head>
<body>
<div >
	<img src="../controller/processImg.action?processDefinitionId=<%=processDefinitionId %>&processInstanceId=<%=processInstanceId %>" style="left:0px;top:0px;">
	
</div> 
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_box">
<tr>
<th>流程实例ID</th>
<th>环节名称</th>
<th>执行者</th>
<th>开始时间</th>
<th>结束时间</th>
<th>状态</th>
</tr>
<c:forEach items="${tasklist}" var="temp">
<tr>
<td>${temp.processInstanceId}</td>
<td>${temp.name}</td>
<td>${temp.assignee}</td>
<td> <fmt:formatDate value="${temp.startTime}" type="both" dateStyle="default" timeStyle="default"/>
</td>
<td>
<fmt:formatDate value="${temp.endTime}" type="both" dateStyle="default" timeStyle="default"/>
</td>
<c:choose>
   <c:when test="${temp.deleteReason=='completed'}">
   <td>已完成</td>
   </c:when>
   <c:otherwise>
       <c:choose>
          <c:when test="${temp.assignee==null}">
          <td>待签收</td>
          </c:when>
           <c:otherwise>
           <td>等待处理</td>
           </c:otherwise>
       </c:choose>
   </c:otherwise>
</c:choose>
</tr>
</c:forEach>
</table>
</body>
</html>