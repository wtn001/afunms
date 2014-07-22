<%@page language="java" contentType="text/html;charset=GB2312"%>
<%
    String fresh = (String)request.getAttribute("fresh");  
    String topoid = (String)request.getAttribute("topoid");  
    String sindex = (String)request.getAttribute("sindex");  
    String filename = (String)request.getAttribute("filename");  
    if("fresh".equals(fresh)){
        response.sendRedirect(request.getContextPath() + "/topology/network/indicatortree.jsp?treeflag=0&fromtopo=true&filename="+filename+"&topoid="+topoid+"&sindex="+sindex);    
    }
%>