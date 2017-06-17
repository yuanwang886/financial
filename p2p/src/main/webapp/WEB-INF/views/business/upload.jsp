<%@ page contentType="text/html;charset=UTF-8"%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title">请选择您给对方打款的截图</h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal"
		enctype="multipart/form-data">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<div class="col-sm-10">
						<div type="file" id="input-1" class="btn btn-primary">请选择文件</div>
						<div id="input-2"></div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-10 text-danger">
						上传图片,支持jpg,gif,png,最多只能传1张
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-12">
						<label for="password">支付密码:</label> <input type="password" class="form-control" id="password" name="password" placeholder="请输入支付密码">
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			<button type="button" id="uploadBtn" class="btn btn-primary">上传图片</button>
		</div>
		<input type="hidden" id="fileName">
	</form>
</div>

<script>
	$(function() {
		var uploader = new plupload.Uploader({
			browse_button : 'input-1', 
			url : ctx + '/upload',
			multipart_params : {id: $("#clickId").val(),  type: 1001, password: $("#password").val()},
			filters : {
				max_file_size:'5mb',
				mime_types: [{title : "Image files", extensions : "jpg,gif,png"}]
			},
			init: {
				FilesAdded: function(up, files){
					plupload.each(files, function(file) {
						$("#fileName").val(file.name);
						$("#input-2").html(file.name);
					});
				},
				FileUploaded:function(uploader,file,responseObject){
					//上传完毕
					var d = JSON.parse(responseObject.response);
					if (d.result == 1) {
						$.alert({
							title : '提示',
							content : "图片上传成功~",
							animation : 'zoom',
							closeAnimation : 'zoom',
							buttons : {
								okay : {
									text : '确定',
									btnClass : 'btn-blue',
									action : function() {
										$('#myModal').modal('hide');
										setTimeout(function() {
											loadPage(ctx + "/business/buy/list/index");
										}, 500);
									}
								}
							}
						});
					}
					else {
						$.alert({
							title : '提示',
							content : d.desc,
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
					$("#fileName").val('');
					$("#input-2").html('');
					$('#input-1').attr("disabled",false);
					$('#uploadBtn').attr("disabled",false);
				},
				UploadComplete:function(up,files){
				},
				Error: function(up, err) {
				}
			}
		});
		
		uploader.init();
		
		$('#uploadBtn').click(function() {
		    var file = $("#fileName").val();
			if (file == null || file == '') {
				$.alert({
					title : '提示',
					content : "请先选择图片~",
					animation : 'zoom',
					closeAnimation : 'zoom',
					backgroundDismiss: true,
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
						}
					}
				});
				return;
			}
			var pass = $("#password").val();
			if (pass == null || pass == '') {
				$.alert({
					title : '提示',
					content : "请输入支付密码~",
					animation : 'zoom',
					closeAnimation : 'zoom',
					backgroundDismiss: true,
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
						}
					}
				});
				return;
			}
			
			uploader.settings.multipart_params.password = $("#password").val();
			//开始请求上传
			uploader.start();
			$('#input-1').attr("disabled",true);
			$('#uploadBtn').attr("disabled",true);
		});
	});
</script>