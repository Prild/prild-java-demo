<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>问答标签列表</title>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap.min.css" type="text/css"/>
<link rel="stylesheet" href="${ctxCss}/css/bootstrap-responsive.min.css" type="text/css"/>
<script type="text/javascript" src="${ctxCss}/common/jquery.js"></script>

<link rel="stylesheet" href="${ctxStatic_}/common/ztree/css/zTreeStyle.css" type="text/css" />
<script type="text/javascript" src="${ctxStatic_}/common/ztree/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript" src="${ctxStatic_}/common/ztree/jquery.ztree.excheck-3.5.min.js"></script>
<script type="text/javascript" src="${ctxStatic_}/common/ztree/jquery.ztree.exedit-3.5.min.js"></script>
<script type="text/javascript">
var ctx = '${ctx}';
var questionsTagList='${questionsTagList}';
</script>
<script type="text/javascript" src="${ctxStatic_}/others/admin/questions/questions_tag.js"></script>
</head>
<body>
	<fieldset>
		<legend>
			<span>问答标签管理</span>
			&gt;
			<span>问答标签列表</span>
		</legend>

		<div class="mt20">
			<div class="commonWrap">
				<form action="${ctx}/subj/delQuestionsTags" method="post" id="updateQuestionsTagForm" name="updateQuestionsTagForm">
					<input type="hidden" name="ids" id="ids" />
					<table width="100%" cellspacing="0" cellpadding="0" border="0" class="commonTab01">
						<tbody>
							<tr>
								<td width="20%">
									<div id="ztreedemo" class="ztree"></div>
								</td>
							</tr>
							<tr>
								<td colspan="2" align="center">
									<input type="button" value="增加问答标签" onclick="addQuestionsTag();" class="button" />
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
		</div>
	</fieldset>
</body>
</html>
