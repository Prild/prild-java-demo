<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="text/javascript" src="${ctxStatic_}/common/admin/js/page_.js"></script>
<c:if test="${page != null && page.total>0}">
	<hr />
	<div>
		<div class="paginationWrap">
			<div style="overflow: hidden; clear: both;">
				<div style="float: right;">
					<div class="pagination pagination-large">
						<ul>
							<c:choose>
								<c:when test="${page.isFirstPage}">
									<li class="disabled">
										<a href="javascript:void(0)">首页</a>
									</li>
									<li id="backpage" class="disabled">
										<a href="javascript:void(0)">←上一页</a>
									</li>
								</c:when>
								<c:otherwise>
									<li>
										<a href="javascript:goPage(1);">首页</a>
									</li>
									<li id="backpage">
										<a href="javascript:goPage(${page.prePage });">←上一页</a>
									</li>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${page.isLastPage}">
									<li id="nextpage" class="disabled">
										<a href="javascript:void(0)">下一页→</a>
									</li>
									<li class="disabled">
										<a href="javascript:void(0)">尾页 </a>
									</li>
								</c:when>
								<c:otherwise>
									<li id="nextpage">
										<a href="javascript:goPage(${page.nextPage});">下一页→</a>
									</li>
									<li>
										<a href="javascript:goPage(${page.navigateLastPage})">尾页 </a>
									</li>
								</c:otherwise>
							</c:choose>
							<li class="c_333">
								<tt class="ml10 disIb">&nbsp;第</tt>
								<input type="text" id="pageNoIpt" size="4" style="height: 16px; margin-top: 2px; width: 24px; border: 1px solid #999999;">
								<tt class="ml10 disIb">页&nbsp;</tt>
								<input type="button" onclick="goPageByInput()" value="确定" class="button">
								&nbsp;&nbsp;
							</li>
						</ul>
					</div>
				</div>
				<div style="float: left;" class="pageDesc">
					<span>共查询到&nbsp;${page.total}&nbsp;条记录，当前第&nbsp;${page.pageNum}/${page.pages}&nbsp;页</span>
				</div>
			</div>
		</div>
	</div>
</c:if>
<%-- ${page.pages 总页数}','${page.pageNum当前页数}','${page.pageSize每页条数}' --%>
<script type="text/javascript">	
	//var totalPageSize=${page.total};//总共多少条记录
	//var currentPage = ${page.pageNum-1}<1?1:${page.pageNum};//当前页
	var currentPage = ${page.pageNum-1}<1?1:${page.pageNum};
	var totalPage = ${page.pages};//总页数
	showPageNumber();//页码显示
</script>  