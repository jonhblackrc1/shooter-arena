# SHOOTER ARENA

Top-down alien shooter — PWA jugable en navegador y móvil.

- **5 niveles × 10 fases**, con un jefe final en la fase 10 de cada nivel
- Disparo automático al enemigo más cercano si disparas sin apuntar; todas las armas con alcance global
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

## APK Android

La carpeta `android/` contiene un contenedor WebView mínimo que incluye el juego dentro de la APK (funciona sin internet, en horizontal y a pantalla completa). Para compilarla:

```bash
KS_PASS=<tu-clave> ./android/build.sh
```

La clave de firma (`android/keystore.jks`) no se sube al repositorio: guarda tu copia, porque es necesaria para publicar actualizaciones de la misma app.
