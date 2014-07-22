
/*
 * ����:wangxiangyong
 * �ű����ƣ���������ͼ
 * ���� showTopology(ajaxurl); ������� ajaxurl ���ض���ΪNodeList�ṹ��json�����ı�
 * 
 */
 
//��Ϣ����ʽ
var infoBgColor = "#F5F5F5";
var infoBorder = "solid black 1px";
var infoBorderColor = "#003399";
var infoBorderWidth = 1;
var infoDelay = 250000;          // �����ӳٵ�ʱ��
var infoFontColor = "#000000";
var infoFontFace = "\u5b8b\u4f53,arial,helvetica,sans-serif";
var infoFontSize = "12px";
var infoFontWeight = "normal";     // alternative: "bold";
var infoPadding = 3;            // spacing between border and content
var infoTitleColor = "#ffffff";    // color of caption text
var infoWidth = 180;
//��û���
var mainBoard;
var mbWidth;
var mbHeight;
var mbLeft;
var mbTop;

//xml����
var xmldoc;
var tempArray = new Array();
//��ʼ��
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
 //��������ͼ
function save(rootPath,slatype)
{
var url=rootPath+"/resource/xml/"+slatype+".jsp";
	xmldoc=initXML(url);
	var nodes = xmldoc.getElementsByTagName("node");
	if(tempArray.length==0){
	  alert("�޷����棬�������ݣ�");
	  return;
	}
	for (var i = 0; i < tempArray.length; i += 1)
	{
		for (var j = 0; j < nodes.length; j += 1)
		{
			var node = nodes[j];
			var id = node.getElementsByTagName("id")[0].text;

			if (tempArray[i] == id)
			{
				node.getElementsByTagName("x")[0].text = document.getElementById(tempArray[i]).style.left;
				node.getElementsByTagName("y")[0].text =  document.getElementById(tempArray[i]).style.top;
			}
		}
	}
	
	saveData(rootPath,slatype,xmldoc.xml);
}
//��������ͼ����
function saveData(rootPath,slatype,dataxml){
var ajaxurl=rootPath+"/serviceQualityAjaxManager.ajax?action=saveData"
	$.ajax({
	    url:ajaxurl,
	    type:"POST", 
	    dataType:"json",
	    timeout:25000,
	    data:"data="+dataxml+"&slatype="+slatype,
	    error:function () {
		   alert("��ʱ�޷���ͨ�����Ժ����ԣ�");
	    }, 
	    success:function (data) {
		  alert(data.flag)
	    }});
}
//��ʾ����ͼ
function showTopology(ajaxurl) {
	getBoard();//��û���
	loadDataSrc(ajaxurl, analysisData);//���Զ������
}
	
//��û���
function getBoard() {
	mainBoard = document.getElementById("board");
	mainBoard.style.position = "absolute";
	mbWidth = mainBoard.style.width.replace("px", "") * 1;//������
	mbHeight = mainBoard.style.height.replace("px", "") * 1;//����߶�
	mbLeft = mainBoard.style.left.replace("px", "") * 1;//���������
	mbTop = mainBoard.style.top.replace("px", "") * 1;//����������
}

//���Զ������
function loadDataSrc(ajaxurl) {
	$.ajax({
	    url:ajaxurl,
	    type:"GET", 
	    dataType:"html",
	    timeout:250000,
	    data:null,
	    error:function () {
		   alert("���Ӵ������Ժ����ԣ�");
	    }, 
	    success:function (html) {
		   analysisData(html);
	    }});
}
var obj;
//��������ʾ�ڵ�����
function analysisData(data) {
	 obj = eval("(" + data + ")");
	var i, j;
	var dragSet = "";
	if(obj.nodeList.length==0){
	alert("�������ݣ����Ժ�����");
	return;
	}
	//�������нڵ�
	for (i = 0; i < obj.nodeList.length; i++) {
		var node = obj.nodeList[i];
		createNode( node.id, node.name, node.url, node.deviceInfo);
		//dragSet = dragSet + "$('#" + node.id + "').drag();";
		
		
		var nodeDiv= document.getElementById(node.id);
		nodeDiv.style.left =node.x+"px";
		nodeDiv.style.top = node.y+"px";
		 dragSet=dragSet+"$('#"+ node.id+"').dragDrop({fixarea:[0,"+(mbWidth-32)+",0,"+(mbHeight-32)+"]});";
		tempArray.push(node.id);
	}
	//$('#1').dragDrop({fixarea:[0,mbWidth-50,0,mbHeight-50]});
	//$('#101015257').dragDrop({fixarea:[0,mbWidth-50,0,mbHeight-50]});
	//alert(dragSet);
	 eval(dragSet);
	//for( v=0;v<tempArray.length;v++){
	//alert(tempArray[v].id);}
     //��ʼ����������
	for (i = 0; i < obj.linkList.length; i++) {
		var link = obj.linkList[i];
		initLinkLine(link.id, link.from,link.to, link.linkInfo,link.linkStatus,link.linkWeight);
		
	}

}
//��ʼ������
function initLinkLine(id,fromId,toId,linkInfo,linkStatus,linkWeight) {
	try {
	
	//����븸���֮��Ĺ�ϵ,����
		var nodeObj_1 = document.getElementById(fromId);
		var nodeObj_2 = document.getElementById(toId);
		var obj1Width = nodeObj_1.style.width.replace("px", "") * 1;
		var obj1Height = nodeObj_1.style.height.replace("px", "") * 1;
		var obj1Left = nodeObj_1.style.left.replace("px", "") * 1;
		var obj1Top = nodeObj_1.style.top.replace("px", "") * 1;
		var obj2Width = nodeObj_2.style.width.replace("px", "") * 1;
		var obj2Height = nodeObj_2.style.height.replace("px", "") * 1;
		var obj2Left = nodeObj_2.style.left.replace("px", "") * 1;
		var obj2Top = nodeObj_2.style.top.replace("px", "") * 1;
		
		var line = document.createElement("v:line");
		line.lineid = "3";
		line.id = id;
		line.style.position = "absolute";
		line.style.zIndex = 0;
		line.from = (parseInt(obj1Left) + parseInt(obj1Width) / 2) + "," + (parseInt(obj1Top) + parseInt(obj1Height) / 2);
		line.to = (parseInt(obj2Left) + parseInt(obj2Width) / 2) + "," + (parseInt(obj2Top) + parseInt(obj2Height) / 2);
		
		line.strokecolor = linkStatus;
		
		line.strokeweight = linkWeight;// 1;
		document.all.board.appendChild(line);
		//��·����ʾ��Ϣ
		 divLineInfo = document.createElement("div");
		divLineInfo.id ="link_"+id;
		divLineInfo.style.position = "absolute";
		divLineInfo.style.border = infoTitleColor;
		divLineInfo.style.width = 220;
		divLineInfo.style.height = "auto";
		divLineInfo.style.color = infoFontColor;
		divLineInfo.style.padding = infoPadding;
		divLineInfo.style.display = "block";
		divLineInfo.style.lineHeight = "120%";
		divLineInfo.style.zIndex = -2;
		divLineInfo.style.backgroundColor = infoTitleColor;
		divLineInfo.style.visibility = "visible";
		divLineInfo.style.fontSize = "12px";
		divLineInfo.innerHTML = linkInfo;
		document.all.board.appendChild(divLineInfo);
		document.all(divLineInfo.id).style.left = parseInt(obj1Left)/2 + parseInt(obj1Width) /4+parseInt(obj2Left)/2 + parseInt(obj2Width) / 4;
		document.all(divLineInfo.id).style.top = parseInt(obj1Top)/2 + parseInt(obj1Height) / 4+parseInt(obj2Top)/2 + parseInt(obj2Height) /4;
		
	}
	catch (e) {
	}
}


//��ӽڵ���Ϣ
function createNode(nodeId,nodeName,imgUrl,deviceInfo) {
	//�ڻ���������µĽڵ�
	var objHtml = "<div onMouseOver=\"showInfo('" + nodeId + "')\" onMouseOut=\"hiddenInfo('" + nodeId + "')\" onMouseMove=\"drawLinkLine('" + nodeId + "')\" id='" + nodeId +"' format='0'  ></div>";
	mainBoard.innerHTML = mainBoard.innerHTML + objHtml;
	
	//�������еĽڵ�
	
	//����´����Ľڵ����
	var nodeObj = document.getElementById(nodeId);
	try {
		nodeObj.style.position = "absolute";
		nodeObj.style.top = "0px";
		nodeObj.style.left = "0px";
		nodeObj.style.zIndex = 1;
		nodeObj.style.width = "32px";
		nodeObj.style.height = "32px";
		nodeObj.innerHTML = "<img src=\"" + imgUrl + "\"/><div style='font-size:12px;align:left;'>"+nodeName+"</div>";
	}
	catch (e) {
	
		//alert(nodeObj);
	}
	//�����豸����ʾ��Ϣ
	var divInfo = document.createElement("div");
	divInfo.id = "info_" + nodeId;
	divInfo.name = "ssssssss";// alias+"("+ip+")";
	divInfo.style.position = "absolute";
	divInfo.style.border = infoBorder;
	divInfo.style.width = infoWidth;
	divInfo.style.height = "auto";
	divInfo.style.color = infoFontColor;
	divInfo.style.padding = infoPadding;
	divInfo.style.lineHeight = "120%";
	divInfo.style.zIndex = 5;
	divInfo.style.backgroundColor = infoBgColor;
	divInfo.style.left = parseInt(nodeObj.style.left.replace("px", ""), 10) + 32;
	divInfo.style.top = parseInt(nodeObj.style.top.replace("px", ""), 10);
	divInfo.style.visibility = "hidden";
	divInfo.style.fontSize = "12px";
	divInfo.innerHTML = deviceInfo;
	document.all.board.appendChild(divInfo);
}

//��ʾ�豸��Ϣ
function showInfo(nodeId) {
	var nodeObj = document.getElementById(nodeId);
	
	document.all("info_" + nodeId).style.left = parseInt(nodeObj.style.left.replace("px", ""), 10) + 52;
	document.all("info_" + nodeId).style.top = parseInt(nodeObj.style.top.replace("px", ""), 10) + 10;
	document.all("info_" + nodeId).style.visibility = "visible";
}
function hiddenInfo(nodeId) {
nodeId=eval(nodeId);
	document.all("info_" + nodeId).style.visibility = "hidden";
}



//�����ӽڵ�Ĳ�
function drawLinkLine(nodeId) {
	try {
	for (i = 0; i < obj.linkList.length; i++) {
		var link = obj.linkList[i];
		if(link.from==nodeId||link.to==nodeId){
		//����븸���֮��Ĺ�ϵ
		
		var nodeObj_1 = document.getElementById(link.from);
		var nodeObj_2 = document.getElementById(link.to);
		var obj1Width = nodeObj_1.style.width.replace("px", "") * 1;
		var obj1Height = nodeObj_1.style.height.replace("px", "") * 1;
		var obj1Left = nodeObj_1.style.left.replace("px", "") * 1;
		var obj1Top = nodeObj_1.style.top.replace("px", "") * 1;
		var obj2Width = nodeObj_2.style.width.replace("px", "") * 1;
		var obj2Height = nodeObj_2.style.height.replace("px", "") * 1;
		var obj2Left = nodeObj_2.style.left.replace("px", "") * 1;
		var obj2Top = nodeObj_2.style.top.replace("px", "") * 1;
		//if(obj2Left>900||obj1Left<12) $("#"+nodeId).jqResize();
		var line = document.getElementById(link.id);
		line.from = (parseInt(obj1Left) + parseInt(obj1Width) / 2) + "," + (parseInt(obj1Top) + parseInt(obj1Height) / 2);
		line.to = (parseInt(obj2Left) + parseInt(obj2Width) / 2) + "," + (parseInt(obj2Top) + parseInt(obj2Height) / 2);
		var id="link_"+link.id;
	    document.all(id).style.left = parseInt(obj1Left)/2 + parseInt(obj1Width) /4+parseInt(obj2Left)/2 + parseInt(obj2Width) / 4;
		document.all(id).style.top = parseInt(obj1Top)/2 + parseInt(obj1Height) / 4+parseInt(obj2Top)/2 + parseInt(obj2Height) /4;
		}
	
	}
	
	
	}
	catch (e) {
	}
}
