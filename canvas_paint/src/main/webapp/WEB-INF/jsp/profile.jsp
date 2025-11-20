<%@ page isELIgnored="false" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Perfil</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card">
      <h2 class="center">Perfil</h2>
      <p><strong>Usuario:</strong> ${name} (${user})</p>

      <div class="actions center">
        <a href="${pageContext.request.contextPath}/canvas"><button>Crear nuevo lienzo</button></a>
        <a href="${pageContext.request.contextPath}/home"><button>Inicio</button></a>
      </div>
      <hr>
      <h3 class="center">Lienzos guardados</h3>
      <c:choose>
        <c:when test="${not empty saves}">
          <div class="canvas-grid">
            <c:forEach var="save" items="${saves}">
              <div class="canvas-card">
                <h4>${save.name}</h4>
                <div class="canvas-info">
                  <p><strong>ID:</strong> ${save.id}</p>
                  <p><strong>Propietario:</strong> ${save.owner}</p>
                  <p><small><strong>Creado:</strong> <fmt:formatDate value="${save.dateCreated}" pattern="dd/MM/yyyy HH:mm" /></small></p>
                  <p><small><strong>Modificado:</strong> <fmt:formatDate value="${save.dateModified}" pattern="dd/MM/yyyy HH:mm" /></small></p>
                </div>
                <div class="canvas-actions">
                  <a href="${pageContext.request.contextPath}/canvas?loadId=${save.id}"><button>Cargar</button></a>
                  <form method="post" action="${pageContext.request.contextPath}/deleteCanvas" style="display:inline" onsubmit="return confirm('¿Eliminar este lienzo guardado?');">
                    <input type="hidden" name="id" value="${save.id}" />
                    <button type="submit">Eliminar</button>
                  </form>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:when>
        <c:otherwise>
          <p class="small-muted center">No hay lienzos guardados.</p>
        </c:otherwise>
      </c:choose>
