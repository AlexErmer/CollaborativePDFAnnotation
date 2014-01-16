<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="header.jsp"/>

<jsp:useBean id="evaluationBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.EvaluationBean"/>

<h1>PDF Extraction Demo - Ergebnisse evaluieren</h1>
<p class="text-info">Die nachfolgenden Ergebnisse können nun bestätigt und als neue Annotationen gespeichert werden.
Im Anschluss daran können neue Modelle mit den erweiterten Annotationen erstellt werden.</p>

<form action="/pages/evaluation" method="post">
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>document</th>
            <th>entity</th>
            <th>action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${evaluationBean.resultMap}" var="item">
            <c:forEach items="${item.value.nameSpans}" var="namespan" varStatus="namespanCounter">
                <tr>
                    <td>${item.key}</td>
                    <td>
                        <c:forEach begin="${namespan.start-evaluationBean.contextRange}" end="${namespan.start-1}"
                                   varStatus="index">
                            ${item.value.tokens[index.index]}
                        </c:forEach>
                        <b>
                            <c:forEach begin="${namespan.start}" end="${namespan.end-1}" varStatus="index">
                                ${item.value.tokens[index.index]}
                            </c:forEach>
                        </b>
                        <c:forEach begin="${namespan.end}" end="${namespan.end+evaluationBean.contextRange}"
                                   varStatus="index">
                            ${item.value.tokens[index.index]}
                        </c:forEach>
                    </td>
                    <td>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" name="ok" value="nsc__${item.key}__${namespanCounter.index}"/> OK!
                            </label>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </c:forEach>
        </tbody>
    </table>
    <button type="submit" class="btn btn-primary" name="submit">Submit</button>
</form>

<form class="form-horizontal" style="margin-top: 20px;" role="form">
    <div class="well well-sm form-group">
        <a class="btn btn-default col-sm-2" href="/" role="button"><span
                class="glyphicon glyphicon-chevron-left"></span> zurück</a>
    </div>
</form>

<jsp:include page="footer.jsp"/>