<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>问答管理列表</title>
<link rel="stylesheet" href="${ctxStatic_}/common/admin/js/layui-master/dist/css/layui.css" type="text/css"/>
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

<script type="text/javascript" src="${ctxStatic_}/others/admin/questions/questions.js"></script>

<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>

<body>
	<div class="pad20">
		<form action="${ctx}/questions/list" method="post" id="searchForm">
			<input type="hidden" id="pageCurrentPage" name="page.currentPage" value="1" />
			标题:
			<input placeholder="标题" name="questions.title" value="${questions.title}" />
			添加时间:
			<input placeholder="添加开始时间" name="questions.beginCreateTime"
				value="<fmt:formatDate value="${questions.beginCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" id="beginCreateTime" type="text"
				readonly="readonly" style="width:120px;"/>
			-
			<input placeholder="添加结束时间" id="endCreateTime" name="questions.endCreateTime"
				value="<fmt:formatDate value="${questions.endCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text" readonly="readonly" style="width:120px;"/>
			
			类型:
			<select id="" name="questions.type">
				<option value="0">--全部--</option>
				<option value="1">课程问答</option>
				<option value="2">学习分享</option>
			</select>
			
			问答标签:
			<select id="" name="questions.questionsTagId">
				<option value="0">--全部--</option>
				<c:forEach items="${questionsTagList }" var="questionsTag">
					<option value="${questionsTag.questionsTagId }">${questionsTag.questionsTagName }</option>
				</c:forEach>
			</select>
			
			<script>
				$(function(){
					//条件选中
					$("select[name='questions.type']").val("${questions.type}");
					if("${questions.questionsTagId}"!=""&&"${questions.questionsTagId}"!=null){
						$("select[name='questions.questionsTagId']").val("${questions.questionsTagId}");
					}
				})
			</script>
			<button type="button" class="btn btn-small" id="searchForm">查询</button> 
			<button type="button" class="btn btn-small" onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);">重置</button> 
		</form>
		<table cellspacing="0" cellpadding="0" border="0" class="table table-striped">
			<thead>
				<tr>
					<!-- <td align="center">id</td> -->
					<td align="center">发表人</td>
					<td align="center">问答标题</td>
					<td align="center">类型</td>
					<td align="center">是否采纳</td>
					<td align="center">回复数</td>
					<td align="center">浏览数</td>
					<td align="center">点赞数</td>
					<td align="center">添加时间</td>
					<td align="center">操作</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${questionsList}" var="questions">
					<tr class="odd">
						<!-- <td align="center">${questions.id_}</td> -->
						<td align="center">${questions.email}</td>
						<td align="center">${questions.title}</td>
						<td align="center">
							<c:if test="${questions.type==1}">课程问答</c:if>
							<c:if test="${questions.type==2}">学习分享</c:if>
						</td>
						<td align="center">
							<c:if test="${questions.status==0}">否</c:if>
							<c:if test="${questions.status==1}">是</c:if>
						</td>
						<td align="center">${questions.replyCount}</td>
						<td align="center">${questions.browseCount}</td>
						<td align="center">${questions.praiseCount}</td>
						<td align="center">
							<fmt:formatDate value="${questions.addTime}" pattern="yyyy/MM/dd HH:mm" />
						</td>
						<td align="center">
							<a href="javascript:void(0)" onclick="delQuestions('${questions.id_}')" class="btn btn-sm btn-link">删除</a>
							<a href="${ctx }/questions/info/${questions.id_}" class="btn btn-sm btn-link">修改</a>
							<a href="${ctx }/questionscomment/list?questionsComment.questionId=${questions.id_}" class="btn btn-sm btn-primary">查看回复</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
	</div>
</body>
</html>