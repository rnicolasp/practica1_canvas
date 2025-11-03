<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Profile</title>
  <link rel="stylesheet" href="/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card">
      <h2 class="center">Profile</h2>
      <p><strong>User:</strong> ${name} (${user})</p>

      <div class="actions center">
        <a href="/canvas"><button>Create new canvas</button></a>
        <a href="/home"><button>Home</button></a>
      </div>
    </div>
  </div>
</body>
</html>
