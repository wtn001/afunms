<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
    String fresh = (String)request.getAttribute("fresh");  
    String xml = (String)request.getAttribute("xml");  
    if("fresh".equals(fresh)){
        response.sendRedirect(request.getContextPath() + "/topology/machineUpAndDown/showMap.jsp?xml="+xml);    
    }
%>