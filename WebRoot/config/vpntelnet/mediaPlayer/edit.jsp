<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.config.model.Supper"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.config.dao.*"%>
<%@page import="com.afunms.config.model.*"%>
<%@page import="java.util.List"%>
<%
   MediaPlayer vo = (MediaPlayer)request.getAttribute("vo");
   String rootPath = request.getContextPath();
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>

<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->


<script language="JavaScript">
	 Ext.onReady(function()
	{ 
		Ext.get("process").on("click",function(){
			Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
    		mainForm.action = "<%=rootPath%>/mediaPlayer.do?action=update";
    		mainForm.submit();
 		});
	});
	function CreateWindow(url)
	{
		msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
	}
	
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
	<script type="text/javascript">
		function showup(){
			CreateWindow("<%=rootPath%>/config/vpntelnet/mediaPlayer/up.jsp");
		}
	</script>
</head>
<body id="body" class="body" onload="initmenu();">
	<form name="mainForm" method="post">
		<input type=hidden name="id" value="<%=vo.getId()%>">
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
						<td class="td-container-main-add">
							<table id="container-main-add" class="container-main-add">
								<tr>
									<td>
										<table id="add-content" class="add-content">
											<tr>
												<td>
													<table id="add-content-header" class="add-content-header">
									                	<tr>
										                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
										                	<td class="add-content-title">�Զ��� >> �Զ������� >> ִ����ʾ��Ƶ���� >> �޸���Ƶ</td>
										                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
										       			</tr>
										        	</table>
			        							</td>
			        						</tr>
			        						<tr>
			        							<td>
			        								<table id="detail-content-body" class="detail-content-body">
			        									<tr>
			        										<td>
			        											<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																	<tr>
																		<td nowrap align="right" height="24">
																			�ļ���&nbsp;
																		</td>
																		<td>
																			&nbsp;<input type="text" name="name" size="40" value="<%=vo.getName() %>">
																			<font color="red">&nbsp;*</font>
																		</td>
																	</tr>
																	<tr style="background-color: #ECECEC;">
																		<td nowrap align="right" height="24">
																			����ҵ��ϵͳ&nbsp;
																		</td>
																		<td>
																			&nbsp;<select name="bsid">
																				<option value="-1" >----��ѡ��----</option>
																				<%
																					BusinessSystemDao bd = new BusinessSystemDao();
																					List list = bd.loadAll();
																					for(int i = 0; i<list.size();i++){
																					BusinessSystem busi = (BusinessSystem)list.get(i);
																				 %>
																				 	<option value="<%=busi.getId() %>" <%if(vo.getBsid() == busi.getId()){ %>selected<%} %>><%=busi.getName() %></option>
																				 <%
																				 	}
																				  %>
																			</select>
																			<font color="red">&nbsp;*</font>
																		</td>
																	</tr>
																	<tr >
																		<td nowrap align="right" height="24" width="10%">
																			����&nbsp;
																		</td>
																		<td nowrap width="40%">
																		&nbsp;
																			<input type="text" id="aaa" size="20" readonly="readonly" value="<%=vo.getFileName() %>" name="fileName"><input type="button" value="��Ӹ���" onclick="showup()"><font color="red">&nbsp;*</font>
																		</td>
																	</tr>
																	<tr style="background-color: #ECECEC;">
																		<td nowrap align="right" height="24">
																			��ע��Ϣ&nbsp;
																		</td>
																		<td>
																			&nbsp;<input type="text" name="desc" value="<%=vo.getDesc() %>" size="80">
																		</td>
																	</tr>
																	<tr>
																		<td nowrap colspan="4" align=center>
																			<br><input type="button" value="�޸�" style="width:50" id="process" onclick="#">&nbsp;&nbsp;
																			<input type="reset" style="width:50" value="����" onclick="javascript:history.back(1)">
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
			        								<table id="detail-content-footer" class="detail-content-footer">
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
								<tr>
									<td>
										
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
</body>
</HTML>