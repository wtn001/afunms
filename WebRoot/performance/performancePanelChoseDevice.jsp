<%@ page language="java" contentType="text/html; charset=gb2312"
    pageEncoding="gb2312"%>
<%@page import="com.afunms.application.model.DBVo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.application.model.PerformancePanelModel"%>
<%@ include file="/include/globe.inc"%>
<%
	String rootPath = request.getContextPath();
	request.setCharacterEncoding("gb2312");
	String deviceIds = (String)request.getAttribute("deviceIds");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<title>���� >> ���ܼ������ >> ��������е��豸</title>
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
		addReCheck();
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
		ddtree.insertNewItem("","root","�豸��Դ��", 0, "", "base.gif","base.gif", "base.gif");
		<%
			List deviceList = (ArrayList)request.getAttribute("deviceList");
			if(deviceList != null){
				for(int i=0; i<deviceList.size(); i++){
					Object obj = deviceList.get(i);
					if(obj instanceof HostNode){
						HostNode node = (HostNode)obj;
		%>
						ddtree.insertNewItem("root","<%=node.getId()%>","<%=node.getAlias()%>", 0, "", "","", "");
		<%
					}else if(obj instanceof DBVo){
						DBVo node = (DBVo)obj;
		%>
						ddtree.insertNewItem("root","<%=node.getId()%>","<%=node.getAlias()%>", 0, "", "","", "");
		<%
					}
				}
			}
		%>
		
	}
	/*******************************
	��ʼ������ѡ��״̬
	*********************************/
	function addReCheck(){
		var allLeafsIds = ddtree.getAllLeafs();
		var allLeafsIdsArry = allLeafsIds.split(',');
		var deviceIds = '<%=deviceIds%>';
		for(var i=0; i<allLeafsIdsArry.length; i++){
			if(deviceIds.indexOf(','+allLeafsIdsArry[i]+',') != -1){
				ddtree.setCheck(allLeafsIdsArry[i],1);
				onNodeCheck(allLeafsIdsArry[i]);//��ʼ���������齨���豸����
			}
		}
	}	
	
	function onNodeCheck(nodeid){  
		var selectedDevice = document.getElementById("selectedDevice");
		//selectedDevice���option
		selectedDevice.length = 0;
		var nodeids = ddtree.getAllChecked().toString();
		var nodeidsArry = nodeids.split(',');
		for(var i=0; i<nodeidsArry.length; i++){
			var nodeid = nodeidsArry[i];
			if(nodeid == 'root'){
				continue;
			}
			//���ӽڵ�ӵ��Ҳ��select����
			var itemText = ddtree.getItemText(nodeid);//����treeid�õ�text�ı�
			var item=document.createElement("option"); 											
			item.text=itemText;   
			item.value=nodeid;                       					
			selectedDevice.add(item);  
		}		
		document.getElementById('deviceIds').value = nodeids
	} 

	function nextPage(){
		var nodeids = ddtree.getAllChecked().toString();
		if(nodeids.trim() == ''){
			alert('����ѡ��һ������!');
			return;
		}
		var panelName = document.getElementById('panelName').value;
		var deviceType = document.getElementById('deviceType').value;
		var deviceIds = document.getElementById('deviceIds').value;
		var url = "panelName="+panelName+"&deviceIds="+deviceIds+"&deviceType="+deviceType+"&nowtime="+(new Date());
		url = encodeURI(url);
		url = encodeURI(url); //����encode����
		$.ajax({
			type:"POST",
			dataType:"json",
			data:url,
			url:"<%=rootPath%>/performancePanelAjaxManager.ajax?action=editPanelDevice",
			success:function(data){
				window.returnValue = 1;
				window.close();
			}	
		});
	}
	
	function lastPage(){
		history.go(-1);
	}
	
	function cancel(){
		window.close();
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
					                	<td class="content-title">&nbsp;���� >> ���ܼ������ >>���ü���豸 </td>
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
										<td align="left"  colspan="2" class="leftLine">��Ӽ���豸-���ͣ������豸��</td>
									</tr>
									<tr align="right" valign="top">
                                        <TD style="height: 100%" align="left" width="50%">
                                            <fieldset style="WIDTH:100%;vertical-align:top;height: 100%;OVERFLOW:auto;"  align="center">
                                                <legend>
                                                		�豸ѡ��
                                                </legend>
                                                <div id="deviceTree"></div>
                                            </fieldset>
                                            <input type="hidden" id="panelName" name="panelName" value="<%=(String)request.getAttribute("panelName") %>"/>
                                            <input type="hidden" id="deviceIds" name="deviceIds"/>
                                            <input type="hidden" id="deviceType" name="deviceType" value="<%= (String)request.getAttribute("deviceType")%>"/>
                                        </TD>
                                        <TD class="TdRightContent">&nbsp;&nbsp;</TD>
                                        <TD  style="height: 100%" align="center" width="50%">
                                            <fieldset style="WIDTH:100%" align="center">
                                                <legend>ѡ����豸</legend>
                                                <select id="selectedDevice" multiple name="selectedDevice" size="22" style="WIDTH:expression(document.body.clientWidth / 2 - 50)"></select>
                                            </fieldset>
                                        </TD>
                                    </TR>
                                    <TR class="menu">
                                        <TD colspan="3" class="TdRightContent" width="100%" align="center">
											<input type="button" id="nextButton" onclick="nextPage();" value="ȷ ��">
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