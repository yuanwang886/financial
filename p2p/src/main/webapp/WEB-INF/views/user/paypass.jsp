<%@ page contentType="text/html;charset=UTF-8"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">编辑支付密码</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="box-body">
					<div class="form-group">
						<label for="money">验证码:</label>
						<div class="input-group">
							<input type="text" class="form-control" id="code" name="code" placeholder="请输入6位短信验证码"> 
							<span class="input-group-btn">
								<input type="button" id="codeBtn" name="codeBtn" class="btn btn-warning btn-flat" value="点击获取验证码">
							</span>
						</div>
					</div>
					<div class="form-group">
						<label for="money">支付密码:</label> 
						<input type="password" class="form-control" id="password" name="password" placeholder="请输入支付密码">
					</div>
					<div class="form-group">
						<label for="money">确认支付密码:</label> 
						<input type="password" class="form-control" id="password2" name="password2" placeholder="请确认支付密码">
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="submit" class="btn btn-primary">提交</button>
		</div>
	</form>
</div>

<script>
	$(function() {
		$('#codeBtn').click(function() {
			var phone = "${user.phone}";
			if (phone == '' || phone == null) {
				$.alert({
					title : '提示',
					content : '登录超时，请重新登陆~',
					animation : 'zoom',
					closeAnimation : 'zoom',
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
							action : function() {
								window.loacation.href="/index";
							}	 
						}
					}
				});
				return;
			}

			$('#codeBtn').attr("disabled",true);
			ajaxP2P(ctx + '/sms/send', {
				"phone" : phone,
				"type" : 1003
			}, function(data) {
				$('#codeBtn').attr("disabled",false);
				if (data.result == 1) {
					countCodeTime();
				} else {
					$.alert({
						title : '提示',
						content : data.desc,
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
				}
			})
		});

		
		$("#notice-form").bootstrapValidator({
			submitHandler : function(validator, roleform, submitButton) {
				submitButton.attr("disabled",true);
				ajaxPost(ctx + "/user/info/paypass/submit", {
					"code" : $("#code").val(),
					"password" : $("#password").val()
				}, function(data) {
					submitButton.attr("disabled",false);
					if (data.result == 1) {
						$.alert({
							title : '提示',
							content : "资料编辑成功~",
							animation : 'zoom',
							closeAnimation : 'zoom',
							buttons : {
								okay : {
									text : '确定',
									btnClass : 'btn-blue',
									action : function() {
										$('#myModal').modal('hide');
										setTimeout(function() {
											loadPage(ctx + "/user/info/index");
										}, 500);
									}
								}
							}
						});
					} else {
						$.alert({
							title : '提示',
							content : data.desc,
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
					}
				})
			},
			fields : {
				code : {
					validators : {
						notEmpty : {
							message : '请输入短信验证码'
						}
					}
				},
				password : {
					validators : {
						notEmpty : {
							message : '请输入支付密码'
						},
						stringLength : {
							min : 6,
							max : 16,
							message : '密码长度必须在6到16之间'
						}
					}
				},
				password2 : {
					validators : {
						notEmpty : {
							message : '请确认支付密码'
						},
						stringLength : {
							min : 6,
							max : 16,
							message : '密码长度必须在6到16之间'
						},
						identical : {//相同
							field : 'password',
							message : '两次密码不一致'
						}
					}
				}
			}
		});
	});
	
	var countTime = 90;
	function countCodeTime() {
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
			countCodeTime()
		}, 1000)
	}
</script>