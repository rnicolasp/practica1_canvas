<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Ver Canvas</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
</head>
<body>
  <div class="page-wrapper">
    <div class="card">
      <div style="display:flex; justify-content:flex-end; gap:8px; align-items:center;">
        <a href="${pageContext.request.contextPath}/home"><button>Home</button></a>
        <a href="${pageContext.request.contextPath}/profile"><button>Mi Perfil</button></a>
      </div>
      <hr>
      <h2 class="center">Visualizador de Canvas</h2>

      <c:choose>
        <%-- Comprobar si el servlet encontró los datos del canvas --%>
        <c:when test="${not empty canvasData}">
          <div style="display:flex; justify-content:center; padding:10px; background:#f4f4f4; border:1px solid #ccc; border-radius: 6px; overflow: auto;">
            <canvas id="viewOnlyCanvas" style="background:#fff; display:block;"></canvas>
          </div>

          <script>
            // Incrustamos los datos JSON pasados desde el servlet
            const canvasData = ${canvasData};

            (function(){
              'use strict';
              const canvas = document.getElementById('viewOnlyCanvas');
              if (!canvas) return;
              const ctx = canvas.getContext('2d');

              // --- Funciones de dibujo (adaptadas de canvas-app.js) ---

              function drawStar(ctx,x,y,r,points){
                const outer=r, inner=r*0.45, step=Math.PI/points;
                ctx.beginPath();
                for(let i=0;i<2*points;i++){
                  const radius=(i%2===0)?outer:inner;
                  const a=i*step-Math.PI/2;
                  const px=x+Math.cos(a)*radius;
                  const py=y+Math.sin(a)*radius;
                  if(i===0) ctx.moveTo(px,py); else ctx.lineTo(px,py);
                }
                ctx.closePath();
              }

              function drawShape(o){
                ctx.save();
                ctx.fillStyle=o.color;
                ctx.strokeStyle=o.color;
                ctx.lineWidth=2;
                const x=o.x,y=o.y,s=o.size;
                if(o.type==='circle'){
                  ctx.beginPath(); ctx.arc(x,y,s,0,Math.PI*2); ctx.fill(); ctx.closePath();
                } else if(o.type==='square'){
                  ctx.fillRect(x-s,y-s,s*2,s*2);
                } else if(o.type==='triangle'){
                  ctx.beginPath(); ctx.moveTo(x,y-s); ctx.lineTo(x-s,y+s); ctx.lineTo(x+s,y+s); ctx.closePath(); ctx.fill();
                } else if(o.type==='star7'){
                  drawStar(ctx,x,y,s,7); ctx.fill();
                }
                ctx.restore();
              }

              function drawFreehand(o){
                const pts = o.points;
                if(!pts||pts.length===0) return;
                ctx.save();
                ctx.strokeStyle=o.color;
                ctx.lineWidth = (o.size || 2);
                ctx.lineJoin='round';
                ctx.lineCap='round';
                ctx.beginPath();
                ctx.moveTo(pts[0].x,pts[0].y);
                for(let i=1;i<pts.length;i++) ctx.lineTo(pts[i].x,pts[i].y);
                ctx.stroke();
                ctx.closePath();
                ctx.restore();
              }
              // --- Fin funciones de dibujo ---

              function redraw(data){
                // Ajustar el tamaño del canvas según los datos guardados
                canvas.width = data.width || 800;
                canvas.height = data.height || 600;

                // Fondo blanco
                ctx.fillStyle = '#fff';
                ctx.fillRect(0,0,canvas.width, canvas.height);

                // Dibujar cada objeto
                if (data.objects && Array.isArray(data.objects)) {
                  data.objects.forEach(o => {
                    if (o.type === 'freehand') drawFreehand(o);
                    else drawShape(o);
                  });
                }
              }

              // Dibujo inicial al cargar la página
              if (canvasData) {
                redraw(canvasData);
              }
            })();
          </script>
        </c:when>
        <c:otherwise>
          <%-- Mensaje si el canvas no se encontró o no pertenece al usuario --%>
          <p class="center small-muted">Canvas no encontrado o no tienes permiso para verlo.</p>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>