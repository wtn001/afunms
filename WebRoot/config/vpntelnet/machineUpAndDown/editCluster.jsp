<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@page import="com.afunms.application.model.Cluster"%>
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.config.model.Business"%>
<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	Cluster cluster = (Cluster) request.getAttribute("cluster");
	String typeStr = cluster.getServerType();
	String mix = "";
	String windows = "";
	String aix = "";
	String unix = "";
	String solarix = "";
	String as400 = "";
	String linux = "";

	if (typeStr.equals("mix")) {
		mix = "selected";
	} else if (typeStr.equals("windows")) {
		windows = "";
	} else if (typeStr.equals("aix")) {
		aix = "";
	} else if (typeStr.equals("solarix")) {
		solarix = "";
	} else if (typeStr.equals("unix")) {
		unix = "";
	} else if (typeStr.equals("as400")) {
		as400 = "";
	} else if (typeStr.equals("linux")) {
		linux = "";
	}
	
	BusinessDao bussdao = new BusinessDao();
    List allbuss = bussdao.loadAll();
    String bid = cluster.getBid();
   	if(bid == null)bid="";
   	String id[] = bid.split(",");
   	List bidlist = new ArrayList();
   	if(id != null &&id.length>0){
		for(int i=0;i<id.length;i++){
	    	bidlist.add(id[i]);
		}
  	}   
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		
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


		<script language="JavaScript" type="text/javascript">

 Ext.onReady(function()
{  

 Ext.get("saveCluster").on("click",function(){
       
          	Ext.MessageBox.wait('数据加载中，请稍后.. ');
        	mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=saveCluster";
        	mainForm.submit();
   
 });	
});
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
function CreateWindow(url){
	msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
} 
</script>



	</head>
	<body id="body" class="body" onload="initmenu();">



		<form id="mainForm" method="post" name="mainForm">

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
																	<td align="left" width="5">
																		<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																			width="5" height="29" />
																	</td>
																	<td class="add-content-title">
																		服务器组 编辑
																	</td>
																	<td align="right">
																		<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																			width="5" height="29" />
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-body"
																class="detail-content-body">
																<tr>
																	<td>

																		<table border="0" id="table1" cellpadding="0"
																			cellspacing="1" width="100%">

																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					名 称&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<input type="text" id="name" name="name"
																						maxlength="50" size="28" class="formStyle"
																						value="<%=cluster.getName()%>">
																						<input type="hidden" id="id" name="id" value="<%=cluster.getId()%>">

																				</TD>
																				<TD nowrap align="right" height="24" width="10%">
																					&nbsp;服务器组操作系统
																				</TD>
																				<TD nowrap width="40%" colspan=3>
																					&nbsp;
																					<select name="serverType" class="formStyle">
																						<option value='mix' <%=mix %>>
																							混合
																						</option>
																						<option value='windows' <%=windows %>>
																							Windows
																						</option>
																						<option value='aix' <%=aix %>>
																							AIX
																						</option>
																						<option value='unix' <%=unix %>>
																							HP UNIX
																						</option>
																						<option value='solarix' <%=solarix %>>
																							SUN Solaris
																						</option>
																						<option value='linux' <%=linux %>>
																							Linux
																						</option>
																						<option value='as400' <%=as400 %>>
																							AS400
																						</option>
																					</select>
																				</TD>
																			</tr>
                                                                            <tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					所属业务&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																						<% 
																							String bussName = "";
																							if( allbuss.size()>0){
																								for(int i=0;i<allbuss.size();i++){
																									Business buss = (Business)allbuss.get(i);
																									
																									if(bidlist.contains(buss.getId()+"")){
																										bussName = bussName + ',' + buss.getName();
																									}
																				 				}
																							}
																						%>   
																						<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="<%=bussName%>">&nbsp;&nbsp;<input type=button value="设置所属业务" onclick='setBid("bidtext" , "bid");'>
																						<input type="hidden" id="bid" name="bid" value="<%=cluster.getBid()%>">
																				</TD>
																				<TD nowrap align="right" height="24" width="10%">
																					&nbsp;
																				</TD>
																				<TD nowrap width="40%" colspan=3>
																					&nbsp;
																				</TD>
																			</tr>


																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="保 存" style="width: 50"
																						id="saveCluster" onclick="#">
																					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																					<input type="reset" style="width: 50" value="返回"
																						onclick="javascript:history.back(1)">
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
															<table id="detail-content-footer"
																class="detail-content-footer">
																<tr>
																	<td>
																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
																			<tr>
																				<td align="left" valign="bottom">
																					<img
																						src="<%=rootPath%>/common/images/right_b_01.jpg"
																						width="5" height="12" />
																				</td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img
																						src="<%=rootPath%>/common/images/right_b_03.jpg"
																						width="5" height="12" />
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
										<tr>
											<td>
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