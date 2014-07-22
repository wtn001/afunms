<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*"%>

<%
	String rootPath = request.getContextPath();
	String commands = (String) request.getAttribute("commands");
	String deviceType=(String)request.getAttribute("deviceType");
	//String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
	String selected1="";
	String selected2="";
	String selected3="";
	if(deviceType!=null){
		if(deviceType.equals("zte")){
			selected1="selected";
		}else if(deviceType.equals("cisco")){
			selected2="selected";
		}else if(deviceType.equals("h3c")){
			selected3="selected";
		}
	}
%>

<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
        <script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
        <script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/jquery/jquery-1.4.2.min.js"></script>
		<link rel="stylesheet" href="<%=rootPath%>/resource/css/discover.css" type="text/css">
		<script language="JavaScript" type="text/javascript">

Ext.onReady(function(){

 Ext.get("backup").on("click",function(){
	  var name=$("#name").val();
	 var flag=$("#flag").val();
	   if(name==""){
	   alert("���Ʋ�����Ϊ�գ�");
	   return;
	   }
	   if(flag=="1"){
	    alert("�����Ѵ���");
	    return;
	   }
     	Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
        mainForm.action = "<%=rootPath%>/vpn.do?action=saveVpnCmdCfg";
        mainForm.submit();
    
 });
});
 function isExistFileName(){
		 var name=$("#name").val();
	        if(name==""){
	         alert("���Ʋ�����Ϊ�գ�");
	         return;
	       }
		var dataUrl="name="+encodeURIComponent(name);
		
			 $.ajax({
			type:"POST",
			dataType:"json",
			url:"<%=rootPath%>/vpnAjaxManager.ajax?action=isExistFileName",
			data:dataUrl,
			success:function(data){
			$("#flag").val(data.isSucess);
			if(data.isSucess=="1")alert("�����Ѵ���");
			  
			}
		});
	}
</script>


	</head>
	<body id="body">



		<form name="mainForm" method="post">
			<input type=hidden name="commands" value="<%=commands%>">
			<table id="body-container" class="body-container">
				<tr style="background-color: #FFFFFF;">
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
																	<td align="left" width="5">
																		<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																			width="5" height="29" />
																	</td>
																	<td class="content-title">
																		&nbsp;���������ļ�
																	</td>
																	<td align="right">
																		<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																			width="5" height="29" />
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table width="100%" border=0 cellpadding=0 cellspacing=1>

																<tr>
																	<td>
																		<table id="detail-content-body"
																			class="detail-content-body">
																			<tr>
																				<td>
                                                                                  <input type="hidden" name="flag" id="flag"  value="">
																					<table border="0" id="table1" cellpadding="0"
																						cellspacing="1" width="100%">
																						<tr>
																							<td colspan="4" width='100%' align="center"
																								bgcolor="#E6E6FA" height="27"></td>
																						</tr>
																						<tr>
																							<TD nowrap align="left" height="24" width="10%">
																								&nbsp;&nbsp;<font color="red">*&nbsp;</font>��   �ƣ�&nbsp;
																							</TD>
																							<TD nowrap width="40%">
																								&nbsp;
																								<input type="text" name="name" id="name" maxlength="50"
																									size="30" class="formStyle" value="" onblur="isExistFileName()">
																							</TD>
																							<TD nowrap align="left" height="24" width="10%">
																								&nbsp;&nbsp;��   ����&nbsp;
																							</TD>
																							<TD nowrap width="40%">
																								&nbsp;
																								<input type="text" name="fileDesc" id="fileDesc"
																									maxlength="60" size="40" class="formStyle"
																									value="">
																							</TD>
																							
																						</tr>
																						<tr>
																							<td colspan="4" width='100%' align="center"
																								bgcolor="#E6E6FA" height="27"></td>
																						</tr>
																						<tr >

																							<TD nowrap align="left" height="24" width="10%">
																								&nbsp;&nbsp;&nbsp;&nbsp;���&nbsp;
																							</TD>
																							<TD nowrap width="40%">
																								&nbsp;
																								<select id="vpnType" name="vpnType">
																									<option value="policy">
																										����
																									</option>
																									<option value="interface">
																										�˿�
																									</option>
																									<option value="deploy">
																										����
																									</option>
																									<option value="auto">
																										�Զ���
																									</option>
																									
																								</select>
																							</TD>
																							<TD nowrap align="left" height="24" width="10%">
																								&nbsp;&nbsp;�豸���ͣ�&nbsp;
																							</TD>

																							<TD nowrap width="40%">
																								&nbsp;
																								<select id="deviceType" name="deviceType">
																								     <option value="zte" <%=selected1 %>>
																										ZTE
																									</option>
																									<option value="cisco" <%=selected2 %>>
																										Cisco
																									</option>
																									<option value="h3c" <%=selected3 %>>
																										H3C
																									</option>
																								</select>
																							</TD>
																						</tr>
																						
																						<tr>
																							<td colspan="4" width='100%' align="center"
																								bgcolor="#E6E6FA" height="27"></td>
																						</tr>
																						<tr>
																							<TD nowrap colspan="4" align=center>
																								<br>
																								<br>

																								<input type="button" id="backup"
																									style="width: 50" value="ȷ  ��" style="width:50"
																									class=btn_mouseout
																									onmouseover="this.className='btn_mouseover'"
																									onmouseout="this.className='btn_mouseout'"
																									onmousedown="this.className='btn_mousedown'"
																									onmouseup="this.className='btn_mouseup'">
																								&nbsp;&nbsp;
																								<input type="button" style="width: 50"
																									value="�� ��" onclick="window.close()"
																									style="width:50" class=btn_mouseout
																									onmouseover="this.className='btn_mouseover'"
																									onmouseout="this.className='btn_mouseout'"
																									onmousedown="this.className='btn_mousedown'"
																									onmouseup="this.className='btn_mouseup'">
																								&nbsp;&nbsp;
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
																		<table id="detail-content-footer"
																			class="detail-content-footer">
																			<tr>
																				<td>
																					<table width="100%" border="0" cellspacing="0"
																						cellpadding="0">
																						<tr>
																							<td align="left" valign="bottom">
																								<img
																									src="<%=rootPath%>/common/images/right_b_01.jpg"
																									width="5" height="12" />
																							</td>
																							<td></td>
																							<td align="right" valign="bottom">
																								<img
																									src="<%=rootPath%>/common/images/right_b_03.jpg"
																									width="5" height="12" />
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
					</td>
				</tr>
			</table>
		</form>
	</BODY>


</HTML>