<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>
<%@page import="java.util.Hashtable"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>

<%
    String rootPath = request.getContextPath();
    String menuTable = (String) request.getAttribute("menuTable");
    String nodeid = (String) request.getAttribute("nodeid");
    String type = (String) request.getAttribute("type");
    String subtype = (String) request.getAttribute("subtype");
    List allNodeDTOlist = (List) request.getAttribute("allNodeDTOlist");
    List<AlarmIndicatorsNode> list = (List<AlarmIndicatorsNode>) request.getAttribute("list");
    Hashtable<String, NodeDTO> nodeDTOHashtable = (Hashtable<String, NodeDTO>) request.getAttribute("nodeDTOHashtable");

    JspPage jp = (JspPage) request.getAttribute("page");
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
				var type = document.getElementById("type");
				var subtype = document.getElementById("subtype");
				if(type.value!="-1" && subtype.value !="-1" ){
					mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showAdd&jspFlag=add";
				mainForm.submit();
				}
				if(type.value=="-1"){
					alert("��ѡ������!");
				}
				if(subtype.value=="-1"){
					alert("��ѡ��������!");
				}
			}
			function toChooseNode(){
				var nodeid = document.getElementById("nodeid");
				var type = document.getElementById("type");
				var subtype = document.getElementById("subtype");
				if(type.value!="-1" && subtype.value !="-1" && nodeid.value !="-1"){
					mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showAdd&jspFlag=multi";
					mainForm.submit();
				}
				if(type.value=="-1"){
					alert("��ѡ������!");
				}
				if(subtype.value=="-1"){
					alert("��ѡ��������!");
				}
				if(nodeid.value=="-1"){
					alert("��ѡ���豸!");
				}
				
			}
			function search(){
				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showlist";
				mainForm.submit();
			}
		</script>
		<script type="text/javascript">
			var curpage= <%=jp.getCurrentPage()%>;
  			var totalpages = <%=jp.getPageTotal()%>;
  			var delAction = "<%=rootPath%>/alarmIndicatorsNode.do?action=showDelete";
  			var listAction = "<%=rootPath%>/alarmIndicatorsNode.do?action=showlist";
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
														<td align="left" width="5">
															<img src="<%=rootPath%>/common/images/right_t_01.jpg"
																width="5" height="29" />
														</td>
														<td class="content-title">
															&nbsp;��Դ >> ���ܼ��� >> ָ�귧ֵ�б�
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
																	<td class="body-data-title" style="text-align: left;">
																		&nbsp;&nbsp;
																		<b>���ͣ�</b>
																		<select id="type" name="type" style="width: 150px"
																			onchange="changeType()">
																			<option value="-1">
																				����
																			</option>
																			<option value="host">
																				������
																			</option>
																			<option value="net">
																				�����豸
																			</option>
																			<option value="firewall">
																				����ǽ
																			</option>
																			<option value="gateway">
																				�ʼ���ȫ����
																			</option>
																			<option value="atm">
																				ATM
																			</option>
																			<option value="f5">
																				F5
																			</option>
																			<option value="db">
																				���ݿ�
																			</option>
																			<option value="middleware">
																				�м��
																			</option>
																			<option value="service">
																				����
																			</option>
																			<option value="ups">
																				UPS
																			</option>
																			<option value="air">
																				�յ�
																			</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;
																		<b>�����ͣ�</b>
																		<select id="subtype" name="subtype"
																			style="width: 150px" onchange="changeNode()">
																			<option value="-1">
																				����
																			</option>
																		</select>
																		&nbsp;&nbsp;&nbsp;
																		<b>�豸��</b>
																		<select id="nodeid" name="nodeid" style="width: 150px">
																		</select>
																		&nbsp;&nbsp;&nbsp;
																		<input type="button" value="��  ѯ" onclick="search()">
																	</td>
																	<td class="body-data-title" style="text-align: right;">
																		<a href="#" onclick="add()">���</a>&nbsp;&nbsp;&nbsp;
																		<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
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
																		<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage"
																				value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal"
																				value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<td class="body-data-title" width=5%>
																	<INPUT type="checkbox" class=noborder name="checkall"
																		onclick="javascript:chkall()" class=noborder>
																	���
																</td>
																<td class="body-data-title" width=8%>
																	�豸����
																</td>
																<td class="body-data-title" width=8%>
																	IP
																</td>
																<td class="body-data-title" width=8%>
																	��ֵ
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
																            if (alarmIndicatorsNode.getCompare() == 2) {
																                compare = "���";
																            }
																            //System.out.println(String.valueOf(alarmIndicatorsNode.getNodeid()) + ":" + alarmIndicatorsNode.getType() + ":" + alarmIndicatorsNode.getSubtype());
																            NodeDTO nodeDTO = (NodeDTO) nodeDTOHashtable.get(String.valueOf(alarmIndicatorsNode.getNodeid()) + ":" + alarmIndicatorsNode.getType() + ":" + alarmIndicatorsNode.getSubtype());
																            if (nodeDTO == null) {
																                nodeDTO = new NodeDTO();
																            }
																            String ipaddress = nodeDTO.getIpaddress();
																            if (ipaddress == null) {
																                ipaddress = "";
																            }
																%>
																<tr >
																	<td class="body-data-list">
																		<INPUT type="checkbox" class=noborder name=checkbox
																			value="<%=alarmIndicatorsNode.getId()%>"
																			class=noborder><%=jp.getStartRow() + i%>
																	</td>
																	<td class="body-data-list"><%=nodeDTO.getName()%></td>
																	<td class="body-data-list"><%=ipaddress%></td>
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
																		<input type="hidden" id="ipaddress" name="ipaddress"
																			value="<%=ipaddress%>">
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

	<script>
	
		
	
		var nodeArray = new Array();
		
		<%
			if(allNodeDTOlist != null){
				for(int j = 0 ; j < allNodeDTOlist.size(); j++){
				 	NodeDTO  nodeDTO = (NodeDTO)allNodeDTOlist.get(j);
					%>
						nodeArray.push({
							id:"<%=nodeDTO.getId()%>",
							nodeid:"<%=nodeDTO.getNodeid()%>",
							name:"<%=nodeDTO.getName()%>",
							ipaddress:"<%=nodeDTO.getIpaddress()%>",
							type:"<%=nodeDTO.getType()%>",
							subtype:"<%=nodeDTO.getSubtype()%>",
							businessId:"<%=nodeDTO.getBusinessId()%>",
							businessName:"<%=nodeDTO.getBusinessName()%>"
					});
					<%
				}
			}
			
		%>
		
		function changeType(){
			var hostArray = [
						["windows" , "windows"],
						["linux" , "linux"],
						["aix" , "aix"],
						["hpunix" , "hpunix"],
						["solaris" , "solaris"],
						["scounix" , "scounix"],
						["scoopenserver" , "scoopenserver"],
						["as400" , "as400"]
						];
			var netArray = new Array();
			netArray = 	[
						["cisco" , "cisco"],
						["h3c" , "h3c"],
						["entrasys" , "entrasys"],
						["maipu" , "maipu"],
						["redgiant" , "redgiant"],
						["northtel" , "northtel"],
						["dlink" , "dlink"],
						["Brocade" , "brocade"],
						["DPtech" , "dptech"],
						["bdcom" , "bdcom"]
						];
			var dbArray = new Array();
			dbArray = 	[
						["oracle" , "oracle"],
						["sqlserver" , "sqlserver"],
						["mysql" , "mysql"],
						["db2" , "db2"],
						["sybase" , "sybase"],
						["Informix" , "informix"]
						];
			var middlewareArray = new Array();
			middlewareArray = 	[
						["mq" , "mq"],
						["domino" , "domino"],
						["was" , "was"],
						["tomcat" , "tomcat"],
						["iis" , "iis"],
						["jboss" , "jboss"],
						["apache" , "apache"],
						["tuxedo" , "tuxedo"],
						["cics" , "cics"],
						["dns" , "dns"],
						["weblogic" , "weblogic"]
								];
			var serviceArray = new Array();
			serviceArray = 	[
						["ftp" , "ftp"],
						["email" , "email"],
						["hostprocess" , "hostprocess"],
						["url" , "url"],
						["port" , "port"]
					];
			var upsArray = new Array();
			upsArray = 	[
						["��Ĭ��" , "ems"],
						["÷������" , "mge"]
								];
			var firewallArray = new Array();
			firewallArray = [
						["ɽʯ" , "hillstone"],
						["Netscreen" , "netscreen"],
						["NOKIA" , "nokia"],
						["DPtech" , "dptech"],
						["TopSec" , "topsec"],
						["SecGate" , "secgate"],

						["Venus" , "venus"],
						["TippingPoint" , "tippingpoint"],
						["SecWrold" , "secworld"]
								];
			var atmArray = new Array();
			atmArray = [
						["ATM" , "atm"]
					   ];
		    var f5Array = new Array();
			f5Array = [
						["F5" , "f5"],
						["���ŷ�" , "sangfor"]
					   ];
			var gatewayArray = new Array();
			gatewayArray = [
						    ["CISCO" , "cisco"]
						   ];
			var airArray = new Array();
			airArray = 	[
						["��Ĭ��" , "ems"]
								];
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			subtype.length = 0;
			if(type.value == "-1"){
				subtype.options[0] = new Option("����" , "-1");      
			}else if(type.value == "net"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <netArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(netArray[i][0],netArray[i][1]);                     
				}	
			}else if(type.value == "host"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <hostArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(hostArray[i][0],hostArray[i][1]);                     
				}	
			}else if(type.value == "db"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <dbArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(dbArray[i][0],dbArray[i][1]);                     
				}	
			}else if(type.value == "middleware"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <middlewareArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(middlewareArray[i][0],middlewareArray[i][1]);                     
				}	
			}else if(type.value == "service"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <serviceArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(serviceArray[i][0],serviceArray[i][1]);            
				}         
			}else if(type.value == "ups"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <upsArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(upsArray[i][0],upsArray[i][1]);                     
				}	
			}else if(type.value == "air"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <airArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(airArray[i][0],airArray[i][1]);                     
				}	
			}else if(type.value == "atm"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <atmArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(atmArray[i][0],atmArray[i][1]);                     
				}	
			}else if(type.value == "f5"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <f5Array.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(f5Array[i][0],f5Array[i][1]);                     
				}	
			}else if(type.value == "firewall"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <firewallArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(firewallArray[i][0],firewallArray[i][1]);                     
				}	
			}else if(type.value == "gateway"){
				subtype.options[0] = new Option("����" , "-1");
				for (var i = 0 ;i <gatewayArray.length;i++)   
	      		{                   
					subtype.options[i+1] = new Option(gatewayArray[i][0],gatewayArray[i][1]);                     
				}	
			}
			
			changeNode();
		}
		
		
		function changeNode(){
			
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			var nodeid = document.getElementById("nodeid");
			nodeid.length = 0;
			nodeid.options[0] = new Option("����","-1");
			if(type.value == "-1"){
				var k = 1;
				
				for (var i = 0 ;i <nodeArray.length;i++)   
	      		{                   
					nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      			k++;               
				}	
			}else if(type.value != "-1" && subtype.value == "-1"){
				var k = 1;
				for (var i = 0 ;i <nodeArray.length;i++)   
	      		{      
	      			if(nodeArray[i].type == type.value){
	      				nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      				k++;
	      			}             
					                
				}	
			}else if(type.value != "-1" && subtype.value != "-1"){
				var k = 1;
				for (var i = 0 ;i <nodeArray.length;i++)
	      		{      
	      			if(nodeArray[i].type == type.value && nodeArray[i].subtype == subtype.value){
	      				nodeid.options[k] = new Option(nodeArray[i].name,nodeArray[i].nodeid);
	      				k++;
	      			}             
					                
				}	
			}
			
		}
		
		function initAttribute(){
			var type = document.getElementById("type");
			var subtype = document.getElementById("subtype");
			var nodeid = document.getElementById("nodeid");
			
			type.value = "<%=type%>";
			changeType();
			subtype.value = "<%=subtype%>";
			changeNode();
			nodeid.value = "<%=nodeid%>";
		}
		
		initAttribute();
	</script>
	<script type="text/javascript">  
			$('.img').contextmenu({
				height:35,
				width:100,
				items : [{
					text :'�༭',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						var ipaddress=$($(target).parent()).find('#ipaddress').val();
						edit(id,ipaddress);
					}
				}]
			});	
			
		//�Ҽ��˵�������ʼ
			function edit(node,ipaddress){
				mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showEdit&id=" + node+"&ipaddress="+ipaddress;
				mainForm.submit();
			}
		//�Ҽ��˵���������
		</script>
</html>
