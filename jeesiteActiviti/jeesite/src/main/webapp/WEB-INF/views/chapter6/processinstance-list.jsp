<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
 <link rel="stylesheet" href="${ctxCss}/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-responsive.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/style.css" type="text/css"/>
    <script type="text/javascript" src="${ctxCss}/common/jquery.js"></script>
    <style type="text/css">
        .state-btn {float: right;}
    </style>
    <title>流程实例列表--chapter14</title>
    <script type="text/javascript">
    var ctx = "${ctx}";
        $(function() {
           $('.delete-btn').click(function() {
              var reason = prompt("请输入删除原因：");
               $.post(ctx + '/act/processinstance/delete/' + $(this).data('pid'), {
                   deleteReason: reason
               }, function(resp) {
                   if (resp) {
                       location.reload();
                   } else {
                       alert('删除失败！');
                   }
               });
           });
        });
    </script>
</head>
<body>
<div class='page-title'>运行中流程实例</div>
<table width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
    <tr>
        <th>流程实例ID</th>
        <th>流程定义ID</th>
        <th>流程名称</th>
        <th>流程版本</th>
        <th>业务KEY</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="pi">
        <tr>
            <td>
                <a href="${ctx}/act/trace/view/${pi.id}" target="_blank">${pi.id}</a>
            </td>
            <td>${pi.processDefinitionId}</td>
            <td>${definitions[pi.processDefinitionId].name}</td>
            <td>${definitions[pi.processDefinitionId].version}</td>
            <td>${pi.businessKey}</td>
            <td>
                ${pi.suspended ? '挂起' : '正常'}
                <c:if test="${pi.suspended}">
                    <a class="btn btn-small state-btn" href="${ctx }/act/processinstance/active/${pi.id}"><i class="icon-ok"></i>激活</a>
                </c:if>
                <c:if test="${!pi.suspended}">
                    <a class="btn btn-small state-btn" href="${ctx }/act/processinstance/suspend/${pi.id}"><i class="icon-lock"></i>挂起</a>
                </c:if>
            </td>
            <td>
                <a class="btn btn-small btn-danger delete-btn" data-pid="${pi.processInstanceId}"><i class="icon-remove"></i>删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
</div>
</body>
</html>