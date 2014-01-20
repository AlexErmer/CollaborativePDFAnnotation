<jsp:include page="common/header.jsp"/>
<jsp:useBean id="loginBean" scope="request" class="de.uni.passau.fim.mics.ermera.model.LoginBean"/>


<h1>Login mit Mendeley</h1>

<p>Bitte klicken Sie auf nachfolgenden Link um sich mit Ihrem mit Mendeley Account einzuloggen</p>
<a href="${loginBean.mendeleyLink}" class="btn btn-default btn-primary">Login mit Mendeley</a></p>
<p>Sie werden automatisch wieder zu dieser Anwendung zurückgeleitet.</p>

<jsp:include page="common/footer.jsp"/>