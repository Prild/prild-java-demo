<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<link rel="stylesheet" href="${ctxStatic_}/common/admin/js/layui-master/dist/css/layui.css" type="text/css"/>
<title>讲师列表</title>

<script type="text/javascript">
/**
 * 删除老师
 * @param tcId 老师ID
 */
function delcfm(url) {  
	if(confirm('确认要删除该讲师？')){
		document.location=url ;
	} 
}  

function urlSubmit(){  
 var url=$.trim($("#url").val());//获取会话中的隐藏属性URL  
 window.location.href=url;    
}  

</script>

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
	var teacherList='${teacherList}';
	var ctx = "${ctx}";
	</script>
</head>
<body>
	<div class="pad20">
		<form action="${ctx}/teacher/list" method="post" id="searchForm" >
			<input type="hidden" id="pageCurrentPage" name="page.currentPage" value="1" />
			讲师名称：
			<input placeholder="讲师名" type="text" name="queryTeacher.name" value="${queryTeacher.name}" />
			<select name="queryTeacher.isStar">
				<option <c:if test="${queryTeacher.isStar==0}"> selected="selected"</c:if> value="0">请选择</option>
				<option <c:if test="${queryTeacher.isStar==1}"> selected="selected"</c:if> value="1">高级讲师</option>
				<option <c:if test="${queryTeacher.isStar==2}"> selected="selected"</c:if> value="2">首席讲师</option>
			</select>
			添加时间:
			<input placeholder="开始添加时间" name="queryTeacher.beginCreateTime"
				value="<fmt:formatDate value="${queryTeacher.beginCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" id="beginCreateTime" type="text"
				readonly="readonly" style="width: 128px;"/>
			-
			<input placeholder="结束添加时间" id="endCreateTime" name="queryTeacher.endCreateTime"
				value="<fmt:formatDate value="${queryTeacher.endCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text" readonly="readonly" style="width: 128px;"/>
			<!-- 
			<a title="查找用户" onclick="javascript:$('#searchForm').submit();" class="button tooltip" href="javascript:void(0)">
				<span class="ui-icon ui-icon-search"></span>
				查找
			</a> -->
			
			<button class="btn btn-primary btn-sm" id="searchForm">查询</button> 
			<button class="btn btn-sm" onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);">重置</button> 
			
			<!-- 
			<a title="清空" onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);" class="button tooltip"
				href="javascript:void(0)">
				<span class="ui-icon ui-icon-cancel"></span>
				清空
			</a> -->
		</form>
		<table cellspacing="0" cellpadding="0" border="0" class="table table-striped">
			<thead>
				<tr>
					<td align="center">ID</td>
					<td align="center">名称</td>
					<td align="center">头衔</td>
					<td align="center" width="150">资历</td>
					<td align="center" width="250">简介</td>
					<td align="center">添加时间</td>
					<td align="center">排序</td>
					<td align="center" width="100">操作</td>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${teacherList}" var="tc">
					<tr class="odd">
						<td align="center">${tc.id_}</td>
						<td align="center">${tc.name}</td>
						<td align="center">
							<c:if test="${tc.isStar==1 }">高级讲师 </c:if>
							<c:if test="${tc.isStar==2 }">首席讲师 </c:if>
						</td>
						<td align="center">${tc.education }</td>
						<td align="center">${fn:substring(tc.career,0,30)}</td>
						<td align="center">
							<fmt:formatDate type="both" value="${tc.createTime}" pattern="yyyy/MM/dd HH:mm" />
						</td>
						<td align="center">${tc.sort}</td>
						<td align="center">
							<a href="${ctx}/teacher/toUpdate/${tc.id_}" class="btn btn-link btn-sm">修改</a>
							<a class="btn btn-link btn-sm" onClick="delcfm('${ctx}/teacher/delete/${tc.id_} ')">删除</a> 
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
	</div>
</body>
</html>