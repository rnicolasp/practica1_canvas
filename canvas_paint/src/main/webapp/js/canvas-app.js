(function(){
  'use strict';
  function ready(fn){ if (document.readyState !== 'loading') fn(); else document.addEventListener('DOMContentLoaded', fn); }

  ready(function(){
  const canvas = document.getElementById('paintCanvas');
  if(!canvas) return;
  const cfg = window.CANVAS_CONFIG || { initialWidth: parseInt(canvas.getAttribute('data-initial-width'),10) || canvas.width, initialHeight: parseInt(canvas.getAttribute('data-initial-height'),10) || canvas.height };
    const ctx = canvas.getContext('2d');
    let drawing = false;
    let currentFree = null;
    let objects = [];
    let idCounter = 1;
    let currentWidth = canvas.width;
    let currentHeight = canvas.height;

    const modeSelect = document.getElementById('mode');
    const shapeType = document.getElementById('shapeType');
  const colorInput = document.getElementById('color');
  const sizeInput = document.getElementById('size');
  const sizeDisplay = document.getElementById('sizeDisplay');
  const objectsList = document.getElementById('objects');

    function redraw(){
      ctx.fillStyle = '#fff'; ctx.fillRect(0,0,canvas.width, canvas.height);
      objects.forEach(o => { if (o.type === 'freehand') drawFreehand(o); else drawShape(o); });
    }

    function drawShape(o){ ctx.save(); ctx.fillStyle=o.color; ctx.strokeStyle=o.color; ctx.lineWidth=2; const x=o.x,y=o.y,s=o.size; if(o.type==='circle'){ ctx.beginPath(); ctx.arc(x,y,s,0,Math.PI*2); ctx.fill(); ctx.closePath(); } else if(o.type==='square'){ ctx.fillRect(x-s,y-s,s*2,s*2); } else if(o.type==='triangle'){ ctx.beginPath(); ctx.moveTo(x,y-s); ctx.lineTo(x-s,y+s); ctx.lineTo(x+s,y+s); ctx.closePath(); ctx.fill(); } else if(o.type==='star7'){ drawStar(ctx,x,y,s,7); ctx.fill(); } ctx.restore(); }

  function drawFreehand(o){ const pts = o.points; if(!pts||pts.length===0) return; ctx.save(); ctx.strokeStyle=o.color; ctx.lineWidth = (o.size || 2); ctx.lineJoin='round'; ctx.lineCap='round'; ctx.beginPath(); ctx.moveTo(pts[0].x,pts[0].y); for(let i=1;i<pts.length;i++) ctx.lineTo(pts[i].x,pts[i].y); ctx.stroke(); ctx.closePath(); ctx.restore(); }

    function drawStar(ctx,x,y,r,points){ const outer=r, inner=r*0.45, step=Math.PI/points; ctx.beginPath(); for(let i=0;i<2*points;i++){ const radius=(i%2===0)?outer:inner; const a=i*step-Math.PI/2; const px=x+Math.cos(a)*radius; const py=y+Math.sin(a)*radius; if(i===0) ctx.moveTo(px,py); else ctx.lineTo(px,py); } ctx.closePath(); }

  function drawShapePreview(o){ if(!o) return; ctx.save(); ctx.globalAlpha = 0.45; ctx.fillStyle = o.color; ctx.strokeStyle = o.color; ctx.lineWidth = 1; const x=o.startX, y=o.startY, s=o.size; if(o.type==='circle'){ ctx.beginPath(); ctx.arc(x,y,s,0,Math.PI*2); ctx.fill(); ctx.closePath(); } else if(o.type==='square'){ ctx.fillRect(x-s,y-s,s*2,s*2); } else if(o.type==='triangle'){ ctx.beginPath(); ctx.moveTo(x,y-s); ctx.lineTo(x-s,y+s); ctx.lineTo(x+s,y+s); ctx.closePath(); ctx.fill(); } else if(o.type==='star7'){ drawStar(ctx,x,y,s,7); ctx.fill(); } ctx.restore(); }

    function addObject(o){ o.id=idCounter++; objects.push(o); renderList(); redraw(); }
    function renderList(){
      objectsList.innerHTML='';
      objects.forEach((o,idx)=>{
        const li=document.createElement('li'); li.className='object-item';
        const left=document.createElement('div'); left.className='object-left';
        const displayName = document.createElement('div'); displayName.className='object-name';
        displayName.innerHTML = '<strong>'+(o.name||(o.type+' '+(idx+1)))+'</strong>';
        const meta = document.createElement('div'); meta.className='small'; meta.textContent = o.type+' @ '+Math.round(o.x)+','+Math.round(o.y);
        left.appendChild(displayName); left.appendChild(meta);

        const right=document.createElement('div'); right.className='object-right';
        const del=document.createElement('button'); del.textContent='Delete'; del.addEventListener('click', ()=>{ removeObject(o.id); });
        right.appendChild(del);

        if(!o.renamed){
          const startRename = ()=>{
            const input=document.createElement('input'); input.type='text'; input.value = o.name || (o.type+' '+(idx+1));
            const save=document.createElement('button'); save.textContent='Save';
            save.addEventListener('click', ()=>{ const v = input.value.trim(); if(v.length>0){ o.name = v; o.renamed = true; renderList(); } });
            displayName.innerHTML=''; displayName.appendChild(input); displayName.appendChild(save);
          };
          displayName.addEventListener('dblclick', startRename);
        }

        li.appendChild(left); li.appendChild(right); objectsList.appendChild(li);
      });
    }
    function removeObject(id){ objects=objects.filter(o=>o.id!==id); renderList(); redraw(); }
    function clearAll(){ objects=[]; renderList(); redraw(); }

  function canvasPos(e){ const r=canvas.getBoundingClientRect(); return { x: e.clientX - r.left, y: e.clientY - r.top }; }

    canvas.addEventListener('mousedown', e=>{
      const p = canvasPos(e);
      if(modeSelect.value==='freehand'){
        drawing=true;
        currentFree={type:'freehand', color:colorInput.value, size: parseInt(sizeInput.value,10)||2, points:[]};
        currentFree.points.push(p);
      } else {
        drawing = true;
        currentShape = { type: shapeType.value, startX: p.x, startY: p.y, size: parseInt(sizeInput.value,10)||40, color: colorInput.value };
      }
    });

    canvas.addEventListener('mousemove', e=>{
      const p = canvasPos(e);
      if(drawing && modeSelect.value==='freehand' && currentFree){
        currentFree.points.push(p); redraw(); drawFreehand(currentFree);
      } else if(drawing && currentShape){
        const dx = p.x - currentShape.startX; const dy = p.y - currentShape.startY;
        const r = Math.sqrt(dx*dx + dy*dy);
        currentShape.size = Math.max(1, Math.round(r));
        redraw();
        drawShapePreview(currentShape);
      }
    });

    window.addEventListener('mouseup', e=>{
      const p = canvasPos(e);
      if(drawing && modeSelect.value==='freehand' && currentFree){
        currentFree.name = ('dibuix ' + (objects.filter(o=>o.type==='freehand').length+1));
        addObject(currentFree); currentFree=null;
      } else if(drawing && currentShape){
        const s = currentShape.size || (parseInt(sizeInput.value,10)||40);
        const o = { type: currentShape.type, x: currentShape.startX, y: currentShape.startY, size: s, color: currentShape.color };
        addObject(o); currentShape = null;
      }
      drawing=false;
    });

  canvas.addEventListener('touchstart', e=>{ e.preventDefault(); if(modeSelect.value==='freehand'){ drawing=true; currentFree={type:'freehand', color:colorInput.value, size: parseInt(sizeInput.value,10)||2, points:[]}; const t=e.touches[0]; const p=canvasPos(t); currentFree.points.push(p); } });
    canvas.addEventListener('touchmove', e=>{ e.preventDefault(); if(drawing && currentFree){ const t=e.touches[0]; const p=canvasPos(t); currentFree.points.push(p); redraw(); drawFreehand(currentFree); } });
  canvas.addEventListener('touchend', e=>{ if(drawing && currentFree){ currentFree.name = ('dibuix ' + (objects.filter(o=>o.type==='freehand').length+1)); addObject(currentFree); currentFree=null; } drawing=false; });

  document.getElementById('addCenter').addEventListener('click', ()=>{ const s=parseInt(sizeInput.value,10)||40; const o={ type: shapeType.value, x: canvas.width/2, y: canvas.height/2, size: s, color: colorInput.value }; addObject(o); });
    document.getElementById('clearAll').addEventListener('click', ()=>{ if(confirm('Clear all objects?')) clearAll(); });

    const inputW=document.getElementById('inputWidth');
    const inputH=document.getElementById('inputHeight');
    document.getElementById('applySize').addEventListener('click', ()=>{
      const w=Math.max(100, Math.min(2000, parseInt(inputW.value)||currentWidth));
      const h=Math.max(100, Math.min(2000, parseInt(inputH.value)||currentHeight));
  const sx = w / currentWidth; const sy = h / currentHeight;
  objects.forEach(o=>{ o.x = o.x * sx; o.y = o.y * sy; if(o.type==='freehand' && o.points) o.points.forEach(p=>{ p.x = p.x * sx; p.y = p.y * sy; }); });
      canvas.width = w; canvas.height = h; currentWidth = w; currentHeight = h;
      inputW.value = w; inputH.value = h;
      redraw();
    });

    document.getElementById('resetSize').addEventListener('click', ()=>{
      const w = (cfg.initialWidth || canvas.width);
      const h = (cfg.initialHeight || canvas.height);
      inputW.value = w; inputH.value = h;
  const sx = w / currentWidth; const sy = h / currentHeight;
  objects.forEach(o=>{ o.x = o.x * sx; o.y = o.y * sy; if(o.type==='freehand' && o.points) o.points.forEach(p=>{ p.x = p.x * sx; p.y = p.y * sy; }); });
      canvas.width = w; canvas.height = h; currentWidth = w; currentHeight = h; inputW.value = w; inputH.value = h; redraw();
    });

    const settingsToggle = document.getElementById('settingsToggle');
    const sizePanel = document.getElementById('sizePanel');
    if(settingsToggle && sizePanel){
      sizePanel.style.display = 'none';
      settingsToggle.addEventListener('click', ()=>{ sizePanel.style.display = (sizePanel.style.display === 'none') ? 'block' : 'none'; });
    }

    if(inputW) inputW.value = cfg.initialWidth || currentWidth;
    if(inputH) inputH.value = cfg.initialHeight || currentHeight;

  if(sizeDisplay && sizeInput){ sizeDisplay.textContent = sizeInput.value; sizeInput.addEventListener('input', ()=>{ sizeDisplay.textContent = sizeInput.value; }); }

    redraw();

    const saveBtn = document.getElementById('saveBtn');
    if(saveBtn){
      saveBtn.addEventListener('click', async ()=>{
        try{
          // first send JSON state to server
          const payload = { objects: objects, width: canvas.width, height: canvas.height };
          const res = await fetch('/saveCanvas', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(payload) });
          if(!res.ok){ throw new Error('Save failed'); }
          const json = await res.json();
          // then download PNG locally as user requested
          const data = canvas.toDataURL('image/png');
          const a = document.createElement('a');
          a.href = data;
          a.download = 'canvas.png';
          document.body.appendChild(a);
          a.click();
          a.remove();
          alert('Guardado en servidor: ' + (json.file || 'ok'));
        }catch(err){
          alert('Error saving canvas: ' + err.message);
        }
      });
    }
  });
})();
