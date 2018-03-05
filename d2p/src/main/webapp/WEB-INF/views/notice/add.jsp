<%@ page contentType="text/html;charset=UTF-8"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">新增公告</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">公告标题</label>
					<div class="col-sm-10">
						<input class="form-control" id="title" name="title"
							placeholder="请输入您的标题"></input>
					</div>
				</div>
				<div class="form-group">
					<label for="content" class="col-sm-2 control-label">公告内容</label>
					<div class="col-sm-10">
						<textarea class="form-control" id="content" name="content"
							placeholder="请输入您的内容"></textarea>
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
				ajaxPost(ctx + "/notice/submit", {
					"title" : $("#title").val(),
					"content" : $("#content").val()
				}, function(data) {
					if (data.result == 1) {
						$.alert({
							title : '提示',
							content : "公告发布成功~",
							animation : 'zoom',
							closeAnimation : 'zoom',
							buttons : {
								okay : {
									text : '确定',
									btnClass : 'btn-blue',
									action : function() {
										$('#myModal').modal('hide');
										setTimeout(function() {
											loadPage(ctx + "/notice/index");
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
				title : {
					validators : {
						notEmpty : {
							message : '请输入公告标题'
						}
					}
				},
				content : {
					validators : {
						notEmpty : {
							message : '请输入公告内容信息'
						}
					}
				}
			}
		});

	});
</script>