<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@ include file="/include/globe.inc"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.topology.model.DiscoverConfig"%>
<%@page import="com.afunms.discovery.*"%>
<%
  String rootPath = request.getContextPath();  
  List communityList = (List)request.getAttribute("community_list");
  List othercoreList = (List)request.getAttribute("othercore_list");
  List specifiedList = (List)request.getAttribute("specified_list"); 
  List shieldList = (List)request.getAttribute("shield_list"); 
  List netshieldList = (List)request.getAttribute("netshield_list");  
  List includeList = (List)request.getAttribute("include_list");
		
  DiscoverDataHelper helper = new DiscoverDataHelper();
  //helper.DB2NetworkXml();
  //helper.DB2NetworkVlanXml();
         try{
    	   Runtime.getRuntime().exec("ping 10.95.240.2");
       }catch(Exception ex){
    	   ex.printStackTrace();
       }
%>
<%String menuTable = (String)request.getAttribute("menuTable");%>
<html>
<head>

<link href="<%=rootPath%>/include/navbar.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=rootPath%>/resource/js/wfm.js"></script>
<script type="text/javascript" src="<%=rootPath%>/resource/js/page.js"></script>

<meta http-equiv="Page-Enter" content="revealTrans(duration=x, transition=y)">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">


<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/discover.css" type="text/css">

<style >
#body{
	
	background:url("<%=rootPath%>/common/images/bg.jpg");
	FONT-SIZE: 9pt; LINE-HEIGHT: 12pt; 
}
</style>
<script language="javascript">
 
  function doAdd1()
  {
     var chk1 = checkinput("globe_community","string","��ͬ����",20,false);
     if(chk1)
     {
        mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=community";
        mainForm.submit();
     }  
  } 
  
  function doAdd2()
  {
     var chk1 = checkinput("special_ip","ip","IP��ַ",15,false);
     var chk2 = checkinput("special_community","string","��ͬ����",20,false);
     if(chk1&&chk2)
     {          
        mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=specified";
        mainForm.submit();        
     }        
  }
  
    function doAdd5()
  {
     var chk1 = checkinput("othercore_ip","ip","IP��ַ",15,false);
     var chk2 = checkinput("othercore_community","string","��ͬ����",20,false);
     if(chk1&&chk2)
     {          
        mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=othercore";
        mainForm.submit();        
     }        
  } 

  function doAdd3()
  {     
     var len = mainForm.net_address.value.length;
     if(len==0)
     {
         alert("���������ε�ַ!");
         mainForm.net_address.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ!");
         mainForm.net_address.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.net_address.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ");
              mainForm.net_address.focus();
              return false;
	       }
        }
     }
     mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=shield";
     mainForm.submit();     
  } 
  
    function doAdd4()
  {     
     var len = mainForm.shieldnetstart.value.length;
     if(len==0)
     {
         alert("���������ε�ַ!");
         mainForm.shieldnetstart.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ!");
         mainForm.shieldnetstart.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.shieldnetstart.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ");
              mainForm.shieldnetstart.focus();
              return false;
	       }
        }
     }
     len = mainForm.shieldnetend.value.length;
     if(len==0)
     {
         alert("���������ε�ַ!");
         mainForm.shieldnetend.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ!");
         mainForm.shieldnetend.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.shieldnetend.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ");
              mainForm.shieldnetend.focus();
              return false;
	       }
        }
     }
     mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=netshield";
     mainForm.submit();     
  }
  
    function doAdd6()
  {     
     var len = mainForm.includenetstart.value.length;
     if(len==0)
     {
         alert("���������ε�ַ!");
         mainForm.includenetstart.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ!");
         mainForm.includenetstart.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.includenetstart.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ");
              mainForm.includenetstart.focus();
              return false;
	       }
        }
     }
     len = mainForm.includenetend.value.length;
     if(len==0)
     {
         alert("���������ε�ַ!");
         mainForm.includenetend.focus();         
         return false;
     }
     else if(len>15)
     {
         alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ!");
         mainForm.includenetend.focus();         
         return false;
     }
     else
     {
        for(i=0;i<len;i++)
        {
           onechar = mainForm.includenetend.value.charAt(i);
           if(isNaN(onechar)&&onechar!='.')
           {
              alert("<���ε�ַ����ȷ>����ȷ�������ε�ַ");
              mainForm.includenetend.focus();
              return false;
	       }
        }
     }
     mainForm.action = "<%=rootPath%>/discover.do?action=add&flag=includenet";
     mainForm.submit();     
  }  
  
  function doDelete(id)
  {
     if (window.confirm("ȷʵҪɾ����?"))
     {
        mainForm.id.value = id;
        mainForm.action = "<%=rootPath%>/discover.do?action=delete&id="+id;
        mainForm.submit();      
     }   
  }
  
  function doDiscover()
  {
     var result;
     if(mainForm.core_ip.disabled == false)
     {
        var chk1 = checkinput("core_ip","ip","�豸IP",15,false);
        var chk2 = checkinput("community","string","��ͬ��",20,false);
        result = chk1&&chk2
     }   
     //else
        //result = checkinput("net_ip","ip","����IP",15,false);
     
     if(result)
     {
        mainForm.action = "<%=rootPath%>/discover.do?action=do_discover";
        mainForm.submit();        
        window.showModalDialog("<%=rootPath%>/topology/discover/monitor.jsp","���ֽ��̼���","dialogHeight:700px;dialogWidth:550px;status:0;help:0;edge:sunken;scroll:1"); 
     }
  }     

  function fromCore()
  {
      mainForm.core_ip.disabled = false;
      mainForm.community.disabled = false;
      //mainForm.net_ip.disabled = true;
      //mainForm.netmask.disabled = true;
      initmenu();
  }
  
  function fromNet()
  {
      mainForm.core_ip.disabled = true;
      mainForm.community.disabled = true;
      //mainForm.net_ip.disabled = false;
      //mainForm.netmask.disabled = false;
  }
  
  function CreateWindow(url)
{
	
msgWindow=window.open(url,"protypeWindow","toolbar=no,width=600,height=400,directories=no,status=no,scrollbars=yes,menubar=no")
}
  
  function setBid(eventTextId , eventId){
	var event = document.getElementById(eventId);
	return CreateWindow('<%=rootPath%>/business.do?action=setBid&event='+event.id+'&value='+event.value + '&eventText=' + eventTextId);
}
</script>

</head>
<BODY leftmargin="0" topmargin="0"    id="body" class="body">
<div id="itemMenu" style="display: none";>
	</div>
<form name="mainForm" method="post">
<table border="0" id="table1" cellpadding="0" cellspacing="0" width=100%>
	<tr>
		<td width="200" valign=top align=center>
			<%=menuTable%>
		
		</td>
			<td width="20" valign=top align=center>
		
		</td>
		<td align="center" valign=top >
			<table width="98%"  cellpadding="0" cellspacing="0" algin="center">
				<tr>
					<td background="<%=rootPath%>/common/images/right_t_02.jpg" width="100%">
						<table width="100%" cellspacing="0" cellpadding="0">
		                  <tr>
		                    <td align="left"><img src="<%=rootPath%>/common/images/right_t_01.jpg" width="5" height="29" /></td>
		                    <td class="layout_title"><b>�Զ����� >> ���ֲ�������</b></td>
		                    <td align="right"><img src="<%=rootPath%>/common/images/right_t_03.jpg" width="5" height="29" /></td>
		                  </tr>
		              </table>
				  	</td>
				 </tr>				
	
				<tr>
					<td>
						<table width="100%" border=0 cellpadding=0 cellspacing=1>
			<tr>
				<td height=300 bgcolor="#FFFFFF" valign="top">				
					<table cellSpacing="0" cellPadding="0" width="100%" border="0" align='left'>
                         <!--================�����豸===========================-->
										
						<tr>
							<td colspan="2" align='center'>
								<table cellspacing="0" cellpadding="0" width="100%">
								<tr><th width='70%'bgcolor="#E6E6FA" height="27">���ӵ�ַ</th></tr>
		     <tr height=27>
		    	<td align="center" bgcolor="#FFFFFF">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		           ����IP:&nbsp;<input type="text" name="core_ip" size="20"  value=""><font color="red">*</font>&nbsp;&nbsp;&nbsp;&nbsp;
		           ����ͬ��:&nbsp;<input type="text" name="community" size="20" class="formStyle" value=""><font color="red">*</font>&nbsp;&nbsp;&nbsp;
		            д��ͬ��:&nbsp;<input type="text" name="writecommunity" size="20" class="formStyle" value="">&nbsp;&nbsp;&nbsp;&nbsp;
		        </td>
		  	 </tr>  	 
  	 
		     <tr height=27>
		    	<td align="center" bgcolor="#FFFFFF">&nbsp;&nbsp;
		           <INPUT type="radio" class=noborder name=discovermodel value="0" checked>ȫ�·���&nbsp;&nbsp;&nbsp;&nbsp;
		           <INPUT type="radio" class=noborder name=discovermodel value="1">���䷢��&nbsp;&nbsp;&nbsp;&nbsp;
		        </td>
		        
		  	 </tr>  
		  	 <tr>
		  	 </tr>	 
<!--================�������ӵ�ַ�빲ͬ��===========================-->  	 
  	 <tr><th width='70%' align="center" bgcolor="#E6E6FA" height="27">�������ӵ�ַ</th></tr>
     <tr >
    	<td  align="center" bgcolor="#FFFFFF" height=27>&nbsp;&nbsp;&nbsp;           
           ����IP:&nbsp;<input type="text" name="othercore_ip" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           ��ͬ��:&nbsp;<input type="text" name="othercore_community" size="20" class="formStyle" value="">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="����" style="width:50" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick="doAdd5()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>
    	  <table width="50%">
  	 
<%
   for(int i=0;i<othercoreList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)othercoreList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='30%'><%=vo.getAddress()%>&nbsp;,&nbsp;</td>
            <td width='30%'>&nbsp;<%=vo.getCommunity()%>&nbsp;<td>
            <td width='20%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="ɾ��" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr>  	 
<!--================ȫ�ֹ�ͬ��===========================-->  	 
  	 <tr><th width='70%' align="center" bgcolor="#E6E6FA" height="27">ȫ�ֹ�ͬ��</th></tr>
     <tr >
    	<td  align="center" bgcolor="#FFFFFF" height=27>&nbsp;           
           ��ͬ����:&nbsp;<input type="text" name="globe_community" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="����" style="width:50" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick="doAdd1()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr>
    	<td  align='center'>
    	  <table width="50%">
  	 
<%
   for(int i=0;i<communityList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)communityList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getCommunity()%>&nbsp;<td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="ɾ��" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr>  
<!--================ָ���豸�ڵ㹲ͬ��===========================--> 
  	 
  	 <tr><th width='60%' align="center" bgcolor="#E6E6FA" height="27">�ض��豸�ڵ㹲ͬ��</th></tr>
     <tr >
    	<td align="center" bgcolor="#FFFFFF" height=27>&nbsp;&nbsp;&nbsp;
    	  IP��ַ:&nbsp;<input type="text" name="special_ip" size="20" >&nbsp;&nbsp;&nbsp;&nbsp;
           ��ͬ����:&nbsp;<input type="text" name="special_community" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="����" style="width:50" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick="doAdd2()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>
    	  <table width="50%">
  	 
<%
   for(int i=0;i<specifiedList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)specifiedList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='30%'><%=vo.getAddress()%>&nbsp;,&nbsp;</td>
            <td width='30%'>&nbsp;<%=vo.getCommunity()%>&nbsp;<td>
            <td width='20%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="ɾ��" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr> 

<!--================ֻ�������������豸===========================-->  	
  	 <tr><th width='70%' align="center" bgcolor="#E6E6FA" height="27">ֻ�������µ�����</th></tr>
     <tr>
    	<td align="center" bgcolor="#FFFFFF" height=27>&nbsp;           
           <!--���ε�ַ:<input type="text" name="net_address" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;-->
           ��ʼ����:&nbsp;<input type="text" name="includenetstart" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           ��������:&nbsp;<input type="text" name="includenetend" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="����" style="width:50" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick="doAdd6()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>
    	  <table width="50%">
  	 
<%
   for(int i=0;i<includeList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)includeList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getIncludenetstart()%>&nbsp;��&nbsp;<%=vo.getIncludenetend()%><td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="ɾ��" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr>
<!--================Ҫ���ε�IP===========================-->  	
  	 <tr><th width='70%' align="center" bgcolor="#E6E6FA" height="27">Ҫ���ε�IP</th></tr>
     <tr >
    	<td align="center" bgcolor="#FFFFFF" height=27>&nbsp;           
           ���ε�ַ:&nbsp;<input type="text" name="net_address" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="����" style="width:50" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick="doAdd3()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr>
    	<td align='center'>
    	  <table width="50%">
  	 
<%
   for(int i=0;i<shieldList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)shieldList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getAddress()%>&nbsp;<td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="ɾ��" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr> 
<!--================Ҫ���ε�����===========================-->  	 
  	 <tr><th width='70%' align="center" bgcolor="#E6E6FA" height="27">Ҫ���ε�����</th></tr>
     <tr >
    	<td align="center" bgcolor="#FFFFFF" height=27>&nbsp;           
           <!--���ε�ַ:<input type="text" name="net_address" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;-->
           ��ʼ����:&nbsp;<input type="text" name="shieldnetstart" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           ��������:&nbsp;<input type="text" name="shieldnetend" size="20" class="formStyle">&nbsp;&nbsp;&nbsp;&nbsp;
           <input type="button" value="����" style="width:50" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick="doAdd4()">&nbsp;&nbsp;
        </td>
  	 </tr>  	 
  	 <tr >
    	<td align='center'>
    	  <table width="50%">
  	 
<%
   for(int i=0;i<netshieldList.size();i++)
   {
       DiscoverConfig vo = (DiscoverConfig)netshieldList.get(i);
%>
          <tr><td width='20%'><font color='red'><%=i+1%>.</font></td>
            <td width='50%'><%=vo.getShieldnetstart()%>&nbsp;��&nbsp;<%=vo.getShieldnetend()%><td>
            <td width='30%'>&nbsp;<a href="#" onclick="doDelete(<%=vo.getId()%>)"><img src="<%=rootPath%>/resource/image/delete.gif" alt="ɾ��" border=0></a>&nbsp;<td>
          </tr>
<%}%>
</table></td></tr> 

 	   	   				
							</table>
							</td>
						</tr>
						<tr>
						<td bgcolor="#E6E6FA">
						<br>
						</td>
						</tr>
						<tr>	
				<TD bgcolor="#FFFFFF" height="27" align='center'>��������ҵ��:&nbsp;
				<input type=text readonly="readonly" onclick='setBid("bidtext" , "bid");' id="bidtext" name="bidtext" size="50" maxlength="32" value="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input type=button value="��������ҵ��" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick='setBid("bidtext" , "bid");'>
				<input type="hidden" id="bid" name="bid" value="">
				</td>
			</tr>
				<tr>
						<td bgcolor="#E6E6FA">
						<br>
						
						</td>
						</tr>	
				<tr><td colspan="2" align='center' bgcolor="#E6E6FA" height=29>
				<input type="button" value="��ʼ����" style="width:100" class=btn_mouseout onmouseover="this.className='btn_mouseover'"
             onmouseout="this.className='btn_mouseout'"
             onmousedown="this.className='btn_mousedown'"
             onmouseup="this.className='btn_mouseup'" onclick="doDiscover()">
				</td></tr>		
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