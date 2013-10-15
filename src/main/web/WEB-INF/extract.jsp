<jsp:include page="header.jsp"/>

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

                <%--//TODO: do tooltips the bootstrap way!--%>
            <c:forEach items="${page.blocks}" var="block">
                <div class="${block.cssClass}${block.selectedBlock?' selectedAnnotation':''}" title="label"
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


<div id="text">

</div>


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
            $('#text')
                    .append(
                        $('<p>').append($('<input type="checkbox" />'))
                                .append($(this).text().trim())
                    );
        });
    }
</script>

<jsp:include page="footer.jsp"/>