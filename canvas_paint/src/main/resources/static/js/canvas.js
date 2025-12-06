(function () {
  'use strict';
  function ready(fn) { if (document.readyState !== 'loading') fn(); else document.addEventListener('DOMContentLoaded', fn); }

  ready(function () {
    const canvas = document.getElementById('paintCanvas');
    if (!canvas) return;

    const metaContextPath = document.querySelector('meta[name="contextPath"]');
    const contextPath = metaContextPath ? metaContextPath.getAttribute('content') : '';
    let currentCanvasId = canvas.getAttribute('data-load-id') || null;

    const CanvasConf = window.CANVAS_CONFIG || { initialWidth: parseInt(canvas.getAttribute('data-initial-width'), 10) || canvas.width, initialHeight: parseInt(canvas.getAttribute('data-initial-height'), 10) || canvas.height };
    const canvasCont = canvas.getContext('2d');
    
    let drawing = false;
    let currentFree = null;
    let currentShape = null;
    let objects = [];
    let idCounter = 1;
    let currentWidth = canvas.width;
    let currentHeight = canvas.height;
    let history = [];
    let redoStack = [];
    let selectedObject = null;
    let isDragging = false;
    let dragOffsetX = 0;
    let dragOffsetY = 0;
    let autosaveTimer = null;
    const modeSelect = document.getElementById('mode');
    const shapeType = document.getElementById('shapeType');
    const colorInput = document.getElementById('color');
    const sizeInput = document.getElementById('size');
    const sizeDisplay = document.getElementById('sizeDisplay');
    const objectsList = document.getElementById('objects');
    const inputW = document.getElementById('inputWidth');
    const inputH = document.getElementById('inputHeight');
    const saveBtn = document.getElementById('saveBtn');
    const canvasNameDisplay = document.getElementById('canvasNameDisplay');
    const canvasNameInput = document.getElementById('canvasNameInput');
    const undoBtn = document.getElementById('undoBtn');
    const redoBtn = document.getElementById('redoBtn');
    const autosaveStatus = document.getElementById('autosaveStatus');
    const toolbox = document.getElementById('floating-toolbox');
    const toolboxHeader = document.querySelector('.toolbox-header');
    let isDraggingToolbox = false;
    let dragToolboxOffsetX = 0;
    let dragToolboxOffsetY = 0;

    function redraw() {
      canvasCont.fillStyle = '#fff';
      canvasCont.fillRect(0, 0, canvas.width, canvas.height);

      objects.forEach(i => {
        if (i !== selectedObject) {
          if (i.type === 'libre') drawFreehand(i); else drawShape(i);
        }
      });

      if (selectedObject) {
        if (selectedObject.type === 'libre') drawFreehand(selectedObject); else drawShape(selectedObject);
        drawSelectionHighlight(selectedObject);
      }
    }

    function drawSelectionHighlight(o) {
        canvasCont.save();
        canvasCont.strokeStyle = '#00f';
        canvasCont.lineWidth = 1;
        canvasCont.setLineDash([5, 3]);
        const bounds = getObjectBounds(o);
        canvasCont.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
        canvasCont.restore();
    }

    function getObjectBounds(objecto) {
        let limits = { x: objecto.x, y: objecto.y, width: objecto.size, height: objecto.size };
        if (objecto.type === 'circulo') {
            limits = { x: objecto.x - objecto.size, y: objecto.y - objecto.size, width: objecto.size * 2, height: objecto.size * 2 };
        } else if (objecto.type === 'cuadrado') {
            limits = { x: objecto.x - objecto.size, y: objecto.y - objecto.size, width: objecto.size * 2, height: objecto.size * 2 };
        } else if (objecto.type === 'triangulo') {
            limits = { x: objecto.x - objecto.size, y: objecto.y - objecto.size, width: objecto.size * 2, height: objecto.size * 2 };
        } else if (objecto.type === 'estrella') {
            limits = { x: objecto.x - objecto.size, y: objecto.y - objecto.size, width: objecto.size * 2, height: objecto.size * 2 };
        } else if (objecto.type === 'libre') {
            let minX = Infinity, minY = Infinity, maxX = -Infinity, maxY = -Infinity;
            objecto.points.forEach(p => {
                if (p.x < minX) minX = p.x;
                if (p.x > maxX) maxX = p.x;
                if (p.y < minY) minY = p.y;
                if (p.y > maxY) maxY = p.y;
            });
            const padding = (objecto.size || 2);
            limits = { x: minX - padding, y: minY - padding, width: (maxX - minX) + padding*2, height: (maxY - minY) + padding*2 };
        }
        return limits;
    }

    function isHit(x, y) {
        for (let i = objects.length - 1; i >= 0; i--) {
            const ob = objects[i];
            const bounds = getObjectBounds(ob);

            if (x >= bounds.x && x <= bounds.x + bounds.width &&
                y >= bounds.y && y <= bounds.y + bounds.height) {
                return ob;
            }
        }
        return null;
    }

    function drawShape(figura) {
        canvasCont.save();
        canvasCont.fillStyle = figura.color;
        canvasCont.strokeStyle = figura.color;
        canvasCont.lineWidth = 2;
        const x = figura.x, y = figura.y, s = figura.size;
        if (figura.type === 'circulo') {
            canvasCont.beginPath();
            canvasCont.arc(x, y, s, 0, Math.PI * 2);
            canvasCont.fill();
            canvasCont.closePath();
        } else if (figura.type === 'cuadrado') {
            canvasCont.fillRect(x - s, y - s, s * 2, s * 2);
        } else if (figura.type === 'triangulo') {
            canvasCont.beginPath();
            canvasCont.moveTo(x, y - s);
            canvasCont.lineTo(x - s, y + s);
            canvasCont.lineTo(x + s, y + s);
            canvasCont.closePath();
            canvasCont.fill();
        } else if (figura.type === 'estrella') {
            drawStar(canvasCont, x, y, s, 7);
            canvasCont.fill();
        }
        canvasCont.restore();
    }

    function drawFreehand(o) {
        const pts = o.points;
        if (!pts || pts.length === 0) return;
        canvasCont.save();
        canvasCont.strokeStyle = o.color;
        canvasCont.lineWidth = (o.size || 2);
        canvasCont.lineJoin = 'round';
        canvasCont.lineCap = 'round';
        canvasCont.beginPath();
        canvasCont.moveTo(pts[0].x, pts[0].y);
        for (let i = 1; i < pts.length; i++) canvasCont.lineTo(pts[i].x, pts[i].y);
        canvasCont.stroke();
        canvasCont.closePath();
        canvasCont.restore();
    }

    function drawStar(ctx, x, y, r, puntas) {
        const outer = r, inner = r * 0.45, step = Math.PI / puntas;
        ctx.beginPath();
        for (let i = 0; i < 2 * puntas; i++) {
            const radius = (i % 2 === 0) ? outer : inner;
            const a = i * step - Math.PI / 2;
            const px = x + Math.cos(a) * radius;
            const py = y + Math.sin(a) * radius;
            if (i === 0) ctx.moveTo(px, py); else ctx.lineTo(px, py);
        }
        ctx.closePath();
    }

    function drawShapePreview(o) {
        if (!o) return;
        canvasCont.save();
        canvasCont.globalAlpha = 0.45;
        canvasCont.fillStyle = o.color;
        canvasCont.strokeStyle = o.color;
        canvasCont.lineWidth = 1;
        const x = o.startX, y = o.startY, s = o.size;
        if (o.type === 'circulo') {
            canvasCont.beginPath();
            canvasCont.arc(x, y, s, 0, Math.PI * 2);
            canvasCont.fill();
            canvasCont.closePath();
        } else if (o.type === 'cuadrado') {
            canvasCont.fillRect(x - s, y - s, s * 2, s * 2);
        } else if (o.type === 'triangulo') {
            canvasCont.beginPath();
            canvasCont.moveTo(x, y - s);
            canvasCont.lineTo(x - s, y + s);
            canvasCont.lineTo(x + s, y + s);
            canvasCont.closePath();
            canvasCont.fill();
        } else if (o.type === 'estrella') {
            drawStar(canvasCont, x, y, s, 7);
            canvasCont.fill();
        }
        canvasCont.restore();
    }
    function addObject(o) {
      pushHistory();
      o.id = idCounter++;
      objects.push(o);
      setSelectedObject(o);
      redraw();
      saveDraft();
      triggerAutosave();
    }

    function renderList() {
      objectsList.innerHTML = '';
      objects.forEach((o, idx) => {
        const li = document.createElement('li');
        li.className = 'object-item';
        if (o === selectedObject) li.classList.add('selected');
        li.addEventListener('click', () => { setSelectedObject(o); });

        const left = document.createElement('div'); left.className = 'object-left';
        const displayName = document.createElement('div'); displayName.className = 'object-name';
        displayName.innerHTML = '<strong>' + (o.name || (o.type + ' ' + (idx + 1))) + '</strong>';
        const meta = document.createElement('div'); meta.className = 'small';
        if (o.type === 'libre') {
             meta.textContent = o.type + ' (' + o.points.length + ' pts)';
        } else {
             meta.textContent = o.type + ' @ ' + Math.round(o.x) + ',' + Math.round(o.y);
        }

        left.appendChild(displayName); left.appendChild(meta);
        const right = document.createElement('div'); right.className = 'object-right';
        const del = document.createElement('button'); del.textContent = 'Delete';
        del.addEventListener('click', (e) => { e.stopPropagation(); removeObject(o.id); });
        right.appendChild(del);

        if (!o.renamed) {
          const startRename = (e) => {
            e.stopPropagation();
            const input = document.createElement('input'); input.type = 'text'; input.value = o.name || (o.type + ' ' + (idx + 1));
            const save = document.createElement('button'); save.textContent = 'Save';
            save.addEventListener('click', () => {
                pushHistory();
                const v = input.value.trim();
                if (v.length > 0) {
                    o.name = v;
                    o.renamed = true;
                    renderList();
                    saveDraft();
                    triggerAutosave();
                }
            });
            displayName.innerHTML = ''; displayName.appendChild(input); displayName.appendChild(save);
          };
          displayName.addEventListener('dblclick', startRename);
        }
        li.appendChild(left); li.appendChild(right); objectsList.appendChild(li);
      });
    }

    function setSelectedObject(o) {
        selectedObject = o;
        if (o) {
            colorInput.value = o.color;
            sizeInput.value = o.size;
            sizeDisplay.textContent = o.size;
        }
        renderList();
        redraw();
    }

    function removeObject(id) {
      pushHistory();
      if (selectedObject && selectedObject.id === id) {
          setSelectedObject(null);
      }
      objects = objects.filter(o => o.id !== id);
      renderList();
      redraw();
      saveDraft();
      triggerAutosave();
    }

    function clearAll() {
      if (confirm('Borrar tots els objectes?')) {
        pushHistory();
        objects = [];
        setSelectedObject(null);
        renderList();
        redraw();
        clearDraft(getLocalKey());
        triggerAutosave();
      }
    }

    function loadState(stateData) {
        if (!stateData) return;

        const state = JSON.parse(JSON.stringify(stateData));

        objects = state.objects || [];
        const w = state.width || 800;
        const h = state.height || 600;

        canvas.width = w; canvas.height = h;
        currentWidth = w; currentHeight = h;
        inputW.value = w; inputH.value = h;

        let maxId = 0;
        objects.forEach(o => { if (o.id && o.id > maxId) maxId = o.id; });
        idCounter = maxId + 1;

        setSelectedObject(null);
        renderList();
        redraw();
    }

    function loadCanvasDataFromServer(data) {
        if (!data || !data.objects) return;
        loadState(data);
    }

    function pushHistory() {
      const currentSelectedId = selectedObject ? selectedObject.id : null;

      const currentState = {
        objects: JSON.parse(JSON.stringify(objects)),
        width: currentWidth,
        height: currentHeight,
        selectedId: currentSelectedId
      };
      history.push(currentState);
      redoStack = [];
      updateUndoRedoButtons();
    }

    function updateUndoRedoButtons() {
      undoBtn.disabled = history.length < 2;
      redoBtn.disabled = redoStack.length === 0;
    }

    function doUndo() {
      if (history.length < 2) return;
      const currentState = history.pop();
      redoStack.push(currentState);
      const previousState = history[history.length - 1];
      loadState(previousState);
      if (previousState.selectedId) {
          const found = objects.find(o => o.id === previousState.selectedId);
          setSelectedObject(found || null);
      }
      saveDraft();
      updateUndoRedoButtons();
      triggerAutosave();
    }

    function doRedo() {
      if (redoStack.length === 0) return;
      const stateToRestore = redoStack.pop();
      loadState(stateToRestore);
      if (stateToRestore.selectedId) {
          const found = objects.find(o => o.id === stateToRestore.selectedId);
          setSelectedObject(found || null);
      }
      history.push(JSON.parse(JSON.stringify(stateToRestore)));
      saveDraft();
      updateUndoRedoButtons();
      triggerAutosave();
    }

    function getLocalKey() {
      return `canvas_autosave_${currentCanvasId || 'new'}`;
    }

    function saveDraft() {
      const key = getLocalKey();
      if (objects.length === 0) {
          localStorage.removeItem(key);
          return;
      }
      const payload = {
        width: canvas.width,
        height: canvas.height,
        objects: objects
      };
      try {
          localStorage.setItem(key, JSON.stringify(payload));
          console.log(`Borrador guardado en: ${key}`);
      } catch (e) {
          console.error("Error al guardar en localStorage", e);
      }
    }

    function clearDraft(key) {
      localStorage.removeItem(key);
      console.log(`Borrador local limpiado: ${key}`);
    }

    function canvasPos(e) { const r = canvas.getBoundingClientRect(); return { x: e.clientX - r.left, y: e.clientY - r.top }; }

    canvas.addEventListener('mousedown', e => {
      const p = canvasPos(e);

      if (modeSelect.value === 'select') {
          const hitObject = isHit(p.x, p.y);
          setSelectedObject(hitObject);

          if (hitObject) {
              isDragging = true;
              dragOffsetX = p.x - hitObject.x;
              dragOffsetY = p.y - hitObject.y;

              if (hitObject.type === 'libre') {
                  dragOffsetX = p.x;
                  dragOffsetY = p.y;
              }
          }
      } else if (modeSelect.value === 'libre') {
        drawing = true;
        currentFree = { type: 'libre', color: colorInput.value, size: parseInt(sizeInput.value, 10) || 2, points: [] };
        currentFree.points.push(p);
      } else if (modeSelect.value === 'shape') {
        drawing = true;
        currentShape = { type: shapeType.value, startX: p.x, startY: p.y, size: parseInt(sizeInput.value, 10) || 40, color: colorInput.value };
      }
    });

    canvas.addEventListener('mousemove', e => {
      const p = canvasPos(e);

      if (isDragging && selectedObject) {
          if (selectedObject.type === 'libre') {
              const dx = p.x - dragOffsetX;
              const dy = p.y - dragOffsetY;
              selectedObject.points.forEach(pt => {
                  pt.x += dx;
                  pt.y += dy;
              });
              dragOffsetX = p.x;
              dragOffsetY = p.y;
          } else {
              selectedObject.x = p.x - dragOffsetX;
              selectedObject.y = p.y - dragOffsetY;
          }
          redraw();

      } else if (drawing && modeSelect.value === 'libre' && currentFree) {
        currentFree.points.push(p); redraw(); drawFreehand(currentFree);
      } else if (drawing && currentShape) {
        const dx = p.x - currentShape.startX; const dy = p.y - currentShape.startY;
        const r = Math.sqrt(dx * dx + dy * dy);
        currentShape.size = Math.max(1, Math.round(r));
        redraw();
        drawShapePreview(currentShape);
      }
    });

    window.addEventListener('mouseup', e => {
      if (isDragging) {
          isDragging = false;
          if (dragOffsetX !== 0 || dragOffsetY !== 0) {
              pushHistory();
              saveDraft();
              renderList();
              triggerAutosave();
          }
          dragOffsetX = 0;
          dragOffsetY = 0;
      }

      const p = canvasPos(e);
      if (drawing && modeSelect.value === 'libre' && currentFree) {
        currentFree.name = ('dibuix ' + (objects.filter(o => o.type === 'libre').length + 1));
        addObject(currentFree);
        currentFree = null;
      } else if (drawing && currentShape) {
        const s = currentShape.size || (parseInt(sizeInput.value, 10) || 40);
        const o = { type: currentShape.type, x: currentShape.startX, y: currentShape.startY, size: s, color: currentShape.color };
        addObject(o);
        currentShape = null;
      }
      drawing = false;
    });

    canvas.addEventListener('touchstart', e => { e.preventDefault(); if (modeSelect.value === 'libre') { drawing = true; currentFree = { type: 'libre', color: colorInput.value, size: parseInt(sizeInput.value, 10) || 2, points: [] }; const t = e.touches[0]; const p = canvasPos(t); currentFree.points.push(p); } });
    canvas.addEventListener('touchmove', e => { e.preventDefault(); if (drawing && currentFree) { const t = e.touches[0]; const p = canvasPos(t); currentFree.points.push(p); redraw(); drawFreehand(currentFree); } });
    canvas.addEventListener('touchend', e => { if (drawing && currentFree) { currentFree.name = ('dibuix ' + (objects.filter(o => o.type === 'libre').length + 1)); addObject(currentFree); currentFree = null; } drawing = false; });

    document.getElementById('addCenter').addEventListener('click', () => { const s = parseInt(sizeInput.value, 10) || 40; const o = { type: shapeType.value, x: canvas.width / 2, y: canvas.height / 2, size: s, color: colorInput.value }; addObject(o); });
    document.getElementById('clearAll').addEventListener('click', clearAll);

    document.getElementById('applySize').addEventListener('click', () => {
      pushHistory();
      const w = Math.max(100, Math.min(2000, parseInt(inputW.value) || currentWidth));
      const h = Math.max(100, Math.min(2000, parseInt(inputH.value) || currentHeight));
      const sx = w / currentWidth; const sy = h / currentHeight;
      objects.forEach(o => { o.x = o.x * sx; o.y = o.y * sy; if (o.type === 'libre' && o.points) o.points.forEach(p => { p.x = p.x * sx; p.y = p.y * sy; }); });
      canvas.width = w; canvas.height = h; currentWidth = w; currentHeight = h;
      inputW.value = w; inputH.value = h;
      redraw();
      saveDraft();
      triggerAutosave();
    });

    document.getElementById('resetSize').addEventListener('click', () => {
      pushHistory();
      const w = (CanvasConf.initialWidth || canvas.width);
      const h = (CanvasConf.initialHeight || canvas.height);
      inputW.value = w; inputH.value = h;
      const sx = w / currentWidth; const sy = h / currentHeight;
      objects.forEach(o => { o.x = o.x * sx; o.y = o.y * sy; if (o.type === 'libre' && o.points) o.points.forEach(p => { p.x = p.x * sx; p.y = p.y * sy; }); });
      canvas.width = w; canvas.height = h; currentWidth = w; currentHeight = h; inputW.value = w; inputH.value = h;
      redraw();
      saveDraft();
      triggerAutosave();
    });

    undoBtn.addEventListener('click', doUndo);
    redoBtn.addEventListener('click', doRedo);
    modeSelect.addEventListener('change', (e) => {
        if (e.target.value === 'select') {
            canvas.classList.add('select-mode');
        } else {
            canvas.classList.remove('select-mode');
        }
    });
    if (modeSelect.value === 'select') {
        canvas.classList.add('select-mode');
    }

    if (toolboxHeader && toolbox) {

        function onToolboxDrag(e) {
            if (!isDraggingToolbox) return;
            e.preventDefault();
            let newX = e.clientX - dragToolboxOffsetX;
            let newY = e.clientY - dragToolboxOffsetY;
            const maxW = window.innerWidth - toolbox.offsetWidth;
            const maxH = window.innerHeight - toolbox.offsetHeight;
            newX = Math.max(0, Math.min(newX, maxW));
            newY = Math.max(0, Math.min(newY, maxH));
            toolbox.style.left = newX + 'px';
            toolbox.style.top = newY + 'px';
        }

        function onToolboxStopDrag() {
            isDraggingToolbox = false;
            document.removeEventListener('mousemove', onToolboxDrag);
            document.removeEventListener('mouseup', onToolboxStopDrag);
        }

        toolboxHeader.addEventListener('mousedown', (e) => {
            isDraggingToolbox = true;
            dragToolboxOffsetX = e.clientX - toolbox.offsetLeft;
            dragToolboxOffsetY = e.clientY - toolbox.offsetTop;

            document.addEventListener('mousemove', onToolboxDrag);
            document.addEventListener('mouseup', onToolboxStopDrag);
            e.preventDefault();
        });
    }

    colorInput.addEventListener('change', (e) => {
        if (selectedObject) {
            pushHistory();
            selectedObject.color = e.target.value;
            redraw();
            saveDraft();
            triggerAutosave();
        }
    });

    sizeInput.addEventListener('input', (e) => {
        const newSize = parseInt(e.target.value, 10);
        sizeDisplay.textContent = newSize;
        if (selectedObject) {
            selectedObject.size = newSize;
            redraw();
        }
    });

    sizeInput.addEventListener('change', (e) => {
        if (selectedObject) {
            pushHistory();
            saveDraft();
            triggerAutosave();
        }
    });

    function saveToServer(showAlert = false) {
        if (!saveBtn) return;

        if (!currentCanvasId && !showAlert) {
            console.log("Autoguardado saltado: Canvas nuevo. Guarda manualmente primero.");
            return;
        }

        if (autosaveStatus) {
            autosaveStatus.textContent = "Guardando...";
            autosaveStatus.style.opacity = "1";
        }

        const payload = {
          width: canvas.width,
          height: canvas.height,
          objects: objects
        };
        let name = canvasNameDisplay.textContent;
        const oldKey = getLocalKey();

        const basePath = (contextPath === '/') ? '' : contextPath;

        let url = basePath + '/saveCanvas?name=' + encodeURIComponent(name);
        if (currentCanvasId) {
            url += '&id=' + encodeURIComponent(currentCanvasId);
        }

        fetch(url, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json;charset=UTF-8' },
          body: JSON.stringify(payload)
        })
        .then(r => {
            if (!r.ok) { throw new Error('Error ' + r.status + ': ' + r.statusText); }
            return r.json();
        })
        .then(j => {
          if (j && j.status === 'ok') {
            currentCanvasId = j.id;

            if (autosaveStatus) {
                autosaveStatus.textContent = "Guardado.";
                setTimeout(() => { autosaveStatus.style.opacity = "0"; }, 2000);
            }

            clearDraft(oldKey);
            if (oldKey.endsWith('_new') && getLocalKey() !== oldKey) {
                clearDraft('canvas_autosave_new');
            }

            if (showAlert) {
                alert('Canvas guardado con ID: ' + j.id);
                history = [ history[history.length - 1] ];
                redoStack = [];
                updateUndoRedoButtons();
            }

          } else {
            if (autosaveStatus) autosaveStatus.textContent = "Error al guardar.";
            if (showAlert) alert('No se ha podido guardar (respuesta JSON no OK)');
          }
        }).catch(err => {
            console.error(err);
            if (autosaveStatus) {
                autosaveStatus.textContent = "Error al guardar.";
                setTimeout(() => { autosaveStatus.style.opacity = "0"; }, 2000);
            }
            if (showAlert) alert('guardado ha fallado: ' + err.message);
        });
    }

    function triggerAutosave() {
        clearTimeout(autosaveTimer);

        if (!saveBtn || !currentCanvasId) {
            return;
        }

        if (autosaveStatus) {
            autosaveStatus.textContent = "Guardando...";
            autosaveStatus.style.opacity = "1";
        }

        autosaveTimer = setTimeout(() => saveToServer(false), 2500);
    }


    if (saveBtn) {
      saveBtn.addEventListener('click', () => {
        clearTimeout(autosaveTimer);
        saveToServer(true);
      });
    }

    const settingsToggle = document.getElementById('settingsToggle');
    const sizePanel = document.getElementById('sizePanel');
    if (settingsToggle && sizePanel) {
      sizePanel.style.display = 'none';
      settingsToggle.addEventListener('click', () => { sizePanel.style.display = (sizePanel.style.display === 'none') ? 'block' : 'none'; });
    }
    if (inputW) inputW.value = CanvasConf.initialWidth || currentWidth;
    if (inputH) inputH.value = CanvasConf.initialHeight || currentHeight;

    const initialKey = getLocalKey(); 
    let loadedFrom = null;

    if (window.INITIAL_CANVAS_DATA) {
        try {
            loadCanvasDataFromServer(window.INITIAL_CANVAS_DATA);
            clearDraft(initialKey);
            loadedFrom = 'server';
        } catch (e) { console.error("Error al cargar datos del servidor", e); }
    } else {
        const localData = localStorage.getItem(initialKey);
        if (localData) {
            try {
                loadState(JSON.parse(localData));
                loadedFrom = 'local';
            } catch (e) { console.error("Error al cargar datos de localStorage", e); }
        }
    }
    pushHistory();
    updateUndoRedoButtons();

  });
})();