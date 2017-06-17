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
		<li class="active"><a href="#home" data-toggle="tab">基本信息</a></li>
		<li><a href="#first"  data-toggle="tab" data-url="/user/team/first">一级团队</a></li>
		<li><a href="#second"  data-toggle="tab" data-url="/user/team/second">二级团队</a></li>
	</ul>
	<div class="row">
		<div class="col-xs-12">
			<div id="u-notice-list" class="box box-warning">
				<ul class="list-group">
					<li class="list-group-item">[${team.realName}]&nbsp;&nbsp;
						团队规模：${team.subordinateNum}&nbsp;&nbsp;&nbsp;&nbsp;
						直推规模：${team.popularizeNum}&nbsp;&nbsp;&nbsp;&nbsp;
						锁定人数：${team.lockingNum}
					</li>
					<li class="list-group-item">[${team.realName}]&nbsp;&nbsp;
						激活码总数量：<font color="red">${team.sumNum}</font> &nbsp;&nbsp;&nbsp;&nbsp;
						激活用户数量：<font color="red">${team.usedNum}</font>&nbsp;&nbsp;&nbsp;&nbsp;
						已经出售数量：<font color="red">${team.sellNum}</font>&nbsp;&nbsp;&nbsp;&nbsp;
						当前剩余数量：<font color="red">${team.lastNum}</font>&nbsp;&nbsp;&nbsp;&nbsp;
					</li>
				</ul>
			</div>
		</div>
	</div>
	<c:if test="${not empty team.superior}">
		<div class="row">
			<div class="col-xs-12">
				<div id="u-notice-list" >
					<ul class="list-group">
						<li class="list-group-item">我的领导人</li>
						<li class="list-group-item">${team.superior.realName}：${team.superior.phone}</li>
					</ul>
				</div>
			</div>
		</div>
	</c:if>
	<c:if test="${not empty team.subordinate}">
		<div class="row">
			<div class="col-xs-12">
				<div id="u-notice-list">
					<ul class="list-group">
						<li class="list-group-item"><a>我的直推人</a></li>
						<c:forEach items="${team.subordinate}" var="p">
							<li class="list-group-item">
								<span>${p.realName}：${p.phone}</span><br>
								<span>用户状态：<c:if test="${p.state == 1}">
									<font color="red">已锁定</font></c:if><c:if test="${p.state == 0}"><font color="green">正常</font></c:if>
								</span>
								<div class="center">
									<span><button type="button" onclick="releaseAdd('${p.userId}', ${p.state}, ${p.activation})" class="btn btn-success" data-target="#myModel" data-toggle="modal">下发激活码</button></span>
									<c:if test="${p.activation == 0}">
										<span><button type="button" onclick="activation('${p.userId}')" class="btn btn-success">激活</button></span>
									</c:if>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</c:if>
	<div class="text-center">
		<button type="button" id="nextPage" class="btn btn-primary">刷新</button>
	</div>
	
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							<span class="fa fa-remove"></span>
						</button>
						<h4 class="modal-title">购买激活码</h4>
					</div>
					<div class="modal-body">
						<div class="row">
							<div class="col-md-12">
								<div class="form-group">
									<label for="num" class="col-sm-2 control-label">购买个数</label>
									<div class="col-sm-10">
										<input type="number" class="form-control" id="num" name="num" placeholder="请输入购买激活码的个数"></textarea>
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
		<input type="hidden"  id="userId" name="userId">
		<input type="hidden" id="sign" name="sign">
</section>
<script>
	$(function() {
		$('#nextPage').click(function() {
			loadTableData();
		});
		
		$('a[data-toggle="tab"]').on('show.bs.tab', function (e) {
			$(".active-tab span").html(loadPage(ctx + $(this).data("url")));
		});

		$("#myModal").on("hidden.bs.modal", function(e) {
			//$(this).removeData("bs.modal");
			//$(this).find(".modal-content").children().remove();

			if ($("#sign").val() == '1') {
				requestServer();
			}
			else {
				$("#sign").val('');
				$("#num").val('')
				$("#userId").val('')
			}
		});

		$('#submitBtn').click(function() {
			if ($("#num").val() == "") {
				$.alert({
					title : '提示',
					content : "请输入购买激活码的个数",
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
		
	});

	/**
	 * 查询记录信息
	 */
	function loadTableData() {
		loadPage(ctx + "/user/team/index");
	}
	
	function activation(userId) {
		ajaxPost(ctx + "/user/team/activation", {
			"userId" : userId
		}, function(data) {
			if (data.result == 1) {
				$.alert({
					title : '提示',
					content : "激活成功~",
					animation : 'zoom',
					closeAnimation : 'zoom',
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
							action : function() {
								loadPage(ctx + "/user/team/index");
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
					backgroundDismiss: true,
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue'
						}
					}
				});
			}
		})
	}
	
	
	function releaseAdd(userId, state, activation) {

		if (state == 1) {
			$.alert({
				title : '提示',
				content : "该用户已经被锁定",
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
		if (activation == 0) {
			$.alert({
				title : '提示',
				content : "该用户还未激活，请先激活",
				backgroundDismiss: true,
				animation : 'zoom',
				closeAnimation : 'zoom',
				buttons : {
					okay : {
						text : '确定',
						btnClass : 'btn-blue'
					}
				}
			});
			return;
		}
		var lastNum = "${team.lastNum}";
		if (lastNum == 0) {
			$.alert({
				title : '提示',
				content : "您当前的激活码数量不够，请先购买",
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
		$("#userMId").val(userId);
		$("#myModal").modal('show');
	}
	
	function requestServer() {
		ajaxPost(ctx + "/user/team/release", {
			"userId" : $("#userId").val(),
			"num" : $("#num").val()
		}, function(data) {
			if (data.result == 1) {
				$.alert({
					title : '提示',
					content : "发放激活码成功~",
					animation : 'zoom',
					closeAnimation : 'zoom',
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
							action : function() {
								$("#userId").val('');
								$("#userMId").val('');
								$("#num").val('');
								loadPage(ctx + "/user/team/index");
							}
						}
					}
				});
			} else {
				$("#num").val('');
				$.alert({
					title : '提示',
					content : data.desc,
					animation : 'zoom',
					closeAnimation : 'zoom',
					backgroundDismiss: true,
					buttons : {
						okay : {
							text : '确定',
							btnClass : 'btn-blue',
						}
					}
				});
			}
		})
	}
</script>