<%@ page isELIgnored="false" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>ePaint</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card center">
      <h2 class="center">ePaint</h2>
      <div class="actions">
        <a href="${pageContext.request.contextPath}/login"><button>Iniciar Sesión</button></a>
        <a href="${pageContext.request.contextPath}/register"><button>Registrarse</button></a>
      </div>
    </div>
  </div>
</body>
</html>