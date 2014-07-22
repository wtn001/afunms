<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="com.afunms.config.model.TimingBackupTelnetConfig"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*" %>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	String cmds="sh class-map\r\nsh policy-map\r\nsh policy-map interface\r\nsh queue fastEthernet 1/0";
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="gb2312" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js"
			charset="gb2312"></script>

		<script language="JavaScript" type="text/javascript">

 Ext.onReady(function()
{
 Ext.get("process").on("click",function(){
     
    var chk1 = checkinput("ipaddress","string","�����豸",200,false);
   var commands=$("#commands").val();
   if(commands==""||commands==undefined){
   alert("�������Ϊ�գ�");
   return;
   }
    if(chk1)
    {
    	Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
    	mainForm.action = "<%=rootPath%>/telnetConfig.do?action=add";
    	mainForm.submit();
    }
 });
});

//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    


function showup(){
	var url="<%=rootPath%>/vpntelnetconf.do?action=multi_netip";
	window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
}

</script>

		<script type="text/javascript">
			function showTelnetNetList(){
				var url="<%=rootPath%>/telnetConfig.do?action=telnetCfg_netip&type=add&id=1";
				window.open(url,"portScanWindow","toolbar=no,width=900,height=600,directories=no,status=no,scrollbars=yes,menubar=no,resizable=yes");
			}
			
			function toAdd(){
				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=ready_add";
			    mainForm.submit();
			}
			
			function toDelete(){  
     				mainForm.action = "<%=rootPath%>/vpntelnetconf.do?action=deleteTimingBackupTelnetConfig";
     				mainForm.submit();
	  		}
		</script>

	</head>
	<body id="body" class="body">


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
											                	<td class="add-content-title">&nbsp;CWBFQ��� >> ���</td>
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
													 						<tr style="background-color: #ECECEC;">
																				
																				<TD nowrap align="right" height="24" width="20%">
																					�����豸&nbsp;
																				</TD>
																				<TD nowrap   >
																					&nbsp;
																					<input name="ipaddress" type="text" size="40" class="formStyle" id="ipaddress" readonly="readonly" >
																					<input type="button" value="ѡ�������豸" onclick="showTelnetNetList()" readonly><font color="red">&nbsp;* </font>
																				</TD>  
																			</tr>
																			
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="20%">
																					����&nbsp;
																				</TD>
																				<TD nowrap   >
																					&nbsp;
																					<select name="status">
																						<option value="1" label="��">��</option>
																						<option value="0" label="��">��</option>
																					</select>
																				</TD>
																			</tr>
																			<tr >
																				<TD nowrap align="right" height="24" width="20%">
																					����&nbsp;
																				</TD>
																				<TD nowrap>
																				
																					<input name="bkpType" type=hidden value='0'/>
																					&nbsp;<textarea id="commands" name="commands" rows="10" cols="85"  ><%=cmds %></textarea>
																					
																				</TD>
																			</tr>
																			
																			<tr>
																				<TD nowrap colspan="4" align=center > 
																				<br>
																				
																					<input type="button" id="process" style="width:50" value="ȷ ��">&nbsp;&nbsp;  
																					<input type="reset" style="width: 50" value="��  ��" onclick="javascript:history.back(1)">
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