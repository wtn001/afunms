<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.List"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.machine.dao.StoreyInfoDao"%>
<%@page import="com.afunms.machine.model.StoreyInfo"%>
<%@page import="com.afunms.system.model.User"%>
<%
    String rootPath = request.getContextPath();  
    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");    
    String menu = request.getParameter("menu");
    if(menu!=null){
       session.setAttribute(SessionConstant.CURRENT_MENU,menu); 
    }     
    String xmlname = request.getParameter("submapXml");
    User current_user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
    String bids[] = current_user.getBusinessids().split(",");
    String xml_current="";
    int tt=0;                 
    StoreyInfoDao dao = new StoreyInfoDao();    
	List list = dao.loadAll();
	//System.out.println("list.size()======"+list.size());
	for(int i=0; i<list.size(); i++)
	{
		StoreyInfo vo = (StoreyInfo)list.get(i);
		int tag = 0;
		if(bids!=null&&bids.length>0){
		    for(int j=0;j<bids.length;j++){
		        if(vo.getBid()!=null&&!"".equals(vo.getBid())&&!"".equals(bids[j])&&vo.getBid().indexOf(bids[j])!=-1){
		            tag++;
		        }
		    }
		}
		xml_current=vo.getXml();
		if(tag>0){
		    ++tt;
		    session.setAttribute(SessionConstant.CURRENT_FLOOR_VIEW,xml_current); 
		    break;
		} 
	} 
	if(xmlname!=null){xml_current=xmlname;session.setAttribute(SessionConstant.CURRENT_FLOOR_VIEW,xml_current); }
	if(tt==0){
	    response.sendRedirect(request.getContextPath() + "/topology/infomanage/blank.jsp");
	} else {
	    session.setAttribute("fatherXML",xml_current);    //yangjun add
	}
	//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
%> 
<html>
<head>
<title>墙面信息点视图</title>
<LINK href="<%=rootPath%>/resource/css/style.css" type="text/css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
</head>     
<frameset name=search cols="0,*" frameborder="NO" border="0" framespacing="0" rows="*">
    <frame name="leftFrame" src="tree.jsp?treeflag=0">
    <frame name="mainFrame" src="submap.jsp">
  </frameset>
</html>
