<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Web paint</title>
  <link rel="stylesheet" href="/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card center">
      <h2>Web paint</h2>
      <div>
        <c:choose>
          <c:when test="${not empty sessionScope.user}">
            <p class="small-muted">Welcome, <strong>${sessionScope.name}</strong></p>
            <div class="actions center">
              <a href="/profile"><button>My profile</button></a>
              <a href="/canvas"><button>Create new canvas</button></a>
              <a href="/logout"><button>Logout</button></a>
            </div>
          </c:when>
          <c:otherwise>
            <div class="actions center">
              <a href="/login"><button>Login</button></a>
              <a href="/register"><button>Register</button></a>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</body>
</html>