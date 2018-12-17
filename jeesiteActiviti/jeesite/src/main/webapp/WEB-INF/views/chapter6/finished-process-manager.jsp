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
    <script type="text/javascript">
    var ctx = "${ctx}";
        $(function() {
            $('.delete-btn').click(function() {
                if (confirm('确定删除先关的流程数据？')) {
                    location.href = ctx + '/act/history/finished/delete/' + $(this).data('pid');<%--/act/history/finished/view/ --%>
                }
            });
        });
    </script>
</head>
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
        <th>结束原因</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="hp">
        <tr>
            <td><a href="${ctx}/act/history/finished/view/${hp.processInstanceId}">${hp.processInstanceId}</a></td> <%--/act/history/finished/view/ --%>
            <td>${definitions[hp.processDefinitionId].name}</td>
            <td>${hp.processDefinitionId}</td>
            <td><fmt:formatDate value="${hp.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <td>${hp.startUserId}</td>
            <td><fmt:formatDate value="${hp.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <td><a href="${ctx}/act/history/finished/view/${hp.superProcessInstanceId}">${hp.superProcessInstanceId}</a></td>
            <td>${hp.deleteReason}</td>
            <td>
                <a class="btn btn-small btn-danger delete-btn" data-pid="${hp.processInstanceId}"><i class="icon-remove"></i>删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
</div>
</body>
</html>