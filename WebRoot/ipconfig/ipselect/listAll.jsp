<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.Supper"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
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
        alert("<密码>不能为空!");
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
//修改菜单的上下箭头符号
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
//添加菜单	
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
									                	<td class="content-title"> IP管理 >> IP列表 </td>
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
																		序号
																	</td>
																	<td align="center" class="body-data-title">
																		节点类型
																	</td>
																	<td align="center" class="body-data-title">
																		Loopback地址段
																	</td>
																	
															<td align="center" class="body-data-title">
															<table>
															   <tr>	
															         <td align="center" class="body-data-title" style='width:125px;'>
																		下联厂站
																	</td>
																	 <td align="center" class="body-data-title" style='width:125px;'>
																		运营状态
																	</td>
                                                                     <td align="center" class="body-data-title" style='width:125px;'>
																		Loopback地址
																	</td>
																	<td align="center" class="body-data-title" style='width:125px;'>
																		PE互联地址
																	</td>
																	<td align="center" class="body-data-title" style='width:125px;'>
																		PE_CE互联地址
																	</td>		
																	<td align="center" class="body-data-title" style='width:125px;'>
																		操&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;作
																	</td>															       
																</tr>
														    </table>
																	
														
                                                              <%
                                                                    alltype all = null;
                                                                    if(list.size() != 0 && list != null){
                                                                       for(int i=0;i<list.size();i++){
                                                                               all = (alltype)list.get(i);
                                                               %>
                                                                    <tr <%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list">
																		<INPUT type="checkbox" class=noborder name=checkbox
																			value="<%=all.getId()%>">
																	</td>
																	<td align="center" class="body-data-list">
																		<font color='blue'><%=1 + i%></font>
																	</td>
																	<td align="center" class="body-data-list">
																	   <%=all.getBackbone_name() %>
																	</td>
																	<td align="center" class="body-data-list">
																	   <%=1 %>---<%=2 %>
																	</td>
																	
																	<td  align="center" class="body-data-list">
																	   <table>
																	                          <% 
																	                          DaoInterface field2 = new fieldDao();
																	                         List field3 = field2.loadAll(" where backbone_id = '"+all.getId()+"'");
																	                         if(field3.size() != 0 && field3 != null){
																	                                 field f = null;
																	                                 for(int j=0;j<field3.size();j++){
																	                                 f = (field)field3.get(j);
																	                                 DaoInterface lop = new loopbackstorageDao();
																	                                 List lk = lop.loadAll(" where field_id = '"+f.getId()+"'");
																	                                 %>
																	                                      <tr>
																	                                          <td align="center" class="body-data-list" rowspan=<%=lk.size()+1 %>  style='width:125px;'><%=f.getName()%></td>
																	                                          <td align="center" class="body-data-list" rowspan=<%=lk.size()+1 %>  style='width:125px;'><%=f.getRunning()%></td>
																	                                      </tr>
																	                                 <%
																	                           DaoInterface loop = new loopbackstorageDao();
																	                           DaoInterface p = new pestorageDao();
																	                           DaoInterface pc = new pe_cestorageDao();
																	                          List loopback = loop.loadAll(" where field_id = '"+f.getId()+"'");
																	                          List pe =  p.loadAll(" where field_id = '"+f.getId()+"'");
																	                          List pe_ce =  pc.loadAll(" where field_id = '"+f.getId()+"'"); 
																	                          
																	                          
																	                            
																	                             if(loopback.size() != 0 && loopback != null && pe_ce.size() != 0 && pe_ce != null && pe.size() != 0 && pe != null){
																	                                for(int k=0;k<loopback.size();k++){
																	                                  loopbackstorage l = (loopbackstorage)loopback.get(k); 
																	                                  pestorage ps = (pestorage)pe.get(k);
																	                                  pe_cestorage pcs = (pe_cestorage)pe_ce.get(k);
																	         %>
																	           <tr>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=l.getLoopback() %></td>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=ps.getBackbone1() %></td>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=pcs.getS1() %></td>
																	               <td align="center" class="body-data-list" style='width:125px;'>
																		                <a
																			             href="<%=rootPath%>/ipfield.do?action=ready_edit&id=<%=f.getId() %>">修改|
																		               </a>
																		                <a
																			             href="<%=rootPath%>/ipfield.do?action=deleteStation&id=<%=f.getId() %>">删除
																		               </a>
																	               </td>
																	          </tr>
																	         <%
																	                      }
																	                    }
																	         
																	               }
																	           }
																	          %>
																	    </table>
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