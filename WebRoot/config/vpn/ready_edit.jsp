<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="com.afunms.config.model.TimingBackupTelnetConfig"%>
<%@page import="com.afunms.polling.node.IfEntity"%>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.config.model.Vpn"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*" %>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	Vpn vpn=(Vpn)request.getAttribute("vpn");
	int id=0;
	int sourceId=0;
	int desId=0;
	String sourceIndex="";
	String desIndex="";
	if(vpn!=null){
		 id=vpn.getId();
	     sourceId=vpn.getSourceId();
		 desId=vpn.getDesId();
		 sourceIndex=vpn.getSourcePortIndex();
		 desIndex=vpn.getDesPortIndex();
	}
	 
	 
	Host sourceHost=(Host) PollingEngine.getInstance().getNodeByID(sourceId);
	Host desHost=(Host) PollingEngine.getInstance().getNodeByID(desId);
	
	List<IfEntity> startHostIfentityList = (ArrayList<IfEntity>)request.getAttribute("startHostIfentityList");
	List<IfEntity> endHostIfentityList = (ArrayList<IfEntity>)request.getAttribute("endHostIfentityList");
    
    String sourceIp="";
    String desIp="";
    String sourceName="";
    String desName="";
    if(sourceHost!=null){
    	sourceIp=sourceHost.getIpAddress();
    	sourceName=sourceHost.getAlias();
    }
    if(desHost!=null){
    	desIp=desHost.getIpAddress();
    	desName=desHost.getAlias();
    }
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>

		<script language="JavaScript" type="text/javascript">
 
 Ext.onReady(function()
{
 Ext.get("process").on("click",function(){
   
    	Ext.MessageBox.wait('数据加载中，请稍后.. ');
    	mainForm.action = "<%=rootPath%>/vpn.do?action=edit";
    	mainForm.submit();
 });
});


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
				        										<input type="hidden" name="sourceId" id='sourceId' value="<%=sourceId %>"/>
                                                                 <input type="hidden" name="desId" id='desId' value="<%=desId %>"/>
                                                                 <input type="hidden" name="id" id='id' value="<%=id %>"/>
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
													 						<tr>
																				<TD nowrap align="right" height="24" width="20%">
																					源网络设备名称：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;<%=sourceName %>
																				</TD>  
																			</tr>
													 						<tr>
																				<TD nowrap align="right" height="24" width="20%">
																					源网络设备IP：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;<%=sourceIp %>
																				</TD>  
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="20%">
																					源网络设备端口：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;
																					<select size=1 name='start_index' style='width:250px;'>
							                                                    <%
							                                                    String sourceSelect="";
							                                                    for(IfEntity ifObj:startHostIfentityList){
							                                                    	if(ifObj.getIndex().equals(sourceIndex)){
							                                                    		sourceSelect="selected";
							                                                    		}
							                                                     %> 			
							                                                         <option value='<%=ifObj.getIndex()%>' title="<%=ifObj.getIndex()%>(<%=ifObj.getDescr()%>)" <%=sourceSelect %>><%=ifObj.getIndex()%>(<%=ifObj.getDescr()%>)</option>
							                                                   <%
							                                                   sourceSelect="";
							                                                    }%></select>	
																				</TD>  
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="20%">
																					目的网络设备名称：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;<%=desName %>
																				</TD>  
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="20%">
																					目标网络设备IP：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;<%=desIp %>
																				</TD>  
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="20%">
																					目标网络设备端口：&nbsp;
																				</TD>
																				<TD nowrap>
																					&nbsp;
																					<select size=1 name='end_index' style='width:250px;' >
																				<%
																				 String desSelect="";
																				  for(IfEntity ifObj:endHostIfentityList){
																					  if(ifObj.getIndex().equals(desIndex))desSelect="selected";
																				%> 			
																						<option value='<%=ifObj.getIndex()%>' title="<%=ifObj.getIndex()%>(<%=ifObj.getDescr()%>)" <%=desSelect %>><%=ifObj.getIndex()%>(<%=ifObj.getDescr()%>)</option>
																				<%
																				 desSelect="";
																				  }%></select>	
																				</TD>  
																			</tr>
																			
																																				
																			<tr >
																				<TD nowrap colspan="4" align=center > 
																				<br>
																				
																					<input type="button" id="process" style="width:50" value="确 定">&nbsp;&nbsp;  
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