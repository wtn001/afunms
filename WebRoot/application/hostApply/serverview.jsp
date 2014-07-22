  <%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.application.model.HostApply" %>
<%@page import="com.afunms.polling.node.Host"%>
<%@page import="com.afunms.application.manage.HostApplyManager"%>
<%@page import="com.afunms.alarm.service.NodeAlarmService"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath();
	String nodeid = (String)request.getAttribute("nodeid");
	String type = (String)request.getAttribute("type");
	String subtype = (String)request.getAttribute("subtype");
	String ipaddress=(String)request.getAttribute("ipaddress");
	Hashtable<String,HostApply> hostApplyHash = (Hashtable<String,HostApply>)request.getAttribute("hostApplyHash");
	
	String freshTimeMinute = (String)request.getAttribute("freshTimeMinute");
	if(freshTimeMinute == null){
		freshTimeMinute = "300";//ҳ��Ĭ��Ϊ60��ˢ��һ��
	}
	String pageFlag = request.getParameter("pageFlag");
	List<String> tableList = null;;
	if(("1").equals(pageFlag)){
		freshTimeMinute = request.getParameter("freshTimeMinute");
		
	}else{
		
	}
	String m = (Integer.parseInt(freshTimeMinute) / 60) + ":" ;//��
    String s = Integer.parseInt(freshTimeMinute) % 60 +""; //��
    if (Integer.parseInt(s) < 10) {
    	s = "0" + s;
    }
	String spanTimeValue = m+s;
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/fusionChart/Contents/Style.css" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/tree/common.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/tree/Tree.js"></script>
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" language="javascript" src="<%=rootPath%>/resource/jqueryResourcesTable/media/js/jquery.dataTables.js"></script>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/jqueryResourcesTable/media/css/demo_page.css" charset="utf-8"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/resource/jqueryResourcesTable/media/css/demo_table.css" charset="utf-8"/>

<style type="text/css">
	#ȫ�ֱ����ʽ
	.totalTable{
		background-color: white;
		width: 100%;
		height: 100%;
	}
.data_content{
	height:20px;
	border-top:1px dotted #999999;
	color:#333333;
	padding:2px;
	text-align: center;
}
.data{
	border:1px solid #679AD4;
	width:100%;
}
</style>
<script type="text/javascript">
	var baseTime = <%=freshTimeMinute%>;//Ĭ��5����
    var refTime;
    
	/***********************************************************************************************************
	��ʼ����ť��ɫ
	***********************************************************************************************************/
	function initmenu(){
		refreshStyle('zjst');
		initRefTime();
	}
	
	/***********************************************************************************************************
	�޸Ķ�ʱˢ��ҳ���ֵ
	������
		timeMinute  ��ʱˢ�µ�ʱ�䣬��λΪ����
	***********************************************************************************************************/
	function refreshPage(){
		var refreshTime = $('#refreshtime').val();
		document.getElementById('freshTimeMinute').value = refreshTime;
		window.location.href = "<%=rootPath%>/performance/hostApply.do?action=serverview&freshTimeMinute="+refreshTime+"&pageFlag=1";   
	}
	
	
	/***********************************************************************************************************
	����ʱ��ʾ����
	***********************************************************************************************************/
	function refreshTime() {
	    var m = parseInt(baseTime / 60) + ":" //��
	    var s = baseTime % 60; //��
	    if (s < 10) {
	    	s = "0" + s;
	    }
	    $('#spanTime').html(m + s);
	    baseTime--;
	    if (baseTime == 0) {
	        refreshPage();
	    }
	}
	
	
	function initRefTime() {
	    refTime = setInterval("refreshTime()", 1000);
	}
	
	/***********************************************************************************************************
	���Ը�ѡ��
	***********************************************************************************************************/
	function setSelectItem(objId,strValue){
		var options = document.getElementById(objId);
	    var option=options.getElementsByTagName("option");  
	    var str = "" ;  
	    for(var i=0;i<option.length;++i){ 
	    	if(option[i].value == baseTime){  
	    		option[i].selected = true; 
	    		//alert('aaa'); 
	    	}  
	    }
	}
	
	/***********************************************************************************************************
	ˢ����ఴť�����ӵ���ʽ
	����
		liId ����б����ӵ�ID
	************************************************************************************************************/
	function refreshStyle(liId){
		var varsLi = window.parent.tabMenuFrame.document.getElementsByTagName('li');
		for(var i=0; i<varsLi.length; i++){
			var varLi = varsLi[i];
			if(varLi.id == liId){
				window.parent.tabMenuFrame.document.getElementById(varLi.id).className = "menu-title-over";
			}else{
				window.parent.tabMenuFrame.document.getElementById(varLi.id).className = "menu-title";
			}
		}
	}
	
	/***********************************************************************************************************
	չʾ������Ӧ���޸ĵ�ҳ��
	************************************************************************************************************/
	function showHostApplyModifyPage(){
		window.open("<%=rootPath%>/hostApply.do?action=allList&type=host&subtype=<%=subtype%>&ipaddress=<%=ipaddress%>","oneping", "height=500, width=1000, top=100, left=100,scrollbars=yes,resizable=yes");
	}
	
	/***********************************************************************************************************
	��ת����ϸҳ��
	����:
		url ��ת����ϸҳ�ĳ�����
	***********************************************************************************************************/
	function toDetailPage(nodeid){
		var url = "<%=rootPath%>/detail/dispatcher.jsp?id=net"+nodeid+"&flag=1&fromtopo=true";;
		window.parent.parent.window.document.getElementById('mainFrame').src = url;
	}
</script>
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$('#example').dataTable();
	} );
</script>
<style>
	.sorting{
		background-image: none;
	}
	.sorting_asc{
		background-image: none;
	}
	.sorting_desc{
		background-image: none;
	}
	tr.odd{
		background-color: white;
	}
</style>
</head>
<body onload="initmenu();" style="background-color: white;">
	<form id="mainForm" method="post" name="mainForm">
		<table>
			<tr>
				<td valign=top>
					<table>
						<tr>
							<td colspan="3">
								<table id="content-header" class="content-header">
				                	<tr>
					                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					                	<td class="content-title">&nbsp;���� >> ������ͼ</td>
					                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
					       			</tr>
					        	</table>
				        	</td>
			        	</tr>
			        	<tr>
							<td colspan="3">
								<table bgcolor="#ECECEC">
									<tr align="right" valign="middle">
										<td align="left" class="content-title">&nbsp;&nbsp;����Ӧ���б����</td>
										<td height="21" align="right" valign="middle">
											<a href="#" onclick="showHostApplyModifyPage();">Ӧ������</a>
											&nbsp;&nbsp;<font class="title">|</font>&nbsp;&nbsp;
	                                        <font class="title">�������ڣ�</font>
	                                        <input type="hidden" id="freshTimeMinute" name="freshTimeMinute"/>
	                                        <select id="refreshtime" name="refreshtime" onchange="refreshPage();" style="WIDTH: 76px">
	                                            <option value="60">1����</option>
	                                            <option value="180">3����</option>
	                                            <option value="300">5����</option>
	                                            <option value="600">10����</option> 
	                                            <option value="1200">20����</option>
	                                            <option value="1800">30����</option>
	                                        </select>&nbsp;
	                                        <script language="javascript">
                                           	 	setSelectItem("refreshtime", "300");
                                        	</script>
	                                        <font class="title">����ʱ��</font><span id="spanTime"><%=spanTimeValue %></span>
	                                        &nbsp;&nbsp;<font class="title">
										</td>
									</tr>
								</table>
							</td>
			        	</tr>
			        	<tr>
							<td colspan="3">
								<table>
									<tr align="right" valign="middle">
										<td>
		        								<table id="content-body" >  
													<tr>
	        											<td height="21" align="right" valign="bottom">
															<font class="title">
										                                            �����澯��<img src="<%=HostApplyManager.getCurrentStatusImage(3)%>" alt="�����澯">&nbsp;
										                                            ���ظ澯��<img src="<%=HostApplyManager.getCurrentStatusImage(2)%>" alt="���ظ澯">&nbsp;
										                                            ��ͨ�澯��<img src="<%=HostApplyManager.getCurrentStatusImage(1)%>" alt="��ͨ�澯">&nbsp;
										                                            ����״̬��<img src="<%=HostApplyManager.getCurrentStatusImage(0)%>" alt="����״̬">
										                    </font>&nbsp;&nbsp;
														</td>
	        										</tr>
		        								</table>
		        							</td>
									</tr>
								</table>
							</td>
			        	</tr>
			        	<tr>
			        		<td>&nbsp;</td>
			        	</tr>
			        	<tr>
   							<td>
   								<table id="content-body" class="content-body">
								<tr>
									<td width="10px;"></td>
									<td>
										<table id="example" class="data" cellSpacing="0" cellPadding="0">
											<thead>
												<tr style="background-color: #ececec;">
									    			<th class="body-data-title">���</th>
									    			<th class="body-data-title">������IP��ַ</th>
									    			<th class="body-data-title">����</th> 
                                                    <td class="body-data-title">״̬</td> 
									    			<th class="body-data-title">Oracle</th>
									    			<th class="body-data-title">SQLServer</th>
									    			<th class="body-data-title">DB2</th>
									    			<th class="body-data-title">Sybase</th>
									    			<th class="body-data-title">MySQL</th>
									    			<th class="body-data-title">Informix</th>
									    			<th class="body-data-title">FTP</th>
									    			<th class="body-data-title">Email</th>
									    			<th class="body-data-title">MQ</th>
									    			<th class="body-data-title">Domino</th>
									    			<th class="body-data-title">WAS</th>
									    			<th class="body-data-title">Weblogic</th>
									    			<th class="body-data-title">Tomcat</th>
									    			<th class="body-data-title">IIS</th>
									    			<th class="body-data-title">CICS</th>
									    			<th class="body-data-title">DNS</th>
									    			<th class="body-data-title">JBOSS</th>
									    			<th class="body-data-title">Apache</th>
									    			<th class="body-data-title">Tuxdeo</th>
			        							</tr>
			        						</thead>
			        						<tbody>
			        							<%
			        							int num = 0;
			        							if(hostApplyHash != null && !hostApplyHash.isEmpty()){
                                                    NodeAlarmService service = new NodeAlarmService();
			        								Iterator<String> iterator = hostApplyHash.keySet().iterator();
			        								while(iterator.hasNext()){
			        									num++;
			        									String ip = iterator.next();
			        									//����IP��ַ�õ�Host
			        									Host node = (Host)PollingEngine.getInstance().getNodeByIP(ip);
                                                        String nodeImgPath = "";
                                                        if (node != null && node.isManaged()) {
                                                            int nodeStatus = service.getMaxAlarmLevel(node);
                                                            nodeImgPath = HostApplyManager.getCurrentStatusImage(nodeStatus);
                                                        }
			        									
			        									HostApply hostApply = hostApplyHash.get(ip);
			        									String oracleStatus = hostApply.getOracleStatus() == null ?  "-1" : hostApply.getOracleStatus();
			        									String oracleImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(oracleStatus));
			        									
			        									String sqlserverStatus = hostApply.getSqlserverStatus() == null ?  "-1" : hostApply.getSqlserverStatus();
			        									String sqlserverImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(sqlserverStatus));
			        									
			        									String db2Status = hostApply.getDb2Status() == null ?  "-1" : hostApply.getDb2Status();
			        									String db2ImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(db2Status));
			        									
			        									String sybaseStatus = hostApply.getSybaseStatus() == null ?  "-1" : hostApply.getSybaseStatus();
			        									String sybaseImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(sybaseStatus));
			        									
			        									String mysqlStatus = hostApply.getMysqlStatus() == null ? "-1" : hostApply.getMysqlStatus();
			        									String mysqlImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(mysqlStatus));
			        									
			        									String informixStatus = hostApply.getInformixStatus() == null ? "-1" : hostApply.getInformixStatus();
			        									String informixImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(informixStatus));
			        									
			        									String ftpStatus = hostApply.getFtpStatus() == null ? "-1" : hostApply.getFtpStatus();
			        									String ftpImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(ftpStatus));
			        									
			        									String emailStatus = hostApply.getEmailStatus() == null ? "-1" : hostApply.getEmailStatus();
			        									String emailImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(emailStatus));
			        									
			        									String mqStatus = hostApply.getMqStatus() == null ? "-1" : hostApply.getMqStatus();
			        									String mqImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(mqStatus));
			        									
			        									String dominoStatus = hostApply.getDominoStatus() == null ? "-1" : hostApply.getDominoStatus();
			        									String dominoImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(dominoStatus));
			        									
			        									String wasStatus = hostApply.getWasStatus() == null ? "-1" : hostApply.getWasStatus();
			        									String wasImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(wasStatus));
			        									
			        									String weblogicStatus = hostApply.getWeblogicStatus() == null ? "-1" : hostApply.getWeblogicStatus();
			        									String weblogicImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(weblogicStatus));
			        									
			        									String tomcatStatus = hostApply.getTomcatStatus() == null ? "-1" : hostApply.getTomcatStatus();
			        									String tomcatImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(tomcatStatus));
			        									
			        									String iisStatus = hostApply.getIisStatus() == null ? "-1" : hostApply.getIisStatus();
			        									String iisImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(iisStatus));
			        									
			        									String cicsStatus = hostApply.getCicsStatus() == null ? "-1" : hostApply.getCicsStatus();
			        									String cicsImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(cicsStatus));
			        									
			        									String dnsStatus = hostApply.getDnsStatus() == null ? "-1" : hostApply.getDnsStatus();
			        									String dnsImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(dnsStatus));
			        									 
			        									String jbossStatus = hostApply.getJbossStatus() == null ? "-1" : hostApply.getJbossStatus();
			        									String jbossImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(jbossStatus));
			        									 
			        									String apacheStatus = hostApply.getApacheStatus() == null ? "-1" : hostApply.getApacheStatus();
			        									String apacheImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(apacheStatus));
			        									
			        									String tuxedoStatus = hostApply.getTuxedoStatus() == null ? "-1" : hostApply.getTuxedoStatus();
			        									String tuxedoImgPath = HostApplyManager.getCurrentStatusImage(Integer.parseInt(tuxedoStatus));
			        							%>
			        										
	       										<tr <%=onmouseoverstyle%>>
	       											<td class="data_content center"><%=num %></td>
	       											<td class="data_content center"><%=ip %></td>
	       											<td class="data_content center">
	       												<%
	       													if(node != null){//�豸��Ϊ��(�������Ѿ������)
	       												%>
	       												<a href="#" onclick="toDetailPage('<%=node.getId() %>')"><%=node.getAlias() %></a>
	       												<%
	       													}else{//������δ���
	       												%>
	       												<%
	       													}
	       												%>
	       											</td>
                                                    <td class="data_content center">
                                                        <%
                                                            if(node != null && node.isManaged()){//�豸��Ϊ��(�������Ѿ������)
                                                        %>
                                                        <img src='<%=nodeImgPath %>'/>
                                                        <%
                                                            }else{//������δ���
                                                        %>
                                                                δ���
                                                        <%
                                                            }
                                                        %>
                                                    </td>
									    			<td class="data_content center">
														<%
															if(oracleImgPath != null && hostApply.isOracleIsShow()){
														%> 
									    				<img src='<%=oracleImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(sqlserverImgPath != null && hostApply.isSqlserverIsShow()){
														%>
									    				<img src='<%=sqlserverImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td> 
									    			<td class="data_content center">
									    				<%
															if(db2ImgPath != null && hostApply.isDb2IsShow()){
														%>
									    				<img src='<%=db2ImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(sybaseImgPath != null && hostApply.isSybaseIsShow()){
														%>
									    				<img src='<%=sybaseImgPath %>'/>
									    				<%
															}
									    				%>
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(mysqlImgPath != null && hostApply.isMysqlIsShow()){
														%>
									    				<img src='<%=mysqlImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(informixImgPath != null && hostApply.isInformixIsShow()){
														%>
									    				<img src='<%=informixImgPath %>'/>
									    				<%
															}
									    				%>
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(ftpImgPath != null && hostApply.isFtpIsShow()){
														%>
									    				<img src='<%=ftpImgPath %>'/>
									    				<%
															}
									    				%>
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(emailImgPath != null && hostApply.isEmailShow()){
														%>
									    				<img src='<%=emailImgPath %>'/>
									    				<%
															}
									    				%>
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(mqImgPath != null && hostApply.isMqIsShow()){
														%>
									    				<img src='<%=mqImgPath %>'/>
									    				<%
															}
									    				%>
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(dominoImgPath != null && hostApply.isDominoIsShow()){
														%>
									    				<img src='<%=dominoImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(wasImgPath != null && hostApply.isWasIsShow()){
														%>
									    				<img src='<%=wasImgPath %>'/>
									    				<%
															}
									    				%>		
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(weblogicImgPath != null && hostApply.isWeblogicIsShow()){
														%>
									    				<img src='<%=weblogicImgPath %>'/>
									    				<%
															}
									    				%>		
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(tomcatImgPath != null && hostApply.isTomcatIsShow()){
														%>
									    				<img src='<%=tomcatImgPath %>'/>
									    				<%
															}
									    				%>		
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(iisImgPath != null && hostApply.isIisIsShow()){
														%>
									    				<img src='<%=iisImgPath %>'/>
									    				<%
															}
									    				%>			
									    			</td>
									    			<td class="data_content center">  
									    				<%
															if(cicsImgPath != null && hostApply.isCicsIsShow()){
														%>
									    				<img src='<%=cicsImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(dnsImgPath != null && hostApply.isDnsIsShow()){
														%>
									    				<img src='<%=dnsImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(jbossImgPath != null && hostApply.isJbossIsShow()){
														%>
									    				<img src='<%=jbossImgPath %>'/>
									    				<%
															}
									    				%>		
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(apacheImgPath != null && hostApply.isApacheIsShow()){
														%>
									    				<img src='<%=apacheImgPath %>'/>
									    				<%
															}
									    				%>		
									    			</td>
									    			<td class="data_content center">
									    				<%
															if(tuxedoImgPath != null && hostApply.isTuxedoIsShow()){
														%>
									    				<img src='<%=tuxedoImgPath %>'/>
									    				<%
															}
									    				%>	
									    			</td>
			        							</tr>
		        								<%	
			        								}
			        							}
		        								%>
		        							</tbody>
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