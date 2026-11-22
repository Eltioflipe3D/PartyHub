package com.partyhub

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.partyhub.headsup.HeadsUpGame
import com.partyhub.impostor.ImpostorGame
import com.partyhub.never.NeverGame
import com.partyhub.ui.HomeScreen

enum class Screen { HOME, IMPOSTOR, HEADS_UP, NEVER }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PartyHubTheme {
                App()
            }
        }
    }
}

@Composable
fun PartyHubTheme(content: @Composable () -> Unit) {
    val dark = isSystemInDarkTheme()
    val context = LocalContext.current
    val colors = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        dark -> darkColorScheme()
        else -> lightColorScheme()
    }
    MaterialTheme(colorScheme = colors, content = content)
}

@Composable
fun App() {
    var screen by rememberSaveable { mutableStateOf(Screen.HOME) }
    val goHome: () -> Unit = { screen = Screen.HOME }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        when (screen) {
            Screen.HOME -> HomeScreen(onOpen = { screen = it })
            Screen.IMPOSTOR -> ImpostorGame(onExit = goHome)
            Screen.HEADS_UP -> HeadsUpGame(onExit = goHome)
            Screen.NEVER -> NeverGame(onExit = goHome)
        }
    }
}
