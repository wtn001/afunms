<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.ErrorMessage"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
  String ec = request.getParameter("errorcode");
  int errorcode = 0;
  if(ec!=null)
     errorcode = Integer.parseInt(ec);

  String errorInfo = null;
  if(errorcode==-1) //直接取错误信息
     errorInfo = (String)request.getAttribute(SessionConstant.ERROR_INFO);
  else
     errorInfo = ErrorMessage.getErrorMessage(errorcode);
  String rootPath = request.getContextPath();     
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html lang="en-US"><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>ERROR</title>
    <link rel="stylesheet" type="text/css" href="<%=rootPath %>/cmstop-error.css" media="all">
    <script language="JavaScript" type="text/javascript">
  function toLogin()
  {
     parent.location="<%=rootPath%>/login.jsp";
  }
</script>
</head>
<body class="body-bg">
 <%
    	if("对不起,用户名或密码不正确!".equals(errorInfo)){
    %>
      <div class="main">
    <p class="title">非常抱歉，您输入的密码错误！</p>
    <a href="#" class="btn" onclick="<%=rootPath%>/user.do?action=login">返回首页</a>
</div>
      <%
      }else{
      %>
       <div class="main">
    <p class="title">非常抱歉，执行错误！</p>
    <a href="#" class="btn"  onclick="javascript:history.back(1)">返回</a>
</div>
      <%
      }
      %>

</body></html>
