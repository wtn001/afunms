<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>

<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@ page import="com.afunms.event.model.EventList"%>
<%@page import="com.afunms.topology.dao.*"%>
<%@page import="com.afunms.topology.model.*"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.vmware.vim25.*"%>
<%@page import="com.afunms.common.util.*"%>
<%@page import="com.afunms.topology.manage.VMWareManager"%>
<%
  String rootPath = request.getContextPath();
  //System.out.println(rootPath);
  List list = (List)request.getAttribute("list");
  String menuTable = (String)request.getAttribute("menuTable");
  String ipaddress = request.getParameter("ipaddress"); 
  
String startdate = (String)request.getAttribute("startdate");
String todate = (String)request.getAttribute("todate");
String vid = request.getParameter("vid");
String ip = request.getParameter("ip");
String name1 = request.getParameter("name");
String name= new String(name1.getBytes("ISO-8859-1"),"GBK");

Hashtable vmData = new Hashtable();
vmData = (Hashtable) ShareData.getVmdata().get(ip);
int size = 0;
PerfEntityMetricBase[] perfEntityMetricBases = (PerfEntityMetricBase[])((HashMap<String, Object>)((HashMap<String, Object>)vmData.get("vmware")).get("vm")).get(vid);
List l = new ArrayList();
DecimalFormat df = new DecimalFormat("0.0");
if(perfEntityMetricBases != null && perfEntityMetricBases.length>0){
for(int j=0;j<9;j++)
        		{
        		 try{
        		  String kpi =((PerfMetricSeriesCSV) ((PerfEntityMetricCSV) perfEntityMetricBases[0]).getValue()[j]).getId().getCounterId()+"";
        		  String[] str = ((PerfMetricSeriesCSV) ((PerfEntityMetricCSV) perfEntityMetricBases[0]).getValue()[j]).getValue().toString().split(",");
        		  //System.out.println("----str----"+str.length);
        		  if(str != null && str.length>0){
        		     if(str.length < 288){
        				  size = str.length-1;
        			  }else{
        				  size = 287;
        			  }
        			  l.add(str[size]);
        			}
        		}catch(NullPointerException e){
        		    l.add("0");
        		}
        	    }
}

VMWareManager manager = new VMWareManager();
HashMap avg = manager.getAvg(ip.replace(".","_"),vid);
HashMap max = manager.getMax(ip.replace(".","_"),vid);
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<script language="JavaScript" src="<%=rootPath%>/include/date.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 
<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="utf-8" />
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8"/>
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="utf-8"></script>



<script language="JavaScript" type="text/JavaScript">

function query(){
	//subforms = document.forms[0];
	mainForm.action="<%=rootPath%>/netreport.do?action=storageButter";
	subforms.submit();
}

function changeOrder(para){
	mainForm.orderflag.value = para;
	mainForm.action="<%=rootPath%>/netreport.do?action=storageButter"
  	mainForm.submit();
}
  Ext.onReady(function()
{  

setTimeout(function(){
	        Ext.get('loading').remove();
	        Ext.get('loading-mask').fadeOut({remove:true});
	    }, 250);
	
 Ext.get("process").on("click",function(){
     
     //if(chk1&&chk2&&chk3)
     //{
     
        Ext.MessageBox.wait('数据加载中，请稍后.. '); 
        //msg.style.display="block";
	mainForm.action="<%=rootPath%>/netreport.do?action=storageButter";
	mainForm.submit();        
        //mainForm.action = "<%=rootPath%>/network.do?action=add";
        //mainForm.submit();
     //}  
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
	
	createChart();
}

/************************************************************************
画曲线图
************************************************************************/
function createChart(){
	//画连通率曲线
	var pingSo = new SWFObject("<%=rootPath%>/amchart/amline.swf", "ampie","100%", "338", "8", "#FFFFFF");
    pingSo.addVariable("path", "<%=rootPath%>/amchart/");
    pingSo.addVariable("settings_file", escape("<%=rootPath%>/amcharts_settings/hdsButter_settings.xml"));
  	pingSo.addVariable("chart_data","<%=request.getAttribute("pingChartDivStr")%>");  
 	pingSo.write("pingChartDiv");
}

function setClass(){
	document.getElementById('netReportTitle-0').className='detail-data-title';
	document.getElementById('netReportTitle-0').onmouseover="this.className='detail-data-title'";
	document.getElementById('netReportTitle-0').onmouseout="this.className='detail-data-title'";
}

function refer(action){
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}

function showcpu(ip){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarecpu&ip="+ip+"&vid=<%=vid%>&cpunow=<%=df.format(Long.parseLong((String)l.get(0))/100) %>",'mywindow','width=,height=,resizable=1');
       }
function showcpuuse(ip){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarecpuuse&ip="+ip+"&vid=<%=vid%>",'mywindow','width=,height=,resizable=1');
       }
function showmem(ip){  
               window.open("<%=rootPath%>/netreport.do?action=vmwaremem&ip="+ip+"&vid=<%=vid%>",'mywindow','width=,height=,resizable=1');
       }
function showmemin(ip){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarememin&ip="+ip+"&vid=<%=vid%>",'mywindow','width=,height=,resizable=1');
       }
function showmemout(ip){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarememout&ip="+ip+"&vid=<%=vid%>",'mywindow','width=,height=,resizable=1');
       }
function showdisk(ip){  
               window.open("<%=rootPath%>/netreport.do?action=vmwaredisk&ip="+ip+"&vid=<%=vid%>",'mywindow','width=,height=,resizable=1');
       }
function shownet(ip){  
               window.open("<%=rootPath%>/netreport.do?action=vmwarenet&ip="+ip+"&vid=<%=vid%>",'mywindow','width=,height=,resizable=1');
       }
</script>


</head>
<body id="body" class="body" onload="createChart();">
	<IFRAME frameBorder=0 id=CalFrame marginHeight=0 marginWidth=0 noResize scrolling=no src="<%=rootPath%>/include/calendar.htm" style="DISPLAY: none; HEIGHT: 189px; POSITION: absolute; WIDTH: 148px; Z-INDEX: 100"></IFRAME>
	<form id="mainForm" method="post" name="mainForm">
	<input type=hidden name="orderflag">
		<div id="loading">
			<div class="loading-indicator">
				<img src="<%=rootPath%>/js/ext/lib/resources/extanim64.gif" width="32" height="32" style="margin-right: 8px;" align="middle" />Loading...</div>
			</div>
		<div id="loading-mask" style=""></div>		
		<table id="body-container" class="body-container">
			<tr>
				<td class="td-container-main">
					<table id="container-main" class="container-main">
						<tr>
							<td class="td-container-main-report">
								<table id="container-main-report" class="container-main-report">
									<tr>
										<td>
											<table id="report-content" class="report-content">
				        						<tr>
				        							<td>
				        								<table id="report-content-body" class="report-content-body">
															<tr>
				        										<td>
				        											
				        											<table id="report-data-body" class="report-data-body">
				        												
				        												<tr> 
                					<td>
                    							<tr> 
                      								<td>
  											<table   cellSpacing="1"  cellPadding="0"  bgcolor='#FFFFFF' width=100%>
												<tr>
													<td >
														<table border=0>
															<tr>
																<td>
																	<div style='background:url(/afunms/img/virtualperfbg.png);background-repeat: no-repeat;background-position: center center;width:;height=440;'>
																	   <table border =0 >
																	      <tr height=30  style="repeat-x;">
																	        <td colspan=3>
																	         <table background="<%=rootPath%>/common/images/right_t_02.jpg" border=0>
																	          <tr height=30>
																	        <td align="left"></td>
					                                                        <td class="layout_title" align=center><b><%=name %></b></td>
					                                                        <td align="right"></td>
					                                                        </tr>
					                                                          </table>
					                                                        </td>
																	      </tr>
																	      <tr height=15>
																	         <td colspan=3></td>
																	      </tr>
																	      <tr height=420>
																	        <td width=41%></td>
																	        <td width=39% valign=top>
																	            <table height='350'   border=0>
																	               <tr  valign=top>
																	                 <td>
																	                   <table border=0 height='100' width=100%>
																	                      <tr height=20 >
																	                            <td width=35%></td>
																	                            <td align=left width=15%>当前</td>
																	                            <td align=left width=15%>平均</td>
																	                            <td align=left width=15%>最大</td>
																	                            <td align=left width=15%></td>
																	                     </tr>
																	                     <tr  height=20>
																	                        <td>利用率：</td>
																	                        <td align=left><%=df.format(Long.parseLong((String)l.get(0))/100)+"%" %></td>
																	                        <td align=left><%=avg.get("cpu")+"%" %></td>
																	                        <td align=left><%=max.get("cpu")+"%" %></td>
																	                        <td align=left><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showcpu("<%=ipaddress %>")' width=15 ></td>
																	                     </tr>
																	                      <tr height=25 valign=bottom>
																	                        <td>使用(MHz)：</td>
																	                        <td align=left><%=l.get(5) %></td>
																	                        <td align=left><%=avg.get("cpuuse") %></td>
																	                        <td align=left><%=max.get("cpuuse") %></td>
																	                         <td align=left><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showcpuuse("<%=ipaddress %>")' width=15 ></td>
																	                      </tr>
																	                      <tr height=35></tr>
																	                    </table>
																	                  </td>
																	               </tr>
																	               
																	                <tr  valign=top>
																	                 <td>
																	                   <table border=0 height='90' width=100%>
																	                      <tr height=5></tr>
																	                      <tr height=15 valign=bottom>
																	                            <td width=35%></td>
																	                            <td align=left width=15%>当前</td>
																	                            <td align=left width=15%>平均</td>
																	                            <td align=left width=15%>最大</td>
																	                            <td align=left width=15%></td>
																	                     </tr>
																	                     <tr  height=20>
																	                        <td>换入(MBPs)：</td>
																	                        <td align=left><%=l.get(3)%></td>
																	                        <td align=left><%=avg.get("memin") %></td>
																	                        <td align=left><%=max.get("memin") %></td>
																	                        <td align=left><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showmemin("<%=ipaddress %>")' width=15 ></td>
																	                        
																	                     </tr>
																	                      <tr height=20>
																	                        <td>换出(MBPs)：</td>
																	                        <td align=left><%=l.get(4) %></td>
																	                        <td align=left><%=avg.get("memout") %></td>
																	                        <td align=left><%=max.get("memout") %></td>
																	                         <td align=left><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showmemout("<%=ipaddress %>")' width=15 ></td>
																	                      </tr>
																	                       <tr  height=20>
																	                        <td>使用：</td>
																	                        <td align=left><%=df.format(Long.parseLong((String)l.get(6))/100)+"%" %></td>
																	                        <td align=left><%=avg.get("mem")+"%" %></td>
																	                        <td align=left><%=max.get("mem")+"%" %></td>
																	                        <td align=left><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showmem("<%=ipaddress %>")' width=15 ></td>
																	                     </tr>
																	                      <tr height=5></tr>
																	                    </table>
																	                  </td>
																	               </tr>
																	                <tr  valign=top>
																	                 <td>
																	                   <table border=0 height='90' width=100%>
																	                      <tr height=35 valign=bottom>
																	                            <td width=35% ></td>
																	                            <td align=left width=15%>当前</td>
																	                            <td align=left width=15%>平均</td>
																	                            <td align=left width=15%>最大</td>
																	                             <td align=left width=15%></td>
																	                     </tr>
																	                     <tr  height=30>
																	                        <td>使用(KBPs)：</td>
																	                        <td align=left><%=l.get(7) %></td>
																	                        <td align=left><%=avg.get("disk") %></td>
																	                        <td align=left><%=max.get("disk") %></td>
																	                        <td align=left><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showdisk("<%=ipaddress %>")' width=15 ></td>
																	                     </tr>
																	                      <tr height=25></tr>
																	                    </table>
																	                  </td>
																	               </tr>
																	               <tr  valign=top>
																	                 <td>
																	                   <table border=0 height='90' width=100%>
																	                      <tr height=40 valign=bottom>
																	                            <td width=35%></td>
																	                           <td align=left width=15%>当前</td>
																	                            <td align=left width=15%>平均</td>
																	                            <td align=left width=15%>最大</td>
																	                             <td align=left width=15%></td>
																	                     </tr>
																	                     <tr  height=30>
																	                        <td>使用(KBPs)：</td>
																	                        <td align=left><%=l.get(8) %></td>
																	                        <td align=left><%=avg.get("net") %></td>
																	                        <td align=left><%=max.get("net") %></td>
																	                        <td align=left><img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='shownet("<%=ipaddress %>")' width=15 ></td>
																	                     </tr>
																	                      <tr height=20></tr>
																	                    </table>
																	                  </td>
																	               </tr>
																	              <!--    
																	                  <td valign=center>cpu利用率：<%=df.format(Long.parseLong((String)l.get(0))/100)+"%" %>&nbsp;&nbsp;&nbsp;&nbsp;<img src="<%=rootPath%>/resource/image/a_xn.gif" onclick='showcpu("<%=ipaddress %>")' width=15 ></td>
																	               </tr>
																	               <tr height=20>
																	                  <td valign=top>cpu使用(MHz):<%=l.get(5) %></td>
																	               </tr>
																	               <tr height=5>
																	                 <td></td>
																	               </tr>
																	               <tr height=20>
																	                  <td >内存(MBPs)换入/换出:<%=l.get(3)+"/"+l.get(4) %></td>
																	               </tr>
																	               <tr height=20>
																	                  <td valign=top>内存使用:<%=df.format(Long.parseLong((String)l.get(6))/100)+"%" %></td ></td>
																	               </tr>
																	               <tr height=5>
																	                  <td></td>
																	               </tr>
																	               <tr height=20>
																	                  <td>磁盘(KBPs)使用:<%=l.get(7) %></td>
																	               </tr>
																	               <tr height=20>
																	                  <td></td>
																	               </tr>
																	               <tr height=20>
																	                  <td>网络(KBPs)使用:<%=l.get(8) %></td>  --> 
																	            </table>
																	        </td>
																	        <td width=20%></td>
																	      </tr>
																	   </table>
																	</div>
																</td>
															</tr>
														</table>
													</td>
												</tr>
										   </table>
																</td>
															</tr>
															
				        							<td></td>
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