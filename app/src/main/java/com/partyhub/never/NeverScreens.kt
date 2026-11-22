package com.partyhub.never

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.partyhub.ui.BigButton
import com.partyhub.ui.BigOutlinedButton
import com.partyhub.ui.CategoryPicker
import com.partyhub.ui.ScreenHeader
import com.partyhub.ui.SectionTitle

@Composable
fun NeverGame(onExit: () -> Unit, vm: NeverViewModel = viewModel()) {
    BackHandler {
        if (vm.playing) vm.stop() else onExit()
    }

    if (vm.playing) NeverPlay(vm) else NeverSetup(vm, onExit)
}

// ───────────────────────── Ajustes ─────────────────────────

@Composable
private fun NeverSetup(vm: NeverViewModel, onExit: () -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ScreenHeader(
                title = "🍻 Yo nunca",
                subtitle = "Sale una frase y quien lo haya hecho alguna vez lo reconoce. " +
                    "¡Descubre los secretos del grupo!",
                onBack = onExit,
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionTitle("Categorías")
                CategoryPicker(
                    all = NeverStatements.categories.keys,
                    selected = vm.selectedCategories,
                    onToggle = vm::toggleCategory,
                )
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Modo beber 🍻", style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (vm.drinkMode) {
                            "Quien lo haya hecho, bebe"
                        } else {
                            "Quien lo haya hecho, baja un dedo"
                        },
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Switch(
                    checked = vm.drinkMode,
                    onCheckedChange = { vm.drinkMode = it },
                )
            }
        }

        item { BigButton("Empezar", onClick = vm::start) }
    }
}

// ───────────────────────── Frases ─────────────────────────

@Composable
private fun NeverPlay(vm: NeverViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().safeDrawingPadding().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Frase ${vm.count}", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth().clickable { vm.next() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "Yo nunca he…",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    vm.current,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            if (vm.drinkMode) "🍻 Quien lo haya hecho, bebe" else "☝️ Quien lo haya hecho, baja un dedo",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(32.dp))
        BigButton("Siguiente", onClick = vm::next)
        Spacer(Modifier.height(12.dp))
        BigOutlinedButton("Terminar", onClick = vm::stop)
    }
}
