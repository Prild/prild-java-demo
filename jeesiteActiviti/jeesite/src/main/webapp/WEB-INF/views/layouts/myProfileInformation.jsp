<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="sitemesh"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress"%>
<%@ include file="/WEB-INF/base.jsp"%>
<compress:html compressJavaScript="false">
<!DOCTYPE HTML>
<!--[if lt IE 7]>      <html class="lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html>
<!--<![endif]-->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0,user-scalable=no,minimal-ui">
<title><sitemesh:title></sitemesh:title>
<c:if test="${not empty websitemap.web}">
${websitemap.web}
</c:if>
<c:if test="${empty websitemap.web}">
${websitemap.web} is null
</c:if>
</title>
<%--
<title><sitemesh:title></sitemesh:title>-${websitemap.web.company}</title>
<meta name="author" content="${websitemap.web.author}" />
<meta name="keywords" content="${websitemap.web.keywords}" />
<meta name="description" content="${websitemap.web.description}" /> --%>
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />
<meta content="telephone=no" name="format-detection" />
<link rel="shortcut icon" href="${ctx_s}/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/reset.css">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/theme.css">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/global.css">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/web.css">
<link href="${ctx_s}/static_/inxweb/css/mw_320_768.css" rel="stylesheet" type="text/css" media="screen and (min-width: 320px) and (max-width: 768px)">
<script type="text/javascript" src="${ctx_s}/static_/common/webutils.js"></script>
<script type="text/javascript" src="${ctx_s}/static_/common/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctx_s}/static_/inxweb/user/ucTop.js"></script>
<script type="text/javascript" src="${ctx_s}/static_/inxweb/header/header.js"></script>
<script type="text/javascript" src="${ctx_s}/static_/inxweb/js/common.js"></script>

<!--[if lt IE 9]><script src="${ctx_s}/static_/common/html5.js"></script><![endif]-->
<sitemesh:head>
</sitemesh:head>

</head>
<body>
	<div class="in-wrap">
		<!-- 公共头引入，开始 -->
		<jsp:include page="/WEB-INF/views/layouts/web/header_.jsp"></jsp:include>
		<!-- 公共头引入，结束 -->
		<div class="bg-fa of">
			<!-- /个人中心 主体 开始 -->
			<section class="container">
				<div class="u-body mt40">
					<!-- 左侧，开始 -->
					<jsp:include page="/WEB-INF/views/layouts/web/left_uc.jsp"></jsp:include>
					<!-- 左侧，结束 -->
					<sitemesh:body></sitemesh:body>
					<!-- /右侧内容区 结束 -->
					<div class="clear"></div>
				</div>
			</section>
			<!-- /个人中心 主体 结束 -->
		</div>
		<!-- 公共尾引入，开始 -->
		 <jsp:include page="/WEB-INF/views/layouts/web/footer_.jsp" />  
		<!-- 公共尾引入，结束 -->
	</div>

	<!-- 统计代码 -->
<%--	${tongjiemap.censusCode.censusCodeString} --%>
</body>

</html>
</compress:html>