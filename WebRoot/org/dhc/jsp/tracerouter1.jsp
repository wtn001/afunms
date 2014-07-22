<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>路由跟踪</title>
<script language="javascript">
	function init(){
		window.resizeTo(600,580);
		window.open('ping.jsp?ip=10.10.1.1','pingwindow','toolbar=no,height=600,width=500');
		window.open('','_top'); 
		window.top.close(1);	
		//setTimeout("self.close()",1000)
	}
	
	 function fun_on_unload(){
	   if(confirm("是否进行TRACEROUTER操作?"))  {    
	   		//window.open('tracerouter_2.jsp','pingwindow','toolbar=no,height=600,width=500');
	   		return true;
 			 //return true;    
  		}else{
  			return true;
  			//window.open('tracerouter_2.jsp','pingwindow','toolbar=no,height=600,width=500');
  		}    
      }
      
      function   IsClose()    
  {    
  if(confirm("是否退出系统?"))    
  {    
  return true;    
  }    
  return  false;    
  }

</script>
</head>
<body onload="init()"  >
路由跟踪具体信息展示
</body>
</html>