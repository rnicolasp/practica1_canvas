<%@ page isELIgnored="false" %>
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
    <div class="card">
      <h2 class="center">ePaint</h2>
      <div>
        <p class="small-muted">Welcome, <strong>${user}</strong></p>
        <div class="actions center">
          <a href="${pageContext.request.contextPath}/profile"><button>My profile</button></a>
          <a href="${pageContext.request.contextPath}/canvas"><button>Create new canvas</button></a>
          <a href="${pageContext.request.contextPath}/logout"><button>Logout</button></a>
        </div>
      </div>

      <hr>
      <h3 class="center">All Canvases</h3>
      <div class="canvas-grid">
        <c:forEach var="canvas" items="${allCanvas}">
          <div class="canvas-card">
            <h4>${canvas.name}</h4>
            <div class="canvas-info">
              <p><strong>Created by:</strong> ${canvas.owner}</p>
              <p><strong>ID:</strong> ${canvas.id}</p>
              <p><strong>Filename:</strong> ${canvas.filename}</p>
            </div>
            <div class="canvas-actions">
              <a href="${pageContext.request.contextPath}/canvas?loadFile=${canvas.filename}"><button>Load</button></a>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</body>
</html>