<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.polling.node.SshEnclosure"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>

<%
	String runmodel = PollingEngine.getCollectwebflag();//�ɼ������ģʽ
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");

	Map map = (Map)request.getAttribute("enclosures");
	 if(map == null)
	 {
		 map = new HashMap();
	 }
int size=map.size();
	Host host = (Host) request.getAttribute("hpstorage");
	if (host == null) {
		host = new Host();
	}
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
			
		<script language="javascript">	
		$(document).ready(function(){
		<%for(int j=1;j<=size;j++){%>
$('#div_<%=j%>').hide();
    $('#tr_<%=j%>').bind('click',function(){
			$('#div_<%=j%>').slideToggle('slow');
			
		});
		<%}%>
		})
		
function setClass(){
	document.getElementById('hpStorageDetailTitle-2').className='detail-data-title';
	document.getElementById('hpStorageDetailTitle-2').onmouseover="this.className='detail-data-title'";
	document.getElementById('hpStorageDetailTitle-2').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		document.getElementById("id").value="<%=host.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}


</script>
	</head>
	<body id="body" class="body" onload="setClass();">
		<form id="mainForm" method="post" name="mainForm">
			<input type=hidden name="orderflag">
			<input type=hidden name="id">
			<table id="body-container" class="body-container">
				<tr>
					

					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-detail" width=98%>
									<table id="container-main-detail" class="container-main-detail">
										<tr>
											<td>
												 <jsp:include page="/topology/includejsp/sysinfo_hpstorage_top.jsp">
												            <jsp:param name="rootPath" value="<%= rootPath %>"/>
												            <jsp:param name="tmp" value="<%=host.getId()%>"/>
										                 </jsp:include>
											</td>
										</tr>
										<tr>
											<td>
												<table id="detail-data" class="detail-data">
													<tr>
														<td class="detail-data-header">
															<%=hpStorageSshDetailTitleTable%>
														</td>
													</tr>
													<tr>

														<td>
															<table class="detail-data-body">
																<tr>
																	<td>
																	
																	<%  if(map != null && map.size()>0)
													                     {
													                    	 List list=null;
													                    	 int j=0;
													                    	 Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
													                    	  while (it.hasNext()) {
													                    	   Map.Entry entry = it.next();
													                    	  List enclosurelist=(List)entry.getValue();
													                    	  ++j;
													                    	  String[] tmp = entry.getKey().toString().trim().split("\\|");
													                    	  for(int k = 0; k < tmp.length; k++){
													                    	  	System.out.println(tmp[k]);
													                    	  }
													                    	   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
													                    	  %>
													                    	  <table width="100%" border="0" cellpadding="0" cellspacing="0">
																			<tr id="tr_<%=j %>">
																				<td colspan=2>
																		<table id="add-content-header" class="add-content-header" >
																			<tr id="cpuliyonglv">
																				<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
																				<td class="add-content-title">
																					<table>
																						<tr style="cursor:hand">
																							<td><%=tmp[0] %></td>
																							<td><%=tmp[1] %></td>
																							<td><%=tmp[2] %></td>
																						</tr>
																					</table>
																				</td>
																				<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
																			</tr>
																		</table>
																		
																	
																				
																				</td>
																			</tr>
																			</table>
													                    	  <div id="div_<%=j %>">
																		<table width="100%" border="0" cellpadding="0" cellspacing="0">
																		
																			<tr>
																				<td>
																				
																					<table>
																						<tr bgcolor="#ECECEC">
																							<td width="10%" height="26" align="center">
																								����
																							</td>
																							<td width="10%" height="26" align="center">
																								���
																							</td>
																							<td width="10%" height="26" align="center">
																								״̬
																							</td>
																							
																							<td width="15%" height="26" align="center">
																								FRU P/N
																							</td>
																							<td width="10%" height="26" align="center">
																								FRU S/N
																							</td>
																							<td width="10%" height="26" align="center">
																								Add'l Data
																							</td>
																							
																						</tr>
																						<%
																							if (enclosurelist != null && enclosurelist.size() > 0) {
																								for (int i = 0; i < enclosurelist.size(); i++) {
																									SshEnclosure enclosure = (SshEnclosure)enclosurelist.get(i);
																						%>

																						<tr>
																							<td class="detail-data-body-list" height="26" align="center"><%=SysUtil.ifNull(enclosure.getType())%></td>
																							<td class="detail-data-body-list" height="26" align="center"><%=SysUtil.ifNull(enclosure.getNumber())%></td>
																							<td class="detail-data-body-list" height="26" align="center"><%=SysUtil.ifNull(enclosure.getStatus())%></td>
																							<td class="detail-data-body-list" height="26" align="center"><%=SysUtil.ifNull(enclosure.getFruPN())%></td>
																							<td class="detail-data-body-list" height="26" align="center"><%=SysUtil.ifNull(enclosure.getFruSN())%></td>
																							<td class="detail-data-body-list" height="26" align="center"><%=SysUtil.ifNull(enclosure.getAddData())%></td>
																							
																						</tr>
                                                                                        <%
																								} 
																							}
																					
																						%>
																						
																					</table>
																				</td>
																			</tr>
																			            
																		</table>
																		</div>
																		 <%
																								} 
																							}
																						
																						%>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
									</table>
						</table>

					</td>
					<td class="td-container-main-tool" width="15%">
						<jsp:include page="/application/hpstorage/hpstoragetoolbar.jsp">
							<jsp:param value="<%=host.getIpAddress()%>" name="ipaddress" />
							<jsp:param value="<%=host.getSysOid()%>" name="sys_oid" />
							<jsp:param value="<%=host.getId()%>" name="tmp" />
							<jsp:param value="storage" name="category" />
							<jsp:param value="hp" name="subtype" />
						</jsp:include>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>