<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.config.model.Huaweitelnetconf"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="java.util.List"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	List list = (List) request.getAttribute("list");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">

		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/jquery/jquery-1.4.2.min.js"></script>


		<script language="JavaScript" type="text/JavaScript">
  Ext.onReady(function()
{  
$('#fragment-1').hide();
$('#fragment-2').hide();

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
}

);

function CreateWindow(url)
{
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=400,height=400,directories=no,status=no,scrollbars=no,menubar=no")
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
	setClass();
}

function setClass(){
	document.getElementById('scriptTitle-0').className='detail-data-title';
	
}
function firstFra(){
    document.getElementById('scriptTitle-0').className='detail-data-title';
	document.getElementById('scriptTitle-1').className='detail-data-title-out';
	document.getElementById('scriptTitle-2').className='detail-data-title-out';
	
	$('#fragment-0').show();
	$('#fragment-1').hide();
	$('#fragment-2').hide();
	//document.getElementById('fragment-0').style.display = "block";
    //document.getElementById('fragment-1').style.display = "none";
    //document.getElementById('fragment-2').style.display = "none";
}

function secondFra(){

   var command=$('#commands').val();
   if(command==null||command==""){
    alert("�������Ϊ�գ�");
    return;
   }
    document.getElementById('scriptTitle-0').className='detail-data-title-out';
	document.getElementById('scriptTitle-1').className='detail-data-title';
	document.getElementById('scriptTitle-2').className='detail-data-title-out';
	
	$('#fragment-0').hide();
	$('#fragment-1').show();
	$('#fragment-2').hide();
	//document.getElementById('fragment-0').style.display = "none";
    //document.getElementById('fragment-1').style.display = "block";
   // document.getElementById('fragment-2').style.display = "none";
}

 function exeCmd(){
 var ip=$("[name='checkbox'][checked]").val();
 if(ip==undefined||ip==""){
 alert("��ѡ���豸IP!!!");
 return;
 }
  Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
   mainForm.action="<%=rootPath%>/vpntelnetconf.do?action=exeCmd";
	mainForm.submit();
// var ip=$("[name='checkbox'][checked]").val();
// var commands=$("#commands").val();
// $.ajax({
//			type:"post",
//			dataType:"json",
//		url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=exeCmd",
//			data:"ip="+ip+"&commands="+commands+"&f="+Math.random(),
			
//			success:function(data){
//			 $('#fragment-1').hide();
//	         $('#fragment-2').show();
//	          $('#scriptTitle-2').removeClass('detail-data-title-out').addClass('detail-data-title');
//	          $('#scriptTitle-1').removeClass('detail-data-title').addClass('detail-data-title-out');
//               $("#content").val(data.content);
			
//		}
		
//		});
 }
function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
function loadFile(){
window.open("<%=rootPath%>/vpntelnetconf.do?action=loadFile","oneping", "height=500, width= 800, top=100, left=100,scrollbars=yes");		
}
function saveFile(){
var commands=$("#commands").text();
   if(commands==null||commands==""){
    alert("�������Ϊ�գ�");
    return;
   }
reg = new RegExp("(\r)","g");
var value=commands.replace(reg,";;") 
window.open("<%=rootPath%>/vpntelnetconf.do?action=saveFile&&commands="+value,"oneping", "height=300, width= 500, top=100, left=100,scrollbars=yes");
				}
</script>


	</head>
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0
			noResize scrolling=no src="<%=rootPath%>/include/calendar.htm"
			style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
			<div id="loading">
				<div class="loading-indicator">
					<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif"
						width="32" height="32" style="margin-right: 8px;" align="middle" />
					Loading...
				</div>
			</div>
			<div id="loading-mask" style=""></div>

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
								<td class="td-container-main-report">
									<table id="container-main-report" class="container-main-report">
										<tr>
											<td>

												<table id="report-content" class="report-content">
													<tr>
														<td>
															<table width="100%"
																background="<%=rootPath%>/common/images/right_t_02.jpg"
																cellspacing="0" cellpadding="0">
																<tr>
																	<td align="left">
																		<div class="noPrint">
																			<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																				width="5" height="29" />
																		</div>
																	</td>
																	<td class="layout_title">
																		<b>�Զ��� >> �Զ������� >> ִ�нű�����</b>
																	</td>
																	<td align="right">
																		<div class="noPrint">
																			<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																				width="5" height="29" />
																		</div>
																	</td>
																</tr>
															</table>
														</td>

													</tr>
													<tr>
														<td>
															<table id="report-content-header"
																class="report-content-header">
																<tr>
																	<td>
																		<%=scriptTitleTable%>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="report-content-body"
																class="report-content-body">
																<tr>
																	<td align=center>

																		<table id="report-data-header"
																			class="report-data-header">
																			<tr>
																				<td>

																					<table id="report-data-header-title"
																						class="report-data-header-title">
																						<tr>
																							<td>
																								&nbsp; <font color="#DC143C">ִ�в��裺1.�������� >> 2.ѡ���豸 >> 3.ִ�н��</font>
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
																		<div id="fragment-0" >
																			<table height="350">
																			<tr>
																					<td>
																					&nbsp;
																					</td>
																					<td>
																					&nbsp;	
																					</td>
																				</tr>
																				<tr>
																					<td width="15%">
																						&nbsp;&nbsp;�Ƿ��з��ؽ����
																					</td>
																					<td width="85%">
																						<select name="isReturn" id="isReturn">
																							<option value="0">
																								��
																							</option>
																							<option value="1">
																								��
																							</option>
																						</select>
																					</td>
																				</tr>
																<tr>
																    <td ></td>
																	<td align=center width="85%">

																		<table id="report-data-header"
																			class="report-data-header">
																			<tr>
																				<td>

																					<table id="report-data-header-title"
																						class="report-data-header-title">
																						<tr>
																						   
																							<td>
																								
																								&nbsp;<a href="#" onclick="loadFile()"><img src="<%=rootPath %>/resource/image/menu/load.gif"/>�����ļ�</a>
																								&nbsp;<a href="#" onclick="saveFile()"><img src="<%=rootPath %>/resource/image/menu/save.gif"/>�����ļ�</a>
																							</td>
																						</tr>
																					</table>
																				</td>
																			</tr>
																		</table>

																	</td>
																</tr>
																				<tr valign=top>
																					
																					<td width="15%">
																						&nbsp;&nbsp;������ű����
																					</td>
																					<td width="85%">
																						<textarea id="commands" name="commands" rows="10"
																							cols="85"></textarea>

																					</td>
																				</tr>

																				<tr>

																					<td colspan=2 align=center>
																						<br>
																						<input type="button" value="��һ��"
																							onclick="secondFra()">
																					</td>
																				</tr>
																				<tr>

																					<td colspan=2 align=center>
																						<br>
																						&nbsp;
																						<br><br><br><br>
																					</td>
																				</tr>
																			</table>

																		</div>
																	</td>
																</tr>
																<tr>
																	<td>

																		<div id="fragment-1">
																			<table cellspacing="0" border="1"
																				bordercolor="#ababab">
																				<tr height=28 style="background: #ECECEC"
																					align="center" class="content-title">
																					<td align="center">
																						<INPUT type="checkbox" name="checkall"
																							onclick="javascript:chkall()">

																					</td>
																					<td align="center">
																						���
																					</td>
																					<td align="center">
																						IP��ַ
																					</td>

																					<td align="center">
																						�豸����
																					</td>

																				</tr>
																				<%
																					Huaweitelnetconf vo = null;
																					for (int i = 0; i < list.size(); i++) {
																						vo = (Huaweitelnetconf) list.get(i);
																				%>
																				<tr <%=onmouseoverstyle%>>

																					<td align='center'>
																						<input type="checkbox" name='checkbox'
																							id='checkbox' value="<%=vo.getIpaddress()%>" />
																					</td>
																					<td align='center'>
																						<%=i + 1%>
																					</td>
																					<td align='center'>
																						<%=vo.getIpaddress()%>
																					</td>

																					<td align='center'><%=vo.getDeviceRender()%></td>

																				</tr>
																				<%
																					}
																				%>
																				<tr style="background-color: #ECECEC;">
																					<TD nowrap colspan="7" align=center>
																						<br>
																						<input type="button" style="width: 50" value="��һ��"
																							onclick="firstFra()">

																						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																						<input type="button" value="ִ ��" style="width: 50"
																							onclick="exeCmd()">


																					</TD>

																				</tr>


																			</table>

																		</div>

																		<div id="fragment-2">
																			<table>
																				<tr>
																					<td>
																						ִ�н����
																					</td>
																					<td>
																						&nbsp;
																						<textarea id="content" name="content" rows="32"
																							cols="140"></textarea>
																					</td>
																				</tr>
																				<tr>
																					<td colspan=2 align=center>

																						<input type="button" value="��һ��"
																							onclick="secondFra()">
																					</td>
																				</tr>
																			</table>

																		</div>

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