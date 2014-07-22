<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.schedule.model.*"%>
<%@page import="com.afunms.schedule.dao.*"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Iterator"%>
<%@ include file="/include/globe.inc"%>
<%
String rootPath = request.getContextPath();
String menuTable = (String) request.getAttribute("menuTable");
PositionDao positionDao = new PositionDao();
List<Position> positionList = positionDao.loadAll();
Map<String,Position> positionMap = new HashMap<String,Position>();
for (Position position : positionList) {
	positionMap.put(position.getId(), position);
}
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css"/>

<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;

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

}


   </script>
   
   <script type="text/javascript">
	 function toAdd()
  {
     mainForm.action = "<%=rootPath%>/schedule.do?action=ready_schedule";
     mainForm.submit();
  }
 	function delSchedule(){
 		var euserid = document.getElementById("userid");
 		var eperiodid = document.getElementById("periodid");
 		var epositionid = document.getElementById("positionid");
 		
 		var txtuserid = euserid.options[euserid.selectedIndex].text;
 		var txtperiodid = eperiodid.options[eperiodid.selectedIndex].text;
 		var txtpositionid = epositionid.options[epositionid.selectedIndex].text;
 		var startdate = document.getElementsByName("startdate")[0].value;
 		var enddate = document.getElementsByName("enddate")[0].value;
 		
 		var alertStr = "";
 		if(txtuserid && txtuserid != "不限"){
 			alertStr += txtuserid + " ";
 		}
 		if(txtperiodid && txtperiodid != "不限"){
 			alertStr += txtperiodid + " ";
 		}
 		if(txtpositionid && txtpositionid != "不限"){
 			alertStr += txtpositionid + " ";
 		}
 		if(startdate){
 			alertStr += startdate + "日期之后 ";
 		}
 		if(enddate){
 			alertStr += enddate + "日期之前";
 		}
 		
 		if(alertStr){
 			alertStr = "是否删除 " + alertStr + "的值班计划？";
 		}else{
 			alertStr = "是否删除所有值班计划？";
 		}
 		if(window.confirm(alertStr)){
 			mainForm.action = "<%=rootPath%>/schedule.do?action=deleteSchedule";
 			mainForm.submit();
 		}
 	}
	
	function edit(){
		mainForm.action = "<%=rootPath%>/roomvideo.do?action=ready_edit&id=" + node;
		mainForm.submit();
	}
	function search()
  {
	mainForm.action = "<%=rootPath%>/schedule.do?action=list";
    mainForm.submit();
	
  }
  function exportReport(exportType){
	 var startdate=document.all.startdate.value;
	 var todate=document.all.enddate.value;
	 window.open('<%=rootPath%>/schedule.do?action=downloadReport&type=schedule&exportType='+exportType+'&startdate='+startdate+'&todate='+todate+'&nowtime='+(new Date()),"_blank","toolbar=no,width=1,height=1,top=2000,left=3000,directories=no,status=no,menubar=no,alwaysLowered=yes");
	}
function onchangeView(){
  		mainForm.action = "<%=rootPath%>/schedule.do?action=calendarView";
  		mainForm.submit();
  	}
</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
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
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 日常办公 >> 值班计划 >> 值班台历 </td>
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
																<tr align="center">
																    <td class="body-data-title" style="text-align:center;">
                                                                       &nbsp;&nbsp;工作地点：<select id="positionid" name="positionid" style="width:100px;">
                                                                       	<option value="">不限</option>
																	    <%
																	    	if (positionMap != null) {
																	    		String selected = "";

																	    		for (Iterator<Map.Entry<String,Position>> iter = positionMap.entrySet().iterator(); iter.hasNext();) {
																	    			Map.Entry<String,Position> entry = iter.next();
																	    			Position vo = entry.getValue();
																	    %>
																	        <option value="<%=vo.getId() %>" <%=selected%>><%=vo.getName() %></option>
																	        <%
																	        	}
																	        }
																	        %>
																	        
                                                                       </select> 
																	</td>
																</tr>
																<tr align="center">
																	<td>
																		<br>
																		<input type="button" value="台历视图" onclick="onchangeView();">
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
			</td>
			</tr>
			</table>
			
		</form>
	</BODY>
</HTML>
