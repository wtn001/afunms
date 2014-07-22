// info ��Ϣ����ʽ
var infoBgColor     = "#ECECEC";
var infoBgImg       = "";           // path to background image;
var infoBorder      = "solid gray 1px";
var infoBorderColor = "#003399";
var infoBorderWidth = 1;
var infoDelay       = 500;          // time span until tooltip shows up [milliseconds]
var infoFontColor   = "#000000";
var infoFontFace    = "����,arial,helvetica,sans-serif";
var infoFontSize    = "12px";
var infoFontWeight  = "normal";     // alternative: "bold";
var infoPadding     = 3;            // spacing between border and content
var infoShadowColor = "";
var infoShadowWidth = 0;
var infoTextAlign   = "left";
var infoTitleColor  = "#ffffff";    // color of caption text
var infoWidth       = 180;

Array.prototype.contains = function()     
{
	for (var i = 0; i < this.length; i++)
	{
		if (arguments[0] == this[i])
		{
			return true;
		}
	}
	return false;
}

//ȫ�ֱ���
var xmldoc;						// xmlȫ�ֶ���
var xx;							// ��굱ǰ���㣭ƫ����
var yy;							// ��굱ǰ���㣭ƫ����
var obj = null;				// �����¼��Ŀؼ�����
var objStyle = null;  // �����¼��Ŀؼ�����,���ڸı�ѡ��ͼƬ����ʽ��
var strSearch = null;			// �������ַ���
var line_ids = new Array();	// ��ŵ�ǰ�¼��ؼ����������
var tempArray = new Array();	// ����޸Ĺ��Ķ���id������
var clickObj = null;
var clickLineObj = null;
var maxWidth = 0;				// �����
var maxHeight = 0;				// ���߶�
var minWidth = 0;				// ��С���
var minHeight = 0;				// ��С�߶�
var strArray = new Array();
var beginx = 0;
var beginy = 0;
var selectStatus = false; // �ж�ѡ��
var ctrlStatus = false;//yangjun add �ж�ctrl
var objMoveAry = new Array();//����ѡ�е�ͼԪ����
var objEntityAry = new Array();//yangjun add Ctrl��ѡ�е�ͼԪ
var objLeftAry = new Array();
var objTopAry = new Array();
var lineMoveAry = new Array();
var assLineMoveAry = new Array();//������·�������yangjun add
var demoLineMoveAry = new Array();//ʾ����·������� yangjun add
var relLineAry = new Array();
var isRemoved = false;
var movedObjFlag = false;		// FIXME: �ñ���δ����
//Ϊ�������ܶ��������------------��1---
var nodeCoorAry = new Array();	// ÿ���ڵ�����꣺ip,x,y
var nodeIdAry = new Array();	// ÿ���ڵ�����꣺id,x,y yangjun add
//---------------------------------
//window.document.onselectstart = selectStart;


// ---------------------------------
// ���ڽ��п�ѡ���϶��޸ģ����涨�弸��������
// ����
var nodeArray = new Array();                // �����豸�ڵ�����
var nodeSelectedArray = new Array();        // ���б�ѡ��Ľڵ�����
var lineArray = new Array();                // ������·����

// ����ͼ�豸����
// ������������豸��ʱ�򴴽��Ķ������ nodeArray �У�
// ѡ�к������ nodeSelectedArray ��
function TopologyMapNode() {

    // id
    this.id = null;

    // ����
    this.nodeType = null;

    // ͼƬ
    this.imgValue = null;

    // X λ��
    this.x = 0;

    // Y λ��
    this.y = 0;

    // IP
    this.ip = null;

    // ����
    this.alias = null;

    // �豸��Ϣ (�ð汾δʹ�ø��ֶ�)
    this.info = null;

    // �豸�Ҽ��˵� (�ð汾δʹ�ø��ֶ�)
    this.menu = null;

    // �豸������ͼ (�ð汾δʹ�ø��ֶ�)
    this.relationMap = null;
    
    // �豸ͼƬ����
    this.imageElement = null;
    
    // �豸�ı�����
    this.divTextElement = null;

    // �豸��Ϣ����
    this.divInfoElement = null;

    // �豸�������¿հ�
    this.aliasHSpace = 0;
    
    // �豸�������ҿհ�
    this.aliasVSpace = 0;
    
    // �Ƿ�ѡ��
    this.isSelected = false;
    
    // �豸�ƶ�֮ǰ�� X λ��
    this.beforeMoveX = 0;

    // �豸�ƶ�֮ǰ�� Y λ��
    this.beforeMoveY = 0;
    
    // �豸����갴ס״̬
    this.mouseDownStatus = false;
}

function TopologyMapLine() {
    
    // XML��· id
    this.lineid = null;

    // ��· a
    this.a = null;
    
    // ��· b
    this.b = null;
    
    // ��·��ɫ
    this.color = null;
    
    // ��·
    this.dash = null;
    
    // ��·���
    this.lineWidth = null;
    
    // ��·��Ϣ
    this.lineInfo = null;
    
    // ��·�˵�
    this.lineMenu = null;

    // ��·id
    this.id = null;
    
    // ��·����
    this.lineElement = null;
}

function addTopologyMapNode(id, imageElement, divTextElement, divInfoElement, divMenuElement) {
    var topologyMapNode = new TopologyMapNode();
    topologyMapNode.id = id;
    topologyMapNode.imageElement = imageElement;
    topologyMapNode.divTextElement = divTextElement;
    topologyMapNode.divInfoElement = divInfoElement;
    topologyMapNode.divMenuElement = divMenuElement;
    nodeArray.push(topologyMapNode);
}

function addTopologyMapLine(lineid, a, b, lineElement) {
    var topologyMapLine = new TopologyMapLine();
    topologyMapLine.lineid = lineid;
    topologyMapLine.a = a;
    topologyMapLine.b = b;
    topologyMapLine.lineElement = lineElement;
    lineArray.push(topologyMapLine);
} 
// ---------------------------------


//window.document.onselectstart = setDrag;

// ʵ��xmlװ�ع���
// url: xml�ļ�
function loadXML(url)
{
	var http = new ActiveXObject("Microsoft.XMLHTTP");
	xmldoc = new ActiveXObject("Microsoft.XMLDOM");
	http.open("POST", url, false);
	http.send();
	xmldoc.async = false;
	xmldoc.loadXML(http.responseText);
	maxWidth = maxHeight = minWidth = minHeight = 0;

	parseData();
	setDrag();
}
function initXML(url)
{
	var http = new ActiveXObject("Microsoft.XMLHTTP");
	xmldoc = new ActiveXObject("Microsoft.XMLDOM");
	http.open("POST", url, false);
	http.send();
	xmldoc.async = false;
	xmldoc.loadXML(http.responseText);
	maxWidth = maxHeight = minWidth = minHeight = 0;

	return xmldoc;
}
//ɾ���ڵ�
function removeNode(nodeid,url){
	var olddiv = document.getElementById("node_" + nodeid);
    document.all.divLayer.removeChild(olddiv);
    var divText =  document.getElementById("text_" + nodeid);
    document.all.divLayer.removeChild(divText);
    var divInfo =  document.getElementById("info_" + nodeid);
    document.all.divLayer.removeChild(divInfo);
    var divMenu =  document.getElementById("menu_" + nodeid);
    document.all.divLayer.removeChild(divMenu);
}
function autorefresh()
{
   //window.location = "pingShowMap.jsp";
	document.all.divLayer.removeAll();
    xmldoc = initXML(url);
    parseData();
}
//��ӽڵ�
function addNode(nodeid,url){
	xmldoc = initXML(url);
	var nodes = xmldoc.getElementsByTagName("node");	
	//alert(url);
	for (var i = nodes.length-1; i >= 0; i--){
	    var node = nodes[i];
		var id = node.getElementsByTagName("id")[0].text;
		if(id==nodeid){
			//alert(id);
		    var nodeType = node.getElementsByTagName("id")[0].getAttribute("category");
			var imgValue = "../../resource/"+node.getElementsByTagName("img")[0].text;
			var x = node.getElementsByTagName("x")[0].text;
			var y = node.getElementsByTagName("y")[0].text;
			var w = node.getElementsByTagName("width")[0].text;
			var h = node.getElementsByTagName("height")[0].text;
			var ip = node.getElementsByTagName("ip")[0].text;
			var alias = node.getElementsByTagName("alias")[0].text;
			var info = node.getElementsByTagName("info")[0].text;
			var menu = node.getElementsByTagName("menu")[0].text;
			var relationMap = node.getElementsByTagName("relationMap")[0].text;
			// ���������õ����飬�������ʼ��---------��2---
			nodeCoorAry.push(ip + "," + x + "," + y);
			nodeIdAry.push(id + "," + x + "," + y)
			// ---------------------------------------
			getConfine(x, y);		// ��ȡ���߽磬�� maxWidth��maxHeight ��ֵ
			var img = document.createElement("v:image");
			// img.id = "image_" + id;//yangjun �޸�
			img.id = "node_" + id;
			if(info=="ʾ���豸") {// yangjun add ����ʾ���豸
				img.name = relationMap+",1";// �ڵ��������ͼ�ļ�����ʾ���豸�жϱ��
			} else {
			    img.name = relationMap+",0";;// �ڵ��������ͼ�ļ�����ʾ���豸�жϱ��
			}
			img.style.width = w;
			img.style.height = h;
			img.style.position = "absolute";
			img.style.cursor = "hand";
			img.style.left = x;
			img.style.top = y;
	
			img.src = imgValue;
			// divBackGround.appendChild(img);
			document.all.divLayer.appendChild(img);
			// ��ʾ�豸�ı�
			var divText = document.createElement("div");
			divText.id = "text_" + id;
			divText.style.position = "absolute";
			divText.style.width = "80";
			divText.style.height = "20";
			divText.style.left = parseInt(x, 10) - parseInt(w, 10);
			divText.style.top = parseInt(y, 10) + parseInt(h, 10);
			divText.style.fontSize = "12px";
			divText.align = "center";
			if (g_viewFlag == 0)
				divText.innerHTML = alias;// ��ʾ�豸����
			else
				divText.innerHTML = ip;// ��ʾ�豸IP
			document.all.divLayer.appendChild(divText);
			// ���������ʾ�豸��Ϣ
			var divInfo = document.createElement("div");
			divInfo.id = "info_" + id;
			divInfo.name = id;// alias+"("+ip+")";//yangjun add
			divInfo.style.position = "absolute";
			divInfo.style.border = infoBorder;
			divInfo.style.width = infoWidth;
			divInfo.style.height = "auto";
			divInfo.style.color = infoFontColor;
			divInfo.style.padding = infoPadding;
			divInfo.style.lineHeight = "120%";
			divInfo.style.filter = "alpha(opacity=70)";
			divInfo.style.zIndex = 2;
			divInfo.style.backgroundColor = infoBgColor;
			divInfo.style.left = parseInt(x, 10) + 32;
			divInfo.style.top = parseInt(y, 10);
			divInfo.style.visibility = "hidden";
			divInfo.style.fontSize = "12px";
			divInfo.innerHTML = info;
			document.all.divLayer.appendChild(divInfo);
	
			// ����"�豸��Ϣ��ʾ"�¼�
			document.all("node_" + id).onmouseover = function() { document.all(this.id.replace("node", "info")).style.visibility = "visible"; };
			document.all("node_" + id).onmouseout = function() { document.all(this.id.replace("node", "info")).style.visibility = "hidden"; };
			
			//#########���ӡ��豸��Ϣ��ʾ��ʱ��Խ�紦���������߽������Զ���ʾ�����ӷ�Χ֮��   HONGLI ADD ########
			if(divInfo.clientHeight != 0){
				if(parseInt(x, 10) > document.body.clientWidth-infoWidth){
					divInfo.style.left =parseInt(x, 10)-infoWidth;
				} 
				if(parseInt(y, 10) > document.body.clientHeight-divInfo.clientHeight){
					divInfo.style.top = parseInt(y, 10)-divInfo.clientHeight;  
			    }
			}
		    //#############HONG ADD END##########
		
		
	        // ˫��ͼԪ�¼�yangjun
			document.all("node_" + id).ondblclick = function() {
			                                         if(this.name.substring(0,this.name.lastIndexOf(","))!=""){
				                                     	 // parent.location =
															// "../submap/submap.jsp?submapXml="
															// +
															// this.name.substring(0,this.name.lastIndexOf(","));
			                                         	 if(this.name.substring(0,this.name.lastIndexOf(","))=="network.jsp"){
			                                         	     window.parent.parent.location = "../network/index.jsp";
			                                         	     // parent.mainFrame.location.reload();
			                                         	 }else{
			                                         	     window.parent.parent.location = "../submap/index.jsp?submapXml=" + this.name.substring(0,this.name.lastIndexOf(","));
			                                         	 }
				                                     	 
				                                     }else{
				                                     	 nodeId = this.id.replace("node_", "");
				                                         showalert(nodeId);
				                                         window.parent.parent.opener.focus();
				                                     }
			                                     };
			
			// �Ҽ�����������ʾ�˵�
			var divMenu = document.createElement("div");
			divMenu.id = "menu_" + id;
			divMenu.style.position = "absolute";
			divMenu.style.width = 120;
			divMenu.style.height = "auto";
			divMenu.style.zIndex = 2;
			divMenu.style.left = parseInt(x, 10) + 28;
			divMenu.style.top = parseInt(y, 10);
			divMenu.style.visibility = "hidden";
			divMenu.style.border = "solid #99A5A5 4px";
			divMenu.style.filter = "alpha(opacity=70)";
			divMenu.style.backgroundColor = "#CAE6FA";
			divMenu.style.padding = "1px";
			divMenu.style.lineHeight = "100%";
			divMenu.style.fontSize = "12px";
			divMenu.innerHTML = menu;
			document.all.divLayer.appendChild(divMenu);
	
			// ���Ӳ˵��Ĵ����¼�
			document.all("node_" + id).oncontextmenu = function()
			{ 
				if(clickObj != null)
				{
					document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden";
					clickObj = null;
				}	
				if(objStyle != null)
				{
				  	unSelectImg(objStyle);
				    objStyle = null;
				}	
				clickObj = this;
				objStyle = this;
				selectImg(objStyle);
				document.all(this.id.replace("node", "info")).style.visibility = "hidden";
				document.all(this.id.replace("node", "menu")).style.visibility = "visible"; 			
		   };
		   
			document.all("menu_" + id).onclick = function() { this.style.visibility = "hidden"; };	
			// ����"�϶�"�¼�
			document.all("node_" + id).onmousedown = down;
			document.onmousemove = null;
			document.all("node_" + id).onmouseup = up;
			
			addTopologyMapNode(id, img, divText, divInfo, divMenu);
			return;
		}
	}
}
//������·
function addlink(lineid,url){
	xmldoc = initXML(url);
    var lines = xmldoc.getElementsByTagName("line");
	for (var j = lines.length-1; j >= 0; j--)
	{
		var lineObj = lines[j];				
		var line_id = lineObj.getAttribute("id");
		if(line_id==lineid){
		    var a = lineObj.getElementsByTagName("a")[0].text;
			var b = lineObj.getElementsByTagName("b")[0].text;
			var color = lineObj.getElementsByTagName("color")[0].text;
			var dash = lineObj.getElementsByTagName("dash")[0].text;
			var lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
			var lineInfo = lineObj.getElementsByTagName("lineInfo")[0].text;
			var lineMenu = lineObj.getElementsByTagName("lineMenu")[0].text;
			var linetext = lineObj.getElementsByTagName("alias")[0].text;
	         
			var line = document.createElement("v:line");
			line.lineid = lineObj.getAttribute("id");
			line.id = "line_" + a + "_"+ b;
			line.style.position = "absolute";
			line.style.zIndex = -1;
			line.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2);
			line.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2);
			line.strokecolor = color;
			line.strokeweight = lineWidth;// 1;
	        // ///////////////yangjun add begin
			if(linetext!="#.#"){
			    var divLineText = document.createElement("div");
				divLineText.id = "linetext_" + line.lineid;
				divLineText.style.position = "absolute";
				divLineText.style.width = "40";
				divLineText.style.height = "20";
				divLineText.style.left = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + b).style.left)-10)/2;
				divLineText.style.top = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2;
				divLineText.style.fontSize = "12px";
				divLineText.align = "center";
		//		divLineText.style.backgroundColor = "#9BFFAC";
		//	    divLineText.style.border="1px solid #007314";
		//	    divLineText.style.filter="Alpha(Opacity=60);";
				divLineText.innerHTML = linetext;// ��ʾ��·����
				
				document.all.divLayer.appendChild(divLineText);
			}
			// ��ʾ��·��Ϣ
			var divLineInfo = document.createElement("div");
			divLineInfo.id = "info_" + a + "_"+ b;
			divLineInfo.style.position = "absolute";
			divLineInfo.style.border = infoBorder;
			divLineInfo.style.width = 220;
			divLineInfo.style.height = "auto";
			divLineInfo.style.color = infoFontColor;
			divLineInfo.style.padding = infoPadding;
			divLineInfo.style.display = "block";
			divLineInfo.style.lineHeight = "120%";
			divLineInfo.style.filter = "alpha(opacity=70)";
			divLineInfo.style.zIndex = 2;
			divLineInfo.style.backgroundColor = infoBgColor;
			divLineInfo.style.visibility = "hidden";
			divLineInfo.style.fontSize = "12px";
			divLineInfo.innerHTML = lineInfo;
			document.all.divLayer.appendChild(divLineInfo);
			var getCoordInDocument = function(e) {// ��ȡ��굱ǰλ��
	             e = e || window.event; 
	             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
	             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
	             return {'x':x,'y':y}; 
	        }
			line.onmousemove = function(e) { 
				                   var pos=getCoordInDocument();
			                       // window.event.srcElement.strokeweight =
									// parseInt(lineWidth)+1;
			                       window.event.srcElement.style.cursor = "hand";
			                       // window.event.srcElement.style.filter="Alpha(Opacity=60);";
			                       document.all(this.id.replace("line","info")).style.left = parseInt(pos['x']);
			                       document.all(this.id.replace("line","info")).style.top = parseInt(pos['y']);
			                       document.all(this.id.replace("line","info")).style.visibility = "visible";
			                   };
			line.onmouseout = function() { 
				                  // alert(lineWidth);
			                      // window.event.srcElement.strokeweight =
									// lineWidth;//1;
				                  // window.event.srcElement.style.filter="";
			                      window.event.srcElement.style.cursor = "default"; 
			                      document.all(this.id.replace("line","info")).style.visibility = "hidden";
			                  };
			// ///////////////end
			// line.onmouseover = function() { window.event.srcElement.strokeweight
			// = 2; window.event.srcElement.style.cursor = "hand";};
			// line.onmouseout = function() { window.event.srcElement.strokeweight =
			// 1; window.event.srcElement.style.cursor = "default"; };
			
			// line.onclick = function() { showLineInfo() };
			document.all.divLayer.appendChild(line);
			document.all("node_" + a).lines += '&' + line.id;
			document.all("node_" + b).lines += '&' + line.id;
			// ��·�˵�
			var divMenu = document.createElement("div");
			divMenu.id = "menu_" + a + "_"+ b;
			divMenu.style.position = "absolute";
			divMenu.style.width = 120;
			divMenu.style.height = "auto";
			divMenu.style.zIndex = 2;
			divMenu.style.visibility = "hidden";
			divMenu.style.border = "solid #99A5A5 4px";
			divMenu.style.backgroundColor = "#CAE6FA";
			divMenu.style.filter = "alpha(opacity=70)";
			divMenu.style.padding = "1px";
			divMenu.style.lineHeight = "100%";
			divMenu.style.fontSize = "12px";
			divMenu.innerHTML = lineMenu;
			document.all.divLayer.appendChild(divMenu);
			// ������·�˵��Ĵ����¼�
			document.all("line_" + a + "_"+ b).oncontextmenu = function()
			{ 
				var pos=getCoordInDocument();
				document.all(this.id.replace("line","menu")).style.left = parseInt(pos['x']);
			    document.all(this.id.replace("line","menu")).style.top = parseInt(pos['y']);
				if(clickLineObj != null)
				{
					document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden";
					clickLineObj = null;
				}
				clickLineObj = this;
				document.all(this.id.replace("line", "info")).style.visibility = "hidden";
				document.all(this.id.replace("line", "menu")).style.visibility = "visible"; 			
		    };
			document.all("menu_" + a + "_"+ b).onclick = function() { this.style.visibility = "hidden"; };
			// ���� stroke ��ǩ
			// window.event.srcElement.stroke.dashstyle = "Solid";
			var stroke = document.createElement("v:stroke");
			stroke.dashstyle = dash;
			document.all(line.id).appendChild(stroke);
			
			addTopologyMapLine(line.lineid, a, b, line);
			return;
		}
	}
}
//
function addAssLink(lineid,url){
	xmldoc = initXML(url);
    var lines = xmldoc.getElementsByTagName("assistant_line");
	for (var j = lines.length-1; j >= 0; j--)
	{
		var lineObj = lines[j];
		var line_id = lineObj.getAttribute("id");
		if(line_id==lineid){
		    var a = lineObj.getElementsByTagName("a")[0].text;
			var b = lineObj.getElementsByTagName("b")[0].text;
			var color = lineObj.getElementsByTagName("color")[0].text;
			var dash = lineObj.getElementsByTagName("dash")[0].text;
			var lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
			var lineInfo = lineObj.getElementsByTagName("lineInfo")[0].text;
			var lineMenu = lineObj.getElementsByTagName("lineMenu")[0].text;
			var linetext = lineObj.getElementsByTagName("alias")[0].text;
	
			var line = document.createElement("v:line");
			line.lineid = lineObj.getAttribute("id");
			line.id = "line_" + a + "_"+ b + "#assistant";
			line.style.position = "absolute";
			line.style.zIndex = -1;
			line.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + 3) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2 + 3);
			line.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2 + 3) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2 + 3);
			line.strokecolor = color;
			line.strokeweight = lineWidth;
			
			if(linetext!="#.#"){
			    var divLineText = document.createElement("div");
				divLineText.id = "linetext_" + line.lineid;
				divLineText.style.position = "absolute";
				divLineText.style.width = "40";
				divLineText.style.height = "20";
				divLineText.style.left = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + b).style.left)-10)/2+3;
				divLineText.style.top = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2+3;
				divLineText.style.fontSize = "12px";
				divLineText.align = "center";
		//		divLineText.style.backgroundColor = "#9BFFAC";
		//	    divLineText.style.border="1px solid #007314";
		//	    divLineText.style.filter="Alpha(Opacity=60);";
				divLineText.innerHTML = linetext;// ��ʾ��·����
				
				document.all.divLayer.appendChild(divLineText);
			}
	
			// ///////////////yangjun add begin
			// ��ʾ��·��Ϣ
			var divLineInfo = document.createElement("div");
			divLineInfo.id = "info_" + a + "_"+ b + "#assistant";
			divLineInfo.style.position = "absolute";
			divLineInfo.style.border = infoBorder;
			divLineInfo.style.width = 200;
			divLineInfo.style.height = "auto";
			divLineInfo.style.color = infoFontColor;
			divLineInfo.style.padding = infoPadding;
			divLineInfo.style.display = "block";
			divLineInfo.style.lineHeight = "120%";
			divLineInfo.style.filter = "alpha(opacity=70)";
			divLineInfo.style.zIndex = 2;
			divLineInfo.style.backgroundColor = infoBgColor;
			divLineInfo.style.visibility = "hidden";
			divLineInfo.style.fontSize = "12px";
			divLineInfo.innerHTML = lineInfo;
			document.all.divLayer.appendChild(divLineInfo);
			var getCoordInDocument = function(e) {// ��ȡ��굱ǰλ��
	             e = e || window.event; 
	             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
	             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
	             return {'x':x,'y':y}; 
	        }
			line.onmousemove = function(e) { 
				                   var pos=getCoordInDocument();
			                       // window.event.srcElement.strokeweight =
									// parseInt(lineWidth)+1;
			                       window.event.srcElement.style.cursor = "hand";
			                       document.all(this.id.replace("line","info")).style.left = parseInt(pos['x']);
			                       document.all(this.id.replace("line","info")).style.top = parseInt(pos['y']);
			                       document.all(this.id.replace("line","info")).style.visibility = "visible";
			                   };
			line.onmouseout = function() { 
			                      // window.event.srcElement.strokeweight =
									// lineWidth;
			                      window.event.srcElement.style.cursor = "default"; 
			                      document.all(this.id.replace("line","info")).style.visibility = "hidden";
			                  };
			// ///////////////end
			// line.onclick = function() { showLineInfo() };
	
			document.all.divLayer.appendChild(line);
			document.all("node_" + a).lines += '&' + line.id;
			document.all("node_" + b).lines += '&' + line.id;
			// ��·�˵�
			var divMenu = document.createElement("div");
			divMenu.id = "menu_" + a + "_"+ b + "#assistant";
			divMenu.style.position = "absolute";
			divMenu.style.width = 120;
			divMenu.style.height = "auto";
			divMenu.style.zIndex = 2;
			divMenu.style.visibility = "hidden";
			divMenu.style.border = "solid #99A5A5 4px";
			divMenu.style.backgroundColor = "#CAE6FA";
			divMenu.style.filter = "alpha(opacity=70)";
			divMenu.style.padding = "1px";
			divMenu.style.lineHeight = "100%";
			divMenu.style.fontSize = "12px";
			divMenu.innerHTML = lineMenu;
			document.all.divLayer.appendChild(divMenu);
			// ������·�˵��Ĵ����¼�
			document.all("line_" + a + "_"+ b + "#assistant").oncontextmenu = function()
			{ 
				var pos=getCoordInDocument();
				document.all(this.id.replace("line","menu")).style.left = parseInt(pos['x']);
			    document.all(this.id.replace("line","menu")).style.top = parseInt(pos['y']);
				if(clickLineObj != null)
				{
					document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden";
					clickLineObj = null;
				}
				clickLineObj = this;
				document.all(this.id.replace("line", "info")).style.visibility = "hidden";
				document.all(this.id.replace("line", "menu")).style.visibility = "visible"; 			
		    };
			document.all("menu_" + a + "_"+ b + "#assistant").onclick = function() { this.style.visibility = "hidden"; };
			
			// ���� stroke ��ǩ
			// window.event.srcElement.stroke.dashstyle = "Solid";
			var stroke = document.createElement("v:stroke");
			stroke.dashstyle = dash;
			document.all(line.id).appendChild(stroke);
			
			addTopologyMapLine(line.lineid, a, b, line);
			return;
		}
	}
}
//ɾ��ʾ����·
function removeLines(lineid,url){

}
//ʾ����·
function addLines(lineid,url){
    // ����lines�ڵ�
	xmldoc = initXML(url);
	var lines = xmldoc.getElementsByTagName("demoLine");
	for (var j = lines.length-1; j >= 0; j--)
	{
		var lineObj = lines[j];
		var line_id = lineObj.getAttribute("id");
		if(line_id==lineid){
		    var a = lineObj.getElementsByTagName("a")[0].text;
			var b = lineObj.getElementsByTagName("b")[0].text;
			var color = lineObj.getElementsByTagName("color")[0].text;
			var dash = lineObj.getElementsByTagName("dash")[0].text;
			var lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
			var lineInfo = lineObj.getElementsByTagName("lineInfo")[0].text;
			var lineMenu = lineObj.getElementsByTagName("lineMenu")[0].text;
	
			var line = document.createElement("v:line");
			line.lineid = lineObj.getAttribute("id");
			line.id = "line_" + a + "_"+ b + "#demoline";
			line.style.position = "absolute";
			line.style.zIndex = -1;
			line.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 - 3) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2 - 3);
			line.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2 - 3) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2 - 3);
			line.strokecolor = color;
			line.strokeweight = lineWidth;
			// ///////////////yangjun add begin
			// ��ʾ��·��Ϣ
			var divLineInfo = document.createElement("div");
			divLineInfo.id = "info_" + a + "_"+ b + "#demoline";
			divLineInfo.style.position = "absolute";
			divLineInfo.style.border = infoBorder;
			divLineInfo.style.width = 200;
			divLineInfo.style.height = "auto";
			divLineInfo.style.color = infoFontColor;
			divLineInfo.style.padding = infoPadding;
			divLineInfo.style.display = "block";
			divLineInfo.style.lineHeight = "120%";
			divLineInfo.style.zIndex = 2;
			divLineInfo.style.backgroundColor = infoBgColor;
			divLineInfo.style.visibility = "hidden";
			divLineInfo.style.fontSize = "12px";
			divLineInfo.innerHTML = lineInfo;
			document.all.divLayer.appendChild(divLineInfo);
			var getCoordInDocument = function(e) {// ��ȡ��굱ǰλ��
	             e = e || window.event; 
	             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
	             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
	             return {'x':x,'y':y}; 
	        }
			line.onmousemove = function(e) { 
				                   var pos=getCoordInDocument();
			                       // window.event.srcElement.strokeweight =
									// parseInt(lineWidth)+1;
			                       window.event.srcElement.style.cursor = "hand";
			                       document.all(this.id.replace("line","info")).style.left = parseInt(pos['x']);
			                       document.all(this.id.replace("line","info")).style.top = parseInt(pos['y']);
			                       document.all(this.id.replace("line","info")).style.visibility = "visible";
			                   };
			line.onmouseout = function() { 
			                      // window.event.srcElement.strokeweight =
									// lineWidth;
			                      window.event.srcElement.style.cursor = "default"; 
			                      document.all(this.id.replace("line","info")).style.visibility = "hidden";
			                  };
			// ///////////////end
			// line.onclick = function() { showLineInfo() };
	
			document.all.divLayer.appendChild(line);
			document.all("node_" + a).lines += '&' + line.id;
			document.all("node_" + b).lines += '&' + line.id;
			// ��·�˵�
			var divMenu = document.createElement("div");
			divMenu.id = "menu_" + a + "_"+ b + "#demoline";
			divMenu.style.position = "absolute";
			divMenu.style.width = 120;
			divMenu.style.height = "auto";
			divMenu.style.zIndex = 2;
			divMenu.style.visibility = "hidden";
			divMenu.style.border = "solid #99A5A5 4px";
			divMenu.style.backgroundColor = "#CAE6FA";
			divMenu.style.padding = "1px";
			divMenu.style.lineHeight = "100%";
			divMenu.style.fontSize = "12px";
			divMenu.innerHTML = lineMenu;
			document.all.divLayer.appendChild(divMenu);
			// ������·�˵��Ĵ����¼�
			document.all("line_" + a + "_"+ b + "#demoline").oncontextmenu = function()
			{ 
				var pos=getCoordInDocument();
				document.all(this.id.replace("line","menu")).style.left = parseInt(pos['x']);
			    document.all(this.id.replace("line","menu")).style.top = parseInt(pos['y']);
				if(clickLineObj != null)
				{
					document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden";
					clickLineObj = null;
				}
				clickLineObj = this;
				document.all(this.id.replace("line", "info")).style.visibility = "hidden";
				document.all(this.id.replace("line", "menu")).style.visibility = "visible"; 			
		    };
			document.all("menu_" + a + "_"+ b + "#demoline").onclick = function() { this.style.visibility = "hidden"; };
			
			// ���� stroke ��ǩ
			// window.event.srcElement.stroke.dashstyle = "Solid";
			var stroke = document.createElement("v:stroke");
			stroke.dashstyle = dash;
			document.all(line.id).appendChild(stroke);
			
			addTopologyMapLine(line.lineid, a, b, line);
			return;
		}
	}
}
var nodeobjArray = [];
var linkobjArray = [];
var nodeobj = function(id,image,category,xmlname){
	this.id = id;
    this.category = category;
    this.image = image;
    this.xmlname = xmlname;
}
var linkObj = function(id,lineid,color,xmlname){
	this.id = id;
    this.color = color;
    this.lineid = lineid;
    this.xmlname = xmlname;
}
function replaceNodeobj(data){
    if(data!=null&&data.length>0){
    	for(var i = 0; i < data.length; i++){
    	     //alert(data[i].image);
    		document.all(data[i].id).src = "../../resource/"+data[i].image;
    	}
        //alert(data.length);
    }
}
function tt(data){
    //alert(data.length);
    if(data!=null&&data.length>0){
        for(var i = 0; i < data.length; i++){
    		document.all(data[i].id).strokecolor = data[i].color;
    	}
    }
}
// ����xml������������չ��
function parseData()
{	
    nodeobjArray = [];
	linkobjArray = [];
	// ����root�ڵ�
	var dateInfo = xmldoc.getElementsByTagName("root")[0].getAttribute("time");

	// ����nodes�ڵ�
	var nodes = xmldoc.getElementsByTagName("node");	
	for (var i = 0; i < nodes.length; i += 1)
	{
		var node = nodes[i];
		var id = node.getElementsByTagName("id")[0].text;
		var imgValue = "../../resource/" + node.getElementsByTagName("img")[0].text;
		var x = node.getElementsByTagName("x")[0].text;
		var y = node.getElementsByTagName("y")[0].text;
		var w = node.getElementsByTagName("width")[0].text;
		var h = node.getElementsByTagName("height")[0].text;
		var ip = node.getElementsByTagName("ip")[0].text
		var alias = node.getElementsByTagName("alias")[0].text;
		var info = "";//node.getElementsByTagName("info")[0].text;
		var menu = node.getElementsByTagName("menu")[0].text;
		var relationMap = node.getElementsByTagName("relationMap")[0].text;
		var nodeType = node.getElementsByTagName("id")[0].getAttribute("category");
		//���������õ����飬�������ʼ��---------��2---
		nodeCoorAry.push(ip + "," + x + "," + y);
		nodeIdAry.push(id + "," + x + "," + y);
		//---------------------------------------
		
		getConfine(x, y);		// ��ȡ���߽磬�� maxWidth��maxHeight ��ֵ

		// ��ʾ�豸
		//var img = document.createElement("<v:image lines=''></v:image>");
		//yangjun add begin ����ѡ��ͼԪ�ı���
//		var divBackGround = document.createElement("v:back");
//		divBackGround.id = "node_" + id;
//		divBackGround.name = relationMap;//��ͼ�ļ���
//		divBackGround.style.position = "absolute";
//		divBackGround.style.width = "60";
//		divBackGround.style.height = "60";
//		divBackGround.style.left = x;
//		divBackGround.style.top = y;
//		divBackGround.style.cursor = "hand";
//		divBackGround.style.padding = "3px";
//		document.all.divLayer.appendChild(divBackGround);
		//end
		//img.id = "image_" + id;//yangjun �޸�
		var img = document.createElement("v:image");
		img.id = "node_" + id;
		if(info=="ʾ���豸") {//yangjun add ����ʾ���豸
			img.name = relationMap+",1";//�ڵ��������ͼ�ļ�����ʾ���豸�жϱ��
		} else {
		    img.name = relationMap+",0";;//�ڵ��������ͼ�ļ�����ʾ���豸�жϱ��
		}
		
		//ͼƬ��ʵ�ʸ߶ȺͿ��
		img.style.width = w;
		img.style.height = h;
				
		img.style.position = "absolute";
		img.style.cursor = "hand";
		img.style.left = x;
		img.style.top = y;
/*		
		if(nodeType == "net_server")
		{
			img.style.width = 65;
			img.style.height = 26;
			aliasHSpace = 10;
			aliasVSpace = 26;			
		} else if(nodeType == "����")
		{
			img.style.width = 95;
			img.style.height = 371;
			aliasHSpace = 10;
			aliasVSpace = 371;
		} else if(nodeType == "����")
		{
			img.style.width = 10;
			img.style.height = 10;
			aliasHSpace = 5;
			aliasVSpace = 5
		} else if(nodeType == "������")
		{
			img.style.width = 65;
			img.style.height = 26;
			aliasHSpace = 5;
			aliasVSpace = 20
		} 
		else if(nodeType == "ups")
		{
			img.style.width = 68;
			img.style.height = 76;
			aliasHSpace = 10;
			aliasVSpace = 70
		}
		else if(nodeType == "weblogic")
		{
			img.style.width = 32;
			img.style.height = 21;
			aliasHSpace = 20;
			aliasVSpace = 22
		}
		else if(nodeType == "ftp")
		{
			img.style.width = 32;
			img.style.height = 32;
			aliasHSpace = 20;
			aliasVSpace = 33
		}
		else if(nodeType == "web")
		{
			img.style.width = 32;
			img.style.height = 32;
			aliasHSpace = 20;
			aliasVSpace = 34
		}
		else if(nodeType == "iis")
		{
			img.style.width = 30;
			img.style.height = 14;
			aliasHSpace = 30;
			aliasVSpace = 16
		}
		else if(nodeType == "mail")
		{
			img.style.width = 30;
			img.style.height = 20;
			aliasHSpace = 15;
			aliasVSpace = 21
		}
		else if(nodeType == "����")
		{
			img.style.width = 95;
			img.style.height = 371;
			aliasHSpace = 10;
			aliasVSpace = 371;
		}
		else if(nodeType == "����")
		{
			img.style.width = 10;
			img.style.height = 10;
			aliasHSpace = 5;
			aliasVSpace = 5
		}
		else if(nodeType == "netping")
		{
			img.style.width = 65;
			img.style.height = 26;
			aliasHSpace = 10;
			aliasVSpace = 26;			
		}
		else
		{
			img.style.width = 30;
			img.style.height = 30;
			aliasHSpace = 24;
			aliasVSpace = 28;
		}
		*/
		
		img.style.position = "absolute";
		img.style.cursor = "hand";
		img.src = imgValue;
		//divBackGround.appendChild(img);
		document.all.divLayer.appendChild(img);
		nodeobjArray[nodeobjArray.length] = new nodeobj(img.id,img.src,nodeType,fatherXML);

		// ��ʾ�豸�ı�
		var divText = document.createElement("div");
		divText.id = "text_" + id;
		divText.style.position = "absolute";
		divText.style.width = "80";
		divText.style.height = "20";
		
		//divText.style.left = parseInt(x, 10) - aliasHSpace;
		//divText.style.top = parseInt(y, 10) + aliasVSpace;
		divText.style.left = parseInt(x, 10) - 15;
		//alert(h);
		divText.style.top = parseInt(y, 10) +  parseInt(h, 10);		
		
		divText.style.fontSize = "12px";
		divText.align = "center";
		if (g_viewFlag == 0)
			divText.innerHTML = alias;
		else
			divText.innerHTML = ip;
		document.all.divLayer.appendChild(divText);
		// ��ʾ�豸��Ϣ
		var divInfo = document.createElement("div");
		divInfo.id = "info_" + id;
		divInfo.name = id;//alias+"("+ip+")";//yangjun add
		divInfo.style.position = "absolute";
		divInfo.style.border = infoBorder;
		divInfo.style.width = infoWidth;
		divInfo.style.height = "auto";
		divInfo.style.color = infoFontColor;
		divInfo.style.padding = infoPadding;
		divInfo.style.lineHeight = "120%";
		//divInfo.style.filter = "alpha(opacity=50)";
		divInfo.style.zIndex = 2;
		divInfo.style.backgroundColor = infoBgColor;
		divInfo.style.left = parseInt(x, 10) + 32;
		divInfo.style.top = parseInt(y, 10);
		divInfo.style.visibility = "hidden";
		divInfo.style.fontSize = "12px";
		divInfo.innerHTML = info;
		document.all.divLayer.appendChild(divInfo);

		// ����"�豸��Ϣ��ʾ"�¼�
		//document.all("node_" + id).onmouseover = function() { document.all(this.id.replace("node", "info")).style.visibility = "visible"; };
		document.all("node_" + id).onclick = (function(nodeType) { 
			return function(){
				//alert(this.id.replace("node_", ""));
			    var thisobj = document.all(this.id.replace("node", "info"));
			    TopoRemoteService.getShowMessage(this.id.replace("node_", ""),nodeType,fatherXML, {
					callback:function(data){
						if(data){
						    thisobj.innerHTML = data;
						}
					}
				});
				document.all(this.id.replace("node", "info")).style.visibility = "visible"; 
			};
		})(nodeType);
		document.all("node_" + id).onmouseout = function() { document.all(this.id.replace("node", "info")).style.visibility = "hidden"; };
        // ˫��ͼԪ�¼�yangjun
		document.all("node_" + id).ondblclick = function() {
			                                     if(this.name.substring(0,this.name.lastIndexOf(","))!=""){
			                                     	//alert(this.name.substring(0,this.name.lastIndexOf(",")));
			                                     	 if(this.name.substring(0,this.name.lastIndexOf(","))=="network.jsp"){
		                                         	     window.parent.parent.location = "../network/index.jsp";
		                                         	 }else{
		                                         	     window.parent.parent.location = "../submap/index.jsp?submapXml=" + this.name.substring(0,this.name.lastIndexOf(","));
		                                         	 }
			                                     }else{
			                                         nodeId = this.id.replace("node_", "");
			                                         //showalert(nodeId);
			                                         //window.parent.parent.opener.focus();
			                                         window.open("/afunms/detail/dispatcher.jsp?flag=1&id=" + nodeId,"fullScreenWindow"+ nodeId, "toolbar=no,target=_black,height="+window.screen.height + ","+"width=" + (window.screen.width-8)+ ",scrollbars=yes,resizable=yes"+"screenX=0,screenY=0");
			                                     }
		                                          
		                                     };
		// ����������ʾ�˵�
		var divMenu = document.createElement("div");
		divMenu.id = "menu_" + id;
		divMenu.style.position = "absolute";
		divMenu.style.width = "auto";
		divMenu.style.height = "auto";
		divMenu.style.zIndex = 2;
		divMenu.style.left = parseInt(x, 10) + 28;
		divMenu.style.top = parseInt(y, 10);
		divMenu.style.visibility = "hidden";
		divMenu.style.border = "solid gray 1px";
		divMenu.style.backgroundColor = "#ECECEC";
		//divMenu.style.filter = "alpha(opacity=70)";
		divMenu.style.padding = "1px";
		divMenu.style.lineHeight = "100%";
		divMenu.style.fontSize = "12px";
		divMenu.innerHTML = menu;
		document.all.divLayer.appendChild(divMenu);

		//���Ӳ˵��Ĵ����¼�
		document.all("node_" + id).oncontextmenu = function()
		{ 
			if(clickObj != null)
			{
				document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden";
				clickObj = null;
			}	
			if(objStyle != null)
			{
			  	unSelectImg(objStyle);
			    objStyle = null;
			}	
			clickObj = this;
			objStyle = this;
			selectImg(objStyle);
			document.all(this.id.replace("node", "info")).style.visibility = "hidden";
			document.all(this.id.replace("node", "menu")).style.visibility = "visible"; 			
	   };
	   
		document.all("menu_" + id).onclick = function() { this.style.visibility = "hidden"; };	
		//����"�϶�"�¼�
		document.all("node_" + id).onmousedown = down;
		document.onmousemove = null;
		document.all("node_" + id).onmouseup = up;
		
		addTopologyMapNode(id, img, divText, divInfo, divMenu);
	}

	// ����lines�ڵ�
	var lines = xmldoc.getElementsByTagName("line");
	for (var j = 0; j < lines.length; j += 1)
	{
		var lineObj = lines[j];
		var a = lineObj.getElementsByTagName("a")[0].text;
		var b = lineObj.getElementsByTagName("b")[0].text;
		var color = "green";//lineObj.getElementsByTagName("color")[0].text;
		var dash = lineObj.getElementsByTagName("dash")[0].text;
		var lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
        var lineInfo = "";//lineObj.getElementsByTagName("lineInfo")[0].text;
		var lineMenu = lineObj.getElementsByTagName("lineMenu")[0].text;
		var linetext = lineObj.getElementsByTagName("alias")[0].text;
		var startDescr = lineObj.getElementsByTagName("start")[0].text;
		var endDescr = lineObj.getElementsByTagName("end")[0].text;
		
		var line = document.createElement("v:line");
		line.lineid = lineObj.getAttribute("id");
		line.id = "line_" + a + "_"+ b;
		line.style.position = "absolute";
		line.style.zIndex = -1;
		line.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2);
		line.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2);
		line.strokecolor = color;
		line.strokeweight = lineWidth;//1;
		
		linkobjArray[linkobjArray.length] = new linkObj(line.id,line.lineid,line.strokecolor,fatherXML);
        /////////////////yangjun add begin
		//��ʾ��·��Ϣ
		if(linetext!="#.#"){
		    var divLineText = document.createElement("div");
			divLineText.id = "linetext_" + line.lineid;
			divLineText.style.position = "absolute";
			divLineText.style.width = "40";
			divLineText.style.height = "20";
			divLineText.style.left = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + parseInt(document.all("node_" + b).style.left)+parseInt(document.all("node_" + b).style.width)/2)/2-10;
			divLineText.style.top = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2;
			divLineText.style.fontSize = "11px";
			divLineText.align = "center";
	//		divLineText.style.backgroundColor = "#9BFFAC";
	//	    divLineText.style.border="1px solid #007314";
	//	    divLineText.style.filter="Alpha(Opacity=60);";
			divLineText.innerHTML = linetext;// ��ʾ��·����
			
			document.all.divLayer.appendChild(divLineText);
		}
		//��ʾ���ӿ�����
		if(startDescr!="##"){
			var xx = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + parseInt(document.all("node_" + b).style.left)+parseInt(document.all("node_" + b).style.width)/2)/2;
		    var yy = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2;
		    var divLine_s = document.createElement("div");
			divLine_s.id = "line_s_" + line.lineid;
			divLine_s.style.position = "absolute";
			divLine_s.style.width = "60";
			divLine_s.style.height = "20";
			divLine_s.style.left = (parseInt(document.all("node_" + a).style.left) + xx)/2-10;
			divLine_s.style.top = (parseInt(document.all("node_" + a).style.top)+ yy)/2;
			divLine_s.style.fontSize = "10px";
			divLine_s.align = "center";
	//		divLineText.style.backgroundColor = "#9BFFAC";
	//	    divLineText.style.border="1px solid #007314";
	//	    divLineText.style.filter="Alpha(Opacity=60);";
			divLine_s.innerHTML = startDescr;// ��ʾ��·�ӿ�����
			
			document.all.divLayer.appendChild(divLine_s);
		}
		//��ʾ�յ�ӿ�����
		if(endDescr!="##"){
			var xx = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + parseInt(document.all("node_" + b).style.left)+parseInt(document.all("node_" + b).style.width)/2)/2;
		    var yy = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2;
		    var divLine_e = document.createElement("div");
			divLine_e.id = "line_e_" + line.lineid;
			divLine_e.style.position = "absolute";
			divLine_e.style.width = "60";
			divLine_e.style.height = "20";
			divLine_e.style.left = (parseInt(document.all("node_" + b).style.left)+xx)/2-10;
			divLine_e.style.top = (parseInt(document.all("node_" + b).style.top)+yy)/2;
			divLine_e.style.fontSize = "10px";
			divLine_e.align = "center";
	//		divLineText.style.backgroundColor = "#9BFFAC";
	//	    divLineText.style.border="1px solid #007314";
	//	    divLineText.style.filter="Alpha(Opacity=60);";
			divLine_e.innerHTML = endDescr;// ��ʾ��·�ӿ�����
			
			document.all.divLayer.appendChild(divLine_e);
		}
		// ��ʾ��·��Ϣ
		var divLineInfo = document.createElement("div");
		divLineInfo.id = "info_" + a + "_"+ b;
		divLineInfo.style.position = "absolute";
		divLineInfo.style.border = infoBorder;
		divLineInfo.style.width = 200;
		divLineInfo.style.height = "auto";
		divLineInfo.style.color = infoFontColor;
		divLineInfo.style.padding = infoPadding;
		divLineInfo.style.display = "block";
		divLineInfo.style.lineHeight = "120%";
		//divLineInfo.style.filter = "alpha(opacity=70)";
		divLineInfo.style.zIndex = 2;
		divLineInfo.style.backgroundColor = infoBgColor;
		divLineInfo.style.visibility = "hidden";
		divLineInfo.style.fontSize = "12px";
		divLineInfo.innerHTML = lineInfo;
		document.all.divLayer.appendChild(divLineInfo);
		var getCoordInDocument = function(e) {//��ȡ��굱ǰλ��
             e = e || window.event; 
             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
             return {'x':x,'y':y}; 
        }
		line.onclick = (function(lineid){
				return function(e) { 
				   // alert(lineid);
                   var thisobj = document.all(this.id.replace("line", "info"));
                   LinkRemoteService.getShowMessage(lineid, {
						callback:function(data){
							if(data){
							    thisobj.innerHTML = data;
							}
						}
					});
                   var pos=getCoordInDocument();
                   // window.event.srcElement.strokeweight =
					// parseInt(lineWidth)+1;
                   window.event.srcElement.style.cursor = "hand";
                   // window.event.srcElement.style.filter="Alpha(Opacity=60);";
                   document.all(this.id.replace("line","info")).style.left = parseInt(pos['x'])-parseInt(document.all.divLayer.style.left.replace("px",""));
                   document.all(this.id.replace("line","info")).style.top = parseInt(pos['y'])-parseInt(document.all.divLayer.style.top.replace("px",""));
                   document.all(this.id.replace("line","info")).style.visibility = "visible";
           		}; 
		})(line.lineid);
		line.onmouseout = function() { 
		                      //window.event.srcElement.strokeweight = lineWidth;//1; 
		                      window.event.srcElement.style.cursor = "default"; 
		                      document.all(this.id.replace("line","info")).style.visibility = "hidden";
		                  };
		/////////////////end

		document.all.divLayer.appendChild(line);
		document.all("node_" + a).lines += '&' + line.id;
		document.all("node_" + b).lines += '&' + line.id;
		//��·�˵�
		var divMenu = document.createElement("div");
		divMenu.id = "menu_" + a + "_"+ b;
		divMenu.style.position = "absolute";
		divMenu.style.width = 120;
		divMenu.style.height = "auto";
		divMenu.style.zIndex = 2;
		divMenu.style.visibility = "hidden";
		divMenu.style.border = "solid gray 1px";
		divMenu.style.backgroundColor = "#ECECEC";
		//divMenu.style.filter = "alpha(opacity=70)";
		divMenu.style.padding = "1px";
		divMenu.style.lineHeight = "100%";
		divMenu.style.fontSize = "12px";
		divMenu.innerHTML = lineMenu;
		document.all.divLayer.appendChild(divMenu);
		//������·�˵��Ĵ����¼�
		document.all("line_" + a + "_"+ b).oncontextmenu = function()
		{ 
			var pos=getCoordInDocument();
			document.all(this.id.replace("line","menu")).style.left = parseInt(pos['x']);
		    document.all(this.id.replace("line","menu")).style.top = parseInt(pos['y']);
			if(clickLineObj != null)
			{
				document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden";
				clickLineObj = null;
			}
			clickLineObj = this;
			document.all(this.id.replace("line", "info")).style.visibility = "hidden";
			document.all(this.id.replace("line", "menu")).style.visibility = "visible"; 			
	    };
	    document.all("menu_" + a + "_"+ b).onclick = function() { this.style.visibility = "hidden"; };
		// ���� stroke ��ǩ
		//window.event.srcElement.stroke.dashstyle = "Solid";
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = dash;
		document.all(line.id).appendChild(stroke);
		
		addTopologyMapLine(line.lineid, a, b, line);
	}
		
	// ����lines�ڵ�
	var lines = xmldoc.getElementsByTagName("assistant_line");
	for (var j = 0; j < lines.length; j += 1)
	{
		var lineObj = lines[j];
		var a = lineObj.getElementsByTagName("a")[0].text;
		var b = lineObj.getElementsByTagName("b")[0].text;
		var color = "blue";//lineObj.getElementsByTagName("color")[0].text;
		var dash = lineObj.getElementsByTagName("dash")[0].text;
		var lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
		var lineInfo = "";//lineObj.getElementsByTagName("lineInfo")[0].text;
		var lineMenu = lineObj.getElementsByTagName("lineMenu")[0].text;
		var linetext = lineObj.getElementsByTagName("alias")[0].text;
		var startDescr = lineObj.getElementsByTagName("start")[0].text;
		var endDescr = lineObj.getElementsByTagName("end")[0].text;

		var line = document.createElement("v:line");
		line.lineid = lineObj.getAttribute("id");
		line.id = "line_" + a + "_"+ b + "#assistant";
		line.style.position = "absolute";
		line.style.zIndex = -1;
		//line.from = (parseInt(document.all("node_" + a).style.left) + 20) + "," + (parseInt(document.all("node_" + a).style.top) + 10);
		//line.to = (parseInt(document.all("node_" + b).style.left) + 20) + "," + (parseInt(document.all("node_" + b).style.top) + 10);
		line.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + 3) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2 + 3);
		line.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2 + 3) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2 + 3);
		line.strokecolor = color;
		line.strokeweight = lineWidth;
		
		linkobjArray[linkobjArray.length] = new linkObj(line.id,line.lineid,line.strokecolor,fatherXML);
		
		if(linetext!="#.#"){
		    var divLineText = document.createElement("div");
			divLineText.id = "linetext_" + line.lineid;
			divLineText.style.position = "absolute";
			divLineText.style.width = "40";
			divLineText.style.height = "20";
			divLineText.style.left = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + parseInt(document.all("node_" + b).style.left)+parseInt(document.all("node_" + b).style.width)/2)/2-10;
			divLineText.style.top = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2;
			divLineText.style.fontSize = "11px";
			divLineText.align = "center";
	//		divLineText.style.backgroundColor = "#9BFFAC";
	//	    divLineText.style.border="1px solid #007314";
	//	    divLineText.style.filter="Alpha(Opacity=60);";
			divLineText.innerHTML = linetext;// ��ʾ��·����
			
			document.all.divLayer.appendChild(divLineText);
		}
        
		/////////////////yangjun add begin
		//��ʾ���ӿ�����
		if(startDescr!="##"){
			var xx = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + parseInt(document.all("node_" + b).style.left)+parseInt(document.all("node_" + b).style.width)/2)/2;
		    var yy = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2;
		    var divLineText = document.createElement("div");
			divLineText.id = "line_s_" + line.lineid;
			divLineText.style.position = "absolute";
			divLineText.style.width = "60";
			divLineText.style.height = "20";
			divLineText.style.left = (parseInt(document.all("node_" + a).style.left) + xx)/2-10;
			divLineText.style.top = (parseInt(document.all("node_" + a).style.top)+ yy)/2;
			divLineText.style.fontSize = "10px";
			divLineText.align = "center";
	//		divLineText.style.backgroundColor = "#9BFFAC";
	//	    divLineText.style.border="1px solid #007314";
	//	    divLineText.style.filter="Alpha(Opacity=60);";
			divLineText.innerHTML = startDescr;// ��ʾ��·�ӿ�����
			
			document.all.divLayer.appendChild(divLineText);
		}
		//��ʾ�յ�ӿ�����
		if(endDescr!="##"){
			var xx = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 + parseInt(document.all("node_" + b).style.left)+parseInt(document.all("node_" + b).style.width)/2)/2;
		    var yy = (parseInt(document.all("node_" + a).style.top)+ parseInt(document.all("node_" + a).style.height)/2 + parseInt(document.all("node_" + b).style.top)+parseInt(document.all("node_" + b).style.height)/2)/2;
		    var divLineText = document.createElement("div");
			divLineText.id = "line_e_" + line.lineid;
			divLineText.style.position = "absolute";
			divLineText.style.width = "60";
			divLineText.style.height = "20";
			divLineText.style.left = (parseInt(document.all("node_" + b).style.left)+xx)/2-10;
			divLineText.style.top = (parseInt(document.all("node_" + b).style.top)+yy)/2;
			divLineText.style.fontSize = "10px";
			divLineText.align = "center";
	//		divLineText.style.backgroundColor = "#9BFFAC";
	//	    divLineText.style.border="1px solid #007314";
	//	    divLineText.style.filter="Alpha(Opacity=60);";
			divLineText.innerHTML = endDescr;// ��ʾ��·�ӿ�����
			
			document.all.divLayer.appendChild(divLineText);
		}
		// ��ʾ��·��Ϣ
		var divLineInfo = document.createElement("div");
		divLineInfo.id = "info_" + a + "_"+ b + "#assistant";
		divLineInfo.style.position = "absolute";
		divLineInfo.style.border = infoBorder;
		divLineInfo.style.width = 200;
		divLineInfo.style.height = "auto";
		divLineInfo.style.color = infoFontColor;
		divLineInfo.style.padding = infoPadding;
		divLineInfo.style.display = "block";
		divLineInfo.style.lineHeight = "120%";
		//divLineInfo.style.filter = "alpha(opacity=70)";
		divLineInfo.style.zIndex = 2;
		divLineInfo.style.backgroundColor = infoBgColor;
		divLineInfo.style.visibility = "hidden";
		divLineInfo.style.fontSize = "12px";
		divLineInfo.innerHTML = lineInfo;
		document.all.divLayer.appendChild(divLineInfo);
		var getCoordInDocument = function(e) {//��ȡ��굱ǰλ��
             e = e || window.event; 
             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
             return {'x':x,'y':y}; 
        }
		line.onclick = (function(lineid){
				return function(e) { 
                   var thisobj = document.all(this.id.replace("line", "info"));
                   LinkRemoteService.getShowMessage(lineid, {
						callback:function(data){
							if(data){
							    thisobj.innerHTML = data;
							}
						}
					});
                   var pos=getCoordInDocument();
                   // window.event.srcElement.strokeweight =
					// parseInt(lineWidth)+1;
                   window.event.srcElement.style.cursor = "hand";
                   // window.event.srcElement.style.filter="Alpha(Opacity=60);";
                   document.all(this.id.replace("line","info")).style.left = parseInt(pos['x'])-parseInt(document.all.divLayer.style.left.replace("px",""));
                   document.all(this.id.replace("line","info")).style.top = parseInt(pos['y'])-parseInt(document.all.divLayer.style.top.replace("px",""));
                   document.all(this.id.replace("line","info")).style.visibility = "visible";
           		}; 
		})(line.lineid);
		line.onmouseout = function() { 
		                      //window.event.srcElement.strokeweight = lineWidth; 
		                      window.event.srcElement.style.cursor = "default"; 
		                      document.all(this.id.replace("line","info")).style.visibility = "hidden";
		                  };
		/////////////////end
		//line.onclick = function() { showLineInfo() };

		document.all.divLayer.appendChild(line);
		document.all("node_" + a).lines += '&' + line.id;
		document.all("node_" + b).lines += '&' + line.id;
		//��·�˵�
		var divMenu = document.createElement("div");
		divMenu.id = "menu_" + a + "_"+ b + "#assistant";
		divMenu.style.position = "absolute";
		divMenu.style.width = 120;
		divMenu.style.height = "auto";
		divMenu.style.zIndex = 2;
		divMenu.style.visibility = "hidden";
		divMenu.style.border = "solid gray 1px";
		divMenu.style.backgroundColor = "#ECECEC";
		//divMenu.style.filter = "alpha(opacity=70)";
		divMenu.style.padding = "1px";
		divMenu.style.lineHeight = "100%";
		divMenu.style.fontSize = "12px";
		divMenu.innerHTML = lineMenu;
		document.all.divLayer.appendChild(divMenu);
		//������·�˵��Ĵ����¼�
		document.all("line_" + a + "_"+ b + "#assistant").oncontextmenu = function()
		{ 
			var pos=getCoordInDocument();
			document.all(this.id.replace("line","menu")).style.left = parseInt(pos['x']);
		    document.all(this.id.replace("line","menu")).style.top = parseInt(pos['y']);
			if(clickLineObj != null)
			{
				document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden";
				clickLineObj = null;
			}
			clickLineObj = this;
			document.all(this.id.replace("line", "info")).style.visibility = "hidden";
			document.all(this.id.replace("line", "menu")).style.visibility = "visible"; 			
	    };
		document.all("menu_" + a + "_"+ b + "#assistant").onclick = function() { this.style.visibility = "hidden"; };
		
		// ���� stroke ��ǩ
		//window.event.srcElement.stroke.dashstyle = "Solid";
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = dash;
		document.all(line.id).appendChild(stroke);
		
		addTopologyMapLine(line.lineid, a, b, line);
	}
	
	// ����lines�ڵ�
	var lines = xmldoc.getElementsByTagName("demoLine");
	for (var j = 0; j < lines.length; j += 1)
	{
		var lineObj = lines[j];
		var a = lineObj.getElementsByTagName("a")[0].text;
		var b = lineObj.getElementsByTagName("b")[0].text;
		var color = lineObj.getElementsByTagName("color")[0].text;
		var dash = lineObj.getElementsByTagName("dash")[0].text;
		var lineWidth = lineObj.getElementsByTagName("lineWidth")[0].text;
		var lineInfo = lineObj.getElementsByTagName("lineInfo")[0].text;
		var lineMenu = lineObj.getElementsByTagName("lineMenu")[0].text;

		var line = document.createElement("v:line");
		line.lineid = lineObj.getAttribute("id");
		line.id = "line_" + a + "_"+ b + "#demoline";
		line.style.position = "absolute";
		line.style.zIndex = -1;
		//line.from = (parseInt(document.all("node_" + a).style.left) + 25) + "," + (parseInt(document.all("node_" + a).style.top) + 12);
		//line.to = (parseInt(document.all("node_" + b).style.left) + 25) + "," + (parseInt(document.all("node_" + b).style.top) + 12);
		line.from = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2);
		line.to = (parseInt(document.all("node_" + b).style.left) + parseInt(document.all("node_" + b).style.width)/2) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2);
		line.strokecolor = color;   
		line.strokeweight = lineWidth;
		
		//var line = document.createElement("v:PolyLine");
		//line.lineid = lineObj.getAttribute("id");
		//line.id = "line_" + a + "_"+ b + "#demoline";
		//line.style.filled = "false";
		//line.style.position = "absolute";
		//line.style.zIndex = -1;
		//line.Points = (parseInt(document.all("node_" + a).style.left) + parseInt(document.all("node_" + a).style.width)/2 - 3) + "," + (parseInt(document.all("node_" + a).style.top) + parseInt(document.all("node_" + a).style.height)/2 - 3)+" "+(parseInt(document.all("node_" + b).style.left)-2) + "," + (parseInt(document.all("node_" + b).style.top) + parseInt(document.all("node_" + b).style.height)/2 - 3);
		//line.strokecolor = color;   
		//line.strokeweight = lineWidth;

		/////////////////yangjun add begin
		// ��ʾ��·��Ϣ
		var divLineInfo = document.createElement("div");
		divLineInfo.id = "info_" + a + "_"+ b + "#demoline";
		divLineInfo.style.position = "absolute";
		divLineInfo.style.border = infoBorder;
		divLineInfo.style.width = 200;
		divLineInfo.style.height = "auto";
		divLineInfo.style.color = infoFontColor;
		divLineInfo.style.padding = infoPadding;
		divLineInfo.style.display = "block";
		divLineInfo.style.lineHeight = "120%";
		//divLineInfo.style.filter = "alpha(opacity=70)";
		divLineInfo.style.zIndex = 2;
		divLineInfo.style.backgroundColor = infoBgColor;
		divLineInfo.style.visibility = "hidden";
		divLineInfo.style.fontSize = "12px";
		divLineInfo.innerHTML = lineInfo;
		document.all.divLayer.appendChild(divLineInfo);
		var getCoordInDocument = function(e) {//��ȡ��굱ǰλ��
             e = e || window.event; 
             var x = e.pageX || (e.clientX + (document.documentElement.scrollLeft|| document.body.scrollLeft)); 
             var y= e.pageY || (e.clientY + (document.documentElement.scrollTop || document.body.scrollTop));   
             return {'x':x,'y':y}; 
        }
		line.onclick = function(e) { 
			                   var pos=getCoordInDocument();
		                       //window.event.srcElement.strokeweight = parseInt(lineWidth)+1; 
		                       window.event.srcElement.style.cursor = "hand";
		                       document.all(this.id.replace("line","info")).style.left = parseInt(pos['x']);
		                       document.all(this.id.replace("line","info")).style.top = parseInt(pos['y']);
		                       document.all(this.id.replace("line","info")).style.visibility = "visible";
		                   };
		line.onmouseout = function() { 
		                      //window.event.srcElement.strokeweight = lineWidth; 
		                      window.event.srcElement.style.cursor = "default"; 
		                      document.all(this.id.replace("line","info")).style.visibility = "hidden";
		                  };
		line.onmousedown =function(){
		                       //alert(line.id);
		                       //objFocusedOn(this.id);
		                   };
		/////////////////end
		//line.onclick = function() { showLineInfo() };

		document.all.divLayer.appendChild(line);
		document.all("node_" + a).lines += '&' + line.id;
		document.all("node_" + b).lines += '&' + line.id;
		//��·�˵�
		var divMenu = document.createElement("div");
		divMenu.id = "menu_" + a + "_"+ b + "#demoline";
		divMenu.style.position = "absolute";
		divMenu.style.width = 120;
		divMenu.style.height = "auto";
		divMenu.style.zIndex = 2;
		divMenu.style.visibility = "hidden";
		divMenu.style.border = "solid gray 1px";
		divMenu.style.backgroundColor = "#ECECEC";
		//divMenu.style.filter = "alpha(opacity=70)";
		divMenu.style.padding = "1px";
		divMenu.style.lineHeight = "100%";
		divMenu.style.fontSize = "12px";
		divMenu.innerHTML = lineMenu;
		document.all.divLayer.appendChild(divMenu);
		//������·�˵��Ĵ����¼�
		document.all("line_" + a + "_"+ b + "#demoline").oncontextmenu = function()
		{ 
			var pos=getCoordInDocument();
			document.all(this.id.replace("line","menu")).style.left = parseInt(pos['x']);
		    document.all(this.id.replace("line","menu")).style.top = parseInt(pos['y']);
			if(clickLineObj != null)
			{
				document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden";
				clickLineObj = null;
			}
			clickLineObj = this;
			document.all(this.id.replace("line", "info")).style.visibility = "hidden";
			document.all(this.id.replace("line", "menu")).style.visibility = "visible"; 			
	    };
		document.all("menu_" + a + "_"+ b + "#demoline").onclick = function() { this.style.visibility = "hidden"; };
		
		// ���� stroke ��ǩ
		//window.event.srcElement.stroke.dashstyle = "Solid";
		var stroke = document.createElement("v:stroke");
		stroke.dashstyle = dash;
		//stroke.StartArrow = "none";
		//stroke.EndArrow = "Classic";
		document.all(line.id).appendChild(stroke);
		
		addTopologyMapLine(line.lineid, a, b, line);
	}
	
	var nodeNum = nodes.length;
	if (nodeNum == 0)
	{
//		document.write("<br/><br/><br/><br/><br/>");
//		document.write("<center>��û��ѡ��۲������ͼ����ѡ����ȷ������ͼ</center>");
	}
	zoomProcDlg("out");//�ر�����
}
//end of ParseData();




//handle node onmousemove event, �����豸�ƶ��¼�
//�������豸���ƶ� X (����Ϊ�����ƶ�������Ϊ�����ƶ�)�� �ƶ� Y (����Ϊ�����ƶ�������Ϊ�����ƶ�)
function handleTopologyNodeMove(topologyMapNode, moveX, moveY) {
      var imageStyle = topologyMapNode.imageElement.style;
      var textStyle = topologyMapNode.divTextElement.style;
      var beforeMoveX = topologyMapNode.beforeMoveX;
      var beforeMoveY = topologyMapNode.beforeMoveY;
      var tempX = beforeMoveX + moveX;
      var tempY = beforeMoveY + moveY;
      imageStyle.left = tempX;
      imageStyle.top = tempY;
      textStyle.left = tempX - 24;
      textStyle.top = tempY + 28;
//      // #########������ק�豸ʱ���豸��Ϣ��ʾ����Խ�紦���������߽������Զ���ʾ�����ӷ�Χ֮��   HONGLI ADD #########
//      if (this.divInfoElement == null) {
//          this.divInfoElement = this.createElement("info_" + topologyMapNode.id, "div");
//          this.divInfoElement.style.visibility = "hidden";
//          this.divLayer.appendChild(this.divInfoElement);
//      }
      var divInfo = topologyMapNode.divInfoElement;
      divInfo.style.left = tempX + 32;
      divInfo.style.top = tempY;
      if(parseInt(tempX, 10) > document.body.clientWidth - infoWidth - 24) {
          divInfo.style.left = parseInt(tempX, 10)- infoWidth;
      }
      if(parseInt(tempY, 10) > document.body.clientHeight - divInfo.clientHeight) {  
          divInfo.style.top = parseInt(tempY, 10)- divInfo.clientHeight;  
      }
      
      var divMenu = topologyMapNode.divMenuElement;
      divMenu.style.left = tempX + 32;
      divMenu.style.top = tempY;
      if(parseInt(tempX, 10) > document.body.clientWidth - divMenu.clientWidth - 24) {
          divMenu.style.left = parseInt(tempX, 10)- divMenu.clientWidth;
      }
      if(parseInt(tempY, 10) > document.body.clientHeight - divMenu.clientHeight) {  
          divMenu.style.top = parseInt(tempY, 10)- divMenu.clientHeight;  
      }
//      
//      // #############HONG ADD END##########
      // ############# ������·λ�� ########
      var iLeft = parseInt(imageStyle.left) + 15;
      var iTop = parseInt(imageStyle.top) + 8;
      var lineNum = lineArray.length;
      var i = 0;
      while (i < lineNum) {
          var topologyMapLine = lineArray[i];
          if (topologyMapLine.a == topologyMapNode.id) {
              topologyMapLine.lineElement.from = iLeft + "," + iTop;
          }
          if (topologyMapLine.b == topologyMapNode.id) {
              topologyMapLine.lineElement.to = iLeft + "," + iTop;
          }
          i = i + 1;
      }
//      
//      var assistantlineNum = this.topologyMapAssistantLines.length;
//      var j = 0;
//      while (j < assistantlineNum) {
//          var topologyMapAssistantLine = this.topologyMapAssistantLines[j];
//          if (topologyMapAssistantLine.a == topologyMapNode.id) {
//              topologyMapAssistantLine.lineElement.from = (parseInt(iLeft)+5) + "," + iTop;
//          }
//          if (topologyMapAssistantLine.b == topologyMapNode.id) {
//              topologyMapAssistantLine.lineElement.to = (parseInt(iLeft)+5) + "," + iTop;
//          }
//          j = j + 1;
//      }
//      
//      var demoLineNum = this.topologyMapDemoLines.length;
//      var k = 0;
//      while (k < demoLineNum) {
//          var topologyMapDemoLine = this.topologyMapDemoLines[k];
//          if (topologyMapDemoLine.a == topologyMapNode.id) {
//              topologyMapDemoLine.lineElement.from = (parseInt(iLeft)+8) + "," + iTop;
//          }
//          if (topologyMapDemoLine.b == topologyMapNode.id) {
//              topologyMapDemoLine.lineElement.to = (parseInt(iLeft)+8) + "," + iTop;
//          }
//          k = k + 1;
//      }
}

function setSelect() {
    setDivLayerMouserDownAction("select");
}

function setDrag() {
    setDivLayerMouserDownAction("drag");
}

function setDivLayerMouserDownAction(action) {
    
    //document.write('<div id="divSelect" style="background-color: #9BFFAC;width:0px;height:0px; top:0px;left:0px; position:absolute;z-index:99;visibility:visible;filter:Alpha(opacity=50);" ></div>');

    if (!$("#divSelect").attr("id")) {
        $("#divLayer").append('<div id="divSelect" style="background-color: #9BFFAC;width:0px;height:0px; top:0px;left:0px; position:absolute;z-index:99;visibility:visible;filter:Alpha(opacity=50);" ></div>');
        document.getElementById("divLayer").oncontextmenu = function(event) {
            setSelect();
        };
        document.getElementById("divDrag").oncontextmenu = function(event) {
            setSelect();
        };
    }
    $(document).mouseup(function(event) {
        $(document).unbind("mousemove");
        $("#divLayer").unbind("mousemove");
        $("#divDrag").unbind("mousemove");
        $("#divSelect").unbind("mousemove");
        $("#divLayer").css("cursor","default");
        $("#divDrag").css("cursor","default");
        $("#divSelect").css("cursor","default");
    });
    $("#divLayer").unbind("mousedown");
    $("#divDrag").unbind("mousedown");
    if (action == "select") {
        $("#divLayer").mousedown(function(event) {
            selectStart(event);
        });
        $("#divDrag").mousedown(function(event) {
            selectStart(event);
        });
    } else {
        $("#divLayer").mousedown(function(event) {
            dragStart(event);
        });
        $("#divDrag").mousedown(function(event) {
            dragStart(event);
        });
    }
}

// ������ק�հ״�
function dragStart(event) {
    if (event.target.id == $("#divLayer").attr("id") || event.target.id == $("#divDrag").attr("id") ||
        event.target.tagName == "BODY") {
        var divLayer = $("#divLayer");
        var divDrag = $("#divDrag");
        divLayer.css("cursor","move");
        divDrag.css("cursor","move");
        var offset = divLayer.offset();          //DIV��ҳ���λ��
        
        var x = 0;
        var y = 0;
        if (event.type == "mousedown") {
            x = event.pageX - offset.left;                  //������ָ����DIVԪ����߽�ľ���
            y = event.pageY - offset.top;                   //������ָ����DIVԪ���ϱ߽�ľ���
        }
        $(document).mousemove(function(ev) {    //�������ƶ��¼�����Ϊ�����DIVԪ������ҲҪ��Ч��������Ҫ��doucment���¼���������DIVԪ�ص��¼�
            $("#divLayer").stop();//�������֮��
            $("#divDrag").stop();//�������֮��

            var _x = 0;
            var _y = 0;
            if (ev.type == "touchmove") {
                ev.preventDefault();
                var touch = ev.originalEvent.touches[0] || ev.originalEvent.changedTouches[0];

                _x = touch.pageX - x;//���X�᷽���ƶ���ֵ
                _y = touch.pageY - y;//���Y�᷽���ƶ���ֵ
            } else if (ev.type == "mousemove") {
                _x = ev.pageX - x;//���X�᷽���ƶ���ֵ
                _y = ev.pageY - y;//���Y�᷽���ƶ���ֵ
            }
            $("#divLayer").animate({left:_x+"px",top:_y+"px"}, 15);
        });
    }
}


// ����ѡ��
function selectStart(event)
{
    if (event.target.id == $("#divLayer").attr("id") || event.target.id == $("#divDrag").attr("id") ||
        event.target.tagName == "BODY") {
        
        // ��������ѡ���С��λ��
        
        var offset = $("#divLayer").offset();          //DIV��ҳ���λ��
        
        var x = 0;
        var y = 0;
        if (event.type == "mousedown") {
            x = event.pageX - offset.left;                  //������ָ����DIVԪ����߽�ľ���
            y = event.pageY - offset.top;                   //������ָ����DIVԪ���ϱ߽�ľ���
        }
        $("#divSelect").css({height: 0 + "px", width: 0 + "px"});
        $("#divSelect").offset({ top: y, left: x });
        $("#divSelect").css({ visibility: "visible"});
        
        // ȡ�����б�ѡ����豸
        cancelAllSelectedNode();

        // ������ƶ��¼�
        $(document).mousemove(function(eventMove) {    //�������ƶ��¼�����Ϊ�����DIVԪ������ҲҪ��Ч��������Ҫ��doucment���¼���������DIVԪ�ص��¼�
            handleSelect(eventMove, x, y);
        });
        $(document).mouseup(function(eventUp) {
            setDrag();
        });
        // 
        $("#divSelect").mousedown(function(eventDown) {
            handleDivSelectedMouseDown(eventDown);
        });
        // ��ѡ�����
//        $("#divSelect").mouseup(function (eventUp) {
//            handleDivSelectedMouseUp(eventUp);
//        });
    }
}

// ����ѡ���С
function handleSelect(event, x, y) {
    
    var offset = $("#divLayer").offset();          //DIV��ҳ���λ��
    
    var _x = 0;
    var _y = 0;
    var left = x;
    var top = y;
    var width = 0;
    var height = 0;
    if (event.type == "mousemove") {
        _x = event.pageX - offset.left;                  //������ָ����DIVԪ����߽�ľ���
        _y = event.pageY - offset.top;                   //������ָ����DIVԪ���ϱ߽�ľ���
        left = Math.min(x, _x);
        top = Math.min(y, _y);
        width = Math.abs(x - _x);
        height = Math.abs(y - _y);
    }
    $("#divSelect").css({left:left+"px",top:top+"px", height:height+"px",width:width+"px"});
    var nodeNum = nodeArray.length;
    var i = 0;
    while (i < nodeNum) {
        var topologyMapNode = nodeArray[i];
        var isInSelect = checkNodeIsInSelect(topologyMapNode);
        if (isInSelect) {
            addSelectedNode(topologyMapNode);
        } else {
            cancelSelectedNode(topologyMapNode);
        }
        i++;
    }
}

// ����ѡ��DIV����갴���¼���ͬʱ��Ҫ��ѡ������ƶ��¼�
function handleDivSelectedMouseDown(event) {
    var divSelect = $("#divSelect");
    if (event.target.id == divSelect.attr("id")) {
        divSelect.css("cursor","move");
        var offset = divSelect.offset();          //DIV��ҳ���λ��
        
        var divLayerOffset = $("#divLayer").offset();
        
        var eventX = 0;
        var eventY = 0;
        var divSelectBeforeMoveX = 0;
        var divSelectBeforeMoveY = 0;
        if (event.type == "mousedown") {
            var eventX = event.pageX;
            var eventY = event.pageY;
            var divSelectBeforeMoveX = offset.left - divLayerOffset.left;
            var divSelectBeforeMoveY = offset.top - divLayerOffset.top;
        }

        // �ƶ��豸֮ǰ�������������б�ѡ���豸���ƶ�֮ǰ�� X Y λ��
        var isSelectedNum = nodeSelectedArray.length;
        var i = 0;
        while (i < isSelectedNum) {
            var isSelectedTopologyMapNode = nodeSelectedArray[i];
            var beforeMoveX = isSelectedTopologyMapNode.imageElement.offsetLeft;
            var beforeMoveY = isSelectedTopologyMapNode.imageElement.offsetTop;
            isSelectedTopologyMapNode.beforeMoveX = beforeMoveX;
            isSelectedTopologyMapNode.beforeMoveY = beforeMoveY;
            i++;
        }
        $(document).mousemove(function(eventMove) {
            handleDivSelectMove(eventMove, divSelectBeforeMoveX, divSelectBeforeMoveY, eventX, eventY);
        });
    }
}

// ����ѡ���ƶ���ͬʱ��Ҫ�ƶ����б���ѡ���豸
function handleDivSelectMove(event, beforeMoveX, beforeMoveY, eventX, eventY) {
    $("#divSelect").stop();
    var moveX = 0;
    var moveY = 0;
    var left = 0;
    if (event.type == "mousemove") {
        moveX = event.pageX - eventX;
        moveY = event.pageY - eventY;
        left = beforeMoveX + moveX;
        top = beforeMoveY + moveY;
    }
    $("#divSelect").animate({left:left+"px",top:top+"px"}, 15);
 
    // �ƶ�����ѡ����豸
    var isSelectedNum = nodeSelectedArray.length;
    var i = 0;
    while (i < isSelectedNum) {
        var isSelectedTopologyMapNode = nodeSelectedArray[i];
        handleTopologyNodeMove(isSelectedTopologyMapNode, moveX, moveY);
        i++;
    }
    
}

// ����ѡ����굯���¼�����ѡ����ƶ��¼����н��
function handleDivSelectedMouseUp(event) {
    $(document).unbind("mousemove");
    $("#divSelect").css("cursor","default");
}

//�ж��豸�Ƿ���ѡ��֮��
function checkNodeIsInSelect(topologyMapNode) {
    var divLayerOffset = $("#divLayer").offset();
    var divLayerLeft = divLayerOffset.left;
    var divLayerTop = divLayerOffset.top;
    var nodeStyleLeft = topologyMapNode.imageElement.style.left;
    var nodeStyleTop = topologyMapNode.imageElement.style.top;
    var nodeStyleWidth = topologyMapNode.imageElement.style.width;
    var nodeStyleHeight = topologyMapNode.imageElement.style.height;

    // divSelect �ķ�Χ
    var divSelectOffset = $("#divSelect").offset();
    var divSelectLeft = divSelectOffset.left;
    var divSelectTop = divSelectOffset.top;
    var divSelectWidth = $("#divSelect").width();
    var divSelectHeight = $("#divSelect").height();
    
    // �ж����
    var result = parseInt(nodeStyleLeft) + parseInt(divLayerLeft) >= parseInt(divSelectLeft);
    // �ж��ϱ�
    var result = result && (parseInt(nodeStyleTop) + parseInt(divLayerTop) >= divSelectTop);
    // �ж��ұ�
    var result = result && (parseInt(nodeStyleLeft) + parseInt(nodeStyleWidth) + parseInt(divLayerLeft) <= parseInt(divSelectLeft) + parseInt(divSelectWidth));
    // �ж��±�
    var result = result && (parseInt(nodeStyleTop) + parseInt(nodeStyleHeight) + parseInt(divLayerTop) <= parseInt(divSelectTop) + parseInt(divSelectHeight));
    return result;
}

//���ѡ����豸
function addSelectedNode(topologyMapNode) {
    var isSelected = topologyMapNode.isSelected;
    if (isSelected) {
        return false;
    }
    topologyMapNode.isSelected = true;
    setSelectedNodeStyle(topologyMapNode);
    nodeSelectedArray.push(topologyMapNode);
    return true;
}

//���ѡ����豸
function addSelectedNode(topologyMapNode) {
    var isSelected = topologyMapNode.isSelected;
    if (isSelected) {
        return false;
    }
    topologyMapNode.isSelected = true;
    setSelectedNodeStyle(topologyMapNode);
    nodeSelectedArray.push(topologyMapNode);
    return true;
}

//ȡ������ѡ����豸
function cancelAllSelectedNode() {
    while (nodeSelectedArray.length > 0) {
        var isSelectedTopologyMapNode = nodeSelectedArray.pop();
        isSelectedTopologyMapNode.isSelected = false;
        setUnSelectedNodeStyle(isSelectedTopologyMapNode);
    }
}

function cancelSelectedNode(topologyMapNode) {
    if (!topologyMapNode.isSelected) {
        return false;
    }
    while (nodeSelectedArray.length > 0) {
        var isSelectedTopologyMapNode = nodeSelectedArray.pop();
        if (isSelectedTopologyMapNode.id == topologyMapNode.id) {
            isSelectedTopologyMapNode.isSelected = false;
            setUnSelectedNodeStyle(isSelectedTopologyMapNode);
        } else {
            nodeSelectedArray.unshift(isSelectedTopologyMapNode);
        }
    }
}

//����ѡ�нڵ�����ʽ
function setSelectedNodeStyle(topologyMapNode) {
    topologyMapNode.imageElement.style.background = "#9BFFAC";
    topologyMapNode.imageElement.style.border="1px solid #007314";
    topologyMapNode.imageElement.style.filter="Alpha(Opacity=60);";
}

// ȡ��ѡ�нڵ����ʽ
function setUnSelectedNodeStyle(topologyMapNode) {
    topologyMapNode.imageElement.style.background = "";
    topologyMapNode.imageElement.style.border = "0px solid #007314";
    topologyMapNode.imageElement.style.color = "#000000";
    topologyMapNode.imageElement.style.filter= "";
}
//function selectStart(event)
//{
//    if (window.event.srcElement.id == "divLayer" || 
//        window.event.srcElement.tagName == "BODY")
//    {
//        obj = null;
//        selectStatus = true;
//        imgTop.style.height = imgTop.style.width = imgLeft.style.height = imgLeft.style.width = imgBottom.style.height = imgBottom.style.width = imgRight.style.height = imgRight.style.width = 1;
//        imgTop.style.visibility = imgLeft.style.visibility = imgBottom.style.visibility = imgRight.style.visibility = "visible";
//        imgTop.style.zIndex = imgLeft.style.zIndex = imgBottom.style.zIndex = imgRight.style.zIndex = 9999;
//        beginx = event.x;
//        beginy = event.y;
//        document.onmousemove = move;
//        document.onmouseup = up;
//    }
//}


function bodyDown()
{
	if (window.event.srcElement.tagName == "BODY") 
	{
		if (document.all("ctrlImgDiv"))//yangjun add ȡ��ctrlѡ�е�ͼԪ
		{
			if(isRemoved == false)
			{
				rmvCtrlImg();	
			}
			isRemoved = true;
		}
		if (clickObj != null )
		{
			document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden"; 
			unSelectImg(clickObj);
			clickObj = null;
		}
		if (clickLineObj != null )
		{
			document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden"; 
			clickLineObj = null;
		}
		if (objStyle != null)
		{
		   unSelectImg(objStyle);
		   objStyle = null;
		}
		if (document.all("containImgDiv"))
		{
			if(isRemoved == false)
			{
				rmvContainedImg();	
			}
			isRemoved = true;
		}
	}
	else if (window.event.srcElement.tagName == "DIV" && 
			 window.event.srcElement.id == "containImgDiv")
	{
		if(isRemoved == false)
		{
			rmvContainedImg();	
		}
		isRemoved = true;
	}
	else if (window.event.srcElement.tagName == "DIV" && 
			 window.event.srcElement.id == "ctrlImgDiv")//yangjun add ȡ��ctrlѡ�е�ͼԪ
	{
		if(isRemoved == false)
		{
			rmvCtrlImg();	
		}
		isRemoved = true;
	}
}

// ����divLayer�հ״�ʱ
function divLayerDown()
{
    //alert(window.event.srcElement.id);
	if (window.event.srcElement.id == "divLayer")
	{
		if (clickObj != null )
		{
			document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden"; 
			unSelectImg(clickObj);
			clickObj = null;
		}
		if (clickLineObj != null )
		{
			document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden"; 
			clickLineObj = null;
		}
		if (objStyle != null)
		{
		   unSelectImg(objStyle);
		   objStyle = null;
		}
		if (document.all("containImgDiv"))
		{
			if(isRemoved == false)
			{
				rmvContainedImg();	
			}
			isRemoved = true;
		}
		if (document.all("ctrlImgDiv"))//yangjun add ȡ��ctrlѡ�е�ͼԪ
		{
			if(isRemoved == false)
			{
				rmvCtrlImg();	
			}
			isRemoved = true;
		}
	}
	else if (window.event.srcElement.tagName == "DIV" && 
			 window.event.srcElement.id == "containImgDiv")
	{
		if(isRemoved == false)
		{
			rmvContainedImg();	
		}
		isRemoved = true;
	}
	else if (window.event.srcElement.tagName == "DIV" && 
			 window.event.srcElement.id == "ctrlImgDiv")//yangjun add ȡ��ctrlѡ�е�ͼԪ
	{
		if(isRemoved == false)
		{
			rmvCtrlImg();	
		}
		isRemoved = true;
	}
}

// �������������¼�
function down()
{
	if (event.button != 1)
	{  // �����Ҽ��϶�
		return false;
	}
	if (clickLineObj != null )
		{
			document.all(clickLineObj.id.replace("line", "menu")).style.visibility = "hidden"; 
			clickLineObj = null;
		}
	if (clickObj != null)
	{ // �����Ҽ�����ͼ���Ĳ˵�
		document.all(clickObj.id.replace("node", "menu")).style.visibility = "hidden";
		clickObj = null;
	}
	this.setCapture();
	///////////////////////////////////yangjun �޸�
	if(event.ctrlKey) {//�����ctrlѡ���¼� yangjun add
		if(document.all("ctrlImgDiv") && objEntityAry.contains(this))
		{
			xx = event.x - document.all("ctrlImgDiv").offsetLeft;
			yy = event.y - document.all("ctrlImgDiv").offsetTop;
			this.setCapture();
			obj = this;
		}
		else
		{
			if(document.all("ctrlImgDiv"))
			{
				document.all.divLayer.removeChild(document.all("ctrlImgDiv"));
				//rmvCtrlImg();
			}
			xx = event.x - this.offsetLeft;
			yy = event.y - this.offsetTop;
			obj = this;
			objStyle = this;
			selectImg(objStyle);
			if (obj.lines != null)
			{
				line_ids = obj.lines.split("&");
			}
			strSearch = obj.id.replace(/^[^_]*/, '') + '_';
			objEntityAry.push(obj);//yangjun add 
			ctrlStatus = true; 
		}
	} 
	else 
	{
	    if (document.all("containImgDiv") && objMoveAry.contains(this))
		{
			xx = event.x - document.all("containImgDiv").offsetLeft;
			yy = event.y - document.all("containImgDiv").offsetTop;
			this.setCapture();
			obj = this;
		}
		else
		{
			if(document.all("containImgDiv"))
			{
				rmvContainedImg();
			}
			xx = event.x - this.offsetLeft;
			yy = event.y - this.offsetTop;
			if (objStyle != null)
			{
				unSelectImg(objStyle);
				objStyle = null;
			}
			obj = this;
			objStyle = this;
			selectImg(objStyle);
			objEntityAry.length=0;//yangjun add
			objEntityAry.push(obj);//yangjun add
			ctrlStatus = true; //yangjun add
			if (obj.lines != null)
			{
				line_ids = obj.lines.split("&");
			}
			strSearch = obj.id.replace(/^[^_]*/, '') + '_';
		}
	}
	////////////////////////////////////////////////////////
	document.onmousemove = move;
}

// ��������ƶ��¼�
function move()
{
	var eventX = window.event.x;
	var eventY = window.event.y;
	if (eventX > 0 & eventY > 0 & eventX < window.screen.width & eventY < window.screen.height)
	{
		if(obj != null)
		{
			var tempX = eventX - xx;
			var tempY = eventY - yy;
			if (document.all("containImgDiv"))
			{
				document.all("containImgDiv").style.left = tempX;
				document.all("containImgDiv").style.top = tempY;
				if (line_ids.length > 0)
				{
					for (var i = 0; i < line_ids.length; i++)
					{
						for(var j = 0; j < line_ids[i].length-1; j++)
						{
							var iElem = document.all(line_ids[i][j]);
							if (iElem == null)
							{
								continue;
							}
							var lth = line_ids[i].length-1;
							var tmpStr = document.all(line_ids[i][lth]).id.replace(/[^_]*/, '') + '_';
							var iLeft = parseInt(document.all(line_ids[i][lth]).style.left) + parseInt(document.all("containImgDiv").style.left) + 15;
							var iTop = parseInt(document.all(line_ids[i][lth]).style.top) + parseInt(document.all("containImgDiv").style.top) + 8;
							if (line_ids[i][j].search(tmpStr) != -1)
							{
								iElem.from = iLeft + "," + iTop;
							}
							else
							{
								iElem.to = iLeft + "," + iTop;
							}
						}
					}
				}
			}
			else
			{
				var textStyle = document.all(obj.id.replace("node", "text")).style;
				var infoStyle = document.all(obj.id.replace("node", "info")).style;
				var menuStyle = document.all(obj.id.replace("node", "menu")).style;
				obj.style.left = tempX;
				obj.style.top = tempY;
				textStyle.left = tempX - 24;
				textStyle.top = tempY + 28;
				infoStyle.left = tempX + 32;
				infoStyle.top = tempY;
				//#########������ק�豸ʱ���豸��Ϣ��ʾ����Խ�紦���������߽������Զ���ʾ�����ӷ�Χ֮��   HONGLI ADD #########
				var divInfo = document.getElementById(obj.id.replace("node", "info"));
				if(parseInt(tempX, 10) > document.body.clientWidth-infoWidth-24){
					infoStyle.left =parseInt(tempX, 10)-infoWidth;
				}
				if(parseInt(tempY, 10) > document.body.clientHeight-divInfo.clientHeight){  
					infoStyle.top = parseInt(tempY, 10)-divInfo.clientHeight;  
			    }
		   		//#############HONG ADD END##########
				menuStyle.left = tempX + 28;
				menuStyle.top = tempY;
				var iLeft = parseInt(obj.style.left) + 15;
				var iTop = parseInt(obj.style.top) + 8;
				for (var i = 1; i < line_ids.length; i += 1)
				{
					var iElem = document.all(line_ids[i]);
					if (line_ids[i].search(strSearch) != -1)
					{
						if(line_ids[i].search('#assistant') != -1){
							iElem.from = (parseInt(iLeft)+5) + "," + iTop;
							if(document.all("linetext_" + iElem.lineid)){
								var ito=iElem.to+"";
							    document.all("linetext_" + iElem.lineid).style.left = (iLeft+parseInt(ito.split(",")[0].replace("pt",""))+10)/2+20;
						        document.all("linetext_" + iElem.lineid).style.top = (iTop+parseInt(ito.split(",")[1].replace("pt",""))+10)/2+20;
							}
							if(document.all("line_s_" + iElem.lineid)){
								var ito=iElem.to+"";
							    document.all("line_s_" + iElem.lineid).style.left =(iLeft+(iLeft+parseInt(ito.split(",")[0].replace("pt",""))+10)/2+20)/2;
						        document.all("line_s_" + iElem.lineid).style.top = (iTop+(iTop+parseInt(ito.split(",")[1].replace("pt",""))+10)/2+20)/2;
							}
							if(document.all("line_e_" + iElem.lineid)){
								var ito=iElem.to+"";
							    document.all("line_e_" + iElem.lineid).style.left = (parseInt(ito.split(",")[0].replace("pt",""))+(iLeft+parseInt(ito.split(",")[0].replace("pt",""))+10)/2+40)/2;
						        document.all("line_e_" + iElem.lineid).style.top = (parseInt(ito.split(",")[1].replace("pt",""))+(iTop+parseInt(ito.split(",")[1].replace("pt",""))+10)/2+40)/2;
							}
						} else if(line_ids[i].search('#demoline') != -1){
						    iElem.from = (parseInt(iLeft)+8) + "," + iTop;
						} else {
						    iElem.from = iLeft + "," + iTop;
						    if(document.all("linetext_" + iElem.lineid)){
						    	var ito=iElem.to+"";
						        document.all("linetext_" + iElem.lineid).style.left = (iLeft+parseInt(ito.split(",")[0].replace("pt","")))/2+20;
						        document.all("linetext_" + iElem.lineid).style.top = (iTop+parseInt(ito.split(",")[1].replace("pt","")))/2+20;
						    }
						    if(document.all("line_s_" + iElem.lineid)){
								var ito=iElem.to+"";
							    document.all("line_s_" + iElem.lineid).style.left =(iLeft+(iLeft+parseInt(ito.split(",")[0].replace("pt",""))+10)/2+20)/2;
						        document.all("line_s_" + iElem.lineid).style.top = (iTop+(iTop+parseInt(ito.split(",")[1].replace("pt",""))+10)/2+20)/2;
							}
							if(document.all("line_e_" + iElem.lineid)){
								var ito=iElem.to+"";
							    document.all("line_e_" + iElem.lineid).style.left = (parseInt(ito.split(",")[0].replace("pt",""))+(iLeft+parseInt(ito.split(",")[0].replace("pt",""))+10)/2+40)/2;
						        document.all("line_e_" + iElem.lineid).style.top = (parseInt(ito.split(",")[1].replace("pt",""))+(iTop+parseInt(ito.split(",")[1].replace("pt",""))+10)/2+40)/2;
							}
						}
					}
					else
					{
						if(line_ids[i].search('#assistant') != -1){
							iElem.to = (parseInt(iLeft)+5) + "," + iTop;
							if(document.all("linetext_" + iElem.lineid)){
							    var ifrom=iElem.from+"";
								document.all("linetext_" + iElem.lineid).style.left = (iLeft+parseInt(ifrom.split(",")[0].replace("pt",""))+10)/2+20;
							    document.all("linetext_" + iElem.lineid).style.top = (iTop+parseInt(ifrom.split(",")[1].replace("pt",""))+10)/2+20;
							}
							if(document.all("line_s_" + iElem.lineid)){
								var ifrom=iElem.from+"";
							    document.all("line_s_" + iElem.lineid).style.left =(parseInt(ifrom.split(",")[0].replace("pt",""))+(iLeft+parseInt(ifrom.split(",")[0].replace("pt",""))+10)/2+40)/2;
						        document.all("line_s_" + iElem.lineid).style.top = (parseInt(ifrom.split(",")[1].replace("pt",""))+(iTop+parseInt(ifrom.split(",")[1].replace("pt",""))+10)/2+40)/2;
							}
							if(document.all("line_e_" + iElem.lineid)){
								var ifrom=iElem.from+"";
							    document.all("line_e_" + iElem.lineid).style.left = (iLeft+(iLeft+parseInt(ifrom.split(",")[0].replace("pt",""))+10)/2+20)/2;
						        document.all("line_e_" + iElem.lineid).style.top = (iTop+(iTop+parseInt(ifrom.split(",")[1].replace("pt",""))+10)/2+20)/2;
							}
						} else if(line_ids[i].search('#demoline') != -1){
							iElem.to = (parseInt(iLeft)+8) + "," + iTop;
						} else{
							iElem.to = iLeft + "," + iTop;
							if(document.all("linetext_" + iElem.lineid)){
							    var ifrom=iElem.from+"";
								document.all("linetext_" + iElem.lineid).style.left = (iLeft+parseInt(ifrom.split(",")[0].replace("pt","")))/2+20;
							    document.all("linetext_" + iElem.lineid).style.top = (iTop+parseInt(ifrom.split(",")[1].replace("pt","")))/2+20;
							}
							if(document.all("line_s_" + iElem.lineid)){
								var ifrom=iElem.from+"";
							    document.all("line_s_" + iElem.lineid).style.left =(parseInt(ifrom.split(",")[0].replace("pt",""))+(iLeft+parseInt(ifrom.split(",")[0].replace("pt",""))+10)/2+40)/2;
						        document.all("line_s_" + iElem.lineid).style.top = (parseInt(ifrom.split(",")[1].replace("pt",""))+(iTop+parseInt(ifrom.split(",")[1].replace("pt",""))+10)/2+40)/2;
							}
							if(document.all("line_e_" + iElem.lineid)){
								var ifrom=iElem.from+"";
							    document.all("line_e_" + iElem.lineid).style.left = (iLeft+(iLeft+parseInt(ifrom.split(",")[0].replace("pt",""))+10)/2+20)/2;
						        document.all("line_e_" + iElem.lineid).style.top = (iTop+(iTop+parseInt(ifrom.split(",")[1].replace("pt",""))+10)/2+20)/2;
							}
						}
							
					}
				}
			}
			movedObjFlag = true;
		}
		if (selectStatus)
		{  // ����ѡ��
			var tmpx = window.event.x;
			var tmpy = window.event.y;
			imgTop.style.left = Math.min(beginx,tmpx);
			imgTop.style.top = Math.min(beginy,tmpy);
			imgTop.style.width = Math.abs(beginx - tmpx);
			imgLeft.style.left = Math.min(beginx,tmpx);
			imgLeft.style.top = Math.min(beginy,tmpy);
			imgLeft.style.height = Math.abs(beginy - tmpy);
			imgBottom.style.left = Math.min(beginx,tmpx);
			imgBottom.style.top = Math.max(beginy,tmpy);
			imgBottom.style.width = Math.abs(beginx - tmpx);
			imgRight.style.left = Math.max(beginx,tmpx);
			imgRight.style.top = Math.min(beginy,tmpy);
			imgRight.style.height = Math.abs(beginy - tmpy);
		}
	}
}

// ���������ͷ��¼�
function up()
{
	if (obj != null)
	{
		this.releaseCapture();
		if (movedObjFlag == true)
		{
			if (document.all("containImgDiv"))
			{
				for (var i = 0; i < objMoveAry.length; i++)
				{
					if (!tempArray.contains(objMoveAry[i]))
					{
						tempArray.push(objMoveAry[i]);
					}
				}
			}
			else if (!tempArray.contains(this))
			{
				tempArray.push(this);
			}
		}
		obj = null;
		if(!document.all("containImgDiv"))
		{
			line_ids.length = 0;
		}		
		strSearch = null;
	}
	if (selectStatus)
	{
		imgTopX = parseInt(imgTop.style.left);   // ȡ�òʿ�Ĵ�Сλ�ã�������λ��������ڵĽڵ���ı���ǩ����objMoveAry����
		imgTopY = parseInt(imgTop.style.top);
		imgBottomX = parseInt(imgTop.style.left) + parseInt(imgTop.style.width);
		imgBottomY = parseInt(imgTop.style.top) + parseInt(imgLeft.style.height);
		var allImgNodes = document.getElementsByTagName("image");//yangjun ��image�޸�Ϊback
		var j = 0;
		objMoveAry.length = 0;
		for (var i = 0; i < allImgNodes.length; i++)
		{
			if (parseInt(allImgNodes[i].style.left) + parseInt(document.all.divLayer.style.left) >= imgTopX 
			   && parseInt(allImgNodes[i].style.left) + parseInt(document.all.divLayer.style.left) <= imgBottomX 
			   && parseInt(allImgNodes[i].style.top) + parseInt(document.all.divLayer.style.top) >= imgTopY 
			   && parseInt(allImgNodes[i].style.top) + parseInt(document.all.divLayer.style.top) <=imgBottomY
			   && parseInt(allImgNodes[i].style.left) + parseInt(allImgNodes[i].style.width) + parseInt(document.all.divLayer.style.left) <= imgBottomX 
			   && parseInt(allImgNodes[i].style.top) + parseInt(allImgNodes[i].style.height) + parseInt(document.all.divLayer.style.top) <= imgBottomY)
			{
				objMoveAry[j] = allImgNodes[i];
				selectImg(objMoveAry[j]);
				j++;
			}
		}
		if (objMoveAry.length > 0)
		{
			var containImgDiv = document.createElement("div");
			containImgDiv.id = "containImgDiv";
			containImgDiv.style.position = "absolute";
			containImgDiv.style.left = imgTopX;
			containImgDiv.style.top = imgTopY;
			containImgDiv.style.width = imgTop.style.width;
			containImgDiv.style.height = imgLeft.style.height;
			document.all.divLayer.appendChild(containImgDiv);
			for (var i = 0; i < objMoveAry.length; i++)
			{
				var txtObj = document.all(objMoveAry[i].id.replace("node","text"));
				var infoObj = document.all(objMoveAry[i].id.replace("node","info"));
				var menuObj = document.all(objMoveAry[i].id.replace("node","menu"));
				document.all("containImgDiv").appendChild(objMoveAry[i]);
				document.all("containImgDiv").appendChild(txtObj);
				document.all("containImgDiv").appendChild(infoObj);
				document.all("containImgDiv").appendChild(menuObj);
				objMoveAry[i].style.position = "absolute";
				objMoveAry[i].style.left = parseInt(objMoveAry[i].style.left) - parseInt(containImgDiv.style.left);
				objMoveAry[i].style.top = parseInt(objMoveAry[i].style.top) - parseInt(containImgDiv.style.top);
				txtObj.style.position = "absolute";
				txtObj.style.left = parseInt(txtObj.style.left) - parseInt(containImgDiv.style.left);
				txtObj.style.top = parseInt(txtObj.style.top) - parseInt(containImgDiv.style.top);
				infoObj.style.position = "absolute";
				infoObj.style.left = parseInt(infoObj.style.left) - parseInt(containImgDiv.style.left);
				infoObj.style.top = parseInt(infoObj.style.top) - parseInt(containImgDiv.style.top);
				menuObj.style.position = "absolute";
				menuObj.style.left = parseInt(menuObj.style.left) - parseInt(containImgDiv.style.left);
				menuObj.style.top = parseInt(menuObj.style.top) - parseInt(containImgDiv.style.top);
			}
			isRemoved = false;
			line_ids.length = 0;
			lineMoveAry.length = 0; // ��ż��ص�containImgDiv��������߶���
			assLineMoveAry.length = 0;
			demoLineMoveAry.length = 0;
			for (var i = 0; i < objMoveAry.length; i++)
			{
				if (objMoveAry[i].lines == null)
				{
					// ����豸û������
					continue;
				}
				if (objMoveAry[i].lines.split("&").length > 0)
				{
					var twoDemAry = new Array();
					twoDemAry = objMoveAry[i].lines.split("&");
					var tmpRelAry = new Array();
					for (var j = 1; j < twoDemAry.length; j++)
					{
						if (! document.all(twoDemAry[j]))
						{
							continue;
						}
						
						if(twoDemAry[j].search('#assistant') != -1){//��������·��������
							//alert(twoDemAry[j].split("#")[0]);
							var conObjAry1 = twoDemAry[j].split("#")[0].replace("line_","").split("_");
							if ((document.all("containImgDiv").all("node_" + conObjAry1[0])) && (document.all("containImgDiv").all("node_" + conObjAry1[1]))) {
							    var addLineObj = document.all(twoDemAry[j]);
							    assLineMoveAry.push(addLineObj);
							}
						}
						if(twoDemAry[j].search('#demoline') != -1){//��ʾ����·��������
							//alert(twoDemAry[j].split("#")[0]);
							var conObjAry2 = twoDemAry[j].split("#")[0].replace("line_","").split("_");
							if ((document.all("containImgDiv").all("node_" + conObjAry2[0])) && (document.all("containImgDiv").all("node_" + conObjAry2[1]))) {
							    var addLineObj = document.all(twoDemAry[j]);
							    demoLineMoveAry.push(addLineObj);
							}
						}
						var conObjAry = twoDemAry[j].replace("line_","").split("_");
						if ((document.all("containImgDiv").all("node_" + conObjAry[0])) && (document.all("containImgDiv").all("node_" + conObjAry[1]))) {
							var addLineObj = document.all(twoDemAry[j]);
							lineMoveAry.push(addLineObj);		
							addLineObj.style.visibility = "hidden";
							addLineObj.style.position = "absolute";
							//addLineObj.from = (parseInt(document.all("node_" + conObjAry[0]).style.left) + 15) + "," + (parseInt(document.all("node_" + conObjAry[0]).style.top) + 8);
							//addLineObj.to = (parseInt(document.all("node_" + conObjAry[1]).style.left) + 15) + "," + (parseInt(document.all("node_" + conObjAry[1]).style.top) + 8);
						    
							addLineObj[i].from = (parseInt(document.all("node_" + conObjAry[0]).style.left) + parseInt(document.all("node_" + conObjAry[0]).style.width)/2) + "," + (parseInt(document.all("node_" + conObjAry[0]).style.left) + parseInt(document.all("node_" + conObjAry[0]).style.height)/2);
							addLineObj[i].to =  (parseInt(document.all("node_" + conObjAry[1]).style.left) + parseInt(document.all("node_" + conObjAry[1]).style.width)/2) + "," + (parseInt(document.all("node_" + conObjAry[1]).style.left) + parseInt(document.all("node_" + conObjAry[1]).style.height)/2);
						    
						    document.all("containImgDiv").appendChild(addLineObj);
							addLineObj.style.visibility = "visible";
						}
						else
						{
							relLineAry.push(twoDemAry[j]);  // ���������div���������ߣ��ǰ��������ŵ���������
							tmpRelAry.push(twoDemAry[j]);   // ���뵱ǰ�ڵ���ص����ߣ�û�����ڵ�ǰdiv�У����ŵ���������
						}
					}
					if (tmpRelAry.length > 0)
					{
						tmpRelAry.push(objMoveAry[i].id);
						line_ids.push(tmpRelAry);	// line_ids�д�����������div�ڵ����������߶���(û�����ڵ�ǰdiv��)id
					}
				}
			}
		}
		selectStatus = false;
		imgTop.style.visibility = "hidden";
		imgLeft.style.visibility = "hidden";
		imgBottom.style.visibility = "hidden";
		imgRight.style.visibility = "hidden";
	}
	if (ctrlStatus) // ����Ctrl+�����ͷ��¼�yangjun 
	{
		if (objEntityAry.length > 0)
		{
//			var imgTopX_min = window.screen.width;
//			var imgTopX_max = 0;
//			var imgTopY_min = window.screen.height;
//			var imgTopY_max = 0;
//			for (var i = 0; i < objEntityAry.length; i++)
//			{
//				if(parseInt(objEntityAry[i].style.left)<=imgTopX_min)
//				{
//					imgTopX_min = parseInt(objEntityAry[i].style.left);
//				}
//				if(parseInt(objEntityAry[i].style.left)>=imgTopX_max)
//				{
//					imgTopX_max = parseInt(objEntityAry[i].style.left);
//				}
//				if(parseInt(objEntityAry[i].style.top)>=imgTopY_max)
//				{
//					imgTopY_max = parseInt(objEntityAry[i].style.top);
//				}
//				if(parseInt(objEntityAry[i].style.top)<=imgTopY_min)
//				{
//					imgTopY_min = parseInt(objEntityAry[i].style.top);
//				}
//				
//			}
			var ctrlImgDiv = document.createElement("div");
			ctrlImgDiv.id = "ctrlImgDiv";
			ctrlImgDiv.style.position = "absolute";
//			ctrlImgDiv.style.left = imgTopX_min - 30;
//			ctrlImgDiv.style.top = imgTopY_min - 30;
//			ctrlImgDiv.style.width = imgTopX_max - imgTopX_min + 50;
//			ctrlImgDiv.style.height = imgTopY_max - imgTopY_min + 50;
//			ctrlImgDiv.style.background = "#ffe4c4";
			document.all.divLayer.appendChild(ctrlImgDiv);
			for (var i = 0; i < objEntityAry.length; i++)
			{
//				var txtObj = document.all(objEntityAry[i].id.replace("node","text"));
//				var infoObj = document.all(objEntityAry[i].id.replace("node","info"));
//				var menuObj = document.all(objEntityAry[i].id.replace("node","menu"));
				//////////////////////////
				document.all("ctrlImgDiv").appendChild(objEntityAry[i]);
				//////////////////////////
//				document.all("ctrlImgDiv").appendChild(txtObj);
//				document.all("ctrlImgDiv").appendChild(infoObj);
//				document.all("ctrlImgDiv").appendChild(menuObj);
//				objEntityAry[i].style.position = "absolute";
//				objEntityAry[i].style.left = parseInt(objEntityAry[i].style.left) - parseInt(ctrlImgDiv.style.left);
//				objEntityAry[i].style.top = parseInt(objEntityAry[i].style.top) - parseInt(ctrlImgDiv.style.top);
//				txtObj.style.position = "absolute";
//				txtObj.style.left = parseInt(txtObj.style.left) - parseInt(ctrlImgDiv.style.left);
//				txtObj.style.top = parseInt(txtObj.style.top) - parseInt(ctrlImgDiv.style.top);
//				infoObj.style.position = "absolute";
//				infoObj.style.left = parseInt(infoObj.style.left) - parseInt(ctrlImgDiv.style.left);
//				infoObj.style.top = parseInt(infoObj.style.top) - parseInt(ctrlImgDiv.style.top);
//				menuObj.style.position = "absolute";
//				menuObj.style.left = parseInt(menuObj.style.left) - parseInt(ctrlImgDiv.style.left);
//				menuObj.style.top = parseInt(menuObj.style.top) - parseInt(ctrlImgDiv.style.top);
			}
			isRemoved = false;
			line_ids.length = 0;
			lineMoveAry.length = 0; // ��ż��ص�containImgDiv��������߶���
			assLineMoveAry.length = 0;
			demoLineMoveAry.length = 0;
			for (var i = 0; i < objEntityAry.length; i++)
			{
				
				if (objEntityAry[i].lines == null)
				{
					// ����豸û������
					continue;
				}
				if (objEntityAry[i].lines.split("&").length > 0)
				{
					var twoDemAry = new Array();
					twoDemAry = objEntityAry[i].lines.split("&");
					var tmpRelAry = new Array();
					for (var j = 1; j < twoDemAry.length; j++)
					{
						if (! document.all(twoDemAry[j]))
						{
							continue;
						}
						if(twoDemAry[j].search('#assistant') != -1){//��������·��������
							//alert(twoDemAry[j].split("#")[0]);
							var conObjAry1 = twoDemAry[j].split("#")[0].replace("line_","").split("_");
							if ((document.all("ctrlImgDiv").all("node_" + conObjAry1[0])) && (document.all("ctrlImgDiv").all("node_" + conObjAry1[1]))) {
							    var addLineObj = document.all(twoDemAry[j]);
							    assLineMoveAry.push(addLineObj);
							}
						}
						if(twoDemAry[j].search('#demoline') != -1){//��ʾ����·��������
							//alert(twoDemAry[j].split("#")[0]);
							var conObjAry2 = twoDemAry[j].split("#")[0].replace("line_","").split("_");
							if ((document.all("ctrlImgDiv").all("node_" + conObjAry2[0])) && (document.all("ctrlImgDiv").all("node_" + conObjAry2[1]))) {
							    var addLineObj = document.all(twoDemAry[j]);
							    demoLineMoveAry.push(addLineObj);
							}
						}
						var conObjAry = twoDemAry[j].replace("line_","").split("_");
						if ((document.all("ctrlImgDiv").all("node_" + conObjAry[0])) && (document.all("ctrlImgDiv").all("node_" + conObjAry[1]))) {
							var addLineObj = document.all(twoDemAry[j]);
							lineMoveAry.push(addLineObj);	
							///////////////////////////////
//							addLineObj.style.visibility = "hidden";
//							addLineObj.style.position = "absolute";
//							addLineObj.from = (parseInt(document.all("node_" + conObjAry[0]).style.left) + 15) + "," + (parseInt(document.all("node_" + conObjAry[0]).style.top) + 8);
//							addLineObj.to = (parseInt(document.all("node_" + conObjAry[1]).style.left) + 15) + "," + (parseInt(document.all("node_" + conObjAry[1]).style.top) + 8);
//						    document.all("ctrlImgDiv").appendChild(addLineObj);
//							addLineObj.style.visibility = "visible";
						}
//						else
//						{
//							relLineAry.push(twoDemAry[j]);  // ���������div���������ߣ��ǰ��������ŵ���������
//							tmpRelAry.push(twoDemAry[j]);   // ���뵱ǰ�ڵ���ص����ߣ�û�����ڵ�ǰdiv�У����ŵ���������
//						}
					}
//	                if (tmpRelAry.length > 0)
//					{
//						tmpRelAry.push(objMoveAry[i].id);
//						line_ids.push(tmpRelAry);	// line_ids�д�����������div�ڵ����������߶���(û�����ڵ�ǰdiv��)id
//					}
				}
			}
		}
		ctrlStatus = false;
	}
	document.onmousemove = null;
}
//���ctrlѡ�е�ͼԪ
function rmvCtrlImg()
{
//	var tmpDivLeft = document.all("ctrlImgDiv").style.left;
//	var tmpDivTop = document.all("ctrlImgDiv").style.top;
	for (var i = 0; i < objEntityAry.length; i++)
	{
		unSelectImg(objEntityAry[i]);
		var txtObj = document.all(objEntityAry[i].id.replace("node","text"));
		var infoObj = document.all(objEntityAry[i].id.replace("node","info"));
		var menuObj = document.all(objEntityAry[i].id.replace("node","menu"));
		document.all.divLayer.appendChild(objEntityAry[i]);
		document.all.divLayer.appendChild(txtObj);
		document.all.divLayer.appendChild(infoObj);
		document.all.divLayer.appendChild(menuObj);
		//////
//		objEntityAry[i].style.left = parseInt(objEntityAry[i].style.left) + parseInt(tmpDivLeft);
//		objEntityAry[i].style.top = parseInt(objEntityAry[i].style.top) + parseInt(tmpDivTop);
//		txtObj.style.left = parseInt(txtObj.style.left) + parseInt(tmpDivLeft);
//		txtObj.style.top = parseInt(txtObj.style.top) + parseInt(tmpDivTop);
//		infoObj.style.left = parseInt(infoObj.style.left) + parseInt(tmpDivLeft);
//		infoObj.style.top = parseInt(infoObj.style.top) + parseInt(tmpDivTop);
//		menuObj.style.left = parseInt(menuObj.style.left) + parseInt(tmpDivLeft);
//		menuObj.style.top = parseInt(menuObj.style.top) + parseInt(tmpDivTop);
	}		
	if (lineMoveAry.length > 0)
	{
		for (var i = 0; i < lineMoveAry.length; i++)
		{
			document.all.divLayer.appendChild(lineMoveAry[i]);
			lineMoveAry[i].style.visibility = "hidden";
			var conObjAry = lineMoveAry[i].id.replace("line_","").split("_");
			var fromObj = document.all("node_" + conObjAry[0]);
			var toObj = document.all("node_" + conObjAry[1]);
			//lineMoveAry[i].from = (parseInt(fromObj.style.left) + 15) + "," + (parseInt(fromObj.style.top) + 8);
			//lineMoveAry[i].to =  (parseInt(toObj.style.left) + 15) + "," + (parseInt(toObj.style.top) + 8);
			lineMoveAry[i].from = (parseInt(fromObj.style.left) + parseInt(fromObj.style.width)/2) + "," + (parseInt(fromObj.style.top) + parseInt(fromObj.style.height)/2);
			lineMoveAry[i].to =  (parseInt(toObj.style.left) + parseInt(toObj.style.width)/2) + "," + (parseInt(toObj.style.top) + parseInt(toObj.style.height)/2);
			lineMoveAry[i].style.visibility = "visible";
		}
	}
	lineMoveAry.length = 0;
	assLineMoveAry.length = 0;
	demoLineMoveAry.length = 0;
	objEntityAry.length = 0;
	line_ids.length = 0;
	document.all.divLayer.removeChild(document.all("ctrlImgDiv"));
}
function rmvContainedImg()
{
	var tmpDivLeft = document.all("containImgDiv").style.left;
	var tmpDivTop = document.all("containImgDiv").style.top;
	for (var i = 0; i < objMoveAry.length; i++)
	{
		unSelectImg(objMoveAry[i]);	
		var txtObj = document.all(objMoveAry[i].id.replace("node","text"));
		var infoObj = document.all(objMoveAry[i].id.replace("node","info"));
		var menuObj = document.all(objMoveAry[i].id.replace("node","menu"));
		document.all.divLayer.appendChild(objMoveAry[i]);
		document.all.divLayer.appendChild(txtObj);
		document.all.divLayer.appendChild(infoObj);
		document.all.divLayer.appendChild(menuObj);
		objMoveAry[i].style.left = parseInt(objMoveAry[i].style.left) + parseInt(tmpDivLeft);
		objMoveAry[i].style.top = parseInt(objMoveAry[i].style.top) + parseInt(tmpDivTop);
		txtObj.style.left = parseInt(txtObj.style.left) + parseInt(tmpDivLeft);
		txtObj.style.top = parseInt(txtObj.style.top) + parseInt(tmpDivTop);
		infoObj.style.left = parseInt(infoObj.style.left) + parseInt(tmpDivLeft);
		infoObj.style.top = parseInt(infoObj.style.top) + parseInt(tmpDivTop);
		menuObj.style.left = parseInt(menuObj.style.left) + parseInt(tmpDivLeft);
		menuObj.style.top = parseInt(menuObj.style.top) + parseInt(tmpDivTop);
	}			
	if (lineMoveAry.length > 0)
	{
		for (var i = 0; i < lineMoveAry.length; i++)
		{
			document.all.divLayer.appendChild(lineMoveAry[i]);
			lineMoveAry[i].style.visibility = "hidden";
			var conObjAry = lineMoveAry[i].id.replace("line_","").split("_");
			var fromObj = document.all("node_" + conObjAry[0]);
			var toObj = document.all("node_" + conObjAry[1]);
			//lineMoveAry[i].from = (parseInt(fromObj.style.left) + 15) + "," + (parseInt(fromObj.style.top) + 8);
			//lineMoveAry[i].to =  (parseInt(toObj.style.left) + 15) + "," + (parseInt(toObj.style.top) + 8);
			lineMoveAry[i].from = (parseInt(fromObj.style.left) + parseInt(fromObj.style.width)/2) + "," + (parseInt(fromObj.style.top) + parseInt(fromObj.style.height)/2);
			lineMoveAry[i].to =  (parseInt(toObj.style.left) + parseInt(toObj.style.width)/2) + "," + (parseInt(toObj.style.top) + parseInt(toObj.style.height)/2);
			lineMoveAry[i].style.visibility = "visible";
		}
	}
	lineMoveAry.length = 0;
	assLineMoveAry.length = 0;
	demoLineMoveAry.length = 0;
	objMoveAry.length = 0;
	line_ids.length = 0;
	document.all.divLayer.removeChild(document.all("containImgDiv"));
}

// ѡ�нڵ�����ʽ
function selectImg(objSty)
{
	document.all(objSty.id).style.background = "#9BFFAC";
	document.all(objSty.id).style.border="1px solid #007314";
	document.all(objSty.id).style.filter="Alpha(Opacity=60);";
	var alarmInfo=document.getElementById("windown-content");
    if(alarmInfo){
        alarmInfo.innerHTML='<div class="mainlist"><ul id="alarmInfo">'+document.all(objSty.id.replace("node_","info_")).innerHTML+'</ul></div>';
    }
	
//	document.all(objSty.id.replace("node_","text_")).style.background = "#9BFFAC";
//	document.all(objSty.id.replace("node_","text_")).style.border="1px solid #007314";
//	document.all(objSty.id.replace("node_","text_")).style.filter="Alpha(Opacity=60);";
	//document.all(objSty.id.replace("node_","text_")).style.color = "#FFFFFF";
	//document.all(objSty.id.replace("node_","text_")).style.height = "12px";
	//document.all(objSty.id.replace("node_","text_")).style.border = "#555555 1px dotted";
	/*if(objSty.src.indexOf("_col.png") == -1){
		objSty.src = objSty.src.replace(".png","_col.png");
		document.all(objSty.id.replace("node_","text_")).style.background = "#94B4DD";
	}*/
}
// ȡ��ѡ�нڵ����ʽ
function unSelectImg(objSty)
{
	document.all(objSty.id).style.background = "";
	document.all(objSty.id).style.border="0px solid #007314";
	document.all(objSty.id).style.color = "#000000";
	document.all(objSty.id).style.filter="";
	  /*if(objSty.src.indexOf("_col.png") != -1){
		objSty.src = objSty.src.replace("_col.png",".png");
		document.all(objSty.id.replace("node_","text_")).style.background = "";
	}*/
}
// ����
function save()
{
	if(document.all("containImgDiv"))
	{
		rmvContainedImg();
	}
	var nodes = xmldoc.getElementsByTagName("node");
	for (var i = 0; i < tempArray.length; i += 1)
	{
		for (var j = 0; j < nodes.length; j += 1)
		{
			var node = nodes[j];
			var id = "node_" + node.getElementsByTagName("id")[0].text;

			if (tempArray[i].id == id)
			{
				node.getElementsByTagName("x")[0].text = tempArray[i].style.left;
				node.getElementsByTagName("y")[0].text = tempArray[i].style.top;
			}
		}
	}
	document.all("hidXml").value = xmldoc.xml;
	document.frmMap.submit();
}

// �ж�X, Y�Ƿ������нڵ���������ֵ
function getConfine(x, y)
{
	var coorX;
	var coorY;
	
	if (x.indexOf("px") >= 0)
	{
		coorX = x.substring(0, x.length-2);
		coorX = parseInt(coorX);
	}
	else {
		coorX = parseInt(x);
	}
	
	if (y.indexOf("px") >= 0)
	{
		coorY = y.substring(0, y.lenght-2);
		coorY = parseInt(coorY);
	}
	else
	{
		coorY = parseInt(y);
	}
	
	if (coorX > maxWidth)
		maxWidth = coorX;
	if (coorY > maxHeight)
		maxHeight = coorY;
		
	if (coorX < minWidth)
		minWidth = coorX;
	if (coorY < minHeight)
		minHeight = coorY;
}

// ͨ�����̵ġ��������ҡ������������ͼλ��
function document.onkeydown()
{
//-----�Ȳ���--------��3-------
/*	try
	{
		if (document.all.blind.style.visibility == "visible")
			return;
	}
	catch (exception)
	{
	}
	*/
//----------------------
	var left = divLayer.style.left;
	var top = divLayer.style.top;
	
	//left = left.substring(0, left.length - 2);
	//top = top.substring(0, top.length - 2);

	left = parseInt(left);
	top = parseInt(top);
	//alert(event.keyCode); return;
	switch (event.keyCode)
	{
		/*
		case 81:		// Q �����л���ͼ
			if (g_viewFlag == 0) {
				g_viewFlag = 1;
				var target = "showMap.jsp?filename=" + filename + "&viewflag=1";
				parent.mainFrame.location = target;
			}
			else if (g_viewFlag == 1) {
				g_viewFlag = 0;
				var target = "showMap.jsp?filename=" + filename + "&viewflag=0";
				parent.mainFrame.location = target;
			}
			break;
		*/
		case 35:		// End
		case 82:		// R
			moveAction();
			break;
		case 37:
		case 65:		// A
			moveAction('left');
			break;
		case 38:
		case 87:		// W
			moveAction('up');
			break;
		case 39:
		case 68:		// D
			moveAction('right');
			break;
		case 40:
		case 83:		// S
			moveAction('down');
			break;
			
		case 90:		// Z
			zoomAll('out');
			break;
		case 88:		// X
			zoomAll();
			break;
		case 67:		// C
			zoomAll('in');
			break;
			
		case 69:		// E ����/��ʾ�������
			showController(!controllerState);
			break;

		default:
			break;
	}
}


/**** �����ƶ�������ĸ���ť�¼������Ű�ť�¼� -- ��ʼ ****/

var distance = 80;
var speed = 12;
var position;
var left;
var top;
var timer;

var zoom = 1.0;
var scale = 0.1;

var controllerState = true;

function moveAction(dir)
{

///-----------------��4----
	closeAnchor();	// �ر�׼��
//	hideLineTip();	// ������·��ʾ
//------------------------

	clearTimer();
	updatePosition();
	
	if (dir == "left")
	{
		position = left + distance;
		timer = setInterval("moveLeft()", speed);
	}
	else if (dir == "up") 
	{
		position = top + distance;
		timer = setInterval("moveUp()", speed);
	}
	else if (dir == "right") 
	{
		position = left - distance;
		timer = setInterval("moveRight()", speed);
	}
	else if (dir == "down") 
	{
		position = top - distance;
		timer = setInterval("moveDown()", speed);
	}
	else
	{
		moveOrigin();
	}
}

function clearTimer()
{
	clearInterval(timer);
}

function moveLeft() 
{
	updatePosition();
	if (left >= position) 
	{
		clearTimer();
		return;
	}
	divLayer.style.left = (left + speed);
}

function moveUp() 
{
	updatePosition();
	if (top >= position)
	{
		clearTimer();
		return;
	}
	divLayer.style.top = (top + speed);
}

function moveRight()
{
	updatePosition();
	if (left <= position) 
	{
		clearTimer();
		return;
	}
	divLayer.style.left = (left - speed);
}

function moveDown() 
{
	updatePosition();
	if (top <= position) 
	{
		clearTimer();
		return;
	}
	divLayer.style.top = (top - speed);
}

function moveOrigin() 
{
	divLayer.style.left = 0;//parseInt(mainX);--���Ǹĺ�ģ����ڻָ�ԭ����λ��-----��5--
	divLayer.style.top = 0;//parseInt(mainY);
}

function updatePosition() 
{
	var divLeft = parseInt(divLayer.style.left);
	var divTop = parseInt(divLayer.style.top);
	
	//divLeft = divLeft.substring(0, divLeft.length - 2);
	//divTop = divTop.substring(0, divTop.length - 2);

	left = parseInt(divLeft);
	top = parseInt(divTop);
}

function zoomAll(state) 
{
	closeAnchor();//---------�ĺ�������-
	if (divLayer.style.zoom == "") 
	{
		divLayer.style.zoom = 1.0;
	}
	
	if (state == "out") 
	{
		// ��С
		if (divLayer.style.zoom != "") 
		{
			zoom = parseFloat(zoom) - scale;
			if (zoom <= 0) 
			{
				zoom = 0.9;
			}
			else if (zoom > 0 && zoom < 0.5) 
			{
				zoom = 0.5;
				return;
			}
			divLayer.style.zoom = parseFloat(zoom);
		}
	}
	else if (state == "in") 
	{
		// �Ŵ�
		if (divLayer.style.zoom != "") 
		{
			zoom = parseFloat(zoom) + scale;
			if (zoom > 2.0) 
			{
				zoom = 2.0;
				return;
			}
			else if (zoom == 0.2) 
			{
				zoom = 1.1;
			}
			divLayer.style.zoom = parseFloat(zoom);
		}
	}
	else {
		// ��ԭ
		if (divLayer.style.zoom != "") 
		{
			divLayer.style.zoom = 1.0;
			zoom = 1.0;
		}
	}
}

function showController(show) 
{
	if (show) 
	{
		document.all.moveController.style.visibility = "visible";
		document.all.sizeController.style.visibility = "visible";
		controllerState = true;
	}
	else 
	{
		document.all.moveController.style.visibility = "hidden";
		document.all.sizeController.style.visibility = "hidden";
		controllerState = false;
	}
	return true;
}

// ------ ͼƬ����Ч������ - ��ʼ ------

function swapImage(imageID, imageSrc) 
{
	document.all(imageID).src = imgPath+imageSrc;
}

// ------ ͼƬ����Ч������ - ���� ------

function loadMoveController() 
{
	document.write('<div id="moveController" style="position:absolute;top:5px;left:5px;z-index:999;background-image:url(image/controller_bg.gif);">');
	document.write('<table width="66" border="0" cellspacing="0" cellpadding="0" style="font-size:9pt;"><tr><td height="19" width="19"></td><td width="28">');
	document.write('<img src="'+imgPath+'image/topo/arrow_up.gif" onmouseout="javascript:swapImage(\'image_up\', \'image/topo/arrow_up.gif\');" onmouseover="javascript:swapImage(\'image_up\', \'image/topo/arrow_up_over.gif\');" alt="�����ƶ� | W" width="28" height="19" onclick="javascript:moveAction(\'up\');" style="cursor:hand;" name="image_up" id="image_up" />');
	document.write('</td><td height="19" width="19"></td></tr><tr><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_left.gif" onmouseout="javascript:swapImage(\'image_left\', \'image/topo/arrow_left.gif\');" onmouseover="javascript:swapImage(\'image_left\', \'image/topo/arrow_left_over.gif\');" alt="�����ƶ� | A" width="19" height="28" onclick="javascript:moveAction(\'left\');" style="cursor:hand;" name="image_left" id="image_left" />');
	document.write('</td><td align="center" valign="middle" style="text-align:center;">');
	document.write('<img src="'+imgPath+'image/topo/arrow_center.gif" onmouseout="javascript:swapImage(\'image_center\', \'image/topo/arrow_center.gif\');" onmouseover="javascript:swapImage(\'image_center\', \'image/topo/arrow_center_over.gif\');" alt="��λ | R" width="19" height="19" onclick="javascript:moveAction(\'origin\');" style="cursor:hand;" name="image_center" id="image_center" />');
	document.write('</td><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_right.gif" onmouseout="javascript:swapImage(\'image_right\', \'image/topo/arrow_right.gif\');" onmouseover="javascript:swapImage(\'image_right\', \'image/topo/arrow_right_over.gif\');" alt="�����ƶ� | D" width="19" height="28" onclick="javascript:moveAction(\'right\');" style="cursor:hand;" name="image_right" id="image_right" />');
	document.write('</td></tr><tr><td></td><td>');
	document.write('<img src="'+imgPath+'image/topo/arrow_down.gif" onmouseout="javascript:swapImage(\'image_down\', \'image/topo/arrow_down.gif\');" onmouseover="javascript:swapImage(\'image_down\', \'image/topo/arrow_down_over.gif\');" alt="�����ƶ� | S" width="28" height="19" onclick="javascript:moveAction(\'down\');" style="cursor:hand;" name="image_down" id="image_down" />');
	document.write('</td><td></td></tr></table></div>');
}

function loadSizeController() 
{
	document.write('<div id="sizeController" style="position:absolute;top:80px;left:8px;z-index:998;background-image:url(image/controller_bg2.gif);width:60px;">');
	document.write('<table width="58" height="20" border="0" cellspacing="0" cellpadding="0"><tr><td width="18" height="20" style="padding-left:2px;">');
	document.write('<img src="'+imgPath+'image/topo/zoom_out.gif" onmouseout="javascript:swapImage(\'image_out\', \'image/topo/zoom_out.gif\');" onmouseover="javascript:swapImage(\'image_out\', \'image/topo/zoom_out_over.gif\');" alt="��С��ͼ | Z" width="18" height="16" onclick="javascript:zoomAll(\'out\');" style="cursor:hand;" name="image_out" id="image_out" />');
	document.write('</td><td align="center" style="text-align:center;">');
	document.write('<img src="'+imgPath+'image/topo/zoom_reset.gif" onmouseout="javascript:swapImage(\'image_reset\', \'image/topo/zoom_reset.gif\');" onmouseover="javascript:swapImage(\'image_reset\', \'image/topo/zoom_reset_over.gif\');" alt="��ԭ | X" width="18" height="16" onclick="javascript:zoomAll();" style="cursor:hand;" name="image_reset" id="image_reset" />');
	document.write('</td><td width="18" height="20">');
	document.write('<img src="'+imgPath+'image/topo/zoom_in.gif" onmouseout="javascript:swapImage(\'image_in\', \'image/topo/zoom_in.gif\');" onmouseover="javascript:swapImage(\'image_in\', \'image/topo/zoom_in_over.gif\');" alt="�Ŵ���ͼ | C" width="18" height="16" onclick="javascript:zoomAll(\'in\');" style="cursor:hand;" name="image_in" id="image_in" />');
	document.write('</td></tr></table></div>');
}

/**** �����ƶ�������ĸ���ť�¼������Ű�ť�¼� -- ���� ****/

function displayInfoFrame() 
{
	var divTop = document.all.linkedline.style.top;
	//divTop = divTop.substring(0, divTop.length - 2);
	var top = parseInt(divTop);
	if (top > (defaultY + parentScrollTop)) 
	{
		document.all.linkedline.style.top = (top - 4) + "px";
	}
	else {
		document.all.linkedline.style.top = (defaultY + parentScrollTop) + "px";
		clearTimer();
	}
}

/**** �༭��� -- ��ʼ ****/

var offsetX;	// ƫ��λ��X
var offsetY;	// ƫ��λ��Y

function loadEditPanel()
{
	document.write('<div class="edit_panel" id="editPanel" title="�༭���" onmousedown="editMouseDown()" onmouseup="editMouseUp()" onmousemove="">');
	document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0" style="padding:1px;background-color:#F5F5F5;"><tr><td>');
	document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr>');
	document.write('<td align="center" style="padding:4px;padding-bottom:2px;border-bottom:#666666 1px solid;background-color:#3979B9;cursor:move;"><font color="#F5F5F5"><b>�༭���</b></font></td>');//background-color:#9FBDE2;
	document.write('</tr></table>');
	document.write('</td></tr>');
	document.write('<tr><td>');
	document.write('<table width="100%" border="0" cellspacing="0" cellpadding="0"><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="adddevice" class="button_add_device_out" onmouseover="javascript:buttonAddDeviceOver();" onmouseout="javascript:buttonAddDeviceOut();" onclick="javascript:addDevice();" value="����豸" title="����ͼ������豸"/></td>');
	document.write('</tr><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="deldevice" class="button_del_device_out" onmouseover="javascript:buttonDelDeviceOver();" onmouseout="javascript:buttonDelDeviceOut();" onclick="javascript:delDevice();" value="ɾ���豸" title="����ͼ��ɾ���豸"/></td>');
	document.write('</tr><tr>');
	//document.write('<td align="center" style="padding:2px;"><input type="button" name="editdevice" class="button_edit_device_out" onmouseover="javascript:buttonEditDeviceOver();" onmouseout="javascript:buttonEditDeviceOut();" onclick="javascript:editDevice();" value="�༭�豸" title="�༭��ͼ���Ѵ��ڵ��豸"/></td>');
	//document.write('</tr><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="addline" class="button_add_line_out" onmouseover="javascript:buttonAddLineOver();" onmouseout="javascript:buttonAddLineOut();" onclick="javascript:addLine();" value="�����·" title="����ͼ�������·"/></td>');
	document.write('</tr><tr>');
	document.write('<td align="center" style="padding:2px;"><input type="button" name="delline" class="button_del_line_out" onmouseover="javascript:buttonDelLineOver();" onmouseout="javascript:buttonDelLineOut();" onclick="javascript:delLine();" value="ɾ����·" title="����ͼ��ɾ����·"/></td>');
	document.write('</tr></table>');
	document.write('</td></tr></table>');
	document.write('</div>');
}

function updateEditPanel(x, y, display)
{
	document.all.editPanel.style.left = x + "px";
	document.all.editPanel.style.top = y + "px";
	if (display)
	{
		document.all.editPanel.style.visibility = "visible";
	}
	else
	{
		document.all.editPanel.style.visibility = "hidden";
	}
}

function queryEditPanel()
{
	var divLeft = document.all.editPanel.style.left;
	//divLeft = divLeft.substring(0, divLeft.length - 2);
	var x = parseInt(divLeft);
	
	var divTop = document.all.editPanel.style.top;
	//divTop = divTop.substring(0, divTop.length - 2);
	var y = parseInt(divTop);
	
	var display = false;
	if (document.all.editPanel.style.visibility == "visible")
	{
		display = true;
	}
	else
	{
		display = false;
	}
	
	var res = new Array();
	res[0] = x;
	res[1] = y;
	res[2] = display;
	return res;
}

// �������������¼�
function editMouseDown()
{
	document.all.editPanel.setCapture();
	offsetX = event.x - document.all.editPanel.offsetLeft;
	offsetY = event.y - document.all.editPanel.offsetTop;
	document.all.editPanel.onmousemove = editMouseMove;
}


// ��������ƶ��¼�
function editMouseMove()
{
	var eventX = window.event.x;
	var eventY = window.event.y;

	var tempX = eventX - offsetX;
	var tempY = eventY - offsetY;
	
	updateEditPanel(tempX, tempY, true);
}


// ���������ͷ��¼�
function editMouseUp()
{
	document.all.editPanel.releaseCapture();
	document.all.editPanel.onmousemove = null;
}

function addDevice()
{
	openProcDlg();  //��ʾ����
	//status����������ʾ����ҳ�Ի���
	var status = "dialogHeight:200px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("addDevice.jsp", window, status);
	if (value == null)
	{
		return;
	}
	
	var tmpArray = value.split("#");
	var page = "addDevice.jsp";
	if (tmpArray.length == 2)
	{
		page += "?ipaddress=" + tmpArray[0];
		page += "&linked=" + tmpArray[1];
	}
	else
	{
		alert("ҳ�������������");
		return;
	}

	openProcDlg();

	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function delDevice()
{
	openProcDlg(); //��ʾ����
	var status = "dialogHeight:280px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("delDevice.jsp", window, status);
	if (value == null)
	{
		return;
	}

	var page = "delDevice.jsp";
	page += "?deleted=" + value;
	
	openProcDlg();
	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function addLine()
{
	openProcDlg();
	var status = "dialogHeight:350px;dialogWidth:600px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("addLink.jsp", window, status);
	if (value == null) 
	{
		return;
	}
	
	var tmp = value.split(",");
	var page = "addLink.jsp";
	page += "?line=" + tmp[0];
	page += "&ifIndex=" + tmp[1];
	
	openProcDlg();
	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function delLine()
{
	openProcDlg();
	var status = "dialogHeight:400px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	var value = window.showModalDialog("delLink.jsp", window, status);
	if (value == null) 
	{
		return;
	}
	
	var page = "delLink.jsp";
	page += "?deleted=" + value;
	
	openProcDlg();
	status = "dialogHeight:170px;dialogWidth:400px;status:0;help:0;edge:sunken;scroll:0;center:yes;resizable:no";
	window.showModalDialog(page, window, status);
}

function buttonAddDeviceOut() 
{
	window.event.srcElement.className="button_add_device_out";
}
function buttonAddDeviceOver() 
{
	window.event.srcElement.className="button_add_device_over";
}

function buttonDelDeviceOut() 
{
	window.event.srcElement.className="button_del_device_out";
}
function buttonDelDeviceOver() 
{
	window.event.srcElement.className="button_del_device_over";
}

function buttonEditDeviceOut() 
{
	window.event.srcElement.className="button_edit_device_out";
}
function buttonEditDeviceOver() 
{
	window.event.srcElement.className="button_edit_device_over";
}

function buttonAddLineOut() 
{
	window.event.srcElement.className="button_add_line_out";
}
function buttonAddLineOver() 
{
	window.event.srcElement.className="button_add_line_over";
}
function buttonDelLineOut() 
{
	window.event.srcElement.className="button_del_line_out";
}
function buttonDelLineOver() 
{
	window.event.srcElement.className="button_del_line_over";
}


/**** �༭��� -- ���� ****/


/**** ������� -- ��ʼ ****/
//����Ҫ��һ�������ƶ�����---��μ����ƶ��ķ���ʹ�С

var anchorPos = new Array();
anchorPos[0] = 300;//(viewWidth - 88) / 2;
anchorPos[1] = 200;
var flashTimer;
var closeAncTimer;

//var mappingIP = window.parent.bottomFrame.getMappingIP();

document.write("<div id=\"postLayer\" style=\"position:absolute;left:"+ anchorPos[0] +"px;top:"+ anchorPos[1] +"px;visibility:hidden;z-index:993;width:88px;height:100px;\">");
document.write("<table width=\"100%\" height=\"100\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr>");
document.write("<td style=\"border:#FF0000 2px dotted\">&nbsp;</td>");
document.write("</tr></table>");
document.write("</div>");

//yangjun add ����id�����豸�ڵ�
function getNodeId(id)
{
	for (var i = 0; i < nodeIdAry.length; ++i)
	{
		var tmp = nodeIdAry[i].split(",");
		if (tmp[0] == id)
		{
			var coor = new Array();
			coor[0] = tmp[1];
			coor[1] = tmp[2];
			return coor;
		}
	}
	return null;
}

function getNodeCoor(ip)
{
	for (var i = 0; i < nodeCoorAry.length; ++i)
	{
		var tmp = nodeCoorAry[i].split(",");
		if (tmp[0] == ip)
		{
			var coor = new Array();
			coor[0] = tmp[1];
			coor[1] = tmp[2];
			return coor;
		}
	}
/*	
	var key = null;
	// �� nodeIPMap �в��������������IP
	for (var i = 0; i < exDevIPAry.length; ++i)
	{
		if (mappingIP.containsKey(exDevIPAry[i]))
		{
			var element = mappingIP.get(exDevIPAry[i]);
			var value = element.value;
			for (var j = 0; j < value.length; ++j)
			{
				if (value[j] == ip)
				{
					key = element.key;
				}
			}
		}
	}
	
	if (key != null)
	{
		for (var i = 0; i < nodeCoorAry.length; ++i)
		{
			var tmp = nodeCoorAry[i].split(",");
			if (tmp[0] == key)
			{
				var coor = new Array();
				coor[0] = tmp[1];
				coor[1] = tmp[2];
				return coor;
			}
		}
		return "�������� "+ ip +" ��Ӧ���豸�������豸δ��������ͼ�С�\r\n�뽫��ͼ��ȫչ�����ٽ��в��ҡ�";
	}
	*/
	return null;
}

function setMainLayerPos(x, y)
{
	document.all.divLayer.style.left = x;
	document.all.divLayer.style.top = y;
}

function showAnchor()
{
	clearInterval(flashTimer);
	clearTimeout(closeAncTimer);
	document.all.postLayer.style.visibility = "visible";
	flashTimer = setInterval("flashAnchor()", 500);
	closeAncTimer = setTimeout("closeAnchor()", 30*1000);
}

function flashAnchor()
{
	var visi = document.all.postLayer.style.visibility;
	if (visi == "visible")
	{
		document.all.postLayer.style.visibility = "hidden";
	}
	else
	{
		document.all.postLayer.style.visibility = "visible";
	}
}

function closeAnchor()
{
	clearInterval(flashTimer);
	clearTimeout(closeAncTimer);
	document.all.postLayer.style.visibility = "hidden";
}

// ���ݽڵ�������ƶ�����
// ������������ϵ�һ���ڵ����꣬�ƶ�֮��ʹ�ڵ�λ�� Anchor ��
function moveMainLayer(coor)
{
	var x = y = 0;
	coor[0] = parseInt(coor[0]);
	coor[1] = parseInt(coor[1]);
	if (coor[0] > 0)
	{
		x = coor[0] - anchorPos[0];
		x = 0 - x;
	}
	else
	{
		x = anchorPos[0] - coor[0];
	}
	
	if (coor[1] > 0)
	{
		y = coor[1] - anchorPos[1];
		y = 0 - y;
	}
	else
	{
		y = anchorPos[1] - coor[1];
	}

	setMainLayerPos(x+23, y+10);
	
	showAnchor();
}

/**** ������� -- ���� ****/

/**** ����ʾ���豸 -- ��ʼ 
handleDropEvent=function(oEvent)
{

    switch(oEvent.type) 
    {
    
    	case "drop":
    	    //window.status="drop";
            break;                   	
        case "dragover":        
            //window.status="dragover";       
            oEvent.returnValue = false;        
        	break;
        case "dragenter":
            //window.status="dragenter";
            oEvent.returnValue = false;                             
            break;        
        case "dragleave":
            //window.status="dragleave";               
            break;
        case "dragend":
            //window.status="dragend";            
            Topology.createEquip(oEvent);
            break;                     
    }
}
//��������ͼʾ���豸
createEquip=function(oEvent)
{	
    var x=0,y=0;    
    var typeName="",unitId="",
        equipName="",defaultIconId="",defaultIconPath="",statusIconId="",statusIconPath="../img/topology/status/normal.gif",text="";
    var srcElement=oEvent.srcElement;
    var tagName=srcElement.tagName;
    var width=92,height=70;
    var setting=new Array();    
	x=oEvent.clientX + Topology.body.scrollLeft;
	y=oEvent.clientY + Topology.body.scrollTop;
	if(tagName=="IMG")
	{
	    typeName="ʾ���豸";   		
	    text=srcElement.alt;
	    defaultIconPath=srcElement.id;
	}   
	if(tagName=="A")
	{
		//�ύ����
		unitId=srcElement.id;
	}	
	setting[0]=unitId;
	setting[1]=typeName;	
	setting[2]=equipName;
    setting[3]=text;	
	setting[4]=defaultIconId;
	setting[5]=defaultIconPath;
	setting[6]=statusIconId;
	setting[7]=statusIconPath;	
	setting[8]=x;	
    setting[9]=y;	
	setting[10]=width;	
    setting[11]=height;	
	topology.addEquip(setting);
}****/
// �Ҽ������߲˵�yangjun�޸�
function showLineInfo(lineid)
{
	window.open('../network/linkedline.jsp?line='+ lineid,'window', 'toolbar=no,height=800,width=850,scrollbars=yes,center=yes,screenY=0');
	return;
}
function replaceNodePic(data){
    if(data!=null){
        //alert(data);
    	document.all("node_"+data.split(":")[0]).src = "../../resource/"+data.split(":")[1];
        //alert(document.all("node_"+data.split(":")[0]).src);
    }
}
/****ȷ����·�澯*****/  
function replaceLinkPic(data){
    if(data!=null){
        //alert(data);
    	document.all(data).strokecolor = "green";
        //alert(document.all("node_"+data.split(":")[0]).src);
    }
}
