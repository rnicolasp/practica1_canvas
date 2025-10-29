<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>

<body>
<h1> Login </h1>


<form method="post" action="/login">
    Username:
    <br>
    <input type="text" name="user">
    <br>
    <input type="password" name="password">
    <br>
    <input type="submit">
    <br>
</form>
<p> <c:if test="${not empty message}">
        ${message}
    </c:if>
</p>
</body>

</html>