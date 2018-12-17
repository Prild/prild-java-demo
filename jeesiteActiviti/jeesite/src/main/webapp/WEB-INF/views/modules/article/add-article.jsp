<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>添加文章</title>

<script type="text/javascript" charset="utf-8" src="${ctxStatic_}/common/webutils.js"></script>

<script type="text/javascript" charset="utf-8" src="${ctxStatic_}/common/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctxStatic_}/common/ueditor/ueditor.all.js"></script>

<script type="text/javascript" src="${ctxStatic_}/others/admin/article/article.js"></script>

<script>
layui.use('laydate', function(){
	  var laydate = layui.laydate;
	  laydate.render({
		elem: '#publishTime' //指定元素
		,type: 'datetime'  
	  });
	});
</script>
	
<script type="text/javascript">
	$(function() {
		//实例化编辑器 UE编辑器
		initUEEditor("content",'580','400');
		//initKindEditor_addblog('content', 580, 400, 'articleContent', 'true');
		initSimpleImageUpload('imageFile', 'article', callback);
	});
</script>

</head>
<body>
		<form action="${ctx}/article/addarticle" method="post" id="articleForm" data-validator-option="{stopOnError:false, timely:false}">
			<input type="hidden" name="article.imageUrl">
			<table class="table table-striped">
				<tr>
					<td>
						<font color="red">*</font>标题
					</td>
					<td>
						<input name="article.title" data-rule="标题:required;" type="text" style="width: 580px;" />
					</td>
				</tr>
				<tr>
					<td>摘要：</td>
					<td>
						<textarea name="article.summary" style="width: 580px; height: 90px;" data-rule="required;"></textarea>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>文章类型：
					</td>
					<td>
						<select name="article.type">
							<option value="2">文章</option>
						</select> 作者：
						<input name="article.author" value="" type="text" style="width: 100px;" />
						来源：
						<input type="text" value="" name="article.source" />
					</td>
				</tr>
				<tr>
					<td>点击数：</td>
					<td>
						<input name="article.clickNum" id="clickNum"   type="text" style="width: 140px;" value="0"  onkeyup="this.value=this.value.replace(/\D/g,'')" data-rule="required;"/>
					</td>
				</tr>
				<tr>
					<td>发布时间：</td>
					<td>
						<input name="article.publishTime" id="publishTime"   readonly="readonly"  type="text" style="width: 140px;z-index: 1000;position:relative;" data-rule="required;"/>
					</td>
				</tr>
				<tr>
					<td>封面图片：</td>
					<td>
						<img id="showImage" width="180" height="100" src="${ctx }/static/admin/assets/logo.png">
						<input type="button" value="上传" id="imageFile" />
						<font color="red">(请上传宽高为： 640*357 的图片)</font>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>内容：
					</td>
					<td>
						<textarea name="articleContent.content" id="content" data-rule="required;" style="background-image: none;margin-top:26px;"></textarea>
					</td>
				</tr>
				<tr>
					<td>排序值：</td>
					<td>
						<input name="article.sort" id="sort"   type="text" style="width: 140px;" value="0"  onkeyup="this.value=this.value.replace(/\D/g,'')" data-rule="required;"/>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<input onclick="saveArticle()" class="button" type="button" value="保存">
					</td>
				</tr>
			</table>
		</form>
</body>
</html>