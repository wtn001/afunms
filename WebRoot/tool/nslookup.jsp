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
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<title>Nslookup</title>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/resource/image/bg4.jpg);
TEXT-ALIGN: center; 
}
-->
</style>
<!-- snow add end -->
  <script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
  <script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>

<script language="javascript" src="/afunms/js/tool.js"></script>
<script language="javascript">
function init(){
	document.getElementById("ipaddress").value='<%=ipaddress%>';
	<%
		if(ipaddress!=null&&!"".equals(ipaddress.trim())){
		%>
		ifExecute('nslookup','execute');
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
         		 closePage('nslookup',ip);
            }
            else
            {
              //  alert("ˢ�´���");
                 var ip=document.getElementById("ipaddress").value;
         		 closePage('nslookup',ip);
            }
        } 
</script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>
<body id="body" class="body" onload="init();">
	<table id="container-main" class="container-main">
		<tr>
			<td>
				<table id="container-main-win" class="container-main-win">
					<tr>
						<td>
							<table id="win-content" class="win-content">
								<tr>
									<td>
										<table id="win-content-header" class="win-content-header">
				                			<tr>
							                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
							                	<td class="win-content-title">&nbsp;��������</td>
							                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
											</tr>
									    
					       				</table>
				       				</td>
				       			</tr>
						       	<tr>
						       		<td>
						       			<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
														<td><b>����</b>��<input type="text" id="ipaddress" name="ipaddress" value="<%=ipaddress%>"></td>
														<td height="28" align="left">&nbsp;<input type="button" name="execute" style="height: auto" size="10" value="ִ��" id="execute" onClick="ifExecute('nslookup','execute')" /></td>
														</tr>  
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" >&nbsp;Nslookup[<%=ipaddress%>]</td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">
													<textarea id="resultofping" name="showList" rows="15" cols="76" readonly="readonly" ></textarea>                    			
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
			
            		<div align=center>
            			<input type=button value="�رմ���" onclick="window.close()">
            		</div>  
					<br>
</body>         		 
</html>