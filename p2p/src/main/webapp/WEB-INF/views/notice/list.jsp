<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>系统公告列表</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">系统公告列表</li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list"></div>
		</div>
	</div>
	<div class="text-center">
		<button type="button" id="nextPage" class="btn btn-primary">点击加载更多公告</button>
	</div>
</section>
<script>
	var list = null, id = null;
	$(function() {
		$('#nextPage').click(function() {
			loadTableData();
		});
		loadTableData();
	});

	/**
	 * 查询记录信息
	 */
	function loadTableData() {
		if (list != null) {
			var last = list[list.length - 1];
			id = last.id;
		}
		ajaxPost(
				ctx + '/notice/u/list',
				{
					"id" : id
				},
				function(d) {
					if (d.result == 1) {
						//查询后我们开始创建界面元素
						list = d.data;
						var $panel;
						if (list == null || list.length < 10) {
							$("#nextPage").hide();
							if (id == null && list.length == 0) {
								$panel = $('<div class="panel panel-info">');
								$panel.append($('<div class="panel-body">暂无任何消息</div>'));
								$panel.append($('</div>'));
								$("#u-notice-list").append($panel);
								return;
							}
						}
						$.each(list, function(index, item) {
											$panel = $('<div class="panel panel-info">');
											$panel.append($('<div class="panel-body"><h4 class="list-group-item-heading">' + item.title + '</h4><p class="list-group-item-text">' + item.content + '</p></div>'));
											$panel.append($('<div class="panel-footer text-right">'+ item.createTime+ '</div>'));
											$panel.append($('</div>'));
											$("#u-notice-list").append($panel);
										});
					}
				});
	}
</script>