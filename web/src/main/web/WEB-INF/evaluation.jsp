<jsp:include page="header.jsp"/>
<jsp:useBean id="evaluationBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.EvaluationBean"/>


<a class="btn btn-default" href="/pages/homepage">&laquo; back</a>
<h1>evaluate Results</h1>

<table class="table table-striped table-hover">
    <thead>
    <tr>
        <th>document</th>
        <th>entity</th>
        <th>action</th>
        <th>context</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${evaluationBean.resultMap}" var="item">
        <c:forEach items="${item.value.nameSpans}" var="namespan" varStatus="namespanCounter">
            <tr>
                <td>${item.key}</td>
                <td>
                    <c:forEach begin="${namespan.start}" end="${namespan.end}" varStatus="index">
                        ${item.value.tokens[index.index]}
                    </c:forEach>
                </td>
                <td>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="ok" value="nsc_${namespanCounter}"/>
                        </label>
                    </div>
                </td>
                <td>context!</td>
            </tr>
        </c:forEach>
    </c:forEach>
    </tbody>
</table>

<jsp:include page="footer.jsp"/>