package com.partyhub.impostor

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.partyhub.data.Words
import com.partyhub.ui.BigButton
import com.partyhub.ui.BigOutlinedButton
import com.partyhub.ui.CategoryPicker
import com.partyhub.ui.ScreenHeader
import com.partyhub.ui.SectionTitle
import kotlinx.coroutines.delay

@Composable
fun ImpostorGame(onExit: () -> Unit, vm: ImpostorViewModel = viewModel()) {
    // Atrás durante la partida vuelve a los ajustes; desde los ajustes, al menú.
    BackHandler {
        if (vm.phase == ImpostorPhase.SETUP) onExit() else vm.backToSetup()
    }

    when (vm.phase) {
        ImpostorPhase.SETUP -> ImpostorSetup(vm, onExit)
        ImpostorPhase.REVEAL -> ImpostorReveal(vm)
        ImpostorPhase.PLAY -> ImpostorPlay(vm)
        ImpostorPhase.RESULT -> ImpostorResult(vm)
    }
}

// ───────────────────────── Ajustes ─────────────────────────

@Composable
private fun ImpostorSetup(vm: ImpostorViewModel, onExit: () -> Unit) {
    val n = vm.players.size
    val maxImp = vm.maxImpostors(n)
    val impostors = vm.impostorCount.coerceIn(1, maxImp)

    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            ScreenHeader(
                title = "🕵️ El Impostor",
                subtitle = "Todos reciben la misma palabra, menos el impostor. " +
                    "Pasaos el móvil, dad pistas y descubrid quién no la sabe.",
                onBack = onExit,
            )
        }

        item { SectionTitle("Jugadores ($n)") }
        itemsIndexed(vm.players) { index, name ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { vm.players[index] = it },
                    label = { Text("Jugador ${index + 1}") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    modifier = Modifier.weight(1f),
                )
                TextButton(
                    onClick = { vm.removePlayer(index) },
                    enabled = n > ImpostorViewModel.MIN_PLAYERS,
                ) { Text("✕") }
            }
        }
        item {
            OutlinedButton(
                onClick = vm::addPlayer,
                enabled = n < ImpostorViewModel.MAX_PLAYERS,
                modifier = Modifier.fillMaxWidth(),
            ) { Text("+ Añadir jugador") }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(Modifier.weight(1f)) { SectionTitle("Impostores") }
                OutlinedButton(
                    onClick = { vm.changeImpostors(-1) },
                    enabled = impostors > 1,
                ) { Text("−") }
                Text("$impostors", style = MaterialTheme.typography.titleLarge)
                OutlinedButton(
                    onClick = { vm.changeImpostors(1) },
                    enabled = impostors < maxImp,
                ) { Text("+") }
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

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Pista para el impostor", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "El impostor ve la categoría de la palabra",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Switch(
                    checked = vm.impostorSeesCategory,
                    onCheckedChange = { vm.impostorSeesCategory = it },
                )
            }
        }

        item { BigButton("Empezar partida", onClick = vm::startRound) }
    }
}

// ───────────────────────── Reparto de roles ─────────────────────────

@Composable
private fun ImpostorReveal(vm: ImpostorViewModel) {
    val i = vm.revealIndex
    var shown by rememberSaveable(i) { mutableStateOf(false) }
    val isImpostor = i in vm.impostors
    val isLast = i == vm.players.size - 1

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Jugador ${i + 1} de ${vm.players.size}",
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            vm.displayName(i),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))

        if (!shown) {
            Text(
                "Asegúrate de que nadie más está mirando la pantalla.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(24.dp))
            BigButton("Ver mi palabra", onClick = { shown = true })
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (isImpostor) {
                        MaterialTheme.colorScheme.errorContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                ),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (isImpostor) {
                        Text(
                            "🕵️ Eres el IMPOSTOR",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            if (vm.impostorSeesCategory) {
                                "Pista: la categoría es ${vm.category}"
                            } else {
                                "No conoces la palabra. ¡Disimula!"
                            },
                            textAlign = TextAlign.Center,
                        )
                    } else {
                        Text("Categoría: ${vm.category}", style = MaterialTheme.typography.labelLarge)
                        Text("La palabra es", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            vm.word,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
            Spacer(Modifier.height(24.dp))
            BigButton(
                if (isLast) "Ocultar y empezar" else "Ocultar y pasar al siguiente",
                onClick = vm::nextReveal,
            )
        }
    }
}

// ───────────────────────── Partida ─────────────────────────

@Composable
private fun ImpostorPlay(vm: ImpostorViewModel) {
    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            now = System.currentTimeMillis()
        }
    }

    // Mantener la pantalla encendida mientras se juega
    val view = LocalView.current
    DisposableEffect(Unit) {
        view.keepScreenOn = true
        onDispose { view.keepScreenOn = false }
    }

    val elapsed = ((now - vm.startedAt) / 1000).coerceAtLeast(0)
    val clock = "${(elapsed / 60).toString().padStart(2, '0')}:${(elapsed % 60).toString().padStart(2, '0')}"

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Empieza", style = MaterialTheme.typography.titleMedium)
        Text(
            vm.displayName(vm.startingPlayer),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(32.dp))
        Text(clock, style = MaterialTheme.typography.displayLarge)
        Spacer(Modifier.height(32.dp))
        Text(
            "Id hablando por turnos y dando pistas sobre la palabra sin decirla. " +
                "Cuando lo tengáis claro, votad quién creéis que es el impostor.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(32.dp))
        BigButton("Revelar al impostor", onClick = vm::showResult)
    }
}

// ───────────────────────── Resultado ─────────────────────────

@Composable
private fun ImpostorResult(vm: ImpostorViewModel) {
    val names = vm.impostors.sorted().map { vm.displayName(it) }

    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            if (names.size == 1) "El impostor era" else "Los impostores eran",
            style = MaterialTheme.typography.titleMedium,
        )
        names.forEach {
            Text(
                "🕵️ $it",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(32.dp))
        Text("La palabra era", style = MaterialTheme.typography.titleMedium)
        Text(
            vm.word,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text("Categoría: ${vm.category}", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(40.dp))
        BigButton("Otra ronda", onClick = vm::startRound)
        Spacer(Modifier.height(12.dp))
        BigOutlinedButton("Cambiar jugadores o ajustes", onClick = vm::backToSetup)
    }
}
