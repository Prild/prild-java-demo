<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="decorator" content="myself"></meta>	
<title>学员列表</title>
<link rel="stylesheet" href="${ctxStatic_}/common/admin/js/layui-master/dist/css/layui.css" type="text/css"/>
<script type="text/javascript" src="${ctxStatic_}/others/admin/user/user.js"></script>

<script>
var ctx = '${ctx}';
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
</head>

<body> 
<!-- /////////////////////////form 搜索部分 ///////////////////////////-->
		<form action="${ctx}/studentuser/getuserList" method="post" id="searchForm">
			<input type="hidden" id="pageCurrentPage" name="page.pageNum" value="1" />
			<input placeholder="邮箱/手机/昵称/姓名" type="text" name="queryUser.keyWord" value="${queryUser.keyWord}" />
			<select name="queryUser.isavalible">
				<option value="0">请选择状态</option>
				<option <c:if test="${queryUser.isavalible==1}"> selected="selected" </c:if> value="1">正常</option>
				<option <c:if test="${queryUser.isavalible==2}"> selected="selected" </c:if> value="2">冻结</option>
			</select>
			注册时间:
			<input placeholder="开始注册时间" name="queryUser.beginCreateTime"
				value="<fmt:formatDate value="${queryUser.beginCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" id="beginCreateTime" type="text"
				readonly="readonly" style="width: 128px;"/>
			-
			<input placeholder="结束注册时间" id="endCreateTime" name="queryUser.endCreateTime"
				value="<fmt:formatDate value="${queryUser.endCreateTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" type="text" readonly="readonly" style="width: 128px;"/>
			
			<button class="btn btn-primary btn-sm" id="searchForm">查询</button> 
			<button class="btn btn-sm" onclick="javascript:$('#searchForm input:text').val('');$('#searchForm select').val(0);">重置</button> 
			
			<a title="导出Excel" onclick="userExcel()" class="button tooltip" href="javascript:void(0)">
				<span class="ui-icon ui-icon-cancel"></span>
				导出Excel
			</a>
	</form>
		
<!-- /////////////////table显示信息区域 ////////////////////////////////////-->	
		<table class="table table-striped">
			<thead>
				<tr>
					<td align="center">邮箱</td>
					<td align="center">手机号</td>
					<td align="center">用户名</td>
					<td align="center">昵称</td>
					<td align="center">性别</td>
					<td align="center">年龄</td>
					<td align="center">注册时间</td>
					<td align="center">状态</td>
					<td align="center" width="285">操作</td>
				</tr>
			</thead>

			<tbody>
				<c:forEach items="${userList}" var="user">
					<tr class="odd">
						<td align="center">${user.email}</td>
						<td align="center">${user.mobile}</td>
						<td align="center">
							<c:choose>
								<c:when test="${user.userName!=null && user.userName!=''}">
					${user.userName}
					</c:when>
								<c:otherwise>--</c:otherwise>
							</c:choose>
						</td>
						<td align="center">
							<c:choose>
								<c:when test="${user.showName!=null && user.showName!=''}">
					${user.showName}
					</c:when>
								<c:otherwise>--</c:otherwise>
							</c:choose>
						</td>
						<td align="center">
							<c:if test="${user.sex==0}">--</c:if>
							<c:if test="${user.sex==1}">男</c:if>
							<c:if test="${user.sex==2}">女</c:if>
						</td>
						<td align="center">${user.age}</td>
						<td align="center">
							<fmt:formatDate value="${user.createTime}" pattern="yyyy/MM/dd HH:mm" />
						</td>
						<td align="center">
							<c:if test="${user.isavalible==1}">正常</c:if>
							<c:if test="${user.isavalible==2}">冻结</c:if>
						</td>
						<td align="center">
						
						<!-- example:<a class="btn btn-primary" onClick="delcfm('${ctx}/teacher/delete/${tc.id} ')">删除</a>  -->
						
						<!-- <a class="btn btn-primary"   onclick="initUpdatePwd(${user.userId})">修改密码</a> -->	
							<button class="btn btn-primary" data-toggle="modal" data-target="#myModal" onclick="initUpdatePwd(${user.userId})">修改密码</button>
							<samp id="frozenOrThaw${user.userId}">
								<c:if test="${user.isavalible==1}">
									<button  onclick="frozenOrThaw(${user.userId},2,this)" class="btn btn-primary">冻结</button>
								</c:if>
								<c:if test="${user.isavalible==2}">
									<button  onclick="frozenOrThaw(${user.userId},1,this)" class="btn btn-primary">解冻</button>
								</c:if>
							</samp>
						<!-- 	<a href="${ctx}/user/lookuserlog/${user.userId}" class="btn btn-primary">查看日志</a> -->
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
<!-- /////////////////////////////////分页部分////////////////////////////////////////////////////////////// -->	
	<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" /> 

<!-- /////////////////////////////修改密码窗口 ,开始/////////////////////////////////////////////////////-->
<!-- 
	<div id="updateWin" aria-labelledby="ui-dialog-title-dialog" role="dialog" tabindex="-1"
		class="ui-dialog ui-widget ui-widget-content ui-corner-all ui-draggable ui-resizable"
		style="display: none; position: absolute; overflow: hidden; z-index: 1010; outline: 0px none; height: auto; width: 511px; top: 173px; left: 367px;">
		<div style="-moz-user-select: none;" unselectable="on" class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">
			<span style="-moz-user-select: none;" unselectable="on" id="ui-dialog-title-dialog" class="ui-dialog-title">修改用户修改</span>
			<a style="-moz-user-select: none;" unselectable="on" role="button" class="ui-dialog-titlebar-close ui-corner-all"
				href="javascript:void(0)">
				<span style="-moz-user-select: none;" unselectable="on" class="ui-icon ui-icon-closethick">close</span>
			</a>
		</div>
		<div style="height: 300px; min-height: 42px; width: auto;" class="ui-dialog-content ui-widget-content">
			<form id="updateUserPwdForm">
				<input type="hidden" name="user.userId" value="0" />
				<table style="line-height: 35px;">
					<tr>
						<td>密码：</td>
						<td>
							<input name="user.password" type="password" />
						</td>
					</tr>
					<tr>
						<td>确定密码：</td>
						<td>
							<input name="passwords" type="password" />
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
			<button class="ui-state-default ui-corner-all" onclick="updateUserPwd()" type="button">确定</button>
			<button class="ui-state-default ui-corner-all closeBut" type="button">取消</button>
		</div>
	</div>    -->
	<!--///////////////////////////////// 修改密码窗口 ,结束////////////////////////////////////////////////////-->
	
	<!-- ///////////////////////////////bootstrap风格弹出框 修改密码//////////////////////////////////////////// -->
	
	<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
					&times;
				</button>
				<h4 class="modal-title" id="myModalLabel">
					修改密码
				</h4>
			</div>
			<div class="modal-body">
			<form id="updateUserPwdForm">
				<input  type="hidden"  name="user.userId" value="0" />
				<table style="line-height: 35px;">
					<tr>
						<td>密码：</td>
						<td>
							<input name="user.password" type="password" />
						</td>
					</tr>
					<tr>
						<td>确定密码：</td>
						<td>
							<input name="passwords" type="password" />
						</td>
					</tr>
				</table>
			</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal" >关闭
				</button>
				<button type="button" class="btn btn-primary"  onclick="updateUserPwd()" >
					提交更改
				</button>
				
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
</body>
</html>