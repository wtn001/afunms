<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.Supper"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.*"%>
<%@page import="com.afunms.ip.stationtype.dao.*"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%@page import="com.afunms.common.base.*"%>

<%
	String rootPath = request.getContextPath();
	
	List list = (List)request.getAttribute("list");
	//List field = (List)request.getAttribute("field");
	//List loopback = (List)request.getAttribute("loopback");
	//List pe = (List)request.getAttribute("pe");
	//List pe_ce = (List)request.getAttribute("pe_ce");

	JspPage jp = (JspPage) request.getAttribute("page");
	
	
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<link type="text/css" rel="stylesheet" href="<%=rootPath%>/ipconfig/ipselect/reset.css" />
        <link type="text/css" rel="stylesheet" href="<%=rootPath%>/ipconfig/ipselect/apply.css" />
        
		<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script language="JavaScript" type="text/javascript">
  var listAction = "<%=rootPath%>/supper.do?action=list";
  var delAction = "<%=rootPath%>/supper.do?action=delete";

  var xx = "";
  function toedit(){
      window.location="<%=rootPath%>/supper.do?action=ready_edit&id="+xx;
  }
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/supper.do?action=ready_add";
     mainForm.submit();
  }
</script>
		<script language="JavaScript" type="text/javascript">
  function toCheck()
  {
     if(FrmDeal.pwd.value=="")
     {
        alert("<����>����Ϊ��!");
        FrmDeal.pwd.focus();
        return false;
     }
     FrmDeal.action = "check.jsp";
     FrmDeal.submit();
  }
  
  

	
 Ext.get("process").on("click",function(){
  
        	mainForm.action = "<%=rootPath%>/";
        	mainForm.submit();
        
 
       // mainForm.submit();
 });	
	
});
  
  
  
  
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
	<body id="body" class="body" onload="initmenu();">
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
											<td>
												<table id="content-header" class="content-header">
								                	<tr>
									                	<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
									                	<td class="content-title"> IP���� >> IP���� >> LoopBack��ַ����� </td>
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
																	<td class="body-data-title" style="text-align: right;">
																	<a href="<%=rootPath%>/ipfield.do?action=createdocLoopback" >
                                                                     <img name="selDay1" alt='' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0"><font size='2'>����WORLD</font></a>
																  </td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														</tr>
														<table>
														<tr>
																	<td align="center" class="body-data-title">
																		<INPUT type="checkbox" class=noborder name="checkall"
																			onclick="javascript:chkall()">
																	</td>
																	<td align="center" class="body-data-title">
																		���
																	</td>
																	<td align="center" class="body-data-title">
																		�ڵ�����
																	</td>
																	<td align="center" class="body-data-title">
																		Loopback��ַ
																	</td>
															         <td align="center" class="body-data-title" >
																		������վ
																	</td>
																	 <td align="center" class="body-data-title" >
																		��Ӫ״̬
																	</td>
                                                                     <td align="center" class="body-data-title" >
																		��վLoopback��ַ
																	</td>
																	<td align="center" class="body-data-title" >
																		��&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;��
																	</td>															       
																</tr>
																	
														
                                                              <%
                                                                    alltype all = null;
                                                                    stationtypeDao st = new stationtypeDao();
                                                                    List station = st.loadAll();
                                                                    stationtype sta = null;
                                                                    Map m = new HashMap(); 
                                                                    for(int x=0;x<station.size();x++){
                                                                         sta = (stationtype)station.get(x);
                                                                         m.put(sta.getId()+"",sta.getName());
                                                                    }
                                                                    if(list.size() != 0 && list != null){
                                                                       for(int i=0;i<list.size();i++){
                                                                               all = (alltype)list.get(i);
                                                                               alltypeDao at = new alltypeDao();
                                                              
                                                                               int num = at.count("ip_field"," where backbone_id="+all.getId());
                                                               %>
                                                                    <tr <%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list" rowspan=<%=num+1 %>>
																		<INPUT type="checkbox" class=noborder name=checkbox
																			value="<%=all.getId()%>">
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=num+1 %>>
																		<font color='blue'><%=1 + i%></font>
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=num+1 %>>
																	   <%=all.getBackbone_name() %>
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=num+1 %>>
																	   <%=all.getLoopback_begin().split("\\.")[0]+"."+all.getLoopback_begin().split("\\.")[1]+"."+all.getLoopback_begin().split("\\.")[2]+"."+"1" %>----
																	   <%=all.getLoopback_begin().split("\\.")[0]+"."+all.getLoopback_begin().split("\\.")[1]+"."+all.getLoopback_begin().split("\\.")[2]+"."+"2" %>
																	</td>
																	<td  align="center" class="body-data-list">
																	          <%
																	              loopbackstorage lp = null;
																	              field f = null;
																	              fieldDao fd = new fieldDao();
																	              List field = fd.loadAll(" where backbone_id = "+all.getId());
																	              if(field.size()!=0 && field != null){
																	          //    System.out.println(all.getId()+"!!!!!!!!!!!"+field.size());
																	               DaoInterface loopbackstorage = null;
																	                for(int j=0;j<field.size();j++){
																	                    f = (field)field.get(j);
																	                    String running = m.get(f.getRunning())+"";
																	                    loopbackstorage= new loopbackstorageDao();  
																	           //          System.out.println("@@@@@@@@@@@@@@@@@@"+"select * from ip_loopback"+" where field_id = "+f.getId());
																	                    List loop = loopbackstorage.loadAll(" where field_id = "+f.getId());
																	            //        System.out.println("############"+loop); 
																	                    lp = null;
																	                     try{
																	                    lp = (loopbackstorage)loop.get(0);
																	                    }catch(Exception e){
																	                       e.printStackTrace();
																	                    }
																	                //    System.out.println(m.get(f.getRunning())+"############"+f.getRunning());
																	                  
																	           %>
																	           <tr>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=f.getName() %></td>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=running %></td>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=lp.getLoopback() %></td>
																	               <td align="center" class="body-data-list" style='width:125px;'>
																		                <a
																			             href="<%=rootPath%>/ipfield.do?action=ready_edit&id=<%=f.getId() %>">�޸�|
																		               </a>
																		                <a
																			             href="<%=rootPath%>/ipfield.do?action=deleteStation&id=<%=f.getId() %>">ɾ��
																		               </a>
																	               </td>
																	          </tr>
																	          <%
																	                }
																	                }
																	           %>
																	</td>
																</tr>
                                                               <%
																	                      }
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
		</form>
	</BODY>
</HTML>