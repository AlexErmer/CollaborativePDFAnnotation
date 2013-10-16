<jsp:include page="header.jsp"/>

<div id="PDFoutput">

    <svg xmlns="http://www.w3.org/2000/svg" version="1.1" style="width:0; height:0">
        <defs>
            <marker id="arrowMarker" viewBox="0 0 10 10" refX="1" refY="5"
                    markerUnits="strokeWidth" orient="auto" markerWidth="8" markerHeight="6">
                <polyline points="0,0 10,5 0,10 1,5" fill="black"></polyline>
            </marker>
        </defs>
    </svg>

    <jsp:useBean id="documentBean" scope="request" class="de.uni.passau.fim.mics.ermera.beans.DocumentBean"/>
    <c:forEach items="${documentBean.pages}" var="page">
        <div class="page" style="position:relative; width: ${page.width + 4}px; height: ${page.height + 4}px">
            <img src="image?file=${page.imagefilename}" width="${page.width}" height="${page.height}"
                 alt="Page ${page.number}"/>


            <div class="svg"
                 style="position:absolute;left:0;top:0;width:${page.width}px;height:${page.height}px;">
                <svg xmlns="http://www.w3.org/2000/svg" version="1.1" width="${page.width}"
                     height="${page.height}">
                    <c:forEach items="${page.lines}" var="line">
                        <line x1="${line.x1}" y1="${line.y1}" x2="${line.x2}" y2="${line.y2}" stroke="black"
                              stroke-width="2" marker-end="url(#arrowMarker)"></line>
                    </c:forEach>
                </svg>
            </div>


            <c:forEach items="${page.blocks}" var="block">
                <div id="${block.id}"
                     data-id="${block.id}"
                     class="${block.cssClass} ${block.selectedBlock?'selectedAnnotation':'draggable'}"
                     title="label"
                     style="position:absolute;left:${block.left}px;top:${block.top}px;width:${block.width}px;height:${block.height}px;"
                     onmouseover="document.getElementById('tooltip_${block.id}').style.display='block'"
                     onmouseout="document.getElementById('tooltip_${block.id}').style.display='none'">
                    <div class="block-text">
                            ${block.text}
                    </div>
                </div>

                <%--//TODO: do tooltips the bootstrap way!--%>
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


<div id="sortedTextOutput">
    <div class="droppable"></div>

    <c:forEach items="${documentBean.pages}" var="page">
        <c:forEach items="${page.blocks}" var="block">
            <c:if test="${block.selectedBlock}">
                <div class="textwrap">
                    <div id="t_${block.id}" data-id="${block.id}" class="text">

                        <div class="options ui-state-default ui-corner-all">
                            <div class="option ui-state-default ui-corner-all">
                                <span class="handle ui-icon ui-icon-arrowthick-2-n-s"></span>
                            </div>
                            <div class="option ui-state-default ui-corner-all">
                                <span class="delete ui-icon ui-icon-trash"></span>
                            </div>
                        </div>

                        <span>${block.text}</span>
                    </div>

                    <div class="droppable"></div>
                </div>
            </c:if>
        </c:forEach>
    </c:forEach>
</div>


<script type="text/javascript">
    $(document).ready(function() {
        //collectText();

        // highlight corresponding texts
        // TODO add autoscroll to the highlightes text?
        $(".page div.block").hover(function() {
            $(this).addClass('highlightCorresponding');
            $('#t_' + $(this).data('id')).addClass('highlightCorresponding');
        }, function() {
            $(this).removeClass('highlightCorresponding');
            $('#t_' + $(this).data('id')).removeClass('highlightCorresponding');
        });
        $("#sortedTextOutput div.text").hover(function() {
            $(this).addClass('highlightCorresponding');
            $('#' + $(this).data('id')).addClass('highlightCorresponding');
        }, function() {
            $(this).removeClass('highlightCorresponding');
            $('#' + $(this).data('id')).removeClass('highlightCorresponding');
        });

        // make the right list sortable
        $("#sortedTextOutput").sortable({
            items: "> div.textwrap",
            placeholder: "ui-state-highlight",
            handle: ".handle",
            update: function(event, ui) {
                //alert("fertig")
            }
        });
        $("#sortedTextOutput").disableSelection();

        // make the pdf text divs draggable
        $(".page div.block.draggable").draggable({
            revert: "invalid",
            containment: "document",
            helper: "clone",
            cursor: "move"
        });
        $(".block-text").disableSelection();

        // enable the dropzones in the right list
        //TODO: droppable je seite ineiner schleife.. über accept eine eigene css klasse
        $("#sortedTextOutput div.droppable").droppable({
            accept: ".page > div.block",

            activeClass: "droppableActive",
            hoverClass: "droppableHover",
            drop: function(event, ui) {
                //deleteImage(ui.draggable);
            }
        });
    });
</script>

<jsp:include page="footer.jsp"/>