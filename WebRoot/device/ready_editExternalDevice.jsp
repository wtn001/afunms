<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.topology.model.RemotePingHost"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.topology.model.IpDistrictDetail"%>
<%@page import="com.afunms.config.model.DistrictConfig"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.device.util.DeviceUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.device.model.ExternalDevice"%>
<%@ include file="/include/globe.inc"%>

<%
  String rootPath = request.getContextPath();
//String pid=request.getParameter("pid");
String id=request.getParameter("id");
DeviceUtil util=new DeviceUtil();
ExternalDevice device=util.getDeviceById(id);
String osType=device.getOsType();
String brand=device.getBrand();
String  deviceId=device.getDeviceId();
String  deviceType=device.getDeviceType();
String insureder=device.getInsureder();
String ipaddress=device.getIpaddress();
String specification=device.getSpecification();
String supplier=device.getSupplier();
int cabinetId=device.getCabinetId();
String osType1="";
String osType2="";
if(osType!=null){
 if(osType.equals("h3c")){
	 osType1="selected";
 }else if(osType.equals("huawei")){
	 osType2="selected";
 }
}
String brand1="";
if(brand!=null){
	 if(brand.equals("h3c")){
		 brand1="selected";
	 }
	}
String deviceType1="";
String deviceType2="";
String deviceType3="";
String deviceType4="";
String deviceType5="";
if(deviceType!=null){
	 if(deviceType.equals("补光灯")){
		 deviceType1="selected";
	 }else if(deviceType.equals("电子警察")){
		 deviceType2="selected";
	 }else if(deviceType.equals("闪光灯")){
		 deviceType3="selected";
	 }else if(deviceType.equals("控制终端")){
		 deviceType4="selected";
	 }else if(deviceType.equals("车检器")){
		 deviceType5="selected";
	 }
	}
String specification1="";
if(specification!=null){
	 if(specification.equals("SISO1885")){
		 specification1="selected";
	 }
	}

String supplier1="";
if(supplier!=null){
	 if(supplier.equals("h3c")){
		 supplier1="selected";
	 }
	}
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script>
		function updateDevice(){
		
		            mainForm.action = "<%=rootPath%>/device.do?action=updateExternalDevice";
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
																           设备ID：
																           </td>
																           <td>
																           <input name="deviceId" id="deviceId" type="text" value="<%=deviceId%>">
																           </td>
																       </tr>
																       <tr>
																           <td>
																           设备IP：
																           </td>
																           <td>
																           <input name="ipaddress" id="ipaddress" type="text"  value="<%=ipaddress %>">
																           </td>
																       </tr>
																        <tr>
																           <td>
																           系统类型：
																           </td>
																           <td>
																           <select size=1 name='osType' style='width:100px;' id='osType'>
											            						<option value='h3c' <%=osType1 %>>h3c</option>
											            						<option value='huawei' <%=osType2 %>>huawei</option>
											            						
											            					</select>
																         
																           </td>
																       </tr>
																        <tr>
																           <td>
																           品牌：
																           </td>
																           <td>
																           <select size=1 name='brand' style='width:100px;' id='brand'>
											            						<option value='h3c' <%=brand1 %>>h3c</option>
											            						
											            					</select>
																         
																           </td>
																       </tr>
																       <tr>
																           <td>
																           设备类型：
																           </td>
																           <td>
																            <select size=1 name='deviceType' style='width:100px;' id='deviceType'>
											            						<option value='补光灯' <%=deviceType1 %>>补光灯</option>
											            						<option value='电子警察' <%=deviceType2 %>>电子警察</option>
											            						<option value='闪光灯' <%=deviceType3 %>>闪光灯</option>
											            						<option value='控制终端' <%=deviceType4 %>>控制终端</option>
											            						<option value='车检器' <%=deviceType5 %>>车检器</option>
											            						
											            					</select>
																         
																           </td>
																       </tr>
																       <tr>
																           <td>
																           规格型号：
																           </td>
																           <td>
																            <select size=1 name='specification' style='width:100px;' id='specification'>
											            						<option value='SISO1885' <%=specification1 %>>SISO1885</option>
											            						
											            					</select>
																           </td>
																       </tr>
																       <tr>
																           <td>
																           供应商：
																           </td>
																           <td>
																            <select size=1 name='supplier' style='width:100px;' id='supplier'>
											            						<option value='1' <%=supplier1 %>>h3c</option>
											            						
											            					</select>
																           </td>
																       </tr>
																       <tr>
																           <td>
																           承包单位：
																           </td>
																           <td>
																           <input name="insureder" id="insureder" type="text" value="<%=insureder %>">
																           </td>
																       </tr>
																      <br>
																      <tr>
																         <td colspan=2 align=center>
																         <input id="save" type="button" value="修 改" onclick="updateDevice()">
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
