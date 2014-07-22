<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.alarm.model.AlarmIndicators"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.alarm.model.AlarmIndicatorsNode"%>
<%@page import="com.afunms.indicators.model.GatherIndicators"%>
<%@page import="com.afunms.indicators.model.NodeGatherIndicators"%>
<%@ include file="/include/globe.inc"%>

<%
  String rootPath = request.getContextPath();
  String menuTable = (String)request.getAttribute("menuTable");
  List gatherIndicatorsList = (List)request.getAttribute("gatherIndicatorsList");
  
  List nodeGatherIndicatorsList = (List)request.getAttribute("nodeGatherIndicatorsList");
  Hashtable moidhash = (Hashtable)request.getAttribute("moidhash");
  
  String nodeid = (String)request.getAttribute("nodeid");
  String type = (String)request.getAttribute("type");
  String subtype = (String)request.getAttribute("subtype");
  
  String jspFlag = (String)request.getAttribute("jspFlag");
  
%>


<html>
	<head>
	
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>
		<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>
		<script type="text/javascript">
		 	
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
			
			
  			
	  function save()
	  {
	      mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=showChooseNodeList";
	      mainForm.submit();
	  }
	  			
		Ext.onReady(function(){  
	
			setTimeout(function(){
		        Ext.get('loading').remove();
		        Ext.get('loading-mask').fadeOut({remove:true});
		    }, 250);
		
		 	Ext.get("process").on("click",function(){
		        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
		        //msg.style.display="block";
		        mainForm.action = "<%=rootPath%>/alarmIndicatorsNode.do?action=addselected";
		        mainForm.submit();
		 	});	
		
		});
			
		</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<!-- 这里用来定义需要显示的右键菜单 -->
		<div id="itemMenu" style="display: none";>
		<table border="1" width="100%" height="100%" bgcolor="#F1F1F1"
			style="border: thin;font-size: 12px" cellspacing="0">
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.edit()">编辑</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.cancelmanage()">取消监视</td>
			</tr>
			<tr>
				<td style="cursor: default; border: outset 1;" align="center"
					onclick="parent.addmanage()">添加监视</td>
			</tr>
		</table>
		</div>
		<!-- 右键菜单结束-->
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="nodeid" id="nodeid" value="<%=nodeid%>">
			<input type="hidden" name="type" id="type" value="<%=type%>">
			<input type="hidden" name="subtype" id="subtype" value="<%=subtype%>">
			<input type="hidden" name="jspFlag" id="jspFlag" value="<%=jspFlag%>">
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
									                	<td class="content-title"> 资源 >> 性能监视 >> 性能指标 >> 性能指标添加 </td>
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
																<td class="body-data-title" width=5% align=right><INPUT type="checkbox" name="checkall" onclick="javascript:chkall()"></td>
													    			<td class="body-data-title" width=10%>名称</td>
													    			<td class="body-data-title" width=10%>描述</td>
							        							</tr>
							        							<%
							        								if("multi".equals(jspFlag)){
							        									if(nodeGatherIndicatorsList != null && nodeGatherIndicatorsList.size() > 0) {
							        									for(int i = 0; i < nodeGatherIndicatorsList.size(); i++){
							        										AlarmIndicatorsNode nodeGatherIndicators = (AlarmIndicatorsNode)nodeGatherIndicatorsList.get(i);
							        										
							        										String dis = "";
							        										
							        										
							        										%>
							        										 
							        										<tr>
							        											<td class="body-data-list" width=30% align=right><input type=checkbox name="checkbox" value="<%=nodeGatherIndicators.getId()%>" <%=dis%>>&nbsp;</td>
																    			<td class="body-data-list" width=40% align=left>&nbsp;<%=nodeGatherIndicators.getName()%></td>
																    			<td class="body-data-list" width=30% align=left>&nbsp;<%=nodeGatherIndicators.getDescr()%></td>
										        							</tr>
							        										<%
							        										}
							        								
							        									}
							        								}else{
							        									if(gatherIndicatorsList != null && gatherIndicatorsList.size() > 0) {
							        									for(int i = 0; i < gatherIndicatorsList.size(); i++){
							        										AlarmIndicators alarmIndicatorsNode = (AlarmIndicators)gatherIndicatorsList.get(i);
							        										
							        										String dis = "";
							        										String isMonitor = "否";
							        										String bcolor="gray";
							        										if("1".equals(alarmIndicatorsNode.getEnabled())){
							        											isMonitor = "是";
							        											bcolor ="#3399CC";
							        										}
							        										
							        										//System.out.println(alarmIndicatorsNode.getDatatype()+ "====================");
							        										
							        										String datatype = "字符串";
							        										if("Number".equals(alarmIndicatorsNode.getDatatype())){
							        											datatype = "数字";
							        										}
							        										
							        										String sms0 = "否";
							        										if("1".equals(alarmIndicatorsNode.getSms0())){
							        											sms0 = "是";
							        										}
							        										
							        										String sms1 = "否";
							        										if("1".equals(alarmIndicatorsNode.getSms1())){
							        											sms1 = "是";
							        										}
							        										
							        										String sms2 = "否";
							        										if("1".equals(alarmIndicatorsNode.getSms2())){
							        											sms2 = "是";
							        										}
							        										String compare = "升序";
							        										String bgcol = "#ffffff";
							        										if(alarmIndicatorsNode.getCompare() == 0){
							        											compare = "降序";
							        											bgcol = "gray";
							        										}
							        										%>
							        										 
							        										<tr>
							        											<td class="body-data-list" width=5% align=right><input type=checkbox name="checkbox" value="<%=alarmIndicatorsNode.getId()%>" <%=dis%>>&nbsp;</td>
																    			<td class="body-data-list" width=10% align=left><%=alarmIndicatorsNode.getName()%></td>
																    			<td class="body-data-list" width=10% align=left>&nbsp;<%=alarmIndicatorsNode.getDescr()%></td>
																    			<td class="body-data-list"><%=alarmIndicatorsNode.getType()%></td>
																    			<td class="body-data-list"><%=alarmIndicatorsNode.getSubtype()%></td>
																    			<td class="body-data-list"><%=datatype%></td>
																    			<td class="body-data-list"><%=alarmIndicatorsNode.getThreshlod_unit()%></td>
																    			<td class="body-data-list" bgcolor="<%=bcolor%>"><%=isMonitor%></td>
																    			<td class="body-data-list" bgcolor="<%=bgcol%>"><%=compare%></td>
																    			<td class="body-data-list" bgcolor="yellow"><%=alarmIndicatorsNode.getLimenvalue0()%></td>
																    			<td class="body-data-list"><%=alarmIndicatorsNode.getTime0()%></td>
																    			<td class="body-data-list"><%=sms0%></td>
																    			<td class="body-data-list" bgcolor="orange"><%=alarmIndicatorsNode.getLimenvalue1()%></td>
																    			<td class="body-data-list"><%=alarmIndicatorsNode.getTime1()%></td>
																    			<td class="body-data-list"><%=sms1%></td>
																    			<td class="body-data-list" bgcolor="red"><%=alarmIndicatorsNode.getLimenvalue2()%></td>
																    			<td class="body-data-list"><%=alarmIndicatorsNode.getTime2()%></td>
																    			<td class="body-data-list"><%=sms2%></td>
										        							</tr>
							        										<%
							        										}
							        								
							        									}
							        								}%>
							        								<tr>
																			<TD nowrap colspan="20" align=center><br>
																			<input type="button" value="选择设备" style="width:100" id="add" onclick="save()">&nbsp;&nbsp;
																			<input type="reset" style="width:50" value="返 回" onclick="javascript:history.back(1)">&nbsp;&nbsp;
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
	</body>
</html>
