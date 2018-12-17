<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
 <link rel="stylesheet" href="${ctxCss}/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-responsive.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/style.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-datetimepicker.min.css">

<script type="text/javascript" src="${ctxCss}/common/jquery.js"></script>
<script type="text/javascript" src="${ctxCss}/common/bootstrap.min.js"></script>
<script type="text/javascript" src="${ctxCss}/common/bootstrap-datepicker.js"></script>
<html>
<head>
	<title>流程管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
		});
		function page(n,s){
        	location = '${ctx}/act/process/?pageNo='+n+'&pageSize='+s;
        }
		function updateCategory(id, category){
			$.jBox($("#categoryBox").html(), {title:"设置分类", buttons:{"关闭":true}, submit: function(){}});
			$("#categoryBoxId").val(id);
			$("#categoryBoxCategory").val(category);
		}
	</script>
	<script type="text/template" id="categoryBox">
		<form id="categoryForm" action="${ctx}/act/process/updateCategory" method="post" enctype="multipart/form-data"
			style="text-align:center;" class="form-search" onsubmit="loading('正在设置，请稍等...');"><br/>
			<input id="categoryBoxId" type="hidden" name="procDefId" value="" />
			<select id="categoryBoxCategory" name="category">
				<option value="">无分类</option>
				<c:forEach items="${fns:getDictList('act_category')}" var="dict">
					<option value="${dict.value}">${dict.label}</option>
				</c:forEach>
			</select>
			<br/><br/>　　
			<input id="categorySubmit" class="btn btn-primary" type="submit" value="   保    存   "/>　　
		</form>
	</script>
	
    <script type="text/javascript">
        var processDefinitionId;
        var ctx = '${ctx}';
        $(function () {
////////////////////////////////////////////////设置候选属性开始///////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            $('.set-startable').click(function () {
                var pid = $(this).data('pid');
                processDefinitionId = pid;
                var rowIndex = $(this).parents('tr').attr('index');
                if ($('#startable-' + rowIndex).length == 1) {
                    var users = $('#startable-' + rowIndex).find('.users li');
                    var groups = $('#startable-' + rowIndex).find('.groups li');

                }
                // 打开对话框的时候先从后台读取已有配置
                $('input[name=user],input[name=group]').attr('checked', false);
                $.getJSON(ctx + '/act/task/startable/read/' + pid, function(result) {
                    $(result.users_).each(function() {
                       $('input[name=user][value=' + this + ']').attr('checked', true);
                    });
                    $(result.groups_).each(function() {
                        $('input[name=group][value=' + this + ']').attr('checked', true);
                    });
                    $('#addStartableModal').modal();
                });
            });
            // 全选用户/组
            $('#selectAllUser').change(function() {
               $('input[name=user]').attr('checked', $(this).attr('checked') || false);
            });
            $('#selectAllGroup').change(function() {
                $('input[name=group]').attr('checked', $(this).attr('checked') || false);
            });
            // 设置候选属性
            $('#addStartableAttr').click(function () {
                var users = new Array();
                var groups = new Array();
                $('input[name=user]:checked').each(function() {
                    users.push($(this).val());
                });

                $('input[name=group]:checked').each(function() {
                    groups.push($(this).val());
                });
                $.post(ctx + "/act/process/startable/set/" + processDefinitionId, {
                    users_: users,
                    groups_: groups
                }, function (resp) {
                    if (resp == 'true') {
                        location.reload();
                    } else {
                        alert('设置失败！');
                    }
                });
            });
////////////////////////////////////设置候选属性结束///////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

            // 显示/隐藏候选属性
            $('.toggle-startable').click(function() {
                var index = $(this).data('index');
                $('#startable-' + index).fadeToggle();
            });
        });
    </script>	
	
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/act/process/">流程定义管理</a></li>
		<li><a href="${ctx}/act/process/deploy/">部署流程</a></li>
	</ul>
	<form id="searchForm" action="${ctx}/act/process/" method="post" class="breadcrumb form-search">
		<select id="category" name="category" class="input-medium">
			<option value="">全部分类</option>
			<c:forEach items="${fns:getDictList('act_category')}" var="dict">
				<option value="${dict.value}" ${dict.value==category?'selected':''}>${dict.label}</option>
			</c:forEach>
		</select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<sys:message content="${message}"/>
	<table class="table table-striped table-bordered table-condensed table-nowrap">
		<thead>
			<tr>
				<th>流程分类</th>
				<th>流程ID</th>
				<th>流程标识</th>
				<th>流程名称</th>
				<th>流程版本</th>
				<th>部署时间</th>
				<th>流程XML</th>
				<th>流程图片</th>
				<th>操作</th>
				<th>候选人/组</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="object" varStatus="row">
				<c:set var="process" value="${object[0]}" />
				<c:set var="deployment" value="${object[1]}" />
				<tr  index="${row.index}">
					<td><a href="javascript:updateCategory('${process.id}', '${process.category}')" title="设置分类">${fns:getDictLabel(process.category,'act_category','无分类')}</a></td>
					<td>${process.id}</td>
					<td>${process.key}</td>
					<td>${process.name}</td>
					<td><b title='流程版本号'>V: ${process.version}</b></td>
					<td><fmt:formatDate value="${deployment.deploymentTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><a target="_blank" href="${ctx}/act/process/resource/read?procDefId=${process.id}&resType=xml">${process.resourceName}</a></td>
					<td><a target="_blank" href="${ctx}/act/process/resource/read?procDefId=${process.id}&resType=image">${process.diagramResourceName}</a></td>
					<td>
						<c:if test="${process.suspended}">
							<a href="${ctx}/act/process/update/active?procDefId=${process.id}" onclick="return confirmx('确认要激活吗？', this.href)">激活</a>
						</c:if>
						<c:if test="${!process.suspended}">
							<a href="${ctx}/act/process/update/suspend?procDefId=${process.id}" onclick="return confirmx('确认挂起除吗？', this.href)">挂起</a>
						</c:if>
						<a href='${ctx}/act/process/delete?deploymentId=${process.deploymentId}' onclick="return confirmx('确认要删除该流程吗？', this.href)">删除</a>
						<a class="set-startable" data-pid="${process.id}" title="设置候选启动人|组"><i class="icon-user"></i>候选启动</a>
						<%--<a href='${ctx}/act/process/convert/toModel?procDefId=${process.id}' onclick="return confirmx('确认要转换为模型吗？', this.href)">转换为模型</a> --%>
						   <td>
						                <c:if test="${not empty linksMap[process.id]['users'] || not empty linksMap[process.id]['groups']}">
                    <a href="#" class="toggle-startable" data-index="${row.index}">
                        人[${fn:length(linksMap[process.id]['users'])}],
                        组[${fn:length(linksMap[process.id]['groups'])}]
                    </a></td>
                </c:if>
                        
					</td>
				</tr>
        <%-- 显示候选属性 --%>
        <c:if test="${not empty linksMap[process.id]}">
            <tr id="startable-${row.index}" style="display: none">
                <td colspan="10">
                    <table>
                        <thead>
                        <tr>
                            <th class="text-info">候选启动（人）</th>
                            <th class="text-info">候选启动（组）</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <ul class="users">
                                    <c:forEach items="${linksMap[process.id]['users']}" var="user">
                                        <li>${user.firstName} ${user.lastName}</li>
                                    </c:forEach>
                                </ul>
                            </td>
                            <td>
                                <ul class="groups">
                                    <c:forEach items="${linksMap[process.id]['groups']}" var="role">
                                        <li>${role.name}</li>
                                    </c:forEach>
                                </ul>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </c:if>				
		</c:forEach>
			
			
		</tbody>
	</table>
	<div class="pagination">${page}</div>	
	
<!-- 流程定义挂起 -->
<div id="addStartableModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="addStartableModalLabel"
     aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="addStartableModalLabel"><span class="state"></span>设置候选启动人|组</h3>
    </div>
    <div class="modal-body">
        <div class="row">
            <div class="span3">
                <label class="checkbox">
                    <input type="checkbox" id="selectAllUser" />候选启动（人）
                </label>
                <hr>
                <c:forEach items="${users_}" var="user">
                    <label class="checkbox"><input type="checkbox" value="${user.loginName}" name="user" />${user.name}</label><br>
                </c:forEach>
            </div>
            <div class="span3">
                <label class="checkbox">
                    <input type="checkbox" id="selectAllGroup" />候选启动（组）
                </label>
                <hr>
                <c:forEach items="${groups_}" var="group">
                    <label class="checkbox"><input type="checkbox" value="${group.enname}" name="group" />${group.name}</label><br>
                </c:forEach>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
        <button type="button" id="addStartableAttr" class="btn btn-primary">确定<span class="state"></span></button>
    </div>
</div>


</body>
</html>
