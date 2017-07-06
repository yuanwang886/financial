<%@ page contentType="text/html;charset=UTF-8"%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">确认收款</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<div class="col-sm-12">
						<label for="password">支付密码:</label> <input type="password" class="form-control" id="password" name="password" placeholder="请输入支付密码">
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="submit" class="btn btn-primary">确认收款</button>
		</div>
	</form>
</div>

<script>
	$(function() {
		$("#notice-form").bootstrapValidator({
			submitHandler : function(validator, roleform, submitButton) {
				submitButton.attr("disabled",true);
				ajaxPost(ctx + "/business/sell/confirm", {
					"id" : $("#clickId").val(),
					"password" : $("#password").val()
				}, function(data) {
					submitButton.attr("disabled",false);
					if (data.result == 1) {
						$.alert({
							title : '提示',
							content : "收款确认成功~",
							animation : 'zoom',
							closeAnimation : 'zoom',
							buttons : {
								okay : {
									text : '确定',
									btnClass : 'btn-blue',
									action : function() {
										$('#myModal').modal('hide');
										setTimeout(function() {
											loadPage(ctx + "/business/sell/list/index");
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
				password : {
					validators : {
						notEmpty : {
							message : '请输入您的支付密码'
						}
					}
				}
			}
		});
	});
</script>