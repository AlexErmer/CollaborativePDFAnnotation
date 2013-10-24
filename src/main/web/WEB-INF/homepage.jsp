<jsp:include page="header.jsp"/>

<jsp:useBean id="indexBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.IndexBean"/>

<h1>PDF Extraction Demo</h1>

<form action="/pages/upload" method="post" enctype="multipart/form-data">
    Choose a PDF to upload:
    <input name="pdfFile" type="file" accept="application/pdf;text/pdf"/><br/>
    <input type="submit"/>
</form>

<h1>Repository</h1>

<ul>
    <c:forEach items="${indexBean.fileIds}" var="fileId">
        <li><a href="/pages/extract?id=${fileId}"> ${fileId}</a></li>
    </c:forEach>
</ul>

<jsp:include page="footer.jsp"/>