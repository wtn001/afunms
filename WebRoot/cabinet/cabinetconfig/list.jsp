<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>

<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.cabinet.model.MachineCabinet"%>

<%@page import="java.util.List"%>
<%@page import="java.util.Hashtable"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	List list = (List) request.getAttribute("list");
	Hashtable roomhash = (Hashtable) request.getAttribute("roomhash");
	Object obj = request.getAttribute("isTreeView");
	JspPage jp = (JspPage) request.getAttribute("page");
	if (roomhash == null)
		roomhash = new Hashtable();
%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/application/environment/resource/ext3.1/resources/css/ext-all.css" />
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all-debug.js"></script>

		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/maccabinet.do?action=delete";
  			var listAction = "<%=rootPath%>/maccabinet.do?action=list";
		</script>
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
		<script>
			function toAdd(){
				Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
				mainForm.action = "<%=rootPath%>/maccabinet.do?action=ready_add"; 
				mainForm.submit();
			}
		</script>
		<script language="JavaScript">
			//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,ip,showItemMenu)
			{	
				ipaddress=ip;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        return false;
			    }
			    else{
			    	popMenu(itemMenu,100,showItemMenu);
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    
			}
			/**
			*��ʾ�����˵�
			*menuDiv:�Ҽ��˵�������
			*width:����ʾ�Ŀ��
			*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //���������˵�
			    var pop=window.createPopup();
			    //���õ����˵�������
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //��õ����˵�������
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //ѭ������ÿ�е�����
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //������ø��в���ʾ����������һ
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //�����Ƿ���ʾ����
			        rowObjs[i].style.display=(hide)?"none":"";
			        //������껬�����ʱ��Ч��
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#99CCFF";
			            this.style.color="white";
			        }
			        //������껬������ʱ��Ч��
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //���β˵��Ĳ˵�
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //ѡ���Ҽ��˵���һ��󣬲˵�����
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //��ʾ�˵�
			    pop.show(event.clientX-1,event.clientY,width,rowCount*30,document.body);
			    return true;
			}
			
			<%
			if(null != obj && "1".equals((String)obj)){
			%>
			function toDetail(){
				mainForm.action = "<%=rootPath%>/maccabinet.do?action=toDetail&id=" + node + "&isTreeView=1";
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/maccabinet.do?action=ready_edit&id=" + node + "&isTreeView=1";
				mainForm.submit();
			}
			<%}else{%>
			function toDetail(){
				mainForm.action = "<%=rootPath%>/maccabinet.do?action=toDetail&id=" + node;
				mainForm.submit();
			}
			
			function edit(){
				mainForm.action = "<%=rootPath%>/maccabinet.do?action=ready_edit&id=" + node;
				mainForm.submit();
			}
			<%}%>

			function toDelete(){
			if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
				mainForm.action = "<%=rootPath%>/maccabinet.do?action=delete";
				mainForm.submit();
			  }
			}
	
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1" style="border: thin; font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center" onclick="parent.edit()">�༭</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center" onclick="parent.toDetail()">�鿴��ϸ��Ϣ</td>
				</tr>
			</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<%
						if (null == obj) {
					%>
					<td class="td-container-menu-bar">
						<table id="container-menu-bar" class="container-menu-bar">
							<tr>
								<td><%=menuTable%></td>
							</tr>
						</table>
					</td>
					<%
						} else {
							String isTreeView = (String) obj;
					%>
					<input type="hidden" name="isTreeView" value="<%=isTreeView%>">
					<%
						}
					%>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
													<tr>
														<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
														<td class="content-title">3D���� &gt;&gt; ����������� &gt;&gt; �����б�</td>
														<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td>
												<table id="content-body" class="content-body">
													<tr>
														<td style="text-align: right" colspan="12" class="body-data-title">
															<a href="#" onclick="toAdd()">���</a>&nbsp;&nbsp;&nbsp;
															<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
														</td>
													</tr>
													<tr>
														<td class="body-data-title" colspan="12">
															<table width="100%" cellpadding="0" cellspacing="1">
																<tr>
																	<td width="80%" align="center">
																		<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td align="center" class="body-data-title"><INPUT type="checkbox" id="checkall" name="checkall"onclick="javascript:chkall()">���</td>
														<td align="center" class="body-data-title">����</td>
														<td align="center" class="body-data-title">��������</td>
														<td align="center" class="body-data-title">��������λ��(x,y,z)</td>
														<td align="center" class="body-data-title">U����</td>
														<td align="center" class="body-data-title">���ܹ��</td>
														<td align="center" class="body-data-title">��Դ����</td>
														<td align="center" class="body-data-title">�߶�</td>
														<td align="center" class="body-data-title">���</td>
														<td align="center" class="body-data-title">���</td>
														<td align="center" class="body-data-title">������</td>
														<td align="center" class="body-data-title">����</td>
													</tr>
													<%
														String room = "";
														if (list != null && list.size() > 0) {
															for (int i = 0; i < list.size(); i++) {
																MachineCabinet machineCabinet = (MachineCabinet) list
																		.get(i);
																String motoroom = machineCabinet.getMotorroom();
																if (roomhash.containsKey(motoroom))
																	room = (String) roomhash.get(motoroom);

																String machine = "(" + machineCabinet.getMachinex() + ","
																		+ machineCabinet.getMachiney() + ","
																		+ machineCabinet.getMachinez() + ")";
													%>
													<tr <%=onmouseoverstyle%>>
														<td align="center" class="body-data-list"><INPUT type="checkbox" name="checkbox"value="<%=machineCabinet.getId()%>"><%=i + jp.getStartRow()%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getName()%></td>
														<td align="center" class="body-data-list"><%=room%></td>
														<td align="center" class="body-data-list"><%=machine%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getUselect()%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getStandards()%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getPowers()%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getHeights()%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getWidths()%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getDepths()%></td>
														<td align="center" class="body-data-list"><%=machineCabinet.getNos()%></td>
														<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/image/status.gif"border="0" width=15 oncontextmenu=showMenu('2','<%=machineCabinet.getId()%>','','1111') title="�Ҽ��˵�"></td>
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
		</form>
	</body>
</html>
