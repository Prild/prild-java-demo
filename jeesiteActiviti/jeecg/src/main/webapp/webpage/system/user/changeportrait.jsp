<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户信息</title>
<t:base type="jquery,easyui,tools"></t:base>
</head>
<body style="overflow-y: hidden" scroll="no">
<t:formvalid formid="formobj" refresh="false" dialog="true" action="userController.do?saveportrait" usePlugin="password" layout="table">
	<input id="id" type="hidden" value="${user.id }">
			<div class="container">
					<%--dangzhenghui begin 20170612 for 去掉路径--%>
					<%--数据库:\"avatar\":\"/jeecg/chat/imController/showOrDownByurl.do?dbPath=upload/files\\\\20170612\\\\e1fe9925bc315c60addea1b98eb1cb1349547719.jpg\",\"id\":\"8a8ab0b246dc81120146dc8181950052\",\"name\":\"管理员\",\"sign\":\"这家伙很你好\",\"type\":\"friend\"} --%>
				<t:webUploader auto="true" buttonText="选择头像" name="fileName" buttonStyle="btn-blue btn-S" type="image" fileNumLimit="3" displayTxt="flase"></t:webUploader>
					<%--dangzhenghui begin 20170612for 去掉路径--%>
				</div>
</t:formvalid>
</body>