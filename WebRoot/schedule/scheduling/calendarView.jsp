<%@page language="java" contentType="text/html;charset=GB2312"%>
<%@page import="java.util.*"%>
<%@page import="com.afunms.system.model.UserTaskLog"%>
<%@page import="com.afunms.common.util.SessionConstant"%>
<%@page import="com.afunms.system.model.User"%>
<%@page import="com.afunms.schedule.model.*"%>
<%@page import="java.text.SimpleDateFormat;"%>
<%@include file="/include/globe.inc"%>
<%
  	String rootPath = request.getContextPath(); 
  	String menuTable = (String)request.getAttribute("menuTable");
  	User user = (User)session.getAttribute(SessionConstant.CURRENT_USER);
  	
  	List<Schedule> scheduleList = (List<Schedule>)request.getAttribute("scheduleList");
	Map<Integer,User> userMap = (HashMap<Integer,User>)request.getAttribute("userMap");
	Map<String,Period> periodMap = (HashMap<String,Period>)request.getAttribute("periodMap");
	Map<String,Position> positionMap = (HashMap<String,Position>)request.getAttribute("positionMap");
	String username = "",strPeriod = "", strPosition = "";
%>

<html>
<head>
<title>E2CS 0.0.14 - Basic Sample</title>
<link rel="shortcut icon" href="<%=rootPath%>/system/usertasklog/resource/images/e2cs_16x16.png">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/system/usertasklog/resource/ext2/resources/css/ext-all.css">
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/system/usertasklog/resource/ext2/resources/css/core.css">
<script type="text/javascript" src="<%=rootPath %>/system/usertasklog/resource/ext2/ext-base.js"></script>
<script type="text/javascript" src="<%=rootPath %>/system/usertasklog/resource/ext2/ext-all-debug.js"></script>
<link id="css" rel="stylesheet" type="text/css" href="<%=rootPath %>/system/usertasklog/resource/calendar.css">
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">


<script language="JavaScript" type="text/javascript" src="<%=rootPath%>/include/navbar.js"></script>
<link href="<%=rootPath%>/resource/<%=com.afunms.common.util.CommonAppUtil.getSkinPath() %>css/global/global.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath%>/include/mainstyle.css" rel="stylesheet" type="text/css">

<style type="text/css">
<!--
/*Custom CSS for the sview.blankHTML porperty */
.custom_image_addNewEvent_scheduler{cursor:pointer;	padding-top: 15px;	padding-right: 5px;	padding-bottom: 5px;	padding-left: 5px;}
.custom_text_addNewEvent_scheduler{font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 14px; font-weight: bold;	color: #FFFFFF;	text-decoration: none;	text-align: center;	padding: 3px;}
.test_taskovercss{ background-image:url(<%=rootPath%>/system/usertasklog/resource/images/task_over_001.png); background-repeat:repeat-y; color:#FFFFFF;} 
.test_taskovercss_b{ background-color:#999999;color:#FFFFFF;background-image: url(<%=rootPath%>/system/usertasklog/resource/images/__header_background_001.png);	background-repeat: repeat-y;} 
.test_taskovercss_sched{ background-image:url(<%=rootPath%>/system/usertasklog/resource/images/header_background_002.png); background-repeat:repeat-x; color:#FFFFFF;}
.test_taskovercss_sched_b{ background-image:url(<%=rootPath%>/system/usertasklog/resource/images/header_background_001.png); background-repeat:repeat-x; color:#FFFFFF;}
.holiday{height:16px; background-image: url(<%=rootPath%>/system/usertasklog/resource/imgs_test/cake.png);	background-repeat: no-repeat;background-position: left 2px;	text-indent: 18px;}
-->
</style>
<script type="text/javascript" src="<%=rootPath %>/schedule/scheduling/resource/locale/e2cs_zh_CN.js"></script>
<script type="text/javascript" src="<%=rootPath %>/schedule/scheduling/resource/e2cs_pack.js"></script>


<script type="text/javascript">

function test_calendar(obj){
	Ext.QuickTips.init();  
	buttonx1= new Ext.menu.Item({ id: 'buttonx1_task', iconCls:'x-calendar-month-btnmv_task',	text: "Custom menu test 1" });
	buttonx2= new Ext.menu.Item({ id: 'buttonx2_task',iconCls:'x-calendar-month-btnmv_task',	text: "Custom menu test 2" });
	buttonz1= new Ext.menu.Item({ id: 'buttonz1_task', iconCls:'x-calendar-month-btnmv_task',	text: "Custom action 1" });
	buttonz2= new Ext.menu.Item({ id: 'buttonz2_task',iconCls:'x-calendar-month-btnmv_task',	text: "Custom action 2" });
	boton_daytimertask  = new Ext.menu.Item({ id: 'btnTimerTask', iconCls:'task_time', text: "Set Task Alarm...."  });
	boton_daytimertaskb = new Ext.menu.Item({ id: 'btnTimerOff' , iconCls:'task_time_off', text: "Delete Task's Alarm...."  });	
	button_sched_1= new Ext.menu.Item({ id: 'buttonx1_task',iconCls:'x-calendar-month-btnmv_task',text: "Custom menu  on sched test 1" });
	button_sched_2= new Ext.menu.Item({ id: 'buttonx2_task',iconCls:'x-calendar-month-btnmv_task',text: "Custom menu  on sched test 2" });
/*
	var reader = new Ext.data.ArrayReader({id:0}, [	
	   {name: 'username' 	, type: 'string'},
       {name: 'date'		, type: 'string'},  
       {name:'position'	    , type: 'string'},
       {name:'period'		, type: 'string'}, 
       {name:'content'  	, type: 'string'},
       {name: 'color'		, type: 'string'}
    ]);
*/
	var reader = new Ext.data.ArrayReader({id:0}, [	
	   {name: 'id' 		, type: 'string'},
       {name: 'userid'		, type: 'string'},  
       {name:'username'	, type: 'string'},
       {name:'content'  	, type: 'string'},
       {name:'date'		, type: 'string'}, 
       {name: 'color'		, type: 'string'}
    ]);
    var dummyData = new Array();
    <%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String strDate = null;
    String log = null;
    for(int i = 0; i < scheduleList.size(); i++){ 
    	Schedule schedule = scheduleList.get(i);
    	username = userMap.get(schedule.getWatcher()).getName();
    	strPosition = positionMap.get(schedule.getPosition()).getName();
    	strPeriod = periodMap.get(schedule.getPeriod()).getName();
    	log = schedule.getLog()==null?"":schedule.getLog();
    	strDate = sdf.format(schedule.getOn_date());
    %>
    /*
    dummyData[<%=i%>] = ['<%=username%>',
    		'<%=strDate%>',
    		'<%=strPosition%>',
    		'<%=strPeriod %>',
    		'<%=log %>',
    		'greenyellow'
    		];

    */
    dummyData[<%=i%>] = ['<%=i%>',
    		'',
    		'<%=username%>',
    		'<%=strPosition + " " + strPeriod + " " + log %>',
    		'<%=strDate %>',
    		'greenyellow'
    		];
    	<%
    }%>
   	
    var calendarstoretest= new Ext.data.Store({reader:reader,data:dummyData });
	prueba = new Ext.ECalendar({
		id: 'test_calx', 
		name: 'test_calx',
		header: true,
		title: '值班表--台历视图',
		mytitle: ' ',	
		height:560, 
		width:1000,
		fieldsRefer:{ //0.0.11 
		/*
			username:'username',test_calx-mtask-item-2013-02-21-192
			date:'date',
			position:'position', 
			period:'period',
			content:'content',
			color:'color'
			*/
			
			id:'id',
			userid:'userid',
			username:'username', 
			content:'content',
			date:'date',
			color:'color'			
		},
		storeOrderby:'username', 	//0.0.11 
		storeOrder:'DESC',		//0.0.11 
		showCal_tbar: true, 
		showRefreshbtn:true,
		currentView: 'month',
		currentdate: new Date(),
		dateSelector: true,
		dateSelectorIcon: '<%=rootPath%>/system/usertasklog/resource/images/date.png',
		dateSelectorIconCls: 'x-cmscalendar-icon-date-selector',
		dateformat :'Y-m-d',
		iconCls: 'x-cmscalendar-icon-main',
		dateSelector:true,
		store:calendarstoretest, 
		monitorBrowserResize:true, 
		widgetsBind: {bindMonth:null,bindDay:null,binWeek:null},
		tplTaskZoom: new Ext.XTemplate( 
		'<tpl for=".">',
			'<div class="ecal-show-basetasktpl-div"><b>Title:</b>{title}<br>',
			'<br><b>Username:</b>{username}',
			'<br><b>Date:</b>{date}',
			'<br><b>Content:</b><div><hr><div>{content}<div><hr>',
		'</tpl>'
		),
		iconToday:'<%=rootPath%>/system/usertasklog/resource/images/cms_calendar.png',
		iconMonthView:'<%=rootPath%>/system/usertasklog/resource/images/calendar_view_month.png',
		iconListView:'<%=rootPath%>/system/usertasklog/resource/images/calendar_view_week.png',
		loadMask:false, //0.0.12  we dont need this saple here cause all the data is loaded its suits better  for Serverside loading 
		customMaskText:'E2CS DEMO<br>Wait a moment please...!<br>Processing Information for calendar', //0.0.12 		
		//-------- NEW on 0.0.10 -------------------
		
		//-------------------------------------------
		mview:{
			header: true,
			headerFormat:'Y-F',
			headerButtons: true,
			dayAction:'viewday',    //dayAction: //viewday , event, window
			//moreMenuItems:[buttonx1,buttonx2],
			showTaskcount: false,
			startDay:0,
			taskStyle:'margin-top:2px;', //Css style for text in day(if it has tasks and showtaskcount:true)
			showTaskList: true,
			showNumTasks:10,
			TaskList_launchEventOn:'click', //0.0.11 
			TaskList_tplqTip: new Ext.XTemplate( 
			'<tpl for=".">{usernamexl}{usernameval}<br>{datexl}{dateval}<hr color=\'#003366\' noshade>{content}</tpl>' ), //0.0.11 
			ShowMenuItems:[1,1,1,1,1,1],  //0.0.11  - ADD, nextmonth, prevmonth, chg Week , CHG Day, chg Sched,	
			//TaskList_moreMenuItems:[buttonz1,buttonz2], 	  //0.0.11
			TaskList_ShowMenuItems:[1,1,1]//0.0.11 	- Add, DELETE, EDIT 	
		}

	});	
	//scheduler only event on this object

	//dayClick only event on this object  
	prueba.viewmonth.on({		
		'dayClick':{
				fn: function(datex, mviewx, calx) { 
					alert ("dayclick event for " + datex);
				},
				scope:this
		},
		'beforeMonthChange':{
				fn: function(currentdate,newdate) { 
					//alert ("gonna change month to " + newdate.format('m/Y') + ' Actual date=' + currentdate.format('m/Y') );
					return false; 
				},
				scope:this
		},
		'afterMonthChange':{
				fn: function(newdate) { 
					//alert ("Month changed to " + newdate.format('m/Y') ) ;
				},
				scope:this
		}
	});

	prueba.on({
		'beforeContextMenuTask': {
				fn: function(refview,datatask,showItems,myactions) { 
						 return false; 
				}
		},	
		'beforeChangeDate': {
			fn: function( newdate , calobj){
				//return true; 
			} 		
		},
		'afterChangeDate':{
			fn: function( newdate , calobj){
				//alert ("Date changed to:" + newdate.format('d-m-Y'));
			}
		},
		
		'onChangeView':{
			fn: function(newView, oldView, calobj){ 
				Ext.get("samplebox_cview").update("<b>Current View:</b> " + newView); 
			},scope: this
		},
		
		'beforeChangeView':{
				fn: function (newView,OldView,calendarOBJ){
					if (newView==OldView){ 
						return true; 
					} 
					var mainForm = document.getElementById("mainForm");
//					mainForm.action = "<%=rootPath%>/userTaskLog.do?action=listType&jp=1";
					mainForm.action = "<%=rootPath%>/schedule.do?action=list&jp=1";
					mainForm.submit();
					return r;
				},scope:this
		},
		'beforeTaskAdd':{
				fn: function( datex, msg ) { 
					var date = new Date(datex);
					var massage = date.format('Y-m-d');
					massage += '  ';
					massage += msg;
					alert(massage);
					return false;
				}
		},
		
		'taskDblClick':{
				fn: function (task,dxview,calendar,refviewname){
					var datatest='<div width="200" style="word-wrap:break-word;word-break:break-all;">';
					//datatest+= '日   志 : ' + task[0] + '<br>';
					//datatest+= '日志id : '  + task[1]+ '<br>';
					datatest+= '值班人 : '    + task[2] + '<br>';
 					datatest+= '日  期 : '   + task[3] + '<br>';
 					datatest+= '地  点 : '   + task[4].split(" ")[0] + '<br>';
 					datatest+= '班  次 : '   + task[4].split(" ")[1] + '<br>';
 					datatest+= '值班日志 :'  +  '<br>';
 					datatest+= '       ' + task[4].split(" ")[2] + '<br>';
 					datatest+='</div>';
 					Ext.Msg.alert('值班日志' , datatest);	
					
				},
				scope:this 
		},
		'beforeTaskDelete': {
				fn: function (datatask,dxview) { 
					return false; 
					// do your stuff to check if the event/task could be deleted 
				}, scope:this
		},
		'onTaskDelete':{
			fn:function(datatask){
				// 20091204 edit
				// var r=confirm("Delete event " + datatask[1] + " " + datatask[2] + "...? YES/NO" );
				var r=confirm("删除 日志 : " + datatask[1] + "  日期 : " + datatask[3] );
				return r; 
				// do your stuf for deletion and return the value 
			},scope:this
		},
	   'afterTaskDelete':{
	   		fn: function(datatask,action){
	   			// 20091204 edit
				// action ? alert("Event: " + datatask[1] + " " + datatask[2] + " Deleted"): alert("Event Delete was canceled..!"); 
				// perform any action after deleting the event/task
				if(action){
				    var mainForm = document.getElementById("mainForm");
				    var id = document.getElementById("id");
				    id.value = datatask[1];
					mainForm.action = "<%=rootPath%>/userTaskLog.do?action=delete";
					mainForm.submit();
				}
			},scope:this
 	    },
		'beforeTaskEdit': {
				fn: function (datatask,dxview) { 
					return false; 							
				}, scope:this
		},	
	   'onTaskEdit':{
			fn:function(datatask){
				var r=confirm("编辑 日志 :" + datatask[1] + "  日期 :　" + datatask[3] );
				return r; 
				// do your stuf for editing and return the value
			},scope:this
	    },
	    'afterTaskEdit':{
	   		fn: function(datatask,action){
				// perform any action after deleting the event/task
				if (action){ 
					var form=new Ext.form.FormPanel({
					labelWidth:60,
					labelAlign:"right",
					frame:true,
					defaults:{xtype:"textfield",width:250,height:150},
					items:[
					{xtype:"textarea",name:"contentxl",fieldLabel:"日志内容",id:"contentxl",value:datatask[4]}
					],
					html:'<font color="red" >字数小于1000字<font>',
					buttons:[{text:"提交",
					handler:function(){
						var mainForm = document.getElementById("mainForm");	
						var content = document.getElementById("content");
						var date = document.getElementById("date");
						var id = document.getElementById("id");
						content.value = document.getElementById("contentxl").value;
						if(content.value.length>1000){
							alert("字数: " + content.value.length + "! 请不要超过 1000 字");
							return false;
						}
						date.value = datatask[3];
						id.value = datatask[1];
						mainForm.action = "<%=rootPath%>/userTaskLog.do?action=edit";
						mainForm.submit({
							waitTitle:"请稍候",
							waitMsg:"正在提交表单数据，请稍候。。。。。。"
							});
						
						}},{text:"重置",
							handler:function(){			
								form.form.reset();
							}
							}]
									
					});
					var win=new Ext.Window({title:"编辑日志",
 							width:400,
 							height:300,
 							modal:true,
 							items:form,
 							maximizable:true}); 
 					win.show();
				} 
				return false; 
			},scope:this
	    },
		'beforeTaskMove':{
				fn: function (datatask,Taskobj,dxview,TaskEl) { // return "true" to cancel or "false" to go on 
					return false; 	
				}, scope:this
		},
		'TaskMoved':{
				fn: function (newDataTask,Taskobj,dxview,TaskEl) {   // do some stuff 
					var test=21;  // use breakpoint in firefox here 
					task = newDataTask; 
					datatest ='Task id:'  + task[0] + ' ' + task[2] + '<br>';
					datatest+='recid:'    + task[1] + '<br>';
					datatest+='starts:'    + task[3] + '<br>';
					datatest+='ends:'   + task[4] + '<br>';
					datatest+='contents:' + task[5] + '<br>';	
					datatest+='index:'    + task[6] + '<br>';	
					Ext.Msg.alert('Information Modified task', datatest);	
				}, scope:this
		},
		'customMenuAction':{
				fn: function (MenuId, Currentview,datatask,objEl,dxview){
					var datatest = ''; 
					if (Currentview=='month'){ 
						task = datatask; 
						datatest ='Element ID :'  + task[0] + '<br>';
						datatest+='Task ID :'  + task[1] + '<br>';
						datatest+='Menu ID :'  + MenuId + '<br>';
						Ext.Msg.alert('(Month) Information- ' + Currentview, datatest);	
					} else if (Currentview=='day'){ 
						task = datatask; 
						datatest ='Task id:'  + task[0] + ' ' + task[1] + '<br>';
						datatest+='starts:'    + task[2] + '<br>';
 						datatest+='Ends:'   + task[3] + '<br>';
 						datatest+='contents:' + task[4] + '<br>';	
						datatest+='index:'    + task[5] + '<br>';	
						datatest+='Test Menu:'  + MenuId + '<br>';	
						Ext.Msg.alert('(Day) Task information' + Currentview, datatest);			
					} else if (Currentview=='week'){
						task = datatask; 
						datatest ='Task el-id:'  + task[0] + '  task id:' + task[1] + '<br>';
						datatest+='Subject:'     + task[2] + '<br>';
 						datatest+='Starts:'   + task[3] + '<br>';
 						datatest+='Ends:' + task[4] + '<br>';	
						datatest+='index:'    + task[6] + '<br>';	
						datatest+='Test Menu:'  + MenuId + '<br>';	
						Ext.Msg.alert('(Week) Task information' + Currentview, datatest);
					} else if (Currentview=='scheduler'){
						task = datatask; 
						datatest ='Task id:'  + task[0] + ':' + task[2] + '<br>';
						datatest+='starts:'    + task[3] + '<br>';
 						datatest+='Ends:'   + task[4] + '<br>';
 						datatest+='contents:' + task[5] + '<br>';	
						datatest+='index:'    + task[6] + '<br>';	
						datatest+='Test Menu:'  + MenuId + '<br>';	
						Ext.Msg.alert('(Scheduler) Task information' + Currentview, datatest);			
					} 
				},scope:this		
		}

	});
	prueba.render('calendar');
	
	Ext.EventManager.onWindowResize( function(){  prueba.refreshCalendarView();  });
	initmenu();
}
</script>
<script language="JavaScript" type="text/JavaScript">
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
}

</script>
<style type="text/css">
body{margin-left: 10px;	margin-top: 10px;	margin-right: 10px;	margin-bottom: 10px;}
.maintitle {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 18px;color: #003366;}
.mainsubtitle {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 14px;color: #003366;}
.main_notes{	font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 9px;}
.task_time {background-image: url(<%=rootPath %>/system/usertasklog/resource/imgs/clock.png);}
.task_time_off {background-image:url(<%=rootPath %>/system/usertasklog/resource/imgs/clock_red.png);}
#samplebox #sampleboxtitle {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 14px;color: #FFFFFF;	background-color: #003366;padding: 3px;}
#samplebox {font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 11px;}
#samplebox .textsample{	font-family: Verdana, Arial, Helvetica, sans-serif;	font-size: 10px;}
.settings_monthview {font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 14px;color: #FFFFFF;	background-color: #006633;padding:4px;font-weight: bold; cursor:pointer;}
.settings_overview  {font-family: Verdana, Arial, Helvetica, sans-serif;font-size: 14px;color: #FFFFFF; background-color: #006699;padding:4px;font-weight: bold;cursor:pointer;}
.style1 {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 9px; font-weight: bold; }
</style>
</head>
<body onLoad="test_calendar();">
	<form method="post" name="mainForm" id="mainForm">
		<input type="hidden" name="content" id="content">
		<input type="hidden" name="date" id="date">
		<input type="hidden" name="id" id="id">
		<table width="100%" border="0" align="center">
			<tr>
				<td width="200px" valign=top align=center>
					<%=menuTable%>
				</td>
				<td width="900" height= "" valign="top">
					<!-- 
					<h2>标题栏<input type="button" value="print" onclick="print();"> </h2>
					 -->
					<div id="calendar"></div>
					<div id="pruebapanel"></div>
					<hr color="#003366" noshade width="1000">
					<!-- 
					<p>&nbsp;</p>
					<div id="samplebox">
					    <div id="sampleboxtitle">Sample BOX</div>
						<div id="newdateselectorsample"></div> 
					    <hr color="#003366" noshade>
					    <div id="samplebox_cview"><b>Current View</b>: Month</div>
					  	<p>&nbsp;</p>
					    <div>	
					<div >
					
					                <table width="77%" border="0" align="center">
					                  <tr class="textsample">
					                    <td colspan="2" ><div id="calendar_hdr_samples" class="settings_monthview">Calendar</div></td>
					                  </tr>
					                  <tr class="textsample">
					                    <td colspan="2" >
					                      <div align="center">
					                        <input type="button" name="buttondates1" id="buttondates1" value="Year" 		onClick="testfechas(2);" />
					                        <input type="button" name="buttondates2" id="buttondates2" value="Month"      	onClick="testfechas(1);" />
					                        <input type="button" name="buttondates3" id="buttondates3" value="Day" 			onClick="testfechas(3);" />
					                        <input type="button" name="buttondates4" id="buttondates4" value="Week Day"   	onClick="testfechas(4);" />
					                        <input type="button" name="buttondates5" id="buttondates5" value="Week Day 2" 	onClick="testfechas(5);" />  
					                        <input type="button" name="buttondates6" id="buttondates6" value="Working Date" onClick="testfechas(6);" />                   
					                      </div></td>
					                  </tr>
					                  <tr class="textsample">
					                    <td colspan="2"><div id="calendar_hdr_monthx" class="settings_monthview">Month view</div></td>
					                  </tr>
					                  <tr class="textsample monthsample">
					                <td width="47%" ><b>Month - showtask count on each day : </b></td>
					                <td width="53%">
					                <form name="formshowtaskcount" id="formshowtaskcount">
					                <label><input name="showtaskcountval" type="radio"  value="1" id="testsvc1" >Yes</label>
					                <label><input name="showtaskcountval" type="radio"  value="0" id="testsvc2" checked>No</label>
					                &nbsp;&nbsp;<input type="button" name="button4" id="button4" value="Refresh Calendar" onClick="samplemonth(1);">
					                </form></td></tr>
					                  <tr class="textsample monthsample">
					                    <td ><b>Month - show task List on each day : </b>&nbsp;</td>
					                    <td>
					                    <form name="formshowtasklist" id="formshowtasklist"># of tasks
					                    <input name="numlistingtasks" type="text" id="numlistingtasks" value="5" size="6" maxlength="3">
					                    <label><input name="showtaskcountval" type="radio"  value="1" id="testlist_tasks1" checked>Yes</label>
					                    <label><input name="showtaskcountval" type="radio"  value="0" id="testlist_tasks2" >No</label>
					                    &nbsp;&nbsp;<input type="button" name="button4" id="button4" value="Refresh Calendar" 
					                    onClick="samplemonth(4);">
					                    </form></td>
					                  </tr>
					                  <tr class="textsample monthsample">
					                    <td ><b>Month  - Select action to Perfom on day click</b> </td>
					                    <td><form name="form1" method="post" action="">
					                      <select name="selectmonthaction" id="selectmonthaction">
					                        <option value="viewday">Change to  Day view</option>
					                        <option value="window">Show Window With tasks</option>
					                        <option value="event">Launch Event</option>
					                      </select>
					                            <input type="button" name="button5" id="button5" value="change" onClick="samplemonth(2);">
					                    </form>                    </td>
					                  </tr>
					                  <tr class="textsample monthsample">
					                    <td ><b>Month - Manually Zoom Day tasks</b></td>
					                    <td><input type="button" name="button6" id="button6" value="Zoom" onClick="samplemonth(3);"></td>
					                  </tr>
					                  <tr class="textsample monthsample">
					                    <td ><b>Month - Launch Event for Task Item (task list)</b></td>
					                    <td><form name="formeventonmonth" method="post" action="">
					                      <select name="selectmonth_tasktimeaction" id="selectmonth_tasktimeaction">
					                        <option value="none">none</option>
					                        <option value="click">Click</option>
					                        <option value="dblclick" selected>Double Click</option>
					                      </select>
					                      <input type="button" name="button2" id="button2" value="change" onClick="samplemonth(5);">
					                    </form></td>
					                  </tr>
					                  <tr class="textsample monthsample">
					                    <td valign="top" ><b>Month - Menu ITEMS (day element)</b></td>
					                    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                      <tr class="textsample">
					                        <td width="49%"><div align="left">ADD</div></td>
					                        <td width="51%"><div align="center">
					                          <input name="m_item_1_checkbox" type="checkbox" id="m_item_1_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Next Month</div></td>
					                        <td><div align="center">
					                          <input name="m_item_2_checkbox" type="checkbox" id="m_item_2_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Previous Month</div></td>
					                        <td><div align="center">
					                          <input name="m_item_3_checkbox" type="checkbox" id="m_item_3_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Change to Week view</div></td>
					                        <td><div align="center">
					                          <input name="m_item_4_checkbox" type="checkbox" id="m_item_4_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Change to Day View</div></td>
					                        <td><div align="center">
					                          <input name="m_item_5_checkbox" type="checkbox" id="m_item_5_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Change to Scheduler View</div></td>
					                        <td><div align="center">
					                          <input name="m_item_6_checkbox" type="checkbox" id="m_item_6_checkbox" checked>
					                        </div></td>
					                      </tr>
					                    </table>
					                    <p align="center"><input type="button" name="button4" id="button4" value="Refresh Calendar" 
					                    onClick="samplemonth(6);"></p>                    </td>
					                  </tr>
					                  <tr class="textsample monthsample">
					                    <td valign="top" ><b>Month - Menu ITEMS For TASK (task list)</b></td>
					                    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                      <tr class="textsample">
					                        <td width="49%">ADD</td>
					                        <td width="51%"><div align="center">
					                          <input name="m2_item_1_checkbox" type="checkbox" id="m2_item_1_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Delete</td>
					                        <td><div align="center">
					                          <input name="m2_item_2_checkbox" type="checkbox" id="m2_item_2_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Edit</td>
					                        <td><div align="center">
					                          <input name="m2_item_3_checkbox" type="checkbox" id="m2_item_3_checkbox" checked>
					                        </div></td>
					                      </tr>
					                    </table>
					                     <p align="center"><input type="button" name="button4" id="button4" value="Refresh Calendar" 
					                     onClick="samplemonth(7);"></p></td>
					                  </tr>                  
					                  <tr class="textsample">
					                    <td colspan="2" ><div id="calendar_hdr_dayx" class="settings_monthview">Day view</div></td>
					                  </tr>
					                  <tr class="textsample daysample">
					                    <td ><b>Day - Change Event(task) Width </b></td>
					                    <td><input name="numtskwidthday" type="text" id="numtskwidthday" value="50" size="6" maxlength="3">
					                    <input type="submit" name="button" id="button" value="change" onClick="sampleday(1);"></td>
					                  </tr>
					                  <tr class="textsample daysample">
					                    <td ><b>Day - Force Task Width (0.0.14)</b></td>
					                    <td><select name="select_day_ftw_sample" id="select_day_ftw_sample">
					                       <option value="1">Yes</option>
					                       <option value="0" selected>No</option> 
					                       </select>
					                     <input type="submit" name="button9" id="button9" value="change" onClick="sampleday(7);"></td>
					                  </tr>
					                   <tr class="textsample daysample">
					                     <td ><b>Day - Custom HTML pos (0.0.14)</b> check 12-Dic-2008 for custom HTML</td>
					                     <td ><select name="select_day_htmlct_sample" id="select_day_htmlct_sample">
					                       <option value="before" selected>Before</option>
					                       <option value="after">After</option>
					                     </select>
					                     <input type="submit" name="button8" id="button8" value="change" onClick="sampleday(8);"></td>
					                   </tr>                  
					                   <tr class="textsample daysample">
					                     <td ><b>Day - task_clsOver Sample</b></td>
					                     <td ><select name="select_day_clasover_sample" id="select_day_clasover_sample">
					                       <option value="test_taskovercss" selected>test_taskovercss</option>
					                       <option value="test_taskovercss_b">test_taskovercss_b</option>
					                     </select>
					                       <input type="submit" name="button8" id="button8" value="change" onClick="sampleday(2);"></td>
					                   </tr>
					                   <tr class="textsample daysample">
					                     <td ><b>Day -</b> <b>Launch Event for Task Item</b></td>
					                     <td ><form name="formeventonday" method="post" action="">
					                      <select name="selectday_tasktimeaction" id="selectday_tasktimeaction">
					                        <option value="none">none</option>
					                        <option value="click">Click</option>
					                        <option value="dblclick" selected>Double Click</option>
					                      </select>
					                      <input type="button" name="button2" id="button2" value="change" onClick="sampleday(6);">
					                    </form></td>
					                   </tr>
					                   <tr class="textsample daysample">
					                     <td ><b>Day - task - Allow DD and resizable</b></td>
					                     <td ><select name="select_day_allowdd_sample" id="select_day_allowdd_sample">
					                       <option value="1" selected>Yes</option>
					                       <option value="0">No</option>
					                                                               </select>
					                     <input type="submit" name="button9" id="button9" value="change" onClick="sampleday(3);"></td>
					                   </tr>
					                   <tr class="textsample daysample">
					                     <td valign="top" ><b>Day - Menu Items on Day Body element</b></td>
					                     <td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                       <tr class="textsample">
					                         <td width="49%"><div align="left">ADD</div></td>
					                         <td width="51%"><div align="center">
					                           <input name="d_item_1_checkbox2" type="checkbox" id="d_item_1_checkbox2" checked>
					                         </div></td>
					                       </tr>
					                       <tr class="textsample">
					                         <td><div align="left">Next Day</div></td>
					                         <td><div align="center">
					                           <input name="d_item_2_checkbox2" type="checkbox" id="d_item_2_checkbox2" checked>
					                         </div></td>
					                       </tr>
					                       <tr class="textsample">
					                         <td><div align="left">Previous Day</div></td>
					                         <td><div align="center">
					                           <input name="d_item_3_checkbox2" type="checkbox" id="d_item_3_checkbox2" checked>
					                         </div></td>
					                       </tr>
					                       <tr class="textsample">
					                         <td><div align="left">Change to Month view</div></td>
					                         <td><div align="center">
					                           <input name="d_item_4_checkbox2" type="checkbox" id="d_item_4_checkbox2" checked>
					                         </div></td>
					                       </tr>
					                       <tr class="textsample">
					                         <td><div align="left">Change to Week View</div></td>
					                         <td><div align="center">
					                           <input name="d_item_5_checkbox2" type="checkbox" id="d_item_5_checkbox2" checked>
					                         </div></td>
					                       </tr>
					                       <tr class="textsample">
					                         <td><div align="left">Change to Scheduler View</div></td>
					                         <td><div align="center">
					                           <input name="d_item_6_checkbox2" type="checkbox" id="d_item_6_checkbox2" checked>
					                         </div></td>
					                       </tr>
					                     </table>
					                      <p align="center"><input type="button" name="button4" id="button4" value="Refresh Calendar" 
					                    onClick="sampleday(4);"></p>                     </td>
					                   </tr>
					                   <tr class="textsample daysample">
					                     <td valign="top" ><b>Day - Menu Items on each Task</b></td>
					                     <td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                      <tr class="textsample">
					                        <td width="49%">ADD</td>
					                        <td width="51%"><div align="center">
					                          <input name="d2_item_1_checkbox" type="checkbox" id="d2_item_1_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Delete</td>
					                        <td><div align="center">
					                          <input name="d2_item_2_checkbox" type="checkbox" id="d2_item_2_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Edit</td>
					                        <td><div align="center">
					                          <input name="d2_item_3_checkbox" type="checkbox" id="d2_item_3_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Next Day</td>
					                        <td><div align="center">
					                          <input name="d2_item_4_checkbox" type="checkbox" id="d2_item_4_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Previous day</td>
					                        <td><div align="center">
					                          <input name="d2_item_5_checkbox" type="checkbox" id="d2_item_5_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      
					                    </table>
					                     <p align="center"><input type="button" name="button4" id="button4" value="Refresh Calendar" 
					                     onClick="sampleday(5);"></p></td>
					                   </tr>
					                   <tr class="textsample">
					                    <td colspan="2" ><div id="calendar_hdr_weekx" class="settings_monthview">Week view</div></td>
					                  </tr>
					                   <tr class="textsample weeksample">
					                     <td ><b>Week - Start Day (0.0.14)</b></td>
					                     <td><select name="select_week_sd" id="select_week_sd">
					                       <option value="0" selected>Sunday</option>
					                       <option value="1">Monday</option>
					                       <option value="6">Saturday</option>
					                     </select>
					                       <input type="submit" name="button8" id="button8" value="change" onClick="sampleweek(10);"></td>
					                   </tr>                  
					                   <tr class="textsample weeksample">
					                     <td ><b>Week - Style view</b></td>
					                     <td><select name="select_week_styleview" id="select_week_styleview">
					                       <option value="plain">Plain</option>
					                       <option value="google" selected>Google</option>
					                     </select>
					                       <input type="submit" name="button8" id="button8" value="change" onClick="sampleweek(7);"></td>
					                   </tr>
					                  <tr class="textsample weeksample">
					                    <td ><b>Week - Change Event(task) Width</b></td>
					                    <td><input name="numtskwidthweek" type="text" id="numtskwidthweek" value="50" size="6" maxlength="3">
					                    <input type="submit" name="buttonweek" id="buttonweek" value="change" onClick="sampleweek(1);"></td>
					                  </tr>
					                  <tr class="textsample weeksample">
					                    <td ><b>Week - Force Task Width (0.0.14)</b></td>
					                    <td><select name="select_w_ftw_sample" id="select_w_ftw_sample">
					                       <option value="1">Yes</option>
					                       <option value="0" selected>No</option> 
					                       </select>
					                     <input type="submit" name="button9" id="button9" value="change" onClick="sampleweek(8);"></td>
					                  </tr> 
										<tr class="textsample weeksample">
					                     <td ><b>Week - Custom HTML pos (0.0.14)</b> check 12-Dic-2008 for custom HTML</td>
					                     <td ><select name="select_w_htmlct_sample" id="select_w_htmlct_sample">
					                       <option value="before" selected>Before</option>
					                       <option value="after">After</option>
					                     </select>
					                     <input type="submit" name="button8" id="button8" value="change" onClick="sampleweek(9);"></td>
					                   </tr>                                    
					                  <tr class="textsample weeksample">
					                    <td ><b>Week- Day's Header click action</b></td>
					                    <td ><select name="select_week_actionclick" id="select_week_actionclick">
					                      <option value="none">none</option>
					                      <option value="viewday" selected>Go to Day view</option></select>
										   <input type="submit" name="buttonweekaction" id="buttonweekaction" value="change" onClick="sampleweek(6);">                                                                                </td>
					                  </tr>
					                  <tr class="textsample weeksample">
					                     <td ><b>Week - task_clsOver Sample</b></td>
					                     <td ><select name="select_week_clasover_sample" id="select_week_clasover_sample">
					                       <option value="test_taskovercss" selected>test_taskovercss</option>
					                       <option value="test_taskovercss_b">test_taskovercss_b</option>
					                     </select>
					                       <input type="submit" name="button8" id="button8" value="change" onClick="sampleweek(2);"></td>
					                   </tr>
										<tr class="textsample weeksample">
					                     <td ><b>Week -</b> <b>Launch Event for Task Item</b></td>
					                     <td ><form name="formeventonweek" method="post" action="">
					                      <select name="selectweek_tasktimeaction" id="selectweek_tasktimeaction">
					                        <option value="none">none</option>
					                        <option value="click">Click</option>
					                        <option value="dblclick" selected>Double Click</option>
					                      </select>
					                      <input type="button" name="button2" id="button2" value="Change" onClick="sampleweek(3);">
					                    </form></td>
					                   </tr>  
									  <tr class="textsample weeksample">
					                    <td valign="top" ><b>Week - Menu ITEMS (day body)</b></td>
					                    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                      <tr class="textsample">
					                        <td width="49%"><div align="left">ADD</div></td>
					                        <td width="51%"><div align="center">
					                          <input name="w_item_1_checkbox" type="checkbox" id="w_item_1_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Next Week</div></td>
					                        <td><div align="center">
					                          <input name="w_item_2_checkbox" type="checkbox" id="w_item_2_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Previous Week</div></td>
					                        <td><div align="center">
					                          <input name="w_item_3_checkbox" type="checkbox" id="w_item_3_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Change to Month view</div></td>
					                        <td><div align="center">
					                          <input name="w_item_4_checkbox" type="checkbox" id="w_item_4_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Change to Day View</div></td>
					                        <td><div align="center">
					                          <input name="w_item_5_checkbox" type="checkbox" id="w_item_5_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Change to Scheduler View</div></td>
					                        <td><div align="center">
					                          <input name="w_item_6_checkbox" type="checkbox" id="w_item_6_checkbox" checked>
					                        </div></td>
					                      </tr>
					                    </table>
					                    <p align="center"><input type="button" name="buttonw4" id="buttonw4" value="Refresh Calendar" 
					                    onClick="sampleweek(4);"></p>                    </td>
					                  </tr>                   
					<tr class="textsample weeksample">
					                     <td valign="top" ><b>Week - Menu Items on each Task</b> <b>(normal tasks)</b></td>
					                     <td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                      <tr class="textsample">
					                        <td width="49%">ADD</td>
					                        <td width="51%"><div align="center">
					                          <input name="w2_item_1_checkbox" type="checkbox" id="w2_item_1_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Delete</td>
					                        <td><div align="center">
					                          <input name="w2_item_2_checkbox" type="checkbox" id="w2_item_2_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Edit</td>
					                        <td><div align="center">
					                          <input name="w2_item_3_checkbox" type="checkbox" id="w2_item_3_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Next Week</td>
					                        <td><div align="center">
					                          <input name="w2_item_4_checkbox" type="checkbox" id="w2_item_4_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Previous Week</td>
					                        <td><div align="center">
					                          <input name="w2_item_5_checkbox" type="checkbox" id="w2_item_5_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      
					                    </table>
					                     <p align="center"><input type="button" name="buttonww4" id="buttonww4" value="Refresh Calendar" 
					                     onClick="sampleweek(5);"></p></td>
					                  </tr> 
					                  <tr class="textsample">
					                    <td colspan="2" ><div id="calendar_hdr_scx" class="settings_monthview">Schedule view</div></td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule - Period Type</b></td>
					                    <td ><select name="select_shced_period" id="select_shced_period">
					                      <option value="0" selected>Day</option>
					                      <option value="1">Week</option>
					                      <option value="2">Month</option>
					                      <option value="3">Two months</option>
					                      <option value="4">Quarter</option>
					                    </select>
					                    <input type="button" name="button7" id="button7" value="change" onClick="samplesched(6);"></td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule-</b> <b>Unit Header TYPE</b></td>
					                    <td ><select name="select_sched_units" id="select_sched_units">
					                      <option value="0" selected>Hours</option>
					                      <option value="1">Days</option>
					                      <option value="2">Weeks</option>
					                    </select>
										   <input type="submit" name="buttonweekaction" id="buttonweekaction" value="change" onClick="samplesched(5);">                    </td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule- Unit Header click Action</b></td>
					                    <td ><select name="select_sched_actionclick" id="select_sched_actionclick">
					                      <option value="none">none</option>
					                      <option value="gotoview">Go to View</option>
					                      <option value="event">Event</option>
					                    </select>
										   <input type="submit" name="buttonweekaction" id="buttonweekaction" value="change" onClick="samplesched(1);">                    </td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule - Use Store Color for List Items BG</b></td>
					                    <td >
					                      <select name="select_sched_colorusebg" id="select_sched_colorusebg">
					                        <option value="0" selected>NO</option>
					                        <option value="1">Yes</option>
					                      </select>
					                      <input type="button" name="button3" id="button3" value="change" onClick="samplesched(2);">                   </td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule - List Item Width</b></td>
					                    <td ><input name="numlistItemwidth" type="text" id="numlistItemwidth" value="250" size="6" maxlength="3">
					                    <input type="submit" name="buttonweek" id="buttonweek" value="change" onClick="samplesched(3);"></td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule - List Item / Time line - Action</b></td>
					                    <td > <select name="select_schedmouseaction" id="select_schedmouseaction">
					                        <option value="">none</option>
					                        <option value="click" selected>Click</option>
					                        <option value="dblclick">Double Click</option>
					                      </select>
					                      <input type="button" name="button2" id="button2" value="Change" onClick="samplesched(4);"></td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule - Unit Header Width</b></td>
					                    <td ><input name="numlistUnitwidth" type="text" id="numlistUnitwidth" value="25" size="6" maxlength="3">
					                    <input type="submit" name="buttonweek" id="buttonweek" value="change" onClick="samplesched(7);"></td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule - Time Line class Over</b></td>
					                   <td ><select name="select_sched_clasover_sample" id="select_sched_clasover_sample">
					                       <option value="test_taskovercss_sched" selected>test_taskovercss_sched</option>
					                       <option value="test_taskovercss_sched_b">test_taskovercss_sched_b</option>
					                     </select>
					                       <input type="submit" name="button8" id="button8" value="change" onClick="samplesched(8);"></td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td ><b>Schedule - Display Subject on the Time-Line body</b></td>
					                    <td ><select name="select_sched_usenames" id="select_sched_usenames">
					                        <option value="0" selected>NO</option>
					                        <option value="1">Yes</option>
					                      </select>
					                    <input type="button" name="button3" id="button3" value="change" onClick="samplesched(9);"></td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td valign="top" ><b>Schedule - Menu ITEMS (ListItems)</b></td>
					                    <td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                      <tr class="textsample">
					                        <td width="49%"><div align="left">ADD</div></td>
					                        <td width="51%"><div align="center">
					                            <input name="s_item_1_checkbox2" type="checkbox" id="s_item_1_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">EDIT</div></td>
					                        <td><div align="center">
					                            <input name="s_item_2_checkbox2" type="checkbox" id="s_item_2_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">DELETE</div></td>
					                        <td><div align="center">
					                            <input name="s_item_3_checkbox2" type="checkbox" id="s_item_3_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">GO Next Period</div></td>
					                        <td><div align="center">
					                            <input name="s_item_4_checkbox2" type="checkbox" id="s_item_4_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Go Prev period</div></td>
					                        <td><div align="center">
					                            <input name="s_item_5_checkbox2" type="checkbox" id="s_item_5_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td><div align="left">Change to Month View</div></td>
					                        <td><div align="center">
					                            <input name="s_item_6_checkbox2" type="checkbox" id="s_item_6_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Change to Week View</td>
					                        <td><div align="center">
					                            <input name="s_item_7_checkbox2" type="checkbox" id="s_item_7_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>Change to Day view</td>
					                        <td><div align="center">
					                            <input name="s_item_8_checkbox2" type="checkbox" id="s_item_8_checkbox2" checked>
					                        </div></td>
					                      </tr>
					                    </table>
										<p align="center"><input type="button" name="buttonw4" id="buttonw4" value="Refresh Calendar" 
					                    onClick="samplesched(10);"></p>                    </td>
					                  </tr>
					                  <tr class="textsample sxsample">
					                    <td valign="top" ><b>Schedule - Menu ITEMS (Time Lines)</b></td>
					                    <td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
					                      <tr class="textsample">
					                        <td width="49%">ADD</td>
					                        <td width="51%"><div align="center">
					                          <input name="s2_item_1_checkbox" type="checkbox" id="s2_item_1_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>EDIT</td>
					                        <td><div align="center">
					                          <input name="s2_item_2_checkbox" type="checkbox" id="s2_item_2_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      <tr class="textsample">
					                        <td>DELETE</td>
					                        <td><div align="center">
					                          <input name="s2_item_3_checkbox" type="checkbox" id="s2_item_3_checkbox" checked>
					                        </div></td>
					                      </tr>
					                      
					
					                    </table>
					                    <p align="center"><input type="button" name="buttonww4" id="buttonww4" value="Refresh Calendar" 
					                     onClick="samplesched(11);"></p></td>
					                  </tr>
					            </table>
					            </div>
					    	</div>
						<div>	</div>
					</div>
					             --> 
					   	  
				</td>
			</tr>
		</table>
	</form>
</body>
</html>