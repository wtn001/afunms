<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.application.dao.ClusterDao"%>
<%@page import="com.afunms.application.dao.UpAndDownMachineDao"%>
<%@page import="com.afunms.application.model.Cluster"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="com.afunms.system.dao.SystemConfigDao"%>
<%    
    String rootPath = request.getContextPath(); 
    //StringBuffer url=request.getRequestURL();
  //System.out.println("url====="+url.substring(0,url.lastIndexOf("afunms")));
    //String urlPath = url.substring(0,url.lastIndexOf("afunms"));
    String typeStr = (String)request.getAttribute("typeStr");
    if(typeStr==null)typeStr="������";
    ClusterDao clusterDao = new ClusterDao();
    List poList = clusterDao.loadAll(); // �õ����и��ڵ�
  //System.out.println("mapList==============="+mapList.size());
    User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
	List list = null;
	BusinessDao dao = new BusinessDao();
	try {
		list = dao.loadAll();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		dao.close();
	}
	if (list == null) {
		list = new ArrayList();
	}
	List bussinessList_tmp = new ArrayList();
	String bids_str = ",";
	for (int i = 0; i < list.size(); i++) {
		Business business = (Business) list.get(i);
		if (current_user.getBusinessids() != null && current_user.getBusinessids().contains("," + business.getId() + ",")) {
			bussinessList_tmp.add(business);
		}
		bids_str = bids_str + business.getPid() + ",";
	}
	if(current_user.getRole() != 0) {
		list = bussinessList_tmp;
	}
%>
<html>
<head>
<title></title>
<link rel="StyleSheet" href="../dtree/dtree.css" type="text/css" />
<script type="text/javascript" src="../dtree/dtree.js"></script>
<style>
body {
margin-left: 6px;
margin-top: 0px;
margin-right: 6px;
margin-bottom: 6px;
scrollbar-face-color: #E0E3EB;
scrollbar-highlight-color: #E0E3EB;
scrollbar-shadow-color: #E0E3EB;
scrollbar-3dlight-color: #E0E3EB;
scrollbar-arrow-color: #7ED053;
scrollbar-track-color: #ffffff;
scrollbar-darkshadow-color: #9D9DA1;
}
body,td,th {color: #666666;line-height:20px}
.div_RightMenu
{
    z-index:30000;
    text-align:left;    
    cursor: default;
    position: absolute;
    background-color:#FAFFF8;
    width:100px;
    height:auto;
    border: 1px solid #333333;
    display:none;
    filter:progid:DXImageTransform.Microsoft.Shadow(Color=#333333,Direction=120,strength=5);    
}
.divMenuItem
{
    height:17px;
    color:Black;
    font-family:����;
    vertical-align:middle;
    font-size:10pt;
    margin-bottom:3px;
    cursor:hand;
    padding-left:10px;
    padding-top:2px;
}
</style>
</head>
<body>
<div class="dtree">
<p><a href="javascript: d.openAll();">չ��</a> | <a href="javascript: d.closeAll();">�ϱ�</a> | <a href="javascript: window.location.reload();">ˢ��</a></p>
<script type="text/javascript">
        var currTreeNodeId = '';		// ��ǰ���Ľڵ� Id
        var treeNodeFatherId = '';		// ��ǰ���Ľڵ�ĸ� Id	
        var key = 0 ;
		d = new dTree('d');
		d.add(0,-1,' Ӧ��������');
		
		<%
		
			//String treeshowflag = "0";
			String treeshowflag_str = "0";
			SystemConfigDao systemConfigDao = new SystemConfigDao();
			try{
				treeshowflag_str = systemConfigDao.getSystemCollectByVariablename("treeshowflag");
			} catch(Exception e){
				
			} finally {
				systemConfigDao.close();
			}
			boolean treeshowflag = false;
			if("1".equals(treeshowflag_str)){
				treeshowflag = true;			// ������ʾģʽ �Ժ� ���Ըĳ�  0��1�� 2������������ 
			}
					
			String allBusinessPid = bids_str;	// ���о����ӽڵ�ҵ���id����
			Business currBusiness = null;		// ��ǰҵ��
			String currbid = "";				// ��ǰҵ��id
			String currBusinessNodeId = "";		// ��ǰҵ��ڵ��Id
			String currTreeNodeId = "";			// ��ǰ�ڵ�Id
			boolean isShowTreeNodeFlag = true;	// �Ƿ���ʾ�ýڵ� 	
			boolean rightFrameFlag = true;		// �ұ߿����ʾҳ���ģʽ ��ʱֻ��Ϊ true �Ժ��������ģʽ 
					
			String currTreeNodeFatherId = "";			// ��ǰ�ڵ㸸 Id
			
			int treeNodeNum = 0;				// ���ڵ��� �ڼ���
			L1: for(Object object : list){
				// ѭ��ÿһ��ҵ��
				currBusiness = (Business)object;
				currbid = currBusiness.getId();
				if(currBusiness == null || allBusinessPid.contains(currbid)){
					continue L1;
				}
				if(poList == null || poList.size() == 0){
					return;
				}
				List tempNodeDTOList = new ArrayList();	
		   		for(Object nodeDTOObject : poList){
		   			Cluster nodeDTO = (Cluster)nodeDTOObject;
		   			if(nodeDTO.getBid() != null && nodeDTO.getBid().contains("," + currbid + ",")){
		   				tempNodeDTOList.add(nodeDTO);
		   			}
		   		}
		   		if(tempNodeDTOList == null || tempNodeDTOList.size() == 0){
					continue L1;
				}
				currTreeNodeFatherId = "0";
				currTreeNodeId = "business_" + currbid;
				currBusinessNodeId = currTreeNodeId;
				%>
				 	currTreeNodeId = '<%=currTreeNodeId%>';
      				currTreeNodeFatherId = '<%=currTreeNodeFatherId%>';
				    var imagestr = "";
				    d.add(currTreeNodeId,currTreeNodeFatherId,'<%=" " + currBusiness.getName()%>',"","","","rightFrame",imagestr,imagestr);
				<%
				// ����ǰ�ڵ�id ��ֵ�� ���ڵ�
				currTreeNodeFatherId = currTreeNodeId;
				L2: for(Object treeNodeObject : tempNodeDTOList){
				    
		   			Cluster currTreeNode = (Cluster)treeNodeObject;
		   			
		   			UpAndDownMachineDao upAndDownMachineDao = new UpAndDownMachineDao();
		   			List clusterlist = upAndDownMachineDao.loadClusterList(currTreeNode.getId());
		   			upAndDownMachineDao.close();
		   			currTreeNodeId = currTreeNode.getId()+"";
		   			
		   			String imagestr = rootPath + "/resource/image/maccurrent.gif";
		   			
		   			%>
				 	currTreeNodeId = '<%=currTreeNodeId%>';
      				currTreeNodeFatherId = '<%=currTreeNodeFatherId%>';
				    var imagestr = "<%=imagestr%>";
				    d.add(currTreeNodeId,currTreeNodeFatherId,'<%=" " + currTreeNode.getName()+"("+clusterlist.size()+")"%>',"<%=rootPath
							+ "/topology/machineUpAndDown/submap.jsp?flag=1&id="
							+ currTreeNode.getId()+"&xml="+currTreeNode.getXml()%>","","","mainFrame",imagestr,imagestr);
					<%
		   		
				}	// ���ÿһ���豸���ڵ�ѭ�� (L2)
				
			}	// ���ÿһ��ҵ��ѭ�� (L1)
			
		%>
		document.write(d);
		

//------------search one device-------------����ѡ�е����ڵ��ڵ�ͼ��������Ӧ���豸

function SearchNode(ip)
{
	var coor = window.parent.mainFrame.mainFrame.getNodeCoor(ip);
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
	window.parent.mainFrame.mainFrame.moveMainLayer(coor);
}
//--------------------end--------------------
//--------------------beginѡ���豸��ʾ�Ҽ��˵�--------------------
var nodeid="";
var nodeip="";
var nodecategory="";
function showMenu(id,ip){
    nodeid = id.split(";")[0];
    nodecategory = id.split(";")[1];
    nodeip = ip;
    /**/
    if(document.getElementById("div_RightMenu") == null)
    {    
        CreateMenu();
        document.oncontextmenu = ShowMenu
        document.body.onclick  = HideMenu    
    }
    else
    {
        document.oncontextmenu = ShowMenu
        document.body.onclick  = HideMenu    
    } 

}
function add(){
    var nodeId = nodeid;//Ҫ��֤nodeid�ĳ��ȴ���3
    var coor = window.parent.mainFrame.mainFrame.getNodeId(nodeId);
    if (coor == null)
	{
         window.parent.mainFrame.mainFrame.addEquip(nodeId,nodecategory);
	}
	else if (typeof coor == "string")
	{
		window.alert(coor);
		return;
	}
    window.parent.mainFrame.mainFrame.moveMainLayer(coor);
    window.alert("���豸�Ѿ�������ͼ�д��ڣ�");
}
function detail(){
    showalert(nodeid);
	window.parent.parent.opener.focus();
}
function showalert(id) {
	//window.parent.parent.opener.location="/afunms/detail/dispatcher.jsp?id="+id;
	window.parent.parent.opener.parent.window.document.getElementById('mainFrame').src="/afunms/detail/dispatcher.jsp?id="+id+"&fromtopo=true";
}
function evtMenuOnmouseMove()
{
    this.style.backgroundColor='#8AAD77';
    this.style.paddingLeft='10px';    
}
function evtOnMouseOut()
{
    this.style.backgroundColor='#FAFFF8';
}
function CreateMenu()
{    
        var div_Menu          = document.createElement("Div");
        div_Menu.id           = "div_RightMenu";
        div_Menu.className    = "div_RightMenu";
        
        var div_Menu1         = document.createElement("Div");
        div_Menu1.id          = "div_Menu1";
        div_Menu1.className   = "divMenuItem";
        div_Menu1.onclick     = add;
        div_Menu1.onmousemove = evtMenuOnmouseMove;
        div_Menu1.onmouseout  = evtOnMouseOut;
        div_Menu1.innerHTML   = "��ӵ�����ͼ";
        var div_Menu2         = document.createElement("Div");
        div_Menu2.id          = "div_Menu2";
        div_Menu2.className   = "divMenuItem";
        div_Menu2.onclick     = detail;
        div_Menu2.onmousemove = evtMenuOnmouseMove;
        div_Menu2.onmouseout  = evtOnMouseOut;
        div_Menu2.innerHTML   = "��ϸ��Ϣ";
        
        div_Menu.appendChild(div_Menu1);
        div_Menu.appendChild(div_Menu2);
        document.body.appendChild(div_Menu);
}
// �жϿͻ��������
function IsIE() 
{
    if (navigator.appName=="Microsoft Internet Explorer") 
    {
        return true;
    } 
    else 
    {
        return false;
    }
}

function ShowMenu()
{
    
    if (IsIE())
    {
        document.body.onclick  = HideMenu;
        var redge=document.body.clientWidth-event.clientX;
        var bedge=document.body.clientHeight-event.clientY;
        var menu = document.getElementById("div_RightMenu");
        if (redge<menu.offsetWidth)
        {
            menu.style.left=document.body.scrollLeft + event.clientX-menu.offsetWidth
        }
        else
        {
            menu.style.left=document.body.scrollLeft + event.clientX
            //�����иĶ�
            menu.style.display = "block";
        }
        if (bedge<menu.offsetHeight)
        {
            menu.style.top=document.body.scrollTop + event.clientY - menu.offsetHeight
        }
        else
        {
            menu.style.top = document.body.scrollTop + event.clientY
            menu.style.display = "block";
        }
    }
    return false;
}
function HideMenu()
{
    if (IsIE())  document.getElementById("div_RightMenu").style.display="none";    
}
</script>
</div>
</body>
</html>