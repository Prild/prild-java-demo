<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>问答回复管理列表</title>
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
<script type="text/javascript" src="${ctxStatic_}/others/admin/questions/questions_comment.js"></script>

<script type="text/javascript">
var ctx = '${ctx}';
</script>
</head>
<body>
	<div class="pad20">
		<form action="${ctx}/questionscomment/list" method="post" id="searchForm">
			<input type="hidden" id="pageCurrentPage" name="page.currentPage" value="1" />
			问答ID:<!-- 后台bean 对应id用integer，可以处理空值;如果用int,空值处理不了 -->
			<input placeholder="问答ID" name="questionsComment.questionId" value="${questionsComment.questionId}" style="width:80px;"/>
			问答标题:
			<input placeholder="问答标题" name="questionsComment.questionsTitle" value="${questionsComment.questionsTitle}" />
			回复添加时间:
			<input placeholder="添加开始时间" name="questionsComment.beginCreateTime"
				value="<fmt:formatDate value="${questionsComment.beginCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" id="beginCreateTime" type="text"
				readonly="readonly" style="width:120px;"/>
			-
			<input placeholder="添加结束时间" id="endCreateTime" name="questionsComment.endCreateTime"
				value="<fmt:formatDate value="${questionsComment.endCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text" readonly="readonly" style="width:120px;"/>
			
			是否采纳:
			<select id="" name="questionsComment.isBest" style="width:80px;">
				<option value="-1">--全部--</option>
				<option value="0">否</option>
				<option value="1">是</option>
			</select>
			
			<script>
				$(function(){
					//条件选中
					$("select[name='questionsComment.isBest']").val("${questionsComment.isBest}");
				})
			</script>
			
			<a title="查询问答回复" onclick="javascript:$('#searchForm').submit();" class="btn btn-sm btn-primary" href="javascript:void(0)">
				查询
			</a>
			<a title="清空" onclick="javascript:$('#searchForm input:text').val('');" class="btn btn-sm btn-primary" href="javascript:void(0)">
				清空
			</a>
			
			<a title="返回" onclick="history.go(-1)" class="btn btn-sm btn-primary" href="javascript:void(0)">
				返回
			</a>
		</form>
		<table cellspacing="0" cellpadding="0" border="0" class="layui-table">
			<thead>
				<tr>
					<td align="center">问答id</td>
					<td align="center">问答标题</td>
					<td align="center">发表人</td>
					<!-- <td align="center" width="230">回复内容</td> -->
					<td align="center">是否采纳</td>
					<td align="center">回复数</td>
					<td align="center">点赞数</td>
					<td align="center">添加时间</td>
					<td align="center">操作</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${questionsCommentList}" var="questionsComment">
					<tr class="odd">
						<td align="center">${questionsComment.questionId }</td>
						<td align="center">${questionsComment.questionsTitle }</td>
						<td align="center">${questionsComment.email}</td>
						<%-- <td align="center">${questionsComment.content}</td> --%>
						<td align="center">
							<c:if test="${questionsComment.isBest==0}">否</c:if>
							<c:if test="${questionsComment.isBest==1}">是</c:if>
						</td>
						<td align="center">${questionsComment.replyCount}</td>
						<td align="center">${questionsComment.praiseCount}</td>
						<td align="center">
							<fmt:formatDate value="${questionsComment.addTime}" pattern="yyyy/MM/dd HH:mm" />
						</td>
						<td align="center">
							<a href="javascript:void(0)" onclick="delQuestionsComment('${questionsComment.id_}')" class="btn btn-sm btn-link">删除</a>
							<a href="javascript:void(0)" onclick='getCommentContent("${questionsComment.id_}")'  data-toggle="modal" data-target="#updateWin"  class="btn btn-sm btn-link">修改</a>
							<a href="${ctx }/admin/questionscomment/querybycommentid/${questionsComment.id_}" class="btn btn-sm btn-link">查看评论</a>
							<c:if test="${questionsComment.questionsStatus==0}">
								<a href="javascript:void(0)" onclick="acceptComment(${questionsComment.id_})" class="btn btn-sm btn-primary">采纳为最佳</a>
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
	</div>
<%--
	<!-- 修改窗口 ,开始-->
	<div id="updateWin" aria-labelledby="ui-dialog-title-dialog" role="dialog" tabindex="-1"
		class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"
		style="display: none; position: absolute; overflow: hidden; z-index: 1010; outline: 0px none; height: auto; width: 600px; top: 173px; left: 367px;">
		<div style="-moz-user-select: none;" unselectable="on" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
			<span style="-moz-user-select: none;" unselectable="on" id="ui-dialog-title-dialog" class="ui-dialog-title">修改问答回复</span>
			<a style="-moz-user-select: none;" unselectable="on" role="button" class="ui-dialog-titlebar-close ui-corner-all"
				href="javascript:void(0)">
				<span style="-moz-user-select: none;" unselectable="on" class="ui-icon ui-icon-closethick">close</span>
			</a>
		</div>
		<div style="height: 300px; min-height: 42px; width: auto;" class="ui-dialog-content ui-widget-content">
			<form id="updateForm">
				<input type="hidden" name="questionsComment.id" value="0" />
				<table style="line-height: 35px;">
					<tr>
						<td>点赞：</td>
						<td>
							<input type="text" value="${questionsComment.praiseCount }" name="questionsComment.praiseCount" onkeyup="value=value.replace(/[^\d]/g,'') "/>
						</td>
					</tr>
					<tr>
						<td>内容：</td>
						<td>
							<textarea rows="" cols="" name="questionsComment.content"></textarea>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div style="-moz-user-select: none;" unselectable="on" class="ui-resizable-handle ui-resizable-n"></div>
		<div style="-moz-user-select: none;" unselectable="on" class="ui-resizable-handle ui-resizable-e"></div>
		<div style="-moz-user-select: none;" unselectable="on" class="ui-resizable-handle ui-resizable-s"></div>
		<div style="-moz-user-select: none;" unselectable="on" class="ui-resizable-handle ui-resizable-w"></div>
		<div unselectable="on" style="z-index: 1001; -moz-user-select: none;"
			class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se ui-icon-grip-diagonal-se"></div>
		<div unselectable="on" style="z-index: 1002; -moz-user-select: none;" class="ui-resizable-handle ui-resizable-sw"></div>
		<div unselectable="on" style="z-index: 1003; -moz-user-select: none;" class="ui-resizable-handle ui-resizable-ne"></div>
		<div unselectable="on" style="z-index: 1004; -moz-user-select: none;" class="ui-resizable-handle ui-resizable-nw"></div>
		<div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
			<button class="ui-state-default ui-corner-all" onclick="updateQuestionComment()" type="button">确定</button>
			<button class="ui-state-default ui-corner-all closeBut" type="button">取消</button>
		</div>
	</div>
	<!-- 修改窗口 ,结束-->
	 --%>	


	
	<!-- 模态框（Modal） -->
<div id="updateWin"  class="modal fade"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					修改
				</h4>
			</div>
			<div class="modal-body">
			<form id="updateForm">
				<input type="hidden" name="questionsComment.id_" value="${questionsComment.id_}" />
				<table style="line-height: 35px;">
					<tr>
						<td>点赞：</td>
						<td>
							<input type="text" value="${questionsComment.praiseCount }" name="questionsComment.praiseCount" onkeyup="value=value.replace(/[^\d]/g,'') "/>
						</td>
					</tr>
					<tr>
						<td>内容：</td>
						<td>
							<textarea rows="" cols="" name="questionsComment.content"></textarea>
						</td>
					</tr>
				</table>
			</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" >关闭
				</button>
				<button type="button" class="btn btn-primary"  onclick="updateQuestionComment()" >
					提交更改
				</button>
				
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
	






	 
</body>
</html>