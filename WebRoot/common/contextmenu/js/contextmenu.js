(function(menu) {
    jQuery.fn.contextmenu = function(options) {
        var defaults = {
			height:100,
			width:100,
            offsetX : 2,        //�����X��ƫ����
            offsetY : 2,        //�����Y��ƫ����
            items   : [],       //�˵���
            action  : $.noop()  //���ɲ˵���ص��¼�
        };
        var opt = menu.extend(true, defaults, options);
        function create(e) {
            var m = menu('<ul class="simple-contextmenu"></ul>').appendTo(document.body);            
            menu.each(opt.items, function(i, item) {
                if (item) {
                    if(item.type == "split"){ 
                        menu("<div class='m-split'></div>").appendTo(m);
                        return;
                    }
                    var row   = menu('<li><div class="menuBarContainerDIV"><div class="menuBarIconDIV"><img src="' + item.icon + '" width="16" height="16" /> </div><div class="menuBarContentDIV"><a href="javascript:void(0)">'+item.text+'</a></div></div></li>').appendTo(m);                                        
                    if (item.action) {
                        row.find('a').click(function() {
                            item.action(e.target);
                        });
                    }
                }
            });
			m.css("width",opt.width+"px");
			$(".menuBarContainerDIV").css("width",opt.width-4+"px");
			$(".menuBarContentDIV").css("width",opt.width-35+"px");	
            return m;
        }
        
        this.live('contextmenu', function(e) {					
            var scrollTop = document.documentElement.scrollTop; //��ֱ���������붥��
		    var scrollLeft = document.documentElement.scrollLeft;//ˮƽ�������������
			var clientX= e.clientX;//�������򽹵�X����ֵ
			var clientY=e.clientY;//�������򽹵�Y����ֵ					
            var m = create(e).show("fast");					
			var left = clientX + opt.offsetX+scrollLeft, top = clientY + opt.offsetY+scrollTop, p = {
                wh : menu(window).height(),//��������
                ww : menu(window).width()//������߶�                
            }
			//���˵��������ڱ߽�ʱ����			
			if((clientX+opt.width)>p.ww){
					left =p.ww- opt.width+scrollLeft-5;					
				}
			if((clientY+opt.height)>p.wh){
					top=p.wh- opt.height+scrollTop;
				}    
            m.css({
                zIndex : 10000,
                left : left,
                top : top
            });
            $(document.body).live('contextmenu click', function() {
                m.hide("fast",function(){
                    m.remove();
                });        
            });
            
            return false;
        });
        return this;
    }
})(jQuery);
