<%@page language="java" contentType="text/html;charset=GBK"%>
<%
	String rootPath = request.getContextPath();
	String c = request.getParameter("c");
%>
<!-- ����----��½���� -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>IT��ά���ϵͳ</title>
<meta http-equiv=Content-Type content="text/html; charset=GBK" />
<meta http-equiv="X-UA-Compatible" content="IE=7" />
<LINK href="resource/css/login.css" type="text/css" rel=stylesheet>	
<meta content="MSHTML 6.00.2800.1106" name=GENERATOR />
<style type="text/css">
html,body {
	background: #1e4765 url(<%=rootPath%>/resource/image/login_bg.jpg)
		no-repeat center 100px;
	font-family: Arial, Helvetica, sans-serif, "����";
	font-size: 12px;
}

.box {
	width: 500px;
	height: 95px;
	margin: 0 auto;
	margin-top: 330px;
}

.sr {
	width: 170px;
	background: none;
	color: #2a6a98;
	border: 0;
	padding: 3px 0;
}

.user {
	margin-left: 40px;
}

.password {
	margin-left: 100px;
}

.txt {
	line-height: 35px;
	color: #f2f2f2;
	padding: 20px 7px;
}

.nav {
	float: right;
	cursor: pointer;
	width: 113px;
	height: 35px;
	background: url(<%=rootPath%>/resource/image/login_nav.jpg) no-repeat;
	border: 0;
}
</style>
<script language="javascript">
	function doLogin() {
		if (mainForm.userid.value == "") {
			alert("�������û���!");
			mainForm.userid.focus();
			return false;
		} else if (mainForm.password.value == "") {
			alert("����������!");
			mainForm.password.focus();
			return false;
		}
	}
</script>

</head>

<body>
	<div class="box">
		<form method="post" name="mainForm"
			action="<%=rootPath%>/user.do?action=login"
			onsubmit="return doLogin();">
			<input name="userid" id="userid" type="text" class="sr user" /> <input
				name="password" id="password" type="password" class="sr password" />

			<div class="txt">
				<input name="Submit" type="submit" class="nav" value="" />����֧�� |
				��������ɷݹ�˾
			</div>
		</form>
	</div>
</body>
</html>
