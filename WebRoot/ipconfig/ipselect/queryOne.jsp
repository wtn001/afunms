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
	
	
    field f_vo = (field)request.getAttribute("field");
    String backbone_name = (String)request.getAttribute("backbone_name");
    String running = (String)request.getAttribute("running");
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
									                	<td class="content-title"> IP管理 >> IP分配 >> IP查询 >> 单个厂站 </td>
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
																		节点名称
																	</td>
																	<td align="center" class="body-data-title">
																		运营状态
																	</td>
																	<td align="center" class="body-data-title">
																		属于骨干点
																	</td>
																	<td align="center" class="body-data-title">
																		LoopBack地址
																	</td>
																	<td align="center" class="body-data-title">
																		PE互联地址
																	</td>
																	<td align="center" class="body-data-title">
																		PE_CE互联地址
																	</td>
																	<td align="center" class="body-data-title">
																		业务类型
																	</td>
															         <td align="center" class="body-data-title" >
																		业务名称
																	</td>
																	 <td align="center" class="body-data-title" >
																		Vlan
																	</td>
                                                                     <td align="center" class="body-data-title" >
																		网段
																	</td>
																	<td align="center" class="body-data-title" >
																		网关
																	</td>
																	<td align="center" class="body-data-title" >
																		纵向加密
																	</td>
																	<td align="center" class="body-data-title" >
																		设备接入地址
																	</td>
																																       
																</tr>
																	<%
																	      loopbackstorageDao lp = new loopbackstorageDao();
																	      pestorageDao p = new pestorageDao();
																	      pe_cestorageDao pc = new pe_cestorageDao();
																	      
																	      loopbackstorage loop_vo = null;
																	      pestorage p_vo = null;
																	      pe_cestorage pc_vo = null;
																	      
																	      List l_loop = lp.loadAll(" where field_id="+f_vo.getId());
																	      List l_pe = p.loadAll(" where field_id="+f_vo.getId());
																	      List l_pc = pc.loadAll(" where field_id="+f_vo.getId());
																	      
																	      loop_vo = (loopbackstorage)l_loop.get(0);
																	      p_vo = (pestorage)l_pe.get(0);
																	      pc_vo = (pe_cestorage)l_pc.get(0);
																	      
																	 %>
														
                                                          
                                                                    <tr <%=onmouseoverstyle%>>
																	
																	<td align="center" class="body-data-list" rowspan=<%=17 %>>
																	   <%=f_vo.getName() %>
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=17 %>>
																	   <%=running %>
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=17 %>>
																	   <%=backbone_name %>
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=17 %>>
																	   <%=loop_vo.getLoopback() %>
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=17 %>>
																	   <br><%=p_vo.getBackbone1() %>/30</br>
																	   <br><%=p_vo.getBackbone2() %>/30</br>    
																	</td>
																	<td align="center" class="body-data-list" rowspan=<%=17 %>>
																	   <br><%=pc_vo.getS1() %>/30</br>
																	   <br><%=pc_vo.getS2() %>/30</br>
																	</td>
																	<td  align="center" class="body-data-list">
																	          <%
																	              ip_bussinesstype bus_vo = null;
																	              bussinessDao bus = new bussinessDao();
																	              List bussiness = bus.loadAll(" where field_id="+f_vo.getId());
																	                for(int j=0;j<bussiness.size();j++){
																	                   bus_vo = (ip_bussinesstype)bussiness.get(j);
																	           %>
																	           <tr>
																	               <td align="center" class="body-data-list"  ><%=bus_vo.getBuskind() %></td>
																	               <td align="center" class="body-data-list" ><%=bus_vo.getBusname() %></td>
																	               <td align="center" class="body-data-list" ><%=bus_vo.getVlan() %></td>
																	               <td align="center" class="body-data-list" ><%=bus_vo.getSegment() %>/28</td>
																	               <td align="center" class="body-data-list" ><%=bus_vo.getGateway() %></td>
																	               <td align="center" class="body-data-list" ><%=bus_vo.getEncryption() %></td>
																	               <td align="center" class="body-data-list" ><%=bus_vo.getIp_use() %></td>
																	          </tr>
																	          <%
																	                }
																	           %>
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