package com.partyhub.headsup

import android.app.Application
import android.content.Context
import android.os.SystemClock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.partyhub.data.Words
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class HeadsUpPhase { SETUP, READY, COUNTDOWN, PLAY, END }

enum class Flash { CORRECT, PASS }

class HeadsUpViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        val DURATIONS = listOf(30, 60, 90, 120)
        private const val FLASH_MS = 700L
        private const val COUNTDOWN_SECONDS = 3
    }

    private val prefs = app.getSharedPreferences("headsup_prefs", Context.MODE_PRIVATE)

    // ---- Ajustes ----
    var selectedCategories by mutableStateOf(Words.categories.keys.toSet())
    var roundSeconds by mutableIntStateOf(60)

    // ---- Estado de la ronda ----
    var phase by mutableStateOf(HeadsUpPhase.SETUP)
        private set
    var word by mutableStateOf("")
        private set
    var countdown by mutableIntStateOf(COUNTDOWN_SECONDS)
        private set
    var timeLeftMs by mutableLongStateOf(0L)
        private set
    var flash by mutableStateOf<Flash?>(null)
        private set
    val correct = mutableStateListOf<String>()
    val passed = mutableStateListOf<String>()

    private val deck = ArrayDeque<String>()
    private val seen = mutableSetOf<String>()
    private var job: Job? = null
    private var flashJob: Job? = null

    init {
        roundSeconds = prefs.getInt("seconds", 60).takeIf { it in DURATIONS } ?: 60
        prefs.getStringSet("categories", null)
            ?.filter { it in Words.categories }
            ?.toSet()
            ?.takeIf { it.isNotEmpty() }
            ?.let { selectedCategories = it }
    }

    fun toggleCategory(name: String) {
        selectedCategories = when {
            name !in selectedCategories -> selectedCategories + name
            selectedCategories.size > 1 -> selectedCategories - name
            else -> selectedCategories // siempre al menos una
        }
    }

    private fun pool(): List<String> =
        Words.categories
            .filterKeys { it in selectedCategories }
            .values
            .flatten()
            .distinct()

    /** Prepara una ronda nueva (mazo barajado) y pasa a la pantalla "pasa el móvil". */
    fun prepare() {
        job?.cancel()
        flashJob?.cancel()
        flash = null
        save()

        correct.clear()
        passed.clear()

        // No repetir palabras de rondas anteriores hasta quedarnos sin frescas
        val all = pool()
        var fresh = all.filter { it !in seen }
        if (fresh.size < 20) {
            seen.clear()
            fresh = all
        }
        deck.clear()
        deck.addAll(fresh.shuffled())
        phase = HeadsUpPhase.READY
    }

    /** El jugador pulsa "¡Listo!": cuenta atrás y empieza la ronda. */
    fun begin() {
        if (phase != HeadsUpPhase.READY) return
        phase = HeadsUpPhase.COUNTDOWN
        timeLeftMs = roundSeconds * 1000L
        job?.cancel()
        job = viewModelScope.launch {
            for (i in COUNTDOWN_SECONDS downTo 1) {
                countdown = i
                delay(1000)
            }
            phase = HeadsUpPhase.PLAY
            nextWord()

            val end = SystemClock.elapsedRealtime() + roundSeconds * 1000L
            while (true) {
                val left = end - SystemClock.elapsedRealtime()
                if (left <= 0) break
                timeLeftMs = left
                delay(100)
            }
            timeLeftMs = 0L
            finishRound()
        }
    }

    /** Acierto (true) o pasar (false). Lo llama el sensor o los botones. */
    fun mark(isCorrect: Boolean) {
        if (phase != HeadsUpPhase.PLAY || flash != null) return
        if (isCorrect) correct.add(word) else passed.add(word)
        flash = if (isCorrect) Flash.CORRECT else Flash.PASS
        flashJob = viewModelScope.launch {
            delay(FLASH_MS)
            flash = null
            if (phase == HeadsUpPhase.PLAY) nextWord()
        }
    }

    private fun nextWord() {
        if (deck.isEmpty()) {
            seen.clear()
            deck.addAll(pool().shuffled())
        }
        word = deck.removeFirst()
        seen.add(word)
    }

    private fun finishRound() {
        flashJob?.cancel()
        flash = null
        phase = HeadsUpPhase.END
    }

    /** Salir a los ajustes (también cancela cualquier ronda en curso). */
    fun exit() {
        job?.cancel()
        flashJob?.cancel()
        flash = null
        phase = HeadsUpPhase.SETUP
    }

    private fun save() {
        prefs.edit()
            .putInt("seconds", roundSeconds)
            .putStringSet("categories", selectedCategories.toSet())
            .apply()
    }
}
