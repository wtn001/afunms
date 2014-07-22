<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.model.*"%>
<%
    String rootPath = request.getContextPath();
    List list = (List) request.getAttribute("list");
    if (list == null)
        list = new ArrayList();
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
		<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/iislog.do?action=delete";
  var listAction = "<%=rootPath%>/iislog.do?action=list";

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/iislog.do?action=ready_add";
     mainForm.submit();
  }
  function toDelete()
  {
  if(confirm('是否确定删除这条记录?')) {
     mainForm.action = "<%=rootPath%>/iislog.do?action=delete";
     mainForm.submit();
     }
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

}

</script>
	</head>
	<BODY onload="initmenu();">

		<form method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td width="200" valign=top align=center>
						<%=menuTable%>

					</td>
					<td align="center" valign=top>
						<table style="width: 98%" cellpadding="0" cellspacing="0"
							align="center">
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
												&nbsp;应用 >> 中间件管理 >> IIS日志监视列表
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
												<a href="#" onclick="toAdd()">添加</a>
												<a href="#" onclick="toDelete()">删除</a>&nbsp;&nbsp;&nbsp;
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td colspan="2">
									<table cellspacing="0" cellpadding="0" width="100%">
										<tr height=28>
											<td align="center" class="body-data-title">
												<INPUT type="checkbox" class=noborder name="checkall"
													onclick="javascript:chkall()">
											</td>
											<td align="center" class="body-data-title">
												序号
											</td>
											<td align="center" class="body-data-title">
												名称
											</td>
											<td align="center" class="body-data-title">
												IP地址
											</td>
											<td align="center" class="body-data-title">
												是否监控
											</td>
											<td align="center" class="body-data-title">
												操作
											</td>
										</tr>
										<%
										    if (list.size() > 0) {
										        for (int i = 0; i < list.size(); i++) {
										            IISLogConfig vo = (IISLogConfig) list.get(i);
										%>
										<tr bgcolor="#FFFFFF">
											<td align="center" class="body-data-list">
												&nbsp;
												<INPUT type="checkbox" class=noborder name=checkbox
													value="<%=vo.getId()%>">
											</td>
											<td align="center" class="body-data-list">
												&nbsp;
												<font color='blue'><%=1 + i%></font>
											</td>
											<td align="center" class="body-data-list">
												&nbsp;<%=vo.getName()%></td>
											<td align="center" class="body-data-list">
												&nbsp;<%=vo.getIpaddress()%></td>
											<td align="center" class="body-data-list">
												<%
												    if (vo.getFlag() == 0) {
												%>
												&nbsp;未监视
												<%
												    } else {
												%>
												&nbsp;已监视
												<%
												    }
												%>
											</td>
											<td align="center" class="body-data-list">
												<input type="hidden" id="id" name="id"
													value="<%=vo.getId()%>">
												<img class="img"
													src="<%=rootPath%>/resource/image/status.gif" border="0"
													width=15 alt="右键操作">
											</td>
										</tr>
										<%
										    }
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
	</BODY>
	<script language="JavaScript">
	$('.img').contextmenu({
		height:115,
		width:100,
		items : [{
			text :'修改信息',
			icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
			action: function(target){
				var id=$($(target).parent()).find('#id').val();
				edit(id);
			}
		},{
			text :'监视信息',
			icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif',
			action: function(target){
				var id=$($(target).parent()).find('#id').val();
				detail(id);
			}
		},{
                  text :'取消监视',
                  icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelMng.gif',
                  action: function(target){
                  	var id=$($(target).parent()).find('#id').val();
                  	cancelalert(id);
                  }
              },{
                  text :'添加监视',
                  icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addMn.gif',
                  action: function(target){
                  	var id=$($(target).parent()).find('#id').val();
                      addalert(id);
                  }
              }]
	});	
	
//右键菜单方法开始
	function detail(ipaddress)
	{
	    location.href="<%=rootPath%>/iislog.do?action=todaylist&ipaddress="+ipaddress;
	}
	function edit(node)
	{
		location.href="<%=rootPath%>/iislog.do?action=ready_edit&id="+node;
	}
	function cancelalert(node)
	{
		location.href="<%=rootPath%>/iislog.do?action=cancelalert&id="+node;
	}
	function addalert(node)
	{
		location.href="<%=rootPath%>/iislog.do?action=addalert&id="+node;
	}
</script>
</HTML>
