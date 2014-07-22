<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.cabinet.model.EqpRoom"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.cabinet.model.RoomLaw"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.afunms.cabinet.model.RoomGuest"%>
<%@ include file="/include/globe.inc"%>
<%
	List<RoomGuest> list = (List<RoomGuest>) request.getAttribute("list");
	int rc = list.size();
	JspPage jp = (JspPage) request.getAttribute("page");
	String rootPath = request.getContextPath();
	String menuTable = (String) request.getAttribute("menuTable");
	 Hashtable<Integer,EqpRoom> eqpRoomHash =(Hashtable<Integer,EqpRoom> )request.getAttribute("eqpRoomHash");
	 Integer cabinetid=(Integer)request.getAttribute("cabinetid");
	 String startdate = (String) request.getAttribute("startdate");
	 String todate = (String) request.getAttribute("todate");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script language="javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/roomguest.do?action=delete";
  var listAction = "<%=rootPath%>/roomguest.do?action=list";
  
  var alertInfo = "ȷʵҪɾ����?";
 
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
//��������
			var node="";
			var filename="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
			*/
			function showMenu(id,nodeid,name,showItemMenu)
			{	
				filename=name;
				node=nodeid;
				//operate=oper;
			    if("" == id)
			    {
			        return false;
			    }
			    else{
			    	popMenu(itemMenu,100,showItemMenu);
			    }
			    event.returnValue=false;
			    event.cancelBubble=true;
			    
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
			            this.style.background="#99CCFF";
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
			    pop.show(event.clientX-1,event.clientY,width,rowCount*30,document.body);
			    return true;
			}
   </script>
   
   <script type="text/javascript">
	 function toAdd()
  {
     mainForm.action = "<%=rootPath%>/roomguest.do?action=ready_add";
     mainForm.submit();
  }
 
	
	function edit(){
		mainForm.action = "<%=rootPath%>/roomguest.do?action=ready_edit&id=" + node;
		mainForm.submit();
	}
	function search(){
	 mainForm.action = "<%=rootPath%>/roomguest.do?action=list";
     mainForm.submit();
	
  }
  function detail(id){
      window.location="<%=rootPath%>/roomguest.do?action=detail&id="+id;
  }
 function exportReport(exportType){

 var startdate=document.all.startdate.value;
 var todate=document.all.todate.value;
 var cabinetid=document.all.cabinetid.value;
 window.open('<%=rootPath%>/roomguest.do?action=downloadReport&type=guest&exportType='+exportType+'&cabinetid='+cabinetid+'&startdate='+startdate+'&todate='+todate+'&nowtime='+(new Date()),"_blank","toolbar=no,width=1,height=1,top=2000,left=3000,directories=no,status=no,menubar=no,alwaysLowered=yes");
 
}
</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
	<!-- ��������������Ҫ��ʾ���Ҽ��˵� -->
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
		<div id="itemMenu" style="display: none";>
			<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
				style="border: thin;font-size: 12px" cellspacing="0">
				<tr>
					<td style="cursor: default; border: outset 1;" align="center"
						onclick="parent.edit()">
						�༭
					</td>
					
				</tr>
				
			</table>
		</div>
		<!-- �Ҽ��˵�����-->
		<form id="mainForm" method="post" name="mainForm">
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
					<td class="td-container-main">
						<table id="container-main" class="container-main">
							<tr>
								<td class="td-container-main-content">
									<table id="container-main-content" class="container-main-content">
										<tr>
								<td background="<%=rootPath%>/common/images/right_t_02.jpg"
									width="100%">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> 3D���� >> �������� >> ���������Ǽ� </td>
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
																   
																	<td class="body-data-title" style="text-align:left;">
																		&nbsp;&nbsp;����λ�ã�<select id="cabinetid" name="cabinetid">
																			<option value="-1">����</option>
																	    <%
																	    if(eqpRoomHash!=null){
																	    	String selected="";
																	    for   (Iterator   it   =   eqpRoomHash.keySet().iterator();   it.hasNext();   )   { 
																	    	Integer   key   =   (Integer)   it.next(); 
																	        Object   value   =   eqpRoomHash.get(key);
																	        EqpRoom eqpRoom=eqpRoomHash.get(key);
																			String room=eqpRoom.getName();
																			if(cabinetid!=null&&(cabinetid==key)){
																				selected="selected";
																			}else{
																				selected="";
																			}
																			
																	        %>
																	        <option value="<%=key%>" <%=selected %>><%=room%></option>
																	        <%
																	    } 
																	    }
                                                                  %>
                                                                       </select>
																		 &nbsp;��ʼ����<input type="text" name="startdate" value="<%=startdate%>" size="10">
																				 <a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar1,document.forms[0].startdate,null,0,330)">
																									<img id=imageCalendar1 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0> </a> 
																		       ��ֹ����<input type="text" name="todate" value="<%=todate%>" size="10" />
																				     <a onclick="event.cancelBubble=true;" href="javascript:ShowCalendar(document.forms[0].imageCalendar2,document.forms[0].todate,null,0,330)">
																									<img id=imageCalendar2 align=absmiddle width=34 height=21 src="<%=rootPath%>/include/calendar/button.gif" border=0> </a> 
																	     &nbsp;&nbsp;<input type="button" value="��  ѯ" onclick="search()">
																	</td>
																	<td class="body-data-title" style="text-align: right;">
																	    <span style="CURSOR: hand" onclick="exportReport('doc')"><img name="selDay1" alt='����WORD' src="<%=rootPath%>/resource/image/export_word.gif" width=18 border="0">����WORD</span>&nbsp;&nbsp;&nbsp;
																		<span style="CURSOR: hand" onclick="exportReport('xls')"><img name="selDay1" alt='����EXCEL' src="<%=rootPath%>/resource/image/export_excel.gif" width=18 border="0">����EXCEL</span>&nbsp;&nbsp;&nbsp;
																		<span style="CURSOR: hand" onclick="exportReport('pdf')"><img name="selDay1" alt='����PDF' src="<%=rootPath%>/resource/image/export_pdf.gif" width=18 border="0">����PDF</span>&nbsp;&nbsp;&nbsp;&nbsp;
																		<a href="#" onclick="toAdd()">���</a>
																		<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<td class="body-data-title">
																		<jsp:include page="../../common/page.jsp">
																			<jsp:param name="curpage" value="<%=jp.getCurrentPage()%>" />
																			<jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>" />
																		</jsp:include>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table>
																<tr>
																	<td align="center" class="body-data-title" width="4%">
																		<INPUT type="checkbox" class=noborder name="checkall"
																			onclick="javascript:chkall()">
																	</td>
																	<td align="center" class="body-data-title" width="4%">
																		���
																	</td>
																	<td align="center" class="body-data-title" width="14%">
																		����
																	</td>
																	<td align="center" class="body-data-title" width="15%">
																		��λ
																	</td>
																	<td align="center" class="body-data-title" width="15%">
																		����ʱ��
																	</td>
																	<td align="center" class="body-data-title" width="15%">
																		�볡ʱ��
																	</td>
																	<td align="center" class="body-data-title" width="12%">
																		����λ��
																	</td>
																	<td align="center" class="body-data-title" width="14%">
																		������
																	</td>
																	<td align="center" class="body-data-title" width="5%">
																		����
																	</td>
																	
																</tr>
																<%
																	int startRow = jp.getStartRow();
																	for (int i = 0; i < rc; i++) {
																		RoomGuest vo = list.get(i);
																		if(vo==null)continue;
																		String room="";
																		EqpRoom eqpRoom=null;
																		
																		if(eqpRoomHash.containsKey(vo.getCabinetid())){
																			eqpRoom=eqpRoomHash.get(vo.getCabinetid());
																			room=eqpRoom.getName();
																		}
																%>
																<tr<%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list">
																		<INPUT type="checkbox" class=noborder name=checkbox
																			value="<%=vo.getId()%>">
																	</td>
																	<td align="center" class="body-data-list">
																		<font color='blue'><%=startRow + i%></font>
																	</td>
																	<td align="center" class="body-data-list"><a href="#" onclick="detail('<%=vo.getId() %>')"><%=vo.getName() %></a></td>
																	<td align="center" class="body-data-list"><%=vo.getUnit()%></td>
																	<td align="center" class="body-data-list"><%=vo.getInTime() %></td>
																	<td align="center" class="body-data-list"><%=vo.getOutTime() %></td>
																	
																	<td align="center" class="body-data-list"><%=room%></td>
																	<td align="center" class="body-data-list"><%=vo.getAudit()%></td>
																	<td align="center" class="body-data-list">
																		<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','','1111') title="�Ҽ��˵�">
			       													</td>
																</tr>
																<%
																	}
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
			</td>
			</tr>
			</table>
			
		</form>
	</BODY>
</HTML>
