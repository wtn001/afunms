<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="java.util.*"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.application.dao.*"%>
<%@page import="com.afunms.application.util.IpTranslation"%>
<%
  String rootPath = request.getContextPath();
  IpTranslation transfer=new IpTranslation();
  List list = (List)request.getAttribute("list");
  if(list == null)list = new ArrayList();
    int rc = list.size();
    JspPage jp = (JspPage)request.getAttribute("page");
    String ipaddress = (String)request.getAttribute("ipaddress");
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />

<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="JavaScript" type="text/javascript">
   var curpage= <%=jp.getCurrentPage()%>;
  var totalpages = <%=jp.getPageTotal()%>;
  var delAction = "<%=rootPath%>/oraspace.do?action=delete";
  var listAction = "<%=rootPath%>/oraspace.do?action=list";

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/db.do?action=ready_add";
     mainForm.submit();
  }
  function createSpaceConfig()
  {
     mainForm.action = "<%=rootPath%>/oraspace.do?action=createspaceconfig";
     mainForm.submit();
  }
  function search()
  {
     mainForm.action = "<%=rootPath%>/oraspace.do?action=search";
     mainForm.submit();
  }  
</script>
<script language="JavaScript">

	//公共变量
	var node="";
	var ipaddress="";
	var dbid="";
	var operate="";
	var sid="";
	/**
	*根据传入的id显示右键菜单
	*/
	function showMenu(id,nodeid,ip,db)
	{	
		ipaddress=ip;
		node=nodeid;
		dbid=db;
		//operate=oper;
		if(ip.indexOf(":")!=-1){
		  sid=ip.split(":")[1];
		}
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
	function detail()
	{
	    location.href="<%=rootPath%>/db.do?action=check&id="+dbid+"&sid="+sid;
	}
	function edit()
	{
		location.href="<%=rootPath%>/oraspace.do?action=ready_edit&id="+node+"&sid="+sid;
	}	
	function clickMenu()
	{
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
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.edit()">修改信息</td>
		</tr>
		<tr>
			<td style="cursor: default; border: outset 1;" align="center"
				onclick="parent.detail();">监视信息</td>
		</tr>		
	</table>
	</div>
	<!-- 右键菜单结束-->
<form method="post" name="mainForm">
<table id="body-container" class="body-container">
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>             		           				
		
		
		</td>
		<td align="center" valign=top>
			<table style="width:98%"  cellpadding="0" cellspacing="0" algin="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table width="100%" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
                    <td class="add-content-title">应用 >> 数据库管理 >> ORACLE告警设置</td>
                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
                  </tr>
              </table>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="1" >
								<tr>
								<td>
								<table width="100%" cellpadding="0" cellspacing="0" >
								<tr>
								<td bgcolor="#ECECEC" width="50%" align='left'>
				&nbsp;&nbsp;&nbsp;&nbsp;查询IP地址：
		    	<select name="ipaddress" >
		    	<%
		    		List iplist = (List)request.getAttribute("iplist");
		    		if (iplist != null && iplist.size()>0){
		    			for(int i=0;i<iplist.size();i++){
		    				String ip = (String)iplist.get(i);
		    				if (ip.equals(ipaddress)){
		    	%>
		    				<option value='<%=ip%>' selected><%=ip%></option>		    	
			<%		    				
						}else{
			%>
						<option value='<%=ip%>'><%=ip%></option>
			<%						
						}
		    			}
		    		}
		    	%>
		    	</select>   
		    	&nbsp;&nbsp;<input type="button" value="搜索" onclick="search()" class="button">     							
          							</td>            
								<td bgcolor="#ECECEC" width="50%" align='right'>
									
									<a href="#" onclick="createSpaceConfig()">刷新表空间阀值</a>&nbsp;&nbsp;&nbsp;
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
							<table width="100%" cellpadding="0" cellspacing="1" >
							<tr>
						    <td bgcolor="#ECECEC" width="80%" align="center">
    <jsp:include page="../../common/page.jsp">
     <jsp:param name="curpage" value="<%=jp.getCurrentPage()%>"/>
     <jsp:param name="pagetotal" value="<%=jp.getPageTotal()%>"/>
   </jsp:include>
        </td>
        </tr>
		</table>
		</td>
		</tr>         										
				
		<tr >
			<td colspan="2">
				<table cellspacing="1" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
    					<th width='5%'>序号</th>
    					<th width='20%'>IP地址</th>
    					<th width='20%'>表空间名称</th>
    					<th width='20%'>告警值</th>
    					<th width='10%'>是否告警</th>
    					<th width='10%'>备注</th>
    					<th width='15%'>操作</th>
</tr>
<%
       DBDao dbdao = new DBDao();
       DBTypeDao typedao = new DBTypeDao();
       DBTypeVo typevo = typedao.findByDbtype("oracle");
	
if(rc > 0){
    for(int i=0;i<rc;i++)
    {  
    //OraclePartsDao oracleDao=new OraclePartsDao();
    try{
       Oraspaceconfig vo = (Oraspaceconfig)list.get(i);
       String flag = "告警";
       if(vo.getSms() == 0)flag = "未告警";
       dbdao = new DBDao();
      
       String ipAddr=vo.getIpaddress();
       String []ipArr=ipAddr.split(":");
       String []ips=transfer.getIpFromHex(ipArr[0]);
	   List shareList = dbdao.getDbByTypeAndIpaddress(typevo.getId(), ips[0]+"."+ips[1]+"."+ips[2]+"."+ips[3]);
	  // oracleDao
	   if(shareList == null || shareList.size()==0)continue;
	   DBVo dbvo = (DBVo)shareList.get(0);
	   //OracleEntity ora=(OracleEntity)oracleDao.getOracleById(Integer.parseInt(ipArr[1]));
       
%>
       <tr bgcolor="#FFFFFF" <%=onmouseoverstyle%> class="microsoftLook">
    		<td height=25>&nbsp;<font color='blue'><%=1 + i%></font></td>
		<td height=25>&nbsp;<%=ips[0]+"."+ips[1]+"."+ips[2]+"."+ips[3]+":"+dbvo.getId()%></td> 
		<td height=25>&nbsp;<%=vo.getSpacename()%></td> 
		<td height=25>&nbsp;<%=vo.getAlarmvalue()%></td> 
		<td height=25>&nbsp;
			<%
				if(vo.getSms()==0){
			%>
			<a href="<%=rootPath%>/oraspace.do?action=addalert&id=<%=vo.getId()%>"><font color=#397DBD>告警</font></a>
			<%
				}else{
			%>
			<a href="<%=rootPath%>/oraspace.do?action=cancelalert&id=<%=vo.getId()%>"><font color=#397DBD>取消告警</font></a>
			<%	}
			%>
		
		</td> 
		<td height=25>&nbsp;<%=vo.getBak()%></td>				
		<td height=25>&nbsp;
		<img src="<%=rootPath%>/resource/image/status.gif" border="0" width=15 oncontextmenu=showMenu('2','<%=vo.getId()%>','<%=vo.getIpaddress()%>','<%=dbvo.getId()%>')>
		</td>
  	</tr>
<%	}catch(Exception e){
 // e.printStatckTrace();
 System.out.println(e);
}finally{
  //oracleDao.close();
  dbdao.close();
}}
}


%>				
			</table>
			</td>

		</tr>
		<tr>
              <td background="<%=rootPath%>/common/images/right_b_02.jpg" ><table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="11" /></td>
                    <td></td>
                    <td align="right" valign="bottom"><img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="11" /></td>
                  </tr>
              </table></td>
            </tr>	
	</table>
</td>
			</tr>
		</table>
</BODY>
</HTML>
