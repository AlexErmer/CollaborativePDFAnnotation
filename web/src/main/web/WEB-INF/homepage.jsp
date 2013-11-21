<jsp:include page="header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo</h1>

<form action="/pages/upload" method="post" enctype="multipart/form-data">
    <div class="form-group">
        <label for="pdfFile">PDF Upload</label>
        <p class="help-block">Choose a PDF to upload:</p>
        <input type="file" id="pdfFile" name="pdfFile" accept="application/pdf;text/pdf"/>
        <button type="submit" class="btn btn-default">Upload</button>
    </div>
</form>

<h1>Repository</h1>

<form action="/pages/nlp" method="post">
    <table class="table table-hover table-striped">
        <thead>
            <tr>
                <th>Filename</th>
                <th>extract text</th>
                <th>annotate in brat</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach items="${indexBean.fileIds}" var="fileId">
            <tr>
                <td>
                    <div class="checkbox">
                        <label>
                            <input type="checkbox" name="files" value="${fileId}" /> ${fileId}
                        </label>
                    </div>
                </td>
                <td><a class="btn btn-default" href="/pages/extract?type=knowminer&amp;id=${fileId}">extract</a></td>
                <td><a class="btn btn-default" href="/pages/export?type=brat&amp;id=${fileId}">annotate</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="form-group">
        <label for="modelname">Modelname</label>
        <input type="text" id="modelname" class="form-control" name="modelname" placeholder="Enter modelname"/>
    </div>
    <div class="form-group">
        <label for="modelselect">Modelname</label>
        <select id="modelselect" name="modelselect">
            <c:forEach items="${indexBean.models}" var="model">
                <option value="${model}">${model}</option>
            </c:forEach>
        </select>
    </div>
    <button type="submit" class="btn btn-default" name="create">create Model</button>
    <button type="submit" class="btn btn-default" name="use">use Model</button>
</form>

<jsp:include page="footer.jsp"/>