<%--@elvariable id="item" type="java.util.Map"--%>
<%--@elvariable id="nameSpan" type="opennlp.tools.util.Span"--%>
<%--@elvariable id="span" type="opennlp.tools.util.Span"--%>
<%--@elvariable id="sentence" type="de.uni.passau.fim.mics.ermera.opennlp.NameFinderResult.Sentence"--%>

<%--@elvariable id="index" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%--@elvariable id="namespanCounter" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%--@elvariable id="sentenceCounter" type="javax.servlet.jsp.jstl.core.LoopTagStatus"--%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="evaluationBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.EvaluationBean"/>

<h1>PDF Extraction Demo - Ergebnisse evaluieren</h1>

<p class="text-info">Die nachfolgenden Ergebnisse können nun bestätigt und als neue Annotationen gespeichert werden.
    Im Anschluss daran können neue Modelle mit den erweiterten Annotationen erstellt werden.</p>

<p><a href="/pages/evaluationgrouped" class="btn btn-default" role="button"><span
        class="glyphicon glyphicon-compressed"></span> Gruppierte Ansicht</a></p>

<form action="/pages/evaluation" method="post">
    <table class="table table-striped table-hover">
        <thead>
        <tr>
            <th>document</th>
            <th>entity</th>
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
        <c:forEach items="${evaluationBean.resultMap}" var="item">
            <c:forEach items="${item.value.sentences}" var="sentence" varStatus="sentenceCounter">
                <c:forEach items="${sentence.findings}" var="nameSpan" varStatus="namespanCounter">
                    <tr>
                        <td>${item.key}</td>
                        <td>
                            <c:forEach begin="0" end="${nameSpan.start-1}" varStatus="index">
                                ${sentence.tokens[index.index].text}
                            </c:forEach>
                            <b>
                                <c:forEach begin="${nameSpan.start}" end="${nameSpan.end}" varStatus="index">
                                    ${sentence.tokens[index.index].text}
                                </c:forEach>
                            </b>
                            <c:forEach begin="${nameSpan.end+1}" end="${fn:length(sentence.tokens)}" varStatus="index">
                                ${sentence.tokens[index.index].text}
                            </c:forEach>

                        </td>
                        <td>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox" name="ok"
                                           value="nsc__${item.key}__${sentenceCounter.index}__${namespanCounter.index}"/> OK!
                                </label>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </c:forEach>
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