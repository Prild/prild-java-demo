<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="decorator" content="myself"></meta>
<title>课程修改</title>

<%--ue编辑器--%>
<script type="text/javascript" charset="utf-8" src="${ctxStatic_}/common/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctxStatic_}/common/ueditor/ueditor.all.js"></script>

<%-- subject course teacher相关js --%>
<script type="text/javascript" charset="utf-8" src="${ctxStatic_}/common/webutils.js"></script>
<script type="text/javascript" src="${ctxStatic_}/others/admin/course/course.js"></script><!-- course.js依赖webutils.js -->
<script type="text/javascript" src="${ctxStatic_}/others/admin/teacher/select_teacher_list.js"></script>

<link rel="stylesheet" href="${ctxStatic_}/common/admin/js/layui-master/dist/css/layui.css" type="text/css"/>
<script>
layui.use('laydate', function(){
	  var laydate = layui.laydate;
	  laydate.render({
		elem: '#endTime' //指定元素
		,type: 'datetime'  
	  });
	});
</script>

<script type="text/javascript">
var ctx = '${ctx}';
var subjectList = eval('('+'${subjectList}'+')');//所有专业:用于修改
var teacherList = eval('('+'${teacherList}'+')');//展示课程所属老师:如果要修改，打开新页面 展示全部老师
    $(function(){
    	var param={
    			data:subjectList,	//处理的数据（必选）数据格式：[{object Object},{object Object}] 
    			showId:'levelId',//显示的数据标签ID（必选）
    			idKey:'subjectId',//数据的ID（必选）
    			pidKey:'parentId',//数据的父ID（必选）
    			nameKey:'subjectName',//数据显示的名（必选）
    			returnElement:'returnId',//返回选中的值（必选 ）
    			//-----------------------------------------------------
    			returnIds:'returnIds',//返回所有级的ID，以“,”隔开（可选）
    			initVal:'${course.subjectId}',//初始默认ID（可选）//////////////////////////////////////显示该课程下的专业//////////////////////////////////////
    			defName:'请选择',//默认显示的选项名（可选，如果不设置默认显示“请选择”）  
    			defValue:'0'//默认的选项值（可选，如果不设置默认是“0”）
    		};
    	ML._init(param);
    	addTeahcerList(teacherList);//见course.js  ///////////////////特定老师///////////////////
    });
    </script>
</head>
<body>
		<form action="${ctx}/course/updateCourse" method="post" id="saveCourseForm" data-validator-option="{stopOnError:false, timely:false}">
			<input type="hidden" name="course.courseId" value="${course.courseId}" />
			<input type="hidden" name="course.logo" value="${course.logo}" />
			<table style="line-height: 35px;">
				<tr>
					<td>
						<font color="red">*</font>课程名称:
					</td>
					<td>
						<input name="course.courseName" value="${course.courseName}" type="text" style="width: 580px;" data-rule="required;"/>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>专业分类:
					</td>
					<td style="text-align: left;">
						<input type="hidden" value="${course.subjectId}" id="returnId" name="course.subjectId" />
						<input type="hidden" id="returnIds" value="${course.subjectLink}" name="course.subjectLink" />
						<div id="levelId"></div>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>状态:
					</td>
					<td>
						<select id="isavaliable" class="valid" name="course.isavaliable">
							<option value="1">上架</option>
							<option value="2">下架</option>
						</select>
					</td>
					<script>
						$("#isavaliable").val('${course.isavaliable}');
					</script>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>总课时:
					</td>
					<td style="text-align: left;">
						<input name="course.lessionNum" value="${course.lessionNum}" type="text" style="width: 140px;" data-rule="required;integer[+0]"/>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>课程原价格:
					</td>
					<td style="text-align: left;">
						<input name="course.sourcePrice" type="text" value="${course.sourcePrice}" style="width: 140px;" data-rule="required;"/>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>课程销售价格:
					</td>
					<td style="text-align: left;">
						<input name="course.currentPrice" type="text" value="${course.currentPrice}" style="width: 140px;" data-rule="required;"/>
					</td>
				</tr>
				<tr>
					<td>
						<font color="red">*</font>有效期类型:
					</td>
					<td>
						<select id="losetype" name="course.loseType">
							<option value="0">到期时间</option>
							<option value="1">按天数</option>
						</select>
					</td>
					<script>
							$("#losetype").val('${course.loseType}');
					</script>
				</tr>
				<tr class="endTimeShow">
					<td>
						<font color="red">*</font>有效期结束时间:
					</td>
					<td style="text-align: left;">
						<input name="course.endTime" value="<fmt:formatDate value="${course.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly="readonly"
							id="endTime" type="text" style="width: 140px;"/>
					</td>
				</tr>
				<tr class="loseTimeShow" style="display: none;">
					<td align="center">
						<font color="red">*</font>按天数:
					</td>
					<td>
						<input id="loseTime" class="required number" type="text" onkeyup="this.value=this.value.replace(/\D/g,'')" value="${course.loseTime }" name="course.loseTime" style="width: 140px;"/>天
					</td>
				</tr>
				<tr>
					<td>添加教师:</td>
					<td style="text-align: left;">
						<input type="hidden" name="teacherIdArr" value="" />
						<div id="teacherList"></div>
						<a href="javascript:void(0)" onclick="selectTeacher()">选择老师</a>
					</td>
				</tr>
				<tr>
					<td>销售数量:</td>
					<td style="text-align: left;">
						<input name="course.pageBuycount" data-rule="required;integer[+0]" value="${course.pageBuycount}" type="text" style="width: 140px;" />
					</td>
				</tr>
				<tr>
					<td>浏览量:</td>
					<td style="text-align: left;">
						<input name="course.pageViewcount" data-rule="required;integer[+0]" value="${course.pageViewcount}" type="text" style="width: 140px;" />
					</td>
				</tr>
				<tr>
					<td>课程简介:</td>
					<td style="text-align: left;">
						<input name="course.title" data-rule="required;" value="${course.title}" type="text" style="width: 580px;" />
					</td>
				</tr>
				<tr>
					<td>课程图片：</td>
					<td style="text-align: left;">
						<c:choose>
							<c:when test="${course.logo!=null && course.logo!=''}">
							<%--<img id="showImage" width="278px" height="155" src="<%=staticImage%>${course.logo}" /> --%>	
							</c:when>
							<c:otherwise>
								<img id="showImage" width="278px" height="155" src="${ctx }/static/admin/assets/logo.png" />
							</c:otherwise>
						</c:choose>
						<input type="button" value="上传" id="imageFile" />
						<font color="red">(请上传 640*357(长X宽)像素 的图片)</font>
					</td>
				</tr>
				<tr>
					<td>课程详情:</td>
					<td style="text-align: left;">
						<textarea name="course.context" id="content" data-rule="required;">${course.context}</textarea>
					</td>
				</tr>

				<tr>
					<td colspan="2" align="center">
						<input onclick="saveCourse()"  class="btn btn-sm"  type="button" value="保存" />
						<input onclick="history.go(-1);"  class="btn btn-sm"  type="button" value="返回" />
					</td>
				</tr>
			</table>
		</form>
</body>
</html>
