<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String rootPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rootPath+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>�����ϴ�</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">
	  var isIE = /msie/i.test(navigator.userAgent) && !window.opera;         
  function fileChange(target) {     
      
    var fileSize = 0;          
    if (isIE && !target.files) {      
      var filePath = target.value;      
      var fileSystem = new ActiveXObject("Scripting.FileSystemObject");         
     var file = fileSystem.GetFile (filePath);      
      fileSize = file.Size;     
    } else {     
     fileSize = target.files[0].size;      
     }    
     var size = fileSize / 1024;     
     if(size>100000){   
    
      alert("���ܴ���100M");
       document.getElementById("upload").disabled=true; 
       
        
    }else{
     document.getElementById("upload").disabled=false;
    }
        
}  
  
	
		/**
	*�����չ��
	*/
		function check(){
			var upfilename=document.uploadform.files.value;
			if(upfilename==""){
			   alert("�㻹û��ѡ����Ҫ�ϴ����ļ�!");
			   return false;
			  } 
			 
			  var filetype=upfilename.substring(upfilename.indexOf(".")+1,upfilename.length).toLowerCase();
			  var fileName=upfilename.substring(0,upfilename.indexOf("."));
			  /**
				*�����ϴ�����
				*/
			  if(filetype!="flv"&& filetype!="avi"&&filetype!="asx"&&filetype!="asf"&& filetype!="mp4"&&filetype!="mov"&&
			  		filetype!="3gp"&& filetype!="wmv"&&filetype!="mpg"&&filetype!="rmvb"&& filetype!="rm"&&filetype!="wmv9")
			  {
			     alert("ֻ֧��flv,avi,asx,asf,mp4,mov,3gp,wmv,mpg,rmvb,rm,wmv9�ļ�!!!");
			     return false;
			  }
			  
			  window.uploadform.action="<%=rootPath%>/config/vpntelnet/mediaPlayer/upload.jsp";
			  window.uploadform.submit();
		}	
	</script>
  </head>
  
  <body>
  	<form method="post" enctype="multipart/form-data" target="_self" name="uploadform">
    <h2 align="center">ѡ����Ҫ�ϴ�����Ƶ</h2> <br>
    ������<input type="file" id="files1" name="files" onchange="fileChange(this);">
    	<input type="button" value="�ϴ�" align="middle" id="upload" onclick="check()">
    	<br><br>
    	<div align="center"><font color="red" style="font-size: 13">&nbsp;��Ƶ��ʽ֧��flv,avi,asx,asf,mp4,mov,3gp,wmv,mpg,rmvb,rm,wmv9����</font></div>
  	</form>
  </body>
</html>
