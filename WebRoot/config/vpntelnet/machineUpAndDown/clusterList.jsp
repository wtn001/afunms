<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.system.util.UserView"%>
<%@ include file="/include/globe.inc"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%
  String rootPath = request.getContextPath();
  List<UpAndDownMachine> machineList = (List<UpAndDownMachine>)request.getAttribute("machineList");
int clusterId=(Integer)request.getAttribute("clusterId");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/table.js"></script> 
<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<LINK href="<%=rootPath%>/resource/css/itsm_style.css" type="text/css" rel="stylesheet">
<link href="<%=rootPath%>/resource/css/detail.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
  
  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=list";
     mainForm.submit();
  }
  function toDelete()
  {
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=delOrAdd&&id=<%=clusterId%>";
     mainForm.submit();
  }  
  function refreshCluster()
  {
     mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=clusterDetailList&id=<%=clusterId%>";
     mainForm.submit();
  }  
function toMovement(){
  	//alert(node);
  	var path = "<%=rootPath%>/serverUpAndDown.do?action=to_movement&id="+node;
  	<%
  		if(machineList.size() > 0){
  			System.out.println(")))))))))))))))))))))))))))IPADDR(((((((((((((((((((");
    		for(int i=0;i<machineList.size();i++){
       				UpAndDownMachine vo = machineList.get(i);
       				System.out.println("(((((((((((((((((((((((((((((IPADDR))))))))))))))))))))))))))"+vo.getIpaddress());
       		}
       	}
  		request.setAttribute("showMachineList",machineList); 
  	%>
  	window.open (path, '动态效果', 'height=330, width=400, top=110,left=220');
  } 
</script>
<script language="JavaScript">

	//公共变量
	var node="";
	var ipaddress="";
	var operate="";
	/**
	*根据传入的id显示右键菜单
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
	*显示弹出菜单
	*menuDiv:右键菜单的内容
	*width:行显示的宽度
	*rowControlString:行控制字符串，0表示不显示，1表示显示，如“101”，则表示第1、3行显示，第2行不显示
	*/
	function popMenu(menuDiv,width,rowControlString)
	{
	    //创建弹出菜单
	    var pop=window.createPopup();
	    //设置弹出菜单的内容
	    pop.document.body.innerHTML=menuDiv.innerHTML;
	    var rowObjs=pop.document.body.all[0].rows;
	    //获得弹出菜单的行数
	    var rowCount=rowObjs.length;
	    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
	    //循环设置每行的属性
	    for(var i=0;i<rowObjs.length;i++)
	    {
	        //如果设置该行不显示，则行数减一
	        var hide=rowControlString.charAt(i)!='1';
	        if(hide){
	            rowCount--;
	        }
	        //设置是否显示该行
	        rowObjs[i].style.display=(hide)?"none":"";
	        //设置鼠标滑入该行时的效果
	        rowObjs[i].cells[0].onmouseover=function()
	        {
	            this.style.background="#397DBD";
	            this.style.color="white";
	        }
	        //设置鼠标滑出该行时的效果
	        rowObjs[i].cells[0].onmouseout=function(){
	            this.style.background="#F1F1F1";
	            this.style.color="black";
	        }
	    }
	    //屏蔽菜单的菜单
	    pop.document.oncontextmenu=function()
	    {
	            return false; 
	    }
	    //选择右键菜单的一项后，菜单隐藏
	    pop.document.onclick=function()
	    {
	        pop.hide();
	    }
	    //显示菜单
	    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
	    return true;
	}
	
	function edit()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=ready_edit&id="+node;
	}
	
	function reboot()
	{
		location.href="<%=rootPath%>/serverUpAndDown.do?action=reboot&value=1&id="+node;
	}
	function clickMenu()
	{
	}
	//关闭
	function shutdown()
	{
	   
		location.href="<%=rootPath%>/serverUpAndDown.do?action=shutdown&value=1&id="+node;
	}
	//关闭集群
	function shutdownAll(){
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=shutdownAll&clusterId=<%=clusterId%>&nowtime="+(new Date()),
			success:function(obj){
				alert("指令已发出，请稍候...");
			}
		});
	// mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=shutdownAll";
    // mainForm.submit();
	}
	
	//重启集群
	function rebootAll(){
	$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=rebootAll&clusterId=<%=clusterId%>&nowtime="+(new Date()),
			success:function(obj){
				alert("指令已发出，请稍候...");
			}
		});
	// mainForm.action = "<%=rootPath%>/serverUpAndDown.do?action=rebootAll";
    // mainForm.submit();
	}
	function updateSequence(name,obj){
	
	var value=obj.options[obj.selectedIndex].text;
	
	}
	function save(){
	var ids=document.getElementById("ids").value;
	var arrayObj = new Array();
	var temp="";
	arrayObj=ids.split(".");
	for( i=0;i<arrayObj.length-1;i++){
	var obj=document.getElementById("sequence"+arrayObj[i]);
	temp+=obj.options[obj.selectedIndex].text+".";
   
	}
	 
$.ajax({
			type:"GET",
			dataType:"json",
			url:"<%=rootPath%>/serverAjaxManager.ajax?action=updateSequence&ids="+ids+"&values="+temp+"&nowtime="+(new Date()),
			success:function(obj){
				alert(obj.data);
			}
		});

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
<BODY leftmargin="0" topmargin="0" bgcolor="#cedefa" onload="initmenu();">

<!-- 这里用来定义需要显示的右键菜单 -->
	<div id="itemMenu" style="display: none";>
	<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
		style="border: thin;font-size: 12px" cellspacing="0">
		<!-- <tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.toMovement()">动态效果</td>
		</tr>-->
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">修改信息</td>
		</tr>
		<!--<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.reboot()">重新启动</td>
		</tr>-->		
	<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.shutdown()">关闭服务器</td>
		</tr>
	</table>
	</div>
	<!-- 右键菜单结束-->
<form method="post" name="mainForm">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		
		</td>
            <td align="center" valign=top>
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="layout_title">自动化 >> 自动化控制 >> 远程集群开关机管理</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
			  </td>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
        			            <tr>
        			            
                                <td bgcolor="#ECECEC" width="50%" align='left' >
                                  &nbsp;&nbsp;&nbsp;<input name="button"  onclick="upLine('flextb')" type="button"  value="上 移" />
                                  <input name="button"  onclick="downLine('flextb')" type="button"  value="下 移" />
                                  &nbsp;&nbsp;&nbsp;<input name="refresh1" id="refresh1" onclick="refreshCluster()" type="button"  value="刷 新" />
                                  <input name="process" id="process" onclick="save()" type="button"  value="保 存" />
                                 <!--  <input name="rebootAll1" id="rebootAll1" onclick="rebootAll()" type="button"  value="重启集群" />
                                  <input name="shutdownAll1" id="shutdownAll1" onclick="shutdownAll()" type="button"  value="关闭集群" />
                                 -->
                                </td>
                              
					            <td bgcolor="#ECECEC" width="50%" align='right'>
									<a href="#" onclick="toAdd()">添加</a>
									<%if(clusterId!=1){ %>
									<a href="#" onclick="toDelete()">移出集群</a>
		  						     <%} %>
		  						     &nbsp;&nbsp;&nbsp;
		  						</td>
									</tr>
        								</table>
										</td>
        						</tr>						
				
		<tr >
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" id="flextb">
					<tr class="microsoftLook0" height=28>
					<th width='5%'style="display:none;"></th>
					<th width='5%' align=left>&nbsp;<INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></th>
    					<th width='8%'>关机次序</th>
      					<th width='18%'>名称</th>
      					<th width='15%'>IP地址</th>	
      					<th width='12%'>服务器类型</th> 
      					<th width='17%'>远程机器状态</th>
      					<th width='17%'>最后一次操作时间</th>    
    					<th width='7%'>操作</th>
</tr>
<%   StringBuffer items=new StringBuffer();
	if(machineList.size() > 0){
    		for(int i=0;i<machineList.size();i++){
    		        int j=0;
       				UpAndDownMachine vo = machineList.get(i);
       				int status = 0;
       				Node node = (Node)PollingEngine.getInstance().getFtpByID(vo.getId());
       				String alarmmessage = "";
       				if(node != null){
       					status = node.getStatus();
       					List alarmlist = node.getAlarmMessage();
       					if(alarmlist!= null && alarmlist.size()>0){
						for(int k=0;k<alarmlist.size();k++){
							alarmmessage = alarmmessage+alarmlist.get(k).toString();
						}
					}
       				}  
       
%>
       <tr bgcolor="#FFFFFF" class="microsoftLook" id="line<%=1 + i%>" onclick="lineclick('flextb',this);">
            <td style="display:none;"><%=1 + i%></td>
    		<td height=25 align=center>&nbsp;<INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    		<td height=25 align=center>&nbsp;
    		<select   name="sequence<%=vo.getId()%>" id="sequence<%=vo.getId()%>"  class="formStyle" onChange="updateSequence('<%=vo.getId()%>',this)">
    		<%
    		items.append(vo.getId()+".");
    		for( j=0;j<machineList.size();j++) {
    		if(i==j)
    		{
    		%>
    		<option value='<%=i+1%>'selected><%=i+1 %></option>
    		<%
    		}else{
    		%>
    	
				<option value='<%=j+1%>'><%=j+1 %></option>
				
			<%}}%>	
			</select>
            </td>
		<td height=25 align=center>&nbsp;<%=vo.getName()%></td> 
		<td height=25 align=center>&nbsp;<%=vo.getIpaddress()%></td> 
		<td height=25 align=center>&nbsp;<%=vo.getServerType()%></td> 
		<%
		int monitorStatus = vo.getMonitorStatus();
		if(monitorStatus == 0)
		{
		 %>
		<td height=25 align=center>&nbsp;服务器未检测到远端客户机</td>
		<%
		}
		else
		{
		 %> 
		 <td height=25 align=center>&nbsp;监控中</td>
		 <%
		 }
		  %>
		<td height=25 align=center><%=vo.getLasttime() %></td>	
		<td height=25 align=center>&nbsp;
		<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getName()%>') alt="右键操作">
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
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" /></td>
                    <td><input id="ids"   type="hidden"  value="<%=items.toString() %>" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" /></td>
                  </tr>
              </table></td>
            </tr>
	</table>
</td>
			</tr>
		</table>
		</form>
</BODY>
</HTML>
