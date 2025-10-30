<%@ page isELIgnored="false" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Canvas</title>
  <link rel="stylesheet" href="/css/home.css">
  <script src="../../js/canvas.js"></script>
  <style>
    .canvas-wrap { border: 1px solid #ccc; display:inline-block; }
  </style>
</head>
<body>
  <div class="page-wrapper">
    <div class="card">
      <h2 class="center">Canvas</h2>
      <p class="center">Size: <strong id="sizeLabel">${width} x ${height}</strong></p>

      <div class="center" style="margin-bottom:12px">
        <div style="display:inline-block; background: rgba(0,0,0,0.12); padding:8px; border-radius:6px; border:1px solid rgba(0,255,213,0.15)">
          <label style="margin-right:8px">W: <input id="inputWidth" type="number" min="100" max="2000" value="${width}" style="width:80px"></label>
          <label style="margin-right:8px">H: <input id="inputHeight" type="number" min="100" max="2000" value="${height}" style="width:80px"></label>
          <button id="applySize">Apply</button>
          <button id="resetSize" type="button">Reset</button>
        </div>
      </div>

      <div class="center">
        <div class="canvas-wrap">
          <canvas id="paintCanvas" width="${width}" height="${height}" style="background:#fff;display:block;"></canvas>
        </div>
      </div>

      <div class="actions center">
        <a href="/"><button>Home</button></a>
      </div>
    </div>
  </div>
</body>
</html>
