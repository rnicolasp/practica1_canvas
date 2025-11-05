<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Profile</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card">
      <h2 class="center">Profile</h2>
      <p><strong>User:</strong> ${name} (${user})</p>

      <div class="actions center">
        <a href="${pageContext.request.contextPath}/canvas"><button>Create new canvas</button></a>
        <a href="${pageContext.request.contextPath}/home"><button>Home</button></a>
      </div>
      <hr>
      <h3 class="center">Saved canvases</h3>
      <c:choose>
        <c:when test="${not empty saves}">
          <div class="canvas-grid">
            <c:forEach var="save" items="${saves}">
              <div class="canvas-card">
                <h4>${save.name}</h4>
                <div class="canvas-info">
                  <p><strong>ID:</strong> ${save.id}</p>
                  <p><strong>Filename:</strong> ${save.filename}</p>
                  <p><strong>Owner:</strong> ${save.owner}</p>
                </div>
                <div class="canvas-actions">
                  <a href="${pageContext.request.contextPath}/saveCanvas?file=${save.filename}"><button>Download JSON</button></a>
                  <a href="${pageContext.request.contextPath}/canvas?loadFile=${save.filename}"><button>Load</button></a>
                  <form method="post" action="${pageContext.request.contextPath}/deleteCanvas" style="display:inline" onsubmit="return confirm('Delete this saved canvas?');">
                    <input type="hidden" name="file" value="${save.filename}" />
                    <button type="submit">Delete</button>
                  </form>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <p class="small-muted center">No saved canvases yet.</p>
        </c:otherwise>
      </c:choose>
