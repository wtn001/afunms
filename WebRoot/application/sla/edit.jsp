<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String rootPath = request.getContextPath();
  BusinessDao bussdao = new BusinessDao();
  SlaNodeConfig vo = (SlaNodeConfig)request.getAttribute("vo");
 String menuTable = (String)request.getAttribute("menuTable");;
 String intervalstr = vo.getIntervalunit();
 String showstr = "����";
 if("m".equals(intervalstr)){
 	showstr = "����";
 }else if("h".equals(intervalstr)){
 	showstr = "Сʱ";
 }else if("d".equals(intervalstr)){
 	showstr = "��";
 }else if("w".equals(intervalstr)){
 	showstr = "��";
 }else if("mt".equals(intervalstr)){
 	showstr = "��";
 }else if("y".equals(intervalstr)){
 	showstr = "��";
 }
%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">


  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
  
     //var chk1 = checkinput("alias","string","����",15,false);
     //var chk2 = checkinput("str","string","·��",30,false);
     //var chk3 = checkinput("ipaddress","ip","IP��ַ",30,false);
     //var chk4 = checkinput("timeout","number","��ʱ",30,false);
     
     //if(chk1&&chk2&&chk3&&chk4)
     {
     
        Ext.MessageBox.wait('���ݼ����У����Ժ�.. '); 
        //msg.style.display="block";
        mainForm.action = "<%=rootPath%>/ciscosla.do?action=update";
        mainForm.submit();
     }  
       // mainForm.submit();
 });	
	
});
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


//-- nielin add at 2010-07-27 start ----------------
function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
//-- nielin add at 2010-07-27 end ----------------




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
<form method="post" name="mainForm">
<input type=hidden name="id" value="<%=vo.getId()%>">
<table id="body-container" class="body-container">
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		
		</td>
            <td align="center" valign=top>
			<table style="width:98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
				<table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="content-title">�������� >> SLA���� >> SLA ���ӱ༭</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>										
		<tr >
			<td >
			
					<table id="detail-content-body" class="detail-content-body">
								
						<tr >						
							<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="name" maxlength="50" size="30" class="formStyle" value="<%=vo.getName()%>"><font color="red">*&nbsp;</font>
							</TD>
							<TD nowrap align="right" height="24" width="10%">����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
								<input type="text" name="descr" maxlength="50" size="30" class="formStyle" value="<%=vo.getDescr()%>"><font color="red">*&nbsp;</font>
							</TD>
						</tr>
						<tr style="background-color: #ECECEC;">	
						<TD nowrap align="right" height="24" width="10%">��ں�&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;<%=vo.getEntrynumber()%>
							<input type="hidden" name="entrynumber" value=<%=vo.getEntrynumber()%>>
							</TD>					
							<TD nowrap align="right" height="24" width="10%">SLA����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
																						<select id="slatype" name="slatype">
																								<option value="<%=vo.getSlatype()%>"><%=vo.getSlatype()%></option>
																								<option value="icmp">ICMP</option>
																								<option value="icmppath">ICMP Path</option>
																								<option value="udp">UDP</option>
																								<option value="jitter">JITTER(����)</option>
																								<option value="tcpconnectwithresponder">TCP Connect(����Responder)</option>
																								<option value="tcpconnectnoresponder">TCP Connect(δ����Responder)</option>
																								<option value="http">HTTP</option>
																								<option value="dns">DNS</option>
																						</select><font color="red">*&nbsp;</font>
							</TD>
							<tr >
							<TD nowrap align="right" height="24" width="10%">�ɼ����&nbsp;</TD>				
							<TD nowrap width="40%">&nbsp;
																						<select id="poll_interval" name="poll_interval">
																								<option value="<%=vo.getIntervals()%>-<%=vo.getIntervalunit()%>" selected><%=vo.getIntervals()%><%=showstr%></option>
																								<option value="1-m">1����</option>
																								<option value="2-m">2����</option>
																								<option value="3-m">3����</option>
																								<option value="4-m">4����</option>
																								<option value="5-m">5����</option>
																								<option value="10-m">10����</option>
																								<option value="30-m">30����</option>
																								<option value="1-h">1Сʱ</option>
																								<option value="4-h">4Сʱ</option>
																								<option value="8-h">8Сʱ</option>
																								<option value="12-h">12Сʱ</option>
																								<option value="1-d">1��</option>
																								<option value="1-w">1��</option>
																								<option value="1-mt">1��</option>
																								<option value="1-y">1��</option>
																						</select><font color="red">*&nbsp;</font>
							</TD>																		      		
							<TD nowrap align="right" height="24" width="10%">�Ƿ�ɼ�&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
								<select   name="mon_flag"  class="formStyle">
								<%
									if(vo.getMon_flag() == 0){
								%>
									<option value=0 selected>��</option>
									<option value=1>��</option>
								<%
									}else{
								%>
									<option value=0 >��</option>
									<option value=1 selected>��</option>								
								<%
									}
								%>
								</select>
							</TD>																						
						</tr>
						<tr style="background-color: #ECECEC;">						
							<TD nowrap align="right" height="24" width="10%">�ɼ���ʽ&nbsp;</TD>				
							<TD nowrap width="40%" colspan=3>&nbsp;
																			    		<select id="collecttype" name="collecttype">
																			    				<option value="<%=vo.getCollecttype()%>"><%=vo.getCollecttype()%></option>
																								<option value="snmp">SNMP</option>
																								<option value="telnet">Telnet</option>
																						</select><font color="red">*&nbsp;</font>
							</TD>
						</tr>												
															<tr>
																<!-- nielin add (for timeShareConfig) start 2010-01-03 -->
																<td><input type="hidden" id="rowNum" name="rowNum"></td>
																<!-- nielin add (for timeShareConfig) end 2010-01-03 -->
															
															</tr>
															<tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="�� ��" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
																</TD>	
															</tr>	
				        								</table>
			</td>
		</tr>
	</table>
</td>
			</tr>
		</table>
</BODY>
</HTML>
