package com.yeidckon.practica05_sharedpreferences.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)

    companion object {
        const val KEY_USERNAME = "key_username"
        const val KEY_NOTIFICATIONS = "key_notifications"
        const val KEY_DARK_THEME = "key_dark_theme"
    }

    fun saveSettings(username: String, notifications: Boolean, darkTheme: Boolean) {
        sharedPreferences.edit()
            .putString(KEY_USERNAME, username)
            .putBoolean(KEY_NOTIFICATIONS, notifications)
            .putBoolean(KEY_DARK_THEME, darkTheme)
            .commit() // síncrono: seguro ante cierre forzado
    }

    fun getUsername(): String =
        sharedPreferences.getString(KEY_USERNAME, "") ?: ""

    fun getNotifications(): Boolean =
        sharedPreferences.getBoolean(KEY_NOTIFICATIONS, false)

    fun getDarkTheme(): Boolean =
        sharedPreferences.getBoolean(KEY_DARK_THEME, false)

    fun clearPreferences() {
        sharedPreferences.edit().clear().commit()
    }
}