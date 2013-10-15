<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Code PDF Extraction Demo Webapp</title>
</head>
<body>

<h1>PDF Extraction Demo</h1>

<form action="/pages/extract" method="post" enctype="multipart/form-data">
    Choose a PDF to upload:
    <input name="pdfFile" type="file" accept="application/pdf;text/pdf"/><br/>
    <input type="submit"/>
</form>

<h3>Examples</h3>

<ul>
    <li><a href="/pages/extract?id=SuhueGw86hcErtX3V9CNiQ==">
        Nestin and CD133: valuable stem cell-specific markers for
        determining clinical outcome of glioma patients</a></li>
    <li><a href="/pages/extract?id=TOyYFtdAAvYk40F0V0pXhQ==">
        Broken Time Reversal of Light Interaction with Planar Chiral Nanostructures
    </a></li>
    <li><a href="/pages/extract?id=vmEM83TrSOEAaIn1lvsY_g==">
        Neurotic disorders and the receipt of psychiatric treatment
    </a></li>
    <li><a href="/pages/extract?id=FkstccVybQR5NYEV18t4SA==">
        Enhanced Hippocampal Long-Term Potentiation and Spatial
        Learning in Aged 11&beta;-Hydroxysteroid Dehydrogenase Type
        1 Knock-Out Mice
    </a></li>
    <li><a href="/pages/extract?id=pud$WCk6ELUhksQwpuhscw==">
        An evaluation of a psychosocial stress and coping model
        in the police work context
    </a></li>
    <li><a href="/pages/extract?id=eIWEZnyj8QnXz$g6oDmqvg==">
        Support vector machines tutorial by Marti A. Hearst
    </a></li>
</ul>

</body>
</html>