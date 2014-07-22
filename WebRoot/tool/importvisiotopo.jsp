<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	List list = (List) request.getAttribute("list");
	Object success = request.getAttribute("success");
%>
<html>
	<head>
		<title>导入Visio设备拓扑</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
		<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
		<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
		
		
		<script language="JavaScript" type="text/javascript">
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
	});
});
</script>
<script language="JavaScript" type="text/javascript">
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
        	
      var chk=true;
        if($('#collecttype').val()=='1')
	 	var chk = checkinput("community","string","共同体",30,false);
     if(chk)
     {
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
         mainForm.action = "<%=rootPath%>/macconfig.do?action=importVisioTopo&flag=1";
        
         mainForm.submit();
     }  
 });	
	
});
//-- nielin modify at 2010-01-04 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


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

	if(<%=success%> == null || <%=success%>=='undefined'){
		var div = document.getElementById('upload');
		div.style.display = 'block';
		div = document.getElementById('result');
		div.style.display = 'none';
	}else{
		document.getElementById('result').style.display = ''; 
		document.getElementById('upload').style.display = 'none';
	}
}

</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm"  >
			<table id="body-container" class="body-container">
				<tr>
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
																	<td class="add-content-title">工具 &gt;&gt; 导入Visio设备拓扑
																	</td>
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
																		<div id="upload">
																			<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																				<tr>
																					<TD nowrap align="right" height="24" width="10%">采集方式&nbsp;</TD>
																					<TD nowrap width="40%">&nbsp;
																						<select size=1 name='collecttype' style='width: 100px;' id='collecttype'>
																							<option value='1' selected="selected">SNMP</option>
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
																								<TD nowrap width="40%">&nbsp;
																									<select size=1 style='width: 100px;' id='snmpversion' name='snmpversion'>
																										<option value='0' selected>V1</option>
																										<option value='1'>V2</option>
																									</select>&nbsp;
																								</TD>
																							</tr>
																						</table>
																					</TD>
																				</tr>
																				<tr style="background-color: #ECECEC;" id='community_tr'>
																					<TD nowrap align="right" height="24" width="10%">读共同体&nbsp;</TD>
																					<TD nowrap width="40%">&nbsp;
																						<input type="text" name="community" maxlength="50" size="20" class="formStyle">
																						<font color="red">&nbsp;*</font>
																					</TD>
																					<TD nowrap align="right" height="24" width="10%">写共同体&nbsp;</TD>
																					<TD nowrap width="40%">&nbsp;
																						<input type="text" name="writecommunity" maxlength="50" size="20" class="formStyle">
																					</TD>
																				</tr>
																				<tr style="background-color: #ECECEC;">
																					<TD nowrap align="right" height="24">所属业务&nbsp;</TD>
																					<td nowrap colspan="3" height="1">&nbsp
																						<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="40" maxlength="32" value="">&nbsp;&nbsp;
																						<input type=button value="设置所属业务" onclick='setBid("bidtext" , "bid");'>
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
																									<td height="28" align="left" width=15%>&nbsp;紧急</td>
																									<td height="28" align="right" width=10%%>
																										<INPUT type="checkbox" class=noborder name="fcheckbox" value="1">
																									</td>
																									<td height="28" align="left" width=15%>&nbsp;报警</td>
																									<td height="28" align="right" width=10%>
																										<INPUT type="checkbox" class=noborder name="fcheckbox" value="2">
																									</td>
																									<td height="28" align="left" width=15%>&nbsp;关键</td>
																									<td height="28" align="right" width=10%>
																										<INPUT type="checkbox" class=noborder name="fcheckbox" value="3">
																									</td>
																									<td height="28" align="left" width=15%>&nbsp;错误</td>
																								</tr>
																								<tr align="left" valign="center" bgcolor=#ffffff>
																									<td height="28" align="right" width=10%>
																										<INPUT type="checkbox" class=noborder name="fcheckbox" value="4">
																									</td>
																									<td height="28" align="left" width=15%>&nbsp;警告</td>
																									<td height="28" align="right" width=10%>
																										<INPUT type="checkbox" class=noborder name="fcheckbox" value="5">
																									</td>
																									<td height="28" align="left" width=15%>&nbsp;通知</td>
																									<td height="28" align="right" width=10%>
																										<INPUT type="checkbox" class=noborder name="fcheckbox" value="6">
																									</td>
																									<td height="28" align="left" width=15%>&nbsp;提示</td>
																									<td height="28" align="right" width=10%>
																										<INPUT type="checkbox" class=noborder name="fcheckbox" value="7">
																									</td>
																									<td height="28" align="left" width=15%>&nbsp;调试</td>
																								</tr>
																							</tbody>
																						</table>
																					</TD>
																				</tr>
																				<tr>
																					<TD nowrap colspan="4" align=center>
																						<br>
																						<input type="button" value="确 定" style="width: 50" id="process" onclick="#">&nbsp;&nbsp;
																						<input type="reset" style="width: 50" value="关闭" onclick="javascript:window.close();">
																					</TD>
																				</tr>
																			</TABLE>
																		</div>
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
																				<td align="left" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" />
																				</td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" />
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
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</body>
</HTML>