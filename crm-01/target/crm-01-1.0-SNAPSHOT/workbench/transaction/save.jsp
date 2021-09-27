<%@ page import="java.util.Map" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String basePath = request.getScheme() + "://" +
request.getServerName() + ":" + request.getServerPort() +
request.getContextPath() + "/";
 Map<String,String> pMap = (Map<String, String>) application.getAttribute("pmap");
 Set<String> set =  pMap.keySet();
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>"/>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-mast
er/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>

</head>
<script type="text/javascript">
	$(function (){

		var json = {
			<%
				for (String key : set) {
					String value = pMap.get(key);
			%>
				"<%=key%>":<%=value%>,
			<%
				}
			%>
		};



		/*时间控件*/
		$(".time1").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});
		/*时间控件*/
		$(".time2").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});
		$("#create-customerName").typeahead({
			source: function (query, process) {
				$.get(
						"workbench/transaction/getCustomerName.do",
						{ "name" : query },
						function (data) {
							//alert(data);
							process(data);
						},
						"json"
				);
			},
			delay: 1500
		});

		//市场活动
		//为放大镜绑定事件打开搜索市场活动窗口
		$("#OpenSearchActivityBtn").click(function (){
			$.ajax({
				url:"workbench/transaction/getActivityList.do",
				type:"get",
				dataType:"json",
				success:function (data){
					$("#searchActivityBtn").val("");
					var html = "";
					$.each(data,function (index,element){
						html += '<tr>'
						html += '<td><input type="radio" value="'+element.id+'" name="axz"/></td>'
						html += '<td id="'+element.id+'">'+element.name+'</td>'
						html += '<td>'+element.startDate+'</td>'
						html += '<td>'+element.endDate+'</td>'
						html += '<td>'+element.owner+'</td>'
						html += '</tr>'
					})
					$("#searchActivityBody").html(html);
					$("#findMarketActivity").modal("show");
				}
			})
		})

		//为搜索框绑定键盘单击事件，获取指定条件的activity列表
		$("#searchActivityBtn").keydown(function (even){
			if (even.keyCode ==13){
				$.ajax({
					url:"workbench/transaction/getActivityListByName.do",
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
							html += '<td><input type="radio" value="'+element.id+'" name="axz"/></td>'
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
			var id =  $("input[name=axz]:checked").val()
			var name = $("#"+id).html();
			//添加数据到数据源和隐藏域中
			$("#activityId").val(id)
			$("#create-ActivityName").val(name);
			$("#searchActivityBtn").val("");
			$("#findMarketActivity").modal("hide");
		})

		//联系人
		//为放大镜绑定事件打开搜索联系人窗口
		$("#OpenSearchContactsNameBtn").click(function (){
			$.ajax({
				url:"workbench/transaction/getContactsList.do",
				type:"get",
				dataType:"json",
				success:function (data){
					$("#searchContactsBtn").val("");
					var html = "";
					$.each(data,function (index,element){
						html += '<tr>'
						html += '<td><input type="radio" value="'+element.id+'" name="cxz"/></td>'
						html += '<td id="'+element.id+'">'+element.fullname+'</td>'
						html += '<td>'+element.email+'</td>'
						html += '<td>'+element.mphone+'</td>'
						html += '</tr>'
					})
					$("#searchContactsBody").html(html);
					$("#findContacts").modal("show");
				}
			})
		})

		//为搜索框绑定键盘单击事件，获取指定条件的Contacts列表
		$("#searchContactsBtn").keydown(function (even){
			if (even.keyCode ==13){
				$.ajax({
					url:"workbench/transaction/getContactsListByName.do",
					type:"get",
					data:{
						name:$.trim($("#searchContactsBtn").val())
					},
					dataType:"json",
					success:function (data){
						$("#searchContactsBtn").val("");
						var html = "";
						$.each(data,function (index,element){
							html += '<tr>'
							html += '<td><input type="radio" value="'+element.id+'" name="cxz"/></td>'
							html += '<td id="'+element.id+'">'+element.fullname+'</td>'
							html += '<td>'+element.email+'</td>'
							html += '<td>'+element.mphone+'</td>'
							html += '</tr>'
						})
						$("#searchContactsBody").html(html);
					}
				})
				return false;
			}
		})
		//为市场活动窗口中的提价按钮绑定事件
		$("#submitContactBtn").click(function (){
			var id =  $("input[name=cxz]:checked").val()
			var name = $("#"+id).html();
			//添加数据到数据源和隐藏域中
			$("#contactsId").val(id)
			$("#create-fullname").val(name);
			$("#searchContactsBtn").val("");
			$("#findContacts").modal("hide");
		})

		//为阶段的下拉框绑定change事件
		$("#create-stage").change(function (){
			var stage = $("#create-stage").val()
			var possibility = json[stage];
			$("#create-possibility").val(possibility);
		})

		//交易添加
		$("#saveBtn").click(function (){
			$("#fromTran").submit();
		})



	})
</script>
<body>

	<!-- 查找市场活动 -->	
	<div class="modal fade" id="findMarketActivity" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找市场活动</h4>
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
					<table id="activityTable3" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
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
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary" id="submitActivityBtn" >提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 查找联系人 -->	
	<div class="modal fade" id="findContacts" role="dialog">
		<div class="modal-dialog" role="document" style="width: 80%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">查找联系人</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" class="form-control" style="width: 300px;" placeholder="请输入联系人名称，支持模糊查询" id="searchContactsBtn">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>邮箱</td>
								<td>手机</td>
							</tr>
						</thead>
						<tbody id="searchContactsBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>李四</td>
								<td>lisi@bjpowernode.com</td>
								<td>12345678901</td>
							</tr>--%>
						</tbody>
					</table>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary" id="submitContactBtn" >提交</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	
	<div style="position:  relative; left: 30px;">
		<h3>创建交易</h3>
	  	<div style="position: relative; top: -40px; left: 70%;">
			<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
			<button type="button" class="btn btn-default">取消</button>
		</div>
		<hr style="position: relative; top: -40px;">
	</div>
	<form class="form-horizontal" action="workbench/transaction/saveTran.do" id="fromTran" method="post" role="form" style="position: relative; top: -30px;">
		<div class="form-group">
			<label for="create-transactionOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionOwner" name="owner">
				  <option></option>
					<c:forEach items="${ulist}" var="u">
						<option value="${u.id}"${user.id eq u.id ? "selected" : ""}>${u.name}</option>
					</c:forEach>

				</select>
			</div>
			<label for="create-amountOfMoney" class="col-sm-2 control-label">金额</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-amountOfMoney" name="money">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-transactionName" name="name">
			</div>
			<label for="create-expectedClosingDate" class="col-sm-2 control-label">预计成交日期<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time1" id="create-expectedClosingDate" name="expectedDate">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-accountName" class="col-sm-2 control-label">客户名称<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-customerName" name="customerName" placeholder="支持自动补全，输入客户不存在则新建">
			</div>
			<label for="create-transactionStage" class="col-sm-2 control-label">阶段<span style="font-size: 15px; color: red;">*</span></label>
			<div class="col-sm-10" style="width: 300px;">
			  <select class="form-control" id="create-stage" name="stage">
			  	<option></option>
			  	<c:forEach items="${stage}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
			  </select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-transactionType" class="col-sm-2 control-label">类型</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-transactionType" name="type">
				  <option></option>
				  <c:forEach items="${transactionType}" var="t">
					  <option value="${t.value}">${t.text}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-possibility" class="col-sm-2 control-label">可能性</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-possibility">
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-clueSource" class="col-sm-2 control-label">来源</label>
			<div class="col-sm-10" style="width: 300px;">
				<select class="form-control" id="create-clueSource" name="source">
				  <option></option>
				  <c:forEach items="${source}" var="s">
					  <option value="${s.value}">${s.text}</option>
				  </c:forEach>
				</select>
			</div>
			<label for="create-activitySrc" class="col-sm-2 control-label">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" <%--data-toggle="modal" data-target="#findMarketActivity"--%>><span class="glyphicon glyphicon-search" id="OpenSearchActivityBtn"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-ActivityName">
				<input type="hidden" id="activityId" name="activityId" />
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactsName" class="col-sm-2 control-label">联系人名称&nbsp;&nbsp;<a href="javascript:void(0);" <%--data-toggle="modal" data-target="#findContacts"--%>><span class="glyphicon glyphicon-search" id="OpenSearchContactsNameBtn"></span></a></label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control" id="create-fullname">
				<input type="hidden" id="contactsId" name="contactsId" />
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-describe" class="col-sm-2 control-label">描述</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-describe" name="description"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
			<div class="col-sm-10" style="width: 70%;">
				<textarea class="form-control" rows="3" id="create-contactSummary" name="contactSummary"></textarea>
			</div>
		</div>
		
		<div class="form-group">
			<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
			<div class="col-sm-10" style="width: 300px;">
				<input type="text" class="form-control time2" id="create-nextContactTime" name="nextContactTime">
			</div>
		</div>
		
	</form>
</body>
</html>