<!-- ���ܣ�ͷҳ�� -->
<%@page language="java" contentType="text/html;charset=GBK"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.model.Function"%>
<%@page import="com.afunms.system.util.CreateRoleFunctionTable"%>
<%@page import="com.afunms.common.util.CommonAppUtil"%>
<%@page import="com.afunms.common.util.ShareData"%>
<%
	String imgPath = "images";
	String skin = CommonAppUtil.getSkin();
	if(null != skin && !"".equals(skin) && !"null".equals(skin))imgPath = skin;

	String rootPath = request.getContextPath();
	User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
	if(user==null){
		response.sendRedirect("/common/error.jsp?errorcode=3003");
	}
   	List<Function> menuRoot = (List<Function>)request.getAttribute("menuRoot");
    System.out.printf(menuRoot.get(0).getFunc_desc());//�˵�����
   	if(null != menuRoot && menuRoot.size() > 0){
   		for(int i = 0;i<menuRoot.size();i++){
   			Function fun = (Function)menuRoot.get(i);
   			try{
   				fun.setImg_url(fun.getImg_url().replaceAll(".+/", imgPath + "/"));
   				//System.out.println("%%%%%%%%%%%%%%%%%%%%%"+fun.getImg_url().replaceAll(".+/", imgPath + "/"));
   			}catch(Exception e){
   				//e.printStackTrace();
   			}
   		}
   	}
   	List<Function> role_Function_list= (List<Function>)request.getAttribute("roleFunction");
   	String timer=ShareData.getControlVoice();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" />
<link rel="stylesheet" rev="stylesheet" id="skin" type="text/css"
	media="all" />
<title>IT��ά���ϵͳ</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	background-color: #ababab;
}
-->
</style>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
 
     </script>
<script type="text/JavaScript">
var xiao="<%=imgPath%>/css/top2.css";   //���ñ���axu_url800x600    ��ʽ��1.css
var da="<%=imgPath%>/css/top.css"; //���ñ���axu_url1024x768   ��ʽ��2.css
if(screen.width>1024) {
    document.getElementById("skin").href=da;   //�жϷֱ�����800x600����1.css
}else {
    document.getElementById("skin").href=xiao; //���� ����2.css
}

<!--
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
   showMenu(a[1]);
}
//-->
  function toHome()
  {
     parent.mainFrame.location = "<%=rootPath%>/user.do?action=home";
  }

  function doQuit()
  {
     if (confirm("�����Ҫ�˳���?"))
     {
         parent.location = "<%=rootPath%>/user.do?action=logout";
     }
  } 
  
  function setTimer(initFlag){
  var bigimg = document.getElementById("timer"); 
          CommonHelper.setTimerFlag(initFlag,{
				callback:function(data){
					if(data.flag==1){
					window.location.reload();
					}else if(data.flag==0){
					 bigimg.src="<%=imgPath%>/voice_dis2.gif";
					clearInterval(timerId);
					}else if(data.flag==2){
					setTimerImg();
					}
				}
			});
     	
         }
        
        function setTimerImg(){
       
        if(flag==1){
        var bigimg = document.getElementById("timer"); 
        bigimg.src="<%=imgPath%>/voice2.gif";
       
        }else{
        var bigimg = document.getElementById("timer"); 
        bigimg.src="<%=imgPath%>/voice_dis2.gif";
        }
        }
</script>

<script type="text/javascript">
	function initWindow(){
		//MM_preloadImages('<%=imgPath%>/menu01b.jpg','<%=imgPath%>/menu02b.jpg','<%=imgPath%>/menu03b.jpg','<%=imgPath%>/menu04b.jpg','<%=imgPath%>/menu05b.jpg','<%=imgPath%>/menu06b.jpg');
		//window.open('test.jsp','�������ƴ���','height=80,width=60,top=0,left=0,toolbar=no, menubar=no ,scrollbars=no, resizable=no, location=no,status=no');
	}

</script>

<script type="text/javascript">
	
	function menuHref(url , isCurrentWindow , width , height){
		
		if(isCurrentWindow == 0){
			parent.mainFrame.location = "<%=rootPath%>/" + url;
		}else{
			if(!width){
				width =window.screen.width;
			}
			if(!height){
				height =window.screen.height;
			}
			window.open("<%=rootPath%>/" + url,"fullScreenWindow", "toolbar=no,height="+height + ","+"width=" + width+ ",resizable=yes,scrollbars=no,"+"screenX=0,screenY=0");
		}
	}
</script>



<SCRIPT LANGUAGE="JScript">
	
	
	
	
// +------------------------
//  ��popup��ʵ�ֲ˵�
//  ����
// --------------------------



var pops = new Array();
function CreatePopup(degree)
{
    if (degree < 0)
        return null;
    if (pops[degree] != null)
        return pops[degree];

    if (degree == 0){
        pops[0] = window.createPopup();
    }
    else{
        if (pops[degree - 1] == null){
            pops[degree - 1] = CreatePopup(degree - 1);    //�ݹ�Ŷ
            }
        pops[degree] = pops[0].document.parentWindow.createPopup();
    }
    pops[degree].document.body.setAttribute("degree", degree);
    return pops[degree];
}

CreatePopup(1);


//Creating the popup window object
var oPopup = pops[0];
var timer = null;
var timerMenu = null;
var switchFlag = false;

//��ʾ�˵�
function showMenu(id)
{

    //alert("############"+id);
    //var lefter = event.offsetY+10;
    //var topper = event.offsetX+10;
    var elmt = event.srcElement; 
    var offsetTop = elmt.offsetTop; 
	var offsetLeft = elmt.offsetLeft; 
    var offsetWidth = elmt.offsetWidth; 
    var offsetHeight = elmt.offsetHeight; 
    while( elmt = elmt.offsetParent ) 
    { 
          // add this judge 
        if ( elmt.style.position == 'absolute' || elmt.style.position == 'relative'  
            || ( elmt.style.overflow != 'visible' && elmt.style.overflow != '' ) ) 
        { 
            break; 
        }  
        offsetTop += elmt.offsetTop; 
        offsetLeft += elmt.offsetLeft; 
    }
    
    var lefter = offsetTop + offsetHeight;
   	var topper = offsetLeft ;
    
    oPopup.document.body.innerHTML = document.getElementById(id).innerHTML; 
    //This popup is displayed relative to the body of the document.
   
    oPopup.show(topper, lefter, 150, parseInt(document.getElementById(id).style.height.replace("px" , ""))+1, document.body);
	pops[1].hide();
}
//��ʾ�Ӳ˵���id��ʾ�ϼ��˵���ID��j��ʾ�ڼ���
function ShowSubMenu(j , id)
{
    ClearTimer();
    pops[1].document.body.innerHTML = document.getElementById(id).innerHTML; 
    
    //This popup is displayed relative to the body of the document.
    //alert(document.getElementById(id).style.height.replace("px" , ""));
    pops[1].show( 150, j*30 , 150 , parseInt(document.getElementById(id).style.height.replace("px" , ""))+1, pops[0].document.body);
}

//�����Ӳ˵�
function HideSubMenu()
{
    ClearTimer();
    timer = window.setTimeout("pops[1].hide()", 3000);
}
//�����ʱ
function ClearTimer()
{
    if (timer != null)
        window.clearTimeout(timer)
    timer = null;
}

function ClearTimer2()
{
    if (timerMenu != null)
        window.clearTimeout(timerMenu)
    timerMenu = null;
}

function HideMenu()
{
    ClearTimer2();
    timerMenu = window.setTimeout("pops[0].hide()", 1000);
}

function ShowBlackArrow(id){
	document.getElementById(id).style.display = 'inline';
}






</SCRIPT>



</head>

<body style="background:#bec7ce;">
	<%
		System.out.println("=====================" + imgPath);

		Map map = new HashMap();
		CreateRoleFunctionTable crft = new CreateRoleFunctionTable();
		if (menuRoot != null && role_Function_list != null) {
			for (int i = 0; i < menuRoot.size(); i++) {
				Function perRootMenu = menuRoot.get(i);
				List secondFunctionList = crft.getFunctionChild(
						perRootMenu, role_Function_list);
				//System.out.println("------�˴���ӡ���ǵ���������----------"
				//		+ perRootMenu.getCh_desc() + perRootMenu.getId());
	%>
	<DIV ID="<%=perRootMenu.getId()%>"
		STYLE="clear:both;display:none;height: <%=secondFunctionList.size() * 30%>;"
		onmouseout="this.style.background='#e4e4e4';parent.HideMenu()">
		<DIV style="border: 1px solid #CECECE;">
			<%
				if (secondFunctionList != null
								&& secondFunctionList.size() > 0) {
							for (int j = 0; j < secondFunctionList.size(); j++) {
								Function perSecondFunction = (Function) secondFunctionList
										.get(j);
								//if(perSecondFunction.getId() == 10){
								//	continue;
								//}
								String perSecondFunctionOnClick = "";
								if (perSecondFunction.getUrl() != null
										&& perSecondFunction.getUrl().trim()
												.length() > 0) {
									perSecondFunctionOnClick = "ONCLICK=parent.menuHref('"
											+ perSecondFunction.getUrl()
											+ "','"
											+ perSecondFunction
													.getIsCurrentWindow()
											+ "','"
											+ perSecondFunction.getWidth()
											+ "','"
											+ perSecondFunction.getHeight() + "');";
									//							System.out.println(perSecondFunctionOnClick);
								}

								String showBlackArrow = "document.getElementById('per-"
										+ perSecondFunction.getId()
										+ "-black-arrow').style.display ='inline';";

								List thirdFunctionList = crft.getFunctionChild(
										perSecondFunction, role_Function_list);
								if (thirdFunctionList == null
										|| thirdFunctionList.size() == 0) {
									showBlackArrow = "";
								}
								//System.out
									//	.println("-----------�˴���ӡ��һ���˵�----------------"
										//		+ perSecondFunction.getCh_desc());
			%>
			<!-- ��ӡ��һ���˵���DIV -->
			<DIV id="per-<%=perSecondFunction.getId()%>"
				onmouseover="<%=showBlackArrow%>this.style.backgroundImage='url(<%=imgPath%>/menubg.jpg)';parent.ShowSubMenu('<%=j%>','<%=perSecondFunction.getId()%>');parent.ClearTimer2();"
				onmouseout="this.style.background='#F0F1F4';parent.HideSubMenu();parent.HideMenu();document.getElementById('per-<%=perSecondFunction.getId()%>-black-arrow').style.display ='none';"
				<%=perSecondFunctionOnClick%>
				STYLE="font-family:verdana; font-size:50%; height:30px;background:#F0F1F4;border-top: 1px solid #FAFAFA;border-bottom: 1px solid #CECECE; padding:4px; cursor:hand ">
				<!-- ��ӡ��һ���˵����Ƶ�SPAN -->
				<SPAN ONCLICK="" STYLE="font-family:verdana; font-size:12;">
					<%=perSecondFunction.getCh_desc()%></SPAN>
				<!-- ��ӡ��һ���˵���DIV�а����Ĵ�ӡ�ڼ�ͷ��DIV -->
				<DIV id="per-<%=perSecondFunction.getId()%>-black-arrow"
					align="right"
					style="display:none;position: absolute;left: 139px;vertical-align:middle;">
					<img src="<%=imgPath%>/black_arrow.gif">
				</DIV>
			</DIV>
			<%
				}
						}
			%>
		</DIV>
	</DIV>
	<%
		}
		}
	%>


	<%
		CreateRoleFunctionTable crft2 = new CreateRoleFunctionTable();
		if (menuRoot != null && role_Function_list != null) {
			for (int i = 0; i < menuRoot.size(); i++) {
				Function perRootMenu = menuRoot.get(i);
				List secondFunctionList = crft.getFunctionChild(
						perRootMenu, role_Function_list);
				if (secondFunctionList != null
						&& secondFunctionList.size() > 0) {
					for (int j = 0; j < secondFunctionList.size(); j++) {
						Function perSecondFunction = (Function) secondFunctionList
								.get(j);
						List thirdFunctionList = crft.getFunctionChild(
								perSecondFunction, role_Function_list);
	%>
	<DIV
		style="clear:both;display:none;border:1px solid blue;height:<%=thirdFunctionList.size() * 30%>;"
		ID="<%=perSecondFunction.getId()%>"
		onmouseover="parent.parent.ClearTimer2();">
		<DIV style="border: 1px solid #CECECE;">
			<%
				if (thirdFunctionList != null
										&& thirdFunctionList.size() > 0) {
									for (int k = 0; k < thirdFunctionList.size(); k++) {
										Function perThirdFunction = (Function) thirdFunctionList
												.get(k);
										int id = perThirdFunction.getId();
										String ch_desc = perThirdFunction
												.getCh_desc();
										String url = perThirdFunction.getUrl();
										int isCurrentWindow = perThirdFunction
												.getIsCurrentWindow();
										//System.out
											//	.println("------------�˴���ӡ�ڶ����˵�-------------"
												//		+ ch_desc);
			%>
			<!-- �˴���ӡ�ڶ����˵���DIV -->
			<DIV
				onmouseover="this.style.backgroundImage='url(<%=imgPath%>/menubg.jpg)';parent.parent.ClearTimer2();"
				onmouseout="this.style.background='#F0F1F4';parent.parent.HideMenu();"
				ONCLICK="parent.parent.menuHref('<%=url%>' , '<%=isCurrentWindow%>','<%=perThirdFunction.getWidth()%>','<%=perThirdFunction.getHeight()%>')"
				STYLE="font-family:verdana; font-size:50%; height:30px; background:#F0F1F4;border-top: 1px solid #FAFAFA;border-bottom: 1px solid #CECECE; padding:4px; cursor:hand ">
				<!-- �˴�SPAN��ӡ�ڶ����˵��ı������� -->
				<SPAN STYLE="font-family:verdana; font-size:12; "> <%=ch_desc%></SPAN>
			</DIV>
			<%
				}
								}
			%>
		</DIV>
	</DIV>
	<%
		}
				}
			}
		}
	%>
	<!-- ��Div��ӡ����ͷ��logo����ҳ���˳����������� -->
	<div class="top_bg">
		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			class="top_bg_map">
			<!-- ���д�ӡ������ҳ���˳��İ�ťͼ�� -->
			<tr>
				<td rowspan="2" class="logo_box"></td>
				<td align="right" class="icons_box"><a
					href="javascript:toHome();"><img src="<%=imgPath%>/home.gif"
						border="0" /></a><a href="javascript:doQuit();"><img
						src="<%=imgPath%>/quit.gif" border="0" /></a></td>
			</tr>
			<tr></tr>
			<tr>
				<td class="top_menu" style="align:center">
					<ul class="sf-menu">
						<%
							for (int i = 0; i < menuRoot.size(); i++) {
								String path = menuRoot.get(i).getImg_url().split("/")[0];
								if (menuRoot.get(i).getImg_url() == null
										|| menuRoot.get(i).getImg_url().equals("")) {
									path = menuRoot.get(0).getImg_url().split("/")[0];
								}
								if (menuRoot.get(i).getCh_desc().equals("��ҳ")) {
									map.put("��ҳ", "home.png");
								} else if (menuRoot.get(i).getCh_desc().equals("��Դ")) {
									map.put("��Դ", "ziyuan.png");
								} else if (menuRoot.get(i).getCh_desc().equals("����")) {
									map.put("����", "xingneng.png");
								} else if (menuRoot.get(i).getCh_desc().equals("Ӧ��")) {
									map.put("Ӧ��", "yingyong.png");
								} else if (menuRoot.get(i).getCh_desc().equals("�澯")) {
									map.put("�澯", "gaojing.png");
								} else if (menuRoot.get(i).getCh_desc().equals("����")) {
									map.put("����", "baobiao.png");
								} else if (menuRoot.get(i).getCh_desc().equals("3D����")) {
									map.put("3D����", "3D.png");
								} else if (menuRoot.get(i).getCh_desc().equals("�Զ���")) {

									map.put("�Զ���", "zidonghua.png");

								} else if (menuRoot.get(i).getCh_desc().equals("��������")) {
									map.put("��������", "fuwu.png");
								} else if (menuRoot.get(i).getCh_desc().equals("ϵͳ����")) {
									map.put("ϵͳ����", "xitong.png");
								} else if (menuRoot.get(i).getCh_desc().equals("IP����")) {
									map.put("IP����", "IP.png");
								} else if (menuRoot.get(i).getCh_desc().equals("���̹���")) {
									map.put("���̹���", "liucheng.png");
								}
						%>
						<!-- �˴���ӡ���ǵ������ı���˵� -->
						<li onmouseout="MM_swapImgRestore()"
							onmouseover="MM_swapImage('<%=menuRoot.get(i).getCh_desc()%>','<%=menuRoot.get(i).getId()%>','<%=menuRoot.get(i).getImg_url()%>b.jpg',1)"><a
							href="<%=rootPath%>/<%=menuRoot.get(i).getUrl()%>"
							target=mainFrame> <img
								src="<%=rootPath%>/common/<%=path%>/<%=map.get(menuRoot.get(i).getCh_desc())%>"
								border="0" /><%=menuRoot.get(i).getCh_desc()%></a></li>
						<%
							//  System.out.println(menuRoot.get(i).getCh_desc()+"-----------"+menuRoot.get(i).getImg_url()+"b.jpg"+"-------------"+rootPath+"/common/"+path+"/home-blue.png");
								if (i != menuRoot.size() - 1) {
						%>
						<li class="divider"></li>
						<%
							}
							}
						%>
					</ul>
				</td>
			</tr>
		</table>

	</div>
</html>
