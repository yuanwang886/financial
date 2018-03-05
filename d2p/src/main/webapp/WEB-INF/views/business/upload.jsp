<%@ page contentType="text/html;charset=UTF-8"%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
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
						<input type="file" name="file" accept="image/*" >
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
			<button type="submit" class="btn btn-primary">上传图片</button>
		</div>
	</form>
</div>

<script>
	$(function() {

		$("#notice-form").bootstrapValidator({
			submitHandler : function(validator, roleform, submitButton) {
				submitButton.attr("disabled",true);

				  //[0]表示获取原生对象  
		        var formData = new FormData($("#notice-form")[0]);  
		        //还可以手动添加自定义字段，如下：  
		        formData.append("type", 1001);
		        formData.append("id", $("#clickId").val());

		        $.ajax({  
		            url :  ctx + '/f/upload',
		            type : 'POST',  
		            data : formData,  
		            async : false,  
		            cache : false,  
		            contentType : false,// 告诉jQuery不要去设置Content-Type请求头  
		            processData : false,// 告诉jQuery不要去处理发送的数据  
		            success : function(d) {
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
							
							$("#clickId").val('');
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
						
						submitButton.attr("disabled",false);
		            },  
		            error : function(data) {  
		
		            }  
		        });  

			},
			fields : {
				file : {
					validators : {
						notEmpty : {
							message : '请选择上传图片'
						}
					}
				},
				password : {
					validators : {
						notEmpty : {
							message : '请输入支付密码'
						}
					}
				}
			}
		});
	});
</script>