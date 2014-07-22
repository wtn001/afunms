 <%@ page language="java" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>性能信息</title>
<script type="text/javascript">

		function init(){
			//alert("=====");
			//window.resizeTo(100,100);
			window.open('ping.jsp?ip=10.10.1.1','pingwindow','toolbar=no,height=1000,width=800');
			window.open('','_top'); 
			window.top.close(1);
		}
</script>
</head>
<body onload="init()">
</body>
</html>