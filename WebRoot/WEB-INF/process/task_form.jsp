<%@page import="org.activiti.engine.impl.pvm.process.ActivityImpl"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.activiti.engine.form.FormType"%>
<%@page import="org.activiti.engine.form.FormProperty"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<c:set var="path" value="${request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程任务表单</title>
<%
String rootPath=request.getContextPath();
%>
<link href="<%=rootPath %>/css/main.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath %>/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/js/main.js"></script>
<%
	List<FormProperty> fpList = (List<FormProperty>) request.getAttribute("fpList");
	Map formMap = (Map) request.getAttribute("formMap");
	String taskId = (String) request.getAttribute("taskId");
	List<ActivityImpl> backAIList = (List<ActivityImpl>) request.getAttribute("backAIList");
	String taskName = (String) request.getAttribute("taskName");
%>
</head>
<body class="content">
<body class="content">
<div class="list_content">
	<%
		if (taskName != null && taskName.equals("申请")) {
	%><font class="font14">任务名：<%=taskName%></font><br><br>
	<form action="../controller/taskSubmit.action" method="post">
		<input type="hidden" name="taskId" value="<%=taskId%>">
		<%
			for (FormProperty fp : fpList) {
		%><font class="font14">
			<%
				out.print(fp.getName());
			%>
		:</font>
		<%
			if (fp.isRequired()) {
						out.print("<font color=red>*</font>");
					}
					FormType ft = fp.getType();
					if (ft.getName().equals("enum")) {
						out.println("<select id='" + fp.getId() + "' name='"
								+ fp.getId() + "' >");
						Map mapValues = (Map<String, String>) ft.getInformation("values");
						Iterator iterator = mapValues.keySet().iterator();
						while (iterator.hasNext()) {
							String value = (String) iterator.next();
							String label = (String) mapValues.get(value);
							out.println("<option value=" + value + ">" + label
									+ "</option>");
						}
						out.println("</select><br><br>");
					} else {
						out.println("<input type='text' name=" + fp.getId()
								+ " /><br><br>");
					}
		%>
		<%
			}
		} else {
	%><font class="font14"><%=taskName%></font>
	<br>
	<form action="../controller/taskSubmit.action" method="post">
		<input type="hidden" name="taskId" value="<%=taskId%>"> 
		<font class="font14"> 请假天数：</font> <input name="day" value="<%=formMap.get("day")%>" readonly="readonly" /><br><br> 
		<font class="font14"> 请假类型：</font>
		<%
			if (((String) formMap.get("type")).equals("1")) 
			{
					out.print("病假");
			} 
			else 
			{
					out.print("事假");
			}
		%><input type="hidden" name="type"
			value="<%=formMap.get("type")%>" /><br> <br>
		<font class="font14">请假原因：</font><%=formMap.get("reason")%>
		<input type="hidden" name="reason" value="<%=formMap.get("reason")%>" /><br><br>
		<%
			}
		%><input class="buttongreen" type="submit" name="result" value="同意" />
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