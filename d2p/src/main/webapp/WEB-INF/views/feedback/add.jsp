<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>意见反馈</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">意见反馈</li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div class="box box-warning">
				<form id="user-form" name="user-form" class="form-horizontal">
					<div class="box-header">
						<div class="col-xs-12 text-center"></div>
					</div>
					<div class="box-body">
						<div class="form-group">
							<div class="col-xs-12">
								<textarea class="form-control" id="content" name="content"
									placeholder="请描述您的问题" rows="3"></textarea>
							</div>
						</div>
					</div>
					<!-- /.box-body -->
					<div class="box-footer text-center">
						<button type="submit" class="btn btn-primary btn-block btn-flat">提交</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</section>
<script>
	$(function() {
		$("#user-form").bootstrapValidator({
			submitHandler : function(validator, userform, submitButton) {

				$.confirm({
					title : '提示',
					content : '您确定要提交该反馈么?',
					icon : 'fa fa-question-circle',
					animation : 'scale',
					closeAnimation : 'scale',
					opacity : 0.5,
					buttons : {
						cancel : {
							text : '取消'
						},
						'confirm' : {
							text : '确定',
							btnClass : 'btn-blue',
							action : function() {
								ajaxPost(ctx + "/feedback/submit", {
									"content" : $("#content").val()
								}, function(data) {
									if (data.result == 1) {
										$.alert({
											title : '提示',
											content : "感谢您的反馈,谢谢~",
											animation : 'zoom',
											closeAnimation : 'zoom',
											buttons : {
												okay : {
													text : '确定',
													btnClass : 'btn-blue',
													action: function () {
														loadPage(ctx + "/main/homePage");
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
							}
						}
					}
				});

			},
			fields : {
				content : {
					validators : {
						notEmpty : {
							message : '请输入您的问题'
						}
					}
				}
			}
		});

	});
</script>