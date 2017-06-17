<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>我的团队</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">我的团队</li>
	</ol>
</section>
<section class="content">
	<ul id="myTab" class="nav nav-tabs">
		<li><a href="#home" data-toggle="tab" data-url="/user/team/index">基本信息</a></li>
		<li><a href="#first"  data-toggle="tab" data-url="/user/team/first">一级团队</a></li>
		<li  class="active"><a href="#second"  data-toggle="tab">二级团队</a></li>
	</ul>
	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list"></div>
		</div>
	</div>
	<div class="text-center">
		<button type="button" id="nextPage" class="btn btn-primary">点击加载更多消息</button>
	</div>
</section>
<script>
$(function() {
	$('#nextPage').click(function() {
		loadTableData();
	});
	
	$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
		$(".active-tab span").html(loadPage(ctx + $(this).data("url")));
	});
	
	loadTableData();
});

/**
 * 查询记录信息
 */
var list = null, id = null;
function loadTableData() {
	if (list != null) {
		var last = list[list.length - 1];
		id = last.id;
	}
	ajaxPost(ctx + '/user/team/list', {
		"type" : 2,
		"id" : id
	}, function(d) {
		if (d.result == 1) {
			//查询后我们开始创建界面元素
			list = d.data;
			var $panel;
			if (list == null || list.length < 10) {
				$("#nextPage").hide();
				if (id == null && list.length == 0) {
					$panel = $('<div class="panel panel-info">');
					$panel.append($('<div class="panel-body">暂无任何排单消息</div>'));
					$panel.append($('</div>'));
					$("#u-notice-list").append($panel);
					return;
				}
			}
			$.each(list, function(index, item) {
				$panel = $('<ul class="list-group">');
				$panel.append($('<li class="list-group-item"><a>　　姓名：</a>' + item.realname + '</li>'));
				$panel.append($('<li class="list-group-item"><a>　　电话：</a>' + item.phone + '</li>'));
				$panel.append($('<li class="list-group-item"><a>　　金额：</a>' + item.money + '</li>'));
				$panel.append($('<li class="list-group-item"><a>　　状态：</a>' + item.stateName + '</li>'));
				$panel.append($('<li class="list-group-item"><a>创建时间：</a>' + item.createTime + '</li>'));
				$panel.append($('</ul>'));
				$("#u-notice-list").append($panel);
			});
		}
	});
}
</script>