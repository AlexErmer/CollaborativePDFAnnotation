<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo - Model anwenden</h1>
<p class="text-info">Wählen Sie das Model und die PDF-Dateien aus, auf die es anwendet werden soll.
    Die Ergebnisse werden auf der folgenden Seite präsentiert, auf der sie evaluiert werden.</p>

<form action="/pages/nlp" method="post">
    <div class="form-group">
        <label for="modelselect">Modelname</label>
        <select id="modelselect" name="modelselect">
            <c:forEach items="${indexBean.models}" var="model">
                <option value="${model}">${model}</option>
            </c:forEach>
        </select>
    </div>

    <table class="table table-hover table-striped">
        <thead>
        <tr>
            <th>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" name="selectAll" class="selectAll"/> <b>Filename</b>
                    </label>
                </div>
            </th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${indexBean.fileIds}" var="fileId">
            <tr>
                <td>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="files" value="${fileId}"/> ${fileId}
                        </label>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="form-group">
        <button type="submit" class="btn btn-primary" name="use">use Model</button>
    </div>
</form>


<form class="form-horizontal" style="margin-top: 20px;" role="form">
    <div class="well well-sm form-group">
        <a class="btn btn-default col-sm-2" href="/" role="button"><span
                class="glyphicon glyphicon-chevron-left"></span> zurück</a>
    </div>
</form>

<jsp:include page="common/footer.jsp"/>