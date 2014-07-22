<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.Supper"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%
   List list = (List)request.getAttribute("list");
   String name = (String)request.getAttribute("name");
   List loopback = (List)request.getAttribute("loopback");
   List pe = (List)request.getAttribute("pe");
   List pe_ce = (List)request.getAttribute("pe_ce");
   String running = (String)request.getAttribute("running");
   String backbone_name =(String) request.getAttribute("backbone_name");
   String backbone_id = (String)request.getAttribute("backbone_id");
   String apply_id =(String)request.getAttribute("apply_id");
   
   Supper vo = (Supper)request.getAttribute("vo");
   String rootPath = request.getContextPath();
   
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
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
  
   
        	mainForm.action = "<%=rootPath%>/ipfield.do?action=update&num=<%=loopback.size()%>";
        	mainForm.submit();
        
     
       // mainForm.submit();
 });
 
 
 Ext.get("process1").on("click",function(){
  
   
        	mainForm.action = "<%=rootPath%>/ipfield.do?action=query&id=<%=apply_id%>";
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
	var value = document.getElementById("h").value;
	return CreateWindow('<%=rootPath%>/ipfield.do?action=setReceiver&event='+event.id+'&value='+event.value+'&val='+value);
}
//-- nielin modify at 2010-01-04 end ----------------

function setReceiver1(eventId){
	var event = document.getElementById(eventId);
	var value = document.getElementById("h1").value;
	return CreateWindow('<%=rootPath%>/ipfield.do?action=setReceiver1&event='+event.id+'&value='+event.value+'&val='+value);
}

function setReceiver2(eventId){
	var event = document.getElementById(eventId);
	var value = document.getElementById("h2").value;
	return CreateWindow('<%=rootPath%>/ipfield.do?action=setReceiver2&event='+event.id+'&value='+event.value+'&val='+value);
}

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

</script>


</head>
<body id="body" class="body" onload="initmenu();">

    <form name="mainForm" method="post">
       <input type=hidden name="id" value="">
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
											                	<td class="add-content-title">IP管理>> IP查询 >> 编辑</td>
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
						    <TD nowrap align="right" height="24" width="10%">位于的骨干点名:&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<input type="text" name="su_name" size="40" value="<%=backbone_name%>" class="formStyle" readonly><font color="red">&nbsp;*</font></TD>
							<TD nowrap align="right" height="24">厂站名:&nbsp;</TD>				
							<TD nowrap>
								&nbsp;<input name="su_area" type="text" size="40" value="<%=name%>" class="formStyle" readonly><font color="red">&nbsp;*</font>
							</TD>		
						</tr>
						<tr style="background-color: #ECECEC;">	
							<TD nowrap align="right" height="24">运营状态:&nbsp;</TD>				
							<TD nowrap>
								&nbsp;<input name="su_area" type="text" size="40" value="<%=running%>" class="formStyle" readonly><font color="red">&nbsp;*</font>
						</tr>
						<%
						
						         loopbackstorage loop = null;
						         if(loopback.size()!= 0 && loopback != null){
						           for(int j=0;j<loopback.size();j++){
						               loop = (loopbackstorage)loopback.get(j);
						 %>
						<tr>
												
							<TD nowrap align="right" height="24">loopback地址<%=j+1 %>:&nbsp;</TD>
							<td nowrap colspan="3" height="1">&nbsp;
				             <input type=text readonly="readonly" id="sendemail<%=j+1 %>" name="sendemail" size="50" maxlength="32" value="<%=loop.getLoopback()%>">&nbsp;&nbsp;<input type=button value="修改" onclick='setReceiver("sendemail<%=j+1 %>");'>
				             <input type="hidden"   name= "l" id = "h" value = "<%=loop.getLoopback_id() %>"/>
				             <input type="hidden"  name="loopback_h" id = "" value = "<%=loop.getLoopback() %>"/>
				            </td>
						</tr>
						<%
						  }
						}
						
						 %>
						 <%
						      pestorage p = null;
						         if(pe.size()!= 0 && pe != null){
						           for(int j=0;j<pe.size();j++){
						              p = (pestorage)pe.get(j);
						 
						  %>
						<tr>
							<TD nowrap align="right" height="24">PE互联地址<%=j+1 %>:&nbsp;</TD>
							<td nowrap colspan="3" height="1">&nbsp;
				             <input type=text readonly="readonly" id="sendemail1<%=j+1 %>" name="sendemail1" size="50" maxlength="32" value="<%=p.getPe()%>">&nbsp;&nbsp;<input type=button value="修改" onclick='setReceiver1("sendemail1<%=j+1 %>");'>
				             <input type="hidden"   name = "p" id = "h1" value = "<%=p.getPe_id()%>"/>
				             <input type="hidden"  name="pe_h" id = "" value = "<%=p.getPe() %>"/>
				            </td>
						</tr>
						<%
						 }
						 }
						 %>
						 <%
						  pe_cestorage pc = null;
						         if(pe_ce.size()!= 0 && pe_ce != null){
						           for(int j=0;j<pe_ce.size();j++){
						              pc = (pe_cestorage)pe_ce.get(j);
						 
						 
						  %>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">PE_CE互联地址<%=j+1 %>:&nbsp;</TD>				
						    <td nowrap colspan="3" height="1">&nbsp;
				             <input type=text readonly="readonly" id="sendemail2<%=j+1 %>" name="sendemail2" size="50" maxlength="32" value="<%=pc.getPe_ce()%>">&nbsp;&nbsp;<input type=button value="修改" onclick='setReceiver2("sendemail2<%=j+1 %>");'>
				             <input type="hidden"  name="pc" id = "h2" value = "<%=pc.getPe_ce_id()%>"/>
				             <input type="hidden"  name="pe_ce_h" id = "" value = "<%=pc.getPe_ce() %>"/>
				            </td>
						</tr>
						<%
						}
						}
						 %>
						
						<%  if(list.size() == 0 ){%>
						        
						        
						        <tr>
										<TD nowrap colspan="4" align=center>
													<br><font color="red" >无该骨干点下可自动分配的业务，请添加业务后再执行操作&nbsp;</font>
										</TD>	
								</tr>	
					
						
					<%	} %>
						<% 
						          ip_bussinesstype  bussiness = null;
						          if(list.size()>0 && list != null){
						              for(int i = 0;i<list.size();i++){
						                bussiness = (ip_bussinesstype)list.get(i);
						              
						%>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24">业务类型&nbsp;</TD>				
							<TD nowrap  colspan="3">&nbsp;<input name="su_url" type="text" size="40" class="formStyle" value="<%=bussiness.getBussiness()%>" readonly></TD>
						</tr>	
						<tr>
							<td align="right" height="24">业务名称&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<input name="su_url" type="text" size="40" class="formStyle" value="<%=bussiness.getName() %>" readonly></td>
						</tr>
						<tr>
							<td align="right" height="24">网段&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<input name="su_url" type="text" size="40" class="formStyle" value="<%=bussiness.getSegment() %>" readonly></td>
						</tr>
						<tr>
							<td align="right" height="24">网关&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<input name="su_url" type="text" size="40" class="formStyle" value="<%=bussiness.getGateway()%>" readonly></td>
						</tr>		   
						<tr>
							<td align="right" height="24">纵向加密&nbsp;</td>
							<td colspan="3" align="left">&nbsp;<input name="su_url" type="text" size="40" class="formStyle" value="<%=bussiness.getEncryption() %>" readonly></td>
						</tr>              	
						<%
						     }
						  }
						 %>									                      								            							
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="修改" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">&nbsp;&nbsp;
																</TD>	
															</tr>
															<tr>
																<TD nowrap colspan="4" align=center>
																<br>
																	
																    <input type="button" value="不修改并查看" style="width:100" id="process1" onclick="#">
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
											
										<br></td>
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