document.addEventListener('DOMContentLoaded', () => {
    console.log("🚀 XODO Frontend Iniciado");

    // ==========================================
    // 1. SELECTORES Y VARIABLES GLOBALES
    // ==========================================
    const $ = id => document.getElementById(id);

    const input = $('commandInput');
    const micBtn = $('mainMicBtn');
    const suggestionsBox = $('suggestions');

    // Elementos de Cámara
    const cameraBtn = $('cameraBtn');
    const cameraModal = $('cameraModal');
    const closeCamera = $('closeCamera');
    const snapPhoto = $('snapPhoto');
    const video = $('videoPreview');
    const canvas = $('photoCanvas');
    let stream = null;

    // Crear contenedor de resultados si no existe
    let resultContainer = $('resultContainer');
    if (!resultContainer) {
        resultContainer = document.createElement('div');
        resultContainer.id = 'resultContainer';
        resultContainer.className = 'result-area';
        document.querySelector('.hero').appendChild(resultContainer);
    }

    // ==========================================
    // 2. BASE DE DATOS DE SUGERENCIAS (Frontend)
    // ==========================================
    const promptsPorNegocio = {
        'TIENDA': [
            "Vendí 2 cocas y unas sabritas",
            "1 leche lala y un paquete de emperador",
            "Vendí 3 sabritas"
        ],
        'FERRETERIA': [
            "3 sacos de cemento y 1kg de clavos",
            "Dame 5 varillas de 3/8",
            "Vendí un juego de llaves allen"
        ],
        'IMPRENTA': [
            "Imprime lona de 2x1 metros",
            "Corte de vinil adherible 5 metros",
            "100 tarjetas de presentación"
        ]
    };

    // ==========================================
    // 3. FUNCIONES GLOBALES (Expuestas a HTML)
    // ==========================================

    // --- A. CAMBIAR CONTEXTO DE NEGOCIO ---
    window.cambiarNegocio = async (tipo) => {
        console.log("🔄 Cambiando contexto a:", tipo);

        // 1. UI: Actualizar botones de arriba (Estilo Activo)
        const botones = document.querySelectorAll('.btn-biz');
        if(botones) {
            botones.forEach(btn => {
                // Compara texto del botón con el tipo (ej: "Tiendita" vs "TIENDA")
                if(btn.innerText.toUpperCase().includes(tipo.substring(0,3))) {
                    btn.classList.add('active');
                } else {
                    btn.classList.remove('active');
                }
            });
        }

        // 2. UI: Renderizar sugerencias nuevas
        renderSugerencias(tipo);

        // 3. UI: Cambiar Placeholder
        const nombreBonito = tipo.charAt(0) + tipo.slice(1).toLowerCase();
        if(input) input.placeholder = `Modo ${nombreBonito}: Escribe o usa cámara...`;

        // 4. BACKEND: Avisar a Java para cambiar el RAG
        try {
            await fetch(`/api/v1/cambiar-negocio?tipo=${tipo}`);
            // Limpiar ticket anterior para no confundir
            if(resultContainer) resultContainer.innerHTML = "";
        } catch (e) {
            console.error("Error conectando con backend:", e);
        }
    };

    // --- B. PROCESAR TEXTO O IMAGEN (Cerebro) ---
    window.processText = async (text, isImage = false) => {
        if (!text && !isImage) return;

        // UI: Loading
        input.value = isImage ? "Analizando imagen..." : text;
        input.disabled = true;

        resultContainer.innerHTML = `
            <div class="glass-panel" style="text-align:center; padding: 20px; color: #00d2d3;">
                <span class="material-symbols-outlined animate-spin" style="font-size: 30px;">sync</span>
                <p>${isImage ? "XODO Vision Analizando..." : "Procesando venta..."}</p>
            </div>
        `;

        try {
            // Construir payload
            const payload = isImage ? { imagenBase64: text } : { texto: text };

            // Llamada al Backend
            const response = await fetch('/api/v1/procesar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!response.ok) throw new Error("Error en servidor Java");

            const data = await response.json();

            renderTicket(data);
            speak(data.resumenHumano);

        } catch (error) {
            console.error(error);
            resultContainer.innerHTML = `
                <div class="glass-panel error-panel" style="padding:20px; color:#ff6b6b;">
                    <h3>⚠️ Error</h3>
                    <p>${error.message}</p>
                </div>
            `;
        } finally {
            input.disabled = false;
            if(isImage) input.value = "";
        }
    };

    // ==========================================
    // 4. FUNCIONES DE AYUDA (UI)
    // ==========================================

    const renderSugerencias = (tipo) => {
        if(!suggestionsBox) return;

        // Obtener lista o default
        const lista = promptsPorNegocio[tipo] || promptsPorNegocio['TIENDA'];

        // Generar HTML de botones
        suggestionsBox.innerHTML = lista.map(txt =>
            `<button class="suggestion-btn" onclick="processText('${txt}')">
                "${txt}"
             </button>`
        ).join('');
    };

    const renderTicket = (data) => {
        // Manejo de error lógico de IA
        if (data.status === 'ERROR') {
            resultContainer.innerHTML = `
                <div class="glass-panel" style="padding: 20px; border-left: 4px solid #ff9f43;">
                    <h3>🤔 Ups...</h3>
                    <p>${data.resumenHumano}</p>
                </div>`;
            return;
        }

        // Construir items
        const itemsHtml = data.items.map(item => `
            <div style="display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px dashed rgba(255,255,255,0.2);">
                <span>${item.cantidad} ${item.unidad} - ${item.descripcion}</span>
                <span style="font-weight: bold;">$${item.importe.toFixed(2)}</span>
            </div>
        `).join('');

        // Ticket Final
        resultContainer.innerHTML = `
            <div class="glass-panel ticket-animation" style="padding: 25px;">
                <div style="text-align: center; margin-bottom: 15px;">
                    <h3 style="margin: 0; color: #00d2d3;">XODO TICKET</h3>
                    <small style="opacity: 0.6;">Folio: ${data.folio || 'X-NEW'}</small>
                </div>

                <div style="margin-bottom: 20px; font-family: 'Courier New', monospace;">
                    ${itemsHtml}
                </div>

                <div style="display: flex; justify-content: space-between; font-size: 1.4em; font-weight: bold; border-top: 2px solid #fff; padding-top: 10px;">
                    <span>TOTAL</span>
                    <span style="color: #2ecc71;">$${data.total.toFixed(2)}</span>
                </div>

                <p style="margin-top: 15px; font-size: 0.9em; opacity: 0.8;">
                    ✅ ${data.resumenHumano}
                </p>
            </div>
        `;
    };

    const speak = (txt) => {
        if ('speechSynthesis' in window) {
            window.speechSynthesis.cancel();
            const u = new SpeechSynthesisUtterance(txt);
            u.lang = 'es-MX';
            u.rate = 1.1;
            window.speechSynthesis.speak(u);
        }
    };

    // ==========================================
    // 5. LÓGICA DE CÁMARA (XODO VISION)
    // ==========================================

    // A. Abrir Cámara
    if (cameraBtn) {
        cameraBtn.addEventListener('click', async () => {
            if(cameraModal) {
                cameraModal.style.display = 'flex';
                cameraModal.classList.remove('hidden');
            }
            try {
                stream = await navigator.mediaDevices.getUserMedia({
                    video: { facingMode: "environment" }
                });
                video.srcObject = stream;
            } catch (err) {
                alert("Error de cámara: " + err.name);
                closeCameraFunc();
            }
        });
    }

    // B. Cerrar Cámara
    const closeCameraFunc = () => {
        if(cameraModal) cameraModal.style.display = 'none';
        if (stream) {
            stream.getTracks().forEach(track => track.stop());
            stream = null;
        }
    };
    if(closeCamera) closeCamera.addEventListener('click', closeCameraFunc);

    // C. Tomar Foto
    if(snapPhoto) {
            snapPhoto.addEventListener('click', () => {
                if (!video || !canvas) return;

                // OPTIMIZACIÓN: Reducir resolución drásticamente
                // No necesitamos 4K, con 512px basta para reconocer una Coca
                const maxWidth = 512;
                const scale = maxWidth / video.videoWidth;

                canvas.width = maxWidth;
                canvas.height = video.videoHeight * scale;

                const ctx = canvas.getContext('2d');
                ctx.drawImage(video, 0, 0, canvas.width, canvas.height);

                // OPTIMIZACIÓN: Calidad JPG baja (0.5)
                // Esto reduce el peso de 4MB -> 50KB (¡100 veces más rápido!)
                const base64 = canvas.toDataURL('image/jpeg', 0.5);

                closeCameraFunc();
                window.processText(base64, true);
            });
        }

    // ==========================================
    // 6. LISTENERS GENERALES
    // ==========================================

    // Enter en input
    input?.addEventListener('keydown', (e) => {
        if (e.key === 'Enter') window.processText(input.value);
    });

    // Micrófono (Alert simple para demo)
    micBtn?.addEventListener('click', () => {
        alert("🎤 Para esta demo, por favor usa los botones de contexto o escribe el comando.");
    });

    // ==========================================
    // 7. INICIALIZACIÓN
    // ==========================================
    // Forzar carga inicial de Tienda
    setTimeout(() => {
        window.cambiarNegocio('TIENDA');
    }, 200);
});