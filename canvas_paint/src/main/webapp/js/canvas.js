document.addEventListener('DOMContentLoaded', function() {
  const canvas = document.getElementById('paintCanvas');
  if (!canvas) {
    console.error('No se encontró el elemento canvas');
    return;
  }
  
  const ctx = canvas.getContext('2d');
  let drawing = false;
  let currentFree = null;
  let currentShape = null;
  const initialWidth = canvas.width;
  const initialHeight = canvas.height;

  // Referencias a elementos DOM
  const modeSelect = document.getElementById('mode');
  const shapeType = document.getElementById('shapeType');
  const colorInput = document.getElementById('color');
  const sizeInput = document.getElementById('size');
  const inputW = document.getElementById('inputWidth');
  const inputH = document.getElementById('inputHeight');
  const settingsToggle = document.getElementById('settingsToggle');
  const sizePanel = document.getElementById('sizePanel');
  
  // Configuración inicial
  ctx.fillStyle = '#fff';
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  ctx.strokeStyle = colorInput.value;
  ctx.lineWidth = parseInt(sizeInput.value, 10) || 2;

  function pos(e) {
    const r = canvas.getBoundingClientRect();
    return { x: e.clientX - r.left, y: e.clientY - r.top };
  }

  function drawShape(x, y, size, type) {
    ctx.beginPath();
    switch(type) {
      case 'circle':
        ctx.arc(x, y, size, 0, Math.PI * 2);
        break;
      case 'square':
        ctx.fillRect(x - size, y - size, size * 2, size * 2);
        break;
      case 'triangle':
        ctx.moveTo(x, y - size);
        ctx.lineTo(x - size, y + size);
        ctx.lineTo(x + size, y + size);
        ctx.closePath();
        break;
    }
    ctx.fill();
  }

  // Eventos del mouse
  canvas.addEventListener('mousedown', e => {
    drawing = true;
    const p = pos(e);
    ctx.strokeStyle = colorInput.value;
    ctx.fillStyle = colorInput.value;
    ctx.lineWidth = parseInt(sizeInput.value, 10) || 2;
    
    if (modeSelect.value === 'freehand') {
      ctx.beginPath();
      ctx.moveTo(p.x, p.y);
    } else {
      currentShape = {
        x: p.x,
        y: p.y,
        size: parseInt(sizeInput.value, 10) || 20
      };
    }
  });

  canvas.addEventListener('mousemove', e => {
    if (!drawing) return;
    const p = pos(e);
    
    if (modeSelect.value === 'freehand') {
      ctx.lineTo(p.x, p.y);
      ctx.stroke();
    }
  });

  window.addEventListener('mouseup', () => {
    if (drawing && currentShape && modeSelect.value === 'shape') {
      drawShape(currentShape.x, currentShape.y, currentShape.size, shapeType.value);
      currentShape = null;
    }
    drawing = false;
  });

  // Eventos táctiles
  canvas.addEventListener('touchstart', e => {
    e.preventDefault();
    drawing = true;
    const t = e.touches[0];
    const p = pos(t);
    ctx.beginPath();
    ctx.moveTo(p.x, p.y);
  });

  canvas.addEventListener('touchmove', e => {
    e.preventDefault();
    if (!drawing) return;
    const t = e.touches[0];
    const p = pos(t);
    ctx.lineTo(p.x, p.y);
    ctx.stroke();
  });

  window.addEventListener('touchend', () => {
    drawing = false;
  });

  // Control de tamaño del canvas
  function clamp(v) {
    return Math.max(100, Math.min(2000, Math.floor(v) || 0));
  }

  document.getElementById('applySize')?.addEventListener('click', () => {
    const w = clamp(inputW.value);
    const h = clamp(inputH.value);
    const tmp = document.createElement('canvas');
    tmp.width = w;
    tmp.height = h;
    const tctx = tmp.getContext('2d');
    tctx.fillStyle = '#fff';
    tctx.fillRect(0, 0, w, h);
    tctx.drawImage(canvas, 0, 0, w, h);
    canvas.width = w;
    canvas.height = h;
    ctx.drawImage(tmp, 0, 0);
  });

  document.getElementById('resetSize')?.addEventListener('click', () => {
    canvas.width = initialWidth;
    canvas.height = initialHeight;
    ctx.fillStyle = '#fff';
    ctx.fillRect(0, 0, canvas.width, canvas.height);
    inputW.value = initialWidth;
    inputH.value = initialHeight;
  });

  // Eventos de cambio de herramientas
  colorInput?.addEventListener('change', () => {
    ctx.strokeStyle = colorInput.value;
    ctx.fillStyle = colorInput.value;
  });

  sizeInput?.addEventListener('change', () => {
    ctx.lineWidth = parseInt(sizeInput.value, 10) || 2;
  });

  // Panel de ajustes
  if (settingsToggle && sizePanel) {
    settingsToggle.addEventListener('click', () => {
      sizePanel.style.display = sizePanel.style.display === 'none' ? 'block' : 'none';
    });
  }

  // Función para guardar el canvas
  window.saveCanvas = async function(name) {
    const canvasData = {
      width: canvas.width,
      height: canvas.height,
      content: canvas.toDataURL()
    };

    try {
      const response = await fetch('/saveCanvas' + (name ? `?name=${name}` : ''), {
        method: 'POST',
        body: JSON.stringify(canvasData)
      });
      
      if (response.ok) {
        alert('Canvas guardado correctamente');
        window.location.href = '/profile';
      }
    } catch (error) {
      console.error('Error al guardar:', error);
    }
  };

  // Botón de guardado
  document.getElementById('saveButton')?.addEventListener('click', () => {
    const name = prompt('Nombre para el canvas:', 'Mi Canvas');
    if (name) {
      saveCanvas(name);
    }
  });
});