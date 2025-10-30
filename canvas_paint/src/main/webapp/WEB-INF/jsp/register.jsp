<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register</title>
        <link rel="stylesheet" href="/css/home.css">
</head>
<body>
    <div class="page-wrapper">
        <div class="card">
            <h2 class="center">Register</h2>
            <form method="post" action="/register">
                <label>Nickname:
                    <input type="text" name="name">
                </label>

                <label>Username:
                    <input type="text" name="user">
                </label>

                <label>Password:
                    <input type="password" name="password">
                </label>

                <div class="actions center">
                    <input type="submit" value="Register">
                </div>
            </form>

            <p class="center small-muted">
                <c:if test="${not empty message}">${message}</c:if>
            </p>
        </div>
    </div>
</body>
</html>