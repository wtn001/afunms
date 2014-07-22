<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
    String submapXml = request.getParameter("submapview");
    String fullscreen = request.getParameter("fullscreen");
    
    response.sendRedirect(request.getContextPath() + "/topology/machineUpAndDown/showMap.jsp?fullscreen=" + fullscreen + "&xml="+submapXml);    
%>