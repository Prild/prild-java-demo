<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>问答评论列表</title>

<script type="text/javascript" src="${ctxStatic_}/others/admin/questions/questions_comment.js"></script>
</head>
<body>
	<div class="pad20">
		<a title="返回" onclick="history.go(-1)" class="btn btn-sm" href="javascript:void(0)">
			返回
		</a>
		<table cellspacing="0" cellpadding="0" border="0" class="table table-striped">
			<thead>
				<tr>
					<td align="center">发表人</td>
					<!-- <td align="center" width="230">评论内容</td> -->
					<td align="center">点赞数</td>
					<td align="center">添加时间</td>
					<td align="center">操作</td>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${questionsCommentList}" var="questionsComment">
					<tr class="odd">
						<td align="center">${questionsComment.email}</td>
						<%-- <td align="center">${questionsComment.content}</td> --%>
						<td align="center">${questionsComment.praiseCount}</td>
						<td align="center">
							<fmt:formatDate value="${questionsComment.addTime}" pattern="yyyy/MM/dd HH:mm" />
						</td>
						<td align="center">
							<a href="javascript:void(0)" onclick="delQuestionsCommentSon('${questionsComment.id}')" class="btn btn-sm">删除</a>
							<a href="javascript:void(0)" onclick='getCommentContent("${questionsComment.id}")' data-toggle="modal" data-target="#updateWin"  class="btn btn-sm">修改</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	
<%-- 
	<!-- 修改窗口 ,开始-->
	<div id="updateWin" aria-labelledby="ui-dialog-title-dialog" role="dialog" tabindex="-1"
		class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"
		style="display: none; position: absolute; overflow: hidden; z-index: 1010; outline: 0px none; height: auto; width: 600px; top: 173px; left: 367px;">
		<div style="-moz-user-select: none;" unselectable="on" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
			<span style="-moz-user-select: none;" unselectable="on" id="ui-dialog-title-dialog" class="ui-dialog-title">修改问答评论</span>
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