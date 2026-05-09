package com.example.ankib

import android.content.Context
import android.content.SharedPreferences

class Store(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("AnkiBStore", Context.MODE_PRIVATE)

    fun loadSrs(base: String): Int {
        return prefs.getInt("box_$base", 0)
    }

    fun saveSrs(base: String, box: Int) {
        prefs.edit().putInt("box_$base", box).apply()
    }

    fun loadConfig(): Config {
        return Config(
            sortReverse = prefs.getBoolean("config_sort_reverse", false),
            volume = prefs.getInt("config_volume", 50),
            pause = prefs.getInt("config_pause", 2),
            speed = prefs.getFloat("config_speed", 1.0f)
        )
    }

    fun saveConfig(config: Config) {
        prefs.edit()
            .putBoolean("config_sort_reverse", config.sortReverse)
            .putInt("config_volume", config.volume)
            .putInt("config_pause", config.pause)
            .putFloat("config_speed", config.speed)
            .apply()
    }
}

data class Config(
    var sortReverse: Boolean = false,
    var volume: Int = 50,
    var pause: Int = 2, // seconds
    var speed: Float = 1.0f
)
