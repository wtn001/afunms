<%@page import="org.activiti.engine.impl.pvm.process.ActivityImpl"%>
<%@page import="java.util.*"%>
<%@page import="java.util.Iterator"%>
<%@page import="org.activiti.engine.form.FormType"%>
<%@page import="org.activiti.engine.form.FormProperty"%>
<%@page import="org.activiti.engine.repository.ProcessDefinition"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.bpm.system.utils.ConstanceUtil"%>
<%@page import="com.bpm.system.utils.StringUtil"%>
<%@page import="com.afunms.bpm.util.MenuConstance"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String rootPath = request.getContextPath();
String userId = ((User)request.getSession().getAttribute(SessionConstant.CURRENT_USER)).getUserid();
String menuTable = MenuConstance.getMenuTable();
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>流程任务表单</title>
<link href="<%=rootPath%>/css/main.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/main.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/global/form_validate.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/DatePicker/WdatePicker.js"></script>
<%
    String findselect = (String) session.getAttribute("findselect");
	String taskId = (String) request.getAttribute("taskId");
	String taskExtId=(String) request.getAttribute("taskExtId");
	String startTime = (String)request.getAttribute("startTime");
	List<ActivityImpl> backAIList = (List<ActivityImpl>) request.getAttribute("backAIList");
	Map<String,Object> map = (Map<String,Object>)request.getAttribute("map");
	String warnprocess = "false";
	String orderId = String.valueOf(StringUtil.getTimeId());
	String nodeId = "";
	String content = "";
	String ostype="";
	String subtype = "";
	String subentity = "";
	String level = "";
	if(map.size()>0) {
		if(null!=map.get("warnprocess")) {
			warnprocess = (String)map.get("warnprocess");
		}
		if("true".equals(warnprocess)) {
			nodeId = (String)map.get("nodeId");
			content = (String)map.get("content");
			ostype = ((String)map.get("ostype")).toLowerCase();
			subtype = (String)map.get("subtype");
			subentity = (String)map.get("subentity");
			level = (String)map.get("level");
		}
		
	}
	ActivityImpl afterActivityImpl=(ActivityImpl)request.getAttribute("afterActivityImpl");
%>

<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
		<script language="javascript">	
		
		
		
		
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("请输入查询条件");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
    function doCancelManage()
  {  
     mainForm.action = "<%=rootPath%>/network.do?action=cancelmanage";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }

</script>
		<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//添加菜单	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}

}
function openkowlege() {

  window.showModalDialog("knowledgelist.action",obj,"dialogWidth=1000px;dialogHeight=800px");

}
 
 
 
  
</script>
</head>
<body id="body" class="body" onload="initmenu();">
  <table>
<tr>
<td>
<%=menuTable%>
</td>
<td>
<div class="list_content">
<form action="../controller/taskFormKeySubmit.action" method="post" id="mainForm" onsubmit="return checkdata()">
    <!--embed-form-->
<table  width="100%"  border="1" cellspacing="0" cellpadding="0" class="table_box">
  <tr>
  <td>
 <label class="labe"> <font class="red">*</font>下单人：</label><input type="text" name="orderuser" value="" title="required" size="20"/>
  </td>
  </tr>
  
  <tr>
  <td>
<label class="labe"> <font class="red">*</font>启动时间：</label><input type="text" title="required"  name="orderstarttime" value="" size="20"/>
  </td>
  </tr>
  
   <tr>
  <td align="center">
 <label class="labe"><font class="red">*</font>订单事件：</label><textarea rows="3" cols="60" title="required" name="ordercontent" id="ordercontent"></textarea>
  </td>
  </tr>
  
<% if("true".equals(warnprocess)) { %>
<tr>
  <td align="center">
  <label class="labe"><font class="red">*</font>订单类型：</label>
<select title="required" name="ordertype">
  </select>
&nbsp;&nbsp;
<label class="labe"> <font class="red">*</font>设备类型</label>
<select name="ordernodetype"  title="required">
</select>
&nbsp;&nbsp;
<label class="labe"> <font class="red">*</font>告警类型</label>
<select name="orderwarntype" title="required">
</select>  
  </td>
  </tr>
<%} else {%>
 <tr>
  <td align="center">
  <label class="labe"><font class="red">*</font>订单类型：</label>
  <input type="text" title="required" name="ordertype" />
&nbsp;&nbsp;
<label class="labe"> <font class="red">*</font>设备类型</label>
<input type="text" title="required" name="ordernodetype" />
&nbsp;&nbsp;
<label class="labe"> <font class="red">*</font>告警类型</label>
<input type="text" title="required" name="orderwarntype" />
  </td>
  </tr>
<%}%>
  
  <tr>
  <td align="center" >
  <label class="labe">备注：</label><textarea rows="3" cols="60" name="beizhu" id="beizhu"></textarea>
  </td>
  </tr>
	<tr>
  <td colspan="2" align="center"><font class="red">*</font>
短信提醒：<select name="ordermessage">
<option>是</option>
<option>否</option>
</select>
  </td>
  </tr>

  </table>

<input type="hidden" name="taskFormType" value="0"> 

<input type="hidden" id="warnstatus" value="1">

<input type="hidden" id="isbanjie" value="0">



    
	<input type="hidden" name="taskId" value="<%=taskId%>">
	<input type="hidden" name="taskExtId" value="<%=taskExtId%>">
	<!-- taskFormType区别是自己填写form表单，还是直接从流程中读取form值 -->
	<input class="buttongreen" type="submit" name="result" value="同意" />
	<% if(backAIList.size()!=0)
	{
	  %><input class="buttongreen" type="submit" name="result" value="驳回" />
	  <select name="backActivityId"><% 
	  	for (ActivityImpl ai : backAIList) 
		{ 
		 	out.println("<option value=" + ai.getId() + " >"+ ai.getProperty("name") + "</option>");
		}
	 %></select> <% 
	}
	if(afterActivityImpl!=null)
	{%>
	<input class="buttongreen" type="submit" name="result" value="半截" onclick="return banjie();" />
	<input type="hidden" name="afterActivityId" value="<%=afterActivityImpl.getId() %>">
   <%}%>
</form>
</div>
</td>
</tr>
</table>
</body>
<script type="text/javascript">
$(document).ready(function(){
try {

if("1"==$("#warnstatus").val()) {

try {
if($("input[name='orderuser']").val()==""){
$("input[name='orderuser']").val("<%=userId%>");
}
}catch(ex) {

}

try{
if($("input[name='orderstarttime']").val()==""){
$("input[name='orderstarttime']").val("<%=startTime%>");
}

}catch(ex) {


}
try{
$("input[name='orderid']").val("<%=orderId%>");
}catch(ex) {

}

try{
$("#ordercontent").val("<%=content%>");
}catch(ex) {

}

}


}
catch(ex) {

}






});

</script>

<script type="text/javascript">
<%=findselect%>
$(document).ready(function(){
	var $s1=$(document.getElementsByName("ordertype")[0]);

	var $s2=$(document.getElementsByName("ordernodetype")[0]);

	var $s3=$(document.getElementsByName("orderwarntype")[0]);
   
	var v1="<%=subentity%>";
	var v2="<%=ostype%>";
	var v3="<%=subtype%>";

	$.each(threeSelectData,function(k,v){

		appendOptionTo($s1,k,v.val,v1);

	});

	$s1.change(function(){

		$s2.html("");

		$s3.html("");

		if(this.selectedIndex==-1) return;

		var s1_curr_val = this.options[this.selectedIndex].value;

		$.each(threeSelectData,function(k,v){

			if(s1_curr_val==v.val){

				if(v.items){

					$.each(v.items,function(k,v){

						appendOptionTo($s2,k,v.val,v2);

					});

				}

			}

		});

		$s2.change();

	}).change();

	$s2.change(function(){

		$s3.html("");

		var s1_curr_val = $s1[0].options[$s1[0].selectedIndex].value;

		if(this.selectedIndex==-1) return;

		var s2_curr_val = this.options[this.selectedIndex].value;

		$.each(threeSelectData,function(k,v){

			if(s1_curr_val==v.val){

				if(v.items){

					$.each(v.items,function(k,v){

						if(s2_curr_val==v.val){

							$.each(v.items,function(k,v){

								appendOptionTo($s3,k,v,v3);

							});

						}

					});

				}

			}

		});

	}).change();

	function appendOptionTo($o,k,v,d){

		var $opt=$("<option>").text(k).val(v);

		if(v==d){$opt.attr("selected", "selected")}

		$opt.appendTo($o);

	}

});

</script>


<!-- 
try {
$("select[@name=ordertype] option").each(function(){
   if($(this).val()=="<%=subentity%>") {
     $(this).attr("selected",true);
   }

});

}catch(ex){

}

try{
$("input[name='orderwarntype']").val("<%=subtype%>");
}catch(ex) {

}

try {
$("select[@name=ordernodetype] option").each(function(){
   if($(this).val()=="<%=ostype%>") {
     $(this).attr("selected",true);
   }

});

}catch(ex){

}

-->

</html>
