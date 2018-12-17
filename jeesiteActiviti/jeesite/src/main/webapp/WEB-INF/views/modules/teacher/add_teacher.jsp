<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="decorator" content="myself"></meta>	
<title>添加讲师</title>

<script type="text/javascript" src="${ctxStatic_}/common/webutils.js"></script>
<link rel="stylesheet" href="${ctxStatic_}/common/nice-validator/jquery.validator.css"></link>
<script type="text/javascript" src="${ctxStatic_}/common/nice-validator/jquery.validator.js"></script>
<script type="text/javascript" src="${ctxStatic_}/common/nice-validator/local/zh-CN.js"></script>
<script type="text/javascript" src="${ctxStatic_}/others/admin/teacher/teacher.js"></script>

<script type="text/javascript">
	var subjectList='${subjectList}';
	var ctx = "${ctx}";
	</script>
</head>
<body>
		<form action="${ctx}/teacher/add" method="post" id="saveTeacherForm" data-validator-option="{stopOnError:false, timely:false}">
			<input type="hidden" name="teacher.picPath" id="imagesUrl" />
			<table class="table table-striped">
				<tr>
					<td>
						<font color="red">*</font>讲师名称:
					</td>
					<td style="text-align: left;">
						<input name="teacher.name" type="text" style="width: 580px;" data-rule="required;"/>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>讲师资历：
					</td>
					<td style="text-align: left;">
						<textarea name="teacher.education" style="width: 580px; height: 30px;" data-rule="required;"></textarea>
					</td>
				</tr>
				<tr>
					<td>讲师等级：</td>
					<td style="text-align: left;">
						<select class="dropdown" name="teacher.isStar">
							<option value="1">高级讲师</option>
							<option value="2">首席讲师</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>讲师专业：
					</td>
					<td style="text-align: left; position: relative;">
						<input type="hidden" name="teacher.subjectId" value="0" />
						<input readonly="readonly" id="subjectId" onclick="showSubjectList()" />
						<div id="ztreedemo" class="ztree" style="display: none; position: absolute; top: 30px; left: 0; background: #f8f8f8; width: 150px;"></div>
					</td>
				</tr>

				<tr>
					<td>讲师排序：</td>
					<td style="text-align: left;">
						<input name="teacher.sort" type="text"  value="0" data-rule="required;integer[+0]"/>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>讲师简介：
					</td>
					<td style="text-align: left;">
						<textarea style="width: 580px; height: 80px;" id="career" data-rule="required;" name="teacher.career"></textarea>
					</td>
				</tr>
				<tr>
					<td>讲师头像：</td>
					<td style="text-align: left;">
						<img src="${ctx }/static/common/admin/images/default_head.jpg" alt="" id="subjcetpic" width="288px" height="288px" />
						<input type="button" value="上传" id="fileuploadButton" />
						<font color="red">(请上传 288*288(长X宽)像素 的图片)</font>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<a class="btn btn-primary btn-sm" title="提 交" href="javascript:void(0)" onclick="teacherFormSubmit()">提 交</a>
						<a class="btn btn-primary btn-sm" title="返 回" href="javascript:history.go(-1);">返 回</a>
					</td>
				</tr>
			</table>
		</form>
</body>
</html>