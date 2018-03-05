<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>登录失效</title>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<script>
   function index() {
	   window.location.href = "${ctx}/index";
   }
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header">
			<h4>您的登录状态已经失效，请重新登录</h4>
		</div>
		<div>
			<a href="javascript:" onclick="index();" class="btn">返回登录</a>
		</div>
	</div>
</body>
</html>