<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%  
   String rootPath = request.getContextPath(); 
   List allbuss = (List)request.getAttribute("allbuss");
   Object obj = request.getAttribute("isTreeView");
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
     var chk1 = checkinput("backbone_name","string","�Ǹɵ�����",50,false);
     var chk4 = checkinput("loopback_begin","string","loopback��ַ����",15,false);
     var chk5 = checkinput("loopback_end","string","loopback��ַ����",15,false);
     var chk6 = checkinput("pe_begin","string","PE������ַ����",15,false);
     var chk7 = checkinput("pe_end","string","PE������ַ����",15,false);
     var chk8 = checkinput("pe_ce_begin","string","PE_CE��ַ����",15,false);
     var chk9 = checkinput("pe_ce_end","string","PE_CE��ַ����",15,false);
     var chk10 = checkinput("bussiness_begin","string","ҵ���ַ����",15,false);
     var chk11 = checkinput("bussiness_end","string","ҵ���ַ����",15,false);
     if(chk1&&chk4&&chk5&&chk6&&chk7&&chk8&&chk9&&chk10&&chk11){
        var ip3 = document.getElementById("loopback_begin").value;
        var ip4 = document.getElementById("loopback_end").value;
        var ip5 = document.getElementById("pe_begin").value;
        var ip6 = document.getElementById("pe_end").value;
        var ip7 = document.getElementById("pe_ce_begin").value;
        var ip8 = document.getElementById("pe_ce_end").value;
        var ip9 = document.getElementById("bussiness_begin").value;
        var ip10 = document.getElementById("bussiness_end").value;
   //     var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;//�������ʽ
        var patrn = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
        if( patrn.test(ip3) && patrn.test(ip4) && patrn.test(ip5) && patrn.test(ip6) && patrn.test(ip7) && patrn.test(ip8) && patrn.test(ip9) && patrn.test(ip10))
          {
                     Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
                     mainForm.action = "<%=rootPath%>/ipAll.do?action=add";
                     mainForm.submit();
                     return true;
                  }else{
                  alert("IP����");
                  return false;
                }
     }
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

//-- hukelei add at 2011-12-13 start ----------------
function setU(eventTextId , eventId){
	var cabinetid = document.getElementById("cabinetid");
	var roomid = document.getElementById("roomid");
	return CreateWindow('<%=rootPath%>/cabinetequipment.do?action=setunumber&cabinetid='+cabinetid.value+'&roomid='+roomid.value);
}
//-- hukelei add at 2010-12-13 end ----------------

//function toAllAdd(){
        // mainForm.target="alladd";
		// window.open("","alladd","toolbar=no,height=400, width= 500, top=200, left= 200,scrollbars=no"+"screenX=0,screenY=0")
		// mainForm.action='<%=rootPath%>/network.do?action=alladd';
		// mainForm.submit();
//}

function toAllAdd(){
	return CreateWindow('<%=rootPath%>/cabinetequipment.do?action=setBid');
}

function setNode(){
	
	return CreateWindow('<%=rootPath%>/cabinetequipment.do?action=setNode');
}

function setUser(){
	
	return CreateWindow('<%=rootPath%>/cabinetequipment.do?action=setUser');
}
function setBusinessSystem(){
	return CreateWindow('<%=rootPath%>/cabinetequipment.do?action=setBis');
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
//���Ӳ˵�	
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
				onclick="parent.addmanage()">���Ӽ���</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->

	<form id="mainForm" method="post" name="mainForm" >
		
		<table id="body-container" class="body-container">
			<tr>
				
				<%
					if(null == obj){
				%>
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td>
									<%=menuTable%>
								</td>
							</tr>
						</table>
					</td>
				<%
					}else{
						String isTreeView = (String)obj;
				%>	
				<input type="hidden" name="isTreeView" value="<%=isTreeView %>">
				<%
					}
				%>	
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
											                	<td class="add-content-title">IP���� >> ���ù��� >> IP����</td>
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
						
						<tr >						
							<TD nowrap align="right" height="24" width="10%">�Ǹɵ����ƣ�&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="backbone_name" id="" maxlength="50" size="20" class="formStyle" ></TD>
						</tr>
						<!--  
						<tr >						
							<TD nowrap align="right" height="24" width="10%">����ƽ�棺&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<select name='select' class="formStyle" style='width:125px;'>
								    <option value=1>---��ѡ��---</option>
								    <option value=2>һƽ��</option>
								    <option value=3>��ƽ��</option>
								</select>
								</TD>
						</tr>
						<tr style="background-color: #ECECEC;" id='community_tr'>				
							<TD nowrap align="right" height="24" width="10%">�Ǹɵ��ַ���壺&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="backbone_begin" id="backbone_begin" maxlength="50" size="20" class="formStyle">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="backbone_end" id="backbone_end" maxlength="50" size="20" class="formStyle">
								</TD>															
						</tr>
						-->
						<tr style="background-color: #ECECEC;" id='community_tr'>				
							<TD nowrap align="right" height="24" width="10%">loopback��ַ���壺&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="loopback_begin" id="loopback_begin" maxlength="50" size="20" class="formStyle">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="loopback_end" id="loopback_end" maxlength="50" size="20" class="formStyle">
								</TD>															
						</tr>
						<tr  id='community_tr'>				
							<TD nowrap align="right" height="24" width="10%">PE������ַ���壺&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="pe_begin" id="pe_begin" maxlength="50" size="20" class="formStyle">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="pe_end" id="pe_end" maxlength="50" size="20" class="formStyle">
								</TD>															
						</tr>
						<tr style="background-color: #ECECEC;" id='community_tr'>				
							<TD nowrap align="right" height="24" width="20%">PE-CE��ַ���壺&nbsp;</TD>				
							<TD nowrap width="30%" >&nbsp;
								<input type="text" name="pe_ce_begin" id="pe_ce_begin" maxlength="50" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; �� 
							</TD>			
							<TD nowrap width="30%" >&nbsp;
								<input type="text" name="pe_ce_end" id="pe_ce_end" maxlength="50" size="20" class="formStyle">
							</TD>																			
						</tr>
						<tr style="background-color: #ECECEC;" id='community_tr'>				
							<TD nowrap align="right" height="24" width="20%">ҵ���ַ�ζ��壺&nbsp;</TD>				
							<TD nowrap width="30%" >&nbsp;
								<input type="text" name="bussiness_begin" id="bussiness_begin" maxlength="50" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp; �� 
							</TD>			
							<TD nowrap width="30%" >&nbsp;
								<input type="text" name="bussiness_end" id="bussiness_end" maxlength="50" size="20" class="formStyle">
							</TD>																			
						</tr>
												
						<tr >
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">��Ҫһ��ʱ��,���Ժ�...</font></div>
							</td>
						</tr>			
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
															
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
	}else if(typeValue == "9"){
		ostype.options[1] = new Option("ATM", "17" );
	}else if(typeValue == "12"){
		ostype.options[1] = new Option("Array Networks", "18" );
	}else if(typeValue == "11"){
		ostype.options[1] = new Option("F5", "27" );
	}else if(typeValue == "10"){
		ostype.options[1] = new Option("Cisco", "22" );
	}
	
}

unionSelect();

</script>

</HTML>                                                     