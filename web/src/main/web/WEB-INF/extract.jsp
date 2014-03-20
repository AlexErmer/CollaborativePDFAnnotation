<%--@elvariable id="fileId" type="java.util.List"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo - Extract - Auswahl</h1>

<p class="text-info">Wählen Sie die PDF-Datei, bei der die automatische Text-Extraktion korrigiert werden soll.</p>

<form action="/pages/nlp" method="post">
    <table class="table table-hover table-striped">
        <thead>
        <tr>
            <th>Filename</th>
            <th>extract text</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${indexBean.fileIds}" var="fileId">
            <tr>
                <td>
                    ${fileId.key}
                </td>
                <td>
                    <c:if test="${!fileId.value}">
                        <span class="glyphicon glyphicon glyphicon-flash"></span>
                    </c:if>
                    <a class="btn btn-primary" href="/pages/extract?type=knowminer&amp;id=${fileId.key}">extract</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a class="btn btn-default col-sm-2" href="/pages/extract?type=knowminer&amp;all=true" role="button"><span
        class="glyphicon glyphicon-flash"></span> alle generieren</a>
</form>

<footer class="navbar navbar-fixed-bottom">
    <div class="container">
        <div class="well well-sm form-group">
            <a class="btn btn-default col-sm-2" href="/" role="button"><span
                    class="glyphicon glyphicon-chevron-left"></span> zurück</a>
            <a class="btn btn-primary col-sm-2 pull-right" href="/pages/annotate" role="button">Annotieren<span
                    class="glyphicon glyphicon-chevron-right"></span></a>
        </div>
    </div>
</footer>

<jsp:include page="common/footer.jsp"/>