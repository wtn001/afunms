<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@page import="com.afunms.polling.impl.ICMPCollectDataManager"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.om.*"%>
<%@page import="com.afunms.common.util.*"%>
<%
  String rootPath = request.getContextPath();
  List<SlaNodeConfig> list = (List<SlaNodeConfig>)request.getAttribute("list");
  Hashtable telnetHash = (Hashtable)request.getAttribute("telnetHash");
  Hashtable slaHash = ShareData.getSlaHash();
  if(slaHash == null)slaHash = new Hashtable();
  if(telnetHash == null)telnetHash = new Hashtable();
 JspPage jp = (JspPage)request.getAttribute("page");
String menuTable = (String)request.getAttribute("menuTable");
%>
<html>
<head>
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<link rel="stylesheet" href="<%=rootPath%>/application/resource/jquery_tablesort/flora/flora.all.css" type="text/css" media="screen" title="Flora (Default)">
<script src="<%=rootPath%>/application/resource/jquery_tablesort/jquery-latest.js" type="text/javascript"></script>
<script  src="<%=rootPath%>/application/resource/jquery_tablesort/jquery.tablesorter.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/slatcp.do?action=delete"; 
  var listAction = "<%=rootPath%>/slatcp.do?action=list";
    var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/ciscosla.do?action=ready_add";
     mainForm.submit();
  }
  function toDelete()
  {
    if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
     mainForm.action = "<%=rootPath%>/ciscosla.do?action=delete";
     mainForm.submit();
     }
  }  
  function toRefresh()
  {
     mainForm.action = "<%=rootPath%>/ciscosla.do?action=listperf";
     mainForm.submit();
  }  
   function CreateWindow(url){
				msgWindow=window.open(url,"protypeWindow","toolbar=no,width=850,height=450,directories=no,status=no,scrollbars=yes,menubar=no")
	}
  function showRTT(configid){  
  				//alert("=====");
				CreateWindow("<%=rootPath%>/application/sla/net_cpu_month.jsp?id=" +configid);
  }
  function showStatus(configid){  
				CreateWindow("<%=rootPath%>/detail/net_cpu_month.jsp?id=" + 2 + "&ip=" +configid);
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
	function detail()
	{
	    location.href="<%=rootPath%>/FTP.do?action=detail&id="+node;
	}
	function edit()
	{
		location.href="<%=rootPath%>/ciscosla.do?action=ready_edit&id="+node;
	}
	function cancelmanage()
	{
		location.href="<%=rootPath%>/FTP.do?action=changeMonflag&value=0&id="+node;
	}
	function reboot()
	{
		location.href="<%=rootPath%>/ciscosla.do?action=detail&id="+node;
	}
	function shutdown()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=shutdown&value=1&id="+node;
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
<script type="text/javascript" charset="utf-8">
	$(document).ready(function() {
		$("#example").tablesorter({
			sortList:[],
			widgets: ['zebra'],
			headers: { 
				0:{sorter: false}, 
				1:{sorter: false}, 
				2:{sorter: false},
				3:{sorter: false},
				12:{sorter: false}
			}
		});//0Ϊ����    1����
	} ); 
</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">

<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">�޸���Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.reboot()">������Ϣ</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.shutdown()">�رշ�����</td>
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
			<table style="width:98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="content-title">�������� >> UDP�˵�����Ӧ >> UDP�����б�</td>
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
									<a href="#" onclick="toDelete()">ɾ��</a>
									<a href="#" onclick="toRefresh()">ˢ��</a>
									&nbsp;&nbsp;&nbsp;
		  						</td>
									</tr>
        								</table>
										</td>
        						</tr>						
		        									<tr>
														<td>
															<table>
																<tr>
													    			<td  class="body-data-title">
							    										<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
							        								</td>
							        							</tr>
															</table>
														</td>
													</tr>				
		<tr >
			<td colspan="2">
				<table id="example" class="tablesorter" cellspacing="1" cellpadding="0" width="100%" >
					<thead>
						<tr height=28 >
							<th align="center" style="color: #000000;" class="body-data-title" width='6%' align=left>&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">���</th>
	    					<th align="center" style="color: #000000;" class="body-data-title" width='10%'>����</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='10%'>Դ��ַ</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='10%'>Ŀ���ַ</th>	
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>��ǰRTT(ms)</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>ƽ��RTT(ms)</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>��СRTT(ms)</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>���RTT(ms)</th>      					
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>��ǰ�ɹ���(%)</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>ƽ���ɹ���(%)</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>��С�ɹ���(%)</th>
	      					<th align="center" style="color: #000000;" class="body-data-title" width='6%'>���ɹ���(%)</th>
	    					<th align="center" style="color: #000000;" class="body-data-title" width='5%'>����</th>
						</tr>
					</thead>
				<tbody>
<%
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String starttime = sdf.format(date) + " 00:00:00";// ��ʼ����
	String totime = sdf.format(date) + " 23:59:59";// ��ֹ����
   //ȡ����ǰICMP��rtt��ͳ�����ݣ������С��ƽ����
   ICMPCollectDataManager iCollectDataManager = new ICMPCollectDataManager();
	try{
		if(list != null && list.size()>0){
			//String monstr = "��";
			Hashtable dataHash = new Hashtable();
	   		for(int i=0;i<list.size();i++){
	 			//String imgpath = "";
	 			String rrtValue = "--";
	 			String statusValue = "0";
	 			dataHash = new Hashtable();
	    		SlaNodeConfig vo = list.get(i);
	    		String sourceIp = "";
	    		if(telnetHash.containsKey(vo.getTelnetconfig_id())){
	    			sourceIp =(String) telnetHash.get(vo.getTelnetconfig_id());
	    		}
	    		Hashtable icmpDataHash = iCollectDataManager.getICMPData(vo.getId()+"", starttime, totime);
			   if(slaHash.containsKey(vo.getId()+"")){
					dataHash = (Hashtable)slaHash.get(vo.getId()+"");
					Pingcollectdata rttdata =  (Pingcollectdata)dataHash.get(1);
					Pingcollectdata statusdata =  (Pingcollectdata)dataHash.get(0);
				   if(rttdata != null)rrtValue = rttdata.getThevalue();
				   if(statusdata != null)statusValue = statusdata.getThevalue();
				}
%>
	
       <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook">
	    	<td align="center" height=25>&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>">&nbsp;<%=1 + i%></td>
			<td align="center" height=25>&nbsp;<%=vo.getName()%></td> 
			<td align="center" height=25><%=sourceIp%></td>
			<td align="center" height=25><%=vo.getDestip()%></td>
			 
			<td align="center" height=25>&nbsp;<%=rrtValue%>&nbsp;<img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showRTT("<%=vo .getId()%>")' width=15></td> 
			<td align="center" height=25>&nbsp;<%=icmpDataHash.get("avgRrtValue")%>&nbsp;<img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showRTT("<%=vo .getId()%>")' width=15></td>
			<td align="center" height=25>&nbsp;<%=icmpDataHash.get("minRrtValue")%>&nbsp;<img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showRTT("<%=vo .getId()%>")' width=15></td>
			<td align="center" height=25>&nbsp;<%=icmpDataHash.get("maxRrtValue")%>&nbsp;<img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showRTT("<%=vo .getId()%>")' width=15></td>
			<td align="center" height=25>
				<%=statusValue%>%
			</td>
			<td align="center" height=25>
				<%=icmpDataHash.get("avgStatusValue")%>%
			</td>
			<td align="center" height=25>
				<%=icmpDataHash.get("minStatusValue")%>%
			</td>
			<td align="center" height=25>
				<%=icmpDataHash.get("maxStatusValue")%>%
			</td>
			<td align="center" height=25>&nbsp;
			<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getName()%>') alt="�Ҽ�����">
			</td>
  		</tr>
  

<%	
			}
		}
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		iCollectDataManager.close();
	}
%>			
				</tbody>	
			</table>
			</td>
		</tr>
		<tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" >
              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
                  </tr>
              </table></td>
            </tr>
	</table>
</td>
			</tr>
		</table>
</BODY>
</HTML>
