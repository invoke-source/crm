<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String basePath = request.getScheme() + "://" +
		request.getServerName() + ":" + request.getServerPort() +
		request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>"/>
	<meta charset="UTF-8">

	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
	<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


	<script type="text/javascript">
		$(function(){
			/*时间控件*/
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});
			/*发起ajax请求，获取下拉列表框的值并填充*/
			$("#addModalBtn").click(function (){
				$.ajax({
					url:"workbench/activity/getuserList.do",
					type:"get",
					dataType:"json",
					success:function (data){
						 var html ="<option ></option>";
						 var id = "${user.id}";
						$.each(data,function (index,element){
							html+="<option id='"+element.id+"'>"+element.name+"</option>"
						})

						$("#create-Owner").html(html)
						$("#"+id).attr("selected",true)
					}
				})
				$("#createActivityModal").modal("show");
			})
			/*保存用户信息*/

			$("#saveBtn").click(function (){
				$.ajax({
					url: "workbench/activity/save.do",
					data:{
						"owner":$.trim($("#create-Owner option:selected").attr("id")),
						"name":$.trim($("#create-name").val()),
						"startDate":$.trim($("#create-startDate").val()),
						"endDate":$.trim($("#create-endDate").val()),
						"cost":$.trim($("#create-cost").val()),
						"description":$.trim($("#create-description").val())
					},
					type: "post",
					dataType: "json",
					success:function (data){
						if (data.success){
							/*
							*
							* $("#activityPage").bs_pagination('getOption', 'currentPage'):
							* 		操作后停留在当前页
							*
							* $("#activityPage").bs_pagination('getOption', 'rowsPerPage')
							* 		操作后维持已经设置好的每页展现的记录数
							*
							* 这两个参数不需要我们进行任何的修改操作
							* 	直接使用即可
							*
							*
							*
							* */
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							/*关闭模态窗口时清空表单中的数据
							* jQuery中没有提供reset方法，使用原生js*/
							$("#activityAddFrom").get(0).reset();
							$("#createActivityModal").modal("hide")
						}else{
							alert("保存失败："+data.success)
						}
					}
				})
			})
			//为修改按钮绑定事件
			$("#editBtn").click(function (){
				//获取要修改记录的id值，发送请求填充模态窗口
				var $xz = $("input[name=xz]:checked");
				if ($xz.length == 0){
					alert("请选择要修改的记录");
				}else if ($xz.length >1){
					alert("只能修改单条记录，请重新选择");
				}else{
					$.ajax({
						url:"workbench/activity/getuserListActivity.do",
						type:"get",
						data:{"id":$xz.val()},
						dataType:"json",
						success:function (data){
							//"success":activity,list:{user,user1,user2}
							var html = "<option></option>";
							$.each(data.list,function (index,element){
								html += "<option value='"+element.id+"'>"+element.name+"</option>";
							})
							$("#edit-owner").html(html)
							//铺展数据
							$("#edit-id").val(data.activity.id);
							$("#edit-owner").val(data.activity.owner);
							$("#edit-name").val(data.activity.name);
							$("#edit-startDate").val(data.activity.startDate);
							$("#edit-endDate").val(data.activity.endDate);
							$("#edit-cost").val(data.activity.cost);
							$("#edit-description").val(data.activity.description);
							//展开模态窗口
							$("#editActivityModal").modal("show");
						}
					})
				}
			})

			//更新按钮的单击事件
			$("#updateBtn").click(function (){
				$.ajax({
					url: "workbench/activity/update.do",
					data:{
						"id":$.trim($("#edit-id").val()),
						"owner":$.trim($("#edit-name").val()),
						"name":$.trim($("#edit-name").val()),
						"startDate":$.trim($("#edit-startDate").val()),
						"endDate":$.trim($("#edit--endDate").val()),
						"cost":$.trim($("#edit-cost").val()),
						"description":$.trim($("#edit-description").val())
					},
					type: "post",
					dataType: "json",
					success:function (data){
						if (data.success){
							pageList($("#activityPage").bs_pagination('getOption', 'currentPage'),$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							$("#editActivityModal").modal("hide")
						}else{
							alert(data.msg)
						}
					}
				})



			})
			pageList(1,2)
			$("#searchBtn").click(function (){
				/*点击查询时将条件赋值到隐藏域中*/
				$("#hidden-name").val($.trim($("#search-name").val()))
				$("#hidden-owner").val($.trim($("#search-owner").val()))
				$("#hidden-startDate").val($.trim($("#search-startDate").val()))
				$("#hidden-endDate").val($.trim($("#search-endDate").val()))
				pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
			})
			/*为全选框绑定事件*/
			$("#qx").click(function (){
				$("input[name=xz]").prop("checked",this.checked);
			})
			/*为全选框的所有子框绑定事件*/
			$("#activityBody").on("click",$("input[name=xz]"),function (){
				/*当子框的checked的值为true的数量和子框的数量一直时，全选框的值为true，否则为false*/
				$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
			})
			/*删除按钮的单击事件*/
			$("#deleteBtn").click(function (){
					//获取选中的文本框
					var $xz = $("input[name=xz]:checked")
					if ($xz.length == 0){
						alert("请选中要删除的记录")
					}else if(confirm("确认要删除选中的记录吗？")){
							//拼接data请求参数
							var param = "";
							for (var i = 0; i<$xz.length; i++){
								param += "id="+$($xz[i]).val();
								if (i <$xz.length-1){
									param +="&";
								}
							}
							//将要删除id编号发送到后台
							$.ajax({
								url:"workbench/activity/delete.do",
								type:"post",
								data:param,
								dataType:"json",
								success:function (data){
									if (data.success){
										pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'))
									}else{
										alert("删除失败")
									}
								}
							})
					}
			})
		})



		/*从数据库中获取数据，添加到活动列表中*/
		function pageList(pageNo,pageSize){
			//将全选框设置为未选中状态
			$("#qx").prop("checked",false)
			//查询前，将隐藏域中的信息保存的信息取出来，重新赋予到搜索框之中
			$("#search-name").val($.trim($("#hidden-name").val()))
			$("#search-owner").val($.trim($("#hidden-owner").val()))
			$("#search-startDate").val($.trim($("#hidden-startDate").val()))
			$("#search-endDate").val($.trim($("#hidden-endDate").val()))
			// alert("展现市场活动列表");
			$.ajax({
				url:"workbench/activity/pageList.do",
				type:"get",
				data: {
					"pageNo":pageNo,
					"pageSize":pageSize,
					"name":$.trim($("#search-name").val()),
					"owner":$.trim($("#search-owner").val()),
					"startDate":$.trim($("#search-startDate").val()),
					"endDate":$.trim($("#search-endDate").val())
				},
				dataType:"json",
				success:function (data){
					/*返回值data中包含两个数据，total表示总记录条数，activitys表示返回的activity对象的数组*/
					var html = "";
					$.each(data.dataList,function (index,element){
						html+='<tr class="active">'
						html+='<td><input name="xz" type="checkbox" value="'+element.id+'"/></td>'
						html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+element.id+'\';">'+element.name+'</a></td>'
						html+='<td>'+element.owner+'</td>'
						html+='<td>'+element.startDate+'</td>'
						html+='<td>'+element.endDate+'</td>'
						html+='</tr>'
					})
					$("#activityBody").html(html);
					//总页数
					var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
					$("#activityPage").bs_pagination({
						currentPage: pageNo, // 页码
						rowsPerPage: pageSize, // 每页显示的记录条数
						maxRowsPerPage: 20, // 每页最多显示的记录条数
						totalPages: totalPages, // 总页数
						totalRows: data.total, // 总记录条数
						visiblePageLinks: 3, // 显示几个卡片
						showGoToPage: true,
						showRowsPerPage: true,
						showRowsInfo: true,
						showRowsDefaultInfo: true,
						/*该回调函数是在点击分页组件时候触发的*/
						onChangePage : function(event, data){
							pageList(data.currentPage , data.rowsPerPage);
						}
					});

				}
			})
		}
	</script>
</head>
<body>
<input type="hidden" id="hidden-name"/>
<input type="hidden" id="hidden-owner"/>
<input type="hidden" id="hidden-startDate"/>
<input type="hidden" id="hidden-endDate"/>

<!-- 创建市场活动的模态窗口 -->
<div class="modal fade" id="createActivityModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 85%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" role="form" id="activityAddFrom">

					<div class="form-group">
						<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="create-Owner">

							</select>
						</div>
						<label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="create-name">
						</div>
					</div>

					<div class="form-group">
						<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control time" id="create-startDate" readonly>
						</div>
						<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control time" id="create-endDate" readonly>
						</div>
					</div>
					<div class="form-group">

						<label for="create-cost" class="col-sm-2 control-label">成本</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="create-cost">
						</div>
					</div>
					<div class="form-group">
						<label for="create-describe" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-10" style="width: 81%;">
							<textarea class="form-control" rows="3" id="create-description"></textarea>
						</div>
					</div>

				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
			</div>
		</div>
	</div>
</div>

<!-- 修改市场活动的模态窗口 -->
<div class="modal fade" id="editActivityModal" role="dialog">
	<div class="modal-dialog" role="document" style="width: 85%;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" role="form">

					<div class="form-group">
						<input type="hidden" id="edit-id"/>
						<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<select class="form-control" id="edit-owner">

							</select>
						</div>
						<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-name" >
						</div>
					</div>

					<div class="form-group">
						<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-startDate">
						</div>
						<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-endDate" >
						</div>
					</div>

					<div class="form-group">
						<label for="edit-cost" class="col-sm-2 control-label">成本</label>
						<div class="col-sm-10" style="width: 300px;">
							<input type="text" class="form-control" id="edit-cost" >
						</div>
					</div>

					<div class="form-group">
						<label for="edit-describe" class="col-sm-2 control-label">描述</label>
						<div class="col-sm-10" style="width: 81%;">
							<textarea class="form-control" rows="3" id="edit-description"></textarea>
						</div>
					</div>

				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
			</div>
		</div>
	</div>
</div>




<div>
	<div style="position: relative; left: 10px; top: -10px;">
		<div class="page-header">
			<h3>市场活动列表</h3>
		</div>
	</div>
</div>
<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

		<div class="btn-toolbar" role="toolbar" style="height: 80px;">
			<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">名称</div>
						<input class="form-control" type="text" id="search-name">
					</div>
				</div>

				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">所有者</div>
						<input class="form-control" type="text" id="search-owner">
					</div>
				</div>


				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">开始日期</div>
						<input class="form-control" type="text" id="search-startDate" />
					</div>
				</div>
				<div class="form-group">
					<div class="input-group">
						<div class="input-group-addon">结束日期</div>
						<input class="form-control" type="text" id="search-endDate">
					</div>
				</div>

				<button type="button" class="btn btn-default" id="searchBtn">查询</button>

			</form>
		</div>
		<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
			<div class="btn-group" style="position: relative; top: 18%;">
				<button type="button" class="btn btn-primary" id="addModalBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				<button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				<button type="button" class="btn btn-danger"  id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
			</div>

		</div>
		<div style="position: relative;top: 10px;">
			<table class="table table-hover">
				<thead>
				<tr style="color: #B3B3B3;">
					<td><input type="checkbox" id="qx" /></td>
					<td>名称</td>
					<td>所有者</td>
					<td>开始日期</td>
					<td>结束日期</td>
				</tr>
				</thead>
				<tbody id="activityBody">
				<%--<tr class="active">
					<td><input type="checkbox" /></td>
					<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
					<td>zhangsan</td>
					<td>2020-10-10</td>
					<td>2020-10-20</td>
				</tr>
				<tr class="active">
					<td><input type="checkbox" /></td>
					<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
					<td>zhangsan</td>
					<td>2020-10-10</td>
					<td>2020-10-20</td>
				</tr>--%>
				</tbody>
			</table>
		</div>

		<div style="height: 50px; position: relative;top: 30px;">

			<div id="activityPage"></div>

		</div>

	</div>

</div>
</body>
</html>