<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK" import="java.util.*"%>
<%@page import="java.io.File"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.yhgl.*"%>
<%@page import="com.afunms.polling.node.*"%>
<%@page import="com.afunms.polling.*"%>
<%@page import="com.afunms.polling.impl.*"%>
<%@page import="com.afunms.polling.api.*"%>
<jsp:useBean id="Upload" scope="page" class="com.jspsmart.upload.SmartUpload" />
<jsp:useBean id="docMgr" scope="session" class="com.yhgl.documentMgr" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%
	String rootPath = request.getContextPath();	
	String ipaddress = request.getParameter("ipaddress");
	//System.out.println(ipaddress);
	//Host host = (Host)PollingEngine.getInstance().getNodeByIP(ipaddress);
	//String oid = host.getSysOid();
	//oid = oid.replaceAll("\\.","-");	
	List indexlist = (List)request.getAttribute("indexlist");
	Hashtable hs = (Hashtable)request.getAttribute("hs");
   
%>
<html>
  <head>
    <style type="text/Css">    
.dragme{position:relative;} 
  .black_overlay{
            display: none;
            position: absolute;
            top: 0%;
            left: 0%;
            width: 100%;
            height: 100%;
            background-color: grey;
            z-index:1001;
            -moz-opacity: 0.8;
            opacity:.80;
            filter: alpha(opacity=30);
        }
        .white_content {
            display: none;
            position: absolute;
            top: 10%;
            left: 20%;
            width: 60%;
            height: 80%;
            padding: 16px;
            border: 3px solid #e0e9fa;
            background-color: white;
            z-index:1002;
            overflow: auto;
        }
</style> 
    <title>选择端口</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!--   <%--> 
	//String index = request.getParameter("index");
	//vo.setIndex(index);
	//String name = request.getParameter("name");
	//vo.setName(name);
	//dao.setVo(vo);
    //List list = dao.portList();
	<!--%>-->  
  </head>
    <script type="text/javascript">     
//alert(document.getElementById("moveid").style.left); 
var ie=document.all; 
var nn6=document.getElementById&&!document.all; 
var isdrag=false; 
var x,y; 
var dobj; 
var getX; 
var getY; 
var picid;
function movemouse(e){ 
  if (isdrag){ 
   dobj.style.left = nn6 ? tx + e.clientX - x : tx + event.clientX - x; 
   dobj.style.top  = nn6 ? ty + e.clientY - y : ty + event.clientY - y; 
   
   //alert(getX); 
  //alert(getY); 
   return false; 
   } 
} 

function selectmouse(e){ 
    
    var fobj       = nn6 ? e.target : event.srcElement; 
    var topelement = nn6 ? "Html" : "BODY"; 
    while (fobj.tagName != topelement && fobj.className != "dragme") { 
     fobj = nn6 ? fobj.parentNode : fobj.parentElement; 
     } 

 
//fobj = document.getElementById("moveid") 

    if (fobj.className=="dragme") { 
   isdrag = true; 
   dobj = fobj; 
   tx = parseInt(dobj.style.left+0); 
   ty = parseInt(dobj.style.top+0); 
   x = nn6 ? e.clientX : event.clientX; 
   y = nn6 ? e.clientY : event.clientY; 
   document.onmousemove=movemouse; 
   return false; 
    } 
} 

document.onmousedown=selectmouse; 
document.onmouseup=new Function("isdrag=false"); 

//得到元素id号
var allidxy = new Array();
var sel = new Array();
function getID(mid){
	var size = document.getElementById("size").value;
	<%
		if(indexlist != null && indexlist.size()>0){
			for(int k=0;k<indexlist.size();k++){
				String index = (String)indexlist.get(k);
	%>
				findPosition("index"+<%=index%>);
				var select = document.getElementById("sel"+<%=index%>).value;
	    			sel.push(select+";");
	<%
			}
		}
	%>
	allidxy=allidxy.join("");
	
	sel=sel.join("");
	
document.getElementById("panelxml").value = allidxy;
document.getElementById("select").value = sel;
alert(document.getElementById("select").value);
panelform.action="<%=rootPath%>/panel.do?action=createpanelmodel";
panelform.submit();
}
//得到元素的坐标位置
function findPosition(id) { 
oElement = document.getElementById(id);
  var x2 = 0; 
  var y2 = 0; 
  var width = oElement.offsetWidth; //得到元素的宽度 
  var height = oElement.offsetHeight; 
  //alert(width + "=" + height); 
  if( typeof( oElement.offsetParent ) != 'undefined' ) 
  { 
    for( var posX = 0, posY = 0; oElement; oElement = oElement.offsetParent ) 
    { 
      posX += oElement.offsetLeft; 
      posY += oElement.offsetTop;      
    } 
    x2 = posX + width; 
    y2 = posY + height; 
   // alert("1====="+posX);
    document.getElementById("posX").value = posX;
    document.getElementById("posY").value = posY;
    document.getElementById("id").value = id;
    
    //alert("2====="+posY);
    //return [ posX, posY ,x2, y2]; 
    allidxy.push(posX+','+posY+','+id+';');
    } else{ 
      x2 = oElement.x + width; 
      y2 = oElement.y + height; 
  alert(oElement.x); 
  alert(oElement.y); 
      //return [ oElement.x, oElement.y, x2, y2]; 
  } 
  getX = posX - 200; 
  getY = posY - 10; 
} 

function confirmpic()
{
    var obj = document.getElementsByName("picradio");
    //alert(obj.length);
    for(var i=0;i<obj.length;i++)
    {
      if(obj[i].checked == true)
      {  
         document.getElementById("index"+picid).src = "<%=rootPath%>/panel/view/image/" + obj[i].value;
         //document.getElementById("div"+picid).style.display = "block";         
         document.getElementById(picid).value = picid + ":" + obj[i].value;
      }  
    }    
    document.getElementById("light").style.display="none";
    document.getElementById("fade").style.display="none";
}
function listpic(id)
{
    for(var i=0;i<document.mainform.checkbox.length;i++)
    {
         if(document.mainform.checkbox[i].value == id)
         {
              if(document.mainform.checkbox[i].checked == true)
              {
                  picid = id;
                  document.getElementById("light").style.display="block";
                  document.getElementById("fade").style.display="block";
              }else{
                  alert("请先选择对应的端口！");
              }             
         }
    }
    
}
function confirmport()
{
     var selectport = new Array();
     for(var i=0;i<document.mainform.checkbox.length;i++)
     {
         if(document.mainform.checkbox[i].checked==true)
         {
              var valid = document.mainform.checkbox[i].value;
              var val = document.getElementById(valid).value;
              selectport.push(val+";");
         }
     }
     selectport = selectport.join("");
     //alert(selectport);
     window.opener.document.getElementById("fselectport").value = selectport;
     //alert(window.opener.document.getElementById("fselectport").value);
     window.close();
}
function chkall()
  {
     if ( mainform.checkbox.length == null )
     {
        if( mainform.checkall.checked )
           mainform.checkbox.checked = true;
        else
           mainform.checkbox.checked = false;
     }
     else
     {
        if(mainform.checkall.checked)
        {
           for( var i=0; i < mainform.checkbox.length; i++ )
              mainform.checkbox[i].checked = true;
        }
        else
        {
           for( var i=0; i < mainform.checkbox.length; i++ )
              mainform.checkbox[i].checked = false;
        }
     }
  }
</script> 
  <body>
  <form name="mainform" method="post">
    <INPUT type="checkbox" id="checkall" name="checkall" onclick="chkall()" class="noborder">全选<br>
    <table>                                                                        
        <%       
	        int size = hs.size();
	        for(int x = 0; x<size;x++){
	        	String[] str = (String[])(hs.get(x).toString()).split(";");
				if(x%6==0){
		    %><tr>
		     <td>		     
		     <input type="checkbox" id="checkbox" name="checkb" value="<%=str[0]%>" /><%=str[0]%><%=str[1]%>
		     <input type="hidden" id="<%=str[0]%>" value="<%=str[0]+":"+str[2] %>"/><br>
		     <!-- <div id="div<=str[0]%>" style="display: none;">  -->	     	      
		     <img src="<%=rootPath%>/panel/view/image/<%=str[2] %>"  id="index<%=str[0]%>"  class="dragme" >
		     <!-- </div> -->
		     <a href="#" onclick="listpic(<%=str[0]%>)">选择图片</a>		     
		     </td><%}else if(x%6==1){%> 
		     <td>		     
		     <input type="checkbox" id="checkbox" name="checkb" value="<%=str[0]%>" /><%=str[0]%><%=str[1]%>
		     <input type="hidden" id="<%=str[0]%>" value="<%=str[0]+":"+str[2] %>"/><br>
		     		     	     	      
		     <img src="<%=rootPath%>/panel/view/image/<%=str[2] %>"  id="index<%=str[0]%>"  class="dragme" >
		     		     
		     <a href="#" onclick="listpic(<%=str[0]%>)">选择图片</a>
		     </td><%}else if(x%6==2){%> 
			 <td>
		     <input type="checkbox" id="checkbox" name="checkb" value="<%=str[0]%>" /><%=str[0]%><%=str[1]%>
		     <input type="hidden" id="<%=str[0]%>" value="<%=str[0]+":"+str[2] %>"/><br>
		     	     	      
		     <img src="<%=rootPath%>/panel/view/image/<%=str[2] %>"  id="index<%=str[0]%>"  class="dragme" >
		     
		     <a href="#" onclick="listpic(<%=str[0]%>)">选择图片</a>
		     </td><%}else if(x%6==3){%> 
			 <td>
		     <input type="checkbox" id="checkbox" name="checkb" value="<%=str[0]%>" /><%=str[0]%><%=str[1]%>
		     <input type="hidden" id="<%=str[0]%>" value="<%=str[0]+":"+str[2] %>"/><br>
		     	     	      
		     <img src="<%=rootPath%>/panel/view/image/<%=str[2] %>"  id="index<%=str[0]%>"  class="dragme" >
		     
		     <a href="#" onclick="listpic(<%=str[0]%>)">选择图片</a>
		     </td><%}else if(x%6==4){%> 
			 <td>
		     <input type="checkbox" id="checkbox" name="checkb" value="<%=str[0]%>" /><%=str[0]%><%=str[1]%>
		     <input type="hidden" id="<%=str[0]%>" value="<%=str[0]+":"+str[2] %>"/><br>
		     	     	      
		     <img src="<%=rootPath%>/panel/view/image/<%=str[2] %>"  id="index<%=str[0]%>"  class="dragme" >
		     
		     <a href="#" onclick="listpic(<%=str[0]%>)">选择图片</a>
		     </td><%}else if(x%6==5){%> 
			 <td>
		     <input type="checkbox" id="checkbox" name="checkb" value="<%=str[0]%>" /><%=str[0]%><%=str[1]%>
		     <input type="hidden" id="<%=str[0]%>" value="<%=str[0]+":"+str[2] %>"/><br>
		     	     	      
		     <img src="<%=rootPath%>/panel/view/image/<%=str[2] %>"  id="index<%=str[0]%>"  class="dragme" >
		     
		     <a href="#" onclick="listpic(<%=str[0]%>)">选择图片</a>		     
		     </td>
		     </tr>
		     <%}
			 }%>	
			 <tr>
			   <td colspan="6" align="center">
			     <input type="button" value="确定" onClick='confirmport()'>
			   </td>
			 </tr>	    
     </table>     
  </form>
  <div id="light" class="white_content">
      <table width="100%" height="100%">
          <tr>
            <!-- <td>
               <input type="radio" value="ma5200_port_el_up_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/ma5200_port_el_up_gray.gif" id="1" class="dragme"/>
            </td> -->
            <td>
               <input type="radio" value="port_100m_lc_down_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_100m_lc_down_gray.gif" id="2" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_100m_lc_left_up_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_100m_lc_left_up_gray.gif" id="3" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_100m_lc_right_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_100m_lc_right_gray.gif" id="4" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_100m_lc_up_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_100m_lc_up_gray.gif" id="5" class="dragme"/>
            <br></td>
          </tr>
          <tr>
            <td>
               <input type="radio" value="port_10000m_down_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_10000m_down_gray.gif" id="6" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_10000m_left_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_10000m_left_gray.gif" id="7" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_10000m_right_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_10000m_right_gray.gif" id="8" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_10000m_up_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_10000m_up_gray.gif" id="9" class="dragme"/>
            <br></td>            
          </tr>
          <tr>
            <td>
               <input type="radio" value="port_ethernet_down_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_ethernet_down_gray.gif" id="10" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_ethernet_left_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_ethernet_left_gray.gif" id="11" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_ethernet_right_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_ethernet_right_gray.gif" id="12" class="dragme"/>
            <br></td>
            <td>
               <input type="radio" value="port_ethernet_up_gray.gif" id="picradio" name="picradio"/>
               <img src="<%=rootPath%>/panel/view/image/port_ethernet_up_gray.gif" id="13" class="dragme"/>
            <br></td>            
          </tr>
          <tr>
            <td align="center" colspan="4"><input type="button" value="确定" onclick="confirmpic()" />
            <br></td>
          </tr>
      </table>
  </div>
  <div id="fade" class="black_overlay"></div>
  </body>
</html>