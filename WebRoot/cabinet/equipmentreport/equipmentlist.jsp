<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@ include file="/include/globe.inc"%>

<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.cabinet.model.EquipmentReport"%>
<%@page import="com.afunms.cabinet.model.EqpRoom"%>
<%@page import="com.afunms.cabinet.model.MachineCabinet"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Hashtable"%>

<%
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	List list = (List) request.getAttribute("list");
	String roomname = (String) session.getAttribute("roomname");
	String cabinetname = (String) session.getAttribute("cabinetname");
	String uselect = (String) session.getAttribute("uselect");
	String unumber = (String) session.getAttribute("unumber");
	String rate = (String) session.getAttribute("rate");
	String roomid = (String) session.getAttribute("roomid");
	String cabinetid = (String) session.getAttribute("cabinetid");
	List roomlist = (List) request.getAttribute("roomlist");
	List cabinetlist = (List) request.getAttribute("cabinetlist");
	String findselect=(String)session.getAttribute("findselect");
	
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
		<script>
    function backList()
   {  
     mainForm.action = "<%=rootPath%>/equipmentreport.do?action=list";
     mainForm.submit();
    }
       function doQuery()
   {  
     mainForm.action = "<%=rootPath%>/equipmentreport.do?action=querylist";
     mainForm.submit();
    }
		</script>
<script type="text/javascript"> 
     var req; 
     window.onload=function() 
     {//ҳ�����ʱ�ĺ��� 
     } 

     function Change_Select(){//����һ���������ѡ����ı�ʱ���øú��� 
       var room = document.getElementById('room').value; 
       var url = "select?id="+ escape(room);
       if(window.XMLHttpRequest){ 
         req = new XMLHttpRequest(); 
       }else if(window.ActiveXObject){
         req = new ActiveXObject("Microsoft.XMLHTTP"); 
       } 
       if(req){ 
         req.open("GET",url,true); 
         //ָ���ص�����Ϊcallback 
         req.onreadystatechange = callback; 
         req.send(null); 
       } 
     } 
     //�ص����� 
     function callback(){ 
       if(req.readyState ==4){ 

         //if(req.status ==200){ 

           parseMessage();//����XML�ĵ� 
        // }else{ 
         //  alert("���ܵõ�������Ϣ:" + req.statusText); 
        // } 
       } 
     } 
     //��������xml�ķ��� 
     function parseMessage(){ 
    
       var xmlDoc = req.responseXML.documentElement;//��÷��ص�XML�ĵ� 
       var xSel = xmlDoc.getElementsByTagName('select'); 
       //���XML�ĵ��е�����<select>��� 
       var select_root = document.getElementById('cabinet'); 
       //�����ҳ�еĵڶ��������� 
       select_root.options.length=0; 
       //ÿ�λ���µ����ݵ�ʱ���Ȱ�ÿ����������ܵĳ�����0 
       for(var i=0;i<xSel.length;i++){ 
         var xValue = xSel[i].childNodes[0].firstChild.nodeValue; 

         //���ÿ��<select>����еĵ�һ����ǵ�ֵ,Ҳ����<value>��ǵ�ֵ 
         var xText = xSel[i].childNodes[1].firstChild.nodeValue; 
         //���ÿ��<select>����еĵڶ�����ǵ�ֵ,Ҳ����<text>��ǵ�ֵ 

         var option = new Option(xText, xValue); 
         //����ÿ��value��text��ǵ�ֵ����һ��option���� 

         try{ 
           select_root.add(option);//��option������ӵ��ڶ����������� 
         }catch(e){ 
         } 
       } 
     }       
   </script> 


	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
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
								<td class="td-container-main-content">
									<table id="container-main-content"
										class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
													<tr>
														<td class="content-title">
															3D���� &gt;&gt; �������� &gt;&gt; ����ʹ�ñ���
														</td>
														<td align="right">
															<img src="<%=rootPath%>/common/images/right_t_03.jpg"
																width="5" />
														</td>
													</tr>
													<tr>
														<td class="content-title" colspan=3>
															&nbsp;����:<select id="room" name="room" style="width: 150px" onChange="Change_Select()"> 
															<option value="0" selected>--��ѡ��--</option>
													<%
														if (roomlist != null) {
															for (int i = 0; i < roomlist.size(); i++) {
															
																String rname = ((EqpRoom) roomlist.get(i)).getName();
																int rid = ((EqpRoom) roomlist.get(i)).getId();
													%>
													<option value="<%=rid%>"><%=rname%>
													<%
														}
														}
													%>
												
												</select>
													
													        ����:<select id="cabinet" name="cabinet" style="width: 150px">
										                          <option value="0">--��ѡ��--</option> 
										
												</select>
													
															<input type="button" value="�� ѯ"
																style="width: 50; margin-left: 10" id="process"
																onclick="doQuery()">
															<a
																href="<%=rootPath%>/equipmentreport.do?action=downexlreport"
																target="_blank"><img name="xls" alt='����EXCEL'
																	style="CURSOR: hand"
																	src="<%=rootPath%>/resource/image/export_excel.gif"
																	width=18 border="0">����EXCEL</a>
															<a
																href="<%=rootPath%>/equipmentreport.do?action=downwordreport"
																target="_blank"><img name="doc" alt='����WORD'
																	style="CURSOR: hand"
																	src="<%=rootPath%>/resource/image/export_word.gif"
																	width=18 border="0">����WORD</a>
															<a
																href="<%=rootPath%>/equipmentreport.do?action=downpdfreport"
																target="_blank"><img name="pdf" alt='����PDF'
																	style="CURSOR: hand"
																	src="<%=rootPath%>/resource/image/export_pdf.gif"
																	width=18 border="0">����PDF</a>

														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr style="align:center;width:100%">
											<td >
												<table id="content-body" class="content-body">
													<tr>
														<td width="650" style="padding: 23px, 90px, 80px, 90px;width:50%">
															<table width="100%" align="center" border="1"
																bordercolor="#000000" cellspacing="0" cellpadding="0"
																bgcolor="#ffffff">
																<tr height=40>
																	<td colspan="6" align="center" style="font-size: 16px;">
																		<B><%= roomname%>����<%= cabinetname%>�Ż���ʹ����ϸ�� 
																	</td>
																</tr>
																<tr height=30>
																	<td colspan="3">
																		<B>����������</B>
																		<%= roomname%>
																	</td>
																	<td colspan="3">
																		<B>�����ţ�</B>
																		<%= cabinetname%>
																	</td>
																</tr>
																<tr height=30>
																	<td colspan="2">
																		<B>U������</B>
																		<%= uselect%>
																	</td>
																	<td colspan="2">
																		<B>��ʹ�ã�</B>
																		<%= unumber%>
																	</td>
																	<td colspan="2">
																		<B>ʹ���ʣ�</B>
																		<%= rate%>
																	</td>
																</tr>
																<tr height=30>
																	<td width=50 align="center">
																		<B>U��� 
																	</td>
																	<td align="center">
																		<B>�豸���� 
																	</td>
																	<td align="center">
																		<B>�豸���� 
																	</td>
																	<td align="center">
																		<B>����ҵ�� 
																	</td>
																	<td align="center">
																		<B>��ϵ�� 
																	</td>
																	<td align="center">
																		<B>��ϵ�绰 
																	</td>
																</tr>
																<%
											if (list != null && list.size() > 0) {
												for (int i = 0; i < list.size(); i++) {
													EquipmentReport vo = (EquipmentReport) list.get(i);
										%>
																<tr height=30
																	style="background-color: <% if(i%2==0){%>#ECECEC;<%}else{%>#ffffff;<%} %>">
																	<td width=40 align="center">
																		<%=i + 1%>
																	</td>
																	<td align="center">
																		<%=vo.getEquipmentname()%>
																	</td>
																	<td align="center">
																		<%=vo.getEquipmentdesc()%>
																	</td>
																	<td align="center">
																		<%=vo.getOperation()%>
																	</td>
																	<td align="center">
																		<%=vo.getContactname()%>
																	</td>
																	<td align="center">
																		<%=vo.getContactphone()%>
																	</td>
																</tr>
																<%
											}
											}
										%>
												<%
											if (( list.size() -15)<0) {
												for (int i = list.size(); i <(15- list.size()); i++) {
													
										%>					
										<tr height=30 style="background-color: <% if(i%2==0){%>#ECECEC;<%}else{%>#ffffff;<%} %>">
																	<td width=40>
																		&nbsp;
																	</td>
																	<td>
																		&nbsp;
																	</td>
																	<td>
																		&nbsp;
																	</td>
																	<td>
																		&nbsp;
																	</td>
																	<td>
																		&nbsp;
																	</td>
																	<td>
																		&nbsp;
																	</td>
																</tr>
																<%
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
