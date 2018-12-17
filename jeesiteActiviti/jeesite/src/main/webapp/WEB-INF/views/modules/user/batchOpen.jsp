<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>批量开通学员</title>
 <link rel="stylesheet" href="${ctxCss}/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-responsive.min.css" type="text/css"/>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script type="text/javascript">
function importExcel(){
	var myFile = $("#myFile").val();
    if(myFile.length <= 0){
    	alert('选择要上传的文件');
        return false;
    }else{
       return true;
    }
}

</script>
</head>
<body>
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

<div class="mt20">
    <div class="commonWrap">
		<form action="${ctx}/studentuser/importExcel" method="post" id="importP" enctype="multipart/form-data"  onsubmit="return importExcel()">
				<table width="100%" cellspacing="0" cellpadding="0" border="0" class="commonTab01">
					<caption>&nbsp;</caption>
					<tbody>
						<tr>
							<td align="center"><font color="red">*</font>&nbsp;信息描述</td>
							<td>excel模版说明：<br>
                                第一列：用户的电子邮箱,必须是未注册过的<br>
                                第二列：用户的手机号<br>
                                第三列：密码 如不填写默认111111,不得填入非法字符例如“. * #”<br>
                                第四列：赠送课程ID
                                （<a href="${ctxStatic_}/common/import_student/import_student.xls"  style="color: red;">点击下载模版</a>）<br>
							</td>
						</tr>
						<tr>
							<td align="center">导入中出错选择项</td>
							<td>
								<select name="mark">
									<option value="1">跳过</option>
									<option value="2">全部放弃</option>
								</select>
							</td>
						</tr>
						<tr>
							<td align="center">上传</td>
							<td>
								<span class="ml10"><input id="myFile" type="file" value="" name="myFile" />
								
								<button type="submit" name="submit" class="btn btn-primary">提交</button>
								
								<a href="javascript:history.go(-1);" title="返回" class="btn btn-danger">返回</a>
								</span>
							</td>
						</tr>
					</tbody>
				</table>
				</form>

			</div>
    </div>
</body>
</html>