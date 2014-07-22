<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.schedule.model.Period"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.afunms.common.util.SessionConstant" %>

<%
   UserView view = new UserView();
   Period vo = (Period)request.getAttribute("vo");
   String startTime = "";
   String endTime = "";
   if(null != vo){
	   SimpleDateFormat sdf = new SimpleDateFormat("HH:ss");
	   startTime = sdf.format(vo.getStart_time());
	   endTime = sdf.format(vo.getEnd_time());
   }
   String rootPath = request.getContextPath();
   
       BusinessDao bussdao = new BusinessDao();
       List allbuss = bussdao.loadAll();   
       
       String businessids = null;//vo.getBusinessids();
       if(businessids == null)businessids = "";
       String[] bids = businessids.split(","); 
       List bussList = new ArrayList();
       if(bids.length>0){
    	   for(int i=0;i<bids.length;i++){
    	   	if(bids[i].trim().length()==0)continue;
    	   	bussList.add(bids[i]);
    	   }	   
       }
       
        User operator = (User)session.getAttribute(SessionConstant.CURRENT_USER); 
       
       
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
     var chk1 = checkinput("name","string","名称",50,false);
     var chk2 = checkinput("start_time","string","开始时间",5,false);
     var chk3 = checkinput("end_time","string","结束时间",5,false);
     /**
     var chk6 = checkinput("phone","string","电话",30,true);
     var chk7 = checkinput("mobile","string","手机",30,true);
     var chk8 = checkinput("email","string","Email",30,true);
     */

     if(chk1&&chk2&&chk3)
     {
        mainForm.action = "<%=rootPath%>/schedule.do?action=updatePeriod";
        mainForm.submit();
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

    <form name="mainForm" method="post">
             <input type=hidden name="id" value="<%=vo.getId()%>">
             <input type=hidden name="userid" value="<%=vo.getName() %>">
             
             <%
               if(operator.getRole()==0)
               {
              %>
             <input type=hidden name="curRole" value="<%=operator.getRole()%>">
             <%}%>
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
											                	<td class="add-content-title">日常办公 >> 排班计划 >> 班次编辑</td>
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
																
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24" width="45%">名称&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<input type="text" name="name" maxlength="50" size="20" class="formStyle" value="<%=vo.getName() %>">
								<font color="red">&nbsp;*</font>
							</TD>						
						</tr>
						<tr>						
							<TD nowrap align="right" height="24" width="45%">开始时间&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<input type="text" name="start_time" maxlength="50" size="20" class="formStyle" value="<%=startTime %>">
								<font color="red">&nbsp;*</font>
							</TD>						
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="45%">结束时间&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<input type="text" name="end_time" maxlength="50" size="20" class="formStyle" value="<%=endTime %>">
								<font color="red">&nbsp;*</font>
							</TD>						
						</tr>
						
							 			                 										                      								            							
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="修改" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
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