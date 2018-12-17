<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
 <link rel="stylesheet" href="${ctxCss}/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-responsive.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/style.css" type="text/css"/>
	<title>待办任务列表--chapter6</title>
	<style type="text/css">
		div.datepicker {z-index: 10000;}
	</style>

<script type="text/javascript" src="${ctxCss}/common/jquery.js"></script>
<script type="text/javascript" src="${ctxCss}/common/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctxCss}/common/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
	$(function() {
		$('.datepicker').datepicker();
	});
	</script>
</head>
<body>
<!-- /////////////////////////////////////////////////////////使用老版本 start //////////////////////////////////////////////////////////////-->
<%-- 消息部分 --%>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">${message}</div>
		<script type="text/javascript">
		setTimeout(function() {
			$('#message').hide('slow');<!-- 自动隐藏提示信息 -->
		}, 5000);
		</script>
	</c:if>
	<c:if test="${not empty error}">
		<div id="error" class="alert alert-error">${error}</div>
		<script type="text/javascript">
		setTimeout(function() {
			$('#error').hide('slow');<!-- 自动隐藏提示信息 -->
		}, 5000);
		</script>
	</c:if>

<%-- 查询部分 --%>
	<form:form id="searchForm" modelAttribute="act" action="${ctx}/act/task2/list/" method="get" class="breadcrumb form-search">
		<div>
			<label>流程类型：&nbsp;</label>
			<form:select path="procDefKey" class="input-medium">
				<form:option value="" label="全部流程"/>
				<form:options items="${fns:getDictList('act_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</form:select>
			<label>创建时间：</label>
			<input id="beginDate"  name="beginDate"  type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${act.beginDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
				　--　
			<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate" style="width:163px;"
				value="<fmt:formatDate value="${act.endDate}" pattern="yyyy-MM-dd"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});"/>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>

<sys:message content="${message}"/>

<%-- 已签收和待签收任务查询列表 --%>

	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				
				<%-- <th>任务名称</th> --%>
				<%-- <th>流程实例ID</th> --%>
				<%-- <th>流程定义ID</th> --%>
				<th>任务ID</th>
				<th>父任务ID</th>
				<th>任务委派状态</th>
				
				
				
				<th>当前环节</th>
				<th>流程名称</th>
				<th>流程版本</th>
				<th>创建时间</th>
				<th>办理人</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="act">
				<c:set var="task" value="${act.task}" />
				<c:set var="vars" value="${act.vars}" />
				<c:set var="procDef" value="${act.procDef}" />
				<c:set var="status" value="${act.status}" />
			<tr>
				<%--任务名称 --%>
				<%--<td>${task.name }</td> --%>
				<%--流程实例ID --%>
				<%--<td><a href="${ctx }/chapter13/process/trace/view/${task.executionId}" target="_blank">${task.processInstanceId }</a></td> --%>
				<%--流程定义ID --%>
				<%--<td>${task.processDefinitionId }</td> --%>
								
					<%--- 任务ID ---%>
					<td><%-- 
						<c:if test="${empty task.assignee}">
							<a href="javascript:claim('${task.id}');" title="签收任务">${fns:abbr(not empty act.vars.map.title ? act.vars.map.title : task.id, 60)}</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
							<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}">${fns:abbr(not empty vars.map.title ? vars.map.title : task.id, 60)}</a>
						</c:if>--%>
						${task.id}
					</td>
					
				<%--父任务ID --%>
					<td style="text-align:center;">
						<c:if test="${empty task.parentTaskId}">无</c:if>
						<c:if test="${not empty task.parentTaskId}">
							<a href="getform/${task.parentTaskId}">${task.parentTaskId}</a>
						</c:if>
					</td>
					
				<%--任务委派状态 --%>
				                    <td style="text-align: center">
                        <c:if test="${task.delegationState == 'PENDING' }">
                            <i class="icon-ok"></i>被委派
                        </c:if>
                        <c:if test="${task.delegationState == 'RESOLVED' }">
                            <i class="icon-ok"></i>任务已处理完成
                        </c:if>
                    </td>

					
				<%-- 当前环节  --%>
					<td>
						<a target="_blank" href="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${task.processDefinitionId}&processInstanceId=${task.processInstanceId}">${task.name}</a>
					</td>
					
					<%--流程名称 --%>
					<td>${procDef.name}</td>
					
					<%--流程版本 --%>
					<td><b title='流程版本号'>V: ${procDef.version}</b></td>
					
					<%-- 创建时间 --%>
					<td><fmt:formatDate value="${task.createTime}" type="both"/></td>
						                    
					<%--办理人 --%>
					<td>${task.assignee }</td>
					
					<%-- 操作 --%>
					<td>
						<c:if test="${empty task.assignee}">
							<a class="btn" href="${ctx}/act/task2/getform/${task.id}"><i class="icon-eye-open"></i>查看</a><%--这个主要是处理未签收改变候选人的 --%>
							<a class="btn" href="${ctx}/act/task2/claim/${task.id}"><i class="icon-ok"></i>签收</a>
						</c:if>
						<c:if test="${not empty task.assignee}">
							<a class="btn" href="${ctx}/act/task2/getform/${task.id}"><i class="icon-user"></i>办理</a>
							<a class="btn" href="${ctx}/act/task2/unclaim/${task.id}"><i class="icon-remove"></i>反签收</a>
						</c:if>
						<shiro:hasPermission name="act:process:edit">
							<c:if test="${empty task.executionId}">
								<a href="${ctx}/act/task/deleteTask?taskId=${task.id}&reason=" onclick="return promptx('删除任务','删除原因',this.href);">删除任务</a>
							</c:if>
						</shiro:hasPermission>
						<a target="_blank" href="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${task.processDefinitionId}&processInstanceId=${task.processInstanceId}">跟踪</a> 
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	</body>
</html>












<!-- /////////////////////////////////////////////////////////使用老版本 end //////////////////////////////////////////////////////////////-->


<%--
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">${message}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
		setTimeout(function() {
			$('#message').hide('slow');
		}, 5000);
		</script>
	</c:if>
	<c:if test="${not empty error}">
		<div id="error" class="alert alert-error">${error}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
		setTimeout(function() {
			$('#error').hide('slow');
		}, 5000);
		</script>
	</c:if>
    <form class="form-search" method="post">
        任务名称：<input type="text" name="taskName" value="${taskName}" class="input-medium search-query">
        <button type="submit" class="btn">查询</button>
    </form>
	<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th>任务ID</th>
				<th>任务名称</th>
				<th>流程实例ID</th>
				<th>流程定义ID</th>
				<th>父任务ID</th>
                <th>任务委派状态</th>
				<th>任务创建时间</th>
				<th>办理人</th>
				<th>操作
					<a href="#newTaskModal" data-toggle="modal" class="btn btn-primary btn-small" style="float:right;">
						<i class="icon-plus"></i>新任务</a>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.result }" var="task">
				<tr>
					<td>${task.id }</td>
					<td>${task.name }</td>
					<td><a href="${ctx }/chapter13/process/trace/view/${task.executionId}" target="_blank">${task.processInstanceId }</a></td>
					<td>${task.processDefinitionId }</td>
					<td style="text-align:center;">
						<c:if test="${empty task.parentTaskId}">无</c:if>
						<c:if test="${not empty task.parentTaskId}">
							<a href="getform/${task.parentTaskId}">${task.parentTaskId}</a>
						</c:if>
					</td>
                    <td style="text-align: center">
                        <c:if test="${task.delegationState == 'PENDING' }">
                            <i class="icon-ok"></i>被委派
                        </c:if>
                        <c:if test="${task.delegationState == 'RESOLVED' }">
                            <i class="icon-ok"></i>任务已处理完成
                        </c:if>
                    </td>
					<td>
						<fmt:formatDate value="${task.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/>
					</td>
					<td>${task.assignee }</td>
					<td>
						<c:if test="${empty task.assignee }">
							<a class="btn" href="getform/${task.id}"><i class="icon-eye-open"></i>查看</a>
							<a class="btn" href="claim/${task.id}"><i class="icon-ok"></i>签收</a>
						</c:if>
						<c:if test="${not empty task.assignee }">
							<a class="btn" href="getform/${task.id}"><i class="icon-user"></i>办理</a>
							<a class="btn" href="unclaim/${task.id}"><i class="icon-remove"></i>反签收</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
    <tags:pagination page="${page}" paginationSize="${page.pageSize}"/>

	<!-- 添加子任务对话框 -->
	<div id="newTaskModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="newTaskModalLabel" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="newTaskModalLabel">新任务</h3>
	  </div>
	  <div class="modal-body">
	    <form id="newTaskForm" action="${ctx}/chapter6/task/new" class="form-horizontal" method="post">
	    	<div class="control-group">
		    	<label class="control-label" for="subTaskName">任务名称:</label>
				<div class="controls">
					<input type="text" name="taskName" class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="description">任务描述:</label>
				<div class="controls">
					<textarea name="description" class="required"></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="priority">优先级:</label>
				<div class="controls">
					<select name="priority">
						<option value="0">低</option>
						<option value="50">中</option>
						<option value="100">高</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="dueDate">到期日:</label>
				<div class="controls">
					<input type="text" name="dueDate" data-date-format="yyyy-mm-dd" class="datepicker required" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			    	<button type="submit" class="btn btn-primary">创建</button>
			    </div>
		    </div>
	    </form>
	  </div>
	</div>
</body>
</html> --%>