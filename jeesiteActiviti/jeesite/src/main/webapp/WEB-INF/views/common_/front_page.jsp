<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript" src="${ctxStatic_}/common/admin/js/page.js"></script>
<c:if test="${page != null && page.total>0}">
	<div class="paging"> 
		<a href="javascript:goPage(1);" title="">首</a>
		<c:choose>
			<c:when test="${page.isFirstPage}">
				<a id="backpage" class="undisable" href="javascript:void(0)" title="">&lt;</a>
			</c:when>
			<c:otherwise>
				<a id="backpage" href="javascript:goPage(${page.prePage });" title="">&lt;</a>
			</c:otherwise>
		</c:choose>
		<c:choose>
			<c:when test="${page.isLastPage}">
				<a id="nextpage"href="javascript:void(0)" title="" class="undisable">&gt;</a>
			</c:when>
			<c:otherwise>
				<a id="nextpage" href="javascript:goPage(${page.nextPage});" title="">&gt;</a>
			</c:otherwise>
		</c:choose>
		<a href="javascript:goPage(${page.navigateLastPage});" title="">末</a>
		<div class="clear"></div>
	</div>


</c:if>
<%--
<script type="text/javascript">
    var totalPageSize =${page.totalPageSize};
    var currentPage =
    ${page.currentPage-1}<1 ? 1 :${page.currentPage};
    var totalPage = ${page.totalPageSize};
    showPageNumber();
</script>
 --%>

<script type="text/javascript">	
	//var totalPageSize=${page.total};
	var currentPage = ${page.pageNum-1}<1?1:${page.pageNum};
	var totalPage = ${page.pages};
	showPageNumber();
</script>  