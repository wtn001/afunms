<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String rootPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rootPath+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>文件加载</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript">
		/**
	*检查扩展名
	*/
		function check(){
			var upfilename=document.uploadform.files.value;
			if(upfilename==""){
			   alert("你还没有选择你要加载的文件!");
			   return false;
			  } 
			   
			  
			  var filetype=upfilename.substring(upfilename.indexOf(".")+1,upfilename.length).toLowerCase();
			  var fileName=upfilename.substring(upfilename.lastIndexOf("\\"),upfilename.indexOf("."));
			   var reg=null;
			   var exp=/.*[\u4e00-\u9fa5]+.*$/;
                     reg = fileName.match(exp);
                     if(reg!=null){
                      alert("为保证能正常打开文件，文件名不要包含中文字符");
                      return;
					 }
			   
			  /**
				*限制上传类型
				*/
			  if(filetype!="xls")
			  {
			     alert("只支持xls文件!!!");
			     return false;
			  }
			  var files=document.getElementById("files").value;
			  $.ajax({
			type:"POST",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=importExcel&files="+encodeURIComponent(files)+"&nowtime="+(new Date()),
			success:function(data){
				if(data.isSuccess=="1"){
				alert("操作成功");
				window.parent.opener.location="<%=rootPath%>/link.do?action=list&jp=1";
			    window.parent.close();
			   }else{
			   alert("操作成功");
			   }
			}
		});
			//  window.uploadform.action="<%=rootPath%>/link.do?action=importExcel";
			 // window.uploadform.submit();
			   
			
		}	
	</script>
  </head>
  
  <body>
  	<form method="post"  name="uploadform">
    <h2 align="center">选择您要加载的文件</h2> <br>
    方案：<input type="file" id="files" name="files" size="40" maxlength="250">
    	<input type="button" value="加载" align="middle" onclick="check()" >
    	<br><br>
    	<div align="center"><font color="red" style="font-size: 13">&nbsp;文件格式必须为xls类型,否则无法读取</font></div>
  	</form>
  </body>
</html>
