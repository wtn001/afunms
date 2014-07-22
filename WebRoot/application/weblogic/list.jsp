<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.topology.util.NodeHelper"%>
<%@page import="com.afunms.application.model.*"%>
<%@page import="com.afunms.polling.base.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.indicators.model.NodeDTO"%>
<%@page import="com.afunms.indicators.util.NodeUtil"%>
<%@page import="com.afunms.common.util.*" %>

<%
  String rootPath = request.getContextPath();
  List list = (List)request.getAttribute("list");
  if(list == null)list = new ArrayList();
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link
	href="<%=rootPath%>/common/contextmenu/skins/default/contextmenu.css"
	rel="stylesheet">
<script src="<%=rootPath%>/common/contextmenu/js/jquery-1.8.2.min.js"
	type="text/javascript"></script>
<script src="<%=rootPath%>/common/contextmenu/js/contextmenu.js"
	type="text/javascript"></script>
<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script> 

<script language="JavaScript" type="text/javascript">
  var delAction = "<%=rootPath%>/db.do?action=delete";
  var listAction = "<%=rootPath%>/db.do?action=list";

  function toAdd()
  {
     mainForm.action = "<%=rootPath%>/weblogic.do?action=ready_add";
     mainForm.submit();
  }
  function toDelete()
  {
  if(confirm('�Ƿ�ȷ��ɾ��������¼?')) {
     mainForm.action = "<%=rootPath%>/weblogic.do?action=delete";
     mainForm.submit();
    }
  }  
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
<BODY onload="initmenu();">
<form method="post" name="mainForm">
<table id="body-container" class="body-container">
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		
		</td>
		<td align="center" valign=top>
			<table style="width:98%"  cellpadding="0" cellspacing="0" align="center">
			<tr>
				<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%"><table id="content-header" class="content-header">
									<tr>
										<td align="left" width="5"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
										<td class="content-title">&nbsp;Ӧ�� >> �м������ >> WEBLOGIC�����б�</td>
										<td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
									</tr>
								</table>
			  </td>
        						<tr>           
								<td>
								<table width="100%" cellpadding="0" cellspacing="0">
									<tr>
										<td width="100%" style="text-align: right;" class="body-data-title">
											<a href="#" onclick="toAdd()">���</a>
											<a href="#" onclick="toDelete()">ɾ��</a>&nbsp;&nbsp;&nbsp;
										</td>
									</tr>
								</table>
										</td>
        						</tr>						
								
				
		<tr >
			<td colspan="2">
				<table cellspacing="0" cellpadding="0" width="100%" >
					<tr class="microsoftLook0" height=28>
					<td align="center" class="body-data-title"><INPUT type="checkbox" class=noborder name="checkall" onclick="javascript:chkall()"></td>
    					<td align="center" class="body-data-title">���</td>
      					<td align="center" class="body-data-title">����</td>
      					<td align="center" class="body-data-title">IP��ַ</td>
      					<td align="center" class="body-data-title">��������</td> 
      					<td align="center" class="body-data-title">��ǰ״̬</td>    
      					<td align="center" class="body-data-title">�Ƿ���</td>
    					<td align="center" class="body-data-title">����</td>
					</tr>
<%
if(list.size() > 0){
    for(int i=0;i<list.size();i++)
    {
       WeblogicConfig vo = (WeblogicConfig)list.get(i);
       Node node = PollingEngine.getInstance().getWeblogicByID(vo.getId());
       String alarmmessage = "";
       int status = 0;
       int alarmLevel = 0;
       if(node != null){	
		Hashtable checkEventHashtable = ShareData.getCheckEventHash();
		NodeUtil nodeUtil = new NodeUtil();
		/*===========for status start==================*/
		NodeDTO nodeDTO = nodeUtil.conversionToNodeDTO(node);
		if(nodeDTO!=null){
			String chexkname = vo.getId()+":"+nodeDTO.getType()+":"+nodeDTO.getSubtype()+":";
			//System.out.println(chexkname);
			if(checkEventHashtable!=null){
				for(Iterator it = checkEventHashtable.keySet().iterator();it.hasNext();){ 
			        String key = (String)it.next(); 
			        if(key.startsWith(chexkname)){
			        	if(alarmLevel < (Integer) checkEventHashtable.get(key)){
			        		alarmLevel = (Integer) checkEventHashtable.get(key); 
			        	}
			        }
				}
			}
		}				
       }
       
%>
       <tr bgcolor="#FFFFFF" class="microsoftLook">
    		<td align="center" class="body-data-list"><INPUT type="checkbox" class=noborder name=checkbox value="<%=vo.getId()%>"></td>
    		<td align="center" class="body-data-list"><font color='blue'><%=1 + i%></font></td>
		<td align="center" class="body-data-list"><%=vo.getAlias()%></td> 
		<td align="center" class="body-data-list"><%=vo.getIpAddress()%></td> 
		<td align="center" class="body-data-list"><%=vo.getCommunity()%></td> 
		<td align="center" class="body-data-list"><img src="<%=rootPath%>/resource/<%=NodeHelper.getCurrentStatusImage(alarmLevel)%>" border="0" alt=<%=alarmmessage%>></td>
		<td align="center" class="body-data-list">
			<%
				if(vo.getMon_flag() == 0){
			%>
			&nbsp;δ����
			<%
				}else{
			%>	
			&nbsp;�Ѽ���
			<%
				}
			%>	
		</td>	
		<td align="center" class="body-data-list">
		<input type="hidden" id="id" name="id"
											value="<%=vo.getId()%>"> <img class="img"
											src="<%=rootPath%>/resource/image/status.gif" border="0"
											width=15 alt="�Ҽ�����">
		</td>
  	</tr>
<%	}
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
	</form>
</BODY>
<script language="JavaScript">
		$('.img').contextmenu({
				height:115,
				width:100,
				items : [{
					text :'�޸���Ϣ',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/edit.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						edit(id);
					}
				},{
					text :'������Ϣ',
					icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/detail.gif',
					action: function(target){
						var id=$($(target).parent()).find('#id').val();
						detail(id);
					}
				},{
                    text :'ȡ������',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/cancelMng.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                    	cancelalert(id);
                    }
                },{
                    text :'��Ӽ���',
                    icon :'<%=rootPath%>/common/contextmenu/skins/default/icons/addMn.gif',
                    action: function(target){
                    	var id=$($(target).parent()).find('#id').val();
                        addalert(id);
                    }
                }]
			});	
	function detail(node)
	{
	    location.href="<%=rootPath%>/weblogic.do?action=detail&id="+node;
	}
	function edit(node)
	{
		location.href="<%=rootPath%>/weblogic.do?action=ready_edit&id="+node;
	}
	function cancelalert(node)
	{
		location.href="<%=rootPath%>/weblogic.do?action=cancelalert&id="+node;
	}
	function addalert(node)
	{
		location.href="<%=rootPath%>/weblogic.do?action=addalert&id="+node;
	}
	</script>
</HTML>
