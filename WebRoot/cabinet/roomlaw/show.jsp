<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.*"%>
<%
String rootPath = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>�����Ķ�</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<%=rootPath%>/config/knowledges/AC_PDF.js" language="javascript"></script>

	<script language="javascript" type="text/javascript">
        check = isAcrobatPluginInstall();
		if(!check)
		{
		    if(window.confirm("���Ļ�����û�а�װAcrobat Reader���Ƿ����ڰ�װ��")) 
		    {
				window.open( "http://www.adobe.com/cn/products/reader/")
			} 
			else
			{
			    alert("���Ļ���û�а�װ Acrobat Reader �� ���ܾͲ���������ʾ������ĵ�");
			}
		}
    </script>

  </head>
  
  <body>
  <%
  	String name=(String)request.getParameter("attachfiles");
  	String  result = new String(name.getBytes("ISO-8859-1"),"GBK");
  %>
    <h3>3D���� >> �������� >> �������ù��� >>�鿴�ļ�</h3>
    <hr/>
     <br>
     
     <script type="text/javascript">
        var ua = navigator.userAgent.toLowerCase();
        var isIE  = (ua.indexOf("msie") != -1) ? true : false;
        if(!isIE)
        {
            document.write("pdf���ݲ���������ʾ����Ҫ��IE6.0��6.0���ϵ������<br>");
        }
     </script>
        <embed width="800" height="580" src="<%=rootPath%>/cabinet/roomlaw/pdf/<%=result%>"  vmmode="transparent"></embed>
         <br>
     
     <br>
  </body>
</html>
