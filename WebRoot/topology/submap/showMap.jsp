<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%@page import="javax.imageio.ImageIO"%>
<%@page import="javax.imageio.ImageReader"%>
<%@page import="javax.imageio.stream.ImageInputStream"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.afunms.system.model.User"%>  
<%
	String rootPath = request.getContextPath();
	String fileName = (String)request.getParameter("fileName");//(
	System.out.println("fileName1======22222222222=="+fileName);
	if(fileName!=null){
	    session.setAttribute(SessionConstant.CURRENT_SUBMAP_VIEW,fileName); 
	}
	if(fileName==null){fileName = (String)session.getAttribute(SessionConstant.CURRENT_SUBMAP_VIEW);}
	User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
	ManageXmlDao dao = new ManageXmlDao();
	ManageXml vo = (ManageXml)dao.findByXml(fileName);
	String bg = "";
	String Title = "";
	String width = "100%";
	String height = "100%";
	if(vo!=null){
	    bg = vo.getTopoBg();
	    Title = vo.getTopoTitle();
	}
	/**if(bg != null&&"null".equalsIgnoreCase(bg)&& !"".equals(bg)&& !"0".equals(bg)){
	    String curDir = System.getProperty("user.dir");
		File file = new File(curDir.replace("bin","")+"webapps/afunms/resource/image/bg/"+bg);
		//System.out.println(curDir);
		try {
		    Iterator readers = ImageIO.getImageReadersByFormatName("jpg");
		    ImageReader reader = (ImageReader)readers.next();
		    ImageInputStream iis = ImageIO.createImageInputStream(file);
		    reader.setInput(iis, true);
		    //System.out.println("width:"+reader.getWidth(0));
		    width = ""+reader.getWidth(0);
		    //System.out.println("height:"+reader.getHeight(0));
		    height = ""+reader.getHeight(0);
	   } catch (IOException e) {
	        e.printStackTrace();
	   }
	}*/
	out.println("<script type=\"text/javascript\">");
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
<html xmlns:v="urn:schemas-microsoft-com:vml">
<head>
<meta http-equiv="content-type" content="text/html; charset=gb2312" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>��ʾ��ͼ</title>
<link href="<%=rootPath%>/resource/css/topo_style.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/top.css" type="text/css">
<script type="text/javascript" src="js/profile.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/disable.js"></script>
<script type="text/javascript" src="js/menu.js"></script>
<script type="text/javascript" src="js/map.js"></script>
<script type="text/javascript" src="js/window.js"></script>
<script type="text/javascript" src="js/edit.js"></script>
<script type="text/javascript" src="js/toolbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/util.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/TopoRemoteService.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/LinkRemoteService.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/AlarmSummarize.js"></script>
<!-- DIV�������js -->
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/common/css/styleDIV.css" />
<script type="text/javascript" src="<%=rootPath%>/common/js/jquery-1.4.1.min.js"></script>
<script type="text/javascript" src="<%=rootPath%>/common/js/tipswindown.js"></script>
<script type="text/javascript" src="<%=rootPath%>/common/js/alarmtipswindown.js"></script>
<!-- ����ͼ�˵����������� -->
<link rel="stylesheet" href="<%=rootPath%>/common/css/style2.css" type="text/css" media="screen"/>
<script type="text/javascript" src="<%=rootPath%>/common/js/topo-tool-bar.js"></script> 
<%
    //-----------�ж�ȫ����ʾ״̬----------------
	String fullscreen = request.getParameter("fullscreen");
	if (fullscreen == null || fullscreen.equals("0")) {
		out.println("<script type=\"text/javascript\">var fullscreen = 0;</script>");
	}
	else {
	// �����ȫ����ʾ���޸� viewWidth
		out.println("<script type=\"text/javascript\">var fullscreen = 1;");
		out.println("viewWidth = window.screen.width;</script>");
	}    
	//�������豸������ʾ��Ϣ��IP��������text��
	//g_viewflag��global.js�ж��壬Ĭ��Ϊ0������Ҫ�����
	String viewflag = request.getParameter("viewflag");	
	if (viewflag == null) 
		out.print("<script type=\"text/javascript\">g_viewFlag = 0;</script>");
	else 
		out.print("<script type=\"text/javascript\">g_viewFlag = " + viewflag + ";</script>");	
%>

<script type="text/javascript" src="js/topology.js"></script>
<script type="text/javascript">
	window.onerror = new Function('return true;');		// �ݴ�
	openProcDlg();  //��ʾ����
	var fatherXML = "<%=fileName%>";//yangjun add ��������ͼʱ��ø�ҳxml
	function saveFile() {
	    //alert(1);
		if (!admin) {
			window.alert("��û�б�����ͼ��Ȩ�ޣ�");
			return;
		}
		//parent.mainFrame.saveFile();
		//alert(2);
		resetProcDlg();
		save();  //topoloty.js�еĺ���,���ڱ���ͼ����--->String��
	}
	function doInit()
	{
		loadXML("<%=rootPath%>/resource/xml/<%=fileName%>");
		var autoR = setInterval(autoRefresh1,1000*30*1);
		autoRefresh1();
		var list = document.all.rp_alarm_table;
		if(window.parent.topFrame.topviewForm.checkbox.checked){
	        list.style.marginLeft="90px";
	    } else {
	        list.style.marginLeft="50px";
	    }
	}
	function autoRefresh1()
	{
	      var xml = "<%=fileName%>";
         LinkRemoteService.refreshLink(linkobjArray,{
			callback:function(data){
			    //alert(data);
				if(data){
					tt(data);
				}
			}
		});
         TopoRemoteService.refreshImage(xml,nodeobjArray,{
			callback:function(data){
				if(data){
					replaceNodeobj(data);
				}
			}
		});
		//alert(linkobjArray);
	}
	function autoRefresh()
	{
	     window.location = "showMap.jsp";
	}
     
    //ɾ��ʾ����·
    function deleteLine(id){
        window.location = "<%=rootPath%>/submap.do?action=deleteLines&id="+id+"&xml=<%=fileName%>";
        //window.location = "<%=rootPath%>/submap.do?action=deleteDemoLink&id="+id+"&xml=<%=fileName%>";
        //alert("ɾ���ɹ���");
       // autoRefresh();
    }
    //ɾ��ʵ����·
    function deleteLink(id) {
        //var xml = "<%=fileName%>";
        if (window.confirm("ȷ��ɾ������·��")) {
            window.location = "<%=rootPath%>/submap.do?action=deleteLink&lineId="+id;
            alert("ɾ���ɹ���");
	        autoRefresh();
	    }
    }
    //�༭ʵ����·   
    function editLink(id) {
	    var url="<%=rootPath%>/submap.do?action=readyEditLink&lineId="+id;
        showModalDialog(url,window,'dialogwidth:500px; dialogheight:430px; status:no; help:no;resizable:0');
    }
    
    //���ʵ���豸
    function addEquip(nodeid,nodeCategory){
        //window.location="<%=rootPath%>/submap.do?action=addEquipToSubMap&xml=<%=fileName%>&node="+nodeid+"&category="+nodeCategory;
        var xml = "<%=fileName%>";
        var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
        TopoRemoteService.addEquipToMap(xml, nodeid, nodeCategory,{
				callback:function(data){
					if(data){
						addNode(nodeid,url);
					}
				}
			});
    }
    //ɾ��ʵ���豸
    function deleteEquip(nodeid,category){
        if (window.confirm("�˲����Ὣ���豸����ɾ��,ȷ��ɾ�����豸��")) {
            window.location="<%=rootPath%>/submap.do?action=deleteEquipFromSubMap&node="+nodeid+"&xml=<%=fileName%>&category="+category;
            alert("ɾ���ɹ���");
            autoRefresh();
        }
    }
    //���ʾ���豸(δ��)
    function createGallery(){
        var a = new xWin("1",300,180,200,200,"����ͼԪ","123");
        ShowHide("1",null);
        ShowHide("1","none");
    }
    //ɾ��ʾ���豸
    function deleteHintMeta(id) {
        var xml = "<%=fileName%>";
        if (window.confirm("ȷ��ɾ�����豸��")) {
            window.location = "<%=rootPath%>/submap.do?action=deleteHintMeta&nodeId="+id+"&xml="+xml;
            alert("ɾ���ɹ���");
	        autoRefresh();
	    }
    }
    //ֻ������ͼ�Ƴ�ʵ���豸
    function removeEquip(nodeid){
        if (window.confirm("�˲����Ὣ���豸�ӵ�ǰ����ͼɾ��,ȷ��ɾ�����豸��")) {
            window.location="<%=rootPath%>/submap.do?action=removeEquipFromSubMap&xml=<%=fileName%>&node="+nodeid;
            alert("ɾ���ɹ���");
            autoRefresh();
        }
    }
	//�澯ȷ��
    function confirmAlarm(nodeid,nodeCategory){
        var xml = "<%=fileName%>";
        if (window.confirm("�˲����Ὣ���豸�ĸ澯����ȷ��,ȷ����")) {
            TopoRemoteService.confirmAlarm(xml, nodeid, nodeCategory,{
				callback:function(data){
					if(data=="error"){
						alert("�澯ȷ��ʧ��!");
					} else {
					    replaceNodePic(data);
					}
				}
			});
        }
    }
    //��·�澯ȷ��
    function confirmAlarmLink(lineId){
        var xml = "<%=fileName%>";
        if (window.confirm("�˲����Ὣ����·�ĸ澯��������,ȷ����")) {
            LinkRemoteService.confirmAlarm(xml, lineId,{
				callback:function(data){
					if(data=="error"){
						alert("�澯ȷ��ʧ��!");
					} else {
					    replaceLinkPic(data);
					}
				}
			});
        }
    }
    //�������豸���Ӧ�����
    function addApplication(nodeid,ip){
        //alert(nodeid+"_"+ip);
        window.location="<%=rootPath%>/submap.do?action=addApplications&xml=<%=fileName%>&node="+nodeid+"&ip="+ip;
        alert("��ȡ�÷��������Ӧ�óɹ���");
        autoRefresh();
    }
    //�鿴�豸���ͼ
    function showpanel(ip,width,height){
        window.open("<%=rootPath%>/submap.do?action=showpanel&ip="+ip,"panelfullScreenWindow", "toolbar=no,height="+height+",width="+width + ",scrollbars=no"+"screenX=0,screenY=0");
    }
    //����ʵ����·
    function addLink(direction1,linkName, maxSpeed, maxPer, xml, start_id, start_index, end_id, end_index,linetext,interf){
        var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
        TopoRemoteService.addLink(direction1,linkName, maxSpeed, maxPer, xml, start_id, start_index, end_id, end_index,linetext,interf, {
				callback:function(data){
					if(data=="error"){
						alert("ʵ����·����ʧ�ܣ�");
					} else if(data=="error1"){
					    alert("ʵ����·����ʧ��:��ͬ�˿ڵ���·�Ѿ�����!");
					} else if(data=="error2"){
					    alert("ʵ����·����ʧ��:�Ѿ�����˫��·!");
					} else {
					    if(data){
					        var arr=data.split(":");
					        if(arr[1]=="0"){
					            addlink(arr[0],url);
					        } else {
					            addAssLink(arr[0],url)
					        }
					    }
					}
				}
			});
    }
    //����ʾ����·
    function addline(direction1,xml,line_name,link_width,start_id,start_x_y,s_alias,end_id,end_x_y,e_alias){
        var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
        LinkRemoteService.addDemoLink(direction1,xml,line_name, link_width, start_id, start_x_y, s_alias, end_id, end_x_y, e_alias, {
				callback:function(data){
					if(data=="error"){
						alert("ʾ����·����ʧ�ܣ�");
					} else {
					    if(data){
					        addLines(data,url);
					    }
					}
				}
			});
    }
     //���ʾ���豸
    function addHintMeta(setting){
        var url = "<%=rootPath%>/resource/xml/<%=fileName%>";
        TopoRemoteService.addHintMeta(setting,{
				callback:function(data){
				    if(data=="error"){
						alert("���ʾ��ͼԪʧ�ܣ�");
					} else {
						addNode(data,url);
					}
				}
			});
    }
    
     function showalert(id) {
		//window.parent.parent.opener.location="/afunms/detail/dispatcher.jsp?id="+id;
		window.parent.parent.opener.parent.window.document.getElementById('mainFrame').src="/afunms/detail/dispatcher.jsp?id="+id+"&fromtopo=true";
	}
	//������
	function showTipsWindown(title,id,width,height){
		tipsWindown(title,"id:"+id,width,height,"true","","false",id);
	}
    function showAlarmTipsWindown(title,id,width,height){
		alarmtipsWindown(title,"id:"+id,width,height,"true","","false",id);
	}
	//���������
	function popTips(){
		showTipsWindown("ϵͳ��Ϣ", 'simTestContent', 250, 55);
	}
	function popTipsAlarm(){
		showAlarmTipsWindown("�澯��Ϣ", 'simTestContentAlarm', 250, 55);
	}
	$(document).ready(function(){
		popTips();
		popTipsAlarm();
		var timer1;
		timer1=window.setInterval("getAlarmData();",200*60);	//2���Ӹ���һ��DIV
	});
	
	function getAlarmData(){
	    //popTips();
		AlarmSummarize.getLastestEventList1('<%=current_user.getBusinessids()%>','<%=fileName%>', {
               callback:function(data){
                   //alert(data.length);
                   if (data && data.length) {
                       var i = 0;
                       var length = data.length;
                       
                       var alarmStr="<marquee direction='up' scrollamount='2' onmouseover='this.stop()' onmouseout='this.start()'>";
                       while (i < length) {
                           var eventList = data[i];
                           var alarmLevel=eventList.alarmlevel;
                           var alarm_image = "a_level_0.gif";
                           if (alarmLevel == 1) {
                               alarm_image = "a_level_1.gif";
                           } else if (alarmLevel == 2) {
                               alarm_image = "a_level_2.gif";
                           } else if (alarmLevel == 3) {
                               alarm_image = "a_level_3.gif";
                           } 
                           alarmStr=alarmStr+"<li><img src='<%=rootPath%>/resource/image/topo/"+alarm_image+"'><font color=#000000>"+eventList.content+"</font></li>";
                           i++;
                       }
                       alarmStr=alarmStr+"</marquee>";
                       var alarmInfo=document.getElementById("windown-content-alarm");
                       //alert(alarmInfo.innerHTML);
                       if(alarmInfo){
                           alarmInfo.innerHTML='<div class="mainlist1"><ul id="alarmInfo1">'+alarmStr+'</ul></div>';
                       }
                   }
               }   
           });
	}
</script>
<style>
v\:*{ behavior:url(#default#VML); }
</style>
</head>

<!--����ѡ��ʱ���õ����������ĸ�����-->
<img src="<%=rootPath%>/resource/image/topo/line_top.gif" id="imgTop" class="tmpImg" style="width:10; height:10 " />
<img src="<%=rootPath%>/resource/image/topo/line_left.gif" id="imgLeft" class="tmpImg" style="width:10; height:10 "/>
<img src="<%=rootPath%>/resource/image/topo/line_bottom.gif" id="imgBottom" class="tmpImg" style="width:10; height:10 "/>
<img src="<%=rootPath%>/resource/image/topo/line_right.gif" id="imgRight" class="tmpImg" style="width:10; height:10 "/>
<div style="display:none;">
	<div id="simTestContent" class="simScrollCont">
		<div class="mainlist">
			<ul id="alarmInfo">
				<li>����Ϣ</li>
			</ul>
		</div>
	</div><!--simTestContent end-->
</div>
<div style="display:none;">
	<div id="simTestContentAlarm" class="simScrollCont">
		<div class="mainlist1">
			<ul id="alarmInfo1">
				<li><font color=#000000>����Ҫ�澯��Ϣ</font></li>
			</ul>
		</div>
	</div><!--simTestContent end-->
</div>
<script type="text/javascript">
var img=new Image();
img.src = "<%=rootPath%>/resource/image/bg/<%=bg%>";
var imgWidth = img.width;
var imgHeight = img.height;
<!--
document.write('<form name="frmMap" method="post" action="<%=rootPath%>/submap.do?action=save">');
document.write('<body class="main_body" onLoad="hideMenuBar();doInit();" onmousewheel="window.parent.parent.document.body.scrollTop -= event.wheelDelta/2;" onmousedown="bodyDown()" onselectstart="return false" marginheight="0" marginwidth="0" topmargin="0" leftmargin="0">');	
loadMoveController();		// �����ƶ�������
loadSizeController();		// ���ش�С������
//<div id="divTitle" align="center" style="font:oblique small-caps 900 29pt ����;"><%=Title%></div>
document.write('<table width="100%" style="height:100%;"><tr><td width="100%" align="left" height="100%">');
document.write('<div id="divDrag" style="background-color: #FFFFFF;width:100%;height:100%; top:0px;left:0px; position:absolute;" onmousedown="divLayerDown()" onclick="javascript:closeLineFrame();"></div>');
document.write('<div id="divLayer" style="width:100%;height:' + imgHeight + '; background:url(<%=rootPath%>/resource/image/bg/<%=bg%>) left top no-repeat; top:0px;left:0px; position:absolute;" onmousedown="divLayerDown()" onclick="javascript:closeLineFrame();"></div>');
//document.write('<div id="divLayer" style="width:100%;height:100%; background:url(<//%=rootPath%>/resource/image/bg/<//%=bg%>) left top no-repeat; top:0px;left:0px; position:absolute;" onmousedown="divLayerDown()" onclick="javascript:closeLineFrame();"></div>');
//}
//document.write('<div id="divLayer" style="width:100%;height:' + height1 + '; background:url(<%=rootPath%>/resource/image/bg/<%=bg%>) left top no-repeat; top:0px;left:0px; position:absolute;" onmousedown="divLayerDown()" onclick="javascript:closeLineFrame();"></div>');
//document.write('<div id="divLayer" style="background-position: center;background-attachment:fixed;background-repeat: no-repeat;background-image:url(<%=rootPath%>/resource/image/bg/<%=bg%>);width:<%=width%>;height:<%=height%>;color:black;position:absolute;top:0px;left:0px;background-color:#FFFFFF;border:#F0F8FF; 1px solid;" onmousedown="divLayerDown()" onclick="javascript:closeLineFrame();"></div>');//#000066
document.write('</td><td height="100%" width="3px" align="right"><img src="<%=rootPath%>/common/images/arrow_close.jpg" onclick="hideMenu();"></td><td align="right" height="100%">');
document.write('<div id="container-menu-bar" style="height:100%;width:200px;"></div>');
document.write('</td></tr></table>');
document.write('<input type="hidden" name="hidXml"/>');
document.write('</body></form>');
createGallery();
//-->
</script>
<script type="text/javascript">
	function hideMenu(){
		var element = document.getElementById("container-menu-bar").parentElement;
		var display = element.style.display;
		if(display == "inline"){
			hideMenuBar();
		}else{
			showMenuBar();
		}
	}
	
	function showMenuBar(){
		var element = document.getElementById("container-menu-bar").parentElement;
		element.style.display = "inline";
		document.getElementById("container-menu-bar").innerHTML="<iframe src='<%=rootPath%>/topology/network/indicatortree.jsp?treeflag=<%=viewflag%>&fromtopo=true&filename=<%=fileName%>' height='100%' width='200px'/>";
	}
	
	function hideMenuBar(){
		var element = document.getElementById("container-menu-bar").parentElement;
		element.style.display = "none";
	}
	AlarmSummarize.getLastestEventList1('<%=current_user.getBusinessids()%>' , {
               callback:function(data){
                   //alert(data.length);
                   if (data && data.length) {
                       var i = 0;
                       var length = data.length;
                       
                       var alarmStr="<marquee direction='up' scrollamount='2' onmouseover='this.stop()' onmouseout='this.start()'>";
                       while (i < length) {
                           var eventList = data[i];
                           var alarmLevel=eventList.alarmlevel;
                           var alarm_image = "alarm_level_1.gif";
                           //alert("alarmLevel==="+alarmLevel);
                           if (alarmLevel == 1) {
                               alarm_image = "alarm_level_1.gif";
                           } else if (alarmLevel == 2) {
                               alarm_image = "alarm_level_2.gif";
                           } else if (alarmLevel == 3) {
                               alarm_image = "alert.gif";
                           }
                           alarmStr=alarmStr+"<li><img src='<%=rootPath%>/resource/image/topo/"+alarm_image+"'>"+eventList.content+"</li>";
                           i++;
                       }
                       alarmStr=alarmStr+"</marquee>";
                       var alarmInfo=document.getElementById("alarmInfo");
                       alarmInfo.innerHTML=alarmStr;
                   }
               }   
           });
</script>
<script type="text/javascript">
<!--
// ���� divLayer ��С
function resizeTopDiv() {
		//document.all.divLayer.style.width = maxWidth + 1024;
		//document.all.divLayer.style.height = maxHeight + 1024;
		zoomProcDlg("out");
}
	
	setTimeout("resizeTopDiv()", 1000);	
	showControllerTool(false);
	function showDevice(action) {
		parent.location = action;
	}
	// ˢ������ͼ
	function refreshFile() 
	{
		if (window.confirm("��ˢ�¡�ǰ�Ƿ���Ҫ���浱ǰ����ͼ��")) {
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
	function cwin()
    {
	     if(parent.parent.search.cols!='230,*')
	     {
	        parent.parent.search.cols='230,*';
	        document.all.pic.style.backgroundImage = 'url("<%=rootPath%>/resource/image/hide_menu.gif")';
	        document.all.pic.title="��������";
	     }
	     else
	     {
	        parent.parent.search.cols='0,*';
	        document.all.pic.style.backgroundImage = 'url("<%=rootPath%>/resource/image/show_menu.gif")';
	        document.all.pic.title="��ʾ����";
	     }
    }
    function searchNode()
	{	
		var ip = window.prompt("��������Ҫ�������豸IP��ַ", "�ڴ������豸IP��ַ");
		if (ip == null)
			return true;
		else if (ip == "�ڴ������豸IP��ַ")
			return;
	
		if (!checkIPAddress(ip))
			searchNode();
	
		var coor = getNodeCoor(ip);
		if (coor == null)
		{
			var msg = "û����ͼ��������IP��ַΪ "+ ip +" ���豸��";
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
	
	// �л���ͼ
	function changeName() 
	{
		// ֮ǰ�����û�����
		if (admin) {
			if (window.confirm("���л���ͼ��ǰ�Ƿ���Ҫ���浱ǰ����ͼ��")) {
				saveFile();
			}
		}
		var curTarget = "showMap.jsp?fullscreen=<%=fullscreen%>";
		if (g_viewFlag == 0) {
			g_viewFlag = 1;
			window.parent.parent.leftFrame.location = "../network/tree.jsp?treeflag=1";
			//alert(curTarget+"&viewflag=1");
			window.location = curTarget+"&viewflag=1";
		}
		else if (g_viewFlag == 1) {
			g_viewFlag = 0;
			window.parent.parent.leftFrame.location = "../network/tree.jsp?treeflag=0";		
			window.location = curTarget+"&viewflag=0";
		}
		else {    
			window.alert("��ͼ���ʹ���");
		}
	}
	//����ʵ����·
function createEntityLink(){
    var objLinkAry = new Array();
    if(window.parent.frames['mainFrame'].objMoveAry!=null&&window.parent.frames['mainFrame'].objMoveAry.length>0){//��ѡ
        objLinkAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry!=null&&window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrlѡ
        objLinkAry = window.parent.frames['mainFrame'].objEntityAry;
    }
    if(objLinkAry==null||objLinkAry.length!=2){
        alert("��ѡ�������豸��");
        return;
    }
    if(objLinkAry[0].name.substring(objLinkAry[0].name.lastIndexOf(",")+1)=="1"){
        alert("��ѡ���ʾ���豸!");
        return;
    }
    var start_id = objLinkAry[0].id.replace("node_","");
    if(objLinkAry[1].name.substring(objLinkAry[1].name.lastIndexOf(",")+1)=="1"){
        alert("��ѡ���ʾ���豸!");
        return;
    }
    var end_id = objLinkAry[1].id.replace("node_","");
    if(start_id.indexOf("net")==-1||end_id.indexOf("net")==-1){
        alert("��ѡ�������豸!");
        return;
    }   
    var xml = "<%=fileName%>";
    var url="<%=rootPath%>/submap.do?action=readyAddLink&start_id="+start_id+"&end_id="+end_id+"&xml="+xml;
    showModalDialog(url,window,'dialogwidth:500px; dialogheight:400px; status:no; help:no;resizable:0');
}
//����ʾ����·
function createDemoLink(){
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
        alert("ѡ�е���̨�豸�Ѿ�����ʾ����·!");
        return;
    }
    var start_x_y=objEntityAry[0].style.left+","+objEntityAry[0].style.top;
    var end_x_y=objEntityAry[1].style.left+","+objEntityAry[1].style.top;
    //alert(start_x_y+"="+end_x_y);
    var url="<%=rootPath%>/link.do?action=readyAddLine&xml="+xml+"&start_id="+start_id+"&end_id="+end_id+"&start_x_y="+start_x_y+"&end_x_y="+end_x_y;
    showModalDialog(url,window,'dialogwidth:510px; dialogheight:350px; status:no; help:no;resizable:0');
    //parent.mainFrame.location = "<%=rootPath%>/submap.do?action=addLines&xml="+xml+"&id1="+start_id+"&id2="+end_id;
    //alert("��·�����ɹ���");
    //parent.mainFrame.location.reload();
}
//����ͼ����
function editSubMap(){
    if("<%=fileName%>"=="testData.jsp"){
        alert("��ѡ����ͼ!");
    } else {
        var url="<%=rootPath%>/submap.do?action=readyEditSubMap&xml=<%=fileName%>";
        showModalDialog(url,window,'dialogwidth:500px; dialogheight:400px; status:no; help:no;resizable:0');
    }
    
}
function checkEntityLink(){
    var objLinkAry = new Array();
    if(window.parent.frames['mainFrame'].objMoveAry!=null&&window.parent.frames['mainFrame'].objMoveAry.length>0){//��ѡ
        objLinkAry = window.parent.frames['mainFrame'].objMoveAry;
    }
    if(window.parent.frames['mainFrame'].objEntityAry!=null&&window.parent.frames['mainFrame'].objEntityAry.length>0){//ctrlѡ
        objLinkAry = window.parent.frames['mainFrame'].objEntityAry;
    }
    if(objLinkAry==null||objLinkAry.length!=2){
        alert("��ѡ�������豸��");
        return;
    }
    if(objLinkAry[0].name.substring(objLinkAry[0].name.lastIndexOf(",")+1)=="1"){
        alert("��ѡ���ʾ���豸!");
        return;
    }
    var start_id = objLinkAry[0].id.replace("node_","");
    if(objLinkAry[1].name.substring(objLinkAry[1].name.lastIndexOf(",")+1)=="1"){
        alert("��ѡ���ʾ���豸!");
        return;
    }
    var end_id = objLinkAry[1].id.replace("node_","");
    if(start_id.indexOf("net")==-1||end_id.indexOf("net")==-1){
        alert("��ѡ�������豸!");
        return;
    }   
    var url="<%=rootPath%>/topology/network/linkAnalytics.jsp?start_id="+start_id+"&end_id="+end_id;
    showModalDialog(url,window,'dialogwidth:670px; dialogheight:370px; status:no; help:no;resizable:0');
}
	//ɾ������ͼ
	function deleteSubMap(){
	    if (window.confirm("ȷ��ɾ��������ͼ��")) {
			window.location = "<%=rootPath%>/submap.do?action=deleteSubMap&xml=<%=fileName%>";
			alert("�����ɹ�!");
			window.parent.parent.location = "../network/index.jsp";
		}
	}
	//�ؽ�����ͼ
	function rebuild(){
	    if (window.confirm("�ò��������¹�����ǰ����ͼ��·����������")) {
			window.location = "<%=rootPath%>/submap.do?action=reBuildSubMap&xml=<%=fileName%>";
			alert("�����ɹ�!");
	        parent.location.reload();
		}
	}
	//��������ͼ
	function backup(){
	    var url="<%=rootPath%>/submap.do?action=readybackup&xml=<%=fileName%>";
	    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
	}
	//�ָ�����ͼ
	function resume(){
	    var url="<%=rootPath%>/submap.do?action=readyresume&xml=<%=fileName%>";
	    showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
	}
	//����ʾ��ͼԪ
	function createDemoObj(){
	    //window.parent.mainFrame.ShowHide("1",null);��ק��ʽ
	    
	    var url="<%=rootPath%>/submap.do?action=readyAddHintMeta&xml=<%=fileName%>";
	    var returnValue = showModalDialog(url,window,'dialogwidth:500px; dialogheight:300px; status:no; help:no;resizable:0');
	    //parent.mainFrame.location.reload();
	}
	// ��ʾ��ͼ������
	function showControllerTool(flag) {
	
		var result;
		if (flag == false)
			controller = false;
		if (controller) {
			result = showController(controller);
			
			if (result == false) {
				window.alert("��û��ѡ����ͼ���޿���������");
				return;
			}
				
			//document.all.controller.value = "�رտ�����";
			document.all.controller.title = "�ر���ʾ���ڵ���ͼ������";
			controller = false;
		}
		else {
			result = showController(controller);
			
			if (result == false) {
				window.alert("��û��ѡ����ͼ���޿���������");
				return;
			}
	
			//document.all.controller.value = "����������";
			document.all.controller.title = "������ʾ���ڵ���ͼ������";
			controller = true;
		}
	}
//-->
</script>
<style>
a{text-decoration:none;}
table.menu
{
font-size:100%;
position:absolute;
visibility:hidden;
background:#ECECEC;
align:right;
}
</style>
<div id="rp_list" class="rp_list" align="right" >
	<ul>
		<li>
			<div id="rp_alarm_table">
				<table width="100%" height="100%" id="alarm-bar">
				    <tr>
						<td align="left">
						    <a href="#" onClick="javascript:cwin();">&nbsp;&nbsp;&nbsp;��ʾ����</a>
						</td>
						<td align="right">
							<input id="pic" type="button" name="showtree" class="button_showtree_out" onmouseover="javascript:buttonShowTreeOver();" onmouseout="javascript:buttonShowTreeOut();" onclick="javascript:cwin();" title="��ʾ����"/>
						</td>
					</tr>
					<tr>
					    <td align="left">
						    <a href="#">&nbsp;&nbsp;&nbsp;����</a>
						</td>
						<td align="right">
						    <input type="button" name="search" class="button_search_out" onmouseover="javascript:buttonSearchOver();" onmouseout="javascript:buttonSearchOut();" onclick="javascript:searchNode();" title="����"/>
						</td>
					</tr>
					<tr>
              <td align="left">
                  <a href="#" onClick="javascript:setSelect();">&nbsp;&nbsp;&nbsp;ѡ���豸</a>
              </td>
              <td align="right">
                  <input type="button" name="select" class="button_select_out" onmouseover="javascript:buttonSelectOver();" onmouseout="javascript:buttonSelectOut();" onclick="javascript:setSelect();" title="ѡ���豸"/>
              </td>
          </tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:saveFile();">&nbsp;&nbsp;&nbsp;��������ͼ</a>
						</td>
						<td align="right">
							<input type="button" name="save" class="button_save_out" onmouseover="javascript:buttonSaveOver();" onmouseout="javascript:buttonSaveOut();" onclick="javascript:saveFile();" title="���浱ǰ����ͼ����"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:refreshFile();">&nbsp;&nbsp;&nbsp;ˢ������ͼ</a>
						</td>
						<td align="right">
							<input type="button" name="refresh" class="button_refresh_out" onmouseover="javascript:buttonRefreshOver();" onmouseout="javascript:buttonRefreshOut();" onclick="javascript:refreshFile();" title="ˢ�µ�ǰ����ͼ����"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:changeName();">&nbsp;&nbsp;&nbsp;�ı��豸��</a>
						</td>
						<td align="right">
							<input type="button" name="view" class="button_view_out" onmouseover="javascript:buttonViewOver();" onmouseout="javascript:buttonViewOut();" onclick="javascript:changeName();" title="�ı��豸����ʾ��Ϣ"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:editSubMap();">&nbsp;&nbsp;&nbsp;����ͼ����</a>
						</td>
						<td align="right">
							<input type="button" name="editmap" class="button_editmap_out" onmouseover="javascript:buttonEditMapOver();" onmouseout="javascript:buttonEditMapOut();" onclick="javascript:editSubMap();" title="����ͼ����"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:editMap();">&nbsp;&nbsp;&nbsp;ɾ������ͼ</a>
						</td>
						<td align="right">
							<input type="button" name="delmap" class="button_delmap_out" onmouseover="javascript:buttonDelMapOver();" onmouseout="javascript:buttonDelMapOut();" onclick="javascript:deleteSubMap();" title="ɾ������ͼ"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:createEntityLink();">&nbsp;&nbsp;&nbsp;����ʵ����·</a>
						</td>
						<td align="right">
							<input type="button" name="create1" class="button_create1_out" onmouseover="javascript:buttonCreate1Over();" onmouseout="javascript:buttonCreate1Out();" onclick="javascript:createEntityLink();" title="����ʵ����·"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:createDemoLink();">&nbsp;&nbsp;&nbsp;����ʾ����·</a>
						</td>
						<td align="right">
							<input type="button" name="create2" class="button_create2_out" onmouseover="javascript:buttonCreate2Over();" onmouseout="javascript:buttonCreate2Out();" onclick="javascript:createDemoLink();" title="����ʾ����·"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:createDemoObj();">&nbsp;&nbsp;&nbsp;����ʾ��ͼԪ</a>
						</td>
						<td align="right">
							<input type="button" name="create3" class="button_create3_out" onmouseover="javascript:buttonCreate3Over();" onmouseout="javascript:buttonCreate3Out();" onclick="javascript:createDemoObj();" title="����ʾ��ͼԪ"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:rebuild();">&nbsp;&nbsp;&nbsp;�ؽ�����ͼ</a>
						</td>
						<td align="right">
							<input type="button" name="create5" class="button_create5_out" onmouseover="javascript:buttonCreate5Over();" onmouseout="javascript:buttonCreate5Out();" onclick="javascript:rebuild();" title="�ؽ�����ͼ"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:backup();">&nbsp;&nbsp;&nbsp;��������ͼ</a>
						</td>
						<td align="right">
							<input type="button" name="create6" class="button_create6_out" onmouseover="javascript:buttonCreate6Over();" onmouseout="javascript:buttonCreate6Out();" onclick="javascript:backup();" title="��������ͼ"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:resume();">&nbsp;&nbsp;&nbsp;�ָ�����ͼ</a>
						</td>
						<td align="right">
							<input type="button" name="create7" class="button_create7_out" onmouseover="javascript:buttonCreate7Over();" onmouseout="javascript:buttonCreate7Out();" onclick="javascript:resume();" title="�ָ�����ͼ"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:checkEntityLink();">&nbsp;&nbsp;&nbsp;��·ͬ��</a>
						</td>
						<td align="right">
							<input type="button" name="create8" class="button_create1_out" onmouseover="javascript:buttonCreate1Over();" onmouseout="javascript:buttonCreate1Out();" onclick="javascript:checkEntityLink();" title="��·ͬ��"/>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#">&nbsp;&nbsp;&nbsp;ȫ���ۿ�</a>
						</td>
						<td align="right">
							<%if (fullscreen == null || fullscreen.equals("0")) {%>
								<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:gotoFullScreen();" title="ȫ���ۿ���ͼ"/>
							<%} else {%>
								<input type="button" name="fullscreen" class="button_fullscreen_out" onmouseover="javascript:buttonFullscreenOver();" onmouseout="javascript:buttonFullscreenOut();" onclick="javascript:window.parent.close();" value="�ر�" title="�رյ�ǰ����"/>
							<%}%>
						</td>
					</tr>
					<tr>
						<td align="left">
						    <a href="#" onClick="javascript:showControllerTool();">&nbsp;&nbsp;&nbsp;�ر���ͼ������</a>
						</td>
						<td align="right">
							<input type="button" name="controller" class="button_controller_out" onmouseover="javascript:buttonControllerOver();" onmouseout="javascript:buttonControllerOut();" onclick="javascript:showControllerTool();" title="�ر���ʾ���ڵ���ͼ������"/>
						</td>
					</tr>
				</table>
			</div>
		</li>
	</ul>
</div>
</html>
