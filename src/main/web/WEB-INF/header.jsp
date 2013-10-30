<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Alexander Ermer">
    <title>Code PDF Extraction Demo Webapp</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/css/ui-lightness/jquery-ui-1.10.3.custom.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css"/>

    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.10.3.custom.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/code.js"></script>
</head>
<body>
<a name="top"></a>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a href="#" class="navbar-brand">PDF Extraction Demo</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="/">Home</a></li>
                <li><a href="/contact.jsp">About/Contact</a></li>
            </ul>
            <c:if test="${not empty sessionScope.profile}">
                <div class="navbar-form navbar-right">
                    <a href="/pages/logout" class="btn btn-default">Logout</a>
                </div>
            </c:if>
        </div>
    </div>
</div>

<div class="container">
    <div class="bodywrapper">

        <c:if test="${successMessage != null && !successMessage.isEmpty()}">
            <div class="alert alert-success">${successMessage}</div>
        </c:if>
        <c:if test="${errorMessage != null && !errorMessage.isEmpty()}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>
        <c:if test="${infoMessage != null && !infoMessage.isEmpty()}">
            <div class="alert alert-warn">${infoMessage}</div>
        </c:if>