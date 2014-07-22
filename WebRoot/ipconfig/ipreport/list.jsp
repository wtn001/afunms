<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%@page import="com.afunms.ip.stationtype.dao.*"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ include file="/include/globe.inc"%>
<%
	List list = (List) request.getAttribute("list");
	int rc = list.size();

	JspPage jp = (JspPage) request.getAttribute("page");
	String rootPath = request.getContextPath();
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />

		<script language="JavaScript" type="text/javascript">
  var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/ipAll.do?action=delete";
  var listAction = "<%=rootPath%>/ipAll.do?action=reportlist";
  
  var alertInfo = "确实要删除所选记录吗?";
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/ipconfig/alltype/add.jsp";
     mainForm.submit();
  }
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
									                	<td class="content-title"> IP管理>> 配置管理 >>IP报表  </td>
									                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									       			</tr>
									        	</table>
		        							</td>
										</tr>
										<tr>
											<td>
												<table id="content-body" class="content-body">
													
														<input type="hidden" name="nation" value="1">
														<input type="hidden" name="intMultibox">
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
																 <tr>
															         <td class="body-data-title">
															              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															              &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
															              <a href="<%=rootPath%>/ipAll.do?action=createdoc" >
                                                                          <img name="selDay1" alt='导出word' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0">导出WORLD</a>
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
																		所在平面
																	</td>
																	<td align="center" class="body-data-title">
																		骨干点
																	</td>
																	<td align="center" class="body-data-title" >
																		IP地址定义
																	</td>
																	<td align="center" class="body-data-title" >
																		IP地段
																	</td>
																	<td align="center" class="body-data-title">
																		IP总数
																	</td>
																	<td align="center" class="body-data-title">
																		已使用
																	</td>
																	<td align="center" class="body-data-title">
																		使用率
																	</td>
																</tr>
																<%
																	alltype vo = null;
																	int startRow = jp.getStartRow();
																	int z_num_loop = 0;
																	int u_num_loop = 0;
																	int z_num_pe = 0;
																	int u_num_pe = 0;
																	int z_num_pe_ce = 0;
																	int u_num_pe_ce = 0;
																	int z_num_bussiness = 0;
																	int u_num_bussiness = 0;
																	for (int i = 0; i < rc; i++) {
																		vo = (alltype) list.get(i);
																		//loopback
																		alltypeDao ad = new alltypeDao();
																		int loop_l = ad.count("ip_loopback","where loopback_id ="+vo.getId());
																		z_num_loop = loop_l;
																		alltypeDao ad1 = new alltypeDao();
																		int loop_l1 = ad1.count("ip_loopback","where type=1 and loopback_id ="+vo.getId());
																		u_num_loop = loop_l1;
																		float loopback1 = (float)loop_l1/(float)loop_l;
																	    DecimalFormat format = new DecimalFormat("#.00");
                                                                        String loopback = format.format(loopback1*100);
																		//pe
																		alltypeDao ad_p = new alltypeDao();
																		int pe_l = ad_p.count("ip_pe","where pe_id ="+vo.getId());
																		z_num_pe = pe_l;
																		alltypeDao ad_p1 = new alltypeDao();
																		int pe_l1 = ad_p1.count("ip_pe"," where type=1 and pe_id ="+vo.getId());
																		u_num_pe = pe_l1;
																		float pe1 = (float)pe_l1/(float)pe_l;
																	    DecimalFormat format1 = new DecimalFormat("#.00");
                                                                        String pe = format1.format(pe1*100);
																		//pe_ce
																		alltypeDao ad_pc = new alltypeDao();
																		int pc_l = ad_pc.count("ip_pe_ce"," where pe_ce_id ="+vo.getId());
																		z_num_pe_ce = pc_l;
																		alltypeDao ad_pc1 = new alltypeDao();
																		int pc_l1 = ad_pc1.count("ip_pe_ce"," where type=1 and pe_ce_id ="+vo.getId());
																		u_num_pe_ce = pc_l1;
																		float pe_ce1 = (float)pc_l1/(float)pc_l;
																	    DecimalFormat format2 = new DecimalFormat("#.00");
                                                                        String pe_ce = format2.format(pe_ce1*100);
																		//bussiness
																		alltypeDao bu = new alltypeDao();
																		int bu_l = bu.count("(select distinct bussiness ,t.id,t.bussiness_id,t.type  from ip_bussiness t ) x"," where bussiness_id ="+vo.getId());
																		z_num_bussiness = bu_l;
																		alltypeDao bu1 = new alltypeDao();
																		int bu_l1 = bu1.count("(select distinct bussiness ,t.id,t.bussiness_id,t.type  from ip_bussiness t) x ","where type=1 and bussiness_id ="+vo.getId());
																		u_num_bussiness = bu_l1;
																		float bussiness1 = (float)bu_l1/(float)bu_l;
																	    DecimalFormat format3 = new DecimalFormat("#.00");
                                                                        String bussiness = format3.format(bussiness1*100);
																%>
																<tr <%=onmouseoverstyle%>>
																	<td align="center" class="body-data-list" rowspan=6>
																		<font color='blue'>平面<%=vo.getSection()%></font>
																	</td>
																</tr>
																<tr>
																	<td align="center" class="body-data-list" rowspan=5><%=vo.getBackbone_name()%></td>
																</tr>
																<tr>
																   <td align="center" class="body-data-list" >loopback地址</td>
																   <td align="center" class="body-data-list"><a href="#" style="cursor: hand"
																			onclick="window.showModalDialog('<%=rootPath%>/ipAll.do?action=listloopback&id=<%=vo.getId()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=SysUtil.ifNull(vo.getLoopback_begin())%>&nbsp;&nbsp;--&nbsp;&nbsp;<%=SysUtil.ifNull(vo.getLoopback_end())%></td>
																   <td align="center" class="body-data-list" ><%=z_num_loop %></td>
																   <td align="center" class="body-data-list" ><%=u_num_loop %></td>
																   <td align="center" class="body-data-list" ><%=loopback+"%" %></td>
																</tr>
																<tr>
																    <td align="center" class="body-data-list" >PE互联地址</td>
																	<td align="center" class="body-data-list"><a href="#" style="cursor: hand"
																			onclick="window.showModalDialog('<%=rootPath%>/ipAll.do?action=listpe&id=<%=vo.getId()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=SysUtil.ifNull(vo.getPe_begin())%>&nbsp;&nbsp;--&nbsp;&nbsp;<%=SysUtil.ifNull(vo.getPe_end())%></td>
																	<td align="center" class="body-data-list" ><%=z_num_pe %></td>
																   <td align="center" class="body-data-list" ><%=u_num_pe %></td>
																   <td align="center" class="body-data-list" ><%=pe+"%" %></td>
																</tr>
																<tr>
																     <td align="center" class="body-data-list" >PE_CE互联地址</td>
																     <td align="center" class="body-data-list"><a href="#" style="cursor: hand"
																			onclick="window.showModalDialog('<%=rootPath%>/ipAll.do?action=listpe_ce&id=<%=vo.getId()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=SysUtil.ifNull(vo.getPe_ce_begin())%>&nbsp;&nbsp;--&nbsp;&nbsp;<%=SysUtil.ifNull(vo.getPe_ce_end())%></td>
																     <td align="center" class="body-data-list" ><%=z_num_pe_ce %></td>
																   <td align="center" class="body-data-list" ><%=u_num_pe_ce %></td>
																   <td align="center" class="body-data-list" ><%=pe_ce+"%" %></td>
																</tr>
																<tr>
																     <td align="center" class="body-data-list" >业务地址</td>
																     <td align="center" class="body-data-list"><a href="#" style="cursor: hand"
																			onclick="window.showModalDialog('<%=rootPath%>/ipAll.do?action=listbussiness&id=<%=vo.getId()%>',window,',dialogHeight:400px;dialogWidth:600px')"><%=vo.getBussiness_begin()%>&nbsp;&nbsp;--&nbsp;&nbsp;<%=vo.getBussiness_end()%></td>
															         <td align="center" class="body-data-list" ><%=z_num_bussiness %></td>
																   <td align="center" class="body-data-list" ><%=u_num_bussiness %></td>
																   <td align="center" class="body-data-list" ><%=bussiness+"%" %></td>
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
		</form>
	</BODY>
</HTML>
