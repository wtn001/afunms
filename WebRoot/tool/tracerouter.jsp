 <%@ page language="java" contentType="text/html; charset=gb2312" %>
 <%@ page  import="java.io.*"%>
<head>
<%
  String rootPath = request.getContextPath();
%>
<title>Tracetouter</title>
<%
	String getIp=(String)request.getParameter("ipaddress") ;
	if(getIp==null) getIp="";
%>
  <script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
  <script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
		<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<style type="text/css">
.bg_image{
	background-image:url("<%=rootPath%>/resource/image/bg.jpg");
	width:100%;
	height:100%;
}
.layout_title1 {
	font-family:����, Arial, Helvetica, sans-serif;
	font-size:12px;
	font-weight:bold;
	color:#000000;
	text-align: center;
}
</style>
<script language="javascript">
	function init(){
	
		document.getElementById("ipaddress").value='<%=getIp.trim() %>';
		<%
			if(getIp!=null&&!"".equals(getIp.trim())){
			%>
			ifExecute('traceroute','execute');
			<%
			}
		%>
		//alert(document.getElementById("ipaddress").value);
	}

	window.onbeforeunload  =  function()   
       {   
            if((event.clientX>document.body.clientWidth&&event.clientY<0)||event.altKey)   
            {          
                // alert("�رմ���");
                 var ip=document.getElementById("ipaddress").value;
         		 closePage('traceroute',ip);
            }
            else
            {
                //alert("ˢ�´���");
                 var ip=document.getElementById("ipaddress").value;
         		 closePage('traceroute',ip);
            }
        }
 
</script>
</head>
<body id="body" class="body">
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
																	<td class="add-content-title">��������
																	</td>
																	<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td bgcolor="#FFFFFF">
															<table id="win-content-body" class="win-content-body" >
				       					<tr>
				       						<td>
												<table>
													<tr>
														<td><b>IP</b>��<input type="text" id="ipaddress" name="ipaddress" value="<%=getIp%>"></td>
														<td height="28" align=right>&nbsp;&nbsp;���������</td>		
														<td height="28">
															<select id="maxjumpnumber" NAME="maxhop">
																<%
																for(int i=1;i<31;i++){
																	if(i==10){
																%>
																<option value=<%=i%> selected><%=i%></option>
																<%
																}else{	  						
																%>
																<option value=<%=i%>><%=i%></option>
																<%
																	}
																}
																%>
														<td align="center"><input type="button"  id="execute" name="traceroute" value="ִ��" onClick="ifExecute('traceroute','execute')"></td>
													</tr>        
												</table>
							       			</td>
							       		</tr>
							       		<tr>
										   <td class="win-data-title" style="height: 29px;" >&nbsp;·�ɸ���[<%=getIp%>]</td>
										</tr>
										<tr align="left" valign="center"> 
                							<td height="28" align="left">
												<textarea id="resultofping" name="display" rows="15" cols="76" readonly="readonly"></textarea>                    			
                							</td>
										</tr>
										<tr>
											<td align="center">
											<input type=button value="�رմ���" onclick="window.close()">
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
</html>