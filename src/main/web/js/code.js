
var showReadingOrder = true;

function toggleReadingOrder() {
  showReadingOrder = !showReadingOrder;
  svgElements = document.getElementsByClassName("svg");
  for (var i=0; i<svgElements.length; i++) {
    var element = svgElements[i];
    if (showReadingOrder) {
      element.style.display = 'block';
    } else {
      element.style.display = 'none';
    }
  }
}

