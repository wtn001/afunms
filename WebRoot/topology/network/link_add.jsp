<%@page language="java" contentType="text/html;charset=gb2312" %>
<%@page import="java.util.*"%>
<%@page import="com.afunms.topology.model.HostNode"%>
<%@page import="com.afunms.polling.node.IfEntity"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%
	String rootPath = request.getContextPath();

	List list = (List) request.getAttribute("list");
	Iterator it1 = (Iterator) request.getAttribute("start_if");
	Iterator it2 = (Iterator) request.getAttribute("end_if");
	int startId = ((Integer) request.getAttribute("start_id"))
			.intValue();
	int endId = ((Integer) request.getAttribute("end_id")).intValue();
	String startIndex = (String) request.getAttribute("start_index");
	String endIndex = (String) request.getAttribute("end_index");
	List<IfEntity> startHostIfentityList = (ArrayList<IfEntity>)request.getAttribute("startHostIfentityList");
	List<IfEntity> endHostIfentityList = (ArrayList<IfEntity>)request.getAttribute("endHostIfentityList");
%>
<%
	String menuTable = (String) request.getAttribute("menuTable");
   String xml = "network.jsp";  
   String viewFile = "network.jsp";  
%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<script type="text/javascript" src="<%=rootPath%>/include/swfobject.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>

<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>

<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css"/>

<link rel="stylesheet" type="text/css" 	href="<%=rootPath%>/js/ext/lib/resources/css/ext-all.css" charset="gb2312" />
<script type="text/javascript" 	src="<%=rootPath%>/js/ext/lib/adapter/ext/ext-base.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/ext-all.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/js/ext/lib/locale/ext-lang-zh_CN.js" charset="gb2312"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/jquery-1.4.2.min.js"></script>
<script language="javascript">     
  var j = jQuery.noConflict();     
</script>  
<!--nielin add for timeShareConfig at 2010-01-04 start-->
<script type="text/javascript" 	src="<%=rootPath%>/application/resource/js/timeShareConfigdiv.js" charset="gb2312"></script>
<!--nielin add for timeShareConfig at 2010-01-04 end-->
<script type="text/javascript" src="<%=rootPath%>/topology/network/js/profile.js"></script>
<script type="text/javascript" src="<%=rootPath%>/topology/network/js/global.js"></script>
<script type="text/javascript" src="<%=rootPath%>/topology/network/js/disable.js"></script>
<script type="text/javascript" src="<%=rootPath%>/topology/network/js/menu.js"></script>
<script type="text/javascript" src="<%=rootPath%>/topology/network/js/map.js"></script>
<script type="text/javascript" src="<%=rootPath%>/js/engine.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/js/util.js"></script> 
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/TopoRemoteService.js"></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/LinkRemoteService.js"></script>
<script type="text/javascript" src="<%=rootPath%>/topology/network/js/topology.js"></script>

<script  type="text/javascript">
	var start_ip = '<%=(String)request.getAttribute("startIp")%>';
	var end_ip = '<%=(String)request.getAttribute("endIp")%>';
	
	var startTypeAndId = '<%=(String)request.getAttribute("startTypeAndId")%>';
	var endTypeAndId = '<%=(String)request.getAttribute("endTypeAndId")%>';
	
	//����Ƿ�ɹ���־
	var addflag = false;

	/*******************************************
	����ѡ��ť���ı��豸�б��ѡ����ʱ����̬�ı��豸�˿������б�
	����:
		deviceSelectId  �豸�����б��id
		ifindexSelectId �˿������б��id
	********************************************/
	function doChange(deviceSelectId, ifindexSelectId){
      //mainForm.action = "<%=rootPath%>/link.do?action=ready_add";
      //mainForm.submit();  
      var deviceSelectOptions = document.getElementById(deviceSelectId);
	  var ifindexSelectOptions = document.getElementById(ifindexSelectId);
	  var nodeid = deviceSelectOptions.value;
	  
	  var start_id = document.getElementById('start_id').value;//�޸�֮ǰ������豸��id
      var end_id = document.getElementById('end_id').value;//�޸�֮ǰ���յ��豸��id
      //��������豸��id���Ӻ�̨�첽��ȡ��·��Ϣ
      j.ajax({
     		type:"POST",
			dataType:"json",
			data:"nodeid="+nodeid+"&startId="+start_id+"&endId="+end_id+"&nowtime="+(new Date()),
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=getLinkList",
			success:function(data){
				//�õ��ӿ�List��json��ʽ����
				var ifEntityMap = data.ifEntityMap;
				if(ifEntityMap != null){
					//���������
					ifindexSelectOptions.length = 0;
					j.each(ifEntityMap,function(i,v){  
						var item=document.createElement("option"); 											
						item.text=v.index+'('+v.descr+')';   
						item.value=v.index;                       					
						ifindexSelectOptions.add(item);
					});
				}
      			//�޸���·Ĭ������
				start_ip = data.startIp;
				end_ip = data.endIp;
				startTypeAndId = data.startTypeAndId;
				endTypeAndId = data.endTypeAndId;
				refreshLinkName();
			}	
      });  
  	}
  
	/*********************************************
		��̬�޸���·����
	********************************************/
	function refreshLinkName(){
	  var start_index = document.getElementById('start_index').value;
	  var end_index = document.getElementById('end_index').value;
	  var start_id = document.getElementById('start_id').value;
	  var end_id = document.getElementById('end_id').value;
	  var startAlias = "";
	  var endAlias = "";
	  j.ajax({
     		type:"post",
			url:"<%=rootPath%>/networkDeviceAjaxManager.ajax?action=getAliasById" + "&startId="+start_id+"&endId="+end_id,
			success:function(data){
				if(data == "") {
					alert("Error!");
					return;
				}
				var datas = data.split(",");
				startAlias = datas[0];
				endAlias = datas[1];
				document.getElementById('link_alias_name').value=startAlias+'_'+start_index+'/'+endAlias+'_'+end_index;
			}
      	});  
	  document.getElementById('link_name').value=start_ip+'_'+start_index+'/'+end_ip+'_'+end_index;
	  
	}
  
	/**********************************************
	js�������ݵ��߼����Ƿ����
	����ֵ��
		�����򷵻�true 
		��������false
	********************************************/
	function checkAndSubmit(){
	  	var retFlag = true;
	  	if(start_ip == end_ip){
	  		alert('�������豸������ͬ');
	  		retFlag = false;
	  	}
	 	if(!retFlag){//������������
			Ext.MessageBox.updateProgress(1);   
			Ext.MessageBox.hide();  
	 		return;
	 	}
	   	mainForm.action = "<%=rootPath%>/link.do?action=add";
	    mainForm.submit();
	    
	    //������·
	   // addLink();
	}
	
	/***********************************************************
	�첽����ʵ����· (ʹ��DWR����)
	����ʵ����·
	������
		
	**********************************************************/
    function addLink(){
    	var direction1 = '1';//�����б�־  1�������豸
       	var direction1 = document.getElementById("direction1").value;
	    var linkName = document.getElementById("link_name").value;
	    var maxSpeed = document.getElementById("max_speed").value;
	    var maxPer = document.getElementById("max_per").value;
	    var xml = "<%=xml%>";
	    var start_id = startTypeAndId;
	    var end_id = endTypeAndId;
	    var start_index = document.getElementById("start_index").value;
	    var end_index = document.getElementById("end_index").value;
	    var linetext = document.getElementById("linetext").value;
	    var interf = document.getElementById("interf").value;
        var url = "<%=rootPath%>/resource/xml/<%=viewFile%>";//�����ͼ
        LinkRemoteService.addLink(direction1,linkName, maxSpeed, maxPer, xml, start_id, start_index, end_id, end_index,linetext,interf, {
				callback:function(data){
					Ext.MessageBox.updateProgress(1);   
					Ext.MessageBox.hide();  
					if(data=="error"){
						alert("ʵ����·����ʧ�ܣ�");
					} else if(data=="error1"){
					    alert("ʵ����·����ʧ��:��ͬ�˿ڵ���·�Ѿ�����!");
					} else if(data=="error2"){
					    alert("ʵ����·����ʧ��:�Ѿ�����˫��·!");
					} else {
					    if(data){
					        var arr=data.split(":");
					        if(arr[1]=="0"){
					            addlink(arr[0],url);
					        } else {
					            addAssLink(arr[0],url)
					        }
					        // ҵ��ɹ�    
							aler('�ɹ����');
					    }
					}
				}
			});
    }
    
     Ext.onReady(function()
     {  
 		Ext.get("process").on("click",function(){
     		{      
	            Ext.MessageBox.wait('���ݼ����У����Ժ�.. ');
	        	checkAndSubmit();
     		}
      	 // mainForm.submit();
 		});	
	
	});
    
//-- nielin modify at 2010-01-04 start ----------------
function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}    

function setReceiver(eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/user.do?action=setReceiver&event='+event.id+'&value='+event.value);
}
//-- nielin modify at 2010-01-04 end ----------------


</script>

<script language="JavaScript" type="text/JavaScript">
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
//��Ӳ˵�	
function initmenu()
{
	refreshLinkName();
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
	timeShareConfiginit(); // nielin add for time-sharing at 2010-01-04
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
								<td class="td-container-main-add">
									<table id="container-main-add" class="container-main-add">
										<tr>
											<td>
												<table id="add-content" class="add-content">
													<tr>
														<td>
															<table id="add-content-header" class="add-content-header">
																<tr>
																	<td align="left" width="5">
																		<img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" />
																	</td>
																	<td class="add-content-title">
																		��Դ&gt;&gt; �豸ά�� &gt;&gt; ��·���
																	</td>
																	<td align="right">
																		<img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" />
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-body"
																class="detail-content-body">
																<tr>
																	<td>
																		<table border="0" id="table1" cellpadding="0" cellspacing="1" width="100%">
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">
																					��·����&nbsp;<input type='hidden' name='direction1' id='direction1' value='1'/>
																				</TD>
																				<TD nowrap width="40%">&nbsp; <input type="text" id='link_name' name="link_name" size="47" class="formStyle" value="" readonly="readonly"></TD>
																				<TD nowrap align="right" height="24" width="10%">
																					��·����&nbsp;<!-- <input type='hidden' name='direction1' id='direction1' value='1'/> -->
																				</TD>
																				<TD nowrap width="40%">&nbsp; <input type="text" id='link_alias_name' name="link_alias_name" size="47" class="formStyle" value=""></TD>
																				
																				
																			</tr>
																			<tr style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					���������ʷ�ֵ&nbsp;
																				</TD>
																				<TD nowrap width="40%">&nbsp; <input type="text"  id='max_per' name="max_per" maxlength="50%" size="20" class="formStyle" value="10">(%)</TD>
																				<TD nowrap align="right" height="24" width="10%">
																					��·������ֵ&nbsp;
																				</TD>
																				<TD nowrap width="40%">&nbsp; <input type="text"  id='max_speed' name="max_speed" maxlength="50%" size="20" class="formStyle" value="200000">(KB/��)</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">
																					����豸&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<select size=1 name='start_id' id='start_id'
																						style='width: 250px;' onchange="doChange('start_id','start_index')">
																						<%
																							for (int i = 0; i < list.size(); i++) {
																								HostNode node = (HostNode) list.get(i);
																								String selected = "";
																								if (node.getId() == startId){
																									selected = "selected";
																								}
																						%>
																						<option value='<%=node.getId()%>' <%=selected%>><%=node.getAlias()%>(<%=node.getIpAddress()%>)</option>
																						<%
																							}
																						%>
																					</select>
																				</TD>
																				<TD nowrap align="right" height="24" width="10%">
																					���˿�����&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<select size=1 name='start_index' id='start_index'
																						style='width: 250px;' onchange="refreshLinkName();">
																						<%
																						
																							for(IfEntity ifObj:startHostIfentityList){
																								String selected = "";
																								if (ifObj.getIndex().equals(startIndex)){
																									selected = "selected";
																								}
																						%>
																						<option value='<%=ifObj.getIndex()%>' <%=selected%>><%=ifObj.getIndex()%>(<%=ifObj.getDescr()%>)</option>
																						<%
																							}
																						%>
																					</select>
																				</TD>
																			</tr>
																			<tr  style="background-color: #ECECEC;">
																				<TD nowrap align="right" height="24" width="10%">
																					�յ��豸&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<select size=1 name='end_id' id='end_id' style='width: 250px;'
																						onchange="doChange('end_id','end_index')">
																						<%
																							for (int i = 0; i < list.size(); i++) {
																								HostNode node = (HostNode) list.get(i);
																								String selected = "";
																								if (node.getId() == endId){
																									selected = "selected";
																								}
																						%>
																						<option value='<%=node.getId()%>' <%=selected%>><%=node.getAlias()%>(<%=node.getIpAddress()%>)</option>
																						<%
																							}
																						%>
																					</select>
																				</TD>
																				<TD nowrap align="right" height="24" width="10%">
																					�յ�˿�����&nbsp;
																				</TD>
																				<TD nowrap width="40%">
																					&nbsp;
																					<select size=1 name='end_index' id='end_index'
																						style='width: 250px;' onchange="refreshLinkName();">
																						<%
																							for(IfEntity ifObj:endHostIfentityList){
																								String selected = "";
																								if (ifObj.getIndex().equals(startIndex)){
																									selected = "selected";
																								}
																						%>
																						<option value='<%=ifObj.getIndex()%>' <%=selected%>><%=ifObj.getIndex()%>(<%=ifObj.getDescr()%>)</option>
																						<%
																							}
																						%>
																					</select>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap align="right" height="24" width="10%">�ӿ���ʾ&nbsp;</TD>
																				<TD nowrap width="40%">&nbsp; 
																					<select size=1 name='interf' style='width:200px;' id="interf">
																				       <option value="0" selected>����ʾ</option>
																					   <option value="1">��ʾ</option>
																				   </select>
																				</TD>
																				<TD nowrap align="right" height="24" width="10%">
																					��·��ʾ&nbsp;
																				</TD>
																				<TD nowrap width="40%">&nbsp;
																					<select size=1 name='linetext' style='width:200px;' id="linetext">
																				       <option value="0" selected>��</option>
																					   <option value="1">���д���������(%)</option>
																					   <option value="2">���д���������(%)</option>
																					   <option value="3">��������(KB/s)</option>
																					   <option value="4">��������(KB/s)</option>
																				   	</select>
																				</TD>
																			</tr>
																			<tr>
																				<TD nowrap colspan="4" align=center>
																					<br>
																					<input type="button" value="�� ��" style="width: 50"
																						id="process" onclick="#">
																					&nbsp;&nbsp;
																					<input type="reset" style="width: 50" value="����"
																						onclick="javascript:history.back(1)">
																				</TD>
																			</tr>
																		</TABLE>
																	</td>
																</tr>
															</table>
														</td>
													</tr>
													<tr>
														<td>
															<table id="detail-content-footer" class="detail-content-footer">
																<tr>
																	<td>
																		<table width="100%" border="0" cellspacing="0"
																			cellpadding="0">
																			<tr>
																				<td align="left" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_01.jpg" width="5" height="12" />
																				</td>
																				<td></td>
																				<td align="right" valign="bottom">
																					<img src="<%=rootPath%>/common/images/right_b_03.jpg" width="5" height="12" />
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
										<tr>
											<td>

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
</HTML>