<%--@elvariable id="item" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult"--%>
<%--@elvariable id="sentence" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult.Sentence"--%>
<%--@elvariable id="finding" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult.Finding"--%>

<%--@elvariable id="namespanCounter" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%--@elvariable id="sentenceCounter" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%--@elvariable id="itemCounter" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%--@elvariable id="index" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="evaluationBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.EvaluationBean"/>

<h1>PDF Extraction Demo - Ergebnisse evaluieren</h1>

<p class="text-info">Die nachfolgenden Ergebnisse können nun bestätigt und als neue Annotationen gespeichert werden.
    Im Anschluss daran können neue Modelle mit den erweiterten Annotationen erstellt werden.</p>

<p><a href="/pages/evaluationGrouped" class="btn btn-default" role="button"><span
        class="glyphicon glyphicon-compressed"></span> Gruppierte Ansicht</a></p>

<form action="/pages/evaluation" method="post">
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th class="col-md-2">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="selectAll" class="selectAll"/> <b>action</b>
                    </label>
                </div>
            </th>
            <th class="col-md-2">entity</th>
            <th class="col-md-8">finding</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${evaluationBean.resultList}" var="item" varStatus="itemCounter">
            <tr class="success">
                <td colspan="3">
                    Document: <c:out value="${item.documentName}"/>
                </td>
            </tr>

            <c:forEach items="${item.sentences}" var="sentence" varStatus="sentenceCounter">
                <tr class="info">
                    <td colspan="3">
                        Context: <c:out value="${sentence.text}"/>
                    </td>
                </tr>

                <c:forEach items="${sentence.findingsList}" var="finding" varStatus="namespanCounter">
                    <tr>
                        <td>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="ok"
                                           value="nsc__${itemCounter.index}__${sentenceCounter.index}__${namespanCounter.index}"/>
                                </label>
                            </div>
                        </td>
                        <td><c:out value="${finding.span.type}"/></td>
                        <td>
                        <b><c:out value="${finding.text}"/></b>
                        </td>
                    </tr>
                </c:forEach>
            </c:forEach>
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