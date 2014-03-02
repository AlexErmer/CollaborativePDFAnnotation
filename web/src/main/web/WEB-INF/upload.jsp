<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo - Upload PDF</h1>
<p class="text-info">Wählen Sie die PDF-Dateien, die in das System eingestellt werden soll. Bitte beachten Sie, dass dieser Vorgang einige Zeit in Anspruch nehmen kann!</p>

<form class="form-inline" action="/pages/upload" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <input type="file" class="form-control" id="pdfFile" name="pdfFile[]" accept="application/pdf;text/pdf" multiple="multiple"/>
    </div>
    <input type="submit" class="btn btn-primary" value="Upload"/>
</form>
<p class="help-block">Es können mehrere Dateien gleichzeitig hochgeladen werden.</p>


<footer class="navbar navbar-fixed-bottom">
    <div class="container">
        <div class="well well-sm form-group">
            <a class="btn btn-default col-sm-2" href="/" role="button"><span
                    class="glyphicon glyphicon-chevron-left"></span> zurück</a>
            <a class="btn btn-primary col-sm-2 pull-right" href="/pages/extract" role="button">Text-Extraktion<span
                    class="glyphicon glyphicon-chevron-right"></span></a>
        </div>
    </div>
</footer>

<jsp:include page="common/footer.jsp"/>