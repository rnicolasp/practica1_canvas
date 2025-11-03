<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Canvas</title>
  <link rel="stylesheet" href="/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card">
      <div style="display:flex; justify-content:flex-end; gap:8px; align-items:center;">
        <a href="/home"><button id="homeBtn">Home</button></a>
        <a href="/profile"><button id="profileBtn">Mi Perfil</button></a>
        <button id="saveBtn">Guardar</button>
        <button id="settingsToggle">Ajustes</button>
      </div>

      <div id="sizePanel" style="display:none; margin:8px 0;">
        <label style="margin-right:8px">W: <input id="inputWidth" type="number" min="100" max="2000" value="${width}" style="width:80px"></label>
        <label style="margin-right:8px">H: <input id="inputHeight" type="number" min="100" max="2000" value="${height}" style="width:80px"></label>
        <button id="applySize">Apply</button>
        <button id="resetSize" type="button">Reset</button>
      </div>

      <div class="canvas-area">
        <script src="/js/canvas-app.js"></script>

        <div style="flex:1">
          <div style="display:flex; justify-content:center">
            <div class="canvas-wrap">
              <canvas id="paintCanvas" width="${width}" height="${height}" data-initial-width="${width}" data-initial-height="${height}" style="background:#fff;display:block;"></canvas>
            </div>
          </div>

        </div>

        <div class="sidebar">
          <div class="card">
            <h3 class="center">Tools</h3>
            <div class="form-column">
              <label>Mode:
                <select id="mode">
                  <option value="shape">Add shape (click)</option>
                  <option value="freehand">Freehand draw</option>
                </select>
              </label>

              <label>Shape:
                <select id="shapeType">
                  <option value="circle">Circle</option>
                  <option value="square">Square</option>
                  <option value="triangle">Triangle</option>
                  <option value="star7">7-point star</option>
                </select>
              </label>

              <label>Color: <input id="color" type="color" value="#ff0000"></label>
              <label>Size (px):
                <input id="size" type="range" min="1" max="100" value="8">
                <span id="sizeDisplay" class="small-muted">8</span>
              </label>

              <div class="controls-row">
                <button id="addCenter">Add at center</button>
                <button id="clearAll">Clear all</button>
              </div>
            </div>
            <hr>
            <h3 class="center">Objects</h3>
            <ul id="objects" class="object-list"></ul>
          </div>
        </div>
      </div>
    </div>
</body>
</html>
