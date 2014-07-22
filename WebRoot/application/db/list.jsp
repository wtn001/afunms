<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.application.model.MonitorDBDTO"%>
<%@page import="java.util.List"%>


<%
    String rootPath = request.getContextPath();
    String menuTable = (String) request.getAttribute("menuTable");
    List list = (List) request.getAttribute("list");
    String flag = (String) request.getAttribute("flag");
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

			function toAdd(){
			     mainForm.action = "<%=rootPath%>/db.do?action=ready_add";
			     mainForm.submit();
			}
			function toDelete(){
			 if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
				mainForm.action = "<%=rootPath%>/db.do?action=delete";
				mainForm.submit();
			 }
			}  
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="flag" id="flag" value="<%=flag%>">
			<table id="body-container" class="body-container" height="100%">
				<tr>
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td><%=menuTable%></td>
							</tr>
						</table>
					</td>
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
															&nbsp;Ӧ�� &gt;&gt; ���ݿ���� &gt;&gt; ���ݿ��б�
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
														<td colspan="11">
															<table>
																<tr>
																	<td style="text-align: right;" class="body-data-title">
																		<b>չʾ��ʽ [<a
																			href="<%=rootPath%>/db.do?action=list&jp=1"> <font
																				color="#397DBD">�б�</font> </a> <a
																			href="<%=rootPath%>/databaseflex/list.jsp?flag=<%=flag%>"><font
																				color="#397DBD">ͼ��</font> </a>]</b>&nbsp;&nbsp;
																		<a href="#" onclick="toAdd()">���</a>
																		<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td align="center" class="body-data-title">
															���
														</td>
														<td align="center" class="body-data-title">
															����
														</td>
														<td align="center" class="body-data-title">
															����
														</td>
														<td align="center" class="body-data-title">
															���ݿ���
														</td>
														<td align="center" class="body-data-title">
															IP��ַ
														</td>
														<td align="center" class="body-data-title">
															�˿�
														</td>
														<td align="center" class="body-data-title">
															����״̬
														</td>
														<td align="center" class="body-data-title">
															״̬
														</td>
														<td align="center" class="body-data-title">
															�澯
														</td>
														<td align="center" class="body-data-title">
															������
														</td>
														<td align="center" class="body-data-title">
															����
														</td>
													</tr>
													<%
													    if (list != null && list.size() > 0) {

													        for (int i = 0; i < list.size(); i++) {
													            MonitorDBDTO monitorDBDTO = (MonitorDBDTO) list.get(i);
													            Hashtable eventListSummary = monitorDBDTO.getEventListSummary();
													            String generalAlarm = (String) eventListSummary.get("generalAlarm");
													            String urgentAlarm = (String) eventListSummary.get("urgentAlarm");
													            String seriousAlarm = (String) eventListSummary.get("seriousAlarm");

													            String status = monitorDBDTO.getStatus();

													            String statusImg = "";
													            if ("1".equals(status)) {
													                statusImg = "alarm_level_1.gif";
													            } else if ("2".equals(status)) {
													                statusImg = "alarm_level_2.gif";
													            } else if ("3".equals(status)) {
													                statusImg = "alert.gif";
													            } else {
													                statusImg = "status_ok.gif";
													            }
													%>
													<tr>
														<td align="center" class="body-data-list">
															<INPUT type="radio" class=noborder name=radio
																value="<%=monitorDBDTO.getId()%>"><%=i + 1%></td>
														<td align="center" class="body-data-list"><%=monitorDBDTO.getAlias()%></td>
														<td align="center" class="body-data-list"><%=monitorDBDTO.getDbtype()%></td>
														<td align="center" class="body-data-list"><%=monitorDBDTO.getDbname()%></td>
														<td align="center" class="body-data-list"><%=monitorDBDTO.getIpAddress()%></td>
														<td align="center" class="body-data-list"><%=monitorDBDTO.getPort()%></td>
														<td align="center" class="body-data-list"><%=monitorDBDTO.getMon_flag()%></td>
														<td align="center" class="body-data-list">
															<img
																src="<%=rootPath%>/resource/image/topo/<%=statusImg%>">
														</td>
														<td align="center" class="body-data-list">
															<table>
																<tr>
																	<td align="center">
																		<table border=1 height=15 bgcolor=#ffffff>
																			<tr>
																				<td width="30%" bgcolor="#ffff00" align="center"><%=generalAlarm%></td>
																				<td width="30%" bgcolor="orange" align="center"><%=urgentAlarm%></td>
																				<td width="30%" bgcolor="#ff0000" align="center"><%=seriousAlarm%></td>
																			</tr>
																		</table>
																	</td>
																</tr>
															</table>
														</td>
														<td align="center" class="body-data-list"><%=monitorDBDTO.getPingValue()%></td>
														<td align="center" class="body-data-list">
															<input type="hidden" id="sid" name="sid"
																value="<%=monitorDBDTO.getSid()%>">
															<input type="hidden" id="id" name="id"
																value="<%=monitorDBDTO.getId()%>">
															<img class="img"
																src="<%=rootPath%>/resource/image/status.gif" border="0"
																width=15 alt="�Ҽ�����">
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
				height:115,
				width:100,
				items : [{
					text :'�޸���Ϣ',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						var sid=$($(target).parent()).find('#sid').val();
						edit(id,sid);
					}
				},{
					text :'������Ϣ',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						var sid=$($(target).parent()).find('#sid').val();
						detail(id,sid);
					}
				},{
                    text :'ȡ������',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelMng.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                    	var sid=$($(target).parent()).find('#sid').val();
                        cancelmanage(id,sid);
                    }
                },{
                    text :'��Ӽ���',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addMn.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                    	var sid=$($(target).parent()).find('#sid').val();
                        addmanage(id,sid);
                    }
                }]
			});	
			
		//�Ҽ��˵�������ʼ
			function detail(node,sid)
			{
			    mainForm.action="<%=rootPath%>/db.do?action=check&id="+node+"&sid="+sid;
			    mainForm.submit();
			}
			function edit(node,sid)
			{
				 mainForm.action="<%=rootPath%>/db.do?action=ready_edit&id="+node+"&sid="+sid;
				 mainForm.submit();
			}
			function cancelmanage(node,sid)
			{
				 mainForm.action="<%=rootPath%>/db.do?action=cancelmanage&id="+node+"&sid="+sid;
				 mainForm.submit();
			}
			function addmanage(node,sid)
			{
				 mainForm.action="<%=rootPath%>/db.do?action=addmanage&id=" + node+ "&sid=" + sid;
		mainForm.submit();
	}
	//�Ҽ��˵���������
</script>
</html>
