<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>"/>
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
</head>
<script type="text/javascript">
	$(function (){
		if (window.top!=window){
			window.top.location = window.location;
		}
		/*在每次一面加载时，清空页面中的用户名和密码*/
		$("#loginAct").val("");
		$("#loginPwd").val("");
		/*在页面加载完成后，让用户文本框获取焦点*/
		$("#loginAct").focus();

		$("#submit").click(function (){
				login()

		})
		$(window).keydown(function (even){
			if (even.keyCode == 13){
				login()
			}
		})
	})
	function login(){
		/*进行登录验证，当用户名和密码不为空时（清除空格后）验证通过*/
		var act = $.trim($("#loginAct").val());
		var pwd = $.trim($("#loginPwd").val());
		if (act.length == 0 || pwd.length == 0){
			$("#msg").html("用户名和密码不能为空");
			return false;
		}
		$.ajax(
				{
					url:"settings/user/login.do",
			 		data:{
						loginAct:act,
						loginPwd:pwd
					},
					dataType:"json",
					type:"post",
					success:function (result){
						if (result.success){
							window.location.href="workbench/index.jsp";
						}else{
							$("#msg").html(result.msg);
						}
					}

				}
		)
	}
</script>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span  id="msg" style="color: red" >

							</span>
						
					</div>
					<button type="button" id="submit" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>