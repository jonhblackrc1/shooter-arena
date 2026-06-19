# SHOOTER ARENA

Top-down alien shooter — PWA jugable en navegador y móvil.

- **20 niveles**, 4 jefes alienígenas
- Tienda de armas, selección de personaje, modos de juego y ranking
- Apuntado manual (mouse en PC, joystick derecho en móvil)
- Instalable como app (PWA) y empaquetable como APK para Play Store

## Jugar localmente

Sirve la carpeta con cualquier servidor estático, por ejemplo:

```powershell
# PowerShell
python -m http.server 8099
```

Luego abre http://localhost:8099

## Estructura

| Archivo | Descripción |
|---------|-------------|
| `index.html` | Juego completo (canvas 2D, lógica, UI) en un solo archivo |
| `sw.js` | Service worker (offline + actualización network-first) |
| `manifest.json` | Manifiesto PWA |
| `icon-192.png`, `icon-512.png` | Íconos de la app |
| `peerjs.min.js` | Librería P2P (co-op, en pausa) |
