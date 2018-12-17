<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>文章列表</title>
<link rel="stylesheet" href="${ctxStatic_}/common/admin/js/layui-master/dist/css/layui.css" type="text/css"/>
<script type="text/javascript" src="${ctxStatic_}/others/admin/article/article.js"></script>

<script>
layui.use('laydate', function(){
	  var laydate = layui.laydate;
	  laydate.render({
		elem: '#beginCreateTime' //指定元素
		,type: 'datetime'  
	  });
	});
</script>
<script>
layui.use('laydate', function(){
	  var laydate = layui.laydate;
	  laydate.render({
		  elem: '#endCreateTime' //指定元素
		  ,type: 'datetime'  
	  });
	});
</script>

<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>

<body>
	<div class="pad20">
		<form action="${ctx}/article/showlist" method="post" id="searchForm">
			<input type="hidden" id="pageCurrentPage" name="page.currentPage" value="1" />
			<input placeholder="标题/作者/来源" type="text" name="queryArticle.queryKey" value="${queryArticle.queryKey}" />
			<select name="queryArticle.type">
				<option value="0">请选择类型</option>
				<option value="2">文章</option>
			</select>
			创建时间:
			<input placeholder="开始创建时间" name="queryArticle.beginCreateTime"
				value="<fmt:formatDate value="${queryArticle.beginCreateTime}" pattern="yyyy-MM-dd HH:mm"/>" id="beginCreateTime" type="text"
				readonly="readonly" style="width: 128px;"/>-
			<input placeholder="结束创建时间" id="endCreateTime" name="queryArticle.endCreateTime"
				value="<fmt:formatDate value="${queryArticle.endCreateTime}" pattern="yyyy-MM-dd HH:mm"/>" type="text" readonly="readonly" style="width: 128px;"/>
				
			<button class="btn btn-sm " id="searchForm">查询</button> 
			<button class="btn btn-sm " onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);">重置</button> 
			
			<a title="批量删除" onclick="deleteArticle()" class="btn btn-sm btn-link" href="javascript:void(0)">
				批量删除
			</a>
		</form>
		<form action="${ctx}/article/delete" id="deleteForm" method="post">
			<table cellspacing="0" cellpadding="0" border="0" class="table table-striped">
				<thead>
					<tr>
						<td align="center">
							<input name="allck"id="allck" type="checkbox" onclick="selectAll(this)" />
						</td>
						<td align="center">标题</td>
						<td align="center">作者</td>
						<td align="center">来源</td>
						<td align="center">类型</td>
						<td align="center">创建时间</td>
						<td align="center">发布时间</td>
						<td align="center">点击量</td>
						<td align="center" width="232">操作</td>
					</tr>
				</thead>

				<tbody>
					<c:forEach items="${articleList}" var="article">
						<tr class="odd">
							<td align="center">
								<input type="checkbox" name="articleId" id="articleId" value="${article.articleId}" />
							</td>
							<td align="center">${article.title}</td>
							<td align="center">${article.author}</td>
							<td align="center">${article.source}</td>
							<td align="center">
								<c:if test="${article.type==1}">公告</c:if>
								<c:if test="${article.type==2}">文章</c:if>
							</td>
							<td align="center">
								<fmt:formatDate value="${article.createTime}" pattern="yyyy/MM/dd HH:mm" />
							</td>
							<td align="center">
								<fmt:formatDate value="${article.publishTime}" pattern="yyyy/MM/dd HH:mm" />
							</td>
							<td align="center">${article.clickNum}</td>
							<td align="center">
								<a href="${ctx}/article/initupdate/${article.articleId}" class="btn btn-sm btn-link">修改</a>
								<a href="javascript:void(0)" onclick="thisDelete(this)" class="btn btn-sm btn-link">删除</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</form>
<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
	</div>
</body>
</html>