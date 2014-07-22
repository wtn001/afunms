var panelevent_var = function(rootpath){ 

				var proxyevent = new Ext.data.HttpProxy({
					url:'getEventListAjaxManager.ajax?action=ajaxGetEventList'
				});
				
				var readerevent = new Ext.data.JsonReader({  
						root : 'EventList'
					},[
						{name : 'nodeid',type:'int'},
						{name : 'level1'},
						{name : 'eventlocation'},
						{name : 'content'},
						{name : 'recordtime'}
						
				]);
				
			    var dataStoreevent = new Ext.data.Store({
			        autoLoad: true,
					proxy : proxyevent,
					reader : readerevent
				});
				
			    var gridevent = new Ext.grid.GridPanel({
			        store: dataStoreevent,
			        title:'�澯��Ϣ�б�',
			        columns: [
			        	{
			                id       :'nodeid',
			                header   : '�豸ID', 
			                width :30,
			                sortable : true, 
			                dataIndex: 'nodeid'
			            },
			            {
			                header   : '�ȼ�', 
			                width :50,
			                sortable : true, 
			                renderer : function(level1,_this,_data){ 
			                	var level = _data.get('level1');
			                	var levelname="";
			                	var bgcolor="";
			                	if("3"==level){
			                		levelname="�����澯";	
			                		bgcolor="red";		                	
			                	}else if("2"==level){
			                		levelname="���ظ澯";	
			                		bgcolor="orange";		                	
			                	}else if("1"==level){
			                		levelname="��ͨ�澯";	
			                		bgcolor="yellow";			                	
			                	}else if("0"==level){
			                		levelname="��ʾ��Ϣ";	
			                		bgcolor="blue";			                	
			                	}
			                	 var tabstr =  '<table width="100%"><tr><td bgcolor='+bgcolor+' height="18px" style="text-align:center;line-height:18px">'+levelname+'</td></tr></table>';
							     return tabstr;
							     
							     },
			                dataIndex: 'level1'
			            },
			            {
			                header   : '��Դ', 
			                width :100,
			                sortable : true, 
			                renderer : 'eventlocation',
			                dataIndex: 'eventlocation'
			            },
			            {
			                header   : '�澯��Ϣ', 
			                sortable : true, 
			                width :170,
			                renderer : 'content', 
			                dataIndex: 'content'
			            },
			             {
			                header   : '��¼ʱ��', 
			                width :90,
			                sortable : true, 
			                renderer : 'recordtime', 
			                dataIndex: 'recordtime'
			            }
			       	],
			        stripeRows: true,
			        autoExpandColumn: 'nodeid',
                    bodyStyle : 'width:100%',   
			        autoHeight : true,
			        viewConfig:{   
						forceFit:true  
					}, 
			        stateful: false,
			        stateId: 'gridevent'
			    });  
			    gridevent.getStore().load();
			    
				var updateClock = function(){
    				dataStoreevent.reload();
				};
				
			    Ext.TaskMgr.start({
    				run: updateClock,
    				interval: 60000
				});
			    
			    
		    var tabs = new Ext.TabPanel({
		    	id:'event',
		    	renderTo:"event_list",
                width:Ext.get("event_list").getWidth(),
		    	height:430,
		        activeTab: 0, 
			    autoScroll:true,
			    autoWidth:false,
		        plain:true,
		        defaults:{autoScroll: true},
		        items:[gridevent]
		    }); 
		    
		    
		    //����Tabҳ���л����¼�
		    tabs.on("tabchange",function(currentTab, newTab ){
		    	newTab.getStore().load();
		    } );
		};