<%--@elvariable id="fileId" type="java.util.List"--%>
<%--@elvariable id="type" type="java.Lang.String"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>


<h1>PDF Extraction Demo - Model erstellen</h1>

<p class="text-info">Wählen Sie die PDF-Dateien aus, deren Annotationen des ebenfalls festgelegten Types in ein
    neues Model fließen sollen.</p>

<form action="/pages/nlp" method="post">
    <div class="form-group">
        <label for="modelname">Modelname</label>
        <input type="text" id="modelname" class="form-control" name="modelname" placeholder="Enter modelname"/>
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
                            <input type="checkbox" name="files" value="${fileId.key}"/> ${fileId.key}
                        </label>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <footer class="navbar navbar-fixed-bottom">
        <div class="container">
            <div class="well well-sm form-group">
                <a class="btn btn-default col-sm-2" href="/" role="button"><span
                        class="glyphicon glyphicon-chevron-left"></span> zurück</a>
                <button type="submit" class="btn btn-primary col-sm-2 pull-right" name="create">Model erstellen</button>
            </div>
        </div>
    </footer>
</form>

<jsp:include page="common/footer.jsp"/>