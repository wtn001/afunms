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
	ip_select list = (ip_select) request.getAttribute("list");
	String backbone1 = (String)request.getAttribute("backbone1");
	String backbone2 = (String)request.getAttribute("backbone2");
	System.out.println(list);
	
	List loopback = (List)request.getAttribute("loopback");
	List pe = (List)request.getAttribute("pe");
	List pe_ce = (List)request.getAttribute("pe_ce");
	List bussiness = (List)request.getAttribute("bussiness");
	
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
      <a href="<%=rootPath%>/ipfield.do?action=createdoc&id=<%=list.getId() %>" >
      <img name="selDay1" alt='' style="CURSOR:hand" src="<%=rootPath%>/resource/image/export_word.gif" width=18  border="0"><font size='2'>导出WORLD</font></a>
      </span>
      </h2>
<form id="applyForm" name="applyForm" method="post" action="${ctx}/sendApply.action" >



<%  if(list != null){ %>
<div>
     <p class="fl">场站名:
        <label><input name="name" type="text" class="keywords" id="name" maxlength="50" value="<%=list.getName()%>" readonly  /> </label>
	  </p>
	  <p class="fl">&nbsp;&nbsp;&nbsp;&nbsp;平面1:
        <label> 
            <input type="text" name="backbone1" value="<%=backbone1 %>"  readonly  />
         </label>
      </p>
      <p class="fl">&nbsp;&nbsp;&nbsp;&nbsp;平面2:
        <label> 
            <input type="text" name="backbone2" value="<%=backbone2 %>"  readonly  />
         </label>
      </p>
      
</div>
<div>
															
						
															<table>
															
															<tr>
       <td colspan=2> <hr size=1 ></hr></td>
       </tr>
       <tr>
          <td style="padding-left:0px;padding-bottom:15px;" width="50%">路由器</td>
          <td  style="padding-left:0px;padding-bottom:15px;" width="50%">交换机</td>
       </tr>
        <tr>
          <td style="align:center" width="50%"> 
             <p class="fl">厂家:
               <label><input name="routeName" type="text" class="keywords" id="routeName" maxlength="50" value="<%=list.getRouteName() %>" readonly  /> </label>
	         </p>
	      </td>
          <td  style="align:center" width="50%">
             <p class="fl">厂家:
                <label> <input name="switchName" type="text" class="keywords" id="switchName" maxlength="50" value="<%=list.getSwitchName() %>" readonly  /> </label>
             </p>
          </td>
       </tr>
       <tr>
          <td style="align:center" width="50%"> 
             <p class="fl">型号:
               <label><input name="routeType" type="text" class="keywords" id="routeType" maxlength="50" value="<%=list.getRouteType() %>" readonly  /> </label>
	         </p>
	      </td>
          <td  style="align:center" width="50%">
             <p class="fl">型号:
                <label> <input name="switchType" type="text" class="keywords" id="switchType" maxlength="50" value="<%=list.getSwitchType() %>" readonly  /> </label>
             </p>
          </td>
       </tr>
        <tr>
          <td style="align:center" width="50%"> 
             <p class="fl">台数:
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="<%=list.getRouteCount() %>" readonly  /> </label>
	         </p>
	      </td>
          <td  style="align:center" width="50%">
             <p class="fl">台数:
                <label> <input name="switchCount" type="text" class="keywords" id="switchCount" maxlength="50" value="<%=list.getSwitchCount() %>" readonly  /> </label>
             </p>
          </td>
       </tr>
       <tr>
       <td colspan=2><hr size=1 ></hr></td>
       </tr>
       
       <tr>
          <td  colspan="2"> 
             <p class="fl">有无其他设备:
               
	         </p>
             <p class="fl">
                <label>
                    <input name="switchType" type="text" class="keywords" id="switchType" maxlength="50" value="<%=list.getDeviceType() %><%=list.getDeviceCount() %>" readonly  />
                 </label>
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">有无网络机柜:
               
	         </p>
             <p class="fl">
                <label>
                    <input name="location" type="text" class="other" id="location" maxlength="50" <%if(list.getHasCabinet().equals("0")){ %>value="无"<%}else{ %>value="有"<%} %> readonly  />
                 </label>
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">网络安装地点:
               
	         </p>
             <p class="fl">
                <label> <input name="location" type="text" class="other" id="location" maxlength="50" value="<%=list.getLocation() %>" readonly  /> </label>
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">施工调试单位:
               
	         </p>
             <p class="fl">
                <label> <input name="debugUnit" type="text" class="other" id="debugUnit" maxlength="50" value="<%=list.getDebugUnit() %>" readonly  /> </label>
             </p>
          </td>
       </tr>
       
        
       <tr>
       <td colspan=2><hr size=1 ></hr></td>
       </tr>
       
       
    
       
       
															
															
															</table>
															<%} %>
		<table>
		     <tr>
          <td style="padding-left:0px;padding-bottom:15px;" width="90%" align='center' colspan=2>Loopback地址</td>
          <td style="padding-left:0px;padding-bottom:15px;" width="90%" align='center' colspan=2>设备命名</td>
       </tr>
       
          <tr>
          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>路由器</td>
          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>交换机</td>
          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>路由器</td>
          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>交换机</td>
          </tr>
          <tr>
          <td>
              <table>
         <%
             if(loopback.size()!=0 && loopback!=null){
               loopbackstorage loop = null;
               for(int i=0;i<list.getRouteCount();i++){
                    loop = (loopbackstorage)loopback.get(i);
                       
        %>
        <tr>
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="<%=loop.getLoopback() %>" readonly  /> </label>
	         </p>
	      </td> 
	    </tr>
       <%
          }
       }
        %>
                 
              </table>
              </td>
             
              
              <td>
              <table>
                    <%
             if(loopback.size()!=0 && loopback!=null){
               loopbackstorage loop = null;
               for(int i=list.getRouteCount();i<loopback.size();i++){
                    loop = (loopbackstorage)loopback.get(i);
                       
        %>
        <tr>
            
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="<%=loop.getLoopback() %>" readonly  /> </label>
	         </p>
	      </td> 
	     </tr>
       <%
          }
       }
        %>
              </table>
              
              </td>
              
              <td>
              <table>
         <%
             if(loopback.size()!=0 && loopback!=null){
               loopbackstorage loop = null;
               for(int i=0;i<list.getRouteCount();i++){
                    loop = (loopbackstorage)loopback.get(i);
                       
        %>
        <tr>
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="" readonly  /> </label>
	         </p>
	      </td> 
	    </tr>
       <%
          }
       }
        %>
                 
              </table>
              </td>
              
              
               <td>
              <table>
                    <%
             if(loopback.size()!=0 && loopback!=null){
               loopbackstorage loop = null;
               for(int i=list.getRouteCount();i<loopback.size();i++){
                    loop = (loopbackstorage)loopback.get(i);
                       
        %>
        <tr>
            
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="" readonly  /> </label>
	         </p>
	      </td> 
	     </tr>
       <%
          }
       }
        %>
              </table>
              
              </td>       
          </tr>
       
    
    
         <!-- -------------------------------------------------------------------------------------------------------------- -->
       <tr>
           <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>链路起点名称</td>
          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>链路起点IP地址</td>
          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>链路终点名称</td>
          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>链路终点地址</td>
       </tr>
       
        <tr>
          <td>
              <table>
         <%
             if(pe.size()!=0 && pe!=null){
               pestorage loop = null;
               for(int i=0;i<pe.size();i++){
                    loop = (pestorage)pe.get(i);
                       
        %>
        <tr>
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="" readonly  /> </label>
	         </p>
	      </td> 
	    </tr>
       <%
          }
       }
        %>
                 
              </table>
              </td>
             
              
              <td>
              <table>
                    <%
             if(pe.size()!=0 && pe!=null){
               pestorage loop = null;
               for(int i=0;i<pe.size();i++){
                    loop = (pestorage)pe.get(i);
                       
        %>
        <tr>
            
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="<%=loop.getPe() %>" readonly  /> </label>
	         </p>
	      </td> 
	     </tr>
       <%
          }
       }
        %>
              </table>
              
              </td>
              
              <td>
              <table>
         <%
             if(pe.size()!=0 && pe!=null){
               pestorage loop = null;
               for(int i=0;i<pe.size();i++){
                    loop = (pestorage)pe.get(i);
                       
        %>
        <tr>
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="" readonly  /> </label>
	         </p>
	      </td> 
	    </tr>
       <%
          }
       }
        %>
                 
              </table>
              </td>
              
              
               <td>
              <table>
                    <%
             if(pe_ce.size()!=0 && pe_ce!=null){
               pe_cestorage loop = null;
               for(int i=0;i<pe_ce.size();i++){
                    loop = (pe_cestorage)pe_ce.get(i);
                       
        %>
        <tr>
            
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="<%=loop.getPe_ce() %>" readonly  /> </label>
	         </p>
	      </td> 
	     </tr>
       <%
          }
       }
        %>
        
                                   
              </table>
              
              </td>       
          </tr>
       
       
                                     <tr>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center' >网管地址</td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50" >
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>       
                                               
                                     </tr>
                                     <tr>
                                         <td  ></td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>       
                                     
                                     </tr>
                                     
                                     <tr>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center' >骨干点端口号1：</td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50" >
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>       
                                               
                                     </tr>
                                     <tr>
                                          <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center' >骨干点端口号2：</td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>
                                         <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center'>
                                           <input type = "text" maxlength="50">
                                         </td>       
                                     
                                     </tr>
                                     <tr>
                                        <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center' colspan=2>MPLS VPN RD/RT</td>
                                        <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center' colspan=2> <input type = "text" maxlength="50"></td>
                                     </tr>
                                     <tr>
                                                   <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center' colspan=4>VPN应用地址</td>
                                                   </tr>
                                                   <tr>
                                                   <td style="padding-left:0px;padding-bottom:15px;" width="25%"   align='center' colspan=4>
                                                      <table>
                                                           <%
             if(bussiness.size()!=0 && bussiness!=null){
               ip_bussinesstype loop = null;
               for(int i=0;i<bussiness.size();i++){
                    loop = (ip_bussinesstype)bussiness.get(i);
                    DaoInterface busst = new bussinessstorageDao();
			        bussinessstorage busst_vo = (bussinessstorage) busst.findByID(loop.getVpn());
                       
        %>
        <tr>
        <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="<%=loop.getName() %>" readonly  /> </label>
	         </p>
	      </td> 
          <td style="align:center" width="30%"> 
             <p class="fl">
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="<%=busst_vo.getBussiness() %>" readonly  /> </label>
	         </p>
	      </td> 
	    </tr>
       <%
          }
       }
        %>
                                                          
                                                      </table>
                                                   </td>
                                     </tr>
                                      
                                      <tr>
																<TD nowrap colspan="4" align=center>
																<br><input type="button" value="确定" style="width:50" id="" onclick="process()">&nbsp;&nbsp;
																	<input type="reset" style="width:50" value="返回" onclick="javascript:history.back(1)">
																</TD>	
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