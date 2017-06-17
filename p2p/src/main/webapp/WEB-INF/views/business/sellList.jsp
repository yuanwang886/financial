<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>得到帮助记录</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">得到帮助记录</li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list"></div>
		</div>
	</div>
	<div class="text-center">
		<button type="button" id="refreshPage" class="btn btn-warning">刷新</button>
		<button type="button" id="nextPage" class="btn btn-primary">点击加载更多记录</button>
	</div>
	
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content"></div>
		</div>
	</div>
	<input type="hidden" id="clickId">
</section>
<script>
	var list = null, id = null;
	$(function() {
		
		$('#refreshPage').click(function() {
			loadPage(ctx + "/business/sell/list/index");
		});
		
		$('#nextPage').click(function() {
			loadTableData();
		});
		
		$("#myModal").on("hidden.bs.modal", function() {
			$(this).removeData("bs.modal");
			$(this).find(".modal-content").children().remove();
		});

		loadTableData();
	});
	
	function loadTableData() {
		if (list != null) {
			var last = list[list.length - 1];
			id = last.id;
		}
		ajaxPost(ctx + '/business/sell/list', {
			"id" : id
		}, function(d) {
			if (d.result == 1) {
				//查询后我们开始创建界面元素
				list = d.data;
				var $panel;
				if (list == null || list.length < 10) {
					$("#nextPage").hide();
					if (id == null && list.length == 0) {
						$panel = $('<ul class="list-group">');
						$panel.append($('<li class="list-group-item">暂无任何消息</li>'));
						$panel.append($('</ul>'));
						$("#u-notice-list").append($panel);
						return;
					}
				}
				$.each(list, function(index, item) {
					$panel = $('<ul class="list-group">');
					$panel.append($('<li class="list-group-item"><a>　订单号：</a>' + item.id + '</li>'));
					$panel.append($('<li class="list-group-item"><a>　　金额：</a>' + item.money + '</li>'));
					if (item.state == 0) {
						$panel.append($('<li class="list-group-item"><div class="input-group"><a>　　状态：</a>' + item.stateName + '<span class="input-group-btn"><button type="button" onclick="accountInfo(' + "'" + item.buyAccountId + "'" + ')" class="btn btn-primary" data-target="#myModel" data-toggle="modal">付款人信息</button></span></div></li>'));	
					}
					else if (item.state == 1) {
						$panel.append($('<li class="list-group-item"><div class="input-group"><a>　　状态：</a>' + item.stateName + '<span class="input-group-btn"><button type="button" onclick="confirmPay(' + "'" + item.id + "'" + ')" class="btn btn-success"  data-target="#confirmModal" data-toggle="modal">确认收款</button><button type="button" onclick="lookImage(' + "'" + item.picUrl + "'" + ')" class="btn btn-info"  data-target="#picModal" data-toggle="modal">查看打款图片</button><button type="button" onclick="accountInfo(' + "'" + item.buyAccountId + "'" + ')" class="btn btn-primary" data-target="#myModel" data-toggle="modal">付款人信息</button></span></div></li>'));	
					}
					else {
						$panel.append($('<li class="list-group-item"><div class="input-group"><a>　　状态：</a>' + item.stateName + '<span class="input-group-btn"><button type="button" onclick="lookImage(' + "'" + item.picUrl + "'" + ')" class="btn btn-info"  data-target="#picModal" data-toggle="modal">查看打款图片</button><button type="button" onclick="accountInfo(' + "'" + item.buyAccountId + "'" + ')" class="btn btn-primary" data-target="#myModel" data-toggle="modal">付款人信息</button></span></div></li>'));	
					}
					$panel.append($('<li class="list-group-item"><a>创建时间：</a>' + item.createTime + '</li>'));
					$panel.append($('</ul>'));
					$("#u-notice-list").append($panel);
				});
			}
		});
	}
	
	function accountInfo(id) {
		$("#myModal").modal({
			remote : ctx + "/business/sell/account?accountId=" + id
		});
	}

	function lookImage(url) {
		$("#myModal").modal({
			remote : ctx + "/business/buy/showImg?url=" + url
		});
	}
	
	function confirmPay(id) {
		$("#clickId").val(id);
		$("#myModal").modal({
			remote : ctx + "/business/sell/confirm/index"
		});
	}
</script>