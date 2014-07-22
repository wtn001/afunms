<%@page language="java" contentType="text/html;charset=GB2312"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%
   String rootPath = request.getContextPath();
   User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
   //System.out.println(current_user.getBusinessids());    
   String bids[] = current_user.getBusinessids().split(",");
   String fileName = (String)request.getParameter("xml");    
%>
<head>         
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="<%=rootPath%>/topology/machineUpAndDown/css/topo_style.css" rel="stylesheet" type="text/css" />
<title>topFrame</title>    
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript" src="js/toolbar.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript" src="js/customview.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/util.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/MachineUpAndDownRemoteService.js"></script>
<%
	out.println("<script type=\"text/javascript\">");
	// �ж�ȫ����ʾ״̬
	String fullscreen = request.getParameter("fullscreen");	
	if (fullscreen.equals("0")) 
	   fullscreen = "0";
	else 
	{
		fullscreen = "1";
		out.println("viewWidth = 0;");
	}

	// ȡ���û�Ȩ��---�������Ʊ��桢ˢ�¡��༭�Ȳ���
	boolean admin = false;
	String user = "admin";

	if (user.equalsIgnoreCase("admin") || user.equalsIgnoreCase("superuser")) {
		out.println("var admin = true;"); //Ϊ�ˣ����༭����������ʹ��
		admin = true;
	}
	else {
		out.println("var admin = false;");	
		admin = false;
	}
	out.println("</script>");
	
	String disable = "";//���ư�ť�Ƿ񼤻�
	if (!admin) {
		disable = "disabled=\"disabled\"";
	}
%>
<script type="text/javascript">
var fileName = "<%=fileName%>";
    window.parent.frames['mainFrame'].location.reload();//������·��ˢ������ͼ
	var curTarget = "showMap.jsp?fullscreen=<%=fullscreen%>";
	var display = false;	    // �Ƿ���ʾ����б�
	var controller = false;		// �Ƿ���ʾ������
	
//������ͼ
function saveFile() {
	if (!admin) {
		window.alert("��û�б�����ͼ��Ȩ�ޣ�");
		return;
	}
	if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
	parent.mainFrame.saveFile();
}

// ȫ���ۿ�
function gotoFullScreen() {
	parent.mainFrame.resetProcDlg();
	var status = "toolbar=no,height="+ window.screen.height + ",";
	status += "width=" + (window.screen.width-8) + ",scrollbars=no";
	status += "screenX=0,screenY=0";
	window.open("index.jsp?submapXml=<%=fileName%>&fullscreen=yes&user=<%=user%>", "fullScreenWindow", status);
	parent.mainFrame.zoomProcDlg("out");
}
var goon=0;
var tId;
var tIdend;
var tIdOne;
var tIdendOne;
var iscontinue;
//���Թػ�
function policyDown(){
    if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
	if (window.confirm("�ò�����Ե�ǰ��������豸���в��Թػ�����������")) {
	    goon=1;
		parent.mainFrame.showLineInfo(1);
		var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
		tId = setInterval('parent.mainFrame.printedAll("'+url+'")',300);
		//����Զ�̹ػ�����
	    MachineUpAndDownRemoteService.policyDown(fileName, {
	        callback:function(data){
				if(data=="error"){
					alert("���Թػ�ʧ��!");
				} else {
				    if(data){
				        stopDown();
				        alert("���Թػ�����!");
				    }
				}
			}
	    });
	}
}
//��ֹ�����豸�ػ�
function stopDownOne(id){
    if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
    goon=0;
    parent.mainFrame.clearprintedOne(id);
    clearInterval(tIdOne);
    //clearInterval(tIdendOne);
}
//��������
function createDemoLink(){
    if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
    var objEntityAry = new Array();
    if(window.parent.frames['mainFrame'].objMoveAry!=null&&window.parent.frames['mainFrame'].objMoveAry.length>0){//��ѡ
        objEntityAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry!=null&&window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrlѡ
        objEntityAry = window.parent.frames['mainFrame'].objEntityAry;
    } 
    if(objEntityAry==null||objEntityAry.length!=2){
        alert("��ѡ�������豸��");
        return;
    }
    
    var start_id = objEntityAry[0].id.replace("node_","");
    var end_id = objEntityAry[1].id.replace("node_","");
    var xml = "<%=fileName%>";
    var lineArr = window.parent.frames['mainFrame'].demoLineMoveAry;
    if(lineArr!=null&&lineArr.length>0){
        alert("ѡ�е���̨�豸�Ѿ����ڲ���!");
        return;
    }
    var start_x_y=objEntityAry[0].style.left+","+objEntityAry[0].style.top;
    var end_x_y=objEntityAry[1].style.left+","+objEntityAry[1].style.top;
    var url="<%=rootPath%>/link.do?action=readyAddLine&xml="+xml+"&start_id="+start_id+"&end_id="+end_id+"&start_x_y="+start_x_y+"&end_x_y="+end_x_y;
    showModalDialog(url,window,'dialogwidth:510px; dialogheight:350px; status:no; help:no;resizable:0');
}
//����ػ�״̬
function refreshState(){
    if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
	var nodeArray = parent.mainFrame.getallnode();
    MachineUpAndDownRemoteService.refreshState(nodeArray,fileName, {
        callback:function(data){
			if(data=="error"){
				alert("���ʧ��!");
			} else {
			    if(data){
			        alert("����ɹ�!");
			    }
			}
		}
    });
    parent.mainFrame.location.reload();
}

//���йػ�
function complicatDown(){
    if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
	if (window.confirm("�ò�����Ե�ǰ��������豸���в��йػ�����������")){
	    goon=1;
	    parent.mainFrame.showLineInfo(1);
	    //tId = setInterval('parent.mainFrame.printed()',800);��ʾ
	    //tIdend = setInterval('parent.mainFrame.endprinted()',5000);��ʾ
	    var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
		tId = setInterval('parent.mainFrame.printedAll("'+url+'")',300);
		var nodeArray = parent.mainFrame.getallnode();
	    MachineUpAndDownRemoteService.complicatDown(nodeArray,fileName, {
	        callback:function(data){
				if(data=="error"){
					alert("���йػ�ʧ��!");
				} else {
				    if(data){
				        stopDown();
				        alert("���йػ�����!");
				    }
				}
			}
	    });
	}
}
//��ֹ�ػ�
function stopDown(){
    if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
    goon=0;
    parent.mainFrame.clearprinted();
    MachineUpAndDownRemoteService.stopDown(fileName, {
        callback:function(data){
			if(data=="error"){
				alert("��ֹ�ػ�ʧ��!");
			} else {
			    if(data){
			        alert("����ֹ�ػ�!");
			    }
			}
		}
    });
    clearInterval(tId);
    clearInterval(tIdend);
}
//���ص�ǰ������
function rebuild(){
    if (fileName=="null") {
		window.alert("��ѡ������飡");
		return;
	}
    if (window.confirm("�ò������������¼��ص�ǰ��������豸����������")) {
		window.location = "<%=rootPath%>/serverUpAndDown.do?action=reBuildSubMap&xml=<%=fileName%>";
		alert("�����ɹ�!");
        parent.location.reload();
	}
}
// ��ʾ��ͼ������
function showController(flag) {

	var result;
	if (flag == false)
		controller = false;
	if (controller) {
		result = parent.mainFrame.showController(controller);
		      
		if (result == false) {
			window.alert("��û��ѡ����ͼ���޿���������");
			return;
		}
			
		//document.all.controller.value = "�رտ�����";
		document.all.controller.title = "�ر���ʾ���ڵ���ͼ������";
		controller = false;
	}
	else {
		result = parent.mainFrame.showController(controller);
		
		if (result == false) {
			window.alert("��û��ѡ����ͼ���޿���������");
			return;
		}

		//document.all.controller.value = "����������";
		document.all.controller.title = "������ʾ���ڵ���ͼ������";
		controller = true;
	}
}
</script>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#CEDFF6">
<form name="topviewForm">
<table width="100%" height="25" border="0" cellspacing="0" cellpadding="0" style="padding:0px;border-top:#CEDFF6 1px solid;border-left:#CEDFF6 1px solid;border-right:#CEDFF6 1px solid;border-bottom:#D6D5D9 1px solid;background-color:#F5F5F5;">
  <tr>
    <td>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/common/images/menubg.jpg">
	  <tr>
		<td width="56"><input type="button" name="create1" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" value="������ͼ" <%=disable%>/></td>
		<td width="56"><input type="button" name="create2" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:createDemoLink();" value="��������"/></td>
		<td width="56"><input type="button" name="create3" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:rebuild();" value="�����豸"/></td>
		<td width="56"><input type="button" name="create4" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:policyDown();" value="���Թػ�"/></td>
		<td width="56"><input type="button" name="create5" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:complicatDown();" value="���йػ�"/></td>
        <td width="56"><input type="button" name="create6" class="button_create6_out" onmouseover="javascript:buttonCreate6Over();" onmouseout="javascript:buttonCreate6Out();" onclick="javascript:stopDown();" value="��ֹ�ػ�"/></td>
		<td width="56"><input type="button" name="create7" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshState();" value="����ػ�״̬"/></td>
		<td width="56">
	<%if (fullscreen.equals("0")) {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:gotoFullScreen();" value="ȫ���ۿ���ͼ"/>
	<%}else {%>
		<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:window.parent.close();" value="�رյ�ǰ����"/>
	<%}%>
		</td>
		<td width="56"><input type="button" name="controller" class="button_controller_out" onmouseover="javascript:buttonControllerOver();" onmouseout="javascript:buttonControllerOut();" onclick="javascript:showController();" value="�ر���ͼ������"/></td>
		<td width="100">
		</td>
		<td width="30" bgcolor="#ffffff">ͼ��:</td><td width="50" bgcolor="yellow">���ڹػ�</td>
		<td width="50" bgcolor="green">�����ػ�</td>
		<td width="50" bgcolor="red">�ػ�ʧ��</td>
		<td width="50" bgcolor="#8E8E8E">�����쳣</td>
		<td width="50" bgcolor="blue">�ֶ��ػ�</td>
		<td width="50" bgcolor="#CEDFF6">�޲���</td>
		<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  </tr>
	</table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
