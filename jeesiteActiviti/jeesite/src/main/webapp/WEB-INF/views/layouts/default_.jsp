<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="sitemesh"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<%@ include file="/WEB-INF/base.jsp"%>
<%--<compress:html  compressJavaScript="false" >--%>
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
<link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/reset.css">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/theme.css">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/global.css">
<link rel="stylesheet" type="text/css" href="${ctx_s}/static_/inxweb/css/web.css">
<link href="${ctx_s}/static_/inxweb/css/mw_320_768.css" rel="stylesheet" type="text/css" media="screen and (min-width: 320px) and (max-width: 768px)">
<!--[if lt IE 9]><script src="${ctx}/static/common/html5.js"></script><![endif]-->
<script type="text/javascript" src="${ctx_s}/static_/common/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${ctx_s}/static_/common/webutils.js"></script>
<script type="text/javascript" src="${ctx_s}/static_/inxweb/header/header.js"></script>
<script type="text/javascript" src="${ctx_s}/static_/inxweb/js/common.js"></script>

<script type="text/javascript">
var ctx_s = '${ctx_s}';
var ctx_ = '${ctx_}';
var ctx = '${ctx}';
</script>


<sitemesh:head></sitemesh:head>
</head>
<body class="W-body">
	<div class="in-wrap" >
		<div class="changeTheme" style="display: none;">
			<div class="ei-item-box">
				<div class="container pr">
					<a class="fr" id="Replacement" href="javascript:void(0);">
						<img alt="" src="${ctx_s}/static_/inxweb/img/HF-BG.png">
					</a>
				</div>
			</div>
			<div class="ei-i-dialog pa" style="height: 120px; top: -120px; opacity: 0; z-index: -1;">
				<div style="height: 100%;" class="ei-i-dialog-box">
					<div class="ei-i-dialog-box-tit">
						<section class="container pr">
							<a title="收起" class="ei-i-close close_a pa" id="close_a" href="javascript: void(0)">
								<em class="vam up-hf"> <img alt="" src="${ctx_s}/static_/inxweb/img/up-hf.png">
								</em>
								<tt class="f-fM fsize14 vam">收起</tt>
							</a>
							<span> 主题切换 </span>
						</section>
					</div>
					<div class="ei-i-dialog-box-boy">
						<div class="container">
							<article class="dialog-box-boy-in">
								<ul class="clearfix">
									<li>
										<div id="themeColororange" class="box-boy-in-i  ">
											<a class="pr" href="javascript:changeThemeColor('orange')">
												<img class="pic" alt="" src="${ctx }/static_/inxweb/img/pic/one-master.jpg">
												<span class="pa name"> 网络课堂--模板一 </span> <em id="triangle-bottomright" class="xz"> &nbsp; </em>
												<div class="loging" style="display:none;">
													<img src="${ctx_s}/static_/inxweb/img/loding.gif" alt="">
												</div>
											</a>
										</div>
									</li>
									<li>
										<div id="themeColorgreen" class="box-boy-in-i" >
											<a class="pr" href="javascript:changeThemeColor('green')">
												<img class="pic" alt="" src="${ctx }/static_/inxweb/img/pic/two-green.jpg">
												<span class="pa name"> 网络课堂--模板二 </span> <em id="triangle-bottomright" class="xz"> &nbsp; </em>
												<div class="loging" style="display:none;">
													<img src="${ctx_s}/static_/inxweb/img/loding.gif" alt="">
												</div>
											</a>
										</div>
									</li>
									<li>
										<div id="themeColorblue" class="box-boy-in-i" >
											<a class="pr" href="javascript:changeThemeColor('blue')">
												<img class="pic" alt="" src="${ctx }/static_/inxweb/img/pic/three-blue.jpg">
												<span class="pa name"> 网络课堂--模板三 </span> <em id="triangle-bottomright" class="xz"> &nbsp; </em>
												<div class="loging" style="display:none;">
													<img src="${ctx_s}/static_/inxweb/img/loding.gif" alt="">
												</div>
											</a>
										</div>
									</li>
								</ul>
							</article>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 公共头引入 -->
		<jsp:include page="/WEB-INF/views/layouts/web/header_.jsp" />
		<!-- 公共头引入 -->
		<sitemesh:body></sitemesh:body>
		<!-- 公共底引入 -->
	 <jsp:include page="/WEB-INF/views/layouts/web/footer_.jsp" />  
		<!-- 公共底引入 -->
	</div>

	<!-- 统计代码 -->
	<%--${tongjiemap.censusCode.censusCodeString} --%>
</body>
</html>
<%--</compress:html>--%>
