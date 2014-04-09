<%--@elvariable id="fileId" type="java.util.List"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo - PDF Annotieren</h1>

<c:if test="${indexBean.selectedFile == null}">
    <p class="text-info">Wählen Sie die PDF-Datei, für die Annotationen erfasst werden sollen. Sie werden zur
        brat-Oberfläche weitergeleitet.</p>

    <form action="/pages/nlp" method="post">
        <table class="table table-hover table-striped">
            <thead>
            <tr>
                <th>Filename</th>
                <th>annotate in brat</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${indexBean.fileIds}" var="fileId">
                <tr>
                    <td>
                        ${fileId.key}
                    </td>
                    <td>
                        <c:if test="${fileId.value}">
                            <a class="btn btn-primary" href="/pages/annotate?fileid=${fileId.key}">annotate</a>
                        </c:if>
                        <c:if test="${!fileId.value}">
                            no documentBean
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </form>
</c:if>

<c:if test="${indexBean.selectedFile != null}">
    <p class="text-info">Um auf der brat Oberfläche zu annotiert ist ein Login nötig.
        Hovern Sie den oberen blauen Balken und klicken Sie auf "Login":
    </p>
    <blockquote>Username: editor<br/>Passwort: annotate</blockquote>

    <iframe src="/pages/export?type=brat&amp;id=${indexBean.selectedFile}" width="100%" height="700">
    </iframe>
</c:if>

<form class="form-horizontal" style="margin-top: 20px;" role="form" action="/">
    <footer class="navbar navbar-fixed-bottom">
        <div class="container">
            <div class="well well-sm form-group">
                <a class="btn btn-default col-sm-2" href="/" role="button"><span
                        class="glyphicon glyphicon-chevron-left"></span> zurück</a>
                <c:if test="${indexBean.selectedFile != null}">
                    <a class="btn btn-default col-sm-2" href="/pages/annotate" role="button"><span
                            class="glyphicon glyphicon-chevron-left"></span> zurück zur Übersicht</a>
                </c:if>
                <c:if test="${not empty requestScope.nextDocumentbeanID}">
                    <a class="btn btn-default pull-right" href="/pages/annotate?fileid=${requestScope.nextDocumentbeanID}">nächstes
                        Dokument
                        <span class="glyphicon glyphicon-chevron-right"></span></a>
                </c:if>

                <a class="btn btn-primary col-sm-2 pull-right" href="/pages/modelCreate" role="button">Modell erstellen <span
                        class="glyphicon glyphicon-chevron-right"></span></a>
                <c:if test="${indexBean.selectedFile == null}">
                    <a class="btn btn-default col-sm-2 pull-right" href="/pages/export?type=brat&all=true" role="button">alle nach brat exportieren <span
                            class="glyphicon glyphicon-chevron-right"></span></a>
                </c:if>
            </div>
        </div>
    </footer>
</form>


<jsp:include page="common/footer.jsp"/>