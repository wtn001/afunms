<%@page import="org.activiti.engine.impl.pvm.process.ActivityImpl"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程任务表单</title>
<link href="../css/main.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="../js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="../js/main.js"></script>
<%
	String taskId = (String) request.getAttribute("taskId");
	String taskExtId=(String) request.getAttribute("taskExtId");
	List<ActivityImpl> backAIList = (List<ActivityImpl>) request.getAttribute("backAIList");
%>
</head>
<body class="content">
<div class="list_content">
<form action="../controller/taskFormKeySubmit.action" method="post">
    <!--embed-form-->
	<input type="hidden" name="taskId" value="<%=taskId%>">
	<input type="hidden" name="taskExtId" value="<%=taskExtId%>">
	<!-- taskFormType区别是自己填写form表单，还是直接从流程中读取form值 -->
	<input class="buttongreen" type="submit" name="result" value="同意" />
	<input class="buttongreen" type="submit" name="result" value="驳回" />
	<select name="backActivityId">
			<% 
				if (backAIList.size() == 0)
				{
					out.println("<option>无节点</option>");
				} else
				{
					for (ActivityImpl ai : backAIList) 
					{
						out.println("<option value=" + ai.getId() + " >"
								+ ai.getProperty("name") + "</option>");
					}
				}
			%>
		</select> 
</form>
</div>
</body>
</html>