XODO 🚀
"Tecnología élite, lenguaje local."

XODO es un ecosistema digital adaptativo diseñado para democratizar el acceso a herramientas de gestión empresarial de alto nivel. Su objetivo es conectar a toda la cadena productiva de Tabasco —desde la micro-tienda hasta la gran corporación— eliminando la brecha tecnológica mediante el uso de Inteligencia Artificial.

🏛️ PARTE 1: LA VISIÓN DEL PROYECTO (Villahermosa 2030)
Este apartado describe la solución completa como producto final de mercado.

🌟 El Problema y la Solución
El 100% de las aplicaciones complejas fallan en PYMES porque no se adaptan al usuario. XODO soluciona esto mediante una Arquitectura de Interfaz Dual:

Zero-UI (Micro-empresa): Registro de ventas mediante voz e imagen. La IA traduce "Vendí dos sacos de cemento" a datos contables estructurados.
Dashboard Analítico (Mediana/Grande): Control de inventarios, gestión por medidas (m2, kg), reportes fiscales y exportación a ERPs masivos (SAP).
🛠️ Stack Tecnológico (Producción)
Backend: Java (Spring Boot) - Motor lógico y seguridad.
Frontend: PWA con HTML5, CSS3, JS (Offline First).
IA Core: Google Gemini / OpenAI API - NLP y Visión Computacional.
Base de Datos: PostgreSQL con soporte JSONB para esquemas híbridos.
Integración: JSON standard para SAT (Facturación 4.0) y SAP.
📂 Arquitectura del Sistema
Diseño modular basado en Domain Driven Design (DDD):

plaintext

XODO/
├── src/main/java/com/xodo/
│   ├── core/               # El "Cerebro"
│   │   ├── ai/             # Conexión Gemini/OpenAI
│   │   ├── sat/            # Lógica fiscal
│   │   └── integration/    # Adaptadores SAP
│   ├── modules/            # Módulos de Negocio
│   │   ├── inventory/      # Productos dinámicos (JSONB)
│   │   ├── sales/          # Ventas y Pedidos
│   │   └── identity/       # Perfil de Complejidad (Micro vs Corp)
🚀 Funcionalidades Clave
Traductor Inteligente: NLP para convertir lenguaje coloquial tabasqueño en transacciones formales.
Módulo Fiscal Invisible: Asignación automática de claves SAT.
Modo Resiliente: Operatividad offline con sincronización automática.
⚡ PARTE 2: PROTOCOLO HACKATHON (Prototipo MVP)
Este apartado es la guía táctica para construir la demo funcional en 24-48 horas.

🎯 Objetivo del Prototipo
Demostrar el "Happy Path":

Input: Voz/Texto natural.
Proceso: IA estructura la data.
Output: Visualización dual (Simple vs. Compleja) instantánea.
🛠️ Stack Simplificado (Modo Hackathon)
BD: H2 (En memoria) o Listas estáticas en Java (para velocidad).
Front: Vanilla JS + Web Speech API (para evitar configuración de frameworks).
Back: Spring Boot Controller simple + RestClient para la IA.
📂 Estructura de Trabajo Rápida
plaintext

src/main/java/com/xodo/
├── controller/
│   └── XodoController.java  // Endpoint único: Recibe texto -> Devuelve JSON
├── service/
│   └── AiService.java       // Prompt Engineering para Gemini
└── model/
    └── DemoTransaction.java // POJO simplificado para la vista
🧠 El "Golden Prompt" (La clave del éxito)
Este es el prompt que deben enviar a la IA para que la magia ocurra en la demo:

"Actúa como un ERP inteligente. Analiza la frase: [INPUT_USUARIO].
Devuelve SOLO un JSON con:
{"accion": "VENTA", "items": [{"producto": "nombre", "cantidad": 0, "sku_ficticio": "ABC"}], "mensaje_micro": "resumen corto", "mensaje_corp": "detalle técnico con impuestos"}.
Si hay modismos, interprétalos formalmente."

🎨 Interfaz de la Demo (UI)
Una sola pantalla (index.html) dividida en dos o con un Switch Toggle:

Botón Micrófono (Grande):
Usa webkitSpeechRecognition en JS.
Al soltar, envía texto al Backend.
Vista A (La Tiendita):
Tarjeta minimalista.
Texto: "✅ Venta: 2 Cementos. Total: $600".
Vista B (El Corporativo - Al activar Switch):
La tarjeta se expande.
Muestra tabla: SKU: CEM-50KG | IVA: 16% | Stock Restante: 48 | Estatus SAT: Pendiente.
🚨 Tips de Supervivencia
Datos Hardcodeados: Si la API de IA falla en vivo, ten un botón oculto que cargue un JSON perfecto pre-hecho.
Enfóquense en el flujo: No pierdan tiempo en Login/Registro. Entren directo al Dashboard.
Visual: Usen colores oscuros y neones (Estilo "Elite") para que se vea profesional rápidamente.
