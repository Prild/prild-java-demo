<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head><title>发送系统消息</title>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-responsive.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/style.css" type="text/css"/>

<script type="text/javascript" src="${ctxCss}/common/jquery.js"></script>
<script type="text/javascript" src="${ctxStatic_ }/kindeditor/kindeditor-all.js"></script>
<script type="text/javascript">
KindEditor.ready(function(K) {
	window.EditorObject = K.create('textarea[id="message"]', {
			resizeType  : 1,
	       filterMode : false,//true时过滤HTML代码，false时允许输入任何代码。
	       allowPreviewEmoticons : false,
	       allowUpload : true,//允许上传 
	       syncType : 'auto',
	       urlType : 'domain',//absolute
	       newlineTag :'br',//回车换行br|p
	     <%--   uploadJson : '<%=keuploadSimpleUrl%>&param=question',//图片上传路径--%>
	       allowFileManager : false,
	       afterBlur:function(){EditorObject.sync();}, 
	       items : ['emoticons']
	});
});
	function sendmessage(){
		var content = $("#message").val();
		if(content==null||content.trim()==""){
			alert("请填写消息内容在发送");
			return false;
		}
		 $.ajax({
             url:"${ctx}/studentuser/letter/sendJoinGroup",
             type:"post",
             data:{"content":content},
             dataType:"json",
             success:function(result){
             	if(result.message=='success'){
             		KindEditor.html('#message', '');
             		 alert("发送成功");
             	}
             }
         });
	}
</script>

</head>
<body  >
	<div class="page_head">
		<h4><em class="icon14 i_01"></em>&nbsp;<span>系统管理</span> &gt; <span>发送系统消息</span> </h4>
	</div>
		<form action="${ctx}/course/addCourse" method="post" id="saveCourseForm">
			<input type="hidden" name="course.logo" />
			<table style="line-height: 35px;">
				<tr>
					<td>
						<font color="red">*</font>发送系统消息:
					</td>
					<td>
						<textarea name="" id="message"></textarea>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<button onclick="sendmessage()" class="btn btn-small" type="button">保存</button>
						<input onclick="history.go(-1);" class="btn btn-small" type="button" value="返回" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>