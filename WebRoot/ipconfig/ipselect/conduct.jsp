<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%@page import="java.util.List"%>
<%
  List station = (List)request.getAttribute("station");
  
  List loopback = (List)request.getAttribute("loopback");
  List pe = (List)request.getAttribute("pe");
  List pe_ce = (List)request.getAttribute("pe_ce");
  List bussiness = (List)request.getAttribute("bussiness");
  ip_select vo = (ip_select)request.getAttribute("ip_select_vo");
  String backbone_id = (String)request.getAttribute("backbone_id");
  String id = (String)request.getAttribute("id");
  
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->




<script language="JavaScript" type="text/javascript">


  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
   
        	mainForm.action = "<%=rootPath%>/ipfield.do?action=save&backbone_id=<%=backbone_id%>&id=<%=id%>";
        	mainForm.submit();
        
     
       // mainForm.submit();
 });	
});
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


</script>

<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//修改菜单的上下箭头符号
function my_on(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="on";
}
function my_off(head,body)
{
	var tag_a;
	for(var i=0;i<head.childNodes.length;i++)
	{
		if (head.childNodes[i].nodeName=="A")
		{
			tag_a=head.childNodes[i];
			break;
		}
	}
	tag_a.className="off";
}
//添加菜单	
function initmenu()
{
	var idpattern=new RegExp("^menu");
	var menupattern=new RegExp("child$");
	var tds = document.getElementsByTagName("div");
	for(var i=0,j=tds.length;i<j;i++){
		var td = tds[i];
		if(idpattern.test(td.id)&&!menupattern.test(td.id)){					
			menu =new Menu(td.id,td.id+"child",'dtu','100',show,my_on,my_off);
			menu.init();		
		}
	}
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
}


function toAdd()
  {
     mainForm.action = "<%=rootPath%>/ipfield.do?action=save&backbone_id=<%=backbone_id%>";
     mainForm.submit();
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
											                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
											                	<td class="add-content-title">IP管理 >> IP处理 >> IP分配</td>
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
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1"
																	width="100%">
																
																	<tr>
						    <TD nowrap align="right" height="24" width="10%">场站名称&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<input type="text" name="name" size="40" value="<%=vo.getName() %>" class="formStyle" readOnly></TD>
							<TD nowrap align="right" height="24">运营状态&nbsp;</TD>				
							<TD nowrap>
								 <select id="type" size=1 name='station' style='width:125px;' >
													 <option value=''>--请选择--</option>
																				<%
																				    stationtype vo1 = null;
																					if(station != null && station.size()>0){
																						for(int k=0;k<station.size();k++){
																							vo1 = (stationtype)station.get(k);
																				%>
																			<option value=<%=vo1.getId()%>><%=vo1.getName()%></option>
																			<%
																						}
																					}
																			%>
					              </select>
								<font color="red">&nbsp;*</font>
							</TD>	
						</tr>
						<tr style="background-color: #ECECEC;">	
							<TD nowrap align="right" height="24">loopback地址&nbsp;</TD>				
							<TD nowrap>
								 &nbsp;<select id="type" size=1 name='loopback' style='width:125px;' >
								             <option value=''>--请选择--</option>
																				<%
																				    loopbackstorage lp_vo = null;
																					if(loopback != null && loopback.size()>0){
																						for(int k=0;k<loopback.size();k++){
																							lp_vo = (loopbackstorage)loopback.get(k);
																				%>
																			<option value=<%=lp_vo.getId()%>><%=lp_vo.getLoopback()%></option>
																			<%
																						}
																					}
																			%>
								</select>
							<font color="red">&nbsp;*</font>
							<TD nowrap align="right" height="24">PE-PE互联地址&nbsp;</TD>				
							<TD nowrap>
								 <select id="type" size=1 name='pe' style='width:150px;' >
								             <option value=''>--请选择--</option>
																				<%
																				    pestorage p_vo = null;
																					if(pe != null && pe.size()>0){
																						for(int k=0;k<pe.size();k++){
																							p_vo = (pestorage)pe.get(k);
																				%>
																			<option value=<%=p_vo.getId()%>><%=p_vo.getBackbone1()%>--<%=p_vo.getBackbone2().split("\\.")[3] %>/30</option>
																			<%
																						}
																					}
																			%>
								</select>
								<font color="red">&nbsp;*</font>
							</tr>		
							<tr style="background-color: #ECECEC;">	
							<TD nowrap align="right" height="24">PE_CE互联地址&nbsp;</TD>				
							<TD nowrap>
								 &nbsp;<select id="type" size=1 name='pe_ce' style='width:150px;' >
								             <option value=''>--请选择--</option>
																				<%
																				    pe_cestorage pc_vo = null;
																					if(pe_ce != null && pe_ce.size()>0){
																						for(int k=0;k<pe_ce.size();k++){
																							pc_vo = (pe_cestorage)pe_ce.get(k);
																				%>
																			<option value=<%=pc_vo.getId()%>><%=pc_vo.getS1()%>--<%=pc_vo.getS2().split("\\.")[3] %>/30</option>
																			<%
																						}
																					}
																			%>
								</select>
							<font color="red">&nbsp;*</font>
							<TD nowrap align="right" height="24">业务地址&nbsp;</TD>				
							<TD nowrap>
								 <select id="type" size=1 name='bussiness' style='width:150px;' >
								             <option value=''>--请选择--</option>
																				<%
																				    ip_bussinesstype bus = null;
																					if(bussiness != null && bussiness.size()>0){
																						for(int k=0;k<bussiness.size();k++){
																							bus = (ip_bussinesstype)bussiness.get(k);
																				%>
																			<option value=<%=bus.getFlag()%>><%=bus.getFlag() %>.0--254</option>
																			<%
																						}
																					}
																			%>
								</select>
								<font color="red">&nbsp;*</font>
							</tr>		
									            							
									            							
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="保存" style="width:100" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:100" value="返回" onclick="javascript:history.back(1)">
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