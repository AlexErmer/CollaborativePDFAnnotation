<div id="paginator">
    <ul class="pagination pagination-sm">
        <c:choose>
            <c:when test="${param.pageNumber == 1}">
                <li class="disabled"><a href="#">&laquo;</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="${param.link}1">&laquo;</a></li>
            </c:otherwise>
        </c:choose>
        <c:forEach begin="1" end="${param.pages}" var="i">
            <c:choose>
                <c:when test="${param.pageNumber == i}">
                    <li class="active"><a href="#">${i}<span class="sr-only">(current)</span></a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="${param.link}${i}">${i}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:choose>
            <c:when test="${param.pageNumber == param.pages}">
                <li class="disabled"><a href="#">&raquo;</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="${param.link}${param.pages}">&raquo;</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>