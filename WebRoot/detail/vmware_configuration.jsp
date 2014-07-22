<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.indicators.model.GatherIndicators"%>
<%@page import="com.afunms.polling.om.VMWareVid"%>
<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List list = (List)request.getAttribute("list");
  List list_vid = (List)request.getAttribute("list_vid");
  JspPage jp = (JspPage)request.getAttribute("page");
  String nodeid = request.getParameter("nodeid");
  String gatherfindselect=(String)session.getAttribute("gatherfindselect");
%>


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		
		<script language="JavaScript">

   Ext.onReady(function(){  
	 	Ext.get("process").on("click",function(){
	        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
	        mainForm.action = "<%=rootPath%>/vmware.do?action=updateFlag";
	        mainForm.submit();
	 	});	
	});

			function add(){
				mainForm.action = "<%=rootPath%>/vmware.do?action=updateFlag&nodeid=<%=nodeid%>";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=edit&id=" + node;
				mainForm.submit();
			}
			function toFind()
			  {
			     mainForm.action = "<%=rootPath%>/gatherIndicators.do?action=find";
			     mainForm.submit();
			  }
			
		</script>
		<script type="text/javascript">
		
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">�༭</td>
			</tr>
		</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
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
									                	<td class="content-title">&nbsp;VMWare ���������� </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
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
													    			<td  class="body-data-title">
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr> 
													<tr>
														<td>
															<table>
																<tr>
													    			<td class="body-data-title"><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()">���</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">����</td>
													    			<td class="body-data-title">������</td>
													    			<td class="body-data-title">�Ƿ����</td>
							        							</tr>
							        							<%
							        							    VMWareVid vo = null;
							        								if(list_vid != null && list_vid.size() > 0) {
							        									for(int i = 0; i < list_vid.size(); i++){
							        										vo= (VMWareVid)list_vid.get(i);
							        										//System.out.println("-----------------"+vo.getCategory());
							        										String name = "";
							        										if("vmware".equals(vo.getCategory())){
							        											name = "�����";
							        										}else if("physical".equals(vo.getCategory())){
							        											name = "�����";
							        										}else if("resourcepool".equals(vo.getCategory())){
							        											name = "��Դ��";
							        										}else if("yun".equals(vo.getCategory())){
							        											name = "��Ⱥ";
							        										}else if("datastore".equals(vo.getCategory())){
							        											name = "�洢";
							        										}
							        										String isCollection = "��";
							        										if("1".equals(vo.getFlag())){
							        											isCollection = "��";
							        										}
							        										%>
							        											<tr <%=onmouseoverstyle%>>
																	    			<td class="body-data-list" align=center><INPUT type="checkbox" id="checkbox" name="checkbox" value="<%=vo.getVid()%>"><%=i+1%></td>
																	    			<td class="body-data-list" align=center><%=vo.getGuestname()%></td>
																	    			<td class="body-data-list" align=center><%=vo.getVid()%></td>
																	    			<td class="body-data-list" align=center><%=vo.getCategory()%></td>
																	    			<td class="body-data-list" align=center><%=name%></td>
																	    			<td class="body-data-list" align=center><%=isCollection%></td>
											        							</tr>
							        										<%
							        									}
							        								}%>
							        								
							        								     <tr>
																			<TD nowrap colspan="6" align=center>
																			<br><input type="button" value="�� ��" style="width:50" id="process" onclick="add()">&nbsp;&nbsp;
																				<input type="button" style="width:50" value="�ر�" onclick="Window.close();">
																			</TD>	
																		</tr>	
							        								
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
