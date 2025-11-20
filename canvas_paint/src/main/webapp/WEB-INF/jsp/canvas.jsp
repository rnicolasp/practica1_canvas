<%@ page isELIgnored="false" %>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="contextPath" content="${pageContext.request.contextPath}">
  <title>ePaint - Canvas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/canvas.css">
</head>
<body class="canvas-editor-body">
  <div class="card">

      <div class="canvas-top-bar">
        <div class="nav-group">
          <a href="${pageContext.request.contextPath}/home"><button>Inicio</button></a>
          <a href="${pageContext.request.contextPath}/profile"><button>Mi Perfil</button></a>
        </div>

        <div class="tool-group">
          <span id="autosaveStatus" class="small-muted" style="margin: 0 8px; transition: opacity 0.5s;"></span>

          <button id="undoBtn" disabled title="Deshacer" style="padding: 6px 8px;">
              <img src="${pageContext.request.contextPath}/icons/undo.svg" alt="Deshacer" style="width:16px; height:16px; vertical-align: middle;">
          </button>
          <button id="redoBtn" disabled title="Rehacer" style="padding: 6px 8px;">
              <img src="${pageContext.request.contextPath}/icons/redo.svg" alt="Rehacer" style="width:16px; height:16px; vertical-align: middle;">
          </button>     

          <c:if test="${isOwner}">
            <button id="saveBtn">Guardar</button>
          </c:if>
          <button id="settingsToggle">Ajustes</button>
        </div>
      </div>

      <div id="sizePanel" style="display:none; margin:0; padding: 12px 20px; background: #f9f9f9; border-bottom: 1px solid #eee;">
        <label style="margin-right:8px">Ancho: <input id="inputWidth" type="number" min="100" max="2000" value="${width}" style="width:80px"></label>
        <label style="margin-right:8px">Alto: <input id="inputHeight" type="number" min="100" max="2000" value="${height}" style="width:80px"></label>
        <button id="applySize">Aplicar</button>
        <button id="resetSize" type="button">Restablecer</button>
      </div>

      <div class="canvas-area">
        <div style="flex:1">
          <div style="display:flex; flex-direction:column; align-items:center; gap:8px;">
            <div style="display:flex; align-items:center; gap:8px;">
              <h3 class="canvas-title" style="margin:0; cursor:pointer;" title="Doble click para editar" ondblclick="this.style.display='none';document.getElementById('canvasNameInput').style.display='block';document.getElementById('canvasNameInput').focus();" id="canvasNameDisplay">${not empty canvasName ? canvasName : 'Lienzo sin título'}</h3>
              <input type="text" id="canvasNameInput" value="${not empty canvasName ? canvasName : 'Lienzo sin título'}" style="display:none; font-size:1.17em; margin:0; padding:4px;" onblur="this.style.display='none';document.getElementById('canvasNameDisplay').style.display='block';document.getElementById('canvasNameDisplay').textContent=this.value;" onkeypress="if(event.keyCode===13)this.blur();">
            </div>
            <div class="canvas-wrap">
              <canvas id="paintCanvas" width="${width}" height="${height}" data-initial-width="${width}" data-initial-height="${height}" data-load-id="${not empty loadId ? loadId : ''}" style="background:#fff;display:block;"></canvas>
            </div>
          </div>
        </div>

      </div>
    </div>

    <div id="floating-toolbox" class="sidebar">
      <div class="toolbox-header">Herramientas</div>
      <div class="card">
        <div class="form-column">
          <label>Modo:
            <select id="mode">
              <option value="freehand">Dibujo a mano alzada</option>
              <option value="shape">Añadir forma (arrastrar)</option>
              <option value="select">Seleccionar / Mover</option>
            </select>
          </label>

          <label>Forma:
            <select id="shapeType">
              <option value="circle">Círculo</option>
              <option value="square">Cuadrado</option>
              <option value="triangle">Triángulo</option>
              <option value="star7">Estrella 7 puntas</option>
            </select>
          </label>

          <label>Color: <input id="color" type="color" value="#ff0000"></label>
          <label>Tamaño (px):
            <input id="size" type="range" min="1" max="100" value="8">
            <span id="sizeDisplay" class="small-muted">8</span>
          </label>

          <div class="controls-row">
            <button id="addCenter">Añadir en centro</button>
            <button id="clearAll">Limpiar todo</button>
          </div>
        </div>
        <hr>
        <h3 class="center">Objetos</h3>
        <ul id="objects" class="object-list"></ul>
      </div>
    </div>
    <c:if test="${not empty canvasData}">
      <script>
        window.INITIAL_CANVAS_DATA = ${canvasData};
      </script>
    </c:if>
    <script src="${pageContext.request.contextPath}/js/canvas.js"></script>
</body>
</html>