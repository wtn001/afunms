<%@page language="java" contentType="text/html;charset=gb2312"%>
<%@page import="com.afunms.indicators.model.NodeGatherIndicators"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.config.model.Errptconfig"%>
<%@page import="com.afunms.topology.model.NetSyslogNodeAlarmKey"%>

<%
  String rootPath = request.getContextPath();
  
String str0="";
String str1="";
String str2="";
String str3="";
String str4="";
String str5="";
String str6="";
String str7="";
String p0="";
String p1="";
String p2="";
String p3="";
String p4="";
String p5="";
String p6="";
String p7="";String p8="";String p9="";
String p16="";String p17="";String p18="";
String p19="";String p20="";String p21="";
String p22="";String p23="";

String nodeid = (String)request.getAttribute("nodeid");
List flist = (List)request.getAttribute("facilitys");
//List plist = (List)request.getAttribute("prioritys");
if(flist != null && flist.size()>0){
	for(int i=0;i<flist.size();i++){
		if("0".equals(flist.get(i))) {
			str0="checked";
		}else if("1".equals(flist.get(i))) {
			str1="checked";
		}else if("2".equals(flist.get(i))) {
			str2="checked";	
		}else if("3".equals(flist.get(i))) {
			str3="checked";
		}else if("4".equals(flist.get(i))) {
			str4="checked";	
		}else if("5".equals(flist.get(i))) {
			str5="checked";
		}else if("6".equals(flist.get(i))) {
			str6="checked";	
		}else if("7".equals(flist.get(i))) {
			str7="checked";								
		}
	}
}

 List netSyslogNodeAlarmList = (ArrayList)request.getAttribute("netSyslogNodeAlarmList");
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
			//�޸Ĳ˵������¼�ͷ����
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
		</script>
		<script language="JavaScript">

			//��������
			var node="";
			var ipaddress="";
			var operate="";
			/**
			*���ݴ����id��ʾ�Ҽ��˵�
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
			*��ʾ�����˵�
			*menuDiv:�Ҽ��˵�������
			*width:����ʾ�Ŀ��
			*rowControlString:�п����ַ�����0��ʾ����ʾ��1��ʾ��ʾ���硰101�������ʾ��1��3����ʾ����2�в���ʾ
			*/
			function popMenu(menuDiv,width,rowControlString)
			{
			    //���������˵�
			    var pop=window.createPopup();
			    //���õ����˵�������
			    pop.document.body.innerHTML=menuDiv.innerHTML;
			    var rowObjs=pop.document.body.all[0].rows;
			    //��õ����˵�������
			    var rowCount=rowObjs.length;
			    //alert("rowCount==>"+rowCount+",rowControlString==>"+rowControlString);
			    //ѭ������ÿ�е�����
			    for(var i=0;i<rowObjs.length;i++)
			    {
			        //������ø��в���ʾ����������һ
			        var hide=rowControlString.charAt(i)!='1';
			        if(hide){
			            rowCount--;
			        }
			        //�����Ƿ���ʾ����
			        rowObjs[i].style.display=(hide)?"none":"";
			        //������껬�����ʱ��Ч��
			        rowObjs[i].cells[0].onmouseover=function()
			        {
			            this.style.background="#397DBD";
			            this.style.color="white";
			        }
			        //������껬������ʱ��Ч��
			        rowObjs[i].cells[0].onmouseout=function(){
			            this.style.background="#F1F1F1";
			            this.style.color="black";
			        }
			    }
			    //���β˵��Ĳ˵�
			    pop.document.oncontextmenu=function()
			    {
			            return false; 
			    }
			    //ѡ���Ҽ��˵���һ��󣬲˵�����
			    pop.document.onclick=function()
			    {
			        pop.hide();
			    }
			    //��ʾ�˵�
			    pop.show(event.clientX-1,event.clientY,width,rowCount*25,document.body);
			    return true;
			}
			
  
			
			
			  
			
		</script>
	<script type="text/javascript">

			var delAction = "<%=rootPath%>/nodeGatherIndicators.do?action=delete";
			var listAction = "<%=rootPath%>/nodeGatherIndicators.do?action=list";
			var rowNum = 0;
  			
	var alarmType = [["1" , "��ͨ�澯"], ["2" , "���ظ澯"], ["3" , "�����澯"]];
  			
	function toAdd()
  {
      mainForm.action = "<%=rootPath%>/nodesyslogrule.do?action=toolbarsave";
      mainForm.submit();
  }
			
	/**
	 * ����һ��
	 */
	function addRow(keywordDivId) {
		var tr = document.getElementById(keywordDivId).insertRow(0);
		rowNum = rowNum + 1;
		var str = "" + rowNum;
		while (str.length < 2) {
			str = "0" + str;
		}
		var types = "";
		for (var i = 0 ; i < alarmType.length; i++){
			types = types + '<OPTION value="' + alarmType[i][0] + '">' + alarmType[i][1] + '</OPTION>';
		}
		var selected = '<select id="level-' + str + '" name="level-' + str
				+ '" style="width:80px" onchange="changeDateSelect(' + str+ ',this.value)">' + types + '</select>';
		
		var td = tr.insertCell(tr.cells.length);
	
		var elemTable = document.createElement("table");
		var elemTBody = document.createElement("tBody");
		elemTable.style.marginTop = "10px";
		elemTable.id = "table" + str;
		elemTable.width = "100%";
		elemTable.align = "left";
		elemTable.cellPadding = "1";
		elemTable.cellSpacing = "1";
		elemTable.bgColor = "black";
		elemTBody.bgColor = "white";
		elemTable.appendChild(elemTBody);
		td.appendChild(elemTable);
	
		var tr0 = document.createElement("tr");
		elemTBody.appendChild(tr0);
		var td13 = tr0.insertCell(tr0.cells.length);
		td13.className = "lab";
		td13.innerHTML = '<span class="must">*</span>������';
		var td14 = tr0.insertCell(tr0.cells.length);
		td14.align = "left";
		td14.innerHTML = '<input class="input-text" id="keywords-'
				+ str
				+ '" name="keywords-'
				+ str
				+ '" type="text"  size="30"/>&nbsp;' + selected;
		var td12 = tr0.insertCell(tr0.cells.length);
		td12.className = "lab";
		td12.innerHTML = '<a href="javascript:delRow(' + keywordDivId + ','
				+ rowNum + ')">ɾ��</a>';
		//changeAlarmLevelSelect(str,document.getElementById("level-" + str).value);
		document.getElementById("rowNum").value = rowNum;
	}
	
		
	function changeAlarmLevelSelect(num , value){
		var daysArray = "";
		var str = num + "";
		while (str.length < 2) {
			str = "0" + str;
		}
		var levelSelect = document.getElementById("level-" + str);
		levelSelect.length = 0;
		for (var i = 0 ; i < daysArray.length; i++){
			levelSelect.options[i] = new Option(daysArray[i][1] , daysArray[i][0]);
		}
	}


	function delRow(keywordDivId, rowNo) {
		var str = "" + rowNo;
		while (str.length < 2) {
			str = "0" + str;
		}
		var i = 0;
		while (keywordDivId.rows[i].firstChild.firstChild.id != "table" + str) {
			i++;
		}
		keywordDivId.deleteRow(i);
	}
	
	function initAlarmWayByArray(alarmConfigDivId, store) {
		for (var i = 0; i < store.length; i++) {
			var item = store[i];
			initAlarmWay(alarmConfigDivId , item);
		}
	}
		
	function initAlarmKeywords(alarmKeywordsDivId , item){
		addRow(alarmKeywordsDivId, item.category);
		var str = "" + rowNum;
		if (str.length < 2) {
			str = "0" + str;
		}
		//document.getElementById("id-" + str).value = item.id;
		document.getElementById("keywords-" + str).value = item.keywords;
		document.getElementById("level-" + str).value = item.level;
	}
	
	// ��ȡ���ŷ�ʱ��ϸ��Ϣ��div
	function initAlarmWayDetail(){
		var rowNum = document.getElementById("rowNum");
		rowNum.value = "0";
		var alarmWayDetail = new Array();
		// ��ȡ�豸�����ķ�ʱ�����б�,
		<%	
			if(netSyslogNodeAlarmList!=null&&netSyslogNodeAlarmList.size()>=0){
				for(int i = 0 ; i < netSyslogNodeAlarmList.size(); i++){	        
		            NetSyslogNodeAlarmKey vo = (NetSyslogNodeAlarmKey)netSyslogNodeAlarmList.get(i);
		%>
		            alarmWayDetail.push({
		                id:"<%=vo.getId()%>",
		                keywords:"<%=vo.getKeywords()%>",
		                level:"<%=vo.getLevel()%>"
		            });
		<%
		        }
	        }
	    %>   
	    for(var i = 0; i< alarmWayDetail.length; i++){
	    	var item = alarmWayDetail[i];
	    	var alarmConfigDivId = "keywordsTable";
	    	initAlarmKeywords(alarmConfigDivId , item);
	    }
	}
	
	//��Ӳ˵�	
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
	
	initAlarmWayDetail();
	//initData();
}
		</script>
	</head>
	<body id="body" class="body""   onload="initmenu();">
		<form id="mainForm" method="post" name="mainForm">
			<input type="hidden" name="nodeid" id="nodeid" value="<%=nodeid%>">
			<table id="body-container" class="body-container">
				<tr>
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
									                	<td class="content-title"> <b>�澯 >> SYSLOG���� >> SYSLOG���˹���</b></td>
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
															<table width="100%" cellPadding=0 cellspacing="1" algin="center">
									                    					<tr algin="left" valign="center">                      														
									                      						<td height="28" bgcolor="#ECECEC" colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;<b>����</b></td>
									                    					</tr>
									                    					<tr align="left" valign="center" bgcolor=#ffffff> 
									                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="0" <%=str0%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
									                    						<td height="28" align="right" width=10%%><INPUT type="checkbox" class=noborder value="1" <%=str1%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td> 
									                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="2" <%=str2%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;�ؼ�</td> 
									                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="3" <%=str3%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
									                    					</tr> 
									                    					<tr align="left" valign="center" bgcolor=#ffffff> 
									                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="4" <%=str4%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
									                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="5" <%=str5%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;֪ͨ</td>
									                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="6" <%=str6%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;��ʾ</td>
									                    						<td height="28" align="right" width=10%><INPUT type="checkbox" class=noborder value="7" <%=str7%> name="fcheckbox"></td>
									                    						<td height="28" align="left" width=15%>&nbsp;&nbsp;����</td>
									                    					</tr>  
									                    					<tr algin="left" valign="center">                      														
									                      						<td height="28" bgcolor="#ECECEC" colspan="8">&nbsp;&nbsp;&nbsp;&nbsp;<b>�澯����</b></td>
									                    					</tr>   
									                    					<tr>
													                            <td style="padding:0px 0px 0px 5px;" colspan="8">
													                            	<div id="formDiv" style="width: 100%;">         
																		                <table width="100%" style="BORDER-COLLAPSE: collapse" borderColor=#cedefa cellPadding=0 rules=none border=1 algin="center" >
																	                        <tr>
																	                            <td style="padding:0px 0px 0px 5px">&nbsp;&nbsp;�ؼ��֣�
																								<input type="hidden" name="rowNum" id="rowNum"></td>
																	                        </tr>
																	                        <tr>
																	                            <td align="center">           
																	                                <table id="keywordsTable"  style="width:80%; padding:0;  background-color:#FFFFFF;" >
																                                        <tr>
																                                            <td colspan="0" height="50" align="center"> 
																                                                <span  onClick='addRow("keywordsTable");' style="border: 1px solid black;margin:10px;padding:0px 3px 0px 3px;cursor: hand;line-height:15px">����һ��</span>
																                                            </td>
																                                        </tr>
																	                                </table>
																	                            </td>
																	                        </tr>
																		                </table>
																		            </div> 
													                            </td>
													                        </tr>
									            							</table>
																		</td>
																	</tr>
																	<tr>
																			<TD nowrap colspan="20" align=center><br>
																			<input type="button" value="ȷ ��" style="width:50" class="formStylebutton" onclick="toAdd()">&nbsp;&nbsp;
																			<input type="button" value="�� ��" style="width:50" onclick="window.close()">
																			</TD>	
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
