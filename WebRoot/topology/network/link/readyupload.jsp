<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
String rootPath = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+rootPath+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>�ļ�����</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript">
		/**
	*�����չ��
	*/
		function check(){
			var upfilename=document.uploadform.files.value;
			if(upfilename==""){
			   alert("�㻹û��ѡ����Ҫ���ص��ļ�!");
			   return false;
			  } 
			   
			  
			  var filetype=upfilename.substring(upfilename.indexOf(".")+1,upfilename.length).toLowerCase();
			  var fileName=upfilename.substring(upfilename.lastIndexOf("\\"),upfilename.indexOf("."));
			   var reg=null;
			   var exp=/.*[\u4e00-\u9fa5]+.*$/;
                     reg = fileName.match(exp);
                     if(reg!=null){
                      alert("Ϊ��֤���������ļ����ļ�����Ҫ���������ַ�");
                      return;
					 }
			   
			  /**
				*�����ϴ�����
				*/
			  if(filetype!="xls")
			  {
			     alert("ֻ֧��xls�ļ�!!!");
			     return false;
			  }
			  var files=document.getElementById("files").value;
			  $.ajax({
			type:"POST",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=importExcel&files="+encodeURIComponent(files)+"&nowtime="+(new Date()),
			success:function(data){
				if(data.isSuccess=="1"){
				alert("�����ɹ�");
				window.parent.opener.location="<%=rootPath%>/link.do?action=list&jp=1";
			    window.parent.close();
			   }else{
			   alert("�����ɹ�");
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
    <h2 align="center">ѡ����Ҫ���ص��ļ�</h2> <br>
    ������<input type="file" id="files" name="files" size="40" maxlength="250">
    	<input type="button" value="����" align="middle" onclick="check()" >
    	<br><br>
    	<div align="center"><font color="red" style="font-size: 13">&nbsp;�ļ���ʽ����Ϊxls����,�����޷���ȡ</font></div>
  	</form>
  </body>
</html>
