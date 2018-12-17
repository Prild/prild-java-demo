<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/base.jsp"%>
<%--<script type="text/javascript" src="${ctx_s}/static_/common/web/js/page.js"></script> --%>

<c:if test="${page != null && page.total>0}">
	<div class="paging">
		<a href="javascript:goPageAjax(1);" title="">首</a>
		<c:choose>
			<c:when test="${page.isFirstPage}">
				<a id="backpage" class="undisable" href="javascript:void(0)" title="">&lt;</a>
			</c:when>
			<c:otherwise>
				<a id="backpage" href="javascript:goPageAjax(${page.prePage });" title="">&lt;</a>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${page.isLastPage}">
				<a id="nextpage"href="javascript:void(0)" title="" class="undisable">&gt;</a>
			</c:when>
			<c:otherwise>
				<a id="nextpage" href="javascript:goPageAjax(${page.nextPage});" title="">&gt;</a>
			</c:otherwise>
		</c:choose>
		<a href="javascript:goPageAjax(${page.navigateLastPage});" title="">末</a>
		<div class="clear"></div>
	</div>
</c:if>


	
<script type="text/javascript">
    //var totalPageSize =${page.total};
    var currentPage =${page.pageNum-1}<1 ? 1 :${page.pageNum};
    var totalPage = ${page.pages};
   // showAjaxPageNumber();
   	var pageHtml = "";
	var maxNum_new = currentPage > 4 ? 6 : 7 - currentPage;//最大显示页码数
	var discnt = 1;
	for ( var i = 3; i > 0; i--) {
		if (currentPage > i) {
			//pageHtml = pageHtml + "<li><a href='javascript:goPage(" + (currentPage - i) + ")'>" + (currentPage - i) + "</a></li>";
			pageHtml = pageHtml + "<a href='javascript:goPageAjax(" + (currentPage - i) + ")' title=''>" + (currentPage - i) + "</a>";
			discnt++;
		}
	}
	//pageHtml = pageHtml + '<li class="disabled"><a href="javascript:void(0)">' + currentPage + '</a></li>';
	pageHtml = pageHtml + '<a href="javascript:void(0)" title="" class="current undisable">' + currentPage + '</a>';
	for ( var i = 1; i < maxNum_new; i++) {
		if (currentPage + i <= totalPage && discnt < 7) {
			//pageHtml = pageHtml + "<li><a href='javascript:goPageAjax("+ (currentPage + i) + ")'>" + (currentPage + i) + "</a></li>";
			pageHtml = pageHtml + "<a href='javascript:goPageAjax(" + (currentPage + i) + ")' title=''>" + (currentPage + i) + "</a>";
			discnt++;
		} else {
			break;
		}
	}
	$(pageHtml).insertBefore("#nextpage");
</script>


