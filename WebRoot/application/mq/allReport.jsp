<%@ page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc" %>

<%@page import="com.afunms.detail.service.IISInfo.IISInfoService"%>
<%@page import="com.afunms.report.jfree.ChartCreator"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.monitor.item.*"%>
<%@page import="com.afunms.polling.node.*" %>
<%@page import="com.afunms.polling.om.*" %>
<%@page import="com.afunms.monitor.item.base.MonitorResult"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.monitor.item.base.MoidConstants"%>
<%@page import="org.jfree.data.general.DefaultPieDataset"%>
<%@page import="com.afunms.application.util.*"%>
<%@page import="org.jdom.Element"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.manage.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.lang.*"%>
<%@page import="com.afunms.polling.*"%>
<html>
<head>
<%
   String runmodel = PollingEngine.getCollectwebflag(); 
   String rootPath = request.getContextPath(); 
   String menuTable = (String)request.getAttribute("menuTable");
   
   MQConfig vo = (MQConfig)request.getAttribute("vo");
   if(vo == null){
		vo = new MQConfig();
   }
	String tmp = vo.getId()+"";
%>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<title>MQ�ۺϱ���</title>
<script type='text/javascript' src='/afunms/dwr/interface/DWRUtil.js'></script>
<script type='text/javascript' src='/afunms/dwr/engine.js'></script>
<script type='text/javascript' src='/afunms/dwr/util.js'></script>
<script language="javascript" src="/afunms/js/tool.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script language="JavaScript" type="text/JavaScript">
function ping_word_report()
{
	mainForm.action = "<%=rootPath%>/mq.do?action=downloadAllReport&type=all&str=1&id=<%=tmp%>";
	mainForm.submit();
}
function ping_excel_report()
{
	mainForm.action = "<%=rootPath%>/mq.do?action=downloadAllReport&type=all&str=0&id=<%=tmp%>";
	mainForm.submit();
}
function ping_pdf_report()
{
	mainForm.action = "<%=rootPath%>/mq.do?action=downloadAllReport&type=all&str=2&id=<%=tmp%>";
	mainForm.submit();
}
function ping_ok()
{
	var starttime = document.getElementById("mystartdate").value;
	var endtime = document.getElementById("mytodate").value;
	mainForm.action = "<%=rootPath%>/mq.do?action=showPingReport&id=<%=tmp%>&startdate="+starttime+"&todate="+endtime;
	mainForm.submit();
}
function ping_cancel()
{
window.close();
}
function query(){
  	var startdate = mainForm.startdate.value;
  	var todate = mainForm.todate.value;      
  	var oids ="";
  	var checkbox = document.getElementsByName("checkbox");
 		for (var i=0;i<checkbox.length;i++){
 			if(checkbox[i].checked==true){
 				if (oids==""){
 					oids=checkbox[i].value;
 				}else{
 					oids=oids+","+checkbox[i].value;
 				}
 			}
 		}
 	if(oids==null||oids==""){
 		alert("������ѡ��һ���豸");
 		return;
 	}
 	window.open ("<%=rootPath%>/hostreport.do?action=downloadmultihostreport&startdate="+startdate+"&todate="+todate+"&ids="+oids, "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes")    	      
}

function openwin(str,operate,ip) 
{	
  var startdate = mainForm.startdate.value;
  var todate = mainForm.todate.value;
  window.open ("<%=rootPath%>/hostreport.do?action="+operate+"&startdate="+startdate+"&todate="+todate+"&ipaddress="+ip+"&str="+str+"&type=host", "newwindow", "height=500, width=600, toolbar= no, menubar=no, scrollbars=yes, resizable=yes, location=yes, status=yes") 
}
function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=netping"
  	mainForm.submit();
}
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/hostreport.do?action=hostcpu";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
       // mainForm.submit();
 });	
	
});
</script>



<script language="JavaScript" type="text/JavaScript">
var show = true;
var hide = false;
//�޸Ĳ˵������¼�ͷ����

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
//��Ӳ˵�	
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
	setClass();
}

function setClass(){
	document.getElementById('hostReportTitle-5').className='detail-data-title';
	document.getElementById('hostReportTitle-5').onmouseover="this.className='detail-data-title'";
	document.getElementById('hostReportTitle-5').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
<!-- snow add 2010-5-28 -->
<style>
<!--
body{
background-image: url(${pageContext.request.contextPath}/resource/image/bg.jpg);
TEXT-ALIGN: center;
}
-->
</style>
</head>
<body id="body" class="body" onload="init();">
<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
<form id="mainForm" method="post" name="mainForm">
<input type=hidden id="ipaddress" name="ipaddress" value=<%=request.getParameter("ipaddress") %>>
	<table id="container-main" class="container-main">
		<tr>
			<td>
				<table id="container-main-win" class="container-main-win">
					<tr>
						<td>
							<table id="win-content" class="win-content">
								<tr>
									<td>
										<table id="win-content-header" class="win-content-header">
				                			<tr>
							                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
							                	<td class="win-content-title" style="align:center">&nbsp;MQ(<%=vo.getIpaddress() %>)�ۺϱ���</td>
							                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>											   
											</tr>
									    
					       				</table>
				       				</td>
				       			</tr>
						       	<tr>
						       		<td>
						       			<table id="win-content-body" class="win-content-body">
											<tr>
						       					<td>
													<table bgcolor="#ECECEC">
														<tr align="left" valign="center"> 
														<td height="28" align="left" width=60%>
														</td>
														<td height="28" align="left">
															<a href="javascript:ping_word_report()"><img name="selDay1" alt='����word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">����WORLD</a>
														</td>
														<td height="28" align="left">
															<a href="javascript:ping_excel_report()"><img name="selDay1" alt='����EXCEL' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_excel.gif" width=18  border="0">����EXCEL</a>
														</td>
														<td height="28" align="left">&nbsp;
															<a href="javascript:ping_pdf_report()"><img name="selDay1" alt='����word' style="CURSOR: hand" src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">����PDF</a>
														</td>
														</tr>  
													</table>
						       					</td>
						       				</tr>
											<tr>
							                	<td class="win-data-title" style="height: 29px;" ></td>
							       			</tr>
							       			<tr align="left" valign="center"> 
			             						<td height="28" align="left" border="0">
													
													<input type=hidden name="eventid">
													<div id="loading">
													<div class="loading-indicator">
														<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
													</div>
													<table border="1" width="90%">
														 <tr>
											    	<td width=100%>
											    		<table class="application-detail-data-body">
							<tr>
								<td>
									<table id="application-detail-data" class="application-detail-data">
                                       <tr>
									    	<td>
									    		<table class="application-detail-data-body">
									 				  <tr>
														<td>
												      		<table width="96%" align="center">
													      		<tr bgcolor="#F1F1F1">
																	<td>ͨ����Ϣ</td>
																</tr>
																<tr>
																	<td align=center>
																		<table width=80%>
		  																	<tr bgcolor="#FFFFFF">
		    																	<td width=10% class="detail-data-body-title" style="height:29;align:center"><strong>���</strong></td>
																			    <td width=20% class="detail-data-body-title" style="height:29;align:center"><strong>ͨ������</strong></td>
																			    <td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>ͨ��״̬</strong></td>
																			    <td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>���յĻ�����</strong></td>
																			    <td width=10% class="detail-data-body-title" style="height:29;align:center"><strong>���͵Ļ�����</strong></td>
																			    <td width=10% class="detail-data-body-title" style="height:29;align:center"><strong>���յ��ֽ���</strong></td>
																			    <td width=10% class="detail-data-body-title" style="height:29;align:center"><strong>���͵��ֽ���</strong></td>
		    																</tr>
										     								<%
										     								    Hashtable basicInfoHashtable = (Hashtable)request.getAttribute("basicInfoHashtable");
																			 	List chstatusList = (List)request.getAttribute("chstatusList"); 	
																			 	if (chstatusList != null && chstatusList.size()>0){
																			 		for(int i=0;i<chstatusList.size();i++){
																			 			Hashtable tmpHashtable = (Hashtable)chstatusList.get(i);
																			 			if (tmpHashtable != null && tmpHashtable.size()>0){
																			 				String chstatusname = (String)tmpHashtable.get("chstatusname");
																							String bufsrcvd = (String)tmpHashtable.get("bufsrcvd");
																							String bufssent = (String)tmpHashtable.get("bufssent");
																							String bytsrcvd = (String)tmpHashtable.get("bytsrcvd");
																							String bytssent = (String)tmpHashtable.get("bytssent");
																							String status = (String)tmpHashtable.get("status");
																			 		
																			 %>    
																						  <tr  class="othertr" <%=onmouseoverstyle%> >
																						    <td height="28" class="detail-data-body-list"><%=i+1%></td>
																						    <td class="detail-data-body-list"><%=chstatusname%></td>
																						    <td class="detail-data-body-list"><%=status%></td>
																						    <td class="detail-data-body-list"><%=bufsrcvd%></td>
																						    <td class="detail-data-body-list"><%=bufssent%></td>
																						    <td class="detail-data-body-list"><%=bytsrcvd%></td>
																						    <td class="detail-data-body-list"><%=bytssent%></td>
																						   </tr>
																			<%
																						}
																					}
																				}
																			%>      														
																		</table>
																	</td>
																</tr>     
													      	</table>
									   					</td>
									 				 </tr>	
									 				  <tr>
														<td>
												      		<table width="96%" align="center">
													      		<tr bgcolor="#F1F1F1">
																	<td>������Ϣ</td>
																</tr>
																<tr>
																	<td align=center>
																		<table width=80%>
		  																	<tr bgcolor="#FFFFFF">
		  																		<td width=10% class="detail-data-body-title" style="height:29;align:center"><strong>���</strong></td>
																			    <td width=20% class="detail-data-body-title" style="height:29;align:center"><strong>����</strong></td>
																			    <td width=30% class="detail-data-body-title" style="height:29;align:center"><strong>������֧�ֵĲ����������������</strong></td>
																			    <td width=14% class="detail-data-body-title" style="height:29;align:center"><strong>�˿�</strong></td>
																			    <td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>״̬</strong></td>
		    																</tr>
										     								<%
				                    										if(null != basicInfoHashtable && basicInfoHashtable.size() > 0){
				                    											String statu = (String)basicInfoHashtable.get("statu");
				                    											if("null".equals(statu)){statu = "";}
				                    											String listener = (String)basicInfoHashtable.get("listener");
				                    											if("null".equals(listener)){listener = "";}
				                    											String backlog = (String)basicInfoHashtable.get("backlog");
				                    											if("null".equals(backlog)){backlog = "";}
				                    											String port = (String)basicInfoHashtable.get("port");
				                    											if("null".equals(port)){port = "";}
				                    										 %>  
																			  <tr  class="othertr" <%=onmouseoverstyle%> >
																			    <td height="28" class="detail-data-body-list">1</td>
																			    <td height="28" class="detail-data-body-list"><%=listener%></td>
																			    <td height="28" class="detail-data-body-list"><%=backlog%></td>
																			    <td height="28" class="detail-data-body-list"><%=port%></td>
																			    <td height="28" class="detail-data-body-list"><%=statu%></td>
																			   </tr>
																			<%
																			}
																			%>      														
																		</table>
																	</td>
																</tr>     
													      	</table>
									   					</td>
									 				 </tr>	
									 				 <tr>
														<td>
												      		<table width="96%" align="center">
		                                                       	<tr bgcolor="#F1F1F1">
																	<td>Զ�̶�����Ϣ</td>
																</tr>
																<tr>
																	<td align=center>
																		<table width=80%>
		  																	<tr bgcolor="#FFFFFF">
		  																	    <td width=10% class="detail-data-body-title" style="height:29;align:center"><strong>���</strong></td>
					                    										<td width=20% class="detail-data-body-title" style="height:29;align:center"><strong>��������</strong></td>
																				<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>��������</strong></td>
																				<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>Զ�̶�������</strong></td>
																				<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>����������</strong></td>
																				<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>�����������</strong></td>
																			</tr>
					                  										<%
																			 	List remoteValue = (ArrayList)request.getAttribute("remoteQueueList"); 	
																			 	if (remoteValue != null && remoteValue.size()>0){
																			 		for(int i=0;i<remoteValue.size();i++){
																			 			Hashtable tmpHashtable = (Hashtable)remoteValue.get(i);
																			 			if (tmpHashtable != null && tmpHashtable.size()>0){ 
																			 				String queue = (String)tmpHashtable.get("queue");
																							String type = (String)tmpHashtable.get("type");
																							String rqmname = (String)tmpHashtable.get("rqmname");
																							String rname = (String)tmpHashtable.get("rname");
																							String xmitq = (String)tmpHashtable.get("xmitq");		
																			 %> 
			   
																							<tr  class="othertr" <%=onmouseoverstyle%> >
					  																			<td height="28" class="detail-data-body-list"><%=i+1 %></td>
					  																			<td height="28" class="detail-data-body-list"><%=queue%></td>
					  																			<td height="28" class="detail-data-body-list">Զ�̶���</td>
					  																			<td height="28" class="detail-data-body-list"><%=rqmname%></td>
					  																			<td height="28" class="detail-data-body-list"><%=rname%></td>
					  																			<td height="28" class="detail-data-body-list"><%=xmitq%></td>
																							 </tr>
																			<%
																					}
																				}
																			}
																			%>
																			</table>
									   									</td>
									 								 </tr>				
															</table>
									   					</td>
									 				 </tr>
									 				 
									 				 <tr>
														<td>
												      		<table width="96%" align="center">
													      		<tr bgcolor="#F1F1F1">
																	<td>���ض�����Ϣ</td>
																</tr>
																<tr>
																	<td align=center>
																		<table width=80%>
		  																	<tr bgcolor="#FFFFFF">
		    																	<td width=10% class="detail-data-body-title" style="height:29;align:center"><strong>���</strong></td>
		    																	<td width=20% class="detail-data-body-title" style="height:29;align:center"><strong>��������</strong></td>
		    																	<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>��������</strong></td>
		    																	<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>��ǰ���</strong></td>
		    																	<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>������</strong></td>
		    																	<td width=15% class="detail-data-body-title" style="height:29;align:center"><strong>��ǰ��Ȱٷֱ�</strong></td>
		    																</tr>
										     								<%
																			 	List localValue = (List)request.getAttribute("localQueueList"); 	
																			 	if (localValue != null && localValue.size()>0){
																			 		for(int i=0;i<localValue.size();i++){
																			 			Hashtable tmpHashtable = (Hashtable)localValue.get(i);
																			 			if (tmpHashtable != null && tmpHashtable.size() > 0){
																			 				String queue = (String)tmpHashtable.get("queue");
																							String curdepth = (String)tmpHashtable.get("curdepth");
																							String maxdepth = (String)tmpHashtable.get("maxdepth");
																							String type = (String)tmpHashtable.get("type");		
																							String percent = (String)tmpHashtable.get("percent");		
																			 %> 
						 																	<tr  class="othertr" <%=onmouseoverstyle%> >
						   																			<td height="28" class="detail-data-body-list"><%=i+1 %></td>
						   																			<td height="28" class="detail-data-body-list"><%=queue%></td>
						   																			<td height="28" class="detail-data-body-list">���ض���</td>
						   																			<td height="28" class="detail-data-body-list"><%=curdepth%></td>
						   																			<td height="28" class="detail-data-body-list"><%=maxdepth%></td>
						   																			<td height="28" class="detail-data-body-list"><%=percent%></td>
						 																	</tr>
																				<%
																						}
																					}
																				}
																				%>																	
																		</table><br><br>
            		<div align=center>
            			<input type=button value="�رմ���" onclick="window.close()">
            		</div> <br>
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
						       		</td>
						       	</tr>
						                
              					</table>
              				</td>
              			</tr>
      				</table>
					</td>
				</tr>
       			</table>
			 
					<br>
</form>  
</body>
</html>