<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>500 - 系统内部错误</title>
<%@include file="/WEB-INF/views/include/head.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header">
			<h1>系统内部错误.</h1>
		</div>
		<div>
			<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a>
		</div>
	</div>
</body>
</html>