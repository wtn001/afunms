<%@page language="java" contentType="text/html;charset=GB2312"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="java.util.List"%>
<%@page import="com.afunms.machine.model.StoreyInfo"%>
<%@page import="com.afunms.machine.dao.StoreyInfoDao"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%
   String rootPath = request.getContextPath();
   User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
   //System.out.println(current_user.getBusinessids());
   String bids[] = current_user.getBusinessids().split(",");
   String fileName = (String)session.getAttribute(SessionConstant.CURRENT_FLOOR_VIEW);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<link href="<%=rootPath%>/resource/css/topo_style.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<%=rootPath%>/resource/css/top.css" type="text/css">
<title>topFrame</title>
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript" src="js/toolbar.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript" src="js/customview.js"></script>
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
    window.parent.frames['mainFrame'].location.reload();//������·��ˢ������ͼ
	var curTarget = "showMap.jsp?fullscreen=<%=fullscreen%>";
	var display = false;	    // �Ƿ���ʾ����б�
	var controller = false;		// �Ƿ���ʾ������
	
function searchNode()
{	
	var ip = window.prompt("��������Ҫ��������Ϣ�����", "�ڴ�������Ϣ�����");
	if (ip == null)
		return true;
	else if (ip == "�ڴ�������Ϣ�����")
		return;

	var coor = window.parent.mainFrame.getNodeCoor(ip);
	if (coor == null)
	{
		var msg = "û����ͼ������������Ϊ "+ ip +" ����Ϣ�㡣";
		window.alert(msg);
		return;
	}
	else if (typeof coor == "string")
	{
		window.alert(coor);
		return;
	}

	// �ƶ��豸�����ı�Ǵ�
	window.parent.mainFrame.moveMainLayer(coor);
}

// ������ͼ
function saveFile() {
	if (!admin) {
		window.alert("��û�б�����ͼ��Ȩ�ޣ�");
		return;
	}
	parent.mainFrame.saveFile();
}

// ˢ����ͼ
function refreshFile() 
{
	if (window.confirm("��ˢ�¡�ǰ�Ƿ���Ҫ���浱ǰ��ͼ��")) {
		parent.topFrame.saveFile();
	}
	parent.mainFrame.location.reload();
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

//����ͼ����
function editSubMap(){
    if("<%=fileName%>"=="testData.jsp"){
        alert("��ѡ����ͼ!");
    } else {
        var url="<%=rootPath%>/storey.do?action=readyEditSmall&xml=<%=fileName%>";
        showModalDialog(url,window,'dialogwidth:500px; dialogheight:400px; status:no; help:no;resizable:0');
    }
    
}
//�ؽ���ͼ
function rebuild(){
    if (window.confirm("�ò��������¹�����ǰ¥��ǽ���λ�ã���������")) {
		window.location = "<%=rootPath%>/storey.do?action=reBuildSubMap&xml=<%=fileName%>";
		alert("�����ɹ�!");
        parent.location.reload();
	}
}
//������ͼ
function backup(){
    var url="<%=rootPath%>/storey.do?action=readybackup&xml=<%=fileName%>";
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
}
//�ָ���ͼ
function resume(){
    var url="<%=rootPath%>/storey.do?action=readyresume&xml=<%=fileName%>";
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
}
function autoRefresh() 
{
	window.clearInterval(freshTimer);
	freshTimer = window.setInterval("refreshFile()",60000);
}

//ѡ����ͼ
function changeView()
{
	if(document.all.submapview.value == "")return;
	window.parent.parent.location = "../infomanage/index.jsp?submapXml=" + document.all.submapview.value;
}
function cwin()
  {
     if(parent.parent.search.cols!='230,*')
     {
        parent.parent.search.cols='230,*';
        document.all.pic.src ="<%=rootPath%>/resource/image/hide_menu.gif";
        document.all.pic.title="��������";
     }
     else
     {
        parent.parent.search.cols='0,*';
        document.all.pic.src ="<%=rootPath%>/resource/image/show_menu.gif";
        document.all.pic.title="��ʾ����";
     }
  }
//����ʾ��ͼԪ
function createDemoObj(){
    //window.parent.mainFrame.ShowHide("1",null);��ק��ʽ
    
    var url="<%=rootPath%>/submap.do?action=readyAddHintMeta&xml=<%=fileName%>";
    var returnValue = showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
    //parent.mainFrame.location.reload();
}
</script>
</head>
<body topmargin="0" leftmargin="0" marginheight="0" marginwidth="0" bgcolor="#CEDFF6">
<form name="topviewForm">
<table width="100%" height="35" border="0" cellspacing="0" cellpadding="0" style="padding:0px;border-top:#CEDFF6 1px solid;border-left:#CEDFF6 1px solid;border-right:#CEDFF6 1px solid;border-bottom:#D6D5D9 1px solid;background-color:#F5F5F5;">
  <tr>
    <td>
	<table width="100%" border="0" cellpadding="0" cellspacing="0" background="<%=rootPath%>/common/images/menubg.jpg">
	  <tr>
	    <td align="left" width="56">
	    </td>
		<td width="56"><input type="button" name="search" class="button_search_out" onmouseover="javascript:buttonSearchOver();" onmouseout="javascript:buttonSearchOut();" onclick="javascript:searchNode();" title="����" <%=disable%>/></td>
		<td width="56"><input type="button" name="save" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" title="���浱ǰ¥������" <%=disable%>/></td>
		<td width="56"><input type="button" name="refresh" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshFile();" title="ˢ�µ�ǰ¥������"/></td>
		<td width="56"><input type="button" name="editmap" class="button_editmap_out" onmouseover="javascript:buttonEditMapOver();" onmouseout="javascript:buttonEditMapOut();" onclick="javascript:editSubMap();" title="¥������"/></td>
		<td width="56"><input type="button" name="create5" class="button_create5_out" onmouseover="javascript:buttonCreate5Over();" onmouseout="javascript:buttonCreate5Out();" onclick="javascript:rebuild();" title="�ؽ���ͼ"/></td>
		<td width="56"><input type="button" name="create6" class="button_create6_out" onmouseover="javascript:buttonCreate6Over();" onmouseout="javascript:buttonCreate6Out();" onclick="javascript:backup();" title="������ͼ"/></td>
		<td width="56"><input type="button" name="create7" class="button_create7_out" onmouseover="javascript:buttonCreate7Over();" onmouseout="javascript:buttonCreate7Out();" onclick="javascript:resume();" title="�ָ���ͼ"/></td>
		<td width="56">
		</td>
		<td width="100">
			<select name="submapview" onchange="changeView()">
			<option value="">--ѡ��¥��--</option>
<%
	StoreyInfoDao dao = new StoreyInfoDao();
	List list = dao.loadAll();
	for(int i=0; i<list.size(); i++)
	{
		StoreyInfo vo = (StoreyInfo)list.get(i);
		int tag = 0;
		if(bids!=null&&bids.length>0){
		    for(int j=0;j<bids.length;j++){
		        if(vo.getBid()!=null&&!"".equals(vo.getBid())&&!"".equals(bids[j])&&vo.getBid().indexOf(bids[j])!=-1){
		            tag++;
		        }
		    }
		}
		if(tag>0&&!fileName.equals(vo.getXml())){
		    out.print("<option value='" + vo.getXml()+ "'>" + vo.getStoreyName()+ "</option>");
		}
	}	
%>
			</select>
		</td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
	  </tr>
	</table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
