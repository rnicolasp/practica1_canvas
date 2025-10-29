<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<head>
    <style>
        .menor {
        background-color = yellow;
        }
    </style>
</head>
<body>

<h1> meow meow meoew chinese cat </h1>

<table border="1">
    <tr>
         <th>N.</th>
    </tr>
    <c:forEach var="i" items="${primers}">
        <tr>
        <c:choose>
         <c:when test="${i < 50}">
            <td class="menor">
        </c:when>
        <c:otherwise>
            <td>
        </c:otherwise>
        </c:choose>
        ${i}
        </td>
        </tr>
    </c:forEach>

</table>

</body>
</html>