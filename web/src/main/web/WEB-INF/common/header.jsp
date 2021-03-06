<%--@elvariable id="messageUtil" type="de.uni.passau.fim.mics.ermera.common.MessageUtil"--%>
<%--@elvariable id="message" type="de.uni.passau.fim.mics.ermera.common.Message"--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Alexander Ermer">
    <meta http-equiv="pragma" content="no-cache">
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

<header class="navbar navbar-inverse navbar-fixed-top">
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
                <ul class="nav navbar-nav navbar-right">
                    <li><p class="navbar-text">Hallo ${sessionScope.profile.main.name}!</p></li>
                    <li><a href="/pages/logout">Logout</a></li>
                </ul>
            </c:if>
        </div>
    </div>
</header>

<div class="container">
    <div class="bodywrapper">

        <c:forEach items="${messageUtil.messages}" var="message">
            <c:if test="${message.type == 'SUCCESS'}">
                <div class="alert alert-success">
            </c:if>
            <c:if test="${message.type == 'ERROR'}">
                <div class="alert alert-danger">
            </c:if>
            <c:if test="${message.type == 'INFO'}">
                <div class="alert alert-warning">
            </c:if>
            ${message.msg}</div>
        </c:forEach>
