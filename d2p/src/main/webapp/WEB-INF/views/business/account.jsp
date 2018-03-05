<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
		<span class="fa fa-remove"></span>
	</button>
	<h4 class="modal-title"><c:if test="${type == 1}">收款账户信息</c:if><c:if test="${type == 2}">付款账户信息</c:if></h4>
</div>
<div class="modal-body">
	<form id="notice-form" name="notice-form" class="form-horizontal">
		<div class="row">
			<div class="col-md-12">
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">注册手机</label>
					<div class="col-sm-10">
						<input class="form-control" id="alipay" name="alipay"  value="${account.phone}" readonly></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　开户名</label>
					<div class="col-sm-10">
						<input class="form-control" id="userName" name="userName" value="${account.userName}" readonly></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　　银行</label>
					<div class="col-sm-10">
						<input class="form-control" id="bank" name="bank"  value="${account.bank}" readonly></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　开户行</label>
					<div class="col-sm-10">
						<input class="form-control" id="bankDetail" name="bankDetail"  value="${account.bankDetail}" readonly></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">银行卡号</label>
					<div class="col-sm-10">
						<input class="form-control" id="bankCard" name="bankCard"  value="${account.bankCard}" readonly></input>
					</div>
				</div>
				<div class="form-group">
					<label for="title" class="col-sm-2 control-label">　支付宝</label>
					<div class="col-sm-10">
						<input class="form-control" id="alipay" name="alipay"  value="${account.alipay}" readonly></input>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-footer">
			<button type="button" id="dismissBtn" class="btn btn-default" data-dismiss="modal">关闭</button>
		</div>
	</form>
</div>
