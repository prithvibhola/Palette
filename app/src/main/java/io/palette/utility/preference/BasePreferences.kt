package io.palette.utility.preference

import android.content.SharedPreferences
import io.palette.utility.extentions.get
import kotlin.reflect.KClass

@Suppress("IMPLICIT_CAST_TO_ANY")
open class BasePreferences(private val preferences: SharedPreferences) {

    fun <T : Any> put(clazz: KClass<T>, key: String, value: T) {
        when (clazz) {
            Boolean::class -> preferences.edit().putBoolean(key, value as Boolean).apply()
            Int::class -> preferences.edit().putInt(key, value as Int).apply()
            Long::class -> preferences.edit().putLong(key, value as Long).apply()
            Float::class -> preferences.edit().putFloat(key, value as Float).apply()
            String::class -> preferences.edit().putString(key, value as String?).apply()
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun <T : Any> get(clazz: KClass<T>, key: String, defValue: T): T {
        return when (clazz) {
            Long::class -> preferences.getLong(key, defValue as Long)
            Int::class -> preferences.getInt(key, defValue as Int)
            String::class -> preferences.getString(key, defValue as? String)
            Boolean::class -> preferences.getBoolean(key, defValue as Boolean)
            Float::class -> preferences.getFloat(key, defValue as Float)
            else -> throw UnsupportedOperationException("Not yet implemented")
        } as T
    }

    operator fun get(key: String, value: String = ""): String = preferences[key, value]
    operator fun get(key: String, value: Set<String> = setOf()): Set<String> = preferences[key, value]
    operator fun get(key: String, value: Int = 0): Int = preferences[key, value]
    fun get(key: String, value: Long): Long = preferences[key, value]
    fun get(key: String, value: Float): Float = preferences[key, value]
    fun get(key: String, value: Boolean): Boolean = preferences[key, value]
}