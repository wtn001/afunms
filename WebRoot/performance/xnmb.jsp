  <%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.afunms.application.manage.PerformancePanelManager"%>
<%@page import="com.afunms.application.model.PerformancePanelModel"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath();
	String pageFlag = request.getParameter("pageFlag");
	String freshTimeMinute = (String)request.getAttribute("freshTimeMinute");
	if(freshTimeMinute == null){
		freshTimeMinute = "300";//ҳ��Ĭ��Ϊ60��ˢ��һ��
	}
	List<String> tableList = null;;
	if(("1").equals(pageFlag)){
		freshTimeMinute = request.getParameter("freshTimeMinute");
		
		//��ع����б�
		//���ܼ������table���
		rootPath = request.getContextPath();
		tableList = PerformancePanelManager.getInstance().getPerformanceList(); 
		//���ؼ�ع���Ľ���
	}else{
		tableList = (ArrayList<String>)request.getAttribute("tableList");
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
}
.data{
	border:1px solid #679AD4;
	width:100%;
}
</style>

<script type="text/javascript">
	/***********************************************************************************************************
	��ʼ����ť��ɫ
	***********************************************************************************************************/
	function initmenu(){
		refreshStyle('xnmb');
		initRefTime();
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
	����������壬�򿪴�������ҳ
	************************************************************************************************************/
	function createXnmb(){
		//var returnValue= showModalDialog('<%=rootPath%>/performance/addXnmb.jsp','','dialogWidth:520px;dialogHeight:510px;help:no;center:yes;resizable:no;status:no;scroll:no'); 
		document.getElementById("mainForm").action = "<%=rootPath%>/performancePanel.do?action=addperformancePanel";
		document.getElementById("mainForm").submit();
	}
	
	/***********************************************************************************************************
	�Ӽ����ŵ��������/��ʾ���
	����
		tableId ��Ҫ���ػ�����ʾ����ID
		imgId   ���ӣ��Ӽ����ŵ�ID��
	***********************************************************************************************************/
	function display(tableId,imgId){
		if(document.getElementById(tableId).style.display != 'none'){
			document.getElementById(tableId).style.display = 'none';
			document.getElementById(imgId).src = '<%=rootPath%>/resource/image/tree/plus.gif';
		}else{
			document.getElementById(tableId).style.display = '';
			document.getElementById(imgId).src = '<%=rootPath%>/resource/image/tree/minus.gif';
		}
	}
	
	/***********************************************************************************************************
	ɾ�����
	***********************************************************************************************************/
	function deletePanel(){
		document.getElementById("mainForm").action = "<%=rootPath%>/performancePanel.do?action=delete";
		document.getElementById("mainForm").submit();
	}
	
	/***********************************************************************************************************
	���ü�ص��豸  ������
	������
		deviceType �豸����
		panelName  �������
	***********************************************************************************************************/
	function editPanelDevice(deviceType, panelName){
		var url = "<%=rootPath%>/performancePanel.do?action=toEditPanelDevice&deviceType="+deviceType+"&panelName="+panelName;
		url = encodeURI(url);
		url = encodeURI(url); 
		 var result = showModalDialog(url, "", "dialogWidth:700px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes","");
		 if(result == 1) {
			window.location.reload(); 
		 }
	}
	
	/***********************************************************************************************************
	�������ļ��ָ�� 
	����
		deviceType �豸����
		panelName  �������
	***********************************************************************************************************/
	function editPanelIndicators(deviceType, panelName){
		var url = "<%=rootPath%>/performancePanel.do?action=toEditPanelIndicators&deviceType="+deviceType+"&panelName="+panelName;
		url = encodeURI(url);
		url = encodeURI(url);
		var result = showModalDialog(url, "", "dialogWidth:700px;dialogHeight:500px;dialogLeft:200px;dialogTop:150px;center:yes;help:yes;resizable:yes;status:yes");
		if(result == 1) {
			window.location.reload(); 
		}
	}
</script>
<script language="javascript"> 
	var baseTime = <%=freshTimeMinute%>;//Ĭ��5����
    var refTime;
        
	/***********************************************************************************************************
	�޸Ķ�ʱˢ��ҳ���ֵ
	������
		timeMinute  ��ʱˢ�µ�ʱ�䣬��λΪ����
	***********************************************************************************************************/
	function refreshPage(){
		var refreshTime = $('#refreshtime').val();
		document.getElementById('freshTimeMinute').value = refreshTime;
		window.location.href = "<%=rootPath%>/performance/xnmb.jsp?freshTimeMinute="+refreshTime+"&pageFlag=1"; 
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
	ȷ��ɾ����ǰ���
	������
		panelName :�������
	***********************************************************************************************************/
	function confrimDeletePanel(panelName){
		document.getElementById('panelName').value = panelName;
		var bln = window.confirm("ȷ��ɾ����壺"+panelName+"?");
		if(bln == true){
			document.getElementById('mainForm').action = 'performancePanel.do?action=delete';
			document.getElementById('mainForm').submit();
		}
	}
	
	/***********************************************************************************************************
	��ת����ϸҳ��
	����:
		url ��ת����ϸҳ�ĳ�����
	***********************************************************************************************************/
	function toDetailPage(url){
		window.parent.parent.window.document.getElementById('mainFrame').src = url;
	}
</script>
</head>
<body onload="initmenu();" style="background-color: white;">
	<form id="mainForm" method="post" name="mainForm">
	<input type="hidden" name="panelName" value=""/>
		<table>
			<tr>
				<td valign=top>
					<table>
						<tr>
							<td colspan="3">
								<table id="content-header" class="content-header">
				                	<tr>
					                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					                	<td class="content-title">&nbsp;���� >> ���ܼ������ </td>
					                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
					       			</tr>
					        	</table>
				        	</td>
			        	</tr>
			        	<tr>
							<td colspan="3"> 
								<table bgcolor="#ECECEC">
									<tr align="right" valign="middle">
										<td align="left" class="content-title">&nbsp;&nbsp;ϵͳ���������������</td>
										<td height="21" align="right" valign="middle">
											<a href="#"onClick="createXnmb();">�½���ط���</a>
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
	                                        &nbsp;&nbsp;<font class="title">|</font>&nbsp;&nbsp;
	                                        <font class="title">�л�����ģ�壺</font>
	                                        <select name="templateid" onchange="javascript:changeTemplate(options[selectedIndex].value);">
	                                            <option value="0" selected>Ĭ��ģ��</option>
	                                        </select>&nbsp;&nbsp;&nbsp;
										</td>
									</tr>
								</table>
							</td>
			        	</tr>
			        	<tr>
							<td colspan="3">
								<table>
									<tr align="right" valign="middle">
										<td height="21" align="right" valign="bottom">
											<font class="title">
						                                            ���ܵ�ʾ����
						                                            �����澯��<img src="<%=PerformancePanelManager.getCurrentStatusImage(3)%>" alt="�����澯">&nbsp;
						                                            ���ظ澯��<img src="<%=PerformancePanelManager.getCurrentStatusImage(2)%>" alt="���ظ澯">&nbsp;
						                                            ��ͨ�澯��<img src="<%=PerformancePanelManager.getCurrentStatusImage(1)%>" alt="��ͨ�澯">&nbsp;
						                                            ����״̬��<img src="<%=PerformancePanelManager.getCurrentStatusImage(0)%>" alt="����״̬">
						                    </font>&nbsp;&nbsp;
										</td>
									</tr>
								</table>
							</td>
			        	</tr>
			        	<%
			        		for(int i=0; i<tableList.size(); i++){
			        		String table = tableList.get(i);
			        	%>
			        		<tr>
			        			<td width="10px;">&nbsp;</td>
			        			<td><%=table %></td>
			        			<td width="10px;">&nbsp;</td>
			        		</tr>
			        	<%
			        		}
			        	 %>
			        </table>
	        	</td>
	        </tr>
	     </table>
	</form>
</body>
</html>