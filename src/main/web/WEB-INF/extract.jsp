<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="Alexander Ermer">
    <title>Code PDF Extraction Demo Webapp</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/code.js"></script>
    <script src="http://d3js.org/d3.v2.min.js?2.9.3"></script>
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
                <li class="active"><a href="/index.jsp">Home</a></li>
                <li><a href="#about">About/Contact</a></li>
            </ul>
        </div>
    </div>
</div>

<div class="container">
    <div class="bodywrapper">
        <c:if test="${errorMessage != null && !errorMessage.isEmpty()}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <div class="output">

            <svg xmlns="http://www.w3.org/2000/svg" version="1.1" style="width:0px; height:0px">
                <defs>
                    <marker id="arrowMarker" viewBox="0 0 10 10" refX="1" refY="5"
                            markerUnits="strokeWidth" orient="auto" markerWidth="8" markerHeight="6">
                        <polyline points="0,0 10,5 0,10 1,5" fill="black"/>
                    </marker>
                </defs>
            </svg>

            <jsp:useBean id="documentBean" scope="request" class="de.uni.passau.fim.mics.ermera.beans.DocumentBean"/>
            <c:forEach items="${documentBean.pages}" var="page">
                <div class="page" style="position:relative; width: ${page.width + 4}px; height: ${page.height + 4}px">
                    <img src="image?file=${page.imagefilename}" width="${page.width}" height="${page.height}"
                         alt="Page ${page.number}"/>


                    <div class="svg"
                         style="position:absolute;left:0px;top:0px;width:${page.width}px;height:${page.height}px;">
                        <svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="${page.width}"
                             height="${page.height}">
                            <c:forEach items="${page.lines}" var="line">
                                <line x1="${line.x1}" y1="${line.y1}" x2="${line.x2}" y2="${line.y2}" stroke="black"
                                      stroke-width="2" marker-end="url(#arrowMarker)"/>
                            </c:forEach>
                        </svg>
                    </div>


                    <c:forEach items="${page.blocks}" var="block">
                        <div class="${block.cssClass}" title="label"
                             style="position:absolute;left:${block.left}px;top:${block.top}px;width:${block.width}px;height:${block.height}px;"
                             onmouseover="document.getElementById('tooltip_${block.id}').style.display='block'"
                             onmouseout="document.getElementById('tooltip_${block.id}').style.display='none'">
                            <div class="block-text">
                                    ${block.text}
                            </div>
                        </div>
                        <div id="tooltip_${block.id}" class="myTooltip"
                             style="position:absolute;left:${block.tooltipBean.left}px;top:${block.tooltipBean.top}px;width:200px;z-index:100;">
                            <b>Label:</b> ${block.tooltipBean.label}<br/>
                            <b>BBox:</b> ${block.tooltipBean.bbox}<br/>
                            <b>Text:</b> ${block.tooltipBean.text}<br/>
                            <b>Maj. Font:</b> ${block.tooltipBean.font}; size=${block.tooltipBean.size};
                            bold=${block.tooltipBean.bold}; italic=${block.tooltipBean.italic};
                        </div>
                    </c:forEach>
                </div>
            </c:forEach>

        </div>
        <div id="text">text</div>
    </div>
</div>

<script type="text/javascript" src="../js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="../js/bootstrap.js"></script>

<script type="text/javascript">
    $(document).ready(function() {
        $('.block').click(function() {
            $(this).toggleClass('selectedAnnotation');
            collectText();
        });
        scanTitleMain();
    });

    function scanTitleMain() {
        $(".block-title, .block-subtitle, .block-heading, .block-main").addClass('selectedAnnotation');
        collectText();
    }
    function collectText() {
        $('#text').empty();

        $(".selectedAnnotation").each(function() {
            $('#text').append($(this).text());
        });
    }
</script>
</body>
</html>