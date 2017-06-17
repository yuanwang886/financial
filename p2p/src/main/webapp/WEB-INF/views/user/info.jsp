<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>我的信息</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">我的信息</li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list" class="box box-warning">
				<ul class="list-group">
					<li class="list-group-item"><a>　　姓名：</a>${user.realname}</li>
					<li class="list-group-item"><a>　　手机：</a>${user.phone}</li>
					<li class="list-group-item"><a>当前状态：</a><c:if test="${user.state == 1}">已锁定</c:if>
						<c:if test="${user.state == 0}">正常</c:if></li>
					<li class="list-group-item"><a>注册日期：</a><fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list">
				<ul class="list-group">
					<li class="list-group-item"><div class="input-group">
							<a>支付密码</a> <span class="input-group-btn">
								<button type="button" id="payBtn" class="btn btn-primary"
									data-target="#myModel" data-toggle="modal">编辑</button>
							</span>
						</div></li>
					<li class="list-group-item">支付密码：<c:if
							test="${not empty user.payPassword}">******</c:if></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list">
				<ul class="list-group">
					<li class="list-group-item">
						<div class="input-group">
							<a>收款账号</a>&nbsp;&nbsp;<span class="input-group-btn"><button
									type="button" id="publishBtn" class="btn btn-primary"
									data-target="#myModel" data-toggle="modal">编辑</button></span>
						</div>
					</li>
					<li class="list-group-item">　开户名：${user.account.userName}</li>
					<li class="list-group-item">　　银行：${user.account.bank}</li>
					<li class="list-group-item">　开户行：${user.account.bankDetail}</li>
					<li class="list-group-item">银行卡号：${user.account.bankCard}</li>
					<li class="list-group-item">　支付宝：${user.account.alipay}</li>
				</ul>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list">
				<ul class="list-group">
					<li class="list-group-item"><a>推广链接</a></li>
					<li class="list-group-item">${user.inviteUrl}</li>
					<li class="list-group-item"><img border="0"
						src="${ctx}/zxingCode?content=${user.inviteUrl}"></li>
				</ul>
			</div>
		</div>
	</div>

	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
		<div class="modal-dialog">
			<div class="modal-content"></div>
		</div>
	</div>
</section>
<script>
	$(function() {
		$('#payBtn').click(function() {
			$("#myModal").modal({
				remote : ctx + "/user/info/paypass"
			});
		});

		$('#publishBtn').click(function() {
			$("#myModal").modal({
				remote : ctx + "/user/info/edit"
			});
		});

		$("#myModal").on("hidden.bs.modal", function() {
			$(this).removeData("bs.modal");
			$(this).find(".modal-content").children().remove();
		});
	});
</script>