<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.dao.TreeNodeDao"%>
<%@page import="com.afunms.topology.model.TreeNode"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.common.util.SessionConstant"%>    
<%@page import="com.afunms.system.model.User"%>   
<%@page import="com.afunms.config.dao.BusinessDao"%>
<%@page import="com.afunms.config.model.Business"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>      
<%@page import="com.afunms.event.dao.CheckEventDao"%>
<%@page import="com.afunms.indicators.util.Constant"%>
<%@page import="com.afunms.system.dao.SystemConfigDao"%>
<%@page import="com.afunms.topology.util.XmlOperator"%>
<%@page import="com.afunms.alarm.util.AlarmIndicatorsUtil"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>
<%@page import="com.afunms.topology.dao.ManageXmlDao"%>
<%@page import="com.afunms.topology.model.ManageXml"%>
<%
	String rootPath = request.getContextPath();
	//System.out.println("rootPath=="+rootPath);
	String treeFlag = request.getParameter("treeflag");
	String filename = request.getParameter("filename");
	List nodelist = null;
	try {
		XmlOperator xmlOpr = new XmlOperator();
		xmlOpr.setFile(filename);
		xmlOpr.init4updateXml();
		nodelist = xmlOpr.getAllNode();
	} catch (Exception e) {
		e.printStackTrace();
	}
	ManageXml topovo = null;
	ManageXmlDao manageXmlDao = new ManageXmlDao();
	try {
		topovo = (ManageXml)manageXmlDao.findByXml(filename);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		manageXmlDao.close();
	}
	if(treeFlag==null) treeFlag = "0";
	//StringBuffer url = request.getRequestURL();
	//String urlPath = url.substring(0, url.lastIndexOf("afunms"));
	User current_user = (User) session.getAttribute(SessionConstant.CURRENT_USER);
%>
<html>
<head>
<title></title>
<link rel="StyleSheet" href="<%=rootPath%>/topology/dtree/dtree.css" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/topology/dtree/dtree.js"></script>
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
<form name="mainForm" method="post">
<div class="dtree">
<p><a href="javascript: d.openAll();">չ��</a> | <a href="javascript: d.closeAll();">�ϱ�</a></p>
<script type="text/javascript">
        var currTreeNodeId = '';		// ��ǰ���Ľڵ� Id
        var treeNodeFatherId = '';		// ��ǰ���Ľڵ�ĸ� Id	
        var key = 0 ;
		d = new dTree('d');
		d.add(0,-1,' �豸��Դ��');
		
		<%
			String treeshowflag_str = "0";
			SystemConfigDao systemConfigDao = new SystemConfigDao();
			try{
				treeshowflag_str = systemConfigDao.getSystemCollectByVariablename("treeshowflag");
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				systemConfigDao.close();
			}
			boolean treeshowflag = false;
			if("1".equals(treeshowflag_str)){
				treeshowflag = true;			// ������ʾģʽ �Ժ� ���Ըĳ�  0��1��2������������ 
			}
					
			String currBusinessNodeId = "";		// ��ǰҵ��ڵ��Id
			List treeNodeList = null;			// ���нڵ��б�
			String currTreeNodeId = "";			// ��ǰ�ڵ�Id
			boolean isShowTreeNodeFlag = true;	// �Ƿ���ʾ�ýڵ� 	
			boolean rightFrameFlag = true;		// �ұ߿����ʾҳ���ģʽ ��ʱֻ��Ϊ true �Ժ��������ģʽ 
					
			String currTreeNodeFatherId = "";			// ��ǰ�ڵ㸸 Id
			TreeNodeDao treeNodeDao = new TreeNodeDao();
			try{
				treeNodeList = treeNodeDao.loadAll(); // ��ȡ���еĽڵ�
			}catch(Exception e){
				e.printStackTrace();
			} finally {
				treeNodeDao.close();
			}
			
			NodeUtil nodeUtil = new NodeUtil();
			nodeUtil.setSetedMonitorFlag(true);
			nodeUtil.setMonitorFlag("1");
			int treeNodeNum = 0;				// ���ڵ��� �ڼ���
			if(treeNodeList == null || treeNodeList.size() == 0){
				return;
			}
			
			// ����ǰ�ڵ�id ��ֵ�� ���ڵ�
			currTreeNodeFatherId = currTreeNodeId;
			L2: for(Object treeNodeObject : treeNodeList){
			    try{
			        // ѭ��ÿһ���豸���ڵ�
					TreeNode currTreeNode = (TreeNode)treeNodeObject;
			   		List nodeDTOList = nodeUtil.getByNodeTag(currTreeNode.getNodeTag(), currTreeNode.getCategory(),currTreeNode.getName(),nodelist);
			   		//List nodeDTOList = nodeUtil.conversionToNodeDTO(nodeList);
			   		
			   		if(nodeDTOList == null){
			   			nodeDTOList = new ArrayList();
			   		}   
			   		
			   		List tempNodeDTOList = new ArrayList();		// ��ʱ�洢node
			   		for(Object nodeDTOObject : nodeDTOList){
			   			NodeDTO nodeDTO = (NodeDTO)nodeDTOObject;
			   			tempNodeDTOList.add(nodeDTO);
			   		}
			   		nodeDTOList = tempNodeDTOList;
			   		currTreeNode.setDeceiveNum(nodeDTOList.size()+"");
				 	isShowTreeNodeFlag = true;
				 	if("0".equals(currTreeNode.getDeceiveNum())){
				 		// ����豸��Ϊ 0 �� �� ��ʾģʽ�ĸ�ֵ��
				 		isShowTreeNodeFlag = treeshowflag;
					}
					System.out.println(currTreeNode.getName()+"=========="+currTreeNode.getCategory()+"=========="+currTreeNode.getDeceiveNum()+"===="+isShowTreeNodeFlag);
					// ����ǰ�ڵ㸳ֵ Ϊ �ø��ڵ� + "_" + �ýڵ�id;
					currTreeNodeId = currBusinessNodeId + "_" + currTreeNode.getId();
					currTreeNodeFatherId = currBusinessNodeId + "_" + currTreeNode.getFatherId();
					if(0 == currTreeNode.getFatherId()){
						currTreeNodeFatherId = currBusinessNodeId;
					}
					if(isShowTreeNodeFlag){
						%>
					 	currTreeNodeId = '<%=currTreeNodeId%>';
	      				treeNodeFatherId = '<%=currTreeNodeFatherId%>';
					    var imagestr = "<%=rootPath%>/topology/<%=NodeHelper.getTypeImage(currTreeNode.getName())%>";
					 
					    d.add(currTreeNodeId,treeNodeFatherId,'<%=" " + currTreeNode.getText() + "("
								+ currTreeNode.getDeceiveNum() + ")"%>',"","","","",imagestr,imagestr);
						
						<%
						treeNodeNum++;
						currTreeNodeFatherId = currTreeNodeId;
						if("1".equals(currTreeNode.getIsHaveChild())){       
				    	// �����κ���
				    	} else {
							L3:for(Object nodeDTOObject : nodeDTOList){
							    try{
						   			NodeDTO nodeDTO = (NodeDTO)nodeDTOObject;
						   			
						   			List alarmlist = null;
									AlarmIndicatorsUtil alarmIndicatorsUtil = new AlarmIndicatorsUtil();
									try {
										alarmlist = alarmIndicatorsUtil.getAlarmInicatorsThresholdForNode(nodeDTO.getId()+"", nodeDTO.getType(), nodeDTO.getSubtype());
									} catch (Exception e) {
										e.printStackTrace();
									}
						   			
						   			currTreeNodeId = currTreeNode.getNodeTag() + "_" + nodeDTO.getId();
						   			
						   			String imagestr = "";
						   			System.out.println("nodeDTO.getType()====="+nodeDTO.getType());
						   			if(Constant.TYPE_GATEWAY.equals(nodeDTO.getType()) || Constant.TYPE_F5.equals(nodeDTO.getType()) 
						   				|| Constant.TYPE_VPN.equals(nodeDTO.getType()) || Constant.TYPE_HOST.equals(nodeDTO.getType()) 
						   				|| Constant.TYPE_NET.equals(nodeDTO.getType()) || Constant.TYPE_DB.equals(nodeDTO.getType())
						   				|| Constant.TYPE_VIRTUAL.equals(nodeDTO.getType())){
						   				imagestr = rootPath + "/topology/" + NodeHelper.getSubTypeImage(nodeDTO.getSubtype());  
						   				//System.out.println("nodeDTO.getIpaddress()===="+nodeDTO.getIpaddress());
						   				//System.out.println("treeFlag==="+treeFlag+"===nodeDTO.getName()===="+nodeDTO.getName().trim()+"==currTreeNode.getNodeTag()=="+currTreeNode.getNodeTag()+"==="+nodeDTO.getId());
						   			}   
						   			//System.out.println("imagestr========"+imagestr);
						   			%>
								 	currTreeNodeId = '<%=currTreeNodeId%>';
				      				treeNodeFatherId = '<%=currTreeNodeFatherId%>';
								    var imagestr = "<%=imagestr%>";
								   
								    	d.add(currTreeNodeId,treeNodeFatherId,"<%="1".equals(treeFlag)?nodeDTO.getName().trim():nodeDTO.getIpaddress()%>","",'<%=nodeDTO.getIpaddress()%>',"<%=currTreeNode.getNodeTag()+nodeDTO.getId()+";"+currTreeNode.getName()%>","",imagestr,imagestr);
									<%
									//yangjun add
									treeNodeNum++;
							        String curralarmTreeNodeFatherId = currTreeNodeId;
							        if(alarmlist == null){   
							   			alarmlist = new ArrayList();
							   		}
							   		
							        L4:for(Object nodeObject : alarmlist){
							            try{
							                AlarmIndicatorsNode alarmnode = (AlarmIndicatorsNode)nodeObject;
							                //System.out.println("alarmlist=======111=getDescr:"+alarmnode.getDescr());
								            String checked = alarmIndicatorsUtil.getShowIndicators(topovo.getId()+"",alarmnode.getId()+"",null);
									        %>
									        
										 	currTreeNodeId = '<%=alarmnode.getId()%>';
						      				treeNodeFatherId = '<%=curralarmTreeNodeFatherId%>';
										    var namestr = "<input type='checkbox' name='check' value='<%=alarmnode.getId()+":"+alarmnode.getNodeid()%>' <%=checked%>><%=alarmnode.getDescr()%>";
										    //alert(currTreeNodeId+"==="+treeNodeFatherId+"==="+namestr);
										    d.add(currTreeNodeId,treeNodeFatherId,namestr,"","","","","");
										    
											<%
											//System.out.println(currTreeNodeId+"==="+currTreeNodeFatherId+"=====");
							            }catch(Exception e){
							                e.printStackTrace();
							            }
							        } // ��� ÿһ���豸���ָ�� ѭ�� (L4)
							    }catch(Exception e){
							        e.printStackTrace();
							    }
							} // ��� ÿһ���豸 ѭ�� (L3)
					   	}
					}
			    }catch(Exception e){
			        e.printStackTrace();
			    }
		    }	// ���ÿһ���豸���ڵ�ѭ�� (L2)
		%>
		document.write(d);

//------------search one device-------------����ѡ�е����ڵ��ڵ�ͼ��������Ӧ���豸

function SearchNode(ip)
{
	//var coor = window.parent.mainFrame.mainFrame.getNodeCoor(ip);
	//if (coor == null)
	//{
	//	var msg = "û����ͼ��������IP��ַΪ "+ ip +" ���豸��";
	//	window.alert(msg);
	//	return;
	//}
	//else if (typeof coor == "string")
	//{
	//	window.alert(coor);
	//	return;
	//}
	//window.parent.mainFrame.mainFrame.moveMainLayer(coor);
}
//--------------------end--------------------
//--------------------beginѡ���豸��ʾ�Ҽ��˵�--------------------
var nodeid="";
var nodeip="";
var nodecategory="";
function showMenu(id,ip){
    //nodeid = id.split(";")[0];
    //nodecategory = id.split(";")[1];
   // nodeip = ip;
    /**/
    if(document.getElementById("div_RightMenu") == null)
    {    
        //CreateMenu();
        //document.oncontextmenu = ShowMenu
        //document.body.onclick  = HideMenu    
    }
    else
    {
        //document.oncontextmenu = ShowMenu
        //document.body.onclick  = HideMenu    
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
function save(){
    mainForm.action = "<%=rootPath%>/submap.do?action=savetree";
    mainForm.submit();
    //window.location.reload();
}
function selectAll(){
    //alert(document.all.check[0].checked);
    if(document.all.check.length>0){
        for(var i=0;i<document.all.check.length;i++){
            document.all.check[i].checked="checked";
        }
    }
}
function diselectAll(){
    if(document.all.check.length>0){
        for(var i=0;i<document.all.check.length;i++){
            document.all.check[i].checked="";
        }
    }
}
</script>
</div>
<table>
<tr>
	<TD nowrap colspan="4" align=center>
	    <input type="hidden" name='topoid' value="<%=topovo.getId()%>"/>
	    <input type="hidden" name='filename' value="<%=filename%>"/>
		<br>
		<input type="button" value="ȫѡ" style="width:50" class="formStylebutton" onclick="selectAll()">
		<input type="button" value="��ѡ" style="width:50" class="formStylebutton" onclick="diselectAll()">
		<input type="button" value="����" style="width:50" class="formStylebutton" onclick="save()">
	</TD>	
</tr>
</table>
</form>
</body>
</html>