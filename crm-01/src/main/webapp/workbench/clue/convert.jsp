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
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>


<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

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
		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//为放大镜绑定事件打开搜索市场活动窗口
		$("#OpenSearchModalBtn").click(function (){
			$.ajax({
				url:"workbench/clue/getActivityList.do",
				type:"get",
				dataType:"json",
				success:function (data){
					$("#searchActivityBtn").val("");
					var html = "";
					$.each(data,function (index,element){
						html += '<tr>'
						html += '<td><input type="radio" value="'+element.id+'" name="xz"/></td>'
						html += '<td id="'+element.id+'">'+element.name+'</td>'
						html += '<td>'+element.startDate+'</td>'
						html += '<td>'+element.endDate+'</td>'
						html += '<td>'+element.owner+'</td>'
						html += '</tr>'
					})
					$("#searchActivityBody").html(html);
					$("#searchActivityModal").modal("show");
				}
			})
		})

		//为搜索框绑定键盘单击事件，获取指定条件的activity列表
		$("#searchActivityBtn").keydown(function (even){
			if (even.keyCode ==13){
				$.ajax({
					url:"workbench/clue/getActivityListByName.do",
					type:"get",
					data:{
						name:$.trim($("#searchActivityBtn").val())
					},
					dataType:"json",
					success:function (data){
						$("#searchActivityBtn").val("");
						var html = "";
						$.each(data,function (index,element){
							html += '<tr>'
							html += '<td><input type="radio" value="'+element.id+'" name="xz"/></td>'
							html += '<td id="'+element.id+'">'+element.name+'</td>'
							html += '<td>'+element.startDate+'</td>'
							html += '<td>'+element.endDate+'</td>'
							html += '<td>'+element.owner+'</td>'
							html += '</tr>'
						})
						$("#searchActivityBody").html(html);
					}
				})
				return false;
			}
		})
		//为市场活动窗口中的提价按钮绑定事件
		$("#submitActivityBtn").click(function (){
			var id =  $("input[name=xz]:checked").val()
			var name = $("#"+id).html();
			//添加数据到数据源和隐藏域中
			$("#activityId").val(id)
			$("#ActivityName").val(name);
			$("#searchActivityBtn").val("");
			$("#searchActivityModal").modal("hide");
		})

		//转换按钮的单击事件
		$("#convertBtn").click(function (){
			if ($("#isCreateTransaction").prop("checked")){
				//当需要创建交易信息的时候发出的请求
				//采用表单的形式来提交数据（方便安全）
				$("#TranForm").submit();
			}else{
				//当不需要创建交易信息的时候发出的请求
				window.location.href="workbench/clue/convert.do?clueId=${param.id}"
			}
		})
		//
		$("#cancelBtn").click(function (){
			window.location.href="workbench/clue/detail.do?id=${param.id}"
		})





	});
</script>

</head>
<body>
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" id="searchActivityBtn" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="searchActivityBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn" >提交</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction" />
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >

		<form id="TranForm" action="workbench/clue/convert.do" method="get">
			<input type="hidden" name="clueId" value="${param.id}"/>
			<div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name" value="${param.company}">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stage}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activity">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="OpenSearchModalBtn" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="ActivityName" placeholder="点击上面搜索" readonly>
			<input type="hidden" id="activityId" />
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" id="cancelBtn" value="取消">
	</div>
</body>
</html>