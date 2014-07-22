<%@page language="java" contentType="text/html;charset=GB18030" %>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.polling.om.IpMac"%>
<%@ page import="com.afunms.system.model.Department"%>
<%@ page import="com.afunms.system.dao.DepartmentDao"%>
<%@ page import="com.afunms.config.model.Employee"%>
<%@ page import="com.afunms.polling.om.IpMacChange"%>
<%@ page import="com.afunms.config.dao.EmployeeDao"%>
<%@ page import="com.afunms.topology.dao.IpMacBaseDao"%>
<%@ page  import="com.afunms.ip.stationtype.dao.*"%>
<%@ page  import="com.afunms.ip.stationtype.model.*"%>
<%@  page import="com.afunms.common.base.DaoInterface"%>
<%@ include file="/include/globe.inc"%>

<%
	String rootPath = request.getContextPath();
	//List list = (List)request.getAttribute("list");
	//int rc = list.size();

	//JspPage jp = (JspPage)request.getAttribute("page");
	
	
//	 DaoInterface field = new fieldDao();
 //    List list = field.loadAll();
 //    field vo = null;
     
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

       <script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/adapter/ext/ext-base.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/application/environment/resource/ext3.1/ext-all.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
         
		<script language="javascript">	
        
  function doQuery()
  {  
     var val = document.getElementById("id1").value;
     if(val == "" ) {
        alert("无可查询对象");
        return;
     }
     mainForm.action = "<%=rootPath%>/ipfield.do?action=find";
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
  
  function toDeleteAll()
  {
      mainForm.action = "<%=rootPath%>/ipmacchange.do?action=deleteall";
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

function change1(){
        var field_id = window.document.getElementById("id").value;
          $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/ipAjax.ajax?action=backbone&select="+field_id,
			success:function(data){
			document.getElementById("div1").innerHTML =data.option;
			}
		});
}
 function change2(){
        var field_id = window.document.getElementById("id2").value;
          $.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/ipAjax.ajax?action=station&select="+field_id,
			success:function(data){
			document.getElementById("div").innerHTML =data.option;
			}
		});
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
									                	<td class="content-title"> IP管理 >> IP查询 >> IP功能查询 </td>
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
															       <td bgcolor="#ECECEC" width="30%" align='center' height="100">
															           &nbsp;&nbsp;&nbsp;
																		<b>请选择界面：</b>
																		<select id="id" size=1 name='select' style='width:125px;' onchange="change1();">
																		    <option value=0>——请选择——</option>
																			<option value=1 >一平面</option>
																			<option value=2 >二平面</option>
																		</select>
															       </td>
															      
															       <td bgcolor="#ECECEC" width="30%" align='center' height="100" id="div1">
																	
																   </td>
															        <td bgcolor="#ECECEC" width="30%" align='center' height="100" id="div">
																	
																   </td>
																</tr>
															</table>
														</td>
													</tr>
													
													<tr>
													   <td>
													      <table class="content-body">
																<tr>
																 <td class="body-data-title"></td>
																</tr>
														  </table>
				                                       </td>
													</tr>
													
													
													<tr>
													  <td>
													    <table class="content-body">
													          <%
													             DaoInterface alltype1 = new alltypeDao();
													             DaoInterface field1 = new fieldDao();
													             alltype all_vo1 = null;
													             field f_vo1 = null;
													             List all1 =  alltype1.loadAll(" where section='1' ");
													             List num1 =  field1.loadAll(" where section='1' ");
													        //     System.out.println(num1.size()+"....................."+all1.size());
													             int x1 = 0;
													             if(all1.size()>num1.size()){
													                 x1 = all1.size();
													             }else{
													                 x1 = num1.size();
													             }
													         //    System.out.println(x1+"....................."+all1.size());
													             if(all1.size()!=0 && all1 != null){
													                
													          %>
													        <tr >
													          <td align="center" class="body-data-list" style='width:125px;'>一平面</td>
													           <td align="center" class="body-data-list" >
													             <table>
													             <%
													                for(int i =0;i<all1.size();i++){
													                     all_vo1 = (alltype)all1.get(i);
													                     DaoInterface f1 = new fieldDao();
													                     List backbone1 = f1.loadAll(" where section='1' and backbone_name='"+all_vo1.getId()+"'");
													                  //   System.out.println(all_vo1.getBackbone_name()+"----------------------------------->>>"+backbone1.size());
													              %>
													                
													                   <tr>
													                      <td align="center" class="body-data-list" rowspan=<%=backbone1.size()+1 %> style='width:125px;'><%=all_vo1.getBackbone_name() %></td>
													                   </tr>
													                
													           
													              <%
													                  DaoInterface alltype2 = new alltypeDao();
													                  DaoInterface field2 = new fieldDao();
													                  alltype all_vo2 = null;
													                  field f_vo2 = null;
													                  ip_select is2 =  null;
													                  List all2 =  alltype2.loadAll(" where section='1' ");
													                  List num2 =  field2.loadAll(" where section='1' and backbone_name='"+all_vo1.getId()+"'");
													                  if(all2.size()!=0 && all2 != null){
													                //    for(int k =0;k<all3.size();k++){
													                  //   all_vo3 = (alltype)all3.get(k);
													                //     DaoInterface f3 = new fieldDao();
													                //     List backbone3 = f3.loadAll(" where section='2' and backbone_name='"+all_vo2.getId()+"'");
													                     if(num2.size()!=0 && num2!=null){
													                      for(int j=0;j<num2.size();j++){
													                          f_vo2 = (field)num2.get(j);
													                          
													                           DaoInterface apply2 = new ip_selectDao();
													                           List l2 = apply2.loadAll(" where ids = '"+all_vo1.getId()+",1' and name='"+f_vo2.getName()+"'");
													                           is2 = (ip_select)l2.get(0);
													               %>
													                   <tr>
													                      <td align="center" class="body-data-list" style='width:125px;'>
													                      <a
																			href="<%=rootPath%>/ipfield.do?action=find&station=<%=f_vo2.getId() %>">&nbsp;&nbsp;<%=f_vo2.getName() %>
																		</a>
													                      </td>
													                      <td align="center" class="body-data-list" style='width:125px;'>&nbsp;<%=f_vo2.getRunning() %></td>
													                      <td align="center" class="body-data-list" style='width:125px;'>路由器<%=is2.getRouteCount() %>台</td>
													                      <td align="center" class="body-data-list" style='width:125px;'>交换机<%=is2.getSwitchCount() %>台</td>
													                      <td align="center" class="body-data-list" style='width:125px;'><%=is2.getDeviceType() %>设备<%=is2.getDeviceCount() %>台</td>
													                   </tr>
													              
													               <%
													                    }
													                      
													                       }
													                  }
													             //     }
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
													    <table class="content-body">
													          <%
													             DaoInterface alltype2 = new alltypeDao();
													             DaoInterface field2 = new fieldDao();
													             alltype all_vo2 = null;
													             field f_vo2 = null;
													             List all2 =  alltype2.loadAll(" where section='2' ");
													             List num2 =  field2.loadAll(" where section='2' ");
													         //    System.out.println(num2.size()+"....................."+all2.size());
													             int x2 = 0;
													             if(all2.size()>num2.size()){
													                 x2 = all2.size();
													             }else{
													                 x2 = num2.size();
													             }
													       //      System.out.println(x2+"....................."+all2.size());
													             if(all2.size()!=0 && all2 != null){
													                
													          %>
													        <tr >
													          <td align="center" class="body-data-list" style='width:125px;'>二平面</td>
													           <td align="center" class="body-data-list" >
													             <table>
													             <%
													                for(int i =0;i<all2.size();i++){
													                     all_vo2 = (alltype)all2.get(i);
													                     DaoInterface f2 = new fieldDao();
													                     List backbone2 = f2.loadAll(" where section='2' and backbone_name='"+all_vo2.getId()+"'");
													       //              System.out.println(all_vo2.getBackbone_name()+"----------------------------------->>>"+backbone2.size());
													              %>
													                
													                   <tr>
													                      <td align="center" class="body-data-list" rowspan=<%=backbone2.size()+1 %> style='width:125px;'><%=all_vo2.getBackbone_name() %></td>
													                   </tr>
													                
													           
													              <%
													                  DaoInterface alltype3 = new alltypeDao();
													                  DaoInterface field3 = new fieldDao();
													                  alltype all_vo3 = null;
													                  field f_vo3 = null;
													                  ip_select is1 =  null;
													                  List all3 =  alltype3.loadAll(" where section='2' ");
													                  List num3 =  field3.loadAll(" where section='2' and backbone_name='"+all_vo2.getId()+"'");
													                  if(all3.size()!=0 && all3 != null){
													                //    for(int k =0;k<all3.size();k++){
													                  //   all_vo3 = (alltype)all3.get(k);
													                //     DaoInterface f3 = new fieldDao();
													                //     List backbone3 = f3.loadAll(" where section='2' and backbone_name='"+all_vo2.getId()+"'");
													                     if(num3.size()!=0 && num3!=null){
													                      for(int j=0;j<num3.size();j++){
													                          f_vo3 = (field)num3.get(j);
													                          
													                           DaoInterface apply2 = new ip_selectDao();
													                           List l2 = apply2.loadAll(" where ids = '"+all_vo2.getId()+",2' and name='"+f_vo3.getName()+"'");
													                           is1 = (ip_select)l2.get(0);
													               %>
													                   <tr>
													                      <td align="center" class="body-data-list" style='width:125px;'>
													                      <a
																			href="<%=rootPath%>/ipfield.do?action=find&station=<%=f_vo3.getId() %>">&nbsp;&nbsp;<%=f_vo3.getName() %>
																		</a>
													                      </td>
													                      <td align="center" class="body-data-list" style='width:125px;'>&nbsp;<%=f_vo3.getRunning() %></td>
													                      <td align="center" class="body-data-list" style='width:125px;'>路由器<%=is1.getRouteCount() %>台</td>
													                      <td align="center" class="body-data-list" style='width:125px;'>交换机<%=is1.getSwitchCount() %>台</td>
													                      <td align="center" class="body-data-list" style='width:125px;'><%=is1.getDeviceType() %>设备<%=is1.getDeviceCount() %>台</td>
													                   </tr>
													              
													               <%
													                    }
													                      
													                       }
													                  }
													             //     }
													                  }
													                  }
													               %>
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
												                    			<td><br></td>
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
