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
	<h2 class="modal-title text-center">风险提示</h2>
</div>
<div class="modal-body">
	<div class="row">
		<div class="col-xs-12">
			<div class="box box-warning">
				<div class="form-horizontal">
					<p class="lead">第一条：
						平台风险提示函是就平台相关服务事项可能纯在的风险，所订立的有效合约。会员通过网络页面点击确认或以其他方式选择接受平台风险提示函，即表示会员与本公司达成协议并同意接受平台风险提示函内所有的各项规则以及本公司网站所包含的其他与该协议或该项协议下各项规则的条款和条件有关的各项规定。
					</p>
					<p class="lead">第二条：
						在接受平台风险提示函之前，请您仔细阅读本风险提示函的全部内容。如果您不同意本风险提示函的任意内容或者无法准确理解本公司对风险提示函的解释，请不要进行后续操作。
					</p>
					<p class="lead">第三条： 年龄限制18-60周岁，具有完全民事行为能力，独立承担法律责任及义务。</p>
					<p class="lead">第四条：
						在平台提供捐助的会员必须保证本人身份的合法性、以及所提供信息的真实性、准确性、完整性和非过时性。</p>
					<p class="lead">第五条：
						若您提供的任何不真实，不准确，不完整或过时的身份信息，或者本公司有合理理由怀疑您所提供的信息为不真实，不准确，不完整或过时时，本公司有权终止或终止向您提供服务，并拒绝您现在或未来使用本服务之各项服务之全部或部分功能，对此本公司不承担任何责任。同时，因此所产生的任何直接或间接地支出、费用、处罚将由您来承担。
					</p>
					<p class="lead">第六条：
						平台仅提供技术运营维护服务，平台并不向任何会员提供资金捐助，也不接受任何会员的资金捐助，因此：任何会员的盈利或亏损，平台不承担任何责任以及任何形式担保。
					</p>
					<p class="lead">第七条：
						如果您自愿为平台系统内的其他会员提供资金捐助时，那么我们视您已充分了解平台的全部风险并自愿承担可能由此产生的全部后果。</p>
					<p class="lead">第八条：
						平台系统因下列状况无法正常运作，使您无法使用或无法正常使用各项服务时，平台不承担损害赔偿责任，该状况包括但不限于；</p>
					<p class="lead">（1） 平台本网站公告之系统停机维护期间的。</p>
					<p class="lead">（2） 电信设备出现故障不能正常运行数据运转的。</p>
					<p class="lead">（3）
						因台风、地震、海啸、洪水、停电、战争、恐怖袭击等不可抗力因素，造成平台系统障碍不能正常执行业务的。</p>
					<p class="lead">（4）
						由于黑客攻击、电信部门技术调整或故障、系统升级、银行限制、国家政策调整对系统造成的问题等原因而导致的服务中断或延迟的。</p>
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
				loadPage(ctx + "/business/buy/index");
			}, 500);
		});
	});
</script>