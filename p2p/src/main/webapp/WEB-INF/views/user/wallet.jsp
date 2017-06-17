<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>我的钱包</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">我的钱包</li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list" class="box box-warning">
				<ul class="list-group">
					<li class="list-group-item"><a>当前信息</a></li>
					<li class="list-group-item">钱包余额：${wallet.wallet}</li>
					<li class="list-group-item">　可卖出：${wallet.cantraded}</li>
					<li class="list-group-item">　交易中：${wallet.freeze}</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list">
				<ul class="list-group">
					<li class="list-group-item"><a>钱包明细</a></li>
					<li class="list-group-item">买入金额：${wallet.buyMoney}</li>
					<li class="list-group-item">卖出金额：${wallet.sellMoney}</li>
					<li class="list-group-item">赚取利息：${wallet.interest}</li>
					<li class="list-group-item">团队奖金：${wallet.integrity}</li>
				</ul>
			</div>
		</div>
	</div>
	
	<c:if test="${empty lastbuy}">
		<div class="row">
			<div class="col-xs-12">
				<div id="u-notice-list">
					<ul class="list-group">
						<li class="list-group-item"><a>最近一笔买入记录</a></li>
						<li class="list-group-item">买入金额：${buy.money}</li>
						<li class="list-group-item">操作时间：<fmt:formatDate value="${buy.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></li>
						<li class="list-group-item">当前状态：${buy.stateName}</li>
						<li class="list-group-item">等待匹配：${buy.matchMoney}</li>
						<li class="list-group-item">支付金额：${buy.outMoney}</li>
					</ul>
				</div>
			</div>
		</div>
	</c:if>
	
	<c:if test="${not empty sell}">
		<div class="row">
			<div class="col-xs-12">
				<div id="u-notice-list">
					<ul class="list-group">
						<li class="list-group-item"><a>最近一笔卖出记录</a></li>
						<li class="list-group-item">卖出金额：${sell.money}</li>
						<li class="list-group-item">操作时间：<fmt:formatDate value="${sell.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></li>
						<li class="list-group-item">当前状态：${sell.stateName}</li>
						<li class="list-group-item">等待匹配：${sell.matchMoney}</li>
						<li class="list-group-item">收款金额：${sell.incomeMoney}</li>
					</ul>
				</div>
			</div>
		</div>
	</c:if>
	
</section>
<script>
	$(function() {
		
	});
</script>