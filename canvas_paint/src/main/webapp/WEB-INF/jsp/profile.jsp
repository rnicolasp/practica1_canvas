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
      <hr>
      <h3 class="center">Saved canvases</h3>
      <c:choose>
        <c:when test="${not empty saves}">
          <ul class="object-list">
            <c:forEach var="save" items="${saves}">
              <li class="object-item">
                <div class="object-left">
                  <div class="object-name"><strong>${save}</strong></div>
                </div>
                <div class="object-right">
                  <a href="/saveCanvas?file=${save}"><button>Download JSON</button></a>
                </div>
              </li>
            </c:forEach>
          </ul>
        </c:when>
        <c:otherwise>
          <p class="small-muted center">No saved canvases yet.</p>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>
