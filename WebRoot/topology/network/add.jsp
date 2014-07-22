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
	$(addTimeConfigRow);//addTimeConfigRow������application/resource/js/addTimeConfig.js�� 
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
     var chk1 = checkinput("ip_address","ip","IP��ַ",15,false);
     var chk2 = checkinput("alias","string","������",30,false);
     var chk3=true;
     var chk4=true;
     var chk5=true;
     var chk7=true;
     var chk8=true;
     var chk9=true;
	 if($('#collecttype').val()=='10'&&$('#type').val()=='15'&&$('#ostype').val()=='40'){
	     
        var chk4 = checkinput("uname","string","�û���",30,false);
        var chk5 = checkinput("pw","string","����",30,false);
        }	
    if($('#collecttype').val()=='10'&&$('#type').val()=='14'&&$('#ostype').val()=='41'){
	     
        var chk4 = checkinput("uname","string","�û���",30,false);
        var chk5 = checkinput("pw","string","����",30,false);
     }
	 if($('#collecttype').val()=='1'){
    	 if($("#snmpversion").val()!="3"){
    		var chk3 = checkinput("community","string","����ͬ��",30,false);
    	 }
    	 if($("#snmpversion").val()=="3"){
	 		 if($("#securityLevel").val()=="1"){
	 			 var chk7 = checkinput("securytyName","string","��ȫ�������û���",30,false);
	 		 }
	 		 if($("#securityLevel").val()=="2"){
	 			 var chk7 = checkinput("securytyName","string","��ȫ�������û���",30,false);
	 			 var chk8 = checkinput("authPassPhrase","string","ͨ����",30,false);
	 		 }
	 		 if($("#securityLevel").val()=="3"){
	 			 var chk7 = checkinput("securytyName","string","��ȫ�������û���",30,false);
	 			 var chk8 = checkinput("authPassPhrase","string","ͨ����",30,false);
	 			 var chk9 = checkinput("privacyPassPhrase","string","����Э����",30,false);
	 		 }
	 	 }
    }

     if(chk1&&chk2&&chk3&&chk4&&chk5&&chk7&&chk8&&chk9)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
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
//�޸Ĳ˵������¼�ͷ����
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
//��Ӳ˵�	
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
	* �˷������ڶ��ŷ�ʱ��ϸ��Ϣ
	* ������ /application/resource/js/timeShareConfigdiv.js 
	*/
	//�����û����б�
	var action = "<%=rootPath%>/user.do?action=setReceiver";
	// ��ȡ���ŷ�ʱ��ϸ��Ϣ��div
	function timeShareConfiginit(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		// ��ȡ�豸�����ķ�ʱ�����б�,
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

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">������Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">ȡ������</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">��Ӽ���</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->

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
											                	<td class="add-content-title">��Դ >> �豸ά��>> �豸���</td>
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
							<TD nowrap align="right" height="24" width="10%">IP��ַ&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="ip_address" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>&nbsp;&nbsp;<a href="#" onclick="toAllAdd()">����׷��</a></TD>						
							<TD nowrap align="right" height="24" width="10%">������&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="alias" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font></TD>															
						</tr>
						<tr>
							<TD nowrap align="right" height="24" width="10%">�ɼ���ʽ&nbsp;</TD>
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='collecttype' style='width:100px;' id='collecttype' onclick='changehid();'>
            						<option value='1' selected>SNMP</option>
            						<option value='2'>����</option>
            						<option value='3'>Ping</option>
            						<option value='4'>Զ��Ping</option>
            						<option value='5'>WMI</option>
            						<option value='6'>Telnet</option>
            						<option value='7'>SSH</option>
            						<option value='8'>Telnet������</option>
            						<option value='9'>SSH������</option>
            						<option value='10'>���ݽӿ�</option>
            					</select>&nbsp;
            					</TD>
            					<TD colspan=2>
            					<table>
            					<tr id='collectversion'>
            					<TD nowrap align="right" height="24" width="10%">�ɼ��汾&nbsp;</TD>
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
							<TD nowrap align="right" height="24" width="10%">����ͬ��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="community" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font></TD>						
							<TD nowrap align="right" height="24" width="10%">д��ͬ��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="writecommunity" maxlength="50" size="20" class="formStyle">
								</TD>															
						</tr>
					
						<tr style="background-color: #ECECEC;" id='securityLevel_tr' >
							<TD nowrap  align="right" height="24" width="10%">��ȫ����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1  style='width:100px;' id='securityLevel' name="securityLevel" onchange='changehMyTr2(this.value);'  >
	            						<option value='1' selected>noAutoNoPriv</option>
	            						<option value='2'>autoNoPriv</option>
	            						<option value='3'>autoPriv</option>
	            				</select>
							</TD>						
							<TD nowrap  align="right" height="24" width="10%">�û���&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="securytyName" name="securityName" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>
							</TD>
						</tr>
						
						<tr id='v3_ap_tr' >
							<TD nowrap  align="right" height="24" width="10%">��֤Э��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1  style='width:100px;' id='v3_ap' name="v3_ap">
	            						<option value='1' selected >MD5</option>
	            						<option value='2'>SHA</option>
	            				</select>
							</TD>						
							<TD nowrap  align="right" height="24" width="10%">ͨ����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="authPassPhrase" name="authPassPhrase" maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;" id='v3_privacy_tr' >
							<TD nowrap  align="right" height="24" width="10%">����Э��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1  style='width:100px;' id='v3_privacy' name="v3_privacy">
	            						<option value='1' selected>DES</option>
	            						<option value='2'>AED</option>
	            				</select>
							</TD>						
							<TD nowrap align="right" height="24" width="10%">����Э����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="privacyPassPhrase" name="privacyPassPhrase"  maxlength="50" size="20" class="formStyle">
								<font color="red">&nbsp;*</font>
							</TD>
						</tr>
				<!-- �������  ���ؿ�-->	
						<tr >												
							<TD nowrap align="right" height="24" width="10%">�豸����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='type' style='width:100px;' onchange="unionSelect();" id='type'>
            								<option value='1' selected>·����</option>
            								<option value='2'>·�ɽ�����</option>
            								<option value='3'>������</option>
            								<option value='4'>������</option>
            								<option value='8'>����ǽ</option>
            								<option value='9'>ATM</option>
            								<option value='10'>�ʼ���ȫ����</option>
            								<option value='11'>F5</option>
            								<option value='12'>VPN</option>
            								<option value='13'>CMTS</option>
            								<option value='14'>�洢</option>
            								<option value='15'>���⻯</option>
                                            <!-- <option value='16'>�յ�</option> -->
                                            <option value='17'>UPS</option>
            							</select>&nbsp;</TD>
            				<TD nowrap align="right" height="24" width="10%">���䷽��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								
								<select size=1 name='transfer' id='transfer' style='width:100px;' >
								            <option value='0' selected>��</option>
            								<option value='1'>��һ��</option>
            								
            								
            							</select>&nbsp;
								</TD>			
							
						</tr>
						<tr  style="background-color: #ECECEC;">												
							<TD nowrap align="right" height="24" width="10%">����ϵͳ&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='ostype' style='width:100px;' onclick='changes();' id='ostype'>
									<option value='0' selected> </option>
            								
            							</select>&nbsp;</TD>
							<TD nowrap align="right" height="24" width="10%">�Ƿ����&nbsp;</TD>				
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='manage' style='width:100px;'>
            								<option value='1' selected>��</option>
            								<option value='0'>��</option>
            							</select>&nbsp;</TD>            																						
						</tr>
						<tr  style="display:none" id="messageadd1">												
							<TD nowrap align="right" height="24" width="10%">������&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<select size=1 name='subtype' style='width:100px;'>
            								<option value='EMC_VNX' selected>EMC_VNX</option>
            								<option value='EMC_DMX'>EMC_DMX</option>
            								<option value='EMC_VMAX'>EMC_VMAX</option>
            							</select>&nbsp;</TD>											
						</tr>
						<tr  style="display:none" id="messageadd">												
							<TD nowrap align="right" height="24" width="10%">�û���&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;&nbsp;<input type="text" id="uname" name="uname" size="20" maxlength="50" class="formStyle"/>
							<font color="red">&nbsp;*</font></TD>											
							<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="password" id="pw" name="pw" size="20" maxlength="50" class="formStyle"/>
								<font color="red">&nbsp;*</font></TD>          																						        																						
						</tr>					
						<!-- snow modify begin */ 2010-05-18 -->
						<tr>												
							<TD nowrap align="right" height="24" width="10%">��Ӧ��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select size=1 name='supper' style="width:260px;">
									<option value='0' selected></option>
									<c:forEach items="${allSupper}" var="al">
            							<option value='${al.su_id }'>${al.su_name }��${al.su_dept}��</option>
            						</c:forEach>
            					</select>
            				</TD>	
            				<TD nowrap align="right" height="24" width="10%">�ͻ��˸澯&nbsp;</TD>				
							<TD nowrap width="40%" >&nbsp;
								<select size=1 name='messageflag' style='width:100px;'>
            								<option value='1' >��</option>
            								<option value='0' selected>��</option>
            							</select>&nbsp;
            				</TD>
            																									
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">�豸�ʲ����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="assetid" maxlength="70" size="20" class="formStyle">
														
							<TD nowrap align="right" height="24" width="10%">�豸����λ��&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="location" maxlength="80" size="20" class="formStyle">
																						
						</tr>
						<!-- snow modify end -->
						
												
						<tr >
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">��Ҫһ��ʱ��,���Ժ�...</font></div>
							</td>
						</tr>			
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
			<!--<tr>
				<TD nowrap align="right" height="24">���Ž�����&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendmobiles" name="sendmobiles" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="���ö��Ž�����" onclick='setReceiver("sendmobiles");'>
				</td>
			</tr>
			<tr >	
				<TD nowrap align="right" height="24">�ʼ�������&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendemail" name="sendemail" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="�����ʼ�������" onclick='setReceiver("sendemail");'>
				</td>
			</tr>
			<tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">�绰������&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" id="sendphone" name="sendphone" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="���õ绰������" onclick='setReceiver("sendphone");'>
				</td>
			</tr>-->
			
		
            <tr style="background-color: #ECECEC;">	
				<TD nowrap align="right" height="24">����ҵ��&nbsp;</TD>
				<td nowrap colspan="3" height="1">
				<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="">&nbsp;&nbsp;<input type=button value="��������ҵ��" onclick='setBid("bidtext" , "bid");'>
				<input type="hidden" id="bid" name="bid" value="">
				</td>
			</tr>
            <tr>
            <TD nowrap align="right" height="24" width="10%">SysLog����&nbsp;</TD>	
           <TD nowrap width="40%" colspan=3>&nbsp;
            						<table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center">
                        			<tbody>
							<tr align="left" valign="center" bgcolor=#ECECEC> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="0"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
                    						<td height="28" align="right" width=10%%><INPUT type="checkbox" class=noborder name="fcheckbox" value="1"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="2"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;�ؼ�</td> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="3"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
                    					</tr>
                    					<tr align="left" valign="center" bgcolor=#ffffff> 
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="4"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="5"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;֪ͨ</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="6"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;��ʾ</td>
                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder name="fcheckbox" value="7"></td>
                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
                    					</tr>
            					</tbody>
            					</table>
			</TD>																			
							</tr>
							
							<!-- snow modify begin (timeConfig div)*/ 2010-05-12 -->
							<tr>
							 	<td nowrap align="right" width="10%" style="height:35px;">��Ϣ�ɼ�ʱ��&nbsp;</td>		
							 	<td nowrap  colspan="3">
							        <div id="formDiv">         
						                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
					                        <tr>
					                            <td align="left">  
						                            <br>
					                                <table id="timeConfigTable" style="width:70%; padding:0;  background-color:#FFFFFF; position:relative; left:15px;" >
				                                        <tr>
				                                            <td colspan="7" height="50" align="center">
				                                                <span id="addTimeConfigRow" style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand; line-height:15px">����һ��</span>
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
														                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;���ŷ�����ϸ����</td>
														                        </tr>
														                        <tr>
														                            <td align="center">           
														                                <table id="smsConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
													                                        <tr>
													                                            <td colspan="0" height="50" align="center"> 
													                                                <span  onClick='addRow("smsConfigTable","sms");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">����һ��</span>
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
														                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;�绰������ϸ����</td>
														                        </tr>
														                        <tr>
														                            <td align="center">           
														                                <table id="phoneConfigTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
													                                        <tr>
													                                            <td colspan="0" height="50" align="center"> 
													                                                <span  onClick='addRow("phoneConfigTable","phone");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;">����һ��</span>
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
																<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
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
	document.getElementById("community_tr").style.display = "block"; // ����ͬ�����أ�
	document.getElementById("securityLevel_tr").style.display = "none"; //��ȫ����
	document.getElementById("v3_ap_tr").style.display = "none";    //��֤Э��
	document.getElementById("v3_privacy_tr").style.display = "none";  //����Э��
}
function changeMyTr3(value){
	if(value==1){
		document.getElementById("community_tr").style.display = "block"; // ����ͬ�����أ�
		document.getElementById("securityLevel_tr").style.display = "none"; //��ȫ����
		document.getElementById("v3_ap_tr").style.display = "none";    //��֤Э��
		document.getElementById("v3_privacy_tr").style.display = "none";  //����Э��
		document.getElementById("snmpversion").options[0].selected = "selected";  
		
	}else{
		document.getElementById("community_tr").style.display = "none"; // ����ͬ�����أ�
		document.getElementById("securityLevel_tr").style.display = "none"; //��ȫ����
		document.getElementById("v3_ap_tr").style.display = "none";    //��֤Э��
		document.getElementById("v3_privacy_tr").style.display = "none";  //����Э��
	}
}
function changehMyTr(value){
	if(value==3){
		document.getElementById("community_tr").style.display = "none"; // ����ͬ�����أ�
		document.getElementById("securityLevel_tr").style.display = "block"; //��ȫ����
		document.getElementById("securityLevel").options[0].selected = "selected";
	}else{
		document.getElementById("community_tr").style.display = "block"; // ����ͬ������
		document.getElementById("securityLevel_tr").style.display = "none"; //��ȫ����
		document.getElementById("v3_ap_tr").style.display = "none";    //��֤Э��
		document.getElementById("v3_privacy_tr").style.display = "none";  //����Э��
	}
}
function changehMyTr2(value){
	if(value==1){
		document.getElementById("community_tr").style.display = "none"; // ����ͬ�����أ�
		document.getElementById("securityLevel_tr").style.display = "block"; //��ȫ����
		document.getElementById("v3_ap_tr").style.display = "none";    //��֤Э��
		document.getElementById("v3_privacy_tr").style.display = "none";  //����Э��
	}
	if(value==2){
		document.getElementById("community_tr").style.display = "none"; // ����ͬ�����أ�
		document.getElementById("securityLevel_tr").style.display = "block"; //��ȫ����
		document.getElementById("v3_ap_tr").style.display = "block";    //��֤Э��
		document.getElementById("v3_privacy_tr").style.display = "none";  //����Э��
	}
	if(value==3){
		document.getElementById("community_tr").style.display = "none"; // ����ͬ�����أ�
		document.getElementById("securityLevel_tr").style.display = "block"; //��ȫ����
		document.getElementById("v3_ap_tr").style.display = "block";    //��֤Э��
		document.getElementById("v3_privacy_tr").style.display = "block";  //����Э��
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
		ostype.options[1] = new Option("����", "35" );
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