<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.config.model.Supper"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.common.base.*"%>
<%@page import="com.afunms.ip.stationtype.dao.*"%>
<%@page import="com.afunms.ip.stationtype.model.*"%>
<%
	String rootPath = request.getContextPath();
	ip_select list = (ip_select) request.getAttribute("is_vo");
	String backbone_name = (String)request.getAttribute("backbone_name");
	String running = (String)request.getAttribute("running");
	field f_vo = (field)request.getAttribute("field");
	
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
     mainForm.action = "<%=rootPath%>/ipfield.do?action=queryAll";
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
  
  
  function process(){
        	mainForm.action = "<%=rootPath%>/ipconfig/ipselect/select.jsp";
        	mainForm.submit();
  }

	
 //Ext.get("process").on("click",function(){
 //           alert("你就坑爹吧！");
 //       	mainForm.action = "<%=rootPath%>/ipfield.do?action=queryAll";
//        	mainForm.submit();
       // mainForm.submit();
 //});	
	
  
  
  
  
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
									                	<td class="content-title"> IP管理 >> IP查询 >> 调度数据网络接入表 </td>
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
																	<td class="body-data-title" style="text-align: center;">
																	 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
																	 <input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													
													<tr>
														<td>
														<div class="messagesBox">
    <div class="login_box fl right_10" align=center>
<h2 class="title_text">
      <span><img src="images/login_icon.jpg" /></span> 
      <span>调度数据网络接入申请表</span>
      <span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </span>
      <span >
      <a href="<%=rootPath%>/ipfield.do?action=createdoc&id=<%=list.getId() %>&field_id=<%=f_vo.getId() %>" >
      <img name="selDay1" alt='' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0"><font size='2'>导出WORLD</font></a>
      </span>
      </h2>
<form id="applyForm" name="applyForm" method="post" action="${ctx}/sendApply.action" >



<%  if(list != null){ %>
<div>
     <p class="fl">场站名:
        <label><input name="name" type="text" class="keywords" id="name" maxlength="50" value="<%=list.getName()%>" readonly  /> </label>
	  </p>
	  <p class="fl">&nbsp;&nbsp;&nbsp;&nbsp;运营状态:
        <label> 
            <input type="text" name="backbone1" value="<%=running %>"  readonly  />
         </label>
      </p>
      <p class="fl">&nbsp;&nbsp;&nbsp;&nbsp;接入骨干点:
        <label> 
            <input type="text" name="backbone2" value="<%=backbone_name %>"  readonly  />
         </label>
      </p>
</div>
<div>
															
						
<table>
       <tr>
       <td colspan=4><hr size=1 ></hr></td>
       </tr>
       <tr>
       <p class="fl"><td style="padding-left:0px;padding-bottom:15px;" width="25%" copspan=2></td>
	    </p>
        <p class="fl"><td style="padding-left:0px;padding-bottom:15px;" width="25%" copspan=2>路由器</td>
	    </p>
	     <p class="fl"><td  style="padding-left:0px;padding-bottom:15px;" width="25%">交换机(一)</td>
	    </p>
	     <p class="fl"><td  style="padding-left:0px;padding-bottom:15px;" width="25%">交换机(二)</td>
	    </p>
       </tr>
        <tr>
          <td style="align:center" width="10%"> 
          <p class="fl">厂家:
	         </p>
	      </td>
          <td  style="align:center" width="25%">
             <input name="routeType" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getRFactory()%>" readonly  /> 
          </td>
          <td  style="align:center" width="25%">
             <input name="s1Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getS1Factory() %>" readonly  /> 
          </td>
          <td  style="align:center" width="25%">
             <input name="s2Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getS2Factory() %>" readonly  /> 
          </td>
       </tr>
       <tr>
          <td style="align:center" width="10%"> 
          <p class="fl">型号:
	         </p>
	      </td>
           <td  style="align:center" width="25%">
             <input name="s2Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getRouteType() %>" readonly  /> 
          </td>
           <td  style="align:center" width="25%">
             <input name="s2Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getS1Type() %>" readonly  /> 
          </td>
           <td  style="align:center" width="25%">
             <input name="s2Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getS2Type() %>" readonly  /> 
          </td>
       </tr>
        <tr>
          <td style="align:center" width="10%"> 
          <p class="fl">设备名:
	         </p>
	      </td>
          <td  style="align:center" width="25%">
             <input name="s2Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getRouteName() %>" readonly  /> 
          </td>
           <td  style="align:center" width="25%">
             <input name="s2Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getS1Name() %>" readonly  /> 
          </td>
           <td  style="align:center" width="25%">
             <input name="s2Type" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getS2Name() %>" readonly  /> 
          </td>
       </tr>
       <tr>
       <td colspan=4><hr size=1 ></hr></td>
       </tr>
       
       <tr>
          <td  colspan="2"> 
             <p class="fl">有无其他设备:
               
	         </p>
             <p class="fl">
                    <input name="switchType" type="text" class="keywords" id="switchType" maxlength="50" value="<%=list.getDeviceType() %><%=list.getDeviceCount() %>" readonly  />
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">有无网络机柜:
               
	         </p>
             <p class="fl">
                    <input name="location" type="text" class="other" id="location" maxlength="50" <%if(list.getHasCabinet().equals("0")){ %>value="无"<%}else{ %>value="有"<%} %> readonly  />
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">网络安装地点:
               
	         </p>
             <p class="fl">
                <input name="location" type="text" class="other" id="location" maxlength="50" value="<%=list.getLocation() %>" readonly  /> 
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">施工调试单位:
               
	         </p>
             <p class="fl">
                 <input name="debugUnit" type="text" class="other" id="debugUnit" maxlength="50" value="<%=list.getDebugUnit() %>" readonly  /> 
             </p>
          </td>
       </tr>
       
        
       <tr>
       <td colspan=4><hr size=1 ></hr></td>
       </tr>
</table>
															<%} %>
		<%
		          loopbackstorageDao lp = new loopbackstorageDao();
		          List l_l = lp.loadAll(" where field_id = "+f_vo.getId());
		          loopbackstorage loop = (loopbackstorage)l_l.get(0);
		          
		          pestorageDao ps = new pestorageDao();
		          List l_p = ps.loadAll(" where field_id = "+f_vo.getId());
		          pestorage pe = (pestorage)l_p.get(0);
		          
		          pe_cestorageDao pcs = new pe_cestorageDao();
		          List l_pc = pcs.loadAll(" where field_id = "+f_vo.getId());
		          pe_cestorage pe_ce = (pe_cestorage)l_pc.get(0);
		 %>
		<table>
		      <tr>
		         <td  width="50%"   align='center'>厂站LoopBack地址</td>
		         <td  width="50%"   align='center'>
		           <%=loop.getLoopback() %>
		         </td>
		      </tr>
		      
		      <tr>
                  <td colspan=4><hr size=1 ></hr></td>
              </tr>
		      <tr>
		         <td  width="50%"   align='center' rowspan=2>PE_PE互联地址</td>
		         <td  width="50%"   align='center'>
		           <%=backbone_name+"1 :" %><%=pe.getBackbone1().split("\\.")[0]+"."+pe.getBackbone1().split("\\.")[1]+"."+pe.getBackbone1().split("\\.")[2]+"."+(Integer.parseInt(pe.getBackbone1().split("\\.")[3])+1) %> ---->  <%=f_vo.getName()+" :" %> <%= pe.getBackbone1().split("\\.")[0]+"."+pe.getBackbone1().split("\\.")[1]+"."+pe.getBackbone1().split("\\.")[2]+"."+(Integer.parseInt(pe.getBackbone1().split("\\.")[3])+2) %> 
		         </td>
		      </tr>
		      
		      <tr>
		         <td  width="50%"   align='center'>
		           <%=backbone_name+"2 :" %><%=pe.getBackbone2().split("\\.")[0]+"."+pe.getBackbone2().split("\\.")[1]+"."+pe.getBackbone2().split("\\.")[2]+"."+(Integer.parseInt(pe.getBackbone2().split("\\.")[3])+1) %> ---->  <%=f_vo.getName()+" :" %> <%= pe.getBackbone2().split("\\.")[0]+"."+pe.getBackbone2().split("\\.")[1]+"."+pe.getBackbone2().split("\\.")[2]+"."+(Integer.parseInt(pe.getBackbone2().split("\\.")[3])+2) %> 
		         </td>
		      </tr>
		      
		      <tr>
                  <td colspan=4><hr size=1 ></hr></td>
              </tr>
		      <tr>
		         <td  width="50%"   align='center' rowspan=2>PE_CE互联地址</td>
		         <td  width="50%"   align='center'>
		           <%=f_vo.getName()+" :" %><%=pe_ce.getS1().split("\\.")[0]+"."+pe_ce.getS1().split("\\.")[1]+"."+pe_ce.getS1().split("\\.")[2]+"."+(Integer.parseInt(pe_ce.getS1().split("\\.")[3])+1) %> ---->  <%=" S1 :" %> <%= pe_ce.getS1().split("\\.")[0]+"."+pe_ce.getS1().split("\\.")[1]+"."+pe_ce.getS1().split("\\.")[2]+"."+(Integer.parseInt(pe_ce.getS1().split("\\.")[3])+2) %> 
		         </td>
		      </tr>
		      
		      <tr>
		         <td  width="50%"   align='center'>
                  <%=f_vo.getName()+" :" %><%=pe_ce.getS2().split("\\.")[0]+"."+pe_ce.getS2().split("\\.")[1]+"."+pe_ce.getS2().split("\\.")[2]+"."+(Integer.parseInt(pe_ce.getS2().split("\\.")[3])+1) %> ---->  <%=" S1 :" %> <%= pe_ce.getS2().split("\\.")[0]+"."+pe_ce.getS2().split("\\.")[1]+"."+pe_ce.getS2().split("\\.")[2]+"."+(Integer.parseInt(pe_ce.getS2().split("\\.")[3])+2) %> 
		         </td>
		      </tr>
		 
		    <tr>
                  <td colspan=4><hr size=1 ></hr></td>
              </tr>
		 
		                              <tr>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="50%"   align='center' >骨干点端口号1：</td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="50%"   align='center'>
                                           <input type = "text" maxlength="50" >
                                         </td>
                                     </tr>
                                     <tr>
                                          <td style="padding-left:0px;padding-bottom:15px;" width="50%"   align='center' >骨干点端口号2：</td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="50%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>
                                     </tr>
                                      <tr>
                  <td colspan=4><hr size=1 ></hr></td>
              </tr>
                                     <tr>
                                        <td style="padding-left:0px;padding-bottom:15px;" width="50%"   align='center' >MPLS VPN RD/RT</td>
                                        <td style="padding-left:0px;padding-bottom:15px;" width="50%"   align='center' > 300:1</td>
                                     </tr>
              <tr>
                  <td colspan=4><hr size=1 ></hr></td>
              </tr>
              <tr>
                  <td colspan=4 align='center'>业务地址</td>
              </tr>
              <tr>
                  <td colspan=4><hr size=1 ></hr></td>
              </tr>
              
              <tr>
              <td colspan=4>
              <table>
              <%
                     bussinessDao bus = new bussinessDao();
                     List bu = bus.loadAll(" where field_id="+f_vo.getId());
                     ip_bussinesstype bussiness = null;
                     for(int i =0;i<bu.size();i++){ 
                           bussiness = (ip_bussinesstype)bu.get(i);
                           
               %>
               <tr>
               <td style="padding-left:0px;padding-bottom:15px;"   align='center' ><%=bussiness.getBusname() %></td>
               <td style="padding-left:0px;padding-bottom:15px;"    align='center' ><%=bussiness.getBuskind() %></td>
               <td style="padding-left:0px;padding-bottom:15px;"    align='center' ><%=bussiness.getVlan() %></td>
               <td style="padding-left:0px;padding-bottom:15px;"   align='center' ><%=bussiness.getVlan_ip() %></td>
               </tr>
               <%
                      }
                %>
                </tr>
              </td>
              </table>
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