<%@ page contentType="text/html;charset=UTF-8"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">编辑收款账户</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　开户名</label>
					<div class="col-sm-10">
						<input class="form-control" id="userName" name="userName" value="${user.account.userName}" placeholder="张三" maxlength="20"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　　银行</label>
					<div class="col-sm-10">
						<input class="form-control" id="bank" name="bank"  value="${user.account.bank}" placeholder="招商银行" maxlength="64"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　开户行</label>
					<div class="col-sm-10">
						<input class="form-control" id="bankDetail" name="bankDetail"  value="${user.account.bankDetail}" placeholder="武汉市招商银行光谷支行" maxlength="64"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">银行卡号</label>
					<div class="col-sm-10">
						<input class="form-control" id="bankCard" name="bankCard"  value="${user.account.bankCard}" placeholder="6225888888888888" maxlength="20" ></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　支付宝</label>
					<div class="col-sm-10">
						<input class="form-control" id="alipay" name="alipay"  value="${user.account.alipay}" placeholder="18688888888" maxlength="40"></input>
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
		//初始化控件
		//数据校验
		$("#notice-form").bootstrapValidator({
			submitHandler : function(validator, roleform, submitButton) {
				submitButton.attr("disabled",true);
				ajaxPost(ctx + "/user/info/acc/submit", {
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