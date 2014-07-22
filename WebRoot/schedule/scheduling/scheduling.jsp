<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.schedule.model.*"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%  
   String rootPath = request.getContextPath(); 
   List allbuss = (List)request.getAttribute("allbuss");
   List<Period> periodList = (ArrayList<Period>)request.getAttribute("periodList");
   List<Position> positionList = (ArrayList<Position>)request.getAttribute("positionList");

%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->

<!-- snow add for gatherTime at 2010-5-12 start -->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script language="JavaScript" type="text/javascript">
Ext.onReady(function(){  
 Ext.get("process").on("click",function(){
 	var users = document.getElementById("username").value;
 	if(users.split(",").length != 4){
 		alert("请选择四位值班人！");
 		return;
 	}
 	
 	var period = document.getElementById("rightPeriod");
 	if(period.options.length != 2){
 		alert("请选择两个班次！");
 		return;
 	}
 	var periodids = "";
 	for(var i=0;i<period.options.length;i++){
 		//period.options[i].selected = true;
 		periodids += period.options[i].value;
 		if(i<period.options.length-1){
 			periodids += ",";
 		}
 	}
 	
 	var position = document.getElementById("rightPosition");
 	if(position.options.length != 1){
 		alert("请选择一个值班地点！");
 		return;
 	}
 	
 	var positionids = "";
 	for(var i=0;i<position.options.length;i++){
 		//position.options[i].selected = true;
 		positionids += position.options[i].value;
 		if(i<position.options.length-1){
 			positionids += ",";
 		}
 	}
 	
 	var startDate = document.getElementById("startdate").value;
	if(!startDate){
		alert("请设置开始日期！");
 		return;
	}
	
	var result = document.getElementById("result").value;
	if(result){
		//if(!confirm(result))
		//	return;
		
		if(confirm(result)){
			$.ajax({
				type:"GET",
				dataType:"json",
				url:"<%=rootPath%>/scheduleAjaxManager.ajax?action=ajaxDelete&startdate=" + startDate + "&nowtime="+(new Date()),
				success:function(data){
				}
			});
		}else{
			return;
		}
	}
	
	Ext.MessageBox.wait('正在排班计算中，请稍后.. '); 
    mainForm.action = "<%=rootPath%>/schedule.do?action=saveSchedule2&periodids="+ periodids +"&positionids="+positionids;
    mainForm.submit();
 });	
	
});

function CreateWindow(url){
	msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function getUsers(){
	return CreateWindow('<%=rootPath%>/user.do?action=getUsers');
}

function move(p1,p2,flag){
	var select1 = document.getElementById(p1);
	var select2 = document.getElementById(p2);
	for(var i=0;i<select1.options.length;i++){
		if(flag && select2.options.length==2){
			break;
		}
		if(select1.options[i].selected){
			var option=document.createElement('option');
			option.text = select1.options[i].text;
			option.value = select1.options[i].value;
			try{
				select2.add(option,null);
			}catch(e){
				select2.add(option);
			}
			select1.remove(i);
			i--;
		}
	}
}

window.onload = function(){
	var startdate = document.getElementById('startdate');
	startdate.attachEvent('onpropertychange',function(o){
		if(o.propertyName == 'value'){
			var value = startdate.value;
			if(value){
				$.ajax({
					type:"GET",
					dataType:"json",
					url:"<%=rootPath%>/scheduleAjaxManager.ajax?action=ajaxCheckDate&startdate=" + value + "&nowtime="+(new Date()),
					success:function(data){
						if(data.count){
							document.getElementById("result").value = "该日期已经有排班，如果继续将会删除该日期及之后的排班计划!是否继续?";
							document.getElementById("msg").innerText = "该日期已经有排班，如果继续将会删除该日期及之后的排班计划!";
						}else{
							document.getElementById("msg").innerText = "";
						}
					}
				});
			}	
		} 
	});
}

</script>
</head>
<body id="body" class="body">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" id="result">
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
							<td class="td-container-main-add">
								<table id="container-main-add" class="container-main-add">
									<tr>
										<td>
											<table id="add-content" class="add-content">
												<tr>
													<td>
														<table id="add-content-header" class="add-content-header">
										                	<tr>
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">日常办公 &gt;&gt; 值班安排 &gt;&gt; 自动排班</td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-body" class="detail-content-body">
				        									<tr>
				        										<td>
				        										
				   <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="40%">值班人&nbsp;</TD>				
							<TD nowrap>&nbsp;
							<!-- 
								<input type="text" name="name" maxlength="50" size="20" class="formStyle">
							 -->
								<input type=text readonly="readonly" id="username" name="username" size="50" maxlength="32">&nbsp;&nbsp;
								<input type=button value="选择人员" onclick='getUsers()'>
								<font color="red">&nbsp;*</font>
								<input type="hidden" id="userids" name="userids">
							</TD>						
						</tr>
						<tr>
							<TD nowrap align="right" height="24" width="40%">班次&nbsp;</TD>			
							<TD nowrap>
								<table border="0" style="margin-left: 10px; width: 300px;">
									<tr>
										<td rowspan="2" width="100">
										<select id="leftPeriod" size="4" multiple="multiple" style="width:80px;">
											<%
											for(int i=0;i<periodList.size();i++){
											%>
											<option value="<%=periodList.get(i).getId() %>"><%=periodList.get(i).getName() %></option>
											<%
											}
											%>
										</select>
										</td>
										<td width="50" valign="bottom">
										<input type="button" value=" &raquo; " onclick="move('leftPeriod','rightPeriod',true)">
										</td>
										<td rowspan="2" width="100">
										<select id="rightPeriod" size="4" multiple="multiple" style="width:80px;">
										</select>
										</td>
										<td rowspan="2" width="200" valign="middle">
											<font color="red">&nbsp;*选择两个班次</font>
										</td>
									</tr>
									<tr>
										<td valign="top">
										<input type="button" value=" &laquo; " onclick="move('rightPeriod','leftPeriod',false)">
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr style="background-color: #ECECEC;">	
							<TD nowrap align="right" height="24" width="40%">值班地点&nbsp;</TD>			
							<TD nowrap>
								<table border="0" style="margin-left: 10px; width: 300px;">
									<tr>
										<td rowspan="2" width="100">
										<select id="leftPosition" size="4" multiple="multiple" style="width:80px;">
											<%
											for(int i=0;i<positionList.size();i++){
											%>
											<option value="<%=positionList.get(i).getId() %>"><%=positionList.get(i).getName() %></option>
											<%
											}
											%>
										</select>
										</td>
										<td width="50" valign="bottom">
										<input type="button" value=" &raquo; "  onclick="move('leftPosition','rightPosition',true)">
										</td>
										<td rowspan="2" width="100">
										<select id="rightPosition" size="4" multiple="multiple" style="width:80px;">
										</select>
										</td>
										<td rowspan="2" width="200" valign="middle">
											<font color="red">&nbsp;*选择一个地点</font>
										</td>
									</tr>
									<tr>
										<td valign="top">
										<input type="button" value=" &laquo; "  onclick="move('rightPosition','leftPosition',false)">
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>						
							<TD nowrap align="right" height="24" width="40%">开始时间&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<input type="text" readonly="readonly" id="startdate" name="startdate" size="10">
								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
								<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0> </a>
								<font color="red">&nbsp;*</font>
								<span id="msg" style="color:red;"></span>
							</TD>						
						</tr>
						<tr style="background-color: #ECECEC;">	
							<TD nowrap align="right" height="24" width="40%">值班周期数&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<select id="loops" name="loops">
									<option value="1">1</option>
									<option value="2">2</option>
									<option value="3">3</option>
									<option value="4">4</option>
									<option value="5">5</option>
								</select>
								<font color="red">&nbsp;* 当前预设的一个值班周期是56天</font>
							</TD>					
						</tr>
						<tr>
							<td><input type="hidden" id="rowNum" name="rowNum"></td>
						</tr>
						<tr>
							<TD nowrap colspan="2" align=center>
							<br><input type="button" value="保 存" style="width:50" id="process">&nbsp;&nbsp;
								<!-- 
								 <input type="button" value="保 存" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
								 -->
								<input type="reset" style="width:50" value="返 回" onclick="javascript:history.back(1)">
							</TD>	
						</tr>	
							</TBODY>
						</TABLE>
										 							
										 							
				        										</td>
				        									</tr>
				        								</table>
				        							</td>
				        						</tr>
				        						<tr>
				        							<td>
				        								<table id="detail-content-footer" class="detail-content-footer">
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
									<tr>
										<td>
											
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
</BODY>

</HTML>                                                     