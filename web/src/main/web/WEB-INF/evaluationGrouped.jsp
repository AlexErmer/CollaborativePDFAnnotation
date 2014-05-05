<%--@elvariable id="item" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderGroupedResultListItem"--%>
<%--@elvariable id="listItem" type="de.uni.passau.fim.mics.ermera.opennlp.SingleNameFinderResult"--%>
<%--@elvariable id="sentence" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult.Sentence"--%>
<%--@elvariable id="finding" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult.Finding"--%>
<%--@elvariable id="itemCounter" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%--@elvariable id="index" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="evaluationBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.EvaluationBean"/>

<h1>PDF Extraction Demo - Ergebnisse evaluieren</h1>

<p class="text-info">Die nachfolgenden Ergebnisse können nun bestätigt und als neue Annotationen gespeichert werden.
    Im Anschluss daran können neue Modelle mit den erweiterten Annotationen erstellt werden.</p>

<p><a href="/pages/evaluation" class="btn btn-default" role="button"><span
        class="glyphicon glyphicon-list"></span> Listen Ansicht</a></p>

<form action="/pages/evaluationGrouped" method="post">
    <input type="hidden" name="grouped" value="true"/>
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>entity</th>
            <th>amount</th>
            <th>text</th>
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
        <c:forEach items="${evaluationBean.groupedResultList}" var="item" varStatus="itemCounter">
            <tr>
                <td>${item.finding.type}</td>
                <td>${item.list.size()}</td>
                <td>${item.finding.text}</td>
                <td>
                    <ul>
                        <c:forEach items="${item.list}" var="listItem">
                            <li>
                                <c:if test="${listItem.finding.span.start-1 > 0}">
                                    <c:forEach begin="0" end="${listItem.finding.span.start-1}" varStatus="index">
                                        ${listItem.sentence.tokens[index.index].text}
                                    </c:forEach>
                                </c:if>
                                <b>
                                        ${listItem.finding.text}
                                </b>
                                <c:forEach begin="${listItem.finding.span.end+1}"
                                           end="${fn:length(listItem.sentence.tokens)}"
                                           varStatus="index">
                                    ${listItem.sentence.tokens[index.index].text}
                                </c:forEach>
                            </li>
                        </c:forEach>
                    </ul>
                </td>
                <td>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="ok" value="nsc__${itemCounter.index}"/>
                            OK!
                        </label>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <footer class="navbar navbar-fixed-bottom">
        <div class="container">
            <div class="well well-sm form-group">
                <a class="btn btn-default col-sm-2" href="/" role="button"><span
                        class="glyphicon glyphicon-chevron-left"></span> zurück</a>
                <button type="submit" class="btn btn-primary col-sm-2 pull-right" name="submit">Speichern</button>
            </div>
        </div>
    </footer>
</form>

<jsp:include page="common/footer.jsp"/>