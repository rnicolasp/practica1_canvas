<%@ page isELIgnored="false" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Iniciar Sesión</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
    <div class="page-wrapper">
        <div class="card">
            <h2 class="center">Iniciar Sesión</h2>
            <form method="post" action="${pageContext.request.contextPath}/login" class="form-column">
                <label>Usuario:
                    <input type="text" name="user">
                </label>

                <label>Contraseña:
                    <input type="password" name="password">
                </label>

                <div class="actions center">
                    <button type="submit" value="Login">Iniciar Sesión</button>
                </div>
            </form>

            <a href="${pageContext.request.contextPath}/register"><button>Registrarse</button></a>

            <p class="center small-muted">
                <c:if test="${not empty message}">${message}</c:if>
            </p>
        </div>
    </div>
</body>
</html>