<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<title>${fns:getConfig('productName')}-忘记密码</title>
</head>
<body class="hold-transition login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="#"><b>${fns:getConfig('productName')}</b></a>
		</div>
		<!-- /.login-logo -->
		<div class="login-box-body">
			<p class="login-box-msg">
				<strong>找回密码</strong>
			</p>
			<div class="form-group has-feedback pull-right">
				<a href="${ctx}/index" class="text-center">已有账号</a>
			</div>
			<br> <br>
			<form action="${ctx}/sys/forgot" method="post" id="forgot-form">
				<div class="form-group has-feedback">
					<input type="text" class="form-control" name="phone" id="phone"
						placeholder="请输入手机号"> <span
						class="glyphicon glyphicon-phone form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<div class="input-group ">
						<input type="text" class="form-control" name="code" id="code" placeholder="请输入验证码"> <span class="input-group-btn">
							<input type="button" id="codeBtn" name="codeBtn" class="btn btn-warning btn-flat" value="点击获取验证码"></input>
						</span>
					</div>
				</div>
				<div class="form-group has-feedback">
					<input type="password" class="form-control" name="password" id="password" placeholder="请输入密码"> <span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" class="form-control" name="repassword" id="repassword" placeholder="再次确认密码"> <span class="glyphicon glyphicon-log-in form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<button type="submit" class="btn btn-info btn-block btn-flat">提交</button>
					</div>
				</div>
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

			$('#codeBtn').click(function() {

				var phone = $('#phone').val();
				if (phone == '' || phone == null) {
					$.alert({
						title : '提示',
						content : '请先填写您的手机号',
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
					return;
				}

				ajaxP2P('${ctx}/sms/send', {
					"phone" : phone,
					"type" : 1002
				}, function(d) {
					if (d.result == 1) {
						settime();
					}
				});

			});

			$("#forgot-form").bootstrapValidator({
				submitHandler : function(valiadtor, loginForm, submitButton) {
					valiadtor.defaultSubmit();
				},
				fields : {
					phone : {
						validators : {
							notEmpty : {
								message : '手机号不能为空'
							},
							stringLength : {
								/*长度提示*/
								min : 11,
								max : 11,
								message : '请输入正确的手机号'
							}
						}
					},
					password : {
						validators : {
							notEmpty : {
								message : '密码不能为空'
							},
							stringLength : {
								/*长度提示*/
								min : 6,
								max : 30,
								message : '密码长度必须在6到30之间'
							},
							different : {//不能和用户名相同
								field : 'phone',//需要进行比较的input name值
								message : '不能和手机号相同'
							},
							regexp : {
								regexp : /^[a-zA-Z0-9_\.]+$/,
								message : '密码由数字字母下划线和.组成'
							}
						}
					},
					repassword : {
						message : '密码无效',
						validators : {
							notEmpty : {
								message : '密码不能为空'
							},
							stringLength : {
								min : 6,
								max : 30,
								message : '用户名长度必须在6到30之间'
							},
							identical : {//相同
								field : 'password',
								message : '两次密码不一致'
							},
							regexp : {//匹配规则
								regexp : /^[a-zA-Z0-9_\.]+$/,
								message : '密码由数字字母下划线和.组成'
							}
						}
					},
					code : {
						validators : {
							notEmpty : {
								message : '验证码不能为空'
							},
							stringLength : {
								/*长度提示*/
								min : 6,
								max : 6,
								message : '请输入正确的验证码'
							}
						}
					}
				}
			});
		});

		var countTime = 90;
		function settime() {
			if (countTime == 0) {
				$('#codeBtn').attr('disabled', "false");
				$('#codeBtn').val("重新获取验证码");

				countTime = 90;
				return;
			} else {
				$('#codeBtn').attr('disabled', "true");
				$('#codeBtn').val("重新发送  (" + countTime + ")");
				countTime--;
			}
			setTimeout(function() {
				settime()
			}, 1000)
		}
	</script>
</body>
</html>
