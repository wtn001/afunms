<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.model.*" %>
<%@page import="com.afunms.config.model.EnvConfig"%>
<%@ include file="/include/globe.inc"%>
<% 
 

  String rootPath = request.getContextPath(); 
  List list = (List)request.getAttribute("list");
  String id = (String)request.getAttribute("id");
  //Host host = (Host)PollingEngine.getInstance().getNodeByID(Integer.parseInt(id));
  String ipaddress = (String)request.getAttribute("ipaddress");
java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM-dd HH:mm");					  	 
         
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=rootPath%>/resource/css/global/global.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">

<script language="JavaScript" type="text/javascript">

function fromNodeFanConfig()
  {  
     mainForm.action = "<%=rootPath%>/envconfig.do?action=fromNodeFanConfig";
     mainForm.submit();
  }  
  
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

<!--wxy add-->
function editNodePort(id,sms)
  {
 
      $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=editnodepower&id="+id+"&enabled="+sms+"&nowtime="+(new Date()),
			success:function(data){
			
			if(data.flagStr==3){
			alert("�޸�ʧ�ܣ�����");
			}else if(data.flagStr==0){
			var smsId =document.getElementById("smsFlag"+id);
		    smsId.innerHTML ="<span style='cursor:hand' onclick='editNodePort("+id+",1)'><font color=#skyblue>����</font></span>";
			}else if(data.flagStr==1){
			var smsId =document.getElementById("smsFlag"+id);
		    smsId.innerHTML ="<span style='cursor:hand' onclick='editNodePort("+id+",0)'><font color=#green>ȡ������</font></span>";
			}
			}
		});
  }
  <!--wxy add-->
  function editNodePortForReport(id,reportFlag)
  {
  
      $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=editnodeport&id="+id+"&reportflag="+reportFlag+"&nowtime="+(new Date()),
			success:function(data){
		
			if(data.flagStr==3){
			alert("�޸�ʧ�ܣ�����");
			}else if(data.flagStr==0){
			var reportId =document.getElementById("reportflag"+id);
		    reportId.innerHTML ="<span style='cursor:hand' onclick='editNodePortForReport("+id+",1)'><font color=#skyblue>��ʾ�ڱ���</font></span>";
			}else if(data.flagStr==1){
			var reportId =document.getElementById("reportflag"+id);
		    reportId.innerHTML ="<span style='cursor:hand' onclick='editNodePortForReport("+id+",0)'><font color color=#green>ȡ����ʾ�ڱ���</font></span>";
			}
			}
		});
  }
</script>

<script language="JavaScript" type="text/JavaScript">
//�޸ľ��漶��
function modifyPowerAlarmlevel(id){
	var t = document.getElementById("alarmlevel"+id);
	var alarmvalue = t.selectedIndex;//��ȡ�������ֵ
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyPowerAlarmlevel&id="+id+"&alarmlevel="+alarmvalue+"&nowtime="+(new Date()),
			success:function(data){
				window.alert("�޸ĳɹ���");
			}
		});
}

</script>

</head>
<body id="body" class="body">

   <form name="mainForm" method="post">
   <input type=hidden name="id" value="<%=id%>">
   <input type=hidden name="ipaddress" value="<%=ipaddress%>">
   <input type=hidden name="entity" value="fan">
		<table id="body-container" class="body-container">
			<tr>
				
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
											                	<td class="add-content-title">�豸����������Ϣ >> <%=ipaddress%></td>
											                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
											       			</tr>
											        	</table>
				        							</td>
				        						</tr>
				        						<tr >
													<td>
														<table id="add-content-header" class="add-content-header" width="100%">
										                	<tr >
											                	<td align="right" width="">
											                	   <INPUT type="button" class="formStyle" value="ˢ ��" onclick="fromNodeFanConfig()"/>
											                	   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											                	</td>	
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
																	<tr>
							      											<th width='4%'   class="report-data-body-title" align="center" bgcolor=#F1F1F1>���</th>				
							      											<th width='18%' class="report-data-body-title" align="center" bgcolor=#F1F1F1>�豸����(ip)</th>
							      											<th width='18%' class="report-data-body-title" align="center" bgcolor=#F1F1F1>����ģ��</th>
							      											<th width='14%' class="report-data-body-title" align="center" bgcolor=#F1F1F1>����</th>      
							      											<th width='7%' class="report-data-body-title" align="center" bgcolor=#F1F1F1>�Ƿ����</th>
							      											<th width='10%' class="report-data-body-title" align="center" bgcolor=#F1F1F1>��������</th>
							      											<th width='5%' class="report-data-body-title" align="center" bgcolor=#F1F1F1>����</th>
							      											
																	</tr>

<%
  EnvConfig vo = null;
    for(int i=0;i<list.size();i++)
    {
       vo = (EnvConfig)list.get(i);
       if(vo==null)continue;

          
%>
   										<tr bgcolor="#FFFFFF" >  
    											<td class="report-data-body-list"><font color='blue'>&nbsp;<%= 1+i%></font></td>
    											<td  class="report-data-body-list"><%=vo.getIpaddress()%></td>
    											<td  class="report-data-body-list"><%=vo.getName()%></td>
    											<td  class="report-data-body-list"><%=vo.getBak()%></td>
    											<%
    												String smsbgcolor="skyblue";
    												String repbgcolor="skyblue";
    												String sms="<span style='cursor:hand' onclick='editNodePort("+vo.getId()+",0)'><font color=green>ȡ������</font></span>";
    												if(vo.getEnabled()==0){
    													smsbgcolor="#FFFFFF";
    													sms="<span style='cursor:hand' onclick='editNodePort("+vo.getId()+",1)'><font color=#skyblue>����</font></span>";
    												}
    											%>
    											<td  align='center' class="report-data-body-list" id="smsFlag<%=vo.getId() %>"><%=sms%></td>
    											<% 
    											   String alarmlevel = vo.getAlarmlevel();
    											   //String varalarmlevel = "";
    											   if(alarmlevel == null || alarmlevel.equals("")) {
    												   alarmlevel = "0";
    											   }
    											%>
    											<td  class="report-data-body-list">
													<select style="width:60px;" id="alarmlevel<%=vo.getId()%>">
    													<option  value="0" <%if(alarmlevel.equals("0")) {%>selected<%} %>>��</option>
    													<option  value="1" <%if(alarmlevel.equals("1")) {%>selected<%} %>>��ͨ</option>
    													<option  value="2" <%if(alarmlevel.equals("2")) {%>selected<%} %>>��Ҫ</option>
    													<option  value="3" <%if(alarmlevel.equals("3")) {%>selected<%} %>>����</option>    													
    													</select>
     												<img href="#" src="<%= rootPath%>/resource/image/menu/xgmm.gif" style="cursor:hand" onclick="modifyPowerAlarmlevel('<%=vo.getId()%>');"/>
												</td>
											
											<td  class="report-data-body-list" align='center'><span style='cursor:hand' onClick='window.open("<%=rootPath%>/envconfig.do?action=readyEdit&entity=fan&id=<%=vo.getId()%>","_blank", "height=400, width= 500, top=200, left= 200")'>
											<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></span></td>
        										
  										</tr>			
<% }%>            
            					</tbody>
            					</table>
            					<br>
			</TD>																			
			</tr>			
															
															<tr>
																<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
																<td><input type="hidden" id="rowNum" name="rowNum"></td>
																<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
															
															</tr>
															<tr>
																<TD nowrap colspan="4" align=center>
																
																	<input type="reset" style="width:50" value="�ر�" onclick="javascript:window.close()">
																</TD>	
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