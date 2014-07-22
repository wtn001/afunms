<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="com.bpm.process.model.UserShipModel"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<%
String rootPath=request.getContextPath();
String content=(String) request.getAttribute("content");
List<UserShipModel> listUser=(List<UserShipModel>)request.getAttribute("listUser");
List<UserShipModel> listGroup=(List<UserShipModel>)request.getAttribute("listGroup");
%>
	<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
	<link rel="StyleSheet" href="<%=rootPath%>/css/dtree.css" type="text/css" />
	<script type="text/javascript" src="<%=rootPath%>/js/dtree.js"></script>
	<script type="text/javascript">
	function getRadioSelected(nodeId){		
		var nname = mytree.aNodes[nodeId].name;   //此处得到的是所选择节点的name值，根据需要，同样可得到其他的值
		document.getElementById('exID').value =  nname;  
	} 
	
	function sureUser()
	{
       var open=  parent.opener.document.getElementById("userid");
       var vv=document.getElementById('exID').value;
       open.value=vv;
       window.close();
	}
	</script>	
</head>
<body id="body" class="body">
<input type="hidden" id="exID" size="20"/>
<div class="dtree">
	<p>
	<input type="button" value="全部展开"  onclick="mytree.openAll();">
	<input type="button" value="全部关闭"  onclick="mytree.closeAll();">
	<input type="button" value="确定"  onclick="sureUser();">
	</p>
	<script type="text/javascript">
    //节点的函数node有9个参数，并不需要全部传，但若只传几个，默认为前面几个
		mytree = new dTree('mytree');
		mytree.config.useRadio = true;  //设置有单选按钮
	  	mytree.add(0,-1,'选择用户');
		<% 
		String groupid=null;
		for(UserShipModel model:listGroup)
		{
			%>mytree.add('<%=model.getGroupid() %>',0,'<%=model.getGroupid() %>');<%
		}
		 %>
		<% 
		
		for(UserShipModel model:listUser)
		{
			%>mytree.add('<%=model.getUserid() %>','<%=model.getGroupid() %>','<%=model.getUserid() %>');<%
		}
		 %>
		document.write(mytree);

	</script>
</div>

</body>
</html>
