<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title></title>
</head>
<body>
blabla... super test
${output}

${bean.blocks}
${bean.getBlocks()}

<c:forEach var="i" begin="1" end="10" step="1">
  <c:out value="${i}" />
  <br />
</c:forEach>

</body>
</html>
