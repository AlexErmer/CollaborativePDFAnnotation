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
                    ${fileId}
                </td>
                <td><a class="btn btn-primary" href="/pages/extract?type=knowminer&amp;id=${fileId}">extract</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</form>


<form class="form-horizontal" style="margin-top: 20px;" role="form" action="/">
    <div class="well well-sm form-group">
        <a class="btn btn-default col-sm-2" href="/" role="button"><span class="glyphicon glyphicon-chevron-left"></span> zurück</a>
    </div>
</form>

<jsp:include page="common/footer.jsp"/>