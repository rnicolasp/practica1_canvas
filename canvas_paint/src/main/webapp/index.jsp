<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Web Paint</title>
  <link rel="stylesheet" href="/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card center">
      <h2 class="center">Web Paint</h2>
      <div class="actions">
        <a href="/login"><button>Login</button></a>
        <a href="/register"><button>Register</button></a>
        <form method="post" action="/logout" style="display:inline;margin:0">
          <button type="submit">Clear session</button>
        </form>
      </div>
    </div>
  </div>
</body>
</html>