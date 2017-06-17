<%@ page contentType="text/html;charset=UTF-8"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">购买激活码</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">手机号</label>
					<div class="col-sm-10">
						<input class="form-control" id="phone" name="phone"
							placeholder="请输入购买人手机号"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="num" class="col-sm-2 control-label">购买个数</label>
					<div class="col-sm-10">
						<input type="number" class="form-control" id="num" name="num"
							placeholder="请输入购买激活码的个数"></textarea>
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
				ajaxPost(ctx + "/release/submit", {
					"phone" : $("#phone").val(),
					"num" : $("#num").val()
				}, function(data) {
					if (data.result == 1) {
						$.alert({
							title : '提示',
							content : "购买激活码成功~",
							animation : 'zoom',
							closeAnimation : 'zoom',
							buttons : {
								okay : {
									text : '确定',
									btnClass : 'btn-blue',
									action : function() {
										$('#myModal').modal('hide');
										setTimeout(function() {
											loadPage(ctx + "/release/index");
										}, 350);
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
						regexp: {
	                         regexp: /^1[3|5|7|8]{1}[0-9]{9}$/,
	                         message: '请输入正确的手机号码'
	                    },
						notEmpty : {
							message : '请输入购买入手机号'
						}
					}
				},
				num : {
					validators : {
						notEmpty : {
							message : '请输入购买激活码数量'
						}
					}
				}
			}
		});

	});
</script>