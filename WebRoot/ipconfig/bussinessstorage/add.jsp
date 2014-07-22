<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%
   UserView view = new UserView();
   String rootPath = request.getContextPath();
       BusinessDao bussdao = new BusinessDao();
       List allbuss = bussdao.loadAll();  
    List backbone = (List)request.getAttribute("backbone");
    List bussiness = (List)request.getAttribute("bussiness");   
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
        var chk1 = checkinput("name","string","业务名称",50,false);
        var chk2 = checkinput("segment","string","网段",18,false); 
        var chk3 = checkinput("gateway","string","网关",15,false);
        var patrn = /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/;
        
   if(chk1 && chk2 && chk3){
        var segment = document.getElementById("segment").value.toString();
        var gateway = document.getElementById("gateway").value;
        var p = "/";
        var sg = segment.split(p);
        var sgt = sg[0];
         if(sg[1] != null){
           if(patrn.test(sgt) && patrn.test(gateway)){
                Ext.MessageBox.wait('数据加载中，请稍后.. ');
                mainForm.action = "<%=rootPath%>/ipstorage.do?action=add";
                mainForm.submit();              
           }
        }else{
           alert("网段格式错误，请重新填写。格式如：10.10.10.1/22");
           return false;
        }
    }
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



//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------


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
											                	<td class="add-content-title">IP管理 >> 配置管理 >> 业务定义 >> 定义业务地址 </td>
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
				        										<input type="hidden" name="id" value="">
																<input type="hidden" name="category" value="25">
																<input type="hidden" name="code" value="DATABASE">
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1"
																	width="100%">
																
																	<tr style="background-color: #ECECEC;">
																	    <TD nowrap align="right" height="24" width="10%">该业务位于的骨干点&nbsp;</TD>				
																		<TD nowrap width="40%">&nbsp;
																		       <select id="type" size=1 name='backbone' style='width:125px;' onchange="unionSelect();">
																				<%
	                                                                                alltype backbonevo = null; 
																					if(backbone != null && backbone.size()>0){
																						for(int k=0;k<backbone.size();k++){
																							backbonevo = (alltype)backbone.get(k);
																				%>
																			<option value=<%=backbonevo.getBackbone_name()%>><%=backbonevo.getBackbone_name()%></option>
																			<%
																						}
																					}
																			%>
																			</select>
																		<font color="red">&nbsp;*</font></TD>
																		</tr>
																		
																		<tr>
																		   <TD nowrap align="right" height="24" width="10%">业务类型&nbsp;</TD>				
																		  <TD nowrap width="40%">&nbsp;
																		       <select id="type" size=1 name='bussiness' style='width:125px;' onchange="unionSelect();">
																				<%
																				    bussinesstype vo = null;
																					if(bussiness != null && bussiness.size()>0){
																						for(int k=0;k<bussiness.size();k++){
																							vo = (bussinesstype)bussiness.get(k);
																				%>
																			<option value=<%=vo.getName()%>><%=vo.getName()%></option>
																			<%
																						}
																					}
																			%>
																			</select>
																		<font color="red">&nbsp;*</font></TD>
																		</tr>
																		
																		
																		 
																		
																	<tr>
																		<TD nowrap align="right" height="24">业务名称&nbsp;</TD>				
																		<TD nowrap>
																			&nbsp;&nbsp;<input type="text" name="name" id="name" size="25" class="formStyle"><font color="red">&nbsp;*</font>
																		</TD>	
																	</tr>
																	<tr>	
																		<TD nowrap align="right" height="24">网段&nbsp;</TD>				
																		<TD nowrap>&nbsp;&nbsp;<input name="segment" id="segment" type="text" size="27" class="formStyle"><font color="red">&nbsp;*</font>														
																		</TD>	
																	</tr>
																	<tr style="background-color: #ECECEC;">
																		<TD nowrap align="right" height="24">网关&nbsp;</TD>				
																		<TD nowrap >&nbsp;&nbsp;<input name="gateway" id="gateway" type = "text" size="25" class="formStyle"><font color="red">&nbsp;*</font>
																		</TD>								
																	</tr>
																	<tr>
																		<td align="right" height="20">纵向加密&nbsp;</td>
																		<TD nowrap >&nbsp;&nbsp;<input name="encryption"  type = "text" size="25" class="formStyle"></TD>
																	</tr>
																	
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="保 存" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">
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