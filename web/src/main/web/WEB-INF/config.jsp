<%--@elvariable id="item" type="java.lang.String"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<jsp:include page="common/header.jsp"/>

<jsp:useBean id="configBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.ConfigBean"/>

<h1>PDF Extraction Demo - Entitätstypen festlegen</h1>

<form action="/pages/config" method="post">
    <div class="form-group">
        <label for="newType">neuer Type</label>
        <input type="text" id="newType" class="form-control" name="newType" placeholder="Enter typename"/>
    </div>
    <div class="form-group">
        <input type="submit" class="btn btn-primary" value="Type anlegen"/>
    </div>

    <div class="form-group">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <th>type</th>
                <th>action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${configBean.types}" var="item">
                <tr>
                    <td>${item}</td>
                    <td>
                        <a class="btn btn-default" title="löschen" href="/pages/config?delete=${item}"
                           role="button">
                            <span class="glyphicon glyphicon-trash"></span> löschen
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</form>

<footer class="navbar navbar-fixed-bottom">
    <div class="container">
        <div class="well well-sm form-group">
            <a class="btn btn-default col-sm-2" href="/" role="button"><span
                    class="glyphicon glyphicon-chevron-left"></span> zurück</a>
            <a class="btn btn-primary col-sm-2 pull-right" href="/pages/upload" role="button">PDFs hochladen<span
                    class="glyphicon glyphicon-chevron-right"></span></a>
        </div>
    </div>
</footer>

<jsp:include page="common/footer.jsp"/>