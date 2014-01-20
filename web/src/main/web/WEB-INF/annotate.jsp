<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo - PDF Annotieren</h1>
<p class="text-info">Wählen Sie die PDF-Datei, für die Annotationen erfasst werden sollen. Sie werden zur brat-Oberfläche weitergeleitet.</p>

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
                    ${fileId}
                </td>
                <td><a class="btn btn-primary" href="/pages/export?type=brat&amp;id=${fileId}">annotate</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</form>


<form class="form-horizontal" style="margin-top: 20px;" role="form">
    <div class="well well-sm form-group">
        <a class="btn btn-default col-sm-2" href="/" role="button"><span class="glyphicon glyphicon-chevron-left"></span> zurück</a>
    </div>
</form>

<jsp:include page="common/footer.jsp"/>