<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.indicators.model.NodeGatherIndicators"%>
<%@page import="java.util.List"%>

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
				mainForm.action = "<%=rootPath%>/nodeGatherIndicators.do?action=add";
				mainForm.submit();
			}
			function addCommon(){
				mainForm.action = "<%=rootPath%>/nodeGatherIndicators.do?action=addCommon";
				mainForm.submit();
			}
			function toChooseNode(){
				mainForm.action = "<%=rootPath%>/nodeGatherIndicators.do?action=showtomultiaddlist";
				mainForm.submit();
			}
			
			
			  
			
		</script>
		<script type="text/javascript">
		
  			var delAction = "<%=rootPath%>/nodeGatherIndicators.do?action=delete";
  			var listAction = "<%=rootPath%>/nodeGatherIndicators.do?action=list";
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
						<table>
							<tr>
								<td>
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
													<tr>
														<td align="left" width="5">
															<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																width="5" height="29" />
														</td>
														<td class="content-title">
															��Դ >> ���ܼ��� >> �豸�ɼ�ָ���б�
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
														<td>
															<table>
																<tr>
																	<td class="body-data-title" style="text-align: right;">
																		<a href="#" onclick="add()">���</a>&nbsp;&nbsp;&nbsp;
																		<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
																		<a href="#" onclick="addCommon()">���ͨ��ָ��</a>&nbsp;&nbsp;&nbsp;
																		<a href="#" onclick="toChooseNode()">����Ӧ��</a>&nbsp;&nbsp;&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<td class="body-data-title">
																		<INPUT type="checkbox" name="checkall"
																			onclick="javascript:chkall()">
																		���
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																	<td class="body-data-title">
																		������
																	</td>
																	<td class="body-data-title">
																		�Ƿ�ɼ�
																	</td>
																	<td class="body-data-title">
																		�Ƿ�����Ĭ������
																	</td>
																	<td class="body-data-title">
																		�ɼ����
																	</td>
																	<td class="body-data-title">
																		����
																	</td>
																</tr>
																<%
																    if (list != null && list.size() > 0) {
																        for (int i = 0; i < list.size(); i++) {
																            NodeGatherIndicators nodeGatherIndicators = (NodeGatherIndicators) list.get(i);

																            String unit = "";
																            if ("s".equals(nodeGatherIndicators.getInterval_unit())) {
																                unit = "��";
																            } else if ("m".equals(nodeGatherIndicators.getInterval_unit())) {
																                unit = "����";
																            } else if ("h".equals(nodeGatherIndicators.getInterval_unit())) {
																                unit = "Сʱ";
																            } else if ("d".equals(nodeGatherIndicators.getInterval_unit())) {
																                unit = "��";
																            } else if ("w".equals(nodeGatherIndicators.getInterval_unit())) {
																                unit = "��";
																            } else if ("mt".equals(nodeGatherIndicators.getInterval_unit())) {
																                unit = "��";
																            } else if ("y".equals(nodeGatherIndicators.getInterval_unit())) {
																                unit = "��";
																            }
																            String isCollection = "��";
																            if ("1".equals(nodeGatherIndicators.getIsCollection())) {
																                isCollection = "��";
																            }
																            String isDefault = "��";
																            if ("1".equals(nodeGatherIndicators.getIsDefault())) {
																                isDefault = "��";
																            }
																%>
																<tr align="center">
																	<td class="body-data-list">
																		<INPUT type="checkbox" name="checkbox"
																			value="<%=nodeGatherIndicators.getId()%>"><%=i + 1%></td>
																	<td class="body-data-list"><%=nodeGatherIndicators.getName()%></td>
																	<td class="body-data-list"><%=nodeGatherIndicators.getAlias()%></td>
																	<td class="body-data-list"><%=nodeGatherIndicators.getType()%></td>
																	<td class="body-data-list"><%=nodeGatherIndicators.getSubtype()%></td>
																	<td class="body-data-list"><%=isCollection%></td>
																	<td class="body-data-list"><%=isDefault%></td>
																	<td class="body-data-list"><%=nodeGatherIndicators.getPoll_interval() + " " + unit%></td>
																	<td class="body-data-list">
																		<input type="hidden" id="id" name="id"
																			value="<%=nodeGatherIndicators.getId()%>">
																		<img class="img"
																			src="<%=rootPath%>/resource/image/status.gif"
																			border="0" width=15>
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
														<TD nowrap colspan="20" align=center>
															<br>
															<input type="button" value="�� ��" style="width: 50"
																onclick="javascript:history.back(1)">&nbsp &nbsp
															<input type="button" value="�� ��" style="width: 50"
																onclick="window.close()">
														</TD>
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
				}]
			});	
			
		//�Ҽ��˵�������ʼ
			function edit(node){
				mainForm.action = "<%=rootPath%>/nodeGatherIndicators.do?action=edit&id=" + node;
				mainForm.submit();
			}
		//�Ҽ��˵���������
        </script>
</html>
