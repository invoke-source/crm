<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

		pageList(1,2)

		$("#searchBtn").click(function (){
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-name").val($.trim($("#search-name").val()))
			$("#hidden-customerName").val($.trim($("#search-customerName").val()))
			$("#hidden-stage").val($.trim($("#search-stage").val()))
			$("#hidden-type").val($.trim($("#search-type").val()))
			$("#hidden-source").val($.trim($("#search-source").val()))
			$("#hidden-contactsName").val($.trim($("#search-contactsName").val()))
			pageList(1,$("#tranPage").bs_pagination('getOption', 'rowsPerPage'))
		})

		//为全选框绑定事件
		$("#qx").click(function (){
			//获取所有子框，将全选框的checked值赋值
			$("input[name=xz]").prop("checked",this.checked);
		})
		//为子框绑定事件
		$("#tranBody").on("click",$("input[name=xz]"),function (){
			/*当子框的checked的值为true的数量和子框的数量一直时，全选框的值为true，否则为false*/
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})
		
	});
	function pageList(pageNo,pageSize){
		//将全选框设置为未选中状态
		$("#qx").prop("checked",false)
		//将隐藏域中的值赋给查询框
		$("#search-owner").val($.trim($("#hidden-owner").val()))
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-customerName").val($.trim($("#hidden-customerName").val()));
		$("#search-stage").val($.trim($("#hidden-stage").val()));
		$("#search-type").val($.trim($("#hidden-type").val()));
		$("#search-source").val($.trim($("#hidden-source").val()));
		$("#search-contactsName").val($.trim($("#hidden-contactsName").val()))
		$.ajax({
			url:"workbench/transaction/pageList.do",
			data: {
				"pageNo":pageNo,
				"pageSize":pageSize,
				"owner":$("#search-owner").val(),
				"name":$("#search-name").val(),
				"customerName":$("#search-customerName").val(),
				"stage":$("#search-stage").val(),
				"type":$("#search-type").val(),
				"source":$("#search-source").val(),
				"contactsName":$("#search-contactsName").val()
			},
			type:"get",
			dataType:"json",
			success:function (data){
				//{total总记录条数,clues查询列表}
				var html = "";
				$.each(data.dataList,function (index,element){
					html += '<tr>'
					html += '<td><input type="checkbox" name="xz" value="'+element.id+'" /></td>'
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/transaction/detail.do?id='+element.id+'\';">'+element.name+'</a></td>'
					html += '<td>'+element.customerId+'</td>'
					html += '<td>'+element.stage+'</td>'
					html += '<td>'+element.type+'</td>'
					html += '<td>'+element.owner+'</td>'
					html += '<td>'+element.source+'</td>'
					html += '<td>'+element.contactsId+'</td>'
					html += '</tr>'
				})
				$("#tranBody").html(html);
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
				$("#tranPage").bs_pagination({
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
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-name">
<input type="hidden" id="hidden-customerName">
<input type="hidden" id="hidden-stage">
<input type="hidden" id="hidden-type">
<input type="hidden" id="hidden-source">
<input type="hidden" id="hidden-contactsName">
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>交易列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">客户名称</div>
				      <input class="form-control" type="text" id="search-customerName">
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">阶段</div>
					  <select class="form-control" id="search-stage">
					  	<option></option>

					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">类型</div>
					  <select class="form-control" id="search-type">
					  	<option></option>
					  	<c:forEach items="${transactionType}" var="t">
							<option value="${t.value}">${t.text}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">来源</div>
				      <select class="form-control" id="search-source">
						  <option></option>
						  <c:forEach items="${source}" var="s">
							  <option value="${s.value}">${s.text}</option>
						  </c:forEach>
						</select>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">联系人名称</div>
				      <input class="form-control" type="text" id="search-contactsName">
				    </div>
				  </div>

					<button type="button" class="btn btn-default" id="searchBtn" >查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 10px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" onclick="window.location.href='workbench/transaction/add.do';"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" onclick="window.location.href='workbench/transaction/edit.jsp';"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
							<td>客户名称</td>
							<td>阶段</td>
							<td>类型</td>
							<td>所有者</td>
							<td>来源</td>
							<td>联系人名称</td>
						</tr>
					</thead>
					<tbody id="tranBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/transaction/detail.jsp';">动力节点-交易01</a></td>
							<td>动力节点</td>
							<td>谈判/复审</td>
							<td>新业务</td>
							<td>zhangsan</td>
							<td>广告</td>
							<td>李四</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">动力节点-交易01</a></td>
                            <td>动力节点</td>
                            <td>谈判/复审</td>
                            <td>新业务</td>
                            <td>zhangsan</td>
                            <td>广告</td>
                            <td>李四</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 20px;">

				<div id="tranPage"></div>
			</div>
			
		</div>
		
	</div>
</body>
</html>