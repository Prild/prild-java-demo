<%@page language="java" pageEncoding="UTF-8"%>
<%@page import="com.thinkgem.jeesite.common.constants.CommonConstants"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>  
<%@taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>

<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%!
/**图片、CSS、js静态资源文件地址*/
	static String staticServer = CommonConstants.staticServer;// http://127.0.0.1:18080/jeesite
	/**上传服务用服务器地址，访问时用staticImage，数据库中不存储域名*/
	static String uploadServerUrl=CommonConstants.uploadImageServer;// http://127.0.0.1:18080/jeesite
	 /**页面显示图片的前缀路径*/
    static String staticImage=CommonConstants.staticImage;// http://127.0.0.1:18080/jeesite

	//内容编辑器上传图片路径
	static String keuploadSimpleUrl = uploadServerUrl+"/image/keupload?";
	//图片上传路径 
	static String uploadSimpleUrl = uploadServerUrl+"/a/image_/gok4"; 
	%>

 <%--前台专用 --%>
<c:set var="ctx_" value="${pageContext.request.contextPath}${fns:getFrontPath()}"></c:set><%--/jeesite/f --%>
<c:set var="ctx_s" value="<%=CommonConstants.contextPath%>"></c:set><%--http://127.0.0.1:18080/jeesite --%>
<c:set var="ctximg" value="<%=CommonConstants.staticServer%>"></c:set><%--http://127.0.0.1:18080/jeesite --%>

<%--后台专用 --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set><%--/jeesite --%>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"></c:set><%--/jeesite/static--%>
<c:set var="ctxStatic_" value="${pageContext.request.contextPath}/static_"></c:set><%--/jeesite/static_--%>




