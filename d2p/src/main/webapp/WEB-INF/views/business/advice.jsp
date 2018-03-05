<%@ page contentType="text/html;charset=UTF-8"%>
<style type="text/css">
/*CSS源代码*/
/* 只滚动窗口中间内容块 */
#myModal .modal-body {
	height: 300px;
	overflow: auto;
}
/*
滚动整个modal
#myModal .modal-content
{
  height:200px;
  overflow:auto;
}
*/
</style>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h2 class="modal-title text-center">卖出须知</h2>
</div>
<div class="modal-body">
	<div class="row">
		<div class="col-xs-12">
			<div class="box box-warning">
				<div class="form-horizontal">
					<p class="lead">第一条： 卖出金额为100-10000。且必须是50的整数倍。</p>
					<p class="lead">第二条： 卖出订单将在48小时内匹配。</p>
					<p class="lead">第三条： 一天只可创建一次卖出订单。订单一旦创建，不可删除。</p>
					<p class="lead">第四条： 确认倒计时12小时内，否则系统自动确认，造成损失自己承担。</p>
					<p class="lead">第五条： 如果提供帮助的订单处于代付款状态，那么您将无法获得帮助。</p>
					<p class="lead">第六条： 匹配后对方打款时间为12小时，超时系统将在第一时间为您重新匹配。</p>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="modal-footer">
	<button type="button" id="nextPage" class="btn btn-primary">我已认真阅读以上说明</button>
</div>
<script>
	$(function() {
		$('#nextPage').click(function() {
			$('#myModal').modal('hide');

			//注释部分也可以解决滚动条的问题
			//$(".modal-backdrop").remove();
			//$("body").removeClass('modal-open');
			setTimeout(function() {
				loadPage(ctx + "/business/sell/index");
			}, 500);
		});
	});
</script>