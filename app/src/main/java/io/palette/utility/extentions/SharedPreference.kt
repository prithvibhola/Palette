package io.palette.utility.extentions

import android.content.SharedPreferences

inline fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}

operator fun SharedPreferences.get(key: String, defaultValue: String = "") = getString(key, defaultValue)
fun SharedPreferences.Editor.put(key: String, value: String) = putString(key, value)
operator fun SharedPreferences.set(key: String, value: String) = edit { put(key, value) }

operator fun SharedPreferences.get(key: String, defaultValue: Set<String> = setOf()) = getStringSet(key, defaultValue)
fun SharedPreferences.Editor.put(key: String, value: Set<String>) = putStringSet(key, value)
operator fun SharedPreferences.set(key: String, value: Set<String>) = edit { put(key, value) }

operator fun SharedPreferences.get(key: String, defaultValue: Int = 0) = getInt(key, defaultValue)
fun SharedPreferences.Editor.put(key: String, value: Int) = putInt(key, value)
operator fun SharedPreferences.set(key: String, value: Int) = edit { put(key, value) }

operator fun SharedPreferences.get(key: String, defaultValue: Long = 0) = getLong(key, defaultValue)
fun SharedPreferences.Editor.put(key: String, value: Long) = putLong(key, value)
operator fun SharedPreferences.set(key: String, value: Long) = edit { put(key, value) }

operator fun SharedPreferences.get(key: String, defaultValue: Float = 0F) = getFloat(key, defaultValue)
fun SharedPreferences.Editor.put(key: String, value: Float) = putFloat(key, value)
operator fun SharedPreferences.set(key: String, value: Float) = edit { put(key, value) }

operator fun SharedPreferences.get(key: String, defaultValue: Boolean = false) = getBoolean(key, defaultValue)
fun SharedPreferences.Editor.put(key: String, value: Boolean) = putBoolean(key, value)
operator fun SharedPreferences.set(key: String, value: Boolean) = edit { put(key, value) }