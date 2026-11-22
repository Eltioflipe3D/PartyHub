# PartyHub

Juegos de fiesta para Android con un solo móvil, de código abierto: sin anuncios,
sin suscripciones, sin cuentas y **sin permisos** (ni internet). Todo ocurre en el móvil.

Totalmente vibecoded btw

## Juegos

- **🕵️ El Impostor** – todos reciben la misma palabra menos el impostor. Se pasa el móvil
  para ver el rol, se dan pistas por turnos y se vota quién no la sabía.
- **🤳 Adivina en la frente** – un jugador se pone el móvil (en horizontal) en la frente y
  los demás describen la palabra. Inclinar hacia abajo = acertada, hacia arriba = pasar.
  Hay vibración y un pitido corto para avisar, y botones por si falla el sensor.
- **🍻 Yo nunca** – frases por categorías (suave, fiesta, atrevido +18, viajes, estudios y
  trabajo). Modo beber o modo dedos.

## Compilar

**Android Studio:** abre esta carpeta (File → Open), espera a que sincronice y pulsa Run
(o Build → Build APK(s)). Requiere Android 8.0 (API 26) o superior.

**Terminal:** con JDK 17, Android SDK y Gradle 8.11+: `gradle assembleDebug`.
El APK queda en `app/build/outputs/apk/debug/`.

**GitHub Actions:** el workflow `.github/workflows/build.yml` compila el APK en cada push
y lo deja como artefacto descargable.

## Estructura

```
app/src/main/java/com/partyhub/
├── MainActivity.kt        navegación entre pantallas y tema
├── data/Words.kt          palabras compartidas (Impostor y Frente)
├── ui/                    menú principal y componentes comunes
├── impostor/              El Impostor
├── headsup/               Adivina en la frente (incluye TiltEffect.kt, el sensor)
└── never/                 Yo nunca (incluye NeverStatements.kt, las frases)
```

### Añadir un juego nuevo

1. Crea un paquete con su ViewModel y una pantalla `MiJuegoGame(onExit: () -> Unit)`.
2. Añade un valor al enum `Screen` en `MainActivity.kt` y su `when`.
3. Añade una entrada a la lista `games` en `ui/HomeScreen.kt`.

### Personalizar

- Palabras: `data/Words.kt`. Frases de Yo nunca: `never/NeverStatements.kt`.
- Sensibilidad de la inclinación: constantes `NEUTRAL` y `TRIGGER` en `headsup/TiltEffect.kt`.

Licencia MIT.
