<%@ page language="java" contentType="text/html; charset=gb2312" pageEncoding="gb2312"%>
<%@page import="com.afunms.machine.dao.WallInfoDao"%>
<%@page import="com.afunms.machine.dao.PCInfoDao"%>
<%@page import="com.afunms.machine.dao.WiringInterfaceInfoDao"%>
<%@page import="com.afunms.machine.dao.JumperInfoDao"%>
<%@page import="com.afunms.machine.dao.SwitchPortInfoDao"%>
<%@page import="com.afunms.machine.model.WallInfo"%>
<%@page import="com.afunms.machine.model.PCInfo"%>
<%@page import="com.afunms.machine.model.WiringInterfaceInfo"%>
<%@page import="com.afunms.machine.model.JumperInfo"%>
<%@page import="com.afunms.machine.model.SwitchPortInfo"%>
 <%
    String rootPath = request.getContextPath();  
    String infoid = (String)request.getParameter("infoid");
    infoid = infoid.substring(3);
    System.out.println(infoid);
    String gallery = "";
    String galleryPanel = "";
    WallInfoDao wallInfoDao = new WallInfoDao();
    WallInfo vo = null;
    try{
        vo = (WallInfo)wallInfoDao.findByID("int_id",infoid);
    }catch(Exception e){
        e.printStackTrace();
    } finally{
        wallInfoDao.close();
    }
    PCInfoDao pCInfoDao = new PCInfoDao();
    PCInfo pCInfo = null;
    try{
        pCInfo = (PCInfo)pCInfoDao.findByID("related_id",infoid);
    }catch(Exception e){
        e.printStackTrace();
    } finally{
        pCInfoDao.close();
    }
    String path1="../../resource/image/topo/line_info.gif";
    String path = "../../resource/image/topo/info_1.gif";
    String type = "";
    String name = "Œ¥≈‰÷√";
    if(pCInfo!=null){
        type = pCInfo.getIpAddr();
        name = pCInfo.getName();
    }
    galleryPanel = galleryPanel +"<td align=\"center\">"+"<img width=60 height=60 src=\"" + path + "\"  alt=\"" + type + "\"/><br>"+name+"</td>";
    galleryPanel = galleryPanel +"<td align=\"center\">"+"<img width=168 height=19 src=\"" + path1 + "\"/></td>";
    
    path = "../../resource/image/topo/info_2.gif";
    type = vo.getInfoNo();
    name = vo.getInfoNo();
    
    galleryPanel = galleryPanel +"<td align=\"center\">"+"<img width=60 height=60 src=\"" + path + "\"  alt=\"" + type + "\"/><br>"+name+"</td>";
    galleryPanel = galleryPanel +"<td align=\"center\">"+"<img width=168 height=19 src=\"" + path1 + "\"/></td>";
    
    WiringInterfaceInfo wiringInterfaceInfo = null;
    path = "../../resource/image/topo/info_3.gif";
    name = "Œ¥≈‰÷√";
    if(vo.getRelated()!=null){
        WiringInterfaceInfoDao wiringInterfaceInfoDao = new WiringInterfaceInfoDao();
	    try{
	        wiringInterfaceInfo = (WiringInterfaceInfo)wiringInterfaceInfoDao.findByID("int_id",vo.getRelatedIntId()+"");
	    }catch(Exception e){
	        e.printStackTrace();
	    } finally{
	        wiringInterfaceInfoDao.close();
	    }
	    if(wiringInterfaceInfo!=null){
	        type = wiringInterfaceInfo.getWiringShelf()+"-"+wiringInterfaceInfo.getWiringRow()+"-"+wiringInterfaceInfo.getWiringNo();
	    	name = wiringInterfaceInfo.getWiringShelf()+"-"+wiringInterfaceInfo.getWiringRow()+"-"+wiringInterfaceInfo.getWiringNo();
	    }
    }
    galleryPanel = galleryPanel +"<td align=\"center\">"+"<img width=60 height=60 src=\"" + path + "\"  alt=\"" + type + "\"/><br>"+name+"</td>";
    galleryPanel = galleryPanel +"<td align=\"center\">"+"<img width=168 height=19 src=\"" + path1 + "\"/></td>";
    
    JumperInfo jumperInfo = null;
    if(wiringInterfaceInfo!=null&&wiringInterfaceInfo.getJumperId()!=0){
        JumperInfoDao jumperInfoDao = new JumperInfoDao();
	    try{
	        jumperInfo = (JumperInfo)jumperInfoDao.findByID("int_id",wiringInterfaceInfo.getJumperId()+"");
	    }catch(Exception e){
	        e.printStackTrace();
	    } finally{
	        jumperInfoDao.close();
	    }
    }
    path = "../../resource/image/topo/info_4.gif";
    type = "";
    name = "Œ¥≈‰÷√";
    SwitchPortInfo switchPortInfo = null;
    if(jumperInfo!=null&&jumperInfo.getDownId()!=0){
        SwitchPortInfoDao switchPortInfoDao = new SwitchPortInfoDao();
	    try{
	        switchPortInfo = (SwitchPortInfo)switchPortInfoDao.findByID("int_id",jumperInfo.getDownId()+"");
	    }catch(Exception e){
	        e.printStackTrace();
	    } finally{
	        switchPortInfoDao.close();
	    }
    }
    if(switchPortInfo != null){
        type = switchPortInfo.getSwitchName();
    	name = switchPortInfo.getIfDescr();
    }
    
    galleryPanel = galleryPanel +"<td align=\"center\">"+"<img width=60 height=60 src=\"" + path + "\"  alt=\"" + type + "\"/><br>"+name+"</td>";

    gallery = "<table width=\"100%\" align=\"center\"><tr>"
				+ galleryPanel
				+ "</tr></table>";
				
	System.out.println(gallery);
 %>
<html>
<META http-equiv="Content-Type" content="text/html; charset=gb2312" />
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<META HTTP-EQUIV="expires" CONTENT="0">
<head>
<base target="_self">
<link rel="stylesheet" href="<%=rootPath%>/resource/css/style.css" type="text/css">
<title></title>
</head>
<body>
<%=gallery%>
</body>
</html>