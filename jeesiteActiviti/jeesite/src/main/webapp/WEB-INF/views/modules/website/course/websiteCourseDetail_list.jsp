<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>
<title>推荐课程管理列表</title>

<script type="text/javascript" src="${ctxStatic_}/others/admin/website/detail.js"></script>
<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>
<body>
	<div class="pad20">
		<form action="${ctx}/website/detail/list" method="post" id="searchForm">
			<input type="hidden" id="pageCurrentPage" name="page.currentPage" value="1" />
			<input placeholder="课程名" type="text" name="dto.courseName" value="${dto.courseName}" />
			<select name="dto.id_">
				<option value="0">请选择类型</option>
				<c:forEach items="${websiteCourseList}" var="list">
					<c:choose>
						<c:when test="${dto.id_==list.id_}">
							<option selected="selected" value="${list.id_}">${list.name}</option>
						</c:when>
						<c:otherwise>
							<option value="${list.id_}">${list.name}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select> <select name="dto.isavaliable">
				<option value="0">请选择状态</option>
				<option <c:if test="${dto.isavaliable==1}"> selected="selected" </c:if> value="1">上架</option>
				<option <c:if test="${dto.isavaliable==2}"> selected="selected" </c:if> value="2">下架</option>
			</select>
			<button class="btn btn-sm" id="searchForm">查询</button> 
			<button class="btn btn-sm " onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);">重置</button> 
			<a title="添加推荐课程" onclick="selectCousre()"  class="btn btn-primary btn-sm" href="javascript:void(0)">
				添加推荐课程
			</a>
		</form>
		<table  class="table table-striped">
			<thead>
				<tr>
					<td align="center">ID</td>
					<td align="center">课程名称</td>
					<td align="center">推荐分类</td>
					<td align="center" width="70">排序值</td>
					<td align="center">课程状态</td>
					<td align="center">操作</td>
				</tr>
			</thead>

			<tbody>
				<c:choose>
					<c:when test="${dtoList!=null && dtoList.size()>0}">
						<c:forEach items="${dtoList}" var="dto">
							<tr class="odd">
								<td align="center">${dto.id}</td>
								<td align="center">${dto.courseName}</td>
								<td align="center">${dto.recommendName}</td>
								<td align="center">${dto.orderNum}</td>
								<td align="center">
									<c:if test="${dto.isavaliable==1}">上架</c:if>
									<c:if test="${dto.isavaliable==2}">下架</c:if>
								</td>
								<td align="center">
									<a href="javascript:void(0)" onclick="deleteDetail(${dto.id_})"  class="btn btn-sm">删除</a>
									<a href="javascript:void(0)" onclick="updateSort(${dto.id_},this)" class="btn btn-sm">修改排序</a>
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr class="odd">
							<td align="center" colspan="6">
								<div class="tips">
									<span>还没有相关推荐课程！</span>
								</div>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
	</div>
</body>
</html>