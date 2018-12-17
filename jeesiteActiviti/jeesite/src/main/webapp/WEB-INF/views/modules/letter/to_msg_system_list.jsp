<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="decorator" content="myself"></meta>	
    <title>系统消息列表</title>
<link rel="stylesheet" href="${ctxStatic_}/common/admin/js/layui-master/dist/css/layui.css" type="text/css"/>
<script>
layui.use('laydate', function(){
	  var laydate = layui.laydate;
	  laydate.render({
		elem: '#startDate' //指定元素
		,type: 'datetime'  
	  });
	});
</script>
<script>
layui.use('laydate', function(){
	  var laydate = layui.laydate;
	  laydate.render({
		  elem: '#endDate' //指定元素
		  ,type: 'datetime'  
	  });
	});
</script>

<script type="text/javascript">
    var ctx = '${ctx}';
        $(function () {
            $("#status").val('${msgSystem.status}');
        });
        function submitSearch(){
            $("#searchForm").submit();
        }
        function clean(){
            $("input:text").val('');
            $("select").val('0');
        }
        /**
         * 全选
         * @param cb
         */
        function allselect(cb) {
            $("input[class='checkbox']").prop('checked', cb.checked);
        }
        /**
         * 批量删除
         */
        function deleteBatch(){
            var checked = false;
            var str = "";
            $(".checkbox").each(function(){
                if($(this).prop("checked")){
                    str+=this.value+",";
                    checked=true;
                }
            });
            if (!checked) {
                alert("请至少选择一条信息");
                return;
            }
            str=str.substring(0,str.length-1);
            if (confirm("确定删除所选消息吗？")) {
                $.ajax({
                    url:"${ctx}/letter/delsystem",
                    data:{"ids":str},
                    type:"post",
                    dataType:"json",
                    success:function(result){
                        if(result.success){
                            alert(result.message);
                            window.location.href=ctx+"/letter/systemmsglist";
                        }else{
                            alert(result.message);
                        }
                    }
                });
            }
        }
    </script>
</head>
<body>

<%--<div class="page_head">
    <h4><em class="icon14 i_01"></em>&nbsp;<span>系统管理</span> &gt; <span>系统消息列表</span></h4>
</div>--%>
<!-- /tab1 begin-->
<div class="pad20">
        <form action="${ctx}/letter/systemmsglist" name="searchForm" id="searchForm" method="post">
            <input id="pageCurrentPage" type="hidden" name="page.currentPage" value="0"/>
            内容：
            <input type="text" name="msgSystem.content" value="${msgSystem.content}" />
            消息添加时间：
            <input id="startDate" type="text" name="msgSystem.addTime" readonly="readonly" value='<fmt:formatDate value="${msgSystem.addTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
            -
            <input id="endDate" type="text" name="msgSystem.endTime" readonly="readonly" value='<fmt:formatDate value="${msgSystem.endTime}" type="both" pattern="yyyy-MM-dd HH:mm:ss"/>'/>
            状态：
            <select id="status" name="msgSystem.status">
                <option value="0">正常</option>
                <option value="1">已删除</option>
                <option value="2">已过期</option>
            </select>

            <button type="button" class="btn btn-small"  onclick="submitSearch()">查询</button>
            <button type="button" class="btn btn-small"  onclick="clean()">清空</button>
            <button type="button" class="btn btn-danger" onclick="deleteBatch()" >批量删除</button>
            <table class="table table-striped">
                <thead>
                <tr>
                    <th ><span><input type="checkbox" onclick="allselect(this)"/></span></th>
                    <th ><span>消息内容</span></th>
                    <th ><span>注册时间</span></th>
                    <th ><span>状态</span></th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${not empty nsgSystemList}">
                    <c:forEach items="${nsgSystemList}" var="list">
                        <tr class="odd">
                            <td><input type="checkbox" class="checkbox" value="${list.id_}"/></td>
                            <td><div style="overflow-y: auto;height:auto;">${list.content }</div></td>
                            <td><fmt:formatDate value="${list.addTime}" type="both"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${list.status==1}">已删除</c:when>
                                    <c:when test="${list.status==2}">已过期</c:when>
                                    <c:otherwise>正常</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${empty nsgSystemList}">
                    <tr>
                        <td align="center" colspan="16">
                            <div class="tips">
                                <span>暂无相关信息！</span>
                            </div>
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </form>
        <!-- /pageBar begin -->
<jsp:include page="/WEB-INF/views/common_/admin_page.jsp" />
        <!-- /pageBar end -->
</div>
</body>
</html>
