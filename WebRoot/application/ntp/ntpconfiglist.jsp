<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  Hashtable ntpHash = (Hashtable)request.getAttribute("ntpHash");
  String flag = (String)request.getAttribute("flag");

%>

<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="javascript">	
  
  var delAction = "<%=rootPath%>/network.do?action=webdelete";
  var listAction = "<%=rootPath%>/network.do?action=list";
  
  function doQuery()
  {  
     if(mainForm.key.value=="")
     {
     	alert("�������ѯ����");
     	return false;
     }
     mainForm.action = "<%=rootPath%>/network.do?action=find";
     mainForm.submit();
  }
  
  function doChange()
  {
     if(mainForm.view_type.value==1)
        window.location = "<%=rootPath%>/topology/network/index.jsp";
     else
        window.location = "<%=rootPath%>/topology/network/port.jsp";
  }

  function toAdd()
  {
      mainForm.action = "<%=rootPath%>/ntp.do?action=ready_add";
      mainForm.submit();
  }
  
  function toDelete()
  {
     if(confirm('�Ƿ�ɾ���豸��')){
     	mainForm.action = "<%=rootPath%>/ntp.do?action=delete";
   	    mainForm.submit();
     }
  }

</script>
<script language="JavaScript">

	//��������
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*���ݴ����id��ʾ�Ҽ��˵�
	*/
	function showMenu(id,nodeid,ip)
	{	
		ipaddress=ip;
		node=nodeid;
		//operate=oper;
	    if("" == id)
	    {
	        popMenu(itemMenu,100,"100");
	    }
	    else
	    {
	        popMenu(itemMenu,100,"1111");
	    }
	    event.returnValue=false;
	    event.cancelBubble=true;
	    return false;
	}
	/**
	*��ʾ�����˵�
	*menuDiv:�Ҽ��˵�������
	*width:����ʾ�Ŀ��
	*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //���������˵�
	    var pop=window.createPopup();
	    //���õ����˵�������
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //��õ����˵�������
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //ѭ������ÿ�е�����
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //������ø��в���ʾ����������һ
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //�����Ƿ���ʾ����
	        rowObjs[i].style.display=(hide)?"none":"";
	        //������껬�����ʱ��Ч��
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //������껬������ʱ��Ч��
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //���β˵��Ĳ˵�
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //ѡ���Ҽ��˵���һ��󣬲˵�����
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //��ʾ�˵�
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	function detail(){
		mainForm.action ="<%=rootPath%>/ntp.do?action=ntpevent&id="+node;
		mainForm.submit();
	}
	function edit()
	{
		mainForm.action ="<%=rootPath%>/ntp.do?action=ready_edit&id="+node;
		mainForm.submit();
	}
	function cancelmanage()
	{
		mainForm.action ="<%=rootPath%>/ntp.do?action=cancelalert&id="+node;
		mainForm.submit();
	}
	function addmanage()
	{
		mainForm.action ="<%=rootPath%>/ntp.do?action=addalert&id="+node;
		mainForm.submit();
	}	
	function clickMenu()
	{
	}
</script>
<script language="JavaScript" type="text/JavaScript">
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
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail()">��ϸ��Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.cancelmanage()">ȡ������</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.addmanage()">��Ӽ���</td>
		</tr>		
	</table>
	</div>
	<!-- �Ҽ��˵�����-->
<form method="post" name="mainForm">
<input type="hidden" name="flag" id="flag" value="<%=flag %>">
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
		<td align="center" valign=top>
			<table style="width:98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="add-content-title">Ӧ�� >> ������� >> Ntp�����б�</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
        			<tr>
					            <td bgcolor="#ECECEC" width="100%" align='right'>
									<a href="#" onclick="toAdd()">���</a>
									<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
		  			</td>
									</tr>
        								</table>
										</td>
        						</tr>
				<tr>
					<td colspan="2">
						<table cellspacing="1" cellpadding="0" width="100%">
	  						<tr class="microsoftLook0" height=28>
      								<td width='10%' align="left"><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">���</td>				
      								<th width='20%'>����</th>
      								<th width='15%'>IP��ַ</th>
      								<th width='14%'>����ʱ��</th>
      								<th width='14%'>NTPʱ��</th>
      								<th width='6%'>��ǰ״̬</th>      
      								<th width='6%'>�Ƿ���</th>
      								<th width='5%'>����</th>
							</tr>
			<%
    			NtpConfig vo = null;
    			Hashtable tmp = new Hashtable();
   				 for(int i=0;i<list.size();i++)
    			{
    				String ntpTime = "";
    				String currTime = "";
       				vo = (NtpConfig)list.get(i);
       				if(ntpHash != null && ntpHash.size() > 0 && ntpHash.containsKey(vo.getIpAddress())){
       					tmp = new Hashtable();
       					tmp = (Hashtable)ntpHash.get(vo.getIpAddress());
       					ntpTime = (String)tmp.get("datetime");
       					currTime = (String)tmp.get("collecttime");
       				}
       				int alarmlevel = 0;
       				String alarmmessage = "";
       				//Node node = (Node)PollingEngine.getInstance().getNasByID(vo.getId());
       				//String alarmmessage = "";
       				//if(node != null){
       				//	alarmlevel = SystemSnap.getNodeStatus(node); 
       				//	List alarmlist = node.getAlarmMessage();
       				//	if(alarmlist!= null && alarmlist.size()>0){
					//	for(int k=0;k<alarmlist.size();k++){
					//		alarmmessage = alarmmessage+alarmlist.get(k).toString();
					//	}
					//}
       				//}      				
			%>
   										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> height=25>  
    											<td ><INPUT type="checkbox" name=checkbox class=noborder value="<%=vo.getId() %>">
    												<font color='blue'><%=1 + i%></font></td>
    											<td  align='center' ><%=vo.getAlias()%></td>
    											<td  align='center'><%= vo.getIpAddress()%></td>
    											<td  align='center' ><%=currTime%></td>
    											<td  align='center' ><%=ntpTime%></td>
    											<td align='center'><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmlevel)%>" border="0" alt=<%=alarmmessage%>></td>
											<%
												if(vo.getFlag()==0){
											%>
											<td  align='center'>��</td>
											<% 
												//if(vo.getFlag()==1);
												}else{
											%>
											 <td  align='center'>��</td>
											 <%
											 	}
											 %>
											<td height=25>&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','') alt="�Ҽ�����">
											</td>											 
  										</tr>			
								<%;} %>
									</table>
								</td>
								</tr>
						<tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="11" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="11" /></td>
                  </tr>
              </table></td>
            </tr>	
			</table>
		<td></td>
	</tr>
</table>
</form>
</BODY>
</HTML>
