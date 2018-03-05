<%@ page contentType="text/html;charset=UTF-8"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">新增内置账户</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label for="phone" class="col-sm-2 control-label">　手机号</label>
					<div class="col-sm-10">
						<input class="form-control" id="userphone" name="userphone"  placeholder="130000000000"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="password" class="col-sm-2 control-label">　　密码</label>
					<div class="col-sm-10">
						<input class="form-control" id="password" name="password"  placeholder="******"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="money" class="col-sm-2 control-label">钱包金额</label>
					<div class="col-sm-10">
						<input type="number" class="form-control" id="money" name="money"  placeholder="100000"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="userName" class="col-sm-2 control-label">　开户名</label>
					<div class="col-sm-10">
						<input class="form-control" id="userName" name="userName" placeholder="张三"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="bank" class="col-sm-2 control-label">　　银行</label>
					<div class="col-sm-10">
						<input class="form-control" id="bank" name="bank"  value="${user.account.bank}" placeholder="招商银行"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="bankDetail" class="col-sm-2 control-label">　开户行</label>
					<div class="col-sm-10">
						<input class="form-control" id="bankDetail" name="bankDetail"  value="${user.account.bankDetail}" placeholder="武汉市招商银行光谷支行"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="bankCard" class="col-sm-2 control-label">银行卡号</label>
					<div class="col-sm-10">
						<input class="form-control" id="bankCard" name="bankCard"  value="${user.account.bankCard}" placeholder="6225888888888888"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="alipay" class="col-sm-2 control-label">　支付宝</label>
					<div class="col-sm-10">
						<input class="form-control" id="alipay" name="alipay"  value="${user.account.alipay}" placeholder="18688888888"></input>
					</div>
				</div>
			</div>
		</div>
		
		<div class="modal-footer">
			<label class="col-sm-6 control-label box-warning">此处登录密码与支付密码相同</label>
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="submit" class="btn btn-primary">提交</button>
		</div>
	</form>
</div>

<script>
	$(function() {
		//初始化控件
		//数据校验
		$("#notice-form").bootstrapValidator({
			submitHandler : function(validator, roleform, submitButton) {
				submitButton.attr("disabled",true);
				ajaxPost(ctx + "/internal/submit", {
					"phone" : $("#userphone").val(),
					"password" : $("#password").val(),
					"money" : $("#money").val(),
					"userName" : $("#userName").val(),
					"bank" : $("#bank").val(),
					"bankDetail" : $("#bankDetail").val(),
					"bankCard" : $("#bankCard").val(),
					"alipay" : $("#alipay").val()
				}, function(data) {
					submitButton.attr("disabled", false);
					if (data.result == 1) {
						$.alert({
							title : '提示',
							content : "新增账号成功~",
							animation : 'zoom',
							closeAnimation : 'zoom',
							buttons : {
								okay : {
									text : '确定',
									btnClass : 'btn-blue',
									action : function() {
										$('#myModal').modal('hide');
										setTimeout(function() {
											loadPage(ctx + "/internal/index");
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
				phone : {
					validators : {
						notEmpty : {
							message : '请输入手机号'
						}
					}
				},
				password : {
					validators : {
						notEmpty : {
							message : '请输入支付密码'
						}
					}
				},
				money : {
					validators : {
						notEmpty : {
							message : '请输入初始化钱包金额'
						}
					}
				},
				userName : {
					validators : {
						notEmpty : {
							message : '请输入银行账户姓名'
						}
					}
				},
				bank : {
					validators : {
						notEmpty : {
							message : '请输入银行名称'
						}
					}
				},
				bankDetail : {
					validators : {
						notEmpty : {
							message : '请输入开户行'
						}
					}
				},
				bankCard : {
					validators : {
						notEmpty : {
							message : '请输入银行卡号'
						}
					}
				},
				alipay : {
					validators : {
						notEmpty : {
							message : '请输入支付宝账号'
						}
					}
				}
			}
		});

	});
</script>