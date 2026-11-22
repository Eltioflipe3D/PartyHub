package com.partyhub.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.partyhub.Screen

private data class GameEntry(
    val emoji: String,
    val title: String,
    val description: String,
    val screen: Screen,
)

// Para añadir un juego nuevo: crea su pantalla, añade un valor a Screen y una entrada aquí.
private val games = listOf(
    GameEntry(
        "🕵️",
        "El Impostor",
        "Todos conocen la palabra menos uno. Descubrid quién es el impostor.",
        Screen.IMPOSTOR,
    ),
    GameEntry(
        "🤳",
        "Adivina en la frente",
        "Ponte el móvil en la frente y adivina la palabra con las pistas de los demás. Inclina para acertar o pasar.",
        Screen.HEADS_UP,
    ),
    GameEntry(
        "🍻",
        "Yo nunca",
        "Frases para descubrir los secretos del grupo. Con categorías suaves, de fiesta y atrevidas.",
        Screen.NEVER,
    ),
)

@Composable
fun HomeScreen(onOpen: (Screen) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    "🎉 PartyHub",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "Juegos para grupos con un solo móvil. Sin anuncios, sin cuentas, sin internet.",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        items(games) { game ->
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onOpen(game.screen) },
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(game.emoji, style = MaterialTheme.typography.displaySmall)
                    Spacer(Modifier.width(16.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            game.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(game.description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
