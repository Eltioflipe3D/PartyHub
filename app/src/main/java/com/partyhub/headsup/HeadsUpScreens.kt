package com.partyhub.headsup

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.ToneGenerator
import android.view.HapticFeedbackConstants
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.partyhub.data.Words
import com.partyhub.ui.BigButton
import com.partyhub.ui.BigOutlinedButton
import com.partyhub.ui.CategoryPicker
import com.partyhub.ui.ScreenHeader
import com.partyhub.ui.SectionTitle

private val CorrectGreen = Color(0xFF2E7D32)
private val PassOrange = Color(0xFFEF6C00)

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun HeadsUpGame(onExit: () -> Unit, vm: HeadsUpViewModel = viewModel()) {
    val context = LocalContext.current
    val view = LocalView.current
    val activity = remember(context) { context.findActivity() }
    val landscape = vm.phase == HeadsUpPhase.COUNTDOWN || vm.phase == HeadsUpPhase.PLAY

    BackHandler {
        if (vm.phase == HeadsUpPhase.SETUP) onExit() else vm.exit()
    }

    // La ronda se juega en horizontal (móvil en la frente); el resto en vertical.
    LaunchedEffect(landscape) {
        activity?.requestedOrientation = if (landscape) {
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
    DisposableEffect(Unit) {
        onDispose { activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT }
    }

    // Pantalla encendida durante la cuenta atrás y la ronda
    DisposableEffect(landscape) {
        view.keepScreenOn = landscape
        onDispose { view.keepScreenOn = false }
    }

    // Inclinar hacia abajo = acertada, hacia arriba = pasar
    TiltEffect(enabled = vm.phase == HeadsUpPhase.PLAY) { down -> vm.mark(down) }

    when (vm.phase) {
        HeadsUpPhase.SETUP -> HeadsUpSetup(vm, onExit)
        HeadsUpPhase.READY -> HeadsUpReady(vm)
        HeadsUpPhase.COUNTDOWN -> HeadsUpCountdown(vm)
        HeadsUpPhase.PLAY -> HeadsUpPlay(vm)
        HeadsUpPhase.END -> HeadsUpEnd(vm)
    }
}

// ───────────────────────── Ajustes ─────────────────────────

@Composable
private fun HeadsUpSetup(vm: HeadsUpViewModel, onExit: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ScreenHeader(
                title = "🤳 Adivina en la frente",
                subtitle = "Un jugador se pone el móvil en la frente sin ver la palabra y los demás " +
                    "se la describen (sin decirla). Inclina el móvil hacia abajo si aciertas o " +
                    "hacia arriba para pasar.",
                onBack = onExit,
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("Duración de la ronda")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    HeadsUpViewModel.DURATIONS.forEach { seconds ->
                        FilterChip(
                            selected = vm.roundSeconds == seconds,
                            onClick = { vm.roundSeconds = seconds },
                            label = { Text("${seconds}s") },
                        )
                    }
                }
            }
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("Categorías")
                CategoryPicker(
                    all = Words.categories.keys,
                    selected = vm.selectedCategories,
                    onToggle = vm::toggleCategory,
                )
            }
        }

        item { BigButton("Jugar", onClick = vm::prepare) }
    }
}

// ───────────────────────── Preparación ─────────────────────────

@Composable
private fun HeadsUpReady(vm: HeadsUpViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "🤳 Pasa el móvil a quien juega",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "Cuando pulse «Listo» el móvil pasará a horizontal y habrá una cuenta atrás de 3 " +
                "segundos. Colócatelo en la frente con la pantalla hacia los demás.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "⬇️ Inclina hacia abajo: ¡acertada!\n⬆️ Inclina hacia arriba: pasar",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.height(32.dp))
        BigButton("¡Listo!", onClick = vm::begin)
        Spacer(Modifier.height(12.dp))
        BigOutlinedButton("Cancelar", onClick = vm::exit)
    }
}

// ───────────────────────── Cuenta atrás ─────────────────────────

@Composable
private fun HeadsUpCountdown(vm: HeadsUpViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Ponte el móvil en la frente 🤳",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Text(
            "${vm.countdown}",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
        )
    }
}

// ───────────────────────── Ronda ─────────────────────────

@Composable
private fun HeadsUpPlay(vm: HeadsUpViewModel) {
    val view = LocalView.current

    // Como quien juega no ve la pantalla, avisamos con vibración y un pitido corto
    // (ninguna de las dos cosas necesita permisos).
    val tone = remember {
        runCatching { ToneGenerator(AudioManager.STREAM_MUSIC, 80) }.getOrNull()
    }
    DisposableEffect(Unit) {
        onDispose { tone?.release() }
    }
    LaunchedEffect(vm.flash) {
        when (vm.flash) {
            Flash.CORRECT -> {
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                tone?.startTone(ToneGenerator.TONE_PROP_ACK, 250)
            }
            Flash.PASS -> {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                tone?.startTone(ToneGenerator.TONE_PROP_NACK, 250)
            }
            null -> Unit
        }
    }

    val background = when (vm.flash) {
        Flash.CORRECT -> CorrectGreen
        Flash.PASS -> PassOrange
        null -> MaterialTheme.colorScheme.primaryContainer
    }
    val foreground = if (vm.flash == null) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        Color.White
    }
    val seconds = ((vm.timeLeftMs + 999) / 1000).toInt()

    Box(modifier = Modifier.fillMaxSize().background(background)) {
        Column(modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    "✓ ${vm.correct.size}",
                    style = MaterialTheme.typography.titleLarge,
                    color = foreground,
                )
                Text(
                    "⏱ ${seconds}s",
                    style = MaterialTheme.typography.titleLarge,
                    color = foreground,
                )
            }

            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                if (vm.flash == null) {
                    Text(
                        vm.word,
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = foreground,
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            if (vm.flash == Flash.CORRECT) "¡CORRECTO!" else "PASO",
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold,
                            color = foreground,
                        )
                        Text(
                            vm.word,
                            style = MaterialTheme.typography.titleLarge,
                            color = foreground,
                        )
                    }
                }
            }

            // Por si el sensor falla o alguien quiere marcar a mano
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                OutlinedButton(onClick = { vm.mark(false) }) { Text("Pasar") }
                Button(onClick = { vm.mark(true) }) { Text("✓ Acertada") }
            }
        }
    }
}

// ───────────────────────── Resultado ─────────────────────────

@Composable
private fun HeadsUpEnd(vm: HeadsUpViewModel) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "⏱️ ¡Se acabó el tiempo!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    "${vm.correct.size}",
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    if (vm.correct.size == 1) "acierto" else "aciertos",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        if (vm.correct.isNotEmpty()) {
            item { SectionTitle("✓ Acertadas") }
            items(vm.correct) { Text(it, style = MaterialTheme.typography.bodyLarge) }
        }
        if (vm.passed.isNotEmpty()) {
            item { SectionTitle("✗ Pasadas") }
            items(vm.passed) { Text(it, style = MaterialTheme.typography.bodyLarge) }
        }

        item { Spacer(Modifier.height(16.dp)) }
        item { BigButton("Siguiente jugador", onClick = vm::prepare) }
        item { BigOutlinedButton("Cambiar ajustes", onClick = vm::exit) }
    }
}
