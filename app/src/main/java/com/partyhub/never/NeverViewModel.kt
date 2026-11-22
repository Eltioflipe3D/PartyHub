package com.partyhub.never

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class NeverViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = app.getSharedPreferences("never_prefs", Context.MODE_PRIVATE)

    // ---- Ajustes ----
    var selectedCategories by mutableStateOf(NeverStatements.categories.keys.toSet())
    var drinkMode by mutableStateOf(false)

    // ---- Estado ----
    var playing by mutableStateOf(false)
        private set
    var current by mutableStateOf("")
        private set
    var count by mutableIntStateOf(0)
        private set

    private val deck = ArrayDeque<String>()
    private val seen = mutableSetOf<String>()

    init {
        drinkMode = prefs.getBoolean("drink", false)
        prefs.getStringSet("categories", null)
            ?.filter { it in NeverStatements.categories }
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

    fun start() {
        save()
        deck.clear()
        count = 0
        next()
        playing = true
    }

    fun next() {
        if (deck.isEmpty()) refill()
        current = deck.removeFirst()
        seen.add(current)
        count++
    }

    fun stop() {
        playing = false
    }

    /** Baraja las frases de las categorías elegidas que aún no han salido. */
    private fun refill() {
        val pool = NeverStatements.categories
            .filterKeys { it in selectedCategories }
            .values
            .flatten()
            .distinct()
        var fresh = pool.filter { it !in seen }
        if (fresh.isEmpty()) {
            seen.clear()
            fresh = pool
        }
        deck.addAll(fresh.shuffled())
    }

    private fun save() {
        prefs.edit()
            .putBoolean("drink", drinkMode)
            .putStringSet("categories", selectedCategories.toSet())
            .apply()
    }
}
