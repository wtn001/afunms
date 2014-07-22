<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%  
   String rootPath = request.getContextPath(); 
   List allbuss = (List)request.getAttribute("allbuss");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

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
<script type="text/javascript">
	$(addTimeConfigRow);//addTimeConfigRow函数在application/resource/js/addTimeConfig.js中 
</script>
<script type="text/javascript">
$(document).ready(function(){
	$('#collecttype').bind('change',function(){
		if($('#collecttype').val()=='1')
		{
			$('#community_tr').show();
			$('#collectversion').show();
		}
		else
		{
			$('#community_tr').hide();
			$('#collectversion').hide();
		}
		//alert('abc');
	});
});
</script>
<!-- snow add for gatherTime at 2010-5-12 end -->






<script language="JavaScript" type="text/javascript">


  Ext.onReady(function()
{  

	
 Ext.get("process").on("click",function(){
  	 //alert($('#collecttype').val()+'eee');
     var chk1 = checkinput("ip_address","ip","IP地址",15,false);
     var chk2 = checkinput("alias","string","机器名",30,false);
     var chk3=true;
     var chk4=true;
     var chk5=true;
     var chk7=true;
     var chk8=true;
     var chk9=true;
	 if($('#collecttype').val()=='10'&&$('#type').val()=='15'&&$('#ostype').val()=='40'){
	     
        var chk4 = checkinput("uname","string","用户名",30,false);
        var chk5 = checkinput("pw","string","密码",30,false);
        }	
    if($('#collecttype').val()=='10'&&$('#type').val()=='14'&&$('#ostype').val()=='41'){
	     
        var chk4 = checkinput("uname","string","用户名",30,false);
        var chk5 = checkinput("pw","string","密码",30,false);
     }
	 if($('#collecttype').val()=='1'){
    	 if($("#snmpversion").val()!="3"){
    		var chk3 = checkinput("community","string","读共同体",30,false);
    	 }
    	 if($("#snmpversion").val()=="3"){
	 		 if($("#securityLevel").val()=="1"){
	 			 var chk7 = checkinput("securytyName","string","安全级别中用户名",30,false);
	 		 }
	 		 if($("#securityLevel").val()=="2"){
	 			 var chk7 = checkinput("securytyName","string","安全级别中用户名",30,false);
	 			 var chk8 = checkinput("authPassPhrase","string","通行码",30,false);
	 		 }
	 		 if($("#securityLevel").val()=="3"){
	 			 var chk7 = checkinput("securytyName","string","安全级别中用户名",30,false);
	 			 var chk8 = checkinput("authPassPhrase","string","通行码",30,false);
	 			 var chk9 = checkinput("privacyPassPhrase","string","加密协议码",30,false);
	 		 }
	 	 }
    }

     if(chk1&&chk2&&chk3&&chk4&&chk5&&chk7&&chk8&&chk9)
     {
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
         mainForm.action = "<%=rootPath%>/network.do?action=add";
         mainForm.submit();
     }  
       // mainForm.submit();
 });	
	
});
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------

//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------


//function toAllAdd(){
        // mainForm.target="alladd";
		// window.open("","alladd","toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")
		// mainForm.action='<%=rootPath%>/network.do?action=alladd';
		// mainForm.submit();
//}

function toAllAdd(){
	return CreateWindow('<%=rootPath%>/network.do?action=alladd');
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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
	initTR();
}
function changehid(){
   var collecttype=document.getElementById("collecttype").value;
   if(collecttype!='10'){
    document.getElementById("messageadd").style.display="none";
    document.getElementById("messageadd1").style.display="none";
   }
}
function changes(){
    var collecttype=document.getElementById("collecttype").value;
    var type=document.getElementById("type").value;
    var ostype=document.getElementById("ostype").value;
    if(collecttype=='10'&&type=='15'&&ostype=='40'){
        if(document.getElementById("messageadd").style.display="none"){
           document.getElementById("messageadd").style.display="";
         }else{
             document.getElementById("messageadd").style.display ="none";
              }
      }else   if(collecttype=='10'&&type=='14'&&ostype=='41'){
        if(document.getElementById("messageadd").style.display="none"){
           document.getElementById("messageadd").style.display="";
           document.getElementById("messageadd1").style.display="";
         }else{
             document.getElementById("messageadd").style.display ="none";
              }
     }else{
       document.getElementById("messageadd").style.display ="none";
     }
 }


</script>

<script>
//-- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------
	/*
	* 此方法用于短信分时详细信息
	* 需引入 /application/resource/js/timeShareConfigdiv.js 
	*/
	//接受用户的列表
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// 获取短信分时详细信息的div
	function timeShareConfiginit(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		// 获取设备或服务的分时数据列表,
		var timeShareConfigs = new Array();
		var smsConfigs = new Array();
		var phoneConfigs = new Array();
		<%	
			List timeShareConfigList = (List) request.getAttribute("timeShareConfigList");
			if(timeShareConfigList!=null&&timeShareConfigList.size()>=0){
			for(int i = 0 ; i < timeShareConfigList.size(); i++){	        
	            TimeShareConfig timeShareConfig = (TimeShareConfig) timeShareConfigList.get(i);
	            int timeShareConfigId = timeShareConfig.getId();
	            String timeShareType = timeShareConfig.getTimeShareType();
	            String timeShareConfigbeginTime = timeShareConfig.getBeginTime();
	            String timeShareConfigendTime = timeShareConfig.getEndTime();
	            String timeShareConfiguserIds = timeShareConfig.getUserIds();
	            
	    %>
	            timeShareConfigs.push({
	                timeShareConfigId:"<%=timeShareConfigId%>",
	                timeShareType:"<%=timeShareType%>",
	                beginTime:"<%=timeShareConfigbeginTime%>",
	                endTime:"<%=timeShareConfigendTime%>",
	                userIds:"<%=timeShareConfiguserIds%>"
	            });
	    <%
	        }
	        }
	    %>   
	    for(var i = 0; i< timeShareConfigs.length; i++){
	    	var item = timeShareConfigs[i];
	    	if(item.timeShareType=="sms"){
	    		smsConfigs.push({
	                timeShareConfigId:item.timeShareConfigId,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    	if(item.timeShareType=="phone"){
	    		phoneConfigs.push({
	                timeShareConfigId:item.timeShareConfigId,
	                timeShareType:item.timeShareType,
	                beginTime:item.beginTime,
	                endTime:item.endTime,
	                userIds:item.userIds
	            });
	    	}
	    }
		timeShareConfig("smsConfigTable",smsConfigs);
		timeShareConfig("phoneConfigTable",phoneConfigs);
	}
//---- nielin add for timeShareConfig 2010-01-03 end-------------------------------------------------------------------------
</script>


</head>
<body id="body" class="body" onload="initmenu();">

<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">修改信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">监视信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">取消监视</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">添加监视</td>
		</tr>		
	</table>
	</div>
	<!-- 右键菜单结束-->

	<form id="mainForm" method="post" name="mainForm">
		
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
											                	<td class="add-content-title">资源 >> 设备维护>> 设备添加</td>
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
							<TD nowrap align="right" height="24" width="10%">IP地址&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="ip_address" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>&nbsp;&nbsp;<a href="#" onclick="toAllAdd()">批量追加</a></TD>						
							<TD nowrap align="right" height="24" width="10%">机器名&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="alias" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font></TD>															
						</tr>
						<tr>
							<TD nowrap align="right" height="24" width="10%">采集方式&nbsp;</TD>
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='collecttype' style='width:100px;' id='collecttype' onclick='changehid();'>
            						<option value='1' selected>SNMP</option>
            						<option value='2'>代理</option>
            						<option value='3'>Ping</option>
            						<option value='4'>远程Ping</option>
            						<option value='5'>WMI</option>
            						<option value='6'>Telnet</option>
            						<option value='7'>SSH</option>
            						<option value='8'>Telnet检测可用</option>
            						<option value='9'>SSH检测可用</option>
            						<option value='10'>数据接口</option>
            					</select>&nbsp;
            					</TD>
            					<TD colspan=2>
            					<table>
            					<tr id='collectversion'>
            					<TD nowrap align="right" height="24" width="10%">采集版本&nbsp;</TD>
            					<TD nowrap width="40%" >&nbsp;&nbsp;
	            					<select size=1  style='width:100px;' id='snmpversion' name='snmpversion' onchange='changehMyTr(this.value);' >
            						<option value='0' selected>V1</option>
            						<option value='1'>V2</option>
	            						<option value='3'>V3</option>
            					</select>&nbsp;
            					</TD>
            					<tr>
            					</table>
            					</TD>
						</tr>
						<tr style="background-color: #ECECEC;" id='community_tr'>				
							<TD nowrap align="right" height="24" width="10%">读共同体&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="community" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font></TD>						
							<TD nowrap align="right" height="24" width="10%">写共同体&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="writecommunity" maxlength="50" size="20" class="formStyle">
								</TD>															
						</tr>
					
						<tr style="background-color: #ECECEC;" id='securityLevel_tr' >
							<TD nowrap  align="right" height="24" width="10%">安全级别&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1  style='width:100px;' id='securityLevel' name="securityLevel" onchange='changehMyTr2(this.value);'  >
	            						<option value='1' selected>noAutoNoPriv</option>
	            						<option value='2'>autoNoPriv</option>
	            						<option value='3'>autoPriv</option>
	            				</select>
							</TD>						
							<TD nowrap  align="right" height="24" width="10%">用户名&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="securytyName" name="securityName" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>
							</TD>
						</tr>
						
						<tr id='v3_ap_tr' >
							<TD nowrap  align="right" height="24" width="10%">认证协议&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1  style='width:100px;' id='v3_ap' name="v3_ap">
	            						<option value='1' selected >MD5</option>
	            						<option value='2'>SHA</option>
	            				</select>
							</TD>						
							<TD nowrap  align="right" height="24" width="10%">通行码&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="authPassPhrase" name="authPassPhrase" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;" id='v3_privacy_tr' >
							<TD nowrap  align="right" height="24" width="10%">加密协议&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1  style='width:100px;' id='v3_privacy' name="v3_privacy">
	            						<option value='1' selected>DES</option>
	            						<option value='2'>AED</option>
	            				</select>
							</TD>						
							<TD nowrap align="right" height="24" width="10%">加密协议码&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="privacyPassPhrase" name="privacyPassPhrase"  maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>
							</TD>
						</tr>
				<!-- 这里结束  隐藏块-->	
						<tr >												
							<TD nowrap align="right" height="24" width="10%">设备类型&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='type' style='width:100px;' onchange="unionSelect();" id='type'>
            								<option value='1' selected>路由器</option>
            								<option value='2'>路由交换机</option>
            								<option value='3'>交换机</option>
            								<option value='4'>服务器</option>
            								<option value='8'>防火墙</option>
            								<option value='9'>ATM</option>
            								<option value='10'>邮件安全网关</option>
            								<option value='11'>F5</option>
            								<option value='12'>VPN</option>
            								<option value='13'>CMTS</option>
            								<option value='14'>存储</option>
            								<option value='15'>虚拟化</option>
                                            <!-- <option value='16'>空调</option> -->
                                            <option value='17'>UPS</option>
            							</select>&nbsp;</TD>
            				<TD nowrap align="right" height="24" width="10%">传输方向&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								
								<select size=1 name='transfer' id='transfer' style='width:100px;' >
								            <option value='0' selected>无</option>
            								<option value='1'>上一级</option>
            								
            								
            							</select>&nbsp;
								</TD>			
							
						</tr>
						<tr  style="background-color: #ECECEC;">												
							<TD nowrap align="right" height="24" width="10%">操作系统&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='ostype' style='width:100px;' onclick='changes();' id='ostype'>
									<option value='0' selected> </option>
            								
            							</select>&nbsp;</TD>
							<TD nowrap align="right" height="24" width="10%">是否监视&nbsp;</TD>				
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='manage' style='width:100px;'>
            								<option value='1' selected>是</option>
            								<option value='0'>否</option>
            							</select>&nbsp;</TD>            																						
						</tr>
						<tr  style="display:none" id="messageadd1">												
							<TD nowrap align="right" height="24" width="10%">子类型&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<select size=1 name='subtype' style='width:100px;'>
            								<option value='EMC_VNX' selected>EMC_VNX</option>
            								<option value='EMC_DMX'>EMC_DMX</option>
            								<option value='EMC_VMAX'>EMC_VMAX</option>
            							</select>&nbsp;</TD>											
						</tr>
						<tr  style="display:none" id="messageadd">												
							<TD nowrap align="right" height="24" width="10%">用户名&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;&nbsp;<input type="text" id="uname" name="uname" size="20" maxlength="50" class="formStyle"/>
							<font color="red">&nbsp;*</font></TD>											
							<TD nowrap align="right" height="24" width="10%">密码&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="password" id="pw" name="pw" size="20" maxlength="50" class="formStyle"/>
								<font color="red">&nbsp;*</font></TD>          																						        																						
						</tr>					
						<!-- snow modify begin */ 2010-05-18 -->
						<tr>												
							<TD nowrap align="right" height="24" width="10%">供应商&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='supper' style="width:260px;">
									<option value='0' selected></option>
									<c:forEach items="${allSupper}" var="al">
            							<option value='${al.su_id }'>${al.su_name }（${al.su_dept}）</option>
            						</c:forEach>
            					</select>
            				</TD>	
            				<TD nowrap align="right" height="24" width="10%">客户端告警&nbsp;</TD>				
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='messageflag' style='width:100px;'>
            								<option value='1' >是</option>
            								<option value='0' selected>否</option>
            							</select>&nbsp;
            				</TD>
            																									
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">设备资产编号&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="assetid" maxlength="70" size="20" class="formStyle">
														
							<TD nowrap align="right" height="24" width="10%">设备机房位置&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="location" maxlength="80" size="20" class="formStyle">
																						
						</tr>
						<!-- snow modify end -->
						
												
						<tr >
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">需要一点时间,请稍候...</font></div>
							</td>
						</tr>			
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
			<!--<tr>
				<TD nowrap align="right" height="24">短信接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendmobiles" name="sendmobiles" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置短信接收人" onclick='setReceiver("sendmobiles");'>
				</td>
			</tr>
			<tr >	
				<TD nowrap align="right" height="24">邮件接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendemail" name="sendemail" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置邮件接收人" onclick='setReceiver("sendemail");'>
				</td>
			</tr>
			<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">电话接收人&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendphone" name="sendphone" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置电话接收人" onclick='setReceiver("sendphone");'>
				</td>
			</tr>-->
			
		
            <tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">所属业务&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="设置所属业务" onclick='setBid("bidtext" , "bid");'>
				<input type="hidden" id="bid" name="bid" value="">
				</td>
			</tr>
            <tr>
            <TD nowrap align="right" height="24" width="10%">SysLog接收&nbsp;</TD>	
           <TD nowrap width="40%" colspan=3>&nbsp;
            						<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center">
                        			<tbody>
							<tr align="left" valign="center" bgcolor=#ECECEC> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="0"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;紧急</td>
                    						<td height="28" align="right" width=10%%><INPUT type="checkbox" class=noborder name="fcheckbox" value="1"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;报警</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="2"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;关键</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="3"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;错误</td>
                    					</tr>
                    					<tr align="left" valign="center" bgcolor=#ffffff> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="4"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;警告</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="5"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;通知</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="6"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;提示</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="7"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;调试</td>
                    					</tr>
            					</tbody>
            					</table>
			</TD>																			
							</tr>
							
							<!-- snow modify begin (timeConfig div)*/ 2010-05-12 -->
							<tr>
							 	<td nowrap align="right" width="10%" style="height:35px;">信息采集时间&nbsp;</td>		
							 	<td nowrap  colspan="3">
							        <div id="formDiv">         
						                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
					                        <tr>
					                            <td align="left">  
						                            <br>
					                                <table id="timeConfigTable" style="width:70%; padding:0;  background-color:#FFFFFF; position:relative; left:15px;" >
				                                        <tr>
				                                            <td colspan="7" height="50" align="center">
				                                                <span id="addTimeConfigRow" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand; line-height:15px">增加一行</span>
				                                            </td>
				                                        </tr>
					                                </table>
					                            </td>
					                        </tr>
						                </table>
						            </div> 
								</td>
							</tr>
							<!-- snow modify end */ 2010-05-12 -->
															
															<!--  
															<tr>	
																<td nowrap colspan="8">
																	
																        <div id="formDiv" style="">         
															                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
														                        <tr>
														                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;短信发送详细配置</td>
														                        </tr>
														                        <tr>
														                            <td align="center">           
														                                <table id="smsConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
													                                        <tr>
													                                            <td colspan="0" height="50" align="center"> 
													                                                <span  onClick='addRow("smsConfigTable","sms");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">增加一行</span>
													                                            </td>
													                                        </tr>
														                                </table>
														                            </td>
														                        </tr>
															                </table>
															            </div> 
																     				
																
																</td>
															</tr>
															<tr>
																<td nowrap colspan="8">
																
																   
																        <div id="formDiv" style="">         
															                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
														                        <tr>
														                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;电话接收详细配置</td>
														                        </tr>
														                        <tr>
														                            <td align="center">           
														                                <table id="phoneConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
													                                        <tr>
													                                            <td colspan="0" height="50" align="center"> 
													                                                <span  onClick='addRow("phoneConfigTable","phone");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">增加一行</span>
													                                            </td>
													                                        </tr>
														                                </table>
														                            </td>
														                        </tr>
															                </table>
															            </div> 
																   				
																	
																</td>
															</tr>
															-->
															<tr>
																<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
																<td><input type="hidden" id="rowNum" name="rowNum"></td>
																<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
															
															</tr>
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="保 存" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">
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

<script>

function initTR(){
	document.getElementById("community_tr").style.display = "block"; // 读共同体隐藏；
	document.getElementById("securityLevel_tr").style.display = "none"; //安全级别；
	document.getElementById("v3_ap_tr").style.display = "none";    //认证协议
	document.getElementById("v3_privacy_tr").style.display = "none";  //加密协议
}
function changeMyTr3(value){
	if(value==1){
		document.getElementById("community_tr").style.display = "block"; // 读共同体隐藏；
		document.getElementById("securityLevel_tr").style.display = "none"; //安全级别；
		document.getElementById("v3_ap_tr").style.display = "none";    //认证协议
		document.getElementById("v3_privacy_tr").style.display = "none";  //加密协议
		document.getElementById("snmpversion").options[0].selected = "selected";  
		
	}else{
		document.getElementById("community_tr").style.display = "none"; // 读共同体隐藏；
		document.getElementById("securityLevel_tr").style.display = "none"; //安全级别；
		document.getElementById("v3_ap_tr").style.display = "none";    //认证协议
		document.getElementById("v3_privacy_tr").style.display = "none";  //加密协议
	}
}
function changehMyTr(value){
	if(value==3){
		document.getElementById("community_tr").style.display = "none"; // 读共同体隐藏；
		document.getElementById("securityLevel_tr").style.display = "block"; //安全级别；
		document.getElementById("securityLevel").options[0].selected = "selected";
	}else{
		document.getElementById("community_tr").style.display = "block"; // 读共同体隐藏
		document.getElementById("securityLevel_tr").style.display = "none"; //安全级别；
		document.getElementById("v3_ap_tr").style.display = "none";    //认证协议
		document.getElementById("v3_privacy_tr").style.display = "none";  //加密协议
	}
}
function changehMyTr2(value){
	if(value==1){
		document.getElementById("community_tr").style.display = "none"; // 读共同体隐藏；
		document.getElementById("securityLevel_tr").style.display = "block"; //安全级别；
		document.getElementById("v3_ap_tr").style.display = "none";    //认证协议
		document.getElementById("v3_privacy_tr").style.display = "none";  //加密协议
	}
	if(value==2){
		document.getElementById("community_tr").style.display = "none"; // 读共同体隐藏；
		document.getElementById("securityLevel_tr").style.display = "block"; //安全级别；
		document.getElementById("v3_ap_tr").style.display = "block";    //认证协议
		document.getElementById("v3_privacy_tr").style.display = "none";  //加密协议
	}
	if(value==3){
		document.getElementById("community_tr").style.display = "none"; // 读共同体隐藏；
		document.getElementById("securityLevel_tr").style.display = "block"; //安全级别；
		document.getElementById("v3_ap_tr").style.display = "block";    //认证协议
		document.getElementById("v3_privacy_tr").style.display = "block";  //加密协议
	}
}
			
function unionSelect(){
	var type = document.getElementById("type");
	var ostype = document.getElementById("ostype");
	
	var typeValue = type.value;
	
	
	ostype.length = 0;
	
	if(typeValue == "1" || typeValue == "2" || typeValue == "3"){
		ostype.options[1] = new Option( "Cisco" ,"1");
		ostype.options[2] = new Option("H3C" , "2" );
		ostype.options[3] = new Option("Entrasys", "3" );
		ostype.options[4] = new Option("Radware", "4" );
		ostype.options[5] = new Option("MaiPu", "10");
		ostype.options[6] = new Option("RedGiant" , "11");
		ostype.options[7] = new Option("NorthTel", "12");
		ostype.options[8] = new Option("D-Link", "13");
		ostype.options[9] = new Option("D-BDCom" , "14");
		ostype.options[10] = new Option("ZTE" , "16");
		ostype.options[11] = new Option("Motorola " , "17");
		ostype.options[12] = new Option("Brocade " , "28");
		ostype.options[13] = new Option("HP " , "30");
		ostype.options[14] = new Option("Alcatel " , "45");
		ostype.options[15] = new Option("Avaya " , "46");
		ostype.options[16] = new Option("Juniper" , "47");
	}else if(typeValue == "4"){
		ostype.options[1] = new Option("Windows", "5" );
		ostype.options[2] = new Option("AIX", "6" );
		ostype.options[3] = new Option("HP UNIX", "7" );
		ostype.options[4] = new Option("SUN Solaris", "8" );
		ostype.options[5] = new Option("Linux" ,"9" );
		ostype.options[6] = new Option("AS400", "15");
		ostype.options[7] = new Option("SCOUNIXWARE", "20");
		ostype.options[8] = new Option("ScoOpenServer", "21");
	}else if(typeValue == "8"){
		ostype.options[1] = new Option("NOKIA", "23" );
		ostype.options[2] = new Option("netscreen", "24" );
		ostype.options[3] = new Option("pix", "25" );
		ostype.options[4] = new Option("hillstone", "26" );
		ostype.options[5] = new Option("DPtech", "29" );
		ostype.options[6] = new Option("SecWorld", "30" );
		ostype.options[7] = new Option("TippingPoint", "31" );
		ostype.options[8] = new Option("Venus", "32" );
		ostype.options[9] = new Option("Redgiant", "37" );
		ostype.options[10] = new Option("Topsec", "38" );
		ostype.options[11] = new Option("ChinaGuard", "39" );
		ostype.options[12] = new Option("checkpoint", "40" );
	}else if(typeValue == "9"){
		ostype.options[1] = new Option("ATM", "17" );
	}else if(typeValue == "13"){
		ostype.options[1] = new Option("Cisco", "34" );
		ostype.options[2] = new Option("Motorola", "36" );
	}else if(typeValue == "12"){
		ostype.options[1] = new Option("Array Networks", "18" );
		ostype.options[2] = new Option("SSL", "19" );
	}else if(typeValue == "11"){
		ostype.options[1] = new Option("F5", "27" );
	}else if(typeValue == "10"){
		ostype.options[1] = new Option("Cisco", "22" );
	}else if(typeValue == "14"){
		ostype.options[1] = new Option("日立", "35" );
		ostype.options[2] = new Option("EMC", "41" );
		ostype.options[3] = new Option("DELL", "42" );
		ostype.options[4] = new Option("IBM", "43" );
		ostype.options[5] = new Option("HP", "44" );
		ostype.options[6] = new Option("NetApp", "48" );
	}else if(typeValue == "15"){
		ostype.options[1] = new Option("VMWare", "40" );
	}
    //else if(typeValue == "16"){
		//ostype.options[1] = new Option("emerson", "47" );
	//}
	else if(typeValue == "17"){
		ostype.options[1] = new Option("emerson", "48" );
	}
}

unionSelect();

</script>

</HTML>                                                     