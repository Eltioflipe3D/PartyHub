package com.partyhub.headsup

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlin.math.abs

// Eje Z del acelerómetro: sale de la pantalla.
//   móvil vertical (en la frente)        -> z ≈ 0
//   pantalla mirando al suelo (inclinar abajo)  -> z ≈ -9.8  => acertada
//   pantalla mirando al techo (inclinar arriba) -> z ≈ +9.8  => pasar
//nota de jaime: habria q arreglar un pelin esto pq funciona bastante mal ngl
private const val NEUTRAL = 4f   // |z| por debajo de esto = posición "vertical", rearma el sensor
private const val TRIGGER = 7f   // |z| por encima de esto = inclinación detectada
private const val COOLDOWN_MS = 700L

/**
 * Escucha el acelerómetro mientras [enabled] sea true y llama a [onTilt]
 * con `true` si se inclina hacia abajo (acertada) o `false` hacia arriba (pasar).
 * Tras cada gesto hay que volver a la posición vertical para que cuente el siguiente.
 */
@Composable
fun TiltEffect(enabled: Boolean, onTilt: (down: Boolean) -> Unit) {
    val context = LocalContext.current
    val currentOnTilt by rememberUpdatedState(onTilt)

    DisposableEffect(enabled) {
        if (!enabled) {
            return@DisposableEffect onDispose { }
        }

        val manager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var armed = false
        var initialized = false
        var smoothed = 0f
        var lastTrigger = 0L

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val z = event.values[2]
                if (!initialized) {
                    smoothed = z
                    initialized = true
                } else {
                    smoothed = smoothed * 0.6f + z * 0.4f
                }

                // Hasta que el móvil no pase por la posición vertical no se arma
                // (así no salta al empezar si lo tenías boca arriba en la mesa).
                if (!armed) {
                    if (abs(smoothed) < NEUTRAL) armed = true
                    return
                }

                val now = SystemClock.elapsedRealtime()
                if (now - lastTrigger < COOLDOWN_MS) return

                if (smoothed < -TRIGGER) {
                    armed = false
                    lastTrigger = now
                    currentOnTilt(true)
                } else if (smoothed > TRIGGER) {
                    armed = false
                    lastTrigger = now
                    currentOnTilt(false)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (sensor != null) {
            manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME)
        }
        onDispose { manager.unregisterListener(listener) }
    }
}
