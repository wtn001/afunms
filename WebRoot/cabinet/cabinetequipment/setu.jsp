<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.afunms.system.model.*"%>
<%
	String rootPath = request.getContextPath();
	List ulist = (List)request.getAttribute("ulist");
	int usum = (Integer)request.getAttribute("usum");
	String event = (String)request.getAttribute("event");
	List allUser = (List)request.getAttribute("allUser");
%>
<html>
<head>
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
<script language="JavaScript" type="text/javascript" fptype="dynamicoutline">

function valid(){
 		//var oids ="";	
 		//window.alert(window.getElementsByTagName("checkbox"));
 		//for (var i=0;i<mainForm.checkbox.length;i++){
 		//	if(mainForm.checkbox[i].checked==true){
 		//		if (oids==""){
 		//			oids=mainForm.checkbox[i].value;
 		//		}else{
 		//			oids=oids+","+mainForm.checkbox[i].value;
 		//		}
 		//	}
 		//}
 		//window.alert(oids);
		//var event = parent.opener.document.getElementById("<%=event%>");
 		//event.value=oids;      
		//window.close();
		var oids ="";
		var formItem=document.forms["mainForm"];
		var formElms=formItem.elements;
		var l=formElms.length;
		var arr = new Array();
		/*var i=0;
		while(l--){
			if(formElms[l].type=="checkbox"){
				var checkbox=formElms[l];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (oids==""){
	 					oids=checkbox.value;
	 				}else{
	 					oids=oids+","+checkbox.value;
	 				}
	 				
	 				arr[i]=checkbox.value;
	 				i++;
 				}
			}
		}*/
		var index = 0;
		for(var i=0; i<formElms.length;i++){
			if(formElms[i].type=="checkbox"){
				var checkbox=formElms[i];
				if(checkbox.name == "checkbox" && checkbox.checked==true){
	 				if (oids==""){
	 					oids=checkbox.value;
	 				}else{
	 					oids=oids+","+checkbox.value;
	 				}
	 				arr[index]=checkbox.value;
	 				index++;
 				}
			}
		}
		var len=arr.length;
		for(var i=0;i<len;i++){
  			 if(i!=0 && (arr[i]-arr[i-1])!=1){
	  			  alert("��ѡ�������ĵ�Ԫ");
	  			  return false;
  			  }
		} 
		
		var unumbers = parent.opener.document.getElementById("unumbers");
 		unumbers.value=oids;      
		window.close();
	}	 

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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
}

</script>
</head>
<body id="body" class="body" >

	<!-- �Ҽ��˵�����-->
   <form name="mainForm" method="post">
   <input type=hidden name="oid">
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
											                	<td class="add-content-title">ѡ�����U��Ԫ</td>
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
																			<tr align="right" > 
							  													<td colspan=10 align=left height=18>
							  															<table width="100%" cellSpacing="0"  cellPadding="0" border=0  class="noprint">
																							  <tr >
																							  <td align="right" height=18>
																							  		&nbsp;  
																							    </td> 
																							    </tr> 
																						</table>
																			    </td>
																			  </tr>
							  <tr align="center">
							  <td style="align:center">
							  		<table style="width:50%;align:center">
							  			<tr bgcolor=gray>
							  				<td class="report-data-body-title" width=20% style="bgcolor:gray">U��Ԫ</td>
							  				<td class="report-data-body-title" width=60% bgcolor=gray>����</td>
							  				<td class="report-data-body-title" width=20% bgcolor=gray>U��Ԫ</td>
							  			</tr>
							  			<%
							  				for(int i=1;i<usum+1;i++){
							  					if(ulist.contains(i+"")){
							  			%>
							  			<tr>
							  				<td bgcolor=gray class="report-data-body-list" height="20" align=center><%=i%></td>
							  				<td bgcolor=lightgray class="report-data-body-list" height="20" >&nbsp;&nbsp;&nbsp;&nbsp;��<%=i%>U��Ԫ</td>
							  				<td bgcolor=gray class="report-data-body-list" height="20" align=center><%=i%></td>
							  			</tr>
							  			<%
							  			}else{
							  			%>							  			
							  			<tr>
							  				<td class="report-data-body-list" height="20" align=center bgcolor=green><%=i%></td>
							  				<td class="report-data-body-list" height="20" bgcolor=#3CB371><input type=checkbox name=checkbox value='<%=i%>' class=noborder >��<%=i%>U��Ԫ</td>
							  				<td class="report-data-body-list" height="20" align=center bgcolor=green><%=i%></td>
							  			</tr>
							  			<%
							  				}
							  			}
							  			%>							  			
							  		</table>
							  </td>
							  </tr>
																			<tr align="right" > 
							  													<td colspan=10 align=left height=18>
							  														<br>
							  															<table width="100%" cellSpacing="0"  cellPadding="0" border=0  class="noprint">
																							  <tr >
																							  <td align="center" height=18>
																							  		<input type="submit" value="ȷ ��" name="delbutton" class=button onclick="return valid()">
																							
																									<INPUT type="button" value="�� ��" id=button1 name=button1 onclick="window.close()" class="button"> 
																							           
																							    </td> 
																							    </tr> 
																						</table>
																			    </td>
																			  </tr>							  
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

<script>

function unionSelect(){
	var type = document.getElementById("type");
	var nameFont = document.getElementById("nameFont");
	var db_nameTD = document.getElementById("db_nameTD");
	var db_nameInput = document.getElementById("db_nameInput");
	var category = document.getElementById("category");
	var port  = document.getElementById("port");
	if(type.value == 2){
		nameFont.style.display="inline";
		db_nameTD.style.display="none";
		db_nameInput.style.display="none";
	}else{
		nameFont.style.display="none";
		db_nameTD.style.display="inline";
		db_nameInput.style.display="inline";
		
	}
	var categoryvalue = "";
	var portvalue = "";
	if(type.value == 1){
		categoryvalue = 53;
		portvalue = 1521;
	}else if(type.value == 2){
		categoryvalue = 54;
		portvalue = 1433;
	}else if(type.value == 4){
		categoryvalue = 52;
		portvalue = 3306;
	}else if(type.value == 5){
		categoryvalue = 59;
		portvalue = 50000;
	}else if(type.value == 6){
		categoryvalue = 55;
		portvalue = 2638;
	}else if(type.value == 7){
		categoryvalue = 60;
		portvalue = 9088;
	}
	port.value = portvalue;
	category.value = categoryvalue;
}

unionSelect();

</script>

</HTML>