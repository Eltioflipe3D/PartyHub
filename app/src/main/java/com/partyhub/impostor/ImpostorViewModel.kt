package com.partyhub.impostor

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.partyhub.data.Words

enum class ImpostorPhase { SETUP, REVEAL, PLAY, RESULT }

class ImpostorViewModel(app: Application) : AndroidViewModel(app) {

    companion object {
        const val MIN_PLAYERS = 3
        const val MAX_PLAYERS = 20
        private const val SEP = "\u001F"
    }

    private val prefs = app.getSharedPreferences("impostor_prefs", Context.MODE_PRIVATE)

    // ---- Ajustes (se guardan en el móvil) ----
    val players = mutableStateListOf<String>()
    var impostorCount by mutableStateOf(1)
    var selectedCategories by mutableStateOf(Words.categories.keys.toSet())
    var impostorSeesCategory by mutableStateOf(false)

    // ---- Estado de la partida ----
    var phase by mutableStateOf(ImpostorPhase.SETUP)
        private set
    var word by mutableStateOf("")
        private set
    var category by mutableStateOf("")
        private set
    var impostors by mutableStateOf(setOf<Int>())
        private set
    var startingPlayer by mutableStateOf(0)
        private set
    var revealIndex by mutableStateOf(0)
        private set
    var startedAt by mutableStateOf(0L)
        private set

    private val usedWords = mutableSetOf<String>()

    init {
        val saved = prefs.getString("players", null)
        if (saved != null) players.addAll(saved.split(SEP)) else repeat(4) { players.add("") }
        while (players.size < MIN_PLAYERS) players.add("")

        impostorCount = prefs.getInt("impostors", 1).coerceIn(1, maxImpostors(players.size))
        impostorSeesCategory = prefs.getBoolean("hint", false)
        prefs.getStringSet("categories", null)
            ?.filter { it in Words.categories }
            ?.toSet()
            ?.takeIf { it.isNotEmpty() }
            ?.let { selectedCategories = it }
    }

    fun maxImpostors(playerCount: Int) = ((playerCount - 1) / 2).coerceAtLeast(1)

    fun displayName(index: Int): String =
        players.getOrNull(index)?.trim().orEmpty().ifEmpty { "Jugador ${index + 1}" }

    // ---- Ajustes ----
    fun addPlayer() {
        if (players.size < MAX_PLAYERS) players.add("")
    }

    fun removePlayer(index: Int) {
        if (players.size > MIN_PLAYERS) {
            players.removeAt(index)
            impostorCount = impostorCount.coerceAtMost(maxImpostors(players.size))
        }
    }

    fun changeImpostors(delta: Int) {
        impostorCount = (impostorCount + delta).coerceIn(1, maxImpostors(players.size))
    }

    fun toggleCategory(name: String) {
        selectedCategories = when {
            name !in selectedCategories -> selectedCategories + name
            selectedCategories.size > 1 -> selectedCategories - name
            else -> selectedCategories // siempre al menos una
        }
    }

    // ---- Flujo de la partida ----
    fun startRound() {
        val n = players.size
        if (n < MIN_PLAYERS || selectedCategories.isEmpty()) return
        impostorCount = impostorCount.coerceIn(1, maxImpostors(n))
        save()

        val pool = Words.categories
            .filterKeys { it in selectedCategories }
            .flatMap { (cat, list) -> list.map { cat to it } }
        var candidates = pool.filter { it.second !in usedWords }
        if (candidates.isEmpty()) {
            usedWords.clear()
            candidates = pool
        }
        val (cat, w) = candidates.random()
        usedWords.add(w)

        category = cat
        word = w
        impostors = (0 until n).shuffled().take(impostorCount).toSet()
        startingPlayer = (0 until n).random()
        revealIndex = 0
        phase = ImpostorPhase.REVEAL
    }

    fun nextReveal() {
        if (revealIndex + 1 >= players.size) {
            startedAt = System.currentTimeMillis()
            phase = ImpostorPhase.PLAY
        } else {
            revealIndex++
        }
    }

    fun showResult() {
        phase = ImpostorPhase.RESULT
    }

    fun backToSetup() {
        phase = ImpostorPhase.SETUP
    }

    private fun save() {
        prefs.edit()
            .putString("players", players.joinToString(SEP))
            .putInt("impostors", impostorCount)
            .putStringSet("categories", selectedCategories.toSet())
            .putBoolean("hint", impostorSeesCategory)
            .apply()
    }
}
