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
									                	<td class="content-title"> IP管理 >> IP分配 >> PE_CE互联地址分配表 </td>
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
																	<a href="<%=rootPath%>/ipfield.do?action=createdocPe_ce" >
                                                                     <img name="selDay1" alt='' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0"><font size='2'>导出WORLD</font></a>
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
																		节点名称
																	</td>
																	<td align="center" class="body-data-title">
																		PE_CE互联地址段
																	</td>
																	<td align="center" class="body-data-title">
																		本端节点
																	</td>
															         <td align="center" class="body-data-title" >
																		交换机一(S1)
																	</td>
																	 <td align="center" class="body-data-title" >
																		交换机二(S2)
																	</td>
																</tr>
																	
														
                                                              <%
                                                                    alltype all = null;
                                                                    
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
																	   <%=all.getPe_ce_begin() %>---<%=all.getPe_ce_end().substring(0,all.getPe_ce_end().lastIndexOf("."))+".254" %>
																	</td>
																	<td  align="center" class="body-data-list">
																	          <%
																	              pe_cestorage lp = null;
																	              field f = null;
																	              fieldDao fd = new fieldDao();
																	              List field = fd.loadAll(" where backbone_id = "+all.getId());
																	              if(field.size()!=0 && field != null){
																	                for(int j=0;j<field.size();j++){
																	                    f = (field)field.get(j);
																	                    pe_cestorageDao lpd = new pe_cestorageDao();
																	                //    System.out.println("@@@@@@@@@@@@@@@@@@"+"select * from ip_pe_ce"+" where field_id = "+f.getId());
																	                    List loop = lpd.loadAll(" where field_id = "+f.getId());
																	               //     System.out.println("############"+loop.size());
																	                    lp = null;
																	                 try{
																	                    lp = (pe_cestorage)loop.get(0);
																	                    }catch(Exception e){
																	                       e.printStackTrace();
																	                    }
																	           %>
																	           <tr>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=f.getName() %></td>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=lp.getS1() %>/30</td>
																	               <td align="center" class="body-data-list" style='width:125px;'><%=lp.getS2() %>/30</td>
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