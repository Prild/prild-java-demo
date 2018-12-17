<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>选择讲师列表</title>
<script type="text/javascript" src="${ctxStatic_}/others/admin/teacher/select_teacher_list.js"></script>
<script type="text/javascript">
/**
 * 全选或反选
 */
function selectAll(em) {
	if (em.checked) {
		$("input[name='teacherId']:checkbox").attr('checked', true);
	} else {
		$("input[name='teacherId']:checkbox").attr('checked', false);
	}
}
</script>
</head>
<body>
	<div class="pad20">
		<form action="${ctx}/teacher/selectlist/${type}" method="post" id="searchForm">
			<input type="hidden" id="pageCurrentPage" name="page.currentPage" value="1" />
			讲师名称：
			<input placeholder="讲师名" type="text" name="queryTeacher.name" value="${queryTeacher.name}" />
			<select name="queryTeacher.isStar">
				<option <c:if test="${queryTeacher.isStar==0}"> selected="selected"</c:if> value="0">请选择</option>
				<option <c:if test="${queryTeacher.isStar==1}"> selected="selected"</c:if> value="1">高级讲师</option>
				<option <c:if test="${queryTeacher.isStar==2}"> selected="selected"</c:if> value="2">首席讲师</option>
			</select>
			<button class="btn btn-primary btn-sm" id="searchForm">查询</button> 
			<button class="btn btn-sm " onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);">重置</button> 
		</form>
		<table class="table table-striped">
			<thead>
				<tr>
					<td align="center">
						<c:if test="${type=='checkbox'}">
							<input name="allck"id="allck" type="checkbox" onclick="selectAll(this)" />
						</c:if>
					</td>
					<td align="center">名称</td>
					<td align="center">头衔</td>
					<td align="center">资历</td>
					<td align="center">简介</td>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${teacherList}" var="tc">
					<tr class="odd">
						<td align="center">
							<c:if test="${type=='checkbox'}">
								<input type="checkbox" name="teacherId" title='${tc.name}' value="${tc.id_}" />
							</c:if>
							<c:if test="${type=='radio'}">
								<input type="radio" name="teacherId" title='${tc.name}' value="${tc.id_}" />
							</c:if>
						</td>
						<td align="center">${tc.name}</td>
						<td align="center">
							<c:if test="${tc.isStar==1 }">高级讲师 </c:if>
							<c:if test="${tc.isStar==2 }">首席讲师 </c:if>
						</td>
						<td align="center">${tc.education }</td>
						<td align="center">${fn:substring(tc.career,0,30)}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
	</div>
	<div style="text-align: center;">
		<button class="btn btn-primary btn-sm"  onclick="confirmSelect()">
			确认
		</button>
		<button class="btn btn-primary btn-sm"  onclick="closeWin()" >
			关闭
		</button>
	</div>
</body>
</html>