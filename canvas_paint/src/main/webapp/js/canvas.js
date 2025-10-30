    (function(){
      const canvas = document.getElementById('paintCanvas');
      const ctx = canvas.getContext('2d');
      let drawing = false;
      // keep a copy of initial sizes
      const initialWidth = canvas.width;
      const initialHeight = canvas.height;

      function pos(e){
        const r = canvas.getBoundingClientRect();
        return { x: e.clientX - r.left, y: e.clientY - r.top };
      }

      canvas.addEventListener('mousedown', e => { drawing = true; const p = pos(e); ctx.beginPath(); ctx.moveTo(p.x,p.y); });
      canvas.addEventListener('mousemove', e => { if(!drawing) return; const p = pos(e); ctx.lineTo(p.x,p.y); ctx.stroke(); });
      window.addEventListener('mouseup', () => { drawing = false; });

      // simple touch support
      canvas.addEventListener('touchstart', e => { e.preventDefault(); drawing = true; const t = e.touches[0]; const p = {x: t.clientX - canvas.getBoundingClientRect().left, y: t.clientY - canvas.getBoundingClientRect().top}; ctx.beginPath(); ctx.moveTo(p.x,p.y); });
      canvas.addEventListener('touchmove', e => { e.preventDefault(); if(!drawing) return; const t = e.touches[0]; const p = {x: t.clientX - canvas.getBoundingClientRect().left, y: t.clientY - canvas.getBoundingClientRect().top}; ctx.lineTo(p.x,p.y); ctx.stroke(); });
      window.addEventListener('touchend', () => { drawing = false; });

      // inline new-canvas controls
      const inputW = document.getElementById('inputWidth');
      const inputH = document.getElementById('inputHeight');
      const applyBtn = document.getElementById('applySize');
      const resetBtn = document.getElementById('resetSize');
      const sizeLabel = document.getElementById('sizeLabel');

      function clamp(v){ return Math.max(100, Math.min(2000, Math.floor(v) || 0)); }

      applyBtn.addEventListener('click', () => {
        const w = clamp(inputW.value);
        const h = clamp(inputH.value);
        // resize canvas while preserving current drawing: create temp canvas
        const tmp = document.createElement('canvas');
        tmp.width = w; tmp.height = h;
        const tctx = tmp.getContext('2d');
        // draw old canvas onto tmp (will scale if sizes differ)
        tctx.fillStyle = '#fff';
        tctx.fillRect(0,0,w,h);
        tctx.drawImage(canvas, 0, 0, w, h);
        // apply to real canvas
        canvas.width = w; canvas.height = h;
        ctx.drawImage(tmp,0,0);
        sizeLabel.textContent = w + ' x ' + h;
      });

      resetBtn.addEventListener('click', () => {
        canvas.width = initialWidth; canvas.height = initialHeight;
        // clear canvas (white background)
        ctx.fillStyle = '#fff'; ctx.fillRect(0,0,canvas.width, canvas.height);
        sizeLabel.textContent = initialWidth + ' x ' + initialHeight;
        inputW.value = initialWidth; inputH.value = initialHeight;
      });
    })();