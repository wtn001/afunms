<%@ page language="java" contentType="text/html;charset=gb2312"%>

<html>
<head>
<%
  String rootPath = request.getContextPath();
  String ipaddress= request.getParameter("ipaddress");
  
  if(ipaddress==null){
  	ipaddress = "";
  }
  
  
%>
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>

<title>Ping</title>
<style type="text/css">
.bg_image{
	background-image:url("<%=rootPath%>/resource/image/bg.jpg");
}
.layout_title1 {
	font-family:����, Arial, Helvetica, sans-serif;
	font-size:12px;
	font-weight:bold;
	color:#000000;
	text-align: center;
}
</style>
  <script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
  <script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<script language="javascript" src="/afunms/js/tool.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
		<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>
<script language="javascript">
function init(){
	document.getElementById("ipaddress").value='<%=ipaddress%>';
	<%
		if(ipaddress!=null&&!"".equals(ipaddress.trim())){
		%>
		ifExecute('ping','execute');
		<%
		}
	%>
	
}
window.onbeforeunload  =  function()   
       {   
       //alert("----------------");
            if((event.clientX>document.body.clientWidth&&event.clientY<0)||event.altKey)   
            {          
           //      alert("�رմ���");
                 var ip=document.getElementById("ipaddress").value;
         		 closePage('ping',ip);
            }
            else
            {
              //  alert("ˢ�´���");
                 var ip=document.getElementById("ipaddress").value;
         		 closePage('ping',ip);
            }
        } 
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>        		
<body id="body" class="body" onload="init();">
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
															<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table class="win-content-title">
														<tr align="left" valign="center"> 
														<td><b>IP</b>��<input type="text" id="ipaddress" name="ipaddress" value="<%=ipaddress%>"></td>
														<td height="28" align="left"><b>&nbsp;����С(Btye)</b></td>
														<td height="28" align="left">&nbsp;
															<select id="packagelength" name=packagelength>
																<option value="0">0</option>
																<option value="1">1</option>
																<option value="2">2</option>
																<option value="4">4</option>
																<option value="8">8</option>
																<option value="16">16</option>
																<option value="32" selected>32</option>
																<option value="64">64</option>
																<option value="128">128</option>
																<option value="256">256</option>
																<option value="1024">1024</option>
																<option value="2048">2048</option>
															</select>
														</td>
														<td height="28" align="left"><b>&nbsp;ִ�д���</b></td>
														<td height="28" align="left">&nbsp;
															<select id="executenumber" name=executenumber>
															<%
																for(int i=1;i<101;i++){
																	if(i==5){
															%>
																		<option value="<%=i%>" selected><%=i%></option>
															<%		} else{
															%>
																<option value="<%=i%>"><%=i%></option>
															<%
																	}
																}
															%>
															</select>                    			
														</td>
														<td height="28" align="left">&nbsp;<input type="button" name="execute" style="height: auto" size="10" value="ִ��" id="execute" onClick="ifExecute('ping','execute')" /></td>
														</tr>  
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" >&nbsp;Ping[<%=ipaddress%>]</td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">
													<textarea id="resultofping" name="showList" rows="15" cols="76" readonly="readonly" ></textarea>                    			
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