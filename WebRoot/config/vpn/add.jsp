<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="com.afunms.config.model.TimingBackupTelnetConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*" %>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript"src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>

		<script language="JavaScript" type="text/javascript">
 

function choosePort(){
		var sourceId=document.getElementById("sourceId").value;
		var desId=document.getElementById("desId").value;
		if(sourceId==""){
		   alert("请选择源设备");
		   return;
		  }
		  if(desId==""){
		     alert("请选择目标设备");
		     return;
		  }
        mainForm.action = "<%=rootPath%>/vpn.do?action=choosePort";
    	mainForm.submit();
}
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}  

</script>

		
		<script type="text/javascript">
			
			function showVpnList(id){
				var url="<%=rootPath%>/vpn.do?action=multi_vpn_netip&id="+id;
				window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
			}
		
	  		
		</script>

	</head>
	<body id="body" class="body" >


		<form name="mainForm" method="post">

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
											                	<td class="content-title">&nbsp;>> 资源 >> VPN管理 >> 添加</td>
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
				        										<input type="hidden" name="selVal" id='selVal' value=""/>
                                                                 <input type="hidden" name="textVal" id='textVal' value=""/>
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
													 						<tr>
																				<TD nowrap align="right" height="24" width="20%">
																					源网络设备：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;
																					<input name="sourceId" type="text" size="40" class="formStyle" id="sourceId" readonly="readonly" >
																					<input type="button" value="选择网络设备" onclick="showVpnList('sourceId')"><font color="red">&nbsp;* </font>
																				</TD>  
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="20%">
																					目标网络设备：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;
																					<input name="desId" type="text" size="40" class="formStyle" id="desId" readonly="readonly" >
																					<input type="button" value="选择网络设备" onclick="showVpnList('desId')"><font color="red">&nbsp;* </font>
																				</TD>  
																			</tr>
																			
																			
																																				
																			<tr >
																				<TD nowrap colspan="4" align=center > 
																				<br>
																				
																					<input type="button" id="process" onclick="choosePort()" style="width:50" value="下一步">&nbsp;&nbsp;  
																					<input type="reset" style="width: 50" value="返  回" onclick="javascript:history.back(1)">
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