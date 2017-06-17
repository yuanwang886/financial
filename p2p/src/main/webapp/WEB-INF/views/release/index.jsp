<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<section class="content-header">
	<h1>
		<span>激活码购买列表</span>
	</h1>
	<ol class="breadcrumb">
		<li><a href="${ctx}/main"><i class="fa fa-dashboard"></i> 首页</a></li>
		<li class="active">激活码购买列表</li>
	</ol>
</section>
<section class="content">
	<div class="row">
		<div class="col-xs-12">
			<div class="box box-warning">
				<!-- /.box-header -->

				<form id="feedback-form" name="feedback-form"
					class="form-horizontal">

					<div class="box-body">
						<div class="col-md-6">
							<div class="form-group">
								<label for="birthday" class="col-sm-3 control-label">开始日期</label>
								<div class="input-group col-sm-8">
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span> 
									<input type="text" class="form-control" data-flag="datepicker" data-format="yyyy-MM-dd" name="startDate" id="startDate">
								</div>
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label for="birthday" class="col-sm-3 control-label">结束日期</label>
								<div class="input-group col-sm-8">
									<span class="input-group-addon"><i class="fa fa-calendar"></i></span> 
									<input type="text" class="form-control" data-flag="datepicker" data-format="yyyy-MM-dd" name="endDate" id="endDate">
								</div>
							</div>
						</div>
					</div>
					<div class="box-content text-center">
						<!--以下两种方式提交验证,根据所需选择-->
						<button type="submit" class="btn btn-primary" data-btn-type="save" data-dismiss="modal">查询</button>
						<button type="reset" class="btn btn-default" data-btn-type="cancel">重置</button>
						<button type="button" id="publishBtn" class="btn btn-success" data-target="#myModel" data-toggle="modal">购买</button>
					</div>

					<div class="box-body">
						<table id="feedback_table"
							class="table table-bordered table-striped">
							<thead>
								<tr>
									<th>姓名</th>
									<th>手机号</th>
									<th>购买个数</th>
									<th>购买日期</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</form>

				<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content"></div>
					</div>
				</div>

			</div>
		</div>
	</div>
</section>
<script>
	$(function() {
		$('#publishBtn').click(function() {
			$("#myModal").modal({
				remote : ctx + "/release/add"
			});
		});

		$("#myModal").on("hidden.bs.modal", function() {
			$(this).removeData("bs.modal");
			$(this).find(".modal-content").children().remove();
		});

		$("#startDate").datepicker({
			language : "zh-CN",
			autoclose : true,//选中之后自动隐藏日期选择框
			clearBtn : true,//清除按钮
			format : "yyyy-mm-dd"//日期格式
		});

		$("#endDate").datepicker({
			language : "zh-CN",
			autoclose : true,//选中之后自动隐藏日期选择框
			clearBtn : true,//清除按钮
			format : "yyyy-mm-dd"//日期格式
		});

		$("#feedback-form").bootstrapValidator({
			submitHandler : function(valiadtor, loginForm, submitButton) {
				loadTableData();
			}
		});

		loadTableData();
	});

	function loadTableData() {
		$('#feedback_table')
				.DataTable(
						{
							"bPaginate" : true,//分页工具条显示  
							"sPaginationType" : "full_numbers",//分页工具条样式  
							"bStateSave" : true, //是否打开客户端状态记录功能,此功能在ajax刷新纪录的时候不会将个性化设定回复为初始化状态    
							"bScrollCollapse" : true, //当显示的数据不足以支撑表格的默认的高度  
							"bLengthChange" : true, //每页显示的记录数  
							"aLengthMenu" : [10, 20, 50],//每页显示的条数选项
							"bFilter" : false, //搜索栏  
							"bSort" : false, //是否支持排序功能  
							"bInfo" : true, //显示表格信息  
							"bAutoWidth" : true, //自适应宽度  
							"bJQueryUI" : false, //是否开启主题  
							"bDestroy" : true,
							"bProcessing" : true, //开启读取服务器数据时显示正在加载中……特别是大数据量的时候，开启此功能比较好  
							"bServerSide" : true, //服务器处理分页，默认是false，需要服务器处理，必须true  
							"sAjaxDataProp" : "data", //是服务器分页的标志，必须有   
							"sAjaxSource" : ctx + "/release/list", //这是要请求json数据的url
							"fnServerParams" : function(aoData) {
								aoData.push({
									"name" : "startDate",
									"value" : $('#startDate').val()
								}, {
									"name" : "endDate",
									"value" : $('#endDate').val()
								});
							},
							"oLanguage" : {//语言设置  
								//"sProcessing" : "处理中...",
								"sProcessing" : "<img src='" + ctxStatic + "/common/images/loading.gif' />",
								"sLengthMenu" : "显示 _MENU_ 项结果",
								"sZeroRecords" : "没有匹配结果",
								"sInfo" : "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
								"sInfoEmpty" : "显示第 0 至 0 项结果，共 0 项",
								"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
								"sInfoPostFix" : "",
								"sSearch" : "搜索:",
								"sUrl" : "",
								"sEmptyTable" : "表中数据为空",
								"sLoadingRecords" : "载入中...",
								"sInfoThousands" : ",",
								"oPaginate" : {
									"sFirst" : "首页",
									"sPrevious" : "上页",
									"sNext" : "下页",
									"sLast" : "末页"
								}
							},
							"aoColumns" : [
									{
										"mDataProp" : "realName",
										"sDefaultContent" : ""
									},
									{
										"mDataProp" : "phone",
										"sDefaultContent" : ""
									},
									{
										"mDataProp" : "num"
									},
									{
										"mDataProp" : "createTime"
									}
							]
						});
	}

</script>