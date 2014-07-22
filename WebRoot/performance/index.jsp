<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%
  String rootPath = request.getContextPath();  
  session.setAttribute("current_page","1");
  String menu = request.getParameter("menu");
  if(menu!=null){
     session.setAttribute(SessionConstant.CURRENT_MENU,menu); 
  }
  User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
  String bids[] = current_user.getBusinessids().split(","); 
  String rightFramePath = request.getParameter("rightFramePath");
%>
<html>
<head>
<title></title>    
</head>  
  <frameset rows="*" cols="32,*" frameborder="no" framespacing="0">
	<frame name="tabMenuFrame" SCROLLING="no"  noresize id="tabMenuFrame"  src="tabMenuFrame.jsp"></frame>
	<frame name="tabMenuContent"  id="tabMenuContent" src="tabMenuContent.jsp?treeflag=0&rightFramePath=<%=rightFramePath%>">
	</frame>
  </frameset>
</html>
