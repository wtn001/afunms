<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    
  

	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'addAplay.jsp' starting page</title>
    <link type="text/css" rel="stylesheet" href="<%=basePath%>/ipconfig/ipselect/reset.css" />
<link type="text/css" rel="stylesheet" href="<%=basePath%>/ipconfig/ipselect/apply.css" />
	

  </head>
  
  <body>

<div class="messagesBox">
    <div class="login_box fl right_10" align=center>
<h2 class="title_text"><span><img src="images/login_icon.jpg" /></span> <span>调度数据网络接入申请表</span></h2>
<form id="applyForm" name="applyForm" method="post" action="${ctx}/sendApply.action" >

<div>
     <p class="fl">场站名:
        <label><input name="name" type="text" class="keywords" id="name" maxlength="50" value="" /> </label>
	  </p>
	  <p class="fl">&nbsp;&nbsp;&nbsp;&nbsp;接入骨干点1:
        <label> <select name="ids1"   id="ids1">
        <c:forEach items="${myMap}" var="hashmap" varStatus="status">   
     
               <option  value="${hashmap.key}" >${hashmap.value}</option>   
  
                </c:forEach > 
                </select>
         </label>
      </p>
      <p class="fl">&nbsp;&nbsp;&nbsp;&nbsp;接入骨干点2:
        <label> 
             <select name="ids2"  id="ids2" >
             <c:forEach items="${myMap}" var="hashmap" varStatus="status">   
     
               <option  value="${hashmap.key}" >${hashmap.value}</option>   
  
                </c:forEach > 
             
             </select>
         </label>
      </p>
      
</div>
<div>

<table  width="100%" class="table">
       <tr>
       <td colspan=2> <hr size=1 ></hr></td>
       </tr>
       <tr>
          <td style="padding-left:0px;padding-bottom:15px;" width="50%">路由器</td>
          <td  style="padding-left:0px;padding-bottom:15px;" width="50%">交换机</td>
       </tr>
        <tr>
          <td style="align:center" width="50%"> 
             <p class="fl">厂家:
               <label><input name="routeName" type="text" class="keywords" id="routeName" maxlength="50" value="" /> </label>
	         </p>
	      </td>
          <td  style="align:center" width="50%">
             <p class="fl">厂家:
                <label> <input name="switchName" type="text" class="keywords" id="switchName" maxlength="50" value="" /> </label>
             </p>
          </td>
       </tr>
       <tr>
          <td style="align:center" width="50%"> 
             <p class="fl">型号:
               <label><input name="routeType" type="text" class="keywords" id="routeType" maxlength="50" value="" /> </label>
	         </p>
	      </td>
          <td  style="align:center" width="50%">
             <p class="fl">型号:
                <label> <input name="switchType" type="text" class="keywords" id="switchType" maxlength="50" value="" /> </label>
             </p>
          </td>
       </tr>
        <tr>
          <td style="align:center" width="50%"> 
             <p class="fl">台数:
               <label><input name="routeCount" type="text" class="keywords" id="routeCount" maxlength="50" value="" /> </label>
	         </p>
	      </td>
          <td  style="align:center" width="50%">
             <p class="fl">台数:
                <label> <input name="switchCount" type="text" class="keywords" id="switchCount" maxlength="50" value="" /> </label>
             </p>
          </td>
       </tr>
       <tr>
       <td colspan=2><hr size=1 ></hr></td>
       </tr>
       
       <tr>
          <td  colspan="2"> 
             <p class="fl">有无其他设备:
               
	         </p>
             <p class="fl">
                <label>
                 &nbsp;&nbsp;<select name="deviceType"   id="deviceType"  >
                        <option value="jiami"> 加密机 </option>
                        <option value="other"> 其他 </option>
                 </select>
                 </label>
                  <label>
                 <select name="deviceCount"  id="deviceCount"  >
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                        <option value="6">6</option>
                        <option value="7">7</option>
                        <option value="8">8</option>
                        <option value="9">9</option>
                        
                 </select>
                 </label>
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">有无网络机柜:
               
	         </p>
             <p class="fl">
                <label><select name="hasCabinet"  id="hasCabinet"  >
                        <option value="0">无</option>
                        <option value="1" selected>有</option>
                       
                        
                 </select> </label>
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">网络安装地点:
               
	         </p>
             <p class="fl">
                <label> <input name="location" type="text" class="other" id="location" maxlength="50" value="" /> </label>
             </p>
          </td>
       </tr>
       <tr>
          <td  colspan="2"> 
             <p class="fl">施工调试单位:
               
	         </p>
             <p class="fl">
                <label> <input name="debugUnit" type="text" class="other" id="debugUnit" maxlength="50" value="" /> </label>
             </p>
          </td>
       </tr>
      
</table>
    
	  
      

</div>

<div class="center"><label> 
    <input style="margin-right: 8px;" type="image" src="images/login_in.jpg" id="reg"  /> 
	<input type="image" src="images/reset.jpg" name="reset"
	id="reset" value="重置" onclick="javascript:document.forms['applyForm'].reset(); return false;"/>
	</label>
	</div>
	</form>
	</div>
	</div>
  </body>
</html>

</html>
