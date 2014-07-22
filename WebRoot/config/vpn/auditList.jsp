<%@page language="java" contentType="text/html;charset=gbk"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.slaaudit.model.SlaAudit"%>
<%@page import="com.afunms.topology.dao.HostNodeDao"%>
<%@page import="com.afunms.system.dao.UserDao"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@ include file="/include/globe.inc"%>

<%
response.addHeader("Cache-Control", "no-cache");

response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
	String rootPath = request.getContextPath();
	List list = (List) request.getAttribute("list");
	JspPage jp = (JspPage) request.getAttribute("page");
    List listOne =(List) request.getAttribute("listOne");
    Hashtable userHash =(Hashtable) request.getAttribute("userHash");
     Hashtable telnetHash =(Hashtable) request.getAttribute("telnetHash");
    
	String startdate = (String) request.getAttribute("startdate");
	String todate = (String) request.getAttribute("todate");
	String userid = (String)request.getAttribute("userid");
    String slatype = (String)request.getAttribute("type");
    String hostId = (String)request.getAttribute("id");
	User operator = (User) session.getAttribute(SessionConstant.CURRENT_USER);
	String selected="";
%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<meta http-equiv="pragma" content="no-cache" />
         <meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
         <meta http-equiv="expires" content="Thu, 01 Jan 1970 00:00:01 GMT" />
				
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/verdict.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />

		<script language="JavaScript" type="text/javascript">		
  var listAction = "<%=rootPath%>/vpn.do?action=list";
  var delAction = "<%=rootPath%>/vpn.do?action=delete";
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
</script>
<script language="JavaScript">

			//��������
			var node="";
			var ipaddress="";
			var operate="";
			var filename;
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,file)
			{	
				filename = file;
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

function search()
{
	mainForm.action = "<%=rootPath%>/vpn.do?action=auditList";
    mainForm.submit();
	
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
	<body id="body" class="body" onload="initmenu();">
		<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<form id="mainForm" method="post" name="mainForm">
			<table id="body-container" class="body-container">
				<tr>
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> ��Դ >> VPN���� >> VPN�������</td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
										</tr>
										<tr>
											<td>
												<table id="content-body" class="content-body">
													<tr>
														<td>
															<table>
																
																<tr>
																<td class="body-data-title" style="text-align: left;" colspan=2> 
																       &nbsp; <b>IP��ַ��</b>
																		<select id="id" name="id" style="width:100px" >
																		<option value="-1">����</option>
																		<%
																		if(telnetHash!=null){
																			String ip="";
																		
																		for(Iterator itr=telnetHash.keySet().iterator();itr.hasNext();){
																			int id=(Integer)itr.next();
																			Huaweitelnetconf conf=(Huaweitelnetconf)telnetHash.get(id);
																			if(conf!=null)ip=conf.getIpaddress();
																			if(!hostId.equals(id+"")){
																				selected="";
																			}else{
																				selected="selected";
																			}
																				
																			
																			
																		 %>
																			<option value="<%=id%>" <%=selected %>><%=ip%></option>
																		<%
																		}
																		}
																%>
																	</select>
																	
																		&nbsp; <b>�û���</b>
																		<select id="userid" name="userid" style="width:100px" >
																		<option value="-1">����</option>
																		<%
																		if(listOne!=null){
																			
																		for (int i=0;i<listOne.size();i++) {
                                                                              String userName=((User)listOne.get(i)).getName();
                                                                              int uid=((User)listOne.get(i)).getId();
                                                                              if(!userid.equals(uid+"")){
  																				selected="";
  																			}else{
  																				selected="selected";
  																			}
  																				
																		    %>
																				<option value="<%=uid%>" <%=selected %>><%=userName%></option>
																			<%
																		}
																		}
																	%>
																		</select>															
																			&nbsp; 
																				
																	        ��ʼ����
																								<input type="text" name="startdate" value="<%=startdate%>" size="10">
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																									<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0> </a> 
																			 ��ֹ����
																								<input type="text" name="todate" value="<%=todate%>" size="10" />
																								<a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																									<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0> </a> 
																	&nbsp;&nbsp;<input type="button" value="��  ѯ" onclick="search()">
																	</td>
							        								
																</tr>
																<tr>
																	<td>
																		<table >
																			<tr>
																    			<td  class="body-data-title" style="text-align: right">
										    										<jsp:include page="../../common/page.jsp">
																						<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
																						<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
										  										 	</jsp:include>
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
															<table>
															<tr>
																	<td align="center" class="body-data-title">
																		<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()">
																	</td>
																	<td align="center" class="body-data-title">
																		���
																	</td>
																	<td align="center" class="body-data-title">
																		IP��ַ
																	</td>
																	<td align="center" class="body-data-title">
																		�豸����
																	</td>
																	<td align="center" class="body-data-title">
																		����
																	</td>
																	<td align="center" class="body-data-title">
																		����
																	</td>
																	<td align="center" class="body-data-title">
																		����״̬
																	</td>
																	<td align="center" class="body-data-title">
																		�û�
																	</td>														
																	<td align="center" class="body-data-title">
																		����ʱ��
																	</td>
																	<td align="center" class="body-data-title">
																		��ϸ
																	</td>
																</tr>
																<%
																	SlaAudit vo = null;
																	String ipname=null;
																	String  uname=null;
															        String  slatypename=null;
																	//HostNodeDao hostNodeDao = new HostNodeDao();
																	//UserDao userDao = new UserDao();
																	SlaCfgCmdFileDao slaCfgCmdFile=new SlaCfgCmdFileDao();
																	CiscoSlaCfgCmdFile cmdFile = null;
																	User user = new User();
																	Huaweitelnetconf telnetconfig = null;
																	int startRow = jp.getStartRow();
																	String deviceType="";
																	for (int i = 0; i < list.size(); i++) {
																		vo = (SlaAudit) list.get(i);
				                                                       if(userHash != null && userHash.containsKey(vo.getUserid())){
				                                                       	user = (User)userHash.get(vo.getUserid());
				                                                       	uname = user.getName();
				                                                       }		
				                                                       if(telnetHash != null && telnetHash.containsKey(vo.getTelnetconfigid())){
				                                                       	telnetconfig = (Huaweitelnetconf)telnetHash.get(vo.getTelnetconfigid());
				                                                       	ipname = telnetconfig.getIpaddress();
				                                                       	deviceType=telnetconfig.getDeviceRender();
				                                                       }	
				                                                       int dostatus = vo.getDostatus(); 
				                                                       String statusStr = "<font color=red>ʧ��</font>";     
				                                                       if(1==dostatus) statusStr = "�ɹ�";   
				                                                       //String opr = "";                                                  	                                                    
																%>
																<tr <%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list">
																		<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>">
																	</td>
																	<td align="center" class="body-data-list">
																		<font color='blue'><%=startRow + i%></font>
																	</td>
																	
																	<td align="center" class="body-data-list"><%=ipname%></td>
																	<td align="center" class="body-data-list"><%=deviceType%></td>
																	<td align="center" class="body-data-list"><%=vo.getSlatype()%></td>
																	<%
																		if("add".equals(vo.getOperation())){
																	%>
																	
																	<td align="left" class="body-data-list"><img src="<%=rootPath%>/img/correct.gif">&nbsp;����</td>
																	<%
																		}else{
																	%>
																	<td align="left" class="body-data-list"><img src="<%=rootPath%>/img/error.gif">&nbsp;ɾ��</td>
																	<%}%>
																	<%
																		if(1== vo.getDostatus()){
																	%>
																	<td align="center" class="body-data-list"><img src="<%=rootPath%>/img/ok_.gif">&nbsp;�ɹ�</td>
																	<%
																		}else{
																	%>
																	<td align="center" class="body-data-list"><img src="<%=rootPath%>/img/error_alarm.gif">&nbsp;ʧ��</td>
																	<%}%>
																	<td align="center" class="body-data-list">
																	<a href="#" style="cursor: hand" onclick="window.showModalDialog('<%=rootPath%>/user.do?action=read&id=<%=vo.getUserid()%>',window,',dialogHeight:400px;dialogWidth:600px')">
																		<%=uname%>
																		</a></td>																	
																	<td align="center" class="body-data-list"><%=vo.getDotime()%></td>
																	<td align="center" class="body-data-list">
																		<a href="#" style="cursor: hand" onclick="window.showModalDialog('<%=rootPath%>/vpn.do?action=read&id=<%=vo.getId()%>',window,',dialogHeight:400px;dialogWidth:600px')">
																		�鿴
																		</a></td>																									
																</tr>
																<%
																	}
																	//hostNodeDao.close();
																	//userDao.close();
																	slaCfgCmdFile.close();
																%>
																
																
															</table>
														</td>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
		        							<td>
		        								<table id="content-footer" class="content-footer">
		        									<tr>
		        										<td>
		        											<table width="100%" border="0" cellspacing="0" cellpadding="0">
									                  			<tr>
									                    			<td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
									                    			<td></td>
									                    			<td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
									                  			</tr>
									              			</table>
		        										</td>
		        									</tr>
		        								</table>
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
