<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>课程列表</title>
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


<script type="text/javascript">
var ctx = '${ctx}';
var subjectList = eval('('+'${subjectList}'+')');
$(function(){
	var param={ 
			data:subjectList,//处理的数据（必选）数据格式：[{object Object},{object Object}]  
			showId:'levelId',//显示的数据标签ID（必选）
			idKey:'subjectId',//数据的ID（必选）
			pidKey:'parentId',//数据的父ID（必选）
			nameKey:'subjectName',//数据显示的名（必选）
			returnElement:'subjectId',//返回选中的值（必选 ）
			//-----------------------------------------------------
			initVal:'${queryCourse.subjectId}',
			defName:'请选择',//默认显示的选项名（可选，如果不设置默认显示“请选择”）
			defValue:'0'//默认的选项值（可选，如果不设置默认是“0”）
		};
	ML._init(param);
});

/**
 * 删除课程 
 */
function avaliable(courseId,type,em){
	if(!confirm('确实要删除吗?')){
		return;
	}
	$.ajax({
		url:ctx +'/course/avaliable/'+courseId+'/'+type,
		type:'post',
		dataType:'json',
		success:function(result){
			if(result.success==false){
				alert(result.message);
			}else{
				location.reload();
			}
		},
		error:function(error){
			alert("系统繁忙，请稍后再操作！");
		}
	});
}
</script>
</head>
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
<body>
	<div class="pad20">
		<form action="${ctx}/course/list" method="post" id="searchForm">
			<input type="hidden" id="pageCurrentPage" name="page.pageNum" value="1" />
			<input type="text" name="queryCourse.courseName" value="${queryCourse.courseName}" placeholder="课程标题" />
			 状态:<select name="queryCourse.isavaliable"  style="width: 98px;">
				<option value="0">请选择</option>
				<option <c:if test="${queryCourse.isavaliable==1 }">selected</c:if> value="1">上架</option>
				<option <c:if test="${queryCourse.isavaliable==2 }">selected</c:if> value="2">下架</option>
			</select>
			<input type="hidden" id="subjectId" name="queryCourse.subjectId" value="${queryCourse.subjectId}" />
			专业:
			<samp id="levelId"></samp>
			创建时间:
			<input placeholder="开始创建时间" id="beginCreateTime" name="queryCourse.beginCreateTime"
				value="<fmt:formatDate value="${queryCourse.beginCreateTime}" pattern="yyyy-MM-dd"/>"  type="text" readonly="readonly" style="width: 128px;" />-
			<input placeholder="结束创建时间" id="endCreateTime" name="queryCourse.endCreateTime"
				value="<fmt:formatDate value="${queryCourse.endCreateTime}" pattern="yyyy-MM-dd"/>" type="text" readonly="readonly" style="width: 128px;"/>
			
			<button class="btn btn-sm" id="searchForm">查询</button> 
			<button class="btn btn-sm " onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);">重置</button> 
			
			<a href="${ctx}/course/toAddCourse" class="btn btn-sm  btn-link">创建课程</a>
		</form>
		<table cellspacing="0" cellpadding="0" border="0" class="layui-table">
			<thead>
				<tr>
					<td align="center" width="150px">课程名</td>
					<td align="center">状态</td>
					<td align="center">专业</td>
					<td align="center">原价</td>
					<td align="center">优惠价</td>
					<td align="center">课时</td>
					<td align="center">销售量</td>
					<td align="center">浏览量</td>
					<td align="center">创建时间</td>
					<td align="center">有效结束时间</td>
					<td align="center">操作</td>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${courseList}" var="course">
					<tr class="odd">
						<td align="center">${course.courseName}</td>
						<td align="center">
							<c:if test="${course.isavaliable==1}">上架</c:if>
							<c:if test="${course.isavaliable==2}">下架</c:if>
						</td>
						<td align="center">${course.subjectName}</td>
						<td align="center">${course.sourcePrice}</td>
						<td align="center">${course.currentPrice}</td>
						<td align="center">${course.lessionNum}</td>
						<td align="center">${course.pageBuycount}</td>
						<td align="center">${course.pageViewcount}</td>
						<td align="center">
							<fmt:formatDate value="${course.addTime}" pattern="yyyy/MM/dd HH:mm" />
						</td>
						<td align="center">
							<c:if test="${not empty course.endTime}">
								<fmt:formatDate value="${course.endTime}" pattern="yyyy/MM/dd HH:mm" />
							</c:if>
							<c:if test="${empty course.endTime}">
								购买后${course.loseTime}天
							</c:if>
						</td>
						<td align="center">
							<a href="${ctx}/kpoint/list/${course.courseId}"  class="btn btn-sm  btn-link">章节管理</a>
							<a href="${ctx}/course/initUpdate/${course.courseId}"  class="btn btn-sm btn-link">修改</a>
							<button  onclick="avaliable(${course.courseId},3,this)"  class="btn btn-sm">删除</button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
	</div>
</body>
</html>