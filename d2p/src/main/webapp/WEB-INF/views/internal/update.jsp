<%@ page contentType="text/html;charset=UTF-8"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">修改内置账户</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label for="phone" class="col-sm-2 control-label">　手机号</label>
					<div class="col-sm-10">
						<input class="form-control" id="userphone" name="userphone"  value="${internal.phone}" disabled=true></input>
					</div>
				</div>
				<div class="form-group">
					<label for="name" class="col-sm-2 control-label">用户姓名</label>
					<div class="col-sm-10">
						<input class="form-control" id="name" name="name"  value="${internal.realname}" disabled=true></input>
					</div>
				</div>
				<div class="form-group">
					<label for="money" class="col-sm-2 control-label">钱包金额</label>
					<div class="col-sm-10">
						<input type="number" class="form-control" id="money" name="money"  placeholder="100000"></input>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="submit" class="btn btn-primary">提交</button>
		</div>
		<input type="hidden" id="userId" name="userId"  value="${internal.userId}"></input>
	</form>
</div>

<script>
	$(function() {
		//初始化控件
		//数据校验
		$("#notice-form").bootstrapValidator({
			submitHandler : function(validator, roleform, submitButton) {
				submitButton.attr("disabled",true);
				ajaxPost(ctx + "/internal/modify", {
					"userId" : $("#userId").val(),
					"money" : $("#money").val()
				}, function(data) {
					submitButton.attr("disabled", false);
					if (data.result == 1) {
						$.alert({
							title : '提示',
							content : "修改账号成功~",
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
				money : {
					validators : {
						notEmpty : {
							message : '请输入增加金额'
						}
					}
				}
			}
		});

	});
</script>