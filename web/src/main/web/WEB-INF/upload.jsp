<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo - Upload PDF</h1>
<p class="text-info">Wählen Sie die PDF-Datei, die in das System eingestellt werden soll.</p>

<form class="form-inline" action="/pages/upload" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <input type="file" class="form-control" id="pdfFile" name="pdfFile" accept="application/pdf;text/pdf"/>
    </div>
    <button type="submit" class="btn btn-primary">Upload</button>
</form>


<form class="form-horizontal" style="margin-top: 20px;" role="form">
    <div class="well well-sm form-group">
        <a class="btn btn-default col-sm-2" href="/" role="button"><span class="glyphicon glyphicon-chevron-left"></span> zurück</a>
    </div>
</form>

<jsp:include page="common/footer.jsp"/>