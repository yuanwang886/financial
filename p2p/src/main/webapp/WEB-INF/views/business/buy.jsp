<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>提供帮助</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">提供帮助</li>
	</ol>
</section>
<section class="content">
	<form role="form">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-primary">

					<div class="box-body">
						<div class="form-group">
							<label for="money">请输入金额</label><label class="text-danger">（金额为1000-50000，且是1000的整数倍）</label>
							<input type="number" class="form-control" id="money" name="money"
								placeholder="请输入金额">
						</div>
					</div>
					<div class="box-footer">
						<button type="button" id="publishBtn" class="btn btn-primary btn-block btn-flat">提供帮助</button>
					</div>
				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog"aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							<span class="fa fa-remove"></span>
						</button>
						<h4 class="modal-title">支付密码</h4>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
								<div class="box-body">
									<div class="form-group">
										<label for="money">支付密码:</label> 
										<input type="password" class="form-control" id="password" name="password" placeholder="请输入支付密码">
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<button type="button" id="submitBtn" class="btn btn-primary">提交</button>
					</div>
				</div>
			</div>
		</div>
		<input type="hidden" id="sign" name="sign">
	</form>
</section>
<script>
	$(function() {
		$('#publishBtn').click(function() {

			var pass = "${user.payPassword}";
			var bank = "${user.account.bank}";
			var activation = "${user.activation}";

			if (activation != 1) {
				$.alert({
					title : '提示',
					content : "您当前处于未激活状态，请联系推荐人激活账户~",
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
			if (pass == '' || bank == '') {
				$.alert({
					title : '提示',
					content : "请完善您的个人信息~",
					animation : 'zoom',
					closeAnimation : 'zoom',
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
							action : function() {
								loadPage(ctx + "/user/info/index");
							}
						}
					}
				});
				return;
			}
			var money = $("#money").val();
			if (money == null || money == "") {
				$.alert({
					title : '提示',
					content : "请输入您的金额~",
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
				return;
			}

			if (!isNumberBy1000(money) || (parseFloat(money) > 50000)) {
				$.alert({
					title : '提示',
					content : "您的金额不符合要求~",
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
				return;
			}

			// 如果到这里了说明已经可以了，那么我们就开始弹出密码需要输入比较
			$("#myModal").modal('show');
		});

		$('#submitBtn').click(function() {
			if ($("#password").val() == "") {
				$.alert({
					title : '提示',
					content : "请输入您的支付密码",
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
				return;
			}
			$("#sign").val('1');
			$("#myModal").modal('hide');
		});
		
		$("#myModal").on("hidden.bs.modal", function() {
			//$(this).removeData("bs.modal");
			//$(this).find(".modal-content").children().remove();

			if ($("#sign").val() == '1') {
				requestServer();
			}
			else {
				$("#sign").val('');
				$("#password").val('')
			}
		});
	});

	function requestServer() {
		ajaxPost(ctx + "/business/buy/submit", {
			"money" : $("#money").val(),
			"password" : $("#password").val()
		}, function(data) {
			if (data.result == 1) {
				$.alert({
					title : '提示',
					content : "购买成功,请注意匹配消息~",
					animation : 'zoom',
					closeAnimation : 'zoom',
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
							action : function() {
								loadPage(ctx + "/main");
							}
						}
					}
				});
			} else {
				$("#password").val('');
				$.alert({
					title : '提示',
					content : data.desc,
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
			}
		})
	}
</script>