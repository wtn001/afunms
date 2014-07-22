<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.jbossmonitor.*"%>
<%
    List list = (List) request.getAttribute("list");
    String rootPath = request.getContextPath();
%>
<%
    String menuTable = (String) request.getAttribute("menuTable");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link
			href="<%=rootPath%>/common/contextmenu/skins/default/contextmenu.css"
			rel="stylesheet">
		<script src="<%=rootPath%>/common/contextmenu/js/jquery-1.8.2.min.js"
			type="text/javascript"></script>
		<script src="<%=rootPath%>/common/contextmenu/js/contextmenu.js"
			type="text/javascript"></script>

		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<script language="javascript">
  var delAction = "<%=rootPath%>/apache.do?action=delete";
  var listAction = "<%=rootPath%>/apache.do?action=list";
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/apache.do?action=ready_add";
     mainForm.submit();
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

}

</script>
	</head>
	<BODY onload="initmenu();">
		<form method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td width="200" valign=top align=center><%=menuTable%></td>
					<td align="center" valign=top>
						<table style="width: 98%" cellpadding="0" cellspacing="0"
							algin="center">
							<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table id="content-header" class="content-header">
										<tr>
											<td align="left" width="5">
												<img src="<%=rootPath%>/common/images/right_t_01.jpg"
													width="5" height="29" />
											</td>
											<td class="content-title">
												&nbsp;Ӧ�� >> �м������ >> Apache�����б�
											</td>
											<td align="right">
												<img src="<%=rootPath%>/common/images/right_t_03.jpg"
													width="5" height="29" />
											</td>
										</tr>
									</table>
								</td>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="0">
										<tr>
											<td width="100%" style="text-align: right;"
												class="body-data-title">
												<a href="#" onclick="toAdd()">���</a>
												<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table cellspacing="0" cellpadding="0" width="100%">
										<tr class="microsoftLook0" height=28>

											<td align="center" class="body-data-title">
												<INPUT type="checkbox" class=noborder name="checkall"
													onclick="javascript:chkall()">
												���
											</td>
											<td align="center" class="body-data-title">
												����
											</td>
											<td align="center" class="body-data-title">
												IP��ַ
											</td>
											<td align="center" class="body-data-title">
												�˿�
											</td>
											<td align="center" class="body-data-title">
												�Ƿ���
											</td>
											<td align="center" class="body-data-title">
												����
											</td>
										</tr>
										<%
										    ApacheConfig vo = null;
										    HttpClientJBoss apache = new HttpClientJBoss();
										    for (int i = 0; i < list.size(); i++) {
										        vo = (ApacheConfig) list.get(i);
										%>
										<tr bgcolor="#ffffff" height=25>
											<td align="center" class="body-data-list">
												<INPUT type="checkbox" name=checkbox class=noborder
													value="<%=vo.getId()%>">
												<font color='blue'><%=1 + i%></font>
											</td>
											<td align="center" class="body-data-list"><%=vo.getAlias()%></td>
											<td align="center" class="body-data-list"><%=vo.getIpaddress()%></td>
											<td align="center" class="body-data-list"><%=vo.getPort()%></td>

											<%
											    if (vo.getFlag() == 0) {
											%>
											<td align="center" class="body-data-list">
												��
											</td>
											<%
											    } else {
											%>
											<td align="center" class="body-data-list">
												��
											</td>
											<%
											    }
											%>
											<td align="center" class="body-data-list">
												<input type="hidden" id="id" name="id"
													value="<%=vo.getId()%>">
												<img class="img"
													src="<%=rootPath%>/resource/image/status.gif" border="0"
													width=15 alt="�Ҽ�����">
											</td>
										</tr>
										<%
										    ;
										    }
										%>
									</table>
								</td>
							</tr>
							<tr>
								<td background="<%=rootPath%>/common/images/right_b_02.jpg">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_01.jpg"
													width="5" height="11" />
											</td>
											<td></td>
											<td align="right" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_03.jpg"
													width="5" height="11" />
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
	<script type="text/javascript">  
			$('.img').contextmenu({
				height:115,
				width:100,
				items : [{
					text :'�޸���Ϣ',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						edit(id);
					}
				},{
					text :'������Ϣ',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						detail1(id);
					}
				},{
                    text :'ȡ������',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelMng.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                    	cancelmanage(id);
                    }
                },{
                    text :'��Ӽ���',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addMn.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        addmanage(id);
                    }
                }]
			});	
			
		//�Ҽ��˵�������ʼ
	function detail1(node)
	{
	    location.href="<%=rootPath%>/apache.do?action=server&id="+node;
	}
	function edit(node)
	{
		location.href="<%=rootPath%>/apache.do?action=ready_edit&id="+node;
	}
	function cancelmanage(node)
	{
		location.href="<%=rootPath%>/apache.do?action=cancelalert&id="+node;
	}
	function addmanage(node)
	{
		location.href="<%=rootPath%>/apache.do?action=addalert&id=" + node;
	}
	//�Ҽ��˵���������
</script>
</HTML>
