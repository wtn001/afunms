<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.Hashtable"%>


<%
	String rootPath = request.getContextPath();
	//String menuTable = (String)request.getAttribute("menuTable");
	
	List list = (List)request.getAttribute("list");
	Hashtable nodeDependListHashtable = (Hashtable)request.getAttribute("nodeDependListHashtable");
		
	String flag = (String)request.getAttribute("flag");
	
	String bid = (String)request.getAttribute("bid");
	
	List allBidList = null;
	BusinessDao dao = new BusinessDao();
	try {
		allBidList = dao.loadAll();
	} catch (Exception e) {
	
	} finally {
		dao.close();
	}
	if(allBidList == null){
		allBidList = new ArrayList();
	}
  
%>
	
	


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		
		<script language="JavaScript" type="text/javascript">
			function showViewNode(viewId){
				if(viewId || viewId == 0){
					mainForm.action = "<%=rootPath%>/businessview.do?action=showViewNode&bid=<%=bid%>&viewId="+viewId;
					mainForm.submit();
				} else {
					alert("��ѡ����ͼ��");
				}
			}
		</script>
		
	</head>
	<body id="body" class="body" leftmargin="0" topmargin="0">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" id="flag" name="flag" value="<%=flag%>">
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
									                	<td class="content-title"> &nbsp; ҵ����ͼ  </td>
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
		        													<td class="body-data-title">����</td> 
		        													<td class="body-data-title">����ҵ������</td> 
		        													<td class="body-data-title">��Դ��</td> 
		        												</tr>
		        												<%
		        													if(list != null) {
		        														for(int i = 0 ; i < list.size(); i++){
		        														ManageXml manageXml = (ManageXml)list.get(i);
		        														String manageXmlBid = manageXml.getBid();
		        														
		        														String bid_ch = "";
		        														if(bid!=null){
		        															String[] bids = manageXmlBid.split(",");
		        															if(bids != null){
		        																for(int j = 0 ; j < bids.length; j++){
		        																	for(int k = 0 ; k < allBidList.size(); k++){
		        																		Business business = (Business)allBidList.get(k);
		        																		if(bids[j].trim().equals(business.getId())){
		        																			bid_ch = bid_ch + business.getName() + ",";
		        																		}
		        																	}
		        																}
		        															}
		        														}
		        														List nodeDependList = (List)nodeDependListHashtable.get(manageXml);
		        														String size = "0";
		        														if(nodeDependList != null){
		        															size = nodeDependList.size() + "";
		        														}
	        															
		        												%>
				        												<tr>
				        													<td class="body-data-list"><a href="#" onclick="showViewNode('<%=manageXml.getId()%>')"><%=manageXml.getTopoName()%></td> 
				        													<td class="body-data-list"><%=bid_ch%></td> 
				        													<td class="body-data-list"><%=size%></td> 
				        												</tr>
		        												<%
		        														}
		        													
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
	</body>
</html>
