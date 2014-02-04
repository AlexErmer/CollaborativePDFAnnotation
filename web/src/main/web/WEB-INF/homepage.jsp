<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo</h1>

<form class="form-horizontal" role="form" action="/">
    <div class="well well-sm form-group">
        <a class="btn btn-primary col-sm-2" href="/pages/upload" role="button">PDF hochladen</a>
        <p class="help-block col-sm-10">Laden Sie weitere PDF Dateien auf die Plattform um ihre Modelle zu verbessern.</p>
    </div>
    <div class="well well-sm form-group">
        <a class="btn btn-primary col-sm-2" href="/pages/config" role="button">Entitätstypen festlegen</a>
        <p class="help-block col-sm-10">Legen Sie ihre Entitätstypen fest.</p>
    </div>
    <div class="well well-sm form-group">
        <a class="btn btn-primary col-sm-2" href="/pages/extract" role="button">Text-Extraktion</a>
        <p class="help-block col-sm-10">Korrigieren Sie die automatische Text-Extraktion.</p>
    </div>
    <div class="well well-sm form-group">
        <a class="btn btn-primary col-sm-2" href="/pages/annotate" role="button">Annotieren</a>
        <p class="help-block col-sm-10">Annotieren Sie die extrahierten Texte.</p>
    </div>
    <div class="well well-sm form-group">
        <a class="btn btn-primary col-sm-2" href="/pages/modelCreate" role="button">Model erstellen</a>
        <p class="help-block col-sm-10">Erstellen Sie aus den annotierten Texten Modelle.</p>
    </div>
    <div class="well well-sm form-group">
        <a class="btn btn-primary col-sm-2" href="/pages/modelUse" role="button">Model anwenden</a>
        <p class="help-block col-sm-10">Wenden Sie ihre erstellten Modelle auf (weitere) Texte an und evaluieren Sie die
            Ergebnisse.
            Die akzeptierten Funde werden direkt als neue Annotationen gespeichert und können in neue Modelle
            einfließen.</p>
    </div>
</form>

<jsp:include page="common/footer.jsp"/>