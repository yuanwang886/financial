<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>得到捐助</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">得到捐助</li>
	</ol>
</section>
<section class="content">
	<form role="form">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-primary">
					<div class="box-body">
						<div class="form-group">
							<label for="money">本息钱包金额：</label><label class="text-danger">${wallet.wallet}</label>
						</div>
						<div class="form-group">
						    <label for="money">可以提现金额：</label><label class="text-danger">${wallet.cantraded}</label>
							<label class="text-danger">（已买入超过5天可提现）</label>
						</div>
						<div class="form-group">
						   <label for="money">请输入提现金额</label><label class="text-danger">（100-10000，且是50的整数倍）</label><input type="number"
								class="form-control" id="money" name="money" placeholder="请输入金额">
						</div>
					</div>
					<div class="box-footer">
						<button type="button" id="publishBtn"
							class="btn btn-primary btn-block btn-flat">得到捐助</button>
					</div>

				</div>
			</div>
		</div>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
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

			//首先判断钱包金额
			var cantraded = "${wallet.cantraded}";
			if (parseFloat(cantraded) == 0) {
				$.alert({
					title : '提示',
					content : "您当前无可提现金额~",
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

			var pass = "${user.payPassword}";
			var bank = "${user.account.bank}";

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
			
			var flag = "${user.invitePhone}";
			if (flag == null || flag == "") {
				if (!isNumberBy50(money) || (parseFloat(money) > parseFloat(cantraded))) {
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
			}
			else if (!isNumberBy50(money) || (parseFloat(money) < 100) || (parseFloat(money) > 10000) || (parseFloat(money) > parseFloat(cantraded))) {
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
		ajaxPost(ctx + "/business/sell/submit", {
			"money" : $("#money").val(),
			"password" : $("#password").val()
		}, function(data) {
			if (data.result == 1) {
				$.alert({
					title : '提示',
					content : "提现成功,请注意匹配消息~",
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
				$.alert({
					title : '提示',
					content : data.desc,
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
			}
		})
	}
</script>