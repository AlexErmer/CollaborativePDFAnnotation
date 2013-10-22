<jsp:include page="header.jsp"/>

<jsp:useBean id="documentBean" scope="request" class="de.uni.passau.fim.mics.ermera.beans.DocumentBean"/>

<c:choose>
    <c:when test="${param.pageNumber == null}">
        <c:set var="pageNumber" value="1"/>
    </c:when>
    <c:when test="${param.pageNumber < 0}">
        <c:set var="pageNumer" value="0"/>
    </c:when>
    <c:when test="${param.pageNumber > fn:length(documentBean.pages)}">
        <c:set var="pageNumber" value="${fn:length(documentBean.pages)}"/>
    </c:when>
    <c:otherwise>
        <c:set var="pageNumber" value="${param.pageNumber}"/>
    </c:otherwise>
</c:choose>
<jsp:include page="paginator.jsp">
    <jsp:param name="pageNumber" value="${pageNumber}"/>
    <jsp:param name="pages" value="${fn:length(documentBean.pages)}"/>
    <jsp:param name="link" value="extract?id=${param.id}&pageNumber="/>
</jsp:include>

<c:set var="page" value="${documentBean.pages[pageNumber-1]}"/>

<div id="PDFoutput">
    <div class="buttonLine">
        <button type="button" class="btn btn-xs" onclick="$('.svg').toggle()">toggle Lines</button>
    </div>

    <svg xmlns="http://www.w3.org/2000/svg" version="1.1" style="width:0; height:0">
        <defs>
            <marker id="arrowMarker" viewBox="0 0 10 10" refX="1" refY="5"
                    markerUnits="strokeWidth" orient="auto" markerWidth="8" markerHeight="6">
                <polyline points="0,0 10,5 0,10 1,5" fill="black"></polyline>
            </marker>
        </defs>
    </svg>

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
                 class="${block.cssClass} ${block.selectedBlock?'selectedAnnotation':'draggable'} text"
                 title="label"
                 style="position:absolute;left:${block.left}px;top:${block.top}px;width:${block.width}px;height:${block.height}px;"
                 onmouseover="document.getElementById('tooltip_${block.id}').style.display='block'"
                 onmouseout="document.getElementById('tooltip_${block.id}').style.display='none'">

                <c:if test="${block.selectedBlock}">
                    <div class="options ui-widget-header ui-corner-all">
                        <div class="option">
                            <button class="btn btn-sm btn-danger" title="L&ouml;schen"
                                    onclick="removeItem($('#t_${block.id}'))">
                                <span class="delete ui-icon ui-icon-trash"></span>
                            </button>
                        </div>
                    </div>
                </c:if>
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
</div>


<div id="sortedTextOutputWrapper">
    <div class="buttonLine">
        <button type="button" class="btn btn-xs" onclick="saveForBrat()">save for Brat</button>
        <a class="btn btn-xs" href="/brat/index.xhtml#/${documentBean.id}">weiter zu brat</a>
    </div>

    <div id="sortedTextOutput">

        <c:forEach items="${page.blocks}" var="block">
            <c:if test="${block.selectedBlock}">
                <div id="t_${block.id}" data-id="${block.id}" class="text ${block.headline?' headline':''} ${block.newParagraph?' newParagraph':''}">
                    <div class="options ui-widget-header ui-corner-all">
                        <div class="option">
                            <button class="btn btn-sm btn-info" title="ist &Uuml;berschrift?"
                                    onclick="toggleHeadline($('#t_${block.id}'))">
                                <span class="ui-icon ui-icon-star"></span>
                            </button>
                        </div>
                        <div class="option">
                            <button class="btn btn-sm btn-info" title="leitet neuen Paragraphen ein?"
                                    onclick="toggleNewParagraph($('#t_${block.id}'))">
                                <span class="ui-icon ui-icon-arrowreturnthick-1-e"></span>
                            </button>
                        </div>
                        <div class="option">
                            <button class="btn btn-sm btn-warning handle" title="Verschieben" onclick="return false;">
                                <span class="ui-icon ui-icon-arrowthick-2-n-s"></span>
                            </button>
                        </div>
                        <div class="option">
                            <button class="btn btn-sm btn-danger" title="L&ouml;schen"
                                    onclick="removeItem($('#t_${block.id}'))">
                                <span class="delete ui-icon ui-icon-trash"></span>
                            </button>
                        </div>
                    </div>

                    <span class="newParagraphIcon ui-icon ui-icon-arrowreturnthick-1-e"></span>
                    <span>${block.text}</span>
                </div>
            </c:if>
        </c:forEach>
    </div>
</div>


<script type="text/javascript">
    $(document).ready(function() {
        // highlight corresponding text
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
            items: "> div.text",
            placeholder: "ui-state-highlight",
            handle: ".handle",
            cancel: "", // needed to user <button> as handle!
            //revert: "invalid",
            receive: function(event, ui) {
                $.ajax({
                    url: '', // blank to submit to same page!
                    async: false,
                    cache: false,
                    data: {action: 'sort', items: $(this).sortable("toArray", {attribute: "data-id"})}
                });
                location.reload(); //TODO: do real ajax without reloading!
            },
            update: function(event, ui) {
                $.ajax({
                    url: '', // blank to submit to same page!
                    async: false,
                    cache: false,
                    data: {action: 'sort', items: $(this).sortable("toArray", {attribute: "data-id"})}
                });
                location.reload(); //TODO: do real ajax without reloading!
            }
        });
        $("#sortedTextOutput").disableSelection();

        // make the pdf text divs draggable
        $(".page div.block.draggable").draggable({
            connectToSortable: "#sortedTextOutput",
            //revert: "valid",
            containment: "document",
            helper: "clone",
            cursor: "move"
        });
        $(".block-text").disableSelection();
    });

    function removeItem(item) {
        $.ajax({
            url: '', // blank to submit to same page!
            async: false,
            cache: false,
            data: {action: 'unselect', item: item.data('id')}
        });
        location.reload(); //TODO: do real ajax without reloading!
    }

    function saveForBrat() {
        $.ajax({
            url: '', // blank to submit to same page!
            async: false,
            cache: false,
            data: {action: 'saveForBrat'}
        });
    }
    function toggleHeadline(item) {
        $('#t_' + item.data('id')).toggleClass('headline');
        $.ajax({
            url: '', // blank to submit to same page!
            async: false,
            cache: false,
            data: {action: 'toggleHeadline', item: item.data('id')}
        });
    }
    function toggleNewParagraph(item) {
        $('#t_' + item.data('id')).toggleClass('newParagraph');
        $.ajax({
            url: '', // blank to submit to same page!
            async: false,
            cache: false,
            data: {action: 'toggleNewParagraph', item: item.data('id')}
        });
    }
</script>

<jsp:include page="footer.jsp"/>