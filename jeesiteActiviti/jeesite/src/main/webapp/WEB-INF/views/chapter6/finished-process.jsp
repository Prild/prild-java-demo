<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-responsive.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/style.css" type="text/css"/>
    <title>已结束流程实例列表--chapter13</title>
    <style type="text/css">
        div.datepicker {z-index: 10000;}
    </style>

    <script type="text/javascript" src="${ctxCss}/common/jquery.js"></script>
    <script type="text/javascript" src="${ctxCss}/common/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctxCss}/common/bootstrap-datepicker.js"></script>
</head>
<body>
<div class='page-title ui-corner-all'>已归档流程实例</div>
<table width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
    <tr>
        <th>流程实例ID</th>
        <th>所属流程</th>
        <th>流程定义ID</th>
        <th>启动时间</th>
        <th>流程启动人</th>
        <th>结束时间</th>
        <th>父流程ID</th>
        <th>结束(删除)原因</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="hp">
        <tr>
            <td><a href="${ctx}/act/history/finished/view/${hp.processInstanceId}">${hp.processInstanceId}</a></td>
            <td>${definitions[hp.processDefinitionId].name}</td>
            <td>${hp.processDefinitionId}</td>
            <td><fmt:formatDate value="${hp.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <td>${hp.startUserId}</td>
            <td><fmt:formatDate value="${hp.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
             <td>
            	<c:if test="${empty hp.superProcessInstanceId}">
            			无
				</c:if>
				<c:if test="${not empty hp.superProcessInstanceId}">
				<a href="${ctx}/act/history/finished/view/${hp.superProcessInstanceId}">${hp.superProcessInstanceId}</a>
				</c:if>
			</td>
            
            <td>
            			<c:if test="${empty hp.deleteReason}">
            			正常结束
						</c:if>
						<c:if test="${not empty hp.deleteReason}">
						${hp.deleteReason}
						</c:if>
			</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
</div>
</body>
</html>