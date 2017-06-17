<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<title>${fns:getConfig('productName')}-登录</title>
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="#"><b>${fns:getConfig('productName')}</b></a>
		</div>
		<!-- /.login-logo -->
		<div class="login-box-body">
			<p class="login-box-msg">
				<strong>用户登录</strong>
			</p>
			<form action="${ctx}/sys/login" method="post" id="login-form">
				<div class="form-group has-feedback">
					<input type="text" class="form-control" name="phone" placeholder="请输入手机号"> <span class="glyphicon glyphicon-phone form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" class="form-control" name="password" placeholder="请输入密码"> <span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<div class="input-group ">
						<input type="text" class="form-control" name="validateCode" id="validateCode" placeholder="请输入验证码"><span class="input-group-btn"><img border="0" style="cursor: pointer" alt="点击刷新验证码" onclick="showImage()" id="validateImg" src="${ctx}/verifyCode"></span>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-6">
						<div class="checkbox icheck">
							<label> <input type="checkbox" name="rememberMe"> 下次自动登录 </label>
						</div>
					</div>
					<!-- /.col -->
					<div class="col-xs-6">
						<div class="checkbox pull-right">
							<a href="${ctx}/forgot">忘记密码</a> <span>&nbsp;/&nbsp;</span> <a href="${ctx}/register" class="text-center">注册</a>
						</div>
					</div>
					<!-- /.col -->
				</div>
				<div class="row">
					<div class="col-xs-12">
						<button type="submit" class="btn btn-info btn-block btn-flat">登 录</button>
					</div>
				</div>
				<input type="hidden" id="message" value="${message}" class="form-control" name="message" />
			</form>
		</div>
	</div>

	<script>
		$(function() {
			$('input').iCheck({
				checkboxClass : 'icheckbox_square-red',
				radioClass : 'iradio_square-red',
				increaseArea : '20%' // optional
			});

			fillbackLoginForm();
			$("#login-form").bootstrapValidator({
				message : '请输入用户名/密码',
				submitHandler : function(valiadtor, loginForm, submitButton) {
					rememberMe($("input[name='rememberMe']").is(":checked"));
					valiadtor.defaultSubmit();
				},
				fields : {
					phone : {
						validators : {
							notEmpty : {
								message : '登录用户名不能为空'
							}
						}
					},
					password : {
						validators : {
							notEmpty : {
								message : '密码不能为空'
							}
						}
					},
					validateCode : {
						validators : {
							notEmpty : {
								message : '验证码不能为空'
							}
						}
					}
				}
			});
		});

		//使用本地缓存记住用户名密码
		function rememberMe(rm_flag) {
			//remember me
			if (rm_flag) {
				localStorage.userName = $("input[name='phone']").val();
				localStorage.password = $("input[name='password']").val();
				localStorage.rememberMe = 1;
			}
			//delete remember msg
			else {
				localStorage.userName = null;
				localStorage.password = null;
				localStorage.rememberMe = 0;
			}
		}

		//记住回填
		function fillbackLoginForm() {
			if (localStorage.rememberMe && localStorage.rememberMe == "1") {
				$("input[name='phone']").val(localStorage.userName);
				$("input[name='password']").val(localStorage.password);
				$("input[name='rememberMe']").iCheck('check');
				$("input[name='rememberMe']").iCheck('update');
			}
		}

		function showImage() {
			$('#validateImg').attr('src', '${ctx}/verifyCode?verifyKey=' + Math.random());
			$('#validateCode').val('');
		}

		var msg = $('#message').val();
		if (msg != null && msg != "") {
			$.alert({
				title : '提示',
				content : msg,
				animation : 'zoom',
				closeAnimation : 'zoom',
				backgroundDismiss: true,
				buttons : {
					okay : {
						text : '确定',
						btnClass : 'btn-blue'
					}
				}
			});
			$('#message').val('');
		}
	</script>
</body>
</html>
