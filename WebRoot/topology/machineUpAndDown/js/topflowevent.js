/*--------------------------------------------------|
| ����Ʒȡ��ԭ������Ȩ�޸��� support@tops.com.cn    |
| ����Ʒtopflow                                     |
|                                                   |
| ���ļ���TopFlow�ĺ����ļ��������˸������õ���     |
| �¼��Լ�������                                  |
|                                                   |
| ��Ȩ���Ϻ�ѩ����Ϣ�������޹�˾���У�              |
| ����֧�֣�sales@shcommit.com �����Ը����û���     |
| ��    վ��www.shcommit.com                        |
|                                                   |
| ����˽�Կ������޸Ļ�������ҵ��;                  |
| ���뱣����ע��.                                   |
|                                                   |
| Updated: 20070613                                 |
|--------------------------------------------------*/

//global variable

var _FLOW = new TTopFlow("");
var _TOOLTYPE = "point";
var _CURRENTX = _CURRENTY = 0;
var _FOCUSTEDOBJ = null;
var _ZOOM = 1;

var _MOVEOBJ = null;
var _MOVETYPE = "";
var _DOLOG = [];
var _DOLOGINDEX = -1;
var _strPt1 = "";
var _strPt2 = "";
var _strSltPt = "";
var _strLine1 = "";
var _strLine2 = "";
var _strSltLine = "";
var _PointOrLine;
var isSelectPoint = 0;
var isSelectLine=0;
var _clkPx = 0;
var _clkPy = 0;
var ptMoveType = "";
var oOval = null;
var _logMoveType = "";
var _MOVELINEOBJ = null;
//ѡ������ͼԪ�ض�����ʾѡ��Ч�� 

function objFocusedOn(objId)
{
	//���ԭ��ѡ�еĶ���
	objFocusedOff();
	_FOCUSTEDOBJ = document.getElementById(objId);
	if(_FOCUSTEDOBJ != null) 
	{
		
		var x = (event.x + document.body.scrollLeft) / _ZOOM;
		var y = (event.y + document.body.scrollTop) / _ZOOM;
		_FOCUSTEDOBJ.StrokeColor = _FOCUSTEDOBJ.fsc;
		
		_MOVEOBJ = document.getElementById(objId);
		_clkPx = x/4*3+"pt";
		_clkPy = y/4*3+"pt";
		if(_FOCUSTEDOBJ.tagName=="PolyLine")
		{
			_MOVELINEOBJ = new TStep(_FLOW);
			_MOVELINEOBJ.clone(_FLOW.getStepByID(objId));
			_MOVEOBJ = _FLOW.getStepByID(objId);
			var strPt =_MOVEOBJ.Points;
			var aryPt = strPt.split(',');
			var nPt=aryPt.length-1;
			_strPt2 = "";
			_strPt1 = "";
			_strLine2 = "";
			_strLine1 = "";
			for(i=0;i<=nPt;i=i+2)
			{
				var m = aryPt[i].substr(0,aryPt[i].length-2)*4/3;
				var n = aryPt[i+1].substr(0,aryPt[i+1].length-2)*4/3;
				var sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
				if(isSelectPoint==0&&sqrta<=10)
				{
					_PointOrLine = 0;
					isSelectPoint=1;
					_MOVETYPE="line_m";			  		
					_strSltPt = aryPt[i]+',' +aryPt[i+1];
					_clkPx = aryPt[i];
					_clkPy = aryPt[i+1];
					if (i==0)ptMoveType="from";
					if (i==nPt-1)ptMoveType="to";
				}
				else
				{
					if(isSelectPoint==1)
						_strPt2 = _strPt2+','+aryPt[i]+','+aryPt[i+1];
					else
						_strPt1 = _strPt1+','+aryPt[i]+',' +aryPt[i+1];
				}

				if(i<=nPt-3)
				{
					var r = aryPt[i+2].substr(0,aryPt[i+2].length-2)*4/3;
					var s = aryPt[i+3].substr(0,aryPt[i+3].length-2)*4/3;
					
					if((Math.abs(x*(n-s)+y*(r-m)+(m*s-n*r))/Math.sqrt((n-s)*(n-s)+(r-m)*(r-m))<=5)&&(isSelectLine==0)&&sqrta>10&&isSelectPoint==0)
					{
						_PointOrLine = 1;
						_MOVETYPE="line_m";
						isSelectLine = 1;
						_strSltLine = aryPt[i]+','+aryPt[i+1]+','+aryPt[i+2]+','+aryPt[i+3];
						_clkPx = x/4*3+"pt";
						_clkPy = y/4*3+"pt";
					}
					else
					{
						if(isSelectLine==1)
						{
							if (i <= nPt-3)
							{
								_strLine2 = _strLine2+','+aryPt[i+2]+','+aryPt[i+3];
							}
						}
						else
						{
							_strLine1 = _strLine1 + "," + aryPt[i]+',' +aryPt[i+1];
						}
					}
				}
			}
			if(_strPt1!= '')
			   _strPt1 = _strPt1.substr(1)+',';
			if(_strLine1!='')
			   _strLine1 = _strLine1.substr(1)+",";
		}
	}  
	stuffProp();
}
function createOval(x,y)
{
	oOval = document.createElement("v:oval");
	oOval.style.position = "absolute";
	oOval.style.width = "6px";
	oOval.style.height="6px"
	oOval.style.left = x;
	oOval.style.top = y;
	oOval.fillcolor = "red";
	oOval.strokecolor = "red";
	document.body.appendChild(oOval);
}
//��HTML����ת����PolyLineʵ��
function toPolyLineObj(objTemp)
{
	var objLine = _FLOW.getStepByID(objTemp.id);
	return objLine;
}
//�������ݱ�
function updateFlow(htmlObj){

}

//����ѡ������ͼԪ�ض�����ʾδѡ��Ч��
function objFocusedOff(){
  if(_FOCUSTEDOBJ != null) 
  {
	  _FOCUSTEDOBJ.StrokeColor = _FOCUSTEDOBJ.sc; //�ָ�ԭ������ɫ
  }
  _FOCUSTEDOBJ = null;
  isSelectPoint = 0;
  isSelectLine = 0;
  ptMoveType = "";
  oOval = null;
  return;
}

//ɾ������ͼԪ�ض���
function deleteObj(ObjId){
  var obj = document.getElementById(ObjId);
  if(obj == null) return false;
  if(obj.typ != "Proc" && obj.typ != "Step") return false;
  if(obj.typ == "Proc" && (obj.st == "BeginProc" || obj.st == "EndProc")){
    alert("����ɾ����һ������ͼ�У���ʼ���������Ǳ����");
    return false;
  }
  if(confirm("ȷ��Ҫɾ��[" +obj.title + "]��")){
    objFocusedOff();
    if(obj.typ == "Proc"){
      var Proc = _FLOW.getProcByID(ObjId);
      _FLOW.deleteProcByID(ObjId);
      pushLog("delproc", Proc);
    }
    else{
      var Step = _FLOW.getStepByID(ObjId);
      _FLOW.deleteStepByID(ObjId);
      pushLog("delstep", Step);
    }
    _FLOW.Modified = true;
    DrawAll();
  }
}

//����[����]��IDֵ����ԭ·������ָ������ID
function changeProcID(OldID, NewID){
  var Step;
  for(var i = 0; i< _FLOW.Steps.length; i++){
    Step = _FLOW.Steps[i];
    if(Step.FromProc == OldID) Step.FromProc = NewID;
    if(Step.ToProc == OldID) Step.ToProc = NewID;
  }
}
function saveStepsToProc(obj)
{
	for(var i = 0; i < _FLOW.Steps.length; i++){
		var oStep = _FLOW.Steps[i];
		if(oStep.FromProc == obj.id || oStep.ToProc == obj.id){
			updateFlow(document.getElementById(oStep.ID));					
		}
	}
}
//����Proc��λ��
function changeProcPos(obj){
  for(var i = 0; i < _FLOW.Steps.length; i++){
    Step = _FLOW.Steps[i];
    if(Step.FromProc == obj.id || Step.ToProc == obj.id){
      objStepHTML = document.getElementById(Step.ID);
      if(Step.ShapeType == "Line"){
        Step.getPath();
        objStepHTML.from = Step.FromPoint;
        objStepHTML.to = Step.ToPoint;
      }
      else if(Step.ShapeType == "PolyLine"){
		var strPt="";
		var arrPt = Step.Points.split(",");
		var objWidth = obj.style.width;
		var objHeight = obj.style.height;
		var objX = obj.style.left;
		var objY = obj.style.top;
		var strMoveType = _MOVETYPE;
		
		objWidth = objWidth.substr(0,objWidth.length-2);
		objHeight = objHeight.substr(0,objHeight.length-2);
		objX = objX.substr(0,objX.length-2)*1;
		objY = objY.substr(0,objY.length-2)*1;
		
		if (_MOVETYPE=="")strMoveType = _logMoveType;
		switch(strMoveType){
			case "proc_sm":
			case "proc_m":
			case "proc_e":
			case "proc_n":
			case "proc_snw":
			case "proc_nw":
				if (Step.FromProc == obj.id)
				{
					strPt = (objX+objWidth*Step.fromRelX)*3/4+"pt,"+(objY+objHeight*Step.fromRelY)*3/4+"pt"
					for (var j=2; j<arrPt.length; j++)
					{
						strPt = strPt + "," + arrPt[j]
					}
				}
				if (Step.ToProc == obj.id)
				{
					for (var j=0; j<arrPt.length-2; j++)
					{
						strPt = strPt + arrPt[j] + ","
					}
					strPt = strPt + (objX+objWidth*Step.toRelX)*3/4+"pt,"+(objY+objHeight*Step.toRelY)*3/4+"pt"
				}
				Step.Points = strPt;
				break;
			default :
		}
		objStepHTML.outerHTML = Step.toString();
      }
    }
  }
  _logMoveType = "";
}

//�޸�[����]
function editProc(objId){
 /* var oldobj = new TProc(_FLOW), newobj = new TProc(_FLOW);
  var proc = _FLOW.getProcByID(objId);
  oldobj.clone(proc);
  var oldID = proc.ID;
  if(vmlOpenWin("proc.htm", proc, 450,350)){
    if(oldID != proc.ID) changeProcID(oldID, proc.ID);
    DrawAll();
    newobj.clone(proc);
    pushLog("editproc", {"_old":oldobj,"_new":newobj});
    _FLOW.Modified = true;
    objFocusedOn(proc.ID);
  }*/
}

//�޸�[·��]
function editStep(objId){
  var oldobj = new TStep(_FLOW), newobj = new TStep(_FLOW);
  var step = _FLOW.getStepByID(objId);
  oldobj.clone(step);
  /*if(vmlOpenWin("step.htm", step, 450,350)){
    _FLOW.Modified = true;
    DrawAll();
    objFocusedOn(step.ID);
    newobj.clone(step);
    pushLog("editstep", {"_old":oldobj,"_new":newobj});
  }*/
  var x = (event.x + document.body.scrollLeft) / _ZOOM;
  var y = (event.y + document.body.scrollTop) / _ZOOM;
  var strPt = step.Points;
  var aryPt = strPt.split(',');
  var nPt = aryPt.length-1;
  isSelectPoint = 0;
      _strPt2 = "";
      _strPt1 = "";
	  for(i=0;i<=nPt;i=i+2){
		var m = aryPt[i].substr(0,aryPt[i].length-2)*1.333;
	    var n = aryPt[i+1].substr(0,aryPt[i+1].length-2)*1.333;
	  	var sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
			if(isSelectPoint==0&&sqrta<=10){
			  _strSltPt = aryPt[i]+',' +aryPt[i+1];
			}
			else if(isSelectPoint==1){
				    _strPt2 = _strPt2+','+aryPt[i]+','+aryPt[i+1];
			}
			     else{
				    _strPt1 = _strPt1+','+aryPt[i]+',' +aryPt[i+1];
			}
      }    
      if(_strPt1!= ''){
	     _strPt1 = _strPt1.substr(1);
      }
  step.Points = _strPt1+_strPt2;
  updateFlow(document.getElementById(step.ID));
  document.getElementById(step.ID).outerHTML=step.toString();
  stuffProp();
  _FLOW.Modified = true;
}

//��Ҫ�ص��������
function beforePropChange(oItem){
  if("rule" in oItem){
    r = new RegExp(oItem.rule);
    if(!r.test(oItem.value)){
      alert(oItem.msg);
      oItem.focus();
      if(oItem.tagName.toLowerCase() == "input") oItem.select();
      return false;
    }
  }
  return true;
}

function setPropID(oItem){
  var obj = null,obj2 = null;
  obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var oldID = obj.ID;
  if(oldID == oItem.value) return;
  obj2 = _FLOW.getProcByID(oItem.value);
  if(obj2 == null) obj2 = _FLOW.getStepByID(oItem.value);
  if(obj2 != null){
    alert("���[" + oItem.value +"-" + obj2.Text + "]�Ѿ����ڣ����������룡");
    oItem.focus();
    oItem.select();
    return;
  }
  document.all(_FOCUSTEDOBJ.id + "Text").id = oItem.value + "Text";
  obj.ID = oItem.value;
  _FOCUSTEDOBJ.id = oItem.value;
  if(_FOCUSTEDOBJ.typ == "Proc") changeProcID(oldID, obj.ID);
  pushLog("editprop",{"obj":obj,"prop":"ID","_old":oldID,"_new":obj.ID});
}

//�޸�����
function setPropText(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.Text == oItem.value) return;
  var oldValue = obj.Text;
  obj.Text = oItem.value;
  //if(obj.ObjType == "Proc")DrawAll();
  DrawAll();
  //else
  //  DrawTree();
  objFocusedOn(obj.ID);
  pushLog("editprop",{"obj":obj,"prop":"Text","_old":oldValue,"_new":obj.Text});
}

//�޸Ļ����
function setActFlag(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.actFlag == oItem.value) return;
  var oldValue = obj.actFlag;
  if (oItem.value == "1013")
  {
	 var Porc
	  for(var i = 0; i< _FLOW.Procs.length; i ++){
		Proc = _FLOW.Procs[i];
		if (Proc.actFlag.substr(0,4)=="1013")
		{
			alert("һ���������治�������2����ʼ�");
			return false;
		}
	  }
  }
  if (oItem.value == "2013")
  {
	 var Porc
	  for(var i = 0; i< _FLOW.Procs.length; i ++){
		Proc = _FLOW.Procs[i];
		if (Proc.actFlag.substr(0,4)=="2013")
		{
			alert("һ���������治�������2�������");
			return false;
		}
	  }
  }
  obj.actFlag = oItem.value + '' + obj.actFlag.substr(4,4);
  if (oldValue.substr(0,4)=="1013" && oItem.value != "1013")
  {
	  _FLOW.deleteStepByID(_FLOW.ID);
	  DrawVML();
  }
  if (oItem.value == "2013")
  {
	  obj.ProcType = "EndProc";
	  obj.ShapeType = "Oval";
	  obj.Width = "40";
	  obj.Height = "40";
	  DrawVML();
  }
  else if (oItem.value == "1013")
  {
	  Step = new TStep(_FLOW);
	  Step.FromProc = "begin";
	  Step.Text = "���̿�ʼ";
	  Step.ID = _FLOW.ID;
	  Step.ToProc = obj.ID;
	  Step.zIndex = 2;
	  Step.ShapeType = "PolyLine";
	  _FLOW.addStep(Step);
	  DrawVML();
  }
  if (oldValue.substr(0,4)=="2013" && oItem.value != "2013")
  {
	  obj.ProcType = "NormalProc";
	  obj.ShapeType = "Rect";
	  obj.Width = "100";
	  obj.Height = "40";
	  DrawVML();
  }
  objFocusedOn(obj.ID);
}

//�޸����ִ������
function setWaittime(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.waittime == oItem.value) return;
  var oldValue = obj.waittime;
  obj.waittime = oItem.value;
}

//�޸�����ʱ����
function setIsSltTrans(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var isSltTrans = oItem.checked ? 1 : 0;
  if(obj.isSltTrans == isSltTrans) return;
  obj.isSltTrans = oItem.checked ? 1 : 0;
}

//�޸���������ʹ����ͬȨ��
function setIsSameCredit(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var isSameCredit = oItem.checked ? 1 : 0;
  if(obj.isSameCredit == isSameCredit) return;
  obj.isSameCredit = oItem.checked ? 1 : 0;
}

function setPropShapeType(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.ShapeType == oItem.value) return;
  var oldValue = obj.ShapeType;
  obj.ShapeType = oItem.value;
  DrawVML();
  objFocusedOn(obj.ID);
  pushLog("editprop",{"obj":obj,"prop":"ShapeType","_old":oldValue,"_new":obj.ShapeType});
}

function setPropTextWeight(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.TextWeight == oItem.value) return;
  var oldValue = obj.TextWeight;
  obj.TextWeight = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Text").style.fontSize = oItem.value;
  pushLog("editprop",{"obj":obj,"prop":"TextWeight","_old":oldValue,"_new":obj.TextWeight});
}

function setPropStrokeWeight(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.StrokeWeight == oItem.value) return;
  var oldValue = obj.StrokeWeight;
  obj.StrokeWeight = oItem.value;
  _FOCUSTEDOBJ.strokeweight = obj.StrokeWeight;
  pushLog("editprop",{"obj":obj,"prop":"StrokeWeight","_old":oldValue,"_new":obj.StrokeWeight});
}

function setPropzIndex(oItem){
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.zIndex == oItem.value) return;
  var oldValue = obj.zIndex;
  obj.zIndex = oItem.value;
  _FOCUSTEDOBJ.style.zIndex = obj.zIndex;
  pushLog("editprop",{"obj":obj,"prop":"zIndex","_old":oldValue,"_new":obj.zIndex});
}

function setPropX(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.X == oItem.value) return;
  var oldValue = obj.X;
  obj.X = oItem.value;
  _FOCUSTEDOBJ.style.left = obj.X;
  _MOVETYPE = "proc_sm"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"X","_old":oldValue,"_new":obj.X});
}

function setPropY(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.Y == oItem.value) return;
  var oldValue = obj.Y;
  obj.Y = oItem.value;
  _FOCUSTEDOBJ.style.top = obj.Y;
  _MOVETYPE = "proc_sm"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"Y","_old":oldValue,"_new":obj.Y});
}

function setPropW(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.Width == oItem.value) return;
  var oldValue = obj.Width;
  obj.Width = oItem.value;
  _FOCUSTEDOBJ.style.width = obj.Width;
  _MOVETYPE = "proc_snw"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"Width","_old":oldValue,"_new":obj.Width});
}

function setPropH(oItem){
  var obj = _FLOW.getProcByID(_FOCUSTEDOBJ.id);
  if(obj.Height == oItem.value) return;
  var oldValue = obj.Height;
  obj.Height = oItem.value;
  _FOCUSTEDOBJ.style.height = obj.Height;
  _MOVETYPE = "proc_snw"
  changeProcPos(_FOCUSTEDOBJ);
  pushLog("editprop",{"obj":obj,"prop":"Height","_old":oldValue,"_new":obj.Height});
}

function setPropCond(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.Cond == oItem.value) return;
  var oldValue = obj.Cond;
  obj.Cond = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Text").innerHTML = oItem.value;
  pushLog("editprop",{"obj":obj,"prop":"Cond","_old":oldValue,"_new":obj.Cond});
}

function setPropStartArrow(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.StartArrow == oItem.value) return;
  var oldValue = obj.StartArrow;
  obj.StartArrow = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Arrow").startarrow = obj.StartArrow;
  pushLog("editprop",{"obj":obj,"prop":"StartArrow","_old":oldValue,"_new":obj.StartArrow});
}

function setPropEndArrow(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.EndArrow == oItem.value) return;
  var oldValue = obj.EndArrow;
  obj.EndArrow = oItem.value;
  document.all(_FOCUSTEDOBJ.id + "Arrow").endarrow = obj.EndArrow;
  pushLog("editprop",{"obj":obj,"prop":"EndArrow","_old":oldValue,"_new":obj.EndArrow});
}

function setPropFromProc(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.FromProc == oItem.value) return;
  //�ж��Ƿ��Ѿ����ظ���Step
  if(_FLOW.StepPathExists(oItem.value, obj.ToProc)){
    alert("�Ѿ���һ��·����[" + oItem[oItem.selectedIndex].text + "]��[" + _FLOW.getProcByID(obj.ToProc).Text + "]������ģ�");
    oItem.focus();
    return false;
  }
  /*if(oItem.value == obj.ToProc && obj.ShapeType == "Line"){
    alert("������յ������ͬ��[·��]ֻ��ʹ���۽��ߣ����Ȼָ�ԭ��㲢������״��");
    oItem.focus();
    return false;
  }*/
  if(oItem.value == obj.ToProc){
	  alert("����ָ����");
	  oItem.focus();
	  return false;
  }
  var oldValue = obj.FromProc;
  obj.FromProc = oItem.value;
  if(obj.ShapeType == "Line"){
    obj.getPath();
    _FOCUSTEDOBJ.from = obj.FromPoint;
    _FOCUSTEDOBJ.to = obj.ToPoint;
  }
  else if(obj.ShapeType == "PolyLine"){
    _FOCUSTEDOBJ.points.value = obj.reGetPath();
  }
  //DrawDataView();
  pushLog("editprop",{"obj":obj,"prop":"FromProc","_old":oldValue,"_new":obj.FromProc});
}

function setPropToProc(oItem){
  var obj = _FLOW.getStepByID(_FOCUSTEDOBJ.id);
  if(obj.ToProc == oItem.value) return;
  //�ж��Ƿ��Ѿ����ظ���Step
  if(_FLOW.StepPathExists(obj.FromProc, oItem.value)){
    alert("�Ѿ���һ��·����[" + _FLOW.getProcByID(obj.FromProc).Text + "]��[" + oItem[oItem.selectedIndex].text + "]������ģ�");
    oItem.focus();
    return false;
  }
  /*if(oItem.value == obj.FromProc && obj.ShapeType == "Line"){
    alert("������յ������ͬ��[·��]ֻ��ʹ���۽��ߣ����Ȼָ�ԭ��㲢������״��");
    oItem.focus();
    return false;
  }*/
  if(oItem.value == obj.FromProc){
	  alert("����ָ����");
	  oItem.focus();
	  return false;
  }
  var oldValue = obj.ToProc;
  obj.ToProc = oItem.value;
  if(obj.ShapeType == "Line"){
    obj.getPath();
    _FOCUSTEDOBJ.from = obj.FromPoint;
    _FOCUSTEDOBJ.to = obj.ToPoint;
  }
  else if(obj.ShapeType == "PolyLine"){
    _FOCUSTEDOBJ.points.value = obj.reGetPath();
  }
  //DrawDataView();
  pushLog("editprop",{"obj":obj,"prop":"ToProc","_old":oldValue,"_new":obj.ToProc});
}
//�ı��ı���
function onTextGetFocus(e,r,c)
{
  e.cols=c;
  e.rows=r;
}

function onTextBlur(e,r,c)
{
  e.cols=c;
  e.rows=r;
}
function stuffProp(){
  for(var i = propview.rows.length - 1; i > 0; i--)
    propview.deleteRow(i);
  if(_FOCUSTEDOBJ == null) return;
  var obj = _FOCUSTEDOBJ.typ == "Proc"?_FLOW.getProcByID(_FOCUSTEDOBJ.id):_FLOW.getStepByID(_FOCUSTEDOBJ.id);
  var idTR, idTD, oSelect, oActSelect, oCoSelect, oOption;
  //ID
  idTR = propview.insertRow();
  idTR.height = "25";
  idTR.className="obj";
  idTD = idTR.insertCell();
  idTD.noWrap = true;
  idTD.style.width = "79";
  idTD.align = "right";
  idTD.innerHTML = "���";
  idTD = idTR.insertCell();
  idTD.innerHTML = '<input readonly type="text" rule="" msg="" onblur="if(beforePropChange(this)) setPropID(this);" name="pID" maxlength="10" value="' + obj.ID + '" style="width:95%;">';
  //Text
  idTR = propview.insertRow();
  idTR.height = "25";
  idTR.className="obj";
  idTD = idTR.insertCell();
  idTD.noWrap = true;
  idTD.style.width = "79";
  idTD.align = "right";
  idTD.innerHTML = "����";
  idTD = idTR.insertCell();
  idTD.innerHTML = '<input id="pName" type="text" maxlength="20" rule="^\\S+$" msg="[����]����Ϊ�ջ�������ַ���" onblur="if(beforePropChange(this)) setPropText(this);" name="pText" maxlength="10" value="' + obj.Text + '" style="width:95%;">';
  
  //ShapeType
  idTR = propview.insertRow();
  idTR.height = "25";
  idTR.className="obj";
  idTD = idTR.insertCell();
  idTD.noWrap = true;
  idTD.style.width = "79";
  idTD.align = "right";
  idTD.innerHTML = "��״";
  idTD = idTR.insertCell();
  idTD.innerHTML = '<select id="pShapeType" name="pShapeType" onblur="if(beforePropChange(this)) setPropShapeType(this);" style="width:95%;"></select>';
  oSelect = document.getElementById("pShapeType");
  //TextWeight
  idTR = propview.insertRow();
  idTR.height = "25";
  idTR.className="obj";
  idTD = idTR.insertCell();
  idTD.noWrap = true;
  idTD.style.width = "79";
  idTD.align = "right";
  idTD.innerHTML = "�ı���С";
  idTD = idTR.insertCell();
  idTD.innerHTML = '<input type="text" rule="^\\d{1,2}p[x|t]{1}$" msg="[�ı���С]������1-2���֣�����������px��pt��" onblur="if(beforePropChange(this)) setPropTextWeight(this);" name="pTextWeight" maxlength="10" value="' + obj.TextWeight + '" style="width:95%;">';
  if(_FOCUSTEDOBJ.typ == "Proc"){
	  //�����
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "�����";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<select id="pActType" onchange="setActFlag(this)" name="pActType" style="width:95%;"></select>';
	  oActSelect = document.getElementById("pActType");
	  //���ִ������
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "���ִ������";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input type="text" onblur="setWaittime(this)" value="'+obj.waittime+'" id="maxP" name="maxP" style="width:95%;">';
	  //����ʱѡ������
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = '&nbsp;';
	  idTD = idTR.insertCell();
	  idTD.colSpan = "2"
	  idTD.innerHTML = '<input id="isSltTrans" type="checkbox" onclick="setIsSltTrans(this);"> ����ʱѡ������';
	  if (obj.isSltTrans=="1")document.getElementById("isSltTrans").checked=true;
	  //����ʱѡ������
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = '&nbsp;';
	  idTD = idTR.insertCell();
	  idTD.colSpan = "2"
	  idTD.innerHTML = '<input type="checkbox" id="isSameCredit" onclick="setIsSameCredit(this)"> ��������ʹ����ͬ�ֶ�Ȩ��';
	  if (obj.isSameCredit=="1")document.getElementById("isSameCredit").checked=true;
	   //ִ����
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "ִ����";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input id="btnPExcuter" type="button" class="cButton" onclick="vmlOpenWin(\'excuter.htm\',\'\',675,455)" value="...">';
	  idTD.innerHTML += '<span id="txtPExcuter"></span>'
	  showExcuter(obj.ID);
	   //������
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "������";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input id="btnPReader" type="button" class="cButton" onclick="vmlOpenWin(\'excuter.htm\',\'\',675,455)" value="...">';
	  //X
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "X������";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input type="text" rule="^\\d{1,4}p{0,1}x{0,1}$" msg="[ͼ��X����]������1-4���֣�" onblur="if(beforePropChange(this)) setPropX(this);" name="pX" maxlength="10" value="' + obj.X + '" style="width:95%;">';
	  //Y
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "Y������";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input type="text" rule="^\\d{1,4}p{0,1}x{0,1}$" msg="[ͼ��Y����]������1-4���֣�" onblur="if(beforePropChange(this)) setPropY(this);" name="pY" maxlength="10" value="' + obj.Y + '" style="width:95%;">';
	  //Width
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.innerHTML = "ͼ�ο��";
	  idTD.align = "right";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input type="text" rule="^\\d{1,4}p{0,1}x{0,1}$" msg="[ͼ�ο��]������1-4���֣�" onblur="if(beforePropChange(this)) setPropW(this);" name="pWidth" name="pWidth" maxlength="10" value="' + obj.Width + '" style="width:95%;">';
	  //Height
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "ͼ�θ߶�";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input type="text" rule="^\\d{1,4}p{0,1}x{0,1}$" msg="[ͼ�θ߶�]������1-4���֣�" onblur="if(beforePropChange(this)) setPropH(this);" name="pHeight" maxlength="10" value="' + obj.Height + '" style="width:95%;">';
	  //�����״
	  oOption = document.createElement("OPTION");
	  oSelect.options.add(oOption);
	  oOption.innerText = "Բ��";
	  oOption.value = "Oval";
	  oOption = document.createElement("OPTION");
	  oSelect.options.add(oOption);
	  oOption.innerText = "����";
	  oOption.value = "Rect";
	  oOption = document.createElement("OPTION");
	  oSelect.options.add(oOption);
	  oOption.innerText = "Բ����";
	  oOption.value = "RoundRect";
	  oOption = document.createElement("OPTION");
	  oSelect.options.add(oOption);
	  oOption.innerText = "����";
	  oOption.value = "Diamond";
	  oSelect.value = obj.ShapeType;

	  oOption = document.createElement("OPTION");
	  oActSelect.options.add(oOption);
	  oOption.innerText = "��һ���";
	  oOption.value = "1013";
	  oOption = document.createElement("OPTION");
	  oActSelect.options.add(oOption);
	  oOption.innerText = "����Ⱥ��";
	  oOption.value = "0013";
	  oOption = document.createElement("OPTION");
	  oActSelect.options.add(oOption);
	  oOption.innerText = "Э��Ⱥ��";
	  oOption.value = "0014";
	  oOption = document.createElement("OPTION");
	  oActSelect.options.add(oOption);
	  oOption.innerText = "����Ⱥ��";
	  oOption.value = "0015";
	  oOption = document.createElement("OPTION");
	  oActSelect.options.add(oOption);
	  oOption.innerText = "��֧";
	  oOption.value = "0213";
	  oOption = document.createElement("OPTION");
	  oActSelect.options.add(oOption);
	  oOption.innerText = "ͬ��";
	  oOption.value = "0113";
	  oOption = document.createElement("OPTION");
	  oActSelect.options.add(oOption);
	  oOption.innerText = "����";
	  oOption.value = "2013";
	  oActSelect.value = obj.actFlag.substr(0,4);
	  oActSelect.svalue = obj.actFlag.substr(0,4);
  }
  else{//Step
	  //FromProc
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "������";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<select id="pFromProc" onblur="if(beforePropChange(this)) setPropFromProc(this);" name="pFromProc" style="width:95%;"></select>';
	  //ToProc
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "�յ����";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<select id="pToProc" onblur="if(beforePropChange(this)) setPropToProc(this);" name="pShapeType" style="width:95%;"></select>';
	  //Cond
	  var strCond
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = '����';
	  idTD = idTR.insertCell();
	  idTD.vAlign = "top";
	  strCond = obj.Cond;
	  strCond = strCond.replace(/&lt;/g,'<');
	  strCond = strCond.replace(/&gt;/g,'>');
	  idTD.innerHTML = '<input id="btnPCond" type="button" class="cButton" onclick="vmlOpenWin(\'cond.htm\',\'\',675,455)" value="...">';
	  idTD.innerHTML += '<textarea id="pCond" onblur="onTextBlur(this,1,19)" readonly cols="19" onfocus="onTextGetFocus(this,5,19)" rows="1" name="pCond">' + strCond + '</textarea>'
	  //idTD.innerHTML = '<input type="text" name="pCond" maxlength="10" value="' + obj.Cond + '" style="width:95%;">';
	   //�ֶ�Ȩ��
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "�ֶ�Ȩ��";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input type="button" class="cButton" onclick="vmlOpenWin(\'flag.htm\',\'\',675,455)" value="...">';
	   //ǩעȨ��
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "ǩעȨ��";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input id="btnPCredit" type="button" class="cButton" onclick="vmlOpenWin(\'qzCredit.htm\',\'\',675,455)" value="...">';
	   //�¼�
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "�¼�";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input id="btnPEvent" type="button" class="cButton" onclick="vmlOpenWin(\'event.htm\',\'\',675,455)" value="...">';
	   //������
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "������";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<input id="btnPSubFlow" type="button" class="cButton" onclick="vmlOpenWin(\'subflow.htm\',\'\',675,455)" value="...">';
	  //StartArrow
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "��ʼ��ͷ";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<select id="pStartArrow" onblur="if(beforePropChange(this)) setPropStartArrow(this);" name="pStartArrow" style="width:95%;"><option value="none" selected>��</option><option value="Classic">����</option><option value="Block">ʵ��</option><option value="Diamond">��ʯ</option><option value="Oval">Բ��</option><option value="Open">С��ͷ</option></select>';
	  if(obj.StartArrow != "") document.getElementById("pStartArrow").value = obj.StartArrow;
	  //EndArrow
	  idTR = propview.insertRow();
	  idTR.height = "25";
	  idTR.className="obj";
	  idTD = idTR.insertCell();
	  idTD.noWrap = true;
	  idTD.style.width = "79";
	  idTD.align = "right";
	  idTD.innerHTML = "��ֹ��ͷ";
	  idTD = idTR.insertCell();
	  idTD.innerHTML = '<select id="pEndArrow" onblur="if(beforePropChange(this)) setPropEndArrow(this);" name="pEndArrow" style="width:95%;"><option value="none" selected>��</option><option value="Classic">����</option><option value="Block">ʵ��</option><option value="Diamond">��ʯ</option><option value="Oval">Բ��</option><option value="Open">С��ͷ</option></select>';
	  if(obj.EndArrow != "") document.getElementById("pEndArrow").value = obj.EndArrow;
	  oOption = document.createElement("OPTION");
	  oSelect.options.add(oOption);
	  oOption.innerText = "ֱ��";
	  oOption.value = "Line";
	  oOption = document.createElement("OPTION");
	  oSelect.options.add(oOption);
	  oOption.innerText = "�۽���";
	  oOption.value = "PolyLine";
	  oSelect.value = obj.ShapeType;
	  var Proc,oSelect2;
	  oSelect = document.all("pFromProc");
	  oSelect1 = document.all("pToProc");
	  for(var i = 0; i< _FLOW.Procs.length; i ++){
		Proc = _FLOW.Procs[i];
		if(Proc.ProcType != "EndProc"){
		  var Option1 = document.createElement("OPTION");
			oSelect.options.add(Option1);
		  Option1.value = Proc.ID;
		  Option1.text = Proc.Text;
		}
		oSelect.value = obj.FromProc;
		if(Proc.ProcType != "BeginProc"){
		  var Option2 = document.createElement("OPTION");
		  oSelect1.options.add(Option2);
		  Option2.value = Proc.ID;
		  Option2.text = Proc.Text;
		}
		oSelect1.value = obj.ToProc;
	  }

  }
  //StrockWeight
  idTR = propview.insertRow();
  idTR.height = "25";
  idTR.className="obj";
  idTD = idTR.insertCell();
  idTD.noWrap = true;
  idTD.style.width = "79";
  idTD.align = "right";
  idTD.innerHTML = "������ϸ";
  idTD = idTR.insertCell();
  idTD.innerHTML = '<input type="text" rule="^\\d{1}$" msg="[�߿��ϸ]������1λ���֣�" onblur="if(beforePropChange(this)) setPropStrokeWeight(this);" name="pStrokeWeight" maxlength="10" value="' + obj.StrokeWeight + '" style="width:95%;">';
  //zIndex
  idTR = propview.insertRow();
  idTR.height = "25";
  idTR.className="obj";
  idTD = idTR.insertCell();
  idTD.noWrap = true;
  idTD.style.width = "79";
  idTD.align = "right";
  idTD.innerHTML = "Z��";
  idTD = idTR.insertCell();
  idTD.innerHTML = '<input type="text" rule="^\\d{1,3}$" msg="[Z��]������1-3λ���֣�" onblur="if(beforePropChange(this)) setPropzIndex(this);" name="pzIndex" maxlength="10" value="' + obj.zIndex + '" style="width:95%;">';
  
  //����
  if(_FOCUSTEDOBJ.typ == "Proc"){
	  if (_FOCUSTEDOBJ.id=="begin")
	  {
		  pName.readOnly = true;
		  pActType.disabled = true;
		  pShapeType.disabled = true;
		  maxP.disabled = true;
		  isSltTrans.disabled = true;
		  isSameCredit.disabled = true;
		  btnPExcuter.disabled = true;
		  btnPReader.disabled = true;
	  }
  }
  else
  {
	  if (_FOCUSTEDOBJ.id.substr(0,1)=="D")
	  {
		  pName.readOnly = true;
		  pFromProc.disabled = true;
		  pToProc.disabled = true;
		  btnPCond.disabled = true;
		  btnPCredit.disabled = true;
		  btnPEvent.disabled = true;
		  btnPSubFlow.disabled = true;
	  }
  }
}

//CACHE
function emptyLog(){
  _DOLOG = [];
  _DOLOGINDEX = -1;
}

function pushLog(act, obj){
  var newLog = _DOLOG.slice(0, _DOLOGINDEX + 1);
  _DOLOG = newLog;
  _DOLOGINDEX = _DOLOG.push({"act" : act, "val": obj}) - 1;
}

function getLog(){
  return _DOLOG[_DOLOGINDEX];
}


function undoLog(){
  if(_DOLOGINDEX == -1){
    alert("û�в�����¼���Գ���.");
    return;
  }
  if(doLog("undo")) _DOLOGINDEX --;
}

function redoLog(){
  if(_DOLOGINDEX == _DOLOG.length - 1){
    alert("û�в�����¼���Իָ�.");
    return;
  }
  _DOLOGINDEX ++;
  doLog("redo");
}

function doLog(act){
  var log = getLog();
/*TODO �Ƿ�Ҫ��������
if(!confirm("ȷ��Ҫ*"+(act == "undo"?"����":"�ָ�")+"*���һ�β���[" + log.act + "]��")){
    if(act == "redo") _DOLOGINDEX --;
    return false;
  }*/
  switch(log.act){
    case "addproc":
      act == "undo"?_FLOW.deleteProcByID(log.val.ID):_FLOW.addProc(log.val);
      DrawAll();
      break;
    case "addstep":
      act == "undo"?_FLOW.deleteStepByID(log.val.ID):_FLOW.addStep(log.val);
      DrawAll();
      break;
    case "delproc":
      act == "undo"?_FLOW.addProc(log.val):_FLOW.deleteProcByID(log.val.ID);
      DrawAll();
      break;
    case "delstep":
      act == "undo"?_FLOW.addStep(log.val):_FLOW.deleteStepByID(log.val.ID);
      DrawAll();
      break;
    case "editproc":
      if(act == "undo"){
        var Proc = _FLOW.getProcByID(log.val._new.ID);
        Proc.clone(log.val._old);
        if(log.val._new.ID != log.val._old.ID) changeProcID(log.val._new.ID, log.val._old.ID);
      }
      else{
        var Proc = _FLOW.getProcByID(log.val._old.ID);
        Proc.clone(log.val._new);
        if(log.val._new.ID != log.val._old.ID) changeProcID(log.val._old.ID, log.val._new.ID);
      }
      DrawAll();
      objFocusedOn(act == "undo"?log.val._old.ID:log.val._new.ID);
      break;
    case "editstep":
      if(act == "undo"){
        var Step = _FLOW.getStepByID(log.val._new.ID);
        Step.clone(log.val._old);
      }
      else{
        var Step = _FLOW.getStepByID(log.val._old.ID);
        Step.clone(log.val._new);
      }
      DrawVML();
      objFocusedOn(act == "undo"?log.val._old.ID:log.val._new.ID);
      break;
    case "moveproc":
      var obj = _FLOW.getProcByID(log.val.objID);
      if(act == "undo"){
        obj.setPropValue("X", log.val._old.X);
        obj.setPropValue("Y", log.val._old.Y);
        obj.setPropValue("Width", log.val._old.Width);
        obj.setPropValue("Height", log.val._old.Height);
      }
      else{
        obj.setPropValue("X", log.val._new.X);
        obj.setPropValue("Y", log.val._new.Y);
        obj.setPropValue("Width", log.val._new.Width);
        obj.setPropValue("Height", log.val._new.Height);
      }
	  _logMoveType = log.val.moveType;
      changeProcPos(obj.InnerObject);
      break;
    case "editprop":
      var CurrentProp = (act == "undo"?log.val._old:log.val._new);
      log.val.obj[log.val.prop] = CurrentProp;
      switch(log.val.prop){
        case "ID":
          if(log.val.obj.ObjType == "Proc") act == "undo"?changeProcID(log.val._new, log.val._old):changeProcID(log.val._old, log.val._new);
          DrawVML();
          objFocusedOn(log.val.obj.ID);
          break;
        case "Text":
          DrawAll();
          objFocusedOn(log.val.obj.ID);
          break;
        case "ShapeType":
          DrawVML();
          objFocusedOn(log.val.obj.ID);
          break;
        case "TextWeight":
          document.all(log.val.obj.ID + "Text").style.fontSize = CurrentProp;
          break;
        case "zIndex":
          log.val.obj.InnerObject.style.zIndex = CurrentProp;
          break;
        case "StrokeWeight":
          log.val.obj.InnerObject.strokeweight = CurrentProp;
          break;
        case "X":
          log.val.obj.InnerObject.style.left = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Y":
          log.val.obj.InnerObject.style.top = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Width":
          log.val.obj.InnerObject.style.width = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Height":
          log.val.obj.InnerObject.style.height = CurrentProp;
          changeProcPos(log.val.obj.InnerObject);
          break;
        case "Cond":
          document.all(log.val.obj.ID + "Text").innerHTML = CurrentProp;
          break;
        case "StartArrow":
          document.all(log.val.obj.ID + "Arrow").startarrow = CurrentProp;
          break;
        case "EndArrow":
          document.all(log.val.obj.ID + "Arrow").endarrow = CurrentProp;
          break;
        case "FromProc":
        case "ToProc":
          if(log.val.obj.ShapeType == "Line"){
            log.val.obj.getPath();
            log.val.obj.InnerObject.from = log.val.obj.FromPoint;
            log.val.obj.InnerObject.to = log.val.obj.ToPoint;
          }
          else if(log.val.ShapeType == "PolyLine"){
            log.val.obj.InnerObject.points.value = log.val.obj.reGetPath();
          }
          //DrawDataView();
          break;
      }      
      break;
  }
  stuffProp();
  return true;
}

function doProcMouseDown(obj, x, y){
  //�ж��Ƿ��ǻ���
  if(_TOOLTYPE == "line" || _TOOLTYPE == "polyline"){
    _CURRENTX = x;
    _CURRENTY = y;
    _MOVEOBJ = document.all("_lineui");
    //_MOVEOBJ.from = _CURRENTX + "," + _CURRENTY;
	_MOVEOBJ.from = _CURRENTX + "," + (_CURRENTY - 0);//ԭ����
    _MOVEOBJ.to = _MOVEOBJ.from;
    _MOVEOBJ.style.display = "block";
    _MOVETYPE = _TOOLTYPE;
  }
  else{
    var rightSide = (parseInt(obj.style.left) + parseInt(obj.style.width) -x <= 2);
    var bottomSide = (parseInt(obj.style.top) + parseInt(obj.style.height) - y <= 2);
    if(rightSide && bottomSide)
      _MOVETYPE = "proc_nw";
    else if(rightSide)
      _MOVETYPE = "proc_e";
    else if(bottomSide)
      _MOVETYPE = "proc_n";
    else{
      _MOVETYPE = "proc_m";
      _CURRENTX = x - obj.offsetLeft;
      _CURRENTY = y - obj.offsetTop;
    }
    _MOVEOBJ = obj;
  }
  window.event.cancelBubble = true;
}

// �����x,y�ǻ������ֵ(/ZOOM)
function fireProcMouseDown(x, y){
  var curProc = null;
  for(var i = 0; i< _FLOW.Procs.length; i ++){
    Proc = _FLOW.Procs[i];
    if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width))
        && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
	  if (curProc == null || Proc.zIndex >= curProc.zIndex) // �ص��������ȡ�����Ǹ�
	    curProc = Proc;
    }
  }

  if (curProc != null) {
    obj = document.getElementById(curProc.ID);
    objFocusedOn(obj.id);
    doProcMouseDown(obj, x, y);
    return true;
  }
  return false;
}

// �����x,y�ǻ������ֵ(/ZOOM)
function doProcMouseMove(obj, x, y){
  if(_TOOLTYPE == "line" || _TOOLTYPE == "polyline")
   
  document.all.Canvas.style.cursor = "crosshair";
  else{
    var rightSide = (parseInt(obj.style.left) + parseInt(obj.style.width) -x <= 2);
    var bottomSide = (parseInt(obj.style.top) + parseInt(obj.style.height) - y <= 2);
    if(rightSide && bottomSide)
      document.all.Canvas.style.cursor = "NW-resize";
    else if(rightSide)
      document.all.Canvas.style.cursor = "E-resize";
    else if(bottomSide)
      document.all.Canvas.style.cursor = "N-resize";
    else
      document.all.Canvas.style.cursor = "hand";
  }
}

// �����x,y�ǻ������ֵ(/ZOOM)
function fireProcMouseMove(x, y){
  if (document.all.Canvas == null) return;
  for(var i = 0; i< _FLOW.Procs.length; i ++){
    Proc = _FLOW.Procs[i];
	  obj = document.getElementById(Proc.ID);
    if (x >= parseInt(Proc.X) && x <= (parseInt(Proc.X) + parseInt(Proc.Width))
      && y >= parseInt(Proc.Y) && y <= (parseInt(Proc.Y) + parseInt(Proc.Height))) {
      doProcMouseMove(obj, x, y);	
	  return true;
    }
  }
  if (i >= _FLOW.Procs.length){
    document.all.Canvas.style.cursor = (_TOOLTYPE == "point"?"default":"crosshair");
  }
  return false;
}

function doDocMouseDown(){
  var x = (event.x + document.body.scrollLeft) / _ZOOM;
  var y = (event.y + document.body.scrollTop) / _ZOOM;
  var oEvt = event.srcElement;
  if (oEvt.id == "tableContainer" || oEvt.id == "") return; 	// ����������ͼ/������ͼ�ϵ��¼�
  if (oEvt.typ=="Step")
  {
	  document.all.Canvas.style.cursor = "default";
	  return;
  }
  if (fireProcMouseDown(x, y)) return;		// ����ͼԪ�ϵ��¼�
  switch(_TOOLTYPE){
    case "rect":
    case "roundrect":
    case "diamond":
    case "oval":
	case "fillrect":
      if(oEvt.tagName != "DIV") return;
      if(oEvt.id != "Canvas") return;
      var obj = document.all("_" + _TOOLTYPE + "ui");
      _CURRENTX = x;
      _CURRENTY = y;
      obj.style.left = _CURRENTX;
      obj.style.top = _CURRENTY;
      obj.style.width  = 0;
      obj.style.height = 0;
      obj.style.display = "block"
      _MOVETYPE = _TOOLTYPE;
      break;
  }
}

function doDocMouseMove(){
var x = (event.x + document.body.scrollLeft) / _ZOOM;
var y = (event.y + document.body.scrollTop) / _ZOOM;
var m,n,aryPt,_movePt,sqrta,_moveLine
  switch(_MOVETYPE){
    case "line":	
    case "polyline":
		//_MOVEOBJ.to = x + "," + y;
	    _MOVEOBJ.to = x + "," + (y - 0);//ԭ����
      break;
	case "line_m":
		var zx=x*_ZOOM
		var zy=y*_ZOOM
		if (oOval==null)createOval(zx,zy);
		if (_PointOrLine==0)
		{
			oOval.fillcolor = "blue";
			oOval.strokecolor = "blue";
		}
		m = _clkPx.substr(0,_clkPx.length-2)*4/3;
		n = _clkPy.substr(0,_clkPy.length-2)*4/3;
		sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
	    var _arySltLine = _strSltLine.split(',');
		if(sqrta>10)
	    {
			oOval.style.left = zx-3;
			oOval.style.top = zy-3;
			if(_PointOrLine==0){
			  _movePt = (x*3/4)+'pt,'+(y*3/4)+"pt";
			  _MOVEOBJ.Points = _strPt1+""+_movePt+""+_strPt2;
			}
			else{
			  _moveLine = _arySltLine[0]+','+_arySltLine[1]+','+x*3/4+'pt,'+y*3/4+'pt,'+_arySltLine[2]+','+_arySltLine[3];
			  _MOVEOBJ.Points = _strLine1+_moveLine+_strLine2;
			}
			document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
			_FLOW.Modified = true;
		}
	  break;
    case "proc_m":
		m = _clkPx.substr(0,_clkPx.length-2)*4/3;
		n = _clkPy.substr(0,_clkPy.length-2)*4/3;
		sqrta = Math.sqrt((x-m)*(x-m)+(y-n)*(y-n));
		if(sqrta>10)
	    {
			var newX = x - _CURRENTX;
			var newY = y - _CURRENTY;
			if(newX < 0) newX = 0;
			if(newY < 30) newY = 30;
			_MOVEOBJ.style.left = newX + 2; //������������
			_MOVEOBJ.style.top = newY + 2;
			changeProcPos(_MOVEOBJ);
		}
		break;
    case "proc_n":
      var newH = y - parseInt(_MOVEOBJ.style.top);
      if(newH < 30) newH = 30;
      _MOVEOBJ.style.height = newH;
      changeProcPos(_MOVEOBJ);
      break;
    case "proc_e":
      var newW = x - parseInt(_MOVEOBJ.style.left);
      if(newW < 30) newW = 30;
      _MOVEOBJ.style.width = newW;
      changeProcPos(_MOVEOBJ);
      break;
    case "proc_nw":
      var newW = x - parseInt(_MOVEOBJ.style.left);
      var newH = y - parseInt(_MOVEOBJ.style.top);
      if(newW < 30) newW = 30;
      if(newH < 30) newH = 30;
      _MOVEOBJ.style.width = newW;
      _MOVEOBJ.style.height = newH;
      changeProcPos(_MOVEOBJ);
      break;
    case "rect":
    case "roundrect":
    case "diamond":
    case "oval":
	case "fillrect":
      var newX = x;
      var newY = y;
      var obj = document.all("_" + _MOVETYPE + "ui");
      if(newX < _CURRENTX) obj.style.left = newX;
      obj.style.width = Math.abs(newX - _CURRENTX);
      if(newY < _CURRENTY) obj.style.top = newY;
      obj.style.height = Math.abs(newY - _CURRENTY);
      break;
    default: // �������ƶ�״̬�£�������ƶ���Ϣ����ͼԪ
      fireProcMouseMove(x, y); 
	 
  }
}
//��ȡ��ǰ�㵽��ǰ�����8����������ĵ�
function getNearPt(oProc,x,y)
{
	var objProc = document.getElementById(oProc.ID)
	var fromW = parseInt(objProc.style.width);
	var fromH = parseInt(objProc.style.height);
	var fromX = parseInt(objProc.style.left);
	var fromY = parseInt(objProc.style.top);
	var arrX = new Array();
	var arrY = new Array();
	var arrPos = new Array(0,0.25,0.5,0.75,1);
	var nX, nY, m,n , nearPt,poX,poY;
	arrX[0] = fromX;
	arrX[1] = fromX + fromW / 4;
	arrX[2] = fromX + fromW / 2;
	arrX[3] = fromX + fromW * 3 / 4;
	arrX[4] = fromX + fromW;

	arrY[0] = fromY;
	arrY[1] = fromY + fromH / 4;
	arrY[2] = fromY + fromH / 2;
	arrY[3] = fromY + fromH * 3 / 4;
	arrY[4] = fromY + fromH;
	m=0;
	n=0;
	for (var i=0; i<4; i++)
	{
		Math.abs(x-arrX[i]) < Math.abs(x-arrX[i+1]) ? m=m : m=i+1;
		Math.abs(y-arrY[i]) < Math.abs(y-arrY[i+1]) ? n=n : n=i+1;
	}
	if (m>0 && m<4 && n>0 && n<4)
	{
		if (m==3)
			m=4;
		else 
			m=0;
		if (n==3)
			n=4;
		else 
			n=0;
	}

	nX = arrX[m];
	nY = arrY[n];
	poX = arrPos[m];
	poY = arrPos[n];
	nearPt = nX*3/4+"pt,"+nY*3/4+"pt|~|"+poX+","+poY
	//nearPt = nX + "," + nY;
	return nearPt;
}
function doDocMouseUp(){
  var x = (event.x + document.body.scrollLeft) / _ZOOM;
  var y = (event.y + document.body.scrollTop) / _ZOOM;
  switch(_MOVETYPE){
	 case "line_m":
		 document.body.removeChild(oOval);
		 oOval = null;
		//var Step = _FLOW.getStepByID(_MOVEOBJ.ID);
		var ProcTo
		ProcTo = _FLOW.getProcAtXY(x, y);
		if (ProcTo==null)
		{
			if (ptMoveType=="from" || ptMoveType=="to")
			{
				_MOVEOBJ.Points = _strPt1+_strSltPt+_strPt2;
				document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
			}
		}
		else
	    {
			if (ptMoveType=="from" || ptMoveType=="to")
			{
				var Proc1,Proc2;
				Proc1 = _FLOW.getProcAtXY(_CURRENTX, _CURRENTY);
				Proc2 = _FLOW.getProcAtXY(x, y);
				
				var nearPt = getNearPt(ProcTo,x,y);
				var strPt = nearPt.split("|~|")[0];
				var arrPt = strPt.split(",");
				var strPos = nearPt.split("|~|")[1];
				var nX = arrPt[0].substr(0,arrPt[0].length-2);
				var nY = arrPt[1].substr(0,arrPt[1].length-2);
				var relX = strPos.split(",")[0];
				var relY = strPos.split(",")[1];
				if (ptMoveType=="from")
				{
					Proc1 = ProcTo;
					Proc2 = _FLOW.getProcByID(_MOVEOBJ.ToProc);
				}
				if (ptMoveType=="to")
				{
					Proc1 = _FLOW.getProcByID(_MOVEOBJ.FromProc);
					Proc2 = ProcTo;
				}
				var existProc = _FLOW.StepPathExists(Proc1.ID, Proc2.ID)
				if((Proc1.ID == Proc2.ID)||(existProc!=null && existProc.ID != _MOVEOBJ.ID)
					||(Proc1.ProcType == "EndProc" || Proc2.ProcType == "BeginProc")
					||(_MOVEOBJ.FromProc=="begin" && ptMoveType=="from" && ProcTo.ID != _MOVEOBJ.FromProc)
					||(_MOVEOBJ.FromProc=="begin" && ptMoveType=="to" && ProcTo.ID != _MOVEOBJ.ToProc)
					||(_MOVEOBJ.FromProc!="begin" && ProcTo.ID=="begin")){
					if(existProc!=null && existProc.ID != _MOVEOBJ.ID)
						alert("�Ѿ���һ��·����[" + _FLOW.getProcByID(Proc1.ID).Text + "]��[" + _FLOW.getProcByID(Proc2.ID).Text + "]���������ɹ�");
					if(Proc1.ProcType == "EndProc" || Proc2.ProcType == "BeginProc")
						alert("·��������ϡ���㲻���ǽ�����㣬�յ㲻���ǿ�ʼ��㡱�Ĺ���");
					if(Proc1.ID == Proc2.ID)
						alert("����ָ����");
					//FromProc�ǿ�ʼ�ڵ������
					//if (ptMoveType=="from"&&ProcTo.ID!=_MOVEOBJ.FromProc)alert(_MOVEOBJ.FromProc);
					//if (ptMoveType=="to"&&ProcTo.ID!=_MOVEOBJ.ToProc)alert(_MOVEOBJ.FromProc);
					_MOVEOBJ.Points = _strPt1+_strSltPt+_strPt2;
					document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
				}
				else
				{
					if (ptMoveType=="from")
					{
						_MOVEOBJ.setPropValue("FromProc",ProcTo.ID);
						_MOVEOBJ.fromRelX = relX;
						_MOVEOBJ.fromRelY = relY;
					}
					if (ptMoveType=="to")
					{
						_MOVEOBJ.setPropValue("ToProc",ProcTo.ID);
						_MOVEOBJ.toRelX = relX;
						_MOVEOBJ.toRelY = relY;
					}
					_MOVEOBJ.Points = _strPt1+strPt+_strPt2;
					document.getElementById(_MOVEOBJ.ID).outerHTML=_MOVEOBJ.toString();
					stuffProp();
					_FLOW.Modified = true;
					var newobj = new TStep(_FLOW);
					newobj.clone(_MOVEOBJ)
					pushLog("editstep", {"_old":_MOVELINEOBJ,"_new":newobj});
				}
			}
			
		}
		updateFlow(document.getElementById(_MOVEOBJ.ID));
		break;
    case "proc_m":
    case "proc_n":
    case "proc_e":
    case "proc_nw":
      var Proc = _FLOW.getProcByID(_MOVEOBJ.id);
      var oldVal = {"X":Proc.X,"Y":Proc.Y,"Width":Proc.Width,"Height":Proc.Height};
      if(_MOVETYPE == "proc_m"){
        Proc.setPropValue("X",_MOVEOBJ.style.left);
        Proc.setPropValue("Y",_MOVEOBJ.style.top);
      }
      else{
        Proc.setPropValue("Width",_MOVEOBJ.style.width);
        Proc.setPropValue("Height",_MOVEOBJ.style.height);
      }
      //���ֻ��ϸС�ĵ��ڣ�����¼��ʷ����
      if(Math.abs(parseInt(oldVal.X) - parseInt(Proc.X)) > 2 || Math.abs(parseInt(oldVal.Y) - parseInt(Proc.Y)) > 2
         || Math.abs(parseInt(oldVal.Width) - parseInt(Proc.Width)) > 2 || Math.abs(parseInt(oldVal.Height) - parseInt(Proc.Height)) > 2){
        pushLog("moveproc", {"objID":Proc.ID,"moveType":_MOVETYPE,"_old": oldVal, "_new":{"X":Proc.X,"Y":Proc.Y,"Width":Proc.Width,"Height":Proc.Height}});
      }
      stuffProp();
      _FLOW.Modified = true;
	  updateFlow(_MOVEOBJ);
	  saveStepsToProc(_MOVEOBJ);
      break;
    case "rect":
    case "roundrect":
    case "diamond":
    case "oval":
	case "fillrect":

    case "line":
    case "polyline":
      var obj = document.all("_" + (_MOVETYPE == "polyline"?"line":_MOVETYPE) + "ui");
      obj.style.display = "none";
      if(_MOVETYPE == "line" || _MOVETYPE == "polyline"){

		var Proc1,Proc2, Step;
        Proc1 = _FLOW.getProcAtXY(_CURRENTX, _CURRENTY);
        Proc2 = _FLOW.getProcAtXY(x, y);
		if(Proc1 == null || Proc2 == null){
          alert("�����յ㲻��[����]������[����]ͼ���ϰ�ס��겢�϶���ĳ[����]ͼ�����ɿ�.");
          break;;
        }
        if(_FLOW.StepPathExists(Proc1.ID, Proc2.ID)!=null){
          alert("�Ѿ���һ��·����[" + _FLOW.getProcByID(Proc1.ID).Text + "]��[" + _FLOW.getProcByID(Proc2.ID).Text + "]������ģ�");
          break;
        }
        if(Proc1.ID == Proc2.ID){
          alert("����ָ����");
          break;
        }
		if(Proc1.ProcType == "BeginProc")
		{
			alert("��ʼ�ڵ㲻�����ֶ�����!");
			break;
		}
        if(Proc1.ProcType == "EndProc" || Proc2.ProcType == "BeginProc"){
          alert("·��������ϡ���㲻���ǽ�����㣬�յ㲻���ǿ�ʼ��㡱�Ĺ���");
          break;
        }
        Step = new TStep(_FLOW);
        Step.FromProc = Proc1.ID;
        Step.ToProc = Proc2.ID;
		//Step.Cond = Step.Text
		Step.zIndex = 2;
        Step.ShapeType = "PolyLine";

        _FLOW.addStep(Step);
        pushLog("addstep", Step);
        DrawAll();
		
      }
      else{
        var Proc = new TProc(_FLOW);
        //Proc.ShapeType = (_MOVETYPE == "rect"?"Rect":(_MOVETYPE == "roundrect"?"RoundRect":(_MOVETYPE == "oval"?"Oval":"Diamond")));
        Proc.ShapeType = (_MOVETYPE == "rect"?"Rect":(_MOVETYPE == "roundrect"?"RoundRect":(_MOVETYPE=="fillrect"?"FillRect":(_MOVETYPE == "oval"?"Oval":"Diamond"))));
		Proc.X = parseInt(obj.style.left);
        Proc.Y = parseInt(obj.style.top);
        Proc.Width = parseInt(obj.style.width);
        Proc.Height = parseInt(obj.style.height);
        if(Proc.Width < 20 || Proc.Height < 20){
          //alert("ͼ�γ���С����Сֵ��������Ĭ�ϴ�С");
          Proc.Width = 100;
          Proc.Height = 40;
        }
		_FLOW.addProc(Proc);
        pushLog("addproc", Proc);
        DrawAll();
      }
      break;
  }
  _MOVETYPE = "";
  _MOVELINEOBJ = null;
  _MOVEOBJ = null;
  return true;
}
function doDocSelectStart(){
  var oEvt = event.srcElement.tagName;
  if(oEvt != "INPUT") return false;
}

function doDocKeyDown(){
  switch(event.keyCode){
    case 46: //Del
      if(_FOCUSTEDOBJ != null && event.srcElement.tagName != "INPUT" && event.srcElement.tagName != "SELECT")
        mnuDelObj();
      break;
   /* case 90: //z
      if(event.ctrlKey && event.shiftKey)
        redoLog();
      else if(event.ctrlKey)
        undoLog();
      break;
    case 89: //y
      if(event.ctrlKey) redoLog();
      break;
    case 112: //F1
      mnuAbout();
      break;
	case 187: //+
      if(event.ctrlKey) mnuSetZoom('in');
      break;
	case 189: //-
      if(event.ctrlKey) mnuSetZoom('out');
      break;*/
	case 83: //s
      if(event.ctrlKey) mnuSaveFlow();
      break;
	/*case 79: //o
      if(event.ctrlKey) mnuOpenFlow();
      break;
	case 78: //n
      if(event.ctrlKey) mnuNewFlow();
      break;
	case 67: //C
      if(event.ctrlKey) mnuCopyProc();
      break;*/
	case 13: //enter
      document.all.Canvas.focus();
      break;
  }
}
function showExcuter(id)
{

}
document.onselectstart = doDocSelectStart;
document.onmousedown = doDocMouseDown;
document.onmousemove = doDocMouseMove;
document.onmouseup = doDocMouseUp;
document.onkeydown = doDocKeyDown;
document.onerror = new Function("return false;"); 
