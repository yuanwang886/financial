<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<style>
.info-box {
	border: 1px solid rgba(0, 0, 0, 0.1);
}

.info-box-text {
	text-transform: none;
}

.info-box:hover .info-box-icon {
	font-size: 60px;
}

.info-box-text-sm {
	font-size: 12px;
	font-style: normal;
}

.info-box-text-italic {
	font-size: 14px;
	font-style: italic;
	font-weight: normal;
}

.text-blod {
	font-weight: bold;
}

.title-sm {
	font-weight: normal;
	margin-left: 10px;
}
</style>
<section class="content-header">
	<h5 class="text-danger">警告：<label>请在48小时内提供帮助,否则您的账号将给锁定</label></h5>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"> <i class="fa fa-dashboard"></i> 首页 </a></li>
	</ol>
</section>

<!-- Main content -->
<section class="content">

   <c:if test="${user.userRole == 1}">
		<div class="row">
			<div class="col-md-12">
				<div class="box box-info">
					<div class="box-header">
	
						<div class="box-tools pull-right">
							<button type="button" class="btn btn-box-tool" data-widget="collapse">
								<i class="fa fa-minus"></i>
							</button>
							<a type="button" class="btn btn-box-tool" href="#top" title="回到顶部">
								<i class="fa fa-arrow-up"></i>
							</a>
						</div>
					</div>
					<!-- /.box-header -->
					<div class="box-body">
						<div class="row">
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-target="#myModel" data-toggle="modal" data-url="/business/buy/risk">
									<span class="info-box-icon bg-aqua"><i class="glyphicon glyphicon-log-in"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">提供捐助</span> <span class="info-box-text-sm"> </span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-target="#myModel" data-toggle="modal" data-url="/business/sell/advice">
									<span class="info-box-icon bg-aqua"><i class="glyphicon glyphicon-log-out"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">得到捐助</span> <span class="info-box-text-sm"> </span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/business/buy/list/index">
									<span class="info-box-icon bg-aqua"><i class="fa fa-list"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">提供捐助记录</span> <span class="info-box-text-sm"> </span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/business/sell/list/index">
									<span class="info-box-icon bg-aqua"><i class="glyphicon glyphicon-th-list"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"> </span> <span class="info-box-text">得到捐助记录</span> <span class="info-box-text-sm"> </span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- /.col -->
		<div class="row">
			<div class="col-md-12">
				<div class="box box-warning">
					<div class="box-header">
						<div class="box-tools pull-right">
							<button type="button" class="btn btn-box-tool" data-widget="collapse">
								<i class="fa fa-minus"></i>
							</button>
							<a type="button" class="btn btn-box-tool" href="#top" title="回到顶部">
								<i class="fa fa-arrow-up"></i>
							</a>
						</div>
					</div>
					<div class="box-body">
						<div class="row">
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/user/team/index">
									<span class="info-box-icon bg-yellow"><i class="glyphicon glyphicon-fire"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">我的团队 </span> <span class="info-box-text-sm"></span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/user/wallet/index">
									<span class="info-box-icon bg-yellow"><i class="glyphicon glyphicon-usd"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">我的钱包 </span> <span class="info-box-text-sm"></span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/user/income/index">
									<span class="info-box-icon bg-yellow"><i class="fa fa-file-text-o"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">我的账单 </span> <span class="info-box-text-sm"></span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/feedback/add">
									<span class="info-box-icon bg-yellow"><i class="fa fa-envelope"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">意见反馈 </span> <span class="info-box-text-sm"></span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<div class="box danger">
					<div class="box-header">
						<div class="box-tools pull-right">
							<button type="button" class="btn btn-box-tool" data-widget="collapse">
								<i class="fa fa-minus"></i>
							</button>
							<a type="button" class="btn btn-box-tool" href="#top" title="回到顶部">
								<i class="fa fa-arrow-up"></i>
							</a>
						</div>
					</div>
					<div class="box-body">
						<div class="row">
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/doc/help/index">
									<span class="info-box-icon bg-red"><i class="glyphicon glyphicon-briefcase"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">操作指南 </span> <span class="info-box-text-sm"></span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/doc/rule/index">
									<span class="info-box-icon bg-red"><i class="glyphicon glyphicon-duplicate"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">规则制度 </span> <span class="info-box-text-sm"></span>
									</div>
								</div>
							</div>
							<div class="col-md-3 col-sm-6 col-xs-12">
								<div class="info-box" data-url="/doc/warn/index">
									<span class="info-box-icon bg-red"><i class="glyphicon glyphicon-flash"></i></span>
									<div class="info-box-content">
										<span class="info-box-text text-blod"></span> <span class="info-box-text">注意事项 </span> <span class="info-box-text-sm"></span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content"></div>
			</div>
		</div>
	</c:if>
	
	<c:if test="${user.userRole == 2}">
	    <!-- 那么我们开始呈现报表信息 -->
		<div class="row">
				<div class="col-xs-12">
					<div id="u-notice-list">
						<ul class="list-group">
							<li class="list-group-item">平台总人数：${sysData.personNum}</li>
							<li class="list-group-item">被锁定人数：${sysData.personLockNum}</li>
							<li class="list-group-item">买入总金额：${sysData.buyMoney}</li>
							<li class="list-group-item">卖出总金额：${sysData.sellMoney}</li>
						</ul>
					</div>
				</div>
		</div>	    
	</c:if>
</section>

<script type="text/javascript">
	$(function() {
		$(".info-box").each(
				function(index, item) {
					$(item).hover(
							function() {
								var bg = $(this).find("span:eq(0)").attr("class").replace("info-box-icon", "").trim();
								if (bg) {
									$(this).find("span:eq(0)").removeClass(bg);
									$(this).addClass(bg);
								} else {
									bg = $(this).attr("class").replace("info-box", "").trim();
									$(this).find("span:eq(0)").addClass(bg);
									$(this).removeClass(bg);
								}
							},
							function() {
								var bg = $(this).attr("class").replace("info-box", "").trim();
								if (bg) {
									$(this).find("span:eq(0)").addClass(bg);
									$(this).removeClass(bg);
								} else {
									bg = $(this).find("span:eq(0)").attr("class").replace("info-box-icon", "").trim();
									$(this).find("span:eq(0)").removeClass(bg);
									$(this).addClass(bg);
								}
							});
				});

		$(".info-box").each(function(index, item) {
			$(item).click(function() {
				if ($(this).data("flag")) {
					return;
				}
				if ($(this).data("toggle")) {
					// 弹出风险提示
					$("#myModal").modal({
						remote : ctx + $(this).data("url")
					});
					return;
				}
				if ($(this).data("url")) {
					location.hash = "#main";
					loadPage(ctx + $(this).data("url"));
				}
			})
		});

		$("#myModal").on("hidden.bs.modal", function() {
			$(this).removeData("bs.modal");
			$(this).find(".modal-content").children().remove();
		});
	});
</script>