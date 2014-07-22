<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>

<%
    String rootPath = request.getContextPath();
    List list = (List) request.getAttribute("list");
    String nodeid = (String) request.getAttribute("nodeid");
    String type = (String) request.getAttribute("type");
    String subtype = (String) request.getAttribute("subtype");
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

		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
		 	
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
		<script language="JavaScript">
			function add(){
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=add";
				mainForm.submit();
			}
			
			function edit(node){
				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=edit&id=" + node;
				mainForm.submit();
			}
			
			function viewAlarmNode(){
				mainForm.action = "<%=rootPath%>/alarmIndicators.do?action=add";
				mainForm.submit();
			}
			
			
			function addmanage(node)
			{
				mainForm.action ="<%=rootPath%>/alarmIndicatorsNode.do?action=changeManage&value=1&id="+ node + "&nodeid=" + "<%=nodeid%>"; 
				mainForm.submit();
			}
			
			function cancelmanage(node)
			{
				mainForm.action ="<%=rootPath%>/alarmIndicatorsNode.do?action=changeManage&value=0&id="+ node + "&nodeid=" + "<%=nodeid%>";
				mainForm.submit();
				
			}
			function todelete(node)
			{
			if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
				mainForm.action ="<%=rootPath%>/alarmIndicatorsNode.do?action=delete&value=0&id="+ node + "&nodeid=<%=nodeid%>";
				mainForm.submit();
				}
			}
			
			  function toAdd()
  			{
      				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showtoaddlist";
      				mainForm.submit();
  			}
  			  function toMultiAdd()
  			{
      				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showtomultiaddlist";
      				mainForm.submit();
  			}
			
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="nodeid" id="nodeid" value="<%=nodeid%>">
			<input type="hidden" name="type" id="type" value="<%=type%>">
			<input type="hidden" name="subtype" id="subtype" value="<%=subtype%>">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content"
										class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
													<tr>
														<td align="left" width="5">
															<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																width="5" height="29" />
														</td>
														<td class="content-title">
															�澯 >> �澯���� >> �澯��ֵ�б�
														</td>
														<td align="right">
															<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																width="5" height="29" />
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="content-body" class="content-body">
													<tr>
														<td class="body-data-title" style="text-align: right">
															<a href="#" onclick="toAdd()">���</a>&nbsp;&nbsp;
															<a href="#" onclick="toMultiAdd()">����Ӧ��</a>&nbsp;&nbsp;
														</td>
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
																<tr>
																	<td class="body-data-title" width=5%>
																		<INPUT type="checkbox" class=noborder name="checkall"
																			onclick="javascript:chkall()" class=noborder>
																		���
																	</td>
																	<td class="body-data-title" width=10%>
																		����
																	</td>
																	<td class="body-data-title" width=8%>
																		����
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title">
																		������
																	</td>
																	<td class="body-data-title" width=7%>
																		��������
																	</td>
																	<td class="body-data-title">
																		��λ
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title">
																		�ȽϷ�ʽ
																	</td>
																	<td class="body-data-title">
																		һ����ֵ
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title" width=3%>
																		�澯
																	</td>
																	<td class="body-data-title">
																		������ֵ
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title" width=3%>
																		�澯
																	</td>
																	<td class="body-data-title">
																		������ֵ
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title" width=3%>
																		�澯
																	</td>
																	<td class="body-data-title" width=5%>
																		����
																	</td>
																</tr>
																<%
																    if (list != null && list.size() > 0) {
																        for (int i = 0; i < list.size(); i++) {
																            AlarmIndicatorsNode alarmIndicatorsNode = (AlarmIndicatorsNode) list.get(i);

																            String isMonitor = "��";
																            String bcolor = "gray";
																            if ("1".equals(alarmIndicatorsNode.getEnabled())) {
																                isMonitor = "��";
																                bcolor = "#3399CC";
																            }

																            //System.out.println(alarmIndicatorsNode.getDatatype()+ "====================");

																            String datatype = "�ַ���";
																            if ("Number".equals(alarmIndicatorsNode.getDatatype())) {
																                datatype = "����";
																            }

																            String sms0 = "��";
																            if ("1".equals(alarmIndicatorsNode.getSms0())) {
																                sms0 = "��";
																            }

																            String sms1 = "��";
																            if ("1".equals(alarmIndicatorsNode.getSms1())) {
																                sms1 = "��";
																            }

																            String sms2 = "��";
																            if ("1".equals(alarmIndicatorsNode.getSms2())) {
																                sms2 = "��";
																            }
																            String compare = "����";
																            String bgcol = "#ffffff";
																            if (alarmIndicatorsNode.getCompare() == 0) {
																                compare = "����";
																                bgcol = "gray";
																            }
																%>

																<tr>
																	<td class="body-data-list">
																		<INPUT type="checkbox" class=noborder name=checkbox
																			value="<%=alarmIndicatorsNode.getId()%>"
																			class=noborder>
																	</td>
																	<td class="body-data-list"><%=alarmIndicatorsNode.getName()%></td>
																	<td class="body-data-list" bgcolor="#33CC33"><%=alarmIndicatorsNode.getDescr()%></td>
																	<td class="body-data-list"><%=alarmIndicatorsNode.getType()%></td>
																	<td class="body-data-list"><%=alarmIndicatorsNode.getSubtype()%></td>
																	<td class="body-data-list"><%=datatype%></td>
																	<td class="body-data-list"><%=alarmIndicatorsNode.getThreshlod_unit()%></td>
																	<td class="body-data-list" bgcolor="<%=bcolor%>"><%=isMonitor%></td>
																	<td class="body-data-list" bgcolor="<%=bgcol%>"><%=compare%></td>
																	<td class="body-data-list" bgcolor="yellow"><%=alarmIndicatorsNode.getLimenvalue0()%></td>
																	<td class="body-data-list"><%=alarmIndicatorsNode.getTime0()%></td>
																	<td class="body-data-list"><%=sms0%></td>
																	<td class="body-data-list" bgcolor="orange"><%=alarmIndicatorsNode.getLimenvalue1()%></td>
																	<td class="body-data-list"><%=alarmIndicatorsNode.getTime1()%></td>
																	<td class="body-data-list"><%=sms1%></td>
																	<td class="body-data-list" bgcolor="red"><%=alarmIndicatorsNode.getLimenvalue2()%></td>
																	<td class="body-data-list"><%=alarmIndicatorsNode.getTime2()%></td>
																	<td class="body-data-list"><%=sms2%></td>
																	<td align="center" class="body-data-list">
																		<input type="hidden" id="id" name="id"
																			value="<%=alarmIndicatorsNode.getId()%>">
																		<img class="img"
																			src="<%=rootPath%>/resource/image/status.gif"
																			border="0" width=15>
																	</td>
																</tr>
																<%
																    }
																    }
																%>
																<tr>
																	<TD nowrap colspan="20" align=center>
																		<br>
																		<input type="button" value="�� ��" style="width: 50"
																			onclick="window.close()">
																	</TD>
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
															<table width="100%" border="0" cellspacing="0"
																cellpadding="0">
																<tr>
																	<td align="left" valign="bottom">
																		<img src="<%=rootPath%>/common/images/right_b_01.jpg"
																			width="5" height="12" />
																	</td>
																	<td></td>
																	<td align="right" valign="bottom">
																		<img src="<%=rootPath%>/common/images/right_b_03.jpg"
																			width="5" height="12" />
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
	<script type="text/javascript">  
			$('.img').contextmenu({
				height:35,
				width:100,
				items : [{
					text :'�༭',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						edit(id);
					}
				},{
					text :'ȡ���ɼ�',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelMng.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						cancelmanage(id);
					}
				},{
					text :'��Ӳɼ�',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addMn.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						addmanage(id);
					}
				},{
					text :'ɾ��',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancel.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						todelete(id);
					}
				}]
			});	
        </script>
</html>
