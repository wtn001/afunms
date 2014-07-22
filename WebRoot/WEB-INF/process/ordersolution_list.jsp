<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="java.util.List"%>
<%@page import="com.bpm.system.utils.StringUtil"%>
<%
	String rootPath = request.getContextPath();
	List<Knowledgebase> list =( List<Knowledgebase>) request.getAttribute("list");
	String category=(String) request.getAttribute("category");
	String entity=(String) request.getAttribute("entity");
	String wordkey=(String) request.getAttribute("wordkey");
	if(StringUtil.isBlank(wordkey)) wordkey="";
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<base target="_self"><base>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
		<script language="JavaScript" type="text/javascript">
			function selectSolution(content)
			{
				document.getElementById('exID').value = content;
			}
			function sureSolution()
			{
      		 	var open =  parent.opener.document.getElementById("ordersolution");
      		 	var vv=document.getElementById('exID').value;
       			open.value=vv;
      		 	window.close();
			}
		</script>
	</head>
	<body id="body" class="body">
	<input type="hidden" id="exID" size="20"/>
		<form action="../controller/orderSolutionList.action" id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="category" value="<%=category %>">
			<input type="hidden" name="entity" value="<%=entity %>">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 历史解决方案 </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
										</tr>
										
										<tr>
		        						  <td colspan="2">
		        						  <table>
		        						      <tr>
					        						<td>
														<table>
															<tr>
															    <td class="detail-data-body-title" style="text-align: left;">
															    	类别：<%=category %>
															    	&nbsp;&nbsp;
															    	类型：<%=entity %>
																	&nbsp;&nbsp;
																	关键字：<input type="text" name="wordkey" value="<%=wordkey %>">
																	&nbsp;&nbsp;
																	<input type="submit" name="submitss" value="查询">
																	<input type="button" value="确定"  onclick="sureSolution();">
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
												<table id="content-body" class="content-body">
													<tr>
														<td>
															<table>
																<tr>
																	<tr>
																	<td align="center" class="body-data-title">
																		序号
																	</td>
																	<td align="center" class="body-data-title">
																		类别
																	</td>
																	<td align="center" class="body-data-title">
																		类型
																	</td>
																	<td align="center" class="body-data-title">
																		指标
																	</td>
																	<td align="center" class="body-data-title">
																		标题
																	</td>
																	<td align="center" class="body-data-title">
																		内容
																	</td>
																	<td align="center" class="body-data-title">
																		上次修改时间
																	</td>
																	<td align="center" class="body-data-title">
																		添加用户ID
																	</td>
																	
																</tr>
																<%
																	for(Knowledgebase vo:list)
																    {
																%>
																<tr>
																	<td align="center" class="body-data-list">
																		<input name="solution" type="radio" value="<%=vo.getId()%>"  onclick='selectSolution("<%=vo.getContents()%>");' >
																	</td>
																	<td align="center" class="body-data-list"><%=vo.getCategory()%></td>
																	<td align="center" class="body-data-list"><%=vo.getEntity()%></td>
																	<td align="center" class="body-data-list"><%=vo.getSubentity()%></td>
																	<td align="center" class="body-data-list"><%=vo.getTitles()%></td>
																	<td align="center" class="body-data-list"><%=vo.getContents()%></td>
																	<td align="center" class="body-data-list"><%=vo.getKtime()%></td>
																	<td align="center" class="body-data-list"><%=vo.getUserid()%></td>
																	
																</tr>
																<%
																	}
																%>
															</table>
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
	</BODY>
</HTML>
