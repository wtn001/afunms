<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.alarm.model.AlarmIndicators"%>
<%@page import="com.afunms.polling.PollingEngine"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath();
	String[][] nodeAlarmsArry = (String[][])request.getAttribute("nodeAlarmsArry");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
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
	.totalTable{
		background-color: white;
		width: 100%;
		height: 100%;
	}
	.leftLine{
		vertical-align: top;
	}

	#formTab .tr{
		width: 100%;
	}
</style>
<script type="text/javascript">
		/**
	*��ʼ����ť��ɫ
	*/
	function initmenu(){
		//refreshStyle('xnmb');
		deviceTree();
	}
	
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
	
	function deviceTree(){
		ddtree = new Tree("deviceTree","100%","100%",0);
		ddtree.setImagePath("<%=rootPath%>/resource/image/tree/");
		ddtree.setDelimiter(",");
		ddtree.setOnCheckHandler(onNodeCheck);
		ddtree.enableCheckBoxes(1);
		ddtree.insertNewItem("","root","�澯ָ����Դ��", 0, "", "base.gif","base.gif", "base.gif");
		<%
			List alarmIndicatorsList = (ArrayList)request.getAttribute("alarmIndicatorsList");
			if(alarmIndicatorsList != null){
				for(int i=0; i<alarmIndicatorsList.size(); i++){
					AlarmIndicators node = (AlarmIndicators)alarmIndicatorsList.get(i);
		%>
			ddtree.insertNewItem("root","<%=node.getName()%>","<%=node.getDescr()%>", 0, "", "","", "");
		<%
				}
			}
		%>
	}
	
	function onNodeCheck(nodeid){  
		var selectedDevice = document.getElementById("selectedAlarmIndicators");
		//selectedDevice���option
		selectedDevice.length = 0;
		var nodeids = ddtree.getAllChecked().toString();
		var nodeidsArry = nodeids.split(',');
		var indicatorNames = "";
		var index = 0;
		for(var i=0; i<nodeidsArry.length; i++){
			var nodeid = nodeidsArry[i];
			if(nodeid == 'root'){
				continue;
			}
			index++;
			//���ӽڵ�ӵ��Ҳ��select����
			var itemText = ddtree.getItemText(nodeid);//����treeid�õ�text�ı�
			var item=document.createElement("option"); 											
			item.text=itemText;   
			item.value=nodeid;                       					
			selectedDevice.add(item);  
			if(index == 1){
				indicatorNames = nodeid+":"+itemText;
			}else{
				indicatorNames = indicatorNames + "," + nodeid+":"+itemText;
			}
		}		
		//alert(indicatorNames);
		document.getElementById('indicatorNames').value = indicatorNames;
	} 
	
	function nextPage(){
		var nodeids = ddtree.getAllChecked().toString();
		if(nodeids.trim() == ''){
			alert('����ѡ��һ������!');
			return;
		}
		document.getElementById("mainForm").action = "performancePanel.do?action=createNewPanel"
		document.getElementById("mainForm").submit();
	}
	
	function lastPage(){
		history.go(-1);
	}
	
	
	function cancel(){
		document.getElementById("mainForm").action = "performancePanel.do?action=panelList"
		document.getElementById("mainForm").submit();
	}
</script>
</head>
<body onload="initmenu();" style="background-color: #ECECEC;">
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
					                	<td class="content-title">&nbsp;���� >> ���ܼ������ >>�½�����  �ڶ��� </td>
					                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
					       			</tr>
					        	</table>
				        	</td>
			        	</tr>
			        	<tr>
			        		<td>&nbsp;</td>
							<td style="width: 100%;">
								<table id="formTab">
									<tr align="left" valign="middle">
										<td align="left"  colspan="2" class="leftLine">��Ӽ��ָ��-���ͣ����ָ�꣩</td>
									</tr>
									<tr align="right" valign="top">
                                        <TD style="height: 100% align="left" width="60%">
                                            <fieldset style="WIDTH:100%;vertical-align:top;height: 100%;OVERFLOW:auto;"  align="center">
                                                <legend>
                                                		ָ��ѡ��
                                                </legend>
                                                <div id="deviceTree"></div>
                                            </fieldset>
                                             <input type="hidden" id="indicatorNames" name="indicatorNames"/>
                                            <input type="hidden" id="panelName" name="panelName" value="<%=(String)request.getAttribute("panelName") %>"/>
                                            <input type="hidden" id="deviceIds" name="deviceIds" value="<%= (String)request.getAttribute("deviceIds")%>"/>
                                            <input type="hidden" id="deviceType" name="deviceType" value="<%= (String)request.getAttribute("deviceType")%>"/>
                                        </TD>
                                        <TD class="TdRightContent">&nbsp;</TD>
                                        <TD  style="height: 100%" align="center" width="40%">
                                            <fieldset style="WIDTH:100%" align="center">
                                                <legend>ѡ���ָ��</legend>
                                                <select id="selectedAlarmIndicators" multiple name="selectedAlarmIndicators" size="22" style="WIDTH:expression(document.body.clientWidth / 2 - 50)"></select>
                                            </fieldset>
                                        </TD>
                                    </TR>
                                    <TR class="menu">
                                        <TD colspan="3" class="TdRightContent" width="100%" align="center">
                                            <input type="button" id="lastButton" onclick="lastPage();"value="��һ��">&nbsp;&nbsp;
											<input type="button" id="nextButton" onclick="nextPage();" value="��һ��">
                                            <input id="cancel_button" type="button" onclick="cancel();" value="ȡ  ��">
                                        </TD>
                                    </TR>
								</table>
							</td>
							<td>&nbsp;</td>
			        	</tr>
			        </table>
	        	</td>
	        </tr>
	     </table>
	</form>
</body>
</html>