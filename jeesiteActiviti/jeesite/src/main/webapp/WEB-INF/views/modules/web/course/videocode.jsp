<%@ page contentType="text/html;charset=UTF-8" language="java"	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/base.jsp"%>
<%-- 依赖上一个页面：course-infor.jsp;对应js:1.courseInfo.js 2.comment.js 3.<script src="http://vod.baofengcloud.com/html/script/bfcloud.js?v=2"></script>--%>
<c:choose>
	<c:when test="${videotype=='IFRAME'}"> <%-- SWF/56视频/IFRAME--%>
		<!-- 错误类型的 先用iframe承接 -->
		<iframe id="playervideoiframe" src="${videourl}" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>
		<script>
			$(function(){
				var width = $("#playervideoiframe").width();
				var height=$("#playervideoiframe").height();
				$("#playervideoiframe").attr("src","${videourl}&width=100%&height="+height);
			});
		</script>
	</c:when>
	<c:when test="${videotype=='CC'}"> <%-- cc视频 --%>
        <script src='http://p.bokecc.com/player?vid=${videourl}&siteid=${ccwebsitemap.cc.ccappID}&autoStart=true&width=100%&height=100%&playerid=51A2AD3118ACAD37&playertype=1' type='text/javascript'></script>
	</c:when>
	<c:when test="${videotype=='INXEDUVIDEO'}"> <%-- 因酷云--%>
		<div id="videoareaname" style="width: 100%;height: 100%"></div>
		<script>
			var vodparam = "${videourl}";
			cloudsdk.initplay("videoareaname",{"src":vodparam,"id":"cloudsdk","isautosize":"0"});//(id,json)http://www.baofengcloud.com/html/src/cloudsdk.js 继承 bfcloud.js?v=2
			var html =  $("video").parent().html();
			$("video").remove();
			if(html!=null&&html!=''){
				$("#videoareaname").html(html);
			}
		</script>
	</c:when>
	<c:when test="${videotype=='uploadVideo'}"><%--上传的视频 --%>
		<script type="text/javascript" src="/static_/common/ckplayer/ckplayer.js" charset="utf-8"></script>
		<div id="videoareaname" style="width: 100%;height: 100%"></div>
		<script type="text/javascript">
			var flashvars={
				f:'${ctx}${videourl}',
				c:0,
				p:1
			};
			var video=['${ctx}${videourl}->video/mp4'];
			CKobject.embed('/static_/common/ckplayer/ckplayer.swf','videoareaname','ckplayer_a1','100%','100%',false,flashvars,video);//
		</script>
	</c:when>
	<c:otherwise>
		<!-- 错误类型的 先用iframe承接 -->
		<iframe src="${videourl}" width="100%" height="100%" frameborder="0" scrolling="no"></iframe>
	</c:otherwise>
</c:choose>
