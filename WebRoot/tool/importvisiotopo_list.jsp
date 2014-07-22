<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	List list = (List)request.getAttribute("list");
	Object success = request.getAttribute("success");
	
%>
<html>
	<head>
		<title>����Visio�豸����</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
		<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
		<script language="JavaScript" type="text/javascript">
	
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     var chk = checkinput("fileName","string","�ϴ��ļ� ����Ϊ��",100,false);
     var pattern=new RegExp(/.+\.vs[dt]$/i);
     var filename = document.getElementById("fileName").value;
     if(!pattern.test(filename)){
     	alert("����ı�������vsd��vstΪ��չ����visio�ļ�!");
     	return;
     }
      if(chk)
     {      
            Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
        	mainForm.action = "<%=rootPath%>/macconfig.do?action=importVisioTopo&flag=2";
        	mainForm.submit();
     }
 
 });	
  Ext.get("processreset").on("click",function(){
        	mainForm.action = "<%=rootPath%>/tool/importvisiotopo.jsp";
        	mainForm.submit();
     
     
 });
	
});
//-- nielin modify at 2010-01-04 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


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

	if(<%=success%> == null || <%=success%>=='undefined'){
		var div = document.getElementById('upload');
		div.style.display = 'block';
		div = document.getElementById('result');
		div.style.display = 'none';
	}else{
		document.getElementById('result').style.display = ''; 
		document.getElementById('upload').style.display = 'none';
	}
}

</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm" enctype="multipart/form-data" >
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
																	<td class="add-content-title">���� &gt;&gt; ����Visio�豸����
																	</td>
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
																		<div id="upload">
																			<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																				<tr style="background-color: #ECECEC;" height="24">
																					<td align="center">
																						<input type="file" id="fileName" name="fileName" style="height: 20px">
																					</td>
																				</tr>			
																				<tr>
																					<TD nowrap colspan="4" align=center>
																						<br>
																						<input type="button" value="ȷ ��" style="width: 50" id="process" onclick="#">&nbsp;&nbsp;
																						<input type="button" value="�� ��"  style="width: 50" id="processreset" onclick="#" >
																					</TD>
																				</tr>
																			</TABLE>
																		</div>
																		<div id="result">
																			<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																				<tr>
																					<TD nowrap colspan="4" align=center>
																						<br>
																						<%
																							if (null != success) {
																								if ((Boolean) success) {
																									out.print("����ɹ���");
																								} else
																									out.print("����ʧ�ܣ���������ͼ�Ƿ������ͼ�淶<br>������һ����������ʹ�ô��ļ��������޷����ʡ���");
																							}
																						%>
																						<br>
																						<input type="reset" style="width: 50" value="����" onclick="javascript:history.back(1)">
																						<input type="reset" style="width: 50" value="�ر�" onclick="javascript:window.close();">
																					</TD>
																				</tr>
																			</TABLE>
																		</div>
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
																				<td align="left" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" />
																				</td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" />
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
		</form>
	</body>
</HTML>