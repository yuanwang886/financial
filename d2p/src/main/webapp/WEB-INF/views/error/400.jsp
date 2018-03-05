<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>400 - 请求出错</title>
<%@include file="/WEB-INF/views/include/head.jsp"%>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header">
			<h1>参数有误,服务器无法解析.</h1>
		</div>

		<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a>
		&nbsp; <br /> <br />
	</div>
</body>
</html>