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
		//定制字段
		$("#definedColumns > li").click(function(e) {
			//防止下拉菜单消失
	        e.stopPropagation();
	    });
		//为删除按钮绑定事件
		$("#deleteBtn").click(function (){
			//获取要修改记录的id值，发送请求填充模态窗口
			var $xz = $("input[name=xz]:checked");
			if ($xz.length == 0){
				alert("请选择要删除的记录");
			}else{
				var param = "";
				for (var i = 0; i < $xz.length; i++) {
					param += "id="+$($xz[i]).val();
					if (i < $xz.length-1){
						param += "&";
					}
				}
				$.ajax({
					url:"workbench/customer/deleteCustomerByid.do",
					type:"post",
					data:param,
					dataType:"json",
					success:function (data){
						if (data){
							pageList(1,$("#CustomerPage").bs_pagination('getOption', 'rowsPerPage'));
						}else{
							alert(data.msg);
						}
					}
				})
			}
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
					url:"workbench/customer/getuserListActivity.do",
					type:"get",
					data:{"id":$xz.val()},
					dataType:"json",
					success:function (data){
						var html = "<option></option>";
						$.each(data.list,function (index,element){
							html += "<option value='"+element.id+"'>"+element.name+"</option>";
						})
						$("#edit-owner").html(html)
						var id = "${user.id}"
						$("#edit-owner").val(id);

						//铺展数据
						$("#edit-name").val(data.customer.name);
						$("#edit-id").val(data.customer.id);
						$("#edit-website").val(data.customer.website);
						$("#edit-phone").val(data.customer.phone);
						$("#edit-contactSummary").val(data.customer.contactSummary);
						$("#edit-nextContactTime").val(data.customer.nextContactTime);
						$("#edit-description").val(data.customer.description);
						$("#edit-address").val(data.customer.address);
						//展开模态窗口
						$("#editCustomerModal").modal("show");
					}
				})
			}
		})
		//更新按钮的单击事件
		$("#updateBtn").click(function (){
			$.ajax({
				url: "workbench/customer/update.do",
				data:{
					"id":$.trim($("#edit-id").val()),
					"owner":$.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"website":$.trim($("#edit-website").val()),
					"phone":$.trim($("#edit-phone").val()),
					"contactSummary":$.trim($("#edit-contactSummary").val()),
					"nextContactTime":$.trim($("#edit-nextContactTime").val()),
					"description":$.trim($("#edit-description").val()),
					"address":$.trim($("#edit-address").val())

				},
				type: "post",
				dataType: "json",
				success:function (data){
					if (data.success){
						pageList($("#CustomerPage").bs_pagination('getOption', 'currentPage'),$("#CustomerPage").bs_pagination('getOption', 'rowsPerPage'));
						$("#editCustomerModal").modal("hide")
					}else{
						alert("修改失败")
					}
				}
			})
		})
		$("#addBtn").click(function (){
			$.ajax({
				url:"workbench/customer/getuserList.do",
				type:"get",
				dataType:"json",
				success:function (data){
					var html = "<option></option>";
					$.each(data,function (index,element){
						html += "<option value='"+element.id+"'>"+element.name+"</option>";
					})
					$("#create-owner").html(html);
					var id = "${user.id}"
					$("#create-owner").val(id);

				}

			})
			$("#customerAddFrom").get(0).reset();
			$("#createCustomerModal").modal("show");
		})
		//添加
		$("#saveBtn").click(function (){
			$.ajax({
				url: "workbench/customer/save.do",
				data:{
					"owner":$.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"website":$.trim($("#create-website").val()),
					"phone":$.trim($("#create-phone").val()),
					"description":$.trim($("#create-description").val()),
					"contactSummary":$.trim($("#create-contactSummary").val()),
					"nextContactTime":$.trim($("#create-nextContactTime").val()),
					"address":$.trim($("#create-address1").val())
				},
				type:"post",
				dataType:"json",
				success:function (data){
					if (data.success){
						//刷新列表
						pageList(1,$("#CustomerPage").bs_pagination('getOption', 'rowsPerPage'))
						//关闭模态窗口
						$("#createCustomerModal").modal("hide")
					}else{
						alert("添加失败")
					}
				}
			})
		})
		pageList(1,2)

		$("#searchBtn").click(function (){
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-phone").val($.trim($("#search-phone").val()))
			$("#hidden-website").val($.trim($("#search-website").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-mphone").val($.trim($("#search-mphone").val()))
			$("#hidden-state").val($.trim($("#search-state").val()))
			pageList(1,$("#CustomerPage").bs_pagination('getOption', 'rowsPerPage'))
		})
		//为全选框绑定事件
		$("#qx").click(function (){
			//获取所有子框，将全选框的checked值赋值
			$("input[name=xz]").prop("checked",this.checked);
		})
		//为子框绑定事件
		$("#customerBody").on("click",$("input[name=xz]"),function (){
			/*当子框的checked的值为true的数量和子框的数量一直时，全选框的值为true，否则为false*/
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

		
	});
	function pageList(pageNo,pageSize){
		//将全选框设置为未选中状态
		$("#qx").prop("checked",false)
		//将隐藏域中的值赋给查询框

		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-phone").val($.trim($("#hidden-phone").val()));
		$("#search-website").val($.trim($("#hidden-website").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$.ajax({
			url:"workbench/customer/pageList.do",
			data: {
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$("#search-name").val(),
				"website":$("#search-website").val(),
				"phone":$("#search-phone").val(),
				"owner":$("#search-owner").val(),
			},
			type:"get",
			dataType:"json",
			success:function (data){
				//{total总记录条数,clues查询列表}
				var html = "";
				$.each(data.dataList,function (index,element){
					html += '<tr>'
					html += '<td><input type="checkbox" name="xz" value="'+element.id+'" /></td>'
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/customer/detail.do?id='+element.id+'\';">'+element.name+'</a></td>'
					html += '<td>'+element.owner+'</td>'
					html += '<td>'+element.phone+'</td>'
					html += '<td>'+element.website+'</td>'
					html += '</tr>'
				})
				$("#customerBody").html(html);
				//总页数
				var totalPages=data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;
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
				* */
				$("#CustomerPage").bs_pagination({
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
<input type="hidden" id="hidden-phone"/>
<input type="hidden" id="hidden-website"/>
<input type="hidden" id="hidden-owner"/>
	<!-- 创建客户的模态窗口 -->
	<div class="modal fade" id="createCustomerModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="customerAddFrom">
					
						<div class="form-group">
							<label for="create-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner" >

								</select>
							</div>
							<label for="create-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-name">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="create-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-website">
                            </div>
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
						</div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control time" id="create-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address1" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address1"></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!--修改客户的模态窗口 -->
	<div class="modal fade" id="editCustomerModal" role="dialog">
		<input type="hidden" id="edit-id">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">修改客户</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-customerOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">

								</select>
							</div>
							<label for="edit-customerName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
                            <label for="edit-website" class="col-sm-2 control-label">公司网站</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
                            </div>
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>

                        <div style="position: relative;top: 15px;">
                            <div class="form-group">
                                <label for="create-contactSummary1" class="col-sm-2 control-label">联系纪要</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="create-nextContactTime2" class="col-sm-2 control-label">下次联系时间</label>
                                <div class="col-sm-10" style="width: 300px;">
                                    <input type="text" class="form-control" id="edit-nextContactTime">
                                </div>
                            </div>
                        </div>

                        <div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address">北京大兴大族企业湾</textarea>
                                </div>
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
				<h3>客户列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon" >名称</div>
				      <input class="form-control" id="search-name" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" id="search-owner" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" id="search-phone" type="text">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司网站</div>
				      <input class="form-control" id="search-website" type="text">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
							<td>所有者</td>
							<td>公司座机</td>
							<td>公司网站</td>
						</tr>
					</thead>
					<tbody id="customerBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/customer/detail.jsp';">动力节点</a></td>
							<td>zhangsan</td>
							<td>010-84846003</td>
							<td>http://www.bjpowernode.com</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/customer/detail.jsp';">动力节点</a></td>
                            <td>zhangsan</td>
                            <td>010-84846003</td>
                            <td>http://www.bjpowernode.com</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			<div style="height: 50px; position: relative;top: 60px;">
				<div id="CustomerPage"></div>
			</div>
		</div>
	</div>
</body>
</html>