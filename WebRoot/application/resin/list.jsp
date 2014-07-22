<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.Resin"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%
	List list = (List) request.getAttribute("list");
	int rc = list.size();
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link
			href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath()%>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<script language="javascript">
  var delAction = "<%=rootPath%>/resin.do?action=delete";
  var listAction = "<%=rootPath%>/resin.do?action=list";
  
    function toDelete2()
  {
  if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
     mainForm.action = "<%=rootPath%>/resin.do?action=delete";
     mainForm.submit();
    }
  }
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/resin.do?action=ready_add";
     mainForm.submit();
  }
  
  function detail(id)
  {
	mainForm.action = "<%=rootPath%>/resin.do?action=detail&id="+id;
	mainForm.submit();
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
	function detail1()
	{
	    location.href="<%=rootPath%>/resin.do?action=detail&id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/resin.do?action=ready_edit&id="+node;
	}
	function cancelalert()
	{
		location.href="<%=rootPath%>/resin.do?action=cancelalert&id="+node;
	}
	function addalert()
	{
		location.href="<%=rootPath%>/resin.do?action=addalert&id="+node;
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
	<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"
		onload="initmenu();">

		<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin; font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.edit()">
						�޸���Ϣ
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.detail1();">
						������Ϣ
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.cancelalert()">
						ȡ������
					</td>
				</tr>
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.addalert()">
						��Ӽ���
					</td>
				</tr>
			</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td width="200" valign=top align=center>
						<%=menuTable%>
					</td>
					<td align="center" valign=top>
						<table style="width: 98%" cellpadding="0" cellspacing="0"
							algin="center">
							<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left">
												<img src="<%=rootPath%>/common/images/right_t_01.jpg"
													width="5" height="29" />
											</td>
											<td class="add-content-title">
												Ӧ�� >> �м������ >> Resin�����б�
											</td>
											<td align="right">
												<img src="<%=rootPath%>/common/images/right_t_03.jpg"
													width="5" height="29" />
											</td>
										</tr>
									</table>
								</td>
							<tr>
								<td>
									<table width="100%" cellpadding="0" cellspacing="1">
										<tr>
											<td bgcolor="#ECECEC" width="100%" align='right'>
												<a href="#" onclick="toAdd()">���</a>
												<a href="#" onclick="toDelete2()">ɾ��</a>&nbsp;&nbsp;&nbsp;
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
										<tr class="microsoftLook0" height=28>
											<th width='22'></th>
											<th width='5%'>
												���
											</th>
											<th width='20%'>
												����
											</th>
											<th width='20%'>
												IP��ַ
											</th>
											<th width='15%'>
												�˿�
											</th>
											<th width='15%'>
												�Ƿ����
											</th>
											<th width='15%'>
												��ǰ״̬
											</th>
											<th width='10%'>
												�༭
											</th>
										</tr>
										<%
											String monStr = "δ����";
											for (int i = 0; i < rc; i++) {
												monStr = "δ����";
												Resin vo = (Resin) list.get(i);
												Node node = PollingEngine.getInstance().getResinByID(vo.getId());
												int status = 0;
												if (node != null) {
													status = SystemSnap.getNodeStatus(node);
													if (node.isManaged() == true)
														monStr = "�Ѽ���";
												}
										%>
										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>
											class="microsoftLook">
											<td>
												<INPUT type="radio" class=noborder name=radio
													value="<%=vo.getId()%>">
											</td>
											<td>
												&nbsp;
												<font color='blue'><%=1 + i%></font>
											</td>
											<td>
												&nbsp;<%=vo.getAlias()%></td>
											<td>
												&nbsp;<%=vo.getIpAddress()%></td>
											<td>
												&nbsp;<%=vo.getPort()%></td>
											<td>
												&nbsp;<%=monStr%></td>
											<td align='center' valign=middle>
												&nbsp;
												<img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(status)%>" border="0">
												&nbsp;<%=NodeHelper.getStatusDescr(status)%></td>
											<td align='center'>
												&nbsp;&nbsp;
												<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getIpAddress()%>') title="�Ҽ��˵�">

											</td>



										</tr>
										<%
											}
										%>
									</table>
								</td>
							</tr>
							<tr>
								<td background="<%=rootPath%>/common/images/right_b_02.jpg">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td align="left" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_01.jpg"
													width="5" height="11" />
											</td>
											<td></td>
											<td align="right" valign="bottom">
												<img src="<%=rootPath%>/common/images/right_b_03.jpg"
													width="5" height="11" />
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</BODY>
</HTML>
