<%@page language="java" contentType="text/html;charset=gb2312" %>
<%
String rootPath = request.getContextPath();  
String tagname = request.getParameter("tagname");
String id = request.getParameter("id");
%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script language="JavaScript" type="text/JavaScript">
function init(){
	var macId = '<%=tagname + id%>';
	var fSelectObj = window.opener.document.getElementById(macId);
	var cSelectObj = document.getElementById("macs");
	for(var i=0;i<fSelectObj.length;i++){
		var option=document.createElement('option');
		option.text = fSelectObj.options[i].text;
		option.value = fSelectObj.options[i].text;
		try{
			cSelectObj.add(option,null);
		}catch(e){
			cSelectObj.add(option);
		}
	}
}

function add(){
	var mac = document.getElementById("mac").value;
	mac = mac.replace(/^\s+|\s+$/g,"");
	if(!mac){
		alert("mac不能为空！");
		return;
	}
	if(mac.length != 17){
		alert("mac地址长度不正确，应该是17位。");
		return;
	}
	var reg_name = /[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}:[a-fA-F\d]{2}/;
	var flag = reg_name.test(mac);
	if(!flag){
		alert("mac地址不正确，mac是由0-9a-fA-F.组成。");
		return;
	}
	
	var option=document.createElement('option');
	option.text = mac;
	option.value = mac;
	var cSelectObj = document.getElementById("macs");
	for(var i=0;i<cSelectObj.length;i++){
		if(mac == cSelectObj.options[i].value){
			alert("已经存在！");
			return;
		}
	}
	try{
		cSelectObj.add(option,null);
	}catch(e){
		cSelectObj.add(option);
	}
	document.getElementById("mac").value = "";
}

function del(){
	var cSelectObj = document.getElementById("macs");
	for(var i=cSelectObj.length-1;i>=0;i--){
		if(cSelectObj.options[i].selected){
			cSelectObj.remove(i);
		}
	}
}

function save(){
	var cSelectObj = document.getElementById('macs');
	var macstr = "";
	for(var i=0;i < cSelectObj.length;i++){
		macstr += cSelectObj.options[i].value + ",";
	}
	if(macstr.length > 0){
		macstr = macstr.substr(0,macstr.length-1);
	}
	
	$.ajax({
		type:"GET",
		dataType:"json",
		url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=updatemac&id=<%=id %>&mac="+macstr + "&t=" + Date(),
		success:function(data){
			window.alert("修改成功！");
			var macId = '<%=tagname + id%>';
			var fSelectObj = window.opener.document.getElementById(macId);
			fSelectObj.options.length = 0;
			
			window.opener.editMac('<%=tagname + id%>', macstr);
			window.close();
		}
	});
	
}
</script>

</head>
<body id="body" class="body" onload="init()" style="margin:0;padding:0;">
					<table id="container-main" class="container-main" style="margin:0;padding:0;">
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
											                	<td class="add-content-title">修改Mac地址</td>
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
																		<tr style="background-color: #ECECEC;">
														    				<TD nowrap align="center" height="24" width="30%">Mac地址&nbsp;</TD>				
																			<TD nowrap width="70%">&nbsp;<input type="text" id="mac" name="mac" size="25" class="formStyle"><font color="red">*</font>
																				<input type="button" value="增加" onclick="add()">
																			</TD>
						                                            	</tr>
						                                            	
																		<tr>
														    				<TD nowrap align="center" height="24" width="30%">Mac列表&nbsp;</TD>				
																			<TD nowrap width="70%">&nbsp;<select id="macs" size="5" multiple="multiple" style="width:160px;"></select>
																			</TD>
						                                            	</tr>
									            							
																		<tr style="background-color: #ECECEC;">
																			<TD nowrap colspan="2" align=center>
																			<br><input type="button" value="保 存" style="width:50" id="process" onclick="save();">&nbsp;&nbsp;
																				<input type="reset" style="width:50" value="删 除" onclick="del();">&nbsp;&nbsp;<input type="reset" style="width:50" value="关 闭" onclick="window.close();">
																			</TD>	
																		</tr>	
							
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
</body>
</HTML>
