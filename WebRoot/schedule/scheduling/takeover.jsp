<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.schedule.model.*"%>
<%@page import="com.afunms.system.model.TimeShareConfig"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>

<%  
   	String rootPath = request.getContextPath(); 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
   	Schedule schedule = (Schedule)request.getAttribute("schedule");
   	String sid = "";//当前值班记录的id
   	String id = "";//上一个班的值班记录id
   	String name = "";
   	String strPeriod = "";
   	String strDate = "";
	String log = "";
	String status = "";//上一个班的状态
	String cStatus = "";//当前班的状态
	String strStatus = "";
	boolean flag = false;
	
	Map<String,Period> periodMap = (HashMap<String,Period>)request.getAttribute("periodMap");
	if(null != schedule){
		sid = schedule.getSid();
		id = schedule.getId();
		name = schedule.getName();
		strPeriod = periodMap.get(schedule.getPeriod()).getName();
		strDate = sdf.format(schedule.getOn_date());
		log = schedule.getLog();
		log = log==null?"":log;
		status = schedule.getStatus();
		cStatus = schedule.getCstatus();
		if("0".equals(cStatus)){
			strStatus = "今天是第一个值班班次，只需要点击接班即可。";
			flag = true;
		}else if("02".equals(cStatus)){
			strStatus = "今天是第一个值班班次，已经接班。";
		}else if("03".equals(cStatus)){
			strStatus = "今天是第一个值班班次，已经交班。";
		}else if("2".equals(cStatus)){
			strStatus = "已经接班了!";
		}else if("3".equals(cStatus)){
			strStatus = "已经交班了!";
		}else if("1".equals(cStatus)){
			if("1".equals(status)){
				strStatus = "前一个班还没有接班!";
			}else if("2".equals(status)){
				strStatus = "前一个班已接班，但还没有交班!";
			}else{
				flag = true;
			}
			
		}
	}
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<script language="JavaScript" type="text/javascript">
function check(){
	var id = document.getElementById("id").value;
 	if(!id){
 		alert("没有交班人，接班异常！");
 		return false;
 	}
 	return true;
}

var schedule = <%=schedule%>;
if(!schedule){
	alert("当前没有值班安排，不用接班!");
	history.go(-1);
}

</script>
</head>
<body id="body" class="body">
	<form id="mainForm" method="post" name="mainForm" action="<%=rootPath%>/schedule.do?action=saveTakeover&id=<%=sid %>" onsubmit="return check();">
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
											                	<td class="add-content-title">日常办公 &gt;&gt; 值班安排 &gt;&gt; 接班</td>
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
				   <input type="hidden" id="scheduleid" name="scheduleid" value="<%=id %>">     										
				   <table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
					<TBODY>
						<%if(!"0".equals(cStatus) && !"02".equals(cStatus) && !"03".equals(cStatus)){ %>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="40%">交班人&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<input type=text readonly="readonly" id="username" name="username" value="<%=name %>" size="50" maxlength="32">&nbsp;&nbsp;
							</TD>						
						</tr>
						<tr>						
							<TD nowrap align="right" height="24" width="40%">值班时间&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<input type="text" readonly="readonly" id="startdate" name="startdate" value="<%=strDate %>" size="10">
							</TD>						
						</tr>
						<tr style="background-color: #ECECEC;">
							<TD nowrap align="right" height="24" width="40%">班次&nbsp;</TD>			
							<TD nowrap>&nbsp;
								<input type=text readonly="readonly" id="period" name="period" value="<%=strPeriod %>" size="50" maxlength="32">&nbsp;&nbsp;
							</td>
						</tr>
						<tr>	
							<TD nowrap align="right" height="24" width="40%">值班日志&nbsp;</TD>				
							<TD nowrap>&nbsp;
								<textarea rows="8" cols="48" readonly="readonly"><%=log %></textarea>
							</TD>					
						</tr>
						<%} %>
						<tr>
							<TD nowrap colspan="2" align=center>
							<br>
								<%
								
								if(flag){
								%>
								<input type="submit" value="接 班" style="width:50" id="process">&nbsp;&nbsp;
								<%} %>
								<input type="reset" style="width:50" value="返 回" onclick="javascript:history.back(1)">
								<font color="red"><%=strStatus %></font>
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