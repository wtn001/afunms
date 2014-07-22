<%@page import="org.activiti.engine.impl.pvm.process.ActivityImpl"%>
<%@page import="java.util.*"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.bpm.system.utils.StringUtil"%>
<%@page import="com.afunms.bpm.util.MenuConstance"%>
<%@page import="com.bpm.system.model.UserModel"%>
<%@page import="com.bpm.system.utils.ConstanceUtil"%>
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

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
<script type="text/javascript" 	src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
<script type="text/javascript" src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/css/color.css" />	
		
<link href="<%=rootPath %>/js/jquery/themes/default/easyui.css" rel="stylesheet" type="text/css"/>
<link href="<%=rootPath %>/js/jquery/themes/icon.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/js/dwr/util.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/CommonHelper.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/main.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/global/form_validate.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/DatePicker/WdatePicker.js"></script>
<%
    String findselect = (String) session.getAttribute("findselect");
	String taskId = (String) request.getAttribute("taskId");
	String taskExtId=(String) request.getAttribute("taskExtId");
	String processInstanceId=(String) request.getAttribute("processInstanceId");
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
	String afterActivityId=(String) request.getAttribute("afterActivityId");
	String taskName=(String) request.getAttribute("taskName");
	List<UserModel> userList=(List<UserModel>) request.getAttribute("userList");
	String isbanjiebutton=(String) request.getAttribute("isbanjiebutton");
%>

<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/JavaScript">
function banjie()
 {
 	$("#reject").attr("title","");
 	$("#reject").attr("value","");
 	$("#divreject").hide();
 	$("#dl").show();
 	$("#ordersolution").attr("title","required");
 }
	
function validatereject()
{
	$("#ordersolution").attr("title","");
	$("#ordersolution").attr("value","");
	$("#dl").hide();
	$("#divreject").show();
 	$("#reject").attr("title","required");
}
 
function suresubmit()
{
	$("#ordersolution").attr("title","");
 	$("#ordersolution").attr("value","");
 	$("#dl").hide();
 	$("#reject").attr("title","");
 	$("#reject").attr("value","");
 	$("#divreject").hide();
}
 
 function openOrderSolution()
 {
 	//设置模式窗口的一些状态值
  var windowStatus = "dialogWidth:800px;dialogHeight:600px;center:1;status:0;";
  //在模式窗口中打开的页面
   var url = "../controller/orderSolutionList.action";
  //将模式窗口返回的值临时保存
  var temp = showModalDialog(url,"",windowStatus);
  //将刚才保存的值赋给文本输入框returnValue
  document.all.returnValue.value = temp;
 
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
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 194px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form action="../controller/taskFormKeySubmit.action" method="post" id="mainForm" onsubmit="return checkdata();">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td>
									<%=menuTable%>
								</td>	
							</tr>
						</table>
					</td>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td colspan="2">
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 流程管理 >> 任务办理 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
		        						</tr>
		        						
		        						<tr>
		        							<td colspan="2">
		        								<table id="content-body" class="content-body">
		        									<tr >
														<td >
															<table width="100%" >
																<tr>
									    							<td class="body-data-title" width="80%" align="center">
																		
														    		</td>
			        											</tr>
															</table>
														</td>
													</tr> 
													<tr>
														<td>
															<table>
					        									<tr>
							                	<td class="win-data-title" style="height: 29px;" >
   <!--embed-form-->
	<input type="hidden" name="taskId" value="<%=taskId%>">
	<input type="hidden" name="taskExtId" value="<%=taskExtId%>">
	<input type="hidden" name="processInstanceId" value="<%=processInstanceId%>">
	<!-- taskFormType区别是自己填写form表单，还是直接从流程中读取form值 -->
	
	<br><br>
	<table>
	   <tr style="background-color: #ECECEC;"><td align="center">
	<% 
	if(StringUtil.isNotBlank(taskName) && taskName.equals(ConstanceUtil.BEFORE_END))
	{%>
		<input type="submit" value="确定" style="width:50"  name="result">&nbsp;&nbsp;
	<%}
	else 
	{%><input type="submit" value="提交" style="width:50" onclick="suresubmit();"  name="result">&nbsp;&nbsp;<% }
	if(backAIList.size()!=0)
	{
	  %><input style="width:50"  type="submit" name="result"  onclick="validatereject();" value="驳回" />
	  <select name="backActivityId"><% 
	    ActivityImpl ai=null;
	  	for (int i= backAIList.size()-1;i>=0;i--) 
		{ 
			ai=backAIList.get(i);
		 	out.println("<option value=" + ai.getId() + " >"+ ai.getProperty("name") + "</option>");
		}
	 %></select>&nbsp;&nbsp; <% 
	}
	if((!(StringUtil.isNotBlank(taskName) && taskName.equals(ConstanceUtil.BEFORE_END))) && isbanjiebutton.equals(ConstanceUtil.Process_BANJIE) )
	{
		%>
		<input style="width:50"  type="submit" name="result" value="办结" onclick="banjie();"  />
		<%
		if(afterActivityImpl!=null)
		{%>
			<input type="hidden" name="afterActivityId" value="<%=afterActivityImpl.getId() %>">
   		<%}
   		else
  		{%>
			<input type="hidden" name="afterActivityId" value="<%=afterActivityId %>"><%
   		}
   }%></td></tr>
		</table>	</td>
						       		</tr>
	
		        											</table>
														
														</td>
													</tr> 
		        								</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td colspan="2">
		        								<table id="content-footer" class="content-footer">
		        									<tr>
		        										<td>
		        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
									                  			<tr>
									                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
									                    			<td></td>
									                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
									                  			</tr>
									              			</table>
		        										</td>
		        									</tr>
		        								</table>
		        							</td>
		        						</tr>
		        						
		        					</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>

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


</html>