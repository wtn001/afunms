<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.ArrayList"%>
<%@ include file="/include/globe.inc"%>

<%
  String rootPath = request.getContextPath();
String cabinetId=request.getParameter("cabinetId");
String id=request.getParameter("id");
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script>
		function saveDevice(){
		//         parent.location.reload();
		            mainForm.action = "<%=rootPath%>/device.do?action=addExternalDevice&flag=1";
					mainForm.submit();
					
					}
		</script>
	</head>
	<body id="body" class="body">
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										
		        						<tr>
		        							<td align="left">
		        								<table id="content-body" class="content-body" align="left">
		        									<tr align="left">
														
														<td width="80%" align="right" valign="middle" >
															<div id="showNodeDiv" >
																<div><input name="id" id="id" type="hidden"  value="<%=id %>"></div>
																<div><input name="cabinetId" id="cabinetId" type="hidden" value="<%=cabinetId %>"></div>
																
																<div>
																<table>
																       <tr>
																          <td>
																           �豸ID��
																           </td>
																           <td>
																           <input name="deviceId" id="deviceId" type="text">
																           </td>
																       </tr>
																       <tr>
																           <td>
																           �豸IP��
																           </td>
																           <td>
																           <input name="ipaddress" id="ipaddress" type="text">
																           </td>
																       </tr>
																        <tr>
																           <td>
																           ϵͳ���ͣ�
																           </td>
																           <td>
																           <select size=1 name='osType' style='width:100px;' id='osType'>
											            						<option value='h3c' selected>h3c</option>
											            						<option value='huawei'>huawei</option>
											            						
											            					</select>
																         
																           </td>
																       </tr>
																        <tr>
																           <td>
																           Ʒ�ƣ�
																           </td>
																           <td>
																           <select size=1 name='brand' style='width:100px;' id='brand'>
											            						<option value='h3c' selected>h3c</option>
											            						<option value='huawei'>huawei</option>
											            					</select>
																         
																           </td>
																       </tr>
																       <tr>
																           <td>
																           �豸���ͣ�
																           </td>
																           <td>
																            <select size=1 name='deviceType' style='width:100px;' id='deviceType'>
											            						<option value='�����' selected>�����</option>
											            						<option value='���Ӿ���'>���Ӿ���</option>
											            						<option value='�����'>�����</option>
											            						<option value='�����ն�'>�����ն�</option>
											            						<option value='������'>������</option>
											            						
											            					</select>
																         
																           </td>
																       </tr>
																       <tr>
																           <td>
																           ����ͺţ�
																           </td>
																           <td>
																            <select size=1 name='specification' style='width:100px;' id='specification'>
											            						<option value='SISO1885' selected>SISO1885</option>
											            						<option value='SISO2885'>SISO2885</option>
											            					</select>
																           </td>
																       </tr>
																       <tr>
																           <td>
																           ��Ӧ�̣�
																           </td>
																           <td>
																            <select size=1 name='supplier' style='width:100px;' id='supplier'>
											            						<option value='h3c' selected>h3c</option>
											            						<option value='huawei'>huawei</option>
											            					</select>
																           </td>
																       </tr>
																       <tr>
																           <td>
																           �а���λ��
																           </td>
																           <td>
																           <input name="insureder" id="insureder" type="text">
																           </td>
																       </tr>
																      <br>
																      <tr>
																         <td colspan=2 align=center>
																         <input id="save" type="button" value="��  ��" onclick="saveDevice()">
																         </td>
																      </tr>
																</table>
																</div>
																
																
															</div>
															<div>
															
															</div>
														</td>
															
													</tr>
		        								</table>
		        							</td>
		        						</tr>
		        						<tr>
		        							<td>
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
</html>
