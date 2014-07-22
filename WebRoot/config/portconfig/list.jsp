<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.config.model.Portconfig"%>
<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  List ips = (List)request.getAttribute("ips");
  
  String ipaddress;
  try {
	  ipaddress = request.getSession().getAttribute("ipaddress").toString();
  } catch (Exception e) {
	  ipaddress = "";
  }
  int rc = list.size();

  JspPage jp = (JspPage)request.getAttribute("page");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>

<script language="javascript">	
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/portconfig.do?action=delete";
  var listAction = "<%=rootPath%>/portconfig.do?action=list";
  
  
  	function toDelete(){
	  if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
		mainForm.action = "<%=rootPath%>/portconfig.do?action=delete";
		mainForm.submit();
		}
	}
  
  function doQuery()
  {  
     mainForm.action = "<%=rootPath%>/portconfig.do?action=find";
     mainForm.submit();
  }
  function doFromlastoconfig()
  {  
     mainForm.action = "<%=rootPath%>/portconfig.do?action=fromlasttoconfig";
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
      mainForm.action = "<%=rootPath%>/network.do?action=ready_add";
      mainForm.submit();
  }
  
    function toEmpty()
  {
      mainForm.action = "<%=rootPath%>/portconfig.do?action=empty";
      mainForm.submit();
  }
  function editNodePort1(d){alert(d);}
  
  function chkall()
		  	{
		    	if ( mainForm.checkflag.length == null ) {
			        if( mainForm.checkall.checked )
			           mainForm.checkflag.checked = true;
			        else
			           mainForm.checkflag.checked = false;
		     	} else {
		        	if(mainForm.checkall.checked) {
		           	for( var i=0; i < mainForm.checkflag.length; i++ ){
		           		mainForm.checkflag[i].checked = true;
		        	}
		   		} else {
		           for( var i=0; i < mainForm.checkflag.length; i++ ){
		              mainForm.checkflag[i].checked = false;
		           }
		        }
		     }
		  }
	function update(){
      mainForm.action = "<%=rootPath%>"+"/portconfig.do?action=updateportflag";
      mainForm.submit();
   }
   
   function updateflag(){     
     var ipaddress = document.getElementsByName("checkflag2");
     var portArray2="";
     var portArray="";
     var test=document.getElementsByName("checkflag");
     var k = 0;
	for (i = 0; i < test.length ; i ++ ){
		if(test[i].checked){
			k = k+1;
			portArray+=test[i].value+",";
			portArray2+=ipaddress[i].value+",";	
		}
	}
	var flag = 0;
	if( k == 0 ){
		flag = 1;
		for (j = 0; j < ipaddress.length ; j ++ ){
        	portArray2+=ipaddress[j].value+",";
        	portArray+=test[j].value+",";
		}
	}
     $.ajax({
					type:"POST",
					dataType:"json",
					url:"<%=rootPath%>/PortConfigAjaxManager.ajax?action=updateflag2",	
					data:"ipaddress="+portArray2+"&portArray="+portArray+"&flag="+flag,				
					success:function(data){
						if(data.isSuccess==1){
							alert("���ݸ��³ɹ�!");							
						}else{
						    alert("�Բ���,���ݸ���ʧ��!");
						}
					}
				});				
	} 
  
function editNodePort(id,sms)
  {
 
      $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=editnodeport&id="+id+"&sms="+sms+"&nowtime="+(new Date()),
			success:function(data){
			
			if(data.flagStr==3){
			alert("�޸�ʧ�ܣ�����");
			}else if(data.flagStr==0){
			var smsId =document.getElementById("smsFlag"+id);
		    smsId.innerHTML ="<span style='cursor:hand' onclick='editNodePort("+id+",1)'><font color=#skyblue>����</font></span>";
			}else if(data.flagStr==1){
			var smsId =document.getElementById("smsFlag"+id);
		    smsId.innerHTML ="<span style='cursor:hand' onclick='editNodePort("+id+",0)'><font color=#green>ȡ������</font></span>";
			}
			}
		});
  }
  function editNodePortForReport(id,reportFlag)
  {
  
      $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=editnodeport&id="+id+"&reportflag="+reportFlag+"&nowtime="+(new Date()),
			success:function(data){
		
			if(data.flagStr==3){
			alert("�޸�ʧ�ܣ�����");
			}else if(data.flagStr==0){
			var smsId =document.getElementById("reportflag"+id);
		    smsId.innerHTML ="<span style='cursor:hand' onclick='editNodePortForReport("+id+",1)'><font color=#skyblue>��ʾ�ڱ���</font></span>";
			}else if(data.flagStr==1){
			var smsId =document.getElementById("reportflag"+id);
		    smsId.innerHTML ="<span style='cursor:hand' onclick='editNodePortForReport("+id+",0)'><font color=#green>ȡ����ʾ�ڱ���</font></span>";
			}
			}
		});
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

//�޸ľ��漶��
function modifyalarmlevelajax(id){
	var t = document.getElementById("alarmlevel"+id);
	var alarmvalue = t.selectedIndex;//��ȡ�������ֵ
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=modifyAlarmlevel&id="+id+"&alarmvalue="+alarmvalue+"&nowtime="+(new Date()),
			success:function(data){
				window.alert("�޸ĳɹ���");
			}
		});
}

</script>
</head>
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa"  onload="initmenu();">
<form method="post" name="mainForm">
<table id="body-container" class="body-container">
	<tr>
		<td width="200" valign=top align=center>
	
	<%=menuTable%>
            				
	</td>
		<td align="center" valign=top>
			<table cellpadding="0" cellspacing="0" algin="center" style="width:98%">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title"><b>��Դ >> �豸ά�� >> �˿�����</b></td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
			  </tr>				
				<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="50%" align='left'>
				&nbsp;&nbsp;&nbsp;&nbsp;<B>��ѯ:</B>
        								<SELECT name="ipaddress" style="width=100">
        									<%
        										if(ips != null && ips.size()>0){
        											for(int k=0;k<ips.size();k++){
        												String ip = (String)ips.get(k);
        									%> 
          									<OPTION value="<%=ip%>" 
          									<%if(ip.equals(ipaddress)) {
          										%>
          										selected="selected"
          									<%} %>><%=ip%></OPTION>
          									<%
          											}
          										}
          									%>        
          									
          								</SELECT>&nbsp;
          								<INPUT type="button" class="formStyle" value="��ѯ" onclick=" return doQuery()">
          							</td> 
          								<td bgcolor="#ECECEC" width="50%" align='right'>
          								<INPUT type="button" class="formStyle" value="�ύ����" onclick="updateflag()"/>
									<INPUT type="button" class="formStyle" value="�� ��" onclick=" return toEmpty()">
					                <INPUT type="button" class="formStyle" value="ˢ ��" onclick=" return doFromlastoconfig()">
									<INPUT type="button" class="formStyle" value="ɾ ��" onclick=" return toDelete()">&nbsp;&nbsp;
		  							</td>
										</tr>
								</table>
		  						</td>
									</tr>
								</table>
		  						</td>                       
        						</tr>
						    <tr>
							<td>
							<table width="100%" cellpadding="0" cellspacing="1" >
							<tr>
						    <td bgcolor="#ECECEC" width="80%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
           </td>
           </tr>
		   </table>
		   </td>
		   </tr>
		      				<tr>
								<td colspan="2">
									<table cellspacing="1" cellpadding="0" width="100%">
	  									<tr class="microsoftLook0">
      											<th width='5%'>���</th>				
      											<th width='13%'>�豸����(ip)</th>
      											<th width='6%'>�˿�</th>
      											<th width='15%'>����</th>   
      											<th width='10%'>����(kb/s)</th>
      											<th width='10%'>�˿�Ӧ��</th>
      											<th width='6%'>Trap����</th>
      											<th width='10%'>��������</th>
      											<th width='12%' class="report-data-body-title" align="center" bgcolor=#F1F1F1>�Ƿ��������
							      					<INPUT type="checkbox" id="checkall" name="checkall" onclick="javascript:chkall()" class="noborder">																				
							      				</th>
      											<th width='8%'>������ʾ����</th>      											
      											<th width='5%'>����</th>
      											
										</tr>
<%
    Portconfig vo = null;
    int startRow = jp.getStartRow();
    for(int i=0;i<rc;i++)
    {
       vo = (Portconfig)list.get(i);          
%>
   										<tr bgcolor="#FFFFFF" <%=onmouseoverstyle%>>  
    											<td >
    												<font color='blue'>&nbsp;<%=startRow + i%></font></td>
    											<td  align='center'height=23><%=vo.getIpaddress()%></td>
    											<td  align='center' height=23><%=vo.getPortindex()%></td>
    											<td  align='center' height=23><%=vo.getName()%></td>
    											<td  align='center' height=23><%=vo.getSpeed()%></td>
    											<td  align='center' height=23 id="linkUse<%=vo.getId()%>"><%=vo.getLinkuse()%></td>
    											<%
    												
    												String sms="<span  onclick='editNodePort("+vo.getId()+",0)'><font color=green>ȡ������</font></span>";
    												if(vo.getSms()==0){
    													
    													sms="<span style='cursor:hand' onclick='editNodePort("+vo.getId()+",1)'><font color=#skyblue>����</font></span>";
    												}
    												String reportflag = "<span style='cursor:hand' onclick='editNodePortForReport("+vo.getId()+",0)'><font color=green>ȡ����ʾ�ڱ���</font></span>";
    												if(vo.getReportflag()==0){
    													
    													reportflag="<span style='cursor:hand' onclick='editNodePortForReport("+vo.getId()+",1)'><font color=#skyblue>��ʾ�ڱ���</font></span>";
    												}
    											%>    											
    											<td  align='center'  id="smsFlag<%=vo.getId() %>" height=23><%=sms%></td>
    											<% 
    											   String alarmlevel = vo.getAlarmlevel();
    											   //String varalarmlevel = "";
    											   if(alarmlevel == null || alarmlevel.equals("")) {
    												   alarmlevel = "0";
    											   }
    											%>
    											<td align="center" height=23>
													<select style="width:60px;" id="alarmlevel<%=vo.getId()%>">
    													<option  value="0" <%if(alarmlevel.equals("0")) {%>selected<%} %>>����</option>
    													<option  value="1" <%if(alarmlevel.equals("1")) {%>selected<%} %>>��ͨ</option>
    													<option  value="2" <%if(alarmlevel.equals("2")) {%>selected<%} %>>����</option>
    													<option  value="3" <%if(alarmlevel.equals("3")) {%>selected<%} %>>����</option>    													
    													</select>
     												<img href="#" src="<%= rootPath%>/resource/image/menu/xgmm.gif" style="cursor:hand" onclick="modifyalarmlevelajax('<%=vo.getId()%>');"/>
												</td>
												<td  align='center' class="report-data-body-list" id="countflag">
												<input type=hidden name="checkflag2"  value="<%=vo.getIpaddress()%>" />
												<input id="<%=vo.getId()%>" name="checkflag" type="checkbox" value="<%=vo.getPortindex()%>"
													<%
														//System.out.println("==================555");
															if( vo.getFlag() != null && vo.getFlag().equals("1")){
															 	//System.out.println("==================666");
																%> checked="checked" <%
															}
													%> />
												</td>
    										    <td  align='center'  id="reportflag<%=vo.getId() %>" height=23><%=reportflag%></td>											
											<td  align='center' height=23><span style='cursor:hand' onClick='window.open("<%=rootPath%>/portconfig.do?action=showedit&id=<%=vo.getId()%>","_blank", "height=400, width= 500, top=200, left= 200")'>
											<img src="<%=rootPath%>/resource/image/editicon.gif" border="0"/></span></td>
        										
  										</tr>			
<% }%>
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
		</td>
	</tr>
</table>
</form>
</BODY>
</HTML>
