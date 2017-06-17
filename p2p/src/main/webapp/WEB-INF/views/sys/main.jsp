<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<title>${fns:getConfig('productName')}-首页</title>
</head>
<body class="hold-transition skin-blue sidebar-mini sidebar-collapse">

	<div class="wrapper">

		<header class="main-header"> 

			<!-- Logo -->
			<a href="${ctx}/main" class="logo"> <span class="logo-mini">${fns:getConfig('engName')}</span>
				<span class="logo-lg">${fns:getConfig('shortName')}</span>
			</a>
			<nav class="navbar navbar-static-top">
				<a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button"> <span class="sr-only"></span></a>

				<div class="navbar-custom-menu">
				
					<ul class="nav navbar-nav">
						<li class="user user-menu">
							<a href='#'><span id="sysTime" class="logo-mini"></span></a>
						</li>
						
						<c:if test="${user.userRole == 1}">
							<li class="messages-menu">
								<a data-url="${ctx}/user/message/index"  > 
									<i class="fa fa-envelope-o"></i> <span class="label label-success">${notice.personMsgNum}</span>
								</a>
							</li>
							<li class="notifications-menu">
								<a data-url="${ctx}/notice/u/index" > 
									<i class="fa fa-bell-o"></i> <span class="label label-warning">${notice.sysMsgNum}</span>
								</a>
							</li>
						</c:if>
						
						<li class="user user-menu">
							<a href="${ctx}/logout" class="dropdown-toggle" ><span class="fa">退出登录</span> </a>
						</li>
					</ul>
				</div>
			</nav>
		</header>
		<aside class="main-sidebar">
			<section class="sidebar">
				<div class="user-panel">
					<div class="pull-left image">
						<!-- 判断性别 -->
						<img src="${ctxStatic}/adminlte/dist/img/avatar5.png"
							class="img-circle" alt="User Image">
					</div>
					<div class="pull-left info">
						<p>${user.realname}</p>
						<a href="#"><i class="fa fa-circle text-success"></i>${user.phone}</a>
					</div>
				</div>
				<ul class="nav nav-list">
					<hr>
				</ul>
				<ul class="sidebar-menu">
				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>

		<div class="content-wrapper" id="mainDiv"></div>

		<footer class="main-footer text-center">
			<strong>Copyright &copy; 2017 <a href="#">金融平台</a></strong> All rights reserved.
		</footer>

	</div>

	<!-- 加入页面的的脚本 -->
	<script>
		//加载菜单
		ajaxPost('${ctx}/main/menu', null,
				function(data) {
					var $li, $menu_f_ul;
					$("ul.sidebar-menu").empty();
					$.each(data.data, function(index, item) {
										if (item.menuPid == 0) {
											$li = $('<li class="treeview"></li>');
											var $menu_f = $('<a href="#" data-url="${ctx}'+item.menuUrl+'">\n'
													+ '<i class="'+item.menuIcon+'"></i> <span>' + item.menuName + '</span>\n'
													+ ' <span class="pull-right-container">\n'
													+ '</span></a>');
											$li.append($menu_f);
											$menu_f_ul = $('<ul class="treeview-menu"></ul>');
											$li.append($menu_f_ul);
											$("ul.sidebar-menu").append($li);
										} else {
											$menu_s = $('<li><a href="#" data-url="${ctx}'+item.menuUrl+'"><i class="'+item.menuName+'"></i>'
													+ item.menuName
													+ '</a></li>');
											$menu_f_ul.append($menu_s);
										}
									});
				});

		$(function() {
			//首页默认加载
			loadPage("${ctx}/main/homePage");
			$("a[data-url]").click(function(evt) {
				loadPage($(this).data("url"));
				$("ul.treeview-menu li").removeClass("active");
				$(this).parent().addClass("active");
			});


			if (isPC()) {
				showTime();
			}
			
		});

		function showTime() {
			var lab = $("#sysTime"); //这是获取显示时间的label
			var date = new Date();
			var year = date.getFullYear();
			var month = date.getMonth() + 1; //月从0开始计数，所以要加1
			var day = date.getDate(); //date.getDay()是获得星期几，getDate()才是日期
			var hour = date.getHours();
			if (hour < 10)
				hour = "0" + hour;
			var min = date.getMinutes();
			if (min < 10)
				min = "0" + min;
			var sec = date.getSeconds();
			if (sec < 10)
				sec = "0" + sec;
			var week = date.getDay();
			switch (week) {
			case 0:
				week = "日";
				break;
			case 1:
				week = "一";
				break;
			case 2:
				week = "二";
				break;
			case 3:
				week = "三";
				break;
			case 4:
				week = "四";
				break;
			case 5:
				week = "五";
				break;
			case 6:
				week = "六";
				break;
			}
			$("#sysTime").html(year + "年" + month + "月" + day + "日   星期" + week + " " + hour + ":" + min + ":" + sec);
			setTimeout('showTime()', 1000); //页面自动调用函数，每隔1000ms调用showTime()函数
		}
		
		function isPC() {
		    var userAgentInfo = navigator.userAgent;
		    var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
		    var flag = true;
		    for (var v = 0; v < Agents.length; v++) {
		        if (userAgentInfo.indexOf(Agents[v]) > 0) {
		            flag = false;
		            break;
		        }
		    }
		    return flag;
		}
	</script>
</body>
</html>
