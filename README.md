# Impostor

Juego del impostor para Android, de código abierto: sin anuncios, sin suscripciones,
sin cuentas y **sin permisos** (ni siquiera internet). Todo ocurre en el móvil.

## Cómo se juega

1. Añade los jugadores (mínimo 3), elige cuántos impostores y las categorías.
2. Se pasa el móvil: cada jugador toca "Ver mi palabra", la lee y oculta la pantalla.
   Todos ven la misma palabra, menos el impostor.
3. Empieza un jugador al azar. Se dan pistas por turnos sin decir la palabra.
4. Votáis, y se revela quién era el impostor y cuál era la palabra.

## Compilar

**Opción A – Android Studio:** abre esta carpeta (File → Open), espera a que sincronice
y pulsa Run, o usa Build → Build APK(s).

**Opción B – GitHub Actions (sin instalar nada):** sube la carpeta a un repositorio de
GitHub. En la pestaña Actions, el workflow "Build APK" genera `app-debug.apk` como
artefacto descargable. Instálalo en el móvil permitiendo "orígenes desconocidos".

**Opción C – terminal:** con JDK 17, Android SDK y Gradle 8.11+:

```
gradle assembleDebug
```

El APK queda en `app/build/outputs/apk/debug/`.

## Personalizar

- Palabras y categorías: `app/src/main/java/com/impostor/game/Words.kt`
- Pantallas: `Screens.kt`
- Lógica del juego: `GameViewModel.kt`

Requiere Android 8.0 (API 26) o superior. Licencia MIT.
