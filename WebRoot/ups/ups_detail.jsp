<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="com.afunms.common.util.SysUtil"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@ include file="/include/globe.inc"%>
<%@ include file="/include/globeChinese.inc"%>
<%@page import="com.afunms.common.base.JspPage"%>
<%@page import="com.afunms.common.util.ShareData"%> 
<%@page import="com.afunms.polling.node.UPSNode"%>   
<%@page import="com.afunms.polling.om.Systemcollectdata"%>   
<%@page import="java.util.*"%>      
<%@page import="java.text.*"%>     
<%@page import="com.afunms.inform.util.SystemSnap"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
  
<%
	 String runmodel = PollingEngine.getCollectwebflag();//采集与访问模式
	 String rootPath = request.getContextPath();
	 String menuTable = (String)request.getAttribute("menuTable");
     HostNode vo  = (HostNode)request.getAttribute("vo");
     String ip = SysUtil.doip(vo.getIpAddress());
     //UPSNode upsnode = (UPSNode)PollingEngine.getInstance().getUpsByID(vo.getId());
     Hashtable ipAllData = null;
     if("0".equals(runmodel)){////采集与访问是集成模式
    	ipAllData = (Hashtable)ShareData.getSharedata().get(vo.getIpAddress());
     }else{//采集与访问分离模式
     	//MgeUpsDao mgeUpsDao = new MgeUpsDao();
     	//try{
     	//	ipAllData = mgeUpsDao.getUpsIpData(vo.getIpAddress());
     	//}catch(Exception e){
     	//	e.printStackTrace();	
     	//} finally{
     	//	mgeUpsDao.close();	
     	//}
     }
     //Vector batteryVector = null;
     //if(ipAllData != null && ipAllData.containsKey("battery")){
     //	batteryVector = (Vector)ipAllData.get("battery");
     //}
    // String DCDY = "";
     //String DCDL = "";
    // String DCSYHBSJ = "";
    // String DCWD = "";
    // String HJWD = "";
    // String sysName = "";
     //String SBMS = "";
     //if(batteryVector!=null){
     //    for(int i=0;i<batteryVector.size();i++){
     //        Systemcollectdata systemdata = (Systemcollectdata)batteryVector.get(i);
     //        if("DCDY".equalsIgnoreCase(systemdata.getEntity())){
     //            DCDY = systemdata.getThevalue()+systemdata.getUnit();
      //       }
      //       if("DCDL".equalsIgnoreCase(systemdata.getEntity())){
       //          DCDL = systemdata.getThevalue()+systemdata.getUnit();
      //       }
      //       if("DCSYHBSJ".equalsIgnoreCase(systemdata.getEntity())){
      //           DCSYHBSJ = systemdata.getThevalue()+systemdata.getUnit();
      //       }
      //       if("DCWD".equalsIgnoreCase(systemdata.getEntity())){
        //         DCWD = systemdata.getThevalue()+systemdata.getUnit();
          //   }
            // if("HJWD".equalsIgnoreCase(systemdata.getEntity())){
              //   HJWD = systemdata.getThevalue()+systemdata.getUnit();
             //}
             //if("SBMC".equalsIgnoreCase(systemdata.getEntity())){
               //  sysName = systemdata.getThevalue()+systemdata.getUnit();
             //}
             //if("SBMS".equalsIgnoreCase(systemdata.getEntity())){
               //  SBMS = systemdata.getThevalue()+systemdata.getUnit();
             //}
         //}
     //}
     Vector systemVector = null;
     if(ipAllData != null && ipAllData.containsKey("systemgroup")){
         //System.out.println("----------11111111111111111--------------");
     	 systemVector = (Vector)ipAllData.get("systemgroup");
     }
     //System.out.println("systemVector    " + systemVector.size());
     String sysDescr = "";
     String sysUpTime = "";
     //System.out.println("systemdata.getSubentity().......1111111   " + systemVector!=null);
     if(systemVector!=null){
         //System.out.println("systemdata.getSubentity().......322222   ");
         for(int i=0;i<systemVector.size();i++){
             Systemcollectdata systemdata = (Systemcollectdata)systemVector.get(i);
             //System.out.println("systemdata.getSubentity()   " + systemdata.getSubentity());
             if("sysDescr".equalsIgnoreCase(systemdata.getSubentity())){                 
                 sysDescr = systemdata.getThevalue();
             }
             if("sysUpTime".equalsIgnoreCase(systemdata.getSubentity())){
                 sysUpTime = systemdata.getThevalue();
             }
         }
     }
     
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String time1 = sdf.format(new Date());
								
	String pingconavg ="0";
	String starttime1 = time1 + " 00:00:00";
	String totime1 = time1 + " 23:59:59";
		
	Hashtable ConnectUtilizationhash = new Hashtable();
	I_HostCollectData hostmanager=new HostCollectDataManager();
	try{
		ConnectUtilizationhash = hostmanager.getCategory(vo.getIpAddress(),"Ping","ConnectUtilization",starttime1,totime1);
	}catch(Exception ex){
		ex.printStackTrace();
	}
		
	if (ConnectUtilizationhash.get("avgpingcon")!=null){
		pingconavg = (String)ConnectUtilizationhash.get("avgpingcon");
		pingconavg = pingconavg.replace("%", "");
	}
	request.setAttribute("pingconavg", new Double(pingconavg));
	double avgpingcon = (Double)request.getAttribute("pingconavg");
    int percent1 = Double.valueOf(avgpingcon).intValue();
    percent1 = 100;
	int percent2 = 100-percent1;
		
%>
<html>
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<script type="text/javascript"
			src="<%=rootPath%>/include/swfobject.js"></script>
		<script language="JavaScript" type="text/javascript"
			src="<%=rootPath%>/include/navbar.js"></script>

		<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css"
			rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css"
			charset="utf-8" />
		<link rel="stylesheet" type="text/css"
			href="<%=rootPath%>/js/ext/css/common.css" charset="utf-8" />
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js"
			charset="utf-8"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
		<script type="text/javascript"
			src="<%=rootPath%>/resource/xml/flush/amcolumn/swfobject.js"></script>
		<script language="javascript">	
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
	setClass();
}
function setClass(){
	document.getElementById('upsDetailTitle-1').className='detail-data-title';
	document.getElementById('upsDetailTitle-1').onmouseover="this.className='detail-data-title'";
	document.getElementById('upsDetailTitle-1').onmouseout="this.className='detail-data-title'";
}
function refer(action){
		document.getElementById("id").value="<%=vo.getId()%>";
		var mainForm = document.getElementById("mainForm");
		mainForm.action = '<%=rootPath%>' + action;
		mainForm.submit();
}
</script>
	</head>
	<body id="body" class="body" onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
		<input type=hidden name="orderflag">
		<input type=hidden name="id">
		
		<table id="body-container" class="body-container">
			<tr>
				 <!--  <td class="td-container-menu-bar">
					<table id="container-menu-bar" class="container-menu-bar">
						<tr>
							<td>
								<=menuTable%>
							</td>	
						</tr>
					</table>
				</td>-->
			
				<td class="td-container-main">
					<table id="container-main" class="container-main" >
						<tr>
							<td class="td-container-main-detail"  width=98%> 
								<table id="container-main-detail" class="container-main-detail" >
										<tr>
										<td> 
											<table id="detail-content" class="detail-content" width="100%" background="<%=rootPath%>/common/images/right_t_02.jpg" cellspacing="0" cellpadding="0">
				<tr>
					<td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
					<td class="layout_title"><b>设备详细信息</b></td>
					<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
				</tr>
				<tr>
				 <td width="20%" height="26" align="left" nowrap  class=txtGlobal>&nbsp;设备标签:										
										        <span id="lable"><%=vo.getAlias()%></span> </td>
                         <td width="20%" height="26" align="left" nowrap class=txtGlobal >&nbsp;系统名称:
											<span id="sysname"><%=vo.getSysName()%></span></td>
                         <td width="20%" height="26" align=left valign=middle nowrap class=txtGlobal>&nbsp;IP地址:
											<%=vo.getIpAddress()%>
						 </td>
				       </tr>
			         </table>
										</td>
									</tr>
									<tr>
										<td>
											<table id="detail-data" class="detail-data">
												<tr>
											    	<td class="detail-data-header">
											    		<%=upsDetailTitleTable%>
											    	</td>
											  </tr>
											  <tr>
											      
											    	<td>
											    		<table class="detail-data-body">
												      		<tr>
												      			<td>
													      			<table width="100%" border="0" cellpadding="0" cellspacing="0">
   	                                         <tr>
										      <td> 
											           <table class="detail-data-body">
												      		  <tr>
											                    			<td align=center >
											                    				<br>
											                    				<table cellpadding="0" cellspacing="0" width=48% align=center>
								              									<tr> 
								                									<td width="100%" align=center> 
								                										<div id="flashcontent1">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Input_V.swf?ipadress=<%=vo.getIpAddress()%>&table=input&obj1=SRXDYAB&obj2=SRXDYBC&obj3=SRXDYCA&tag=IV", "Area_Input_V", "430", "280", "8", "#ffffff");
																					so.write("flashcontent1");
																					//var so = new SWFObject("<%=rootPath%>/flex/common_line.swf?ipadress=<%=vo.getIpAddress()%>&title=输入线电压&tablename=input<%=ip%>&obj1=SRXDYAB&obj2=SRXDYBC&obj3=SRXDYCA&tag=IV", "Area_Input_V", "430", "280", "8", "#ffffff");
																					//so.write("flashcontent1");
																				</script>				
															                		</td>
																		</tr>             
																	</table> 
											                  			</td>
												                		<td align=center >
												                			<br>
												                			<table cellpadding="0" cellspacing="0" width=48% align=center>
								              									<tr> 
								                									<td width="100%"  align=center> 
								                										<div id="flashcontent2">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Input_A.swf?ipadress=<%=vo.getIpAddress()%>&table=input&obj1=AXSRDL&obj2=BXSRDL&obj3=CXSRDL&tag=IA", "Area_Input_A", "430", "280", "8", "#ffffff");
																					so.write("flashcontent2");
																				</script>				
															                		</td>
																		</tr>             
																	</table> 
												           			</td>
                    													</tr>
                    													 
                    													<tr>
                  												<td align=center >
												            		<br>
												                	<table cellpadding="0" cellspacing="0" width=48%>
								              							<tr> 
								                							<td width="100%"  align=center> 
								                								<div id="flashcontent3">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Output_V.swf?ipadress=<%=vo.getIpAddress()%>&table=output&obj1=SCXDYA&obj2=SCXDYB&obj3=SCXDYC&tag=OV", "Area_Output_V", "430", "280", "8", "#ffffff");
																					so.write("flashcontent3");
																				</script>				
															                </td>
																		</tr>             
																	</table> 
																	<br>
												          		</td>
												          		<td align=center >
																	<br>
																	<table cellpadding="0" cellspacing="0" width=48%>
								              							<tr> 
								                							<td width="100%"  align=center> 
								                								<div id="flashcontent4">
																					<strong>You need to upgrade your Flash Player</strong>
																				</div>
																				<script type="text/javascript">
																					var so = new SWFObject("<%=rootPath%>/flex/Area_Output_Kw.swf?ipadress=<%=vo.getIpAddress()%>&table=output&obj1=AXSCYGGL&obj2=BXSCYGGL&obj3=CXSCYGGL&tag=OP", "Area_Output_Kw", "430", "280", "8", "#ffffff");
																					so.write("flashcontent4");
																				</script>				
															                </td>
																		</tr>             
																	</table> 
																	<br>
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
									</table>
									
							</td>						
							<td class="td-container-main-tool" width="15%">
								<jsp:include page="/ups/upstoolbar.jsp">										
								    <jsp:param value="<%=vo.getIpAddress()%>" name="ipaddress" />
									<jsp:param value="<%=vo.getSysOid()%>" name="sys_oid" />
									<jsp:param value="<%=vo.getId()%>" name="tmp" />
									<jsp:param value="ups" name="category" />
									<jsp:param value="emerson" name="subtype" />
								</jsp:include>
						   </td>
						</tr>					
					</table>
				</td>
			</tr>
		</table>
	</form>
	</body>
</html>