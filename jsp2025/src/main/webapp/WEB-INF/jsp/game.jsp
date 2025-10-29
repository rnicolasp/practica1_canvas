<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>

<body>
<h1> GAME!!!! </h1>

<c:if test="${not gameover}">

<p> ${tries} intents restants.</p>

<form method="post" action="/game">
    Endivina:
    <br>
    <input type="number" name="numero">

    <br>
    <input type="submit">
        <br>
</form>
</c:if>

<c:if test="${not empty msg}">
    <p> ${msg} </p>
</c:if>
</body>

</html>