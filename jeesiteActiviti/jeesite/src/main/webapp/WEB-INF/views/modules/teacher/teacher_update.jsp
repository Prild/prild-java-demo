<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ include file="/WEB-INF/base.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="decorator" content="myself"></meta>	
<title>修改讲师</title>

<script type="text/javascript" src="${ctxStatic_}/common/webutils.js"></script>
<link rel="stylesheet" href="${ctxStatic_}/common/nice-validator/jquery.validator.css"></link>
<script type="text/javascript" src="${ctxStatic_}/common/nice-validator/jquery.validator.js"></script>
<script type="text/javascript" src="${ctxStatic_}/common/nice-validator/local/zh-CN.js"></script>
<script type="text/javascript" src="${ctxStatic_}/others/admin/teacher/teacher.js"></script>

<script type="text/javascript">
	subjectList='${subjectList}';
	var uploadSimpleUrl =  '<%= uploadSimpleUrl %>' ;
	</script>
</head>
<body>
		<form action="${ctx}/teacher/update" method="post" id="saveTeacherForm" data-validator-option="{stopOnError:false, timely:false}" >
			<input type="hidden" name="teacher.id_" value="${teacher.id_}" />
			<input type="hidden" name="teacher.picPath" id="imagesUrl" value="${teacher.picPath}" />
			<div class="form-group">
	    		<label for=""><font color="red">*</font>讲师名称:</label>
	    		<input name="teacher.name" value="${teacher.name}" type="text" style="width: 580px;" data-rule="required;"/>
    		</div>
			<div class="form-group">
	    		<label for=""><font color="red">*</font>讲师资历：</label>
	    		<textarea name="teacher.education" style="width: 580px; height: 30px;" data-rule="required;">${teacher.education}</textarea>
    		</div>
			<div class="form-group">
	    		<label for=""><font color="red">*</font>讲师专业：</label>
	    				<input type="hidden" name="teacher.subjectId" value="${teacher.subjectId}" />
						<input readonly="readonly" value="${subject.subjectName}" id="subjectId" onclick="showSubjectList()" />
						<div id="ztreedemo" class="ztree" style="display: none; position: absolute; top: 30px; left: 0; background: #f8f8f8; width: 150px;"></div>
    		</div>
    		<div class="form-group">
    		<label for="">讲师等级：</label>
    					<select class="dropdown" name="teacher.isStar" >
							<option <c:if test="${teacher.isStar==1}">selected="selected"</c:if> value="1">高级讲师</option>
							<option <c:if test="${teacher.isStar==2}">selected="selected"</c:if> value="2">首席讲师</option>
						</select>
    		</div>
    		
    		<div class="form-group">
	    		<label for="">讲师排序：</label>
	    		<input name="teacher.sort" value="${teacher.sort}" data-rule="required;integer[+0]"/>
    		</div>
    		<div class="form-group">
	    		<label for=""><font color="red">*</font>讲师简介：</label>
	    		<textarea style="width: 580px; height: 80px;" id="career" name="teacher.career" data-rule="required;">${teacher.career}</textarea>
    		</div>
    		<div class="form-group">
	    		<label for="">讲师头像：</label>
	   			<c:choose>
							<c:when test="${teacher.picPath!=null && teacher.picPath!=''}">
							<img src="<%=staticImage%>${teacher.picPath}" alt="" id="subjcetpic" width="288px" height="288px"/> 
							</c:when>
							<c:otherwise>
								<img src="${ctx }/static_/common/admin/images/default_head.jpg" alt="" id="subjcetpic" width="288px" height="288px" />
							</c:otherwise>
						</c:choose>
						<input type="button" value="上传" id="fileuploadButton" />
						<font color="red">(请上传 288*288(长X宽)像素 的图片)</font>
    		</div>
    		
    		<div class="form-group">
    		<a class="btn btn-primary btn-sm" title="提 交" href="javascript:void(0)" onclick="teacherFormSubmit()">提 交</a>
    		<a class="btn btn-primary btn-sm" title="返 回" href="javascript:history.go(-1);">返 回</a>
    		</div>
		</form>
</body>
</html>
