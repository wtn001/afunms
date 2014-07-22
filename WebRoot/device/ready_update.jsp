<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="java.util.*"%>
<%@page import="com.afunms.device.model.Cabinet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%  
   String rootPath = request.getContextPath(); 
   String menuTable = (String)request.getAttribute("menuTable");
   Cabinet cabinet=(Cabinet)request.getAttribute("cabinet");
   int  id=cabinet.getId();
   String city=cabinet.getCity();
	String roadInfo=cabinet.getRoadInfo();
	String affiliation=cabinet.getAffiliation();
	String belong=cabinet.getBelong();
	String latitude=cabinet.getLatitude();
	String longitude=cabinet.getLongitude();
	String ipaddress=cabinet.getIpaddress();
	String rackNumber=cabinet.getRackNumber();
	
	
   %>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>


<!-- snow add for gatherTime at 2010-5-12 start -->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/addTimeConfig.js" charset="gb2312"></script>
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/jquery-1.4.2.js" charset="gb2312"></script>






<script language="JavaScript" type="text/javascript">



function toAllAdd(){
	return CreateWindow('<%=rootPath%>/network.do?action=alladd');
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
	initTR();
}
function changehid(){
   var collecttype=document.getElementById("collecttype").value;
   if(collecttype!='10'){
    document.getElementById("messageadd").style.display="none";
    document.getElementById("messageadd1").style.display="none";
   }
}

 Ext.onReady(function()
{  

	
 Ext.get("process").on("click",function(){
  	
     var chk1 = true;
     var chk2 =true;
     var chk3=true;
     var chk4=true;
     var chk5=true;
     var chk7=true;
     var chk8=true;
     var chk9=true;


     if(chk1&&chk2&&chk3&&chk4&&chk5&&chk7&&chk8&&chk9)
     {
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
       
         mainForm.action = "<%=rootPath%>/device.do?action=updateCabinet";
         mainForm.submit();
     }  
     
 });	
	
});

</script>




</head>
<body id="body" class="body" onload="initmenu();">

<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">修改信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">监视信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">取消监视</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">添加监视</td>
		</tr>		
	</table>
	</div>
	<!-- 右键菜单结束-->

	<form id="mainForm" method="post" name="mainForm">
		<input type="hidden" value="<%=id %>" id="id" name="id"/>
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
											                	<td class="add-content-title">设备管理>> 机柜添加</td>
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
				        										
				        												<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">城市&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="city" maxlength="50" size="20" class="formStyle" value="<%=city %>">
								<font color="red">&nbsp;</font>&nbsp;&nbsp;</TD>						
							<TD nowrap align="right" height="24" width="10%">路口信息&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="roadInfo" maxlength="150" size="40" class="formStyle" value="<%=roadInfo %>">
								<font color="red">&nbsp;</font></TD>															
						</tr>
						
						<tr  id='community_tr'>				
							<TD nowrap align="right" height="24" width="10%">所属机构&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="affiliation" maxlength="50" size="20" class="formStyle" value="<%=affiliation %>">
								<font color="red">&nbsp;</font></TD>						
							<TD nowrap align="right" height="24" width="10%">所属辖区&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="belong" maxlength="50" size="20" class="formStyle" value="<%=belong %>">
								</TD>															
						</tr>
					
						<tr style="background-color: #ECECEC;" id='securityLevel_tr' >
							<TD nowrap  align="right" height="24" width="10%">经度&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="latitude" name="latitude" maxlength="50" size="20" class="formStyle" value="<%=latitude %>">
								<font color="red">&nbsp;</font>
							</TD>						
							<TD nowrap  align="right" height="24" width="10%">纬度&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="longitude" name="longitude" maxlength="50" size="20" class="formStyle" value="<%=longitude %>">
								<font color="red">&nbsp;</font>
							</TD>
						</tr>
						
						<tr id='v3_ap_tr' >
							<TD nowrap  align="right" height="24" width="10%">IP地址&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="ipaddress" name="ipaddress" maxlength="50" size="20" class="formStyle" value="<%=ipaddress %>">
								<font color="red">&nbsp;</font>
							</TD>						
							<TD nowrap  align="right" height="24" width="10%">机柜编号&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" id="rackNumber" name="rackNumber" maxlength="100" size="30" class="formStyle" value="<%=rackNumber %>">
								<font color="red">&nbsp;</font>
							</TD>
						</tr>
										
						<tr >
							<td colspan="4" align='center'>
								<div id="msg" style="display:none"><font color="#FF0000">需要一点时间,请稍候...</font></div>
							</td>
						</tr>			
						<tr>
							<td nowrap colspan="4" height="1" bgcolor="#8EADD5"></td>
						</tr>
			
		
           
           
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="保 存" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">
																</TD>	
															</tr>	
							</TBODY>
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
</BODY>


</HTML>                                                     