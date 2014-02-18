<%--@elvariable id="item" type="java.util.Map"--%>
<%--@elvariable id="listItem" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderGroupedResultListItem"--%>

<%--@elvariable id="namespan" type="opennlp.tools.util.Span"--%>
<%--@elvariable id="index" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%--@elvariable id="namespanCounter" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="evaluationBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.EvaluationBean"/>

<h1>PDF Extraction Demo - Ergebnisse evaluieren</h1>

<p class="text-info">Die nachfolgenden Ergebnisse können nun bestätigt und als neue Annotationen gespeichert werden.
    Im Anschluss daran können neue Modelle mit den erweiterten Annotationen erstellt werden.</p>

<p><a href="/pages/evaluation" class="btn btn-default" role="button"><span
        class="glyphicon glyphicon-list"></span> Listen Ansicht</a></p>

<form action="/pages/evaluationGrouped" method="post">
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>entity</th>
            <th>amount</th>
            <th>findings</th>
            <th>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="selectAll" class="selectAll"/> <b>action</b>
                    </label>
                </div>
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${evaluationBean.groupedResultMap}" var="item">
            <tr>
                <td>${item.key}</td>
                <td>${item.value.size()}</td>
                <td>
                    <ul>
                        <c:forEach items="${item.value}" var="listItem">
                            <li>${listItem.sentence.text}</li>
                        </c:forEach>
                    </ul>
                </td>
                <td>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="ok" value="nsc__${item.key}__"/>
                            OK!
                        </label>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <button type="submit" class="btn btn-primary" name="submit">Submit</button>
</form>

<form class="form-horizontal" style="margin-top: 20px;" role="form" action="/">
    <div class="well well-sm form-group">
        <a class="btn btn-default col-sm-2" href="/" role="button"><span
                class="glyphicon glyphicon-chevron-left"></span> zurück</a>
    </div>
</form>

<jsp:include page="common/footer.jsp"/>